require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('ordercdprint', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        url_root = '';
    var m = {
        barcode:null,
        reset:null,
        str: null,
        search: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        store_Cd: null,
        toKen: null,
        use: null,
        error_pcode: null,
        dc:null,
        orderStatus:null,
        orderMethod:null,
        optionTime:null,
        orderId: null,
        main_box: null,
        identity: null,
        searchJson: null,
        order_Date:null,
        orderSts:null,
        delivery_Date:null,
        orderDirectSupplierDateStartDate: null,
        orderDirectSupplierDateEndDate: null,
        grid_table: null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        url_root = _common.config.surl;
        url_left=_common.config.surl+"/cdOrder";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        createJqueryObj();
        //拼接参数
        var _startDate = fmtStringDate(m.orderDirectSupplierDateStartDate.val());
        var _endDate = fmtStringDate(m.orderDirectSupplierDateEndDate.val());
        var searchJsonStr = {
            regionCd:m.aRegion.val(),
            cityCd:m.aCity.val(),
            districtCd:m.aDistrict.val(),
            storeCd:m.aStore.val(),
            orderId: m.orderId.val(),
            reviewStatus:m.orderStatus.val(),
            orderMethod:m.orderMethod.val(),
            orderDifferentiate: '1', // 订货区分(0:供货商1：物流)
            orderDirectSupplierDateStartDate: _startDate,
            orderDirectSupplierDateEndDate: _endDate,
            optionTime: m.optionTime.val(),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
     let record=m.searchJson.val();
        $.myAjaxs({
            url:url_left+"/getPrintInfor",
            async:true,
            cache:false,
            type :"post",
            data: 'searchJson='+record,
            dataType:"json",
            success:function (result) {
                let dataList = result.o;
                for (let i = 0; i < dataList.length; i++) {
                    var item = dataList[i];
                    let tempTrHtml = '<tr style="text-align: center;">' +
                        '<td style="text-align: right">'+isEmpty(item.orderId)+'</td>' +
                        '<td style="text-align: center">'+fmtIntDate(item.orderDate)+'</td>' +
                        '<td style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                        '<td style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                        '<td style="text-align: left">'+isEmpty(item.vendorId)+'</td>' +
                        '<td style="text-align: left">'+isEmpty(item.warehouseName)+'</td>' +
                        '<td style="text-align: left">'+getReviewStatus(item.reviewStatus)+'</td>' +
                        '<td style="text-align: left">'+getMenthod(item.orderMethod)+'</td>' +
                        '</tr>';
                    m.grid_table.append(tempTrHtml);
                }
                window.print();

            },
            error : function(e){
                    _common.prompt("Failed to load data!",5,"error");
            }
        });

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
            case "15":
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
    var isZero = function (str) {
        if (str == null || str == undefined || str == '') {
            return '0';
        }
        return str;
    };
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('ordercdprint');
_start.load(function (_common) {
    _index.init(_common);
});
