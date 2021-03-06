package com.share.mod.pay.ums._test;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.QueryEntity;
import com.share.mod.pay.ums.inter.IQuery;

public class QueryImpl implements IQuery {
	
	Logger logger = Logger.getLogger(QueryImpl.class);


	@Override
	public void doService(HttpServletRequest request, HttpServletResponse response, QueryEntity queryEntity)
			throws ServletException, IOException {
		
		//logger.debug(JSON.toJSONString(queryEntity));
		response.getWriter().write(JSON.toJSONString(queryEntity));
	}

	@Override
	public void handlePayException(HttpServletRequest request,
			HttpServletResponse response, PayException e)
			throws ServletException, IOException {
		logger.error(e);
		throw new InvalidParameterException("参数不能为空");
		
	}

	@Override
	public void handleNullException(HttpServletRequest request,
			HttpServletResponse response, InvalidParameterException e)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new InvalidParameterException("参数不能为空");
	}

}
