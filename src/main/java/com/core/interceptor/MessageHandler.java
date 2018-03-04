package com.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sso.client.SessionUtils;
import com.sso.redis.IRpcUserRedisService;
import com.sso.rpc.RpcUser;
import com.util.ReturnCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 
 * @ClassName: MessageHandler 
 * @Description: websocket处理类, 该对象提供了客户端连接,关闭,错误,发送等方法,重写这几个方法即可实现自定义socket业务逻辑
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:40:11 
 */
public class MessageHandler extends TextWebSocketHandler {

	private static Logger logger = Logger.getLogger(MessageHandler.class);
	 
	@Autowired
	IRpcUserRedisService rpcUserRedisService;
	
    private static final Hashtable<String, WebSocketSession> socketUserTable = new Hashtable<String, WebSocketSession>();

    /**
     * 
     * @Title: afterConnectionEstablished 
     * @Description: 与客户端连接成功时候，会触发页面上onopen方法  
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
    	/*
        System.out.println("afterConnectionEstablished");
        System.out.println("getId:" + session.getId());
        System.out.println("getLocalAddress:" + session.getLocalAddress().toString());
        System.out.println("getTextMessageSizeLimit:" + session.getTextMessageSizeLimit());
        System.out.println("getUri:" + session.getUri().toString());
        System.out.println("getPrincipal:" + session.getPrincipal());
        */
    	//从session中获取用户
        RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
    	user = rpcUserRedisService.getUserById(user.getID());
        if(user != null ) {
            socketUserTable.put(user.getID(), session);
            System.out.println("用户 " + user.getNICKNAME() + " 已上线");
            
            /*
            //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
            TextMessage returnMessage = new TextMessage("你将收到的离线");
            session.sendMessage(returnMessage);
            */
            
            logger.debug("DEBUG: 用户 " + user.getNICKNAME() + " 已上线");
            logger.info("INFO: 用户 " + user.getNICKNAME() + " 已上线");
            logger.warn("INFO: 用户 " + user.getNICKNAME() + " 已上线");
            logger.error("ERROR: 用户 " + user.getNICKNAME() + " 已上线");
        }
    }

    /**
     * 
     * @Title: afterConnectionClosed 
     * @Description: 客户端连接断开时时触发  
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    	
        //System.out.println("afterConnectionClosed");
        RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
        socketUserTable.remove(user.getID());
        rpcUserRedisService.delUser(user.getID());
        
        System.out.println("用户 "+user.getNICKNAME()+"已退出！");
        System.out.println("剩余在线用户"+socketUserTable.size());
    }
    
    /**
     * 
     * @Title: handleTransportError 
     * @Description: 消息传输出错时调用  
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    	System.out.println("websocket connection closed......");
        if(session.isOpen()){
            session.close();
        }
        socketUserTable.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 
     * @Title: handleTextMessage 
     * @Description: (js调用websocket.send时)接收到客户端消息时调用  
     * @param session
     * @param message
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
    	
    	RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
        System.out.println("收到 " + user.getNICKNAME() + "发送的消息:" + message.getPayload());
        
        sendMessageToUser(user.getID());
        
    }
    
    /**
     * 
     * @Title: sendMessage 
     * @Description: 给指定 session 推送消息
     * @author 张龙龙
     * @date 2018年3月3日 下午4:29:17
     * @param @param session
     * @param @param jsonObject  消息内容   
     * @return void     
     * @throws 
     */
    private void sendMessage(WebSocketSession session, ReturnCode returnCode) {
        try {
            TextMessage textMessage =  new TextMessage(new Gson().toJson(returnCode).getBytes("UTF-8"));
            if(session.isOpen()){
                session.sendMessage(textMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Title: sendWorkunitMessage 
     * @Description: 给指定用户发送消息
     * @author 张龙龙
     * @date 2018年3月3日 下午4:29:42
     * @param @param userId     
     * @return void     
     * @throws 
     */
    public void sendMessageToUser(String userId)  {
        WebSocketSession session = socketUserTable.get(userId);
        if(session !=null){
        	/*
        	JSONObject ret = new JSONObject();
            ret.put("msg","欢迎进入课堂！");
            //可以有4种值：success,info,warning,danger
            ret.put("style","info");
            */
        	RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
        	
        	JSONObject ret = new JSONObject();
        	ret.put("id", user.getID());
            ret.put("msg",  "欢迎 " + user.getNICKNAME()+ "上线!");
            
            ReturnCode returnCode = new ReturnCode(ReturnCode.SUCCESS, "欢迎 " + user.getNICKNAME()+"上线!", ret);
            
            sendMessage(session, returnCode);
            sendMessageToUsers(userId, returnCode);
        }
    }
   
    /**
     * 
     * @Title: sendMessageToUsers 
     * @Description: 给除exceptUserId外的所有在线用户发送消息
     * @author 张龙龙
     * @date 2018年3月3日 下午5:50:01
     * @param @param exceptUserId
     * @param @param returnCode     
     * @return void     
     * @throws 
     */
    public void sendMessageToUsers(String exceptUserId, ReturnCode returnCode) {
        try {
        	TextMessage textMessage =  new TextMessage(new Gson().toJson(returnCode).getBytes("UTF-8"));
        	
        	Iterator<Entry<String, WebSocketSession>> iter = socketUserTable.entrySet().iterator();
        	while (iter.hasNext()) {
        		Entry<String, WebSocketSession> entry =  iter.next();
        		String key = entry.getKey();
        		WebSocketSession val = entry.getValue();
	        	if (val.isOpen() && (exceptUserId == null || !key.equals(exceptUserId))) {
	        		val.sendMessage(textMessage);
	            }
        	}
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
