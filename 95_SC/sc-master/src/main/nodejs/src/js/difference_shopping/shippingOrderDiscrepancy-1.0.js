require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax=require("myAjax");
define('shoppingOrderDifference', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrOrderId = null;// 选中的单据编号;
    const KEY = 'SHOP_DELIVERY_VARIANCE_QUERY';
    var m = {
        typeId:null,
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        // 查询部分
        rd_start_date:null,
        rd_end_date:null,
        order_id:null,
        vd_start_date:null,
        vd_end_date:null,
        delivery_start_date:null,
        delivery_end_date:null,
        query_type_val:null,
        search:null,
        reset:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
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
        // 初始化店铺运营组织检索
        _common.initOrganization();
        initPageBytype("1");
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.rd_start_date,m.rd_end_date);
        // 初始化参数
        initParam();
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
        if (obj.flg=='0') {
            return;
        }

        m.order_id.val(obj.orderId);
        m.delivery_start_date.val(obj.deliveryStartDate);
        m.delivery_end_date.val(obj.deliveryEndDate);
        m.rd_start_date.val(obj.receiveStartDate);
        m.rd_end_date.val(obj.receiveEndDate);
        $('#query_type_val').val(obj.dc);

        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/query");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", obj.page);
        tableGrid.loadData();
    }

    // 保存 参数信息
    var saveParamToSession = function () {
        let searchJson = m.searchJson.val();
        if (!searchJson) {
            return;
        }

        let obj = eval("("+searchJson+")");
        obj.regionName=$('#aRegion').attr('v');
        obj.cityName=$('#aCity').attr('v');
        obj.districtName=$('#aDistrict').attr('v');
        obj.storeName=$('#aStore').attr('v');
        obj.page=tableGrid.getting("page");
        obj.deliveryStartDate=m.delivery_start_date.val();
        obj.deliveryEndDate=m.delivery_end_date.val();
        obj.receiveStartDate=m.rd_start_date.val();
        obj.receiveEndDate=m.rd_end_date.val();
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    //表格按钮事件
    var table_event = function(){
        $("#view").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                saveParamToSession();
                top.location = url_left+"/toView?order_id="+ tempTrOrderId +"&identity="+m.identity.val();
            }
        });

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
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
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
    }

    function judgeNaN (value) {
        return value !== value;
    }

    //验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#rd_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#rd_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#rd_start_date").val())).getTime();
            if(_common.judgeValidDate($("#rd_start_date").val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#rd_start_date").css("border-color","red");
                $("#rd_start_date").focus();
                return false;
            }else {
                $("#rd_start_date").css("border-color","#CCCCCC");
            }
        }
        let _EndDate = null;
        if(!$("#rd_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#rd_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#rd_end_date").val())).getTime();
            if(_common.judgeValidDate($("#rd_end_date").val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#rd_end_date").css("border-color","red");
                $("#rd_end_date").focus();
                return false;
            }else {
                $("#rd_end_date").css("border-color","#CCCCCC");
            }
        }
        if(_StartDate>_EndDate){
            $("#rd_end_date").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#rd_end_date").focus();
            return false;
        }

        let _deliveryStartDate = 0;
        if($("#delivery_start_date").val()){
            _deliveryStartDate = new Date(fmtDate($("#delivery_start_date").val())).getTime();
            if (_common.judgeValidDate($("#delivery_start_date").val())) {
                _common.prompt("Please enter a valid date!", 3, "info");
                $("#delivery_start_date").css("border-color", "red");
                $("#delivery_start_date").focus();
                return false;
            } else {
                $("#delivery_start_date").css("border-color", "#CCC");
            }
        }
        let _deliveryEndDate = 0;
        if($("#delivery_end_date").val()){
            _deliveryEndDate = new Date(fmtDate($("#delivery_end_date").val())).getTime();
            if (_common.judgeValidDate($("#delivery_end_date").val())) {
                _common.prompt("Please enter a valid date!", 3, "info");
                $("#delivery_end_date").css("border-color", "red");
                $("#delivery_end_date").focus();
                return false;
            } else {
                $("#delivery_end_date").css("border-color", "#CCC");
            }
        }
        if($("#delivery_start_date").val() && $("#delivery_end_date").val()){
            if(_deliveryStartDate>_deliveryEndDate){
                $("#delivery_end_date").focus();
                _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
                return false;
            }
            let deliveryDifValue = parseInt(Math.abs((_deliveryEndDate-_deliveryStartDate)/(1000*3600*24)));
            if(deliveryDifValue>62){
                _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
                $("#delivery_end_date").focus();
                return false;
            }
        }

        return true;
    }

    //拼接检索参数
    var setParamJson = function(){
        var dc = $('#query_type_val').val().trim();
        // 创建请求字符串
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            orderId : m.order_id.val().trim(),
            deliveryOrderId : $("#deliveryOrderId").val(),
            deliveryStartDate : fmtIntDate(String(m.delivery_start_date.val())),
            deliveryEndDate : fmtIntDate(String(m.delivery_end_date.val())),
            receiveStartDate : fmtIntDate(String(m.rd_start_date.val())),
            receiveEndDate : fmtIntDate(String(m.rd_end_date.val())),
            dc: dc,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //画面按钮点击事件
    var but_event = function(){

        //订货开始日
        m.delivery_start_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        }).on('changeDate', function (ev) {
            if (ev.date) {
                m.delivery_end_date.datetimepicker('setStartDate', new Date(ev.date.valueOf()))
            } else {
                m.delivery_end_date.datetimepicker('setStartDate', null);
            }
        });
        //结束日
        m.delivery_end_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        }).on('changeDate', function (ev) {
            if (ev.date) {
                m.delivery_start_date.datetimepicker('setEndDate', new Date(ev.date.valueOf()))
            } else {
                m.delivery_start_date.datetimepicker('setEndDate', new Date());
            }
        });

        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/query");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });
        m.reset.on("click",function(){
            $("#regionRemove").click();
            $("#deliveryOrderId").val("");
            m.rd_start_date.val("");
            m.rd_end_date.val("");
            m.order_id.val("");
            m.delivery_start_date.val("");
            m.delivery_end_date.val("");
            m.query_type_val.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
    }

    // 选项选中事件
    var trClick_table1 = function(){
        var cols = tableGrid.getSelectColValue(selectTrTemp,"orderId");
        tempTrOrderId = cols["orderId"];
    }

    //表格初始化-配送差异查询及调整一览样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["PO No.","Delivery Order Id","Delivery Date","Last Date Received","Last Received No.","Store No.","Store Name","DC No.","DC Name","(Ordered-DC Picked) Qty","(DC Picked-Received)Qty"],
            colModel:[
                {name:"orderId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"deliveryOrderId",type:"text",text:"right",width:"130",ishide:true,css:""},
                {name:"deliveryDate",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"receiveDate",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"receiveId",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"80",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"160",ishide:false,css:""},
                {name:"deliveryCenterId",type:"text",text:"right",width:"160",ishide:false,css:""},
                {name:"deliveryCenterName",type:"text",text:"left",width:"160",ishide:false,css:""},
                {name:"totalAmt2",type:"text",text:"right",width:"150",ishide:false,getCustomValue:getThousands},
                {name:"totalAmt",type:"text",text:"right",width:"150",ishide:false,getCustomValue:getThousands},
            ],//列内容
            // traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                return trObj;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                return self;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                trClick_table1();
            },
            buttonGroup:[
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""//,
                },//查看
                {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                },
            ],

        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#view").remove();
        }
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
    }

    //小数格式化
    var floatFmt = function(tdObj, value){
        if(value!=null&&value.trim()!==''){value = parseFloat(value);}
        return $(tdObj).text(value);
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtStringDate(value);
        }
        return $(tdObj).text(value);
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date){
        if(!!date) {
            return date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        }
    }
    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
        }
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('shoppingOrderDifference');
_start.load(function (_common) {
    _index.init(_common);
});
