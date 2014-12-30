package com.share.mod.common.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil 
{
	private static final String classPath;
	
	static{
		classPath = Thread.currentThread().getContextClassLoader().getResource(".").toString();
	}
	
	public static List<Class<?>> scanPacket(){
		return scanPacket("");
	}
	
	/***
	 * 扫描包名称,如com.apache
	 * @param packetName
	 * @return
	 */
	public static List<Class<?>> scanPacket(String packetName){
		
		List<Class<?>> classes = new ArrayList<>();
		try {
			scan(classPath + packetName.replaceAll("\\.", "/"), packetName, classes);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	private static void scan(String path, String packetName, List<Class<?>> classes) throws URISyntaxException{
		URI uri = new URI(path);
		File file = new File(uri);
		if (file.isDirectory() && file.exists())
		{
			for (File f : file.listFiles()) {
				if (f.isDirectory()){					
					if (packetName == "")
						scan(path + "/" + f.getName(), f.getName(), classes);
					else
						scan(path + "/" + f.getName(), packetName + "." + f.getName(), classes);
				}
				
				Class<?> cla = null;
				try {
					String name = f.getName();
					if (!name.endsWith(".class")) continue;
					if (name.indexOf("$") != -1) continue;
					if (name.indexOf(".") != 0)
						name = name.substring(0, name.indexOf("."));

					cla = Class.forName(packetName + "." + name);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				classes.add(cla);
			}
		}
	}
	
	/***
	 * 
	 * @param cla 被检测的类
	 * @param clb 接口
	 * @return
	 */
	public static boolean hasImpl(Class<?> cla, Class<?> clb)
	{
		if (clb.isInterface())
		{
			for (Class<?> c : cla.getInterfaces()) {
				if (c.equals(clb))
					return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getInterfaceImpl(Class<T> clb)
	{
		List<Class<?>> list = ClassUtil.scanPacket();
		for (Class<?> class1 : list) {
			if (hasImpl(class1, clb))
				return (Class<T>)class1;
		}
		return null;
	}
}
