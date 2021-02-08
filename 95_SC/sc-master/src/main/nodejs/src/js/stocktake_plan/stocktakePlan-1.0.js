require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('stocktakePlan', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        dataForm=[],
        systemPath='';
    const KEY = 'STOCKTAKING_PLAN_SETTING';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        main_box:null,
        identity : null,
        searchJson:null,
        pd_cd:null, // 盘点单号
        pd_start_date:null,
        pd_end_date:null,
        clear_pd_date:null,
        search : null,//检索按钮
        reset:null,
        search_store_input:null,
        pd_status:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        typeId:null,//审核类型id
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    };
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/stocktakePlanEntry";
        systemPath = _common.config.surl;
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 下拉框数据
        getComboxData();
        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        initPageBytype("1");
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.pd_start_date,m.pd_end_date);
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

        m.pd_start_date.val(obj.piStartDate);
        m.pd_end_date.val(obj.piEndDate);
        m.pd_status.val(obj.piStatus);
        m.pd_cd.val(obj.piCd);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/inquire");
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

    // 17:05:16 -> 170516
    var formatTime = function (str) {
        return str.replace(/:/g,'');
    }

    var table_event = function(){
        //进入添加
        $("#addPlan").on("click", function (e) {
            saveParamToSession();
            top.location = url_left+"/edit?identity="+m.identity.val()+"&enterFlag=add";
        });

        $("#print").on("click",function(){
            if(verifySearch()) {
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "excelprint", "width=1400,height=600,scrollbars=yes");
            }
        });
        // 导出按钮点击事件
        $("#exportInfo").on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });
        $("#exportItems").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,storeCd,piStartTime,piEndTime");

            let piDate = formatDate(cols['piDate']);
            let businessDate = $("#businessDate").val();
            // let businessDate = new Date().Format('yyyyMMdd');

            if (piDate !== businessDate) {
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
            var flg = _common.getValByKey('0005');
            if (flg=='1') {
                _common.prompt("Daily settlement, cannot stocktaking!",5,"error");
                return;
            }

            //获取数据审核状态
            _common.getRecordStatus(cols["piCd"],m.typeId.val(),function (result) {

                // 需要审核通过才可以导出
                if (result.data != 10) {
                    _common.prompt("Please approve it first!",5,"error");
                    return;
                }

                _common.loading();
                paramGrid = m.searchJson.val();
                $.myAjaxs({
                    // url:url_left+"/queryExport",
                    url:url_left+"/queryCsv",
                    async:true,
                    timeout: 60000, // 一分钟超时
                    cache:false,
                    data:'jsonStr='+paramGrid+'&piCd='+cols['piCd']+'&piDate='+formatDate(cols['piDate'])+'&storeCd='+cols['storeCd'],
                    type :"post",
                    dataType:"json",
                    success:function(result){
                        if(result.success){
                            //导出excle
                            // window.location.href=url_left+"/download?piCd="+cols["piCd"];
                            window.location.href=url_left+"/downloadCsv?piCd="+cols["piCd"];
                        } else {
                            _common.prompt(result.msg,5,"error");
                        }
                        _common.loading_close();
                    },
                    error : function(e){
                        _common.prompt("Export failed!",5,"error");
                        _common.loading_close();
                    },
                });
            });
        });
        var table_ck_all = $("#table_ck_all");
        table_ck_all.on("change",function(event){
            event.preventDefault();
            var ck = $(this).prop("checked");
            $("#zgGridTtable_tbody").find("input[type='checkbox']").prop("checked",ck);
        });

        $("#modifyPlan").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,storeCd,piStartTime,piEndTime,piStatusCode,exportFlg");

            if(cols==null){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{

                if (cols["exportFlg"]=='1') {
                    // 盘点已开始，不能修改
                    _common.prompt("Stocktaking has started, cannot be modified!",5,"error");
                    return;
                }

                if (cols['piStatusCode'] == '02') {
                    _common.prompt("Stocktaking in Progress, cannot be modified!",5,"error");
                    return;
                }
                if (cols['piStatusCode'] == '03') {
                    _common.prompt("Stocktaking Done, cannot be modified!",5,"error");
                    return;
                }
                if (cols['piStatusCode'] == '04') {
                    _common.prompt("Stocktaking Plan Expired, cannot be modified!",5,"error");
                    return;
                }
                if (cols['piTypeCode'] == '01') {
                    // 当前盘点类型为 All Item，不可修改！
                    _common.prompt("The current type is All item and cannot be modified!",5,"error");
                    return;
                }
                //获取数据审核状态
                _common.getRecordStatus(cols["piCd"],m.typeId.val(),function (result) {
                    if(result.success){
                        saveParamToSession();
                        top.location = url_left+"/edit?identity="+m.identity.val()+"&enterFlag=update&piCdParam="+cols['piCd']+"&piDateParam="+formatDate(cols['piDate']);
                    }else{
                        _common.prompt(result.message,5,"error");
                    }
                });
            }
        });

        $("#viewPlan").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate");

            if(cols==null){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                saveParamToSession();
                top.location = url_left+"/edit?identity="+m.identity.val()+"&enterFlag=view&piCdParam="+cols['piCd']+"&piDateParam="+formatDate(cols['piDate']);
            }
        });

        //审核记录
        $("#approvalRecords").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
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

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                initTable2();//列表初始化
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
            _common.prompt("Query Period cannot exceed 62 days!!",3,"info"); // 日期期间取值范围不能大于62天
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
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            piStartDate:_startDate,
            piEndDate:_endDate,
            piCd:m.pd_cd.val(),
            piStatus:piStatus
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //画面按钮点击事件
    var but_event = function(){
        //清空日期
        m.clear_pd_date.on("click",function(){
            m.pd_start_date.val("");
            m.pd_end_date.val("");
        });

        //点击查找店铺编号按钮事件
        m.search_store_input.on("click",function(){
            var inputVal = m.pd_cd.val();
            m.pd_cd.text("");
            if($.trim(inputVal)==""){
                _common.prompt("Store No. cannot be empty!",5,"error");
                m.pd_cd.attr("status","2").focus();
                return false;
            }
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/inquire");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
        m.reset.on("click",function(){
            m.pd_start_date.val("");
            m.pd_end_date.val("");
            m.pd_cd.val("");
            m.pd_status.val("");
            $("#regionRemove").click();
            selectTrTemp = null;
            m.searchJson.val('');
            _common.clearTable();
        });
    }

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Stocktake Plan",
            param:paramGrid,
            localSort: true,
            colNames:["Date","Start Time","End Time","Document No.","Store No.","Store Name","Status","Type","Remarks","Day End of Now","Created by","Date Created","piStatusCode","piTypeCode","exportFlg"],
            colModel:[
                {name:"piDate",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piStartTime",type:"text",text:"center",width:"130",ishide:false},
                {name:"piEndTime",type:"text",text:"center",width:"130",ishide:false},
                {name:"piCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"storeName",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"piStatus",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"piType",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"remarks",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"dayEndOfNow",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"createUserId",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"createYmd",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},

                {name:"piStatusCode",type:"text",text:"left",width:"130",ishide:true,},
                {name:"piTypeCode",type:"text",text:"left",width:"130",ishide:true,},
                {name:"exportFlg",type:"text",text:"left",width:"130",ishide:true,},
            ],//列内容
            traverseData:dataForm,
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
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                // trClick_table1();
            },
            buttonGroup:[
                {
                    butType: "add",
                    butId: "addPlan",
                    butText: "Add",
                    butSize: "",
                },
                {
                    butType: "update",
                    butId: "modifyPlan",
                    butText: "Modify",
                    butSize: "",
                },
                {
                    butType: "view",
                    butId: "viewPlan",
                    butText: "View",
                    butSize: "",
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
                    butHtml:"<button id='exportInfo' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='exportItems' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export Stocktaking Items</button>"
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
        var addButPm = $("#addButPm").val();
        if (Number(addButPm) != 1) {
            $("#addPlan").remove();
        }
        var editButPm = $("#editButPm").val();
        if (Number(editButPm) != 1) {
            $("#modifyPlan").remove();
        }
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#viewPlan").remove();
        }
        var printButPm = $("#printButPm").val();
        if (Number(printButPm) != 1) {
            $("#print").remove();
        }
        var exportBasicButPm = $("#exportBasicButPm").val();
        if (Number(exportBasicButPm) != 1) {
            $("#exportInfo").remove();
        }
        var exportItemButPm = $("#exportItemButPm").val();
        if (Number(exportItemButPm) != 1) {
            $("#exportItems").remove();
        }
        var approvalBut = $("#approvalBut").val();
        if (Number(approvalBut) != 1) {
            $("#approvalRecords").remove();
        }
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    var getComboxData = function () {
        $.myAjaxs({
            url:url_left+"/getComboxData",
            async:false,
            cache:false,
            type :"get",
            dataType:"json",
            success:function(result){
                if(result.success){
                    var record = result.o;
                    var piStsList = record.piStatus;
                    piStsList.forEach(function (item) {
                        var temp = '<option value="'+item.codevalue+'">'+item.codename+'</option>'
                        $('#pd_status').append(temp);
                    });
                }
            }
        });
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('stocktakePlan');
_start.load(function (_common) {
    _index.init(_common);
});
