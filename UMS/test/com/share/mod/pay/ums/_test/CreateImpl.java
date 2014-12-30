package com.share.mod.pay.ums._test;

import java.security.InvalidParameterException;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.OrderEntity;
import com.share.mod.pay.ums.UMSConfig;
import com.share.mod.pay.ums.inter.ICreate;

public class CreateImpl implements ICreate {

	Logger logger = Logger.getLogger(CreateImpl.class);
	@Override
	public String createMerOrderId() {
		return UUID.randomUUID().toString().substring(0, 15);
	}

	@Override
	public void saveForQuery(String transId, String merOrderId, String orderDate) {
		logger.info("transid:" + transId + " merOrderid:" + merOrderId + " orderDate:" + orderDate);
	}

	@Override
	public void handlePayException(PayException e) {
		logger.error("UMS create ERROR");
		logger.error(e);
	}

	@Override
	public void handleNullException() {
		throw new InvalidParameterException("金额(" + UMSConfig.FORM_MONEY + ")或描述(" + UMSConfig.FORM_DESC + ")不能为空");
	}

	@Override
	public void handleMoneyException() {
		throw new InvalidParameterException("金额必须为整数");
	}

	@Override
	public void recordOrder(OrderEntity request, OrderEntity response) {
		// TODO Auto-generated method stub
		
	}


}
