require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('stockTakeNonList', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        file = $('<input type="file"/>');
        common=null;
    const KEY = 'ITEM_MASTER_QUERY';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson: null,
        itemCode : null,
        itemName : null,
        barcode : null,
        topDepCd : null,
        // depCd : null,
        category : null,
        subCategory : null,
        search: null,
        reset: null,
        dep:null,
        pma:null

    };

    // 创建js对象
    var createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = url_root + "/stockTakeNonCount";
        // 首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        // 表格内按钮事件
        table_event();
        // 初始化分类选择
        _common.initCategoryAutoMatic();
    }

    // 表格内按钮点击事件
    var table_event = function(){

        $("#importFiles").on("click",function(){
            file.click();
            file.change(function(e){
                var select_file = file[0].files[0];
                let suffix = new RegExp(/[^\.]+$/).exec(select_file.name)[0];
                let excel_reg = /([xX][lL][sS][xX]){1}$|([xX][lL][sS]){1}$/;
                if (!excel_reg.test(suffix)) {
                    _common.prompt('Upload file format error!',5,"error");
                    file.val('');
                    return;
                }
                var formData = new FormData();
                formData.append('fileData',select_file);
                formData.append("toKen",m.toKen.val());
                // 确定要导入吗？
                _common.myConfirm("Are you sure you want to import?",function(result) {
                    if (result !== "true") {
                        file.val('');
                        return false;
                    }
                    $.myAjaxs({
                        url:url_left+"/uploadFile",
                        async:false,
                        cache:false,
                        type :"post",
                        data :formData,
                        dataType:"json",
                        processData : false, // 使数据不做处理
                        contentType : false, // 不要设置Content-Type请求头
                        success: function(data){
                            file.val('');
                            if (data.success) {
                                _common.prompt(data.message,5,"success");
                            } else {
                                _common.prompt(data.message,5,"error");
                            }
                        },
                        error:function(){
                            file.val('');
                            _common.prompt("Upload fail!",5,"error");
                        }
                    });
                });
            })
        })
    }

    // 画面按钮点击事件
    var but_event = function(){
        // 清空按钮
        m.reset.on("click",function(){
            m.itemCode.val("");
            m.itemName.val("");
            $("#depRemove").click();
            selectTrTemp = null;
            m.searchJson.val("");
            _common.clearTable();
        });
        // 检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/getList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        return true;
    }

    // 拼接检索参数
    var setParamJson = function(){
        // 创建请求字符串
        var searchJsonStr ={
            articleId:m.itemCode.val().trim(),
            articleName:m.itemName.val().trim(),
            depCd:m.dep.attr("k"),
            pmaCd:m.pma.attr("k"),
            categoryCd:m.category.attr("k"),
            subCategoryCd:m.subCategory.attr("k")
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
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }
    // 表格初始化-采购样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",//查询结果
            param:paramGrid,
            localSort: true,
            colNames:["Item Code","Item Name","Item Specification","UOM",
                "Top Department","Department","Category","Sub-Category","Created by","Date Created"],
            colModel:[
                {name:"articleId",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"spec",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"salesUnitId",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"depName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"categoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"subCategoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"createUserId",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"createYmd",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},
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
            },
            buttonGroup:[
                {butType: "upload",butId: "importFiles",butText: "Import Non-Count List",},
            ]
        });
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('stockTakeNonList');
_start.load(function (_common) {
    _index.init(_common);
});
