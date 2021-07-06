require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('pogShelfMaintenance', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    const KEY = 'POG_SHELF_MAINTENANCE_MANAGEMENT';
    var m = {
        toKen : null,
        use : null,
        main_box : null,
        error_pcode : null,
        identity : null,
        searchJson: null,
        ud_start_date : null,
        ud_end_date : null,
        pog_cd : null,
        pog_name : null,
        reviewStatus : null,
        aStore : null,
        search : null,
        reset : null
    };

    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = url_root + "/shelfMaintenance";
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
        //初始化下拉
        initSelectValue();
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.ud_start_date,m.ud_end_date);
        initParam();
    }

    // 初始化下拉列表
    function initSelectValue(){
        aStore = $("#aStore").myAutomatic({  //加上名字可以存入自动填充
            url: url_root + "/ma1000/getStoreByPM",
            ePageSize: 5,
            startCount: 0
        });
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
        m.ud_start_date.val(fmtIntDate(obj.startDate));
        m.ud_end_date.val(fmtIntDate(obj.endDate));
        m.pog_cd.val(obj.pogCd);
        m.pog_name.val(obj.pogName);
        m.reviewStatus.val(obj.reviewStatus);
        $.myAutomatic.setValueTemp(aStore,obj.storeCd,obj.storeName);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/getdata");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData();
    };

    //表格内按钮点击事件
    var table_event = function(){
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

        $("#view").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                var cols = tableGrid.getSelectColValue(selectTrTemp,"pogCd,pogName,storeCd,storeName," +
                    "createTime,createUserName");
                var createTime = '';
                if(cols["createTime"]!=null&&cols["createTime"]!==''){
                    createTime = cols["createTime"];
                }
                var param = "pogCd=" + cols["pogCd"] + "&pogName=" + cols["pogName"]+ "&storeCd=" + cols["storeCd"]+
                    "&storeName=" + cols["storeName"] + "&createTime=" + createTime+
                    "&createUserName=" + cols["createUserName"];
                saveParamToSession();
                top.location = url_left+"/view?"+param;
            }
        });
    };

    // 保存 参数信息
    var saveParamToSession = function () {
        let searchJson = m.searchJson.val();
        if (!searchJson) {
            return;
        }

        let obj = eval("("+searchJson+")");
        obj.pog_name=m.pog_name.val();
        obj.storeName=m.aStore.attr('v');
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
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

    // 画面按钮点击事件
    var but_event = function(){
        // 检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/getdata");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
        // 清空按钮事件
        m.reset.on("click",function(){
            m.ud_start_date.val("");
            m.ud_end_date.val("");
            m.pog_cd.val("");
            m.pog_name.val("");
            m.reviewStatus.val("");
            $.myAutomatic.cleanSelectObj(aStore);
            selectTrTemp = null;
            _common.clearTable();
        });
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#ud_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#ud_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#ud_start_date").val())).getTime();
            if(_common.judgeValidDate($("#ud_start_date").val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#ud_start_date").css("border-color","red");
                $("#ud_start_date").focus();
                return false;
            }else {
                $("#ud_start_date").css("border-color","#CCCCCC");
            }
        }
        let _EndDate = null;
        if(!$("#ud_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#ud_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#ud_end_date").val())).getTime();
            if(_common.judgeValidDate($("#ud_end_date").val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#ud_end_date").css("border-color","red");
                $("#ud_end_date").focus();
                return false;
            }else {
                $("#ud_end_date").css("border-color","#CCCCCC");
            }
        }
        if(_StartDate>_EndDate){
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            $("#ud_end_date").focus();
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#ud_end_date").focus();
            $("#ud_end_date").css("border-color","#red");
            return false;
        }else{
            $("#ud_end_date").css("border-color","#CCC");
        }

        var _storeCd = $("#aStore").attr("k");
        if (_storeCd == null || _storeCd == "") {
            _common.prompt("Please select a store first!", 3, "info");
            $("#aStore").focus();
            $("#aStore").css("border-color","red");
            return false;
        }else {
            $("#aStore").css("border-color","#CCC");
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        var _startDate = fmtStringDate(m.ud_start_date.val())||null;
        var _endDate = fmtStringDate(m.ud_end_date.val())||null;
        // 创建请求字符串
        var searchJsonStr ={
            startDate:_startDate,
            endDate:_endDate,
            storeCd:m.aStore.attr('k'),
            pogCd:m.pog_cd.val(),
            pogName:m.pog_name.val(),
            reviewStatus:m.reviewStatus.val()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    };


    //表格初始化-库存报废样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Document Date","Store No.","Store Name","Document","Document Name","Review Status","Is Expired","Created By"],
            colModel:[
                {name:"createTime",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},
                {name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"pogCd",type:"text",text:"right",width:"130",ishide:true,css:""},
                {name:"pogName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"reviewStatusName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"isExpired",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getIsExpired},
                {name:"createUserName",type:"text",text:"left",width:"130",ishide:false,css:""}
            ],//列内容
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
            },
            buttonGroup:[
                {butType:"custom",butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-pencil'></span> View</button>"},
                {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
            ]
        });
    };

    var getIsExpired = function(tdObj, value){
        let temp;
        switch (value) {
            case "0":
                temp = "Effective";
                break;
            case "1":
                temp = "Expired";
                break;
            default:
                temp = "";
        }
        return $(tdObj).text(temp);
    }

    // 按钮权限验证
    var isButPermission = function () {
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        date = date.replace(/\//g,"");
        res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    // 格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('pogShelfMaintenance');
_start.load(function (_common) {
    _index.init(_common);
});
