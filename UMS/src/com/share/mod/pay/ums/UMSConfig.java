package com.share.mod.pay.ums;

import com.share.mod.pay.ums.inter.ICallback;
import com.share.mod.pay.ums.inter.ICreate;
import com.share.mod.pay.ums.inter.INotify;
import com.share.mod.pay.ums.inter.IQuery;

public class UMSConfig {
	
	// 接口访问
	public static String URL_CALLBACK;
	public static String URL_NOTIFY;
	
	// form表单参数
	public static String FORM_MONEY;
	public static String FORM_DESC;
	
	public static String FORM_QUERY_TRANSID ;
	public static String FORM_QUERY_MERORDERID;
	public static String FORM_QUERY_ORDERDATE;
	
	// 订单有效时间
	public static String ORDER_EFFCTIVE_TIME; // 订单有效时间，单位为秒，值小于等于0表 示订单长期有效
	public static ICreate iCreate;
	public static ICallback iCallback;
	public static INotify iNotify;
	public static IQuery iQuery;
	
	
	//生产环境地址
	public static String creatOrderUrl	= "https://mpos.quanminfu.com:6004/merFrontMgr/orderBusinessServlet";
	public static String queryOrderUrl 	= "https://mpos.quanminfu.com:6004/merFrontMgr/orderQueryServlet";
	
	
	// 生产环境参数
	public static String verifyKeyMod;
	public static String verifyKeyExp;

	
	// 下面是商户的一些参数，需要根据实际修改
	// 测试环境下请勿修改
	public static String merId;// 商户号
	public static String merTermId;// 终端号

	
	//生产环境参数
	public static String signKeyMod;
	public static String signKeyExp;
	
}
