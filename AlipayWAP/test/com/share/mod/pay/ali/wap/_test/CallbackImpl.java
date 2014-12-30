package com.share.mod.pay.ali.wap._test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.share.mod.pay.ali.wap.inter.ICallback;


public class CallbackImpl implements ICallback {

	private Logger logger = Logger.getLogger(CallbackImpl.class);
	
	@Override
	public void fail(HttpServletRequest request, HttpServletResponse response,
			String out_trade_no, String trade_no) throws ServletException, IOException {
		
		logger.debug("callback fail  out:" + out_trade_no + "  no:" + trade_no);
		response.getWriter().print("fail.jsp");
	}

	@Override
	public void success(HttpServletRequest request, HttpServletResponse response,
			String out_trade_no, String trade_no) throws ServletException, IOException {
		
		logger.debug("callback success  out:" + out_trade_no + "  no:" + trade_no);
		response.getWriter().print("success.jsp");
	}

	@Override
	public void tradeFinishedOrSuccess(HttpServletRequest request,
			HttpServletResponse response, String out_trade_no, String trade_no)
			throws ServletException, IOException {
		
		logger.debug("callback success  out:" + out_trade_no + "  no:" + trade_no);
		response.getWriter().print("??x.jsp");
	}

	@Override
	public void handleException(HttpServletRequest request,
			HttpServletResponse response, Exception e) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		logger.error(e);
	}

}
