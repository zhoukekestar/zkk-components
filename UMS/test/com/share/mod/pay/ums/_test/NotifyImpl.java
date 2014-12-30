package com.share.mod.pay.ums._test;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.NoticeEntity;
import com.share.mod.pay.ums.inter.INotify;

public class NotifyImpl implements INotify {

	private Logger logger = Logger.getLogger(NotifyImpl.class);

	@Override
	public void handlePayException(PayException e) {
		logger.error(e);
		throw new InvalidParameterException("系统错误");
	}

	@Override
	public void save(HttpServletRequest request, NoticeEntity noticeEntity) throws ServletException, IOException {
		logger.debug(JSON.toJSONString(noticeEntity));
	}
}
