require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax=require("myAjax");
define('shoppingOrderDifferEdit', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        reThousands = null,
        toThousands = null,
        getThousands = null,
        firstInsertOrder = null,//第一次新增单据
        common=null;
    const KEY = 'SHOP_DELIVERY_VARIANCE_QUERY';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        // 基本信息
        search:null,
        modified_by:null,
        create_by:null,
        //后台
        userId:null,
        flag:null,
        bOrderId:null,
        date_modified:null,
        create_dateBy:null,
        // 页面
        order_id:null,
        deliveryOrderId:null,
        org_order_id:null,
        deliveryCenterId:null,
        deliveryCenterName:null,
        create_date:null,
        store_cd:null,
        deliveryDate:null,
        receive_date:null,
        total_qty:null,
        total_amt:null,
        returnsViewBut:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/shoppingOrderDifference";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        initPageByType("1");
        //表格内按钮事件
        table_event();
        m.search.click();
        flushItems();
    }


    //禁用不可修改栏位
    var disableNotModify = function () {
        m.order_id.prop('disabled', true);
        m.create_date.prop('disabled', true);
        m.store_cd.prop('disabled', true);
        m.total_amt.prop('disabled', true);
        m.total_qty.prop('disabled', true);
    }

    //View时禁用所有栏位
    var disableAll = function () {
        m.order_id.prop('disabled', true);
        m.org_order_id.prop('disabled', true);
        m.create_date.prop('disabled', true);
        m.store_cd.prop('disabled', true);
        m.total_qty.prop('disabled', true);
        m.total_amt.prop('disabled', true);
        m.order_date.prop('disabled', true);
        m.receive_date.prop('disabled', true);
        $("#add").prop('disabled', true);
        $("#modify").prop('disabled', true);
        $("#delete").prop('disabled', true);
    }

    //表格按钮事件
    var table_event = function(){
        // //返回一览
        m.returnsViewBut.on("click",function(){
            _common.updateBackFlg(KEY);
            top.location = url_left;
        });
    };


    // 根据权限类型的不同初始化不同的画面样式
    var initPageByType = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                initTable1();//列表初始化
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    };

    //拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={
            orderId :  m.bOrderId.val(),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    };

    //画面按钮点击事件
    var but_event = function(){
        m.deliveryDate.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });

        m.search.on("click",function(){
            //Ajax请求头档事件
            $.myAjaxs({
                url:url_left+"/getDetails?orderId="+ m.bOrderId.val(),
                async:true,
                cache:false,
                type :"get",
                dataType:"json",
                success:function(data){
                    m.order_id.val(data['orderId']);
                    m.deliveryOrderId.val(data['deliveryOrderId']);
                    m.deliveryDate.val(fmtStringDate(String(data['deliveryDate'])));
                    m.store_cd.val(data['storeCd']+' '+data['storeName']);
                    let totalVarianceQty = toThousands(parseFloat(String(data['totalVarianceQty'])));
                    m.total_qty.val(totalVarianceQty);
                    m.deliveryCenterId.val(data['deliveryCenterId']);
                    m.deliveryCenterName.val(data['deliveryCenterName']);
                },
                complete:_common.myAjaxComplete
            });
        });
    };

    //请求单据商品信息
    var flushItems = function () {
        //Ajax请求商品
        setParamJson();
        paramGrid = "searchJson=" + m.searchJson.val();
        tableGrid.setting("url", url_left + "/getItemsByOrderId");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData(null);
    };

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Delivery Variance Detail",
            param:paramGrid,
            localSort: true,
            colNames:["Item Barcode","Item Code","Item Name","tax","UOM","Order Qty","DC Picked Quantity","Receiveing  Qty","(Ordered-DC Picked) Qty","(DC Picked-Received)Qty"],
            colModel:[
                {name:"barcode",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"articleId",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"orderTax",type:"text",text:"center",width:"80",ishide:true,css:""},//隐藏
                {name:"unitName",type:"text",text:"left",width:"80",ishide:false,css:""},
                {name:"orderedQty",type:"text",text:"left",width:"80",ishide:false,css:"",getCustomValue:getThousands},
                {name:"dcPickedQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"receiveQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"orderqtyDcPickedQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"adjustQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands}
            ],//列内容
            // traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            isCheckbox:false,
            eachTrClick: function (trObj) {//正常左侧点击
                $("#update").removeAttr("disabled");
            },
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
        });
    };

    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date){
        if(!!date) {
            return date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        }
    };
    //获取当前日期  20200106
    function getFormatDate() {
        var date = new Date();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentDate = date.getFullYear() + month + strDate;
        return currentDate;
    };

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('shoppingOrderDifferEdit');
_start.load(function (_common) {
    _index.init(_common);
});
