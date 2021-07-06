[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title' /]Purchasing Order And Return Query Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.rrVoucherQueryPrint'/]"></script>
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
            height: 20px;
            line-height: 20px;
        }
    </STYLE>
</head>
<body>
<div id="print">
    <div class="table-box">
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                <span style="font-size: 25px;font-weight: bold">Purchasing Order And Return Query Print</span>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span style="float: left" id="userName">User Name:${userName}</span>
                <span style="float: right">Print Date：${printTime!}</span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <table id="grid_table" class="table table-hover table-striped table-condensed table-bordered zgrid-table" style="margin-bottom: 10px">
                    <tr>
                        <th>Document Date</th>
                        <th>Document No.</th>
                        <th>Original Document No.</th>
                        <th>Original Document Date</th>
                        <th>Document Type</th>
                        <th>Store Code</th>
                        <th>Store Name</th>
                        [#--<th>Area Manager Code</th>--]
                        <th>Area Manager Name</th>
                        [#--<th>Operation Manager Code</th>--]
                        <th>Operation Manager Name</th>
                        [#--<th>Operation Controller Code</th>--]
                        <th>Operation Controller Name</th>

                        <th>Vendor Code</th>
                        <th>Vendor Name</th>
                        <th>Order Method</th>
                        <th>Vat In(%)</th>
                        <th>Document Status</th>
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
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="editPer" value="" />
<input type="hidden" id="searchJson" value='${searchJson!}'>
</body>
</html>
