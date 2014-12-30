package com.share.mod.pay.ali.wap._test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.share.mod.pay.ali.wap.inter.IInterrupt;


public class InterruptImpl implements IInterrupt {

	Logger logger = Logger.getLogger(InterruptImpl.class);
	@Override
	public void get(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		logger.debug("interrupt:" + request.getQueryString());
		response.getWriter().print("interrupt.jsp");
	}

}
