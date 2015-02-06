package com.share.mod.pay.ali.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.share.mod.pad.ali.sdk.android.SignUtils;
import com.share.mod.pay.ali.sdk.inter.ICreate;
import com.share.mod.pay.ali.sdk.inter.INotify;
import com.share.mod.pay.ali.sdk.util.AlipayNotify;

/***
 * 在properties文件中需要包含以下参数
 * <ul>
 * <li>partner 合作身份者ID，以2088开头由16位纯数字组成的字符串</li>
 * <li>private_key 商户的私钥</li>
 * <li>ali_public_key 支付宝的公钥，无需修改该值</li>
 * <li>email 卖家支付宝账号</li>
 * <li>url_notify 支付宝支付通知接口</li>
 * <li>notify iNotify的实现类包全名</li>
 * <li>create iCreate的实现类包全名</li>
 * </ul>
 * 
 * <ul>
 * <li>以下参数可选：</li>
 * <li>log_path 默认为“./”</li>
 * </ul>
 * 
 */
public class AlipaySDKHandler {
	
	private static String[] paramList = {
		"partner","private_key","ali_public_key","email", "url_notify", //4
		"notify","create"}; // 6
	
	private static AlipaySDKHandler alipaySDKHandler;
	public static AlipaySDKHandler getInstance(Properties properties)
	{
		if (alipaySDKHandler == null)
			alipaySDKHandler = new AlipaySDKHandler(properties);
		return alipaySDKHandler;
	}
	
	private AlipaySDKHandler(Properties properties)
	{
		for (String key : paramList) {
			if (properties.getProperty(key, "").equals(""))
				throw new InvalidParameterException("SDK params error:[" + key + "] empty.");
		}
		AlipaySDKConfig.partner 		= properties.getProperty(paramList[0]);
		AlipaySDKConfig.private_key 	= properties.getProperty(paramList[1]);
		AlipaySDKConfig.ali_public_key 	= properties.getProperty(paramList[2]);
		AlipaySDKConfig.email			= properties.getProperty(paramList[3]);
		AlipaySDKConfig.URL_NOTIFY		= properties.getProperty(paramList[4]);
		try {			
			AlipaySDKConfig.iNotify 		= (INotify)Class.forName(properties.getProperty(paramList[5])).newInstance();
			AlipaySDKConfig.iCreate			= (ICreate)Class.forName(properties.getProperty(paramList[6])).newInstance();
		} catch (Exception e) {
			throw new InvalidParameterException("SDK params error:" + properties.getProperty(paramList[5]) + " OR " + properties.getProperty(paramList[6]) +  " not found.");
		}
		AlipaySDKConfig.log_path		= properties.getProperty("log_path", "./");
	}
	
	@SuppressWarnings("rawtypes")
	public void notify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = request.getParameter("out_trade_no");

		//支付宝交易号

		String trade_no = request.getParameter("trade_no");

		//交易状态
		String trade_status = request.getParameter("trade_status");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if(AlipayNotify.verify(params)){
			//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				AlipaySDKConfig.iNotify.tradeFinished(request, out_trade_no, trade_no);
				
			} else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
				AlipaySDKConfig.iNotify.tradeSuccess(request, out_trade_no, trade_no);
			}

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			AlipaySDKConfig.iNotify.success(request, out_trade_no, trade_no);
			response.getWriter().println("success");	
			//请不要修改或删除
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			//验证失败
			AlipaySDKConfig.iNotify.fail(request, out_trade_no, trade_no);
			response.getWriter().println("fail");
		}
	}
	
	/**
	 * create the order info. 创建订单信息
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	private String getOrderInfo(HttpServletRequest request, String subject, String body, String price) throws UnsupportedEncodingException {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + AlipaySDKConfig.partner + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + AlipaySDKConfig.email + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + AlipaySDKConfig.iCreate.createOutTradeNo(request)+ "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + URLEncoder.encode(AlipaySDKConfig.URL_NOTIFY, "UTF-8") + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	public String getOrderString(HttpServletRequest request, String subject, String body, String price) throws UnsupportedEncodingException
	{
		String orderInfo = getOrderInfo(request, subject, body, price);
		String sign = SignUtils.sign(orderInfo, AlipaySDKConfig.private_key);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		return payInfo;
	}
	
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
