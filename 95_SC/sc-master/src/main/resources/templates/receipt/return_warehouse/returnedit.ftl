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
	<script src="${m.staticpath}/[@spring.message 'js.receiptReturnWarehouseEdit'/]"></script>
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
		[id$="_tr_ckline_"]{
			 width: 50px;
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
[@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Receiving&Item Return Entry(To DC)"][/@common.nav]
<div class="container-fluid" id="main_box">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title">
					<div class="tt">
						Basic Information
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for="order_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="order_id" class="form-control input-sm" >
									</div>

									<label for="order_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Date Returned</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input id="order_date" placeholder="Returning Date" class="form-control input-sm select-date" type="text" >
									</div>

									<label for="org_order_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"[#-- style="white-space: nowrap"--]>Original Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="org_order_id" placeholder="" autocomplete="off">
											<a id="org_order_id_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="org_order_id_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>

									[#--									<label for="org_order_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Source Document No.</label>--]
									[#--									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
									[#--										<input type="text" id="org_order_id" class="form-control input-sm">--]
									[#--									</div>--]
									<label for="store_cd" hidden  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2" hidden>
										<input type="text" id="store_cd" class="form-control input-sm" >
									</div>

									<label for="store_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="store_name" class="form-control input-sm" >
									</div>

									[#--									<label for="a_store"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>--]
									[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
									[#--										<div class="aotu-pos">--]
									[#--											<input type="text" class="form-control my-automatic input-sm" id="a_store" placeholder="" autocomplete="off">--]
									[#--											<a id="store_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>--]
									[#--											<a id="store_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>--]
									[#--										</div>--]
									[#--									</div>--]

									[#--									<label for="warehouseCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DC No.</label>--]
									[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
									[#--										<div class="aotu-pos">--]
									[#--											<input type="text" class="form-control my-automatic input-sm" id="warehouseCd">--]
									[#--											<a id="" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>--]
									[#--											<a id="vendorIdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>--]
									[#--										</div>--]
									[#--									</div>--]
								</div><!-- form-group -->

								<div class="form-group">
									<label for="dc_id" hidden class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Id</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2" hidden>
										<input type="text" id="dc_id" class="form-control input-sm" >
									</div>

									<label for="dc_name"  class="col-xs-12 col-sm-3 col-md-2 col-lg-1 control-label" >DC</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="dc_name" readonly="true" class="form-control input-sm">
									</div>

									<label for="total_qty"  class="col-xs-12 col-sm-3 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Total Return Qty</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="total_qty" readonly="true" class="form-control input-sm" style="text-align: right">
									</div>

									<label for="total_amt" hidden class="col-xs-12 col-sm-3 col-md-2 col-lg-1 control-label" >Total Amount</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2" hidden>
										<input type="text" id="total_amt" readonly="true" class="form-control input-sm">
									</div>

									<label for="order_remark"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
										<input type="text" id="order_remark" class="form-control input-sm">
									</div>
								</div>

								<div class="form-group">
									<label for="create_user"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Created By</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input id="create_user" placeholder="Created By" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for="create_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Created Date</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input id="create_date" placeholder="Created Date" class="form-control input-sm select-date" type="text" value="">
									</div>
									<label for="update_user"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Modified By</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input id="update_user" placeholder="Modification By" class="form-control input-sm select-date" type="text" value="">

									</div>
									<label for="update_ymd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Date Modified</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<input id="update_ymd" placeholder="Modification Date" class="form-control input-sm select-date" type="text" value="">
									</div>
								</div>
							</div>
						</div>
						<button id="search" type="button" class="btn btn-primary btn-sm hidden"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
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
			<button id="returnsSubmitBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			<button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>

<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Item Information</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="a_item"  class="col-sm-2 col-lg-4 control-label">Item</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="a_item" placeholder="" autocomplete="off" onmouseover="this.title=this.value">
								<a id="item_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="item_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
							<input type="hidden" id="itemName" readonly="true" >
						</div>
					</div>
					<div class="form-group">
						<label for="barcode" class="col-sm-2 col-lg-4 control-label">Barcode</label>
						<div class="col-sm-6">
							<input type="text" id="barcode" readonly="true" class="form-control input-sm">
						</div>
					</div>
					<div class="form-group" hidden>
						<label for="vendorId" class="col-sm-2 col-lg-4 control-label">Vendor Id</label>
						<div class="col-sm-6">
							<input type="text" id="vendorId" readonly="true" class="form-control input-sm">
						</div>
					</div>
					<div class="form-group">
						<label for="unitName" class="col-sm-2 col-lg-4 control-label">UOM</label>
						<div class="col-sm-6">
							<input type="text" id="unitName" readonly="true" class="form-control input-sm">
							<input type="hidden" id="unitId" readonly="true" >
						</div>
					</div>
					<div class="form-group">
						<label for="spec" class="col-sm-2 col-lg-4 control-label">Specification</label>
						<div class="col-sm-6">
							<input type="text" id="spec" readonly="true" class="form-control input-sm">
						</div>
					</div>
					<div class="form-group" hidden>[#--退货价格--]
						<label for="orderPrice" class="col-sm-2 col-lg-4 control-label">Returning Price</label>
						<div class="col-sm-6">
							<input type="text" id="orderPrice" readonly="true" class="form-control input-sm">
						</div>
					</div>
					<div class="form-group" hidden>
						<label for="orderAmount" class="col-sm-2 col-lg-4 control-label">Returning Amount</label>
						<div class="col-sm-6">
							<input type="text" id="orderAmount" readonly="true" class="form-control input-sm">
						</div>
					</div>
					<div class="form-group">
						<label for="orderQty" class="col-sm-2 col-lg-4 control-label">Returning Quantity</label>
						<div class="col-sm-6">
[#--							orderQty不能手动输入--]
							<input type="text" id="orderQty" class="form-control input-sm" readonly="true" autocomplete="off">
						</div>
						<input type="text" hidden id="orgOrderQty">
						<input type="text" hidden id="orgOrderNochargeQty">
					</div>
					<div class="form-group" hidden>
						[#--							orderNoCharge不能手动输入--]
						<label for="orderNoChargeQty" class="col-sm-2 col-lg-4 control-label">Return Free Qty</label>
						<div class="col-sm-6">
							<input type="text" id="orderNoChargeQty" class="form-control input-sm" readonly="true" autocomplete="off" disabled="disabled">
						</div>
[#--						<input type="text" hidden id="orgOrderQty">--]
[#--						<input type="text" hidden id="orgOrderNochargeQty">--]
					</div>

					<div class="form-group">
						<label for="reasonId" class="col-sm-2 col-lg-4 control-label">Return Reason</label>
						<div class="col-sm-6">
							<input type="text" id="reasonId" class="form-control input-sm" readonly="true">
						</div>
					</div>

					<div class="form-group">
						<label for="receiveQty" class="col-sm-2 col-lg-4 control-label">Actual Return Quantity</label>
						<div class="col-sm-6">
							<input type="text" id="receiveQty" class="form-control input-sm" autocomplete="off">
						</div>
					</div>
                    [#-- 实际退搭赠量可以手动输入--]
					<div class="form-group" hidden>
						<label for="receiveNoChargeQty" class="col-sm-2 col-lg-4 control-label">Actual Return Free Qty</label>
						<div class="col-sm-6">
							<input type="text" id="receiveNoChargeQty" class="form-control input-sm" autocomplete="off">
						</div>
					</div>

				</div>
			</div>

			<div class="modal-footer">
				<input type="hidden" id="dialog_flg" value="" />
				<button id="close" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
				<button id="submit" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>
<!--审核-->
[@common.audit][/@common.audit]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="orderId" value="${orderId!}">
<input type="hidden" id="flag" value="${flag}">
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
</body>
</html>
