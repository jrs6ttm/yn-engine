package com.web.runtimeCourse.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.impl.util.io.StreamSource;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
//import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.util.FileUtils;
import com.util.SysLog;
import com.web.fileManager.entity.ActOwnFile;
import com.web.fileManager.entity.ActStudyFile;
import com.web.fileManager.service.IActOwnFileService;
import com.web.fileManager.service.IActStudyFileService;
import com.web.formManager.entity.ActForm;
import com.web.formManager.entity.ActFormRun;
import com.web.formManager.service.IActFormRunService;
import com.web.formManager.service.IActFormService;
import com.yineng.dev_V_3_0.model.ActStudyComment;
import com.yineng.dev_V_3_0.model.ActStudyVar;
import com.yineng.dev_V_3_0.model.CourseOrg;
import com.yineng.dev_V_3_0.model.CourseOrgStructure;
import com.yineng.dev_V_3_0.model.CourseOrgUser;
import com.yineng.dev_V_3_0.service.IActStudyCommentService;
import com.yineng.dev_V_3_0.service.IActStudyVarService;
import com.yineng.dev_V_3_0.service.ICourseOrgService;
import com.yineng.dev_V_3_0.service.ICourseOrgStructureService;
import com.yineng.dev_V_3_0.service.ICourseOrgUserService;

/**
 * 
 * 业务引擎操作
 * @author zhangLL
 *
 */
@Service("courseEngine")
public class CourseEngine {	

  @Autowired
  private RuntimeService runtimeService;
  
  @Autowired
  private TaskService taskService;
  
  @Autowired
  private RepositoryService repositoryService;
  
  @Autowired
  private IdentityService  identityService;
  
  @Autowired
  private HistoryService historyService;
  
  @Autowired
  private FormService formService;
  
  @Resource
  public ICourseOrgService courseOrgService;
  
  @Resource
  public ICourseOrgStructureService courseOrgStructureService;
  
  @Resource
  private ICourseOrgUserService courseOrgUserService;
  
  @Autowired
  private IActOwnFileService actOwnFileService;
	
  @Autowired
  private IActStudyFileService actStudyFileService;
  
  @Autowired
  private IActStudyCommentService actStudyCommentService;
  
  @Autowired
  private IActStudyVarService actStudyVarService;
  
  @Autowired
  private IActFormService actFormService;
  
  @Autowired
  private IActFormRunService actFormRunService;
  
  private String USERNAME = "";//, OCPath="";
  private ServletContext SERVLET_CONTEXT = null;
  /**
   * true:课程组织起来学习 , false:课程不组织，独立完成课程
   */
  private boolean ISCOOPERATION = true;
  
  /**
   * ISCOOPERATION = true时有意义
   * true:每人独立学习课程 , false:多人协作学习课程
   */
  private boolean ISSINGLE = false;
  
  /**
   * http请求次数 
   */
  private static int CONNECT_TIMES = 0;

  public static CloseableHttpClient getHttpClient(){
	//创建默认的httpClient实例.    
	CloseableHttpClient httpClient = HttpClients.createDefault();
	
	return httpClient;
  }
  
  public static void closeHttpClient(CloseableHttpClient httpClient){
	  try {
			 httpClient.close();
	  } catch (IOException ex) {
			ex.printStackTrace();
	 }
  }
  
  /**
   * http post请求 
   * @param bpmnInstanceId
   * @return
   */
  public static String POST(String path, Map<String, String> params){  
	  String resStr = "";
	  CloseableHttpClient httpClient = CourseEngine.getHttpClient();
	  
      try {  
    	// 创建httppost    
          HttpPost httppost = new HttpPost(path); 
          // 创建参数队列    
          List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
          for(String key:params.keySet()){
        	  formparams.add(new BasicNameValuePair(key, params.get(key))); 
          }
          UrlEncodedFormEntity uefEntity;
          uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
          httppost.setEntity(uefEntity);  
          //System.out.println("POST: executing request " + httppost.getURI());  
          SysLog.info(null, null, "POST: executing request " + httppost.getURI());
          
          CloseableHttpResponse response = httpClient.execute(httppost);  
          try {  
              HttpEntity entity = response.getEntity();  
              if (entity != null) {  
            	  resStr = EntityUtils.toString(entity, "UTF-8"); 
              }  
          } finally {  
              response.close(); 
              CourseEngine.closeHttpClient(httpClient);
          } 
      } catch (ClientProtocolException e) {
    	  
    	  resStr = "HttpClient:客户端连接协议错误";
    	  CourseEngine.closeHttpClient(httpClient);
    	  
	  } catch (UnsupportedEncodingException e) {
		  
		  resStr = "HttpClient:不支持的编码集";
		  CourseEngine.closeHttpClient(httpClient);
		  
	  } catch (IOException e) {
		  CourseEngine.closeHttpClient(httpClient);
		  
		  CONNECT_TIMES ++;
		  if(CONNECT_TIMES <= 3){
			  //System.out.println("时间："+CourseEngine.getDateStr(null)+"第"+CONNECT_TIMES+"次请求OC服务创建文件实例。");
			  SysLog.info(null, null, "时间："+CourseEngine.getDateStr(null)+", 第"+CONNECT_TIMES+"次请求OC服务创建文件实例。");
			  
			  resStr = CourseEngine.POST(path, params);
		  }else{
			  resStr = "OC服务没有响应，您可以刷新网页试试";
		  }
	  }
      
      return resStr;
  }  

  /**
   * http get请求
   * @param bpmnInstanceId
   * @return
   */
  public static String GET(String path) {
      String xml = "";
      CloseableHttpClient httpClient = CourseEngine.getHttpClient();
      try {  
          // 创建httpget    
          HttpGet httpget = new HttpGet(path);  
          //System.out.println("executing request " + path);
          //System.out.println("executing request " + httpget.getURI());  
          String errorMsg = "时间："+CourseEngine.getDateStr(null)+", executing request : " + httpget.getURI();
		  SysLog.info(null, errorMsg, errorMsg);
          // 执行get请求.    
          CloseableHttpResponse response = httpClient.execute(httpget);  
          try {  
              // 获取响应实体    
              HttpEntity entity = response.getEntity(); 
              // 打印响应状态    
              System.out.println(response.getStatusLine());  
              if (entity != null) {
            	  xml = EntityUtils.toString(entity, "UTF-8");
              }  
              
          } finally {  
              response.close();  
          }  
      } /*catch (ClientProtocolException e) {  
          e.printStackTrace();  
      } catch (ParseException e) {  
          e.printStackTrace();  
      } catch (IOException e) {  
          e.printStackTrace();  
      } */catch (Exception e) {
    	  System.out.println("请求课程文件出错："+path);
          e.printStackTrace(); 
          
      }finally{
    	  CourseEngine.closeHttpClient(httpClient);
      } 
      
      return xml;
  }
  
  /**
   * 
   * @param bpmnInstanceId
   * @return
   */
  private  BufferedInputStream getResource(String bpmnXml, boolean isTest){
	  //String xml = this.getCourseFile("get", bpmnInstanceId), 
	  String  strName = "TempCourseFile.bpmn",
			  rootPath = this.getClass().getResource("/").getFile().toString();
	          //classPath = this.getClass().getClassLoader().getResource("").getPath();
	  
	  	if(!"".equals(bpmnXml)){
			try {
				if(isTest){
					strName = bpmnXml;
				}else{
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
			                new FileOutputStream(new File(rootPath, strName)), "UTF-8")); 
					writer.write(bpmnXml);
					writer.flush();
					writer.close();
				}
				
		  		File tempXml = new File(rootPath, strName);
		  		if(!tempXml.exists()){
		  			System.out.println("------------------ 要部署的资源文件【"+rootPath+strName+"】不存在！------------------ ");
					return null;
		  		}
		  		
		  		BufferedInputStream input = new BufferedInputStream(new FileInputStream(tempXml));
		  		
		  		return input;
		  		
			} catch (IOException e) {
				e.printStackTrace();
				
				return null;
			}
	  		
	  	}else{
	  		return null;
	  	}
  }
  
  public static String getDateStr(Date date){
	  if(date == null){
		 date = new Date();
	  }
	  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	  return sf.format(date);
	}
  
  
 public void validateBpmnXml(String bpmnXml, InputStream sourceIn){
	 ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
	 ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
	 try {
		 StreamSource xmlSource = null;
		 if(bpmnXml != null){
			 InputStream in = new ByteArrayInputStream(bpmnXml.getBytes("utf-8"));
			 xmlSource = new InputStreamSource(in);
		 }else{
			 xmlSource = new InputStreamSource(sourceIn);
		 }
		
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlSource, false, false, "UTF-8");
		if(bpmnModel.getErrors().size() > 0){
			System.out.println("error:");
			Map<String, String> errorMap = bpmnModel.getErrors();
			for(String key:errorMap.keySet()){
				System.out.println(key+" : "+errorMap.get(key));
			}
		}
		//bpmnModel.get
		//验证失败信息的封装ValidationError
		List<ValidationError>  validateList = defaultProcessValidator.validate(bpmnModel);
		for(ValidationError valiError:validateList){
			System.out.println(valiError.getProcessDefinitionId());
			System.out.println(valiError.getProcessDefinitionName());
			System.out.println(valiError.getActivityId());
			System.out.println(valiError.getActivityName());
			System.out.println(valiError.getXmlColumnNumber());
			System.out.println(valiError.getXmlLineNumber());
			System.out.println(valiError.getProblem());
			System.out.println("----------------------------------------------");
		}
		
	 } catch (Exception e) {
		e.printStackTrace();
	 }  
	 
 }
  
  
  /**
   * 部署process资源
   * @param processDefinitionKey
   */
  public synchronized String deployCourse(JSONObject deployObj, boolean isTest) {
	   JSONObject deployResultObj = new JSONObject();
	   String bpmnInstanceId = deployObj.getString("bpmnInstanceId"),
			  courseInstanceId = deployObj.getString("courseInstanceId"), bpmnXml = "", dId = "",
			  isCooperation = deployObj.getString("isCooperation");
	   
	   deployResultObj.put("courseInstanceId", courseInstanceId);
	   
	   //System.out.println("------------------ Deploy course[courseInstanceId: "+courseInstanceId+"] :------------------ ");
	   List<Deployment> ds = null;
	   try{
		    ds = repositoryService.createDeploymentQuery().deploymentName(courseInstanceId).list();
	   }catch(Exception e){
		   deployResultObj.put("processDefinitionId", "");
		   String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：查询部署课程失败！<br />原因：【"+e.getMessage()+"】";
		   deployResultObj.put("errorMsg", errorMsg);
		   
		   //System.out.println(errorMsg);
		   SysLog.error(null, deployObj.toString(), errorMsg);
		   
		   return deployResultObj.toString();
	   }
	   if(ds != null && ds.size() > 0){
		   //System.out.println("------------------ Course[courseInstanceId: "+courseInstanceId+"] is exists.------------------ ");
		   
		   dId = ds.get(0).getId();
	   }else{
		   if(isTest){
			   bpmnXml = "MyFormFlow.bpmn";
		   }else{
			   //bpmnXml = CourseEngine.GET("http://"+deployObj.getString("ecgeditorHost")+"/load/loadStandardXml?instanceId="+bpmnInstanceId+"&source=egn");
			   try{
				   bpmnXml = CourseEngine.GET("http://"+deployObj.getString("ecgeditorHost")+"/load/loadStandardXml?instanceId="+bpmnInstanceId+"&isCooperation="+isCooperation+"&source=egn");
			   }catch(Exception e){
				   deployResultObj.put("processDefinitionId", "");
				   String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：请求部署的课程文件失败！<br />原因：【"+e.getMessage()+"】";
				   deployResultObj.put("errorMsg", errorMsg);
				   
				   //System.out.println(errorMsg);
				   SysLog.error(null, deployObj.toString(), errorMsg);
				   
				   return deployResultObj.toString();
			   }
			   if(!bpmnXml.startsWith("<?xml")){
				   deployResultObj.put("processDefinitionId", "");
				   String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：部署课程失败！<br />原因：获取的课程文件有错误:"+bpmnXml;
				   deployResultObj.put("errorMsg", errorMsg);
				   
				   //System.out.println(errorMsg);
				   SysLog.error(null, deployObj.toString(), errorMsg);
				   
				   return deployResultObj.toString();
			   }
		   }
		   
		   if(!"".equals(bpmnXml)){
			   if(isTest){//测试用，直接读本地文件
				   BufferedInputStream inputStream = this.getResource(bpmnXml, isTest);
				   //this.validateBpmnXml(null, inputStream);
				   if(inputStream != null){
					   try{
							Deployment d = repositoryService.createDeployment()
															 .name(courseInstanceId)
															 .addInputStream("TempCourseFile.bpmn", inputStream)
															 .deploy();
							
							dId = d.getId();
					   } catch (Exception e) {
							//e.printStackTrace();
						   deployResultObj.put("processDefinitionId", "");
						   String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：部署课程失败！<br />原因：课程文件有错误【"+e.getMessage()+"】";
						   deployResultObj.put("errorMsg", errorMsg);
						   
						   //System.out.println(errorMsg);
						   SysLog.error(null, deployObj.toString(), errorMsg);
						   
						   return deployResultObj.toString();
					  }
					}
			   }else{
				   //this.validateBpmnXml(bpmnXml, null);
				   try{
					   Deployment d = repositoryService.createDeployment()
								 .name(courseInstanceId)
								 .addString(bpmnInstanceId+".bpmn", bpmnXml)
								 .deploy();
		
					   dId = d.getId();
				   } catch (Exception e) {
						//e.printStackTrace();
					   deployResultObj.put("processDefinitionId", "");
					   String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：部署课程失败！<br>原因：课程文件有错误【"+e.getMessage()+"】";
					   deployResultObj.put("errorMsg", errorMsg);
					   
					   //System.out.println(errorMsg);
					   SysLog.error(null, deployObj.toString(), errorMsg);
					   
					   return deployResultObj.toString();
				  }
			   }
		   }
	   }
	   
	   List<ProcessDefinition> pds1 = repositoryService.createProcessDefinitionQuery()
														.deploymentId(dId)
														.list();
	   
		if(pds1.size() > 0){
			deployResultObj.put("processDefinitionId", pds1.get(0).getId());
		}else{
			deployResultObj.put("processDefinitionId", "");
			String errorMsg = "时间： "+CourseEngine.getDateStr(null)+" <br />错误：【deploy】创建课程定义失败！";
			deployResultObj.put("errorMsg", errorMsg);
			
			//System.out.println(errorMsg);
			SysLog.error(null, deployObj.toString(), errorMsg);
		}
	   
	   return deployResultObj.toString();
  }
  
  /**
   * 
   * @param processDefinitionId
   * @param courseInstanceId
   * @param roleCid 角色唯一id
   * @param groupId 组id
   * @param courseOrgId 课程组织id
   * @param assignee
   * @return
   */
  public JSONObject runCourse(String processDefinitionId, String courseInstanceId, String roleCid, String groupId, String courseOrgId, String assignee){
	  JSONObject runResultObj = new JSONObject();
	  String procInsBusinessKey = "";
	  if(!ISSINGLE && ISCOOPERATION){//多人协作，并组织起来学习时  按组生成实例
		  procInsBusinessKey = groupId;
	  }else{
		  procInsBusinessKey = courseInstanceId;//不组织  或  组织后独立学习时   每个人生成单独实例
	  }
	  List<ProcessInstance> pis = runtimeService.createProcessInstanceQuery()
												.processDefinitionId(processDefinitionId)
												.processInstanceBusinessKey(procInsBusinessKey)
												.list(); 
	  if(pis.size() > 0){
		   runResultObj.put("processInstanceId", pis.get(0).getProcessInstanceId());
	  }else{
		  long count = historyService.createHistoricProcessInstanceQuery()
									  .processDefinitionId(processDefinitionId)
									  .processInstanceBusinessKey(procInsBusinessKey)
									  .count();
		  if(count > 0){
			  runResultObj.put("end", "时间："+CourseEngine.getDateStr(null)+", 课程【procInsBusinessKey: "+procInsBusinessKey+"】已经结束了。");
			  //System.out.println("时间："+CourseEngine.getDateStr(null)+",  课程【procInsBusinessKey: "+procInsBusinessKey+"】已经结束了。");
			  SysLog.info(null, null, "时间："+CourseEngine.getDateStr(null)+",  课程【procInsBusinessKey: "+procInsBusinessKey+"】已经结束了。");
		  }else{
			  List<ProcessDefinition> pds1 = repositoryService.createProcessDefinitionQuery()
																.processDefinitionId(processDefinitionId)
																.list();
	
			if(pds1.size() > 0){
				ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinitionId, procInsBusinessKey);
				if(ISCOOPERATION){
					if(!ISSINGLE){
						runtimeService.setVariable(pi.getId(), "groupId", groupId);
					}else{
						runtimeService.setVariable(pi.getId(), "roleCid", roleCid);
					}
					runtimeService.setVariable(pi.getId(), "courseOrgId", courseOrgId);
					
					//更改课程状态
					CourseOrg c = new CourseOrg();
					c.setLrnscnOrgId(courseOrgId);
					c.setStatus("2");
					c.setLstupddate(CourseEngine.getDateStr(null));
					c.setLstupdid(assignee);
					int result1 = courseOrgService.updateByPrimaryKeySelective(c);
					//System.out.println("更新课程状态, 结果:"+result1);
					SysLog.info(assignee, null, "更新课程状态, 结果:"+result1);
					
					//登记人员的过程实例
					CourseOrgUser cUser = new CourseOrgUser();
					cUser.setLrnscnOrgId(courseOrgId);
					cUser.setUserId(assignee);
					cUser.setProcInstId(pi.getId());
					cUser.setLstupddate(CourseEngine.getDateStr(null));
					cUser.setLstupdid(assignee);
					int result2 = courseOrgUserService.updateByPrimaryKeySelective(cUser);
					//System.out.println("登记人员的过程实例, 结果:"+result2);
					SysLog.info(assignee, null, "登记人员的过程实例, 结果:"+result2);
				}
				runResultObj.put("processInstanceId", pi.getProcessInstanceId());
				
				//创建虚拟机
				
			}else{
				String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：找不到课程的流程定义【定义id :"+processDefinitionId+"】!";
				runResultObj.put("errorMsg", errorMsg);
				
				//System.out.println(errorMsg);
				SysLog.error(assignee, null, errorMsg);
			}
		  }
	  }
	  
	  return runResultObj;
  }
 
  //no using
  public JSONObject getExtensionElementsOfTask(UserTask userTask, String taskId, String assignee){
	  JSONObject taskData = new JSONObject(), taskInfo = new JSONObject();
      List<ExtensionElement> lstElement = userTask.getExtensionElements().get("properties").get(0).getChildElements().get("property");
	  //List<ExtensionElement> lstElement = userTask.getExtensionElements().get(strExtendTag);
      if (lstElement == null || lstElement.size() <= 0){
    	  //System.out.println("任务["+userTask.getName()+"]没有包含"+strExtendTag+"的扩展属性！");
    	  System.out.println("任务【"+userTask.getName()+"】没有包含扩展属性！");
      	  return taskData;
      }
 	  
      /*//似乎没意义，任务上要么没人（单人课程），要么就是assignee的（多人课程）
	  String taskAssignee = userTask.getAssignee();
	  if(taskAssignee != null && !"".equals(taskAssignee)){
		  if(!(assignee.trim()).equals(taskAssignee)){
			  return taskData;
		  }
	  }
	  */
	  
 	  taskInfo.put("taskId", taskId);
 	  taskInfo.put("taskDefinKey", userTask.getId());
 	  taskInfo.put("taskName", userTask.getName());
 	  taskData.put("assignee", assignee);
      
 	 JSONObject dictRetValue = new JSONObject();
      for(ExtensionElement elmtTmp : lstElement){
    	  /*
    	  for(Entry<String , List<ExtensionAttribute>> objDDDDD : elmtTmp.getAttributes().entrySet()){
  	  		if (objDDDDD == null ||objDDDDD.getKey() == null || objDDDDD.getValue() == null){
  	  			continue;
  	  		}
  	  		
  	  		for(ExtensionAttribute objExtAttr:objDDDDD.getValue()){
  	  			dictRetValue.put(objExtAttr.getName(), objExtAttr.getValue());
  			}
  	  	  }
  	  	  */
    	  
    	  String fieldName = elmtTmp.getAttributes().get("name").get(0).getValue();
    	  String fieldValue = elmtTmp.getAttributes().get("value").get(0).getValue();
    	  dictRetValue.put(fieldName, fieldValue);
      }
      taskInfo.put("data", dictRetValue);
      taskData.put("taskInfo", taskInfo);
      
	  return taskData;
  }
  
  /**
   * 获取扩展属性
   * @param lstElement
   * @return
   */
  public JSONObject getExtensionElements(List<ExtensionElement> lstElement){
	  JSONObject dictRetValue = new JSONObject();
      if (lstElement == null || lstElement.size() <= 0){
      	  return dictRetValue;
      }
 	  
      /*//似乎没意义，任务上要么没人（单人课程），要么就是assignee的（多人课程）
	  String taskAssignee = userTask.getAssignee();
	  if(taskAssignee != null && !"".equals(taskAssignee)){
		  if(!(assignee.trim()).equals(taskAssignee)){
			  return taskData;
		  }
	  }
	  */
 	 
      for(ExtensionElement elmtTmp : lstElement){
    	  /*
    	  for(Entry<String , List<ExtensionAttribute>> objDDDDD : elmtTmp.getAttributes().entrySet()){
  	  		if (objDDDDD == null ||objDDDDD.getKey() == null || objDDDDD.getValue() == null){
  	  			continue;
  	  		}
  	  		
  	  		for(ExtensionAttribute objExtAttr:objDDDDD.getValue()){
  	  			dictRetValue.put(objExtAttr.getName(), objExtAttr.getValue());
  			}
  	  	  }
  	  	  */
    	  
    	  String fieldName = elmtTmp.getAttributes().get("name").get(0).getValue();
    	  String fieldValue = elmtTmp.getAttributes().get("value").get(0).getValue();
    	  dictRetValue.put(fieldName, fieldValue);
      }
      
	  return dictRetValue;
  }
  
  /**
   * 获取本任务的业务数据
   * @param processDefinitionId
   * @param taskDefinekey
   * @param subProcesskey
   * @param taskId
   * @param assignee
   * @return
   */
  public JSONObject getCourseDatasOfTask(String processDefinitionId, String taskDefinekey, String subProcesskey, String taskId, String assignee){
	    BpmnModel objProcessModel =	repositoryService.getBpmnModel(processDefinitionId);
		Process  obhModelProcess = objProcessModel.getMainProcess();
		JSONObject taskData = new JSONObject(), taskInfo = new JSONObject();
		
		if(subProcesskey == null){//不是子流程
			//JSONObject dictObject = this.getExtensionElementsOfTask(objTaskElmt, taskId, assignee, "courseProperty");
			UserTask objTaskElmt = (UserTask)obhModelProcess.getFlowElement(taskDefinekey);
			taskData.put("assignee", assignee);
			taskInfo.put("taskId", taskId);
		 	taskInfo.put("taskDefinKey", objTaskElmt.getId());
		 	if(ISCOOPERATION){
		 		taskInfo.put("taskName", objTaskElmt.getName().split("@")[0]);
		 	}else{
		 		taskInfo.put("taskName", objTaskElmt.getName());
		 	}
		 	
		 	//List<ExtensionElement> lstElement = userTask.getExtensionElements().get(strExtendTag);
		 	List<ExtensionElement> lstElement = objTaskElmt.getExtensionElements().get("properties").get(0).getChildElements().get("property");
			JSONObject dictRetValue = this.getExtensionElements(lstElement);
			taskInfo.put("data", dictRetValue);
		    taskData.put("taskInfo", taskInfo);
			return taskData;
		}else{
			SubProcess subProcess = (SubProcess) obhModelProcess.getFlowElement(subProcesskey);
			if(taskDefinekey == null){//获取子流程的扩展属性
				JSONObject subProcessObj = new JSONObject(subProcess.getDocumentation());
				subProcessObj.put("subId", subProcess.getId());
				subProcessObj.put("subName", subProcess.getName());
				/*这样貌似图部署不成功，启动不起来
				List<ExtensionElement> lstElement2 = subProcess.getExtensionElements().get("properties").get(0).getChildElements().get("property");
				JSONObject dictObject2 = this.getExtensionElements(lstElement2);
				dictObject2.put("subId", subProcess.getId());
				dictObject2.put("subName", subProcess.getName());
				*/
				/*
				UserTask subFlow = (UserTask)obhModelProcess.getFlowElement(subProcesskey);
				JSONObject subProcessObj = this.getExtensionElementsOfTask(subFlow, taskId, assignee);
				*/
				//System.out.println(String.format("子流程解析结果为:%s", subProcessObj.toString()));
				
				return subProcessObj;
				
			}else{//获取子流程下taskDefinekey的扩展属性
				UserTask objTaskElmt1 = (UserTask)subProcess.getFlowElement(taskDefinekey);
				taskData.put("assignee", assignee);
				taskInfo.put("taskId", taskId);
			 	taskInfo.put("taskDefinKey", objTaskElmt1.getId());
			 	//taskInfo.put("taskName", objTaskElmt1.getName());
			 	if(ISCOOPERATION){
			 		taskInfo.put("taskName", objTaskElmt1.getName().split("@")[0]);
			 	}else{
			 		taskInfo.put("taskName", objTaskElmt1.getName());
			 	}
			 	
			 	//List<ExtensionElement> lstElement = userTask.getExtensionElements().get(strExtendTag);
			 	List<ExtensionElement> lstElement1 = objTaskElmt1.getExtensionElements().get("properties").get(0).getChildElements().get("property");
				JSONObject dictObject1 = this.getExtensionElements(lstElement1);
				taskInfo.put("data", dictObject1);
			    taskData.put("taskInfo", taskInfo);
				
				return taskData;
			}
		}
  }
  
  /**
   * 获取所有activity
   * @param processDefinitionId
   * @return
   */
  public List<ActivityImpl> getAllActivities(String processDefinitionId){
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
	  
	    return activitiList;
	}
	
  /**
   * 获取目标activity
   * @param activityId
   * @param processDefinitionId
   * @param type
   * @return
   */
	public ActivityImpl getDestActivityImpl(String activityId, String processDefinitionId, String type){
		  ActivityImpl activity = null, subProcess = null;
		  
		  List<ActivityImpl> activitiList = this.getAllActivities(processDefinitionId);
		  //获取任务所属的子流程
		  for (ActivityImpl activityImpl : activitiList){
			String id = activityImpl.getId();
			if(id.equals(activityId)){
				activity = activityImpl;
				break;
			}
			if("subProcess".equals(activityImpl.getProperty("type"))){
				ActivityImpl destactivity = activityImpl.findActivity(activityId);
				if(destactivity != null){
					activity = destactivity;
					subProcess = activityImpl;
					break;
				}
			}
		  }
		  
		  if("self".equals(type))
			  return activity;
		  else
			  return subProcess;
	}
	
	/**
	 * 获取业务变量
	 * @param varType
	 * @param userId
	 * @param taskId
	 * @param taskDefinekey
	 * @param processInstanceId
	 * @return
	 */
	public List<ActStudyVar> getBusinessVar(String varType, String userId, String taskId, String taskDefinekey, String processInstanceId){
		 
		  ActStudyVar tempVar = new ActStudyVar(varType, userId, taskId, taskDefinekey, processInstanceId);
		  List<ActStudyVar> actStudyVarList = this.actStudyVarService.selectActStudyVars(tempVar);
		  
		  return actStudyVarList;
   }
  
   /**
   * 保存业务变量
   * @param varType ： "output", "scoreRules", "summarize"
   * @param varValue
   * @param task
   */
   public int saveBusinessVar(String varType, String varValue, Task task){
	  //保存输出到业务表
	  ActStudyVar actStudyVar = new ActStudyVar(null, varType, varValue, task.getAssignee(), USERNAME, 
			  									task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
	  int insertR = this.actStudyVarService.insertSelective(actStudyVar);
	  if(insertR <=0){
		  SysLog.error(task.getAssignee(), actStudyVar.toString(), "抱歉，保存业务变量信息失败！");
	  }
	  
	  return insertR;
   }
   
   /**
    * 获取学习过程中产生的文件
    * @param fileid
    * @param filename
    * @param filetype
    * @param userId
    * @param taskid
    * @param taskdefinekey
    * @param processinstanceid
    * @return
    */
	public List<ActStudyFile> getStudyFile(String fileid, String filename, String filetype, String userId, String taskid, String taskdefinekey, String processinstanceid){
	  ActStudyFile tempFile = new ActStudyFile(fileid, filename, filetype, userId, taskid, taskdefinekey, processinstanceid);
	  List<ActStudyFile> actStudyFileList = this.actStudyFileService.selectActStudyFile(tempFile);
	  
	  return actStudyFileList;
    }
 
  /**
  * 保存学习过程中产生的文件
  * @param varType ： "output", "scoreRules", "summarize", "commentNum"
  * @param varValue
  * @param task
  * 
  * no using
  * 
  */
  public int saveStudyFile(String varType, String varValue, Task task){
	  //保存输出到业务表
	  ActStudyVar actStudyVar = new ActStudyVar(null, varType, varValue, task.getAssignee(), USERNAME, 
			  									task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
	  int insertR = this.actStudyVarService.insertSelective(actStudyVar);
	  if(insertR <=0){
		  SysLog.error(task.getAssignee(), actStudyVar.toString(), "抱歉，保存任务输出信息失败！");
	  }
	  System.out.println("保存任务输出信息结果：" + insertR);
	  
	  return insertR;
  }
  
  /**
   * 获取学习过程中我对文档的评论
   * @param fileId
   * @param commenterId
   * @param ownerId
   * @param taskId
   * @param taskDefinekey
   * @param processInstanceId
   * @return
   */
   public List<ActStudyComment> getStudyComments(String fileId, String commenterId, String ownerId, String taskId, String taskDefinekey, String processInstanceId){
	  ActStudyComment tempComment = new ActStudyComment(fileId, commenterId, ownerId, taskId, taskDefinekey, processInstanceId);
	  List<ActStudyComment> actStudyCommentList = this.actStudyCommentService.selectActStudyComments(tempComment);
	  
	  return actStudyCommentList;
    }
	  
   /**
    * 保存学习过程中我对文档的评论
    * @param cId
    * @param fileId
    * @param comments
    * @param ownerId
    * @param ownerName
    * @param task
    * @return
    */
   public int saveStudyComment(String cId, String fileId, String fileName, String comments,  String ownerId, String ownerName, Task task){
	  //保存输出到业务表
	  ActStudyComment actStudyC = new ActStudyComment(cId, fileId, task.getAssignee(), USERNAME, comments,  ownerId, ownerName, 
	    											task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
	  int insertR = this.actStudyCommentService.insertSelective(actStudyC);
	  if(insertR <=0){
		  SysLog.error(task.getAssignee(), actStudyC.toString(), "抱歉，保存我对文档的评论信息失败！");
	  }
	  System.out.println("保存我对文档的评论信息结果：" + insertR);
	  
	  return insertR;
   }
   
   /**
    * 表单数据流转到目标表单
    * @param sourceformRun
    * @param destformRun
    * @return
    */
   public ActFormRun installForm(ActFormRun sourceformRun, ActFormRun destformRun){
	   String flowAttrs = sourceformRun.getFlowAttrs();
	   if(flowAttrs != null && !"".endsWith(flowAttrs)){
		   JSONArray flowAttrArr = new JSONArray(flowAttrs);
		   Document doc = Jsoup.parse("<div id=\"installForm\">"+destformRun.getFormHtml()+"</div>");
		   
		   for(int i=0;i<flowAttrArr.length();i++){
			   JSONObject tempFlowAttr = flowAttrArr.optJSONObject(i);
			   String  attrName = tempFlowAttr.getString("attrName"),
					   attrType = tempFlowAttr.getString("attrType"),
					   defaultValue = tempFlowAttr.getString("defaultValue");
			   if("string".equals(attrType)){
				   //doc.select("input[name='"+attrName+"']").val(defaultValue);
				   doc.select("input[name='"+attrName+"']").attr("value", defaultValue);
			   }else if("textArea".equals(attrType)){
				   //doc.select("textarea[name='"+attrName+"']").val(defaultValue);
				   doc.select("textarea[name='"+attrName+"']").html(defaultValue);
			   }else if("enum".equals(attrType)){
				   if(defaultValue != null && !"".equals(defaultValue)){
					   JSONObject dVObj = new JSONObject(defaultValue);
					   String dV = dVObj.getString("dValue");
					   
					   Iterator<Element> eles = doc.select("select[name='"+attrName+"'] option").iterator();
					   while(eles.hasNext()){
						   Element ele = eles.next();
						   if(dV.equals(ele.text())){
							   ele.attr("selected", "selected");
							   break;
						   }
					   }
				   }
			   }else{
				   System.out.println("待扩展的属性类型："+attrType+"，不能处理。");
			   }
		   }
		   
		   destformRun.setFormHtml(doc.select("#installForm").html());
	   }
	   
	   return destformRun;
   }
   
   /**
    * 创建表单实例
    * @param sourceData
    * @param task
    * @return
    */
   public JSONObject createFormInstance(JSONObject sourceData, Task task, String tooType){
	  JSONObject destData = new JSONObject();
	  ActStudyVar sourceStudyVar = null;
	  ActFormRun sourceformRun = null, destformRun = null;
	  
	  if(sourceData.has("materialSource")){
		  String actionId = sourceData.getJSONObject("materialSource").getString("actionId");
		  if(actionId == null || "".equals(actionId.trim())){
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输出表单属性来源的任务actionId为null或空！<br />原因：课程文件有错误，请检查此任务的输出表单属性来源的materialSource的数据格式的正确性。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
		  }else{
			  String sourceTaskDefKey = "EC"+actionId.trim();
			  
			  //不加userId搜索，考虑避免多角色时多份表单无法选择的逻辑问题
			  List<ActStudyVar> actStudyVarList = this.getBusinessVar("output", null, null, sourceTaskDefKey, task.getProcessInstanceId());
			  if(actStudyVarList.size() > 0){
				  sourceStudyVar = actStudyVarList.get(0);//默认取第一个
				  
				  JSONObject tOutput = new JSONObject(sourceStudyVar.getVarValue());
				  if(tOutput.getBoolean("requirement")){//需要流转
					  List<ActFormRun> formRunList = actFormRunService.getFormRunList(
									  new ActFormRun(null, null, null, null,
													  null, sourceTaskDefKey, null, task.getProcessInstanceId(),
													  null, null)
							  );
					  if(formRunList.size() > 0){
						  sourceformRun = formRunList.get(0);
					  }
				  }
			  }
		  }
	  }
	  
	  if(sourceData.has("template")){
		  Object templateObj = sourceData.get("template");
		  if(templateObj instanceof JSONObject){
			  JSONObject templateData = sourceData.getJSONObject("template");
			  ActForm actForm = actFormService.selectByPrimaryKey(templateData.getString("id"));
			  String newCid = UUID.randomUUID().toString(),
					  newFormHtml = actForm.getFormHtml();
			  
			  newFormHtml = newFormHtml.replace("form_"+actForm.getFormId(), "form_"+newCid);
			  destformRun = new ActFormRun(newCid, actForm.getFormId(), actForm.getFormName(), newFormHtml,
			    		task.getId(), task.getTaskDefinitionKey(), task.getName(), task.getProcessInstanceId(),
			    		task.getAssignee(), USERNAME);
			  
		  }else{
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 表单，但目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的输入表单模板数据格式【"+templateObj.toString()+"】错误！<br />原因：课程文件目标任务的数据有错误。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
		 }
	  }
	  
	  if("form".equals(tooType)){//表单
		  String inputWay = sourceData.getString("inputWay"); 
		  if("actionOutput".equals(inputWay)){//表单流转
			  if(sourceformRun != null){
				  String newCid = UUID.randomUUID().toString(),
						  newFormHtml = sourceformRun.getFormHtml();
				  
				  newFormHtml = newFormHtml.replace("form_"+sourceformRun.getcId(), "form_"+newCid);
				  destformRun = new ActFormRun(newCid, sourceformRun.getcId(), sourceformRun.getFormName(), newFormHtml,
				    		task.getId(), task.getTaskDefinitionKey(), task.getName(), task.getProcessInstanceId(),
				    		task.getAssignee(), USERNAME);
				  
				  int result2 = actFormRunService.insertSelective(destformRun);
				  System.out.println("创建表单实例结果："+result2+" , 类型："+inputWay);
			  }else{
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 流转表单[actionOutput]，但找不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的目标流转表单！<br />原因：请检查此任务的表单属性输出actionOutput的数据格式是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  destData.put("errorMsg", errorMsg);
			  }
		  }else if("template".equals(inputWay)){//表单模板
			  if(destformRun != null){
				  int result3 = actFormRunService.insertSelective(destformRun);
				  System.out.println("创建表单实例结果："+result3+" , 类型："+inputWay);
			  }else{
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 表单模板[template]，但找不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的目标表单模板！<br />原因：请检查此任务的输入表单模板template的数据格式是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  destData.put("errorMsg", errorMsg);
			  }
		  }else{
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：不识别的表单输入类型："+inputWay+"！<br />原因：请检查此任务的输入表单的操作类型是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
		  }
	  }else if("dynamicForm".equals(tooType)){//表单属性流转
		  if(destformRun != null && sourceformRun != null){//组装表单与流转的属性
			  
			  destformRun = this.installForm(sourceformRun, destformRun);
			  int result1 = this.actFormRunService.insertSelective(destformRun);
			  
			  System.out.println("创建表单实例结果："+result1+" , 类型：[dynamicForm]表单属性流转");
		  }else{
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 表单属性流转[dynamicForm]，但获取不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的目标表单模板或来源表单！<br />原因：请检查此任务的输入表单模板的数据格式是否正确，或者检查是否记录了目标表单或来源表单信息。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
		  }
	  }else{
		  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：不识别的表单工具类型："+tooType+"！<br />原因：请检查此任务的工具类型是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
		  SysLog.error(task.getAssignee(), null, errorMsg);
		  
		  destData.put("errorMsg", errorMsg);
	  }
	  
	  if(!destData.has("errorMsg")){
		  destData.put("formId", destformRun.getcId());
		  destData.put("formName", destformRun.getFormName());
		  destData.put("formHtml", destformRun.getFormHtml());
	  }
	  
	  return destData;
  }
	
   /**
    * 有变动，暂时不用了
    * @param sourceData
    * @param task
    * @return
    */
   public JSONObject createFormInstance_old(JSONObject sourceData, Task task){
		  JSONObject destData = new JSONObject();
		  ActStudyVar sourceStudyVar = null;
		  ActFormRun sourceformRun = null, destformRun = null;
		  
		  if(sourceData.has("materialSource")){
			  String actionId = sourceData.getJSONObject("materialSource").getString("actionId");
			  if(actionId == null || "".equals(actionId.trim())){
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输出表单属性来源的任务actionId为null或空！<br />原因：课程文件有错误，请检查此任务的输出表单属性来源的materialSource的数据格式的正确性。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  destData.put("errorMsg", errorMsg);
			  }else{
				  String sourceTaskDefKey = "EC"+actionId.trim();
				  
				  //不加userId搜索，考虑避免多角色时多份表单无法选择的逻辑问题
				  List<ActStudyVar> actStudyVarList = this.getBusinessVar("output", null, null, sourceTaskDefKey, task.getProcessInstanceId());
				  if(actStudyVarList.size() > 0){
					  sourceStudyVar = actStudyVarList.get(0);//默认取第一个
					  
					  JSONObject tOutput = new JSONObject(sourceStudyVar.getVarValue());
					  if(tOutput.getBoolean("requirement")){//需要流转
						  List<ActFormRun> formRunList = actFormRunService.getFormRunList(
										  new ActFormRun(null, null, null, null,
														  null, sourceTaskDefKey, null, task.getProcessInstanceId(),
														  null, null)
								  );
						  if(formRunList.size() > 0){
							  sourceformRun = formRunList.get(0);
						  }
					  }
				  }
			  }
		  }
		  
		  if(sourceData.has("template")){
			  Object templateObj = sourceData.get("template");
			  if(templateObj instanceof JSONObject){
				  JSONObject templateData = sourceData.getJSONObject("template");
				  ActForm actForm = actFormService.selectByPrimaryKey(templateData.getString("id"));
				  String newCid = UUID.randomUUID().toString(),
						  newFormHtml = actForm.getFormHtml();
				  
				  newFormHtml = newFormHtml.replace("form_"+actForm.getFormId(), "form_"+newCid);
				  
				  destformRun = new ActFormRun(newCid, actForm.getFormId(), actForm.getFormName(), newFormHtml,
				    		task.getId(), task.getTaskDefinitionKey(), task.getName(), task.getProcessInstanceId(),
				    		task.getAssignee(), USERNAME);
				  
			  }else{
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 表单，但目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的输入表单模板数据格式【"+templateObj.toString()+"】错误！<br />原因：课程文件目标任务的数据有错误。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  destData.put("errorMsg", errorMsg);
			 }
		  }
		  
		  String inputWay = sourceData.getString("inputWay"); 
		  if("actionOutput".equals(inputWay)){
			  if(sourceStudyVar != null){
				  JSONObject output = new JSONObject(sourceStudyVar.getVarValue());
				  if(output.getBoolean("requirement")){//需要流转
					  String useType = output.getString("type");
					  if("formAttr".equals(useType)){//表单属性流转
						  if(destformRun != null && sourceformRun != null){//组装表单与流转的属性
							  
							  destformRun = this.installForm(sourceformRun, destformRun);
							  
							  int result1 = actFormRunService.insertSelective(destformRun);
							  System.out.println("创建表单实例结果："+result1+" , 类型：表单属性流转");
						  }else{
							  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 流转表单属性，但找不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的输入表单属性来源，或找不到目标表单！<br />原因：请检查此任务的表单属性输出actionOutput的数据格式与输入表单模板template的数据格式是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
							  SysLog.error(task.getAssignee(), null, errorMsg);
							  
							  destData.put("errorMsg", errorMsg);
						  }
					  }else{//表单流转，获取要流转的表单
						  if(sourceformRun != null){
							  String newCid = UUID.randomUUID().toString(),
									  newFormHtml = sourceformRun.getFormHtml();
							  
							  newFormHtml = newFormHtml.replace("form_"+sourceformRun.getcId(), "form_"+newCid);
							  destformRun = new ActFormRun(newCid, sourceformRun.getcId(), sourceformRun.getFormName(), newFormHtml,
							    		task.getId(), task.getTaskDefinitionKey(), task.getName(), task.getProcessInstanceId(),
							    		task.getAssignee(), USERNAME);
							  
							  int result2 = actFormRunService.insertSelective(destformRun);
							  System.out.println("创建表单实例结果："+result2+" , 类型："+inputWay);
						  }else{
							  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 流转表单，但找不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的目标流转表单！<br />原因：请检查此任务的表单属性输出actionOutput的数据格式是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
							  SysLog.error(task.getAssignee(), null, errorMsg);
							  
							  destData.put("errorMsg", errorMsg);
						  }
					  }
				  }else{
					  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的输出信息需要设置为true！<br />原因：请检查此任务的输出output的数据格式是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
					  SysLog.error(task.getAssignee(), null, errorMsg);
					  
					  destData.put("errorMsg", errorMsg);
				  }
			  }else{
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：找不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的输出信息，导致无法生成表单！<br />原因：请检查此任务的输出信息是否记录。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  destData.put("errorMsg", errorMsg);
			  }
		  }else if("template".equals(inputWay)){
			  if(destformRun != null){
				  int result3 = actFormRunService.insertSelective(destformRun);
				  System.out.println("创建表单实例结果："+result3+" , 类型："+inputWay);
			  }else{
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：输入类型为 表单模板，但找不到目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的目标表单模板！<br />原因：请检查此任务的输入表单模板template的数据格式是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  destData.put("errorMsg", errorMsg);
			  }
		  }else{
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：不识别的表单输入类型："+inputWay+"！<br />原因：请检查此任务的输入表单的操作类型是否正确。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
		  }
		  
		  if(!destData.has("errorMsg")){
			  destData.put("formId", destformRun.getcId());
			  destData.put("formName", destformRun.getFormName());
			  destData.put("formHtml", destformRun.getFormHtml());
		  }
		  
		  
		  return destData;
	  }
   
  /**
   * 处理阅读材料数据
   * @param sourceData
   * @param isHuPing
   * @param task
   * @return
   */
  public JSONObject dealWithReadMeterial(JSONObject sourceData, String commentType, Task task){
	  JSONObject destData = new JSONObject();
	  String taskDefinKey = sourceData.getString("actionId"),
			 materialMaker = sourceData.getString("materialMaker");
	  //没有阅读材料
	  if(taskDefinKey == null || "".equals(taskDefinKey)){
		  return destData;
	  }
	  List<ActStudyVar> tempVars = this.getBusinessVar("output", null, null, taskDefinKey, task.getProcessInstanceId());
	  if(tempVars.size() == 0){
		  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：找不到任务【taskDefinKey:EC"+taskDefinKey+"】上的阅读材料!<br />课程实例id："+task.getProcessInstanceId();
		  //System.out.println(errorMsg);
		  SysLog.error(task.getAssignee(), null, errorMsg);
		  
		  destData.put("errorMsg", errorMsg);
		  return destData;
	  }
	  JSONObject output = new JSONObject(tempVars.get(0).getVarValue());
	  String outputType = output.getString("type");
	  JSONArray tempMeterials = new JSONArray();
	  
	  HistoricTaskInstanceQuery hisTaskQuery = historyService.createHistoricTaskInstanceQuery()
															  .processInstanceId(task.getProcessInstanceId())
															  .taskDefinitionKey(taskDefinKey);
	  if("self".equals(materialMaker)){
		  if(commentType == null || "self".equals(commentType)){
			  hisTaskQuery.taskAssignee(task.getAssignee());
		  }else{
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：阅读材料的materialMaker类型为self，与评论类型commentType为"+commentType+"时冲突！<br />原因：课程文件数据错误，请将此任务的阅读材料类型与评论类型数据保持匹配.<br />课程实例id："+task.getProcessInstanceId();
			  //System.out.println(errorMsg);
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
			  return destData;
		  }
	  }else{
		  if("self".equals(commentType)){
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：阅读材料的materialMaker类型为group，与评论类型commentType为"+commentType+"时冲突！<br />原因：课程文件数据错误，请将此任务的阅读材料类型与评论类型数据保持匹配.<br />课程实例id："+task.getProcessInstanceId();
			  //System.out.println(errorMsg);
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  destData.put("errorMsg", errorMsg);
			  return destData;
		  }
	  }
	  List<HistoricTaskInstance> hisTaskList = hisTaskQuery.list();
	  for(HistoricTaskInstance hisTask:hisTaskList){
		  String currAssignee = hisTask.getAssignee();
		  if("exchange".equals(commentType) && task.getAssignee().equals(currAssignee)){//互评时，不取自己的文件
			  continue;
		  }
		  
		  if("file".equals(outputType)){
			  List<ActStudyFile> tempFiles = this.getStudyFile(null, null, null, hisTask.getAssignee(), hisTask.getId(), null, task.getProcessInstanceId());
			  if(tempFiles.size() > 0){
				  ActStudyFile tempFile = tempFiles.get(0);
				  JSONObject hisFile_task = new JSONObject();
				  hisFile_task.put("fileId", tempFile.getFileid());
				  hisFile_task.put("fileName", tempFile.getFilename());
				  hisFile_task.put("filePath", tempFile.getFilepath());
				  hisFile_task.put("optType", tempFile.getOpttype());
				  hisFile_task.put("userId", tempFile.getUserid());
				  hisFile_task.put("userName", USERNAME);
				  hisFile_task.put("taskName", hisTask.getName());
				  // 查找我对此文档的评论    //现在不能查找此文档的所有评论
				  if(commentType != null){
					  List<ActStudyComment> tempComments = this.getStudyComments(hisFile_task.getString("fileId"), task.getAssignee(), null, task.getId(), null, task.getProcessInstanceId());
					  if(tempComments.size() > 0){
						  JSONObject myComment = new JSONObject();
						  ActStudyComment tempComment = tempComments.get(0);
						  myComment.put("fileId", tempComment.getFileId());
						  myComment.put("commenterId", tempComment.getCommenterId());
						  myComment.put("commenterName", tempComment.getCommenterName());
						  myComment.put("ownerId", tempComment.getOwnerId());
						  myComment.put("ownerName", tempComment.getOwnerName());
						  myComment.put("comments", tempComment.getComments());
						  myComment.put("commentTime", tempComment.getLastUpdateTime());
						  
						  hisFile_task.put("myComment", myComment);
					  }
				  }
				  tempMeterials.put(hisFile_task);
			  }
		  }else{
			  List<ActStudyComment> tempComments = this.getStudyComments(null, null, task.getAssignee(), null, null, task.getProcessInstanceId());
			  if(tempComments.size() > 0){
				  for(ActStudyComment tempC : tempComments){
					  JSONObject myComment = new JSONObject();
					  myComment.put("fileId", tempC.getFileId());
					  myComment.put("commenterId", tempC.getCommenterId());
					  myComment.put("commenterName", tempC.getCommenterName());
					  myComment.put("ownerId", tempC.getOwnerId());
					  myComment.put("ownerName", tempC.getOwnerName());
					  myComment.put("comments", tempC.getComments());
					  myComment.put("commentTime", tempC.getLastUpdateTime());
					  
					  tempMeterials.put(myComment);
				  }
			  }
		  }
	  }
	  
	  String keyType = "file".equals(outputType) ? "files" : "strings";
	  destData.put("type", outputType);
	  //是评论
	  if(commentType != null){
		  //TODO 评论业务规则
		  //JSONObject huPingMeterials  = this.getHuPingMeterials(tempMeterials);
		  //destData.put(keyType, huPingMeterials);
		  
		  destData.put(keyType, tempMeterials);
	  }else{
		  destData.put(keyType, tempMeterials);
	  }
	  
	  return destData;
  }
  
  /**
   * 创建文件副本
   * @param fileData
   * @param fileName
   * @param optType
   * @param task
   * @return
   */
  public JSONObject createFileInstance(JSONObject templateData, String fileName, String toolType, String optType, String inputWay, Task task, boolean isFlow){
	  JSONObject destData = new JSONObject();
	  String fileId = "" , last = "", userId = task.getAssignee(), filePath = "", targetName = "";
	  if("private".equals(optType)){
		  UUID uuid = UUID.randomUUID();
		  fileId = uuid.toString();
	  }else if("cooperation".equals(optType)){
		  fileId = task.getProcessInstanceId()+"_"+task.getTaskDefinitionKey();
	  }else{
		  fileId = "时间："+CourseEngine.getDateStr(null)+" <br />错误：未识别当前任务【id:"+task.getId()+", name:"+task.getName()+"】的文件操作类型："+optType+"!<br />原因：课程文件有错误";
		  //System.out.println(fileId);
		  SysLog.error(task.getAssignee(), null, fileId);
		  
		  destData.put("errorMsg", fileId);
		  return destData;
	  }
	  
	  //生成文件副本
	  if(templateData != null){
		  //文件来源是actionOutput
		  if(templateData.has("filePath")){
			  filePath =  templateData.getString("filePath");
		  }else{
			  fileId = "时间："+CourseEngine.getDateStr(null)+" <br />错误：无法生成当前任务【id:"+task.getId()+", name:"+task.getName()+"】的预设文件!<br />原因：预设文件的数据格式【"+templateData.toString()+"】有问题!";
			  //System.out.println(fileId);
			  SysLog.error(task.getAssignee(), null, fileId);
			  
			  destData.put("errorMsg", fileId);
			  return destData;
		  }
	  }
	  
	  if("textArea".equals(toolType)){
		  last = "html";
	  }else if("word".equals(toolType)){
		  last = "doc";
	  }else if("excel".equals(toolType)){
		  last = "xls";
	  }else if("ppt".equals(toolType)){
		  last = "ppt";
	  }else if("mindMap".equals(toolType)){
		  last = "jm";
	  }else{
		  last = "doc";
	  }
	  
	  //文件来源是template
	  if(templateData != null && templateData.has("name")){
		  fileName = templateData.getString("name");
		  String tLast = templateData.getString("fileType");
		  if(!tLast.startsWith(last)){
			  fileId = "时间："+CourseEngine.getDateStr(null)+" <br />错误：此任务的工具类型为"+last+",而选择的预设文件输入却是"+tLast+", 两者不一致！请确保二者设置一致！";
			  SysLog.error(task.getAssignee(), null, fileId);
			  
			  destData.put("errorMsg", fileId);
			  return destData;
		  }else{
			  last = tLast;
		  }
	  }else{
		  fileName = fileId+ "." + last;//给一个默认名字
	  }
	  
	  targetName =  fileId+ "." +last;
	  String temLast = "."+last;
	  if(fileName.startsWith(temLast)){
		  fileName = targetName;
	  }
	  
	  Map<String, String> file_Map = FileUtils.createFileInstance(SERVLET_CONTEXT, userId, filePath, targetName, last);
	  if(file_Map.containsKey("errorMsg")){
		   fileId = "时间："+CourseEngine.getDateStr(null)+
				    " <br />错误：生成当前任务【id:"+task.getId()+", name:"+task.getName()+"】的预设文件【"+fileName+"】失败!<br />原因："+
				    file_Map.get("errorMsg");
		   SysLog.error(task.getAssignee(), null, fileId);
		   destData.put("errorMsg", fileId);
		   
		   return destData;
	  }else{
		  int isEndFlow = isFlow ? 1 : 0;
		  ActStudyFile newSFile = new ActStudyFile(fileId, file_Map.get("filePath"), fileName,
															userId, null, null,
															last, optType, task.getId(), 
															task.getTaskDefinitionKey(), task.getProcessInstanceId(), 
															fileId, fileId, isEndFlow);
		  //把要copy的源文件找出来
		  if(filePath != null && !"".equals(filePath)){
			  int lastSep_ = filePath.lastIndexOf(File.separator) + 1;
			  int lastIndex_ = filePath.lastIndexOf(".");
			  String fileid_ = filePath.substring(lastSep_, lastIndex_);
			  
			  if("template".equals(inputWay)){//复制的是上传的的文件资料, 是文件流转的源头
				  ActOwnFile actOwnFile = this.actOwnFileService.selectByPrimaryKey(fileid_);
				  if(actOwnFile != null){
					  newSFile.setFilesize(actOwnFile.getFilesize());
					  newSFile.setImagezooms(actOwnFile.getImagezooms());
					  //...设置其他属性
				 }
			  }else{//复制的是学习中的文件资料
				  ActStudyFile actStudyFile = this.actStudyFileService.selectByPrimaryKey(fileid_);
				  if(actStudyFile != null){
					  newSFile.setFilesize(actStudyFile.getFilesize());
					  newSFile.setImagezooms(actStudyFile.getImagezooms());
					  //文件继续流转(条件不满足，说明是一个新文件的开始)
					  if(userId.equals(actStudyFile.getUserid())){
						  newSFile.setLastflow(fileid_);
						  newSFile.setFlowsource(actStudyFile.getFlowsource());
					  }
				 }
			  }
		  }
		  //保存学习过程中产生的文件
		  int insertR = this.actStudyFileService.insertSelective(newSFile);
		  if(insertR <= 0){
			  SysLog.error(task.getAssignee(), newSFile.toString(), "抱歉，保存创建的文件信息失败！");
			  destData.put("errorMsg", "抱歉，保存创建的文件信息失败！");
			  return destData;
		  }
	  
	      SysLog.info(task.getAssignee(), null, file_Map.toString());
	      destData.put("fileId", newSFile.getFileid());
	      destData.put("filePath", newSFile.getFilepath());
		  destData.put("optType", optType);
		  destData.put("userId", newSFile.getUserid());
		  destData.put("userName", USERNAME);
		  destData.put("fileName", newSFile.getFilename());
	  }
	  
	  return destData;
  }
  
  /**
   * 处理任务输入数据
   * @param sourceData
   * @param output
   * @param toolType
   * @param task
   * @return
   */
  public JSONObject dealWithInput(JSONObject sourceData, JSONObject output, String toolType, Task task){
	  JSONObject destData = new JSONObject();
	  String errorMsg = "";
	  //没有输入
	  if(toolType == null || "".equals(toolType)){
		  return destData;
	  }
	  if("textArea".equals(toolType) || "word".equals(toolType) || "excel".equals(toolType) || "ppt".equals(toolType) || "mindMap".equals(toolType)){
		  String optType = "";
		  if(ISCOOPERATION){
			  optType = sourceData.getString("optType");
		  }else{
			  optType = "private";
		  }
		  //优先查找数据库
		  List<ActStudyFile> tempFiles = this.getStudyFile(null, null, null, task.getAssignee(), task.getId(), null, task.getProcessInstanceId());
		  if(tempFiles.size() > 0){
			  ActStudyFile tempFile = tempFiles.get(0);
			  destData.put("fileName", tempFile.getFilename());
			  destData.put("filePath", tempFile.getFilepath());
			  destData.put("optType", tempFile.getOpttype());
			  destData.put("userId", tempFile.getUserid());
			  destData.put("userName", USERNAME);
		  }else{
			  //再做解析
			  String inputWay = sourceData.getString("inputWay"),
					  type = output.getString("type");
			  boolean isFlow = output.getBoolean("requirement");
			  //System.out.println("dealing with input: type=" + type + ", sourceData = "+sourceData.toString());
			  SysLog.info(task.getAssignee(), null, "dealing with input: type=" + type + ", sourceData = "+sourceData.toString());
			  if("file".equals(type)){
				  String fileName = output.getString("name");//文件名
				  if("init".equals(inputWay)){
					  
					  destData = this.createFileInstance(null, fileName, toolType, optType, inputWay, task, isFlow);
					  
				  }else if("actionOutput".equals(inputWay)){
					  String actionId = sourceData.getJSONObject("materialSource").getString("actionId");
					  if(actionId == null || "".equals(actionId.trim())){
						  errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：预设文件类型："+inputWay+"，预设文件来源的actionId为null或空！<br />原因：课程文件有错误，请检查此任务的预设文件来源materialSource的数据格式的正确性。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
						  //System.out.println(errorMsg);
						  SysLog.error(task.getAssignee(), null, errorMsg);
						  
						  destData.put("errorMsg", errorMsg);
					  }else{
						  String inputTaskDefKey = "EC"+actionId.trim();
						  List<ActStudyVar> tempVars = this.getBusinessVar("output", null, null, inputTaskDefKey, task.getProcessInstanceId());
						  if(tempVars.size() == 0){
							    errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：预设文件类型："+inputWay+",找不到任务节点【key:"+inputTaskDefKey+"】的输出output!<br />原因：课程文件在此任务需要设置输出，因为当前任务【name:"+task.getName()+"】的输入基于此任务的输出。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
								SysLog.error(task.getAssignee(), null, errorMsg);
								destData.put("errorMsg", errorMsg);
						  }else{
							  ActStudyVar tempVar1 = tempVars.get(0);
							  JSONObject hisOutput = new JSONObject(tempVar1.getVarValue());
							  String typeStr = hisOutput.getString("type").toLowerCase();
							  if(!hisOutput.has("type") || !"file".equals(typeStr)){
								    errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：预设文件类型："+inputWay+", 任务节点【key:"+inputTaskDefKey+"】的输出类型【"+typeStr+"】设置错误!<br />原因：基于此任务输出的当前任务【name:"+task.getName()+"】的输入设置为file类型。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
									SysLog.error(task.getAssignee(), null, errorMsg);
									destData.put("errorMsg", errorMsg);
							  }else{
								  	List<ActStudyFile> tempFiles1 = this.getStudyFile(null, null, null, tempVar1.getUserId(), tempVar1.getTaskId(), null, task.getProcessInstanceId());
								  	if(tempFiles1.size() > 0){
										JSONObject inputFileData = new JSONObject();
										inputFileData.put("filePath", tempFiles1.get(0).getFilepath());
										SysLog.info(task.getAssignee(), null, "create file["+fileName+"] acorrding to actionOutput:" + inputFileData.toString());
										destData = this.createFileInstance(inputFileData, fileName, toolType, optType, inputWay, task, isFlow);
									}else{
										errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：预设文件类型："+inputWay+",找不到人员"+tempVar1.getUserId()+"在任务【id:"+tempVar1.getTaskId()+", key:"+inputTaskDefKey+", name:"+tempVar1.getTaskName()+"】上的输出文件!【课程实例id:"+task.getProcessInstanceId()+"】";
										//System.out.println(errorMsg);
										SysLog.error(tempVar1.getUserId(), null, errorMsg);
										
										destData.put("errorMsg", errorMsg);
									}
							  }
						  }
						  
					  }
				  }else if("template".equals(inputWay)){
					  Object templateObj = sourceData.get("template");
					  if(templateObj instanceof JSONObject){
						  JSONObject templateData = sourceData.getJSONObject("template");
						  //System.out.println("create file【"+fileName+"】 acorrding to template:" + templateData.toString());
						  SysLog.info(task.getAssignee(), null, "create file【"+fileName+"】 acorrding to template:" + templateData.toString());
						  
						  destData = this.createFileInstance(templateData, fileName, toolType, optType, inputWay, task, isFlow);
					  }else{
						  errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：预设文件类型："+inputWay+"，目标任务【id:"+task.getTaskDefinitionKey()+", name:"+task.getName()+"】的预设文件模板数据格式【"+templateObj.toString()+"】错误！<br />原因：课程文件目标任务的数据有错误。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
						  //System.out.println(errorMsg);
						  SysLog.error(task.getAssignee(), null, errorMsg);
						  
						  destData.put("errorMsg", errorMsg);
					 }
				  }else{
					  errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：未识别的富文本输入类型："+inputWay+"!<br />原因：课程文件有错误，请检查此任务的富文本输入类型inputWay的正确性。";
					  //System.out.println(errorMsg);
					  SysLog.error(task.getAssignee(), null, errorMsg);
					  
					  destData.put("errorMsg", errorMsg);
				  }
			  }
		  }
	  }else if("VM".equals(toolType)){
		  //TODO 创建虚拟机
		  //String vm_group = 
		  destData = sourceData;
		  
	  }else if("choose".equals(toolType)){
		  destData = sourceData;
	  }else if("board".equals(toolType)){
		  destData = sourceData;
	  }else if("email".equals(toolType)){
		  destData = sourceData;
	  }/*
	  else if("mindMap".equals(toolType)){//头脑风暴
		  destData = sourceData;
	  }*/else if("form".equals(toolType) || "dynamicForm".equals(toolType)){
		  //检查是否已存在
		  ActFormRun formRunQuery = new ActFormRun();
		  formRunQuery.setTaskId(task.getId());
		  formRunQuery.setUserId(task.getAssignee());
		  formRunQuery.setTaskDefineKey(task.getTaskDefinitionKey());
		  formRunQuery.setProcessInstanceId(task.getProcessInstanceId());
		  List<ActFormRun> formRunList = actFormRunService.getFormRunList(formRunQuery);
		  if(formRunList.size() > 0){
			  ActFormRun tempFormRun = formRunList.get(0);
			  destData.put("formId", tempFormRun.getcId());
			  destData.put("formName", tempFormRun.getFormName());
			  destData.put("formHtml", tempFormRun.getFormHtml());
		  }else{
			  destData = this.createFormInstance(sourceData, task, toolType);
		  }
		  
	  }else if("comment".equals(toolType)){
		  destData = sourceData;
	  }else{
		  errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：未识别的工具类型："+toolType+"!<br />原因：课程文件有错误，请检查此任务的工具类型inputType的正确性。";
		  //System.out.println(errorMsg);
		  SysLog.error(task.getAssignee(), null, errorMsg);
		  
		  destData.put("errorMsg", errorMsg);
	  }
	  
	  return destData;
  }
  
  /**
   * 处理任务输出数据
   * @param sourceData
   * @param commentType
   * @return
   */
  public JSONObject dealWithOutput(JSONObject sourceData, String commentType){
	  JSONObject destData = new JSONObject();
	  if(commentType != null){
		  if(!"comment".equals(sourceData.getString("type")) || !sourceData.getBoolean("requirement")){
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：当前工具类型为 【评论】，但输出output的数据结构【"+sourceData.toString()+"】不匹配！<br />原因：课程文件有错误，请检查当前任务的输出output的数据格式的正确性。";
			  //System.out.println(errorMsg);
			  SysLog.error(null, null, errorMsg);
			  
			  sourceData.put("errorMsg", errorMsg);
		  }
	  }
	  
	 destData = sourceData;
	 
	 return destData;
  }
  
  /**
   * 处理任务上的业务数据
   * @param sourceData
   * @param task
   * @return
   */
  public JSONObject dealWith(JSONObject sourceData, Task task){
	  //保存输出为全局变量
	  JSONObject output = new JSONObject(sourceData.getString("output"));
	  List<ActStudyVar> tempVars = this.getBusinessVar("output", task.getAssignee(), task.getId(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
	  if(tempVars.size() == 0){
		  this.saveBusinessVar("output", sourceData.getString("output"), task);
	  }
	  if(output.has("lastTaskId")){
		  sourceData.put("lastTaskId", output.getString("lastTaskId"));
	  }
	  //TODO 评分规则
	  if(sourceData.has("scoreRules")){
		  JSONArray scoreRules = new JSONArray(sourceData.getString("scoreRules"));
		  if(scoreRules.length() > 0){//不为空，此任务输出参与评分
			  List<ActStudyVar> tempVars1 = this.getBusinessVar("scoreRules", task.getAssignee(), task.getId(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
			  if(tempVars1.size() == 0){
				  this.saveBusinessVar("scoreRules", scoreRules.toString(), task);
			  }
			  //设置总评估
			  List<ActStudyVar> tempVars2 = this.getBusinessVar("summarize", task.getAssignee(), null, null, task.getProcessInstanceId());
			  if(tempVars2.size() == 0){
				  this.saveBusinessVar("summarize", new JSONObject().toString(), task);
			  }
		  }
	  }
	  
	  String tooType = sourceData.getString("toolType"), commentType = null;
	  if("comment".equals(tooType)){
		  if(sourceData.has("commentType")){
			  commentType = sourceData.getString("commentType");
		  }
	  }else{
		  //处理输入
		  JSONObject myInput = new JSONObject();
		  if(sourceData.has("input")){
			  myInput = this.dealWithInput(new JSONObject(sourceData.getString("input")), output, sourceData.getString("toolType"), task);
		  }else{
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：当前工具类型为 【"+tooType+"】，但input数据为空！<br />原因：课程文件有错误，请检查当前任务的input数据格式。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
			  SysLog.error(task.getAssignee(), null, errorMsg);
			  
			  myInput.put("errorMsg", errorMsg);
		  }
		  sourceData.remove("input");
		  if(myInput.length() > 0){
			  sourceData.put("input", myInput);
		  }
		  
	  }
	  //处理阅读材料
	  JSONObject myReadMeterial = new JSONObject();
	  if("comment".equals(tooType) && commentType == null){
		  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：当前工具类型为 评论，但评论类型为空！<br />原因：课程文件有错误，请检查当前任务的评论类型数据格式。<br />【课程实例id:"+task.getProcessInstanceId()+"】";
		  SysLog.error(task.getAssignee(), null, errorMsg);
		  
		  myReadMeterial.put("errorMsg", errorMsg);
	  }else{
		  myReadMeterial = this.dealWithReadMeterial(new JSONObject(sourceData.getString("materialSource")), commentType, task);
		 //评论，把assignee要评论的文档数目记下来
		  if("comment".equals(tooType) && !myReadMeterial.has("errorMsg")){
			  List<ActStudyVar> tempVars3 = this.getBusinessVar("commentNum", task.getAssignee(), task.getId(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
			  if(tempVars3.size() == 0){
				  //TODO
				  this.saveBusinessVar("commentNum", Integer.toString(myReadMeterial.getJSONArray("files").length()), task);
			  }
		  }
	  }
	  //处理输出, //输出output似乎没用了，可以不必处理了
	  JSONObject myOutput = this.dealWithOutput(new JSONObject(sourceData.getString("output")), commentType);
	  
	  sourceData.remove("materialSource");
	  if(myReadMeterial.length() > 0){
		  sourceData.put("materialSource", myReadMeterial);
	  }
	  sourceData.remove("output");
	  sourceData.put("output", myOutput);
	  
	  return sourceData;
  }
  
  /**
   * 根据过程的id[processDefinitionId], 从给定的运行时task中得到负责人assignee的当前任务数据和以后的任务数据列表
   * @param processDefinitionId
   * @param task
   * @param assignee
   * @return
   */
  public JSONObject getTasklistELRuntimeTask(String processDefinitionId, String procInstId, List<Task> tasks, String assignee){
	  JSONObject myTasklist = new JSONObject();
	  
	  String subProcesskey = null;
	  //this.clearTasklist();
	  
	  //for(Task task:tasks){
	  Task task = tasks.get(0);
		  JSONObject currTask = new JSONObject();
		  //多人协作课程的task里必有人员，可以不必设置
		  if(!ISCOOPERATION){
			  String currAssignee = task.getAssignee();
			  if(currAssignee == null || "".equals(currAssignee)){
				 task.setAssignee(assignee);
				 taskService.saveTask(task);
			  }
		  }
		  
		  ActivityImpl subProcess = this.getDestActivityImpl(task.getTaskDefinitionKey(), processDefinitionId, "subProcess");
		  if(subProcess != null){
			  subProcesskey = subProcess.getId();
			  JSONObject currSubTask = this.getCourseDatasOfTask(processDefinitionId, null, subProcesskey, task.getId(), assignee);
			  myTasklist.put("currSubTask", currSubTask);
		  }
		  
		  currTask = this.getCourseDatasOfTask(processDefinitionId, task.getTaskDefinitionKey(), subProcesskey, task.getId(), assignee);
		  //加工任务数据currTaskData
		  JSONObject taskInfo = currTask.getJSONObject("taskInfo");
		  JSONObject myTaskData = this.dealWith(taskInfo.getJSONObject("data"), task);
		  
		  taskInfo.remove("data");
		  taskInfo.put("data", myTaskData);
		  currTask.remove("taskInfo");
		  currTask.put("taskInfo", taskInfo);
		  
		  myTasklist.put("currTask", currTask);
		  
		  return myTasklist;
		  
		  //this.addToTasklist(currTask);
		  //myTasklist.put("currTask", currTask);
	  //}
  }
  
  /**
   * 本课程是否结束
   * @param processInstanceId
   * @return
   */
  public boolean isEnded(String processInstanceId){
	  ProcessInstance pi = runtimeService.createProcessInstanceQuery()
										  .processInstanceId(processInstanceId)
										  .singleResult();
	  
	  return pi == null ? true:false;
  }
  
  /**
   * 检查输出文件情况，是否为空
   * @param outputFile
   * @return
   */
  public boolean checkOutputFile(JSONObject outputFile){
	  boolean fileIsEmpty = false;
	  //TODO 获取文件大小
	  /*
	  Map<String, String> params = new HashMap<String, String>();
	  params.put("filePath", outputFile.getString("outputFile"));
	  String fileSize = CourseEngine.POST(OCPath+"/apps/managementsysext/file_api/cp_file_to_system", params);
	  
	  if(Integer.parseInt(fileSize) < 10){
		  fileIsEmpty = true;
	  }
	  */
	  if(outputFile == null || outputFile.length() == 0)
		  fileIsEmpty = true;
	  
	  return fileIsEmpty;
  }
  
  /**
   * 检查文件是否提交
   * @param outputFile
   * @return
   */
  public boolean checkFileIsSubmit(JSONObject outputFile){
	  boolean fileIsSubmit = false;
	  
	  if(outputFile.has("isSubmit")){
		  fileIsSubmit = outputFile.getBoolean("isSubmit");
	  }
	  
	  return fileIsSubmit;
  }
  
  /**
   * 系统评分
   * @param checkResult
   * @param task
   * @return
   */
  public JSONObject setScoreBySystem(JSONObject checkResult, Task task){
	  if(checkResult.getBoolean("canComplete")){
		  //此步任务参与评分
		  if(!taskService.hasVariableLocal(task.getId(), "scoreRules_"+task.getAssignee())){
			  String temp_scoreRules = (String)taskService.getVariableLocal(task.getId(), "scoreRules_"+task.getAssignee());
			  temp_scoreRules = temp_scoreRules.replaceAll("\\n", "");
			  JSONArray scoreRuleArr =  new JSONArray(temp_scoreRules);
			  int sumScore = 0;
			  for(int i=0; i<scoreRuleArr.length();i++){
				  JSONObject optObj = scoreRuleArr.optJSONObject(i);
				  if(optObj.has("type")){
					  String scoreType = optObj.getString("type");
					  if("isSubmited".equals(scoreType)){
						  
						  int myScore = Integer.parseInt(optObj.getString("score"));
						  optObj.put("myScore", myScore);
						  sumScore += myScore;
						  
					  }else if("examScore".equals(scoreType)){
						  
					  }else if("peopleScore".equals(scoreType)){
						  if(optObj.has("myScore")){
							  sumScore += optObj.getInt("myScore");
						  }
						  
						  continue;
					  }else{
						  SysLog.error(task.getAssignee(), null, "不能识别的评分类型："+scoreType);
						  
						  continue;
					  }
					  
					  scoreRuleArr.put(i, optObj);
				  }
			  }
			  
			  //改变总分
			  JSONObject summarize = null;
			  if(runtimeService.hasVariable(task.getProcessInstanceId(), "summarize_"+task.getAssignee()))
				  summarize = new JSONObject((String)runtimeService.getVariable(task.getProcessInstanceId(), "summarize_"+task.getAssignee()));
			  else
				  summarize = new JSONObject();
			  
			  summarize.put("sumScore", sumScore);
			  runtimeService.setVariable(task.getProcessInstanceId(), "summarize_"+task.getAssignee(), summarize.toString());
			}
	  }
	  
	  return checkResult;
  }
  
  /**
   * 检查我的任务输出
   * @param myOutput
   * @param task
   * @return
   */
  public JSONObject checkTaskOutput(JSONObject myOutput, Task task){
	  JSONObject checkResult = new JSONObject();
	  JSONObject output = null;
	  List<ActStudyVar> tempVars = this.getBusinessVar("output", null, null, task.getTaskDefinitionKey(), task.getProcessInstanceId());
	  if(tempVars.size() == 0){
		  checkResult.put("canComplete", true);
		  return checkResult;
	  }else{
		  output = new JSONObject(tempVars.get(0).getVarValue());
	  }
	  
	  boolean requirement = output.getBoolean("requirement");
	  if(requirement){
		  String outputType = output.getString("type");
		  if("file".equals(outputType)){
			  //JSONObject outputFile = new JSONObject((String)taskService.getVariableLocal(task.getId(), "file_"+task.getAssignee()));
			  //if(this.checkOutputFile(outputFile)){//输出文件为空
			  if(this.checkOutputFile(myOutput)){//输出文件为空
			//if(this.checkFileIsSubmit(outputFile)){
				  checkResult.put("canComplete", false);
				  checkResult.put("outputType", "file");
				  //String infoMsg = "请先完成并提交任务["+task.getName()+"]的学习成果文件["+myOutput.getString("fileName")+"]!";
				  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：不能提交任务到下一步！<br />原因：output为空，您在本任务上的操作文件需要输出到下一任务！请检查您此任务上的文件是否生成成功，或完成保存。";
				  //System.out.println(errorMsg);
				  SysLog.error(task.getAssignee(), null, errorMsg);
				  
				  checkResult.put("errorMsg", errorMsg);
			  }else{
				  checkResult.put("canComplete", true);
			  }
		  }else if("comment".equals(outputType)){
			  if(myOutput == null || myOutput.length() == 0){
				  checkResult.put("canComplete", false);
				  String infoMsg = "你有文件等待评论！";
				  //System.out.println(infoMsg);
				  SysLog.info(task.getAssignee(), null, infoMsg);
				  
				  checkResult.put("infoMsg", infoMsg);
			  }else{
				  // 保存评论内容，评论针对文件还是人？
				  this.saveStudyComment(null, myOutput.getString("fileId"), myOutput.getString("fileName"), myOutput.getString("comment"), myOutput.getString("ownerId"), myOutput.getString("ownerName"), task);
				  
				  List<ActStudyComment> tempC = this.getStudyComments(null, task.getAssignee(), null, task.getId(), null, task.getProcessInstanceId());
				  List<ActStudyVar> tempVars2 = this.getBusinessVar("commentNum", task.getAssignee(), task.getId(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
				  if(tempVars2.size() > 0){
					  int total = Integer.parseInt(tempVars2.get(0).getVarValue());
					  if(tempC.size() == total){
						  checkResult.put("canComplete", true);
					  }else{
						  checkResult.put("canComplete", false);
						  int leave = total - tempC.size();
						  String infoMsg = "评论"+myOutput.getString("ownerName")+"的文档成功！你还有"+leave+"份文件等待评论！";
						  //System.out.println(infoMsg);
						  SysLog.info(task.getAssignee(), null, infoMsg);
						  
						  checkResult.put("infoMsg", infoMsg);
					  }
				  }else{
					  SysLog.info(task.getAssignee(), null, "获取不到你在任务 【"+task.getName()+"】上待评价文档的数量!");
				  }
			  }
		  }else if("question".equals(outputType)){
			//TODO 系统评分
			checkResult = this.setScoreBySystem(checkResult, task);
			
		  }else if("form".equals(outputType)){
			  //先向引擎设置表单里的流程变量
			  List<ActFormRun> formRunList = actFormRunService.getFormRunList(
																					  new ActFormRun(null, null, null, null,
																									  null, task.getTaskDefinitionKey(), null, task.getProcessInstanceId(),
																									  null, null)
																			  );
			  if(formRunList.size() > 0){
				  ActFormRun taskformRun = formRunList.get(0);
				  
				  Map<String, Object> map = new HashMap<String, Object>();
				  String processVars = taskformRun.getProcessVars();
				  if(processVars != null && !"".equals(processVars)){
					  JSONArray vars = new JSONArray(processVars);
					  if(vars.length() > 0){
						  for(int k=0;k<vars.length();k++){
							  JSONObject tempV = vars.optJSONObject(k);
							  String attrType = tempV.getString("attrType");
							  if("enum".equals(attrType)){
								  JSONObject dV = new JSONObject(tempV.getString("defaultValue"));
								  map.put(tempV.getString("attrName"), dV.getString("dId"));
							  }else{
								  map.put(tempV.getString("attrName"), tempV.getString("defaultValue"));
							  }
						  }
						  
						  taskService.setVariablesLocal(task.getId(), map);
					  }
				  }
				  
			  }
			  
			  checkResult.put("canComplete", true);
			  
		  }else{//其他待定的输出类型处理
			  checkResult.put("canComplete", true);
		  }
	  }else{
		  checkResult.put("canComplete", true);
	  }
	  
	  return checkResult;
  }
  
  /**
   * 结束小组课程
   * @param procInstId
   */
  public void completeGroupCourse(String procInstId, String assignee){
	  String groupId = (String)historyService.createHistoricVariableInstanceQuery().processInstanceId(procInstId).variableName("groupId").singleResult().getValue();
	  
	  CourseOrgStructure cc = new CourseOrgStructure();
	  cc.setOrgStructureId(groupId);
	  cc.setStatus("1");
	  cc.setLstupdid(assignee);
	  cc.setLstupddate(CourseEngine.getDateStr(null));
	  int result1 = this.courseOrgStructureService.updateByPrimaryKeySelective(cc);
	  //System.out.println("更新小组【"+groupId+"】课程状态:"+result1);
	  SysLog.info(null, null, "更新小组【"+groupId+"】课程状态:"+result1);
	  
	  String courseOrgId = (String)historyService.createHistoricVariableInstanceQuery().processInstanceId(procInstId).variableName("courseOrgId").singleResult().getValue();
	  //未完成课程的小组数
	  int count = this.courseOrgStructureService.checkIsAllGroupFinishCourse(courseOrgId);
	  if(count == 0){
		//更改课程状态
		  CourseOrg co = new CourseOrg();
		  co.setLrnscnOrgId(courseOrgId);
		  co.setStatus("3");
		  co.setLstupdid(assignee);
		  co.setLstupddate(CourseEngine.getDateStr(null));
		  int result2 = courseOrgService.updateByPrimaryKeySelective(co);
		  //System.out.println("更新课程状态结果:"+result2);
		  SysLog.info(null, null, "更新课程状态结果:"+result2);
	  }
  }
  
  /**
   * 结束自己的课程
   * @param procInstId
   */
  public void completeUserCourse(String procInstId, String assignee){
	  String roleCid = (String)historyService.createHistoricVariableInstanceQuery().processInstanceId(procInstId).variableName("roleCid").singleResult().getValue();
	  CourseOrgUser cUser = new CourseOrgUser();
	  cUser.setOrgStructureId(roleCid);
	  cUser.setUserId(assignee);
	  cUser.setStatus("1");
	  cUser.setLstupddate(CourseEngine.getDateStr(null));
	  cUser.setLstupdid(assignee);
	  int result1 = courseOrgUserService.updateByPrimaryKeySelective(cUser);
	  SysLog.info(assignee, null, "结束自己的课程, 结果:"+result1);
	  
	  String courseOrgId = (String)historyService.createHistoricVariableInstanceQuery().processInstanceId(procInstId).variableName("courseOrgId").singleResult().getValue();
	  //未完成课程的学员数
	  int count = this.courseOrgUserService.checkIsAllUserFinishCourse(courseOrgId);
	  if(count == 0){
		  //更改课程状态
		  CourseOrg co = new CourseOrg();
		  co.setLrnscnOrgId(courseOrgId);
		  co.setStatus("3");
		  co.setLstupdid(assignee);
		  co.setLstupddate(CourseEngine.getDateStr(null));
		  int result2 = courseOrgService.updateByPrimaryKeySelective(co);
		  System.out.println("更新课程状态结果:"+result2);
	  }
  }
  
  /**
   * 获取当前任务
   * @param processDefinitionId
   * @param processInstanceId
   * @param assignee
   * @return
   */
  public List<Task> getCurrentTasks(String processDefinitionId, String processInstanceId, String assignee){
	   TaskQuery taskQuery = taskService.createTaskQuery()
										.processDefinitionId(processDefinitionId)
										.processInstanceId(processInstanceId);
	  
	  if(ISCOOPERATION){
		  taskQuery.taskAssignee(assignee);
	  }
	  
	  List<Task> tasksToDo = taskQuery.list();
	  
	  return tasksToDo;
  }
  
  /**
   * 结束task[taskId=taskIdToEnd], 然后获取负责人assignee在课程实例processInstanceId中的下一步的任务列表。
   * @param processDefinitionId
   * @param processInstanceId
   * @param assignee
   * @param taskIdToEnd
   * @return
   */
  public JSONObject getRuntimeTask(String processDefinitionId, String processInstanceId, String assignee, String taskIdToEnd, JSONObject output){
	  JSONObject myTasklist = new JSONObject(), checkOutput = new JSONObject();
	  if(taskIdToEnd != null && !"".equals(taskIdToEnd) && !"null".equals(taskIdToEnd)){
		    List<Task> tasksToEnd = taskService.createTaskQuery()
											   .processInstanceId(processInstanceId)
											   .taskAssignee(assignee)
											   .taskId(taskIdToEnd)
											   .list();
		    
			if(tasksToEnd.size() > 0){
				//TODO 结束之前，检查输出是否完成
				checkOutput = this.checkTaskOutput(output, tasksToEnd.get(0));
				if(checkOutput.getBoolean("canComplete")){
					//TODO 流程变量, 尚未用到
					Map<String, Object> map = new HashMap<String, Object>();
					if(output != null && output.length() > 0 && "processVar".equals(output.getString("type"))){
						JSONObject vars = output.getJSONObject("vars");
						if(vars != null && vars.length() > 0){
							String[] keys = JSONObject.getNames(vars);
							for(String key:keys){
								map.put(key, vars.get(key));
							}
						}
					}
					//taskService.complete(tasksToEnd.get(0).getId());
					taskService.complete(tasksToEnd.get(0).getId(), map);
					//System.out.println("------------------ task[ name: "+tasksToEnd.get(0).getName()+", id: "+tasksToEnd.get(0).getId()+" , assignee: "+assignee+" ] is finished.------------------ ");
				 }
			}else{
				//System.out.println("时间："+CourseEngine.getDateStr(null)+" <br />错误：找不到要结束的任务【 processInstanceId: "+processInstanceId+" , taskId: "+taskIdToEnd+" , assignee: "+assignee+" 】!");
				SysLog.error(assignee, null, "时间："+CourseEngine.getDateStr(null)+" <br />错误：找不到要结束的任务【 processInstanceId: "+processInstanceId+" , taskId: "+taskIdToEnd+" , assignee: "+assignee+" 】!");
				checkOutput.put("canComplete", true);
				//TODO   结束task失败时，该怎么做处理。多角色时貌似这个不是问题，可以忽略。
			}
	  }else{
		  checkOutput.put("canComplete", true);
	  }
	  
	  if(!checkOutput.getBoolean("canComplete")){
		  myTasklist.put("courseStatus", "start");
		  if(checkOutput.has("infoMsg"))
			  myTasklist.put("infoMsg", checkOutput.getString("infoMsg"));
		  else
			  myTasklist.put("errorMsg", checkOutput.getString("errorMsg"));
	  }else{
		    List<Task> tasksToDo = this.getCurrentTasks(processDefinitionId, processInstanceId, assignee);

			if(tasksToDo.size() > 0){
			//System.out.println("------------------ task[ name: "+tasksToDo.get(0).getName()+" , taskId: "+tasksToDo.get(0).getId()+", taskDefKey: "+tasksToDo.get(0).getTaskDefinitionKey()+" ] has started.------------------ ");
			
			//按角色取tasklist, 支持多角色, 支持网关。(支持并行处理, 支持子过程)
			//TODO 暂时不考虑多任务并行
			myTasklist = this.getTasklistELRuntimeTask(processDefinitionId, processInstanceId, tasksToDo, assignee);
			myTasklist.put("courseStatus", "start");
			
			}else{
				//System.out.println("------------------All tasks are finished, processDefinitionId: "+processDefinitionId+" , processInstanceId : "+processInstanceId+".------------------ ");
				if(this.isEnded(processInstanceId)){
					myTasklist.put("courseStatus", "end");
					
					if(ISCOOPERATION){
						if(!ISSINGLE){//多人（多角色）协作学习
							this.completeGroupCourse(processInstanceId, assignee);
						}else{//单人独立学习
							this.completeUserCourse(processInstanceId, assignee);
						}
					}
						
				}else{
					//TODO 可能要判断是角色内其他人员还没完成任务，还是当前任务是其他角色在执行
					if(!ISSINGLE && ISCOOPERATION){
						myTasklist.put("courseStatus", "start");
						myTasklist.put("studyMsg", "您暂时没有新的学习任务可做了！请等待其他人完成任务！");
					}else{
						myTasklist.put("courseStatus", "start");
						String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：找不到您的新的学习任务【procInsId:"+processInstanceId+", last_taskId:"+taskIdToEnd+"】！<br />原因：课程文件设计可能有错误";
						myTasklist.put("errorMsg", errorMsg);
						SysLog.error(null, null, errorMsg);
					}
				}
			}
	  }
	  
	  return myTasklist;
  }
  
  /**
   * 结束角色role的task[taskDefKey=taskDefKeyToEnd], 然后获取下一步当前任务负责人的任务列表。
   * CoursePlayer获取用户课程任务列表的总入口
   * @param processDefinitionId
   * @param courseId
   * @param role
   * @param taskDefKeyToEnd
   * @return
   * 
   */
  //public synchronized String getStudyTasks(String processDefinitionId, String courseInstanceId, String assignee, String taskIdToEnd, JSONObject output){
  public synchronized String getStudyTasks(ServletContext servletContext, JSONObject dataInfoObj){
	  JSONObject studyTasks = new JSONObject(), output = null;
	  String processDefinitionId = dataInfoObj.getString("processDefinitionId"),
			courseInstanceId = "",
			roleCid = "",
			groupId = "",
			courseOrgId = "",
			assignee = dataInfoObj.getString("assignee"), 
			taskIdToEnd = dataInfoObj.getString("taskId");
	  
	  USERNAME = dataInfoObj.getString("userName");
	  //OCPath = "http://"+dataInfoObj.getString("OCPath");
	  SERVLET_CONTEXT = servletContext;
	  
	  if(dataInfoObj.has("output")){
		  output = dataInfoObj.getJSONObject("output");
	  }
	  
	  if(dataInfoObj.has("courseOrgId")){
		  ISCOOPERATION = true;
		  courseOrgId = dataInfoObj.getString("courseOrgId");
		  groupId = dataInfoObj.getString("groupId");
		  roleCid = dataInfoObj.getString("roleCid");
	  }else{
		  ISCOOPERATION = false;
		  courseInstanceId = dataInfoObj.getString("courseInstanceId");
	  }
	  if(dataInfoObj.has("isSingle")){
		  //ISSINGLE = dataInfoObj.getBoolean("isSingle");
		  ISSINGLE = dataInfoObj.getInt("isSingle") == 1 ? true:false;
	  }
	  /*
	  if(dataInfoObj.has("isCooperation")){
		  ISCOOPERATION = dataInfoObj.getBoolean("isCooperation");
	  }
	  if(ISCOOPERATION){
		  courseOrgId = dataInfoObj.getString("courseOrgId");
		  groupId = dataInfoObj.getString("groupId");
		  roleCid = dataInfoObj.getString("roleCid");
	  }else{
		  courseInstanceId = dataInfoObj.getString("courseInstanceId");
	  }
	  */
						
	  //运行课程实例
	  JSONObject runResult = this.runCourse(processDefinitionId, courseInstanceId, roleCid, groupId, courseOrgId, assignee);
	  if(runResult.has("processInstanceId")){
		  //获取用户的课程任务列表
		  studyTasks = this.getRuntimeTask(processDefinitionId, 
													runResult.getString("processInstanceId"), 
													assignee,
													taskIdToEnd,
													output);
	 }else if(runResult.has("end")){
		 studyTasks.put("courseStatus", "end");
	 }else{
		 studyTasks.put("errorMsg", runResult.get("errorMsg"));
	 }
	  
	  return studyTasks.toString();
  }
  
  /**
   * 提交业务数据
   * @param dataInfoObj
   * @return
   * 
   * 
   * no using
   * 
   */
  public synchronized String submitData(JSONObject dataInfoObj){
	  JSONObject submitResult = new JSONObject();
	  String assignee = dataInfoObj.getString("assignee"), 
			taskId = dataInfoObj.getString("taskId"),
			submitType = dataInfoObj.getString("submitType");
	  Task task = taskService.createTaskQuery()
			  				   .taskId(taskId)
							   .taskAssignee(assignee)
							   .singleResult();
	  
	  if(task == null){
		  submitResult.put("submitResult", false);
		  submitResult.put("errorMsg", "时间："+CourseEngine.getDateStr(null)+" <br />错误：【submitData】找不到您当前的任务，无法提交!");
		  
		  return submitResult.toString();
	  }
	  
	  if("file".equals(submitType)){
		  if(taskService.hasVariable(taskId, "file_"+assignee)){
			  JSONObject outputFile = new JSONObject((String)taskService.getVariableLocal(taskId, "file_"+assignee));
			  outputFile.put("isSubmit", true);
			  outputFile.put("submitTime", CourseEngine.getDateStr(null));
			  taskService.setVariableLocal(task.getId(), "file_"+task.getAssignee(), outputFile);
			  submitResult.put("submitResult", true);
		  }else{
			  submitResult.put("submitResult", false);
			  submitResult.put("errorMsg", "时间："+CourseEngine.getDateStr(null)+" <br />错误：【submitData】找不到您提交的文件!");
		  }
	  }
	  /*else if("form".equals(submitType)){
		  
	  }else if("question".equals(submitType)){
		  
	  }
		*/	
	  return submitResult.toString();
  }
  
  /**
   * 获取任务上的文件
   * @param dataInfoObj
   * @return
   * 
   * no using
   * 
   */
  public String getFile(JSONObject dataInfoObj){
	  
	  HistoricTaskInstanceQuery hisTaskQuery =  historyService.createHistoricTaskInstanceQuery();
	  
	  if(dataInfoObj.has("courseInstanceId")){//单人课程的课程id
		  hisTaskQuery.processInstanceBusinessKey(dataInfoObj.getString("courseInstanceId"));
	  }
	  if(dataInfoObj.has("bpmnInstanceId")){
		  hisTaskQuery.processInstanceBusinessKeyLike(dataInfoObj.getString("bpmnInstanceId")+"%");
	  }
	  if(dataInfoObj.has("courseOrgId")){
		  hisTaskQuery.processInstanceBusinessKeyLike("%"+dataInfoObj.getString("courseOrgId"));
	  }
	  if(dataInfoObj.has("processInstanceId")){
		  hisTaskQuery.processInstanceId(dataInfoObj.getString("processInstanceId"));
	  }
	  if(dataInfoObj.has("assignee")){
		  hisTaskQuery.taskAssignee(dataInfoObj.getString("assignee"));
	  }
	  if(dataInfoObj.has("taskDefKey")){
		  hisTaskQuery.taskDefinitionKey(dataInfoObj.getString("taskDefKey"));
	  }
	  if(dataInfoObj.has("taskId")){
		  hisTaskQuery.taskId(dataInfoObj.getString("taskId"));
	  }
	  if(dataInfoObj.has("taskName")){
		  hisTaskQuery.taskNameLikeIgnoreCase("%"+dataInfoObj.getString("taskName")+"%");
	  }
	  
	  List<HistoricTaskInstance> hisTaskList = hisTaskQuery.list();
	  JSONArray fileArr = new JSONArray();
	  
	  for(HistoricTaskInstance hisTask:hisTaskList){
		  HistoricVariableInstance hisTaskVari = historyService.createHistoricVariableInstanceQuery()
													  				.processInstanceId(hisTask.getProcessInstanceId())
													  				.taskId(hisTask.getId())
													  				.variableName("file_"+hisTask.getAssignee())
													  				.singleResult();
		  if(hisTaskVari != null){
			  JSONObject file = new JSONObject((String) hisTaskVari.getValue());
			  file.put("taskId", hisTask.getId());
			  file.put("taskName", hisTask.getName().split("@")[0]);
			  file.put("processDefinitionId", hisTask.getProcessDefinitionId());
			  //file.put("processInstanceId", hisTask.getProcessInstanceId());
			  file.put("taskStartTime", CourseEngine.getDateStr(hisTask.getStartTime()));
			  file.put("taskEndTime", CourseEngine.getDateStr(hisTask.getEndTime()));
			  file.put("duration", hisTask.getDurationInMillis());
			  
			  fileArr.put(file);
		  }
	  }
	  
	  return fileArr.toString();
  }
  
  /**
   * 获取我学习的课程的评估信息
   * @param dataInfoObj
   * @return
   */
  public JSONObject getMyEstimation(JSONObject dataInfoObj){
	  JSONObject resultObj = new JSONObject();
	  
	  JSONArray myEstiItems = this.getMyEstimationItems(dataInfoObj);
	  resultObj.put("estimationItems", myEstiItems);
	  
	  JSONObject summarize = this.getMySummarize(dataInfoObj);
	  resultObj.put("summarize", summarize);
	  
	  return resultObj;
  }
  
  /**
   * 获取我的评估项信息
   * @param dataInfoObj
   * @return
   */
  public JSONArray getMyEstimationItems(JSONObject dataInfoObj){
	  JSONArray resultArr = new JSONArray();
	  String taskId = null;
	  String procInstId = dataInfoObj.getString("procInstId"), userId = dataInfoObj.getString("userId");
	  if(dataInfoObj.has("taskId")){
		  taskId = dataInfoObj.getString("taskId");
	  }
	  
	  //获取评估项
	  List<ActStudyVar> tempVars = this.getBusinessVar("scoreRules", userId, taskId, null, procInstId);
	  if(tempVars.size() > 0){
		  for(ActStudyVar tempVar : tempVars){
			  JSONObject tempObj = new JSONObject(), tempFile = new JSONObject();
			  //寻找此任务中我的待评估文件
			  List<ActStudyFile> tempFiles = this.getStudyFile(null, null, null, userId, tempVar.getTaskId(), null, procInstId);
			  if(tempFiles.size() > 0){
				  ActStudyFile studyFile = tempFiles.get(0);
				  tempFile.put("fileId", studyFile.getFileid());
				  tempFile.put("fileName", studyFile.getFilename());
				  tempFile.put("filePath", studyFile.getFilepath());
				  tempFile.put("optType", studyFile.getOpttype());
				  tempFile.put("userId", studyFile.getUserid());
				  tempFile.put("createTime", studyFile.getCreatetime());
			  }else{
				  tempFile = new JSONObject();
				  tempFile.put("errorMsg", "获取不到学生【"+dataInfoObj.getString("userId")+"】在任务【id:"+tempVar.getTaskId()+", name:"+tempVar.getTaskName().split("@")[0]+"】上的待评估的文件！");
			  }
			  
			  tempObj.put("taskId", tempVar.getTaskId());
			  tempObj.put("taskName", tempVar.getTaskName().split("@")[0]);
			  tempObj.put("procInstId", procInstId);
			  
			  String temp_scoreRules = tempVar.getVarValue();
			  temp_scoreRules = temp_scoreRules.replaceAll("\\n", "");
			  tempObj.put("scoreRules", new JSONArray(temp_scoreRules));
			  
			  tempObj.put("target", tempFile);
			  
			  resultArr.put(tempObj);
		  }
	  }
	  
	  return resultArr;
  }
  
  /**
   * 获取我学习的课程的总评信息
   * @param dataInfoObj
   * @return
   */
  public JSONObject getMySummarize(JSONObject dataInfoObj){
	  //获取总结
	  List<ActStudyVar> tempVars = this.getBusinessVar("summarize", dataInfoObj.getString("userId"), null, null, dataInfoObj.getString("procInstId"));
	  if(tempVars.size() > 0){
		  String temp_summarize = tempVars.get(0).getVarValue();
		  temp_summarize = temp_summarize.replaceAll("\\n", "");
		  
		  return  new JSONObject(temp_summarize);
	  }else{
		  
		  return  new JSONObject("{\"errorMsg\": \"找不到总评信息！\"}");
	  }
  }
  
  /**
   * 为评估项设置得分
   * @param dataInfoObj
   * @return
   */
  public String setScoreToEstimationItem(JSONObject dataInfoObj){
	  JSONObject resultObj = new JSONObject();
	  int scoreId = Integer.parseInt(dataInfoObj.getString("scoreId")) - 1, myScore = Integer.parseInt(dataInfoObj.getString("myScore"));
	  
	  List<ActStudyVar> tempVars = this.getBusinessVar("scoreRules", dataInfoObj.getString("userId"), dataInfoObj.getString("taskId"), null, dataInfoObj.getString("procInstId"));
	  if(tempVars.size() > 0){
		  ActStudyVar tempVar = tempVars.get(0);
		  String temp_scoreRules = tempVar.getVarValue();
		  temp_scoreRules = temp_scoreRules.replaceAll("\\n", "");
		  
		  JSONArray scoreRules = new JSONArray(temp_scoreRules);
		  JSONObject optObj = scoreRules.optJSONObject(scoreId);
		  
		  if(optObj.getInt("score") < myScore){
			  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：不能提交评分！你提交的评分【"+myScore+"分】大于评分规则的分数【"+optObj.getInt("score")+"分】！";
			  resultObj.put("isOk", false);
			  resultObj.put("errorMsg", errorMsg);
			  
			  SysLog.error(dataInfoObj.getString("userId"), dataInfoObj.toString(), errorMsg);
		  }else{
			  optObj.put("myScore", myScore);
			  scoreRules.put(scoreId, optObj);
			  
			  //存入数据库
			  ActStudyVar actStudyVar = new ActStudyVar(tempVar.getVarId(), scoreRules.toString());
			  this.actStudyVarService.updateByPrimaryKeySelective(actStudyVar);
			  
			  resultObj.put("isOk", true);
		  }
	  }else{
		  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：评分失败！查找不到你在此任务上的评估项信息！";
		  resultObj.put("isOk", false);
		  resultObj.put("errorMsg", errorMsg);
		  
		  SysLog.error(dataInfoObj.getString("userId"), dataInfoObj.toString(), errorMsg);
	  }
	  
	  return resultObj.toString();
  }
  
  /**
   * 设置总评信息
   * @param dataInfoObj
   * @return
   */
  public String setSummarize(JSONObject dataInfoObj){
	  JSONObject resultObj = new JSONObject(), summraize = new JSONObject();
	  summraize.put("sumScore", Integer.parseInt(dataInfoObj.getString("sumScore")));
	  summraize.put("comment", dataInfoObj.getString("comment"));
	  
	  List<ActStudyVar> tempVars = this.getBusinessVar("summarize", dataInfoObj.getString("userId"), null, null, dataInfoObj.getString("procInstId"));
	  if(tempVars.size() > 0){
		  //记录到数据库
		  ActStudyVar actStudyVar = new ActStudyVar(tempVars.get(0).getVarId(), summraize.toString());
		  this.actStudyVarService.updateByPrimaryKeySelective(actStudyVar);
		  
		  resultObj.put("isOk", true);
	  }else{
		  String errorMsg = "时间："+CourseEngine.getDateStr(null)+" <br />错误：设置总评失败！查找不到你的总评信息！";
		  resultObj.put("isOk", false);
		  resultObj.put("errorMsg", errorMsg);
		  
		  SysLog.error(dataInfoObj.getString("userId"), dataInfoObj.toString(), errorMsg);
	  }
	  
	  
	  return resultObj.toString();
  }
  
}