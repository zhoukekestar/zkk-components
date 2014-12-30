package com.share.mod.pay.ums._test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.share.mod.pay.ums.inter.ICallback;

public class CallbackImpl implements ICallback {

	private Logger logger = Logger.getLogger(CallbackImpl.class);
	@Override
	public void doService(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.debug(JSON.toJSONString(request.getParameterMap()));
		response.getWriter().write("callback.jsp");
	}
}
