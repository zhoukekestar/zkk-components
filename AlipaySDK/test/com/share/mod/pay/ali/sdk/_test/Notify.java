package com.share.mod.pay.ali.sdk._test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.share.mod.pay.ali.sdk.inter.INotify;

public class Notify implements INotify {

	@Override
	public void fail(HttpServletRequest request,
			String out_trade_no, String trade_no) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void success(HttpServletRequest request, String out_trade_no, String trade_no)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tradeFinished(HttpServletRequest request, String out_trade_no, String trade_no)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tradeSuccess(HttpServletRequest request, String out_trade_no, String trade_no)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
