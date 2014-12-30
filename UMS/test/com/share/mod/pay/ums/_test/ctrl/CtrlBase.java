package com.share.mod.pay.ums._test.ctrl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import com.share.mod.pay.ums.UMSHandler;

public class CtrlBase extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static UMSHandler handler;
	
	static
	{
		Properties properties = new Properties();
		try {
			String path = CtrlBase.class.getClassLoader().getResource("").toURI().getPath();
			path += "com/share/mod/pay/ums/_test/ctrl/ums.properties";
			properties.load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		handler = UMSHandler.getInstance(properties);
	}
}
