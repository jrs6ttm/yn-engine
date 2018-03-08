/**
 * 
 */
(function($){
	var EditFormInfoHtml = 
			'<div class="row">'+
				'<label style="font-size:20px;" class="col-md-4">表单名称：</label>'+
				'<div class="col-md-8">'+
					'<div id="form-name" style="display:block;">'+
						'<span style="cursor:pointer;font-size:16px;" title="编辑表单名称" id="<%=formId %>" onclick="editFormName(this);"><%=formName %></span>'+
						'<span style="cursor:pointer;float:right" class="glyphicon glyphicon-edit" onclick="editFormName(this);" title="编辑表单名称"></span>'+
					'</div>'+
					'<div id="edit-form-name" style="display:none" class="row">'+
						'<div class="col-md-8">'+
			            	'<input type="text" class="form-control item-required" name="edit-form-name" value="<%=formName %>">'+
			            '</div>'+
			            '<div class="col-md-4" onclick="submitFormName(this, \'<%=userId%>\');">'+
			            	'<button class="btn btn-info" type="button">确  定</button>'+
			            '</div>'+
			        '</div>'+
				'</div>'+
			'</div>';
				
	EditFormInfoView = Backbone.View.extend({
		el : '.form-info',
		attributes : {},
		events : {},
		initialize : function(){
			_.bindAll(this, 'render');
			
			//this.render();
		},
		
		render : function(){
			this.template = _.template(EditFormInfoHtml);
			$(this.el).empty();
			$(this.el).append(
					this.template(this.model.toJSON())
			);
		}
	});
})(jQuery)