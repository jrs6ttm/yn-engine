<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<title>我的表单</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="表单设计器,activiti">
	<meta http-equiv="description" content="activiti的表单设计器的简单实现。">
	
	<link href="css/kendoUI/kendo.common.min.css" rel="stylesheet" type="text/css" />
	<link href="css/kendoUI/kendo.rtl.min.css" rel="stylesheet" type="text/css" />
	<link href="css/kendoUI/kendo.default.min.css" rel="stylesheet" type="text/css" />
	<link href="css/kendoUI/kendo.dataviz.min.css" rel="stylesheet" type="text/css" />
	<link href="css/kendoUI/kendo.dataviz.default.min.css" rel="stylesheet" type="text/css" />
	<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	
	<script src="js/jquery.min.js" type="text/javascript"></script>	
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/kendoUI/kendo.all.min.js" type="text/javascript"></script>
	<script src="js/kendoUI/kendo.messages.zh-CN.min.js" type="text/javascript"></script>
		
	</head>
	<body>
		<% 
			String userId = request.getParameter("userId");
		%>
		<div class="container" style="margin-top:5%;">
			<div style="margin-bottom:15px;">
				<button class="btn btn-info" title="创建一个新表单" onclick="optForm('preCreate');">创建新表单</button>
				<span class="glyphicon glyphicon-refresh" style="margin-left:80%;cursor:pointer" onclick="setFormsGrid('<%=userId%>');"> 刷新</span>
			</div>
			<div class="modal" id="create-form" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			    <div class="modal-dialog">
			        <div class="modal-content">
			            <div class="modal-header">
			                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			                <h4 class="modal-title" id="myModalLabel">创建新表单</h4>
			            </div>
			            <div class="modal-body">
			            	<div class="form-info" style="margin-left:5%;margin-right:5%">
				            	<div class="form-group">
								    <label for="formAttr_0">表单名字：</label>
								    <input type="text" class="form-control form-attr attr-required"  name="formName" placeholder="请输入表单名称">
								 	<small style="margin-top:5px;">*必填项</small>
								 </div>
								 <div class="form-group">
								    <label for="formAttr_0">表单描述：</label>
								    <textarea rows="5" class="form-control form-attr" name="description" placeholder="请输入表单的描述内容"></textarea>
								 </div>
							 </div>
			            </div>
			            <div class="modal-footer">
			                <button type="button" class="btn btn-default" data-dismiss="modal">取 消</button>
			                <button type="button" class="btn btn-info" title="创建表单" onclick="optForm('create', '', '<%=userId %>');">创 建</button>
			            </div>
			        </div><!-- /.modal-content -->
			    </div><!-- /.modal-dialog -->
			</div>
			<div class="modal" id="preview-form" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			    <div class="modal-dialog">
			        <div class="modal-content">
			            <div class="modal-header">
			                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			                <h4 class="modal-title" id="myModalLabel">表单查看</h4>
			            </div>
			            <div class="modal-body">
			            	<!-- 
			            	<div class="my-form" style="margin-left:5%;margin-right:5%">
					            <div class="form-group" align="center" style="margin-bottom:20px;">
								   <span style="font-size:20px;" form-id="form_1"><strong>【示例表】ofo单车报损表</strong></span>
								</div>
				            	<div class="form-group">
								    <label for="formAttr_0">地点：</label>
								    <input type="text" class="form-control form-attr attr-required" id="formAttr_0" value="软件新城" placeholder="请输入名称">
								 </div>
								 <div class="form-group">
								    <label for="formAttr_1">损坏程度：</label>
								    <select class="form-control form-attr">
								      <option value="optionValue_0">不严重</option>
								      <option value="optionValue_1">一般</option>
								      <option value="optionValue_2" selected>严重</option>
								    </select>
								 </div>
							 </div>
							  -->
			            </div>
			            <div class="modal-footer">
			                <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
			                <!-- <button type="button" class="btn btn-info" title="保存表单" onclick="optFormAttr('save', '<%=userId %>');">保 存</button> -->
			            </div>
			        </div><!-- /.modal-content -->
			    </div><!-- /.modal-dialog -->
			</div>
			<div id="formGrid" style="overflow-x:auto;"></div>
		</div>
		<script type="text/javascript">
			var EngineHost = '<%=basePath%>';
			//EngineHost = EngineHost.replace(/localhost:8080/, '192.168.1.25:8080');
			EngineHost = EngineHost.replace(/com:80/, 'com');
			console.log(EngineHost);
		</script>
		<script src="my_js/my_form.js" type="text/javascript"></script>
		<script type="text/javascript">
			var setFormsGrid = function(userId){
		        $("#formGrid").empty();
		        /*
		        var usersResult = [
			                           {
			                        	    formId : 'formId_0',
				                       		formName : 'ofo单车报损情况',
				                       		description : '',
				                       		userId : 'zhangll',
				                       		userName : '张龙龙',
				                       		formStatus : '1',
				                       		
				                       		createTime : '2017-4-15 16:44:20',
				                       		lastUpdateTime : '2017-4-16 18:34:20'
			                           },
			                           {
			                        	    formId : 'formId_1',
				                       		formName : 'ofo单车报损情况2',
				                       		description : '',
				                       		userId : 'lijie',
				                       		userName : '李杰',
				                       		formStatus : '0',
				                       		
				                       		createTime : '2017-4-18 16:44:20',
				                       		lastUpdateTime : '2017-4-20 10:34:55'
			                           }
		                           ];
		        */
		        $.post('form/getFormList', {userId: userId}, function(formList){
		            if(formList && formList.length>0){
		                $("#formGrid").kendoGrid({
		                    dataSource: {
		                        data: formList,
		                        pageSize: 20
		                    },
		                    sortable: {
		                        mode: "single",
		                        allowUnsort: false
		                    },
		                    pageable: {
		                        buttonCount: 5
		                    },
		                    scrollable: false,
		                    columns: [
		                        {
		                            field: "formName",
		                            title: "<div align='center'><strong>表单名字</strong></div>",
		                            width: "12%"
		                        },
	
		                        {
		                            template: '<div style="height:55px;word-wrap:break-word;overflow-y:auto" title="#=description#">#=description#</div>',
		                            title: "<div align='center'><strong>表单描述</strong></div>",
		                            width: "25%"
		                        },
	
		                        {
		                            template: '#if(formStatus == "0"){#未使用#}else if(formStatus == "1"){#使用中#}#',
		                            title: "<div align='center'><strong>表单状态</strong></div>",
		                            width: "5%"
		                        },
		                        
		                        {
		                            field: "createTime",
		                            title: "<div align='center'><strong>创建时间</strong></div>",
		                            width: "13%"
		                        },
		                        
		                        {
		                            field: "lastUpdateTime",
		                            title: "<div align='center'><strong>最后修改时间</strong></div>",
		                            width: "13%"
		                        },
	
		                        {
		                            template: '<div align="center"><button type="button" class="btn btn-info btn-xs" name="preview" onclick="optForm(\'preview\', \'#=formId#\');">查 看</button>&nbsp;&nbsp;'+
		                            			'<!--<button type="button" class="btn btn-info btn-xs" title="引用到课程里" name="use" onclick="optForm(\'use\', \'#=formId#\');">使 用</button>&nbsp;&nbsp;-->' +
		                            			'<button type="button" class="btn btn-info btn-xs" title="复用此表单创建一个新表单" name="multiplex" onclick="optForm(\'multiplex\', \'#=formId#\', \'<%=userId%>\');">复 用</button>&nbsp;&nbsp;' +
					                            '#if(formStatus == "0"){#'+
					                            	'<button type="button" class="btn btn-info btn-xs" name="edit" onclick="optForm(\'edit\', \'#=formId#\', \'<%=userId%>\');">编 辑</button>&nbsp;&nbsp;' +
					                            	'<button type="button" class="btn btn-info btn-xs optPerson" name="delete" onclick="optForm(\'delete\', \'#=formId#\', \'<%=userId%>\');">删 除</button>'+
					                            '#}#</div>',
		                            title: "<div align='center'><strong>操作</strong></div>",
		                            width: "17%"
		                        }
		                    ]
		                });
		            }
		        });
		    };
		    $(document).ready(function(){
		    	setFormsGrid('<%=userId%>');
		    });
		</script>
	</body>
</html>