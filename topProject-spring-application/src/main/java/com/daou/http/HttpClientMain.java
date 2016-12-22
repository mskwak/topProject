package com.daou.http;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;

public class HttpClientMain {
	public static void main(String[] args) {
		String url = "http://172.21.29.152";
		CookieStore cookieStore = HttpClientUtil.getInitCookieStore(url);

/*		List<Cookie> cookieList = cookieStore.getCookies();

		if(cookieList.isEmpty()) {
			System.out.println("1: cookie is null");
		} else {
			for(Cookie cookie: cookieList) {
				System.out.println("1: " + cookie.getName() + ": " + cookie.getValue());
			}
		}*/

		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(url + "/login").
				addParameter("", "{\"username\":\"mailadm\",\"password\":\"tims\",\"returnUrl\":\"\"}").build();


		HttpClientUtil.login(httpUriRequest, cookieStore);

		List<Cookie> cookieList = cookieStore.getCookies();

		if(cookieList.isEmpty()) {
			System.out.println("1: cookie is null");
		} else {
			for(Cookie cookie: cookieList) {
				System.out.println("1: " + cookie.getName() + ": " + cookie.getValue());
			}
		}

/*
		//NameValuePair nameValuePair = new BasicNameValuePair(name, value)
		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(url).
				addParameter("username", "mskw").
				addParameter("password", "ffff").addParameter("returnUrl", "").build();
		CloseableHttpResponse closeableHttpResponse2 = null;

		try {
			closeableHttpResponse2 = closeableHttpClient.execute(httpUriRequest);

			HttpEntity httpEntity = closeableHttpResponse2.getEntity();
			EntityUtils.consumeQuietly(httpEntity);

			System.out.println(closeableHttpResponse2.getStatusLine());

			List<Cookie> cookieList = basicCookieStore.getCookies();

			if(cookieList.isEmpty()) {
				System.out.println("2: cookie is null");
			} else {
				for(Cookie cookie: cookieList) {
					System.out.println("2: " + cookie.getName() + ": " + cookie.getValue());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(closeableHttpResponse2 != null) {
					closeableHttpResponse2.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}
}
