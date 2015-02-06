package com.share.mod.pay.ali.direct._test;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.share.mod.pay.ali.direct.inter.ICreate;


public class CreateImpl implements ICreate {

	Logger logger = Logger.getLogger(CreateImpl.class);
	@Override
	public String createOutTradeNo(HttpServletRequest request) {
		String string = "test-ali" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		//System.out.println(string);
		logger.debug("create: " + string);
		return string;
	}
	@Override
	public void handleNumberFormatException(HttpServletRequest request,
			HttpServletResponse response,NumberFormatException e) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print("{\"msg\":\"金额转换错误,单位为分\"}");
		
	}

	@Override
	public void handleException(HttpServletRequest request,
			HttpServletResponse response, Exception e) throws ServletException,
			IOException {
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print("{\"msg\":\"创建失败\",\"detail\":\""+e.getMessage()+"\"}");
	}

	@Override
	public void handleInvalidParameterException(HttpServletRequest request,
			HttpServletResponse response, InvalidParameterException e)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void log(HttpServletRequest request, Map<String, String> params,
			String returnHtml) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
