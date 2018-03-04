package com.sso.client;

import com.google.gson.Gson;
import com.util.ConfigUtils;
import com.util.ReturnCode;
import com.util.SpringUtils;
import com.sso.rpc.AuthenticationRpcService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @ClassName: ClientFilter 
 * @Description: 单点登录权限系统Filter基类
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:40:48 
 */
public abstract class ClientFilter implements Filter {

	// 管理系统单点登录服务端URL
	protected String ssoBackServerUrl;
	// 管理系统登录地址
	protected String backLoginUrl;
	// 管理系统单点登录服务端提供的RPC服务，由Spring容器注入
	protected AuthenticationRpcService authenticationRpcService;
	
	// 官网系统单点登录服务端URL
	protected String ssoWebServerUrl;
	// 官网系统登录地址
	protected String webLoginUrl;

	// 排除拦截
	protected List<String> excludeList = null;
	
	private PathMatcher pathMatcher = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (StringUtils.isBlank(ssoBackServerUrl = ConfigUtils.getProperty("sso.back.server.url"))) {
			throw new IllegalArgumentException("sso.back.server.url不能为空!");
		}
		if (StringUtils.isBlank(backLoginUrl = ConfigUtils.getProperty("sso.back.login_url"))) {
			throw new IllegalArgumentException("sso.back.login_url不能为空!");
		}
		if ((authenticationRpcService = SpringUtils.getBean(AuthenticationRpcService.class)) == null) {
			throw new IllegalArgumentException("authenticationRpcService注入失败!");
		}
		
		String excludes = filterConfig.getInitParameter("excludes");
		if (StringUtils.isNotBlank(excludes)) {
			excludeList = Arrays.asList(excludes.split(","));
			pathMatcher = new AntPathMatcher();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (matchExcludePath(httpRequest.getServletPath())){
			chain.doFilter(request, response);
		}else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			try {
				doFilter(httpRequest, httpResponse, chain);
			}catch (Exception e) {
				//e.printStackTrace();
				//System.out.println(e.getMessage());
				httpResponse.setContentType("application/json;charset=UTF-8");
				httpResponse.setStatus(HttpStatus.OK.value());
				PrintWriter writer = httpResponse.getWriter();
				ReturnCode returnCode = null;
				
				if("app".equals(e.getMessage())){
					
					returnCode = new ReturnCode(ReturnCode.LOGIN, "你还未登录,或登录超时！请登录系统.", null);
					
				}else if("web".equals(e.getMessage())){
					
					returnCode = new ReturnCode(ReturnCode.LOGIN, "你还未登录,或登录超时！请登录系统.", null);
					
				}else if("backstage".equals(e.getMessage())){//TODO 暂时处理, 管理系统还未兼容 ReturnCode.LOGIN 情况.
					
					returnCode = new ReturnCode(ReturnCode.FAIL, "你还未登录,或登录超时！请登录系统.", null);
					
				}else{
					
					returnCode = new ReturnCode(ReturnCode.FAIL, e.getMessage().split(";")[0], null);
					
				}
				
				writer.write(new Gson().toJson(returnCode));
				writer.flush();
				writer.close();
				/*
				String directUrl = e.getMessage();
				//告诉ajax我是重定向
				httpResponse.setHeader("REDIRECT", "REDIRECT");
	            //告诉ajax我重定向的路径
				httpResponse.setHeader("CONTENTPATH", directUrl);
				httpResponse.setStatus(HttpServletResponse.SC_FOUND);
				*/
			}
		}
	}
	
	private boolean matchExcludePath(String path) {
		if (excludeList != null) {
			for (String ignore : excludeList) {
				if (pathMatcher.match(ignore, path)) {
					return true;
				}
			}
		}
		return false;
	}

	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws Exception;

	@Override
	public void destroy() {
	}
}