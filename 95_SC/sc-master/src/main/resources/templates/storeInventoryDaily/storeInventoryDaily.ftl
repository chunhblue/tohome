[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0"
          user-scalable=no/>
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.storeInventoryDaily'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
    </script>
    <STYLE>
        .good-select .form-control {
            padding-right: 35px;
        }

        label.radio {
            margin-left: 20px;
        }

        .businessDaily_box {
            border-color: #ddd;
            margin-bottom: 20px;
            background-color: #fff;
            -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 12px;
        }

        #daily_table td, #daily_table th {
            font-size: 13px;
            text-align: center;
        }
        #daily_table th {
            background-color: #87CEFF;
        }
        #daily_table { table-layout: fixed;}
        #daily_table th, #daily_table td{
            overflow:hidden;
            white-space:nowrap;
            text-overflow:ellipsis;
            -o-text-overflow:ellipsis;
            -moz-text-overflow: ellipsis;
            -webkit-text-overflow: ellipsis;
        }
        #daily_table th, #daily_table tr {
            height: 28px;
            line-height: 28px;
        }

        @media (min-width: 1200px) {
            .col-lg-1-5 {
                width: 20%;
                float: left;
            }

            .col-lg-offset-1-5 {
                margin-left: 20%;
            }

            .col-lg-5-1 {
                width: 20%;
                float: left;
            }

            .col-lg-offset-5-1 {
                margin-left: 40%;
            }

            .col-lg-1-6 {
                width: 20%;
                float: left;
            }

            .col-lg-offset-1-6 {
                margin-left: 6.66%;
            }
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
[@common.nav "HOME&Report&Store Inventory Daily Report"][/@common.nav]
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
                                    <label for="dep" class="col-sm-1 control-label" style="white-space: nowrap">Top Department</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="dep">
                                            <a id="depRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="depRemove" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="pma" class="col-sm-1 control-label">Department</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="pma">
                                            <a id="pmaRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="pmaRemove" href="javascript:void(0);"
                                               title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="category" class="col-sm-1 control-label">Category</label>[#--中分类--]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="category">[#-- placeholder="全中分类"--]
                                            <a id="categoryRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="categoryRemove" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="subCategory" class="col-sm-1 control-label">Sub Category</label>[#--小分类--]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="subCategory">[#-- placeholder="全小分类"--]
                                            <a id="subCategoryRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="subCategoryRemove" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group ">
                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Query Type</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-1">
                                        <label class="radio">
                                            <input name="queryType" type="radio" value="0" checked="checked"/>
                                            Item</label>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-2 col-lg-1">
                                        <label class="radio">
                                            <input name="queryType" type="radio" value="1"/>
                                            Category</label>
                                    </div>

                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Area Manager</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="am">
                                            <a id="amRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="amRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>

                                    <label for="itemId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Barcode</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="itemId" class="form-control input-sm">
                                    </div>

                                    <label for="itemName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code/Item
                                        Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="itemName" class="form-control input-sm">
                                    </div>

                                </div><!-- form-group -->

                                <div class="form-group">
                                    <label for="writeOffStartDate"
                                           class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Inventory Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="writeOffStartDate" placeholder="Date"
                                               class="form-control input-sm select-date" type="text" value="">
                                    </div>

                                    <div hidden>
                                        <label for="writeOffEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <input id="writeOffEndDate" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                        </div>
                                    </div>

                                    <label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="aStore" autocomplete="off">
                                            <a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div><!-- form-group -->

                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <button id='search' type="button" class="btn btn-primary btn-sm"><span
                                        class="glyphicon glyphicon-search icon-right"></span>Inquire
                            </button>
                            <button id='reset' type='button' class='btn btn-primary btn-sm'><span
                                        class='glyphicon glyphicon-refresh icon-right'></span>Reset
                            </button>
                            <button id='print' type='button' class='btn  btn-info   btn-sm '><span
                                        class='glyphicon glyphicon-print'></span>Print
                            </button>
                            <button id='export' type='button' class='btn  btn-info   btn-sm '><span
                                        class='glyphicon glyphicon-export'></span>Export
                            </button>
                        </div>
                    </div>
                </div>
            </div><!--box end-->
        </div>
    </div>
    <!--row 分割-->
    <div>
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 [#--col-lg-offset-1--] businessDaily_box">
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                    <span id="title" style="font-size: 25px;font-weight: bold">Store Inventory Daily Report</span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                    <span style="float: left;margin-left:20px;">Created By：${author}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                    <span style="float: left;margin-left:20px;">Date Created：${bsDate!}</span>
                </div>
            </div>
            <div class="row" style="overflow: scroll">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="width: 130%;">
                    <table id="daily_table" class="table table-hover table-striped table-condensed table-bordered">
                        <tr>
                            <th title="Store No.">Store No.</th>
                            <th title="Store Name">Store Name</th>
                            <th title="Inventory Date">Inventory Date</th>
                            <th title="Top Department">Top Department</th>
                            <th title="Department">Department</th>
                            <th title="Category">Category</th>
                            <th title="Sub-Category">Sub-Category</th>
                            <th title="Barcode">Barcode</th>
                            <th title="Item Code">Item Code</th>
                            <th title="Item Name">Item Name</th>
                            <th title="UOM">UOM</th>
                            <th title="Area Manager Name">Area Manager Name</th>
                            <th title="Quantity">Quantity</th>
                        </tr>
                    </table>
                    <nav aria-label="Page navigation" style="text-align: right">
                        <ul class="pagination">
                            <li class="disabled"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
                            <li class="active"><a href="javascript:void(0)">1</a></li>
                            <li class="disabled"><a href="javascript:void(0)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="businessDate" value="${businessDate!}"/>
<input type="hidden" id="searchJson" value=""/>
</body>
</html>
