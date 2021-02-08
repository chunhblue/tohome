require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("checkboxGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('fundQuery', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    const KEY = 'EXPENDITURE_MANAGEMENT';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson: null,
        // 查询参数
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        department : null,
        businessStartDate : null,
        businessEndDate : null,
        paymentType : null,
        expenditureSubject : null,
        expenditureCd : null,
        expenditureStatus : null,
        operator : null,
        status : null,
        typeId:null,//审核类型id
        // 按钮
        search : null,
        reset : null,
        hidStore:null,
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
        url_left = url_root + "/fundQuery";
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
        // 表格内按钮事件
        table_event();
        // // 初始化店铺运营组织检索
        _common.initOrganization();
        // 画面初始化加载
        initSelectValue();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.businessStartDate,m.businessEndDate);
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

        m.businessStartDate.val(obj.businessStartDate);
        m.businessEndDate.val(obj.businessEndDate);
        m.department.val(obj.department);
        m.paymentType.val(obj.paymentType);
        m.expenditureSubject.val(obj.expenditureSubject);
        m.expenditureCd.val(obj.expenditureCd);
        m.expenditureStatus.val(obj.expenditureStatus);
        m.operator.val(obj.operator);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/getList");
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
        obj.businessStartDate=m.businessStartDate.val();
        obj.businessEndDate=m.businessEndDate.val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 表格内按钮事件
    var table_event = function(){

        $("#storeRemove").on("click",function (e) {
            $.myAutomatic.cleanSelectObj(operator);
        })
        // 新增
        $("#add").on("click", function (e) {
            saveParamToSession();
            top.location = url_root + "/fundEntry";
        });

        // 修改
        $("#update").on("click", function (e) {
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length != 1){
                _common.prompt("Please select at least one row of data!",5,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(_list[0],"accDate,storeCd,voucherNo");
            let _date = '';
            if(cols["accDate"]!=null&&cols["accDate"]!=''){
                _date = fmtStringDate(cols["accDate"]);
            }
            let param = "storeCd=" + cols["storeCd"] + "&voucherNo=" + cols["voucherNo"]+
                "&accDate=" + _date;
            //获取数据审核状态
            _common.getRecordStatus(cols["voucherNo"],m.typeId.val(),function (result) {
                if(result.success) {
                    saveParamToSession();
                    top.location = url_root + "/fundEntry/edit?" + param;
                }else{
                    _common.prompt(result.message,5,"error");
                }
            });
        });

        // 查看
        $("#view").on("click", function (e) {
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length != 1){
                _common.prompt("Please select at least one row of data!",5,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(_list[0],"accDate,storeCd,voucherNo");
            let _date = '';
            if(cols["accDate"]!=null&&cols["accDate"]!=''){
                _date = fmtStringDate(cols["accDate"]);
            }
            let param = "storeCd=" + cols["storeCd"] + "&voucherNo=" + cols["voucherNo"]+
                "&accDate=" + _date;
            saveParamToSession();
            top.location = url_root + "/fundEntry/view?" + param;
        });

        // 删除
        $("#delete").on("click",function(){
            var _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length == 0){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var _params = [];
            for(var i = 0; i < _list.length; i++){
                var cols = tableGrid.getSelectColValue(_list[i],"accDate,storeCd,voucherNo");
                var _date = fmtStringDate(cols['accDate']);
                var _param ={
                    accDate:_date,
                    storeCd:cols['storeCd'],
                    voucherNo:cols['voucherNo']
                };
                _params.push(_param);
            }
            if(_params.length == 0){
                _common.prompt("Failed to get selected data!",5,"error");/*获取选中的数据失败*/
                return false;
            }
            var params = JSON.stringify(_params);
            _common.myConfirm("Are you sure you want to delete?",function(result){
                if(result=="true"){
                    $.myAjaxs({
                        url:url_root+"/fundEntry/del",
                        async:true,
                        cache:false,
                        type :"post",
                        data :"searchJson="+params,
                        dataType:"json",
                        success:function(re){
                            if(re.success){
                                m.search.click();
                                _common.prompt(re.msg,5,"success");
                            }else{
                                m.search.click();
                                _common.prompt(re.msg,5,"error");
                            }
                        },
                        error : function(e){
                            _common.prompt("Request failed!",5,"error");/*请求删除失败*/
                        }
                    });
                }
            })
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

        //审核记录
        $("#approvalRecords").on("click",function(){
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length != 1){
                _common.prompt("Please select at least one row of data!",5,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(_list[0],"voucherNo");
            approvalRecordsParamGrid = "id="+cols['voucherNo']+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable();//列表初始化
                isDisabledBtn();
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
        // 重置按钮点击事件
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.department.val("");
            m.businessStartDate.val("");
            m.businessEndDate.val("");
            m.paymentType.val("");
            m.expenditureSubject.val("");
            m.expenditureCd.val("");
            m.expenditureStatus.val("");
            m.operator.val("");
            m.searchJson.val("");
            selectTrTemp = null;
            _common.clearTable();
            isDisabledBtn();
        });
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#businessStartDate").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#businessStartDate").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#businessStartDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#businessStartDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#businessEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#businessEndDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#businessEndDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#businessEndDate").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            $("#businessEndDate").focus();
            return false;
        }
        var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue >62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#businessEndDate").focus();
            return false;
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        var _startDate = fmtStringDate(m.businessStartDate.val())||null;
        var _endDate = fmtStringDate(m.businessEndDate.val())||null;
        // 创建请求字符串
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            department:m.department.val().trim(),
            businessStartDate: _startDate,
            businessEndDate: _endDate,
            paymentType:m.paymentType.val().trim(),
            expenditureSubject:m.expenditureSubject.val().trim(),
            expenditureCd:m.expenditureCd.val().trim(),
            expenditureStatus:m.expenditureStatus.val().trim(),
            operator:$("#operator").attr("k"),
            status:m.status.val().trim()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 多选时查看、编辑禁用
    function isDisabledBtn(){
        let _list = tableGrid.getCheckboxTrs();
        if(_list == null || _list.length != 1){
            $("#view").prop("disabled",true);
            $("#update").prop("disabled",true);
        }else{
            $("#view").prop("disabled",false);
            $("#update").prop("disabled",false);
        }
    }

    // 表格初始化
    var initTable = function(){
        tableGrid = $("#zgGridTable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Business Date","Expenditure Code","Store No.","Store Name","Department","Expenditure Subject","Expense Type","Expenditure Amount",
                "Payment Type","Expenditure Description","Expenditure Remarks","Expenditure Status","Document status","Operator",
                "Date Created","Created By"],
            colModel:[
                {name:"accDate",type:"text",text:"center",width:"110",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"voucherNo",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"140",ishide:false,css:""},
                {name:"depName",type:"text",text:"left",width:"120",ishide:false,css:""},
                {name:"itemName",type:"text",text:"left",width:"140",ishide:false,css:""},
                {name:"expenseType",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:expenseTypeFmt},
                {name:"expenseAmt",type:"text",text:"right",width:"140",ishide:false,css:"",getCustomValue:getThousands},
                {name:"payName",type:"text",text:"left",width:"110",ishide:false,css:""},
                {name:"description",type:"text",text:"left",width:"180",ishide:false,css:""},
                {name:"remark",type:"text",text:"left",width:"180",ishide:false,css:""},
                {name:"statusName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"status",type:"text",text:"left",width:"120",ishide:false,css:"",getCustomValue:getStatus},
                {name:"userName",type:"text",text:"left",width:"120",ishide:false,css:""},
                {name:"createYmd",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},
                {name:"createUser",type:"text",text:"left",width:"120",ishide:false,css:""}
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            loadCompleteEvent:function(self){
                isDisabledBtn()
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                isDisabledBtn()
            },
            buttonGroup:[
                {
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""
                },
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
                },
                {
                    butType: "delete",
                    butId: "delete",
                    butText: "Delete",
                    butSize: ""
                },
                {
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
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
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#view").remove();
        }
        var editBut = $("#editBut").val();
        if (Number(editBut) != 1) {
            $("#update").remove();
        }
        var delBut = $("#delBut").val();
        if (Number(delBut) != 1) {
            $("#delete").remove();
        }
        var addBut = $("#addBut").val();
        if (Number(addBut) != 1) {
            $("#add").remove();
        }
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
        var approvalrecordButPm = $("#approvalrecordButPm").val();
        if (Number(approvalrecordButPm) != 1) {
            $("#approvalRecords").remove();
        }
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }
    var expenseTypeFmt = function(tdObj, value){
        switch (value) {
            case "04":
                value = "Additional";
                break;
            case "05":
                value = "Offset Claim";
                break;
            default:
                value = "";
        }
        return $(tdObj).text(value);
    };

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    // 格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }

    // 加载部门下拉
    function initDepartment(){
        getSelectOptions("Department","/ma0070/getDpt","",function(res){
            var selectObj = $("#department");
            selectObj.find("option:not(:first)").remove();
            for (var i = 0; i < res.length; i++) {
                var optionValue = res[i].depCd;
                var optionText = res[i].depCd+' '+res[i].depName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });
    }

    // 加载支取方式下拉
    function initPaymentType(){
        getSelectOptions("Payment Type","/sa0000/getList", null,function(res){
            var selectObj = $("#paymentType");
            selectObj.find("option:not(:first)").remove();
            for (var i = 0; i < res.length; i++) {
                var optionValue = res[i].payCd;
                var optionText = res[i].payName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });
    }

    // 加载经费科目下拉
    function initExpenditureSubject(){
        getSelectOptions("Expenditure Subject","/ma1060/getList", null,function(res){
            var selectObj = $("#expenditureSubject");
            selectObj.find("option:not(:first)").remove();
            for (var i = 0; i < res.length; i++) {
                var optionValue = res[i].itemId;
                var optionText = res[i].itemName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });
    }

    // 加载经费状态下拉
    function initExpenditureStatus(){
        var param = "codeValue=00225";
        getSelectOptions("Expenditure Status","/cm9010/getCode", param,function(res){
            var selectObj = $("#expenditureStatus");
            selectObj.find("option:not(:first)").remove();
            for (var i = 0; i < res.length; i++) {
                var optionValue = res[i].codeValue;
                var optionText = res[i].codeValue+' '+res[i].codeName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });
    }

    // 加载经办人下拉
    // function initOperator(){
    //     getSelectOptions("Operator","/cm9030/getList", null,function(res){
    //         var selectObj = $("#operator");
    //         selectObj.find("option:not(:first)").remove();
    //         for (var i = 0; i < res.length; i++) {
    //             var optionValue = res[i].userId;
    //             var optionText = res[i].userName;
    //             selectObj.append(new Option(optionText, optionValue));
    //         }
    //     });
    // }

    // 请求下拉数据
    function getSelectOptions(title, url, param, fun) {
        $.myAjaxs({
            url:url_root+url,
            async:false,
            cache:false,
            type :"post",
            data : param,
            dataType:"json",
            success : fun,
            error : function(e){
                _common.prompt(title+" Failed to load data!",5,"error");/*数据加载失败*/
            }
        });
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
                value = "Unknown";
        }
        return $(tdObj).text(value);
    }

    // 初始化下拉列表
    function initSelectValue(){
        operator=$("#operator").myAutomatic({
            url:url_left+"/getOperator",
            ePageSize:10,
            startCount: 0,
            param:[{
                'k':'storeCd',
                'v':'hidStore'
            }],
            selectEleClick: function (thisObj){

            }
        })




        // 加载部门下拉
        initDepartment();
        // 加载支取方式下拉
        initPaymentType();
        // 加载经费科目下拉
        initExpenditureSubject();
        // 加载经费状态下拉
        initExpenditureStatus();
        // 加载经办人下拉
        // initOperator();
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('fundQuery');
_start.load(function (_common) {
    _index.init(_common);
});
