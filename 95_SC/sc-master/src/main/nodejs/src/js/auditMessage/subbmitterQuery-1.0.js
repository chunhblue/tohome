require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('subbmitter', function (_common) {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common = null;

    var m = {
        toKen: null,
        use: null,
        up: null,
        // 查询部分
        user_id: null, //提交者编号
        user_name: null, // 提交者姓名
        user_role: null,
        UserId : null,
        UserName : null,
        UserRole : null,

        alert_div : null,//页面提示框
        search: null,//检索按钮
        reset: null,//清空按钮
        main_box: null,//检索按钮
        searchJson: null,
        checkCount: null,
        urgencyCount: null,
        del_alert: null
    }

    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    // 页面初始化
    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/subbmitterInfo";
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }

        initTable1();//列表初始化
        //表格内按钮事件
        table_event();
        // 初始化查询加载数据
        m.search.click();
    }

    // 画面按钮点击事件
    var but_event = function () {
    //     $("#cancelSubbmitter").on("click",function () {
    //         alter("Are you sure you want to return ?");
    //     })

        // 清空弹窗
        m.reset.on("click",function(){
            m.user_id.val("");
            m.user_name.val("");
            m.user_role.val("");
        });

        //点击查找提交者编号按钮事件
        m.UserId.on("click", function () {
            var inputVal = m.user_id.val();
            m.user_id.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("Submitter ID cannot be empty!", 5, "error");
                m.user_id.attr("status", "2").focus();
                return false;
            }
            m.user_id.val('Submitter ID');
        });

        //点击查找提交者姓名按钮事件
        m.UserName.on("click", function () {
            var inputVal = m.user_name.val();
            m.user_name.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("Submitter name cannot be empty!", 5, "error");
                m.user_name.attr("status", "2").focus();
                return false;
            }
            m.user_name.val('Submitter name');
        });

        //点击查找提交者角色按钮事件
        m.UserRole.on("click", function () {
            var inputVal = m.user_role.val();
            m.user_role.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("Submitter privilege name cannot be empty!", 5, "error");
                m.user_role.attr("status", "2").focus();
                return false;
            }
            m.user_role.val('Submitter privilege');
        });


        //检索按钮点击事件
        m.search.on("click", function () {

                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val(); ;
                tableGrid.setting("url", url_left + "/getSubbmitterList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);

                tableGrid.loadData();
        });

    }

    // 拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={
            userId:m.user_id.val(),
            userName:m.user_name.val(),

            userStatus:m.user_role.val(),

        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }



    //表格初始化-我的提交收据
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Submissions Details",
            param:paramGrid,
            colNames:["User ID","User Name","User Status","Email","Telephone No","Mobile Phone No","Gender","Address","Postal Code"],
            colModel:[
                {name:"userId",type:"text",text:"center",width:"130",ishide:true,css:"",getCustomValue:formatNum},
                {name:"userName",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"userStatus",type:"text",text:"center",width:"130",ishide:true,css:""},
                {name:"email",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"telephoneNo",type:"text",text:"center",width:"100",ishide:true,css:""},
                {name:"mobilephoneNo",type:"text",text:"center",width:"100",ishide:true,css:""},
                {name:"gender",type:"text",text:"center",width:"100",ishide:true,css:""},
                {name:"adress",type:"text",text:"center",width:"100",ishide:true,css:""},
                {name:"postalCode",type:"text",text:"center",width:"100",ishide:true,css:""},
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"effective_sts,cashier_id",//排序字段
            sord:"asc",//升序
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
                    butType:"custom",
                    butHtml:"<button id='selectSubbmitter' type='button' class='btn btn-primary''>Select</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='cancelSubbmitter' type='button' class='btn btn-primary'><a href=''>Cancel</a></button>"
                }
            ],
        });
    }


    //number格式化
    var formatNum = function (tdObj, value) {
        return $(tdObj).text(fmtIntNum(value));
    }
    // 日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        if (value != null && value.trim() != '' && value.length == 8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

    //格式化数字带千分位
    function fmtIntNum(val) {
        if (val == null || val == "") {
            return "0";
        }
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    //字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date) {
        var res = "";
        date = date.replace(/\//g, "");
        res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
        return res;
    }

    // 为0返回'0'
    function getString0(tdObj, value) {
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 日期处理
    function subfmtDate(date) {
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    self.init = init;
    return self;


});

var _start = require('start');
var _index = require('subbmitter');
_start.load(function (_common) {
    _index.init(_common);
});