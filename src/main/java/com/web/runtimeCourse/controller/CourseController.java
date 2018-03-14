package com.web.runtimeCourse.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.util.FileUtils;
import com.util.SysLog;
import com.web.runtimeCourse.service.impl.CourseEngine;
import com.yineng.dev_V_3_0.model.ActStudyVar;
import com.yineng.dev_V_3_0.model.CourseOrgStructure;
import com.yineng.dev_V_3_0.service.IActStudyVarService;
import com.yineng.dev_V_3_0.service.ICourseOrgStructureService;

@Controller
@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	private CourseEngine courseEngine;
	
	@Resource
	public ICourseOrgStructureService courseOrgStructureService;
	
	@Autowired
	private IActStudyVarService actStudyVarService;
	
	@RequestMapping(value="/version") 
    private @ResponseBody String version(){ 
		FileUtils.getFileRootDir(null);
		//TEST log
		String version = "system version:  2.1.0,  update time: 2016-12-15 16:33:20";
		JSONObject destData = new JSONObject();
		destData.put("userName", "zhangll");
		destData.put("password", "123");
		
		SysLog.info("zhangll_id", destData.toString(), version);
		SysLog.error("zhangll_id", destData.toString(), version);
		
        return version;
    }
	
	@RequestMapping(value="/{username}/{password}") //用来处理前台的login请求  
    private @ResponseBody String hello(@PathVariable("username") String username, @PathVariable("password") String password){  
		JSONObject destData = new JSONObject();
		destData.put("userName", username);
		destData.put("password", password);
		
		destData.put("jsonO", new JSONObject("{\"test\": \"data\"}"));
		Object o1 = destData.get("userName");
		Object o2 = destData.get("jsonO");
		if(o1 instanceof String){
			System.out.println("o1 的类型为String.");
		}else{
			System.out.println("o1 的类型不为String.");
		}
		
		if(o2 instanceof String){
			System.out.println("o2 的类型为String.");
		}else{
			System.out.println("o2 的类型不为String.");
		}
		
		if(o2 instanceof JSONObject){
			System.out.println("o1 的类型为JSONObject.");
		}else{
			System.out.println("o1 的类型不为JSONObject.");
		}
		
        return "Hello "+username+",Your password is: "+password;  
          
    }
	
	@RequestMapping(value="/testVar") //用来处理前台的login请求  
    private @ResponseBody String testVar(){  
		System.out.println("123");
		/*
		ActStudyVar actStudyVar = new ActStudyVar(null, "output_test", "test_value", "zhangll", "zhanglonglong", 
					"123", "任务", "EC_test", "456");
		int insertR = this.actStudyVarService.insertSelective(actStudyVar);
		System.out.println(insertR);
		*/
		/*
		ActStudyVar actStudyVar = new ActStudyVar("32229280-9c88-4b66-9a8d-c0d800687d01", "test_newValue");
		int updateR = this.actStudyVarService.updateByPrimaryKeySelective(actStudyVar);
		System.out.println(updateR);
		System.out.println(actStudyVar.getVarId());
		
		ActStudyVar testVar = this.actStudyVarService.selectByPrimaryKey("32229280-9c88-4b66-9a8d-c0d800687d01");
		System.out.println(testVar);
		*/
		ActStudyVar tempVar = new ActStudyVar("output_test", "zhangll", "123", null, "456");
		List<ActStudyVar> actStudyVarList = this.actStudyVarService.selectActStudyVars(tempVar);
		System.out.println(actStudyVarList);
		
        return "123";  
          
    }
	
	/**
	 * 获取用户在一个已组织的课程里的组信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getUserGroupInfo")
	public @ResponseBody void getUserGroupInfo(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String userId = request.getParameter("userId");
		String lrnscnOrgId = request.getParameter("courseOrgId");
		
		SysLog.info(userId, null, "getUserGroupInfo request: userId="+userId+", lrnscnOrgId="+lrnscnOrgId);
		//System.out.println("-------------------------getUserGroupInfo:---------------------------------------");
		List<CourseOrgStructure> infoList = courseOrgStructureService.getUserGroupOnfo(userId, lrnscnOrgId); 
		
		JSONObject groupInfo = new JSONObject();
		if(infoList.size() > 0){
			groupInfo.put("groupId", infoList.get(0).getParentId());
			groupInfo.put("roleCid", infoList.get(0).getOrgStructureId());
			groupInfo.put("roleId", infoList.get(0).getContextId());
			groupInfo.put("roleName", infoList.get(0).getContextDes());
			groupInfo.put("members", infoList.get(0).getCounts());
			groupInfo.put("teacherId", infoList.get(0).getTeacherId());
			groupInfo.put("teacherName", infoList.get(0).getTeacherName());
			groupInfo.put("orgUserId", infoList.get(0).getOrgUserId());
			groupInfo.put("orgUserName", infoList.get(0).getOrgUserName());
			groupInfo.put("processDefinitionId", infoList.get(0).getProcessDefinitionId());
			
			SysLog.info(userId , "{\"userId\":"+userId+", \"lrnscnOrgId\":"+lrnscnOrgId+"}", "getUserGroupInfo response : "+ groupInfo.toString());
		}else{
			groupInfo.put("errorMsg", "时间："+CourseEngine.getDateStr(null)+" <br />错误：查询不到您所在小组的信息！");
			
			SysLog.error(userId , "{\"userId\":"+userId+", \"lrnscnOrgId\":"+lrnscnOrgId+"}", "getUserGroupInfo response 错误：查询不到您所在小组的信息！");
		}
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write(groupInfo.toString());
		} catch (IOException e) {
			//System.out.println("获取用户组信息响应失败!");
			
			SysLog.error(userId , "{\"userId\":"+userId+", \"lrnscnOrgId\":"+lrnscnOrgId+"}", "getUserGroupInfo response 错误：获取用户组信息响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	/**
	 * 响应外部ajax请求
	 * @param receivebyte
	 * 发布和学习课程时的请求与响应数据结构：
	 * JSONObject deployCourseRequestData = {
     *       courseInstanceId : "",
     *       bpmnInstanceId : "" ,
     *       ecgeditorHost : ""
     *   };
     * JSONObject deployCourseResponseData = {
     *       courseInstanceId : "",
     *       processDefinitionId : "",
     *       errorMsg : "" //出错的时候会有
     *   };
     **/   
	@RequestMapping(value="/deploy")  
    public @ResponseBody void deploy(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject deployObj = new JSONObject();
		deployObj.put("ecgeditorHost", request.getParameter("ecgeditorHost"));
		deployObj.put("bpmnInstanceId", request.getParameter("bpmnInstanceId"));//GET、POST都是这样取参数
		deployObj.put("courseInstanceId", request.getParameter("courseInstanceId"));
		
		deployObj.put("isCooperation", request.getParameter("isCooperation"));//是否是组织课程
		//System.out.println(deployObj.toString());
		
		SysLog.info(null, null, "deploy request: "+deployObj.toString());
        String sendMsg = this.courseEngine.deployCourse(deployObj, false);
        SysLog.info(null, null, "deploy response: "+sendMsg);
        //System.out.println("[deploy result]:" + sendMsg);
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			//System.out.println("deploy响应失败!");
			SysLog.error(null, deployObj.toString(), "deploy response 错误：deploy响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	@RequestMapping(value="/deploy_test")  
    public @ResponseBody void deploy_test(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject deployObj = new JSONObject();
		deployObj.put("ecgeditorHost", request.getParameter("ecgeditorHost"));
		deployObj.put("bpmnInstanceId", request.getParameter("bpmnInstanceId"));//GET、POST都是这样取参数
		deployObj.put("courseInstanceId", request.getParameter("courseInstanceId"));
		deployObj.put("isCooperation", request.getParameter("isCooperation"));//是否是组织课程
		System.out.println(deployObj.toString());
        
        String sendMsg = this.courseEngine.deployCourse(deployObj, true);
        //System.out.println("[deploy result]:" + sendMsg);
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			System.out.println("deploy响应失败!");
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
		
	/**
	 * 响应外部http请求
	 * JSONObject studyRequestData = {
     *      processDefinitionId : "",
     *      userName:"",
     *      
     *      OCPath:"",//不再需要
     *      
     *      courseInstanceId : "",
     *      isSingle: 1, //1:每人独立学习课程 , 0:多人协作学习课程   ------------------》新增
     *      
     *      isCooperation:true,//true:课程组织起来学习 , false:课程不组织，独立完成课程
     *          
     *      //isCooperation == true时     
     *      courseOrgId:"",//课程组织id
     *      members: 1, //组内成员人数
     *      groupId:"", //组id, 按组生成课程实例
     *      roleCid:"", //角色cid,唯一标识
     *      roleId:"", //角色id
     *      roleName:"",//角色名字
     *           
     *      assignee : "",//oc_课程学习者_id
     *      taskId : "",
     *      output : {
     *          
     *      }
     *   };
     * JSONObject studyResponseData = {
	 *   	courseStatus : "start",
     *      
     *      currSubTask:{subName:"活动",subId:"subId", basicInfo: "子流程任务"},
	 *   	currTask : {
	 *   		assignee : "zhangll",
	 *   		taskInfo : {
	 *   			taskId : "taskId",
	 *   			taskDefKey : "taskDefKey",
	 *   			taskName : "",
	 *   			data : {
	 *   				name1 : "value1",
	 *   				name2 : "value2"
	 *   			}
	 *   		}
	 *   	}
	 *   
	 *   	errorMsg : "" //出错的时候会有
	 *   }
	 * 
	 */
	@RequestMapping(value="/study", method = RequestMethod.POST, headers="Content-Type=application/json;charset=UTF-8", produces = {"application/json;charset=UTF-8"})  
    public @ResponseBody String study(@RequestBody String studyStr, HttpServletRequest request){
        //System.out.println("时间："+CourseEngine.getDateStr(null)+", receive study request.");
        //System.out.println(studyStr);
        SysLog.info(null, null, "study request: "+studyStr);
        
		JSONObject studyObj = new JSONObject(studyStr);
		String sendMsg = this.courseEngine.getStudyTasks(request.getServletContext(), studyObj);
		//System.out.println("时间："+CourseEngine.getDateStr(null)+", response to study request.");
		SysLog.info(null, null, "study response: "+sendMsg);
		
        return sendMsg;
    }
	
	@RequestMapping(value="/study_test")  
    public @ResponseBody void study_test(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String studyReqData = request.getParameter("studyReqData");
		JSONObject studyObj = new JSONObject(studyReqData);
        
		String sendMsg = this.courseEngine.getStudyTasks(request.getServletContext(), studyObj);
		System.out.println(sendMsg.toString());
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			System.out.println("study响应失败!");
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/**
	 *
	  submitDataRequest = 
	  submitData :"{
				  		taskId : "",
				  		assignee : "",
				  		submitType : "file", //"form", "question"
				  
				  		fileInfo : {
				  			filePath:"", fileName:"", optType:"", userId:"", userName:""
				  		},
				  
				  		formInfo : {
				  
				  		},
				  
				  		question : {
				  
				  		}
				  }", //json字符串
	  
	  submitDataResponse = 
	   {
	  		submitResult : true,/false
	  
	  		errorMsg : ""
	  
	  }
	 */
	@RequestMapping(value="/submitData")  
    public @ResponseBody void submitData(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String submitDataStr = request.getParameter("submitData");//json字符串
		JSONObject submitDataObj = new JSONObject(submitDataStr);
        
		String sendMsg = this.courseEngine.submitData(submitDataObj);
		System.out.println(sendMsg.toString());
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			System.out.println("submitData响应失败!");
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/**
	 * requestData = {
	 * 		procInstId : "317834",
	 * 		userId : "138_user14"
	 * }
	 * 
	 * responseData = {
	 *  //评估项
	 *	estimationItems:[
	 *				{
	 *					 taskId: "123",
	 *					 taskName: "任务活动",
	 *					 procInstId: "456",
	 *					 //评分规则
	 *					 scoreRules: [{
	 *					 		id: 1, //编号， 1，2，3...
	 *					  		name: "教学内容科学性", //评分规则的名字， "界面设计"， "教学内容科学性"
	 *					  		type: "peopleScore", //评分类型， "isSubmited"作业是否提交， "examScore"试卷得分， "peopleScore"教师/组员评分
	 *					  		score: 3, //分值
	 *					  		direction: "这是一段评分标准，正确得3分...",
	 *					  		myScore: 3 //我的得分，需要判断myScore存在否
	 *					  	}],
	 *					 //目标
	 *					 target: {
	 *						  		filePath:"", 
	 *						  		fileName:"", 
	 *						  		optType:"", 
	 *						  		userId:"", 
	 *						  		userName:"", 
	 *						  		createTime: "xxx"
	 *						  		
	 *						  		//or
	 *						  		errorMsg:"获取不到待评估的文件！"
	 *					  		}
	 *				 }
	 *			],
	 *	//总结，summarize可能是个空json		
	 *	summarize: {
	 *		sumScore: 88,
	 *		comment: "做的不错！"
	 *		
	 *		//or
	 *		errorMsg:"找不到总评信息！"
	 *	}
	 * }
	 * 
	 * 
	 * 获取我的课程评估信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMyEstimation")  
    public @ResponseBody void getMyEstimation(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject reqObj = new JSONObject();
		reqObj.put("procInstId", request.getParameter("procInstId"));
		reqObj.put("userId", request.getParameter("userId"));//GET、POST都是这样取参数
		
		SysLog.info(null, null, "getMyEstimation request: "+reqObj.toString());
		JSONObject sendMsg = this.courseEngine.getMyEstimation(reqObj);
        SysLog.info(null, null, "getMyEstimation response: "+sendMsg.toString());
        
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg.toString());
		} catch (IOException e) {
			SysLog.error(null, reqObj.toString(), "getMyEstimation response 错误：getMyEstimation(获取我的课程评估信息)响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/**
	 * 获取我的课程评估项信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMyEstimationItems")  
    public @ResponseBody void getMyEstimationItems(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject reqObj = new JSONObject();
		reqObj.put("procInstId", request.getParameter("procInstId"));
		reqObj.put("userId", request.getParameter("userId"));//GET、POST都是这样取参数
		
		SysLog.info(null, null, "getMyEstimationItems request: "+reqObj.toString());
        JSONArray sendMsg = this.courseEngine.getMyEstimationItems(reqObj);
        SysLog.info(null, null, "getMyEstimationItems response: "+sendMsg);
        
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg.toString());
		} catch (IOException e) {
			SysLog.error(null, reqObj.toString(), "getMyEstimationItems response 错误：getMyEstimationItems(获取我的评估项)响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/**
	 * 获取我的课程总评信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMySummarize")  
    public @ResponseBody void getMySummarize(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject reqObj = new JSONObject();
		reqObj.put("procInstId", request.getParameter("procInstId"));
		reqObj.put("userId", request.getParameter("userId"));//GET、POST都是这样取参数
		
		SysLog.info(null, null, "getMySummarize request: "+reqObj.toString());
        JSONObject sendMsg = this.courseEngine.getMySummarize(reqObj);
        SysLog.info(null, null, "getMySummarize response: "+sendMsg.toString());
        
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg.toString());
		} catch (IOException e) {
			SysLog.error(null, reqObj.toString(), "getMySummarize response 错误：getMySummarize(获取我的课程总评信息)响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/**
	 * 为评估项设置得分
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/setScoreToEstimationItem")  
    public @ResponseBody void setScoreToEstimationItem(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject reqObj = new JSONObject();
		reqObj.put("procInstId", request.getParameter("procInstId"));
		reqObj.put("taskId", request.getParameter("taskId"));//GET、POST都是这样取参数
		reqObj.put("scoreId", request.getParameter("scoreId"));
		reqObj.put("myScore", request.getParameter("myScore"));
		
		//System.out.println("时间："+CourseEngine.getDateStr(null)+", setScoreToEstimationItem: "+reqObj.toString());
		SysLog.info(null, null, "setScoreToEstimationItem request: "+reqObj.toString());
        String sendMsg = this.courseEngine.setScoreToEstimationItem(reqObj);
        SysLog.info(null, null, "setScoreToEstimationItem response: "+sendMsg);
        
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			//System.out.println("时间："+CourseEngine.getDateStr(null)+", 错误：setScoreToEstimationItem(为评估项设置得分)响应失败!");
			SysLog.error(null, reqObj.toString(), "setScoreToEstimationItem response 错误：setScoreToEstimationItem(为评估项设置得分)响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/**
	 * 设置总评
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/setSummarize")  
    public @ResponseBody void setSummarize(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		JSONObject reqObj = new JSONObject();
		reqObj.put("procInstId", request.getParameter("procInstId"));
		reqObj.put("userId", request.getParameter("userId"));//GET、POST都是这样取参数
		reqObj.put("sumScore", request.getParameter("sumScore"));
		reqObj.put("comment", request.getParameter("comment"));
		
		//System.out.println("时间："+CourseEngine.getDateStr(null)+", setSummarize: "+reqObj.toString());
		SysLog.info(null, null, "setSummarize request: "+reqObj.toString());
        String sendMsg = this.courseEngine.setSummarize(reqObj);
        SysLog.info(null, null,"setSummarize response: "+sendMsg);
        
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			//System.out.println("时间："+CourseEngine.getDateStr(null)+", 错误：setSummarize(设置总评)响应失败!");
			SysLog.error(null, reqObj.toString(), "setSummarize response 错误：setSummarize(设置总评)响应失败!");
			//e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/*
	@RequestMapping(value="/submitData", method = RequestMethod.POST, headers="Content-Type=application/json;charset=UTF-8", produces = {"application/json;charset=UTF-8"})  
    public @ResponseBody String study_new(@RequestBody String submitData){
        System.out.println("receive submitData request.");
		JSONObject studyObj = new JSONObject(submitData);
		
		String sendMsg = this.courseEngine.submitData(studyObj);
		
        return sendMsg;
    }
    */
	
	@RequestMapping(value="/getFile")  
    public @ResponseBody void getFile(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String fileDataStr = request.getParameter("fileData");//json字符串
		JSONObject fileDataObj = new JSONObject(fileDataStr);
        
		String sendMsg = this.courseEngine.getFile(fileDataObj);
		//System.out.println(sendMsg.toString());
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(sendMsg);
		} catch (IOException e) {
			System.out.println("submitData响应失败!");
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
}