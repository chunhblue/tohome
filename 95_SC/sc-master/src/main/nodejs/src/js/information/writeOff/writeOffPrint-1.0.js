require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('writeOffPrint', function () {
    var self = {};
    var url_left = "",
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
        url_left=_common.config.surl+"/writeOff/print";
        systemPath=_common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
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
            data: 'searchJson='+record,
            dataType: "json",
            success: function (result) {
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                if (result.success) {
                    let dataList = result.o.data;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td title="'+isEmpty(item.storeCd)+'" style="text-align: right">'+isEmpty(item.storeCd)+'</td>' +
                            '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td title="'+isEmpty(item.writeOffDate)+'" style="text-align: center">'+fmtIntDate(item.writeOffDate) +'</td>' +
                            // '<td title="'+isEmpty(item.depCd)+'" style="text-align:left;">'+isEmpty(item.depCd)+'</td>' +
                            '<td title="'+isEmpty(item.depName)+isEmpty(item.depCd)+'" style="text-align: left">'+isEmpty(item.depCd)+"&nbsp;&nbsp;"+isEmpty(item.depName)+'</td>' +
                            // '<td title="'+isEmpty(item.pmaCd)+'" style="text-align: left">'+isEmpty(item.pmaCd)+'</td>' +
                            '<td title="'+isEmpty(item.pmaName)+isEmpty(item.pmaCd)+'" style="text-align: left">'+isEmpty(item.pmaCd)+"&nbsp;&nbsp;"+isEmpty(item.pmaName)+'</td>' +
                            // '<td title="'+isEmpty(item.categoryCd)+'" style="text-align: left">'+isEmpty(item.categoryCd)+'</td>' +
                            '<td title="'+isEmpty(item.categoryName)+isEmpty(item.categoryCd)+'" style="text-align: left">'+isEmpty(item.categoryCd)+"&nbsp;&nbsp;"+isEmpty(item.categoryName)+'</td>'+
                            // '<td title="'+isEmpty(item.subCategoryCd)+'" style="text-align: left">'+isEmpty(item.subCategoryCd)+'</td>' +
                            '<td title="'+isEmpty(item.subCategoryCd)+isEmpty(item.subCategoryName)+'" style="text-align: left">'+isEmpty(item.subCategoryCd)+"&nbsp;&nbsp;"+isEmpty(item.subCategoryName)+'</td>' +
                            '<td title="'+isEmpty(item.articleId)+'" style="text-align: right">'+isEmpty(item.articleId)+'</td>' +
                            '<td title="'+isEmpty(item.articleName)+'" style="text-align: left">'+isEmpty(item.articleName)+'</td>' +
                            '<td title="'+isEmpty(item.barcode)+'" style="text-align: right">'+isEmpty(item.barcode)+'</td>' +
                            '<td title="' + isEmpty(item.uom) + '" style="text-align: left">' + isEmpty(item.uom) + '</td>' +
                            '<td title="'+toThousands(item.writeOffQty)+'" style="text-align:right;">'+toThousands(item.writeOffQty)+'</td>' +
                            '<td title="' + toThousands(item.saleQty) + '" style="text-align:right;">' + toThousands(item.saleQty) + '</td>' +
                            //'<td title="'+toThousands(item.writeOffAmt) +'" style="text-align:left;">'+toThousands(item.writeOffAmt) +'</td>' +
                            "<td style='text-align:left;'>" + isEmpty(item.adjustReason) + "</td>" +
                            '<td title="'+isEmpty(item.ofc)+'" style="text-align: left">'+isEmpty(item.ofc)+'</td>' +
                            '<td title="'+isEmpty(item.ofcName)+'" style="text-align: left">'+isEmpty(item.ofcName)+'</td>' +
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
                        "<td style='text-align:right'>"+toThousands(result.o.totalItem)+"</td>"+
                        "<td></td>"+
                        "<td></td>"+
                        "<td style='text-align:right'>total qty </td>"+
                        "<td style='text-align:right'>"+toThousands(result.o.ItemQty)+"</td>"+
                        "<td style='text-align:right'>"+toThousands(result.o.itemSaleQty)+"</td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "</tr>";
                        $("#grid_table").append(totalQty);
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
    //         url: systemPath+"/writeOff/getOffQty",
    //         async: false,
    //         cache: false,
    //         type: "post",
    //         data: 'searchJson=' + data,
    //         dataType: "json",
    //         success: function (result) {
    //             if(result.success){
    //                 $("#toTotalQty").val(result.o.writeOffQty);
    //                 $("#itemSaleQty").val(result.o.saleQty);
    //                 $("#totalDoc").val(result.o.records);
    //             }else {
    //                 $("#toTotalQty").val('');
    //                 $("#itemSaleQty").val('');
    //                 $("#totalDoc").val('');
    //             }
    //         },
    //         error:function () {
    //             $("#toTotalQty").val('');
    //             $("#totalDoc").val('');
    //         }
    //     })
    // };

    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
        }
    }
    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
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
var _index = require('writeOffPrint');
_start.load(function (_common) {
    _index.init(_common);
});
