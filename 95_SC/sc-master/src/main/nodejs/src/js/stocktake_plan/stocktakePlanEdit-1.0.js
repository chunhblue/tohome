require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('receiptEdit', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        pmaSelectTr = null,
        tempTrObjValue = {},
        startTime = '',
        endTime = '',
        systemPath = '',
        subFlag = '',
        pmaList = [],
        submitFlag = false,
        pmaTableGrid,
        a_store = null,
        saveFlag = false;
    const KEY = 'STOCKTAKING_PLAN_SETTING';
    var dataForm = [];
    var m = {
        toKen: null,
        use: null,
        up: null,
        error_pcode: null,
        dpt: null,
        pd_date: null,
        pd_start_time: null,
        pd_end_time: null,
        returnsViewBut: null,
        saveBut: null,
        store: null,
        storeNo: null,
        skType: null,
        skStatus: null,
        skMethod: null,
        createUser: null,
        remarks: null,
        viewSts: null,
        del_alert: null,
        p_code_buyer_del: null,
        enterFlag: null, // 操作状态
        piCdParam: null, // 一览传入的参数
        piDateParam: null,
        //审核
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
        url_left = _common.config.surl + "/stocktakePlanEntry";
        systemPath = _common.config.surl;

        //首先验证当前操作人的权限是否混乱，
        but_event();

		//列表初始化
		initTable1();

        // store 下拉
        initAutoMatic();

        // 获取下拉框数据
        getComboxData();

        //表格内按钮事件
        table_event();

        // 初始化pmaTable
        // initPmaTable();

        // pmaList数据
        getPmaList();

        // 根据 操作状态初始化 页面
		initViewPage();

		// 初始化隐藏
		$('#update_dialog').modal("hide");

        //审核事件
        approval_event();
    }

    //审核事件
    var approval_event = function () {
        //点击审核按钮
        m.approvalBut.on("click", function () {
            var recordId = m.storeNo.val();
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

    var initViewPage = function () {
		// 判断操作状态
		if (m.enterFlag.val() == 'add') {
			setDisable(false);
            m.pd_end_time.prop('disabled',true);
            m.pd_start_time.prop('disabled',true);
            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
		} else if (m.enterFlag.val() == 'update') {
			getData(m.piCdParam.val(), m.piDateParam.val());
			setDisable(true);
			$("#remarks").prop("disabled", false);
			$("#saveBut").prop("disabled", false);
			$("#updatePlanDetails").prop("disabled", false);
			$("#addMPlanDetails").prop("disabled", false);
			$("#deletePlanDetails").prop("disabled", false);
            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
		} else if (m.enterFlag.val() == 'view') {
			getData(m.piCdParam.val(), m.piDateParam.val());
			setDisable(true);
            //检查是否允许审核
            _common.checkRole(m.piCdParam.val(), m.typeId.val(), function (success) {
                if (success) {
                    m.approvalBut.prop("disabled", false);
                } else {
                    m.approvalBut.prop("disabled", true);
                }
            });
		} else {
			setDisable(true);
            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
		}
	}

	var getAllItemDepartmentByStore = function (storeCd) {
        _common.loading();
        $.myAjaxs({
            url: url_left + "/getAllItemDepartmentByStore",
            async: true,
            cache: false,
            type: "get",
            data: "storeCd=" + storeCd,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let pmaList = result.o;
                    dataForm = [];
                    $("#zgGridTtable  tr:not(:first)").remove();
                    pmaList.forEach(function (item) {
                        let obj = {
                            'pmaCd': item.pmaCd,
                            'pmaName': item.pmaName
                        }
                        dataForm.push(obj);
                        let temp = '<tr>' +
                            '<td tag="pmaCd"   width="410" align="center" sortindex="pmaCd" thindex="pmaGridTable_pmaCd">' + item.pmaCd + '</td>' +
                            '<td tag="pmaName" width="380" align="center" sortindex="pmaName" thindex="pmaGridTable_pmaName">' + item.pmaName + '</td>' +
                            '</tr>';
                        tableGrid.append(temp);
                    })
                }
                loading_close();
            }
        });
    }

    var loading_close = function () {
        $(".loading-mask-div").fadeOut(100,function(){
            $(".init-loading-box").fadeOut(200);
        });
    }

    var table_event = function () {
        $('#addMPlanDetails').on('click', function () {
            subFlag = 'batchAdd';
            pmaTableGrid.find("input[type='checkbox']").prop("checked", false);
            $('#update_dialog').modal("show");
        });

        $('#cancel').on('click', function () {
            _common.myConfirm("Are you sure to Cancel?", function (result) {
                if (result == "true") {
                    $('#update_dialog').modal("hide");
                }
            })
        });

        $('#affirm').on('click', function () {
            var trs = pmaTableGrid.getCheckboxTrs();
            if (trs.length < 1) {
                _common.prompt("Please select at least one row of data!", 5, "error");
                return;
            }

            var _addFlg = false;
            if (subFlag == 'batchAdd') {
                // 多选状态
                trs.forEach(function (item) {
                    let cols = pmaTableGrid.getSelectColValue(item, 'pmaCd');
                    $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                        let _pmaCd = $(this).find('td[tag=pmaCd]').text();
                        if (_pmaCd == cols['pmaCd']) {
                            _addFlg = true;
                            return false; // 结束遍历
                        }
                    });
                })
            } else {
                let td = trs[0];
                let selCol = pmaTableGrid.getSelectColValue(td, 'pmaCd,pmaName');
                var selPmaCd = selCol['pmaCd'];
                var selPmaName = selCol['pmaName'];
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var _pmaCd = $(this).find('td[tag=pmaCd]').text();
                    if (_pmaCd == selPmaCd) {
                        _addFlg = true;
                        return false; // 结束遍历
                    }
                });
            }

            if (_addFlg) { // type code 相同
                _common.prompt("Department already exists!", 5, "error");
                return;
            }
            _common.myConfirm("Are you sure you want to submit?", function (result) {
                if (result == "true") {
                    if (subFlag == 'add' || subFlag == 'batchAdd') {

                        if (subFlag == 'batchAdd') {
                            var trs = pmaTableGrid.getCheckboxTrs();
                            trs.forEach(function (item) {
                                let cols = pmaTableGrid.getSelectColValue(item, 'pmaCd,pmaName');
                                let obj = {
                                    'pmaCd': cols['pmaCd'],
                                    'pmaName': cols['pmaName']
                                }
                                dataForm.push(obj);
                                let temp = '<tr>' +
                                    '<td tag="pmaCd"  width="410" align="center" sortindex="pmaCd" thindex="pmaGridTable_pmaCd">' + cols['pmaCd'] + '</td>' +
                                    '<td tag="pmaName" width="380" align="center" sortindex="pmaName" thindex="pmaGridTable_pmaName">' + cols['pmaName'] + '</td>' +
                                    '</tr>';
                                tableGrid.append(temp);
                            })
                            _common.prompt("Data added successfully!", 3, "success");
                        } else {
                            var obj = {
                                'pmaCd': selPmaCd,
                                'pmaName': selPmaName
                            }
                            dataForm.push(obj);
                            var temp = '<tr>' +
                                '<td tag="pmaCd"  width="410" align="center" sortindex="pmaCd" thindex="pmaGridTable_pmaCd">' + selPmaCd + '</td>' +
                                '<td tag="pmaName"  width="380"  align="center" sortindex="pmaName" thindex="pmaGridTable_pmaName">' + selPmaName + '</td>' +
                                '</tr>';
                            tableGrid.append(temp);
                            _common.prompt("Data added successfully!", 3, "success");
                        }

                    } else if (subFlag == 'update') {
                        var _pmaCd = $($(selectTrTemp[0]).find('td')[0]).text();
                        $("#zgGridTtable>.zgGrid-tbody tr").each(function (index) {
                            var temp = $(this).find('td[tag=pmaCd]').text();
                            if (temp == _pmaCd) {
                                $(this).find('td[tag=pmaCd]').text(selPmaCd);
                                $(this).find('td[tag=pmaName]').text(selPmaName);
                                // dataForm.shift(index,1); // 删除数组中的数据
                                var obj = {
                                    'pmaCd': selPmaCd,
                                    'pmaName': selPmaName
                                }
                                dataForm[index] = obj; // 修改数组中的数据
                                _common.prompt("Data modification successful", 3, "success");
                            }
                        });
                    }
                }
            });

            $('#update_dialog').modal("hide");
        });

        $("#updatePlanDetails").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
                return;
            }
            $("#pmaGridTable").find("input[type='checkbox']").prop("checked", false);
            var cols = tableGrid.getSelectColValue(selectTrTemp, "pmaCd,pmaName");
            if (cols == null || cols.length == 0) {
                _common.prompt("Please select at least one row of data!", 5, "error");
            } else {
                subFlag = 'update';
                $('#update_dialog').modal("show");
            }
        });
        $("#deletePlanDetails").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
                return;
            }

            var pmaCd = $($(selectTrTemp[0]).find('td')[0]).text();
            var pmaName = $($(selectTrTemp[0]).find('td')[1]).text();
            if (pmaCd == '' || pmaCd == null) {
                _common.prompt("Please select the data to delete!", 5, "error");
                return false;
            } else {
                _common.myConfirm("Are you sure you want to delete?", function (result) {
                    if (result == "true") {
                        for (let i = 0; i < dataForm.length; i++) {
                            if (pmaCd == dataForm[i].pmaCd) {
                                dataForm.splice(i, 1);
                                $("#zgGridTtable  tr:not(:first)")[i].remove(); // 删除
                                _common.prompt("Data deleted successfully", 3, "success");
                            }
                        }
                    }
                });
            }
        });

        m.skType.on('change', function () {
            /**
             *  skType: 01 All Item
             *          02 Partial Item
             *    1.Type选择为All Item类型时，自动添加该店可盘点商品的所有Department,
             *        并且禁用Add/Modify/Delete按钮（但是如果Type修改回Partical Item选项，可以恢复可自由增删改状态）
             *    2.选择partical item的时候，需要按照用户追加的department，
             *        去过滤出来本店可订货 或者可销售商品里符合所选deparement的商品
             */
            if (m.enterFlag.val()!='add') {
                return;
            }

			let storeCd = m.store.attr('k');

            let _skType = m.skType.val();

            if (_skType == '01') {
                setBtnDisable(true);

                if (!storeCd) {
                    return;
                }
                getAllItemDepartmentByStore(storeCd);
            } else {
                setBtnDisable(false);
                $("#zgGridTtable  tr:not(:first)").remove();
                dataForm=[];
            }
        });

        //返回一览
        m.returnsViewBut.on("click", function () {
            let back = $("#saveBut").attr("disabled");

            if (back != 'disabled' && submitFlag === false) {
                _common.myConfirm("Crrent change is not submitted yet,are you sure to exit?", function (result) {
                    if (result == "true") {
                        _common.updateBackFlg(KEY);
                        top.location = url_left;
                    }
                })
            }
            if (back == 'disabled' && submitFlag === true) {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
            if (back == 'disabled' && submitFlag === false) {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
        });

        // 提交保存数据
        m.saveBut.on('click', function () {
            if (saveFlag) {
                return;
            }
            // 判断必录项是否录入
            if (verifySearch()) {
                if (dataForm.length < 1) {
                    _common.prompt("Please fill in the detailed data!", 5, "error");
                    return false;
                }
                var storeNo = m.storeNo.val();
                var skDate = formatDate(m.pd_date.val());
                var store = m.store.attr('k');
                var skType = m.skType.val();
                var skMethod = m.skMethod.val();
                var skStatus = m.skStatus.val();
                var skStartTime = formatTime(m.pd_start_time.val());
                var skEndTime = formatTime(m.pd_end_time.val());
                var createUser = m.createUser.val();
                var remarks = m.remarks.val();
                var flag = m.enterFlag.val(); // 操作状态

                // 封装数据
                var obj = {
                    'piCd':storeNo,
                    'piDate': skDate,
                    'storeCd': store,
                    'piType': skType,
                    'piMethod': skMethod,
                    'piStatus': skStatus,
                    'piStartTime': skStartTime,
                    'piEndTime': skEndTime,
                    'createUserId': createUser,
                    'remarks': remarks,
                    'flag': flag,
                    'details': dataForm
                };
                // var record = encodeURI(encodeURI(encodeURI(JSON.stringify(obj))));.replace(/\&/g,'%26\t')
                var record = encodeURIComponent(JSON.stringify(obj));
                _common.myConfirm("Are you sure you want to save?", function (result) {
                    if (result != "true") {
                        return false;
                    }
                    _common.loading();
                    $.myAjaxs({
                        url: url_left + "/save",
                        async: true,
                        cache: false,
                        type: "post",
                        data: "record=" + record,
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                $("#storeNo").val(result.o);
                                // m.storeNo.val(result.o);
                                submitFlag = true;
                                // m.enterFlag = 'update';
                                // $("#pd_date").prop("disabled", true);
                                // $("#store").prop("disabled", true);
                                $("#storeRefresh").hide();
                                $("#storeRemove").hide();
                                // $("#skType").prop("disabled", true);
                                // 变为查看模式
                                setDisable(true);
                                _common.prompt("Data saved successfully！",2,"success");
                                // 选择 All Item 自动保存 盘点对象商品, 成功后发起 审核, 暂时 注释
                                /*if (skType != '01') {
                                    return;
                                }*/
                                //发起审核
                                var typeId =m.typeId.val();
                                var	nReviewid =m.reviewId.val();
                                var	recordCd = $("#storeNo").val();
                                var storeCd = m.store.attr('k');
                                _common.initiateAudit(storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
                                    m.toKen.val(token);
                                    m.enterFlag.val('view');
                                });
                            } else {
                                _common.prompt(result.msg, 5, "error");
                            }
                            loading_close();
                        },
                        error: function (e) {
                            _common.prompt("Data saved failed！", 5, "error");/*保存失败*/
                            loading_close();
                        }
                    });
                })

            } else {
                _common.prompt("Please fill in all required information!", 5, "error");
                return false;
            }
        });
    }

    //画面按钮点击事件
    var but_event = function () {
        //盘点日期
        m.pd_date.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true,
            startDate: new Date(fmtIntDate($('#businessDate').val()))
        });

        m.pd_date.on('change',function () {
            let piDate = formatDate(m.pd_date.val());
            piDate = fmtIntDate(piDate);
            startTime = piDate+' 06:00:00';
            endTime = piDate+' 23:59:59';
            clearTime();
            if (piDate) {
                m.pd_end_time.prop('disabled',false);
                m.pd_start_time.prop('disabled',false);
            } else {
                m.pd_end_time.prop('disabled',true);
                m.pd_start_time.prop('disabled',true);
            }
        })
    };

    //清空日期
    var clearTime = function () {
        m.pd_start_time.val("");
        m.pd_end_time.val("");
        m.pd_start_time.datetimepicker('remove');
        m.pd_end_time.datetimepicker('remove');
        initStocktakeTime(startTime,endTime);
    }

    var initStocktakeTime = function (startTime,endTime) {
        //开始时间
        m.pd_start_time.datetimepicker(
            {
                language: 'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: false,//今日按钮
                format: 'hh:ii:ss',
                startView: 'day',// 进来是月
                minView: 'hour',// 可以看到小时
                maxView: 1,
                minuteStep:1, //分钟间隔为1分
                todayHighlight: false,
                forceParse: true,
                startDate: new Date(startTime),
                endDate: new Date(endTime),
            }).on('changeDate', function (ev) {
                if (ev.date) {
                    $("#pd_end_time").datetimepicker('setStartDate', ev.date)
                } else {
                    $("#pd_end_time").datetimepicker('setStartDate', null);
                }
        });

        //结束时间
        m.pd_end_time.datetimepicker(
            {
                language: 'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: false,//今日按钮
                format: 'hh:ii:ss',
                startView: 'day',// 进来是月
                minView: 'hour',// 可以看到小时
                maxView: 1,
                minuteStep:1, //分钟间隔为1分
                todayHighlight: false,
                forceParse: true,
                startDate: new Date(startTime),
                endDate: new Date(endTime),
            }).on('changeDate', function (ev) {
                if (ev.date) {
                    $("#pd_start_time").datetimepicker('setEndDate', ev.date)
                } else {
                    $("#pd_start_time").datetimepicker('setEndDate', null);
                }
        });
    }

    var getComboxData = function () {
        $.myAjaxs({
            url: url_left + "/getComboxData",
            async: false,
            cache: false,
            type: "get",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    var piMetList = record.piMethod;
                    var piStsList = record.piStatus;
                    var piTypeList = record.piType;
                    piMetList.forEach(function (item) {
                        var temp = '<option value="' + item.codevalue + '">' + item.codename + '</option>'
                        $('#skMethod').append(temp);
                    });

                    piStsList.forEach(function (item) {
                        var temp = '<option value="' + item.codevalue + '">' + item.codename + '</option>'
                        $('#skStatus').append(temp);
                    });

                    piTypeList.forEach(function (item) {
                        var temp = '<option value="' + item.codevalue + '">' + item.codename + '</option>'
                        $('#skType').append(temp);
                    });
                }
            }
        });
    }

    var getData = function (piCd, piDate) {
        if (!piCd || !piDate) {
            clearAll();
            return;
        }

        $.myAjaxs({
            url: url_left + "/getData",
            async: true,
            cache: false,
            type: "get",
            data: "piCd=" + piCd + "&piDate=" + piDate,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    //$("#pmaGridTable  tr:not(:first)").html("");
                    dataForm = [];
                    $("#storeNo").val(record.piCd);
                    $("#pd_date").val(record.piDate);
                    $("#skType").val(record.piType);
                    $("#skMethod").val(record.piMethod);
                    $("#skStatus").val(record.piStatus);
                    $("#pd_start_time").val(record.piStartTime);
                    $("#pd_end_time").val(record.piEndTime);
                    $("#createUser").val(record.createUserId);
                    $("#updateUser").val(record.updateUserId);
                    $("#remarks").val(record.remarks);
                    $.myAutomatic.setValueTemp(a_store, record.storeCd, record.storeName);//赋值

                    if (m.enterFlag.val()=='update' && (record.piStatus!='01'||record.piType=='01'||record.exportFlg=='1')) {
                        setDisable(true);
                    }

                    for (let i = 0; i < record.details.length; i++) {
                        var item = record.details[i];
                        var pmaCd = item.pmaCd;
                        var pmaName = item.pmaName;
                        var obj = {
                            'pmaCd': pmaCd,
                            'pmaName': pmaName
                        }
                        dataForm.push(obj);
                        var temp = '<tr>' +
                            '<td tag="pmaCd" align="center" width="410" sortindex="pmaCd" thindex="pmaGridTable_pmaCd">' + pmaCd + '</td>' +
                            '<td tag="pmaName" align="center" width="380" sortindex="pmaName" thindex="pmaGridTable_pmaName">' + pmaName + '</td>' +
                            '</tr>';
                        tableGrid.append(temp);
                    }
                }
                m.skType.change();
            }
        });
    };

    // 设置为查看模式
    var setDisable = function (flag) {
        $("#pd_date").prop("disabled", flag);
        $("#store").prop("disabled", flag);
        $("#search_item_but").prop("disabled", flag);
        $("#skType").prop("disabled", flag);
        $("#pd_start_time").prop("disabled", flag);
        $("#pd_end_time").prop("disabled", flag);
        $("#remarks").prop("disabled", flag);
        $("#saveBut").prop("disabled", flag);
        $("#updatePlanDetails").prop("disabled", flag);
        $("#addMPlanDetails").prop("disabled", flag);
        $("#deletePlanDetails").prop("disabled", flag);

        if (flag) {
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        } else {
            $("#storeRefresh").show();
            $("#storeRemove").show();
        }
    }

    // 设置按钮为查看模式
    var setBtnDisable = function (flag) {
        $("#updatePlanDetails").prop("disabled", flag);
        $("#addMPlanDetails").prop("disabled", flag);
        $("#deletePlanDetails").prop("disabled", flag);
    }

    // 清空值
    var clearAll = function () {
        $("#pmaGridTable  tr:not(:first)").html("");
        $("#storeNo").val('');
        $("#pd_date").val('');
        $("#store").val('');
        $("#skType").val('');
        $("#skMethod").val('');
        $("#skStatus").val('');
        $("#pd_start_time").val('');
        $("#pd_end_time").val('');
        $("#createUser").val('');
        $("#remarks").val('');
    }

    //验证检索项是否合法
    var verifySearch = function () {
        var store = m.store.val();  // Stocktaking Store
        var pd_date = m.pd_date.val();
        var skType = m.skType.val(); // Stocktaking Type
        var skMethod = m.skMethod.val(); // Stocktaking Method
        var skStatus = m.skStatus.val(); // Stocktaking Status
        var pd_start_time = m.pd_start_time.val(); // Stocktaking Time
        var pd_end_time = m.pd_end_time.val();

        if(isNull(pd_date)){
            $("#pd_date").css("border-color","red");
            $("#pd_date").focus();
            return false;
        }else {
            $("#pd_date").css("border-color","#CCCCCC");
        }
        if(isNull(store)){
            $("#store").css("border-color","red");
            $("#store").focus();
            return false;
        }else {
            $("#store").css("border-color","#CCCCCC");
        }
        if(isNull(skType)){
            $("#skType").css("border-color","red");
            $("#skType").focus();
            return false;
        }else {
            $("#skType").css("border-color","#CCCCCC");
        }
        if(isNull(pd_start_time)){
            $("#pd_start_time").css("border-color","red");
            $("#pd_start_time").focus();
            return false;
        }else {
            $("#pd_start_time").css("border-color","#CCCCCC");
        }
        if(isNull(pd_end_time)){
            $("#pd_end_time").css("border-color","red");
            $("#pd_end_time").focus();
            return false;
        }else {
            $("#pd_end_time").css("border-color","#CCCCCC");
        }
        return true;
    }

    //初始化店铺下拉
    var initAutoMatic = function () {
        a_store = $("#store").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
				m.skType.change();
            },
            cleanInput: function (thisObj) {
                dataForm=[];
                $("#zgGridTtable  tr:not(:first)").remove();
            },
        });
    };
    // 获取pmaList
    var getPmaList = function () {
        pmaTableGrid.setting("url", url_left + "/getPmaList");
        pmaTableGrid.setting("page", 1);
        pmaTableGrid.loadData();
    }
    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }

    // 17:05:16 -> 170516
    var formatTime = function (str) {
        return str.replace(/:/g,'');
    }

    var isNull = function (str) {
        return str == '' || str == null || str == undefined;
    }

    //表格初始化
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Stocktaking Plan Details",
            param: paramGrid,
            localSort: true,
            colNames: ["Department Code", "Department Name"],
            colModel: [
                {name: "pmaCd", type: "text", text: "right", width: "410", ishide: false, css: ""},
                {name: "pmaName", type: "text", text: "left", width: "380", ishide: false, css: ""},
            ],//列内容
            traverseData: dataForm,
            width: "max",//宽度自动
            height: "300",
            isPage: false,//是否需要分页
            isCheckbox: false,
            freezeHeader:true,
            // sidx:"pmaCd",//排序字段
            // sord:"asc",//升降序
            formatter: function (value, row, index) {
                return index + 1;
            },
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            footerrow: true,
            loadCompleteEvent: function (self) {

            },
            userDataOnFooter: true,
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {
                    butType: "add",
                    butId: "addMPlanDetails",
                    butText: "Add",
                    butSize: "",
                },
                {
                    butType: "update",
                    butId: "updatePlanDetails",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType: "delete",
                    butId: "deletePlanDetails",
                    butText: "Delete",
                    butSize: ""
                }
            ],
        });

        pmaTableGrid = $("#pmaGridTable").zgGrid({
            title: "Please select Department for Stocktaking",
            param: paramGrid,
            localSort: true,
            colNames: ["Department Code", "Department Name"],
            colModel: [
                {name: "pmaCd", type: "text", text: "center", width: "200", ishide: false, css: ""},
                {name: "pmaName", type: "text", text: "center", width: "200", ishide: false, css: ""}
            ],//列内容
            // traverseData:pmaList,
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: true,
            formatter: function (value, row, index) {
                return index + 1;
            },
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            footerrow: true,
            loadCompleteEvent: function (self) {

            },
            userDataOnFooter: true,
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                pmaSelectTr = trObj;
            },
        });
    }

    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }

    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receiptEdit');
_start.load(function (_common) {
    _index.init(_common);
});
