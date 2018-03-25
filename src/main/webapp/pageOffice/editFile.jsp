<%@ page import="java.io.File, com.util.FileUtils"%>
<%@ page language="java"
	import="com.zhuozhengsoft.pageoffice.*,java.util.*,java.io.*,javax.servlet.*,javax.servlet.http.*"
	pageEncoding="utf-8"%>

<!-- 引入下语句可以在前端页面支持标签：po:PageOfficeCtrl -->
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>

<!-- 下面是页面是的JAVA处理逻辑 -->
<%
	String userName = "易能教育";
	//得到网站的根址URL
	String strBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	
	String filePath = request.getParameter("filePath");
	System.out.println("filePath : " + filePath);
	if(filePath != null && !"".equals(filePath.trim())){
		String permission = request.getParameter("permission");
		if(permission == null || "".equals(permission.trim())){
			permission = "rw";
		}
		
		String fileName = "";
		if(filePath != null && !"".equals(filePath)){
			//******************************卓正PageOffice组件的使用*******************************
			
			//创建控件的对象
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			
		    ////此行必须
			poCtrl1.setServerPage(request.getContextPath() + "/poserver.zz");

			poCtrl1.setCustomToolbar(false);//隐藏自定义工具栏
			
			//指定保存文件后台处理JSP页面，如要保存文件，此行必须
			poCtrl1.setSaveFilePage("./saveFile.jsp?filePath=" + filePath);

			poCtrl1.setCaption(fileName);
		    
			String abPath = FileUtils.getFileRootDir() + File.separator + filePath;
			if (permission.equals("rw")) {
		        //打开文件
				if (filePath.endsWith(".docx") || filePath.endsWith(".doc"))
				{
					poCtrl1.webOpen("file://" + abPath, OpenModeType.docNormalEdit, userName);
				}
				else if (filePath.endsWith(".xlsx") || filePath.endsWith(".xls"))
				{
					poCtrl1.webOpen("file://" + abPath, OpenModeType.xlsNormalEdit, userName);
				}
				else if (filePath.endsWith(".pptx") || filePath.endsWith(".ppt"))
				{
					poCtrl1.webOpen("file://" + abPath, OpenModeType.pptNormalEdit, userName);
				}
			} else if (permission.equals("r")) {
				//打开文件
				if (filePath.endsWith(".docx") || filePath.endsWith(".doc"))
				{
					poCtrl1.webOpen("file://" + abPath, OpenModeType.docReadOnly, userName);
				}
				else if (filePath.endsWith(".xlsx") || filePath.endsWith(".xls"))
				{
					poCtrl1.webOpen("file://" + abPath, OpenModeType.xlsReadOnly, userName);
				}
				else if (filePath.endsWith(".pptx") || filePath.endsWith(".ppt"))
				{
					poCtrl1.webOpen("file://" + abPath, OpenModeType.pptReadOnly, userName);
				}
			}	
			
			poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		}
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<!-- base href用于Head内，告诉页面所有连接都是基于该链接的地址 -->
		<base href="<%=strBasePath%>">
		
		<title>PageOffice</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>

	<body>
	<po:PageOfficeCtrl id="PageOfficeCtrl1" />
		<script>
    window.onunload = function() {
		var dirty = document.getElementById("PageOfficeCtrl1").IsDirty;
		if (dirty) {
	     	document.getElementById("PageOfficeCtrl1").WebSave();
		}
    }
  </script>
	</body>
</html>
