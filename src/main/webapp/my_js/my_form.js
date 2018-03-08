/**
 * 
 */
(function($){
	$(document).ready(function(){
		/*
		$('input.item-required').blur(function(){
			checkInputValue(this);
		});
		*/
		$('select.attr-item', '.edit-attr-item').change(function(){
			showEnumValueArea(this);
		});
		/*
		$('input.attr-required').blur(function(){
			checkInputValue(this);
		});
		*/
	});
	
	//---------------------------------------------------- 表单属性操作 开始 -------------------------------------------------------
	initFormAttrList = function(formId){
		if(formId != null && formId != 'null' && formId != '' && formId != 'undefined'){
			 $.post(EngineHost+'form/getForm', {formId : formId}, function(form){
				if(form){
					
					$('.form-info').empty();
					var formModel = new FormModel(form);
					var editFormInfoView = new EditFormInfoView({model : formModel});
					
					editFormInfoView.render();
				}
			});
			
			$.post(EngineHost+'form/getFormAttrList', {formId : formId}, function(formAttrs){
				if(formAttrs && formAttrs.length > 0){
					
					$('tbody#form-attr-list', '.form-attrs').empty();
					_.each(formAttrs, function(formAttr){
						if(formAttr.attrType == 'enum'){
							if(formAttr.defaultValue){
								formAttr.defaultValue = JSON.parse(formAttr.defaultValue);
							}
							if(formAttr.optionValues){
								formAttr.optionValues = eval('('+formAttr.optionValues+')');
							}
						}
						var formAttrModel = new FormAttrModel(formAttr);
						var formAttrListView = new FormAttrListView({model : formAttrModel});
						
						formAttrListView.render();
					});
				}
			});
		}
		
		/*
		var form = {
				formId : 'formId_123',
				formName : 'ofo单车报损情况统计',
				description : 'ofo单车报损情况统计描述123',
				userId : 'zhangll',
				userName : '戴维',
				formStatus : '0',
				
				createTime : '2017-4-16 10:28:20',
				lastUpdateTime : '2017-4-21 17:08:20'
		};
		var formModel = new FormModel(form);
		var editFormInfoView = new EditFormInfoView({model : formModel});
		editFormInfoView.render();
		
		var formAttrs = [
							{
								attrId : '123',
								formId : '9876',
								attrName : 'place',
								label : '地点',
								attrType : 'string',
								defaultValue : '软件新城',
								optionValues : []
							},
							{
								attrId : '456',
								formId : '9876',
								attrName : 'level',
								label : '级别',
								attrType : 'enum',
								defaultValue : {dId : 'enum_1', dValue : '一般'},
								optionValues : [
													{oId : 'enum_0', oValue : '小'},
													{oId : 'enum_1', oValue : '一般'},
													{oId : 'enum_2', oValue : '大'}
								                ]
							}
		                ];
		
		_.each(formAttrs, function(formAttr){
			var formAttrModel = new FormAttrModel(formAttr);
			var formAttrListView = new FormAttrListView({model : formAttrModel});
			
			formAttrListView.render();
		});
		*/
	};
	
	editFormName = function(obj){
		var parents = $(obj).parent().parent();
		$(parents).find('div').eq(0).css('display', 'none');
		$(parents).find('div').eq(1).css('display', 'block');
		
		$('input[name="edit-form-name"]', parent).focus();
	};
	
	checkInputValue = function(obj){
		var enumValue = $(obj).val();
		if(!enumValue || enumValue.trim() == ''){
			alert('此处不允许为空！');
			
			$(obj).focus();
		}
	};
	
	submitFormName = function(obj, userId){
		var parent = $(obj).parent();
		var formName = $('input[name="edit-form-name"]', parent).val(),
			nameSpan = $('#form-name').find('span').eq(0),
			oldFormName = nameSpan.text();
		
		if(formName.trim() == oldFormName){
			$('#form-name').css('display', 'block');
			$('#edit-form-name').css('display', 'none');
			
			return false;
		}
		
		//检查表单重名否
		$.post(EngineHost+'form/checkFormName', {formName: formName}, function(result){
			if(result){
				alert('该表单名字已经存在！');
			}else{
				var formInfo = {userId : userId, formName : formName};
				var parents = $(parent).parent(), 
					formId = nameSpan.attr('id');
				if(formId && formId != '' && formId != 'undefined'){
					formInfo.formId = formId;
				}
				
				$.post(EngineHost+'form/saveForm', formInfo, function(rFormId){
					if(rFormId && rFormId != 'error'){
						$('#form-name').css('display', 'block');
						$('#edit-form-name').css('display', 'none');
						
						nameSpan.text(formName);
						nameSpan.attr('id', rFormId);
					}else{
						alert('抱歉，表单创建失败！');
					}
				});
			}
		});
		
		/*
		var parents = $(parent).parent();
		$(parents).find('div').eq(0).css('display', 'block');
		$(parents).find('div').eq(1).css('display', 'none');
		
		$(parents).find('div').eq(0).find('span').eq(0).text(formName);
		*/
	};
	
	selectTr = function(obj){
		var table = $(obj).parent();
		$('tr', table).removeClass('btn-info');
		$(obj).addClass('btn-info');
	};
	
	showEnumValueArea = function(obj){
		var type = $(obj).val();
		if(type == 'enum'){
			$('#var-value-set').css('display', 'block');
			$('#defaultValue', '.edit-attr-item').attr('readonly', true);
		}else{
			//$('div.attr-option-value', '#var-value-set').remove();
			$('#var-value-set').css('display', 'none');
			$('#defaultValue', '.edit-attr-item').attr('readonly', false);
		}
	};
	
	editEnumValue = function(){
		var index = 'optionValue_'+$('.attr-option-value', '#var-value-set').length;
		
		var enumOption = '<div class="row attr-option-value" style="margin-top:6px;">'+
					  		'<div class="col-md-9">'+
				  			'<input type="text" id="'+index+'" class="form-control item-required" name="optionValue" onblur="checkInputValue(this);">'+
					  		'</div>'+
					  		'<div class="col-md-3">'+
					  			'<button class="btn btn-default" title="设为默认值">'+
					  				'<span class="glyphicon glyphicon-ok" onclick="setEnumDefaultValue(this);"></span>'+
					  			'</button>'+
					  			'<button class="btn btn-default" title="移除此值">'+
					  				'<span class="glyphicon glyphicon-remove" onclick="removeEnumValue(this);"></span>'+
					  			'</button>'+
					  		'</div>'+
					  	'</div>';
		
		$('#var-value-set').append(enumOption);
	};
	
	setEnumDefaultValue = function(obj){
		var attrDiv = $(obj).parent().parent().parent();
		var enumValue = $('input[name="optionValue"]', attrDiv).val(),
			id = $('input[name="optionValue"]', attrDiv).attr('id');
		
		$('#defaultValue', '.edit-attr-item').val(enumValue);
		$('#defaultValue', '.edit-attr-item').attr('option-id', id);
	};
	
	removeEnumValue = function(obj){
		$(obj).parent().parent().parent().remove();
	};
	
	clearAttrItem = function(){
		$('.edit-attr-item').removeAttr('id');
		
		$('.attr-item', '.edit-attr-item').each(function(index, item){
			if($(item).attr('id') == 'attrType'){//TODO select框处理
				$(item).val('string');
			}else if($(item).attr('id') == 'isFlow'){
				if($(item).val() == 'y'){
					//$(item).attr('checked', 'checked');
					item.checked = true;
				}else{
					//$(item).removeAttr('checked');
					item.checked = false;
				}
			}else if($(item).attr('id') == 'isProcessVar'){
				if($(item).val() == 'y'){
					//$(item).removeAttr('checked');
					item.checked = false;
				}else{
					//$(item).attr('checked', 'checked');
					item.checked = true;
				}
			}else{
				$(item).val('');
			}
		});
		
		$('div.attr-option-value', '#var-value-set').remove();
		$('#var-value-set').css('display', 'none');
	};
	
	createFormHtml = function(formInfo){
		var formInfoModel = new FormInfoModel(formInfo);
		var parseFormAttrs = new ParseFormAttrView({model : formInfoModel});
		
		var parseHtml = parseFormAttrs.render();
		/*
		parseHtml = '<link href="'+EngineHost+'css/bootstrap.min.css" rel="stylesheet" type="text/css" />' + 
						parseHtml +
					'<script src="'+EngineHost+'js/jquery.min.js" type="text/javascript"></script>'+
					'<script src="'+EngineHost+'js/underscore.js" type="text/javascript"></script>'+
					'<script src="'+EngineHost+'js/backbone.js" type="text/javascript"></script>'+
					'<script src="'+EngineHost+'my_js/parseFormAttrs.js" type="text/javascript"></script>'+
					'<script src="'+EngineHost+'my_js/form.player.js" type="text/javascript"></script>';
					*/
		parseHtml = '<link href="'+EngineHost+'css/bootstrap.min.css" rel="stylesheet" type="text/css" />' + 
						parseHtml +
					'<script  type="text/javascript">'+
						'$.getScript("'+EngineHost+'js/underscore.js", function(){'+
							'console.log("underscore.js is loaded.");'+
							'$.getScript("'+EngineHost+'js/backbone.js", function(){'+
								'console.log("backbone.js is loaded.");'+
								'$.getScript("'+EngineHost+'my_js/parseFormAttrs.js", function(){'+
									'console.log("parseFormAttrs.js is loaded.");'+
									'$.getScript("'+EngineHost+'my_js/form.player.js", function(){'+
										'console.log("form.player.js is loaded.");'+
									'});'+
								'});'+
							'});'+
						'});'+
					'</script>';
		
		return parseHtml;
	};
	
	createFormXml =　function(formInfo){
		
		return '';
	};
	
	parseFormAttr = function(parseType){
		var formSpan = $('span', '#form-name').eq(0);
		var formId = formSpan.attr('id'), formName = formSpan.text().trim();
		
		var formAttrs = [];
		$('tr', 'tbody').each(function(trIndex, tr){
			var attrId = $(tr).attr('id'), formAttr = {attrId : attrId};
			$('td', tr).each(function(tdIndex, td){
				var tdName = $(td).attr('name'), tdValue = $(td).text();
				if(tdValue && tdValue.trim() != ''){
					if(tdName == 'optionValues'){
						var data_enum = eval('('+$('span', td).text()+')');
						formAttr.optionValues = data_enum;
					}else if(tdName == 'defaultValue'){
						var optionId = $(td).attr('option-id');
						if(optionId && optionId != ''){
							//formAttr[tdName] = optionId;//选择框，默认值记录选项的id
							formAttr[tdName] = {dId : optionId, dValue :　tdValue};
						}else{
							formAttr[tdName] = tdValue;
						}
					}else if(tdName == 'isFlow' || tdName == 'isProcessVar'){
						var isV = tdValue == '是' ? 'y' : 'n';
						formAttr[tdName] = isV;
					}else if(tdName == 'attrType'){
						formAttr[tdName] = $(td).attr('data-value');
					}else{
						formAttr[tdName] = tdValue;
					}
				}
			});
			
			formAttrs.push(formAttr);
		});
		
		console.log(formAttrs);
		var formInfo = {formId : formId, formName : formName, formAttrs : formAttrs},
			parseFormInfo = {};
		
		if(parseType == 'html' || parseType == 'all'){
			parseFormInfo.parseHtml = createFormHtml(formInfo);
		}
		if(parseType == 'xml' || parseType == 'all'){
			parseFormInfo.parseXml = createFormXml(formInfo);
		}
		
		return parseFormInfo;
	};
	
	optFormAttr = function(type, userId){
		//先把预览的表单删掉，避免里面引用的js进行覆盖
		//$('#preview-form').modal('hidden');
		$('.modal-body', '#preview-form').empty();
		
		if(type == 'up'){
			var tr = $('tr.btn-info', 'tbody');
			
		}else if(type == 'down'){
			var tr = $('tr.btn-info', 'tbody');
			
		}else if(type == 'add'){
			var nameSpan = $('#form-name').find('span').eq(0),
				formId = nameSpan.attr('id');
			if(!formId || formId == '' || formId == 'undefined'){
				alert('请先编辑生成你的表单名字！');
				return false;
			}
			
			var required = true;
			$('.item-required', '.edit-attr-item').each(function(index, item){
				var v = $(item).val();
				if(!v || v.trim() == ''){
					required = false;
					return false;
				}
			});
			
			if(!required){
				alert('您有必填项未完成！');
				return false;
			}
			
			var hasOptionValue = true;
			var selectValue = $('select#attrType', '.edit-attr-item').val();
			if(selectValue == 'enum'){
				if($('div.attr-option-value', '#var-value-set').length <= 0){
					hasOptionValue = false;
					return false;
				}
			}
			
			if(!hasOptionValue){
				alert('请为选择框添加至少两个候选值！');
				return false;
			}
			
			var attrItem = {formId : formId}, itemHtml = '';
			$('.attr-item', '.edit-attr-item').each(function(index, item){
				//var v = $(item).val(), vName = $(item).attr('id');
				var v = item.value, vName = item.id;
				if(v && v.trim() != ''){
					var optionId = $(item).attr('option-id');
					//var optionId = item.option-id;
					if(vName == 'defaultValue' && optionId && optionId != ''){//选择框
						//attrItem[vName] = optionId;//选择框的默认值记录其选项的id
						var defaultV = {dId : optionId, dValue : v.trim()};
						attrItem[vName] = JSON.stringify(defaultV);
						itemHtml += '<td name="'+vName+'" option-id="'+optionId+'">'+v.trim()+'</td>';
					}else if(vName == 'isFlow' || vName == 'isProcessVar'){
						//if($(item).attr('checked')){
						if(item.checked){
							attrItem[vName] = v;
							var isStr = v == 'y' ? '是' : '否';
							itemHtml += '<td name="'+vName+'">' + isStr + '</td>';
						}
					}else if(vName == 'attrType'){
						attrItem[vName] = v.trim();
						var transferValue = '';
						if(v.trim() == "string"){
							transferValue = '单行文本';
						}else if(v.trim() == "textArea"){
							transferValue = '多行文本';
						}else if(v.trim() == "enum"){
							transferValue = '选择框';
						}
						itemHtml += '<td name="'+vName+'" data-value="'+v.trim()+'">'+transferValue+'</td>';
					}else{
						attrItem[vName] = v.trim();
						itemHtml += '<td name="'+vName+'">'+v.trim()+'</td>';
					}
				}else{
					if(vName != 'isFlow' && vName != 'isProcessVar'){
						itemHtml += '<td name="'+vName+'"></td>';
					}
					
				}
			});
			
			//处理候选值
			var optionValues = $('input[name="optionValue"]', '.attr-option-value');
			if(optionValues.length <= 0){
				itemHtml += '<td name="optionValues"></td>';
			}else{
				//attrItem.optionValues = [];
				var vHtml = '', myOptionV = [];
				optionValues.each(function(index, item){
					var oV = {}, id = $(item).attr('id'), v = $(item).val().trim();
					//oV[index] = v;
					oV.oId = id, oV.oValue = v;
					myOptionV.push(oV);
					
					vHtml += '<option value="'+id+'">'+v+'</option>';
				});
				
				attrItem.optionValues = JSON.stringify(myOptionV);
				
				itemHtml += '<td name="optionValues">'+
							'<span style="display:none">'+attrItem.optionValues+'</span>'+
							'<select class="form-control">' + vHtml + '</select></td>';
			}
			
			var trIndex = $('.edit-attr-item').attr('id');
			if(trIndex && trIndex.trim() != ''){//edit
				attrItem.attrId = trIndex;
				//保存form表单属性至数据库
				$.post(EngineHost+'form/saveFormAttr', attrItem, function(attrId){
					if(attrId && attrId != 'error'){
						var editTr = $('tr#'+trIndex, 'tbody');
						editTr.empty();
						editTr.append(itemHtml);
					}
				});
			}else{//add new
				//保存form表单属性至数据库
				$.post(EngineHost+'form/saveFormAttr', attrItem, function(attrId){
					if(attrId && attrId != 'error'){
						itemHtml = '<tr style="cursor:pointer" title="点击选中" id="'+attrId+'" onclick="selectTr(this);">'+itemHtml+'</tr>';
						$('tbody', '.form-attrs').append(itemHtml);
					}
				});
			}
			
			clearAttrItem();
		}else if(type == 'remove'){
			var tr = $('tr.btn-info', 'tbody');
			if(tr.length <= 0){
				alert('请先从表格里选中一个表单属性！');
				return false;
			}
			
			if(confirm('确定要移除选中的表单属性吗？')){
				/*
				var id = tr.find('td').eq(0).text(),
					name = tr.find('td').eq(0).text();
					*/
				var attrId = tr.attr('id');
				
				//从数据库里移除此属性
				$.post(EngineHost+'form/deleteFormAttr', {attrId : attrId}, function(result){
					if(result && result == 'success'){
						tr.remove();
					}
				});
			}
		}else if(type == 'edit'){
			var tr = $('tr.btn-info', 'tbody');
			if(tr.length <= 0){
				alert('请先从表格里选中一个表单属性！');
				return false;
			}
			
			var formAttrs = {};
			$('td', tr).each(function(index, td){
				var attrName = $(td).attr('name'), tdValue = $(td).text();
				if(tdValue && tdValue.trim() != ''){
					if(attrName == 'optionValues'){
						var data_enum = eval('('+$('span', td).text()+')');
						formAttrs[attrName] = data_enum;
					}else if(attrName == 'defaultValue'){
						var optionId = $(td).attr('option-id');
						if(optionId && optionId != ''){
							//formAttr[tdName] = optionId;//选择框，默认值记录选项的id
							formAttrs[attrName] = {dId : optionId, dValue :　tdValue};
						}else{
							formAttrs[attrName] = tdValue;
						}
					}else if(attrName == 'isFlow' || attrName == 'isProcessVar'){
						var isV = tdValue == '是' ? 'y' : 'n';
						formAttrs[attrName] = isV;
					}else if(attrName == 'attrType'){
						formAttrs[attrName] = $(td).attr('data-value');
					}else{
						formAttrs[attrName] = tdValue;
					}
				}
			});
			
			formAttrs.attrId = tr.attr('id');
			console.log(formAttrs);
			
			var editFormAttrView = new EditFormAttrView({model : new FormAttrModel(formAttrs)});
			editFormAttrView.render();
			
		}else if(type == 'preview'){
			
			var parseFormInfo = parseFormAttr('html');
			
			$('.modal-body', '#preview-form').empty();
			$('.modal-body', '#preview-form').append(parseFormInfo.parseHtml);
			
			$('#preview-form').modal('show');
		}else if(type == 'save'){
			var nameSpan = $('#form-name').find('span').eq(0),
				formId = nameSpan.attr('id');
			if(!formId || formId == '' || formId == 'undefined'){
				alert('请先编辑生成你的表单名字！');
				return false;
			}
			
			var tr = $('tr', 'tbody');
			if(tr.length <= 0){
				alert('请先至少生成一个表单属性！');
				return false;
			}
			
			var parseFormInfo = parseFormAttr('all');
			$.post(EngineHost+'form/saveForm', {formId : formId, formHtml : parseFormInfo.parseHtml, formXml :　parseFormInfo.parseXml}, function(formId){
				if(formId && formId != 'error'){
					window.location.href = EngineHost+'form/list.jsp?userId='+userId;
				}else{
					alert('抱歉，表单生成失败！');
				}
			});
			
			//window.location.href = EngineHost+'form/list.jsp?userId=zhangll';
		}
	};
	
	//---------------------------------------------------- 表单属性操作 结束 -------------------------------------------------------
	
	//---------------------------------------------------- 表单操作 开始 ----------------------------------------------------------
	
	optForm = function(type, formId, userId){
		//先把预览的表单删掉，避免里面引用的js进行覆盖
		$('.modal-body', '#preview-form').empty();
		
		if(type == 'preCreate'){
			
			$('#create-form').modal('show');
			
		}else if(type == 'create'){
			var canAccess = true, formInfo = {};
			$('.form-attr', '.form-info').each(function(index, attr){
				var attrName = $(attr).attr('name'),
					attrValue = $(attr).val();
				if(attrName == 'formName'){
					if(!attrValue || attrValue.trim() == ''){
						alert('请输入表单名字！');
						canAccess = false;
						$(attr).focus();
						return false;
					}else{
						formInfo[attrName] = attrValue;
					}
				}else{
					if(attrValue && attrValue.trim() != ''){
						formInfo[attrName] = attrValue;
					}
				}
			});
			formInfo.userId = userId;
			
			//console.log(formInfo);
			if(canAccess){
				$.post(EngineHost+'form/saveForm', formInfo, function(formId){
					if(formId && formId != 'error'){
						window.location.href = EngineHost+'form/design.jsp?userId='+userId+'&formId='+formId;
					}else{
						alert('抱歉，表单创建失败！');
					}
				});
				
			}
		}else if(type == 'preview'){
			//console.log(type + ' ' + formId);
			
			$.post(EngineHost+'form/getForm', {formId : formId}, function(form){
				if(form){
					$('.modal-body', '#preview-form').empty();
					$('.modal-body', '#preview-form').append(form.formHtml);
					$('#preview-form').modal('show');
				}else{
					alert('抱歉，获取不到表单信息，预览失败！');
				}
			});
			
			//$('#preview-form').modal('show');
		}else if(type == 'use'){
			//console.log(type + ' ' + formId);
			
			$.post(EngineHost+'form/saveFormBind', {formId : formId, formName : formName, courseId : courseId, courseName : courseName}, function(cId){
				if(cId && cId != 'error'){
					alert('抱歉，表单绑定成功！');
				}else{
					alert('抱歉，表单绑定失败！');
				}
			});
			
		}else if(type == 'multiplex'){//复用表单
			//console.log(type + ' ' + formId);
			//console.log(userId);
			$.post(EngineHost+'form/multiplex', {formId : formId}, function(newFormId){
				if(newFormId && newFormId != 'error'){
					window.location.href = EngineHost+'form/design.jsp?userId='+userId+'&formId='+newFormId;
				}else{
					alert('抱歉，表单复用失败！');
				}
			});
			
		}else if(type == 'edit'){
			//console.log(type + ' ' + formId);
			
			window.location.href = EngineHost+'form/design.jsp?userId='+userId+'&formId='+formId;
		}else if(type == 'delete'){
			//console.log(type + ' ' + formId);
			//console.log(userId);
			
			$.post(EngineHost+'form/deleteForm', {formId : formId}, function(result){
				if(result && result == 'success'){
					window.location.href = EngineHost+'form/list.jsp?userId='+userId;
				}else{
					alert('抱歉，表单删除失败！');
				}
			});
			
			//window.location.href = EngineHost+'form/list.jsp?userId='+userId;
		}
	};
	//---------------------------------------------------- 表单操作 结束 ----------------------------------------------------------
	
})(jQuery)