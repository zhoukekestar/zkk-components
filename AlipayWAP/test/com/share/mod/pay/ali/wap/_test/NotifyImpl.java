package com.share.mod.pay.ali.wap._test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.share.mod.pay.ali.wap.inter.INotify;


public class NotifyImpl implements INotify {

	private Logger logger = Logger.getLogger(NotifyImpl.class);

	@Override
	public void fail(HttpServletRequest request, String out_trade_no,
			String trade_no) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("notify fail out:" + out_trade_no + " no:" + trade_no);
	}

	@Override
	public void success(HttpServletRequest request, String out_trade_no,
			String trade_no) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("notify success out:" + out_trade_no + " no:" + trade_no);
	}

	@Override
	public void tradeFinished(HttpServletRequest request, String out_trade_no,
			String trade_no) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("notify finshed out:" + out_trade_no + " no:" + trade_no);
	}

	@Override
	public void tradeSuccess(HttpServletRequest request, String out_trade_no,
			String trade_no) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("notify ....?? out:" + out_trade_no + " no:" + trade_no);
	}

	@Override
	public void handleException(Exception e) {
		// TODO Auto-generated method stub
		logger.error(e);
		e.printStackTrace();
	}

}
