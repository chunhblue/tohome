require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receiptReturnWarehousePrint', function () {
    var self = {};
    var url_left= "",
        systemPath='';
    var m = {
        searchJson:null,
        grid_table:null,
        rt_date:null,
        order_id:null,
        org_order_id:null,
        delivery_center_id:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        typeId:null,//审核类型id
        rt_start_date:null,
        rt_end_date:null,
        reviewStatus:null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/returnWarehouse/print";
        systemPath=_common.config.surl;
        getList();
    }

    //拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var	_reviewStatus = null;
        if(m.reviewStatus.val()!==""){
            _reviewStatus = m.reviewStatus.val()
        }
        var searchJsonStr ={
            regionCd:m.aRegion.val(),
            cityCd:m.aCity.val(),
            districtCd:m.aDistrict.val(),
            storeCd:m.aStore.val(),
            orderStartDate:m.rt_start_date.val(),
            orderEndDate:m.rt_end_date.val(),
            orderId :m.order_id.val(),
            orgOrderId :m.org_order_id.val(),
            deliveryCenterId :m.delivery_center_id.val(),
            returnSts:"10",//退货状态  为审核通过的退货
            reviewStatus : _reviewStatus
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //画面按钮点击事件
    var getList = function(){
        setParamJson();
        $.myAjaxs({
            url:url_left+"/getPrintData",
            async:true,
            cache:false,
            type :"post",
            data :'searchJson='+m.searchJson.val(),
            dataType:"json",
            success:function(result){
                var trList = $("#grid_table  tr:not(:first)");
                trList.remove();
                if(result.success){
                    let dataList = result.o;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: center">'+dateFmt(item.orderDate)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.orderId)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.orgOrderId)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.vendorId)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.vendorName)+'</td>' +
                            '<td style="text-align: right">'+isEmpty(item.storeCd)+'</td>' +
                            '<td style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td style="text-align: left">'+getStatus(item.reviewStatus)+'</td>' +
                            '<td style="text-align: left">'+getStatus(item.status)+'</td>' +
                            '<td style="text-align: right">'+_common.toThousands(item.orderQty)+'</td>' +
                            '<td style="text-align: right">'+_common.toThousands(item.receiveQty)+'</td>' +
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
                value = "";
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
var _index = require('receiptReturnWarehousePrint');
_start.load(function (_common) {
    _index.init(_common);
});
