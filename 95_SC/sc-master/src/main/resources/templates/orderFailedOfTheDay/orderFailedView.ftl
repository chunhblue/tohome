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
    <script src="${m.staticpath}/[@spring.message 'js.dayOrderFailureView'/]"></script>
    <script>
    </script>
    <STYLE>
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
[@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Ordering&Failed Order List of the day"][/@common.nav]
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
                                    <label for="orderDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="orderDate" readonly="true" class="form-control input-sm" value="${dto.orderDate!}">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order By</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="orderBy" readonly="true" class="form-control input-sm" value="${dto.createUserId!}">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Type</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="methodName" readonly="true" class="form-control input-sm"  value="${dto.methodName!}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="storeCd" readonly="true" class="form-control input-sm" value="${dto.storeCd!}">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="storeName" readonly="true" class="form-control input-sm" value="${dto.storeName!''}">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Code</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="vendorId" readonly="true" class="form-control input-sm" value="${dto.vendorId!''}">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Name</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="vendorName" readonly="true" class="form-control input-sm" value="${dto.vendorName!}">
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
            <table id="zgGridTtable"></table>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div><!--row 分割-->
</div>
[#--模态框--]
<div id="update_dialog" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-lg" role="document" >
        <div class="modal-content" style="width: 950px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Failed Order Detail</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="form-horizontal" >
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-group">
                                <label for="item_input_barcode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Barcode</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" class="form-control input-sm" id="item_input_barcode" readonly="readonly" autocomplete="off" >
                                </div>

                                <label for="item_input_articleId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Code</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_articleId" readonly="readonly" class="form-control input-sm">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="item_input_articleName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Name</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_articleName" readonly="readonly" class="form-control input-sm">
                                </div>

                                <label for="item_input_uom"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">UOM</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_uom" readonly="readonly" class="form-control input-sm">
                                    <input type="hidden" id="item_input_uomCd">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="item_input_spec"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Specification</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_spec" readonly="readonly" class="form-control input-sm">
                                </div>
                                <label for="item_input_psd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">PSD</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_psd" readonly="readonly" class="form-control input-sm">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="item_input_minDisplayQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Display on Shelf</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_minDisplayQty" readonly="readonly" class="form-control input-sm">
                                </div>
                                <label for="item_input_minOrderQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Min. Order Quantity</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_minOrderQty" readonly="readonly" class="form-control input-sm">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="item_input_orderQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Order Quantity</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_orderQty" readonly="readonly" class="form-control input-sm">
                                </div>

                                <label for="item_input_orderNochargeQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Free Order Quantity</label>
                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
                                    <input type="text" id="item_input_orderNochargeQty" readonly="readonly" class="form-control input-sm">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="cancel" type="button" class="btn btn-default btn-sm" > <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
                <button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
            </div>
        </div>
    </div>
</div>

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="orderId" value="${dto.orderId!}"/>
</body>
</html>
