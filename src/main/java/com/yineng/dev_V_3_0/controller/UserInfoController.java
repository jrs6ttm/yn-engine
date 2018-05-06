package com.yineng.dev_V_3_0.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yineng.dev_V_3_0.model.CourseOrg;
import com.yineng.dev_V_3_0.service.ICourseOrgService;

/**
 * @RequestMapping("/userinfo") 具有层次关系，方法级的将在类一级@RequestMapping之一,
 * 如下面示例, 访问方法级别的@RequestMapping("/new"),则URL为 /userinfo/new
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController {//extends BaseSpringController
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	@Resource
	private ICourseOrgService courseOrgService;
	
	//private UserInfoManager userInfoManager;
	
	private final String LIST_ACTION = "redirect:/userinfo";
	
	/** 
	 * 通过spring自动注入
	 **/
	/*
	public void setUserInfoManager(UserInfoManager manager) {
		this.userInfoManager = manager;
	}
	*/
	
	@RequestMapping(value="/{username}/{password}") //用来处理前台的login请求  
    private @ResponseBody String hello(@PathVariable("username") String username, @PathVariable("password") String password){  
        return "Hello "+username+",Your password is: "+password;  
          
    } 
	
	//前后台测试, 支持表单POST和url GET请求, 返回页面
	@RequestMapping(value="/showCourseOrg")  
    public String showCourseOrg(HttpServletRequest request, Model model){  
        String lrnscnOrgId = request.getParameter("id"); 
        
        CourseOrg courseOrg = this.courseOrgService.getCourseOrgById(lrnscnOrgId);
        model.addAttribute("courseOrg", courseOrg);  
        return "index";  
    }
	//前后台ajax访问测试
	@RequestMapping(value="/showCourseOrg1", produces = {"application/json;charset=UTF-8"})  
    public @ResponseBody String showCourseOrg1(HttpServletRequest request){  
        String lrnscnOrgId = request.getParameter("id"); 
        
        CourseOrg courseOrg = this.courseOrgService.getCourseOrgById(lrnscnOrgId);
        return courseOrg.getInstanceName();  
    }
	
	//testJava系列是本系统与外系统http请求的交互测试
	@RequestMapping(value="/testJava", method = RequestMethod.POST, headers="Content-Type=application/json", produces = {"application/json;charset=UTF-8"})  
    public @ResponseBody String testJava(@RequestBody String myParams){
		
        //HttpSession session = request.getSession(); 
        System.out.println(myParams); 
        JSONObject studyObj = new JSONObject(myParams);
        
        return studyObj.toString();
    }
	
	@RequestMapping(value="/testJava1", method = RequestMethod.GET, headers="Content-Type=application/json", produces = {"application/json;charset=UTF-8"})  
    public @ResponseBody String testJava1(HttpServletRequest request){
        String bpmnInstanceId = request.getParameter("bpmnInstanceId"); 
        //HttpSession session = request.getSession(); 
        System.out.println(bpmnInstanceId); 
        
        return "{\"bpmnInstanceId\":\""+bpmnInstanceId+"\"}";
    }
	
	//本系统与外系统http请求的交互测试
	@RequestMapping(value="/testJava2")  
    public void testJava2(HttpServletRequest request, HttpServletResponse response){  
		response.setContentType("application/json;charset=utf-8");
		System.out.println(request.getServerName());
		System.out.println("PageOffice://|http://newengine3w.xuezuowang.com/NKTOForMyDemo/MyNTKODemo/MyFirstWordEditor.jsp?path=newengine3w.xuezuowang.com/ec_engine/fileManager/fileRead%3FfileId%3Da8d566ab-dec6-48bc-91b7-76727e7327e6%26userId%3D457bff90-135d-11e7-8c55-1f33be1fa07e%26createType%3Down%26ignoreme%3D&amp;fileName=零件结构及加工工艺分析.ppt&amp;permission=r&amp;userName=457bff90-135d-11e7-8c55-1f33be1fa07e"
				.replaceAll("ec_engine", "yn-engine").replaceAll("\\/NKTOForMyDemo\\/MyNTKODemo\\/MyFirstWordEditor\\.jsp\\?path", "/yn-engine/pageOffice/editFile.jsp?filePath"));
																  
		
		PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write("{\"bpmnInstanceId\":\"好好学习\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	//本系统与外系统ajax请求的交互测试
	@RequestMapping(value="/testJava3")  
    public @ResponseBody void testJava3(HttpServletRequest request, HttpServletResponse response){ 
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
        //String lrnscnOrgId = request.getParameter("id"); //GET、POST都是这样取参数
        
        //CourseOrg courseOrg = this.courseOrgService.getCourseOrgById(lrnscnOrgId);
        //System.out.println(courseOrg.getInstanceName());
        
        PrintWriter res = null;
        try {
        	res = response.getWriter();
        	res.write("{\"Course\":\"好好学习\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(res != null)
				res.close();
		}
    }
	
	/*表单提交方式
	@RequestMapping("login") //用来处理前台的login请求  
    private @ResponseBody String hello(  
            @RequestParam(value = "username", required = false)String username,  
            @RequestParam(value = "password", required = false)String password  
            ){  
        return "Hello "+username+",Your password is: "+password;  
          
    } 
    */
	
	/** 列表 */
	/*
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) {
		PageRequest<Map> pageRequest = new PageRequest(request,DEFAULT_SORT_COLUMNS);
		//pageRequest.getFilters(); //add custom filters
		
		Page page = this.userInfoManager.findByPageRequest(pageRequest);
		savePage(page,pageRequest,request);
		return new ModelAndView("/userinfo/list","userInfo",userInfo);
	}
	*/
	
	/** 进入新增 */
	/*
	@RequestMapping(value="/new")
	public ModelAndView _new(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) throws Exception {
		return new ModelAndView("/userinfo/new","userInfo",userInfo);
	}
	*/
	
	/** 显示 */
	@RequestMapping(value="/{id}")
	public ModelAndView show(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		/*
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		return new ModelAndView("/userinfo/show","userInfo",userInfo);
		*/
		
		return new ModelAndView("/userinfo/show");
	}
	
	/** 编辑 */
	@RequestMapping(value="/{id}/edit")
	public ModelAndView edit(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		/*
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		return new ModelAndView("/userinfo/edit","userInfo",userInfo);
		*/
		
		return new ModelAndView("/userinfo/show");
	}
	
	/** 保存新增 */
	/*
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) throws Exception {
		
		userInfoManager.save(userInfo);
		return new ModelAndView(LIST_ACTION);
		
	}
	*/
	
	/** 保存更新 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public ModelAndView update(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		/*
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		bind(request,userInfo);
		userInfoManager.update(userInfo);
		return new ModelAndView(LIST_ACTION);
		*/
		
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 删除 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		/*
		userInfoManager.removeById(id);
		return new ModelAndView(LIST_ACTION);
		*/
		return new ModelAndView(LIST_ACTION);
	}

	/** 批量删除 */
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView batchDelete(@RequestParam("items") Long[] items,HttpServletRequest request,HttpServletResponse response) {
		/*
		for(int i = 0; i < items.length; i++) {
			
			userInfoManager.removeById(items[i]);
		}
		return new ModelAndView(LIST_ACTION);
		*/
		return new ModelAndView(LIST_ACTION);
	}
	
}