package com.hangugi.tma2.crawler.domino.sender;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hangugi.tma2.crawler.domino.envelope.Envelope;
import com.hangugi.tma2.crawler.domino.envelope.HttpEnvelope;

public class HttpMimeHandler extends MimeHandler {
	private static final Logger logger = LoggerFactory.getLogger(HttpMimeHandler.class);

	@Override
	public boolean send(String documentUnid, Envelope envelope, Object targetAuthObject) throws Exception {
		String url = (String) targetAuthObject;
		HttpEnvelope httpEnvelope = (HttpEnvelope) envelope;
		File emlFile = new File(envelope.getEmlPath());
		boolean isSuccess = false;

		if (!emlFile.exists()) {
			logger.info("FILE:" + emlFile.getAbsolutePath() + " " + "DS:eml not found .");
			return false;
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		MultipartEntity multipartEntity = new MultipartEntity();

		try {
			multipartEntity.addPart("receivedTime", new StringBody(httpEnvelope.getEdate()));
			multipartEntity.addPart("sender", new StringBody(httpEnvelope.getEnvFrom()));
			multipartEntity.addPart("senderIp", new StringBody(httpEnvelope.getEsip()));
			multipartEntity.addPart("dataType", new StringBody("email"));

			for (String receiver : envelope.getRcptList()) {
				multipartEntity.addPart("receiver", new StringBody(receiver));
			}

			multipartEntity.addPart("data", new FileBody(emlFile));
			httpPost.setEntity(multipartEntity);
			httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				isSuccess = true;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (ClientProtocolException e) {
			logger.error(e.toString(), e);
			throw e;
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("UNID:" + documentUnid + " ");

			if (isSuccess) {
				stringBuffer.append("DS:send success");
			} else {
				stringBuffer.append("RE:" + httpResponse.getStatusLine().toString().trim() + " " + "DS:send failed");
			}

			logger.info(stringBuffer.toString());
		}

		return isSuccess;
	}

	@Override
	public void recordUnidHistory(String documentUnid, String historyDirString) throws IOException {
		return;
	}
}