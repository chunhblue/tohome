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
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    const KEY = 'MM_PROMOTION_QUERY';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        promotionCd: null,
        promotionName: null,
        promotionStartDate: null,
        promotionEndDate: null,
        search: null,
        reset: null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl+"/mmPromotion";
        // 首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype("1");
        // 表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化查询加载
        // m.search.click();

        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 初始化日期
        _common.initDate(m.promotionStartDate,m.promotionEndDate);
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

        m.promotionStartDate.val(obj.promotionStartDate);
        m.promotionEndDate.val(obj.promotionEndDate);
        m.promotionName.val(obj.promotionName);
        m.promotionCd.val(obj.promotionCd);
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
        obj.promotionStartDate=m.promotionStartDate.val();
        obj.promotionEndDate=m.promotionEndDate.val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 表格内按钮点击事件
    var table_event = function(){
        // 查看按钮
        $("#view").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select the data to view!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"promotionCd");
            var param = "promotionCd=" + cols["promotionCd"];
            saveParamToSession();
            top.location = url_left+"/view?"+param;
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
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable();//列表初始化
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
        // 清空按钮
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.promotionCd.val("");
            m.promotionName.val("");
            m.promotionStartDate.val("");
            m.promotionEndDate.val("");
            m.searchJson.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
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
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#promotionStartDate").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#promotionStartDate").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#promotionStartDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#promotionStartDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#promotionEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#promotionEndDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#promotionEndDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#promotionEndDate").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            $("#promotionEndDate").focus();
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue >62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#promotionEndDate").focus();
            return false;
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 日期转换
        var _startDate = fmtStringDate(m.promotionStartDate.val())||null;
        var _endDate = fmtStringDate(m.promotionEndDate.val())||null;
        // 创建请求字符串
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            promotionCd:m.promotionCd.val().trim(),
            promotionName:m.promotionName.val().trim(),
            promotionStartDate:_startDate,
            promotionEndDate:_endDate
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 一览表格初始化
    var initTable = function(){
        tableGrid = $("#zgGridTable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Promotion No.","Promotion Theme","Promotion Start Date","Promotion End Date"],
            colModel:[
                {name:"promotionCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"promotionName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"promotionStartDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"promotionEndDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt}
            ],//列内容
            width:"max",//宽度自动 max
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
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                },
                {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
            ]
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#view").remove();
        }
        var exportBut = $("#exportBut").val();
        if (Number(exportBut) != 1) {
            $("#export").remove();
        }
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        if(date == null||$.trim(date) == ''||date.length!=8){
            return date;
        }
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }

    //格式化数字类型的日期
    // function fmtIntDate(date){
    //     var res = "";
    //     res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
    //     return res;
    // }

    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        date = date.replace(/\//g,"");
        res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
        return res;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 格式化数字类型的日期
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
