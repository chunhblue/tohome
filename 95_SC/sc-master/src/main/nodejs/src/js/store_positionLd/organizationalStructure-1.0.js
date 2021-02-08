require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("treeview");
define('organizationalStructure',function(){
    var self = {};
    var url_left = "",
        paramGrid = null,
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    var m = {
        searchJson:null,
        toKen : null,
        use : null,
        up : null,
        tableGrid : null,
        error_pcode : null,
        alert_div : null,//页面提示框
        search : null,//检索按钮
        main_box : null//检索按钮
    }

    // 创建js对象
    var  createJqueryObj = function() {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        initTable1();
        createJqueryObj();
        url_left=_common.config.surl+'/organizationalStructure';
        $.myAjaxs({
            url:url_left+"/list",
            async:true,
            cache:false,
            type :"get",
            data :"",
            dataType:"json",
            success:showResponse,
            complete:_common.myAjaxComplete,
        });
    }

    var showResponse = function(data,textStatus, xhr) {
        selectTrTemp = null;
        var resp = xhr.responseJSON;
        if (resp.result == false) {
            top.location = resp.s + "?errMsg=" + resp.errorMessage;
            return;
        }

        $('#tree').treeview({
            data: data.list,
            showCheckbox: false,   //是否显示复选框
            highlightSelected: true,    //是否高亮选中
            multiSelect: false,    //多选
            enableLinks: false,//必须在节点属性给出href属性
            color: "#010A0E",
            onNodeSelected(event,data){
                show(data.href)
            }
        })

        // 展示页面 ok！
        var show=function(data){
            paramGrid = "searchJson="+data;
            tableGrid.setting("url",url_left+'/lista');
            tableGrid.setting("param", paramGrid);
            tableGrid.loadData();
        }
    }

    var initTable1 = function() {
        tableGrid = $("#zgGridTable").zgGrid({
            title: "Organization Details",//查询结果
            param: paramGrid,
            localSort: true,
            lineNumber: false,
            colNames: ["Parent Organization ID","Parent Organization Name","Organization ID",
                "Organization Name","Status", "Level"],
            colModel: [
                {name: "adminStructureCd", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "adminStructureName", type: "text", text: "left", width: "150", ishide: false, css: ""},
                {name: "structureCd", type: "text", text: "right", width: "120", ishide: false, css: ""},
                {name: "structureName", type: "text", text: "left", width: "200", ishide: false, css: ""},
                {name: "effectiveSts", type: "text", text: "left", width: "90", ishide: false, css: "",getCustomValue:getEffectiveSts},
                {name: "structureLevel", type: "text", text: "right", width: "90", ishide: false, css: ""}
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage:false,//是否需要分页
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                return self;
            }
        });
    }

    function getEffectiveSts(tdObj, value) {
        var _value = "";
        switch (value) {
            case "10":
                _value = "Effective";
                break;
            case "00":
                _value = "Ineffective";
                break;
        }
        return $(tdObj).text( _value);
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('organizationalStructure');
_start.load(function (_common) {
    _index.init(_common);
});
