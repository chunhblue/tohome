require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('dcReceiptDailyPrint', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null;
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
        searchJson:null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/dcReceiptDaily/print";
        systemPath=_common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        getList();
    }

    //画面按钮点击事件
    var getList = function(){
''
        $.myAjaxs({
            url:url_left+"/getprintData",
            async:true,
            cache:false,
            type :"post",
            data :'searchJson='+m.searchJson.val(),
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                if(result.success){
                    let dataList = result.o.data;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr>' +
                            '<td title="' + isEmpty(item.receiveId) + '" style="text-align:right;">' + isEmpty(item.receiveId) + '</td>' +
                            '<td title="' + isEmpty(item.storeCd) + '" style="text-align:left;">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align:left;">' + isEmpty(item.storeName) + '</td>' +
                            '<td title="' + isEmpty(item.receiveDate) + '" style="text-align:center;">' + isEmpty(item.receiveDate) + '</td>' +
                            '<td title="' + isEmpty(item.pmaName) + '" style="text-align:left;">' + isEmpty(item.pmaName) + '</td>' +
                            '<td title="' + isEmpty(item.categoryName) + '" style="text-align:left;">' + isEmpty(item.categoryName) + '</td>' +
                            '<td title="' + isEmpty(item.subCategoryName) + '" style="text-align:left;">' + isEmpty(item.subCategoryName) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align:right;">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align:left;">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align:right;">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.dc) + '" style="text-align:left;">' + isEmpty(item.dc) + '</td>' +
                            '<td title="' + isEmpty(item.amName) + '" style="text-align:left;">' + isEmpty(item.amName) + '</td>' +
                            '<td title="' + isEmpty(item.omName) + '" style="text-align:left;">' + isEmpty(item.omName) + '</td>' +
                            '<td title="' + toThousands(item.receiveTotalQty) + '" style="text-align:right;">' + toThousands(item.receiveTotalQty) + '</td>' +
                            '<td title="' + isEmpty(item.userName) + '" style="text-align:left;">' + isEmpty(item.userName) + '</td>' +
                            '<td title="' + isEmpty(item.receivedRemark) + '" style="text-align:left;">' + isEmpty(item.receivedRemark) + '</td>' +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                    }
                    var totalQty= "<tr style='text-align:right' id='total_qty'>"
                        +"<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td style='text-align:right'>Total:</td>" +
                        "<td style='text-align:right'>total Item</td>" +
                        "<td style='text-align:right'>"+result.o.totalItemSKU+"</td>"+
                        "<td></td>"+
                        "<td style='text-align:right'>total qty </td>"+
                        "<td style='text-align:right'>"+result.o.totalReceiveQty+"</td>"+
                        "<td></td>" +
                        "<td></td>" +
                        "</tr>";
                    m.grid_table.append(totalQty)
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

    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
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
var _index = require('dcReceiptDailyPrint');
_start.load(function (_common) {
    _index.init(_common);
});
