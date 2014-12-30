package com.share.mod.pay.ali.wap.inter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IInterrupt {
	/***
	 * 用户放弃支付
	 */
	public void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
