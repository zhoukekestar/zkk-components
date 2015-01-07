package com.share.mod.pay.ali.direct._test.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import com.share.mod.pay.ali.direct.AlipayDirectHandler;

public class CtrlBase extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static AlipayDirectHandler handler;
	
	static
	{
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File("D:/pay/alipaydirect.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		handler = AlipayDirectHandler.getInstance(properties);
	}
}
