package com.share.mod.pay.ums;

import static com.share.mod.pay.ums.UMSConfig.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.chinaums.pay.api.impl.DefaultSecurityService;
import com.chinaums.pay.api.impl.UMSPayServiceImpl;


public class UMSUtils {
	public static UMSPayServiceImpl getUMSService()
	{

		DefaultSecurityService ss = new DefaultSecurityService();

		ss.setSignKeyModHex(signKeyMod);
		ss.setSignKeyExpHex(signKeyExp);
		ss.setVerifyKeyExpHex(verifyKeyExp);
		ss.setVerifyKeyModHex(verifyKeyMod);

		UMSPayServiceImpl service = new UMSPayServiceImpl();
		service.setSecurityService(ss);
		service.setOrderServiceURL(creatOrderUrl);
		service.setQueryServiceURL(queryOrderUrl);
		return service;
	}
	
	public static DefaultSecurityService getDafaultService()
	{
		DefaultSecurityService ss = new DefaultSecurityService();

		ss.setSignKeyModHex(signKeyMod);
		ss.setSignKeyExpHex(signKeyExp);
		ss.setVerifyKeyExpHex(verifyKeyExp);
		ss.setVerifyKeyModHex(verifyKeyMod);
		
		return ss;
	}
	
	public static String getCurrentTime()
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
		return sf.format(new Date());
	}
}
