package com.share.mod.pay.ali.wap;

import static com.share.mod.pay.ali.wap.AlipayWAPConfig.FORM_FEE;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.FORM_SUBJECT;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.URL_CALLBACK;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.URL_INTERRUPT;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.URL_NOTIFY;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.ali_public_key;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.email;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.iCallback;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.iCreate;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.iInterrupt;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.iNotify;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.input_charset;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.key;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.log_path;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.partner;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.private_key;
import static com.share.mod.pay.ali.wap.AlipayWAPConfig.sign_type;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.share.mod.pay.ali.wap.inter.ICallback;
import com.share.mod.pay.ali.wap.inter.ICreate;
import com.share.mod.pay.ali.wap.inter.IInterrupt;
import com.share.mod.pay.ali.wap.inter.INotify;
import com.share.mod.pay.ali.wap.util.AlipayNotify;
import com.share.mod.pay.ali.wap.util.AlipaySubmit;
import com.share.mod.pay.ali.wap.util.UtilDate;

/***
 * 
 * <ul>
 * <li> email		商户的账号（商户登陆支付宝的email账号），如abc@abc.com</li>
 * <li> partner	合作身份者ID，以2088开头由16位纯数字组成的字符串</li>
 * <li> key		交易安全检验码，由数字和字母组成的32位字符串, 如果签名方式设置为“MD5”时，请设置该参数</li>
 * <li> private_key 	商户的私钥, 如果签名方式设置为“0001”时，请设置该参数</li>
 * <li> ali_public_key	支付宝的公钥  如果签名方式设置为“0001”时，请设置该参数</li>
 * <li> create		相应接口的实现类</li>
 * <li> notify		相应接口的实现类</li>
 * <li> callback		相应接口的实现类</li>
 * <li> interrupt	相应接口的实现类</li>
 * <li>	url_notify  通知地址</li>
 * <li>	url_callback 回调地址 </li>
 * <li>	url_interrupt  中断地址</li>
 * </ul>
 * <ul>
 * <li>以下参数可选：</li>
 * <li> form_subject	创建订单时的备注的字段名(默认为"desc")</li>
 * <li> form_fee		创建订单时的金额的字段名(默认为"money")</li>
 * <li> log_path 日志路径(默认为"./")</li>
 * </ul>
 */
public class AlipayWAPHandler {
	
	private static String[] paramList = {
		"email","partner",//1
		"key","private_key","ali_public_key",//4
		"create","notify","callback","interrupt",//8
		"url_notify","url_callback","url_interrupt"//11
		};
		
	private static AlipayWAPHandler alipayWAPHandler;
	public static AlipayWAPHandler getInstance(Properties properties)
	{
		if (alipayWAPHandler == null)
			alipayWAPHandler = new AlipayWAPHandler(properties);
		return alipayWAPHandler;
	}
	
	private AlipayWAPHandler(Properties properties)
	{
		for (String key : paramList) {
			if (properties.getProperty(key, "").equals(""))
				throw new InvalidParameterException("Wap params error:[" + key + "] empty.");
		}
		email 			= properties.getProperty(paramList[0]);
		partner 		= properties.getProperty(paramList[1]);
		key 			= properties.getProperty(paramList[2]);
		private_key 	= properties.getProperty(paramList[3]);
		ali_public_key 	= properties.getProperty(paramList[4]);
		try {
			iCreate		= (ICreate)Class.forName(properties.getProperty(paramList[5])).newInstance();
			iNotify		= (INotify)Class.forName(properties.getProperty(paramList[6])).newInstance();
			iCallback	= (ICallback)Class.forName(properties.getProperty(paramList[7])).newInstance();
			iInterrupt	= (IInterrupt)Class.forName(properties.getProperty(paramList[8])).newInstance();
		} catch (Exception e) {
			throw new InvalidParameterException("Wap params error: icreate, inotify, icallback, iinterrupt." + e.getMessage());
		}
		
		URL_NOTIFY		= properties.getProperty(paramList[9]);
		URL_CALLBACK	= properties.getProperty(paramList[10]);
		URL_INTERRUPT	= properties.getProperty(paramList[11]);
		
		FORM_SUBJECT	= properties.getProperty("form_subject", "desc");
		FORM_FEE		= properties.getProperty("form_fee", "money");
		log_path		= properties.getProperty("log_path", "./");
	}
	
	public void create(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 支付宝网关地址
			String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";

			// //////////////////////////////////调用授权接口alipay.wap.trade.create.direct获取授权码token//////////////////////////////////////

			// 返回格式
			String format = "xml";
			// 必填，不需要修改

			// 返回格式
			String v = "2.0";
			// 必填，不需要修改

			// 请求号
			String req_id = UtilDate.getOrderNum();
			// 必填，须保证每次请求都是唯一

			// req_data详细信息

			// 服务器异步通知页面路径
			String notify_url = URL_NOTIFY;
			// 需http://格式的完整路径，不能加?id=123这类自定义参数

			// 页面跳转同步通知页面路径
			String call_back_url = URL_CALLBACK;
			// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

			// 操作中断返回地址
			String merchant_url = URL_INTERRUPT;
			// 用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数

			// 卖家支付宝帐户
			String seller_email = email;
			// 必填

			// 商户订单号
			String out_trade_no = iCreate.createOutTradeNo(request);
			// 商户网站订单系统中唯一订单号，必填

			// 订单名称
			String subject = request.getParameter(FORM_SUBJECT);
			// 必填

			// 付款金额  ali原来的单位为元，改为单位为分
			Integer money = new Integer(request.getParameter(FORM_FEE));
			String total_fee =  String.valueOf(money * 1.0 / 100);
			// 必填

			// 请求业务参数详细
			String req_dataToken = "<direct_trade_create_req><notify_url>"
					+ notify_url + "</notify_url><call_back_url>"
					+ call_back_url + "</call_back_url><seller_account_name>"
					+ seller_email + "</seller_account_name><out_trade_no>"
					+ out_trade_no + "</out_trade_no><subject>" + subject
					+ "</subject><total_fee>" + total_fee
					+ "</total_fee><merchant_url>" + merchant_url
					+ "</merchant_url></direct_trade_create_req>";
			// 必填

			// ////////////////////////////////////////////////////////////////////////////////

			// 把请求参数打包成数组
			Map<String, String> sParaTempToken = new HashMap<String, String>();
			sParaTempToken.put("service", "alipay.wap.trade.create.direct");
			sParaTempToken.put("partner", partner);
			sParaTempToken.put("_input_charset", input_charset);
			sParaTempToken.put("sec_id", sign_type);
			sParaTempToken.put("format", format);
			sParaTempToken.put("v", v);
			sParaTempToken.put("req_id", req_id);
			sParaTempToken.put("req_data", req_dataToken);

			// 建立请求
			String sHtmlTextToken = AlipaySubmit.buildRequest(
					ALIPAY_GATEWAY_NEW, "", "", sParaTempToken);
			// URLDECODE返回的信息
			sHtmlTextToken = URLDecoder.decode(sHtmlTextToken,
					input_charset);
			// 获取token
			String request_token = AlipaySubmit.getRequestToken(sHtmlTextToken);
			// out.println(request_token);

			// //////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////

			// 业务详细
			String req_data = "<auth_and_execute_req><request_token>"
					+ request_token + "</request_token></auth_and_execute_req>";
			// 必填

			// 把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
			sParaTemp.put("partner", partner);
			sParaTemp.put("_input_charset", input_charset);
			sParaTemp.put("sec_id", sign_type);
			sParaTemp.put("format", format);
			sParaTemp.put("v", v);
			sParaTemp.put("req_data", req_data);

			// 建立请求
			String sHtmlText = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW,
					sParaTemp, "get", "确认");
			
			response.setContentType("text/html; charset=utf-8");
			
			sHtmlText = "<!DOCTYPE HTML><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>支付宝手机网页支付</title></head>"
						+ sHtmlText +
						"<body></body></html>";	
			response.getWriter().print(sHtmlText);
			
			iCreate.log(request);
			
		} catch (NumberFormatException e){
			
			iCreate.handleNumberFormatException(request, response, e);
			
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
			String result = request.getParameter("result");
	
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verifyReturn(params);
			
			if(verify_result){//验证成功
				
				if (result.equals("TRADE_FINISHED") || result.equals("TRADE_SUCCESS")) {
					iCallback.tradeFinishedOrSuccess(request, response, out_trade_no, trade_no);
				}
				
				iCallback.success(request, response, out_trade_no, trade_no);
				
			}else{
				iCallback.fail(request, response, out_trade_no, trade_no);
			}
		}catch (Exception e){
			iCallback.handleException(request, response, e);
		}
	}
	
	public void interrupt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		iInterrupt.get(req, resp);
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

			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//

			// RSA签名解密
			if (sign_type.equals("0001")) {
				params = AlipayNotify.decrypt(params);
			}
			// XML解析notify_data数据
			Document doc_notify_data = DocumentHelper.parseText(params
					.get("notify_data"));

			// 商户订单号
			String out_trade_no = doc_notify_data.selectSingleNode(
					"//notify/out_trade_no").getText();

			// 支付宝交易号
			String trade_no = doc_notify_data.selectSingleNode(
					"//notify/trade_no").getText();

			// 交易状态
			String trade_status = doc_notify_data.selectSingleNode(
					"//notify/trade_status").getText();

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			if (AlipayNotify.verifyNotify(params)) {// 验证成功
				// ////////////////////////////////////////////////////////////////////////////////////////
				// 请在这里加上商户的业务逻辑程序代码

				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 该种交易状态只在两种情况下出现
					// 1、开通了普通即时到账，买家付款成功后。
					// 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
					iNotify.tradeFinished(request, out_trade_no, trade_no);
					response.getWriter().print("success"); // 请不要修改或删除
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
					iNotify.tradeSuccess(request, out_trade_no, trade_no);
					response.getWriter().print("success"); // 请不要修改或删除
				}

				iNotify.success(request, out_trade_no, trade_no);
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				// ////////////////////////////////////////////////////////////////////////////////////////
			} else {// 验证失败
				
				iNotify.fail(request, out_trade_no, trade_no);
				response.getWriter().print("fail");
			}
		} catch (Exception e) {
			iNotify.handleException(e);
		}
	}
}
