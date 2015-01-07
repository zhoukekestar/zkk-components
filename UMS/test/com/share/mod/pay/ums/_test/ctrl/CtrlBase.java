package com.share.mod.pay.ums._test.ctrl;

import java.io.File;
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
			properties.load(new FileInputStream(new File("D:/pay/ums.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		handler = UMSHandler.getInstance(properties);
	}
}
