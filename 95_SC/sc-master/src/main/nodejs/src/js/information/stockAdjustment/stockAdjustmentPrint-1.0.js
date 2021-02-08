require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('stockAdjustmentPrint', function () {
    var self = {};
    var url_left = "",
        reThousands = null,
        toThousands = null,
        getThousands = null,
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
        url_left=_common.config.surl+"/adjustmentDaily/print";
        systemPath=_common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // total_detail(m.searchJson.val());
        getList();
    }

    //画面按钮点击事件
    var getList = function(){
        let record = m.searchJson.val();
        $.myAjaxs({
            url: url_left + "/getprintData",
            async: true,
            cache: false,
            type: "post",
            data: 'SearchJson='+record,
            dataType: "json",
            success: function (result) {
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                if (result.success) {
                    let dataList = result.o.data;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td title="' + isEmpty(item.storeCd) + '" style="text-align: left">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align: left">' + isEmpty(item.storeName) + '</td>' +
                            '<td title="' + fmtIntDate(item.adjustmentDate) + '" style="text-align: center">' + fmtIntDate(item.adjustmentDate) + '</td>' +
                            '<td title="' + isEmpty(item.depCd) + isEmpty(item.depName) + '" style="text-align: left">' + isEmpty(item.depCd) + "&nbsp;&nbsp;" + isEmpty(item.depName) + '</td>' +
                            '<td title="' + isEmpty(item.pmaCd) + isEmpty(item.pmaName) + '" style="text-align: left">' + isEmpty(item.pmaCd) + "&nbsp;&nbsp;" + isEmpty(item.pmaName) + '</td>' +
                            '<td title="' + isEmpty(item.categoryCd) + isEmpty(item.categoryName) + '" style="text-align: left">' + isEmpty(item.categoryCd) + "&nbsp;&nbsp;" + isEmpty(item.categoryName) + '</td>' +
                            '<td title="' + isEmpty(item.subCategoryCd) + isEmpty(item.subCategoryName) + '" style="text-align: left">' + isEmpty(item.subCategoryCd) + "&nbsp;&nbsp;" + isEmpty(item.subCategoryName) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align: right">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align: left">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align: right">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.uom) + '" style="text-align:left;">' + isEmpty(item.uom) + '</td>' +
                            '<td title="' + toThousands(item.adjustmentQty) + '" style="text-align:right;">' + toThousands(item.adjustmentQty) + '</td>' +
                            '<td title="' + isEmpty(item.generalReasonText) + '" style="text-align: left">' + isEmpty(item.generalReasonText) + '</td>' +
                            '<td title="' + isEmpty(item.adjustReasonText) + '" style="text-align: left">' + isEmpty(item.adjustReasonText) + '</td>' +
                            '<td title="' + isEmpty(item.ofc) + '" style="text-align: left">' + isEmpty(item.ofc) + '</td>' +
                            '<td title="' + isEmpty(item.ofcName) + '" style="text-align: left">' + isEmpty(item.ofcName) + '</td>' +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                    }
                    var totalQty= "<tr style='text-align:right' id='total_qty'>"
                        +"<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td style='text-align:right'>Total:</td>" +
                        "<td style='text-align:right'>total Item</td>" +
                        "<td style='text-align:right'>"+toThousands(result.o.totalItemSKU1)+"</td>"+
                        "<td></td>"+
                        "<td></td>" +
                        "<td style='text-align:right'>total qty </td>"+
                        "<td style='text-align:right'>"+toThousands(result.o.ItemQty)+"</td>"+
                        "<td></td>" +"<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "</tr>";
                    m.grid_table.append(totalQty);
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

    // var total_detail = function (data) {
    //     $.myAjaxs({
    //         url: systemPath+"/adjustmentDaily/getItemQty",
    //         async: false,
    //         cache: false,
    //         type: "post",
    //         data: 'searchJson=' + data,
    //         dataType: "json",
    //         success: function (result) {
    //             if(result.success){
    //                 $("#itemTolQty").val(result.o.adjustmentQty);
    //                 $("#totalItem").val(result.o.records);
    //             }
    //         },
    //         error:function () {
    //             $("#itemTolQty").val('');
    //             $("#totalItem").val('');
    //         }
    //     })
    // };

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
var _index = require('stockAdjustmentPrint');
_start.load(function (_common) {
    _index.init(_common);
});
