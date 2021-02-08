require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('stocktakeProcessPrint', function () {
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
        orderId:null,
        orgOrderId:null,
        orderDate:null,
        receiveDate:null,
        createDate:null,
        storeCd:null,
        storeName:null,
        deliveryCenterId:null,
        deliveryCenterName:null,
        totalAmt:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/difference/print";
        systemPath=_common.config.surl;

        getList();
    }
    //拼接检索参数
    var setParamJson = function(){
        let orderId = m.orderId.val();
        let orgOrderId = m.orgOrderId.val();
        let orderDate = fmtIntDate(String(m.orderDate.val()));
        let receiveDate = fmtIntDate(String(m.receiveDate.val()));
        let createDate = fmtIntDate(String(m.createDate.val()));
        let storeCd = m.storeCd.val();
        let storeName = m.storeName.val();
        let deliveryCenterId = m.deliveryCenterId.val();
        let deliveryCenterName = m.deliveryCenterName.val();
        let totalAmt = m.totalAmt.val();

        var searchJsonStr ={
            'orderId':orderId,
            'orgOrderId':orgOrderId,
            'orderDate': orderDate,
            'receiveDate': receiveDate,
            'createDate':createDate,
            'storeCd':storeCd,
            'storeName':storeName,
            'deliveryCenterId':deliveryCenterId,
            'deliveryCenterName':deliveryCenterName,
            'totalAmt':totalAmt,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }



    //画面按钮点击事件
    var getList = function(){
        setParamJson();
        $.myAjaxs({
            url:url_left+"/getprintData",
            async:true,
            cache:false,
            type :"post",
            data :`SearchJson=m.searchJson.val()`,
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                console.log(result);
                if(result.success){
                    let dataList = result.o;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        $("#storeName").text("Store Name:"+item.storeName);
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: center">'+isEmpty(item.orderId)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.orgOrderId)+'</td>' +
                            '<td style="text-align: center">'+isEmpty(item.orderDate)+'</td>' +
                            '<td style="text-align: center">'+isEmpty(item.receiveDate)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.createDate)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.deliveryCenterId)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.deliveryCenterName)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.totalAmt)+'</td>' +
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
var _index = require('stocktakeProcessPrint');
_start.load(function (_common) {
    _index.init(_common);
});
