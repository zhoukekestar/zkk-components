package com.share.mod.pay.ums._test;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.OrderEntity;
import com.share.mod.pay.ums.UMSConfig;
import com.share.mod.pay.ums.inter.ICreate;

public class CreateImpl implements ICreate {

	Logger logger = Logger.getLogger(CreateImpl.class);
	@Override
	public String createMerOrderId(HttpServletRequest request) {
		return UUID.randomUUID().toString().substring(0, 15);
	}

	@Override
	public void log(String transId, String merOrderId, String orderDate) {
		logger.info("transid:" + transId + " merOrderid:" + merOrderId + " orderDate:" + orderDate);
	}

	@Override
	public void recordOrder(OrderEntity request, OrderEntity response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePayException(HttpServletRequest request,
			HttpServletResponse response, PayException e)
			throws ServletException, IOException {
		logger.error("UMS create ERROR");
		logger.error(e);
	}

	@Override
	public void handleNullException(HttpServletRequest request,
			HttpServletResponse response, InvalidParameterException e)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new InvalidParameterException("金额(" + UMSConfig.FORM_MONEY + ")或描述(" + UMSConfig.FORM_DESC + ")不能为空");
	}

	@Override
	public void handleMoneyException(HttpServletRequest request,
			HttpServletResponse response, NumberFormatException e)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new InvalidParameterException("金额必须为整数");
	}


}
