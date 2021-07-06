require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('mySubmissions', function () {
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
        documentNo:null,
        approvalStatus:null,
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
        //审核事件
        approval_event();
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
        initSelectOptions("Aprroval Status", "approvalStatus", "00831");
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
                    "&status="+m.approvalStatus.val()+
                    "&recordCd="+m.documentNo.val().trim()+
                    "&startDate=" + subfmtDate(m.startDate.val()) +
                    "&endDate=" + subfmtDate(m.endDate.val());
                tableGrid.setting("url", url_left + "/getSubmissionsApprovals");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });

    }

    //表格按钮事件
    var table_event = function(){
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

    var approval_event = function () {
        //点击撤销按钮
        $("#withdraw").on("click",function(){
            var _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length == 0){
                _common.prompt("Please select at least one row of data!",5,"error");/*请至少选择一行数据数据*/
                return false;
            }
            _params = [];
            var withdrawStatus = true;
            for(var i = 0; i < _list.length; i++){
                var cols = tableGrid.getSelectColValue(_list[i],"recordCd,typeId,status");
                let typeId = cols['typeId'];
                let recordCd = cols['recordCd'];
                let status = cols['status'];
                if(status!="1"){
                    _common.prompt("Cannot select data other than Pending!",5,"info");
                    withdrawStatus = false;
                    return ;
                }
                var _param = {
                    recordCd:recordCd,
                    typeId:typeId,
                    status:status
                };
                _params.push(_param);
            }
            if(!withdrawStatus){
                return ;
            }
            if(_params.length < 1){
                _common.prompt("No Matching!",5,"info");
                return;
            }
            $("#auditContent").val("");
            $("#approval_dialog").modal("show");
            $("#audit_affirm").prop("disabled",false);
        });
        //撤销提交
        $("#audit_affirm").on("click",function () {
            //撤销数据
            var params = JSON.stringify(_params);
            //审核意见
            var auditContent = $("#auditContent").val();
            if(auditContent.length>200){
                _common.prompt("Approval comments cannot exceed 200 characters!",5,"error");
                return false;
            }
            //审核记录id
            // var auditStepId = $("#auditId").val();
            //用户id
            var auditUserId = $("#auditUserId").val();
            //审核结果
            var auditStatus = $("#auditStatusGroup").find("input[type='radio']:checked").val();

            _common.myConfirm("Are you sure to save?", function (result) {
                if (result != "true") {
                    return false;
                }
                $("#audit_affirm").prop("disabled",true);
                $.myAjaxs({
                    url: url_left + "/batchAudit",
                    async: true,
                    cache: false,
                    type: "post",
                    data: {
                        auditData: params,
                        auditUserId: auditUserId,
                        auditStatus: auditStatus,
                        auditContent: auditContent,
                        toKen: m.toKen.val()
                    },
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            m.search.click();
                            $("#approval_dialog").modal("hide");
                            _common.prompt("Saved Successfully!", 3, "success");// 保存审核信息成功
                        } else {
                            _common.prompt("Saved Failure!", 5, "error");// 保存审核信息失败
                        }
                        m.toKen.val(result.toKen);
                    },
                    complete: _common.myAjaxComplete
                });
            })
        })
    }

    //表格初始化-我的提交审核样式
    var initTable = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"My Submissions List",
            param:paramGrid,
            colNames:["Approval Type Id","Url","Approval Type","Status","Status","Document No.","Submitter","Created By","Date Created"],
            colModel:[
                {name:"typeId",type:"text",text:"left",width:"100",ishide:true,css:""},
                {name:"url",type:"text",text:"left",width:"100",ishide:true,css:""},
                {name:"typeName",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:urlFmt},
                {name:"status",type:"text",text:"left",width:"130",ishide:true,css:""},
                {name:"statusName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"recordCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                /*隐藏 start */
                {name:"createUserId",type:"text",text:"right",width:"130",ishide:true,css:""},

                {name:"createUserName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"auditTime",type:"text",text:"center",width:"130",ishide:false,css:""}
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            sidx:"",//排序字段
            sord:"",//升降序
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            isCheckbox:true,
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
                    butType: "review",
                    butId: "withdraw",
                    butText: "Withdraw",
                    butSize: ""
                },{
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
        var withdrawBut = $("#withdrawBut").val();
        if (Number(withdrawBut) != 1) {
            $("#withdraw").remove();
        }
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
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
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:false,
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
                _common.prompt(title+" Failed to load data!",5,"error");
            }
        });
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
var _index = require('mySubmissions');
_start.load(function (_common) {
    _index.init(_common);
});