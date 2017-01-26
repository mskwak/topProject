package com.hangugi.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtil {
	private HttpClientUtil() {
		// no action
	}

	public static CookieStore getInitCookieStore(String url) {
		HttpGet httpGet = new HttpGet(url);

		BasicCookieStore basicCookieStore = new BasicCookieStore();
		CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).build();
		CloseableHttpResponse closeableHttpResponse = null;

		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
			IOUtils.closeQuietly(closeableHttpClient);
		}

		return basicCookieStore;
	}

	public static CookieStore login(HttpUriRequest httpUriRequest, CookieStore cookieStore) {

		CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		CloseableHttpResponse closeableHttpResponse = null;

		try {
			closeableHttpResponse = closeableHttpClient.execute(httpUriRequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
			IOUtils.closeQuietly(closeableHttpClient);
		}

		return cookieStore;
	}

}
