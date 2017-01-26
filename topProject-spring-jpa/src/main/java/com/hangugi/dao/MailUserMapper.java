package com.hangugi.dao;

import com.hangugi.model.MailUser;

public interface MailUserMapper {

	int deleteByPrimaryKey(Long mailUserSeq);

	int insert(MailUser record);

	int insertSelective(MailUser record);

	MailUser selectByPrimaryKey(Long mailUserSeq);

	int updateByPrimaryKeySelective(MailUser record);

	int updateByPrimaryKey(MailUser record);
}