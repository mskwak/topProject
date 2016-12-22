package com.daou;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO 클래스 리팩토링

//@Component
//@Scope("prototype")
public class FilterdComponent {
	private final static Map<String, String> ENCODING_MAP;
	static {
		Map<String, String> map = new HashMap<>();
		map.put("7bit", "1");
		map.put("8bit", "2");
		map.put("binary", "3");
		map.put("quoted-printable", "4");
		map.put("base64", "5");
		map.put("x-uuencode", "8");
		ENCODING_MAP = Collections.unmodifiableMap(map);
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String address = "172.21.29.188";
	private int port = 20001;
	private long attachMaxSize = 100 * 1024 * 1024;

	private Socket socket = null;
	private InputStream is = null;
	private OutputStream os = null;
	private BufferedWriter writer = null;

	//TODO 추후 localconfig에서 가져오는 방식으로 변경
	/*
	 *
		private void init(String address, int port, long attachMaxSize) {
			this.address = address;
			this.port = port;
			this.attachMaxSize = attachMaxSize;
		}
	*/
	public String extractTxt(InputStream is, long fileSize, String fileName, String encoding) throws Exception {
		// TODO vaildation 로직 함수 밖으로 추출하기
		encoding = encoding.toLowerCase();
		encoding = FilterdComponent.ENCODING_MAP.get(encoding);

		if (encoding == null) {
			return null;
		}

		String command = "convert " + fileSize + " " + encoding + " " + fileName;

		this.connect();
		try {

			this.sendCommand(command);

			this.sendData(is);

			byte[] receiveBytes = this.makeByte();

			String dataStr = this.receiveDataStr(receiveBytes);
			this.log.debug(dataStr);
			this.checkSuccess();

			return dataStr;
		} finally {
			this.release();
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setAttachMaxSize(long attachMaxSize) {
		this.attachMaxSize = attachMaxSize;
	}

	private void checkSuccess() throws Exception {
		String reply = this.receiveReply();
		if (!StringUtils.startsWith(reply, "2")) {
			String[] errorCodes = reply.split(" ");
			if (reply.length() < 1024) {
				String errorCode = errorCodes[0];
				String synapErrorCode = errorCodes.length > 1 ? errorCodes[1] : "00000";
				throw new IOException();
			} else {
				throw new IOException();
			}
		}
	}

	private void sendData(InputStream input) throws IOException {
		if (input instanceof InputStream) {
			long sendSize = IOUtils.copyLarge(input, this.os);
			this.writeLog("sendData [" + sendSize + "]");
			IOUtils.closeQuietly(input);
		}
		this.os.flush();
	}

	boolean validConnection() throws Exception {
		boolean connected = false;
		try {
			connected = this.connect();
		} finally {
			this.release();
		}
		return connected;
	}

	boolean connect() throws Exception {
		int try_cnt = 0;
		while (++try_cnt <= 3) {
			try {
				this.socket = new Socket(this.address, this.port);

				this.socket.setKeepAlive(true);
				this.socket.setSoTimeout(60 * 1000);

				break;
			} catch (Exception e) {
				Thread.sleep(3 * 1000);
				if (try_cnt > 2) {
					throw new IOException();
				}
			}
		}

		this.is = this.socket.getInputStream();

		if (!StringUtils.startsWith(this.receiveReply(), "100")) {
			throw new IOException();
		}

		this.os = this.socket.getOutputStream();
		this.writer = new BufferedWriter(new OutputStreamWriter(this.os, "UTF8"));
		return this.socket instanceof Socket ? this.socket.isConnected() : false;
	}

	void release() throws Exception {
		try {
			this.sendCommand("quit");
			this.receiveReply();
		} finally {
			IOUtils.closeQuietly(this.is);
			IOUtils.closeQuietly(this.writer);
			IOUtils.closeQuietly(this.os);
			if (this.socket != null && !this.socket.isClosed()) {
				this.socket.close();
			}
		}
	}

	private void sendCommand(String command) throws IOException {
		this.writeLog("command : " + command);
		if (!(this.writer instanceof BufferedWriter)) {
			return;
		}
		this.writer.write(command + "\r\n");
		this.writer.flush();
	}

	private String receiveReply() throws IOException {
		ByteArrayOutputStream memoryByteStream = new ByteArrayOutputStream();
		for (int byteValue = -1, i = 0; (byteValue = this.is.read()) != '\n' && i < 1024; i++) {
			memoryByteStream.write(byteValue);
		}
		String replyStr = StringUtils.trim(new String(memoryByteStream.toByteArray(), "UTF-8"));
		memoryByteStream.close();
		this.writeLog(replyStr);
		return replyStr;
	}

	private byte[] makeByte() throws Exception {
		byte[] bytes = null;

		String replyStr = this.receiveReply();
		String[] reply = StringUtils.split(replyStr, " ");

		if (StringUtils.startsWith(replyStr, "2")) {
			int size = Integer.parseInt(reply[1]);

			if (size > (this.attachMaxSize / 10)) {
				throw new IOException();
			}
			bytes = new byte[size];
		} else if (StringUtils.startsWith(replyStr, "50")) { // status 500 or 501 - 첨부에 대해서 처리하지 않는다.
			String errorCode = reply[0];
			String synapErrorCode = reply.length > 1 ? reply[1] : "00000";
			throw new IOException();
		}
		return bytes;
	}

	private String receiveDataStr(byte[] bytes) throws Exception {
		if (!(bytes instanceof byte[]) || bytes.length == 0) {
			return "";
		}

		int currentTotal = 0;
		int orgTotal = bytes.length;
		int bufferSize = 8 * 1024;//8k ==> 테스트 결과 최대 64k까지 사용되나  대부분의 경우 8k가 가장 효율이 좋음
		int readBytes = bufferSize;
		byte[] buffer = new byte[bufferSize];
		long start = System.currentTimeMillis();
		while (currentTotal < orgTotal) {
			if (currentTotal + bufferSize > orgTotal) {
				// 테스트 결과 버퍼 사이즈를 줄여나가는 것보다 큰 버퍼를 재 생성하는 것이 효율이 좋음
				buffer = new byte[orgTotal - currentTotal];
			}
			readBytes = this.readStream(bytes, buffer, currentTotal);
			currentTotal += readBytes;

			this.checkSize(currentTotal * 10);//추출한 데이터 사이즈가 max 사이즈의 10%를 넘을경우
		}
		this.writeLog("total read size [" + currentTotal + "/" + orgTotal + "] : " + (System.currentTimeMillis() - start) + " ms");

		new KatakanaFilter();
		String returnStr = KatakanaFilter.halfToFullWidthKatakana(new String(bytes, "UTF-8"));

		this.writeLog(returnStr);
		return returnStr;
	}

	/**
	 * BufferedInputStream을 사용하면 한번에 가져올 수 있는 버퍼의 양이 일정하지 못하기때문에 filterd 데몬과의 정확한 통신을 할 수 없다.<br/>
	 * 때문에 매번 버퍼의 사이즈가 변경될 수 있는 코드로 구현해야한다.<br/>
	 * 절대로 buffered 계열을 사용하면 안된다.!! <br/>
	 *
	 * @param bytes
	 * @param buffer
	 * @param currentTotal
	 * @return
	 * @throws IOException
	 */
	private int readStream(byte[] bytes, byte[] buffer, int currentTotal) throws IOException {
		this.writeLog(new String(bytes, "UTF-8"));
		int idx = -1;
		try {
			idx = this.is.read(buffer);
			System.arraycopy(buffer, 0, bytes, currentTotal, idx);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return idx;
	}

	private void writeLog(String msg) {
		this.log.debug(msg);
	}

	private void checkSize(long size) throws Exception {
		if (size > this.attachMaxSize) {
			throw new IOException();
		}
	}

	public static boolean isSupportedEncoding(String encoding) {
		boolean result = false;
		if (StringUtils.isNotEmpty(encoding) && ENCODING_MAP.containsKey(encoding.toLowerCase())) {
			return true;
		}
		return result;
	}

	private static class KatakanaFilter {

		// Zero-base table for mapping half-width katakana to full-width
		private final static char FWKatakana[] = { '\u3002', '\u300C', '\u300D', '\u3001', '\u30FB', // U+FF61 - U+FF65
		'\u30F2', '\u30A1', '\u30A3', '\u30A5', '\u30A7', // U+FF66 - U+FF6A
		'\u30A9', '\u30E3', '\u30E5', '\u30E7', '\u30C3', // U+FF6B - U+FF6F
		'\u30FC', '\u30A2', '\u30A4', '\u30A6', '\u30A8', // U+FF70 - U+FF74
		'\u30AA', '\u30AB', '\u30AD', '\u30AF', '\u30B1', // U+FF75 - U+FF79
		'\u30B3', '\u30B5', '\u30B7', '\u30B9', '\u30BB', // U+FF7A - U+FF7E
		'\u30BD', '\u30BF', '\u30C1', '\u30C4', '\u30C6', // U+FF7F - U+FF83
		'\u30C8', '\u30CA', '\u30CB', '\u30CC', '\u30CD', // U+FF84 - U+FF88
		'\u30CE', '\u30CF', '\u30D2', '\u30D5', '\u30D8', // U+FF89 - U+FF8D
		'\u30DB', '\u30DE', '\u30DF', '\u30E0', '\u30E1', // U+FF8E - U+FF92
		'\u30E2', '\u30E4', '\u30E6', '\u30E8', '\u30E9', // U+FF93 - U+FF97
		'\u30EA', '\u30EB', '\u30EC', '\u30ED', '\u30EF', // U+FF98 - U+FF9C
		'\u30F3', '\u309B', '\u309C' // U+FF9D - U+FF9F
		};

		// Class method for converting half-width katakana to full-width
		private static String halfToFullWidthKatakana(String string_input) {
			int ixIn = 0;
			int ixOut = 0;
			int bufferLength = string_input.length();
			char[] input = string_input.toCharArray();
			char[] output = new char[bufferLength + 1];

			while (ixIn < bufferLength) {
				if (input[ixIn] >= '\uFF61' && input[ixIn] <= '\uFF9F') {
					if (ixIn + 1 >= bufferLength) {
						output[ixOut++] = FWKatakana[input[ixIn++] - '\uFF61'];
					} else {
						if (input[ixIn + 1] == '\uFF9E' || input[ixIn + 1] == '\u3099' || input[ixIn + 1] == '\u309B') {
							if (input[ixIn] == '\uFF73') {
								output[ixOut++] = '\u30F4';
								ixIn += 2;
							} else if (input[ixIn] >= '\uFF76' && input[ixIn] <= '\uFF84' || input[ixIn] >= '\uFF8A' && input[ixIn + 1] == '\uFF8E') {
								output[ixOut] = FWKatakana[input[ixIn] - '\uFF61'];
								output[ixOut++]++;
								ixIn += 2;
							} else {
								output[ixOut++] = FWKatakana[input[ixIn++] - '\uFF61'];
							}
						} else if (input[ixIn + 1] == '\uFF9F' || input[ixIn + 1] == '\u309A' || input[ixIn + 1] == '\u309C') {
							if (input[ixIn] >= '\uFF8A' && input[ixIn] <= '\uFF8E') {
								output[ixOut] = FWKatakana[input[ixIn] - '\uFF61'];
								output[ixOut++] += 2;
								ixIn += 2;
							} else {
								output[ixOut++] = FWKatakana[input[ixIn++] - '\uFF61'];
							}
						} else {
							output[ixOut++] = FWKatakana[input[ixIn++] - '\uFF61'];
						}
					}
				} else if (input[ixIn] == '　') {
					output[ixOut] = ' ';
					ixOut++;
					ixIn++;
				} else {
					output[ixOut++] = input[ixIn++];
				}
			}
			String output_string = new String(output);
			return output_string.substring(0, ixOut);
		}
	}
}

