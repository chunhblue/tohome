[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Cost Item Adjustment List Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.custPlanPrint'/]"></script>

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
        .printInfo .searchInfo{
            float: left;
            text-align: left;
            font-size:11px;
            width: 60%;
        }

        .searchInfo span+span{
            margin-left: 10px;
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
        <div class="title">Cost Item Adjustment List</div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left" id="userName">User Name：${userName!}</span>
                <span style="float: right">Print Date：${printTime!}</span>
            </div>
        </div>
        <table id="grid_table" class="gridTable table table-hover table-striped table-condensed table-bordered zgrid-table">
            <tr style="text-align: center">
                <th>Store No.</th>
                <th>Store Name</th>
                <th>Document No.</th>
                <th>Status</th>
                <th>Remarks</th>
                <th>Created By</th>
                <th>Date Created</th>
            </tr>
        </table>
    </div>
</div>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="searchJson" value='${searchJson!}'/>
</body>
</html>
