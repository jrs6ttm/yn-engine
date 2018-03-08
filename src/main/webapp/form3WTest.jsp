<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
	    <base href="<%=basePath%>">
		<title>外网form表单测试</title>
		<script src="script/socket.io.js" type="text/javascript"></script>
		<script src="script/jquery.min.js" type="text/javascript"></script>
		<script>
		    $(function(){
				
		        var socket = io.connect( "http://oldengine3w.xuezuowang.com");
				
				socket.on('study_fa329ea0-3c6e-11e7-b889-d5397af687d0@f88231f7-518d-4b2c-83d4-9e5462deeb71', function (data) {
					console.log(data);
					var currInput = data.currTask.taskInfo.data.input;
					var taskName = data.currTask.taskInfo.data.taskName;
					$('#task-name').append('<h3>任务 <strong>' + taskName + '</strong> 的表单：</h3><br />');
					$('#task-form').append(currInput.formHtml);
					}
				);
				socket.emit("study", 
								{
									processDefinitionId: 'Process_fa329ea0-3c6e-11e7-b889-d5397af687d0:1:20004',
									courseInstanceId : 'fa329ea0-3c6e-11e7-b889-d5397af687d0@f88231f7-518d-4b2c-83d4-9e5462deeb71',
									isSingle:1,
									assignee : '9d036ac0-200d-11e7-9cfc-63ebde8b8323',
									userName: 'yetao',
									taskId: ''
								}		
							);
				
				/*435101
				$.post('http://192.168.1.25:8080/ec_engine/course/deploy_test',
						{
							"courseInstanceId":	"ada770f0-d22a-11e6-80f3@d24458a0-d22b-11e6-b67d",
							"bpmnInstanceId":	"ada770f0-d22a-11e6-80f3",
							"ecgeditorHost":	"authoring.learn.com",
							"isCooperation": "0"
						},
						function(data){
							if(data.processDefinitionId == ''){
								console.log(data.errorMsg);
							}else{
								console.log(data);
							}
						}
						);
				*/	
			});
			var submitMyForm1 = function(){
				//engineHost, formId, callback
				submitMyForm('http://newengine3w.xuezuowang.com', '2008ce86-42b3-4bba-a1b3-23b845569f6c',function(formId){
					if(formId && formId != 'error'){
						console.log(formId);
					}
				});
			};	
		</script>
	</head>
	<body>
		<div style="margin-left:15%;margin-top:5%;margin-right:15%;">
			<div id="task-name"></div>
			<div id="task-form"></div>
		</div>
		<br />
		<div align="center"><button type="button" onclick="submitMyForm1();" class="btn btn-info" >提 交</button></div>
		<br />
		<div style="margin-left:15%;margin-top:5%;margin-right:15%;">
			<div><h3>保存的表单：</h3></div>
			<div id="savedform"></div>
		</div>
	</body>
</html>