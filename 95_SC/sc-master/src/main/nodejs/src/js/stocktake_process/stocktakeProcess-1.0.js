require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("jqprint");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receipt', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        dataForm=[],
        systemPath='';
    const KEY = 'STOCKTAKING_VARIANCE_REPORT';
    var m = {
        use : null,
        item_name : null,
        tableGrid : null,
        selectTrObj : null,
        error_pcode : null,
        identity : null,
        pi_cd: null,
        dpt: null,
        pi_start_date:null,
        pi_end_date:null,
        reviewStatus:null,
        clear_pi_date:null,
        search : null,//检索按钮
        main_box : null,//检索按钮
        print : null,
        printHtml : null,
        search_item_but : null,
        searchJson:null,
        reset : null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        typeId:null, //审核类型id
        reviewId: null,
        toKen: null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/stocktakeProcess";
        systemPath=_common.config.surl;
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 初始化店铺运营组织检索
        _common.initOrganization();
        getComboxData();
        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        initPageBytype("1");
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.pi_start_date,m.pi_end_date);
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
        // 日期格式转换
        m.pi_start_date.val(obj.piStartDate);
        m.pi_end_date.val(obj.piEndDate);
        m.reviewStatus.val(obj.reviewStatus);
        m.pi_cd.val(obj.piCd);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left + "/search");
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
        obj.piStartDate=m.pi_start_date.val();
        obj.piEndDate=m.pi_end_date.val();
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    var table_event = function(){
        //进入查看
        $("#viewStock").on("click", function (e) {
            if (selectTrTemp==null||selectTrTemp=='') {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            saveParamToSession();
            let cols=tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate");
            top.location = url_left+'/edit?identity='+m.identity.val()+'&piCdParam='+cols['piCd']+'&piDateParam='+formatDate(cols['piDate']);
        });

        $("#print").on("click",function(){
            if(verifySearch()) {
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "excelprint", "width=1300,height=500,scrollbars=yes");
            }
        });

        // 点击后提交发起审核
        $("#submitPlan").on("click",function () {
            if (_result.data) {
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,storeCd,piStartTime,piEndTime");

            let piDate = formatDate(cols['piDate']);
            let businessDate = $("#businessDate").val();
            // let businessDate = new Date().Format('yyyyMMdd');

            if (piDate != businessDate) {
                // 请在盘点当天完成
                _common.prompt("Please complete the entry on the day of stocktaking!",5,"error");
                return;
            }

            let piStartTime = formatTime(cols["piStartTime"]);
            let piEndTime = formatTime(cols["piEndTime"]);
            let nowTime = new Date().Format("hhmmss");

            if (nowTime < piStartTime ||
                nowTime > piEndTime) {
                // 请在盘点时间内操作
                _common.prompt("Please operate within the stocktake time( "+cols["piStartTime"]+"~"+cols["piEndTime"]+" )!",5,"error");
                return;
            }

            // 判断日结状态
            let flg = _common.getValByKey('0005');
            if (flg=='1') {
                _common.prompt("Daily settlement, cannot stocktaking!",5,"error");
                return;
            }

            // 确定要提交吗？审核过程中不能修改！
            _common.myConfirm("Are you sure you want to submit? Cannot be modified during approval!",function(result) {
                if (result == "true") {
                    //发起审核
                    var typeId =m.typeId.val();
                    var	nReviewid =m.reviewId.val();
                    var	recordCd = cols['piCd'];
                    var storeCd = cols['storeCd'];
                    _common.initiateAudit(storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
                        m.toKen.val(token);
                        m.search.click();
                        synchronizedEndTime(recordCd,piDate,storeCd);
                    })
                }
            })
        });

        $("#export").on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });
        var table_ck_all = $("#table_ck_all");
        table_ck_all.on("change",function(event){
            event.preventDefault();
            var ck = $(this).prop("checked");
            $("#zgGridTtable_tbody").find("input[type='checkbox']").prop("checked",ck);
        });
        $("#viewPlan").on("click",function(){
            var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
            if(selectObj==null||selectObj.length==0){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                checkOrRejectClick(1,null,selectObj);
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

    // 同步盘点结束时间
    var synchronizedEndTime = function (piCd,piDate,storeCd) {
        $.myAjaxs({
            url:systemPath+"/stocktakeEntry/modifyEndTime",
            async:true,
            cache:false,
            type :"post",
            data:"piCd="+piCd+"&piDate="+piDate+"&storeCd="+storeCd,
            dataType:"json",
            success:function(result){

            }
        });
    }

    // 17:05:16 -> 170516
    var formatTime = function (str) {
        return str.replace(/:/g,'');
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

    //画面按钮点击事件
    var but_event = function(){
        m.reset.on('click',function () {
            m.pi_start_date.val('');
            m.pi_end_date.val('');
            m.pi_cd.val('');
            m.reviewStatus.val('');
            $("#regionRemove").click();
            selectTrTemp = null;
            _common.clearTable();
        });
        //清空日期
        m.clear_pi_date.on("click",function(){
            m.pi_start_date.val("");
            m.pi_end_date.val("");
        });

        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()) {
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/search");
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
        if(!$("#pi_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#pi_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#pi_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#pi_start_date").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#pi_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#pi_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#pi_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#pi_end_date").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#pi_end_date").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!!",3,"info"); // 日期期间取值范围不能大于62天
            $("#pi_end_date").focus();
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

    // 初始化参数下拉
    var getComboxData = function () {
        // 获取审核状态
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:false,
            cache:false,
            type :"post",
            data :"codeValue=00430",
            dataType:"json",
            success:function(result){
                var selectObj = $("#reviewStatus");
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    if(optionValue == '20'){
                        continue;
                    }
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
        });
    }

    //拼接检索参数
    var setParamJson = function(){
        let piCd = m.pi_cd.val().trim();
        let piStartDate = formatDate(m.pi_start_date.val());
        let piEndDate = formatDate(m.pi_end_date.val());
        let reviewStatus = m.reviewStatus.val();
        // 创建请求字符串
        var searchJsonStr ={
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'piCd':piCd,
            'piStartDate':piStartDate,
            'piEndDate':piEndDate,
            'reviewStatus':reviewStatus,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
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
            default:
                value = "";
        }
        return $(tdObj).text(value);
    }

    //表格初始化-采购样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Stocktaking Plan",
            param:paramGrid,
            localSort: true,
            colNames:["Date","Start Time","End Time","Document No.","Store No.","Store Name","Status","Document Type","Document Status","Remarks","Created by","Date Created","storeCd","piStatusCd"],
            colModel:[
                {name:"piDate",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piStartTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piEndTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"piStatusName",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"voucherTypeName",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"reviewStatus",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getStatus},
                {name:"remarks",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"createUserId",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"createYmd",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},

                {name:"storeCd",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:null},
                {name:"piStatusCd",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:null}
            ],//列内容
            traverseData:dataForm,
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
                let cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,piStatusCd");
                let status = cols["piStatusCd"];
                _common.getRecordStatus(cols['piCd'],m.typeId.val(),function (result) {
                    _result = result;
                    if (_result.data == null&&status!="01") {
                        $("#submitPlan").prop("disabled",false);
                    } else {
                        $("#submitPlan").prop("disabled",true);
                    }
                });
            },
            buttonGroup:[
                {
                    butType: "view",
                    butId: "viewStock",
                    butText: "View",
                    butSize: ""
                },
                {
                    butType:"custom",
                    butHtml:"<button id='submitPlan' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-ok'></span>Submit</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
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
                {name:"remarks",type:"text",text:"left",width:"200",ishide:false,css:""},
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
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#viewStock").remove();
        }
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
        var printButPm = $("#printButPm").val();
        if (Number(printButPm) != 1) {
            $("#print").remove();
        }
        var approvalRecordButPm = $("#approvalRecordButPm").val();
        if (Number(approvalRecordButPm) != 1) {
            $("#approvalRecords").remove();
        }
    }

    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g,'');
        var day = dateStr.substring(0,2);
        var month = dateStr.substring(2,4);
        var year = dateStr.substring(4,8);
        return year+month+day;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receipt');
_start.load(function (_common) {
    _index.init(_common);
});
