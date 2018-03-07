package com.core.interceptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

import com.yineng.dev_V_3_0.service.ICourseOrgUserService;

@Service("myBusinessListener")
public class MyBusinessListener implements ExecutionListener, TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6433470998275398753L;
	
	@Resource
	private ICourseOrgUserService courseOrgUserService;

	//任务监听
	//为任务分配人员
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("创建任务：id:"+delegateTask.getId()+", 任务名:"+delegateTask.getName()+", 执行人:"+ delegateTask.getAssignee());
	}

	//执行监听
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		/*
		String activityId = execution.getCurrentActivityId();
		String[] arr = activityId.split("_");
		if(arr.length == 2){
			if(execution.getVariable(arr[1]) == null){
				String groupId = execution.getProcessBusinessKey();
				//默认一个角色
				List<String> uList = courseOrgUserService.getUsersOfRole(groupId, arr[1]);
				execution.setVariable(arr[1], uList);
			}
		}else{
			System.out.println("时间："+this.getDateStr()+"， 错误：任务["+execution.getCurrentActivityName()+"]上没有设置角色！");
			//throw new RuntimeException("时间："+this.getDateStr()+"， 错误：检测到任务["+execution.getCurrentActivityName()+"]上没有设置角色！");
		}
		*/
		
		String activityId = execution.getCurrentActivityId();
		String[] arr = execution.getCurrentActivityName().split("@");
		if(arr.length > 1){
			if(execution.getVariable(activityId) == null){
				String groupId = execution.getProcessBusinessKey();
				List<String> uList = new ArrayList<String>();
				int i=1;
				for(i=1;i<arr.length;i++){
					List<String> tempUsers = courseOrgUserService.getUsersOfRole(groupId, arr[i]);
					uList.addAll(tempUsers);
				}
				execution.setVariable(activityId, uList);
			}
		}else{
			System.out.println("时间："+this.getDateStr()+"， 错误：任务["+execution.getCurrentActivityName()+"]上没有设置角色！");
			//throw new RuntimeException("时间："+this.getDateStr()+"， 错误：检测到任务["+execution.getCurrentActivityName()+"]上没有设置角色！");
		}
		
	}
	
	private String getDateStr(){
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sf.format(date);
	}

}
