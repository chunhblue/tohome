[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>[@spring.message 'common.title'/]</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.defaultRole'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
	</script>
	<STYLE>
	
	</STYLE>
</head>

<body>
	<!--页头-->
	[@common.header][/@common.header]
	<!--导航-->
[#--	[@common.nav "HOME&Privilege management&默认角色授权"][/@common.nav]--]
[#--	[@common.nav "HOME&Privilege&Default Role Authorization"][/@common.nav]--]
	[@common.nav "HOME&Privilege&General Privilege Management"][/@common.nav]
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			 <div class="box">
					<div class="box-title ">
						<div class="tt">Query Condition</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
							  		<div class="form-group">
[#--									    <label for="iyPost"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Job</label>--][#--职务--]
									    <label for="iyPost"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Position</label>[#--职务--]
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										   <div class="aotu-pos">
											    <input type="text" class="form-control my-automatic input-sm" id="iyPost" placeholder="">
										        <a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											    <a id=""  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
									    	</div>
									    </div>
									  </div>
									  <div class="form-group ">
									    <label for="roleName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Privilege</label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<div class="aotu-pos">
											    <input type="text" class="form-control my-automatic input-sm" id="roleName" placeholder="">
										        <a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											    <a id=""  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
									    	</div>
									    </div>
									  </div>

								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--row 分割-->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
				<table id="zgGridTtable"></table>
			</div>
		</div>
		<!--row 分割-->
	</div>
	<div id="edit_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
		  <div class="modal-dialog modal-md" role="document">
		    <div class="modal-content">
		     <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">Edit</h4>
		      </div>
		      <div class="modal-body">
		        	<div class="form-horizontal">
			        	<div class="form-group">
[#--						    <label for="e_iyPost"  class="col-sm-2 control-label">Job</label>--]
						    <label for="e_iyPost"  class="col-sm-2 control-label"><span style="color:red;">* </span>Position</label>
						    <div class="col-sm-5">
							   <div class="aotu-pos">
								    <input type="text" class="form-control my-automatic input-sm" id="e_iyPost" placeholder="">
							        <a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh a-up-hide"></a>
								    <a id=""  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle a-up-hide"></a>
						    	</div>
						    </div>
						  </div>
				  	 <div class="form-group ">
[#--					    <label for="e_role" class="col-sm-2 control-label">Role</label>--]
					    <label for="e_role" class="col-sm-2 control-label"><span style="color:red;">* </span>Privilege</label>
					    <div class="col-sm-8">
					    	<div class="aotu-pos">
							    <input type="text" class="form-control my-automatic input-sm" id="e_role" placeholder="">
						        <a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
							    <a id=""  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
					    	</div>
					    </div>
					  </div>
				</div>
		      </div>
		      <div class="modal-footer">
		      	<input type="hidden" id="e_id" value="" />
		        <button id="cancel" type="button" class="btn btn-default btn-sm" > <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
		        <button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
		      </div>
		    </div>
		  </div>
		</div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen}"/>
	[@permission code="P-DEFASS-001"]<input type="hidden" id="viewPer" value="${displayVar!}" />[/@permission]
	[@permission code="P-DEFASS-002"]<input type="hidden" id="editPer" value="${displayVar!}" />[/@permission]
	[@permission code="P-DEFASS-003"]<input type="hidden" id="cancelPer" value="${displayVar!}" />[/@permission]
</body>
</html>
