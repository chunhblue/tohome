require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('operatorLog', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {};
    var m = {
        reset: null,
        toKen: null,
        use: null,
        userId: null,
        userName: null,
        menuName: null,
        tableGrid: null,
        search: null,//检索按钮
        searchJson: null,
        startDate: null,
        endDate: null,
        startTime: null,
        endTime: null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/operatorLog";

        initTable1();

        but_event();
        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
    }


    // 拼接搜素参数
    var but_event = function () {
        m.reset.on("click", function () {
            m.userName.val("");
            m.menuName.val("");
            m.userId.val("");
            m.startDate.val("");
            m.endDate.val("");
            m.startTime.val("");
            m.endTime.val("");
            selectTrTemp = null;
            _common.clearTable();
        })

        m.search.on("click", function () {
            if(verifySearch()){
                paramSentence();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + '/search');
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });

        //开始时间
        m.startTime.datetimepicker(
            {
                language: 'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: false,//今日按钮
                format: 'hh:ii:ss',
                startView: 'day',// 进来是月
                minView: 'hour',// 可以看到小时
                maxView: 1,
                minuteStep:1, //分钟间隔为1分
                todayHighlight: false,
                forceParse: true,
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#endTime").datetimepicker('setStartDate', ev.date)
            } else {
                $("#endTime").datetimepicker('setStartDate', null);
            }
        });

        //结束时间
        m.endTime.datetimepicker(
            {
                language: 'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: false,//今日按钮
                format: 'hh:ii:ss',
                startView: 'day',// 进来是月
                minView: 'hour',// 可以看到小时
                maxView: 1,
                minuteStep:1, //分钟间隔为1分
                todayHighlight: false,
                forceParse: true,
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#startTime").datetimepicker('setEndDate', ev.date)
            } else {
                $("#startTime").datetimepicker('setEndDate', null);
            }
        });
    };
    //验证检索项是否合法
    var verifySearch = function(){
        if(m.startDate.val()!==""&&m.startDate.val()!=null){
            if(_common.judgeValidDate(m.startDate.val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#startDate").focus();
                return false;
            }
        }
        if(m.endDate.val()!==""&&m.endDate.val()!=null){
            if(_common.judgeValidDate(m.endDate.val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#endDate").focus();
                return false;
            }
        }
        if(m.startDate.val()!=""&&m.endDate.val()!="") {
            var _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
            var _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
            if(_StartDate>_EndDate){
                _common.prompt("The start date cannot be greater than the end date!!", 5, "error"); //开始时间不能大于结束时间
                $("#endDate").focus();
                return false;
            }

            var difValue = parseInt(Math.abs((_EndDate - _StartDate) / (1000 * 3600 * 24)));
            if (difValue > 62) {
                _common.prompt("Query Period cannot exceed 62 days!", 5, "error"); // 日期期间取值范围不能大于62天
                $("#endDate").focus();
                return false;
            }
        }
        if(m.startTime.val()!="") {
            if(_common.judgeValidTime($("#startTime").val())){
                _common.prompt("Please enter a valid time!",3,"info");
                $("#startTime").css("border-color","red");
                $("#startTime").focus();
                return false;
            }else {
                $("#startTime").css("border-color","#CCCCCC");
            }
        }
        if(m.endTime.val()!="") {
            if(_common.judgeValidTime($("#endTime").val())){
                _common.prompt("Please enter a valid time!",3,"info");
                $("#endTime").css("border-color","red");
                $("#endTime").focus();
                return false;
            }else {
                $("#endTime").css("border-color","#CCCCCC");
            }
        }

        if(m.startTime.val()!=""&&m.endTime.val()!="") {
            var _startTime = parseInt(formatTime($("#startTime").val()));
            var _endTime = parseInt(fmtDate($("#endDate").val()));
            if (_startTime > _endTime) {
                _common.prompt("The start date cannot be greater than the end date!!", 5, "error"); // 日期期间取值范围不能大于62天
                $("#endDate").focus();
                return false;
            }
        }
        return true;
    };
    var paramSentence = function () {
        let userName = m.userName.val().trim();
        let menuName = m.menuName.val().trim();
        let userId = m.userId.val().trim();
        let startYmd = formatDate(m.startDate.val());
        let endYmd = formatDate(m.endDate.val());
        let startHms = formatTime(m.startTime.val());
        let endHms = formatTime(m.endTime.val());
        let searchJson = {
            'userName': userName,
            'menuName': menuName,
            'userId': userId,
            'startYmd': startYmd,
            'endYmd': endYmd,
            'startHms': startHms,
            'endHms': endHms,
        }
        m.searchJson.val(JSON.stringify(searchJson));
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

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        }
        return $(tdObj).text(value);
    };

    // 时间字段格式化格式
    var timeFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==6) {
            value = value.replace(/^(\d{2})(\d{2})(\d{2})$/, '$1:$2:$3');
        }
        return $(tdObj).text(value);
    };
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    //表格初始
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Query Result",
            param: paramGrid,
            localSort: true,
            lineNumber: false,
            colNames: ["No.","User ID", "User Name", "Menu Name", "Operation Date", "Operation Time"],
            colModel: [
                {name: "rowNum", type: "text", text: "center", width: "30", ishide: false,},
                {name: "userId", type: "text", text: "right", width: "130", ishide: false,},
                {name: "userName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "menuName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "operateYmd", type: "text", text: "center", width: "130", ishide: false, getCustomValue: dateFmt},
                {name: "operateHms", type: "text", text: "center", width: "130", ishide: false, getCustomValue: timeFmt},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            }
        });
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('operatorLog');
_start.load(function (_common) {
    _index.init(_common);
});
