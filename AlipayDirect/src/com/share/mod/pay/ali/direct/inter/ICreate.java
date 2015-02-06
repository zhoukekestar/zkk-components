package com.share.mod.pay.ali.direct.inter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICreate {
	/***
	 *  创建商户订单号
	 */
	public String createOutTradeNo(HttpServletRequest request);
	
	/***
	 * 处理金额异常，无法转换成整数值
	 */
	public void handleNumberFormatException(HttpServletRequest request, HttpServletResponse response, NumberFormatException e)  throws ServletException, IOException;
	
	/***
	 * 处理参数异常，当body、subject、show_url其中一个为null时
	 */
	public void handleInvalidParameterException(HttpServletRequest request, HttpServletResponse response, InvalidParameterException e)  throws ServletException, IOException;
	/***
	 * 处理异常，当创建订单有未知异常时
	 */
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e)  throws ServletException, IOException;
	
	/***
	 * 根据请求记录创建订单记录
	 * @param params 请求的参数
	 * @param returnHtml 返回客户端的html
	 */
	public void log(HttpServletRequest request, Map<String, String> params, String returnHtml)  throws ServletException, IOException;
}
