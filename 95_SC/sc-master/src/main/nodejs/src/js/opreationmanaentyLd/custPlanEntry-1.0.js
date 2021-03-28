require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('custPlanEntry', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        approvalRecordsParamGrid = null,
        tempTrObjValue = {},//临时行数据存储
        systemPath=null,
        tableGrid=null;
    const KEY = 'COST_ITEM_ADJUSTMENT_MANAGEMENT';
    var m = {
        toKen : null,
        use : null,
        selectTrObj : null,
        error_pcode : null,
        identity : null,
        pd_start_date:null,
        pd_end_date:null,
        clear_pd_date:null,
        search : null,//检索按钮
        main_box : null,//检索按钮
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        reset : null,
        checkResources:null,
        searchJson:null,
        pd_cd:null,
        pd_status:null,
        enterFlag:null,
        piCdParam:null,
        dj_status:null, // 审核状态
        //审核
        typeId:null,
        reviewId:null
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/custEntry";
        systemPath=_common.config.surl;
        but_event();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        initTable1();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.pd_start_date,m.pd_end_date);
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

        // 日期格式转换
        m.pd_start_date.val(obj.piStartDate);
        m.pd_end_date.val(obj.piEndDate);
        m.pd_status.val(obj.piStatus);
        m.pd_cd.val(obj.piCd);
        m.dj_status.val(obj.reviewSts);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",systemPath+"/custPlanEntry/inquire");
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
        obj.piStartDate=m.pd_start_date.val();
        obj.piEndDate=m.pd_end_date.val();
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    var table_event = function(){
        //进入新增
        $("#addPlan").on("click",function(){
            saveParamToSession();
            top.location = url_left+"/edit";
        });
        //进入修改
        $("#updatePlan").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,storeCd,storeName,createUserName,createYmd");
            if(cols==null){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                //获取数据审核状态
                _common.getRecordStatus(cols['piCd'],m.typeId.val(),function (result) {
                    if (result.success){
                        saveParamToSession();
                        top.location = url_left+"/edit?identity="+m.identity.val()+"&enterFlag=update&piCdParam="+cols['piCd']+"&piDateParam="+cols['createYmd']+"&storeCd="+cols['storeCd']+"&storeName="+cols['storeName']+"&createBy="+cols['createUserName'];
                    }else{
                        _common.prompt(result.message,5,"error");
                    }
                },m.reviewId.val())
            }
        });

        $("#printPlan").on("click",function(){
            if(verifySearch()) {
                setParamJson();
                var url = url_left + "/print?searchJson=" + m.searchJson.val();
                window.open(encodeURI(url), "excelprint", "top=10,height=600,width=2000px,toolbar=no,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no,center:yes");
            }
        });

        $("#exportFiles").on("click",function(){
            if(verifySearch()){
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                var url = url_left +"/excel?"+ paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
            }
        });
        $("#importFiles").on("click",function(){
            //setParamJson();
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });

        $("#viewPlan").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,storeCd,storeName,createUserName,createYmd");
            if(cols==null){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                saveParamToSession();
                top.location = url_left+"/edit?identity="+m.identity.val()+"&enterFlag=view&piCdParam="+cols['piCd']+"&piDateParam="+cols['createYmd']+"&storeCd="+cols['storeCd']+"&storeName="+cols['storeName']+"&createBy="+cols['createUserName'];
            }
        });

        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd");
            approvalRecordsParamGrid = "id="+cols["piCd"]+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });
    }

    //画面按钮点击事件
    var but_event = function(){
        //清空日期
        m.clear_pd_date.on("click",function(){
            m.pd_start_date.val("");
            m.pd_end_date.val("");
        });

        m.reset.on('click',function () {
            $("#regionRemove").click();
            m.pd_start_date.val("");
            m.pd_end_date.val("");
            m.pd_cd.val('');
            m.dj_status.val('');
            m.searchJson.val('');
            selectTrTemp = null;
            _common.clearTable();
        });

        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",systemPath+"/custPlanEntry/inquire");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
    }

    function judgeNaN (value) {
        return value !== value;
    }

    //验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#pd_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#pd_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#pd_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#pd_start_date").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#pd_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#pd_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#pd_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#pd_end_date").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#pd_end_date").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#pd_end_date").focus();
            return false;
        }
        return true;
    }

    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g,'');
        var day = dateStr.substring(0,2);
        var month = dateStr.substring(2,4);
        var year = dateStr.substring(4,8);
        return year+month+day;
    }

    //拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        var _startDate = formatDate(m.pd_start_date.val())||null;
        var _endDate = formatDate(m.pd_end_date.val())||null;
        var piStatus = m.pd_status.val();
        // 创建请求字符串
        var searchJsonStr ={
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'piStartDate':_startDate,
            'piEndDate':_endDate,
            'piCd':m.pd_cd.val().trim(),
            'piStatus':piStatus,
            'reviewSts':m.dj_status.val()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //表格初始化-费用录入样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Store No.","Store Name","Document No.","Status","Remarks","Created by","Date Created"],
            colModel:[
                {name:"storeCd",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"piCd",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"reviewSts",type:"text",text:"left",width:"130",ishide:false,getCustomValue:getStatus},
                {name:"remarks",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"createUserName",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"createYmd",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},
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
                    butType: "add",
                    butId: "addPlan",
                    butText: "Add",
                    butSize: ""
                },
                {
                    butType: "update",
                    butId: "updatePlan",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType: "view",
                    butId: "viewPlan",
                    butText: "View",
                    butSize: ""
                },
                {
                    butType:"custom",
                    butHtml:"<button id='exportFiles' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                },
                // {
                //     butType: "upload",
                //     butId: "importFiles",
                //     butText: "Import Stocktake",
                //     butSize: "",
                // },
                {
                    butType:"custom",
                    butHtml:"<button id='printPlan' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print Actual Stocktake Files</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                }
            ],
        });

        //审核记录
        approvalRecordsTableGrid = $("#approvalRecordsTable").zgGrid({
            title:"Approval Records",
            param:approvalRecordsParamGrid,
            colNames:["Approval Type","Result","User Code","User Name","Date Time","Comments"],
            colModel:[
                {name:"typeName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"result",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"userCode",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"userName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"dateTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"comments",type:"text",text:"left",width:"200",ishide:false,css:""},
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            sidx:"",//排序字段
            sord:"",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            }
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var addBut = $("#addBut").val();
        if (Number(addBut) != 1) {
            $("#addPlan").remove();
        }
        var editBut = $("#editBut").val();
        if (Number(editBut) != 1) {
            $("#updatePlan").remove();
        }
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#viewPlan").remove();
        }
        var exportBut = $("#exportBut").val();
        if (Number(exportBut) != 1) {
            $("#exportFiles").remove();
        }
        var importBut = $("#importBut").val();
        if (Number(importBut) != 1) {
            $("#importFiles").remove();
        }
        var printBut = $("#printBut").val();
        if (Number(printBut) != 1) {
            $("#printPlan").remove();
        }
        var approvalBut = $("#approvalBut").val();
        if (Number(approvalBut) != 1) {
            $("#approvalRecords").remove();
        }
    }

    // 获取审核状态
    var getStatus = function(tdObj, value){
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
                value = "Received";
                break;
            case "30":
                value = "Paid";
                break;
            default:
                value = "";
        }
        return $(tdObj).text(value);
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }

    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        if(date!=null&&date!='')
            res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    //格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('custPlanEntry');
_start.load(function (_common) {
    _index.init(_common);
});
