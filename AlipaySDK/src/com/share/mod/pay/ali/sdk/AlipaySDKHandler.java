package com.share.mod.pay.ali.sdk;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.share.mod.pay.ali.sdk.inter.INotify;
import com.share.mod.pay.ali.sdk.util.AlipayNotify;

/***
 * 在properties文件中需要包含以下参数
 * <ul>
 * <li>partner 合作身份者ID，以2088开头由16位纯数字组成的字符串</li>
 * <li>private_key 商户的私钥</li>
 * <li>ali_public_key 支付宝的公钥，无需修改该值</li>
 * <li>notify iNotify的实现类包全名</li>
 * </ul>
 * 
 * <ul>
 * <li>以下参数可选：</li>
 * <li>log_path 默认为“./”</li>
 * </ul>
 * 
 */
public class AlipaySDKHandler {
	
	private static String[] paramList = {"partner","private_key","ali_public_key","notify"};
	
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
		try {			
			AlipaySDKConfig.iNotify 		= (INotify)Class.forName(properties.getProperty(paramList[3])).newInstance();
		} catch (Exception e) {
			throw new InvalidParameterException("SDK params error:" + properties.getProperty(paramList[3]) + " not found.");
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
}
