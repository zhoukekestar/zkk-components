package com.share.mod.pay.ums;

import static com.share.mod.pay.ums.UMSConfig.FORM_DESC;
import static com.share.mod.pay.ums.UMSConfig.FORM_MONEY;
import static com.share.mod.pay.ums.UMSConfig.FORM_QUERY_MERORDERID;
import static com.share.mod.pay.ums.UMSConfig.FORM_QUERY_ORDERDATE;
import static com.share.mod.pay.ums.UMSConfig.FORM_QUERY_TRANSID;
import static com.share.mod.pay.ums.UMSConfig.ORDER_EFFCTIVE_TIME;
import static com.share.mod.pay.ums.UMSConfig.URL_CALLBACK;
import static com.share.mod.pay.ums.UMSConfig.URL_NOTIFY;
import static com.share.mod.pay.ums.UMSConfig.iCallback;
import static com.share.mod.pay.ums.UMSConfig.iCreate;
import static com.share.mod.pay.ums.UMSConfig.iNotify;
import static com.share.mod.pay.ums.UMSConfig.iQuery;
import static com.share.mod.pay.ums.UMSConfig.merId;
import static com.share.mod.pay.ums.UMSConfig.merTermId;
import static com.share.mod.pay.ums.UMSConfig.signKeyExp;
import static com.share.mod.pay.ums.UMSConfig.signKeyMod;
import static com.share.mod.pay.ums.UMSConfig.verifyKeyExp;
import static com.share.mod.pay.ums.UMSConfig.verifyKeyMod;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinaums.pay.api.PayException;
import com.chinaums.pay.api.entities.NoticeEntity;
import com.chinaums.pay.api.entities.OrderEntity;
import com.chinaums.pay.api.entities.QueryEntity;
import com.chinaums.pay.api.impl.DefaultSecurityService;
import com.chinaums.pay.api.impl.UMSPayServiceImpl;
import com.share.mod.pay.ums.inter.ICallback;
import com.share.mod.pay.ums.inter.ICreate;
import com.share.mod.pay.ums.inter.INotify;
import com.share.mod.pay.ums.inter.IQuery;

/****
 * <ul>
 * <li>merId 商户号</li>
 * <li>merTermId 终端号</li>
 * <li>verifyKeyMod 验证模</li>
 * <li>verifyKeyExp 验证指数</li>
 * <li>signKeyMod 签名模</li>
 * <li>signKeyExp 签名指数</li>
 * <li>url_callback 回调地址</li>
 * <li>create 创建订单实现类（包全名）</li>
 * <li>callback 回调执行实现类（包全名）</li>
 * <li>notify 通知执行实现类（包全名）</li>
 * <li>query 查询实现类（包全名）</li>
 * </ul>
 * <ul>
 * <li>以下参数可选：</li>
 * <li>form_money 创建订单金额字段名，默认“money”</li>
 * <li>form_desc 创建订单秒速字段名，默认“desc”</li>
 * <li>form_query_transid 查询订单transid，默认“transid”</li>
 * <li>form_query_merorderid 查询订单merorderid，默认“merorderid”</li>
 * <li>form_query_orderdate 查询订单orderdate，默认“orderdate”</li>
 * <li>effective_time 订单有效时间，单位为秒，值小于等于0表 示订单长期有效，默认为“0”</li>
 * </ul>
 * 
 * <h3>依赖包</h3>
 * <ul>
 * <li>ums-commons-beanutils-1.8.1.jar</li>
 * <li>ums-commons-collections-3.2.jar</li>
 * <li>ums-commons-lang-2.5.jar</li>
 * <li>ums-commons-logging-1.1.jar</li>
 * <li>ums-ezmorph-1.0.6.jar</li>
 * <li>ums-jackson-all-1.7.6.jar</li>
 * <li>ums-json-lib-2.4-jdk15.jar</li>
 * <li>ums-pay-api-2.0.2_for-ums.jar</li>
 * </ul>
 * @param properties
 */
public class UMSHandler {
	
	private String[] paramList = {
			"merId","merTermId",//1
			"verifyKeyMod","verifyKeyExp",//3
			"signKeyMod","signKeyExp",//5
			"url_callback","url_notify",//7
			"create","callback","notify","query"//11
	};
	
	
	private static UMSHandler umsHandler;
	public static UMSHandler getInstance(Properties properties)
	{
		if (umsHandler == null)
			umsHandler = new UMSHandler(properties);
		return umsHandler;
	}
	
	public UMSHandler(Properties properties)
	{
		for (String key : paramList) {
			if (properties.getProperty(key, "").equals(""))
				throw new InvalidParameterException("UMS params error:[" + key + "] empty.");
		}
		merId			= properties.getProperty(paramList[0]);
		merTermId 		= properties.getProperty(paramList[1]);
		verifyKeyMod	= properties.getProperty(paramList[2]);
		verifyKeyExp	= properties.getProperty(paramList[3]);
		signKeyMod		= properties.getProperty(paramList[4]);
		signKeyExp		= properties.getProperty(paramList[5]);
		URL_CALLBACK	= properties.getProperty(paramList[6]);
		URL_NOTIFY		= properties.getProperty(paramList[7]);
		
		try {
			iCreate 	= (ICreate)Class.forName(properties.getProperty(paramList[8])).newInstance();
			iCallback 	= (ICallback)Class.forName(properties.getProperty(paramList[9])).newInstance();
			iNotify 	= (INotify)Class.forName(properties.getProperty(paramList[10])).newInstance();
			iQuery 		= (IQuery)Class.forName(properties.getProperty(paramList[11])).newInstance();
		} catch (Exception e) {
			throw new InvalidParameterException("UMS params error:icreate, icallback, inotify, iquery." + e.getMessage());
		}
		
		FORM_MONEY 		= properties.getProperty("form_money","money");
		FORM_DESC 		= properties.getProperty("form_desc","desc");
		FORM_QUERY_MERORDERID 	= properties.getProperty("form_query_merorderid","merorderid");
		FORM_QUERY_ORDERDATE 	= properties.getProperty("form_query_orderdate","orderdate");
		FORM_QUERY_TRANSID 		= properties.getProperty("form_query_transid","transid");
		FORM_DESC 		= properties.getProperty("form_desc","desc");
		ORDER_EFFCTIVE_TIME = properties.getProperty("effective_time", "0");
	}
	
	public void create(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 获取订单参数
			String formMoney = request.getParameter(FORM_MONEY);
			String formDesc = request.getParameter(FORM_DESC);

			if (formMoney == null || formDesc == null || formDesc.trim().length() == 0) {
				throw new InvalidParameterException("One of Params[" + FORM_MONEY + "," + FORM_DESC + "] is null.");
			}
			@SuppressWarnings("unused")
			Integer temp = new Integer(formMoney);
			

			// 工具类
			DefaultSecurityService ss = UMSUtils.getDafaultService();
			UMSPayServiceImpl service = UMSUtils.getUMSService();
			// 订单实体
			OrderEntity order = new OrderEntity();
			OrderEntity respOrder = new OrderEntity();

			String curreTime = UMSUtils.getCurrentTime();
			String merOrderId = iCreate.createMerOrderId(request);
			// 构建订单
			order.setMerId(merId); 							// 商户号
			order.setMerTermId(merTermId); 					// 终端号
			order.setMerOrderId(merOrderId); 				// 订单号，商户根据自己的规则生成，最长32位
			order.setOrderDate(curreTime.substring(0, 8)); 	// 订单日期
			order.setOrderTime(curreTime.substring(8)); 	// 订单时间
			order.setTransAmt(formMoney); 					// 订单金额(单位分)
			order.setOrderDesc(formDesc); 					// 订单描述
			order.setNotifyUrl(URL_NOTIFY);					// 通知商户地址，保证外网能够访问
			order.setTransType("NoticePay"); 				// 固定值
			order.setEffectiveTime(ORDER_EFFCTIVE_TIME); 	// 订单有效期期限（秒），值小于等于0表示订单长期有效
			order.setMerSign(ss.sign(order.buildSignString()));

			respOrder = service.createOrder(order);
			iCreate.recordOrder(order, respOrder);

			// 页面值的设置
			
			//iCreate.doService(request, response, ss.sign(respOrder.getTransId() + respOrder.getChrCode()), respOrder.getChrCode(), respOrder.getTransId());
			
			Map<String, String> params = new HashMap<>();
			params.put("merSign", ss.sign(respOrder.getTransId() + respOrder.getChrCode()));
			params.put("chrCode", respOrder.getChrCode());
			params.put("tranId", respOrder.getTransId());
			params.put("url", UMSConfig.URL_CALLBACK);
			
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(buildRequest("https://mpos.quanminfu.com:8018/umsFrontWebQmjf/umspay", params));

			iCreate.log(respOrder.getTransId(), respOrder.getMerOrderId(), curreTime.substring(0, 8));
			
		}catch(InvalidParameterException e){
			iCreate.handleNullException(request, response, e);
		}catch(NumberFormatException e){
			iCreate.handleMoneyException(request, response, e);
		}catch (PayException e) {		
			iCreate.handlePayException(request, response, e);
		}
	}
	
	public static String buildRequest(String action, Map<String, String> params) {
	
		StringBuffer sbHtml = new StringBuffer();
		
		sbHtml.append("<form id=\"umspaysubmit\" name=\"umspaysubmit\" action=\"" + action + "\" method=\"post\">");
		
		for (String key : params.keySet()) {
			sbHtml.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + params.get(key) + "\"/>");
		}
		sbHtml.append("</form>");
		sbHtml.append("<script>document.forms['umspaysubmit'].submit();</script>");
	    return sbHtml.toString();
	}
	
	public void callback(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		iCallback.doService(request, response);
	}
	
	public void notify(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			
			UMSPayServiceImpl service = UMSUtils.getUMSService();
			
			NoticeEntity noticeEntity = service.parseNoticeEntity(req);
			NoticeEntity respEntity = new NoticeEntity();
			
			iNotify.log(req, noticeEntity);
			
			respEntity.setMerOrderState("00");
			
			// 返回银联
			String respStr = service.getNoticeRespString(respEntity);
			resp.setCharacterEncoding("utf-8");
			PrintWriter out = resp.getWriter();
			out.write(respStr);
			out.flush();
			out.close();
		
		} catch (PayException e) {
			iNotify.handlePayException(e);
		}
	}
	
	public void query(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// 参数
			String transid 		= req.getParameter(FORM_QUERY_TRANSID);
			String merOrderid 	= req.getParameter(FORM_QUERY_MERORDERID);
			String orderDate 	= req.getParameter(FORM_QUERY_ORDERDATE);

			if (transid == null || merOrderid == null || orderDate == null)
			{
				throw new InvalidParameterException("One of params[" + FORM_QUERY_TRANSID + "," + FORM_QUERY_MERORDERID + "," + FORM_QUERY_ORDERDATE + "] is null.");
			}
			UMSPayServiceImpl service = UMSUtils.getUMSService();

			QueryEntity queryOrder = new QueryEntity();
			queryOrder.setMerId(merId);
			queryOrder.setMerTermId(merTermId);
			queryOrder.setTransId(transid);
			queryOrder.setMerOrderId(merOrderid);
			queryOrder.setOrderDate(orderDate);
			queryOrder.setReqTime(UMSUtils.getCurrentTime());

			QueryEntity respOrder = new QueryEntity();
			respOrder = service.queryOrder(queryOrder);
			
			iQuery.doService(req, resp, respOrder);
			
		}catch(InvalidParameterException e){
			iQuery.handleNullException(req, resp, e);
		}catch (PayException e) {
			iQuery.handlePayException(req, resp, e);
		}

	}
}
