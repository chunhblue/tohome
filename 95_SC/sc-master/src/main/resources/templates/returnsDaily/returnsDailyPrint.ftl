[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Customer Refund Daily Report Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.returnsDailyPrint'/]"></script>

    <script>
    </script>
    <STYLE>
        .table-box {
            font-family: verdana,arial,sans-serif;
            text-align: center;
        }
        .table-box .title {
            font-size:20px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .printInfo .storeInfo{
            float: left;
            text-align: left;
            font-size:11px;
            padding-left: 50px;
        }
        .printInfo .dateInfo{
            float: right;
            text-align: right;
            font-size:11px;
            padding-right: 30px;
        }
        #grid_table th {
            font-size: 13px;
            text-align: center;
            background-color: #87CEFF;
        }

        #grid_table th, tr {
            height: 28px;
            line-height: 28px;
        }
    </STYLE>
</head>
<body>
<div id="print">
    <div class="table-box">
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                <span style="font-size: 20px;font-weight: bold">Customer Refund Daily Report</span>
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
                <span style="float: right">Print Date：${printTime!}</span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <table id="grid_table" class="table table-hover table-striped table-condensed table-bordered zgrid-table" style="margin-bottom: 10px">
                    <tr>
                        <th title="Store No.">Store<br> No.</th>
                        <th title="Store Name">Store<br> Name</th>
                        <th title="Item Code">Item<br> Code</th>
                        <th title="Item Name">Item<br> Name</th>
                        [#--                            <th title="Barcode">Barcode</th>--]
                        <th title="Date">Date</th>
                        <th title="POS No.">POS No.</th>
                        <th title="Receipt No.">Receipt<br> No.</th>
                        <th title="Non Sale Type">Non Sale<br> Type</th>
                        <th title="Quantity">Quantity </th>
                        <th title="Amount">Amount</th>
                        <th title="Cashier ID">Cashier<br> ID</th>
                        <th title="Cashier Name">Cashier<br> Name</th>
                        [#--                            <th title="Area Manager ID">Area<br> Manager ID</th>--]
                        <th title="Area Manager Name">Area<br> Manager Name</th>
                        <th title="Mode">Mode</th>
                    </tr>

                </table>
            </div>
        </div>
    </div>
</div>


</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value='${searchJson!}'/>
<input type="hidden" id="userStoreCd" value='${userStoreCd!}'/>
<input type="hidden" id="userStoreName" value='${userStoreName!}'/>
</body>
</html>
