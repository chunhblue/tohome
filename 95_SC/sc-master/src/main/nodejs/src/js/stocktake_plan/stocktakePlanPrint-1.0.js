require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('stocktakePlanPrint', function () {
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
        url_left=_common.config.surl+"/stocktakePlanEntry/print";
        systemPath=_common.config.surl;

        getList();
    }


    //画面按钮点击事件
    var getList = function(){
        $.myAjaxs({
            url:url_left+"/getprintData",
            async:true,
            cache:false,
            type :"post",
            data :{searchJson:m.searchJson.val()},
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                console.log(result);
                if(result.success){
                    let dataList = result.o;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        // $("#storeName").text("Store Name:"+item.storeName);
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: center">'+isEmpty(item.piDate)+'</td>' +
                            '<td style="text-align: center">'+isEmpty(item.piStartTime)+'</td>' +
                            '<td style="text-align: center">'+isEmpty(item.piEndTime)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.piCd)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.storeCd)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.storeName)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.piStatus)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.piType)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.remarks)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.dayEndOfNow)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.createUserId)+'</td>' +
                            '<td style="text-align: center">'+_common.formatCreateDate(item.createYmd)+'</td>' +
                            '</tr>';
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
var _index = require('stocktakePlanPrint');
_start.load(function (_common) {
    _index.init(_common);
});
