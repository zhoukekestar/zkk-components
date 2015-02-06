package test.put;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
@WebServlet("/browser")
public class Browser extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Enumeration<String> names = req.getHeaderNames();
		
		resp.getWriter().write("<table>");
		while (names.hasMoreElements())
		{
			String name = names.nextElement();
			resp.getWriter().write("<tr><td>" + name + "</td><td>" + req.getHeader(name) + "</td></tr>");
		}
		resp.getWriter().print("</table>");
	}
}
