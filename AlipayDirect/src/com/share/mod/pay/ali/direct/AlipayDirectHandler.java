package com.share.mod.pay.ali.direct;

import static com.share.mod.pay.ali.direct.AlipayConfig.FORM_BODY;
import static com.share.mod.pay.ali.direct.AlipayConfig.FORM_FEE;
import static com.share.mod.pay.ali.direct.AlipayConfig.FORM_SHOW_URL;
import static com.share.mod.pay.ali.direct.AlipayConfig.FORM_SUBJECT;
import static com.share.mod.pay.ali.direct.AlipayConfig.URL_CALLBACK;
import static com.share.mod.pay.ali.direct.AlipayConfig.URL_NOTIFY;
import static com.share.mod.pay.ali.direct.AlipayConfig.email;
import static com.share.mod.pay.ali.direct.AlipayConfig.iCallback;
import static com.share.mod.pay.ali.direct.AlipayConfig.iCreate;
import static com.share.mod.pay.ali.direct.AlipayConfig.iNotify;
import static com.share.mod.pay.ali.direct.AlipayConfig.key;
import static com.share.mod.pay.ali.direct.AlipayConfig.log_path;
import static com.share.mod.pay.ali.direct.AlipayConfig.partner;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.share.mod.pay.ali.direct.inter.ICallback;
import com.share.mod.pay.ali.direct.inter.ICreate;
import com.share.mod.pay.ali.direct.inter.INotify;
import com.share.mod.pay.ali.direct.util.AlipayNotify;
import com.share.mod.pay.ali.direct.util.AlipaySubmit;

/***
 * 
 * <ul>
 * <li> email		商户的账号（商户登陆支付宝的email账号），如abc@abc.com</li>
 * <li> partner	合作身份者ID，以2088开头由16位纯数字组成的字符串</li>
 * <li> key		交易安全检验码，由数字和字母组成的32位字符串, 如果签名方式设置为“MD5”时，请设置该参数</li>
 * <li> create		相应接口的实现类</li>
 * <li> notify		相应接口的实现类</li>
 * <li> callback		相应接口的实现类</li>
 * <li>	url_notify  通知地址</li>
 * <li>	url_callback 回调地址 </li>
 * </ul>
 * <ul>
 * <li>以下参数可选：</li>
 * <li> form_subject	创建订单时的订单名称的字段名(默认为"desc")</li>
 * <li> form_fee		创建订单时的金额的字段名(默认为"money")</li>
 * <li> form_body		创建订单时的订单描述的字段名(默认为"body")</li>
 * <li> form_show_url	创建订单时的商品展示url的字段名(默认为"show_url")</li>
 * <li> log_path 日志路径(默认为"./")</li>
 * </ul>
 */
public class AlipayDirectHandler {
	
	private static String[] paramList = {
		"email","partner",//1
		"key",//2
		"create","notify","callback",//5
		"url_notify","url_callback"//7
		};
		
	private static AlipayDirectHandler alipayDirectHandler;
	public static AlipayDirectHandler getInstance(Properties properties)
	{
		if (alipayDirectHandler == null)
			alipayDirectHandler = new AlipayDirectHandler(properties);
		return alipayDirectHandler;
	}
	
	private AlipayDirectHandler(Properties properties)
	{
		for (String key : paramList) {
			if (properties.getProperty(key, "").equals(""))
				throw new InvalidParameterException("Direct params error:[" + key + "] empty.");
		}
		email 			= properties.getProperty(paramList[0]);
		partner 		= properties.getProperty(paramList[1]);
		key 			= properties.getProperty(paramList[2]);
		try {
			iCreate		= (ICreate)Class.forName(properties.getProperty(paramList[3])).newInstance();
			iNotify		= (INotify)Class.forName(properties.getProperty(paramList[4])).newInstance();
			iCallback	= (ICallback)Class.forName(properties.getProperty(paramList[5])).newInstance();
		} catch (Exception e) {
			throw new InvalidParameterException("Direct params error: icreate, inotify, icallback, iinterrupt." + e.getMessage());
		}
		
		URL_NOTIFY		= properties.getProperty(paramList[6]);
		URL_CALLBACK	= properties.getProperty(paramList[7]);
		
		FORM_SUBJECT	= properties.getProperty("form_subject", "desc");
		FORM_FEE		= properties.getProperty("form_fee", "money");
		FORM_BODY		= properties.getProperty("form_body", "body");
		FORM_SHOW_URL	= properties.getProperty("form_show_url", "show_url");
		log_path		= properties.getProperty("log_path", "./");
	}
	
	public void create(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			//支付类型
			String payment_type = "1";
			//必填，不能修改
			//服务器异步通知页面路径
			String notify_url = URL_NOTIFY;
			//需http://格式的完整路径，不能加?id=123这类自定义参数		
			
			//页面跳转同步通知页面路径
			String return_url = URL_CALLBACK;
			//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/		
					
			//卖家支付宝帐户
			String seller_email = email;
			//必填		
			
			//商户订单号
			String out_trade_no = iCreate.createOutTradeNo();
			//商户网站订单系统中唯一订单号，必填		
			
			// 订单名称
			String subject = request.getParameter(FORM_SUBJECT);
			// 必填

			// 付款金额  ali原来的单位为元，改为单位为分
			Integer money = new Integer(request.getParameter(FORM_FEE));
			String total_fee =  String.valueOf(money * 1.0 / 100);
			// 必填
							
			
			//订单描述		
			String body = request.getParameter(FORM_BODY);
			//商品展示地址
			String show_url = request.getParameter(FORM_SHOW_URL);
			//需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html		
			
			//防钓鱼时间戳
			String anti_phishing_key = AlipaySubmit.query_timestamp();
			//若要使用请调用类文件submit中的query_timestamp函数		
			
			//客户端的IP地址
			String exter_invoke_ip = request.getRemoteAddr();
			//非局域网的外网IP地址，如：221.0.0.1
			
			
			if(subject == null || body == null || show_url == null)
			{
				throw new InvalidParameterException(" Param[" + FORM_SUBJECT + "," + FORM_BODY + "," + FORM_SHOW_URL + "] null");
			}
			
			//////////////////////////////////////////////////////////////////////////////////
			
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "create_direct_pay_by_user");
	        sParaTemp.put("partner", AlipayConfig.partner);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", payment_type);
			sParaTemp.put("notify_url", notify_url);
			sParaTemp.put("return_url", return_url);
			sParaTemp.put("seller_email", seller_email);
			sParaTemp.put("out_trade_no", out_trade_no);
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", total_fee);
			sParaTemp.put("body", body);
			sParaTemp.put("show_url", show_url);
			sParaTemp.put("anti_phishing_key", anti_phishing_key);
			sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
			
			//建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println(sHtmlText);
			
		}catch (NumberFormatException e){
			
			iCreate.handleNumberFormatException(request, response);
			
		}catch (Exception e) {
			
			iCreate.handleException(request, response, e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void callback(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			//获取支付宝GET过来反馈信息
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
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
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
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params);
			
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					iCallback.tradeFinishedOrSuccess(request, response, out_trade_no, trade_no);
				}
				
				//该页面可做页面美工编辑
				iCallback.success(request, response, out_trade_no, trade_no);
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{
				//该页面可做页面美工编辑
				iCallback.fail(request, response, out_trade_no, trade_no);
			}
		}catch (Exception e){
			iCallback.handleException(request, response, e);
		}
	}
	
	/***
	 * <strong style="color:#f00">请注意，该方法的请求为post方法</strong>
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public void notify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {

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

			if(AlipayNotify.verify(params)){//验证成功
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
					iNotify.tradeFinished(request, out_trade_no, trade_no);
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
					iNotify.tradeSuccess(request, out_trade_no, trade_no);
				}

				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				iNotify.success(request, out_trade_no, trade_no);
				response.getWriter().println("success");	//请不要修改或删除

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
				iNotify.fail(request, out_trade_no, trade_no);
				response.getWriter().println("fail");
			}
		} catch (Exception e) {
			iNotify.handleException(e);
		}
	}
}
