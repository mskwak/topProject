package com.daou.tma2.crawler.domino.envelope;

import javax.mail.Session;

public class SmtpEnvelope extends Envelope {
	private String ehlo;
	private String esip;
	private String edate;
	private String envFrom;
	private Session session;

	public String getEhlo() {
		return this.ehlo;
	}

	public void setEhlo(String ehlo) {
		this.ehlo = ehlo;
	}

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

	public Session getSession() {
		return this.session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public SmtpEnvelope() {
		super();
	}

	@Override
	public String toString() {
		return "SmtpEnvelope [ehlo=" + this.ehlo + ", esip=" + this.esip + ", edate=" + this.edate + ", envFrom=" + this.envFrom + ", session=" + this.session + "]";
	}
}
