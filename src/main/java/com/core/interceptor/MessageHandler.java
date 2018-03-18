package com.core.interceptor;

import com.sso.client.SessionUtils;
import com.sso.redis.IRpcUserRedisService;
import com.sso.rpc.RpcUser;
import com.web.runtimeCourse.service.impl.CourseEngine;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
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
	
	@Autowired
	private CourseEngine courseEngine;
	
    private static final Hashtable<String, WebSocketSession> socketUserTable = new Hashtable<String, WebSocketSession>();

    /**
     * 
     * @Title: afterConnectionEstablished 
     * @Description: 与客户端连接成功时候，会触发页面上onopen方法  
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
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
        //从redis中取用户
    	//user = rpcUserRedisService.getUserById(user.getID());
        if(user != null ) {
            socketUserTable.put(user.getId(), session);
            System.out.println("用户 " + user.getNickName() + " 已进入课堂");
            
            /*
            //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
            TextMessage returnMessage = new TextMessage("你将收到的离线");
            session.sendMessage(returnMessage);
            */
            /*
            logger.debug("DEBUG: 用户 " + user.getNickName() + " 已进入课堂");
            logger.info("INFO: 用户 " + user.getNickName() + " 已进入课堂");
            logger.warn("INFO: 用户 " + user.getNickName() + " 已进入课堂");
            logger.error("ERROR: 用户 " + user.getNickName() + " 已进入课堂");
            */
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
        //System.out.println("afterConnectionClosed");
        RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
        socketUserTable.remove(user.getId());
        //rpcUserRedisService.delUser(user.getID());
        
        System.out.println("用户 "+user.getNickName()+"已退出！");
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
    public void handleTransportError(WebSocketSession session, Throwable exception){
    	try {
	    	System.out.println("websocket connection closed......");
	        if(session.isOpen()){
	            session.close();
	        }
	        socketUserTable.remove(session);
    	} catch (Exception e) {
            e.printStackTrace();
        }
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
    public void handleTextMessage(WebSocketSession session, TextMessage message){
    	try {
	    	System.out.println(message.getPayload());
	    	
	    	JSONObject studyObj = new JSONObject(message.getPayload());
	    	JSONObject sendMsg = courseEngine.getStudyTasks(studyObj);
			
	    	sendCourseData(session, studyObj, sendMsg);
			//sendMessageToUser(user.getID(), sendMsg);
    	} catch (Exception e) {
    		logger.error("ERROR: handleTextMessage时出现错误！");
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Title: sendCourseData 
     * @Description: 将课程数据推送给学生
     * @author 张龙龙
     * @date 2018年3月18日 下午2:14:39
     * @param @param session
     * @param @param studyObj
     * @param @param sendMsg     
     * @return void     
     * @throws 
     */
    public void sendCourseData(WebSocketSession session, JSONObject studyObj, JSONObject sendMsg){
    	RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
    	//未组织课程
    	if(!studyObj.has("courseOrgId") || StringUtils.isBlank(studyObj.getString("courseOrgId"))){
    		sendMessageToUser(user.getId(), sendMsg);
    	}else{//组织课程
    		if("end".equals(sendMsg.getString("courseStatus"))){
    			JSONObject sendObj = new JSONObject();
    			sendObj.put("infoMsg", "恭喜，您的课程学习完了!");
    			sendMessageToGroup(studyObj.getString("groupId"), sendObj);
    		}else{
    			if(sendMsg.has("errorMsg") || sendMsg.has("infoMsg")){
    				sendMessageToUser(user.getId(), sendMsg);
    			}else{
        			sendMessageToUser(user.getId(), sendMsg);
        			
        			if(studyObj.has("taskId") && StringUtils.isNotBlank(studyObj.getString("taskId"))){
        				JSONObject sendObj = new JSONObject();
        				if(sendMsg.has("studyMsg")){
        					sendObj.put("infoMsg", studyObj.getString("userName") + "完成了本任务！");
        				}else{
        					sendObj.put("taskMsg", "你有新任务了！");
        				}
        				sendMessageToRole(user.getId(), studyObj.getString("roleCid"), sendObj);
        			}
        		}
    		} 
    	}
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
    private void sendMessage(WebSocketSession session, JSONObject sendMsg) {
        try {
            TextMessage textMessage =  new TextMessage(sendMsg.toString());
            if(session.isOpen()){
                session.sendMessage(textMessage);
            }
        } catch (IOException e) {
        	logger.error("ERROR: sendMessage时出现错误！");
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
    public void sendMessageToUser(String userId, JSONObject sendMsg)  {
        WebSocketSession session = socketUserTable.get(userId);
        if(session !=null){
        	/*
        	JSONObject ret = new JSONObject();
            ret.put("msg","欢迎进入课堂！");
            //可以有4种值：success,info,warning,danger
            ret.put("style","info");
            */
        	/*
        	RpcUser user = (RpcUser)session.getAttributes().get(SessionUtils.SESSION_USER);
        	
        	JSONObject ret = new JSONObject();
        	ret.put("id", user.getID());
            ret.put("msg",  "欢迎 " + user.getNICKNAME()+ "上线!");
            
            ReturnCode returnCode = new ReturnCode(ReturnCode.SUCCESS, "欢迎 " + user.getNICKNAME()+"上线!", ret);
            */
            sendMessage(session, sendMsg);
            //sendMessageToUsers(userId, returnCode);
        }
    }
   
    /**
     * 
     * @Title: sendMessageToRole 
     * @Description: 给指定角色的人员发送消息
     * @author 张龙龙
     * @date 2018年3月18日 下午3:47:47
     * @param @param exceptUserId
     * @param @param roleCid
     * @param @param sendMsg     
     * @return void     
     * @throws 
     */
    public void sendMessageToRole(String exceptUserId, String roleCid, JSONObject sendMsg) {
        try {
        	TextMessage textMessage =  new TextMessage(sendMsg.toString());
        	
        	Iterator<Entry<String, WebSocketSession>> iter = socketUserTable.entrySet().iterator();
        	WebSocketSession wSocket = null;
        	RpcUser user = null;
        	while (iter.hasNext()) {
        		Entry<String, WebSocketSession> entry =  iter.next();
        		String key = entry.getKey();
        		wSocket = entry.getValue();
        		user = (RpcUser)wSocket.getAttributes().get(SessionUtils.SESSION_USER);
	        	if (wSocket.isOpen() && (StringUtils.isBlank(exceptUserId) || !key.equals(exceptUserId))) {
	        		if(StringUtils.isNotBlank(roleCid) && StringUtils.isNotBlank(user.getRoleCid()) && roleCid.equals(user.getRoleCid())){
	        			wSocket.sendMessage(textMessage);
	        		}
	            }
        	}
        	
        } catch (IOException e) {
        	logger.error("ERROR: sendMessageToRole时出现错误！");
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Title: sendMessageToGroup 
     * @Description: 给指定的小组发送消息
     * @author 张龙龙
     * @date 2018年3月18日 下午3:50:31
     * @param @param groupCid
     * @param @param sendMsg     
     * @return void     
     * @throws 
     */
    public void sendMessageToGroup(String groupCid, JSONObject sendMsg) {
        try {
        	TextMessage textMessage =  new TextMessage(sendMsg.toString());
        	
        	Iterator<Entry<String, WebSocketSession>> iter = socketUserTable.entrySet().iterator();
        	WebSocketSession wSocket = null;
        	RpcUser user = null;
        	while (iter.hasNext()) {
        		Entry<String, WebSocketSession> entry =  iter.next();
        		//String key = entry.getKey();
        		wSocket = entry.getValue();
        		user = (RpcUser)wSocket.getAttributes().get(SessionUtils.SESSION_USER);
	        	if (wSocket.isOpen() && StringUtils.isNotBlank(groupCid) && StringUtils.isNotBlank(user.getGroupId()) && groupCid.equals(user.getGroupId())){
	        			wSocket.sendMessage(textMessage);
	        	}
        	}
        	
        } catch (IOException e) {
        	logger.error("ERROR: sendMessageToGroup时出现错误！");
            e.printStackTrace();
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
    public void sendMessageToUsers(String exceptUserId, JSONObject sendMsg) {
        try {
        	TextMessage textMessage =  new TextMessage(sendMsg.toString());
        	
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
        	logger.error("ERROR: sendMessageToUsers时出现错误！");
            e.printStackTrace();
        }
    }

}
