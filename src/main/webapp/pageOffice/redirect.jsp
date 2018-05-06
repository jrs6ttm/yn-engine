<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String userName = "易能教育";
    String fileName = "";
	//得到网站的根址URL
	String strBasePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<!-- base href用于Head内，告诉页面所有连接都是基于该链接的地址 -->
	<base href="<%=strBasePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script src="script/jquery.min.js" type="text/javascript"></script>
	<title>welcome</title>

<% 
	String permission = request.getParameter("permission");
	if(permission == null || "".equals(permission.trim())){
		permission = "rw";
	}

   /*这两个字段包含中文的话会乱码
	fileName = request.getParameter("fileName");
	if(fileName != null && !"".equals(fileName)){
		fileName = new String(fileName.trim().getBytes("ISO-8859-1"), "utf-8");
	}
	System.out.println("fileName : " + fileName);
	
	String userName = request.getParameter("userName");
	if(userName != null && !"".equals(userName)){
		userName = new String(userName.trim().getBytes("ISO-8859-1"), "utf-8");
	}
	System.out.println("userName : " + userName);
	*/
	//兼容旧课程的旧资料链接问题
	//newengine3w.xuezuowang.com/yn-engine/fileManager/fileRead?fileId=a8d566ab-dec6-48bc-91b7-76727e7327e6&userId=457bff90-135d-11e7-8c55-1f33be1fa07e&createType=own&ignoreme=
	String path = request.getParameter("path");
	System.out.println("path : " + path);
	
	String [] params = path.split("&");
	String fileId = params[0].split("\\?")[1].split("=")[1];
	String userId = params[1].split("=")[1];
%>
	<script type="text/javascript">
	    var fileId = '<%=fileId%>';
		$.ajax({
	        url: '/yn-engine/fileManager/getOwnFiles',
	        data:{fileId : fileId},
	        cache: false,
	        async:false,
	        type: 'POST',
	        success : function(result){
	            if(result){
	                result = JSON.parse(result);
	                filePath = result[0].filePath;
	                window.location.href = "/yn-engine/pageOffice/editFile.jsp?filePath=" + filePath + "&permission=<%=permission%>";
	            }
	        }
	    });
	</script>
</head>
<body>
	
</body>
</html>