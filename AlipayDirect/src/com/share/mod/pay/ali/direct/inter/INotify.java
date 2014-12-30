package com.share.mod.pay.ali.direct.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface INotify {
	/***
	 * 验证失败执行的方法
	 * @param request
	 * @param response	
	 * @param out_trade_no 商户订单号
	 * @param trade_no	支付宝订单号
	 * @throws IOException
	 */
	public void fail(HttpServletRequest request, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	
	/***
	 * 验证成功执行的方法<br>
	 * 如果已经对tradeFinished或者tradeSuccess处理过，该函数会重复执行
	 * @param request
	 * @param response	
	 * @param out_trade_no 商户订单号
	 * @param trade_no	支付宝订单号
	 * @throws IOException
	 */
	public void success(HttpServletRequest request, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	/****
	 * 
	 * 判断该笔订单是否在商户网站中已经做过处理 <br>
	 * 	如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序 <br>
	 * 	如果有做过处理，不执行商户的业务程序<br>
	 * <h2>注意：</h2><br>
	 * 	该种交易状态只在两种情况下出现<br>
	 * 	1、开通了普通即时到账，买家付款成功后。<br>
	 * 	2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。<br>
	 * @param request
	 * @param response	
	 * @param out_trade_no 商户订单号
	 * @param trade_no	支付宝订单号
	 * @throws IOException
	 */
	public void tradeFinished(HttpServletRequest request, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	/****
	 * 
	 * 判断该笔订单是否在商户网站中已经做过处理 <br>
	 * 	如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序 <br>
	 * 	如果有做过处理，不执行商户的业务程序<br>
	 * <h2>注意：</h2><br>
	 * 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。<br>
	 * @param request
	 * @param response	
	 * @param out_trade_no 商户订单号
	 * @param trade_no	支付宝订单号
	 * @throws IOException
	 */
	public void tradeSuccess(HttpServletRequest request, String out_trade_no, String trade_no) throws ServletException, IOException;
	
	/***
	 * 处理异常
	 */
	public void handleException(Exception e);
}
