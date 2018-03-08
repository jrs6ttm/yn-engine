/**
 * 
 */
(function($){
	var parseHtml = 
			'<div id="form_<%=formId%>">'+
				 '<div id="form-header">'+
				 	 '<div class="form-group" align="center" style="margin-bottom:20px;">'+
					    '<span style="font-size:20px;"><strong><%=formName%></strong></span>'+
					  '</div>'+
				  '</div>'+
				  '<div id="form-body">'+
					  '<%if(formAttrs && formAttrs.length > 0){%>'+
					  	'<%_.each(formAttrs, function(formAttr){%>'+
					  		'<%if(formAttr.attrType == "enum"){%>'+
						  		'<div class="form-group">'+
								    '<label for="<%=formAttr.attrId%>"><%=formAttr.label%>：</label>'+
								    '<select class="form-control form-attr" name="<%=formAttr.attrName%>" id="<%=formAttr.attrId%>" data-label="<%=formAttr.label%>" data-flow="<%=formAttr.isFlow%>" data-processvar="<%=formAttr.isProcessVar%>">'+
								        '<option value="">---请选择---</option>'+
								    	'<%if(formAttr.optionValues && formAttr.optionValues.length > 0){%>'+
								    		'<%_.each(formAttr.optionValues, function(optionValue){%>'+
								    			'<option value="<%=optionValue.oId%>" <%if(formAttr.defaultValue) { if(optionValue.oId == formAttr.defaultValue.dId){%>selected<%} }%> ><%=optionValue.oValue%></option>'+
								    		'<%})%>'+
								      	'<%}%>'+
								    '</select>'+
							    '</div>'+
					  		'<%}else if(formAttr.attrType == "string"){%>'+
								  '<div class="form-group">'+
								    '<label for="<%=formAttr.attrId%>"><%=formAttr.label%>：</label>'+
								    '<input type="text" class="form-control form-attr attr-required" name="<%=formAttr.attrName%>" id="<%=formAttr.attrId%>" data-label="<%=formAttr.label%>" data-flow="<%=formAttr.isFlow%>"  data-processvar="<%=formAttr.isProcessVar%>" value="<%=formAttr.defaultValue%>" placeholder="请输入<%=formAttr.label%>">'+
								  '</div>'+
							  '<%}else if(formAttr.attrType == "textArea"){%>'+
								  '<div class="form-group">'+
								    '<label for="<%=formAttr.attrId%>"><%=formAttr.label%>：</label>'+
								    '<textArea class="form-control form-attr attr-required" rows="4" name="<%=formAttr.attrName%>" id="<%=formAttr.attrId%>" data-label="<%=formAttr.label%>" data-flow="<%=formAttr.isFlow%>" data-processvar="<%=formAttr.isProcessVar%>" placeholder="请输入<%=formAttr.label%>"><%=formAttr.defaultValue%></textArea>'+
								  '</div>'+
							  '<%}%>'+
						  '<%})%>'+
					  '<%}%>'+
				  '</div>'+
			  '</div>';
		   
				 
	  ParseFormAttrView = Backbone.View.extend({
			attributes : {
				style : 'margin-left:5%;margin-right:5%'
			},
			events : {},
			initialize : function(){
				_.bindAll(this, 'render');
				
				//this.render();
			},
			
			render : function(){
				this.template = _.template(parseHtml);
				$(this.el).append(
						this.template(this.model.toJSON())
				);
				//$(this.el).attr('id', this.model.get('attrId'));
				//$('body').append(this.el);
				
				return this.el.innerHTML;
			}
	});
})(jQuery)