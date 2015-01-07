package test.put;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.share.mod.common.req.ParameterRequestWrapper;

@WebFilter("/*")
public class PutFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String method = ((HttpServletRequest)request).getMethod().toLowerCase(); 
		if (method.equals("put") || method.equals("delete"))
		{
			if (request.getContentType().equals("application/x-www-form-urlencoded"))
			{
				BufferedReader reader = request.getReader();
				StringBuffer buf = new StringBuffer();
				String str;
				while ((str = reader.readLine()) != null)
				{
					buf.append(str);
				}
				str = buf.toString();
				System.out.println("filter{put,delete}:" + str);
				chain.doFilter(addRequestParams(request, str), response);
			}
			else
			{
				System.out.println("put or delete method");
				chain.doFilter(request, response);
			}
		}
		else
			chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	private ServletRequest addRequestParams(ServletRequest request, String params) {
		HashMap<String, String[]> m = new HashMap<String, String[]>(request.getParameterMap());
		String[] p = params.split("&");
		for (String str : p) {
			String[] kv = str.split("=");
			m.put(kv[0], new String[]{kv[1]});
		}
		ParameterRequestWrapper wrap = new ParameterRequestWrapper((HttpServletRequest)request, m);
		return wrap;
	}
}
