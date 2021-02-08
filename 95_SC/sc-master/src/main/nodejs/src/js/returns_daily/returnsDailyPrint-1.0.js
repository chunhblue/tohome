require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('storeInventoryDailyPrint', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null;
    approvalRecordsParamGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        dataForm=[];
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
        url_left=_common.config.surl+"/returnsDaily/print";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

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

                    let totalAmt = 0; // 总金额
                    let cash = 0; // 现金
                    let cardPayment = 0; // 信用卡
                    let eVoucher = 0; // E-Voucher 支付
                    let momo = 0; // Momo
                    let payoo = 0; // Payoo
                    let viettel = 0; // Viettel
                    if (dataList.length < 1) {
                        _common.prompt("No data found!", 5, "info");/*查询数据为空*/
                        return;
                    }
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        totalAmt += parseFloat(item.totalAmt);
                        cash += parseFloat(item.cash);
                        cardPayment += parseFloat(item.cardPayment);
                        eVoucher += parseFloat(item.eVoucher);
                        momo += parseFloat(item.momo);
                        payoo += parseFloat(item.payoo);
                        viettel += parseFloat(item.viettel);
                        let tempTrHtml = '<tr>' +
                            "<td title='"+isEmpty(item.storeCd)+"' style='text-align:left;'>" + isEmpty(item.storeCd) + "</td>" +
                            "<td title='"+isEmpty(item.storeName)+"' style='text-align:left;'>" + isEmpty(item.storeName) + "</td>" +
                            "<td title='"+isEmpty(item.articleId)+"' style='text-align:right;'>" + isEmpty(item.articleId) + "</td>" +
                            "<td title='"+isEmpty(item.articleName) +"' style='text-align:left;'>" + isEmpty(item.articleName)  + "</td>" +
                            "<td title='"+isEmpty(item.barcode) +"' style='text-align:right;'>" + isEmpty(item.barcode)  + "</td>" +
                            "<td title='"+formatDate(isEmpty(item.saleDate)) +"' style='text-align:center;'>" + formatDate(isEmpty(item.saleDate))  + "</td>" +
                            "<td title='"+isEmpty(item.posId) +"' style='text-align:right;'>" + isEmpty(item.posId) + "</td>" +
                            "<td title='"+isEmpty(item.saleSerialNo) +"' style='text-align:right;'>" + isEmpty(item.saleSerialNo) + "</td>" +
                            "<td title='"+toThousands(item.orderQty)  +"' style='text-align:right;'>" + toThousands(item.orderQty)  + "</td>" +
                            "<td title='"+toThousands(item.priceActual)  +"' style='text-align:right;'>" + toThousands(item.priceActual)  + "</td>" +
                            "<td title='"+toThousands(item.totalAmt)  +"' style='text-align:right;'>" + toThousands(item.totalAmt)  + "</td>" +
                            "<td title='"+toThousands(item.cash)  +"' style='text-align:right;'>" + toThousands(item.cash)  + "</td>" +
                            "<td title='"+toThousands(item.cardPayment)  +"' style='text-align:right;'>" + toThousands(item.cardPayment)  + "</td>" +
                            "<td title='"+ toThousands(item.eVoucher)  +"' style='text-align:right;'>" +  toThousands(item.eVoucher)  + "</td>" +
                            "<td title='"+ toThousands(item.momo)  +"' style='text-align:right;'>" +  toThousands(item.momo)  + "</td>" +
                            "<td title='"+ toThousands(item.payoo) +"' style='text-align:right;'>" +  toThousands(item.payoo)  + "</td>" +
                            "<td title='"+ toThousands(item.viettel) +"' style='text-align:right;'>" +  toThousands(item.viettel)  + "</td>" +
                            "<td title='"+  isEmpty(item.cashierId) +"' style='text-align:right;'>" +   isEmpty(item.cashierId)  + "</td>" +

                            "<td title='"+ isEmpty(item.cashierName) +"' style='text-align:left;'>" +  isEmpty(item.cashierName)  + "</td>" +
                            // "<td title='"+ isEmpty(item.amCd) +"' style='text-align:left;'>" +  isEmpty(item.amCd)  + "</td>" +
                            "<td title='"+ isEmpty(item.amName) +"' style='text-align:left;'>" +  isEmpty(item.amName)  + "</td>" +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                    }
                    // 合计行
                    let totalTempTrHtml = '<tr style="background-color: #87CEFF">' +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td</td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td title='Total:'>Total:</td>" +
                        "<td title='" + toThousands(totalAmt)  +"' style='text-align:right;'>" + toThousands(totalAmt) + "</td>" +
                        "<td title='" + toThousands(cash) + "' style='text-align:right;'>" + toThousands(cash) + "</td>" +
                        "<td title='" + toThousands(cardPayment) + "' style='text-align:right;'>" + toThousands(cardPayment) + "</td>" +
                        "<td title='" + toThousands(eVoucher) + "' style='text-align:right;'>" + toThousands(eVoucher) + "</td>" +
                        "<td title='" + toThousands(momo) + "' style='text-align:right;'>" + toThousands(momo) + "</td>" +
                        "<td title='" + toThousands(payoo) + "' style='text-align:right;'>" + toThousands(payoo) + "</td>" +
                        "<td title='" + toThousands(viettel) + "' style='text-align:right;'>" + toThousands(viettel) + "</td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        // '<td></td>' +
                        '</tr>';
                    m.grid_table.append(totalTempTrHtml);
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
    }

    // 格式化日期
    var formatDate = function (dateStr) {
        if (!dateStr) {
            return '';
        }
        return new Date(dateStr).Format('dd/MM/yyyy')
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
var _index = require('storeInventoryDailyPrint');
_start.load(function (_common) {
    _index.init(_common);
});
