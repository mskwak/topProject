package com.daou.tma2.crawler.domino.envelope;

import java.util.Date;

import javax.mail.Flags;

public class ImapEnvelope extends Envelope {
	private Date internalDate;
	private String mailBoxName;
	private final Flags flags = new Flags(Flags.Flag.SEEN);

	public Date getInternalDate() {
		return this.internalDate;
	}

	public void setInternalDate(Date internalDate) {
		this.internalDate = internalDate;
	}

	public String getMailBoxName() {
		return this.mailBoxName;
	}

	public void setMailBoxName(String mailBoxName) {
		this.mailBoxName = mailBoxName;
	}

	public Flags getFlags() {
		return this.flags;
	}

	public ImapEnvelope() {
		super();
	}
}
