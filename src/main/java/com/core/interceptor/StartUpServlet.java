package com.core.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartUpServlet extends HttpServlet{
	
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 724012962786034384L;
	
	private static final Logger log = LoggerFactory.getLogger(StartUpServlet.class);
    //应用根目录
    public static String APPLICATION_URL;
    public static ServletContext APPLICATION_SERVLET;
    
    @Override
    public void init() throws ServletException {
    	APPLICATION_SERVLET = getServletContext();
        APPLICATION_URL = getServletContext().getRealPath("/");
    }
    
}
