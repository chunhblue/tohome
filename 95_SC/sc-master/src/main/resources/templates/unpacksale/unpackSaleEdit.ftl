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
    <script src="${m.staticpath}/[@spring.message 'js.unpackSaleEdit'/]"></script>
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

[#--    <div class="error-pcode" id="error_pcode">--]
[#--        <div class="error-pcode-text"> ${useMsg!} </div>--]
[#--        <div class="shade"></div>--]
[#--    </div>--]
<!--导航-->
[@common.nav "HOME&Sale&Unpacking Sales Info"][/@common.nav]
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
                                <div class="form-group ">
                                    <label for="storeName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" id="storeName" class="form-control my-automatic input-sm" autocomplete="off">
                                            <a id="refreshIcon" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="clearIcon" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="unpackId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Unpacking No.</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="unpackId" readonly="true" class="form-control input-sm" value="${dto.unpackId!}">
                                    </div>
                                    <label for="unpackDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space: nowrap">Unpacking Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-4 col-lg-2">
                                        <input id="unpackDate" readonly="true" placeholder="Unpacking Date" class="form-control input-sm" type="text" value="">
                                    </div>
                                </div><!-- form-group -->

                                <div class="form-group ">
                                    <label for="articleId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Item Code</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos good-select">
											<input type="text" class="form-control my-automatic input-sm" placeholder="Please enter 3 digit" id="articleId">
											<a id="itemRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="itemRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
                                    </div>
                                    <label for="barcode" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Barcode</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="barcode" readonly="true" class="form-control input-sm">
                                    </div>
                                    <label for="unpackAmt" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Unpacking Amount</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="unpackAmt" readonly="true" class="form-control input-sm">
                                    </div>
                                </div><!-- form-group -->

                                <div class="form-group ">
                                    <label for="unpackQty" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space: nowrap">Unpacking Qty</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="unpackQty" class="form-control input-sm">
                                    </div>
                                    <label for="remarks" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-5">
                                        <input type="text" id="remarks" class="form-control input-sm">
                                    </div>
                                </div><!-- form-group -->

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
            <button id='resetBtn' type='button' style='display:none;' class='btn btn-default'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
            <button id='saveBtn' type='button' style='display:none;' class='btn btn-default'><span class='glyphicon glyphicon-ok icon-right'></span>Save</button>
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div><!--row 分割-->
</div>

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="viewSts" value="${viewSts!}"/>
<input type="hidden" id="_storeCd" value="${dto.storeCd!}"/>
<input type="hidden" id="_articleId" value="${dto.parentArticleId!}"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="isHide" value="${isHide!}" />
</body>
</html>