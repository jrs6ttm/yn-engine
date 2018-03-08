<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
	    <base href="<%=basePath%>">
		<title>课程播放器测试</title>
		<script src="script/socket.io.js" type="text/javascript"></script>
		<script src="script/jquery.min.js" type="text/javascript"></script>
		<script>
		    $(function(){
		    	var url = "http://localhost:9100/yn-engine/socket/hello";
		        var socketIo = null;
		        
		        function connect_socketio(){
		        	socketIo = io.connect(url);
		        	
		        	socket.on('study_2f6dc550-3066-11e7-a28a-91ed89d81569@64dc8311-827d-47fb-8206-b1fca5f592f2', function (data) {
						console.log(data);
						var currInput = data.currTask.taskInfo.data.input;
						var taskName = data.currTask.taskInfo.data.taskName;
						$('#task-name').append('<h3>任务 <strong>' + taskName + '</strong> 的任务内容：</h3><br />');
						$('#task-info').append(JSON.stringify(data));
						}
					);
		        	
					socket.emit("study", 
									{
										processDefinitionId: 'Process_2f6dc550-3066-11e7-a28a-91ed89d81569:34:22559',
										courseInstanceId : '2f6dc550-3066-11e7-a28a-91ed89d81569@64dc8311-827d-47fb-8206-b1fca5f592f2',
										isSingle:1,
										assignee : '457bff90-135d-11e7-8c55-1f33be1fa07e',
										userName: 'ynlisa',
										taskId: ''
									}		
								);
					
		        }
				
		        /*435101
				socket.on('study_2f6dc550-3066-11e7-a28a-91ed89d81569@64dc8311-827d-47fb-8206-b1fca5f592f2', function (data) {
					console.log(data);
					var currInput = data.currTask.taskInfo.data.input;
					var taskName = data.currTask.taskInfo.data.taskName;
					$('#task-name').append('<h3>任务 <strong>' + taskName + '</strong> 的任务内容：</h3><br />');
					$('#task-info').append(JSON.stringify(data));
					}
				);
				socket.emit("study", 
								{
									processDefinitionId: 'Process_2f6dc550-3066-11e7-a28a-91ed89d81569:34:22559',
									courseInstanceId : '2f6dc550-3066-11e7-a28a-91ed89d81569@64dc8311-827d-47fb-8206-b1fca5f592f2',
									isSingle:1,
									assignee : '457bff90-135d-11e7-8c55-1f33be1fa07e',
									userName: 'ynlisa',
									taskId: ''
								}		
							);
				
				
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
		</script>
	</head>
	<body>
		<div style="margin-left:15%;margin-top:5%;margin-right:15%;">
			<div id="task-name"></div>
			<div id="task-info"></div>
		</div>
		<br />
	</body>
</html>