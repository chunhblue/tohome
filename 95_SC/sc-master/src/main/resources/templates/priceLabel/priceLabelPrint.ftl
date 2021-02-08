[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Price Label Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.priceLabelPrint'/]"></script>

    <script>
    </script>
    <STYLE>
        @page {
            size: 210mm 290mm​;  /* auto is the initial value */
            margin: 0mm auto; /* this affects the margin in the printer settings */
        }
    </STYLE>
</head>
<body>
<div id="print">
    <div class="table-box">
        <div class="price_label_box">

        </div>
    </div>
</div>


</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="searchJson" value='${searchJson!}'/>
<input type="hidden" id="flg" value='${flg!}'/>
<input type="hidden" id="params" value='${params!}'/>
</body>
</html>
