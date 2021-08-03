require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('vendorReturnDocumentPrint', function () {
    var self = {};
    var url_left= "",
        systemPath=''
    var m = {
        searchJson:null,
        returnType:null,
        remarkValue:null,
        grid_table:null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/returnVendor";
        systemPath=_common.config.surl;
        getList();
    }

    var totalQty=0;
    //画面按钮点击事件
    var getList = function(){

        $.myAjaxs({
            url:url_left+"/getDocumentPrintData",
            async:true,
            cache:false,
            type :"get",
            data :{
                searchJson:m.searchJson.val()
            },
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                if (result.success) {
                    let dataList = result.data;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        var s = parseInt(item.orderQty);
                        totalQty=parseInt(totalQty)+s;
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: center">' + (i + 1) + '</td>' +
                            '<td style="text-align: center">' + item.barcode + '</td>' +
                            '<td style="text-align: center">' + item.articleId + '</td>' +
                            '<td style="text-align: left">' + item.articleName + '</td>' +
                            '<td style="text-align: center">' + item.unitName + '</td>' +
                            '<td style="text-align: center">' + _common.toThousands(item.orderQty) + '</td>' +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                    }
                    let ToalTrHtml = '<tr style="text-align: center;">' +
                        '<td style="text-align: center">' + " " + '</td>' +
                        '<td style="text-align: center">' + " " + '</td>' +
                        '<td style="text-align: center">' + " " + '</td>' +
                        '<td style="text-align: center">' + " " + '</td>' +
                        '<td style="text-align: center">' + "Total " + '</td>' +
                        '<td style="text-align: center">'+totalQty+'</td>' +

                    '</tr>';
                    m.grid_table.append(ToalTrHtml);
                    //$("#remarkValue").val(item.orderRemark);

                } else {
                    _common.prompt("Print data exception！", 5, "info");/*打印数据异常*/
                }
                // 打印
                window.print();
            },
            error : function(e){
                _common.prompt("Failed to load data!",5,"error");
            }
        });
    }
    var getSts=function(str){
        if (str=="10"){
            return "Direct Return";
        }else {
            return "Original Document Based  Return";
        }


    }
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
            case "20":
                value = "Returned";
                break;
            default:
                value = "Unknown";
        }
        return value;
    }



    //日期字段格式化格式
    var dateFmt = function(value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            return value.substring(6,8)+"/"+value.substring(4,6)+"/"+value.substring(0,4);
        }
        return '';
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
var _index = require('vendorReturnDocumentPrint');
_start.load(function (_common) {
    _index.init(_common);
});