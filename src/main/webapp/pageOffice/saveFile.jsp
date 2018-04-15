<%@ page import="java.io.File, com.util.FileUtils"%>
<%@page import="java.io.FileInputStream"%>
<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>

<!-- 保存文件的处理 -->
<%
	FileInputStream strmFile = null;
	com.zhuozhengsoft.pageoffice.FileSaver fs = null;

	String filePath = request.getParameter("filePath");

	if(filePath != null && !"".equals(filePath.trim())){
		fs = new FileSaver(request,response);
		fs.saveToFile(FileUtils.getFileRootDir() + File.separator + filePath);
		fs.close();
	}
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>保存文件的页面</title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>  
  <body>
  </body>
</html>
