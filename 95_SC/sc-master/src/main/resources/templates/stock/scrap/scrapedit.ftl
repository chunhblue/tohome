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
	<script src="${m.staticpath}/[@spring.message 'js.stockScrapEdit'/]"></script>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	[#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
	[#--<![endif]--]
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
		[id$="_tr_ckline"]{
			width: 50px;
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
[@common.nav "HOME&Inventory&Item Write-off Detail"][/@common.nav]
<div class="container-fluid" id="main_box">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title">
					<div class="tt">
						Document Information
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for="tf_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="tf_cd" readonly="true" class="form-control input-sm">
									</div>
									<label for="tf_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="tf_date" readonly="true" placeholder="Document Date" class="form-control input-sm" type="text" value="">
									</div>
									<label for="dj_type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document Type</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="dj_type" disabled="disabled" class="form-control input-sm">
											<option value="">-- All/Please Select --</option>
											<option value="603" selected="selected">Inventory Write-off</option>
										</select>
									</div>
									<label for="dj_status"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" hidden style="white-space: nowrap">Document Status</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
										<select id="dj_status" disabled="disabled" class="form-control input-sm">
											<option value="">-- All/Please Select --</option>
											<option value="0"></option>
											<option value="1">Pending</option>
											<option value="5">Rejected</option>
											<option value="6">Withdrawn</option>
											<option value="10">Approved</option>
										</select>
									</div>

									<label for="vstore"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos good-select">
											<input type="text" class="form-control my-automatic input-sm" id="vstore">
											<a id="vstoreRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="vstoreRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div>

								<div class="form-group">
									<label for="scrapSum"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Amount</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="scrapSum" readonly="true" class="form-control input-sm">
									</div>
									<label for="cRemark"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-5">
										<input type="text" id="cRemark" class="form-control input-sm">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="modifier"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="creater" class="form-control input-sm">
									</div>
									<label for="md_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date Created</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="ca_date" readonly="true" placeholder="Created Date" class="form-control input-sm" type="text" value="">
									</div>

									<label for="modifier"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Modified By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="modifier" class="form-control input-sm">
									</div>
									<label for="md_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date Modified</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="md_date" readonly="true" placeholder="Modify Date" class="form-control input-sm" type="text" value="">
									</div>

									[#--<label for="ap_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Approved By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="ap_name" class="form-control input-sm">
									</div>
									<label for="ap_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date Approved</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="ap_date" readonly="true" placeholder="Approve Date" class="form-control input-sm" type="text" value="">
									</div>--]
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
			<table id="zgGridTtable"></table>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
		    <button id='resetBtn' type='button' class='btn btn-default'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
		    <button id='saveBtn' type='button' class='btn btn-default'><span class='glyphicon glyphicon-ok icon-right'></span>Submit</button>
			<button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>

<div id="update_dialog" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Write-off Information</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-horizontal" >
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-group">
								<label for="item_input"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Item/Barcode</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<div class="aotu-pos good-select">
										<input type="text" class="form-control my-automatic input-sm" placeholder="Please enter 3 digit" id="item_input" style="overflow:scroll;text-overflow:ellipsis;">
										<a id="itemRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
										<a id="itemRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
									</div>
								</div>

								<label for="item_input_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Item Barcode</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="hidden" id="tax_rate" readonly="true" class="form-control input-sm">
									<input type="hidden" id="item_input_price" readonly="true" class="form-control input-sm">
									<input type="text" id="item_input_cd" readonly="true" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
								<label for="item_input_uom"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">UOM</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_uom" readonly="true" class="form-control input-sm">
								</div>

								<label for="item_input_spec"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Specification</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_spec" readonly="true" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
[#--								<label for="item_input_tamount"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Quantity</label>--]
[#--								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">--]
[#--									<input type="hidden" id="on_hand_qty" class="form-control input-sm">--]
[#--									<input type="text" id="item_input_tamount" class="form-control input-sm">--]
[#--								</div>--]
								<label for="item_input_tamount"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Quantity</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_tamount" class="form-control input-sm">
								</div>
								<label for="adjustReason"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Write-off Reason</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<div class="aotu-pos good-select">
										<input type="text" class="form-control my-automatic input-sm" id="adjustReason">
										<a id="reasonRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
										<a id="reasonRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="on_hand_qty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Inventory Qty</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="on_hand_qty" class="form-control input-sm">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="e_id" value="" />
				<button id="dialog_cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
				<button id="dialog_affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>
<!--附件一览-->
[@common.attachments][/@common.attachments]
<!--审核-->
[@common.audit][/@common.audit]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="viewSts" value="${viewSts!}"/>
<input type="hidden" id="itemSts" value="0"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="_storeCd" value="${data.storeCd!}"/>
<input type="hidden" id="_voucherNo" value="${data.voucherNo!}"/>
<input type="hidden" id="_voucherType" value="${data.voucherType!}"/>
<input type="hidden" id="_voucherDate" value="${data.voucherDate!}"/>
<input type="hidden" id="_storeCd1" value="${data.storeCd1!}"/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="totalItemQty" value="">
<input type="hidden" id="totalQty" value="">
</body>
</html>
