package com.share.mod.pay.ums.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICallback {
	
	/***
	 * 自定义返回请求
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doService(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException;
}
