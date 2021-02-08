require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("checkboxGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('unpack', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        a_region = null,
        a_city = null,
        a_district = null,
        a_store = null,
        tempTrObjValue = {};
    const KEY = 'UNPACKING_SALES';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        main_box : null,
        isHide : null,
        // 栏位
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        unpackStartDate : null,
        unpackEndDate : null,
        unpackNo : null,
        parentItem : null,
        // 按钮
        search:null,
        reset:null,
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
        url_left = url_root + "/unpackSale";
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
        // 初始化店铺运营组织检索
        _common.initOrganization();
        //权限验证
        isButPermission();
        isHideBtn();
        // 初始化检索日期
        _common.initDate(m.unpackStartDate,m.unpackEndDate);
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

        m.unpackStartDate.val(obj.unpackStartDate);
        m.unpackEndDate.val(obj.unpackEndDate);
        m.unpackNo.val(obj.unpackId);
        m.parentItem.val(obj.parentArticleId);
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
        obj.unpackStartDate=m.unpackStartDate.val();
        obj.unpackEndDate=m.unpackEndDate.val();
        // obj.unpackId=m.unpackNo.val();
        // obj.parentArticleId=m.parentItem.val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 表格内按钮事件绑定
    var table_event = function(){
        // 新增
        $("#add").on("click", function (e) {
            if(m.isHide.val() == '0'){
                saveParamToSession();
                top.location = url_left+"/edit?flag=add";
            }
        });
        // 编辑
        $("#update").on("click", function (e) {
            if(m.isHide.val() != '0'){ return false; }
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length != 1){
                _common.prompt("Please select at least one row of data!",5,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(_list[0],"storeCd,unpackId,parentArticleId");
            let param = "storeCd=" + cols["storeCd"] + "&unpackId=" + cols["unpackId"] +
                "&parentArticleId=" + cols["parentArticleId"] + "&flag=edit";
            saveParamToSession();
            top.location = url_left + "/edit?" + param;
        });
        // 删除
        $("#delete").on("click",function(){
            if(m.isHide.val() != '0'){ return false; }
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length == 0){
                _common.prompt("Please select the data records to delete!",5,"info");
                return false;
            }
            let _params = [];
            for(var i = 0; i < _list.length; i++){
                var cols = tableGrid.getSelectColValue(_list[i],"storeCd,unpackId,parentArticleId");
                var _param ={
                    storeCd:cols['storeCd'],
                    unpackId:cols['unpackId'],
                    parentArticleId:cols['parentArticleId']
                };
                _params.push(_param);
            }
            let params = JSON.stringify(_params);
            _common.myConfirm("Are you sure you want to delete?",function(result){
                if(result=="true"){
                    $.myAjaxs({
                        url:url_left+"/del",
                        async:true,
                        cache:false,
                        type :"post",
                        data :"searchJson="+params,
                        dataType:"json",
                        success:function(re){
                            if(re.success){
                                m.search.click();
                                _common.prompt("Deleted successfully!",5,"success");
                            }else{
                                _common.prompt(re.msg,5,"info");
                            }
                        },
                        error : function(e){
                            _common.prompt("Deleted failed!",5,"info");
                        }
                    });
                }
            })
        });
        // 查看
        $("#view").on("click", function (e) {
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length != 1){
                _common.prompt("Please select at least one row of data!",5,"info");
                return false;
            }
            let cols = tableGrid.getSelectColValue(_list[0],"storeCd,unpackId,parentArticleId");
            let param = "storeCd=" + cols["storeCd"] + "&unpackId=" + cols["unpackId"] +
                "&parentArticleId=" + cols["parentArticleId"];
            saveParamToSession();
            top.location = url_left + "/view?" + param;
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
                initTable();// 列表初始化
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
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/getList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
        // 清空按钮点击事件
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.unpackStartDate.val("");
            m.unpackEndDate.val("");
            m.unpackNo.val("");
            m.parentItem.val("");
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
        if(!$("#unpackStartDate").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#unpackStartDate").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#unpackStartDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#unpackStartDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#unpackEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#unpackEndDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#unpackEndDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#unpackEndDate").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#unpackEndDate").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#unpackEndDate").focus();
            return false;
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        var _startDate = subfmtDate(m.unpackStartDate.val())||null;
        var _endDate = subfmtDate(m.unpackEndDate.val())||null;
        // 创建请求字符串
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            unpackStartDate:_startDate,
            unpackEndDate:_endDate,
            unpackId:m.unpackNo.val().trim(),
            parentArticleId:m.parentItem.val().trim()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 加载时判断功能是否可用
    function isHideBtn(){
        if(m.isHide.val()=='0'){
            $("#add").show();
            $("#update").show();
            $("#delete").show();
        }else{
            $("#add").hide();
            $("#update").hide();
            $("#delete").hide();
        }
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
            title:"Unpacking Sales List",
            param:paramGrid,
            localSort: true,
            colNames:["Store No.","Store Name","Unpacking Date","Unpacking No.","Item Barcode","Parent Item Code","Parent Item Name",
                "Unpacking Qty"," Unpacking Amount","Remarks"],
            colModel:[
                {name:"storeCd",type:"text",text:"right",width:"80",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"unpackDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"unpackId",type:"text",text:"right",width:"150",ishide:false,css:""},
                {name:"barcode",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"parentArticleId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"160",ishide:false,css:""},
                {name:"unpackQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
                {name:"unpackAmt",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
                {name:"remarks",type:"text",text:"left",width:"200",ishide:false,css:""}
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
                isDisabledBtn();
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                isDisabledBtn();
            },
            buttonGroup:[
                {
                    butType:"custom",
                    butHtml:"<button id='add' type='button' style='display:none;' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-plus'></span> Add</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='update' type='button' style='display:none;' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-edit'></span> Modify</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-list-alt'></span> View</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='delete' type='button' style='display:none;' class='btn btn-danger btn-sm'><span class='glyphicon glyphicon-trash'></span> Delete</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                }
            ],
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

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    // 日期处理
    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('unpack');
_start.load(function (_common) {
    _index.init(_common);
});