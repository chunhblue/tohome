require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('remoteUpdate', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        a_store,
        common=null;
    const KEY = 'POS_REMOTE_UPDATE_MANAGEMENT';
    var m = {
        reset: null,
        search: null,
        searchJson:null,
        // 栏位
        aStore : null,//店铺
        updateType : null,//分组
        startDate:null,//开始时间
        groupName:null,//分组名称
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left =_common.config.surl+"/remoteUpdate";
        systemPath = _common.config.surl;
        //初始化下拉
        initAutoMatic();
        //事件绑定
        but_event();
        //列表初始化
        initTable1();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.startDate,null);
        // 初始化参数
        initParam();
    }

    // 初始化自动下拉
    var initAutoMatic = function () {
        // 获取店铺
        a_store = $("#aStore").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0
        });
        m.startDate.val(new Date());
        initSelectOptions("Group Type","updateType","00920")
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
        $.myAutomatic.setValueTemp(a_store,obj.aStoreK,obj.aStoreV);
        if(obj.startDate == null){
            m.startDate.val("");
        }else {
            m.startDate.val(fmtDMYDate(obj.startDate));
        }
        m.updateType.val(obj.updateType);
        m.groupName.val(obj.groupName);

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
        // 开始日期
        var _startDate = subfmtDate(m.startDate.val())||null;
        // 创建请求字符串
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        obj.aStoreV=m.aStore.attr("v");
        obj.aStoreK=m.aStore.attr("k");
        obj.updateType=m.updateType.val().trim();
        obj.groupName=m.groupName.val().trim();
        obj.startDate = _startDate;
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    //画面按钮点击事件
    var but_event = function(){
        //重置搜索
        m.reset.on("click",function(){
            m.aStore.val("")
            m.updateType.val("");
            m.groupName.val("");
            m.startDate.val("");
            selectTrTemp = null;
            paramGrid = '';
            _common.clearTable();
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            setParamJson();
            paramGrid = "searchJson="+m.searchJson.val();
            tableGrid.setting("url",url_left+"/getList");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData();
        });

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

    //表格按钮事件
    var table_event = function(){
        $("#add").on("click", function () {
            saveParamToSession();
            top.location = url_left+"/edit?flag=add";
        })
        $("#modify").on("click", function () {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"id,startDate");
            var fmtStartDate = ddMMyyyyfmtDate(cols["startDate"]);
            if(fmtStartDate <= new Date()){
                _common.prompt("Please select a future date record to modify!",5,"error");
                m.startDate.focus();
                return false;
            }
            var param = "id="+cols["id"]+"&flag=edit";
            //进入修改界面
            saveParamToSession();
            top.location = url_left+"/edit?"+param;
        })
        $("#view").on("click", function () {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"id");
            var param = "flag=view&id="+cols["id"];
            saveParamToSession();
            top.location = url_left+"/edit?"+param;
        })
    }

    //表格初始化-POS支付方式设定头档列表样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            colNames:["ID","Start Date","Store Code","Store Name","Update Type","Group Name","Modified By","Date Modified"],
            colModel:[
                {name:"id",type:"text",text:"right",width:"100",ishide:true,css:""},
                {name:"startDate",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:dateFmtDMYDate},
                {name:"storeCd",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"updateTypeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"groupName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"updateUserName",type:"text",text:"left",width:"130",ishide:true,css:""},
                {name:"updateDate",type:"text",text:"center",width:"130",ishide:true,css:"",getCustomValue:dateFmtDMYDate},
            ],//列内容
            width:"max",//宽度自动
            isPage:true,//是否需要分页
            page:1,//当前页
            sidx:"pay_cd",//排序字段
            sord:"desc",//升降序
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
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""
                }, {
                    butType: "update",
                    butId: "modify",
                    butText: "Modify",
                    butSize: ""
                }, {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                }
            ]
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var remoteButView = $("#remoteButView").val();
        if (Number(remoteButView) != 1) {
            $("#view").remove();
        }
        var remoteButAdd = $("#remoteButAdd").val();
        if (Number(remoteButAdd) != 1) {
            $("#add").remove();
        }
        var remoteButEdit = $("#remoteButEdit").val();
        if (Number(remoteButEdit) != 1) {
            $("#modify").remove();
        }
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 开始日期
        var _startDate = subfmtDate(m.startDate.val())||null;
        // 创建请求字符串
        var searchJsonStr ={
            startDate:_startDate,
            storeCd:m.aStore.attr("k"),
            updateType:m.updateType.val().trim(),
            groupName:m.groupName.val().trim(),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
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

    // 格式化数字类型的日期
    function fmtDate(date){
        var res = "";
        res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        return res;
    }


    function subfmtDate(date){
        var res = "";
        if(date!=null&&date!=""){
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
        return res;
    }

    var dateFmtDMYDate = function(tdObj, value){
        return $(tdObj).text(fmtDMYDate(value));
    }

    function fmtDMYDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }

    //dd/MM/yyyy 格式转化为日期
    function ddMMyyyyfmtDate(ddMMyyyyStr){
        var yyyyMMddStr = ddMMyyyyStr.substring(6,10)+"-"+ddMMyyyyStr.substring(3,5) +"-"+ ddMMyyyyStr.substring(0,2);
        return new Date(yyyyMMddStr);
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('remoteUpdate');
_start.load(function (_common) {
    _index.init(_common);
});
