package com.yineng.dev_V_3_0.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yineng.dev_V_3_0.service.IUserAndGroupService;

@Controller
@RequestMapping("/userAndGroup")
public class UserAndGroupController {
	@Resource
	public IUserAndGroupService userAndGroupService;
	
	@RequestMapping("/createUser")
	public @ResponseBody void createUser(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String userId = request.getParameter("userId");
		userAndGroupService.createUser(userId);
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write("{\"type\":\"ok\"}");
		} catch (IOException e) {
			System.out.println("createUser error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	@RequestMapping("/createGroup")
	public @ResponseBody void createGroup(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String groupId = request.getParameter("groupId");
		userAndGroupService.createGroup(groupId);
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write("{\"type\":\"ok\"}");
		} catch (IOException e) {
			System.out.println("createGroup error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	@RequestMapping("/createMembership")
	public @ResponseBody void createMembership(HttpServletRequest request, HttpServletResponse response){
		  
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");
		userAndGroupService.createMembership(userId, groupId);
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write("{\"type\":\"ok\"}");
		} catch (IOException e) {
			System.out.println("createMembership error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	@RequestMapping("/deleteGroup")
	public @ResponseBody void deleteGroup(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String groupId = request.getParameter("groupId");
		userAndGroupService.deleteGroup(groupId);
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write("{\"type\":\"ok\"}");
		} catch (IOException e) {
			System.out.println("deleteGroup error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	@RequestMapping("/deleteUser")
	public @ResponseBody void deleteUser(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String userId = request.getParameter("userId");
		userAndGroupService.deleteUser(userId);
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write("{\"type\":\"ok\"}");
		} catch (IOException e) {
			System.out.println("deleteUser error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
	
	@RequestMapping("/deleteMembership")
	public @ResponseBody void deleteMembership(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");
		userAndGroupService.deleteMembership(userId, groupId);
		
		PrintWriter res = null;
		try {
			res = response.getWriter();
			res.write("{\"type\":\"ok\"}");
		} catch (IOException e) {
			System.out.println("deleteMembership error!");
			e.printStackTrace();
		}finally{
			if(res != null){
				res.close();
			}
		}
	}
}
