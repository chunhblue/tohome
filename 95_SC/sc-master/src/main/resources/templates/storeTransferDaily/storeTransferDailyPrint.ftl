[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Store Transfer Daily Report Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.storeTransferDailyPrint'/]"></script>

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
                <span style="font-size: 20px;font-weight: bold">Store Allocation Daily Report</span>
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
                        <th title="From Store">From Store</th>
                        [#--                            <th title="Store Name">Store Name</th>--]
                        <th title="To Store">To Store</th>
                        [#--                            <th title="Store Name">Store Name</th>--]
                        <th title="Tranfer Out Date">Tranfer Out Date</th>
                        <th title="Top Department">Top Department</th>
                        <th title="Department">Department</th>
                        <th title="Category">Category</th>
                        <th title="Sub Category">Sub Category</th>
                        <th title="Item Code">Item Code</th>
                        <th title="Item Name">Item Name</th>
                        <th title="Barcode">Barcode</th>
                        <th title="UOM">UOM</th>
                        <th title="Transfer Out Qty">Transfer Out Qty</th>[#--调出数量--]
                        <th title="Transfer In Qty">Transfer In Qty</th>[#--调入数量--]
                        <th title="Tranfer In Date">Tranfer In Date</th>
                        <th title="Different Qty Reason">Different Qty Reason</th>[#--调拨数量不一致的原因--]
                        [#--                            <th title="Transfer Amount">Transfer Amount</th>--][#--调拨金额--]
                        <th title="Transfer Reason">Transfer Reason</th>[#--调拨原因--]
                        <th title="Area Manager Name">Area Manager Name</th>
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
<input type="hidden" id="toTotalOutQty" value=""/>
<input type="hidden" id="toTotalInQty" value=""/>
<input type="hidden" id="totalDoc" value=""/>
</body>
</html>
