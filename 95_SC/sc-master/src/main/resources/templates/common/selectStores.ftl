[#ftl]
<div id="${divId!''}" class="modal fade bs-example-modal-sm" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
     <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="">选择店铺</h4>
      </div>
      <div class="modal-body">
	     <table class="table table-hover table-condensed">
	      <thead>
	        <tr>
	          <th style="width:10px">#</th>
	          <th style="width:70px">店铺</th>
	          <th>店铺名称</th>
	        </tr>
	      </thead>
	      <tbody id="${divId!''}Tbody">
	      [#if storeList??]
		      [#list storeList as item ]
		        <tr>
		          <td><input type="checkbox" value="${item.store!}" tvalue="${item.storeName!}"/></td>
		          <td class="ck">${item.store!}</td>
		          <td class="ck">${item.storeName!}</td>
		        </tr>
				[/#list]
			[/#if]
	      </tbody>
	    </table>
      </div>
      <div class="modal-footer">
	      <div class="pull-left">
		      <label><input id="${divId}all_ck" type="checkbox" value="0"/> 全选/取消</label>
	      </div>
			<div class="pull-right">
	        	<button id="${divId!''}cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
    		    <button id="${divId!''}affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>确认选择</button>
			</div>
      </div>
    </div>
  </div>
</div>
