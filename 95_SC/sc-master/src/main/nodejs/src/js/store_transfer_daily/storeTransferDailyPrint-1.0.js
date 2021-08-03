require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('storeTransferDailyPrint', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        dataForm=[],
        systemPath='';
    var m = {
        use : null,
        tableGrid : null,
        selectTrObj : null,
        identity : null,
        grid_table : null,
        searchJson:null,
        storeName: null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/storeTransferDaily/print";
        total_detail(m.searchJson.val());
        getList();
    }




    //画面按钮点击事件
    var getList = function(){
        $.myAjaxs({
            url:url_left+"/getprintData",
            async:true,
            cache:false,
            type :"post",
            data :'jsonStr='+m.searchJson.val(),
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                if(result.success){
                    let dataList = result.o;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        // $("#storeName").text("Store Name:"+item.storeName);
                        let tempTrHtml = '<tr>' +
                            // '<td title="' + isEmpty(item.storeCd) + '" style="text-align:left;">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align:left;">' + isEmpty(item.storeName) + '</td>' +
                            // '<td title="' + isEmpty(item.storeCd1) + '" style="text-align:left;">' + isEmpty(item.storeCd1) + '</td>' +
                            '<td title="' + isEmpty(item.storeName1) + '" style="text-align:left;">' + isEmpty(item.storeName1) + '</td>' +
                            '<td title="' + isEmpty(item.voucherDate) + '" style="text-align:center;">' + isEmpty(item.voucherDate) + '</td>' +
                            '<td title="' + isEmpty(item.depName) + '" style="text-align:left;">' + isEmpty(item.depName) + '</td>' +
                            '<td title="' + isEmpty(item.pmaName) + '" style="text-align:left;">' + isEmpty(item.pmaName) + '</td>' +
                            '<td title="' + isEmpty(item.categoryName) + '" style="text-align:left;">' + isEmpty(item.categoryName) + '</td>' +
                            '<td title="' + isEmpty(item.subCategoryName) + '" style="text-align:left;">' + isEmpty(item.subCategoryName) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align:right;">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align:left;">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align:right;">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.uom) + '" style="text-align:left;">' + isEmpty(item.uom) + '</td>' +
                            '<td title="' + toThousands(item.qty2) + '" style="text-align:right;">' + toThousands(item.qty2) + '</td>' +
                            // '<td title="' + toThousands(item.amtNoTax) + '" style="text-align:right;">' + toThousands(item.amtNoTax) + '</td>' +
                            '<td title="' + isEmpty(item.adjustReasonText) + '" style="text-align:left;">' + isEmpty(item.adjustReasonText) + '</td>' +
                            '<td title="' + isEmpty(item.amName) + '" style="text-align:left;">' + isEmpty(item.amName) + '</td>' +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                    }
                    // 合计行
                    var totalOutQty = $("#toTotalOutQty").val();
                    var totalInQty = $("#toTotalInQty").val();
                    var totalDoc = $("#totalDoc").val();
                    var totalTr = "<tr>" +
                        "<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +
                        "<td>Total Document:</td>" +
                        "<td style='text-align:right;'>" + toThousands(totalDoc) + "</td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td>Total Qty</td>" +
                        "<td style='text-align:right;'>" + toThousands(totalOutQty) + "</td>" +
                        "<td></td>" +"<td></td>" +
                        "</tr>";
                    m.grid_table.append(totalTr);
                }else {
                    _common.prompt("Print data exception！",5,"info");/*打印数据异常*/
                }
                // 打印
                window.print();
            },
            error : function(e){
                _common.prompt("Failed to load data!",5,"error");
            }
        });
    };

    // 获取库存调拨总数量
    var total_detail = function (data) {
        $.myAjaxs({
            url: _common.config.surl+"/storeTransferDaily/getTranferQty",
            async: false,
            cache: false,
            type: "post",
            data: 'jsonStr=' + data,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    $("#toTotalOutQty").val(result.o.qty2);
                    $("#toTotalInQty").val(result.o.qty1);
                    $("#totalDoc").val(result.o.records);
                }
            },
            error:function () {
                $("#toTotalOutQty").val('');
                $("#toTotalInQty").val('');
                $("#totalDoc").val('');
            }
        })
    };

    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
        }
    }

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num,dit) {
        if (!num) {
            return '';
        }
        dit = typeof dit !== 'undefined' ? dit : 0;
        num = num + ''
        const reg = /\d{1,3}(?=(\d{3})+$)/g
        let intNum = ''
        let decimalNum = ''
        if (num.indexOf('.') > -1) {
            if (num.indexOf(',') != -1) {
                num = num.replace(/\,/g, '');
            }
            num = parseFloat(num).toFixed(dit);
            return (num + '').replace(reg, '$&,');
        } else {
            return (num + '').replace(reg, '$&,');
        }
    }

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeTransferDailyPrint');
_start.load(function (_common) {
    _index.init(_common);
});
