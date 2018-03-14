package com.core.interceptor;

import com.sso.client.SessionUtils;
import com.sso.redis.IRpcUserRedisService;
import com.sso.rpc.RpcUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 
 * @ClassName: HandShakeInterceptor 
 * @Description: 握手接口, WebSocket拦截器
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:39:42 
 */
public class HandShakeInterceptor extends HttpSessionHandshakeInterceptor {
	
	@Autowired
	IRpcUserRedisService rpcUserRedisService;
	
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        System.out.println("---- Before Handshake ----");
        //解决The extension [x-webkit-deflate-frame] is not supported问题  
        if(request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {  
            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");  
        }  
        if(request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
            
            /*
            //方法一, 登录用户信息直接存在session中
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                //使用userName区分WebSocketHandler，以便定向发送消息
                String userName = (String) session.getAttribute("SESSION_USERNAME");
                if (userName==null) {
                    userName="default-system";
                }
                attributes.put("WEBSOCKET_USERNAME",userName);
            }
            */
            
            //方法二, 登录用户信息从远程服务器获取后设置进session, 再缓存到redis
            RpcUser user = SessionUtils.setSessionUser_test(servletRequest.getServletRequest());
            if(user == null){
            	return false;
            }
            
            /*
            user.setNICKNAME(user.getNICKNAME() + "_redis");
            rpcUserRedisService.cacheUser(user);
            System.out.println("---- 缓存用户 "+user.getNICKNAME()+"到redis. ----");
            */
            
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {

        System.out.println("---- After Handshake ----");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}