require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('clearInventory', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        file = $('<input type="file"/>'),
        selectTrTemp = null,
        tempTrObjValue = {};

    var m = {
        toKen: null,
        use: null,
        identity: null,
        _common: null,
        searchJson: null,
        businessDate: null,
        // 查询部分
        search: null,
        reset: null,
        // 查询条件
        dep: null,
        pma: null,
        category: null,
        subCategory: null,
        articleId: null,
        articleName: null,
        // input框
        inputItem: null,
        inputItemRefresh: null,
        inputItemClear: null,
        inputUom: null,
        inputSpec: null,
        inputOnHandQty: null,
    }

    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    // 页面初始化
    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = _common.config.surl + "/clearInventory";
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        }
        initAutomatic();
        // 加载下拉列表
        _common.initCategoryAutoMatic();
        // 初始化表格
        initTable1();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        but_event();
    }

    // 根据Store No.取得该店铺信息
    var initAutomatic = function(){
        m.inputItem.myAutomatic({
            url:url_left+"/getItemList",
            ePageSize: 10,
            startCount: 3,
            cleanInput: function() {
                clearInput();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") !== null && thisObj.attr("k") !== "") {
                    getItemInfo(thisObj.attr("k"));
                }
            },
        });
    };

    var getItemInfo = function (articleId) {
        $.myAjaxs({
            url:url_left+"/getItemInfo",
            async:false,
            cache:false,
            type :"post",
            data :"articleId="+articleId,
            dataType:"json",
            success:function(res){
                if (!res.success) {
                    _common.prompt(res.msg,5,"info");
                    return;
                }
                let data = res.o;
                m.inputSpec.val(data.spec);
                m.inputUom.val(data.uom);
                m.inputOnHandQty.val(toThousands(data.onHandQty));
            },
            complete:_common.myAjaxComplete
        });
    }

    var clearInput = function () {
        m.inputItem.val("").attr("k","").attr("v","");
        m.inputSpec.val("");
        m.inputUom.val("");
        m.inputOnHandQty.val("");
    }

    //表格内按钮点击事件
    var table_event = function () {
        // 提交
        $("#submit").on("click", function () {
            let articleId = m.inputItem.attr("k");
            let onHandQty = reThousands(m.inputOnHandQty.val());

            if (articleId === null || articleId === "") {
                _common.prompt('Please select a Item!',5,"error");
                return;
            }

            // 封装数据
            let obj = {
                "articleId": articleId,
                "onHandQty": onHandQty,
            }
            _common.myConfirm("Are you sure you want to save?",function(result){
                if(result!="true"){return false;}
                $.myAjaxs({
                    url:url_left+'/save',
                    async:true,
                    cache:false,
                    type :"post",
                    data :obj,
                    dataType:"json",
                    success:function(result){
                        if(!result.success){
                            _common.prompt(result.msg,5,"error");
                            return;
                        }
                        // 保存成功, 关闭弹框重新加载数据
                        _common.prompt(result.msg,5,"success");
                        $("#close").click();
                        m.search.click();
                    },
                    error : function(e){
                        _common.prompt("Data saved failed！",5,"error");/*保存失败*/
                    }
                });
            })

        });

        // 关闭弹出窗口
        $("#close").on("click", function () {
            clearInput();
            $('#update_dialog').modal("hide");
        });

        // 添加
        $("#add").on("click", function () {
            clearInput();
            $('#update_dialog').modal("show");
        });

        // 导出按钮点击事件
        $("#import").on("click",function(){
            file.click();
        });

        file.change(function(e){
            var select_file = file[0].files[0];
            let suffix = new RegExp(/[^\.]+$/).exec(select_file.name)[0];
            let excel_reg = /([xX][lL][sS][xX]){1}$|([xX][lL][sS]){1}$/;
            // let excel_reg = /([cC][sS][vV]){1}$/;
            if (!excel_reg.test(suffix)) {
                _common.prompt('Upload file format error!',5,"error");
                file.val('');
                return;
            }
            var formData = new FormData();
            formData.append('fileData',select_file);

            // 确定要导入吗？
            _common.myConfirm("Are you sure you want to import?",function(result) {
                if (result != "true") {
                    file.val('');
                    return false;
                }
                _common.loading();
                $.ajax({
                    url:url_left+"/import",
                    dataType:'json',
                    type:'POST',
                    async: true,
                    data: formData,
                    processData : false, // 使数据不做处理
                    contentType : false, // 不要设置Content-Type请求头
                    success: function(data){
                        file.val('');
                        if (data.success) {
                            _common.prompt('Upload success!',5,"success");
                            m.search.click();
                        }
                        _common.loading_close();
                    },
                    error: function () {
                        _common.loading_close();
                    }
                });
            });
        })
    }

    // 按钮权限验证
    var isButPermission = function () {
        var addButPm = $("#addButPm").val();
        if (Number(addButPm) != 1) {
            $("#add").remove();
        }
        var importButPm = $("#importButPm").val();
        if (Number(importButPm) != 1) {
            $("#import").remove();
        }
    }

    // 画面按钮点击事件
    var but_event = function () {
        //清空按钮
        m.reset.on("click", function () {
            $("#depRemove").click();
            m.articleId.val('');
            m.articleName.val('');
            selectTrTemp = null;
            _common.clearTable();
        });

        m.search.on("click", function () {
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            tableGrid.setting("url", url_left + '/inquire');
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData();
        });
    }

    //拼接检索参数
    var setParamJson = function () {
        let depCd = m.dep.attr('k');
        let pmaCd = m.pma.attr('k');
        let categoryCd = m.category.attr('k');
        let subCategoryCd = m.subCategory.attr('k');
        let articleId = m.articleId.val();
        let articleName = m.articleName.val();

        // 创建请求字符串
        var searchJsonStr = {
            'depCd': depCd,
            'pmaCd': pmaCd,
            'categoryCd': categoryCd,
            'subCategoryCd': subCategoryCd,
            'articleId': articleId,
            'articleName': articleName,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        }
        return $(tdObj).text(value);
    }

    //表格初始化-库存样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTable").zgGrid({
            param: paramGrid,
            title: "Query Result",
            colNames: ["Date", "Item Code",  "Item Name", "Spec", "UOM", "Inventory Quantity(Previous Day)"],
            colModel: [
                {name: "accDate", type: "text", text: "right", width: "50", getCustomValue:dateFmt},
                {name: "articleId", type: "text", text: "right", width: "70", ishide: false, css: ""},
                {name: "articleName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "spec", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "uom", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "onHandQty", type: "text", text: "left", width: "100", ishide: false, getCustomValue: getThousands},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox: false,
            freezeHeader:false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                if(resData.message != null && resData.message !== ""){
                    // 提示 没有连接上ES(库存异动)
                    common.prompt(resData.message, 5, "info");
                }
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {butType: "add",butId: "add",butText: "Add",butSize: ""},//新增
                {butType: "upload", butId: "import", butText: "Import", butSize: ""},
            ],
        });
    }


    //格式化数字带千分位
    function fmtIntNum(val){
        if(val==null||val==""){
            return "0";
        }
        var reg=/\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    //字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date) {
        var res = "";
        date = date.replace(/\//g, "");
        res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 日期处理
    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }
    // 去除千分位
    function reThousands(num) {
        num+='';
        return num.replace(/,/g, '');
    }

    function getThousands(tdObj, value) {
        var dit = 0;
        if(value !== ""){
            var str = value + "";
            if (str.indexOf(".") !== -1) {
                dit = value.toString().split(".")[1].length;
            }
        }
        var num = toThousands(value,dit);
        // 省略小数点后为0的数
        return $(tdObj).text(num);
    }

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num,dit) {
        if (!num) {
            return '0';
        }
        dit = typeof dit !== 'undefined' ? dit : 0;
        num = num + '';
        if(dit > 4){
            dit = 4;
        }
        num = parseFloat(num).toFixed(dit);

        var source = String(num).split(".");//按小数点分成2部分
        source[0] = source[0].replace(new RegExp('(\\d)(?=(\\d{3})+$)', 'ig'), "$1,");//只将整数部分进行都好分割
        num = source.join(".");//再将小数部分合并进来
        return num;
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('clearInventory');
_start.load(function (_common) {
    _index.init(_common);
});
