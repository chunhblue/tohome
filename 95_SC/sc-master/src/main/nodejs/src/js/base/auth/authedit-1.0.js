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
        o_name:null,
        o_pass:null,
        pe_date:null,
        ef_date: null,
        ex_date: null,
        o_status:null,
        returnsViewBut:null,
        submit:null
    }

    // 创建js对象
    var createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/operator";
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }
        // console.log(m.use.val());
        //根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        // initPageBytype("1");
        //表格内按钮事件
        table_event();

        // $("#update").attr("disabled", "disabled");
        // $("#view").attr("disabled", "disabled");
    }

    //表格内按钮点击事件
    var table_event = function(){

    }

    //画面按钮点击事件
    var but_event = function(){
        //密码失效日期
        m.pe_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        //账号生效日期
        m.ef_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        //账号失效日期
        m.ex_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        //返回一览
        m.returnsViewBut.on("click",function(){
            top.location = url_left;
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
                        url:url_left+"/updateOperator",
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
    //验证检索项是否合法
    var verifySearch = function(){
        return true;
    }
    //拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={

        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }
    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                initTable2();//列表初始化
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }
    //表格初始化-采购样式
    var initTable1 = function(){
        var data = '[{"sc1":"20000790","sc2":"29835926539265","sc3":"麻子大豆油","sc4":"500ml","sc5":"组","sc6":"500.00","sc7":"0.000","sc8":"100.000","sc9":"13.000","sc10":"0.000","sc11":"613.000","sc12":"231.000","sc13":"321.000"}]';
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Item Code","Item Barcode","Item Name","Specification","UOM","Inventory Quantity(Previous Day)","Sales Quantity of the Day","Receiving Quantity of the Day","Inventory Adjustment Quantity of the Day","Quantity in Transit","Stock on Hand Quantity","Write-off Quantity","Transfer Quantity"],
            colModel:[
                {name:"sc1",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"sc2",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"sc3",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"sc4",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"sc5",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:null},
                {name:"sc6",type:"text",text:"center",width:"230",ishide:false,css:"",getCustomValue:null},
                {name:"sc7",type:"text",text:"center",width:"200",ishide:false,css:"",getCustomValue:null},
                {name:"sc8",type:"text",text:"center",width:"200",ishide:false,css:"",getCustomValue:null},
                {name:"sc9",type:"text",text:"center",width:"300",ishide:false,css:"",getCustomValue:null},
                {name:"sc10",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"sc11",type:"text",text:"center",width:"180",ishide:false,css:"",getCustomValue:null},
                {name:"sc12",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"sc13",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null}
            ],//列内容
            traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
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

            ],
        });
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
