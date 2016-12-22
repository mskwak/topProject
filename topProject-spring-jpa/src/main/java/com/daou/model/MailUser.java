package com.daou.model;

public class MailUser {

	private Long mailUserSeq;
	private String mailUid;
	private Long mailDomainSeq;
	private Long mailGroupSeq;
	private String mailPassword;
	private String mailHost;
	private String messageStore;
	private String accountExpireDate;
	private String accountStatus;
	private String userType;
	private Long mailServices;
	private String mailAddQuota;
	private String quotaOverlookRatio;
	private String quotaWarningMode;
	private String quotaWarningRatio;
	private String quotaViolationAction;
	private String mailMaxSendSize;
	private String inboxExpireDays;
	private String sentExpireDays;
	private String trashExpireDays;
	private String spamExpireDays;
	private String userExpireDays;
	private String deliveryNotiType;
	private String deliveryNotiMode;
	private String forwardingMode;
	private String hiddenForwardingMode;
	private String autoReplyMode;
	private String autoReplyInclude;
	private String autoReplyStartTime;
	private String autoReplyEndTime;
	private String autoReplySubject;
	private String autoReplyText;
	private String deliveryMode;
	private String accountDormantDate;
	private String accountDisabledDate;
	private String accountStatusAdmin;
	private String sendAllowMode;

	public Long getMailUserSeq() {
		return mailUserSeq;
	}

	public void setMailUserSeq(Long mailUserSeq) {
		this.mailUserSeq = mailUserSeq;
	}

	public String getMailUid() {
		return mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid == null ? null : mailUid.trim();
	}

	public Long getMailDomainSeq() {
		return mailDomainSeq;
	}

	public void setMailDomainSeq(Long mailDomainSeq) {
		this.mailDomainSeq = mailDomainSeq;
	}

	public Long getMailGroupSeq() {
		return mailGroupSeq;
	}

	public void setMailGroupSeq(Long mailGroupSeq) {
		this.mailGroupSeq = mailGroupSeq;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword == null ? null : mailPassword.trim();
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost == null ? null : mailHost.trim();
	}

	public String getMessageStore() {
		return messageStore;
	}

	public void setMessageStore(String messageStore) {
		this.messageStore = messageStore == null ? null : messageStore.trim();
	}

	public String getAccountExpireDate() {
		return accountExpireDate;
	}

	public void setAccountExpireDate(String accountExpireDate) {
		this.accountExpireDate = accountExpireDate == null ? null
				: accountExpireDate.trim();
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus == null ? null : accountStatus
				.trim();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType == null ? null : userType.trim();
	}

	public Long getMailServices() {
		return mailServices;
	}

	public void setMailServices(Long mailServices) {
		this.mailServices = mailServices;
	}

	public String getMailAddQuota() {
		return mailAddQuota;
	}

	public void setMailAddQuota(String mailAddQuota) {
		this.mailAddQuota = mailAddQuota == null ? null : mailAddQuota.trim();
	}

	public String getQuotaOverlookRatio() {
		return quotaOverlookRatio;
	}

	public void setQuotaOverlookRatio(String quotaOverlookRatio) {
		this.quotaOverlookRatio = quotaOverlookRatio == null ? null
				: quotaOverlookRatio.trim();
	}

	public String getQuotaWarningMode() {
		return quotaWarningMode;
	}

	public void setQuotaWarningMode(String quotaWarningMode) {
		this.quotaWarningMode = quotaWarningMode == null ? null
				: quotaWarningMode.trim();
	}

	public String getQuotaWarningRatio() {
		return quotaWarningRatio;
	}

	public void setQuotaWarningRatio(String quotaWarningRatio) {
		this.quotaWarningRatio = quotaWarningRatio == null ? null
				: quotaWarningRatio.trim();
	}

	public String getQuotaViolationAction() {
		return quotaViolationAction;
	}

	public void setQuotaViolationAction(String quotaViolationAction) {
		this.quotaViolationAction = quotaViolationAction == null ? null
				: quotaViolationAction.trim();
	}

	public String getMailMaxSendSize() {
		return mailMaxSendSize;
	}

	public void setMailMaxSendSize(String mailMaxSendSize) {
		this.mailMaxSendSize = mailMaxSendSize == null ? null : mailMaxSendSize
				.trim();
	}

	public String getInboxExpireDays() {
		return inboxExpireDays;
	}

	public void setInboxExpireDays(String inboxExpireDays) {
		this.inboxExpireDays = inboxExpireDays == null ? null : inboxExpireDays
				.trim();
	}

	public String getSentExpireDays() {
		return sentExpireDays;
	}

	public void setSentExpireDays(String sentExpireDays) {
		this.sentExpireDays = sentExpireDays == null ? null : sentExpireDays
				.trim();
	}

	public String getTrashExpireDays() {
		return trashExpireDays;
	}

	public void setTrashExpireDays(String trashExpireDays) {
		this.trashExpireDays = trashExpireDays == null ? null : trashExpireDays
				.trim();
	}

	public String getSpamExpireDays() {
		return spamExpireDays;
	}

	public void setSpamExpireDays(String spamExpireDays) {
		this.spamExpireDays = spamExpireDays == null ? null : spamExpireDays
				.trim();
	}

	public String getUserExpireDays() {
		return userExpireDays;
	}

	public void setUserExpireDays(String userExpireDays) {
		this.userExpireDays = userExpireDays == null ? null : userExpireDays
				.trim();
	}

	public String getDeliveryNotiType() {
		return deliveryNotiType;
	}

	public void setDeliveryNotiType(String deliveryNotiType) {
		this.deliveryNotiType = deliveryNotiType == null ? null
				: deliveryNotiType.trim();
	}

	public String getDeliveryNotiMode() {
		return deliveryNotiMode;
	}

	public void setDeliveryNotiMode(String deliveryNotiMode) {
		this.deliveryNotiMode = deliveryNotiMode == null ? null
				: deliveryNotiMode.trim();
	}

	public String getForwardingMode() {
		return forwardingMode;
	}

	public void setForwardingMode(String forwardingMode) {
		this.forwardingMode = forwardingMode == null ? null : forwardingMode
				.trim();
	}

	public String getHiddenForwardingMode() {
		return hiddenForwardingMode;
	}

	public void setHiddenForwardingMode(String hiddenForwardingMode) {
		this.hiddenForwardingMode = hiddenForwardingMode == null ? null
				: hiddenForwardingMode.trim();
	}

	public String getAutoReplyMode() {
		return autoReplyMode;
	}

	public void setAutoReplyMode(String autoReplyMode) {
		this.autoReplyMode = autoReplyMode == null ? null : autoReplyMode
				.trim();
	}

	public String getAutoReplyInclude() {
		return autoReplyInclude;
	}

	public void setAutoReplyInclude(String autoReplyInclude) {
		this.autoReplyInclude = autoReplyInclude == null ? null
				: autoReplyInclude.trim();
	}

	public String getAutoReplyStartTime() {
		return autoReplyStartTime;
	}

	public void setAutoReplyStartTime(String autoReplyStartTime) {
		this.autoReplyStartTime = autoReplyStartTime == null ? null
				: autoReplyStartTime.trim();
	}

	public String getAutoReplyEndTime() {
		return autoReplyEndTime;
	}

	public void setAutoReplyEndTime(String autoReplyEndTime) {
		this.autoReplyEndTime = autoReplyEndTime == null ? null
				: autoReplyEndTime.trim();
	}

	public String getAutoReplySubject() {
		return autoReplySubject;
	}

	public void setAutoReplySubject(String autoReplySubject) {
		this.autoReplySubject = autoReplySubject == null ? null
				: autoReplySubject.trim();
	}

	public String getAutoReplyText() {
		return autoReplyText;
	}

	public void setAutoReplyText(String autoReplyText) {
		this.autoReplyText = autoReplyText == null ? null : autoReplyText
				.trim();
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode == null ? null : deliveryMode.trim();
	}

	public String getAccountDormantDate() {
		return accountDormantDate;
	}

	public void setAccountDormantDate(String accountDormantDate) {
		this.accountDormantDate = accountDormantDate == null ? null
				: accountDormantDate.trim();
	}

	public String getAccountDisabledDate() {
		return accountDisabledDate;
	}

	public void setAccountDisabledDate(String accountDisabledDate) {
		this.accountDisabledDate = accountDisabledDate == null ? null
				: accountDisabledDate.trim();
	}

	public String getAccountStatusAdmin() {
		return accountStatusAdmin;
	}

	public void setAccountStatusAdmin(String accountStatusAdmin) {
		this.accountStatusAdmin = accountStatusAdmin == null ? null
				: accountStatusAdmin.trim();
	}

	public String getSendAllowMode() {
		return sendAllowMode;
	}

	public void setSendAllowMode(String sendAllowMode) {
		this.sendAllowMode = sendAllowMode == null ? null : sendAllowMode
				.trim();
	}
}