package com.share.mod.pay.ums.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.QueryEntity;

public interface IQuery {
	
	/***
	 * 根据返回的查询结果或请求，自定义返回客户端
	 * @param request 请求request
	 * @param response	返回response
	 * @param queryEntity	查询结果
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doService(HttpServletRequest request, HttpServletResponse response, QueryEntity queryEntity)	throws ServletException, IOException;
	
	
	/***
	 * 当查询过程出现异常
	 * @param e 异常
	 */
	public void handlePayException(PayException e);
	
	/***
	 * 当银联订单号、商户订单号或日期其中一个为空时
	 */
	public void handleNullException();
}
