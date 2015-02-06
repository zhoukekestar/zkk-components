package com.share.mod.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;


@WebFilter(filterName="com_toomao_filter_Encoding", urlPatterns="/*", asyncSupported=true)
public class Encoding implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		/*
		 * Change the tomcat's server.xml file.
		 * You should change the node marked as follows:
		 * <Connector>
		 * 
		 * Add the attribute to "<Connector> node":
		 * useBodyEncodingForURI="true" 
		 * URIEncoding="UTF-8" 			
		 * 
		 * 
		 * 
		 * All configuration like as follows:
		 * <Connector connectionTimeout="20000" useBodyEncodingForURI="true" URIEncoding="UTF-8"  port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
		 * 
		 * */	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
