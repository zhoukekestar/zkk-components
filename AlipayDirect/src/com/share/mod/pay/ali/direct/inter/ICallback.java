package com.share.mod.pay.ali.direct.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICallback {
	/***
	 * 验证失败时
	 */
	public void fail(HttpServletRequest request, HttpServletResponse response, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	/***
	 * 验证通过时
	 */
	public void success(HttpServletRequest request, HttpServletResponse response, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	/***
	 * 验证通过时，注意，调用完后会继续调用success函数
	 * out_trade_no 商户订单号
	 * trade_no 支付宝订单号
	 */
	public void tradeFinishedOrSuccess(HttpServletRequest request, HttpServletResponse response, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	/****
	 * 异常处理
	 */
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException;
}
