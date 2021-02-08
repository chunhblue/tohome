require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('reconciliationMng', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        aStore = null,
        doucument = null,
        excel = null,
        //附件
        attachmentsParamGrid = null,
        common=null;
    var m = {
        toKen : null,
        use : null,
        up : null,
        identity : null,
        search : null,//检索按钮
        reset : null,//清空按钮
        import : null,//上传按钮
        affirmByAttachments : null,//保存按钮
        main_box : null,
        searchJson:null,
        affirm:null,
        td_start_date:null,
        td_end_date:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        ifExistsExcel : null,
    }

    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left=_common.config.surl+"/storeReconciliationMng";
        but_event();
        // 自动下拉
        initAutomatic();
        //初始化表格
        initTable1();
        attachments_event();
        // 表格内按钮
        table_event();
        //权限验证
        // isButPermission();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 初始化检索日期
        _common.initDate(m.td_start_date,m.td_end_date);
    }

    //附件
    var attachments_event = function () {
        //附件一览显示
        $("#import").on("click",function () {
            selectTrTempFile = null;//清空选择的行
            if(verifyExcel()) {
                $('#fileUpload_dialog').modal("show");
                $("#fileData").val("");
                $("#fileData").parent().parent().show();
            }
        });

        //提交按钮点击事件 文件上传
        $("#affirmByFile").on("click",function(){
            _common.myConfirm("Are you sure you want to upload？",function(result){
                if(result=="true"){
                    if($('#fileData')[0].files[0]==undefined||$('#fileData')[0].files[0]==null){
                        $("#fileData").css("border-color","red");
                        _common.prompt("File cannot be empty!",5,"error");
                        $("#fileData").focus();
                        return;
                    }else {
                        $("#fileData").css("border-color","#CCCCCC");
                    }
                    var formData = new FormData();
                    formData.append("fileData",$('#fileData')[0].files[0]);
                    formData.append("excelGroupCd",$("#excelGroupCd").attr("k"));
                    formData.append("toKen",m.toKen.val());
                    $.myAjaxs({
                        url:url_left+"/uploadFile",
                        async:false,
                        cache:false,
                        type :"post",
                        data :formData,
                        dataType:"json",
                        processData:false,
                        contentType:false,
                        success:function(data,textStatus, xhr){
                            _common.prompt(data.message,5,"success");
                            m.toKen.val(data.toKen);
                            $('#fileUpload_dialog').modal("hide");
                        },
                        complete:_common.myAjaxComplete
                    });
                }
            });
        });

        $("#cancelByFile").on("click",function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if (result==="true"){
                    $("#fileData").css("border-color","#CCC");
                    $('#fileUpload_dialog').modal("hide");
                }
            })
        });


    };

    // 表格内按钮
    var table_event = function(){

    };

    var but_event = function () {
        // 重置按钮
            m.reset.on("click",function(){
                m.td_start_date.val("");
                m.td_end_date.val("");
                $("#regionRemove").click();
                $.myAutomatic.cleanSelectObj(doucument);
                selectTrTemp = null;
                _common.clearTable();
            });

            // 检索按钮点击事件
            m.search.on("click",function(){
                if(verifySearch()){
                    //拼接检索参数
                    setParamJson();
                    paramGrid = "searchJson=" + m.searchJson.val();
                    tableGrid.setting("url", url_left + "/getDataByType");
                    tableGrid.setting("param", paramGrid);
                    tableGrid.setting("page", 1);
                    tableGrid.loadData();
                }
            });
        };




    function judgeNaN (value) {
        return value !== value;
    }

    //验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#td_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#td_start_date").css("border-color","red");
            $("#td_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#td_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#td_start_date").css("border-color","red");
                $("#td_start_date").focus();
                return false;
            }
            $("#td_start_date").css("border-color","#CCC");
        }
        let _EndDate = null;
        if(!$("#td_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#td_end_date").css("border-color","red");
            $("#td_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#td_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",5,"error");
                $("#td_end_date").css("border-color","red");
                $("#td_end_date").focus();
                return false;
            }
            $("#td_end_date").css("border-color","#CCC");
        }
        if(_StartDate>_EndDate){
            $("#td_end_date").focus();
            $("#td_end_date").css("border-color","red");
            _common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
            return false;
        }else {
            $("#td_end_date").css("border-color","#CCC");
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
            $("#td_end_date").focus();
            return false;
        }else {
            $("#td_end_date").css("border-color","#CCC");
        }

        if(!$("#doucument").attr('k')){
            _common.prompt("Please select Services Reconciliation!",3,"info");/*请选择文件夹参数*/
            $("#doucument").focus();
            $("#doucument").css("border-color","red");
            return false;
        }else {
            $("#doucument").css("border-color","#CCC");
        }

        if(!$("#excelGroupCd").attr('k')){
            _common.prompt("Please select Excel Type!",3,"info");/*请选择文件夹参数*/
            $("#excelGroupCd").focus();
            $("#excelGroupCd").css("border-color","red");
            return false;
        }else {
            $("#excelGroupCd").css("border-color","#CCC");
        }

        return true;
    };

    var verifyExcel = function(){
        if(!$("#doucument").attr('k')){
            _common.prompt("Please select Services Reconciliation!",3,"info");/*请选择文件夹参数*/
            $("#doucument").focus();
            $("#doucument").css("border-color","red");
            return false;
        }else {
            $("#doucument").css("border-color","#CCC");
        }

        if(!$("#excelGroupCd").attr('k')){
            _common.prompt("Please select Excel Type!",3,"info");/*请选择文件夹参数*/
            $("#excelGroupCd").focus();
            $("#excelGroupCd").css("border-color","red");
            return false;
        }else {
            $("#excelGroupCd").css("border-color","#CCC");
        }
        return true;
    };

    //拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        var _startDate = fmtStringDate(m.td_start_date.val())||null;
        var _endDate = fmtStringDate(m.td_end_date.val())||null;

        // 创建请求字符串
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            transStrartDate:_startDate,
            transEndDate:_endDate,
            excelGroupCd : $("#excelGroupCd").attr('k')
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 按钮权限验证
    var isButPermission = function () {
        var storeShelfButUpload = $("#storeShelfButUpload").val();
        if (Number(storeShelfButUpload) != 1) {
            $("#import").remove();
        }
        var storeShelfButExport = $("#storeShelfButExport").val();
        if (Number(storeShelfButExport) != 1) {
            $("#export").remove();
        }
    };

    var initAutomatic = function() {
        // 初始化，子级禁用
        $("#excelGroupCd").prop("disabled",true);
        $("#excelRefresh").hide();
        $("#excelRemove").hide();
        doucument = $("#doucument").myAutomatic({
            url: url_left + "/getMb0010",
            ePageSize: 5,
            startCount: 0,
            cleanInput: function() {
                $.myAutomatic.cleanSelectObj(excel);
                $("#excelGroupCd").prop("disabled",true);
                $("#excelRefresh").hide();
                $("#excelRemove").hide();
            },
            selectEleClick: function (thisObject) {
                $.myAutomatic.cleanSelectObj(excel);
                if (thisObject.attr("k") != null && thisObject.attr("k") != "") {
                    $("#excelGroupCd").attr("disabled", false);
                    $("#excelRefresh").show();
                    $("#excelRemove").show();
                }
                let str = "&documentReconCd="+thisObject.attr("k").trim();
                $.myAutomatic.replaceParam(excel, str);
            }
        });

        excel = $("#excelGroupCd").myAutomatic({
            url: url_left + "/getMb0020",
            ePageSize: 5,
            startCount: 0,
            cleanInput: function() {

            },
        })
    };

    //表格初始化
    var initTable1 = function() {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Reconciliation Query",
            param: paramGrid,
            // localSort: true,
            colNames: [ "Store No.", "Store Name" ,"Trans Date", "Barcode","Item Code","Item Name","third-party Qty","third-party Amount",
                 "CK Qty","CK Amount", "Variance Qty","Variance Amount"],
            colModel: [
                {name: "storeCd", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "storeName", type: "text", text: "left", width: "150", ishide: false, css: ""},
                {name: "transDate", type: "text", text: "center", width: "130", ishide: false, css: "",getCustomValue:dateFmt},
                {name: "barcode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "articleId", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "articleName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "thirdQty", type: "text", text: "right", width: "100", ishide: false, css: "",getCustomValue: getThousands},
                {name: "thirdAmount", type: "text", text: "right", width: "130", ishide: false, css: "",getCustomValue: getThousands},
                {name: "ckQty", type: "text", text: "right", width: "80", ishide: false, css: "",getCustomValue: getThousands},
                {name: "ckAmount", type: "text", text: "right", width: "100", ishide: false, css: "",getCustomValue: getThousands},
                {name: "varyQty", type: "text", text: "right", width: "130", ishide: false, css: "",getCustomValue: getThousands},
                {name: "varyAmount", type: "text", text: "right", width: "130", ishide: false, css: "",getCustomValue: getThousands},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: false,
            lineNumber: false,// 首列是否需要序号
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
            ],
        });
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

    function fmtIntDate(date) {
        var res = "";
        if (date == null || date == "") {
            return res;
        }
        res = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
        return res;
    }

    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        date = date.replace(/\//g,"");
        res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
        return res;
    }
// 去除千分位
    function reThousands(num) {
        num+='';
        return num.replace(/,/g, '');
    }

    function getThousands(tdObj, value) {
        var dit = 0;
        var num = toThousands(value,dit);
        // 省略小数点后为0的数
        return $(tdObj).text(num);
    }

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num,dit) {
        if (!num) {
            return '0';
        }
        dit = typeof dit !== 'undefined' ? dit : 0;
        num = num + '';
        num = parseFloat(num).toFixed(dit);

        var source = String(num).split(".");//按小数点分成2部分
        source[0] = source[0].replace(new RegExp('(\\d)(?=(\\d{3})+$)', 'ig'), "$1,");//只将整数部分进行都好分割
        num = source.join(".");//再将小数部分合并进来
        return num;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('reconciliationMng');
_start.load(function (_common) {
    _index.init(_common);
});