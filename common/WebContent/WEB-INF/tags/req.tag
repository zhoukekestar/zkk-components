<%@tag pageEncoding="UTF-8" import="com.share.mod.common.utils.HttpRequest"%>
<%@tag import="java.security.InvalidParameterException"%>
<%@tag import="org.apache.log4j.Logger"%>
<%@tag import="com.alibaba.fastjson.JSON"%>

<%@attribute name="url" required="true" %>
<%@attribute name="save" required="true" %>
<%@attribute name="method" required="true" %>
<%
	String result = "";
	try{
		// 获取cookie
		String cookie = request.getHeader("Cookie");
		if (cookie == null) cookie = "";

		// 细分请求方法
		if (method.equals("") || method.equalsIgnoreCase(HttpRequest.METHOD_GET))
			result = HttpRequest.get(url).header("Cookie", cookie).body();
		
		else if (method.equalsIgnoreCase(HttpRequest.METHOD_POST))
			result = HttpRequest.post(url).header("Cookie", cookie).body();
		
		else if (method.equalsIgnoreCase(HttpRequest.METHOD_PUT))
			result = HttpRequest.put(url).header("Cookie", cookie).body();
		
		else if (method.equalsIgnoreCase(HttpRequest.METHOD_DELETE))
			result = HttpRequest.delete(url).header("Cookie", cookie).body();
		
		else
			throw new InvalidParameterException("method not found.");
		// 保存请求信息
		request.setAttribute(save, JSON.parse(result));
	}
	catch(Exception e)
	{
		Logger logger = Logger.getLogger("/WEB-INF/tags/req.tag");
		logger.error("req:[" + url + "]");
		logger.error("res:[" + result + "]");
		e.printStackTrace();
		request.setAttribute(save, "error");
	}
%>