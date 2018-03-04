<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Socket Test</title>
</head>
<body>
	<h2>Socket Test</h2>
	<script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
	<script type="text/javascript" src="http://cdn.bootcss.com/sockjs-client/1.0.0/sockjs.js"></script>
    <script type="text/javascript">
	    var websocket = null;
	    var url = "http://localhost:8022/yn-engine/socket/hello";
	    
	    function connect(){
	    	if(!url){
	    		console.log("url缺失!");
	    		return false;
	    	}
	    	
	    	websocket = new SockJS(url);
	    	
	    	websocket.onopen = function(evnt) {
		        console.log("服务器已连接!");
		        console.log(evnt);
		        alert("服务器已连接!");
		    };
		    websocket.onmessage = function(evnt) {
		        console.log("收到服务器的消息:" + evnt.data);
		        console.log(evnt);
		        alert("收到服务器的消息:" + evnt.data);
		        document.getElementById("acceptMessage").value = evnt.data;
		        //messageHandler("收到服务器的消息:" + evnt.data);
		    };
		    websocket.onerror = function(evnt) {
		        console.log("服务器连接出错!");
		        console.log(evnt);
		        alert("服务器连接出错!");
		    };
		    websocket.onclose = function(evnt) {
		    	alert(evnt.code + " : " + evnt.reason);
		    	if(evnt.code == 1000){
		    		console.log("你已主动退出连接服务器!");
			        console.log(evnt);
		    	}
		    	if(evnt.code == 1002){
		    		console.log("无法连接到服务器!");
			        console.log(evnt);
		    	}
		    	if(evnt.code == 1006){
		    		console.log("服务器连接中断!");
			        console.log(evnt);
		    	}
		    }
	    }
	    
	    function disconnect() {
	        if (websocket) {   
	        	websocket.close();//调用后台afterConnectionClosed方法
	        	websocket = null; 
	        } else {  
	            alert("退出连接失败!");  
	        }  
	    }
	    
	    function sendMessage() {
	        if (websocket && websocket.readyState == 1) {   
	            websocket.send(document.getElementById("inputMsg").value);//调用后台handleTextMessage方法
	            alert("发送成功!");  
	        } else {  
	            alert("连接失败!");  
	        }  
	    }
	    
	    window.close = function(){
	    	console.log("窗口已关闭!");
	    	websocket.close();
	    }
	</script>
	1,<button onclick="connect();">进入连接</button>
	<br />
	<br />
	2,发送消息：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
    <button onclick="sendMessage();">发送</button>
    <br />
    <br />
    3,收到消息 : <input type="text" id="acceptMessage" readonly></input>
    <br />
    <br />
    4,<button onclick="disconnect();">退出连接</button>
</body>
</html>