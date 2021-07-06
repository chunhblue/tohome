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
    <script src="${m.staticpath}/[@spring.message 'js.dailySaleReport'/]"></script>
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

        #dailyTable th,#dailyPosTable th {
            font-size: 13px;
            text-align: center;
            background-color: #87CEFF;
        }

        #dailyTable,#dailyPosTable { table-layout: fixed;}
        #dailyPosTable th,#dailyPosTable td{
            overflow:hidden;
            white-space:nowrap;
            text-overflow:ellipsis;
            -o-text-overflow:ellipsis;
            -moz-text-overflow: ellipsis;
            -webkit-text-overflow: ellipsis;
        }
        #dailyTable th, #dailyTable td{
            overflow:hidden;
            white-space:nowrap;
            text-overflow:ellipsis;
            -o-text-overflow:ellipsis;
            -moz-text-overflow: ellipsis;
            -webkit-text-overflow: ellipsis;
        }
        #dailyTable th,#dailyTable tr {
            height: 28px;
            line-height: 28px;
        }
        #dailyPosTable th, #dailyPosTable tr{
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

        .td-align tr td:first-child {
            text-align: left;
        }

        .td-align tr td:last-child {
            text-align: right;
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
[@common.nav "HOME&Report&Store Sales Daily Report"][/@common.nav]
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
                                <div class="form-group" id="show_status">
                                    <label for="type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Type</label>
                                    <div class="col-xs-11 col-sm-10 col-md-10 col-lg-11 col-xs-offset-1  col-sm-offset-0  col-md-offset-0 col-lg-offset-0 ">
                                        <div class="radio-inline">
                                            <label>
                                                <input type="radio" name="order_type" id="order_type_1" nextele="order_type_2" value="1">
                                                POS Business Date
                                            </label>
                                        </div>
                                        <div class="radio-inline">
                                            <label>
                                                <input type="radio" name="order_type" id="order_type_2" value="2"  />
                                                System Date
                                            </label>
                                        </div>
                                    </div>
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input id="effstartDate"  nextele="sell_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input  id="effendDate"  placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Area Manager</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm" autocomplete="off"
                                                   id="am">
                                            <a id="amRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="amRemove" href="javascript:void(0);"
                                               title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Include/Exclude Services</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                    <select id="include_services" class="form-control input-sm">
                                        <option value="20">Exclude Services</option>
                                        <option value="10">Include Services</option>

                                    </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input id="PosstartDate"  nextele="sell_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input  id="PosendDate"  placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <div class="wire"></div>
                                    <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                                    <button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
                                    <button id='export' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-export icon-right'></span>Export</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--box end-->
        </div>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 [#--col-lg-offset-1--] businessDaily_box">
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                <span style="font-size: 25px;font-weight: bold">Store Sales Daily Report </span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left">Created By：<span id="author"></span></span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left">Date Created：${bsDate!}</span></br>
                <span style="float: left">Sales data during POS Internet crush(e.g., power off) period will be synchronize back to NRI store system once internet connection back
                 (POS sales data will save to local during isolated period)
                </span>
            </div>
        </div>
        <div class="row" id="dataTables_scroll" style="overflow: scroll">
            <div id="data_scollHead" class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="positon: relative;width: 170%;overflow: auto">
                <table id="dailyPosTable" class="table table-hover table-striped table-condensed table-bordered zgrid-table"
                       cellspacing="0" cellpadding="5" style="margin-bottom: 10px">
                    <tr>
                        <th title="Store No.">Store No.</th>
                        <th title="Store Name" style="width: 8%">Store Name</th>
                        <th title="Date" style="width: 6%">Date</th>
                        <th title="Customer Count" style="width: 7%">Customer Count</th>
                        <th title="6h-8h">6h-8h</th>
                        <th title="8h-10h">8h-10h</th>
                        <th title="10h-12h">10h-12h</th>
                        <th title="12h-14h">12h-14h</th>
                        <th title="Shift1">Shift1</th>
                        <th title="14h-16h">14h-16h</th>
                        <th title="16h-18h">16h-18h</th>
                        <th title="18h-20h">18h-20h</th>
                        <th title="20h-22h">20h-22h</th>
                        <th title="Shift2">Shift2</th>
                        <th title="22h-24h">22h-24h</th>
                        <th title="0h-2h">0h-2h</th>
                        <th title="2h-4h">2h-4h</th>
                        <th title="4h-6h">4h-6h</th>
                        <th title="Shift3">shift3</th>
                        <th title="Total Amt">Total Amt</th>
                        <th title="Area Manager Name" style="width: 10%">Area Manager Name</th>
                    </tr>
                </table>[#--分页--]
                <table id="dailyTable" class="table table-hover table-striped table-condensed table-bordered zgrid-table"
                       style="margin-bottom: 10px">
                    <tr>
                        <th title="Store No.">Store No.</th>
                        <th title="Store Name" style="width: 8%">Store Name</th>
                        <th title="Date"  style="width: 6%">Date</th>
                        <th title="Customer Count" style="width: 6%">Customer Count</th>
                        <th title="6h-8h">6h-8h</th>
                        <th title="8h-10h">8h-10h</th>
                        <th title="10h-12h">10h-12h</th>
                        <th title="12h-14h">12h-14h</th>
                        <th title="Shift1">Shift1</th>
                        <th title="14h-16h">14h-16h</th>
                        <th title="16h-18h">16h-18h</th>
                        <th title="18h-20h">18h-20h</th>
                        <th title="20h-22h">20h-22h</th>
                        <th title="Shift2">Shift2</th>
                        <th title="22h-24h">22h-24h</th>
                        <th title="0h-2h">0h-2h</th>
                        <th title="2h-4h">2h-4h</th>
                        <th title="4h-6h">4h-6h</th>
                        <th title="Shift3">shift3</th>
                        <th title="Total Amt">Total Amt</th>
                        <th title="Area Manager Name" style="width: 10%">Area Manager Name</th>
[#--                        <th title="Store No.">Store No.</th>--]
[#--                        <th title="Store Name">Store Name</th>--]
[#--                        <th title="Date">Date</th>--]
[#--                        <th title="Customer Count">Customer Count</th>--]
[#--                        <th title="12h-14h">12h-14h</th>--]
[#--                        <th title="14h-16h">14h-16h</th>--]
[#--                        <th title="16h-18h">16h-18h</th>--]
[#--                        <th title="18h-20h">18h-20h</th>--]
[#--                        <th title="Shift1">Shift1</th>--]
[#--                        <th title="20h-22h">20h-22h</th>--]
[#--                        <th title="22h-24h">22h-24h</th>--]
[#--                        <th title="0h-2h">0h-2h</th>--]
[#--                        <th title="2h-4h">2h-4h</th>--]
[#--                        <th title="Shift2">Shift2</th>--]
[#--                        <th title="4h-6h">4h-6h</th>--]
[#--                        <th title="6h-8h">6h-8h</th>--]
[#--                        <th title="8h-10h">8h-10h</th>--]
[#--                        <th title="10h-12h">10h-12h</th>--]
[#--                        <th title="Shift3">shift3</th>--]
[#--                        <th title="Total Amt">Total Amt</th>--]
[#--                        <th title="Area Manager Name">Area Manager Name</th>--]
                    </tr>
                </table>[#--分页--]
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
    <!--row 分割-->
    <!--页脚-->
    [@common.footer][/@common.footer]
    <input type="hidden" id="use" value="${use!}"/>
    <input type="hidden" id="searchJson" value=""/>
    <input type="hidden" id="identity" value="${identity!}"/>
    <input type="hidden" id="enterFlag" value="${enterFlag!}"/>
    <input type="hidden" id="piCdParam" value="${piCdParam!}"/>
    <input type="hidden" id="piDateParam" value="${piDateParam!}"/>
    <input type="hidden" id="typeDate" value=""/>

</div>
</body>
</html>
