package com.share.mod.pay.ali.sdk._test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.share.mod.pay.ali.sdk.AlipaySDKHandler;

public class AlipaySDKApplication {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		System.out.println(ClassLoader.getSystemResource("").toString());
		properties.load(new FileInputStream(new File("D:/pay/alipaysdk.properties")));
		
		AlipaySDKHandler handler = AlipaySDKHandler.getInstance(properties);
		
		System.out.println("ok");
	}
}
