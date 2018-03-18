package com.sso.client;

import com.alibaba.fastjson.JSONObject;
import com.util.Base64Util;
import com.util.CookieUtils;
import com.util.ReturnCode;
import com.util.SpringUtils;
import com.sso.rpc.AuthenticationRpcService;
import com.sso.rpc.RpcUser;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

/**
 * 
 * @ClassName: SessionUtils 
 * @Description: 当前已登录用户Session
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:41:05 
 */
public class SessionUtils {
	
	/**
	 * 用户信息
	 */
	public static final String SESSION_USER = "login_user";
	
	public static final String BACKSTAGE_URL_SOURCE = "backstage"; //来自管理系统的请求
	public static final String WEB_URL_SOURCE = "web"; //来自官网系统的请求
	public static final String APP_URL_SOURCE = "app"; //来自APP端的请求
	public static final String GROUP_URL_SOURCE = "group"; //来自集团的请求
	public static final String URL_KEY = "ignoreMe"; //白名单请求, 放行
	
	// 管理系统单点登录服务端提供的RPC服务，由Spring容器注入
	protected static AuthenticationRpcService authenticationRpcService;
	
	public static ReturnCode getSessionUser(HttpServletRequest request) {
		ReturnCode returncode = null;
		String urlKey = request.getHeader("UrlKey");
		//System.out.println(request.getHeader("UrlKey"));
	    
		if(urlKey != null && URL_KEY.equals(Base64Util.decode(urlKey + "U="))){
			returncode = new ReturnCode(ReturnCode.SUCCESS, null , null);
			return returncode;
		}
		String requestURI = request.getRequestURI().substring(10).toLowerCase();
		if(requestURI.startsWith(BACKSTAGE_URL_SOURCE) || requestURI.startsWith("/" + BACKSTAGE_URL_SOURCE)){
			returncode = SessionUtils.getBackstageSessionUser(request);
		}else if(requestURI.startsWith(WEB_URL_SOURCE)  || requestURI.startsWith("/" + WEB_URL_SOURCE)){
			//returncode = SessionUtils.getWebSessionUser(request);
		}else if(requestURI.startsWith(APP_URL_SOURCE)  || requestURI.startsWith("/" + APP_URL_SOURCE)){
			//returncode = SessionUtils.getAppSessionUser(request);
		}else if(requestURI.startsWith(GROUP_URL_SOURCE)  || requestURI.startsWith("/" + GROUP_URL_SOURCE)){
			//returncode = SessionUtils.getWebSessionUser(request);
			returncode = new ReturnCode(ReturnCode.SUCCESS, null , null);
		}
		
		return returncode;
	}
	
	/**
	 * 
	 * @Title: getBackstageSessionUser 
	 * @Description: 获取后台管理登录用户信息
	 * @author longlong zhang
	 * @param @param request
	 * @param @return     
	 * @return RpcUser     
	 * @throws 
	 */
	public static ReturnCode getBackstageSessionUser(HttpServletRequest request) {
		ReturnCode returnCode = null;
		JSONObject ret = new JSONObject();
		ret.put("sys", BACKSTAGE_URL_SOURCE);
		
		if(authenticationRpcService == null){
			authenticationRpcService = SpringUtils.getBean(AuthenticationRpcService.class);
		}
		String token = CookieUtils.getCookie(request, "token");
		if (token != null) {
			System.out.println("URL:" + request.getRequestURI());
			System.out.println("TOKEN:" + token);
			
			RpcUser rpcUser = authenticationRpcService.findAuthInfo(token);
			if(rpcUser != null){
				ret.put("user", rpcUser);
				returnCode = new ReturnCode(ReturnCode.SUCCESS, null, ret);
				
				return returnCode;
			}
		}
		
		returnCode = new ReturnCode(ReturnCode.FAIL, null, ret);
		
		return returnCode;
	}

	/**
	 * 
	 * @Title: setSessionUser 
	 * @Description: 登录用户设置进session
	 * @author 张龙龙
	 * @date 2018年3月3日 下午6:24:51
	 * @param @param request     
	 * @return void     
	 * @throws 
	 */
	public static boolean setSessionUser(HttpServletRequest request) {
		ReturnCode code = getSessionUser(request);
		if(code.getCode() == 1){
			JSONObject jsonUser = (JSONObject)code.getData();
			WebUtils.setSessionAttribute(request, SESSION_USER, jsonUser.get("user"));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: setSessionUser_test 
	 * @Description: 测试登录用户
	 * @author 张龙龙
	 * @date 2018年3月3日 下午6:17:52
	 * @param @param request     
	 * @return void     
	 * @throws 
	 */
	public static RpcUser setSessionUser_test(HttpServletRequest request) {
		String userId = request.getParameter("userId"), 
				roleCid = request.getParameter("roleCid"), 
        		groupId = request.getParameter("groupId");
		
		RpcUser user = new RpcUser();
		user.setId(userId);
        user.setNickName(request.getParameter("userName"));
        if(StringUtils.isNotBlank(roleCid)){
        	user.setRoleCid(roleCid);
        }
        if(StringUtils.isNotBlank(groupId)){
        	user.setGroupId(groupId);
        }
        
		WebUtils.setSessionAttribute(request, SESSION_USER, user);
		return user;
	}

}