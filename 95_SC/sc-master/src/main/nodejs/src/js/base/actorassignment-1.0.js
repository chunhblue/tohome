require("zgGrid");
require("zgGrid.css");
require("myAutomatic");
require("myAutoComplete.css");
require("myValidator.css");
require("bootstrap-datetimepicker.css");
var _myAjax = require("myAjax");
var _myValidator = require("myValidator");
var _datetimepicker = require("datetimepicker");
define('actorassignment', function () {
    var self = {},
        url_left = "ua",
        tableGrid = null,
        paramGrid = "",
        common = null,
        roleName = "",
        userId = "",
        d_roleId = null,
        d_userId = "",
        p_userId = "";

    function init(_common) {
        token = $("#toKen");
        e_id = $("#e_id");
        common = _common;
        _myValidator.init();
        initSearchAutomatic();
        setParamGrid();
        initTable();
        execute();
        initListPageBut();

        if ($("#editPer").val() == "2") {
            $('#add').remove();
            $('#update').remove();
        }
        if ($('#cancelPer').val() == "2") {
            $('#del').remove();
        }
    }

    var setParamGrid = function () {
        paramGrid = "";
        var roleId = $("#roleName").attr("k");
        if (roleName != "") {
            paramGrid += "&roleId=" + roleId;
        }
        var userId = $("#userId").attr("k");
        if (userId != "") {
            paramGrid += "&userId=" + userId;
        }
        var assType = $("input[name='assType']:checked").val();
        if (assType != "") {
            paramGrid += "&assType=" + assType;
        }
    }
    var initListPageBut = function () {
        $("#del").prop("disabled", "disabled");
        $("#update").prop("disabled", "disabled");
    }
    //设定弹出窗中 元素是否可用，添加和修改分别有不同的展示效果
    var diaLogStyleSet = function (flg) {
        clearEditDialog();
        if (flg == "add") {
            //添加
            $("#add_to_store").show();
            $("#edit_dialog").find("a[class*='a-up-hide']").show();
        } else {
            //修改
            $("#add_to_store").hide();
            $("#edit_dialog").find("a[class*='a-up-hide']").hide();
        }
    }
    //事件挂载
    var execute = function () {
        $("#search").on("click", function () {
            setParamGrid();
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData(null);
        });
        // 添加授权
        $("#add").on("click", function () {
            diaLogStyleSet("add");
            $('#edit_dialog').modal("show");

        });
        // 修改授权
        $("#update").on("click", function () {
            if (selectTrObj == null) {
                _common.prompt("Please select content to edit!", 5, "info");
            } else {
                diaLogStyleSet("up");
                var cols = tableGrid.getSelectColValue(selectTrObj, "id,roleId,roleName,userId,userName,startDate,endDate");
                e_id.val(cols['id']);
                $.myAutomatic.setValueTemp(d_roleId, cols['roleId'], cols['roleName']);//赋值
                $.myAutomatic.setValueTemp(d_userId, cols['userId'], cols['userId'] + " " + cols['userName']);//赋值
                if (cols['startDate'] != null && cols['startDate'] != "") {
                    $("#startDate").val(cols['startDate']);
                    $("#endDate").val(cols['endDate']);
                    $("#d_assType1").attr("checked", true);
                    $("#startDateDiv").show();
                    $("#endDateDiv").show();
                    $("#blueline").show();
                }

                $('#edit_dialog').modal("show");
            }
        });
        // 取消授权
        $("#del").on("click", function () {
            if (selectTrObj == null) {
                _common.prompt("Please select content to edit!", 5, "info");
            } else {
                common.myConfirm("Are you sure to cancel the authorization? Data cannot be recovered after cancellation!", function (result) {/*确认取消该授权么？取消后数据将无法恢复!*/
                    if (result == "true") {
                        var cols = tableGrid.getSelectColValue(selectTrObj, "id");
                        $.myAjaxs({
                            url: url_left + "/delete",
                            async: false,
                            cache: false,
                            type: "post",
                            data: "aaId=" + cols['id'] + "&toKen=" + $("#toKen").val(),
                            dataType: "json",
                            success: showResponse,
                            complete: _common.myAjaxComplete
                        });
                    }
                });
            }
        });

        //编辑弹出窗 确认提交按钮
        $("#affirm").on("click", function () {
            //验证
            if (verifyDialog()) {
                common.myConfirm("Are you sure you want to submit?",function(result){
                    if(result=="true"){
                        $.myAjaxs({
                            url: url_left + "/edit",
                            async: false,
                            cache: false,
                            type: "post",
                            data: getAffirmParam(),
                            dataType: "json",
                            success: showResponse,
                            complete: _common.myAjaxComplete
                        });
                    }
                })
            }
        });

        //弹出窗 确认取消按钮
        $("#cancel").on("click",function(){
            common.myConfirm("Are you sure you want to close?",function(result){
                if(result=="true") {
                    var p_userId = $("#p_userId").val();
                    $("#p_userId").css("border-color", "#CCCCCC");
                    var d_userId = $("#d_userId").val();
                    $("#d_userId").css("border-color", "#CCCCCC");
                    var d_roleId = $("#d_roleId").val();
                    $("#d_roleId").css("border-color", "#CCCCCC");
                    var startDate = $("#startDate").val();
                    $("#startDate").css("border-color", "#CCCCCC");
                    var endDate = $("#endDate").val();
                    $("#endDate").css("border-color", "#CCCCCC");
                    clearEditDialog();
                    $("#edit_dialog").modal("hide");
                }
            })
        });

        $("input[name='d_assType']").on("click", function (e) {
            if (Number($(this).val()) == 1) {
                $("#startDateDiv").show();
                $("#endDateDiv").show();
                $("#blueline").show();
                $("#p_user").show();
            } else {
                $("#startDateDiv").hide();
                $("#endDateDiv").hide();
                $("#blueline").hide();
                $("#p_user").hide();
            }
        })

    }

    function showResponse(data, textStatus, xhr) {
        var resp = xhr.responseJSON;
        if (resp.result == false) {
            top.location = resp.s + "?errMsg=" + resp.errorMessage;
            return;
        }
        if (data.success == true) {
            _common.prompt("Operation Succeeded!", 2, "success", function () {
                $('#edit_dialog').modal("hide");
                clearEditDialog();
                $("#search").click();
            }, true);
        } else {
            _common.prompt(data.message, 5, "error");
        }
        $("#toKen").val(data.toKen);
    }

    //整理提交参数
    var getAffirmParam = function () {
        var param = "";
        param += "&id=" + e_id.val();
        param += "&roleId=" + d_roleId.attr("k");
        param += "&userId=" + d_userId.attr("k");
        param += "&startDate=" + $("#startDate").val();
        param += "&endDate=" + $("#endDate").val();
        param += "&toKen=" + token.val();
        param += "&assType=" + $("input[name='d_assType']:checked").val();
        if (p_userId != null && p_userId != "" && typeof(p_userId) != "undefined" && typeof(p_userId.attr("k")) != "undefined") {
            param += "&assUserId=" + p_userId.attr("k");
        }
        param += "&toKen=" + $("#toKen").val();
        return param;
    }

    // 验证弹出窗内是否还有未完成输入的内容，如果存在则给出提交，并终止执行
    var verifyDialog = function () {
        var str = "d_userId,d_roleId";
        if (Number($("input[name='d_assType']:checked").val()) == 1) {
            str = str + ",startDate,endDate"
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            if (new Date(startDate).getTime() > new Date(endDate).getTime()) {
                _common.prompt("Start date is greater than end date, cannot submit!", 5, "info");/*代审开始日期大于代审结束日期，不可提交*/
                return false;
            }
        }
        var isValIdtion = _myValidator.startValidator(str);
        return isValIdtion;
    }

    //清空编辑弹出框的所有内容
    var clearEditDialog = function () {
        $.myAutomatic.cleanSelectObj(d_roleId);
        $.myAutomatic.cleanSelectObj(d_userId);
        $("#startDate").val("");
        $("#endDate").val("");
        $("#edit_dialog").find("div[class*='add-to']").remove();
        e_id.val("");
    }

    //检索项 自动下拉 初始化
    var initSearchAutomatic = function () {
        roleName = $("#roleName").myAutomatic({
            url: "role/getrolename",
            ePageSize: 10,
            startCount: 0,
        });
        userId = $("#userId").myAutomatic({
            url: url_left + "/u",
            ePageSize: 10,
            startCount: 0,
        });
        p_userId = $("#p_userId").myAutomatic({
            url: url_left + "/u",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                var str = "&userId=" + thisObj.attr("k");
                $.myAutomatic.replaceParam(d_roleId, str);//赋值
            }
        });
        d_roleId = $("#d_roleId").myAutomatic({
            url: "role/getrolename",
            ePageSize: 10,
            startCount: 0,
            isTrim: false
        });

        d_userId = $("#d_userId").myAutomatic({
            url: url_left + "/u",
            ePageSize: 10,
            startCount: 0,
        });
        $("#startDate").datetimepicker({
            language:'en',
            format: 'yyyy-mm-dd',
            maxView: 4,
            startView: 2,
            minView: 2,
            startDate: new Date(),
            autoclose: true,
            todayHighlight: true,
            todayBtn: true,
        });
        $("#endDate").datetimepicker({
            language:'en',
            format: 'yyyy-mm-dd',
            maxView: 4,
            startView: 2,
            minView: 2,
            startDate: new Date(),
            autoclose: true,
            todayHighlight: true,
            todayBtn: true,
        });

    }

    //表格初始化
    var initTable = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            // title: "特殊角色授权",
            title: "Special role authorization",
            url: url_left + "/list",
            param: paramGrid,
            // colNames: ["ID", "roleid", "角色名称", "Role Description", "用户ID", "用户名称", "代审开始日期", "代审结束日期"],
            colNames: ["ID", "roleid", "Privilege Name", "Privilege Description", "User ID", "User Name", "Start Date", "End Date"],
            colModel: [
                {name: "id", type: "int", text: "right", width: "1", ishide: true},
                {name: "roleId", type: "int", text: "right", width: "1", ishide: true},
                {name: "roleName", type: "text", text: "left", width: "200", ishide: false},
                {name: "remark", type: "text", text: "left", width: "200", ishide: false},
                {name: "userId", type: "text", text: "right", width: "100", ishide: false},
                {name: "userName", type: "text", text: "left", width: "100", ishide: false},
                {name: "startDate", type: "text", text: "center", width: "150", ishide: false},
                {name: "endDate", type: "text", text: "center", width: "150", ishide: false}
            ],//列内容
            width: "max",//宽度自动
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            sidx: "id",//排序字段
            sord: "desc",//升降序
            page: 1,//当前页
            loadCompleteEvent: function (self) {
                //数据加载完成后
                initListPageBut();//按钮与选中的tr初始化
                selectTrObj = null;
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                butPermissions(trObj);
            },
            buttonGroup: [
                {butType: "add", butId: "add", butText: "Add", butSize: ""},//增加 默认："",lg:最大，sm小，xs超小 添加授权
                {butType: "update", butId: "update", butText: "Modify", butSize: ""},//修改 修改授权
                {butType: "delete", butId: "del", butText: "Delete", butSize: ""}
            ],

        });
    }
    //按钮初始化
    var butPermissions = function (trObj) {
        selectTrObj = trObj;//将选择的行赋给外部变量待用
        var buts = $("#zgGridTtable_but_box button");
        buts.prop("disabled", "disabled");
        buts.each(function () {
            var thisBut = $(this);
            thisBut.removeAttr("disabled");
        });
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('actorassignment');
_start.load(function (_common) {
    _index.init(_common);
});
