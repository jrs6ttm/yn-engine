package com.web.formManager.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.formManager.entity.ActForm;
import com.web.formManager.entity.ActFormAttr;
import com.web.formManager.entity.ActFormBind;
import com.web.formManager.entity.ActFormRun;
import com.web.formManager.service.IActFormAttrService;
import com.web.formManager.service.IActFormBindService;
import com.web.formManager.service.IActFormRunService;
import com.web.formManager.service.IActFormService;

@Controller
@RequestMapping("/form")
public class FormManagerController {
	
	@Autowired
	private IActFormAttrService actFormAttrService;
	
	@Autowired
	private IActFormService actFormService;
	
	@Autowired
	private IActFormBindService actFormBindService;
	
	@Autowired
	private IActFormRunService actFormRunService;
	
	/**
	 * 获取日期字符串
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	private  String getDateStr(Date date, String dateFormat){
		if(date == null){
			date = new Date();
		}
		if(dateFormat == null){
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		
		return sf.format(date);
	}
	
	//----------------------------------------- formAttr opt start -------------------------------------
	@RequestMapping(value="/deleteFormAttr")
	public @ResponseBody String deleteFormAttr(@ModelAttribute ActFormAttr record) {
		
		int result = actFormAttrService.deleteFormAttr(record);
		
		return result < 0 ? "error" : "success";
	}

	
	@RequestMapping(value="/saveFormAttr")
	public @ResponseBody String saveFormAttr(@ModelAttribute ActFormAttr record) {
		String formAttrId = record.getAttrId();
		if(formAttrId == null || "".equals(formAttrId.trim())){//new
			formAttrId = UUID.randomUUID().toString();
			record.setAttrId(formAttrId);
			record.setCreateTime(getDateStr(null, null));
			record.setLastUpdateTime(record.getCreateTime());
			
			int result = actFormAttrService.insertSelective(record);
			if(result < 0){
				formAttrId = "error";
			}
		}else{
			record.setLastUpdateTime(getDateStr(null, null));
			int result = actFormAttrService.updateByPrimaryKeySelective(record);
			if(result < 0){
				formAttrId = "error";
			}
		}
		
		return formAttrId;
	}
	
	@RequestMapping(value="/getFormAttr")
	public @ResponseBody ActFormAttr getFormAttr(HttpServletRequest request) {
		String attrId = request.getParameter("attrId"); 
		
		return actFormAttrService.selectByPrimaryKey(attrId);
	}

	@RequestMapping(value="/getFormAttrList", produces = {"application/json;charset=UTF-8"}) 
	public @ResponseBody List<ActFormAttr> getFormAttrList(@ModelAttribute ActFormAttr record) {
		
		//String formId = request.getParameter("formId"); 
		
		return actFormAttrService.getFormAttrList(record);
	}

	
	//----------------------------------------- formAttr opt end -------------------------------------
	
	//----------------------------------------- form opt start -------------------------------------
	@RequestMapping(value="/deleteForm")
	public @ResponseBody String deleteForm(HttpServletRequest request) {
		String formId = request.getParameter("formId");
		int result2 = -1;
		
		ActFormAttr actFormAttr = new ActFormAttr();
		actFormAttr.setFormId(formId);
		int result = actFormAttrService.deleteFormAttr(actFormAttr);
		if(result >=0){
			result2 = actFormService.deleteByPrimaryKey(formId);
		}
		
		return result2 < 0 ? "error" : "success";
	}

	
	@RequestMapping(value="/saveForm")
	public @ResponseBody String saveForm(@ModelAttribute ActForm record) {
		String formId = record.getFormId();
		if(formId == null || "".equals(formId.trim())){//new
			formId = UUID.randomUUID().toString();
			record.setFormId(formId);
			record.setCreateTime(getDateStr(null, null));
			record.setLastUpdateTime(record.getCreateTime());
			
			int result = actFormService.insertSelective(record);
			if(result < 0){
				formId = "error";
			}
		}else{//update
			record.setLastUpdateTime(getDateStr(null, null));
			int result = actFormService.updateByPrimaryKeySelective(record);
			if(result < 0){
				formId = "error";
			}
		}
		
		return formId;
	}
	
	@RequestMapping(value="/multiplex")  
	public @ResponseBody String multiplex(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setContentType("application/json;charset=utf-8"); 
		String formId = request.getParameter("formId"); 
		
		return actFormService.multiplexForm(formId);
	}
	
	@RequestMapping(value="/getForm", produces = {"application/json;charset=UTF-8"})  
	public @ResponseBody ActForm getForm(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setContentType("application/json;charset=utf-8"); 
		String formId = request.getParameter("formId"); 
		
		return actFormService.selectByPrimaryKey(formId);
	}
	
	@RequestMapping(value="/checkFormName", produces = {"application/json;charset=UTF-8"})  
	public @ResponseBody ActForm checkFormName(HttpServletRequest request) {
		
		String formName = request.getParameter("formName"); 
		
		return actFormService.checkFormName(formName);
	}

	@RequestMapping(value="/getFormList", produces = {"application/json;charset=UTF-8"}) 
	public @ResponseBody List<ActForm> getFormList(HttpServletResponse response, @ModelAttribute ActForm record) {
		//String userId = request.getParameter("userId"); 
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setContentType("application/json;charset=utf-8"); 
		
		return actFormService.getFormList(record);
	}
	
	//----------------------------------------- form opt end -------------------------------------
	
	//----------------------------------------- formBind opt start -------------------------------------
	@RequestMapping(value="/saveFormBind")
	public @ResponseBody void saveFormBind(HttpServletResponse response, @ModelAttribute ActFormBind record) {
		String cId = record.getcId();
		if(cId == null || "".equals(cId.trim())){//new
			cId = UUID.randomUUID().toString();
			record.setcId(cId);;
			record.setBindStatus("1");
			record.setBindTime(getDateStr(null, null));
			int result = actFormBindService.insertSelective(record);
			
			ActForm actForm = new ActForm();
			actForm.setFormStatus("1");
			actForm.setLastUpdateTime(getDateStr(null, null));
			int result1 = actFormService.updateByPrimaryKeySelective(actForm);
			
			if(result < 0 || result1 < 0){
				cId = "error";
			}
		}else{//update
			int result = actFormBindService.updateByPrimaryKeySelective(record);
			if(result < 0){
				cId = "error";
			}
		}
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setContentType("application/json;charset=utf-8"); 
		PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(cId);
		} catch (IOException e) {
			System.out.println("saveFormRun响应失败!");
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
	}
	
	//----------------------------------------- formBind opt end -------------------------------------
	
	//----------------------------------------- formRun opt start -------------------------------------
	@RequestMapping(value="/deleteFormRun")
	public @ResponseBody int deleteFormRun(String cid) {
		return actFormRunService.deleteByPrimaryKey(cid);
	}

	@RequestMapping(value="/saveFormRun")
	public @ResponseBody void saveFormRun(HttpServletResponse response, @ModelAttribute ActFormRun record) { 
		String cId = record.getcId();
		if(cId == null || "".equals(cId.trim())){//new
			cId = UUID.randomUUID().toString();
			record.setcId(cId);
			record.setCreateTime(getDateStr(null, null));
			record.setLastUpdateTime(record.getCreateTime());
			
			int result = actFormRunService.insertSelective(record);
			if(result < 0){
				cId = "error";
			}
		}else{//update
			record.setLastUpdateTime(getDateStr(null, null));
			int result = actFormRunService.updateByPrimaryKeySelective(record);
			if(result < 0){
				cId = "error";
			}
		}
		
		//TODO 向引擎设置流程变量 
		/*
		JSONObject processVars = new JSONObject(record.getProcessVars());
		Map<String, String> variables = new HashMap<String, String>();
		Iterator<String> keys = processVars.keys();
		while(keys.hasNext()){
			String key = keys.next();
			variables.put(key, processVars.getString(key));
		}
		*/
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setContentType("application/json;charset=utf-8"); 
		PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write(cId);
		} catch (IOException e) {
			System.out.println("saveFormRun响应失败!");
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
		
	}

	@RequestMapping(value="/getFormRun", produces = {"application/json;charset=UTF-8"})  
	public @ResponseBody ActFormRun getFormRun(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setContentType("application/json;charset=utf-8"); 
		String cId = request.getParameter("cId"); 
		
		return actFormRunService.selectByPrimaryKey(cId);
	}
	
	@RequestMapping(value="/getFormRunList", produces = {"application/json;charset=UTF-8"})
	public List<ActFormRun> getFormRunList(@ModelAttribute ActFormRun record) {
		return actFormRunService.getFormRunList(record);
	}
	
	//----------------------------------------- formRun opt end -------------------------------------
	
	
}
