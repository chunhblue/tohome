require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _datetimepicker = require("datetimepicker");
var _myAjax=require("myAjax");
define('pogShelfListView', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        reThousands = null,
        toThousands = null,
        getThousands = null,
        common=null;
    const KEY = 'POG_SHELF_MAINTENANCE_MANAGEMENT';
    var m = {
        toKen : null,
        use : null,
        identity : null,
        searchJson:null,
        // 页面信息
        returnsViewBut:null,
        pogCd:null,
        storeCd:null,
        // 弹窗信息
        input_pogCd:null,
        input_pogName:null,
        input_shelf:null,
        input_Sub_Shelf:null,
    };
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/shelfMaintenance/view";
        url_root=_common.config.surl+"/shelfMaintenance";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){

        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        //表格内按钮事件
        table_event();
        flushItems();
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    //表格按钮事件
    var table_event = function(){
        // //返回一览
        m.returnsViewBut.on("click",function(){
            _common.myConfirm("Are you sure to exit?",function(result) {
                if (result==="true") {
                    _common.updateBackFlg(KEY);
                    top.location = url_root;
                }
            });
        });
        $("#POGShelfDetails").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            itemClear();
            //设置商品信息
            setDialogValue();
            //禁用窗口信息
            $("#affirm").attr("disabled","true");
            $('#update_dialog').modal("show");
        });
        //弹出窗 确认取消按钮
        $("#cancel").on("click",function(){
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if(result=="true") {
                    $("#update_dialog").modal("hide");
                }
            });
        });
    };
    var itemClear = function () {
        m.input_pogCd.val('');
        m.input_pogName.val('');
        m.input_shelf.val('');
        m.input_Sub_Shelf.val('');
    };


    //拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={
            pogCd : m.pogCd.val(),
            storeCd : m.storeCd.val()
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    };

    //请求单据商品信息
    var flushItems = function () {
        //Ajax请求商品
        setParamJson();
        paramGrid = "searchJson=" + m.searchJson.val();
        tableGrid.setting("url", url_left +"/getDetailData");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData(null);
    };
    var setDialogValue = function () {
        var cols = tableGrid.getSelectColValue(selectTrTemp,"pogCd,pogName,shelf,subShelf");
        m.input_pogCd.val(cols['pogCd']);
        m.input_pogName.val(cols['pogName']);
        m.input_shelf.val(cols['shelf']);
        m.input_Sub_Shelf.val(cols['subShelf']);
    }

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"POG Shelf Detail",
            param:paramGrid,
            localSort: true,
            colNames:["Department","Document Name","Shelf","Sub-Shelf"],
            colModel:[
                {name:"pogCd",type:"text",text:"right",width:"100",ishide:true,css:""},
                {name:"pogName",type:"text",text:"left",width:"80",ishide:false,css:""},
                {name:"shelf",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"subShelf",type:"text",text:"right",width:"100",ishide:false,css:""},
            ],//列内容
            // traverseData:data,
            width:"max",//宽度自动
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
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                {
                    butType: "view",
                    butId: "POGShelfDetails",
                    butText: "View",
                    butSize: ""//,
                }
            ]
        });
    };


    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('pogShelfListView');
_start.load(function (_common) {
    _index.init(_common);
});
