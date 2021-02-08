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
        systemPath='';
    var m = {
        use : null,
        tableGrid : null,
        selectTrObj : null,
        identity : null,
        grid_table : null,
        searchJson:null,
        receioadFlg:null,
        typeId:null,
        aRegion:null,
        aCity:null,
        aDistrict:null,
        aStore:null,
        voucherNo:null,
        orgVoucherNo:null,
        orderMethod:null,
        voucherType:null,
        orderStartDate:null,
        orderEndDate:null,
        vendorId:null,
        voucherSts:null,
        orgOrderStartDate:null,
        orgOrderEndDate:null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl+"/fsInventoryEntry/print";
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
            data :"searchJson="+m.searchJson.val(),
            dataType:"json",
            success:function(result){
                let trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                console.log(result);
                if(result.success){
                    let dataList = result.o;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.storeName)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.piCd)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.remarks)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.createUserName)+'</td>' +
                            '<td style="text-align: right">'+formatDateAndTime(item.createYmd)+'</td>' +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                    }
                }else {
                    _common.prompt(result.msg, 3, "info");
                    return false;
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

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };

    // 格式化百分比
    function fmtRate(value){
        if(value==null||value===""){
            return "0%";
        }else {
            value=Number(accMul(value,100)).toFixed(2);
            let string = value.split(".")[0];
            return string+"%";
        }
    }
    function accMul(arg1,arg2){
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    }

    // 日期字段格式化格式
    function dateFmt(value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }else{
            value = '';
        }
        return value;
    }




    // 获取单据类型名称
    function getTypeName(value){
        let _value = "";
        switch (value) {
            case "0":
                _value = "[0] Return Document";
                break;
            case "1":
                _value = "[1] Receiving Document";
                break;
        }
        return _value;
    }

    // 审核状态获取
    function getStatus(value){
        switch (value) {
            case 1:
                return "Pending";
            case 5:
                return "Rejected";
            case 6:
                return "Withdrawn";
            case 10:
                return "Approved";
            case 20:
                return "Returned/Received";
            case 7:
                return "Expired";
            default:
                return "";
        }
    }
    var formatDateAndTime = function (str) {
        if (str.length!=14) {
            return '';
        }
        return formatCreateDate(str);
    }
    var formatCreateDate = function (str) {
        if (str.length!=14) {
            return '';
        }
        let year = str.substring(0,4);
        let month = str.substring(4,6);
        let day = str.substring(6,8);
        let hour = str.substring(8,10);
        let minute = str.substring(10,12);
        let second = str.substring(12,14);
        let date = day+"/"+month+"/"+year+" "+hour+":"+minute+":"+second;
        return date;
    }
    // 设置千分位, 四舍五入不保留小数位
    function toThousands(num) {
        if (!num) {
            return '0';
        }
        num = num + ''
        const reg = /\d{1,3}(?=(\d{3})+$)/g
        if (num.indexOf('.') > -1) {
            if (num.indexOf(',') != -1) {
                num = num.replace(/\,/g, '');
            }
            num = parseFloat(num).toFixed(0);
            return (num + '').replace(reg, '$&,');
        } else {
            return (num + '').replace(reg, '$&,');
        }
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receiptPrint');
_start.load(function (_common) {
    _index.init(_common);
});
