package com.share.mod.pay.ali.direct.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICreate {
	/***
	 *  创建商户订单号
	 * @return
	 */
	public String createOutTradeNo();
	
	/***
	 * 处理金额异常
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void handleNumberFormatException(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException;
	
	/***
	 * 处理异常
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e)  throws ServletException, IOException;
	
	/***
	 * 根据请求记录创建订单记录
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void save(HttpServletRequest request)  throws ServletException, IOException;
}
