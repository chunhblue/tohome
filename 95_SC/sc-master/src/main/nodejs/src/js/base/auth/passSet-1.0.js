require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('nonoperatingList', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;

    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson: null,
        o_id:null,
        o_pass:null,
        o_rpass:null,
        submit:null,
        reset:null
    }

    // 创建js对象
    var createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/passwordSet";
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }
    }

    //画面按钮点击事件
    var but_event = function(){
        //重置
        m.reset.on("click",function(){
            m.o_id.val("");
            m.o_pass.val("");
            m.o_rpass.val("");
        });
        //提交按钮点击事件
        m.submit.on("click",function(){
            _common.myConfirm("Are you sure you want to save",function(result){
                if(result=="true"){
                    _common.prompt("Saving data!",5,"error");
                    return;

                    var cols = tableGrid.getSelectColValue(selectTrTemp,"item_code");
                    var mdVoucherNo = cols['md_cd'];
                    var identity = m.identity.val();

                    $.myAjaxs({
                        url:url_left+"/updatePassword",
                        async:true,
                        cache:false,
                        type :"get",
                        data :"o_id="+o_id+"&identity="+identity+"&toKen="+m.toKen.val(),
                        dataType:"json",
                        success:showResponse,
                        complete:_common.myAjaxComplete
                    });
                }
            });
        });
    }
    //拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={

        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date){
        var res = "";
        date = date.replace(/\//g,"");
        res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
        return res;
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('nonoperatingList');
_start.load(function (_common) {
    _index.init(_common);
});
