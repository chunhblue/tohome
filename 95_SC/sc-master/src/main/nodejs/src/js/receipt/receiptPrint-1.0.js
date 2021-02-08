require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('receiptPrint', function () {
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
        url_left = _common.config.surl+"/receipt/print";
        systemPath = _common.config.surl;

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
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: right">'+isEmpty(item.receiveId)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td style="text-align: center">'+fmtDate(item.receiveDateT)+'</td>' +
                            // '<td style="text-align: center">'+fmtDate(item.deliveryDate)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.orderId)+'</td>' +
                            '<td style="text-align: center">'+fmtDate(item.orderDate)+'</td>' +
                            '<td style="text-align: right">'+getStatus(item.reviewSts)+'</td>' +
                            // '<td style="text-align: right">'+isEmpty(item.orderAmt)+'</td>' +
                            // '<td style="text-align: right">'+isEmpty(item.receiveAmt)+'</td>' +
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

    // 获取审核状态
    // 获取审核状态
    var getStatus = function(value){
        switch (value) {
            case "1":
                value = "Pending";
                break;
            case "5":
                value = "Rejected";
                break;
            case "6":
                value = "Withdrawn";
                break;
            case "7":
                value = "Expired";
                break;
            case "10":
                value = "Approved";
                break;
            case "15":
                value = "Receiving Pending";
                break;
            case "20":
                value = "Received";
                break;
            case "30":
                value = "Paid";
                break;
            default:
                value = "";
        }
        return value;
    }

    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
        }
    }

    function fmtDate(date){
        var res = "";
        if (date==null ||date==""){
            return res;
        }
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
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
var _index = require('receiptPrint');
_start.load(function (_common) {
    _index.init(_common);
});
