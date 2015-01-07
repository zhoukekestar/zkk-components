package test.put;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet("/test")
@MultipartConfig
public class Put extends HttpServlet {
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("---------put---------");
		print(req);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//req.getParameterMap().put("zkk", new String[]{"zkktest"});
		System.out.println("---------get---------");
		//System.out.println(req.getParameter("zkk"));
		print(req);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("---------post---------");
		print(req);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("---------delete---------");
		print(req);
	}
	
	private void print(HttpServletRequest request) throws IOException
	{
		Map<String, String[]> params = request.getParameterMap();
		
		for (String key : params.keySet()) {
			System.out.println(key + " " + params.get(key)[0].toString());
		}
		System.out.println("----------------");
		if (request.getContentType() != null && request.getContentType().toLowerCase().indexOf("multipart/form-data") != -1)
		{
			System.out.println(request.getContentType());
			Collection<Part> parts = null;
			try {
				parts = request.getParts();
			} catch (IllegalStateException | IOException | ServletException e) {
				
				e.printStackTrace();
			}
			System.out.println(parts.size());
			for (Part part : parts) {
				System.out.println("part[" + part.getName() + "]");
				System.out.println(part.getHeader("content-disposition"));
				printStream(part.getInputStream());
				System.out.println("");
			}
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^");
		}
		else
		{
			BufferedReader reader = request.getReader();
			String str;
			while ((str = reader.readLine()) != null)
			{
				System.out.println(str);
			}
			System.out.println("^^^^^^^url-----------");
		}
	}
	
	private void printStream(InputStream in)
	{
		try {
			byte[] bytes = new byte[in.available()];
			in.read(bytes, 0, in.available());
			System.out.println(new String(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
