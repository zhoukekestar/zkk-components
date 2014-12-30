package com.share.mod.pay.ali.wap._test.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import com.share.mod.pay.ali.wap.AlipayWAPHandler;

public class CtrlBase extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static AlipayWAPHandler handler;
	
	static
	{
		Properties properties = new Properties();
		try {
			String path = CtrlBase.class.getClassLoader().getResource("").toURI().getPath();
			path += "com/share/mod/pay/ali/wap/_test/alipaywap.properties";
			properties.load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		handler = AlipayWAPHandler.getInstance(properties);
	}
}
