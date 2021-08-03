require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receipt', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        selectTrTemp = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        main_box : null,
        // 检索栏位
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        orderMethod : null,
        voucherType : null,
        voucherSts : null,
        orderStartDate : null,
        orderEndDate : null,
        orgOrderStartDate : null,
        orgOrderEndDate : null,
        voucherNo : null,
        orgVoucherNo : null,
        vendorId : null,
        // 按钮
        search:null,
        reset:null,
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
        url_left = url_root + "/rrVoucherQuery";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // 首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 初始化下拉
        getSelectValue();
        // 表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // m.search.click();
        // 初始化检索日期
        _common.initDate(m.orderStartDate,m.orderEndDate);
    }

    // 表格内按钮事件
    var table_event = function(){
        // 查看按钮
        $("#view").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select the data to view!",3,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(selectTrTemp,"orderType,orderDifferentiate,storeCd,orderId");
            let _type = cols["orderType"];
            let _differentiate = cols["orderDifferentiate"];
            let _url = null;
            if(_type == '0'){
                if(_differentiate == '0'){
                    // 供应商退货
                    _url = url_root + "/returnVendor/cancelOrderDetail?flag=0&orderId="+cols["orderId"];
                }else if(_differentiate == '1'){
                    // DC退货
                    _url = url_root + "/returnWarehouse/cancelOrderDetail?flag=0&orderId="+cols["orderId"];
                }
            }else if(_type == '1'){
                if(_differentiate == '0'){
                    // 供应商收货
                    _url = url_root + "/vendorReceipt/view?type=1&receiveId="+cols["orderId"];
                }else if(_differentiate == '1'){
                    // DC收货
                    _url = url_root + "/receipt/view?type=1&receiveId="+cols["orderId"];
                }
            }
            if(_url == null){
                _common.prompt("Failed to get parameters, please refresh!",3,"info");
                return false;
            }
            top.location = _url;
        })

        // 详修改按钮
        $("#update").on("click",function(){
            //验证当前batch处理是否禁用
            let batchFlg = false;
            _common.batchCheck(function (result) {
                if(!result.success){//禁用
                    _common.prompt(result.message,5,"info");
                    batchFlg = false;
                }else{
                    batchFlg = true;
                }
            })
            if(!batchFlg){return batchFlg};
            if(selectTrTemp==null){
                _common.prompt("Please select the data to edit!",3,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(selectTrTemp,"orderType,orderDifferentiate,orderDate,storeCd,orderId,orgOrderId,reviewSts,status");
            let _type = cols["orderType"];
            let _differentiate = cols["orderDifferentiate"];
            let _url = null;
            if(_type == '0'){
                let _typeId = 0;
                if(_differentiate == '0'){
                    // 供应商退货
                    _typeId = 8;
                }else if(_differentiate == '1'){
                    // DC退货
                    _typeId = 7;
                }
                // 获取数据审核状态
                getRecordStatus(cols["orderId"],cols["status"], _typeId,function (result) {
                    if(result.data=='5' || result.data=='10'){
                        // 验证退货日是否为当前业务日
                        // _common.checkBusinessDate(subfmtDate(cols["orderDate"]),null,function (result) {
                        //     if(result.success){
                                if(_differentiate == '0'){
                                    // 供应商退货
                                    top.location = url_root + "/returnVendor/cancelOrderDetail?flag=1&orderId="+cols["orderId"];
                                }else if(_differentiate == '1'){
                                    // DC退货
                                    top.location = url_root + "/returnWarehouse/cancelOrderDetail?flag=1&orderId="+cols["orderId"];
                                }else{
                                    _common.prompt("Failed to get parameters, please refresh!",3,"info");
                                    return false;
                                }
                        //     }else{
                        //         _common.prompt("Selected document is not created within today and cannot be modified!",3,"info");
                        //         return false;
                        //     }
                        // })
                    }else if(result.data=='20'){
                        _common.prompt("The document has been Returned/Received!",3,"info");
                        return false;
                    }else{
                        _common.prompt(result.message,3,"info");
                        return false;
                    }
                });
            }else if(_type == '1'){
                if(cols["reviewSts"] == '15'){
                    // 验证收货日是否为当前业务日
                    // _common.checkBusinessDate(subfmtDate(cols["orderDate"]), cols["orderId"],function (result) {
                    //     if(result.success){
                            if(_differentiate == '0'){
                                console.log('供应商收货');
                                // 供应商收货
                                top.location = url_root + "/vendorReceipt/details?type=1&storeCd="+cols["storeCd"]+"&orderId="+cols["orgOrderId"]+"&receiveId="+cols["orderId"];
                            }else if(_differentiate == '1'){
                                console.log('DC收货');
                                // DC收货
                                top.location = url_root + "/receipt/details?type=1&storeCd="+cols["storeCd"]+"&orderId="+cols["orgOrderId"]+"&receiveId="+cols["orderId"];
                            }else{
                                console.log('unknown');
                                _common.prompt("Failed to get parameters, please refresh!",3,"info");
                                return false;
                            }
                    //     }else{
                    //         _common.prompt("Selected document is not created within today and cannot be modified!",3,"info");
                    //         return false;
                    //     }
                    // })
                }else if(cols["reviewSts"] == '20'){
                    _common.prompt("The document has been Returned/Received!",3,"info");
                    return false;
                }else{
                    _common.prompt("Document in this state cannot be edited!",3,"info");
                    return false;
                }
            }
        })

        // 打印功能
        $("#print").on("click",function(){
            if(verifySearch()) {
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "print", "width=1500,height=600,scrollbars=yes");
            }
        });

        // 导出功能
        $("#outExcel").on("click",function(){
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
                initTable();// 列表初始化
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

    // 画面按钮点击事件
    var but_event = function(){
        // 原订单日期
        m.orgOrderStartDate.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        // 结束日
        m.orgOrderEndDate.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });

        // 清空按钮
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.orderMethod.val("");
            m.voucherType.val("");
            m.voucherSts.val("");
            m.orderStartDate.val("");
            m.orderEndDate.val("");
            m.orgOrderStartDate.val("");
            m.orgOrderEndDate.val("");
            m.vendorId.val("");
            m.voucherNo.val("");
            m.orgVoucherNo.val("");
            $("#amRemove").click();
            $("#omRemove").click();
            $("#ocRemove").click();
            selectTrTemp = null;
            _common.clearTable();
        });

        // 检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/getList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#orderStartDate").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#orderStartDate").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#orderStartDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#orderStartDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#orderEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#orderEndDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#orderEndDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#orderEndDate").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            $("#orderEndDate").focus();
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue >62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#orderEndDate").focus();
            return false;
        }
        let _oStartDate = null, _oEndDate = null;
        if(m.orgOrderStartDate.val()){
            _oStartDate = new Date(fmtDate($("#orgOrderStartDate").val())).getTime();
            if(judgeNaN(_oStartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#orgOrderStartDate").focus();
                return false;
            }
        }
        if(m.orgOrderEndDate.val()){
            _oEndDate = new Date(fmtDate($("#orgOrderEndDate").val())).getTime();
            if(judgeNaN(_oEndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#orgOrderEndDate").focus();
                return false;
            }
        }
        if(_oStartDate && _oEndDate){
            if(_oStartDate>_oEndDate){
                _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
                $("#orgOrderEndDate").focus();
                return false;
            }
            difValue = parseInt(Math.abs((_oEndDate-_oStartDate)/(1000*3600*24)));
            if(difValue >62){
                _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
                $("#orgOrderEndDate").focus();
                return false;
            }
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        let _startDate = subfmtDate(m.orderStartDate.val())||null;
        let _endDate = subfmtDate(m.orderEndDate.val())||null;
        let _oStartDate = subfmtDate(m.orgOrderStartDate.val())||null;
        let _oEndDate = subfmtDate(m.orgOrderEndDate.val())||null;
        // 创建请求字符串
        let searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            orderMethod : m.orderMethod.val().trim(),
            orderType : m.voucherType.val().trim(),
            orderSts : m.voucherSts.val().trim(),
            orderStartDate : _startDate,
            orderEndDate : _endDate,
            orgOrderStartDate : _oStartDate,
            orgOrderEndDate : _oEndDate,
            vendorId : m.vendorId.val().trim(),
            orderId : m.voucherNo.val().trim(),
            orgOrderId : m.orgVoucherNo.val().trim(),
            omCode:$("#om").attr("k"),
            ocCode:$("#oc").attr("k"),
            amCode:$("#am").attr("k")
            // status : parseInt($("#status").val())
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //表格初始化-采购样式
    var initTable = function(){
        tableGrid = $("#zgGridTable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Type","Status","Differentiate","Document Date","Document No.","Original Document No.","Original Document Date","Document Type","Store Code",
                "Store Name",/*"Area Manager Code",*/"Area Manager Name",/*"Operation Manager Code",*/"Operation Manager Name",/*"Operation Controller Code",*/"Operation Controller Name","Vendor Code","Vendor Name","Order Method","Vat In(%)","Order Amount","Document Status","Document Status","Status"],
            colModel:[
                {name:"orderType",type:"text",text:"right",ishide:true},
                {name:"reviewSts",type:"text",text:"right",ishide:true},
                {name:"orderDifferentiate",type:"text",text:"right",ishide:true},
                {name:"orderDate",type:"text",text:"center",width:"110",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"orderId",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"orgOrderId",type:"text",text:"left",width:"170",ishide:false,css:""},
                {name:"orgOrderDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"orderType",type:"text",text:"left",width:"150",ishide:false,css:"",getCustomValue:getTypeName},
                {name:"storeCd",type:"text",text:"right",width:"90",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"180",ishide:false,css:""},
                // {name:"ofc",type:"text",text:"right",width:"90",ishide:false,css:""},
                {name:"ofcName",type:"text",text:"left",width:"180",ishide:false,css:""},
                // {name:"om",type:"text",text:"right",width:"90",ishide:false,css:""},
                {name:"omName",type:"text",text:"left",width:"180",ishide:false,css:""},
                // {name:"oc",type:"text",text:"right",width:"90",ishide:false,css:""},
                {name:"ocName",type:"text",text:"left",width:"180",ishide:false,css:""},
                {name:"warehouseCd",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"warehouseName",type:"text",text:"left",width:"180",ishide:false,css:""},
                {name:"orderMethodName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"taxRate",type:"text",text:"right",width:"90",ishide:false,css:"",getCustomValue:fmtRate},
                {name:"orderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                {name:"reviewSts",type:"text",text:"right",width:"120",ishide:true,css:""},
                {name:"reviewStsText",type:"text",text:"right",width:"160",ishide:false,css:""},
                {name:"status",type:"text",text:"right",width:"120",ishide:true,css:""}
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
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
                {
                    butType: "update",
                    butId: "update",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                },//查看
                {butType:"custom",butHtml:"<button id='outExcel' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"},
                {butType:"custom",butHtml:"<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"}
            ]
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#view").remove();
        }
        var confirmBut = $("#confirmBut").val();
        if (Number(confirmBut) != 1) {
            $("#update").remove();
        }
        var receiveBut = $("#receiveBut").val();
        if (Number(receiveBut) != 1) {
            $("#quickReceiving").remove();
        }
        var exportBut = $("#exportBut").val();
        if (Number(exportBut) != 1) {
            $("#outExcel").remove();
        }
        var printBut = $("#printBut").val();
        if (Number(printBut) != 1) {
            $("#print").remove();
        }
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 获取审核状态
    var getStatus = function(tdObj, value){
        switch (value) {
            case 1:
                value = "Return Request Pending";
                break;
            case 5:
                value = "Rejected";
                break;
            case 6:
                value = "Withdrawn";
                break;
            case 10:
                value = "Approved";
                break;
            case 15:
                value = "Receiving Pending";
                break;
            case 20:
                value = "Returned/Received";
                break;
            case 7:
                value = "Expired";
                break;
            default:
                value = "";
        }
        return $(tdObj).text(value);
    };

    // 获取单据类型名称
    function getTypeName(tdObj, value){
        let _value = "";
        switch (value) {
            case "0":
                _value = "[0] Return Document";
                break;
            case "1":
                _value = "[1] Receiving Document";
                break;
        }
        return $(tdObj).text(_value);
    }

    // 判断是否标识
    function isYesOrNo(tdObj, value){
        var _value = "No";
        switch (value) {
            case "1":
                _value = "Yes";
                break;
        }
        return $(tdObj).text(_value);
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
        value = fmtIntDate(value);
    }
    return $(tdObj).text(value);
}

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }
    // 格式化百分比
    function fmtRate(tdObj,value){
        if(value==null||value===""){
            return   $(tdObj).text("0%");
        }else {
            value=Number(accMul(value,100)).toFixed(2);

            var string = value.split(".")[0];
            return $(tdObj).text(string+"%");
        }
    }
    function accMul(arg1,arg2){
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    }
    // 日期处理
    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }

    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:url_root+"/cm9010/getCode",
            async:true,
            cache:false,
            type :"post",
            data :"codeValue="+code,
            dataType:"json",
            success:function(result){
                var selectObj = $("#" + selectId);
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+"Failed to load data!",5,"error");
            }
        });
    }

    /**
     * 获取审核状态
     * @param recordId 数据id
     * @param typeId 类型id
     * @param reviewId 流程id
     * @param callback
     */
    var getRecordStatus = function (recordId,status,typeId,callback,reviewId) {
        if(!reviewId){
            reviewId = 0;
        }
        $.myAjaxs({
            url:url_left+"/getRRVoucherRecordStatus",
            async:true,
            cache:false,
            type :"get",
            data :{
                recordId : recordId,
                status:parseInt(status),
                typeId : typeId,
                reviewId : reviewId
            },
            dataType:"json",
            success:function(result){
                //回调
                callback(result);
            },
            error : function(e){
                prompt("The request failed, Please try again!",5,"error");// 请求失败
            }
        });
    }

    // 请求加载下拉列表
    function getSelectValue(){

        am = $("#am").myAutomatic({
            url: url_root + "/ma1000/getAMByPM",
            ePageSize: 10,
            startCount: 0,
        });
        om = $("#om").myAutomatic({
            url: url_root + "/ma1000/getOM",
            ePageSize: 10,
            startCount: 0,
        });
        oc = $("#oc").myAutomatic({
            url: url_root + "/ma1000/getOC",
            ePageSize: 10,
            startCount: 0,
        });
        // 加载select
        initSelectOptions("Order Method","orderMethod", "00065");
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('receipt');
_start.load(function (_common) {
    _index.init(_common);
});
