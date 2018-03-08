/**
 * 
 */
(function($){
	var AttrListHtml = 
			 '<tr style="cursor:pointer" id="<%=attrId%>" title="点击选中" onclick="selectTr(this);">'+
			    '<td name="attrName"><%=attrName%></td>'+
			    '<td name="label"><%=label%></td>'+
			    '<td name="attrType" data-value="<%=attrType%>"><%if(attrType == "string"){%>单行文本<%}else if(attrType == "textArea"){%>多行文本<%}else if(attrType == "enum"){%>选择框<%}%></td>'+
			    '<td name="isFlow"><%if(isFlow == "y"){%>是<%}else{%>否<%}%></td>'+
			    '<td name="isProcessVar"><%if(isProcessVar == "y"){%>是<%}else{%>否<%}%></td>'+
			    '<%if(attrType == "enum"){%>'+
			    	'<%if(defaultValue) {%>'+
				    	'<td name="defaultValue" option-id="<%=defaultValue.dId%>"><%=defaultValue.dValue%></td>'+
				    '<%}else{%>'+    
				    	'<td name="defaultValue"></td>'+
				    '<%}%>'+ 
				    '<td name="optionValues">'+
				    	'<span style="display:none"><%=JSON.stringify(optionValues)%></span>'+
				    	'<select class="form-control">' + 
				    		'<%if(optionValues && optionValues.length > 0){%>'+
					    		'<%_.each(optionValues, function(optionValue, index){%>'+
					    			'<option value="<%=optionValue.oId%>" <%if(defaultValue) { if(optionValue.oId == defaultValue.dId){%>selected<%} }%> ><%=optionValue.oValue%></option>'+
					    		'<%})%>'+
				    		'<%}%>'+
				    	'</select>'+
				    '</td>'+
			    '<%}else{%>'+
			    	'<td name="defaultValue"><%=defaultValue%></td>'+
			    	'<td name="optionValues"></td>'+
			    '<%}%>'+
			  '</tr>';
	
	FormAttrListView = Backbone.View.extend({
		el : '#form-attr-list',
		attributes : {},
		events : {},
		initialize : function(){
			_.bindAll(this, 'render');
			
			//this.render();
		},
		
		render : function(){
			this.template = _.template(AttrListHtml);
			//$(this.el).empty();
			$(this.el).append(
					this.template(this.model.toJSON())
			);
			//$(this.el).attr('id', this.model.get('attrId'));
			//$('.container').append(this.el);
		}
	});
})(jQuery)