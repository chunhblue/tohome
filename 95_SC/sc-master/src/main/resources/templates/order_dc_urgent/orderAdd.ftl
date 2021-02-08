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
	<script src="${m.staticpath}/[@spring.message 'js.orderDcEdit'/]"></script>
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
		.zgGrid-main-body{
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
[@common.nav "HOME&Ordering&Urgent Order Entry(To DC)"][/@common.nav]
<div class="container-fluid" id="main_box">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Order Basic Information
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aStore">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for="order_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">PO No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="order_id" class="form-control input-sm" value="" readonly="readonly">
									</div>

									<label for="delivery_center_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DC</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="hidden" class="form-control input-sm" id="delivery_center_id" readonly="readonly">
										<input type="text" id="delivery_center_name" class="form-control input-sm" value="" readonly="readonly">
									</div>

									[#--<label for="delivery_center_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">DC</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="delivery_center_id" placeholder="" autocomplete="off" nextele="">
											<a id="dc_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="dc_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>--]

									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<button class="btn btn-default btn-sm glyphicon glyphicon-plus"
												role="button" id="auto_import_item" style="height: 30px;">
											Automatically Import Order List
										</button>
									</div>
									<div class="col-xs-12 col-sm-2 col-md-2 col-lg-1"></div>
								</div>
								<div class="form-group">
									<label for="od_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="od_date" placeholder="Order Date" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for="orderType"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Type</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="orderType" class="form-control input-sm">
											<option value="">-- All/Please Select --</option>
										</select>
									</div>

									<label for="reviewSts"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">PO Status</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select disabled id="reviewSts" class="form-control input-sm" nextele="">
											<option value="1">Pending</option>
											<option value="5">Rejected</option>
											<option value="6">Withdrawn</option>
											<option value="7">Expired</option>
											<option value="10">Approved</option>
											<option value="20">Received</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="orderRemark"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
										<input type="text" id="orderRemark" class="form-control input-sm">
									</div>

									<label for="order_total_amt"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" hidden>Total Amount</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
										<input type="hidden" id="order_total_amt" placeholder="Total Amount" class="form-control input-sm" readonly="readonly">
									</div>
									[#--总订货数量--]
									<input hidden type="text" id="order_total_qty" placeholder="Total Qty" readonly="readonly">
								</div>

								<div class="form-group">
									<label for="ca_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date Created</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="ca_date" readonly="true" placeholder="Date Created" class="form-control input-sm" type="text" value="">
									</div>

									<label for="ca_user"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="ca_user" readonly="true" placeholder="Created By" class="form-control input-sm" type="text" value="">
									</div>

									<label for="md_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date Modified</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="md_date" readonly="true" placeholder="Date Modified" class="form-control input-sm" type="text" value="">
									</div>

									<label for="md_user"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Modified By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="md_user" readonly="true" placeholder="Modified By" class="form-control input-sm" type="text" value="">
									</div>
								</div>

								[#--<div class="form-group">
									<label for="orderRemark"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-8">
										<input type="text" id="orderRemark" class="form-control input-sm">
									</div>
								</div>--]
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
			<button id="returnsSubmitBut" type="button" class="btn btn-primary "><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>

<div id="update_dialog" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-lg" role="document" >
		<div class="modal-content" style="width: 950px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Order Details</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-horizontal" >
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-group">
								<label for="a_item"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label"><span style="color:red;">* </span>Item</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<div class="aotu-pos">
										<input type="text" class="form-control my-automatic input-sm" id="a_item" placeholder="" autocomplete="off" onmouseover="this.title=this.value" data-toggle="tooltip" data-trigger="hover">
										<a id="item_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
										<a id="item_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
									</div>
								</div>
[#--								<label for="item_input_code"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Code</label>--]
[#--								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">--]
[#--									<div class="aotu-pos">--]
[#--										<input type="text" class="form-control my-automatic input-sm" id="item_input_code" placeholder="" autocomplete="off">--]
[#--										<a id="search_item_input" href="javascript:void(0);" title="Item Code" class="auto-but glyphicon glyphicon-zoom-in item-select-but "></a>--]
[#--									</div>--]
[#--								</div>--]

								<label for="item_input_barcode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Barcode</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_barcode" readonly="readonly" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
[#--								<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Name</label>--]
[#--								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">--]
[#--									<div class="aotu-pos" id="auto_item">--]
										<input type="hidden" readonly="readonly" class="form-control input-sm" id="item_input_name" placeholder="">
[#--									</div>--]
[#--								</div>--]
								<label for="item_input_spec"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Specification</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_spec" readonly="readonly" class="form-control input-sm">
								</div>

								<label for="item_input_uom"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">UOM</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_uom" readonly="readonly" class="form-control input-sm">
									<input type="hidden" id="item_input_uomCd">
								</div>
							</div>
							<div class="form-group">
								<label for="item_input_orderBatchQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Incremental Quantity</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_orderBatchQty" readonly="readonly" class="form-control input-sm">
								</div>
								<label for="item_input_converter"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Converter</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_converter" readonly="readonly" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
								<label for="item_input_minOrderQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">DC MOQ</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_minOrderQty" readonly="readonly" class="form-control input-sm">
								</div>
								<label for="item_input_minDisplayQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Qty on Shelf</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_minDisplayQty" readonly="readonly" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
								<label for="item_input_psd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">PSD</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_psd" readonly="readonly" class="form-control input-sm">
								</div>

								<label for="item_input_autoOrderQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">AI Recommended Order Qty</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_autoOrderQty" readonly="readonly" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
								<label for="inventoryQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Inventory Qty</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="inventoryQty" readonly="readonly" class="form-control input-sm">
								</div>

								<label for="orderQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label"><span style="color:red;">* </span>Order Qty</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="orderQty" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
								<label for="item_input_orderNochargeQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Free Qty</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_orderNochargeQty" readonly="readonly" class="form-control input-sm">
								</div>

								<label for="item_input_orderTotalQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Total Qty</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="text" id="item_input_orderTotalQty" readonly="readonly" class="form-control input-sm">
								</div>
							</div>
							<div class="form-group">
								<label for="item_input_salePrice"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label" hidden="true">Price</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="hidden" id="item_input_salePrice" readonly="readonly" class="form-control input-sm" >
								</div>

								<label for="item_input_orderPrice"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label" hidden="true">Purchasing Price</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="hidden" id="item_input_orderPrice" readonly="readonly" class="form-control input-sm">
								</div>

								<label for="item_input_orderTotalAmt"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label" hidden="true">Price Amount</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
									<input type="hidden" id="item_input_orderTotalAmt" readonly="readonly" class="form-control input-sm" >
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="item_input_vendorId">
				[#--税区分--]
				<input type="hidden" id="purchaseVatCd">
				<input type="hidden" id="taxRate">
				<button id="cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="businessDate" value="${date!}"/>
<input type="hidden" id="toKen" value="${toKen!}">
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="searchJson" value=""/>
</body>
</html>
