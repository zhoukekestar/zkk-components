package com.share.mod.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContext implements ServletContextListener {

	//private Logger logger = Logger.getLogger(ServletContext.class);
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		/*sce.getServletContext().setAttribute("ws", MobileConfig.SERVICE_REMOTE);
		sce.getServletContext().setAttribute("base", MobileConfig.BASE);
		sce.getServletContext().setAttribute("ts", MobileConfig.SERVICE_LOCAL);
		*/
		//logger.info("ServletContext init: ws->" + MobileConfig.SERVICE_REMOTE + "  base->" + MobileConfig.BASE + " ts->" + MobileConfig.SERVICE_LOCAL);
		
		sce.getServletContext().getSessionCookieConfig().setName("j_session");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//logger.info("ServletContext destory.");
	}

}
