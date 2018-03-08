<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
	    <base href="<%=basePath%>">
		<title>form表单测试</title>
		<script src="script/socket.io.js" type="text/javascript"></script>
		<script src="script/jquery.min.js" type="text/javascript"></script>
		<script>
		    $(function(){
				
		        var socket = io.connect( "http://192.168.1.25:25000");
				
				socket.on('study_eqa318390978fa4@d22b-11e6-b67d', function (data) {
					console.log(data);
					var currInput = data.currTask.taskInfo.data.input;
					var taskName = data.currTask.taskInfo.data.taskName;
					$('#task-name').append('<h3>任务 <strong>' + taskName + '</strong> 的表单：</h3><br />');
					$('#task-form').append(currInput.formHtml);
					}
				);
				socket.emit("study", 
								{
									processDefinitionId: 'Process_be5b0350-0f77-11e7-af85-ef8f843fb11a:8:455104',
									courseInstanceId : 'eqa318390978fa4@d22b-11e6-b67d',
									isSingle:1,
									assignee : 'zhangll',
									userName: 'zhangll',
									taskId: ''
								}		
							);
				
				
				
				$.post('http://192.168.1.25:8080/ec_engine/course/deploy_test',
						{
							"courseInstanceId":	"eqa318390978fa4@d22b-11e6-b67d",
							"bpmnInstanceId":	"eqa318390978fa4",
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
				
			});
			var submitMyForm1 = function(){
				//engineHost, formId, callback
				submitMyForm('http://192.168.1.25:8080', '3144ac38-e09f-49d2-b693-e784742c9a65',function(formId){
					if(formId && formId != 'error'){
						console.log(formId);
						$('#savedform').text(formId);
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