package com.share.mod.pay.ali.direct._test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.share.mod.pay.ali.direct.AlipayDirectHandler;

public class ApplicationTest {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("D:/pay/alipaydirect.properties")));
		AlipayDirectHandler alipayWAPHandler = AlipayDirectHandler.getInstance(properties);
		
	}
}
