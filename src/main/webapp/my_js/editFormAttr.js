/**
 * 
 */
(function($){
	 EditFormAttrHtml = 
		  '<div class="form-group">'+
		    '<label for="attrName">属性英文名</label>'+
		    '<input type="text" class="form-control attr-item item-required" id="attrName" value="<%=attrName%>" placeholder="请输入属性的英文名称" />'+
		    '<small style="margin-top:5px;">*必填项</small>'+
		  '</div>'+
		  '<div class="form-group">'+
		    '<label for="label">属性中文名</label>'+
		    '<input type="text" class="form-control attr-item item-required" id="label" value="<%=label%>" placeholder="请输入属性的中文名称" />'+
		    '<small style="margin-top:5px;">*必填项</small>'+
		  '</div>'+
		  '<div class="form-group">'+
		    '<label for="attrType">属性类型</label>'+
		    '<select class="form-control attr-item" id="attrType">'+
		      '<option value="string" <%if(attrType == "string"){%>selected<%}%> >单行文本框</option>'+
		      '<option value="string" <%if(attrType == "textArea"){%>selected<%}%> >多行文本框</option>'+
		      '<!--<option value="long" <%if(attrType == "long"){%>selected<%}%> >long</option>'+
		      '<option value="boolean" <%if(attrType == "boolean"){%>selected<%}%> >boolean</option>'+
		      '<option value="date" <%if(attrType == "date"){%>selected<%}%> >date</option>-->'+
		      '<option value="enum" <%if(attrType == "enum"){%>selected<%}%> >选择框</option>'+
		    '</select>'+
		  '</div>'+
		  '<div class="form-group">'+
		    '<label for="isFlow">属性流转</label>'+
		    '<div>'+
			    '<label class="checkbox-inline">'+
			    	'<input type="radio" name="isFlow" id="isFlow" class="attr-item" value="y" <%if(isFlow == "y"){%>checked="checked"<%}%> /> 是'+
			    '</label>'+
			    '<label class="checkbox-inline">'+
		    		'<input type="radio" name="isFlow" id="isFlow" class="attr-item" value="n" <%if(isFlow == "n"){%>checked="checked"<%}%> /> 否'+
		    	'</label>'+
			 '</div>'+
		  '</div>'+
		  '<div class="form-group">'+
		    '<label for="isProcessVar">流程变量</label>'+
		    '<div>'+
			    '<label class="checkbox-inline">'+
			    	'<input type="radio" name="isProcessVar" id="isProcessVar" class="attr-item" value="y" <%if(isProcessVar == "y"){%>checked="checked"<%}%> /> 是'+
			    '</label>'+
			    '<label class="checkbox-inline">'+
		    		'<input type="radio" name="isProcessVar" id="isProcessVar" class="attr-item" value="n" <%if(isProcessVar == "n"){%>checked="checked"<%}%> /> 否'+
		    	'</label>'+
			 '</div>'+
		  '</div>'+
		  '<div class="form-group">'+
		    '<label for="defaultValue">默认值</label>'+
		    '<%if(attrType == "enum"){%>'+
		    	'<input type="text" class="form-control attr-item" id="defaultValue" option-id="<%=defaultValue.dId%>" value="<%=defaultValue.dValue%>" />'+
		    '<%}else{%>'+
		    	'<input type="text" class="form-control attr-item" id="defaultValue" value="<%=defaultValue%>" />'+
		    '<%}%>'+
		  '</div>'+
		  
		  '<div id="var-value-set" <%if(attrType == "enum"){%>style="display:block"<%}else{%>style="display:none"<%}%> >'+
		  	'<hr />'+
		  	'<label><strong>候选值项</strong></label>'+
		  	'<div>Add value <button class="btn btn-default"><span class="glyphicon glyphicon-plus" onclick="editEnumValue();"></span></button></div>'+
		  	
		  	'<%if(optionValues.length > 0){%>'+
			  	'<%_.each(optionValues, function(optionValue, index){%>'+
				  	'<div class="row attr-option-value" style="margin-top:6px;">'+
				  		'<div class="col-md-9">'+
				  			'<input type="text" id="<%=optionValue.oId%>" class="form-control" name="optionValue" onblur="checkInputValue(this);" value="<%=optionValue.oValue%>" placeholder="请输入候选值">'+
				  		'</div>'+
				  		'<div class="col-md-3">'+
				  			'<button class="btn btn-default" title="设为默认值">'+
				  				'<span class="glyphicon glyphicon-ok" onclick="setEnumDefaultValue(this);"></span>'+
				  			'</button>'+
				  			'<button class="btn btn-default" title="移除此值">'+
				  				'<span class="glyphicon glyphicon-remove" onclick="removeEnumValue(this);"></span>'+
				  			'</button>'+
				  		'</div>'+
				  	'</div>'+
			  	'<%})%>'+
		  	'<%}%>'+
		  '</div>';
	
	 EditFormAttrView = Backbone.View.extend({
		el : '.edit-attr-item',
		attributes : {},
		events : {},
		initialize : function(){
			_.bindAll(this, 'render');
			
			//this.render();
		},
		
		render : function(){
			this.template = _.template(EditFormAttrHtml);
			$(this.el).empty();
			$(this.el).append(
					this.template(this.model.toJSON())
			);
			$(this.el).attr('id', this.model.get('attrId'));
			//$('.container').append(this.el);
		}
	});
})(jQuery);