require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('storeReason', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        flag = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        common = null;
    var m = {
        reset: null,
        search: null,
        cancel: null,
        reason_cd: null,//原因编号
        reason_name: null,//原因名称
        reason_type: null,//原因类型
        operateFlg: null,//操作类型
        i_reason_cd: null,//原因编号
        i_reason_name: null,//原因名称
        i_reason_type: null,//原因类型
        affirm: null
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/storeReason";
        systemPath = _common.config.surl;
        //初始化下拉
        getSelectValue();
        //事件绑定
        but_event();
        //列表初始化
        initTable1();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // m.search.click();
    }


    // 请求加载下拉列表
    function getSelectValue() {
        // 加载select
        initSelectOptions("Reason Type", "reason_type", "00395");
        initSelectOptions("Reason Type", "i_reason_type", "00395");
    }

    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url: systemPath + "/cm9010/getCode",
            async: true,
            cache: false,
            type: "post",
            data: "codeValue=" + code,
            dataType: "json",
            success: function (result) {
                var selectObj = $("#" + selectId);
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeValue + ' ' + result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error: function (e) {
                _common.prompt(title + " Failed to load data!", 5, "error");
            }
        });
    }

    //画面按钮点击事件
    var but_event = function () {
        //重置搜索
        m.reset.on("click", function () {
            m.reason_cd.val("");
            m.reason_name.val("");
            m.reason_type.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
        m.cancel.on("click",function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                $("#i_reason_cd").css("border-color", "#CCCCCC");
                $("#i_reason_name").css("border-color", "#CCCCCC");
                $("#i_reason_type").css("border-color", "#CCCCCC");
                if (result=="true"){
                    $('#storeReason_dialog').modal("hide");
                }
            })
        })
        //检索按钮点击事件
        m.search.on("click", function () {
            paramGrid = "reasonCd=" + m.reason_cd.val() +
                "&reasonName=" + m.reason_name.val() +
                "&reasonType=" + m.reason_type.val();
            tableGrid.setting("url", url_left + "/getList");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData(null);
        });

        //确认
        m.affirm.on("click", function () {
            var reasonCd = m.i_reason_cd.val();
            var reasonName = m.i_reason_name.val();
            var reasonType = m.i_reason_type.val();
            var operateFlg = m.operateFlg.val();
            if (reasonCd == null || reasonCd == '') {
                $("#i_reason_cd").css("border-color","red");
                _common.prompt("Reason No. cannot be empty!", 5, "info");/*原因编号不能为空*/
                m.i_reason_cd.focus();
                return false;
            }else{
                $("#i_reason_cd").css("border-color","#CCCCCC");
            }

            if (m.operateFlg.val() == '1') {
                flag = checkUnique(reasonCd);
                if (flag) {
                    $("#i_reason_cd").css("border-color","red");
                    _common.prompt('Reasons No. already exists!', 5, 'error');
                    m.i_reason_cd.focus();
                    return false;
                } else{
                    $("#i_reason_cd").css("border-color","#CCCCCC");
                }
            }
            if (reasonName == null || reasonName == '') {
                $("#i_reason_name").css("border-color","red");
                _common.prompt("Reason Name cannot be empty!", 5, "info");/*原因名称不能为空*/
                m.i_reason_name.focus();
                return false;
            }else{
                $("#i_reason_name").css("border-color","#CCCCCC");
            }
            if (reasonType == null || reasonType == '') {
                $("#i_reason_type").css("border-color","red");
                _common.prompt("Reason Category cannot be empty!", 5, "info");/*原因类型不能为空*/
                m.i_reason_type.focus();
                return false;
            }else{
                $("#i_reason_type").css("border-color","#CCCCCC");
            }
            _common.myConfirm("Are you sure you want to save?", function (result) {
                if (result == "true") {
                    $.myAjaxs({
                        url: url_left + "/save",
                        async: true,
                        cache: false,
                        type: "post",
                        data: {
                            reasonCd: reasonCd,
                            reasonName: reasonName,
                            reasonType: reasonType,
                            operateFlg: operateFlg
                        },
                        dataType: "json",
                        success: function (rest) {
                            var operateMsg = "";
                            if (operateFlg == "1") {
                                operateMsg = "Add"
                            } else if (operateFlg == "2") {
                                operateMsg = "Update"
                            }
                            if (rest.success) {
                                // _common.prompt(operateMsg + " Success！", 2, "info", function () {
                                _common.prompt("Data saved successfully！", 2, "info", function () {
                                    m.search.click();
                                    $('#storeReason_dialog').modal("hide");
                                });
                            } else {
                                _common.prompt(rest.message, 5, "error")
                            }
                        },
                        complete: _common.myAjaxComplete
                    });
                }
            });
        });
    }

    var checkUnique = function (reasonCd) {
        let flag = false;
        $.myAjaxs({
            url: url_left + "/checkUnique",
            async: false,
            cache: false,
            type: "get",
            data: {
                "reasonCd": reasonCd,
            },
            dataType: "json",
            success: function (rest) {
                if (!rest.success) {
                    flag = true;
                }
            },
            complete: _common.myAjaxComplete
        });
        return flag;
    }

    //表格按钮事件
    var table_event = function () {
        $("#add").on("click", function () {
            m.operateFlg.val("1");
            m.i_reason_cd.prop('disabled', false);
            m.i_reason_cd.val("");
            m.i_reason_name.val("");
            m.i_reason_type.find("option:first").prop("selected", true);
            $('#storeReason_dialog').modal("show");
        })
        $("#modify").on("click", function () {
            m.operateFlg.val("2");
            m.i_reason_cd.prop('disabled', true);
            if (selectTrTemp == null) {
                _common.prompt("Please select the data to modify!", 5, "info");/*请选择要修改的数据*/
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp, "reasonCd,reasonName,reasonType");
            m.i_reason_cd.val(cols['reasonCd']);
            m.i_reason_name.val(cols['reasonName']);
            m.i_reason_type.val(cols['reasonType']);
            $('#storeReason_dialog').modal("show");
        })
        $("#delete").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select the data to delete!", 5, "error");
                return false;
            }
            _common.myConfirm("Please confirm whether you want to delete the selected data？？", function (result) {
                if (result == "true") {
                    var cols = tableGrid.getSelectColValue(selectTrTemp, "reasonCd");
                    var reasonCd = cols['reasonCd'];

                    $.myAjaxs({
                        url: url_left + "/delete",
                        async: true,
                        cache: false,
                        type: "post",
                        data: {
                            reasonCd: reasonCd
                        },
                        dataType: "json",
                        success: function (rest) {
                            if (rest.success) {
                                _common.prompt("Deleted succeed!", 2, "info", function () {
                                    m.search.click();
                                });
                            } else {
                                _common.prompt(rest.message, 5, "error")
                            }
                        },
                        complete: _common.myAjaxComplete
                    });
                }
            });
        })
    }

    //表格初始化-门店原因列表样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Query Result",
            param: paramGrid,
            colNames: ["Reason No.", "Reason Category Cd", "Reason Category", "Reason Name"],
            colModel: [
                {name: "reasonCd", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "reasonType", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "reasonTypeName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "reasonName", type: "text", text: "left", width: "130", ishide: false, css: ""},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            sidx: "reason_cd",//排序字段
            sord: "asc",//升降序
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
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
                {
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""
                },
                {
                    butType: "update",
                    butId: "modify",
                    butText: "Modify",
                    butSize: ""
                }, {
                    butType: "delete",
                    butId: "delete",
                    butText: "Delete",
                    butSize: ""
                }
            ]
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var storeReasonButAdd = $("#storeReasonButAdd").val();
        if (Number(storeReasonButAdd) != 1) {
            $("#add").remove();
        }
        var storeReasonButEdit = $("#storeReasonButEdit").val();
        if (Number(storeReasonButEdit) != 1) {
            $("#modify").remove();
        }
        var storeReasonButDel = $("#storeReasonButDel").val();
        if (Number(storeReasonButDel) != 1) {
            $("#delete").remove();
        }
    }

    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }


    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    // 格式化数字类型的日期
    function fmtDate(date) {
        var res = "";
        res = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        return res;
    }

    function subfmtDate(date) {
        var res = "";
        if (date != null && date != "") {
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeReason');
_start.load(function (_common) {
    _index.init(_common);
});
