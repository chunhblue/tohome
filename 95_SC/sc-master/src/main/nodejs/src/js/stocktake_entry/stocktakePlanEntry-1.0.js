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
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        systemPath=null,
        _result=null,
        dataForm=[],
        tableGrid=null,
        file = $('<input type="file"/>');
    const KEY = 'STOCKTAKING_RESULT_AND_QUERY';
    var m = {
        toKen : null,
        use : null,
        reset : null,
        selectTrObj : null,
        error_pcode : null,
        identity : null,
        pd_start_date:null,
        pd_end_date:null,
        clear_pd_date:null,
        search : null,//检索按钮
        main_box : null,//检索按钮
        search_item_but : null,
        checkResources:null,
        searchJson:null,
        pd_cd:null,
        pd_status:null,
        enterFlag:null,
        piCdParam:null,
        piDateParam:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
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
        url_left=_common.config.surl+"/stocktakeEntry";
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
        // 获取下拉框数据
        getComboxData();

        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        initTable1();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化参数
        initParam();
        // 初始化检索日期
        _common.initDate(m.pd_start_date,m.pd_end_date);
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

    var table_event = function(){
        //进入修改
        $("#updatePlan").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,storeCd,piStartTime,piEndTime,exportFlg");
            let piDate = formatDate(cols['piDate']);
            let businessDate = $("#businessDate").val();
            // let businessDate = new Date().Format('yyyyMMdd');

            if (_result.data==null&&piDate !== businessDate) {
                // 请在盘点当天完成
                _common.prompt("Please complete the entry on the day of stocktaking!",5,"error");
                return;
            }

            let piStartTime = formatTime(cols["piStartTime"]);
            let piEndTime = formatTime(cols["piEndTime"]);
            let nowTime = new Date().Format("hhmmss");
            let timeFlg = (nowTime < piStartTime || nowTime > piEndTime);
            if (_result.data==null&&timeFlg) {
                // 请在盘点时间内操作
                _common.prompt("Please operate within the stocktake time( "+cols["piStartTime"]+"~"+cols["piEndTime"]+" )!",5,"error");
                return;
            }

            // 判断是否 有导出标记
            if (cols['exportFlg']=='0') {
                // 请先导出盘点计划商品
                _common.prompt("Please export the stocktaking plan item first!",5,"error");
                return;
            }

            // 判断日结状态
            let flg = _common.getValByKey('0005');
            if (flg=='1') {
                _common.prompt("Daily settlement, cannot stocktaking!",5,"error");
                return;
            }
            let auditFlg = _result.data === 5 || _result.data === 6 ||  _result.data == null;
            // 判断审核状态
            if(auditFlg){
                saveParamToSession();
                top.location = url_left+"/edit?identity="+m.identity.val()+"&enterFlag=update&piCdParam="+cols['piCd']+
                    "&piDateParam="+formatDate(cols['piDate']);
            }else{
                _common.prompt(_result.message,5,"error");
            }
        });

        $("#printPlan").on("click",function(){
            if(verifySearch()) {
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "excelPrint", "width=1400,height=600,scrollbars=yes");
            }
        });

        $("#exportFiles").on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });
        $("#importFiles").on("click",function(){
            if (selectTrTemp==null) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }

            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,storeCd,piStartTime,piEndTime,exportFlg");

            let piDate = formatDate(cols['piDate']);
            let businessDate = $("#businessDate").val();
            // let businessDate = new Date().Format('yyyyMMdd');

            if (_result.data==null&&piDate != businessDate) {
                // 请在盘点当天完成
                _common.prompt("Please complete the entry on the day of stocktaking!",5,"error");
                return;
            }

            let piStartTime = formatTime(cols["piStartTime"]);
            let piEndTime = formatTime(cols["piEndTime"]);
            let nowTime = new Date().Format("hhmmss");
            let timeFlg = (nowTime < piStartTime || nowTime > piEndTime);
            if (_result.data==null&&timeFlg) {
                // 请在盘点时间内操作
                _common.prompt("Please operate within the stocktake time( "+cols["piStartTime"]+"~"+cols["piEndTime"]+" )!",5,"error");
                return;
            }

            // 判断是否 有导出标记
            if (cols['exportFlg']=='0') {
                // 请先导出盘点计划商品
                _common.prompt("Please export the stocktaking plan item first!",5,"error");
                return;
            }

            // 判断日结状态
            var flg = _common.getValByKey('0005');
            if (flg=='1') {
                _common.prompt("Daily settlement, cannot stocktaking!",5,"error");
                return;
            }

            // 判断审核状态
            if(_result.data==5||_result.data==6||_result.data==null){
                file.click();
            }else if(_result.data==10) {
                _common.prompt("The record audit is completed",5,"error");
            }else{
                _common.prompt(_result.message,5,"error");
            }
        });

        file.change(function(e){
            var cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,storeCd");
            var select_file = file[0].files[0];
            let suffix = new RegExp(/[^\.]+$/).exec(select_file.name)[0];
            // let excel_reg = /([xX][lL][sS][xX]){1}$|([xX][lL][sS]){1}$/;
            let excel_reg = /([cC][sS][vV]){1}$/;
            if (!excel_reg.test(suffix)) {
                _common.prompt('Upload file format error!',5,"error");
                file.val('');
                return;
            }
            var formData = new FormData();
            formData.append('fileData',select_file);
            formData.append('piCd',cols['piCd']);
            formData.append('piDate',formatDate(cols['piDate']));
            formData.append('storeCd',cols['storeCd']);

            // 确定要导入吗？
            _common.myConfirm("Are you sure you want to import? This will overwrite the last entry!",function(result) {
                if (result != "true") {
                    file.val('');
                    return false;
                }

                $.ajax({
                    url:url_left+"/upload",
                    dataType:'json',
                    type:'POST',
                    async: false,
                    data: formData,
                    processData : false, // 使数据不做处理
                    contentType : false, // 不要设置Content-Type请求头
                    success: function(data){
                        file.val('');
                        if (data.success) {
                            console.log(data.message);
                            if(data.message !== null && data.message !== 'undefined'){
                                // _common.prompt(data.message,5,"success");
                                _common.prompt(data.message,5,"success");
                            }else {
                             //   _common.prompt("Upload Succeed!",5,"success");
                                _common.prompt(data.message,5,"success");
                            }
                            // != null 表示已经发起过审核, 直接重新发起审核
                            if (_result.data != null) {
                                //发起审核
                                var typeId =m.typeId.val();
                                var	nReviewid =m.reviewId.val();
                                var	recordCd = cols['piCd'];
                                _common.initiateAudit(cols['storeCd'],recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
                                    m.toKen.val(token);
                                    m.search.click();
                                })
                            }
                        } else {
                            _common.prompt(data.message,5,"error");
                        }
                    },
                    error:function(response){
                        file.val('');
                        _common.prompt(data.message,5,"error");
                    }
                });
            });
        })

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
    }

    var getComboxData = function () {
        $.myAjaxs({
            url:systemPath+"/stocktakePlanEntry/getComboxData",
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

    //画面按钮点击事件
    var but_event = function(){
        //清空日期
        m.clear_pd_date.on("click",function(){
            m.pd_start_date.val("");
            m.pd_end_date.val("");
        });

        m.reset.on('click',function () {
            m.pd_start_date.val("");
            m.pd_end_date.val("");
            m.pd_cd.val("");
            m.pd_status.val("");
            $("#regionRemove").click();
            selectTrTemp = null;
            m.searchJson.val('');
            _common.clearTable();
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

    // 17:05:16 -> 170516
    var formatTime = function (str) {
        return str.replace(/:/g,'');
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
            'piStatus':piStatus
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
            colNames:["Date","Start Time","End Time","Document No.","Store No.","Store Name","Status","Type","Document Status","Remarks","Created by","Date Created","piStatusCode","piTypeCode","exportFlg"],
            colModel:[
                {name:"piDate",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piStartTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piEndTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"piCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"piStatus",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"piType",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"reviewStatus",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getStatus},
                {name:"remarks",type:"text",text:"left",width:"130",ishide:false,css:""},
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
            freezeHeader:false,
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
                let cols = tableGrid.getSelectColValue(selectTrTemp,"piCd,piDate,piStatusCode");
                _common.getRecordStatus(cols['piCd'],m.typeId.val(),function (result) {
                    _result = result;
                    $('#importFiles').prop('disabled',(_result.data!=null));
                });
            },
            buttonGroup:[
                {
                    butType: "update",
                    butId: "updatePlan",
                    butText: "Modify",
                    butSize: ""
                }, {
                    butType: "view",
                    butId: "viewPlan",
                    butText: "View",
                    butSize: ""
                }, {
                    butType:"custom",
                    butHtml:"<button id='exportFiles' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Export</button>"
                },{
                    butType: "upload",
                    butId: "importFiles",
                    butText: "Import Stocktaking",
                },{
                    butType:"custom",
                    butHtml:"<button id='printPlan' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print Actual Stocktaking Files</button>"
                },
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
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#viewPlan").remove();
        }
        var editButPm = $("#editButPm").val();
        if (Number(editButPm) != 1) {
            $("#updatePlan").remove();
        }
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#exportFiles").remove();
        }
        var importButPm = $("#importButPm").val();
        if (Number(importButPm) != 1) {
            $("#importFiles").remove();
        }
        var printButPm = $("#printButPm").val();
        if (Number(printButPm) != 1) {
            $("#printPlan").remove();
        }
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    //格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
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
var _index = require('receipt');
_start.load(function (_common) {
    _index.init(_common);
});
