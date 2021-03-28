require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('auditRejected', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        _params = [],//撤销临时数据储存
        common=null;
    var m = {
        toKen: null,
        main_box:null,
        reset: null,
        search: null,
        typeId : null,//审核类型
        userCode : null,//用户编号
        userName : null,//用户名称
        startDate : null,//开始日期
        endDate : null,//结束日期
        clear_date : null,//清空日期
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left =_common.config.surl+"/auditMessage";
        systemPath = _common.config.surl;
        //初始化检索部分内容
        initSearchArea();
        //事件绑定
        but_event();
        //列表初始化
        initTable();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
    }

    // 初始化检索部分内容
    var initSearchArea = function(){
        m.clear_date.on("click",function(){
            m.startDate.val("");
            m.endDate.val("");
        });
        //初始化审核类型下拉
        $.myAjaxs({
            url:systemPath+"/auditMessage/getApprovalType",
            async:true,
            cache:false,
            type :"post",
            data :"",
            dataType:"json",
            success:function(result){
                m.typeId.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].k;
                    var optionText = result[i].v;
                    m.typeId.append(new Option(optionValue+" "+optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt("Failed to load data!",5,"error");
            },
            complete:_common.myAjaxComplete
        });
    }
    //验证检索项是否合法
    var verifySearch = function(){
        if(m.startDate.val()!=""&&m.endDate.val()!="") {
            var _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
            var _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
            var difValue = parseInt(Math.abs((_EndDate - _StartDate) / (1000 * 3600 * 24)));
            if (difValue > 62) {
                _common.prompt("Query Period cannot exceed 62 days!", 5, "error"); // 日期期间取值范围不能大于62天
                $("#endDate").focus();
                return false;
            }
        }
        return true;
    }

    //画面按钮点击事件
    var but_event = function(){
        //重置搜索
        m.reset.on("click",function(){
            m.userCode.val("");
            m.userName.val("");
            m.typeId.val("");
            m.startDate.val("");
            m.endDate.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()) {
                paramGrid = "userCode=" + m.userCode.val().trim() +
                    "&userName=" + m.userName.val().trim() +
                    "&typeId=" + m.typeId.val().trim() +
                    "&startDate=" + subfmtDate(m.startDate.val()) +
                    "&endDate=" + subfmtDate(m.endDate.val());
                tableGrid.setting("url", url_left + "/getRejectedApprovals");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });

    }

    //表格按钮事件
    var table_event = function(){
        //跳转详细页面
        m.main_box.on("click","a[class*='url']",function(){
            var url = $(this).parent().parent().find("td[tag='url']").text();
            var typeId = $(this).parent().parent().find("td[tag='typeId']").text();
            var recordCd = $(this).parent().parent().find("td[tag='recordCd']").text();
            if(url.trim()==""||typeId.trim()==""||recordCd.trim()==""){
                _common.prompt("Parameters are missing!",5,"error");
                return false;
            }
            top.location = url_left+"/auditSkip?url="+url+"&typeId="+typeId+"&recordCd="+recordCd;
        });
        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"recordCd,typeId");
            approvalRecordsParamGrid = "id="+cols["recordCd"]+
                "&typeIdArray="+cols["typeId"];
            approvalRecordsTableGrid.setting("url",systemPath+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });
    }

    //表格初始化-我提交被驳回审核样式
    var initTable = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Rejected Approvals List",
            param:paramGrid,
            colNames:["Approval Type Id","Url","Approval Type","Status","Status","Document No.","Submitter","Created By","Date Created"],
            colModel:[
                {name:"typeId",type:"text",text:"right",width:"100",ishide:true,css:""},
                {name:"url",type:"text",text:"left",width:"100",ishide:true,css:""},
                {name:"typeName",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:urlFmt},
                {name:"status",type:"text",text:"left",width:"130",ishide:true,css:""},
                {name:"statusName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"recordCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                // 隐藏
                {name:"createUserId",type:"text",text:"right",width:"130",ishide:true,css:""},

                {name:"createUserName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"auditTime",type:"text",text:"center",width:"130",ishide:false,css:""}
            ],//列内容
            width:"max",//宽度自动
            isPage:true,//是否需要分页
            page:1,//当前页
            sidx:"",//排序字段
            sord:"",//升降序
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
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
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                }
            ]
        });

        //审核记录
        approvalRecordsTableGrid = $("#approvalRecordsTable").zgGrid({
            title:"Approval Records",
            param:approvalRecordsParamGrid,
            localSort:true,
            colNames:["Approval Type","Result","User Code","User Name","Date Time","Comments"],
            colModel:[
                {name:"typeName",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"result",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"userCode",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"userName",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"dateTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"comments",type:"text",text:"center",width:"200",ishide:false,css:""},
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
        var rejectedButView = $("#rejectedButView").val();
        if (Number(rejectedButView) != 1) {
            $("#approvalRecords").remove();
        }
    }

    var urlFmt = function(tdObj, value){
        var html = '<a href="javascript:void(0);" title="'+value+'" class="url" "><span class="glyphicon glyphicon-expand icon-right"></span>'+value+'</a>';
        return $(tdObj).html(html);
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
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

    function subfmtDate(date){
        var res = "";
        if(date!=null&&date!=""){
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
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
var _index = require('auditRejected');
_start.load(function (_common) {
    _index.init(_common);
});