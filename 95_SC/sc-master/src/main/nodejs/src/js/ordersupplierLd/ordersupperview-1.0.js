require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");

define('ordersupperview', function () {
    var self = {};
    var url_left = "",
        paramGrid=null,
    url_root="";
    var m={
        orderId:null,
        orderDate:null,
        storeCd:null,
        storeName:null,
        vendorId:null,
        vendorName:null,
        articleId:null,
        barcode:null,
        productName:null,
        orderUnit:null,
        minOrderQty:null,
        orderQty:null,
        orderNochargeQty:null,
        receiveQty:null,
        receivingDifferences:null,
        deliveryDate:null,
        data:null,
        order_id:null,
        order_date:null,
        store_cd:null,
        store_name:null,
        vendor_id:null,
        searchJson:null,
        search:null,
        returnsViewBut:null,
        vendor_name:null,
        order_qty:null
    }
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left=_common.config.surl+ "/directSupplierOrder";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        initTable2();
        table_event();
       initFunction();
        m.search.click();
    }




    initFunction = function(){
        var searchJsonStr={
           orderId: m.orderId.val(),
            orderDate:m.orderDate.val(),
            storeCd:m.storeCd.val(),
            storeName:m.storeName.val(),
            vendorId:m.vendorId.val(),
            vendorName:m.vendorName.val(),
            articleId:m.articleId.val(),
            barcode:m.barcode.val(),
            productName:m.productName.val(),
            orderUnit:m.orderUnit.val(),
            minOrderQty:m.minOrderQty.val(),
            orderQty:m.orderQty.val(),
            orderNochargeQty:m.orderNochargeQty.val(),
            receiveQty:m.receiveQty.val(),
            receivingDifferences:m.receivingDifferences.val(),
            deliveryDate:m.deliveryDate.val(),
        }
        m.searchJson.val(JSON.stringify(searchJsonStr))
        paramGrid ="searchJson="+m.searchJson.val();
         m.search.on("click",function () {
          tableGrid.setting("url",url_left+"/getViewsupperInfor");
          tableGrid.setting("param", paramGrid);
          tableGrid.setting("page", 1);
          tableGrid.loadData(null);
      })
    }
    var table_event=function () {
        m.returnsViewBut.on("click",function(){

                top.location = url_left;
        });
    }
    var initTable2 = function () {
        tableGrid = $("#zgGridTable").zgGrid({
            title: "Query Result",//查询结果
            param: paramGrid,
            localSort: true,
            rownumbers: true,
            rownumWidth:35,
            colNames: ["PO No.","Store No.","Store Name","Vendor Code","Vendor Name", "Item Code", "Barcode ", "Item Name", "UOM", "Min. Order Quantity", "Order Quantity",
                "order Date","Free Order Quantity", "Receive Quantity", "Receiving Differences"],
            colModel: [
                {name: "orderId", type: "text", text: "right", width: "130", ishide: true, css: ""},
                {name: "storeCd", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "storeName", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "vendorId", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "vendorName", type: "text", text: "left", width: "130", ishide:true, css: ""},
                {name: "articleId", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "barcode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "productName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "orderUnit",type: "text",text: "left",width: "130",ishide: false,css: "",getCustomValue: null},
                {name: "minOrderQty",type: "text",text: "right",width: "160",ishide: false,css: "",getCustomValue: getThousands},
                {name: "orderQty",type: "text",text: "right",width: "130",ishide: false,css: "",getCustomValue: getThousands},
                {name: "orderDate",type: "text",text: "right",width: "130",ishide: true,css: ""},
                {name: "orderNochargeQty",type: "text",text: "right",width: "160",ishide: false,css: "",getCustomValue: getThousands},
                {name: "receiveQty",type: "text",text: "right",width: "130",ishide: false,css: "",getCustomValue: getThousands},
                {name: "receivingDifferences",type: "number",text: "right",width: "200",ishide: false,css: "",getCustomValue: getThousands},
            ],//列内容
            // traverseData:data,
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            // buttonGroup:[
            //     {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"},
            //     {butType:"custom",butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-view'></span> View</button>"}
            // ]
        });
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('ordersupperview');
_start.load(function (_common) {
    _index.init(_common);
});