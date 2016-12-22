package com.daou.tma2.crawler.domino.envelope;

public class HttpEnvelope extends Envelope {
	private String esip;
	private String edate;
	private String envFrom;

	public String getEsip() {
		return this.esip;
	}

	public void setEsip(String esip) {
		this.esip = esip;
	}

	public String getEdate() {
		return this.edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getEnvFrom() {
		return this.envFrom;
	}

	public void setEnvFrom(String envFrom) {
		this.envFrom = envFrom;
	}

	public HttpEnvelope() {
		super();
	}
}
