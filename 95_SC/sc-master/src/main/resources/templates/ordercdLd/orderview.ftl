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
    <script src="${m.staticpath}/[@spring.message 'js.orderview'/]"></script>
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

    </STYLE>
</head>
<body>
[@common.header][/@common.header]
[@common.nav "HOME&Ordering&DC Store Order Query"][/@common.nav]
<div class="container-fluid" id="main_box">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">
                        Query Condition
                    </div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="row">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="order_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">PO No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="order_id" class="form-control input-sm" value="${data.orderId!}" disabled>
                                    </div>
                                    <label for="order_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="order_date" class="form-control input-sm" value="${data.orderDate!}" disabled>
                                    </div>
                                    <label for="store_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="store_cd" class="form-control input-sm" value="${data.storeCd!}"disabled>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="store_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="store_name" class="form-control input-sm"  value="${data.storeName!}" disabled>
                                    </div>
                                    <label for="vendor_id"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DC No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="vendor_id" class="form-control input-sm" value="${data.vendorId!}"disabled>
                                    </div>
                                    <label for="_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DC Name</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="warehouse_name" class="form-control input-sm"  value="${data. warehouseName!}" disabled>
                                    </div>
[#--                                    <label for="order_qty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Qty</label>--]
[#--                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
[#--                                        <input type="text" id="order_qty" class="form-control input-sm" disabled>--]
[#--                                    </div>--]

                                </div>
                            </div>
                            </div>
[#--                        </div>--]

                    </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <button id="search"  hidden type="button" class="btn btn-primary btn-sm  disabled"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                            <button id='reset'  hidden type='button' class='btn btn-primary btn-sm disabled'><span class='glyphicon glyphicon-refresh icon-right '></span>Reset</button>
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
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div>
</div>
[@common.footer][/@common.footer]
<input   hidden id="use"  value="${use!}"/>
<input  hidden id="orderId" value="${data.orderId!}"/>
<input hidden  id="orderDate" value="${data.orderDate!}"/>
<input  hidden id="storeCd" value="${data.storeCd!}"/>
<input  hidden id="storeName" value="${data.storeName!}"/>
<input hidden  id="vendorId" value="${data.vendorId!}"/>
<input hidden  id="cdOrderView" value="${data.cdOrderView!}"/>
<input hidden id="articleId" value="${data.articleId!}"/>
<input  hidden id="barcode" value="${data.barcode!}"/>
<input  hidden id="productName" value="${data.productName!}"/>
<input  hidden id="orderUnit" value="${data.orderUnit!}"/>
<input  hidden id="minOrderQty" value="${data.minOrderQty!}"/>
<input hidden  id="orderQty" value="${data.orderQty!}"/>
<input hidden  id="orderNochargeQty" value="${data.orderNochargeQty!}"/>
<input  hidden id="receiveQty" value="${data.receiveQty!}"/>
<input  hidden id="receivingDifferences" value="${data.receivingDifferences!}"/>
<input hidden id="deliveryDate" value="${data.deliveryDate!}"/>
<input  hidden  id="searchJson"/>

</body>