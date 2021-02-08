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
    <script src="${m.staticpath}/[@spring.message 'js.fsInventoryDataEntry'/]"></script>
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
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none;
        }
        input[type="number"]{
            -moz-appearance: textfield;
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
    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--导航-->
[@common.nav "HOME&Operation&FS Stock Entry"][/@common.nav]
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
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="piCd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" readonly="readonly" id="piCd" placeholder="Document No." class="form-control input-sm">
                                    </div>

                                    <label for="create_ymd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date Created</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="create_ymd" readonly="readonly" placeholder="Date Created" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos good-select">
                                            <input type="text" id="store" class="form-control input-sm" style="overflow:scroll;text-overflow:ellipsis;">
                                            <a id="refresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="clear" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="createUser"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created by</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" readonly="readonly" id="create_user" placeholder="Created by" class="form-control input-sm">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-8">
                                        <input style="width: 61.5%" type="text" id="remarks" class="form-control input-sm">
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                <input type="text" id="searchItemInp" placeholder="Item Code" class="form-control input-sm">
                            </div>
                            <button id="searchItemBtn" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
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
            <button id="saveBut" type="button" class="btn  btn-primary saveBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div><!--row 分割-->
</div>
<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Details</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="barcode" class="col-sm-2 col-lg-5 control-label">Barcode</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" id="barcode" readonly="readonly" class="form-control my-automatic input-sm">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="articleId" class="col-sm-2 col-lg-5 control-label">Item Code</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" readonly="readonly"  id="articleId" class="form-control my-automatic input-sm">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="a_article" class="col-sm-2 col-lg-5 control-label not-null">Item Name</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" class="form-control my-automatic input-sm" id="a_article" placeholder="Please enter 3 digit" autocomplete="off" nextele="">
                                <a id="article_refresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                <a id="article_clear" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="uom" class="col-sm-2 col-lg-5 control-label">UOM</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" readonly="readonly" id="uom"  class="form-control my-automatic input-sm">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="qty" class="col-sm-2 col-lg-5 control-label not-null">FS Qty</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" id="qty" class="form-control my-automatic input-sm">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">[#--实时库存--]
                        <label for="inventoryQty" class="col-sm-2 col-lg-5 control-label">Inventory Qty</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" id="inventoryQty" class="form-control my-automatic input-sm" readonly="true">
                            </div>
                        </div>
                    </div>
                    <div class="form-group" hidden>
                        <label for="lastUpdateTime" class="col-sm-2 col-lg-5 control-label">Last Update Time</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" id="lastUpdateTime" class="form-control my-automatic input-sm">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">[#--note--]
                        <label for="note" class="col-sm-2 col-lg-5 control-label">Notes</label>
                        <div class="col-sm-6">
                            <div class="aotu-pos">
                                <input type="text" id="note" class="form-control my-automatic input-sm">
                            </div>
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
<!--页脚-->
[#--货号定位高亮参照 ：https://blog.csdn.net/mhshencaobo/article/details/84588847 --]
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="piCdParam" value="${piCdParam!}"/>
<input type="hidden" id="enterFlag" value="${enterFlag!}"/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="i_storeCd" value="">
</body>
</html>
