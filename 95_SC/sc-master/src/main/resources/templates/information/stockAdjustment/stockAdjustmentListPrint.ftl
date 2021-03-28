[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Store Stock Adjustment Daily Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.adjustmentDailyPrint'/]"></script>

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
    <div class="table-box">
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                <span style="font-size: 20px;font-weight: bold">Store Stock Adjustment Daily</span>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left;margin-left:20px;">Created Staff：${author}</span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left;margin-left:20px;">Date Prepared：${bsDate?string('dd/MM/yyyy')}</span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <table id="grid_table" class="table table-hover table-striped table-condensed table-bordered" style="margin-bottom: 10px">
                    <tr>
                        <th title="Store No.">Store No.</th>
                        <th title="Store Name">Store Name</th>
                        <th title="Inventory Date">Inventory Date</th>
                        <th title="Top Department">Top Department</th>
                        <th title="Department">Department</th>
                        <th title="Category">Category </th>
                        <th title="Sub-Category">Sub-Category</th>
                        <th title="Item Code">Item Code</th>
                        <th title="Item Name">Item Name</th>
                        <th title="Barcode">Barcode</th>
                        <th title="UOM">UOM</th>
                        <th title="Write Off Quantity">Write Off Quantity</th>
                        <th title="Adjustment Reason">Adjustment Reason</th>
                        <th title="Are Manager Code">Are Manager Code</th>
                        <th title="Are Manager Name">Are Manager Name</th>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>


</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="searchJson" value='${searchJson!}'/>
<input type="hidden" id="itemTolQty" value=""/>
<input type="hidden" id="totalItem" value=""/>
</body>
</html>