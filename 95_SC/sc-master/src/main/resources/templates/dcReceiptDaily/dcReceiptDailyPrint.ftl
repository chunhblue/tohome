[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Store Receiving Daily Report(To DC) Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.dcReceiptDailyPrint'/]"></script>

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
            font-size: 9px;
            text-align: center;
            background-color: #87CEFF;
        }

        #grid_table th, tr {
            height: 20px;
            line-height: 20px;
        }
    </STYLE>
</head>
<body>
<div id="print">
    <div>
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 [#--col-lg-offset-1--] businessDaily_box">
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                    [#--<span style="font-size: 25px;font-weight: bold">门店商品收货日报(Form DC) (含退货)</span>--]
                    <span style="font-size: 25px;font-weight: bold">Store Receiving Daily Report(To DC)</span>
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
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                    <table id="grid_table" class="table table-hover table-striped table-condensed table-bordered zgrid-table"
                           style="margin-bottom: 10px">
                        <tr>
                            <th title="Document No.">Document No.</th>
                            <th title="Store No.">Store No.</th>
                            <th title="Store Name">Store Name</th>
                            <th title="Submission Date">Submission<br>Date</th>
                            <th title="Department">Department</th>
                            <th title="Category">Category</th>
                            <th title="Sub-category">Sub-category</th>
                            <th title="Item Code">Item Code</th>
                            <th title="Item Name">Item Name</th>
                            <th title="Barcode">Barcode</th>
                            <th title="DC">DC</th>
                            <th title="Area Manager ID">Area<br>Manager ID</th>
                            <th title="Operation Manager ID">Operation<br>Manager ID</th>
                            <th title="Receiving Quantity">Receiving<br>Quantity</th>[#--收货数量--]
                            <th title="Created By">Created By</th>
                            <th title="Remark">Remark</th>
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
</body>
</html>
