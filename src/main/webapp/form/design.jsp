<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<head>
	    <base href="<%=basePath%>">
		<title>表单设计</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="表单设计器,activiti">
		<meta http-equiv="description" content="activiti的表单设计器的简单实现。">
	
		<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/underscore.js" type="text/javascript"></script>
		<script src="js/backbone.js" type="text/javascript"></script>
		
		<script src="my_js/formAttrModel.js" type="text/javascript"></script>
		<script src="my_js/formAttrList.js" type="text/javascript"></script>
		<script src="my_js/editFormAttr.js" type="text/javascript"></script>
		<script src="my_js/editFormInfo.js" type="text/javascript"></script>
		<script src="my_js/parseFormAttrs.js" type="text/javascript"></script>
		<script type="text/javascript">
			var EngineHost = '<%=basePath%>';
			//EngineHost = EngineHost.replace(/localhost:8080/, '192.168.1.25:8080');
			EngineHost = EngineHost.replace(/com:80/, 'com');
			console.log(EngineHost);
		</script>
		<script src="my_js/my_form.js" type="text/javascript"></script>
	</head>
	<body>
		<% 
			String formId = request.getParameter("formId");
			String userId = request.getParameter("userId");
		%>
		<div class="container" style="margin-top:5%;">
			<div class="form-info">
				<div class="row">
					<label style="font-size:20px;" class="col-md-4">表单名称：</label>
					<div class="col-md-8">
						<div id="form-name" style="display:block;">
							<span style="cursor:pointer;font-size:16px;" title="编辑表单名称" onclick="editFormName(this);">点击编辑你的表单名字</span>
							<span style="cursor:pointer;float:right" class="glyphicon glyphicon-edit" onclick="editFormName(this);" title="编辑表单名称"></span>
						</div>
						<div id="edit-form-name" style="display:none" class="row">
							<div class="col-md-8">
		                    	<input type="text" class="form-control item-required" name="edit-form-name" placeholder="请输入表单名称">
		                    </div>
		                    <div class="col-md-4" onclick="submitFormName(this, '<%=userId %>');">
		                    	<button class="btn btn-info" type="button">确  定</button>
		                    </div>
		                </div>
					</div>
				</div>
			</div>
			<div class="modal" id="preview-form" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			    <div class="modal-dialog">
			        <div class="modal-content">
			            <div class="modal-header">
			                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			                <h4 class="modal-title" id="myModalLabel">表单预览</h4>
			            </div>
			            <div class="modal-body">
			              <!-- 
			            	<div class="my-form" style="margin-left:5%;margin-right:5%">
					            <div class="form-group" align="center" style="margin-bottom:20px;">
								   <span style="font-size:20px;" form-id="form_1"><strong>ofo单车报损表</strong></span>
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
			                <button type="button" class="btn btn-info" title="保存表单" onclick="optFormAttr('save', '<%=userId %>');">保 存</button>
			            </div>
			        </div><!-- /.modal-content -->
			    </div><!-- /.modal-dialog -->
			</div>
			<hr />
			<div class="row form-attrs">
				<div class="col-md-8">
					<label style="font-size:20px;">表单属性：</label>
					<div>
						<table class="table table-bordered">
						  <thead>
						    <tr>
						      <th>属性英文名</th>
						      <th>属性中文名</th>
						      <th>属性类型</th>
						      <th>是否流转</th>
						      <th>是否流程变量</th>
						      <th>默认值</th>
						      <th>候选值项</th>
						    </tr>
						  </thead>
						  <tbody id="form-attr-list">
						  <!-- 
						    <tr style="cursor:pointer" title="点击选中" onclick="selectTr(this);">
						      <td>title</td>
						      <td>标题</td>
						      <td>string</td>
						      <td>二维码损毁</td>
						      <td></td>
						    </tr>
						    <tr style="cursor:pointer" title="点击选中" onclick="selectTr(this);">
						      <td>tel</td>
						      <td>联系电话</td>
						      <td>string</td>
						      <td>13026157464</td>
						      <td></td>
						    </tr>
						    <tr style="cursor:pointer" title="点击选中" onclick="selectTr(this);">
						      <td>level</td>
						      <td>严重程度</td>
						      <td>enum</td>
						      <td>严重</td>
						      <td></td>
						    </tr>
						    <tr style="cursor:pointer" id="formAttr_3" title="点击选中" onclick="selectTr(this);">
						      <td name="attrName">place</td>
						      <td name="label">地点</td>
						      <td name="attrType">string</td>
						      <td name="defaultValue">软件新城</td>
						      <td name="optionValues"></td>
						    </tr>
						    <tr style="cursor:pointer" title="点击选中" onclick="selectTr(this);">
						      <td>content</td>
						      <td>内容</td>
						      <td>string</td>
						      <td></td>
						      <td></td>
						    </tr>
						     -->
						  </tbody>
						</table>
					</div>
				</div>
				<div class="col-md-4">
					<div class="edit-attr-item">
						 <div class="form-group">
						    <label for="attrName">属性英文名</label>
						    <input type="text" class="form-control attr-item item-required" id="attrName" placeholder="请输入属性的英文名称">
						    <small style="margin-top:5px;">*必填项</small>
						  </div>
						  <div class="form-group">
						    <label for="label">属性中文名</label>
						    <input type="text" class="form-control attr-item item-required" id="label" placeholder="请输入属性的中文名称">
						    <small style="margin-top:5px;">*必填项</small>
						  </div>
						  <div class="form-group">
						    <label for="attrType">属性类型</label>
						    <select class="form-control attr-item" id="attrType">
						      <option value="string">单行文本框</option>
						      <option value="textArea">多行文本框</option>
						      <!-- 
						      <option value="long">long</option>
						      <option value="boolean">boolean</option>
						      <option value="date">date</option>
						       -->
						      <option value="enum">选择框</option>
						    </select>
						  </div>
						  <div class="form-group">
						    <label for="isFlow">属性流转</label>
						    <div>
							    <label class="checkbox-inline">
							    	<input type="radio" name="isFlow" id="isFlow" class="attr-item" value="y" checked="checked" /> 是
							    </label>
							    <label class="checkbox-inline">
						    		<input type="radio" name="isFlow" id="isFlow" class="attr-item" value="n" /> 否
						    	</label>
							 </div>
						  </div>
						  <div class="form-group">
						    <label for="isProcessVar">流程变量</label>
						    <div>
							    <label class="checkbox-inline">
							    	<input type="radio" name="isProcessVar" id="isProcessVar" class="attr-item" value="y" /> 是
							    </label>
							    <label class="checkbox-inline">
						    		<input type="radio" name="isProcessVar" id="isProcessVar" class="attr-item" value="n" checked="checked" /> 否
						    	</label>
							 </div>
						  </div>
						  <div class="form-group">
						    <label for="defaultValue">默认值</label>
						    <input type="text" class="form-control attr-item" id="defaultValue">
						  </div>
						  
						  <div id="var-value-set" style="display:none">
						  	<hr />
						  	<label><strong>候选值项</strong></label>
						  	<div>Add value <button class="btn btn-default"><span class="glyphicon glyphicon-plus" onclick="editEnumValue();"></span></button></div>
						  	
						  	<!-- 
						  	<div class="row attr-option-value" style="margin-top:6px;">
						  		<div class="col-md-9">
						  			<input type="text" id="0" class="form-control" name="optionValue" onblur="test();" placeholder="请输入候选值">
						  		</div>
						  		<div class="col-md-3">
						  			<button class="btn btn-default" title="设为默认值">
						  				<span class="glyphicon glyphicon-ok" onclick="setEnumDefaultValue(this);"></span>
						  			</button>
						  			<button class="btn btn-default" title="移除此值">
						  				<span class="glyphicon glyphicon-remove" onclick="removeEnumValue(this);"></span>
						  			</button>
						  		</div>
						  	</div>
						  	<div class="row attr-option-value" style="margin-top:6px;">
						  		<div class="col-md-9">
						  			<input type="text" id="1" class="form-control" name="optionValue"  onblur="test();" placeholder="请输入候选值">
						  		</div>
						  		<div class="col-md-3">
						  			<button class="btn btn-default" title="设为默认值">
						  				<span class="glyphicon glyphicon-ok" onclick="setEnumDefaultValue(this);"></span>
						  			</button>
						  			<button class="btn btn-default" title="移除此值">
						  				<span class="glyphicon glyphicon-remove" onclick="removeEnumValue(this);"></span>
						  			</button>
						  		</div>
						  	</div>
						  	  -->
						  </div>
						<!-- 
						<div>
						    <label class="checkbox-inline">
						      <input type="checkbox" name="optionSet" value="Required" />Required
						    </label>
						    <label class="checkbox-inline">
						      <input type="checkbox" name="optionSet" value="Readable" />Readable
						    </label>
						    <label class="checkbox-inline">
						      <input type="checkbox" name="optionSet" value="Writable" />Writable
						    </label>
						</div>
						 -->
					 </div>
					 <!-- 
					<hr />
					<div>
						<button  class="btn btn-default">取 消</button>
						<button  class="btn btn btn-info">保 存</button>
					</div>
					 -->
				</div>
			</div>
			<hr />
			<div>
				<!-- 
				<button class="btn btn-info" title="上移属性" ><span class="glyphicon glyphicon-arrow-up" onclick="optFormAttr('up');"></span></button>
				<button class="btn btn-info" title="下移属性" ><span class="glyphicon glyphicon-arrow-down" onclick="optFormAttr('down');"></span></button>&nbsp;&nbsp;
			 -->	
				<button class="btn btn-info" title="添加属性" ><span class="glyphicon glyphicon-plus" onclick="optFormAttr('add', '<%=userId %>');"></span></button>
				<button class="btn btn-info" title="移除属性" ><span class="glyphicon glyphicon-minus" onclick="optFormAttr('remove', '<%=userId %>');"></span></button>
				<button class="btn btn-info" title="编辑属性" ><span class="glyphicon glyphicon-pencil" onclick="optFormAttr('edit', '<%=userId %>');"></span></button>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button class="btn btn-info" title="预览表单" onclick="optFormAttr('preview');">预 览</button>&nbsp;&nbsp;
				<button class="btn btn-info" title="保存表单" onclick="optFormAttr('save', '<%=userId %>');">保 存</button>
			</div>
		</div>
		<script type="text/javascript">
			initFormAttrList('<%=formId%>');
		</script>
	</body>
</html>