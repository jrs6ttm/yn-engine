/**
 * 
 */
/*
	 errorMsg: 错误信息
	 infoMsg:  通知信息
	 studyMsg: 任务完成信息
 */
/*
 *
 * submitDataRequest = 
 * {
 * 		taskId : "",
 * 		assignee : "",
 * 		submitType : "file", //"form", "question"
 * 
 * 		fileInfo : {
 * 			filePath:"", fileName:"", optType:"", userId:"", userName:""
 * 		},
 * 
 * 		formInfo : {
 * 
 * 		},
 * 
 * 		question : {
 * 
 * 		}
 * }
 */

/*
 * 
     系统里的变量：
     	output_taskDefinKey  任务的输出变量
     	scoreRules_userId    任务的评分规则变量
     	summarize_userId     任务的总评变量
		file_userId = {filePath:"", fileName:"", optType:"", userId:"", userName:"", createTime: "xxx"}	文件变量	业务变量	局部变量
		string_userId = {fromUserId:"", fromUserName:"", toUserId:"", toUserName:"", comment:"", time:""}	表单变量	业务变量	局部变量
		form_userId  	文件变量	业务变量	局部变量
		exam_userId		试题变量	业务变量	局部变量
		//vm_userId 	虚拟机变量	业务变量	局部变量
		vm_group =  	虚拟机组变量	业务变量	全局变量
		
		//"file"文件变量,"string"评论变量,"question"试题变量,"form"表单变量,"processVar"流程变量
		output_taskDefinKey = output:{type:"file", name:"", requirement:true//false}	输出变量	全局变量
		process_xxx 流程变量	全局变量
		
		vm_group = [
					    {
					        "courseID": 0,
					        "networks": [
					            "ed7a4156-4b85-8723-904b-252c19f56d54"
					        ],
					        "role": 3,
					        "routerFloag": true,
					        "userID": "supervisor",
					        "uuid": "ce08aaef-a6c1-ba23-f348-2ef9bad63d28"
					    },
					    {
					        "courseID": 1473838423847,
					        "courseName": "weixin_文件上传漏洞原理解析及修复_文件上传漏洞_课程_v1",
					        "networks": [],
					        "role": 3,
					        "routerFloag": false,
					        "templateUUID": "3d687bf8-30b2-883e-f736-6c8d9bdca526",
					        "userID": "supervisor",
					        "uuid": "6c12a66b-f552-97a2-01da-a6350c4a070b"
					    },
					    {
					        "courseID": 1473838423847,
					        "courseName": "weixin_文件上传漏洞原理解析及修复_文件上传漏洞_课程_v1",
					        "networks": [],
					        "role": 1,
					        "routerFloag": false,
					        "templateUUID": "8277a6a2-6023-d1d7-b175-8d223fb8ef31",
					        "userID": "user1_test",
					        "uuid": "3c5c7ff0-d5c9-e95a-c715-8afe2d0a32b6"
					    },
					    {
					        "courseID": 1473838423847,
					        "courseName": "weixin_文件上传漏洞原理解析及修复_文件上传漏洞_课程_v1",
					        "networks": [],
					        "role": 1,
					        "routerFloag": false,
					        "templateUUID": "8277a6a2-6023-d1d7-b175-8d223fb8ef31",
					        "userID": "user2_test",
					        "uuid": "893f50dc-5311-aed4-d771-a083d54a9675"
					    }
					]
*/
/*
		  currTask.taskInfo.data = 
			  {
				  taskId:"",//taskDefinitionKey
				  taskName:"",
				  taskDescription:"这是一段引导文",
				  //其他行动的输出作为阅读材料
				  materialSource:{
				  					actionId:"",//taskDefinitionKey
				  					materialMaker:"self"//"group"
				  					},
				  	toolType:"form",//["textArea", "office", "VM", "choose", "form", "board", "email", "comment"],//textArea富文本,office文档,VM虚拟机,choose选择题,board画板,email发邮件,comment评论
				  	//role:"",//提出来
				  	input:{
				  		inputWay:"",//"init","actionOutput","template"
				  		optType:"",//"private"个人文件,"cooperation"合作文件
				  		template:{
				  			id:"",
				  			sourceF:"",
				  			ownerId:"",
				  			name:""
				  		},
				  		materialSource:{
				  			actionId:"",
				  			materialMaker:"self"//"group"
				  		},
				  		VMId:"",
				  		questionId:""
				  		
				  		//
				  	},
				  	commentType: "self", //"self"自评,"exchange"互评,"teacher"师评 
				  	//评分规则
				  	scoreRules: [{
				  		name: "教学内容科学性", //评分规则的名字， "界面设计"， "教学内容科学性"
				  		type: "peopleScore", //评分类型， "isSubmited"作业是否提交， "examScore"试卷得分， "peopleScore"教师/组员评分
				  		score: 3, //分值
				  		direction: "这是一段评分标准，正确得3分..."
				  	}],
				  	output:{
				  		type:"file",//"file"文件变量,"comment"评论变量,"question"试题变量,"form"表单变量,"formAttr"表单属性变量, "processVar"流程变量
				  		name:"",
				  		requirement:true//false
				  	},
				  	//isHuPing:"",//"0"否,"1"是
			  }
			  加工后：
			  currTask.taskInfo.data = 
			  {
				  taskId:"",//taskDefinitionKey
				  taskName:"",
				  taskDescription:"这是一段引导文",
				  isHuPing:"",//"0"否,"1"是
				  //其他行动的输出作为阅读材料
				  materialSource:{
				  					errorMsg : "", //出错的时候会有
				  					type:"file",
				  					files:[
				  							{filePath:"", fileName:"", optType:"", userId:"", userName:"",
				  							//互评时，我对此文档的评论
				  							 myComment: {fromUserId:"", fromUserName:"", toUserId:"", toUserName:"", comment:"", time:""}
				  							 }
				  						  ]
				  					or
				  					type:"string"
				  					strings:[
				  								{fromUserId:"", fromUserName:"", toUserId:"", toUserName:"", comment:"", time:""}
				  							]
				  					},
				  	toolType:"form",//["textArea", "word", "excel", "ppt", "VM", "choose", "board", "email", "form"],//textArea富文本,office文档,VM虚拟机,choose选择题,board画板,email发邮件,"form"表单
				  	role:"",
				  	input:{
				  			errorMsg : "", //出错的时候会有
		  					filePath:"",//"文件path"
		  					fileName:"",//"文件名",
		  					optType:"",//"private"个人文件,"cooperation"合作文件
		  					userId:"",//文件所有者id
		  					//userName:"",//文件所有者name
		  					
					  		VMId:"",
					  		questionId:"",
					  		
					  		formId:"",
					  		formName:"",
					  		formHtml:""
				  	},
				  	//评分规则
				  	scoreRules: [{
				  		name: "教学内容科学性", //评分规则的名字， "界面设计"， "教学内容科学性"
				  		type: "peopleScore", //评分类型， "isSubmited"作业是否提交， "examScore"试卷得分， "peopleScore"教师/组员评分
				  		score: 3, //分值
				  		direction: "这是一段评分标准，正确得3分...",
				  		target: {//目标
							  		filePath:"", 
							  		fileName:"", 
							  		optType:"", 
							  		userId:"", 
							  		userName:"", 
							  		createTime: "xxx"
						  		},
				  		myScore: 3 //我的得分，需要判断myScore存在否
				  	}],
				  	//告诉player此步需要输出什么
				  	output:{
				  		type:"file",//"string","question","form"
				  		name:""
				  		or
				  		type:"string",
				  		name:"comment",
				  		description:"评论内容"
				  		or
				  		type:"form",
				  		name:"xxx"
				  		or
				  		type:"question",
				  		...
				  		
				  		
				  		requirement:true//false
				  	},
				  	
				  	lastTaskId: ""
			  }
			*/  
/*
	studyRequestData.output = 
	{
		type:"file",//"file"文件变量,"string"评论变量,"question"试题变量,"form"表单变量,"processVar"流程变量
  		filePath:"",
  		fileName:""
  			
  		or
  		
  		type:"string",
  		fileId:"",
  		fileName:"",
  		ownerId:"userId",
  		ownerName:"userName",
  		comment:"评论内容"
  			
  		or
  		
  		type:"form",
  		formId:""
  			
  		or
  		
  		type:"processVar"
  		vars:{"process_xxx":""}
	
  		or
  		
  		type:"question",
  		...
  		
	}
*/

/*我的待评估项的信息
myEstimationItems = 
{
	//
	errorMsg : "此课程不参与评估评分活动！",
	
	//--------------------- 评估信息 ----------------------------
	
	//评估项
	estimationItems:[
				{
					 taskId: "123",
					 taskName: "任务活动",
					 procInstId: "456",
					 //评分规则
					 scoreRules: [{
					 		id: 1, //编号， 1，2，3...
					  		name: "教学内容科学性", //评分规则的名字， "界面设计"， "教学内容科学性"
					  		type: "peopleScore", //评分类型， "isSubmited"作业是否提交， "examScore"试卷得分， "peopleScore"教师/组员评分
					  		score: 3, //分值
					  		direction: "这是一段评分标准，正确得3分...",
					  		myScore: 3 //我的得分，需要判断myScore存在否
					  	}],
					 //目标
					 target: {
						  		filePath:"", 
						  		fileName:"", 
						  		optType:"", 
						  		userId:"", 
						  		userName:"", 
						  		createTime: "xxx"
						  		
						  		//or
						  		errorMsg:"获取不到待评估的文件！"
					  		}
				 }
			],
	//总结，summarize可能是个空json		
	summarize: {
		sumScore: 88,
		comment: "做的不错！"
		
		//or
		errorMsg:"找不到总评信息！"
	}
}

act_study_var:
varId
varType //output, scoreRules, summarize
varValue
userId
userName

taskId
taskName
taskDefinekey
processInstanceId

createTime
lastUpdateTime

act_study_comment:
commentId
fileId

userId
userName
comments

taskId
taskName
taskDefinekey
processInstanceId

createTime
lastUpdateTime

*/
