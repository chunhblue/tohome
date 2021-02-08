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
    <script src="${m.staticpath}/[@spring.message 'js.orderCd'/]"></script>
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
    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--导航-->
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
                            [#--                            <div class="form-horizontal">--]
                            [#--                                <div class="form-group">--]
                            [#--                                    <label for="orderDirectSupplierDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>--]
                            [#--                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
                            [#--                                        <input id="orderDirectSupplierDateStartDate" placeholder="Start Date"  readonly="true" class="form-control input-sm select-date" type="text" value="">--]
                            [#--                                    </div>--]
                            [#--                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
                            [#--                                        <input id="orderDirectSupplierDateEndDate" placeholder="End Date"  readonly="true" class="form-control input-sm select-date" type="text" value="">--]
                            [#--                                    </div>--]
                            [#--                                </div><!-- form-group -->--]
                            [#--                                <div class="form-group">--]
                            [#--                                    <label for="storeNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>--]
                            [#--                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
                            [#--                                        <input id="storeNo" class="form-control" type="text" value="">--]
                            [#--                                    </div>--]
                            [#--                                    <label for="storeCD" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>--]
                            [#--                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
                            [#--                                        <select id="store_Cd" class="form-control input-sm">--]
                            [#--                                            <option value="">-- All/Please Select --</option>--]
                            [#--                                        </select>--]
                            [#--                                    </div>--]
                            [#--                                    <label for="store_Name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>--]
                            [#--                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" id="storeName">--]
                            [#--                                        <input type="text" id="store_Name" class="form-control input-sm">--]
                            [#--                                    </div>--]
                            [#--                                    <div class="form-group" id="show_status">--]
                            [#--                                        <label for="optionTimeType"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Query range</label>--]
                            [#--                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
                            [#--                                            <div class="radio-inline"><label><input type="radio" name="order_type" id="order_Date"value="orderDate" checked="checked">Order Date</label></div>--]
                            [#--                                            <div class="radio-inline"><label><input type="radio" name="order_type" id="delivery_Date" value="deliveryDate"/>Expected Delivery Date</label></div>--]
                            [#--                                        </div>--]
                            [#--                                    </div>--]
                            [#--                                    <label for="vendorId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">CD</label>--]
                            [#--                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" >--]
                            [#--                                        <input type="text" id="vendorId" class="form-control input-sm">--]
                            [#--                                    </div>--]
                            [#--                                </div><!-- form-group -->--]
                            [#--                            </div>--]
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="aRegion" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Region</label>[#-- 大区 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="aRegion">
                                            <a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="aCity" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">City</label>[#-- 城市 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="aCity">
                                            <a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="aDistrict" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">District</label>[#-- 区域 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="aDistrict">
                                            <a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>[#-- 店铺 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="aStore">
                                            <a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="orderDirectSupplierDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Start Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
[#--                                        <input id="orderDirectSupplierDateStartDate" placeholder="Start Date"  readonly="true" class="form-control input-sm select-date" type="text" value="">--]
                                        <input id="orderDirectSupplierDateStartDate" placeholder="Start Date"  class="form-control input-sm select-date" type="text" value="">
                                    </div>
[#--                                    <label for="orderDirectSupplierDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">End Date</label>--]
                                    <label for="orderDirectSupplierDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">End Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
[#--                                        <input id="orderDirectSupplierDateEndDate" placeholder="End Date"  readonly="true" class="form-control input-sm select-date" type="text" value="">--]
                                        <input id="orderDirectSupplierDateEndDate" placeholder="End Date"   class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    [#--                                        查询期间--]
                                    <label for="optionTimeType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Query Range</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                        [#--                                            订单日期--]
                                        <div class="radio-inline">
                                            <label style="white-space: nowrap"><input type="radio" name="order_type" id="order_Date"value="orderDate" checked="checked">Order Date</label>
                                        </div>
                                        [#--                                     预定交货日期--]
                                        <div class="radio-inline">
                                            <label style="white-space: nowrap"><input type="radio" name="order_type" id="delivery_Date" value="deliveryDate"/>Expected Delivery Date</label>
                                        </div>
                                    </div>
                                </div><!-- form-group -->
                                <div class="form-group">
                                    [#--                                    dc编号
                                    <label for="vendorId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DC</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" >
                                        <input type="text" id="dc" class="form-control input-sm">
                                    </div>
                                    --]
                                    [#--订单编号--]
                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">PO No.</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" >
                                        <input type="text" id="orderId" class="form-control input-sm">
                                    </div>
                                    <label for="orderMethod"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Method</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="orderMethod" class="form-control input-sm">
                                            <option value="">-- All/Please Select --</option>
                                        </select>
                                    </div>
                                    <label for="orderStatus"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">PO Status</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="orderStatus" class="form-control input-sm">
                                            <option value="">-- All/Please Select --</option>
                                            <option value="1">Pending</option>
                                            <option value="5">Rejected</option>
                                            <option value="6">Withdrawn</option>
                                            <option value="7">Expired</option>
                                            <option value="10">Approved</option>
                                            <option value="15">Receiving Pending</option>
                                            <option value="20">Received</option>
                                        </select>
                                    </div>
                                </div><!-- form-group -->
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                            <button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
                            <button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print icon-right'></span>Print</button>
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
</div>
<!--审核记录-->
[@common.approvalRecords][/@common.approvalRecords]
<!--页脚-->
[@common.footer][/@common.footer]
[#--	<input type="hidden" id="toKen" value="${toKen!}"/>--]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="typeId" value="${typeId!}"/>
[#--	<input type="hidden" id="store" value="${store!}"/>--]
[#--	<input type="hidden" id="checkResources" value="${checkResources!}"/>--]
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
[@permission code="SC-DIRECR-ORDER-002"]<input type="hidden" class="permission-verify" id="printButPm" value="${displayVar!}" />[/@permission]
[@permission code="SC-DIRECR-ORDER-003"]<input type="hidden" class="permission-verify" id="exportButPm" value="${displayVar!}" />[/@permission]
</body>
</html>
