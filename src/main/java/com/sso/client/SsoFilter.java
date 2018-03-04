package com.sso.client;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.util.ReturnCode;

/**
 * 
 * @ClassName: SsoFilter 
 * @Description: 单点登录及Token验证Filter
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:41:22 
 */
public class SsoFilter extends ClientFilter {

	public static final String BACKSTAGE_URL_SOURCE = "backstage"; //来自管理系统的请求
	public static final String WEB_URL_SOURCE = "web"; //来自官网系统的请求
	public static final String APP_URL_SOURCE = "app"; //来自APP端的请求
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception {
		ReturnCode returncode = SessionUtils.getSessionUser(request);
		if(returncode.getCode() == 1){
			// 再跳转一次当前URL，以便去掉URL中token参数
			chain.doFilter(request, response);
		}else{
			redirectLogin(request, response, returncode);
		}
	}

	/**
	 * 跳转登录
	 *
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	private void redirectLogin(HttpServletRequest request, HttpServletResponse response, ReturnCode returnCode) throws Exception {
		JSONObject rData = (JSONObject)returnCode.getData();
		String directUrl = "/index.html";
		if(BACKSTAGE_URL_SOURCE.equals(rData.getString("sys"))){
			directUrl = backLoginUrl;
			//TODO 暂时处理, 管理系统还未兼容 ReturnCode.LOGIN 情况.
			if(request.getContentType() == null){
				response.sendRedirect(directUrl);
			}else{
				throw new Exception(BACKSTAGE_URL_SOURCE);
			}
		}else if(WEB_URL_SOURCE.equals(rData.getString("sys"))){
			directUrl =  webLoginUrl;
			
			throw new Exception(WEB_URL_SOURCE);
		}else if(APP_URL_SOURCE.equals(rData.getString("sys"))){
			//directUrl =  webLoginUrl;
			
			throw new Exception(APP_URL_SOURCE);
		}
		
	}

	/**
	 * 是否Ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}
}