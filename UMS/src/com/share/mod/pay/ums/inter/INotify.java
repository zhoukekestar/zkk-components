package com.share.mod.pay.ums.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.NoticeEntity;

public interface INotify {
	
	/***
	 * 保存相关通知信息
	 * @param request 请求信息
	 * @param noticeEntity	包装后的notice
	 * @throws ServletException
	 * @throws IOException
	 */
	public void save(HttpServletRequest request, NoticeEntity noticeEntity)	throws ServletException, IOException;
	
	
	/***
	 * 通知处理过程中出现的异常
	 * @param e	异常
	 */
	public void handlePayException(PayException e);
	
}
