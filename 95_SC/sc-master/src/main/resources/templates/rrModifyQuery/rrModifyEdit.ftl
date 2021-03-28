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
	<script src="${m.staticpath}/[@spring.message 'js.rrModifyEdit'/]"></script>
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
[@common.nav "HOME&Receiving&Receiving And Return Document Correction Entry"][/@common.nav]
<div class="container-fluid" id="main_box">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Document Information
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for="modificationType"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Modification Type</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="modificationType" class="form-control input-sm">
											<option value="">-- All/Please Select --</option>
											<option value="07">[07] Receiving Document Correction</option>
											<option value="08">[08] Return Document Correction</option>
										</select>
									</div>
									<label for="storeCd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Store</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="storeCd">
                                            <a id="storeCdRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="storeCdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
									</div>
								</div>

								<div class="form-group">
									<label for="modificationDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Modification Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="modificationDate" readonly="true" placeholder="Modification Date" class="form-control input-sm select-date" type="text" value="">
									</div>
									<label for="orVoucherNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Original Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="orVoucherNo">
                                            <a id="orgOrderRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="orgOrderRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
									</div>
								</div>

								<div class="form-group">
									<label for="voucherNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Modified Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="voucherNo" readonly="true" class="form-control input-sm">
									</div>
									<label for="vendorName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Vendor</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="hidden" id="vendorId" readonly="true" class="form-control input-sm">
										<input type="text" id="vendorName" readonly="true" class="form-control input-sm">
									</div>
								</div>

								<div class="form-group">
									<label for="description"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Modification Description</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-6">
										<input type="text" id="description" class="form-control input-sm">
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
			<table id="zgGridTable"></table>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
		    <button id='saveBtn' type='button' class='btn btn-primary'><span class='glyphicon glyphicon-ok icon-right'></span>Submit</button>
			<button id="approvalBut" type="button" class="btn btn-default"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>

	<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
		<div class="modal-dialog modal-md" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Information</h4>
				</div>
				<div class="modal-body">
					<div class="form-horizontal">
						<div class="form-group">
							<label for="barcode" class="col-sm-2 col-lg-5 control-label">Item Barcode</label>
							<div class="col-sm-6">
								<input type="text" id="barcode" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group">
							<label for="itemId" class="col-sm-2 col-lg-5 control-label">Item Code</label>
							<div class="col-sm-6">
                                <input type="text" id="itemId" readonly="true" class="form-control input-sm">
								<input type="hidden" id="orderPrice" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group">
							<label for="itemName" class="col-sm-2 col-lg-5 control-label">Item Name</label>
							<div class="col-sm-6">
                                <input type="text" id="itemName" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group">
							<label for="specification" class="col-sm-2 col-lg-5 control-label">Specification</label>
							<div class="col-sm-6">
                                <input type="text" id="specification" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group">
							<label for="uom" class="col-sm-2 col-lg-5 control-label">Purchase UOM</label>
							<div class="col-sm-6">
                                <input type="text" id="uom" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group" id="orderQtyById">
							<label for="orderQty" class="col-sm-2 col-lg-5 control-label">Order Quantity</label>
							<div class="col-sm-6">
								<input type="text" id="orderQty" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group" id="InventoryQtyByDct" hidden>
							<label for="InventoryQty" class="col-sm-2 col-lg-5 control-label">Inventory Quantity</label>
							<div class="col-sm-6">
								<input type="text" id="InventoryQty" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group" hidden>
							<label for="orderNochargeQty" class="col-sm-2 col-lg-5 control-label">Order Free Quantity</label>
							<div class="col-sm-6">
								<input type="text" id="orderNochargeQty" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group">
							<label for="rrOrderQty"  id="reOrderQty" class="col-sm-2 col-lg-5 control-label">Return Quantity</label>
							<label for="rrOrderQty" id="rOrderQty" class="col-sm-2 col-lg-5 control-label">Receipt Quantity</label>
							<div class="col-sm-6">
								<input type="text" id="rrOrderQty" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group">
							<label for="modifyQty" class="col-sm-2 col-lg-5 control-label">Modification Quantity</label>
							<div class="col-sm-6">
								<input type="text" id="modifyQty" class="form-control input-sm" placeholder="Please Entry">
							</div>
						</div>


						<div class="form-group" hidden>
							<label for="receiveNochargeQty" class="col-sm-2 col-lg-5 control-label">Received/Returned Free Qty</label>
							<div class="col-sm-6">
								<input type="text" id="receiveNochargeQty" readonly="true" class="form-control input-sm">
							</div>
						</div>

						<div class="form-group" hidden>
							<label for="modifyNochargeQty" class="col-sm-2 col-lg-5 control-label">Modification Free Quantity</label>
							<div class="col-sm-6">
								<input type="text" id="modifyNochargeQty" class="form-control input-sm" placeholder="Please Entry">
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="e_id" value="" />
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
<input type="hidden" id="viewSts" value="${viewSts!}"/>
<input type="hidden" id="_storeCd" value="${storeCd!}"/>
<input type="hidden" id="_orderId" value="${orderId!}"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="returnType" > [#-- 退货类型: 10 直接退货 20 原订单退货--]
</body>
</html>
