<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>文件基本操作测试</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="margin-left:35%;margin-right:35%;margin-top:5%;">
     <!-- enctype 默认是 application/x-www-form-urlencoded -->
     <div>
     	<p>上传文件测试：</p>
	     <form action="fileManager/ownFileUpload" enctype="multipart/form-data" method="post" >
              <p>参数：</p>
              <!-- <input type="text" name="imageSizes" value="125m125_75m75"><br/> -->
              <span>用户ID: </span><input type="text" name="userId"><br/><br/>
              <span>createType: </span><input type="text" name="createType"><br/><br/>
              <span>文      件: </span><input type="file" name="file"><br/><br/>
              <input type="submit" value="上 传"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>上传图片文件并裁剪测试：</p>
	     <form action="http://newengine3w.xuezuowang.com/ec_engine/fileManager/fileUpload" enctype="multipart/form-data" method="post" >
              <p>参数：</p>
              <!-- <input type="text" name="imageSizes" value="125m125_75m75"><br/> -->
              <span>用户ID : </span><input type="text" name="userId"><br/><br/>
              <span>裁剪尺寸 : </span><input type="text" name="imageSizes" value="125m125_75m75"><br/><br/>
              <span>文      件   : </span><input type="file" name="file1"><br/><br/>
              <input type="submit" value="上 传"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>创建新文件测试：</p>
	     <form action="fileManager/fileCopy"  method="post" >
              <p>参数：</p>
              <!-- <input type="text" name="imageSizes" value="125m125_75m75"><br/> -->
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>新文件名: </span><input type="text" name="targetName" placeholder="123.html"><br/><br/>
              <input type="submit" value="创 建"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>复制文件测试：</p>
	     <form action="fileManager/fileCopy"  method="post" >
              <p>参数：</p>
              <!-- <input type="text" name="imageSizes" value="125m125_75m75"><br/> -->
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>复制路径: </span><input type="text" name="filePath"><br/><br/>
              <span>生成文件: </span><input type="text" name="targetName" placeholder="123.html"><br/><br/>
              <input type="submit" value="复 制"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>下载文件测试：</p>
	     <form action="http://newengine3w.xuezuowang.com/ec_engine/fileManager/fileDownload"  method="post" >
              <p>参数：</p>
              <!-- <input type="text" name="imageSizes" value="125m125_75m75"><br/> -->
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>下载路径: </span><input type="text" name="filePath"><br/><br/>
              <span>生成文件: </span><input type="text" name="fileName" placeholder="123.html"><br/><br/>
              <input type="submit" value="下 载"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>删除文件测试：</p>
	     <form action="fileManager/fileDelete"  method="post" >
              <p>参数：</p>
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>文件路径: </span><input type="text" name="filePath"><br/><br/>
              <input type="submit" value="删 除"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>列出文件(目录)测试：</p>
	     <form action="fileManager/fileList"  method="post" >
              <p>参数：</p>
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>目录路径: </span><input type="text" name="filePath"><br/><br/>
              <input type="submit" value="列 出"/>
	     </form>
     </div>
     <br /><hr />
     <div>
     	<p>读取文件测试：</p>
	     <form action="http://newengine3w.xuezuowang.com/ec_engine/fileManager/fileRead"  method="post" >
              <p>参数：</p>
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>下载路径: </span><input type="text" name="filePath"><br/><br/>
              <input type="submit" value="读 取"/>
	     </form>
     </div>
     <br /><hr />
      <div>
     	<p>读取富文本文件内容测试：</p>
	     <form action="fileManager/fileContentRead"  method="post" >
              <p>参数：</p>
              <span>用户  ID: </span><input type="text" name="userId"><br/><br/>
              <span>下载路径: </span><input type="text" name="filePath"><br/><br/>
              <input type="submit" value="读 取"/>
	     </form>
     </div>
     
     <br /><hr />
     <p>例子：</p>
    <object type="video/x-msvideo" data="http://localhost:8080/ec_engine/fileManager/fileRead?userId=zhangll&fileId=6d8c919c-8cf1-4611-a65b-f9a94702aa34&createType=own"  width="320" height="240">
		<param name="src"value="http://localhost:8080/ec_engine/fileManager/fileRead?userId=zhangll&fileId=6d8c919c-8cf1-4611-a65b-f9a94702aa34&createType=own" />
        <param name="autostart" value="true" />
        <param name="controller" value="true" />
	</object> 
     <div>
     	<a href="http://localhost:8080/ec_engine/fileManager/fileRead?userId=zhangll&fileId=6d8c919c-8cf1-4611-a65b-f9a94702aa34&createType=own">读取视频</a>
     </div>
     <!--
     <div>
     	<img alt="加载中..." src="fileManager/fileRead?userId=tangfz&filePath=tangfz\\own\\2017-02\\images\\7c3ccac0-68a5-46dd-a9da-6d874a1dc621.jpg"></img> 
     </div>
     <br />
     -->
     <div>
     	<iframe width="100%" height="650px" src="http://localhost:8080/ec_engine/fileManager/fileRead?userId=zhangll&fileId=6d8c919c-8cf1-4611-a65b-f9a94702aa34&createType=own"></iframe>
     </div>
        
  </body>
</html>