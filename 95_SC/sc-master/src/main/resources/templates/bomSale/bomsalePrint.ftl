[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>BOM Sales Report Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

	<link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
	<script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.bomSalePrint'/]"></script>
</head>
<style>
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
    table.gridTable {
        color:#333333;
        margin: auto;
        border: none;
        border-collapse: collapse;
        -moz-border-radius: 5px;
        -webkit-border-radius: 5px;
        -khtml-border-radius: 5px;
    }
    table.gridTable th {
        padding: 8px;
        font-size:13px;
        border-top: 1px solid #666666;
        border-bottom: 1px solid #666666;
    }
    table.gridTable td {
        border: none;
        padding: 8px;
        font-size:12px;
        background-color: #ffffff;
    }
    table.gridTable tr:last-child td {
        border-top: 1px dashed #666666;
    }
</style>
<body>
<div class="table-box">
	<div class="title">BOM Sales Report</div>
	<div class="printInfo">
	    <div class="searchInfo">
[#--			<span>Store：${dto.regionName!}</span>--]
[#--			<span>City：${dto.cityName!}</span>--]
[#--			<span>District：${dto.districtName!}</span>--]
			<span>Store：${dto.storeName!}</span>
			<br/>
			<span>Sales Date：
				[#if dto.saleStartDate?? && dto.saleStartDate!='']
					${dto.saleStartDate?substring(6,8)}/${dto.saleStartDate?substring(4,6)}/${dto.saleStartDate?substring(0,4)}
				[/#if]
				-
				[#if dto.saleEndDate?? && dto.saleEndDate!='']
					${dto.saleEndDate?substring(6,8)}/${dto.saleEndDate?substring(4,6)}/${dto.saleEndDate?substring(0,4)}
				[/#if]
			</span>
			<span>Item Code：${dto.articleId!}</span>
		</div>
	    <div class="dateInfo"><span>Print Date：</span><span id="_printDate"></span></div>
	</div>
	<table border=0 cellSpacing=0 cellPadding=0 id="grid_table_p" class="gridTable"></table>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="regionCd" value="${dto.regionCd!}"/>
<input type="hidden" id="cityCd" value="${dto.cityCd!}"/>
<input type="hidden" id="districtCd" value="${dto.districtCd!}"/>
<input type="hidden" id="storeCd" value="${dto.storeCd!}"/>
<input type="hidden" id="saleStartDate" value="${dto.saleStartDate!}"/>
<input type="hidden" id="saleEndDate" value="${dto.saleEndDate!}"/>
<input type="hidden" id="itemId" value="${dto.articleId!}"/>
</body>
</html>
