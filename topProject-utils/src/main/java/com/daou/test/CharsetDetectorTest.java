package com.daou.test;


/*
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

//
//
public class CharsetDetectorTest {
	public static void main(String[] args) throws IOException {
		String charset = get("/department.utf8.csv");
		System.out.println(charset);
		System.out.println("-------------");
		Charset charr = Charset.forName(charset);
		System.out.println(charr.name());
		System.out.println(charr.name());
		System.out.println(charr.name());
		System.out.println(charr.name());
	}

	public static String get(String fileName) {
		return getCsvCharset(new File(fileName));
	}

	public static String getCsvCharset(File fileName) {
		long maxLength = 1024;
		byte[] buffer = new byte[(int) maxLength];
		long length = 0;
		InputStream inputStream = null;
		String charset = null;

		try {
			length = (fileName.length() > maxLength) ? maxLength : fileName.length();
			inputStream = new FileInputStream(fileName);
			inputStream.read(buffer, 0, (int) length);

			CharsetDetector charsetDetector = new CharsetDetector();
			charsetDetector.setText(buffer);
			CharsetMatch charsetMatch = charsetDetector.detect();
			charset = charsetMatch.getName();

		} catch (FileNotFoundException e) {
			//TODO
		} catch (IOException e) {
			//TODO
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}

		return charset;
	}
}
*/
