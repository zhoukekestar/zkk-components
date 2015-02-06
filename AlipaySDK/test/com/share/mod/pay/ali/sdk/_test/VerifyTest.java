package com.share.mod.pay.ali.sdk._test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.share.mod.pay.ali.sdk.sign.RSA;
import com.share.mod.pay.ali.sdk.util.AlipayCore;

public class VerifyTest {
	public static void main(String[] args) {
		
		String a = "12";
		double d = Double.valueOf(a);
		
		int i = (int)(d * 100);
		System.out.println(i);
		
		
		String data = "partner=\"2088311706076427\"&seller_id=\"weicheng@otovc.com\"&out_trade_no=\"1220150126000004\"&subject=\"宣传单支付\"&body=\"商家宣传单的支付账单\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fm.api.toomao.com%2Ftoomao_api%2Fpay%2Fali%2Fsdk%2Fnotify\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&return_url=\"m.alipay.com\"&success=\"true\"&sign_type=\"RSA\"&sign=\"UIKsDBvwZsUvCvTEWE6AtbLNZGpP5pkdRnuM8YkF5K5OtGw6hGf/5A0OiOqTr7DhyU76V/tiPoCADSyqLXL1xNodZCyltcKmd6iAKGlpRAjGDs5SbtqoE163K4fIOBr9ivedmgl2rI0Y8mVSCVnWd64nsEzSSEKjsLgFLpcuMH8=\"";
		
		String[] paramArray = data.split("&");
		Map<String, String> paramMap = new HashMap<String, String>();
		for (String string : paramArray) {
			String key = string.substring(0, string.indexOf("="));
			//String val = string.substring(string.indexOf("=") + 2, string.length() - 1);
			String val = string.substring(string.indexOf("=") + 2, string.length() - 1);
			paramMap.put(key, val);
		}
       
        
		String sign = paramMap.get("sign");
		String pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCniCUMlQjqv7hOE1syABLFqo7HTmkUHbKVw47UGh7tavFfWR1HlEv/EJGDNFP384RMzhRX8zLmqUyWA4ac1Z41CAqB+g5TRWMZtvCHIhRvEv6gCBlxen3JP6PY0aB51YeNV5/x246ClKSv20ECrNlWvkijDZggZC9Hi9W01dz3gQIDAQAB";
		
		System.out.println(sign);
		
		printMap(paramMap);
		//过滤空值、sign与sign_type参数
		paramMap = AlipayCore.paraFilter(paramMap);
		printMap(paramMap);
		
		System.out.println(getStr(paramMap));
		System.out.println(RSA.verify(getStr(paramMap), sign, pub_key, "utf-8"));
	}
	
	public static String getStr(Map<String, String> params)
	{
		List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
	}
	
	public static void printMap(Map<String, String> map)
	{
		System.out.println();
		for (String key : map.keySet()) {
			System.out.print(key + ":" + map.get(key) + "\t");
		}
		System.out.println();
	}
}
