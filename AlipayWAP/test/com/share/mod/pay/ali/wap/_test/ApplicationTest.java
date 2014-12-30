package com.share.mod.pay.ali.wap._test;

import java.io.IOException;
import java.util.Properties;

import com.share.mod.pay.ali.wap.AlipayWAPHandler;

public class ApplicationTest {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("com/share/mod/pay/ali/wap/_test/alipaywap.properties"));
		AlipayWAPHandler alipayWAPHandler = AlipayWAPHandler.getInstance(properties);
		
		System.out.print("ok");
	}
}
