require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('storeposition', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    var m = {
        reset:null,
        toKen : null,
        use : null,
        up : null,
        job_code:null,
        job_title:null,
        entiretyBmStatus : null,
        clear_ys_date : null,
        clear_yy_date : null,
        clear_ck_date : null,
        item_name : null,
        bm_division : null,
        bm_department : null,
        tableGrid : null,
        selectTrObj : null,
        error_pcode : null,
        identity : null,
        reject_dialog : null,
        // show_user_info : null,//检索中用户部分
        // show_status_all : null,//bm商品状态-全部单选按钮
        // show_status_del : null,//手工删除单选按钮
        // show_status_reject : null,//驳回选按钮
        // show_select_div : null,//事业部auto
        // show_select_dpt : null,//DPTauto
        // bm_be_part : null,//BM所属下拉项
        // show_div : null,//操作人员信息-事业部
        // show_store : null,//操作人员信息-店铺
        // show_status : null,//BM商品状态
        // bm_be_part_div : null,//bm所属
        // bm_type_box : null,//bm类型-box
        // bm_type : null,//bm类型
        // show_bm_code_input : null,
        // input_item : null,
        // division : null,
        // dpt: null,
        jobTypeCd:null,//职务代码
        jobCatagoryName:null,//职务头衔
        effectiveSts:null,//状态
        search : null,//检索按钮
        main_box : null,//检索按钮
        p_code_buyer_del : null,
        p_code_excel : null,
        p_code_buyer_affirm : null,
        rejectreason : null,
        reject_dialog_cancel : null,
        reject_dialog_affirm : null,
        store : null,
        search_item_but : null,
        p_code_div_check:null,
        p_code_sys_del:null,
        p_code_sys_list_del:null,
        checkResources:null,
        tempTableType:null,
        searchJson:null,
        checkCount:null,
        urgencyCount:null,
        del_alert:null,
        //   bm_status_5_select : null,//bm商品状态下拉项
        pd_cd:null,
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
        url_left=_common.config.surl+"/officeManagement";
        // if(m.use.val()=="0"){
        //     but_event();
        // }else{
        //     m.error_pcode.show();
        //     m.main_box.hide();
        // }
        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        // initPageBytype("1");
        //表格内按钮事件
        // table_event();
        //m.search.click();
        //初始化表格

        initTable1();
        //初始有效
        initSelectOptions("Status","effectiveSts", "00220");
        //调用函数
        // show();
        but_event();
        //初始化查询加载 3/5
        // m.search.click();
        //table_event();

    }  // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:true,
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
                _common.prompt(title+"Failed to load data!",5,"error");
            }
        });
    }

    var show=function(){
        //  paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+'/getList');
        //  tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData();
    }

    // 拼接搜素参数
    var but_event=function () {
        m.reset.on("click",function () {
            m.job_title.val("");
            m.job_code.val("");
            m.effectiveSts.val("");
            selectTrTemp = null;
            _common.clearTable();
        })
        var paramSentence=function () {
            let jobTitle = m.job_title.val().trim();
            let jobCode=m.job_code.val().trim();
            var effectiveSts = m.effectiveSts.val();
            let searchJson={
                'effectiveSts':effectiveSts,
                'jobCatagoryName':jobTitle,
                'jobTypeCd':jobCode,
            }
            m.searchJson.val(JSON.stringify(searchJson));
        }
        m.search.on("click",function () {
            paramSentence();
            paramGrid = "searchJson=" + m.searchJson.val();
            tableGrid.setting("url",url_left+'/searchOne');
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData();
        });
    }
    //表格初始化-职务表格样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Position Code","Position Title","Status"],
            colModel:[
                {name:"jobTypeCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"jobCatagoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"effectiveSts",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getEffectiveSts},
            ],//列内容
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
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
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
    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    //格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        return res;
    }
    //格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeposition');
_start.load(function (_common) {
    _index.init(_common);
});
