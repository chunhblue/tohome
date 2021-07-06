require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('myMessage', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
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
                    var optionText = result[i].k+' '+result[i].v;
                    m.typeId.append(new Option(optionText, optionValue));
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
        if(m.startDate.val()!==""&&m.startDate.val()!=null){
            if(_common.judgeValidDate(m.startDate.val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#startDate").focus();
                return false;
            }
        }
        if(m.endDate.val()!==""&&m.endDate.val()!=null){
            if(_common.judgeValidDate(m.endDate.val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#endDate").focus();
                return false;
            }
        }
        if(m.startDate.val()!=""&&m.endDate.val()!="") {
            var _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
            var _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
            if(_StartDate>_EndDate){
                _common.prompt("The start date cannot be greater than the end date!!", 5, "error"); //开始时间不能大于结束时间
                $("#endDate").focus();
                return false;
            }
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
                tableGrid.setting("url", url_left + "/getMyMessage");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });

    }

    //表格初始化-我的信息样式
    var initTable = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Messages List",
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
            ]
        });
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
var _index = require('myMessage');
_start.load(function (_common) {
    _index.init(_common);
});