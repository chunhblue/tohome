require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('stocktakePlan', function () {
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
        searchJson:null,
        pd_start_date:null,
        pd_end_date:null,
        clear_pd_date:null,
        search : null,//检索按钮
        reset:null,
        st_cd:null,
        search_store_input:null,
        od_status:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/officeManagement";
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        console.log(m.use.val())
        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        initPageBytype("1");
        //表格内按钮事件
        table_event();
        m.search.click();
    }

    var table_event = function(){
        //进入添加
        $("#addPlan").on("click", function (e) {
            top.location = url_left+"/edit?identity="+m.identity.val();
        });

        $("#printPlan").on("click",function(){
            //setParamJson();
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });

        $("#exportInfo").on("click",function(){
            //setParamJson();
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });
        $("#exportItems").on("click",function(){
            //setParamJson();
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });
        var table_ck_all = $("#table_ck_all");
        table_ck_all.on("change",function(event){
            event.preventDefault();
            var ck = $(this).prop("checked");
            $("#zgGridTtable_tbody").find("input[type='checkbox']").prop("checked",ck);
        });
        $("#viewPlan").on("click",function(){
            var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
            if(selectObj==null||selectObj.length==0){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                checkOrRejectClick(1,null,selectObj);
            }
        });
        $("#invalidPlan").on("click",function(){
            var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
            if(selectObj==null||selectObj.length==0){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                checkOrRejectClick(1,null,selectObj);
            }
        });
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

    //画面按钮点击事件
    var but_event = function(){
        //开始日
        m.pd_start_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#pd_end_date").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
            } else {
                $("#pd_end_date").datetimepicker('setStartDate', null);
            }
        });
        //结束日
        m.pd_end_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#pd_start_date").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
            } else {
                $("#pd_start_date").datetimepicker('setEndDate', new Date());
            }

        });
        //清空日期
        m.clear_pd_date.on("click",function(){
            m.pd_start_date.val("");
            m.pd_end_date.val("");
        });

        //点击查找店铺编号按钮事件
        m.search_store_input.on("click",function(){
            var inputVal = m.st_cd.val();
            m.st_cd.text("");
            if($.trim(inputVal)==""){
                _common.prompt("Store No. cannot be empty!",5,"error");
                m.st_cd.attr("status","2").focus();
                return false;
            }
            // getItemInfoByItem1(inputVal,function(res){
            //     if(res.success){
            //         m.vendor_code.text($.trim(res.data.itemName));
            //         return true;
            //     }else{
            //         m.vendor_code.text("未取得供应商编号");
            //         _common.prompt(res.message,5,"error");
            //         return false;
            //     }
            // });
            m.st_cd.val('胡志明门店');
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                // tableGrid.setting("url",url_left+"/getdata");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);

            }else{
                $("#search").prop("disabled",true);
            }
            //判断当前操作人是否为事业部长或系统部，如果是，曾增加待审核数量等内容的刷新机制，其他身份不参与此操作
            if(m.identity.val()=="2"||m.identity.val()=="3"){
                refreshWaitingReview();
            }
        });
        m.reset.on("click",function(){
            m.pd_start_date.val("");
            m.pd_end_date.val("");
            m.st_cd.val("");
            m.od_status.val("");
        });
    }

    //表格初始化
    var initTable1 = function(){
        var data = '[{"pd_date":"12/12/2019","pd_cd":"DS180109000205","pd_start_date":"12/12/2019","pd_end_date":"12/12/2019","pd_organ":"盘点","pd_status":"已盘点","dj_type":"发票","remark":"请及时盘点"}]';
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Start Stocktaking",
            param:paramGrid,
            localSort: true,
            colNames:["Stocktaking Date","Stocktaking No.","Start Time","End Time","Stocktaking Store","Stocktaking Status","Document Type","Remarks"],
            colModel:[
                {name:"pd_date",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"pd_cd",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"pd_start_date",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"pd_end_date",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"pd_organ",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"pd_status",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"dj_type",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
                {name:"remark",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null}
            ],//列内容
            traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:false,
            eachTrClick: function (trObj) {//正常左侧点击
                $("#update").removeAttr("disabled");
            },
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                trClick_table1();
            },
            buttonGroup:[
                {
                    butType:"custom",
                    butHtml:"<button id='addPlan' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-plus'></span> Add</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='viewPlan' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-pencil'></span> View</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='invalidPlan' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-remove'></span> Invalid</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='printPlan' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"
                },{
                    butType: "download",
                    butId: "exportInfo",
                    butText: "Basic Information",
                    butSize: "",
                },{
                    butType: "download",
                    butId: "exportItems",
                    butText: "Export Item Inventory",
                    butSize: "",
                },
            ],
        });
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
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
var _index = require('stocktakePlan');
_start.load(function (_common) {
    _index.init(_common);
});
