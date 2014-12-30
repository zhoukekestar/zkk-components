package com.share.mod.pay.ums.inter;

import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.OrderEntity;

public interface ICreate {
	/***
	 * 订单号，商户根据自己的规则生成， 最长32位
	 * @return 商户订单号
	 */
	public String createMerOrderId();
	
	/***
	 * 创建订单的时，与服务器的通讯信息记录
	 * @param request	发送请求的信息
	 * @param response	银联返回的信息
	 */
	public void recordOrder(OrderEntity request, OrderEntity response);
	
	/***
	 * 当需要查询订单是，需要的参数记录
	 * @param transId	银联订单号
	 * @param merOrderId	商户订单号
	 * @param orderDate	日期
	 */
	public void saveForQuery(String transId, String merOrderId, String orderDate);
	
	
	/***
	 * 创建订单的流程中出现的异常
	 * @param e 异常
	 */
	public void handlePayException(PayException e);
	
	/***
	 * 当金额或描述为空时
	 */
	public void handleNullException();
	
	/***
	 * 当金额不是整数，或金额无法转换成整数
	 */
	public void handleMoneyException();
}
