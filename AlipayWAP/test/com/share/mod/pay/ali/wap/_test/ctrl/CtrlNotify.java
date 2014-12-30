package com.share.mod.pay.ali.wap._test.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pay/wap/notify")
public class CtrlNotify extends CtrlBase {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("in!!!!!!!!!!!!!!!!!!!-----------------------");
		handler.notify(req, resp);
	}
}
