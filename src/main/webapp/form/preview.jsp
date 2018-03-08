<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
	    <base href="<%=basePath%>">
		<title>表单预览</title>
		
		<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery.min.js" type="text/javascript"></script>
	</head>
	<body>
	  <div class="edit-attr-item" style="margin-left:20%;margin-top:5%;margin-right:20%;">
	  	 <div class="form-group" align="center" style="margin-bottom:20px;">
		    <span style="font-size:20px;"><strong>ofo单车报损表</strong></span>
		  </div>
		 <div class="form-group">
		    <label for="Id">标题：</label>
		    <input type="text" class="form-control attr-item item-required" id="Id" placeholder="请输入名称">
		  </div>
		  <div class="form-group">
		    <label for="Name">联系电话：</label>
		    <input type="text" class="form-control attr-item item-required" id="Name" placeholder="请输入名称">
		  </div>
		  <div class="form-group">
		    <label for="Type">严重程度</label>
		    <select class="form-control attr-item" id="Type">
		      <option value="string">不严重</option>
		      <option value="long">一般</option>
		      <option value="boolean">严重</option>
		    </select>
		  </div>
		  <div class="form-group">
		    <label for="Default-Value">地点：</label>
		    <input type="text" class="form-control attr-item" id="Default-Value">
		  </div>
		  <div class="form-group">
		    <label for="Default-Value">内容：</label>
		    <input type="text" class="form-control attr-item" id="Default-Value">
		  </div>
	 </div>
	</body>
</html>