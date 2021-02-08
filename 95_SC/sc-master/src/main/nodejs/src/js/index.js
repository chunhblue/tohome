require("bootstrap-datetimepicker.css");
require("zgGrid.css");
require("zgGrid");

define('index', function () {
    var self = {};
    var systemPath = "",
        paramGrid = null,
        selectTrTemp = null;

    var m = {
        main_box: null,
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
        console.log(_common);
        $(".index-message").text(1);

        //初始化表格
        initInformTable();
        //初始化表格数据
        initTableData();
        //加载表格事件
        table_event();
    }
    var initTableData = function () {
        //加载没有回复的消息
        tableGrid.setting("url",systemPath+"/getTodoTask");
        tableGrid.loadData(null);
    }

    //表格事件
    var table_event = function () {
        m.main_box.on("click","a[class*='task']",function(){
            var url = $(this).parent().prev().text();
            top.location = systemPath+url;
        });
        $("#refresh").on("click",function () {
            tableGrid.loadData(null);
        })
    }

    var initInformTable = function(){
        tableGrid = $("#informTable").zgGrid({
            title:"TODO Tasks",
            param:paramGrid,
            colNames:["url","Title","Count"],
            colModel:[
                {name:"url",type:"text",text:"left",width:"100",ishide:true,css:""},
                {name:"notificationTitle",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:titleFmt},
                {name:"notificationCount",type:"int",text:"center",width:"70",ishide:false,css:"",getCustomValue:zeroFmt}
            ],//列内容
            width:"max",//宽度
            isPage:false,//是否需要分页
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
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
                    butHtml:"<button id='refresh' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-refresh'></span> Refresh</button>"
                }
            ]
        });
    }

    // 为0返回'0'
    var zeroFmt =  function(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    var titleFmt = function(tdObj, value){
        var html = '<a href="javascript:void(0);" title="'+value+'" class="task" "><span class="glyphicon glyphicon-expand icon-right"></span>'+value+'</a>';
        return $(tdObj).html(html);
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('index');
_start.load(function (_common) {
    _index.init(_common);
});
