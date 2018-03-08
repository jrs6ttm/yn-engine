/**
 * 
 */
//表单属性
//OptionValues = [{oId : '', oValue : ''}];//数组格式的字符串
//DefaultValue = '' || {dId : '', dValue : ''} //json字符串
var FormAttrModel = Backbone.Model.extend({
	defaults : {
		attrId : '',
		formId : '',
		attrName : '',
		label : '',
		attrType : '',
		defaultValue : '',
		optionValues : '',
		isFlow : 'y', //y: 此属性流转，n: 此属性不流转
		isProcessVar : 'n', //y: 此属性是流程变量，n: 此属性不是流程变量
		
		createTime : '',
		lastUpdateTime : ''
	}
});

//表单内容，生成表单页面用
//formAttrs = [formAttrModel.toJSON()];
var FormInfoModel = Backbone.Model.extend({
	defaults : {
		formId : '',
		formName : '',
		
		formAttrs : []
	}
});

//表单
var FormModel = Backbone.Model.extend({
	defaults : {
		formId : '',
		formName : '',
		description : '',
		userId : '',
		userName : '',
		formStatus : '',
		formHtml : '',
		formXml : '',
		
		createTime : '',
		lastUpdateTime : ''
	}
});

var BindFormModel = Backbone.Model.extend({
	defaults : {
		cId : '',
		formId : '',
		formName : '',
		courseId : '',
		courseName : '',
		taskDefineKey : '',
		taskName : '',
		userId : '',
		userName : '',
		bindStatus : '',
		bindTime : ''
	}
});

var RuntimeForm = Backbone.Model.extend({
	cId : '',
	formId : '',
	formName : '',
	formHtml : '',
	flowAttrs : '[]', //流转属性
	processVars : '[]', //流程变量
	taskId : '',
	taskDefineKey : '',
	taskName : '',
	processInstanceId : '',
	userId : '',
	userName : '',
	
	createTime : '',
	lastUpdateTime : ''
});

var FormAttrList = Backbone.Collection.extend({
	model : FormAttrModel,
	initialize : function(){
		_.bindAll(this, 'getFormList');
	},
	getFormAttrList : function(){
		var me = this;
		$.post('/form/getFormAttrList', {}, function(formAttrs){
			if(formAttrs && formAttrs.length > 0){
				//TODO
				me.reset(formAttrs);
			}
		});
	}
});