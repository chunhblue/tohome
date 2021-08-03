[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title' /]Item Return Request Entry(To Supplier) Print</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0"
          user-scalable=no/>
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.returnDcDocumentPrint'/]"></script>

    <STYLE>
        .table-box {
            -webkit-print-color-adjust: exact;
            font-family: verdana, arial, sans-serif;
            text-align: center;
        }

        .table-box .title {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .printInfo .storeInfo {
            float: left;
            text-align: left;
            font-size: 11px;
            padding-left: 50px;
        }

        .printInfo .dateInfo {
            float: right;
            text-align: right;
            font-size: 11px;
            padding-right: 30px;
        }

        #grid_table th {
            font-size: 14px;
            text-align: center;
            background-color: #87CEFF;
        }

        #grid_table th, tr {
            height: 20px;
            line-height: 20px;
        }

        #storeCdText, #storeCd, #storeNameText, #storeName, #orderId, #orderIdText, #orderDate, #orderDateText {
            font-size: 15px;
            font-weight: bold;
            font-family: "Times New Roman", fantasy;
        }
    </STYLE>
</head>
<body>

<div class="table-box">

    <div class="row" style="margin-top: 20px;">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div style="float: left;margin-right:100px;margin-bottom:10px">
                <img id="nriImage" style="width: 220px;height:80px;text-align: center "
                     src="${m.staticpath}/electronic.png"/></div>
        </div>
    </div>
    <div class="row" style="margin-top: 5px;margin-left:-125px">
        <div class="row" style="margin-top: 10px;">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div style="float: left;margin-left: 100px" id="storeCd">
                        <span style="margin-left:25px;">
                               Store Number:
                        </span>

                    <span id="storeCdText" style="margin-left: 100px">${returnParamDTO.storeCd}</span>
                    <span style="float: right"></span>
                </div>
            </div>
        </div>
        <div class="row" style="margin-top: 10px;">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div style="float: left;margin-left: 115px" id="storeName">
                            <span style="margin-left:10px;">
                                   Store Name:
                            </span>

                    <span id="storeNameText" style="margin-left: 115px">${returnParamDTO.storeName}</span>
                    <span style="float: right"></span>
                </div>
            </div>
        </div>
        <div class="row" style="margin-top: 10px;">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div style="float: left;margin-left: 115px" id="orderId">
                         <span style="margin-left:10px;">
                        No:
                         </span>
                    <span id="orderIdText" style="margin-left: 170px"> ${returnParamDTO.orderId}</span>
                </div>
            </div>
        </div>
        <div class="row" style="margin-top: 10px">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div style="float: left;margin-left: 115px" id="orderDate">
                          <span style="margin-left:10px;">
                                  Date:
                          </span>

                    <span style="margin-left: 158px" id="orderDateText">
                      ${returnParamDTO.orderDate}
                    </span>
                </div>
            </div>
        </div>
        <div class="row" style="margin-top: 10px">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div style="float: left;margin-left: 125px" id="orderDate">
                    <span style="font-weight: normal">(Số thứ tự phiếu trả hàng trong tháng )</span>
                </div>
            </div>
        </div>
        <div class="row" style="margin-top: 20px">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                <span style="font-size: 25px;font-weight: bold;margin-left: 100px">GOODS RETURN NOTE</span>
                <div style="margin-left: 100px">PHIẾU TRẢ HÀNG NHÀ CUNG CẤP
                    <div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            [#--                <span style="float: left" id="userName">User Name:${userName}</span>--]
            <span style="float: left">Print Date：${printTime}</span>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <table id="grid_table"
                   class="table table-hover table-striped table-condensed table-bordered zgrid-table"
                   style="margin-bottom: 10px;">
                <tr>
                    <th>No.</th>
                    <th>Item Code</th>
                    <th>Item Barcode</th>
                    <th>Item Name</th>
                    <th>Unit</th>
                    <th>Qty</th>
                </tr>
            </table>
        </div>
    </div>
    <div class="row">
        <div style="margin-top: 10px">
            <div style="margin-left:15px;float: left;font-weight: bold">
                Remark:
                <div style="border:0px;font-weight: normal;float:right;" id="remarkValue">
                       <span style="margin-left: 5px">
                             ${remarkValue!}
                       </span>

                </div>

            </div>
        </div>

    </div>
    <div class="row">
        <div style="margin-left: 5px">
            <div style="float: left;margin-top: 10px;">
                <div style="font-weight: bold;margin-right:28px">
                    SIGNED BY CIRCLE K
                </div>
                <div style="font-weight: normal;margin-right: -15px"> ( (Ký, đóng dấu và ghi rõ họ tên )</div>
            </div>

            <div style="margin-right: 100px;float: right;font-weight: bold;margin-top: 10px;">
                SIGNED BY SUPPLIER
                <br/>
                <span style="font-weight: normal"> (Ký và ghi rõ họ tên )</span>
            </div>
        </div>
    </div>
    <div class="row" style="border:1px dashed #DCDCDC; height:1px;margin-top: 20px">
    </div>
    <div class="row" style="font-weight: normal;margin-left: auto;margin-right: auto;margin-top: 20px">
        Ghi chú : Store làm 3 liên, có đầy đủ chữ ký của store và nhà cung cấp trên 3 liên
    </div>
    <div class="row" style="font-weight: normal;margin-left: auto;margin-right: auto;margin-top: 20px">
        ( liên 1 giao NCC, liên 2 store giữ, liên 3 gửi văn phòng)
    </div>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="searchJson" value='${searchJson!}'/>
<input type="hidden" id="returnType" value='${returnType!}'/>
<input type="hidden" id="logoPath" value="/scmaster/electronic.png"/>
</body>
</html>