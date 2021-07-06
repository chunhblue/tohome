[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Store Inventory Daily Report Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.storeInventoryDailyPrint'/]"></script>

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
            font-size: 10px;
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
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <table id="grid_table" class="table table-hover table-striped table-condensed table-bordered">
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
            </div>
        </div>
    </div>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="searchJson" value='${searchJson!}'/>
<input type="hidden" id="page" value="${page!}"/>
<input type="hidden" id="rows" value="${rows!}"/>
</body>
</html>
