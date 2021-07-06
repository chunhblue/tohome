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
    <script src="${m.staticpath}/[@spring.message 'js.returnsDaily'/]"></script>
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

        .my-user-info {
            padding: 10px;
            overflow: hidden;
        }

        span.my-label,
        span.my-text {
            float: left;
            color: #333333;
            font-size: 12px;
            margin-left: 10px;
        }

        span.my-text {
            margin-right: 20px;
        }

        span.my-label {
            font-weight: 900;
        }

        .my-div {
            overflow: hidden;
            float: left;
        }

        .del-alert {
            display: none;
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

        #dailyTable td, #dailyTable th {
            font-size: 13px;
            text-align: center;
        }
        #dailyTable th {
            background-color: #87CEFF;
        }
        #dailyTable {
            table-layout: fixed;
        }
        #dailyTable th, #dailyTable td{
            overflow:hidden;
            white-space:nowrap;
            text-overflow:ellipsis;
            -o-text-overflow:ellipsis;
            -moz-text-overflow: ellipsis;
            -webkit-text-overflow: ellipsis;
        }
        #dailyTable th, #dailyTable tr {
            height: 28px;
        }
        label {
            width: auto;
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

        .td-align tr td:first-child {
            text-align: left;
        }

        .td-align tr td:last-child {
            text-align: right;
        }

        .myDiv{
            float:left
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
[@common.nav "HOME&Report&Non Sale Daily Report"][/@common.nav]
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

                                <div class="form-group">
                                    [#--退货时间--]
                                    <label for="" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input id="returnDate" nextele="sell_end_date" placeholder="Sales Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    [#--班次--]
                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Shift</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="shift" class="form-control input-sm" nextele="">
                                            <option value="">-- All/Please Select --</option>
                                            <option value="00">Shift1</option>
                                            <option value="01">Shift2</option>
                                            <option value="02">Shift3</option>
                                        </select>
                                    </div>
                                    [#--barcode--]
                                    [#--<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Barcode</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="barcode" class="form-control input-sm">
                                    </div>--]
                                    [#--商品名称--]
                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code\Item Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="articleName" class="form-control input-sm">
                                    </div>
                                </div>
                                <div class="form-group">
                                    [#--收营员--]
                                    <label for="a_cashier" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Cashier</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="a_cashier" placeholder="" autocomplete="off">
                                            <a id="a_cashier_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="a_cashier_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>

                                    <label for="am" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Area Manager</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="am">
                                            <a id="amRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="amRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    
                                    <label for="totalAmt"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Total Amount</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div  class="myDiv" style="width:40%;">
                                            <select id="calType" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value=">=">>=</option>
                                                <option value="=">=</option>
                                                <option value="<="><=</option>
                                            </select>
                                        </div>
                                        <label class="myDiv" style="width:10%;"></label>
                                        <div  class="myDiv" style="width:50%;">
                                            <input type="text" id="totalAmt" class="form-control input-sm">
                                        </div>
                                    </div>

                                    <label for="totalAmt"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Non Sale Type</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="nonSaleType" class="form-control my-automatic input-sm">
                                            <option value="">-- All/Please Select --</option>
                                            <option value="VOID">VOID</option>
                                            <option value="REMOVE ITEM">REMOVE ITEM</option>
                                            <option value="REFUND ITEM">REFUND ITEM</option>
                                        </select>
                                    </div>
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <div class="wire"></div>
                                    <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                                    <button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
                                    <button id='print' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-print'></span>Print</button>
                                    <button id='export' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span>Export</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--box end-->
        </div>
    </div>
    <div>
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 [#--col-lg-offset-1--] businessDaily_box">
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                    [#--每日顾客退货明细表--]
                    <span style="font-size: 25px;font-weight: bold">Non Sale Daily Report</span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                    <span style="float: left">Created By：${userName!}</span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                    <span style="float: left">Date Created：${bsDate!}</span>
                </div>
            </div>
            <div class="row" style="overflow: scroll">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="width: 140%;">
                    <table id="dailyTable" class="table table-hover table-striped table-condensed table-bordered zgrid-table"
                           style="margin-bottom: 10px">
                        <tr>
                            <th title="Store No.">Store No.</th>
                            <th title="Store Name">Store Name</th>
                            <th title="Item Code">Item Code</th>
                            <th title="Item Name">Item Name</th>
[#--                            <th title="Barcode">Barcode</th>--]
                            <th title="Date" style="width: 8%">Date</th>
                            <th title="POS No.">POS No.</th>
                            <th title="Receipt No.">Receipt No.</th>
                            <th title="Non Sale Type">Non Sale Type</th>
                            <th title="Quantity">Quantity </th>
                            <th title="Amount">Amount</th>
                            <th title="Cashier ID">Cashier ID</th>
                            <th title="Cashier Name">Cashier Name</th>
                            [#--                            <th title="Area Manager ID">Area<br> Manager ID</th>--]
                            <th title="Area Manager Name" style="width: 8%">Area Manager Name</th>
                            <th title="Mode">Mode</th>
                        </tr>
                    </table>

                    [#--分页--]
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
    <!--row 分割-->
    <!--页脚-->
    [@common.footer][/@common.footer]
    <input type="hidden" id="entiretyBmStatus" value=""/>
    <input type="hidden" id="tempTableType" value="-1"/>
    <input type="hidden" id="searchJson" value=""/>
    <input type="hidden" id="userStoreCd" value="${userStoreCd!}"/>
    <input type="hidden" id="userStoreName" value="${userStoreName!}"/>
    <input type="hidden" id="businessDate" value="${businessDate!}"/>
    <input type="hidden" id="checkFlg" value='${checkFlg!}'/>
</div>
</body>
</html>
