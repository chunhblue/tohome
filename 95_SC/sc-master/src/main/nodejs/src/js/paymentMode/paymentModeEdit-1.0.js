require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('paymentModeEdit', function () {
    var self = {};
    var url_left = "",
        url_back = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        a_store = null,
        common = null;
    const KEY = 'POS_PAYMENT_METHODS_MANAGEMENT';

    var m = {
        toKen: null,
        reset: null,
        userId: null,
        deleteDelData: null,
        userName: null,
        operateFlgByDialog: null,//Dialog操作类型
        operateFlg: null,//页面操作类型
        h_payCd: null,//隐藏支付编号
        payCd: null,//支付编号
        payName: null,//支付名称
        payNamePrint: null,//打印名称
        payParameter: null,//付款属性
        changeSts: null,//是否找零
        swipingCardSts: null,//是否刷卡类型
        exChargeSts: null,//能否溢收
        interfaceAddress: null,//外部接口地址
        posId: null,//pos机号
        posDisplay: null,//pos是否显示
        cancel: null,//取消
        affirm: null,// 确认
        returnsViewBut: null,//返回
        submitBtn: null,//提交按钮
        auditFlag: null,
        recordStatus:null,
        //审核
        delJsonParam: null,
        approvalBut: null,
        approval_dialog: null,
        audit_cancel: null,
        audit_affirm: null,
        typeId: null,
        reviewId: null
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_back = _common.config.surl + "/paymentMode";
        url_left = _common.config.surl + "/paymentMode/edit";
        systemPath = _common.config.surl;
        //初始化下拉
        getSelectValue();
        initAutoMatic();
        //事件绑定
        but_event();
        //列表初始化
        initTable1();
        initTable2();
        //表格内按钮事件
        table_event();
        // 根据跳转加载数据，设置操作模式
        setValueByType();
        //审核事件
        approval_event();
    }

    //审核事件
    var approval_event = function () {
        //点击审核按钮
        m.approvalBut.on("click", function () {
            var recordId = m.payCd.val();
            if (recordId != null && recordId != "") {
                $("#approval_dialog").modal("show");
                //获取审核记录
                _common.getStep(recordId, m.typeId.val());
            } else {
                _common.prompt("Record fetch failed, Please try again!", 5, "error");
            }
        });
        //审核提交
        $("#audit_affirm").on("click", function () {
            //审核意见
            var auditContent = $("#auditContent").val();
            if (auditContent.length > 200) {
                _common.prompt("Approval comments cannot exceed 200 characters!", 5, "error");
                return false;
            }
            //审核记录id
            var auditStepId = $("#auditId").val();
            //用户id
            var auditUserId = $("#auditUserId").val();
            //审核结果
            var auditStatus = $("#auditStatusGroup").find("input[type='radio']:checked").val();

            _common.myConfirm("Are you sure to save?", function (result) {
                if (result != "true") {
                    return false;
                }
                $.myAjaxs({
                    url: _common.config.surl + "/audit/submit",
                    async: true,
                    cache: false,
                    type: "post",
                    data: {
                        auditStepId: auditStepId,
                        auditUserId: auditUserId,
                        auditStatus: auditStatus,
                        auditContent: auditContent,
                        toKen: m.toKen.val()
                    },
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            $("#approval_dialog").modal("hide");
                            _common.checkFinish(m.payCd.val(), m.typeId.val(), function (success) {
                                // 最后审核 删除记录
                                deleData(auditStatus, success);
                            });
                            //更新主档审核状态值
                            //_common.modifyRecordStatus(auditStepId,auditStatus);
                            m.approvalBut.prop("disabled", true);
                            _common.prompt("Saved Successfully!", 3, "success");// 保存审核信息成功
                        } else {
                            m.approvalBut.prop("disabled", false);
                            _common.prompt("Saved Failure!", 5, "error");// 保存审核信息失败
                        }
                        m.toKen.val(result.toKen);
                    },
                    complete: _common.myAjaxComplete
                });
            })
        })
    }

    // 请求加载下拉列表
    function getSelectValue() {
        // 加载select
        initSelectOptions("Visible on POS", "posDisplay", "00385");/*POS是否隐藏*/
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
                for (var i = 0; i<result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText  =  result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error: function (e) {
                _common.prompt(title + "Failed to load data!", 5, "error");
            }
        });
    }
    var  deleData=function (auditStatus, success) {
    if (auditStatus === "1" && success) {
        // 审核 完成 删掉  中间数据库所有数据,并且将中间库所有数据存入源数据库
        $.myAjaxs({
            url: url_left + "/deleAllRecord",
            async: true,
            cache: false,
            type: "post",
            data: "payCd="+m.payCd.val(),
            success: function (result) {
                if (result.success) {
                    console.log("成功删除")
                }
            }
        })
    }
}
    //获取pos机号下拉
    var getSelectPosId = function (storeCd, val) {
        if (storeCd != null && storeCd != '') {
            $.myAjaxs({
                url: url_left + "/getPosId",
                async: true,
                cache: false,
                type: "post",
                data: "storeCd=" + storeCd,
                dataType: "json",
                success: function (result) {
                    m.posId.empty();
                    if (result.success) {
                        for (var i = 0; i < result.data.length; i++) {
                             var optionValue = result.data[i].posId;
                             var optionText = result.data[i].posName;
                             m.posId.append(new Option(optionText, optionValue));
                        }
                        m.posId.find("option:first").prop("selected", true);
                        if (val != null && val != "") {
                            m.posId.val(val);
                        }
                    }
                },
                error: function (e) {
                    _common.prompt("request failed!", 5, "error");
                },
                complete: _common.myAjaxComplete
            });
        }
    }

    var table_event = function () {
        $("#add").on("click", function () {
            $("#a_store_clear").click();
            m.operateFlgByDialog.val("1");
            m.recordStatus.val("add");
            $('#paymentModeEdit_dialog').modal("show");
        });
        //修改权限
        $("#update").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp, "storeCd,storeName,posId,posDisplay");
            var storeCd = cols["storeCd"];
            var storeName = cols["storeName"];
            var posId = cols["posId"];
            var posDisplay = cols["posDisplay"];
            $.myAutomatic.setValueTemp(a_store, storeCd, storeName);
            $("#oldStoreCd").val(storeCd);
            $("#oldPosId").val(posId);
            getSelectPosId(storeCd, posId);
            m.posDisplay.val(posDisplay);
            m.operateFlgByDialog.val("2");
            m.recordStatus.val("modify");
            $('#paymentModeEdit_dialog').modal("show");
        });
        //删除
        $("#delete").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }

            var cols = tableGrid.getSelectColValue(selectTrTemp, "storeCd,storeName,posId,posName,posDisplay,posDisplayName");
            // 点击删除后将内容append 到删除table
            _common.myConfirm("Please confirm whether you want to delete the selected data？", function (result) {
                if (result == "true") {
                    var date = new Date();
                    var rowindex = 0;
                    var trId = $("#zgGridTtable1>.zgGrid-tbody tr:last").attr("id");
                    if (trId != null && trId != '') {
                        rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
                    }
                    var tr1 = '<tr id="zgGridTtable_' + rowindex + '_tr" class="">' +
                        '<td tag="storeCd" width="100" title="' + cols["storeCd"] + '" align="center" id="zgGridTtable_' + rowindex + '_tr_storeCd" tdindex="zgGridTtable_storeCd">' + cols["storeCd"] + '</td>' +
                        '<td tag="storeName" width="130" title="' + cols["storeName"] + '" align="center" id="zgGridTtable_' + rowindex + '_tr_storeName" tdindex="zgGridTtable_storeName">' + cols["storeName"] + '</td>' +
                        '<td tag="posId" width="130" hidden="true" title="' + cols["posId"] + '" align="center" id="zgGridTtable_' + rowindex + '_tr_posId" tdindex="zgGridTtable_posId">' + cols["posId"] + '</td>' +
                        '<td tag="posName" width="130" title="' + cols["posName"] + '" align="center" id="zgGridTtable_' + rowindex + '_tr_posName" tdindex="zgGridTtable_posName">' + cols["posName"] + '</td>' +
                        '<td tag="posDisplay" width="130" hidden="true" title="' + cols["posDisplay"] + '" align="center" id="zgGridTtable_' + rowindex + '_tr_posDisplay" tdindex="zgGridTtable_posDisplay">' + cols["posDisplay"] + '</td>' +
                        '<td tag="posDisplayName" width="130" title="' + cols["posDisplayName"] + '" align="center" id="zgGridTtable_' + rowindex + '_tr_posDisplayName" tdindex="zgGridTtable_posDisplayName">' + cols["posDisplayName"] + '</td>' +
                        '<td tag="empName" width="130" title="' + m.userName.val() + '" align="center" id="zgGridTtable_' + rowindex + '_tr_posDisplayName" tdindex="zgGridTtable_posDisplayName">' + m.userName.val() + '</td>' +
                        '<td tag="createYmd" width="130" title="' + getNow(date.getDate())  + "/" + getNow(date.getMonth() + 1) +date.getFullYear()+ '" align="center" id="zgGridTtable_' + rowindex + '_tr_posDisplayName" tdindex="zgGridTtable_posDisplayName">' +getNow(date.getDate())  + "/" + getNow(date.getMonth() + 1) + "/" + date.getFullYear() + '</td>' +
                        '<td tag="createHms" width="130" title="' + getNow(date.getHours()) + ":" + getNow(date.getMinutes()) + ":" + getNow(date.getSeconds()) + '" align="center" id="zgGridTtable_' + rowindex + '_tr_posDisplayName" tdindex="zgGridTtable_posDisplayName">' + getNow(date.getHours()) + ":" + getNow(date.getMinutes()) + ":" + getNow(date.getSeconds()) + '</td>' +
                        '</tr>';
                    $("#zgGridTtable1>.zgGrid-tbody").append(tr1);
                    $(selectTrTemp).find("td[tag=recordStatus]").text("delete");
                 //   $(selectTrTemp).remove();
                    selectTrTemp = null;
                    _common.prompt("Data has been modify delete status,Waitting  apprroving");
                    //_common.prompt("Data deleted successfully!", 3, "success");
                }
            });
        });


        $("#deleteDelData").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }
            //	var cols = tableGrid1.getSelectColValue(selectTrTemp,"storeCd,posId");
            _common.myConfirm("Please confirm whether you want to delete the selected data？", function (result) {
                if (result == "true") {
                    $(selectTrTemp).remove();
                    selectTrTemp = null;
                    _common.prompt("Data deleted successfully!", 3, "success");
                }
            });
        })


        m.cancel.on("click", function () {
            _common.myConfirm("Are you sure you want to cancel?", function (result) {
                $("#a_store").css("border-color", "#CCCCCC");
                m.posId.css("border-color", "#CCCCCC");
                m.posDisplay.css("border-color", "#CCCCCC");
                if (result == "true") {
                    $('#paymentModeEdit_dialog').modal("hide");
                }
            })
        })
        //新增加确认
        m.affirm.on("click", function () {
            var storeCd = $("#a_store").attr("k");
            var storeName = $("#a_store").attr("v");
            var posId = m.posId.val();
            var posName = m.posId.find("option:selected").text();
            var posDisplay = m.posDisplay.val();
            var posDisplayName = m.posDisplay.find("option:selected").text();
            var operateFlgByDialog = m.operateFlgByDialog.val();
            if (storeCd == null || storeCd == "") {
                $("#a_store").css("border-color", "red");
                _common.prompt("Please select Store！", 5, "error");
                $("#a_store").focus();
                return false;
            } else {
                $("#a_store").css("border-color", "#CCCCCC");
            }
            if (posId == null || posId == "") {
                m.posId.css("border-color", "red");
                _common.prompt("Please select POS No.", 5, "error");/*请选择的POS机号*/
                m.posId.focus();
                return false;
            } else {
                m.posId.css("border-color", "#CCCCCC");
            }

            if (posDisplay == null || posDisplay == "") {
                m.posDisplay.css("border-color", "red");
                _common.prompt("Please select whether to hide POS！", 5, "error");
                m.posDisplay.focus();
                return false;
            } else {
                m.posDisplay.css("border-color", "#CCCCCC");
            }
            var repArticle = 0;
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                var posId_text = $(this).find('td[tag=posId]').text();
                var storeCd_text = $(this).find('td[tag=storeCd]').text();
                if (operateFlgByDialog == "1" && m.posId.val() == posId_text && storeCd == storeCd_text) {
                    repArticle++;
                }
                if (operateFlgByDialog == "2" && m.posId.val() == posId_text && storeCd == storeCd_text && (storeCd != $("#oldStoreCd").val() || m.posId.val() != $("#oldPosId").val())) {
                    repArticle++;
                }
            });
            if (repArticle > 0) {
                _common.prompt("The data already exists！", 5, "error");
                return false;
            }
            _common.myConfirm("Are you sure to save the data？", function (result) {
                if (result == "true") {
                    if (operateFlgByDialog == "1") {
                        appendTr(storeCd, storeName, posId, posName, posDisplay, posDisplayName,m.recordStatus.val());
                    } else if (operateFlgByDialog == "2") {
                        $(selectTrTemp).find('td[tag=storeCd]').text(storeCd);
                        $(selectTrTemp).find('td[tag=storeName]').text(storeName);
                        $(selectTrTemp).find('td[tag=posId]').text(posId);
                        $(selectTrTemp).find('td[tag=posName]').text(posName);
                        $(selectTrTemp).find('td[tag=posDisplay]').text(posDisplay);
                        $(selectTrTemp).find('td[tag=posDisplayName]').text(posDisplayName);
                        $(selectTrTemp).find('td[tag=recordStatus]').text(m.recordStatus.val());
                    }
                    _common.prompt("Data saved successfully!", 3, "success"); // 数据添加成功
                    $('#paymentModeEdit_dialog').modal("hide");
                }
            });
        });
    }

    //表格新增
    var appendTr = function (storeCd, storeName, posId, posName, posDisplay, posDisplayName,recordStatus) {
        var rowindex = 0;
        var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
        if (trId != null && trId != '') {
            rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
        }
        var tr = '<tr id="zgGridTtable_' + rowindex + '_tr" class="">' +
            '<td tag="storeCd" width="100" title="' + storeCd + '" align="right" id="zgGridTtable_' + rowindex + '_tr_storeCd" tdindex="zgGridTtable_storeCd">' + storeCd + '</td>' +
            '<td tag="storeName" width="130" title="' + storeName + '" align="left" id="zgGridTtable_' + rowindex + '_tr_storeName" tdindex="zgGridTtable_storeName">' + storeName + '</td>' +
            '<td tag="posId" width="130" hidden="true" title="' + posId + '" align="left" id="zgGridTtable_' + rowindex + '_tr_posId" tdindex="zgGridTtable_posId">' + posId + '</td>' +
            '<td tag="posName" width="130" title="' + posName + '" align="left" id="zgGridTtable_' + rowindex + '_tr_posName" tdindex="zgGridTtable_posName">' + posName + '</td>' +
            '<td tag="posDisplay" width="130" hidden="true" title="' + posDisplay + '" align="left" id="zgGridTtable_' + rowindex + '_tr_posDisplay" tdindex="zgGridTtable_posDisplay">' + posDisplay + '</td>' +
            '<td tag="posDisplayName" width="130" title="' + posDisplayName + '" align="left" id="zgGridTtable_' + rowindex + '_tr_posDisplayName" tdindex="zgGridTtable_posDisplayName">' + posDisplayName + '</td>' +
            '<td tag="recordStatus" width="130" title="' +recordStatus + '" align="left" id="zgGridTtable_' + rowindex + '_tr_recordStatus" tdindex="zgGridTtable_recordStatus">' + recordStatus + '</td>' +
            '</tr>';
        $("#zgGridTtable>.zgGrid-tbody").append(tr);
    }

    //画面按钮点击事件
    var but_event = function () {
        $('input:radio[name="interfaceSts"]').change(function () {
            if ($("#interfaceSts_1").is(":checked")) {
                $("#interfaceAddress").show();
                $("#interfaceAddress_lable").show();
            }
            if ($("#interfaceSts_2").is(":checked")) {
                $("#interfaceAddress").hide();
                $("#interfaceAddress_lable").hide();
            }
        })
        //返回一览
        m.returnsViewBut.on("click", function () {
            var bank = $("#submitBtn").attr("disabled");
            if (bank != 'disabled') {
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?", function (result) {
                    if (result === "true") {
                        _common.updateBackFlg(KEY);
                        top.location = url_back;
                    }
                });
            }
            if (bank === 'disabled') {
                _common.updateBackFlg(KEY);
                top.location = url_back;
            }
        });
        //提交变价信息
        m.submitBtn.on("click", function () {
            var payCd = m.payCd.val();
            var payName = m.payName.val();
            var payNamePrint = m.payNamePrint.val();
            var operateFlg = m.operateFlg.val();
            //是否调用外部接口
            var interfaceSts = $('input:radio[name="interfaceSts"]:checked').val();
            //外部接口地址
            var interfaceAddress = m.interfaceAddress.val();
            if (interfaceSts == "0") {
                interfaceAddress = "";
            }
            if (payCd == null || payCd == "") {
                m.payCd.css("border-color", "red");
                _common.prompt("Please enter Payment No.！", 3, "info");
                m.payCd.focus();
                return false;
            } else {
                m.payName.css("border-color", "#CCCCCC");
            }
            if (payName == null || payName == "") {
                m.payName.css("border-color", "red");
                _common.prompt("Please enter Payment Name！", 3, "info");
                m.payName.focus();
                return false;
            } else {
                m.payName.css("border-color", "#CCCCCC");
            }
            if (payNamePrint == null || payNamePrint == "") {
                m.payNamePrint.css("border-color", "red");
                _common.prompt("Please enter Printed Name！", 3, "info");
                m.payNamePrint.focus();
                return false;
            } else {
                m.payNamePrint.css("border-color", "#CCCCCC");
            }

            var changeSts = "0";
            var swipingCardSts = "0";
            var exChargeSts = "0";
            if (m.changeSts.is(':checked')) {
                changeSts = "1";
            }
            if (m.swipingCardSts.is(':checked')) {
                swipingCardSts = "1";
            }
            if (m.exChargeSts.is(':checked')) {
                exChargeSts = "1";
            }
            var payParameter = changeSts + swipingCardSts + exChargeSts;
            if ($("#zgGridTtable>.zgGrid-tbody tr").length < 1) {
                _common.prompt("Please add POS pay method maintenance details！", 3, "info");/*请添加POS支付方式维护明细*/
                return false;
            }
            var paymentModeDetail = [];
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                var paymentMode = {
                    payCd: payCd,
                    storeCd: $(this).find('td[tag=storeCd]').text(),//店铺号
                    posId: $(this).find('td[tag=posId]').text(),//pos机号
                   posDisplay: $(this).find('td[tag=posDisplay]').text(),//pos是否隐藏
                   recordStatus: $(this).find('td[tag=recordStatus]').text(),
                }
                paymentModeDetail.push(paymentMode);
            });

            var paymentModeDetailJson = "";
            if (paymentModeDetail.length > 0) {
                paymentModeDetailJson = JSON.stringify(paymentModeDetail)
            }
            // 添加删除表格的数据进入table
            var paymentDelModeDetail = [];
            $("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
                var paymentDelModeDetailJson = {
                    payCd: m.payCd.val(),
                    storeCd: $(this).find('td[tag=storeCd]').text(),//店铺号
                    posId: $(this).find('td[tag=posId]').text(),//pos机号
                    posDisplay: $(this).find('td[tag=posDisplay]').text(),//pos是否隐藏
                    createUserId: m.userId.val(),
                    createYmd: fmtDate($(this).find('td[tag=createYmd]').text()),//pos是否隐藏
                    createHms: fmtTime($(this).find('td[tag=createHms]').text()),//pos是否隐藏
                }
                paymentDelModeDetail.push(paymentDelModeDetailJson);
            });
            var paymentDelModeDetailJson = "";
            paymentDelModeDetailJson = JSON.stringify(paymentDelModeDetail);
            // 开始保存数据
            _common.myConfirm("Are you sure to save the data？", function (result) {
                if (result == "true") {
                    $.myAjaxs({
                        url: url_left + "/save",
                        async: true,
                        cache: false,
                        type: "post",
                        // processData: false,
                        data: {
                            payCd: payCd,
                            payName: payName,
                            payNamePrint: payNamePrint,
                            payParameter: payParameter,
                            interfaceSts: interfaceSts,
                            interfaceAddress: interfaceAddress,
                            paymentModeDetailJson: paymentModeDetailJson,
                            operateFlg: operateFlg,//操作状态
                            delJsonParam: paymentDelModeDetailJson,
                            toKen: m.toKen.val()
                        },
                        dataType: "json",
                        success: function (result) {
                            m.toKen.val(result.toKen);
                            if (result.success) {
                                m.operateFlg.val("edit");
                                m.payCd.prop("disabled", true);
                                _common.prompt("Data Saved Successfully！", 3, "success");/*保存成功*/
                                //发起审核
                                var typeId = m.typeId.val();
                                var nReviewid = m.reviewId.val();
                                var recordCd = m.payCd.val();
                                _common.initiateAudit(null, recordCd, typeId, nReviewid, m.toKen.val(), function (token) {
                                    //禁用页面按钮
                                    disableAll();
                                    //审核按钮禁用
                                    m.approvalBut.prop("disabled", true);
                                    m.toKen.val(token);
                                })
                            } else {
                                _common.prompt(result.message, 5, "error");
                            }
                        },
                        complete: _common.myAjaxComplete
                    });
                }
            });

        });
    }
    //禁用页面按钮
    var disableAll = function () {
        $("#deleteDelData").prop("disabled", true);
        m.payCd.prop("disabled", true);
        m.payName.prop("disabled", true);
        m.payNamePrint.prop("disabled", true);
        m.changeSts.prop("disabled", true);
        m.swipingCardSts.prop("disabled", true);
        m.exChargeSts.prop("disabled", true);
        $("#interfaceSts_1").prop("disabled", true);
        $("#interfaceSts_2").prop("disabled", true);
        m.interfaceAddress.prop("disabled", true);
        $("#add").prop("disabled", true);
        $("#update").prop("disabled", true);
        $("#delete").prop("disabled", true);
        m.submitBtn.prop("disabled", true);
    }
    //启用页面按钮
    var enableAll = function () {
        m.payCd.prop("disabled", false);
        m.payName.prop("disabled", false);
        m.payNamePrint.prop("disabled", false);
        m.changeSts.prop("disabled", false);
        m.swipingCardSts.prop("disabled", false);
        m.exChargeSts.prop("disabled", false);
        $("#interfaceSts_1").prop("disabled", false);
        $("#interfaceSts_2").prop("disabled", false);
        m.interfaceAddress.prop("disabled", false);
        $("#add").prop("disabled", false);
        $("#update").prop("disabled", false);
        $("#delete").prop("disabled", false);
        m.submitBtn.prop("disabled", false);
    }

    // 根据跳转方式，设置画面可否编辑、加载内容
    var setValueByType = function () {
        var _sts = m.operateFlg.val();
        if (_sts == "add") {
            $("#interfaceSts_2").click();//默认第2个选中
            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
        } else if (_sts == "edit" && m.auditFlag.val() == "auditted") {
            // 查询加载数据
            dataInitByPayCd();
            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
            m.payCd.prop("disabled", true);
            m.payName.prop("disabled", true);
            m.payNamePrint.prop("disabled", true);
        } else if (_sts == "edit") {
            // 查询加载数据
            dataInitByPayCd();
            //支付cd禁用
            m.payCd.prop("disabled", true);
            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
        } else if (_sts == "view") {
            // 查询加载数据
            dataInitByPayCd();
            //检查是否允许审核
            _common.checkRole(m.h_payCd.val(), m.typeId.val(), function (success) {
                if (success) {
                    m.approvalBut.prop("disabled", false);
                } else {

                    m.approvalBut.prop("disabled", true);
                }
            });
        }
    }
   var getSa0005record=function(){
        $.myAjaxs({
            url: url_left + "/getdelList",
            async: true,
            cache: false,
            data: "payCd=" + m.h_payCd.val(),
            type: "post",
            success:function (result) {
                for (var i = 0; i < result.length; i++) {
                    appendTr(result[i].storeCd, result[i].storeName,result[i].posId, result[i].posName, result[i].posDisplay, result[i].posDisplayName,result[i].recordStatus);
                }
            }
        })
   };

    //页面数据赋值
    var dataInitByPayCd = function () {
        $.myAjaxs({
            url: url_left + "/getPaymentInfo",
            async: true,
            cache: false,
            type: "post",
            data: "payCd=" + m.h_payCd.val(),
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var data = result.data;
                    //赋值POS支付方式维护基本信息
                    m.payCd.val(data.payCd);
                    m.payName.val(data.payName);
                    m.payNamePrint.val(data.payNamePrint);
                    var payParameter = data.payParameter.split("");
                    if (payParameter[0] == "1") {
                        m.changeSts.click();
                    }
                    if (payParameter[1] == "1") {
                        m.swipingCardSts.click();
                    }
                    if (payParameter[2] == "1") {
                        m.exChargeSts.click();
                    }
                    if (data.interfaceSts == "1") {
                        $("#interfaceSts_1").click();
                        m.interfaceAddress.val(data.interfaceAddress);
                    } else {
                        $("#interfaceSts_2").click();
                    }
                    //查询POS支付方式维护明细信息
                    paramGrid = "payCd=" + m.h_payCd.val();
                    tableGrid.setting("url", url_left + "/getList");
                    tableGrid.setting("param", paramGrid);
                    tableGrid.loadData(null);
                   // 获取中间库数据
                    _common.checkOnlyRole(m.h_payCd.val(), m.typeId.val(), function (success) {
                        if (success) {
                            getSa0005record();
                            m.approvalBut.prop("disabled", false);
                        } else {
                            m.approvalBut.prop("disabled", true);
                        }
                    });

                    // 查询审核后删掉的数据
                    // tableGrid1.setting("url", url_left + "/getDelList");
                    // tableGrid1.setting("param", paramGrid);
                    // tableGrid1.loadData(null);
                    var _sts = m.operateFlg.val();
                    if (_sts == "view") {
                        //禁用页面
                        disableAll();
                    }
                } else {
                    _common.prompt(result.message, 5, "error");
                }
            },
            error: function (e) {
                _common.prompt("request failed!", 5, "error");
            },
            complete: _common.myAjaxComplete
        });
    }

    //初始化自动下拉
    var initAutoMatic = function () {
        a_store = $("#a_store").myAutomatic({
            url: url_left + "/getStore",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
                m.posId.empty();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    getSelectPosId(thisObj.attr("k"))
                }
            }
        });
    }

    //表格初始化-POS支付方式维护列表样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            // title:"POS支付方式维护明细",
            title: "POS Payment Methods Details",
            param: paramGrid,
            colNames: ["Store No.", "Store Name", "POS Id", "POS No.", "POS Display", "Visible on POS","Status"],
            colModel: [
                {name: "storeCd", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "storeName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "posId", type: "text", text: "right", width: "130", ishide: true, css: ""},
                {name: "posName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "posDisplay", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "posDisplayName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "recordStatus", type: "text", text: "left", width: "130", ishide: false, css: ""}
            ],//列内容
            width: "max",//宽度自动
            isPage: false,//是否需要分页
            isCheckbox: false,
            freezeHeader:true,
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
                    butSize: ""//,
                },//新增
                {
                    butType: "update",
                    butId: "update",
                    butText: "Modify",
                    butSize: ""//,
                },//修改
                {
                    butType: "delete",
                    butId: "delete",
                    butText: "Delete",
                    butSize: ""//,
                },//删除
            ],
        });
    }
    var initTable2 = function () {
        tableGrid1 = $("#zgGridTtable1").zgGrid({
            // title:"POS支付方式维护明细",
            title: "Haven Deleted POS Payment Methods Details",
            param: paramGrid,
            colNames: ["Store No.", "Store Name", "POS Id", "POS No.", "POS Display", "Visible on POS", "Deleted User Name", "Deleted Date", "Deleted Hms"],
            colModel: [
                {name: "storeCd", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "storeName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "posId", type: "text", text: "right", width: "130", ishide: true, css: ""},
                {name: "posName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "posDisplay", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "posDisplayName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "empName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {
                    name: "createYmd",
                    type: "text",
                    text: "left",
                    width: "130",
                    ishide: false,
                    css: "",
                    getCustomValue: _common.forCreateNewDate
                },
                {
                    name: "createHms",
                    type: "text",
                    text: "left",
                    width: "130",
                    ishide: false,
                    css: "",
                    getCustomValue: _common.forCreateTime
                },

            ],//列内容
            width: "max",//宽度自动
            isPage: false,//是否需要分页
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
                    butType: "delete",
                    butId: "deleteDelData",
                    butText: "Delete",
                    butSize: ""//,
                },//删除
            ],
        });
    }
    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }
    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    /**
     * 自定义函数名：PrefixZero
     * @param num： 被操作数
     * @param n： 固定的总位数
     */
    function PrefixZero(num, n) {
        return (Array(n).join(0) + num).slice(-n);
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

    function fmtDate(date) {
        var res = "";
        if (date != null && date != "") {
            res = date.replace(/\//g, '').replace(/\//, "");
        }
        return res;
    }

    function subfmtDate(date) {
        var res = "";
        if (date != null && date != "") {
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
        return res;
    }

    // 判断当前系统时间是否加0
    //判断是否在前面加0
    function getNow(s) {
        return s < 10 ? '0' + s : s;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }

    // 时间格式化
    function fmtTime(time) {
        var res = "";
        res = time.replace(/:/, "").replace(/:/, "");
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('paymentModeEdit');
_start.load(function (_common) {
    _index.init(_common);
});
