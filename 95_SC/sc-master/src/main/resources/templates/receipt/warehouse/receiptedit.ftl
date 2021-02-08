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
	<script src="${m.staticpath}/[@spring.message 'js.receiptEdit'/]"></script>
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
[@common.nav "HOME&Receiving&DC Receiving Details"][/@common.nav]
<div class="container-fluid" id="main_box">
<!--row 分割-->
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
                                    <label for="storeName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="storeName" readonly="true" class="form-control input-sm" >
                                    </div>
                                    <label for="receiveDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Submission Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="receiveDate" readonly="true" class="form-control input-sm"  value="${date!}">
                                    </div>
                                </div><!-- form-group -->

                                <div class="form-group">
                                    <label for="receiveNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="receiveNo" readonly="true" class="form-control input-sm" type="text" >
                                    </div>
                                    <label for="orderNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">PO No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="taxRate" readonly="true" class="form-control input-sm" type="hidden" >
                                        <input id="orderNo" readonly="true" class="form-control input-sm" type="text" >
                                    </div>
                                    <label for="warehouseName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Warehouse</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="warehouseName" readonly="true" class="form-control input-sm" >
                                    </div>
                                </div><!-- form-group -->

                                <div class="form-group">
                                    <label for="orderType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Type</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="orderType" readonly="true" class="form-control input-sm" type="text" >
                                    </div>
                                    <label for="orderDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="orderDate" readonly="true" class="form-control input-sm" type="text" >
                                    </div>
                                    <label for="physicalReceivingDatee" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Receiving Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="physicalReceivingDate" class="form-control input-sm" type="text" >
                                    </div>
                                </div><!-- form-group -->

                                <div class="form-group">
                                    <label hidden for="orderAmount" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Amount</label>
                                    <div hidden class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="orderAmount" readonly="true" class="form-control input-sm" type="text" >
                                    </div>
                                    <label hidden for="receiveAmount" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Receive Amount</label>
                                    <div hidden class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="receiveAmount" readonly="true" class="form-control input-sm" type="text" >
                                    </div>
                                    <label for="reviewSts" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document Status</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <select id="reviewSts" disabled="disabled" class="form-control input-sm">
                                            <option value="1">Pending</option>
                                            <option value="5">Rejected</option>
                                            <option value="6">Withdrawn</option>
                                            <option value="10">Approved</option>
                                            <option value="15">Receiving Pending</option>
                                            <option value="20">Received</option>
                                        </select>
                                    </div>

                                    <label for="remarks"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                        <input type="text" id="remarks"  class="form-control input-sm">
                                    </div>
                                </div>

                                [#--<div class="form-group">
                                    <label for="remarks"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-8">
                                        <input type="text" id="remarks" readonly="true" class="form-control input-sm">
                                    </div>
                                </div>--]

                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
		    <button id='saveBtn' type='button' class='btn btn-default'><span class='glyphicon glyphicon-ok icon-right'></span>Save</button>
            <button id="submitAuditBut" type="button" class="btn btn-default submitBut"><span class="glyphicon glyphicon-saved icon-right"></span>Submit</button>
[#--            <button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>--]
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
                        <label for="barcode" class="col-sm-2 col-lg-4 control-label">Item Barcode</label>
                        <div class="col-sm-6">
                            <input type="text" id="barcode" disabled="disabled" class="form-control my-automatic input-sm">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="itemId" class="col-sm-2 col-lg-4 control-label">Item Code</label>
                        <div class="col-sm-6">
                            <input type="text" id="itemId" disabled="disabled" class="form-control my-automatic input-sm">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="itemName" class="col-sm-2 col-lg-4 control-label">Item Name</label>
                        <div class="col-sm-6">
                            <input type="text" id="itemName" disabled="disabled" class="form-control my-automatic input-sm">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="uom" class="col-sm-2 col-lg-4 control-label">UOM</label>
                        <div class="col-sm-6">
                            <input type="text" id="uom" disabled="disabled" class="form-control my-automatic input-sm">
                        </div>
                    </div>

                    <div class="form-group" hidden>
                        <label for="receivePrice" class="col-sm-2 col-lg-4 control-label">Receiving Price</label>
                        <div class="col-sm-6">
                            <input type="text" id="receivePrice" disabled="disabled" class="form-control my-automatic input-sm">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="receiveQty" class="col-sm-2 col-lg-4 control-label">Receiving Quantity</label>
                        <div class="col-sm-6">
                            <input type="hidden" id="orderQty" class="form-control my-automatic input-sm">
                            <input type="text" id="receiveQty" class="form-control my-automatic input-sm" placeholder="Please Entry">
                        </div>
                    </div>

                    <div class="form-group" hidden>
                        <label for="receiveNoQty" class="col-sm-2 col-lg-4 control-label">Receiving Free Quantity</label>
                        <div class="col-sm-6">
                            <input type="hidden" id="orderNoQty" class="form-control my-automatic input-sm">
                            <input type="text" id="receiveNoQty" class="form-control my-automatic input-sm" placeholder="Please Entry">
                        </div>
                    </div>

                    <div class="form-group" hidden>
                        <label for="updateAmount" class="col-sm-2 col-lg-4 control-label">Receiving Amount</label>
                        <div class="col-sm-6">
                            <input type="text" id="updateAmount" disabled="disabled" class="form-control my-automatic input-sm">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="isFreeItemText" class="col-sm-2 col-lg-4 control-label">Item Type</label>
                        <div class="col-sm-6">
                            <input type="text" id="isFreeItemText" class="form-control my-automatic input-sm" readonly="true">
                        </div>
                    </div>
                    [#--收货量与订货量不一致的原因--]
                    <div class="form-group">
                        <label for="differenceReason"  class="col-sm-2 col-lg-4 control-label">Differnece Reason</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos good-select">
                                <input type="text" class="form-control my-automatic input-sm" id="differenceReason" style="overflow:scroll;text-overflow:ellipsis;">
                                <a id="diffreasonRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                <a id="diffreasonRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

			<div class="modal-footer">
				<button id="update_cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
                <button id="update_affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
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
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="_type" value="${type!}"/>
<input type="hidden" id="_storeCd" value="${storeCd!}"/>
<input type="hidden" id="_orderId" value="${orderId!}"/>
<input type="hidden" id="_receiveId" value="${receiveId!}"/>
<input type="hidden" id="viewSts" value="${orderSts!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="orderDiff" value=""/>
</body>
</html>
