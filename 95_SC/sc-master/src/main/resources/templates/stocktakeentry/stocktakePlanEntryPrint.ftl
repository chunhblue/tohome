[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Stocktaking Result Entry and Query Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.stocktakePlanEntryPrint'/]"></script>

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
                <span style="font-size: 20px;font-weight: bold">Stocktaking Result Entry and Query</span>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left">User Name:${userName}</span>
                <span style="float: right">Print Date：${printTime!}</span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <table id="grid_table" class="table table-hover table-striped table-condensed table-bordered zgrid-table" style="margin-bottom: 10px">
                    <tr style="text-align: center">
                        <th>Date</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Document No.</th>
                        <th>Store No.</th>
                        <th>Store Name</th>
                        <th>Status</th>
                        <th>Type</th>
                        <th>Document Status</th>
                        <th>Remarks</th>
                        <th>Created by</th>
                        <th>Date Created</th>
                    </tr>

                </table>
            </div>
        </div>
    </div>
</div>


</div>
<!--审核记录-->
[@common.approvalRecords][/@common.approvalRecords]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="searchJson" value='${searchJson!''}'/>

</body>
</html>
