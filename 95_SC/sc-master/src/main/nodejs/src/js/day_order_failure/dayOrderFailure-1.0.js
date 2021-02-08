require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('orderFailed', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    const KEY = 'FAILED_ORDER_OF_THE_DAY';
    var m = {
        toKen : null,
        use : null,
        main_box : null,
        error_pcode : null,
        identity : null,
        userName : null,
        searchJson: null,
        orderDate: null,
        businessDate:null,
        articleId : null,
        vendorInfo : null,
        ItemInfo : null,
        barcode : null,
        createUserId : null,
        search : null,
        reset : null
    }

    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = url_root + "/dayOrderFailure";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // 首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            var businessDate = m.businessDate.val();
            m.orderDate.val(fmtIntDate(businessDate));
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 加载下拉列表
        // getSelectValue();
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        table_event();
        //权限验证
        isButPermission();

        initParam();
    }

    //表格内按钮点击事件
    var table_event = function(){
        // 导出按钮点击事件
        $("#export").on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });
        $("#view").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                var cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId,createUserId,methodName," +
                    "storeCd,storeName,vendorId,vendorName");
                var orderDate = '';
                if(cols["orderDate"]!=null&&cols["orderDate"]!=''){
                    orderDate = cols["orderDate"];
                }
                var param = "orderDate=" + orderDate + "&orderId=" + cols["orderId"]+ "&createUserId=" + cols["createUserId"]+
                    "&methodName=" + cols["methodName"] + "&storeCd=" + cols["storeCd"]+
                    "&storeName=" + cols["storeName"]+ "&vendorId=" + cols["vendorId"]+
                    "&vendorName=" + cols["vendorName"];
                saveParamToSession();
                top.location = url_left+"/view?"+param;
            }
        });
    };
    // 保存 参数信息
    var saveParamToSession = function () {
        let searchJson = m.searchJson.val();
        if (!searchJson) {
            return;
        }

        let obj = eval("("+searchJson+")");
        obj.orderDate=m.orderDate.val();
        obj.vendorInfo=m.vendorInfo.val();
        obj.ItemInfo=m.ItemInfo.val();
        obj.barcode=m.barcode.val();
        obj.createUserId=m.createUserId.val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    // 初始化参数, back 的时候 回显 已选择的检索项
    var initParam = function () {
        let searchJson=sessionStorage.getItem(KEY);
        if (!searchJson) {
            return;
        }
        let obj = eval("("+searchJson+")");
        // 取出后就清除
        sessionStorage.removeItem(KEY);
        // 不是通过 back 正常方式返回, 清除不加载数据
        if (obj.flg==='0') {
            return;
        }
        if (obj.reviewSts=='-1') {
            obj.reviewSts='';
        }
        m.orderDate.val(obj.orderDate);
        m.vendorInfo.val(obj.vendorInfo);
        m.ItemInfo.val(obj.ItemInfo);
        m.barcode.val(obj.barcode);
        m.createUserId.val(obj.createUserId);

        // 拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/getdata");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData();
    }

    // 画面按钮点击事件
    var but_event = function(){
        // 开始日
        m.orderDate.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        // 检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/getdata");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
        // 清空按钮事件
        m.reset.on("click",function(){
            m.vendorInfo.val("");
            m.ItemInfo.val("");
            m.barcode.val("");
            m.createUserId.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
    }

    // 判断是否是数字
    var checkNum = function(value){
        var reg = /^[0-9]*$/;
        return reg.test(value);
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        if(!$("#orderDate").val()){
            _common.prompt("Please select order date!",3,"info");/*请选择日期*/
            $("#orderDate").focus();
            return false;
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 订货日期
        var _orderDate = fmtStringDate(m.orderDate.val())||null;
        // 创建请求字符串
        var searchJsonStr ={
            orderDate:_orderDate,
            vendorInfo:m.vendorInfo.val().trim(),
            ItemInfo:m.ItemInfo.val().trim(),
            barcode:m.barcode.val().trim(),
            createUserId:m.createUserId.val().trim()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }


    //表格初始化-库存报废样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Failed Order Of The Day Query List",
            param:paramGrid,
            localSort: true,
            colNames:["Order Date","Order ID","Order By","Order Type","Store No.","Store Name","Vendor Code","Vendor Name",
                "Order Quantity","Vender MOQ","Order Gap Quantity","Order Amount", "Vender MOA","Order Gap Amount"],
            colModel:[
                {name:"orderDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"orderId",type:"text",text:"right",width:"100",ishide:true,css:""},
                {name:"createUserId",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"methodName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"vendorId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"vendorName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"totalOrderQty",type:"text",text:"right",width:"120",ishide:false,css:"",getCustomValue:getThousands},
                {name:"minOrderQty",type:"text",text:"right",width:"120",ishide:false,css:"",getCustomValue:getThousands},
                /*与最小的订货数量差距数量*/
                {name:"orderGapQuantity",type:"text",text:"right",width:"160",ishide:false,css:"",getCustomValue:getThousands},
                /*供应商订货总金额*/
                {name:"totalOrderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                {name:"minOrderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                /*与最小的订货金额差距金额*/
                {name:"orderGapAmount",type:"text",text:"right",width:"150",ishide:false,css:"",getCustomValue:getThousands},

            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                {butType:"custom",butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-pencil'></span> View</button>"},
                {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
            ]
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var exportButPm = $("#orderButExport").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }


    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        date = date.replace(/\//g,"");
        res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
        return res;
    }

    //格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }


    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
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
var _index = require('orderFailed');
_start.load(function (_common) {
    _index.init(_common);
});
