require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _datetimepicker = require("datetimepicker");
var _myAjax=require("myAjax");
define('dayOrderFailureView', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        reThousands = null,
        toThousands = null,
        getThousands = null,
        common=null;
    const KEY = 'FAILED_ORDER_OF_THE_DAY';
    var m = {
        toKen : null,
        use : null,
        identity : null,
        searchJson:null,
        // 页面信息
        returnsViewBut:null,
        orderDate:null,
        vendorId:null,
        orderId:null,
        // 弹窗信息
        item_input_barcode:null,
        item_input_articleId:null,
        item_input_articleName:null,
        item_input_uom:null,
        item_input_uomCd:null,
        item_input_spec:null,
        item_input_psd:null,
        item_input_minDisplayQty:null,
        item_input_minOrderQty:null,
        item_input_orderQty:null,
        item_input_orderNochargeQty:null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/dayOrderFailure/view";
        url_root=_common.config.surl+"/dayOrderFailure";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){

        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        //表格内按钮事件
        table_event();
        flushItems();
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    //表格按钮事件
    var table_event = function(){
        // //返回一览
        m.returnsViewBut.on("click",function(){
            _common.myConfirm("Are you sure to exit?",function(result) {
                if (result==="true") {
                    _common.updateBackFlg(KEY);
                    top.location = url_root;
                }
            });
        });
        $("#viewOrderDetails").on("click",function(){
                if(selectTrTemp==null){
                    _common.prompt("Please select at least one row of data!",5,"error");
                    return false;
                }
                itemClear();
                //设置商品信息
                setDialogValue();
                //禁用窗口信息
                $("#affirm").attr("disabled","true");
                $('#update_dialog').modal("show");
        });
        //弹出窗 确认取消按钮
        $("#cancel").on("click",function(){
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if(result=="true") {
                    $("#update_dialog").modal("hide");
                }
            });
        });
    };
    var itemClear = function () {
        m.item_input_barcode.val('');
        m.item_input_articleId.val('');
        m.item_input_articleName.val('');
        m.item_input_uom.val('');
        m.item_input_uomCd.val('');
        m.item_input_spec.val('');
        m.item_input_psd.val('');
        m.item_input_minDisplayQty.val('');
        m.item_input_minOrderQty.val('');
        m.item_input_orderQty.val('');
        m.item_input_orderNochargeQty.val('');
    };


    //拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={
            orderDate : subfmtDate(m.orderDate.val()),
            vendorId : m.vendorId.val(),
            orderId : m.orderId.val()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    };

    //请求单据商品信息
    var flushItems = function () {
        //Ajax请求商品
        setParamJson();
        paramGrid = "searchJson=" + m.searchJson.val();
        tableGrid.setting("url", url_left +"/getDetaildata");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData(null);
    };
    var setDialogValue = function () {
        var cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,unitName,spec,psd,minDisplayQty,minOrderQty,orderQty,orderNochargeQty");
        m.item_input_barcode.val(cols['barcode']);
        m.item_input_articleId.val(cols['articleId']);
        m.item_input_articleName.val(cols['articleName']);
        m.item_input_uom.val(cols['unitName']);
        m.item_input_spec.val(cols['spec']);
        m.item_input_psd.val(cols['psd']);
        m.item_input_minDisplayQty.val(cols['minDisplayQty']);
        m.item_input_minOrderQty.val(cols['minOrderQty']);
        m.item_input_orderQty.val(cols['orderQty']);
        m.item_input_orderNochargeQty.val(cols['orderNochargeQty']);
    }

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Failed Order Detail",
            param:paramGrid,
            localSort: true,
            colNames:["Item Barcode","Item Code","Item Name","UOM","Specification","Purchasing Price","Price","PSD",
                "Display on Shelf","Min. Order Quantity","Order Quantity","Free Order Quantity"],
            colModel:[
                {name:"barcode",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"articleId",type:"text",text:"right",width:"80",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"unitName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"spec",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"orderPrice",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                {name:"salePrice",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                {name:"psd",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"minDisplayQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"minOrderQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"orderQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"orderNochargeQty",type:"text",text:"right",width:"200",ishide:true,css:"",getCustomValue:getThousands}
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
            buttonGroup:[
                {
                    butType: "view",
                    butId: "viewOrderDetails",
                    butText: "View",
                    butSize: ""//,
                }
            ]
        });
    };

    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date){
        if(!!date) {
            return date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        }
    };
    // 日期处理 -->yyyymmdd
    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('dayOrderFailureView');
_start.load(function (_common) {
    _index.init(_common);
});
