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
        o_name:null,
        o_status:null,
        search: null,
        reset: null,
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
        $("#add").on("click", function () {
            top.location = url_left+"/edit?identity="+m.identity.val();
        });
        $("#update").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                top.location = url_left+"/edit?identity="+m.identity.val();
            }
        });
        //删除按钮
        $("#delete").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            _common.myConfirm("Are you sure you want to delete the selected data?",function(result){
                if(result=="true"){
                    _common.prompt("Deleting selected data!",5,"error");
                    return;

                    var cols = tableGrid.getSelectColValue(selectTrTemp,"item_code");
                    var mdVoucherNo = cols['md_cd'];
                    var identity = m.identity.val();

                    $.myAjaxs({
                        url:url_left+"/deleterrVoucherdetails",
                        async:true,
                        cache:false,
                        type :"get",
                        data :"md_cd="+mdVoucherNo+"&identity="+identity+"&toKen="+m.toKen.val(),
                        dataType:"json",
                        success:showResponse,
                        complete:_common.myAjaxComplete
                    });
                }
            });
        });

        $("#updatePass").on("click", function () {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                top.location = url_left+"/edit?identity="+m.identity.val();
            }
        });

    }

    //画面按钮点击事件
    var but_event = function(){
        //清空按钮
        m.reset.on("click",function(){
            m.o_name.val("");
            m.o_status.val("");
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
    //表格初始化
    var initTable1 = function(){
        var data = '[{"sc1":"007","sc2":"Administrator","sc3":"system","sc4":"正常"}]';
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:["Operator ID","Operator Name","Login ID","Operator Status"],
            colModel:[
                {name:"sc1",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"sc2",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"sc3",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"sc4",type:"text",text:"center",width:"100",ishide:false,css:""}
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
                {
                    butType:"custom",
                    butHtml:"<button id='add' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-plus'></span> Add</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='update' type='button' class='btn btn-info btn-sm''><span class='glyphicon glyphicon-pencil'></span> Modify</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='delete' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-remove'></span> Delete</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='updatePass' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-pencil'></span> Update Password</button>"
                }
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
