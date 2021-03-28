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
	<script src="${m.staticpath}/[@spring.message 'js.orderNewStoreEdit'/]"></script>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	[#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
	[#--<![endif]--]
	<STYLE>
		.grid-bg{
			background-color: #FF4500;
			color: #f0f0f0;
		}
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
[@common.nav "HOME&Ordering&Order Entry"][/@common.nav]
<div class="container-fluid" id="main_box">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Basic Information
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>
									<div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">
										<input type="text" id="order_date" class="form-control input-sm" value="${data.orderDate?substring(6,8)}/${data.orderDate?substring(4,6)}/${data.orderDate?substring(0,4)}" readonly="true" style="text-align: center;">
									</div>
									<label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>
[#--									<div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">--]
[#--										<input type="text" id="store_cd" class="form-control input-sm" value="${data.storeCd!}" readonly="true">--]
[#--									</div>--]
									<div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">
										<input type="text" id="store_name" class="form-control input-sm" value="${data.storeName!}" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									[#if data.orderType=="1"]
										<label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-1 control-label">Top Department</label>
										<div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">
											<input type="text" id="dep" class="form-control input-sm" value="${data.depName!}" readonly="true">
										</div>
										<label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>
										<div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">
											<input type="text" id="pma" class="form-control input-sm" value="${data.pmaName!}" readonly="true">
										</div>
									[#elseif data.orderType=="2"]
										<label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-1 control-label">Shelf</label>
										<div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">
											<input type="text" id="shelf" class="form-control input-sm" value="${data.shelf!}" readonly="true">
										</div>
									[/#if]
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
		<div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-1" style="height: 550px" id="itemGrid">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Quick Search
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									[#if data.orderType=="1"]
									[#--									<label for=""  class="col-sm-2 control-label">Department</label>--]
									[#--									<div class="col-sm-3">--]
									[#--										<input type="text" id="pma" class="form-control input-sm" value="${data.pmaName!}" readonly="true">--]
									[#--									</div>--]
[#--										<label for="param_category" class="col-sm-2 control-label">Category</label>--]
[#--										<div class="col-sm-3">--]
[#--											<select id="param_category" class="form-control input-sm">--]
[#--												<option value="" selected>-- All/Please Select --</option>--]
[#--											</select>--]
[#--										</div>--]
[#--										<label for="param_subCategory" class="col-sm-2 control-label">Sub-Category</label>--]
[#--										<div class="col-sm-3">--]
[#--											<select id="param_subCategory" class="form-control input-sm">--]
[#--												<option value="" selected>-- All/Please Select --</option>--]
[#--											</select>--]
[#--										</div>--]
										<label for="category" class="col-sm-2 control-label">Category</label>[#--中分类--]
										<div class="col-sm-3">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="category">[#-- placeholder="全中分类"--]
												<a id="categoryRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="categoryRemove" href="javascript:void(0);" title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle" ></a>
											</div>
										</div>
										<label for="subCategory" class="col-sm-2 control-label">Sub Category</label>[#--小分类--]
										<div class="col-sm-3">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="subCategory">[#-- placeholder="全小分类"--]
												<a id="subCategoryRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="subCategoryRemove" href="javascript:void(0);" title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
									[#elseif data.orderType=="2"]
										<label for="param_shelf"  class="col-sm-2 control-label">Shelf</label>
										<div class="col-sm-3">
											<select id="param_shelf" class="form-control input-sm" disabled="disabled">
												<option value="" selected>-- All/Please Select --</option>
											</select>
											[#--										<input type="text" id="param_shelf" class="form-control input-sm" value="${data.shelf!}" readonly="true">--]
										</div>
										<label for="param_subShelf"  class="col-sm-2 control-label">Sub-Shelf</label>
										<div class="col-sm-3">
											<select id="param_subShelf" class="form-control input-sm">
												<option value="" selected>-- All/Please Select --</option>
											</select>
											[#--										<input type="text" id="param_subShelf" class="form-control input-sm" value="" >--]
										</div>
									[/#if]
									<div class="col-sm-2">
										<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="barcode" class="col-sm-2 control-label">Barcode</label>
									<div class="col-sm-3">
										<input type="text" id="barcode" class="form-control input-sm" value="" >
									</div>
									<label for="itemCode" class="col-sm-2 control-label">Item Code</label>
									<div class="col-sm-3">
										<input type="text" id="itemCode" class="form-control input-sm" value="" >
									</div>
									<div class="col-sm-2">
										<button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
									</div>
								</div>
								<div class="form-group">
									<label for="itemName" class="col-sm-2 control-label">Item Name</label>
									<div class="col-sm-3">
										<input type="text" id="itemName" class="form-control input-sm" value="" >
									</div>

									<label for="vendorId" class="col-sm-2 control-label">Supplier</label>
									<div class="col-sm-3">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="vendorId">
											<a id="" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="vendorIdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!--box end-->
			<table id="zgGridTtable"></table>
		</div>
		<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4" style="height: 550px">
			<div class="box">
				<div class="box-title">
					<div class="tt">
						Item Information
					</div>
				</div>
				<div class="box-body box-body-padding-10 item-info">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal" id="item_info" >
								<div class="form-group">
									<label for="articleId"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Item Code</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="articleId" class="form-control input-sm" readonly="true">
									</div>
								</div>
								<div class="form-group">
									<label for="vendorName"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Supplier</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="vendorName" class="form-control input-sm" readonly="true">
									</div>
								</div>
								<div class="form-group">
									<label for="preserveTypeName"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Storage Condition</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="preserveTypeName" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group" hidden>
									<label for="spec"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Specification</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="spec" class="form-control input-sm" readonly="true">
									</div>
								</div>
								<div class="form-group">
									<label for="unitName"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Order UOM</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="unitName" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="orderUnitQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" >converter</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="orderUnitQty" class="form-control input-sm" readonly="true">
									</div>
								</div>
								<div class="form-group" >
									<label for="minOrderQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" id="label_minOrderQty">DC MOQ</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="minOrderQty" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									[#--订货批量--]
									<label for="orderBatchQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Incremental Quantity</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="orderBatchQty" class="form-control input-sm" readonly="true">
									</div>
									<label for="maxOrderQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" hidden>Max. Order Qty</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6" hidden>
										<input type="text" id="maxOrderQty" class="form-control input-sm" readonly="true">
									</div>
								</div>
								<div class="form-group" hidden>
									<label for="orderPromotionType"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" style="white-space:nowrap">Purchasing Promotion Type</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="orderPromotionType" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="minDisplayQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Qty on Shelf</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="minDisplayQty" class="form-control input-sm" readonly="true">
									</div>
								</div>
								<div class="form-group" hidden>
									<label for="adviseSalePrice"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">RSP</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="adviseSalePrice" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="dms"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" style="white-space:nowrap">Avg. Sales Amount DMS</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="dms" class="form-control input-sm" readonly="true">
									</div>
									[#--									<label for="vendorMinOrderQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" style="white-space:nowrap">Vendor Min. Order Qty</label>--]
									[#--									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">--]
									[#--										<input type="text" id="vendorMinOrderQty" class="form-control input-sm" readonly="true">--]
									[#--									</div>--]
								</div>
								<div class="form-group">
									<label for="warrantyDays"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Shelf Life(day)</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="warrantyDays" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group" hidden>
									<label for="orderQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Order Qty</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="orderQty" class="form-control input-sm">
									</div>

									<div class="col-sm-2 col-md-1 col-lg-1">
										<button id="updateOrderQty" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Order</button>
									</div>
								</div>
								<div class="form-group">
									<label for="articleNoChargeId"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Free Item Code</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="articleNoChargeId" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="freeItemQty"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Free Order Qty</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="freeItemQty" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
									<label for="promotionDescription"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" style="white-space: nowrap">Promotion Description</label>
									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">
										<input type="text" id="promotionDescription" class="form-control input-sm" readonly="true">
									</div>
								</div><!-- form-group -->
[#--								<div class="form-group">--]
[#--									<label for="receiveDateLimit"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label" style="white-space:nowrap">Expire Date(purchasing)</label>--]
[#--									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">--]
[#--										<input type="text" id="receiveDateLimit" class="form-control input-sm" readonly="true">--]
[#--									</div>--]
[#--								</div><!-- form-group -->--]
[#--								<div class="form-group">--]
[#--									<label for="saleDateLimit"  class="col-xs-4 col-sm-2 col-md-4 col-lg-4 control-label">Expire Date(selling)</label>--]
[#--									<div class="col-xs-8 col-sm-3 col-md-6 col-lg-6">--]
[#--										<input type="text" id="saleDateLimit" class="form-control input-sm" readonly="true">--]
[#--									</div>--]
[#--								</div>--]
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
			<input hidden id="loadPage">
			<table id="zgGridTtable1"></table>
		</div>
	</div>
	<!--row 分割-->
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<table id="zgGridTtable2"></table>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			[#--<button id="submitBut" type="button" class="btn btn-primary submitBut"><span class="glyphicon glyphicon-ok icon-right"></span>提交</button>--]
			<button id="submitBut" type="button" class="btn btn-default submitBut"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			<button id="submitAuditBut" type="button" class="btn btn-default submitBut"><span class="glyphicon glyphicon-saved icon-right"></span>Submit</button>
			<button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>
<div id="orderAdd_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Quick Order Entry</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="i_articleId"  class="col-sm-5 control-label">Item Code</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_articleId" placeholder="">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="i_articleName" class="col-sm-5 control-label">Item Name</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_articleName" placeholder="">
							</div>
						</div>
					</div>

					<div class="form-group">
						<label for="i_realtimeStock" class="col-sm-5 control-label">Real-time Inventory</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_realtimeStock" placeholder="">
							</div>
						</div>
					</div>

					<div class="form-group" id="psd">
						<label for="i_psd" class="col-sm-5 control-label">PSD</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_psd" placeholder="">
							</div>
						</div>
					</div>

					<div class="form-group" id="autoOrderQty">
						<label for="i_autoOrderQty" class="col-sm-5 control-label">Recommended Order Qty</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_autoOrderQty" placeholder="">
							</div>
						</div>
					</div>

					<div class="form-group">
						<label for="i_orderQty" class="col-sm-5 control-label">Order Qty</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_orderQty" placeholder="">
							</div>
						</div>
					</div>
					<div class="form-group">[#--Min. Order Qty--]
						<label for="i_minOrderQty" class="col-sm-5 control-label" id="label_i_minOrderQty">DC MOQ</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_minOrderQty" placeholder="" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group" hidden>
						<label for="i_maxOrderQty" class="col-sm-5 control-label">Max. Order Qty</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_maxOrderQty" placeholder="" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="i_orderBatchQty" class="col-sm-5 control-label">Incremental Quantity</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_orderBatchQty" placeholder="" readonly="readonly">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="e_id" value="" />
				<button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>
<!--审核-->
[@common.audit][/@common.audit]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="storeCd" value="${data.storeCd!}"/>
<input type="hidden" id="orderDate" value="${data.orderDate!}"/>
<input type="hidden" id="depCd" value="${data.depCd!}"/>
<input type="hidden" id="pmaCd" value="${data.pmaCd!}"/>
<input type="hidden" id="categoryCd" value="${data.categoryCd!}"/>
<input type="hidden" id="subCategoryCd" value="${data.subCategoryCd!}"/>
<input type="hidden" id="shelf" value="${data.shelf!}"/>
<input type="hidden" id="subShelf" value="${data.subShelf!}"/>
<input type="hidden" id="orderType" value="${data.orderType!}"/>
<input type="hidden" id="type" value="${type!}"/>
<input type="hidden" id="storeSts" value="${storeSts!}"/>
<input type="hidden" id="searchJson" value=""/>
[@permission code="SC-NS-ORDER-002"]<input type="hidden" class="permission-verify" id="orderButEdit" value="${displayVar!}" />[/@permission]
</body>
</html>
