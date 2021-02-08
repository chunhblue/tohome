require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('orderDirectSupplierPrint', function () {
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
        identity : null,
        grid_table : null,
        searchJson:null,
        aRegion:null,
        aCity:null,
        aDistrict:null,
        aStore:null,
        orderDirectSupplierDateStartDate:null,
        orderDirectSupplierDateEndDate:null,
        optionTime:null,
        orderDifferentiate:null,
        orderId:null,
        orderMethod:null,
        orderStatus:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/directSupplierOrder";
        systemPath=_common.config.surl;

        getList();
    }
    //拼接检索参数
    var setParamJson = function(){
        var _startDate = fmtStringDate(m.orderDirectSupplierDateStartDate.val());
        var _endDate = fmtStringDate(m.orderDirectSupplierDateEndDate.val());
        var searchJsonStr = {
            regionCd:m.aRegion.val(),
            cityCd:m.aCity.val(),
            districtCd:m.aDistrict.val(),
            storeCd:m.aStore.val(),
            orderId: m.orderId.val(),
            orderMethod: m.orderMethod.val(),
            reviewStatus:m.orderStatus.val(),
            optionTime: m.optionTime.val(),
            orderDirectSupplierDateStartDate: _startDate,
            orderDirectSupplierDateEndDate: _endDate
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
            data :"searchJson="+m.searchJson.val(),
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
                            '<td style="text-align: right">'+isEmpty(item.orderId)+'</td>' +
                            '<td style="text-align: center">'+fmtIntDate(item.orderDate)+'</td>' +
                            '<td style="text-align: center">'+fmtIntDate(item.deliveryDate)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.vendorId)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.vendorName)+'</td>' +
                            '<td style="text-align: left">'+getMenthod(item.orderMethod)+'</td>' +
                            '<td style="text-align: left">'+getReviewStatus(item.reviewStatus)+'</td>' +
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

    //格式化日期 dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date) {
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }
    // 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
    function fmtIntDate(date){
        if(date==null||date.length!=8){
            return "";
        }
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }
    var getMenthod = function(value){
        let str = '';
        switch (value) {
            case '40':
                str = 'DC Store Order';
                break;
            case '30':
                str='DC Allocation Order';
                break;
            case '20':
                str = 'Direct Store Purchase Order';
                break;
            case '10':
                str='Direct Store Purchase Allocation';
                break;
        }
        return str;
    }
    var getReviewStatus = function(value){
        let str = '';
        switch (value) {
            case '1':
                str = 'Pending';
                break;
            case '5':
                str='Rejected';
                break;
            case '6':
                str='Withdrawn';
                break;
            case '7':
                str = "Expired";
                break;
            case '10':
                str='Approved';
                break;
            case '15':
                str = "Receiving Pending";
                break;
            case '20':
                str='Received';
                break;
            case '30':
                str='Paid';
                break;
        }
        return str;
    }

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };

    var dateFmt = function (tdObj, value) {
         if (value == null || value == undefined || value == '') {
             return '0';
        }
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('orderDirectSupplierPrint');
_start.load(function (_common) {
    _index.init(_common);
});
