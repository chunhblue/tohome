require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
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
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        writeOffStartDate : null,
        writeOffEndDate : null,
        itemId : null,
        itemName : null,
        am : null,
        dep : null,
        pma : null,
        category : null,
        subCategory : null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/storeInventoryDaily/print";
        systemPath=_common.config.surl;
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
            data :"searchJson="+m.searchJson.val()+"&page="+$('#page').val()+"&rows="+$('#rows').val(),
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                if(result==null||result.length==0) {
                    $("#grid_table  tr:not(:first)").remove();
                    return false;
                }
                if(result.success){
                    let dataList = result.o;
                    for (let i = 0; i < dataList.length; i++) {
                        var re = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            "<td title='"+re.storeCd+"' style='text-align:right;'>" + re.storeCd + "</td>" +
                            "<td title='"+re.storeName+"' style='text-align:left;'>" + re.storeName + "</td>" +
                            "<td title='"+re.voucherDate+"' style='text-align:center;'>"  + re.voucherDate + "</td>" +
                            "<td title='"+re.depName+"' style='text-align:right;'>" + re.depName + "</td>" +
                            "<td title='"+re.pmaName+"' style='text-align:left;'>"+ re.pmaName +"</td>" +
                            "<td title='"+re.categoryName+"' style='text-align:left;'>"+ re.categoryName +"</td>" +
                            "<td title='"+re.subCategoryName+"' style='text-align:left;'>"+ re.subCategoryName +"</td>" +
                            "<td title='"+re.barcode+"' style='text-align:right;'>"+ re.barcode +"</td>" +
                            "<td title='"+re.articleId+"' style='text-align:right;'>" + re.articleId + "</td>" +
                            "<td title='"+re.articleName+"' style='text-align:left;'>" + re.articleName + "</td>" +
                            "<td title='"+re.uom+"' style='text-align:left;'>" + re.uom + "</td>" +
                            "<td title='"+re.amName+"' style='text-align:left;'>" + re.amName + "</td>" +
                            "<td title='"+toThousands(re.qty1)+"' style='text-align:right;'>" + toThousands(re.qty1) + "</td>" +
                            "</tr>";
                        m.grid_table.append(tempTrHtml);
                    }
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

    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        if(date == null || date == ''){
            return;
        }
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
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
var _index = require('storeInventoryDailyPrint');
_start.load(function (_common) {
    _index.init(_common);
});
