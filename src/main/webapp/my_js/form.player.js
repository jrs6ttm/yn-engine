/**
 * 
 * Description : form in player
 * Date : 2017年5月27日
 * Author : zhangLL
 * 
 */
var FormInfoModel = Backbone.Model.extend({
	defaults : {
		formId : '',
		formName : '',
		
		formAttrs : []
	}
});

var createFormHtml_player = function(EngineHost, formInfo){
		var formInfoModel = new FormInfoModel(formInfo);
		var parseFormAttrs = new ParseFormAttrView({model : formInfoModel});
		
		var parseHtml = parseFormAttrs.render();
		/*
		parseHtml = '<link href="'+EngineHost+'/ec_engine/css/bootstrap.min.css" rel="stylesheet" type="text/css" />' + 
						parseHtml +
					 '<script src="'+EngineHost+'/ec_engine/js/jquery.min.js" type="text/javascript"></script>'+
					 '<script src="'+EngineHost+'/ec_engine/js/underscore.js" type="text/javascript"></script>'+
					 '<script src="'+EngineHost+'/ec_engine/js/backbone.js" type="text/javascript"></script>'+
					 '<script src="'+EngineHost+'/ec_engine/my_js/parseFormAttrs.js" type="text/javascript"></script>'+
					 '<script src="'+EngineHost+'/ec_engine/my_js/form.player.js" type="text/javascript"></script>';
		*/
		
		parseHtml = '<link href="'+EngineHost+'/ec_engine/css/bootstrap.min.css" rel="stylesheet" type="text/css" />' + 
						parseHtml +
					'<script  type="text/javascript">'+
						'$.getScript("'+EngineHost+'/ec_engine/js/underscore.js", function(){'+
							'console.log("underscore.js is loaded.");'+
							'$.getScript("'+EngineHost+'/ec_engine/js/backbone.js", function(){'+
								'console.log("backbone.js is loaded.");'+
								'$.getScript("'+EngineHost+'/ec_engine/my_js/parseFormAttrs.js", function(){'+
									'console.log("parseFormAttrs.js is loaded.");'+
									'$.getScript("'+EngineHost+'/ec_engine/my_js/form.player.js", function(){'+
										'console.log("form.player.js is loaded.");'+
									'});'+
								'});'+
							'});'+
						'});'+
					'</script>';
		
		return parseHtml;
};
	
var parseFormAttr_player = function(EngineHost, formId){
	var formHeader = $('#form-header', '#form_'+formId),
		formBody = $('#form-body', '#form_'+formId), 
		formName = formHeader.text().trim();
	
	var formAttrs = [], flowAttrs = [], processVars = [];
	$('.form-attr', formBody).each(function(aIndex, currAttr){
		var attrId = $(currAttr).attr('id'), 
			attrName = $(currAttr).attr('name'),
			label = $(currAttr).attr('data-label'),
			isFlow = $(currAttr).attr('data-flow'),
			isProcessVar = $(currAttr).attr('data-processvar');
			formAttr = {attrId : attrId, attrName : attrName, label : label, isFlow : isFlow, isProcessVar : isProcessVar};
		
		if($(currAttr).is('input')){
			formAttr.attrType = 'string';
			formAttr.defaultValue = $(currAttr).val();
		}else if($(currAttr).is('textArea')){
			formAttr.attrType = 'textArea';
			formAttr.defaultValue = $(currAttr).val();
		}else if($(currAttr).is('select')){
			formAttr.attrType = 'enum';
			var optionId = $(currAttr).val();
			formAttr.optionValues = [];
			$('option', currAttr).each(function(oIndex, currOption){
				var oId = $(currOption).attr('value'),
					oValue = $(currOption).text();
				if(optionId == oId){
					formAttr.defaultValue = {dId : optionId, dValue :　oValue};
				}
				if(oId.trim() != ''){
					formAttr.optionValues.push({oId : oId, oValue : oValue});
				}
			});
			
		}else{//TODO 扩展类型
			console.log('待扩展的属性类型：' + $(currAttr).tagName);
		}
		
		formAttrs.push(formAttr);
		
		if(formAttr.isFlow == 'y'){//此属性流转
			flowAttrs.push(formAttr);
		}
		if(formAttr.isProcessVar == 'y'){//此属性是流程变量
			processVars.push(formAttr);
		}
	});
	
	console.log(formAttrs);
	var formInfo = {formId : formId, formName : formName, formAttrs : formAttrs};
	
	var parseResult = {
			formHtml : createFormHtml_player(EngineHost, formInfo),
			flowAttrs : JSON.stringify(flowAttrs),
			processVars : JSON.stringify(processVars)
	};
	
	return parseResult;
};

var submitMyForm = function(EngineHost, formId, callback){
	var parseResult = parseFormAttr_player(EngineHost, formId);
	parseResult.cId = formId;
	$.post(EngineHost+'/ec_engine/form/saveFormRun', parseResult, 
		   function(cId){
				//$('#savedform').empty();
				//$('#savedform').append(parseResult.formHtml);
				callback(cId);
			}
	 );
}