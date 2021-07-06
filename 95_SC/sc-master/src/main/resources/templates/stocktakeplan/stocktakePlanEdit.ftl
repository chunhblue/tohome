[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
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
	<script src="${m.staticpath}/[@spring.message 'js.stocktakePlanEdit'/]"></script>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	[#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
	[#--<![endif]--]
	<script>
	</script>
	<STYLE>
		.box div.radio-inline {
			margin-left: 0;
			padding-left: 0;
		}
		.box div.radio-inline label {
			padding-left: 20px;
			position: relative;
		}
		.good-select .form-control {
			padding-right: 35px;
		}
		.good-select .item-select-but {
			width: 30px;
			right: 1px;
		}
		.my-user-info{
			padding:10px;
			overflow:hidden;
		}
		span.my-label,
		span.my-text{
			float:left;
			color: #333333;
			font-size:12px;
			margin-left:10px;
		}
		span.my-text{
			margin-right:20px;
		}
		span.my-label{
			font-weight: 900;
		}
		.my-div{
			overflow:hidden;
			float:left;
		}
		.del-alert{
			display:none;
		}
		.zgGrid-main-body {
			border-left: 1px solid #428bca;
			border-right: 1px solid #428bca;
			overflow: hidden;
			overflow-y: scroll;
			clear: both;
			position: relative;
			overflow-x: auto;
		}
	</STYLE>
	<!--页头-->
	[@common.header][/@common.header]
</head>

<body>

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--导航-->
[@common.nav "HOME&Stocktaking&Stocktaking Plan Setting"][/@common.nav]
<div class="container-fluid" id="main_box">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Stocktaking info
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2 disabled">
										<input type="text" id="storeNo" class="form-control input-sm" disabled>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="pd_date" nextele="sell_end_date" placeholder="Date" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="store">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Type</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="skType" class="form-control input-sm" nextele="pd_status">
											<option value="">-- All/Please Select --</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" >Stocktaking Time</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="pd_start_time" nextele="sell_end_date" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">
									</div>
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input  id="pd_end_time" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" >Method</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="skMethod" disabled class="form-control input-sm" nextele="dj_status"></select>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" >Status</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="skStatus" disabled class="form-control input-sm" nextele="pd_status"></select>
									</div>
								</div>

								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="createUser" readonly="readonly" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Modified By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="updateUser" readonly="readonly" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-5 col-m">
										<input type="text" id="remarks" class="form-control input-sm">
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div><!--box end-->
		</div>
	</div>

	<!--row 分割-->
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<table id="zgGridTtable" style="border: 1px solid transparent !important;"></table>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<button id="saveBut" type="button" class="btn btn-default saveBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			<button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>

<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="" style="width: 700px; margin: 0 auto">
	<div class="modal-dialog modal-lg" role="document" style="width: 80%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Start Stocktaking</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<table id="pmaGridTable"></table>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>
<!--审核-->
[@common.audit][/@common.audit]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="enterFlag" value="${enterFlag!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="piCdParam" value="${piCdParam!}"/>
<input type="hidden" id="piDateParam" value="${piDateParam!}"/>
<input type="hidden" id="businessDate" value="${businessDate!}"/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
</body>
</html>
