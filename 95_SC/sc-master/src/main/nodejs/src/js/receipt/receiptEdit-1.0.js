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
        url_root = "",
        paramGrid = null,
        // reThousands = null,
        // toThousands = null,
        // getThousands = null,
        selectTrTemp = null,
        differenceReason = null,
        Sts = null,
        table = null,
        tempTrObjValue = {},//临时行数据存储
        //附件
        attachmentsParamGrid = null,
        selectTrTempFile = null,
        orderAdd = true,
        common = null;
    const KEY = 'ITEM_RECEIVING_ENTRY_FROM_DC';
    var m = {
        toKen: null,
        use: null,
        error_pcode: null,
        identity: null,
        searchJson: null,
        main_box: null,
        // Key
        _type: null,
        _storeCd: null,
        _orderId: null,
        _receiveId: null,
        viewSts: null,
        // 栏位
        storeName: null,
        receiveNo: null,
        orderNo: null,
        orderDate: null,
        warehouseName: null,
        orderType: null,
        receiveDate: null,
        orderAmount: null,
        receiveAmount: null,
        reviewSts: null,
        remarks: null,
        taxRate: null,
        // 弹窗栏位
        receivePrice: null,
        orderQty: null,
        receiveQty: null,
        orderNoQty: null,
        receiveNoQty: null,
        updateAmount: null,
        differenceReason: null,
        // 按钮
        returnsViewBut: null,
        physicalReceivingDate: null,
        saveBtn: null,
        update_cancel: null,
        update_affirm: null,
        //审核
        approvalBut: null,
        approval_dialog: null,
        audit_cancel: null,
        audit_affirm: null,
        typeId: null,
        reviewId: null,
        submitAuditBut: null,
        // 隐藏域
        orderDiff: null,
        articleId: null,
        isFreeItem: null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = url_root + "/receipt";
        // reThousands=_common.reThousands;
        // toThousands=_common.toThousands;
        // getThousands=_common.getThousands;
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
        //附件事件
        attachments_event();
        //审核事件
        // approval_event();
        // 设置按钮是否可用
        if (m.viewSts.val() == 'view') {
            setDisable();
            if (m.submitAuditBut.attr("disabled",true)){
                $("#addByFile").attr("disabled",true);
                $("#updateByFile").attr("disabled",true);
                $("#deleteByFile").attr("disabled",true);
            }
            //检查是否允许审核
            // _common.checkRole(m._receiveId.val(),m.typeId.val(),function (success) {
            // 	if(success){
            // 		m.approvalBut.prop("disabled",false);
            // 	}else{
            // 		m.approvalBut.prop("disabled",true);
            // 	}
            // });
        } else {
            //禁用审核按钮
            // m.approvalBut.prop("disabled",true);
            // m.approvalBut.hide();
            $("#remarks").prop("disabled", false);
            m.submitAuditBut.hide();
        }
        // 加载数据
        getRecord();
        bodyScroll();
    }

    // 冻结表头时，表头可以横向移动
    var bodyScroll  = function(){
        $(document).ready(function() {
            if (table.defaults.freezeHeader) {
                var initHeaderWidth = table.table.width();
                table.zgGridHeaderTable.width(initHeaderWidth);
                table.zgGridBodyBox.scroll(function () {
                    var left = $(table.zgGridBodyBox).scrollLeft();
                    if (left < 0) {
                        left = 0;
                    }
                    left = (~left) + "px";
                    table.zgGridHeaderTable.stop().animate({
                        marginLeft: left
                    }, 0);
                });
            }
        });
    };

    var checkSubmit = function () {
        if (m.viewSts.val() != 'view') {
            m.submitAuditBut.hide();
            return;
        }
        //获取数据审核状态
        _common.getRecordStatus(m._receiveId.val(), m.typeId.val(), function (result) {
            if (result.success || result.data == '0') {
                //检查是否允许submit 店长sm才拥有权限
                // $.myAjaxs({
                //     url: url_root + "/storeOrder/checkSubmit",
                //     async: true,
                //     cache: false,
                //     type: "post",
                //     data: {
                //         storeCd: m._storeCd.val()
                //     },
                //     dataType: "json",
                //     success: function (result) {
                //         if (result.success) {
                //             m.submitAuditBut.removeAttr("disabled");
                //         } else {
                //             m.submitAuditBut.attr("disabled", "disabled");
                //         }
                //     },
                //     complete: _common.myAjaxComplete
                // });

                var position = false;
                _common.getPositionList(m._storeCd.val(),function (result) {
                    let arr = result;
                    for(let i=0;i<arr.length;i++){
                        if(arr[i] === 4 || arr[i]==2 || arr[i]==1){
                            position =true;
                        }
                    }
                });
                if(position){
                    m.submitAuditBut.removeAttr("disabled");
                }

            } else {
                m.submitAuditBut.attr("disabled", "disabled");
            }
        },m.reviewId.val());
    };

    //附件
    var attachments_event = function () {
        //附件一览表格
        attachmentsTable = $("#attachmentsTable").zgGrid({
            title: "Attachments",
            param: attachmentsParamGrid,
            colNames: ["File Name", "Download", "PreView"],
            colModel: [
                {name: "fileName", type: "text", text: "center", width: "130", ishide: false, css: ""},
                {
                    name: "filePath",
                    type: "text",
                    text: "center",
                    width: "100",
                    ishide: false,
                    css: "",
                    getCustomValue: function (tdObj, value) {
                        var obj = value.split(",");
                        var html = '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="' + obj[0] + '" filePath="' + obj[1] + '"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>';
                        return $(tdObj).html(html);
                    }
                },
                {name:"filePath1",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
                        var obj = value.split(",");
                        var html = '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>';
                        return $(tdObj).html(html);
                    }
                }
            ],//列内容
            width: "max",//宽度自动
            isPage: false,//是否需要分页
            isCheckbox: false,
            freezeHeader:false,
            loadEachBeforeEvent: function (trObj) {
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTempFile = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTempFile = trObj;
            },
            buttonGroup: [
                {butType: "add", butId: "addByFile", butText: "Add", butSize: ""},//新增
                {butType: "update", butId: "updateByFile", butText: "Modify", butSize: ""},//修改
                {butType: "delete", butId: "deleteByFile", butText: "Delete", butSize: ""},//删除
            ],
        });
        //附件表格新增
        var appendTrByFile = function (fileName, filePath) {
            var rowindex = 0;
            var trId = $("#attachmentsTable>.zgGrid-tbody tr:last").attr("id");
            if (trId != null && trId != '') {
                rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
            }
            var tr = '<tr id="attachmentsTable_' + rowindex + '_tr" class="">' +
                '<td tag="fileName" width="130" title="' + fileName + '" align="center" id="attachmentsTable_' + rowindex + '_tr_fileName" tdindex="attachmentsTable_fileName">' + fileName + '</td>' +
                '<td tag="filePath" width="100" title="Download" align="center" id="attachmentsTable_' + rowindex + '_tr_filePath" tdindex="attachmentsTable_filePath">' +
                '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="' + fileName + '" filePath="' + filePath + '"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>' +
                '</td>' +
                '<td tag="filePath1" width="100" title="Preview" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath1" tdindex="attachmentsTable_filePath1">'+
                '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>'+
                '</td>' +
                '</tr>';
            $("#attachmentsTable>.zgGrid-tbody").append(tr);
        }

        //附件一览显示
        $("#attachments").on("click", function () {
            $('#attachments_dialog').modal("show");
        })
        //附件一览关闭
        $("#cancelByAttachments").on("click", function () {
            _common.myConfirm("Are you sure you want to cancel?", function (result) {
                if (result == "true") {
                    $('#attachments_dialog').modal("hide");
                }
            })
        })
        //添加文件
        $("#addByFile").on("click", function () {
            $('#fileUpload_dialog').modal("show");
            $("#operateFlgByFile").val("1");
            $("#file_name").val("");
            $("#fileData").val("");
            $("#fileData").parent().parent().show();
        });
        //修改文件名称
        $("#updateByFile").on("click", function () {
            if (selectTrTempFile == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }
            $('#fileUpload_dialog').modal("show");
            $("#operateFlgByFile").val("2");
            $("#fileData").parent().parent().hide();
            var cols = attachmentsTable.getSelectColValue(selectTrTempFile, "fileName");
            var fileName = cols["fileName"];
            $("#file_name").val(fileName);
        });
        //删除文件
        $("#deleteByFile").on("click", function () {
            if (selectTrTempFile == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }
            _common.myConfirm("Please confirm whether you want to delete the selected data？", function (result) {
                if (result == "true") {
                    $(selectTrTempFile[0]).remove();
                    selectTrTempFile = null;
                }
            });
        });
        //提交按钮点击事件 文件上传
        $("#affirmByFile").on("click", function () {
            if ($("#file_name").val() == null || $("#file_name").val() == '') {
                $("#file_name").css("border-color", "red");
                _common.prompt("File name cannot be empty!", 5, "error");
                $("#file_name").focus();
                return;
            } else {
                $("#file_name").css("border-color", "#CCCCCC");
            }
            _common.myConfirm("Are you sure you want to upload？", function (result) {
                if (result == "true") {
                    var flg = $("#operateFlgByFile").val();
                    if (flg == "1") {
                        if ($('#fileData')[0].files[0] == undefined || $('#fileData')[0].files[0] == null) {
                            $("#fileData").css("border-color", "red");
                            _common.prompt("File cannot be empty!", 5, "error");
                            $("#fileData").focus();
                            return;
                        } else {
                            $("#fileData").css("border-color", "#CCCCCC");
                        }
                        var formData = new FormData();
                        formData.append("fileData", $('#fileData')[0].files[0]);
                        formData.append("toKen", m.toKen.val());
                        $.myAjaxs({
                            url: _common.config.surl + "/file/upload",
                            async: false,
                            cache: false,
                            type: "post",
                            data: formData,
                            dataType: "json",
                            processData: false,
                            contentType: false,
                            success: function (data, textStatus, xhr) {
                                var resp = xhr.responseJSON;
                                if (resp.result == false) {
                                    top.location = resp.s + "?errMsg=" + resp.errorMessage;
                                    return;
                                }
                                if (data.success == true) {
                                    var fileName = $("#file_name").val();
                                    var filePath = data.data.filePath;
                                    appendTrByFile(fileName, filePath);
                                    _common.prompt("Operation Succeeded!", 2, "success");
                                    $('#fileUpload_dialog').modal("hide");
                                } else {
                                    _common.prompt(data.message, 5, "error");
                                }
                                m.toKen.val(data.toKen);
                            },
                            complete: _common.myAjaxComplete
                        });
                    } else if (flg == "2") {
                        var fileName = $("#file_name").val();
                        $(selectTrTempFile[0]).find('td').eq(0).text(fileName);
                        _common.prompt("Operation Succeeded!", 2, "success");
                        $('#fileUpload_dialog').modal("hide");
                    }
                }
            });
        });

        $("#cancelByFile").on("click", function () {
            _common.myConfirm("Are you sure you want to cancel?", function (result) {
                if (result == "true") {
                    $("#file_name").css("border-color", "#CCCCCC");
                    $("#fileData").css("border-color", "#CCCCCC");
                    $('#fileUpload_dialog').modal("hide");
                }
            })
        })

        //下载
        $("#attachments_dialog").on("click", "a[class*='downLoad']", function () {
            var fileName = $(this).attr("fileName");
            var filePath = $(this).attr("filePath");
            window.open(_common.config.surl + "/file/download?fileName=" + fileName + "&filePath=" + filePath, "_self");
        });
        //预览
        $("#attachments_dialog").on("click","a[class*='preview']",function(){
            var fileName = $(this).attr("fileName");
            var filePath = $(this).attr("filePath");
            var url = _common.config.surl+"/file/preview?fileName="+fileName+"&filePath="+filePath;
            window.open(url,"toPreview");
        });
    };

    //审核事件
    var approval_event = function () {
        //点击审核按钮
        m.approvalBut.on("click", function () {
            var recordId = m.receiveNo.val();
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

    // 设置为查看模式
    var setDisable = function () {
        $("#physicalReceivingDate").prop("disabled", true);
        $("#remarks").prop("disabled", true);
        $("#update").prop("disabled", true);
        $("#saveBtn").prop("disabled", true);
        $("#submitAuditBut").prop("disabled", true);
        //附件按钮
        $("#addByFile").prop("disabled", false);
        $("#updateByFile").prop("disabled", false);
        $("#deleteByFile").prop("disabled", false);
    }

    // 表格内按钮事件
    var table_event = function () {
        $("#receivePrice").blur(function () {
            $("#receivePrice").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#receivePrice").focus(function () {
            $("#receivePrice").val(reThousands(this.value));
        });

        $("#receiveQty").blur(function () {
            $("#receiveQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#receiveQty").focus(function () {
            $("#receiveQty").val(reThousands(this.value));
        });

        $("#receiveNoQty").blur(function () {
            $("#receiveNoQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#receiveNoQty").focus(function () {
            $("#receiveNoQty").val(reThousands(this.value));
        });

        $("#updateAmount").blur(function () {
            $("#updateAmount").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#updateAmount").focus(function () {
            $("#updateAmount").val(reThousands(this.value));
        });

        differenceReason = $("#differenceReason").myAutomatic({
            url: url_root + "/cm9010/getReceiptDifferReason",
            ePageSize: 5,
            startCount: 0,
        })

        $("#diffreasonRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(differenceReason);
        });

        // 进入查看页面
        $("#view").on("click", function (e) {
            if (selectTrTemp == null) {
                _common.prompt("Please select the data to view!", 5, "error");
                return false;
            }

            if ($("#receiveQty2").length == 1) {
                _common.prompt("The table being modified cannot be viewed",5,"error");
                return false;
            }
            clearDialogValue(true);
            setDialogIsDisable(true);// 设置弹窗不允许编辑
            var cols = tableGrid.getSelectColValue(selectTrTemp, "barcode,articleId,articleName,orderUnit,receivePrice,orderQty,orderNoChargeQty,receiveQty,receiveNoChargeQty,receiveTotalAmt,differenceReasonText,isFreeItemText");
            var attribute1 = $(selectTrTemp[0]).find('td').eq(3).text();
            if (attribute1==="Total:"){
                _common.prompt("Please select the effective data to view!", 5, "error");
                $("#update_dialog").hide();
                return false;
            }
            $("#barcode").val(cols['barcode']);
            $("#itemId").val(cols['articleId']);
            $("#itemName").val(cols['articleName']);
            $("#uom").val(cols['orderUnit']);
            $("#receivePrice").val(toThousands(isNotNull(cols['receivePrice'])));
            $("#receiveQty").val(toThousands(isNotNull(cols['receiveQty'])));
            $("#orderNoQty").val(toThousands(isNotNull(cols['orderNoChargeQty'])));
            $("#receiveNoQty").val(toThousands(isNotNull(cols['receiveNoChargeQty'])));
            $("#isFreeItemText").val(cols['isFreeItemText']);
            differenceReason = $("#differenceReason").myAutomatic({
                url: url_root + "/cm9010/getReceiptDifferReason",
                ePageSize: 5,
                startCount: 0,
            })
            $.myAutomatic.setValueTemp(differenceReason, cols['differenceReason'], cols['differenceReasonText']);
            $('#update_dialog').attr("flg", "view");
            $('#update_dialog').modal("show");
            $("#update_affirm").prop("disabled", true); //设置submit为只查看模式
        });

        // 进入编辑页面
        $("#update").on("click", function (e) {
            if (selectTrTemp == null) {
                _common.prompt("Please select the data to modify!", 5, "info");/*请选择要修改的数据*/
                return false;
            }
            // clearDialogValue(true);
            // setDialogIsDisable(false);// 设置弹窗允许编辑
            // var cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,orderUnit,receivePrice,orderQty,orderNoChargeQty,receiveQty,receiveNoChargeQty,receiveTotalAmt,differenceReasonText,isFreeItemText");
            // $("#barcode").val(cols['barcode']);
            // $("#itemId").val(cols['articleId']);
            // $("#itemName").val(cols['articleName']);
            // $("#uom").val(cols['orderUnit']);
            // $("#receivePrice").val(toThousands(isNotNull(cols['receivePrice'])));
            // $("#isFreeItemText").val(cols['isFreeItemText']);
            // if(cols['receiveQty'] === null || cols['receiveQty'] === ''){
            // 	$("#receiveQty").val(toThousands(isNotNull(cols['orderQty'])));
            // 	$("#orderQty").val(toThousands(isNotNull(cols['orderQty'])));
            // 	$("#orderNoQty").val(toThousands(isNotNull(cols['orderNoChargeQty'])));
            // 	$("#receiveNoQty").val(toThousands(isNotNull(cols['orderNoChargeQty'])));
            // }else {
            // 	$("#receiveQty").val(toThousands(isNotNull(cols['receiveQty'])));
            // 	$("#orderQty").val(toThousands(isNotNull(cols['orderQty'])));
            // 	$("#orderNoQty").val(toThousands(isNotNull(cols['orderNoChargeQty'])));
            // 	$("#receiveNoQty").val(toThousands(isNotNull(cols['receiveNoChargeQty'])));
            // }
            // $.myAutomatic.setValueTemp(differenceReason, '', cols['differenceReasonText']);
            // if(cols['differenceReasonText']!=null&&cols['differenceReasonText']!=''){
            // 	$("#differenceReason").prop("disabled", false);
            // 	$("#diffreasonRefresh").show();
            // 	$("#diffreasonRemove").show();
            // }
            // $("#receiveQty,#receiveNoQty").on("blur change", function () {
            // 	let receiveQty = parseInt(reThousands(m.receiveQty.val()));
            // 	let receiveNoQty = parseInt(reThousands(m.receiveNoQty.val()));
            // 	let orderQty = parseInt(reThousands(m.orderQty.val()));
            // 	let orderNoQty = parseInt(reThousands(m.orderNoQty.val()));
            // 	if(receiveQty < orderQty){
            // 		m.differenceReason.prop("disabled",false);
            // 		$("#diffreasonRefresh").show();
            // 		$("#diffreasonRemove").show();
            // 		$.myAutomatic.replaceParam(differenceReason, '');
            // 	}else {
            // 		if(receiveNoQty === orderNoQty) {
            // 			m.differenceReason.prop("disabled", true);
            // 			$.myAutomatic.cleanSelectObj(differenceReason);
            // 			$("#diffreasonRefresh").hide();
            // 			$("#diffreasonRemove").hide();
            // 		}
            // 	}
            // 	if(receiveNoQty < orderNoQty){
            // 		m.differenceReason.prop("disabled",false);
            // 		$("#diffreasonRefresh").show();
            // 		$("#diffreasonRemove").show();
            // 		$.myAutomatic.replaceParam(differenceReason, '');
            // 	}else {
            // 		if(receiveQty === orderQty) {
            // 			m.differenceReason.prop("disabled", true);
            // 			$.myAutomatic.cleanSelectObj(differenceReason);
            // 			$("#diffreasonRefresh").hide();
            // 			$("#diffreasonRemove").hide();
            // 		}
            // 	}
            // });
            // $('#update_dialog').attr("flg","upt");
            // $('#update_dialog').modal("show");
            // $("#update_affirm").prop("disabled", false);
            //添加输入框添加数据
            addInputValue();
        });
        if (m.identity.val() == "1") {
            //是否有冻结列的标记
            var freeze = tableGrid.getting("freezeIndex");
            //重写选择背景变色事件
            tableGrid.find("tbody").unbind('mousedown').on('mousedown', 'td', function (e) {
                var thisObj = $(this),
                    thisParent = $(this).parent();
                if (3 == e.which) { //鼠标右键
                    thisParent.addClass("info").siblings().removeClass("info");
                    var eachTrRightclickFun = !!$.isFunction(tableGrid.getting("eachTrRightclick"));
                    if (eachTrRightclickFun) {
                        tableGrid.getting("eachTrRightclick").call(this, thisParent, thisObj);
                    }
                    if (freeze >= 0) {
                        var freeToTr = thisParent.prop("id");
                        $("#" + freeToTr + "_free").addClass("info").siblings().removeClass("info");
                    }
                } else if (1 == e.which) { //鼠标左键
                    //还原input输入框
                    var cols = tableGrid.getSelectColValue(thisParent, "articleId,isFreeItem");
                    let articleId = tableGrid.find('.info td[tag=articleId]').text();
                    let deliveryQty2 = isNotNull(tableGrid.find('.info td[tag=deliveryQty]').text());
                    let receiveQty2= isNotNull( $("#receiveQty2").val());
                    let isFreeItem = tableGrid.find('.info td[tag=isFreeItem]').text();
                    let selArticleId = m.articleId.val();
                    let selIsFreeItem = m.isFreeItem.val();
                    if ((articleId !== cols["articleId"] || isFreeItem !== cols["isFreeItem"])
                        && !orderAdd && verifyDialogSearch2(true)) {
                        let receiveQty = parseInt(reThousands($("#receiveQty2").val()));
                        let hhtReceiveQty = parseInt(reThousands($(".info [tag=hhtReceiveQty]").text()));
                        let deliveryQty = parseInt(reThousands($(".info [tag=deliveryQty]").text()));
                        let receiveNoQty = parseInt(reThousands($("#receiveNoQty2").val()));
                        let orderQty = parseInt(reThousands($(".info [tag=orderQty]").text()));
                        let orderNoQty = parseInt(reThousands($(".info [tag=orderNoChargeQty]").text()));
                        let msg = "are you sure to submit?";
                        // $("#receiveQty2").trigger("blur change");
                        $("#receiveQty2").trigger("focus change");
                        m.differenceReason = $("#differenceReason2");
                        m.receiveNoQty = $("#receiveNoQty2");
                        m.receiveQty = $("#receiveQty2");
                        if (isNaN(hhtReceiveQty) || hhtReceiveQty === null || hhtReceiveQty === '') {
                            hhtReceiveQty = 0;
                        }
                        if (isNaN(deliveryQty) || deliveryQty === null || deliveryQty === '') {
                            deliveryQty = 0;
                        }
                        /*if(receiveNoQty > orderNoQty){
							// msg = "receiving free quantity is greater than order free quantity, " + msg;
							_common.prompt("receiving free qty can not be greater than order free qty!");
							m.receiveNoQty.focus();
							$("#receiveNoQty2").css("border-color","red");
							return false;
						}else if(receiveNoQty < orderNoQty){
							$("#receiveNoQty2").css("border-color","#CCC");
							if(m.differenceReason.val().trim()==''){
								_common.prompt("Differnece Reason cannot be empty!");
								m.differenceReason.focus();
								m.differenceReason.css("border-color","red");
								return false;
							}else{
								m.differenceReason.css("border-color","#CCCCCC");
							}
							msg = "receiving free quantity is less than order free quantity, " + msg;
						}*/
                        if (receiveQty > orderQty) {
                            /*_common.prompt("receiving qty can not be greater than order qty!");
                            m.receiveQty.focus();
                            $("#receiveQty2").css("border-color", "red");
                            return false;*/
                            _common.prompt("receiving quantity is more than order quantity!", 3, "info");
                        } else if (receiveQty < orderQty) {
                            $("#receiveQty2").css("border-color", "#CCC");
                            // if (m.differenceReason.val().trim() == '') {
                            //     _common.prompt("Differnece Reason cannot be empty!");
                            //     m.differenceReason.focus();
                            //     m.differenceReason.css("border-color", "red");
                            //     return false;
                            // } else {
                            //     m.differenceReason.css("border-color", "#CCCCCC");
                            // }
                            msg = "receiving quantity is less than order quantity, " + msg;
                        }
                        if(deliveryQty2-receiveQty2 !=0){
                            if (m.differenceReason.val().trim() == '') {
                                _common.prompt("Differnece Reason cannot be empty!");
                                m.differenceReason.focus();
                                m.differenceReason.css("border-color", "red");
                                return false;
                            } else {
                                m.differenceReason.css("border-color", "#CCCCCC");
                            }
                        }
                        msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
                        let cols = tableGrid.getSelectColValue(selectTrTemp, "articleId,isFreeItem");
                        $("#zgGridTable>.zgGrid-tbody tr").each(function () {
                            let articleId = $(this).find('td[tag=articleId]').text();
                            let isFreeItem = $(this).find('td[tag=isFreeItem]').text();
                            if (articleId === selArticleId && isFreeItem === selIsFreeItem) {
                                let _price = reThousands(m.receivePrice.val());
                                let _amount = Number(accMul(receiveQty, _price)).toFixed(2);
                                let _total = accAdd(receiveQty, receiveNoQty);
                                // $(this).find('td[tag=varQty]').text(toThousands(receiveQty-hhtReceiveQty));
                                $(this).find('td[tag=varQty]').text(toThousands(deliveryQty-receiveQty));
                                $(this).find('td[tag=receiveQty]').text(toThousands(m.receiveQty.val()));
                                $(this).find('td[tag=receiveQty1]').text(toThousands(m.receiveQty.val()));
                                $(this).find('td[tag=receiveNoChargeQty]').text(toThousands(m.receiveNoQty.val()));
                                $(this).find('td[tag=receiveNoChargeQty1]').text(toThousands(m.receiveNoQty.val()));
                                $(this).find('td[tag=receiveTotalQty]').text(toThousands(_total));
                                $(this).find('td[tag=receiveTotalQty1]').text(toThousands(_total));
                                $(this).find('td[tag=receiveTotalAmt]').text(toThousands(_amount));
                                $(this).find('td[tag=differenceReason]').text(m.differenceReason.attr('k'));
                                $(this).find('td[tag=differenceReasonText]').text(m.differenceReason.attr('v'));
                            }
                        });
                        let _amt = getAmount();
                        m.receiveAmount.val(toThousands(_amt));
                        orderAdd = true;
                    }
                    //改变背景颜色
                    thisParent.addClass("info").siblings().removeClass("info");
                    if (freeze >= 0) {
                        var freeToTr = thisParent.prop("id");
                        $("#" + freeToTr + "_free").addClass("info").siblings().removeClass("info");
                    }
                    $(".zgGrid-modal").hide();
                    var eachTrClickFun = !!$.isFunction(tableGrid.getting("eachTrClick"));
                    if (eachTrClickFun) {
                        tableGrid.getting("eachTrClick").call(this, thisParent, thisObj);
                    }
                }
                total_qty();
            });
        }
    }// 验证表格
    var verifyDialogSearch2 = function (flag) {
        var reg = /^[0-9]*$/;
        if (orderAdd) {
            return;
        }
        m.receiveQty = $('#receiveQty2');
        if (!reg.test(m.receiveQty.val()) || m.receiveQty.val().indexOf(",")>0){
            m.receiveQty.css("border-color", "red");
            _common.prompt("Received Quantity can only be a pure number!", 5, "error");
            return false;
        }else {
            m.receiveQty.css("border-color", "#CCCCCC");

        }
        var inputVal = reThousands(m.receiveQty.val());
        if ($.trim(inputVal) == "") {
            m.receiveQty.focus();
            m.updateAmount.val("0");
            _common.prompt("Received Quantity cannot be empty!", 5, "error");
            return false;
        }
        if (!checkNum(inputVal)) {
            m.receiveQty.focus();
            m.updateAmount.val("0");
            _common.prompt("Received Quantity can only be a pure number!", 5, "error");
            return false;
        }
        if (parseFloat(inputVal) > 999999) {//不得大于最高收货量
            _common.prompt("Received Quantity can not be greater than 999999!", 3, "info");
            m.receiveQty.focus();
            return false;
        }
        if (flag) {
            m.receiveNoQty = $('#receiveNoQty2');
            inputVal = reThousands(m.receiveNoQty.val());
            if ($.trim(inputVal) == "") {
                m.receiveNoQty.focus();
                // _common.prompt("搭赠数量不能为空",5,"error");
                _common.prompt("Receiving Free Quantity cannot be empty!", 5, "error");
                return false;
            }
            if (!checkNum(inputVal)) {
                m.receiveNoQty.focus();
                _common.prompt("Receiving Free Quantity can only be a pure number!", 5, "error");
                return false;
            }
            if (parseFloat(inputVal) > 999999) {//不得大于最高收货量
                _common.prompt("Receiving Free Quantity can not be greater than 999999!", 3, "info");
                m.receiveNoQty.focus();
                return false;
            }
        }
        return true;
    }
// 添加输入框进行数据添加
    var addInputValue = function () {
        //正在修改的行不能再修改
        if ($("#receiveQty2").length == 1) {
            return false;
        }
        orderAdd = false;
        //获取旧值
        var cols = tableGrid.getSelectColValue(selectTrTemp, "articleId,isFreeItem,receiveQty,deliveryQty,receiveNoChargeQty,differenceReasonText,differenceReason,orderQty,orderNoChargeQty");
        //添加input框
        tableGrid.find(".info [tag=receiveQty]").text("").append("<input type='text' id='receiveQty2'  class='form-control my-automatic input-sm' placeholder='Please Entry'>");
        // 记录article id
        m.articleId.val(cols['articleId'])
        m.isFreeItem.val(cols['isFreeItem'])
        //赋值
        if (checkNotNull(cols['receiveQty'])) {
            $('#receiveQty2').val(isNotNull(cols['orderQty']));
        } else {
            $('#receiveQty2').val(isNotNull(cols['receiveQty']));
        }

        var reg = /^[0-9]*$/;
        $("#receiveQty2").blur(function () {
            if (reg.test($("#receiveQty2").val()) && $("#receiveQty2").val().indexOf(",")<0){
                $("#receiveQty2").val(this.value);
            }
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#receiveQty2").focus(function () {
            if (reg.test($("#receiveQty2").val()) && $("#receiveQty2").val().indexOf(",")<0){
                $("#receiveQty2").val(this.value);
            }
        });

        //添加input框
        tableGrid.find(".info [tag=receiveNoChargeQty]").text("").append("<input type='text' id='receiveNoQty2' class='form-control my-automatic input-sm' placeholder='Please Entry'>");
        //赋值
        $('#receiveNoQty2').val(toThousands(isNotNull(cols['receiveNoChargeQty'])));

        if (checkNotNull(cols['receiveNoChargeQty'])) {
            $('#receiveNoQty2').val(toThousands(isNotNull(cols['orderNoChargeQty'])));
        } else {
            $('#receiveNoQty2').val(toThousands(isNotNull(cols['receiveNoChargeQty'])));
        }

        $("#receiveNoQty2").blur(function () {
            $("#receiveNoQty2").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#receiveNoQty2").focus(function () {
            $("#receiveNoQty2").val(reThousands(this.value));
        });

        //添加input框
        tableGrid.find(".info [tag=differenceReasonText]").text("").append("<div class='aotu-pos good-select' style='z-index:1;'><input type='text' class='form-control my-automatic input-sm' id='differenceReason2' style='overflow:scroll;text-overflow:ellipsis;'>" +
            " <a id='diffreasonRefresh2' href='javascript:void(0);' title='Refresh' class='auto-but glyphicon glyphicon-refresh refresh'></a>" +
            " <a id='diffreasonRemove2' href='javascript:void(0);' title='Clear' class='auto-but glyphicon glyphicon-remove circle'></a></div>");

        differenceReason = $("#differenceReason2").myAutomatic({
            url: url_root + "/cm9010/getReceiptDifferReason",
            ePageSize: 5,
            startCount: 0,
        });
        $.myAutomatic.setValueTemp(differenceReason, cols['differenceReason'], cols['differenceReasonText']);
        $('td[tag=differenceReasonText]').css("overflow", "visible");
        if (cols['differenceReasonText'] != null && cols['differenceReasonText'] != '') {
            $("#differenceReason2").prop("disabled", false);
            $("#diffreasonRefresh2").show();
            $("#diffreasonRemove2").show();
        } else {
            $("#differenceReason2").prop("disabled", true);
            $("#diffreasonRefresh2").hide();
            $("#diffreasonRemove2").hide();
        }

        $("#receiveQty2").on("focus change", function () {
            var  deliveryQty=isNotNull(tableGrid.find('.info td[tag=deliveryQty]').text());
            var receiveQty2 = $("#receiveQty2").val();
            var  varQtyval= deliveryQty-receiveQty2;
            tableGrid.find('.info td[tag=varQty]').text(varQtyval);
            var varQty=tableGrid.find('.info td[tag=varQty]').text();
            // let receiveNoQty = parseInt(reThousands($("#receiveNoQty2").val()));
            // let orderQty = parseInt(reThousands($(".info [tag=orderQty]").text()));
            // let orderNoQty = parseInt(reThousands($(".info [tag=orderNoChargeQty]").text()));
            if(varQty!=0){
                $("#differenceReason2").prop("disabled", false);
                $("#diffreasonRefresh2").show();
                $("#diffreasonRemove2").show();
                $.myAutomatic.replaceParam(differenceReason, '');
            }else {
                if (varQty==0) {
                    $("#differenceReason2").prop("disabled", true);
                    $.myAutomatic.cleanSelectObj(differenceReason);
                    $("#diffreasonRefresh2").hide();
                    $("#diffreasonRemove2").hide();
                }
            }
            // if (receiveQty < orderQty) {
            //     $("#differenceReason2").prop("disabled", false);
            //     $("#diffreasonRefresh2").show();
            //     $("#diffreasonRemove2").show();
            //     $.myAutomatic.replaceParam(differenceReason, '');
            // } else {
            //     if (receiveQty === orderQty) {
            //         $("#differenceReason2").prop("disabled", true);
            //         $.myAutomatic.cleanSelectObj(differenceReason);
            //         $("#diffreasonRefresh2").hide();
            //         $("#diffreasonRemove2").hide();
            //     }
            // }
        });
    }
    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function (flgType) {
        switch (flgType) {
            case "1":
                initTable();//列表初始化
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    /**
     * 发起审核并通过
     * @param recordId
     */
    var approval = function (recordId) {
        $.myAjaxs({
            url: url_root + "/audit/getStep",
            async: true,
            cache: false,
            type: "post",
            data: {
                recordId: recordId,
                typeId: m.typeId.val()
            },
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    if (result.data) {
                        // 设置审核记录ID
                        $("#auditId").val(result.data);
                        $("#audit_affirm").prop('disabled', false);

                        var auditStepId = $("#auditId").val();
                        //审核结果
                        var auditStatus = "1";//通过
                        var detailType = "tmp_receive";

                        $.myAjaxs({
                            url: url_root + "/audit/submit",
                            async: true,
                            cache: false,
                            type: "post",
                            data: {
                                auditStepId: auditStepId,
                                auditStatus: auditStatus,
                                auditContent: "",
                                detailType:detailType,
                                toKen: m.toKen.val()
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result.success) {
                                    $("#approval_dialog").modal("hide");
                                    //更新主档审核状态值
                                    //_common.modifyRecordStatus(auditStepId,auditStatus);
                                    m.submitAuditBut.attr("disabled", "disabled");
                                    updateReceiveStatus();
                                    _common.prompt("Saved Successfully!", 3, "success");// 保存审核信息成功
                                } else {
                                    m.submitAuditBut.removeAttr("disabled");
                                    _common.prompt("Saved Failure!", 5, "error");// 保存审核信息失败
                                }
                                m.toKen.val(result.toKen);
                            },
                            complete: _common.myAjaxComplete
                        });
                    }
                } else {
                    if (result.data == 'Param') {
                        _common.prompt('Record fetch failed, Please try again!', 5, "error"); // 没有查询到记录（参数为空）
                        return;
                    }
                    if (result.data == 'Null') {
                        _common.prompt('No Matching!', 5, "error"); // 没有查询到记录（返回结果为空）
                        return;
                    }
                    if (result.data == 'Inconformity') {
                        _common.prompt('You do not have permission to process!', 5, "error"); // 角色不符合
                        return;
                    }
                    _common.prompt('Query failed, Please try again!', 5, "error"); // 查询失败
                    $("#audit_affirm").prop('disabled', true);
                }
            },
            error: function (e) {
                _common.prompt("The request failed, Please try again!", 5, "error");// 请求失败
            }
        });
    };

    var updateReceiveStatus = function () {
        $.myAjaxs({
            url: url_root + "/receive/updateStatus",
            async: true,
            cache: false,
            type: "post",
            data: {
                receiveId: $("#receiveNo").val(),
                toKen: m.toKen.val()
            },
            dataType: "json",
            success: function (result) {
            },
            error: function (e) {
                _common.prompt("The request failed, Please try again!", 5, "error");// 请求失败
            }
        })
    };

    // 画面按钮点击事件
    var but_event = function () {
        m.physicalReceivingDate.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });
        $("#receiveNoQty").on("blur", function (e) {
            if (m.receiveNoQty.val() != null || m.receiveNoQty.val() != null) {
                $("#receiveNoQty").css("border-color", "#CCC");
            }
        });
        $("#receiveQty").on("blur", function (e) {
            if (m.receiveQty.val() != null || m.receiveQty.val() != null) {
                $("#receiveQty").css("border-color", "#CCC");
            }
        });

        // 发起审核, 直接通过
        m.submitAuditBut.on("click", function (){
            if (verifyTable()){
                _common.myConfirm("Are you sure to submit?", function (result) {
                    if (result != "true") {
                        return false;
                    }
                    if (result == "true") {
                        _common.loading();
                        //发起审核
                        var typeId = m.typeId.val();
                        var nReviewid = m.reviewId.val();
                        var recordCd = m.receiveNo.val();
                        var detailType = "tmp_receive";

                        var fileDetail = [], fileDetailJson = "";
                        $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                            var file = {
                                informCd: m.receiveNo.val(),
                                fileType: '03',//文件类型 - 收货
                                fileName: $(this).find('td[tag=fileName]').text(),//文件名称
                                filePath: $(this).find('td>a').attr("filepath"),//文件路径
                            }
                            fileDetail.push(file);
                        });
                        if (fileDetail.length > 0) {
                            fileDetailJson = JSON.stringify(fileDetail)
                        }
                        // 处理头档数据
                        if (fileDetail.length > 0){
                            let bean = {
                                receiveId: m.receiveNo.val(),
                                // receiveDate: fmtStringDate(m.receiveDate.val()),
                            }
                            let _data = {
                                searchJson: JSON.stringify(bean),
                                fileDetailJson: fileDetailJson,
                                toKen: m.toKen.val()
                            }
                            $.myAjaxs({
                                url: url_root + '/receive/saveDocumentUrl',
                                async: true,
                                cache: false,
                                timeout: 50000,
                                type: "post",
                                data: _data,
                                dataType: "json",
                            });
                        }
                        _common.initiateReceiveAudit(m._storeCd.val(), recordCd, typeId, nReviewid,detailType, m.toKen.val(), function (callback) {
                            // 变为查看模式
                            setDisable();
                            m.viewSts.val("view");
                            //审核按钮禁用
                            // m.approvalBut.prop("disabled",true);
                            m.submitAuditBut.prop("disabled", true);
                            m.toKen.val(callback.token);
                            if (callback.success) {
                                $("#approval_dialog").modal("hide");
                                //更新主档审核状态值
                                //_common.modifyRecordStatus(auditStepId,auditStatus);
                                m.submitAuditBut.attr("disabled","disabled");
                                // updateReceiveStatus();
                                _common.prompt("Saved Successfully!", 3, "success");// 保存审核信息成功
                            } else {
                                m.submitAuditBut.removeAttr("disabled");
                                _common.prompt(callback.message, 5, "error");// 保存审核信息失败
                            }
                            _common.loading_close();
                            // approval(recordCd);
                        });
                        _common.loading_close();
                    }

                })
            }

        })

        // 保存按钮
        m.saveBtn.on("click", function () {
            var positionFlg = true;
            _common.checkPosition(m._storeCd.val(),function (result) {
                if(!result.success){
                    _common.prompt("You do not have permission to submit it!",5,"info");
                    positionFlg = false;
                }
            });
            if(!positionFlg){
                return false;
            }
            // 还原表格
            if (!orderAdd && verifyDialogSearch2(true)) {
                let varQty = tableGrid.find('.info td[tag=varQty]').text();
                let receiveQty = parseInt(reThousands($("#receiveQty2").val()));
                let receiveNoQty = parseInt(reThousands($("#receiveNoQty2").val()));
                let orderQty = parseInt(reThousands($(".info [tag=orderQty]").text()));
                let orderNoQty = parseInt(reThousands($(".info [tag=orderNoChargeQty]").text()));
                let msg = "are you sure to submit?";
                // $("#receiveQty2").trigger("blur change");
                $("#receiveQty2").trigger("focus change");
                m.differenceReason = $("#differenceReason2");
                m.receiveNoQty = $("#receiveNoQty2");
                m.receiveQty = $("#receiveQty2");
                /*if(receiveNoQty > orderNoQty){
					// msg = "receiving free quantity is greater than order free quantity, " + msg;
					_common.prompt("receiving free qty can not be greater than order free qty!");
					m.receiveNoQty.focus();
					$("#receiveNoQty2").css("border-color","red");
					return false;
				}else if(receiveNoQty < orderNoQty){
					$("#receiveNoQty2").css("border-color","#CCC");
					if(m.differenceReason.val().trim()==''){
						_common.prompt("Differnece Reason cannot be empty!");
						m.differenceReason.focus();
						m.differenceReason.css("border-color","red");
						return false;
					}else{
						m.differenceReason.css("border-color","#CCCCCC");
					}
					msg = "receiving free quantity is less than order free quantity, " + msg;
				}*/
                if (receiveQty > orderQty) {
                    /*_common.prompt("receiving qty can not be greater than order qty!");
                    m.receiveQty.focus();
                    $("#receiveQty2").css("border-color", "red");
                    return false;*/
                    _common.prompt("receiving quantity is more than order quantity!", 3, "info");
                } else if (receiveQty < orderQty) {
                    $("#receiveQty2").css("border-color", "#CCC");
                    msg = "receiving quantity is less than order quantity, " + msg;
                }
                if (varQty !==0){
                    if (m.differenceReason.val().trim() == '') {
                        _common.prompt("Differnece Reason cannot be empty!");
                        m.differenceReason.focus();
                        m.differenceReason.css("border-color", "red");
                        return false;
                    } else {
                        m.differenceReason.css("border-color", "#CCCCCC");
                    }
                }
                msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
                let cols = tableGrid.getSelectColValue(selectTrTemp, "articleId");
                $("#zgGridTable>.zgGrid-tbody tr:not(:last-child)").each(function () {
                    let articleId = $(this).find('td[tag=articleId]').text();
                    if (articleId == cols["articleId"]) {
                        let _price = reThousands(m.receivePrice.val());
                        let _amount = Number(accMul(receiveQty, _price)).toFixed(2);
                        let _total = accAdd(receiveQty, receiveNoQty);
                        $(this).find('td[tag=receiveQty]').text(toThousands(m.receiveQty.val()));
                        $(this).find('td[tag=receiveQty1]').text(toThousands(m.receiveQty.val()));
                        $(this).find('td[tag=receiveNoChargeQty]').text(toThousands(m.receiveNoQty.val()));
                        $(this).find('td[tag=receiveNoChargeQty1]').text(toThousands(m.receiveNoQty.val()));
                        $(this).find('td[tag=receiveTotalQty]').text(toThousands(_total));
                        $(this).find('td[tag=receiveTotalQty1]').text(toThousands(_total));
                        $(this).find('td[tag=receiveTotalAmt]').text(toThousands(_amount));
                        $(this).find('td[tag=differenceReason]').text(m.differenceReason.attr('k'));
                        $(this).find('td[tag=differenceReasonText]').text(m.differenceReason.attr('v'));
                    }
                });
                let _amt = getAmount();
                m.receiveAmount.val(toThousands(_amt));
                orderAdd = true;
                total_qty();
            }
            // 判断是否允许保存
            let _sts = m.viewSts.val();
            let _t = m._type.val();
            if (_sts == "view" && _t != "0") {
                return;
            }
            // 数据检查
            if (verifySearch()) {
                total_qty();
                // 获取单据编号
                let _storeCd = m._storeCd.val();
                let _voucherNo = m._orderId.val();

                // 遍历List，处理求和
                // let _taxRate = m.taxRate.val();
                let itemDetail = [], _amount = 0, _receiveTax = 0;
                $("#zgGridTable>.zgGrid-tbody tr:not(:last-child)").each(function () {
                    // 数据计算
                    let _qty = reThousands($(this).find('td[tag=receiveQty]').text());
                    let orderQty = reThousands($(this).find('td[tag=orderQty]').text());
                    let varQty = reThousands($(this).find('td[tag=varQty]').text());
                    if (_qty == "") {
                        _qty = null;
                    }
                    let _price = reThousands($(this).find('td[tag=receivePrice]').text());
                    let _receiveNoChargeQty = reThousands($(this).find('td[tag=receiveNoChargeQty]').text());
                    if (_receiveNoChargeQty == "") {
                        _receiveNoChargeQty = null;
                    }
                    let _receiveTotalQty = reThousands($(this).find('td[tag=receiveTotalQty]').text());
                    if (_receiveTotalQty == "") {
                        _receiveTotalQty = null;
                    }
                    let _taxRate = null, _amtNoTax = null, _amt = null, _tax = null;
                    if (_qty != null && _price != null) {
                        //税率
                        let _taxRate = $(this).find('td[tag=taxRate]').text();
                        let _amtNoTax = Number(accMul(_qty, _price)).toFixed(2);
                        let _amt = Number(accMul(_amtNoTax, accAdd(1, _taxRate))).toFixed(2);
                        let _tax = Number(accMul(_amtNoTax, _taxRate)).toFixed(2);
                    }
                    let orderItem = {
                        storeCd: _storeCd,
                        orderId: _voucherNo,
                        serialNo: $(this).find('td[tag=serialNo]').text(),
                        articleId: $(this).find('td[tag=articleId]').text(),
                        receiveQty: _qty,
                        orderQty:orderQty,
                        varQty:varQty,
                        // receiveNoChargeQty:reThousands($(this).find('td[tag=receiveNoChargeQty1]').text()),
                        receiveNoChargeQty: _receiveNoChargeQty,
                        // receiveTotalQty:reThousands($(this).find('td[tag=receiveTotalQty1]').text()),
                        receiveTotalQty: _receiveTotalQty,
                        differenceReason: $(this).find('td[tag=differenceReason]').text(),
                        receiveTotalAmt: _amt,
                        receiveTotalAmtNoTax: _amtNoTax,
                        receiveTax: _tax,
                        isFreeItem: $(this).find('td[tag=isFreeItem]').text()
                    }
                    itemDetail.push(orderItem);
                    if (_amt != null) {
                        _amount = accAdd(_amount, _amt);
                    } else if (_tax != null) {
                        _receiveTax = accAdd(_receiveTax, _tax);
                    }
                });
                if (itemDetail.length < 1) {
                    _common.prompt("Commodity information cannot be empty!", 5, "error");
                    return false;
                }
                // 校验原因
                for (let i = 0; i < itemDetail.length; i++) {
                    if (itemDetail[i].receiveQty == null) {
                        continue;
                    }
                    // if (itemDetail[i].receiveQty < itemDetail[i].orderQty) {
                    //     if(itemDetail[i].differenceReason==''){
                    //         _common.prompt("Item "+itemDetail[i].articleId+" Differnece Reason cannot be empty!");
                    //         return false;
                    //     }
                    // }
                    if (itemDetail[i].varQty!=0) {
                        if(itemDetail[i].differenceReason==''){
                            _common.prompt("Item "+itemDetail[i].articleId+" Differnece Reason cannot be empty!");
                            return false;
                        }
                    }
                }
                //附件信息
                var fileDetail = [], fileDetailJson = "";
                $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                    var file = {
                        informCd: m.receiveNo.val(),
                        fileType: '03',//文件类型 - 收货
                        fileName: $(this).find('td[tag=fileName]').text(),//文件名称
                        filePath: $(this).find('td>a').attr("filepath"),//文件路径
                    };
                    fileDetail.push(file);
                });
                if (fileDetail.length > 0) {
                    fileDetailJson = JSON.stringify(fileDetail)
                }
                let orderDiff = m.orderDiff.val();
                if ($("#physicalReceivingDate").val() == null || $("#physicalReceivingDate").val() === ''
                   || $("#physicalReceivingDate").val().length<16) {
                    $("#physicalReceivingDate").val(new Date().Format("dd/MM/yyyy hh:mm"));
                }
                // 处理头档数据
                let bean = {
                    receiveId: m.receiveNo.val(),
                    storeCd: _storeCd,
                    orderId: _voucherNo,
                    // receiveDate: fmtStringDate(m.receiveDate.val()),
                    receiveAmt: _amount,
                    receiveTax: _receiveTax,
                    physicalReceivingDate: fmtStringDate($("#physicalReceivingDate").val().substring(0,10)),
                    physicalReceivingHms: fmtStringTime($("#physicalReceivingDate").val().substring(11)),
                    orderDifferentiate: orderDiff,
                    receivedRemark: m.remarks.val()
                };
                let _data = {
                    searchJson: JSON.stringify(bean),
                    listJson: JSON.stringify(itemDetail),
                    fileDetailJson: fileDetailJson,
                    toKen: m.toKen.val()
                };
                _common.myConfirm("Are you sure you want to save?", function (result) {
                    if (result != "true") {
                        return false;
                    }
                    $.myAjaxs({
                        url: url_root + '/receive/save',
                        async: true,
                        cache: false,
                        timeout: 50000,
                        type: "post",
                        data: _data,
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                m.toKen.val(result.toKen);
                                // 赋值收货编号
                                m.receiveNo.val(result.o);
                                m.viewSts.val("edit");
                                _common.prompt("Data saved successfully！", 2, "success");
                                setDisable();
                                tableGrid.find("tr").unbind("dblclick");
                            } else {
                                _common.prompt(result.msg, 5, "error");
                            }
                        },
                        error: function (e) {
                            _common.prompt("Data saved failed！", 5, "error");/*保存失败*/
                        }
                    });
                })
            }
        });

        // 返回一览
        m.returnsViewBut.on("click", function () {
            _common.updateBackFlg(KEY);
            top.location = url_left;
        });

        // 收货数量栏位监听事件
        m.receiveQty.on("change", function () {
            if (verifyDialogSearch(false)) {
                let _qty = reThousands(m.receiveQty.val());
                let _price = reThousands(m.receivePrice.val());
                let _amount = Number(accMul(_qty, _price)).toFixed(2);
                m.updateAmount.val(toThousands(_amount));
            }
        });

        // 修改弹出窗-确认按钮事件
        $("#update_affirm").on("click", function () {
            let flg = $('#update_dialog').attr("flg");
            if (flg != "upt") {
                return;
            }
            if (selectTrTemp == null) {
                _common.prompt("Please select a record first!", 3, "info"); // 未选中商品
                return false;
            }
            if (verifyDialogSearch(true)) {
                let receiveQty = parseInt(reThousands(m.receiveQty.val()));
                // let receiveNoQty = parseInt(reThousands(m.receiveNoQty.val()));
                let orderQty = parseInt(reThousands(m.orderQty.val()));
                // let orderNoQty = parseInt(reThousands(m.orderNoQty.val()));
                let msg = "are you sure to submit?";
                /*if(receiveNoQty > orderNoQty){
					// msg = "receiving free quantity is greater than order free quantity, " + msg;
					_common.prompt("receiving free qty can not be greater than order free qty!");
					m.receiveNoQty.focus();
					$("#receiveNoQty").css("border-color","red");
					return false;
				}else if(receiveNoQty < orderNoQty){
					$("#receiveNoQty").css("border-color","#CCC");
					if(m.differenceReason.val().trim()==''){
						_common.prompt("Differnece Reason cannot be empty!");
						m.differenceReason.focus();
						m.differenceReason.css("border-color","red");
						return false;
					}else{
						m.differenceReason.css("border-color","#CCCCCC");
					}
					msg = "receiving free quantity is less than order free quantity, " + msg;
				}*/
                if (receiveQty > orderQty) {
                    _common.prompt("receiving qty can not be greater than order qty!");
                    m.receiveQty.focus();
                    $("#receiveQty").css("border-color", "red");
                    return false;
                } else if (receiveQty < orderQty) {
                    $("#receiveQty").css("border-color", "#CCC");
                    if (m.differenceReason.val().trim() === '') {
                        _common.prompt("Differnece Reason cannot be empty!");
                        m.differenceReason.focus();
                        m.differenceReason.css("border-color", "red");
                        return false;
                    } else {
                        m.differenceReason.css("border-color", "#CCCCCC");
                    }
                    msg = "receiving quantity is less than order quantity, " + msg;
                }
                msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
                _common.myConfirm(msg, function (result) {
                    if (result == "true") {
                        let cols = tableGrid.getSelectColValue(selectTrTemp, "articleId");
                        $("#zgGridTable>.zgGrid-tbody tr:not(:last-child)").each(function () {
                            let articleId = $(this).find('td[tag=articleId]').text();
                            if (articleId == cols["articleId"]) {
                                let _price = reThousands(m.receivePrice.val());
                                let _amount = Number(accMul(receiveQty, _price)).toFixed(2);
                                let _total = accAdd(receiveQty, receiveNoQty);
                                $(this).find('td[tag=receiveQty]').text(toThousands(m.receiveQty.val()));
                                $(this).find('td[tag=receiveQty1]').text(toThousands(m.receiveQty.val()));
                                $(this).find('td[tag=receiveNoChargeQty]').text(toThousands(m.receiveNoQty.val()));
                                $(this).find('td[tag=receiveNoChargeQty1]').text(toThousands(m.receiveNoQty.val()));
                                $(this).find('td[tag=receiveTotalQty]').text(toThousands(_total));
                                $(this).find('td[tag=receiveTotalQty1]').text(toThousands(_total));
                                $(this).find('td[tag=receiveTotalAmt]').text(toThousands(_amount));
                                $(this).find('td[tag=differenceReason]').text(m.differenceReason.attr('k'));
                                $(this).find('td[tag=differenceReasonText]').text(m.differenceReason.attr('v'));
                            }
                        });
                        let _amt = getAmount();
                        m.receiveAmount.val(toThousands(_amt));
                        $('#update_dialog').modal("hide");
                        total_qty();
                    }
                });
            }
        });

        // 修改弹出窗-关闭按钮事件
        $("#update_cancel").on("click", function () {
            let flg = $('#update_dialog').attr("flg");
            if (flg == "view") {
                $('#update_dialog').modal("hide");
                return false;
            }
            _common.myConfirm("Are you sure you want to close?", function (result) {
                $("#receiveNoQty").css("border-color", "#CCC");
                $("#receiveQty").css("border-color", "#CCC");
                m.differenceReason.css("border-color", "#CCC");
                if (result == "true") {
                    $('#update_dialog').modal("hide");
                }
            });
        });
    }

    // 清空弹窗内容
    var clearDialogValue = function (flag) {
        $("#barcode").val("");
        $("#itemId").val("");
        $("#itemName").val("");
        $("#uom").val("");
        $("#receivePrice").val("");
        $("#orderQty").val("");
        $("#receiveQty").val("");
        $("#orderNoQty").val("");
        $("#receiveNoQty").val("");
        $("#updateAmount").val("");
        $("#differenceReason").val("");
    }
    // 设置弹窗是否允许编辑
    var setDialogIsDisable = function (flag) {
        $("#receiveQty").prop("disabled", flag);
        $("#receiveNoQty").prop("disabled", flag);
        $("#differenceReason").prop("disabled", true);
        $("#diffreasonRefresh").hide();
        $("#diffreasonRemove").hide();
    }

    // 计算总金额
    var getAmount = function () {
        var _amount = 0;
        $("#zgGridTable>.zgGrid-tbody tr").each(function () {
            // 金额计算
            var _amt = reThousands($(this).find('td[tag=receiveTotalAmt]').text());
            _amount = accAdd(_amount, _amt);
        });
        return _amount;
    }

    // 获取详情信息
    var getRecord = function () {
        let _data = {};
        let _url = url_left;
        if (m._type.val() == '1' || m.viewSts.val() == 'edit') {
            _url = url_root + "/receive";
            _data = {
                orderType: '0',
                receiveId: m._receiveId.val()
            }
        } else {
            // 判断订货单 view|edit
            let _sts = m.viewSts.val() == 'add' ? '1' : '0';
            Sts = _sts;
            _data = {
                storeCd: m._storeCd.val(),
                orderId: m._orderId.val(),
                pageSts: _sts
            }
        }
        let param = JSON.stringify(_data);
        $.myAjaxs({
            url: _url + "/get",
            async: true,
            cache: false,
            type: "post",
            data: "searchJson=" + param,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    // 获取商品详情
                    getDetails(param, _url);
                    // 加载数据
                    m._storeCd.val(result.o.storeCd);
                    m.storeName.val(result.o.storeName);
                    m.receiveNo.val(result.o.receiveId);
                    m.orderNo.val(result.o.orderId);
                    m.orderDate.val(fmtIntDate(result.o.orderDate));
                    m.warehouseName.val(result.o.vendorName);
                    m.orderType.val(result.o.orderMethodName);
                    if (result.o.receiveDate == null || result.o.receiveDate === '') {
                        m.receiveDate.val(new Date().Format("dd/MM/yyyy hh:mm:ss"));
                    } else {
                        m.receiveDate.val(fmtIntDate(result.o.receiveDate)+" "+fmtIntTime(result.o.receiveHms));
                    }
                    m.orderAmount.val(toThousands(result.o.orderAmt));
                    m.receiveAmount.val(toThousands(result.o.receiveAmt));
                    m.reviewSts.val(result.o.reviewSts);
                    m.remarks.val(result.o.receivedRemark);
                    m.taxRate.val(result.o.taxRate);
                    m.orderDiff.val(result.o.orderDifferentiate);
                    if(m._receiveId.val()){
                        m.physicalReceivingDate.val(fmtIntDate(result.o.physicalReceivingDate)+" "+fmtIntPhyTime(result.o.physicalReceivingHms));
                        $("#creater").val(result.o.commonDTO.createUserId);
                        $("#ca_date").val(_common.formatCreateDate(result.o.commonDTO.createYmd+result.o.commonDTO.createHms));
                    }
                    //加载附件信息
                    attachmentsParamGrid = "recordCd=" + result.o.receiveId + "&fileType=03";
                    attachmentsTable.setting("url", url_root + "/file/getFileList");//加载附件
                    attachmentsTable.setting("param", attachmentsParamGrid);
                    attachmentsTable.loadData(null);
                    // 检查是否允许submit 店长sm才拥有权限
                    checkSubmit();
                } else {
                    _common.prompt(result.msg, 5, "error");
                    return;
                }
            },
            error: function (e) {
                _common.prompt("Failed to load data!", 5, "error");
                return;
            }
        });
    }

    // 获取详情List
    var getDetails = function (param, _url) {
        paramGrid = "searchJson=" + param;
        tableGrid.setting("url", _url + "/getDetails");
        tableGrid.setting("param", paramGrid);
        tableGrid.loadData();
    }

    //验证数据是否合法
    var verifySearch = function () {
        var temp = m._storeCd.val();
        if (temp == "" || temp == null) {
            _common.prompt("Failed to get store No.!", 5, "error");
            return false;
        }
        temp = m._orderId.val();
        if (temp == "" || temp == null) {
            _common.prompt("Failed to get order cd!", 5, "error");
            return false;
        }
        /*temp = m.physicalReceivingDate.val();
        if (temp == "" || temp == null) {
            _common.prompt("Please select the Receiving Date!", 5, "error");
            m.physicalReceivingDate.focus();
            m.physicalReceivingDate.css("border-color", "red");
            return false;
        }else if(_common.judgeValidDate(m.physicalReceivingDate.val())){
            _common.prompt("Please enter a valid date!",3,"info");
            m.physicalReceivingDate.css("border-color","red");
            $("#physicalReceivingDate").focus();
            return false;
        } else {
            m.physicalReceivingDate.css("border-color", "#CCC");
        }*/
        return true;
    }

    // 验证弹窗页面录入项是否符合
    var verifyDialogSearch = function (flag) {
        var flg = $('#update_dialog').attr("flg");
        if (flg != "upt") {
            return false;
        }
        var inputVal = reThousands(m.receiveQty.val());
        if ($.trim(inputVal) == "") {
            m.receiveQty.focus();
            m.updateAmount.val("0");
            _common.prompt("Received Quantity cannot be empty!", 5, "error");
            return false;
        }
        if (!checkNum(inputVal)) {
            m.receiveQty.focus();
            m.updateAmount.val("0");
            _common.prompt("Received Quantity can only be a pure number!", 5, "error");
            return false;
        }
        if (parseFloat(inputVal) > 999999) {//不得大于最高收货量
            _common.prompt("Received Quantity can not be greater than 999999!", 3, "info");
            m.receiveQty.focus();
            return false;
        }
        // if(flag){
        // 	inputVal = reThousands(m.receiveNoQty.val());
        // 	if($.trim(inputVal)==""){
        // 		m.receiveNoQty.focus();
        // 		_common.prompt("Receiving Free Quantity cannot be empty!",5,"error");
        // 		return false;
        // 	}
        // 	if(!checkNum(inputVal)){
        // 		m.receiveNoQty.focus();
        // 		_common.prompt("Receiving Free Quantity can only be a pure number!",5,"error");
        // 		return false;
        // 	}
        // 	if(parseFloat(inputVal)>999999){//不得大于最高收货量
        // 		_common.prompt("Receiving Free Quantity can not be greater than 999999!",3,"info");
        // 		m.receiveNoQty.focus();
        // 		return false;
        // 	}
        // }
        return true;
    }

    // 判断是否是数字
    var checkNum = function (value) {
        var reg = /^[0-9]*$/;
        return reg.test(value);
    }

    //表格初始化-采购样式
    var initTable = function () {
        tableGrid = $("#zgGridTable").zgGrid({
            title: "Warehouse Delivery Receiving Document",
            param: paramGrid,
            localSort: true,
            height: "300",
            colNames: ["Store", "Order", "No.", "Price", "Item Barcode", "Item Code", "Item Name", "UOM",
                "Order Qty", "Order Free Qty", "Total Order Qty", "Order Amount", "Tax Rate"," SO Transfer Qty","HHT Receiving Qty", "Receiving Qty", "Receiving Qty",
                "Receiving Free Qty", "Receiving Free Qty", "Total Receiving Qty", "Total Receiving Qty", "Receiving Amount",
                "Variance Qty","Item Type", "Item Type", "DifferenceReason", "Difference Reason","hht Receive Date","hht Receive Time"],
            colModel: [
                {name: "storeCd", type: "text", text: "right", ishide: true},
                {name: "orderId", type: "text", text: "right", ishide: true},
                {name: "serialNo", type: "text", text: "right", ishide: true},
                {name: "receivePrice", type: "text", text: "right", ishide: true},
                {name: "barcode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "articleId", type: "text", text: "right", width: "110", ishide: false, css: ""},
                {name: "articleName", type: "text", text: "left", width: "500", ishide: false, css: ""},
                {name: "orderUnit", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "orderQty",type: "text",text: "right",width: "130",ishide: false,css: "",getCustomValue: getThousands},
                {name: "orderNoChargeQty", type: "text", text: "right", width: "160", ishide: true, css: "", getCustomValue: getThousands},
                {name: "orderTotalQty", type: "text", text: "right", width: "150", ishide: true, css: "", getCustomValue: getThousands},
                {name: "orderAmt", type: "text", text: "right", width: "150", ishide: true, css: "", getCustomValue: getThousands},
                {name: "taxRate", type: "text", text: "right", width: "130", ishide: true, css: ""},
                {name: "deliveryQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getValue},
                {name: "hhtReceiveQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
                {name: "receiveQty", type: "text", text: "right", width: "130", ishide: false, css: "", getCustomValue: getThousands},
                {name: "receiveQty1", type: "text", text: "right", width: "130", ishide: true, css: "", getCustomValue: isNull},
                {name: "receiveNoChargeQty", type: "text", text: "right", width: "160", ishide: true, css: "", getCustomValue: getThousands},
                {name: "receiveNoChargeQty1", type: "text", text: "right", width: "160", ishide: true, css: "", getCustomValue: isNull},
                {name: "receiveTotalQty", type: "text", text: "right", width: "160", ishide: true, css: "", getCustomValue: getThousands},
                {name: "receiveTotalQty1", type: "text", text: "right", width: "160", ishide: true, css: "", getCustomValue: isNull},
                {name: "receiveTotalAmt", type: "text", text: "right", width: "130", ishide: true, css: "", getCustomValue: getThousands},
                {name: "varQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
                {name: "isFreeItem", type: "text", text: "left", ishide: true},
                {name: "isFreeItemText", type: "text", text: "left", width: "100", ishide: false, css: "", getCustomValue: getItemType},
                {name: "differenceReason", type: "text", text: "left", ishide: true},
                {name: "differenceReasonText", type: "text", text: "left", width: "210", ishide: false, css: ""},
                {name:"hhtReceiveDate",type:"text",text:"left",width:"200",ishide:true,css:""},
                {name:"hhtReceiveTime",type:"text",text:"left",width:"200",ishide:true,css:""},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: false,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox: false,
            freezeHeader: true,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                table = self;
                selectTrTemp = null;//清空选择的行
                //更新总条数(注减一是去除标题行)
                var total = tableGrid.find("tr").length - 1;
                $("#records").text(total);
                if(m.viewSts.val() !== 'view') {
                    tableGrid.find("tr").on('dblclick', function (e) {
                        addInputValue();
                    });
                }
                total_qty();
                var hhtDate = $("#zgGridTable_0_tr").find('td[tag=hhtReceiveDate]').text();
                var hhtTime = $("#zgGridTable_0_tr").find('td[tag=hhtReceiveTime]').text();
                if(!m._receiveId.val() && $("#_reviewSts").val() == "Receiving Pending"){
                    m.physicalReceivingDate.val(fmtIntDate(hhtDate)+" "+fmtIntPhyTime(hhtTime));
                }

                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {butType: "view", butId: "view", butText: "View", butSize: "",},// 查看
                {butType: "update", butId: "update", butText: "Modify", butSize: "",},// 修改,
                {
                    butType: "custom",
                    butHtml: "<button id='attachments' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>"
                },//附件
            ],
        });
        $("#zgGridTable_main_foot").append("<div class='zgGrid-tfoot-td' id='zgGridTtable_tfoot_box'><span class='zg-page-records'>Total: <span id='records'>0</span> items</span></div>");
    };

    // 数量合计
    var total_qty = function () {
        if ($("#zgGridTable>.zgGrid-tbody tr").length > 0) {
            var orderQty = 0, orderNoChargeQty = 0, orderTotalQty = 0, receiveQty = 0, receiveNoChargeQty = 0,
                receiveTotalQty = 0,hhtReceiveQty =0,deliveryQty=0;
            $("#zgGridTable>.zgGrid-tbody tr").each(function () {
                var td_orderQty = parseFloat($(this).find('td[tag=orderQty]').text().replace(/,/g, ""));
                /*var td_orderNoChargeQty= parseFloat($(this).find('td[tag=orderNoChargeQty]').text().replace(/,/g, ""));
				var td_orderTotalQty= parseFloat($(this).find('td[tag=orderTotalQty]').text().replace(/,/g, ""));*/
                var td_deliveryQty = parseFloat($(this).find('td[tag=deliveryQty]').text().replace(/,/g, ""));
                var td_hhtReceiveQty = parseFloat($(this).find('td[tag=hhtReceiveQty]').text().replace(/,/g, ""));
                var td_receiveQty = parseFloat($(this).find('td[tag=receiveQty]').text().replace(/,/g, ""));
                /*var td_receiveNoChargeQty= parseFloat($(this).find('td[tag=receiveNoChargeQty]').text().replace(/,/g, ""));
				var td_receiveTotalQty= parseFloat($(this).find('td[tag=receiveTotalQty]').text().replace(/,/g, ""));*/
                if (!isNaN(td_orderQty))
                    orderQty += parseFloat(td_orderQty);
                /*if(!isNaN(td_orderNoChargeQty))
					orderNoChargeQty +=  parseFloat(td_orderNoChargeQty);
				if(!isNaN(td_orderTotalQty))
					orderTotalQty +=  parseFloat(td_orderTotalQty);*/
                if(!isNaN(td_deliveryQty))
                    deliveryQty +=  parseFloat(td_deliveryQty);
                if(!isNaN(td_hhtReceiveQty))
                    hhtReceiveQty +=  parseFloat(td_hhtReceiveQty);
                if (!isNaN(td_receiveQty))
                    receiveQty += parseFloat(td_receiveQty);
                /*if(!isNaN(td_receiveNoChargeQty))
					receiveNoChargeQty +=  parseFloat(td_receiveNoChargeQty);
				if(!isNaN(td_receiveTotalQty))
					receiveTotalQty +=  parseFloat(td_receiveTotalQty);*/
            })
            var total = "<tr style='text-align:right' id='total_qty'><td></td><td></td><td></td>" +
                "<td>Total:</td>" +
                "<td>" + fmtIntNum(orderQty) + "</td>" +
                /*"<td>"+fmtIntNum(orderNoChargeQty)+"</td>"+
				"<td>"+fmtIntNum(orderTotalQty)+"</td>" +*/
                "<td>" + fmtIntNum(deliveryQty) + "</td>" +
                "<td>" + fmtIntNum(hhtReceiveQty) + "</td>" +
                "<td>" + fmtIntNum(receiveQty) + "</td>" +
                /*"<td>"+fmtIntNum(receiveNoChargeQty)+"</td>" +
				"<td>"+fmtIntNum(receiveTotalQty)+"</td>" +*/
                "<td></td><td></td><td></td>"+
                "</tr>";
            $("#total_qty").remove();
            $("#zgGridTable_tbody").append(total);
        }
    };

    function getItemType(tdObj, value) {
        let temp = '' + value;
        switch (temp) {
            case "0":
                value = "Normal Item";
                break;
            case "1":
                value = "Free Item";
                break;
            default:
                value = "";
        }
        return $(tdObj).text(toThousands(value));
    }
    function fmtIntTime(time) {
        var res = "";
        if (time==null ||time==""){
            return res;
        }
        res = time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
        return res;
    }
    function fmtIntPhyTime(time) {
        var res = "";
        if (time==null ||time==""){
            return res;
        }
        res = time.substring(0,2)+":"+time.substring(2,4);
        return res;
    }

    // 为0返回'0'
    function getString0(tdObj, value) {
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    function isNull(tdObj, value) {
        return $(tdObj).text("");
    }
    function getValue(tdObj, value) {
         if (value=="" || value==null){
             return $(tdObj).text("0");
         }else {
             return $(tdObj).text(toThousands(value));
         }
    }

    // 运算
    function accAdd(arg1, arg2) {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        return (accMul(arg1, m) + accMul(arg2, m)) / m;
    }

    function accSub(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        return accMul(arg1, m) - accMul(arg2, m);
    }

    function accMul(arg1, arg2) {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
    }

    function accDiv(arg1, arg2) {
        var t1 = 0, t2 = 0, r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        } catch (e) {
        }
        try {
            t2 = arg2.toString().split(".")[1].length
        } catch (e) {
        }
        with (Math) {
            r1 = Number(arg1.toString().replace(".", ""))
            r2 = Number(arg2.toString().replace(".", ""))
            return (r1 / r2) * pow(10, t2 - t1);
        }
    }

    //格式化数字带千分位
    function fmtIntNum(val) {
        if (val == null || val == "") {
            return "0";
        }
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    var verifyTable = function () {
        if ($("#zgGridTable>.zgGrid-tbody tr").length > 0) {
            var blankReason = 0;
            $("#zgGridTable>.zgGrid-tbody tr:not(:last-child)").each(function () {
                var td_deliveryQty = isNotNull(parseFloat($(this).find('td[tag=deliveryQty]').text().replace(/,/g, "")));
                var td_receiveQty = isNotNull(parseFloat($(this).find('td[tag=receiveQty]').text().replace(/,/g, "")));
                if (td_deliveryQty !== td_receiveQty) {
                    if (($(this).find('td[tag=differenceReasonText]').text()).trim() === "" || ($(this).find('td[tag=differenceReason]').text()).trim() === null) {
                        //添加input框
                        $(this).find("td[tag=differenceReasonText]").text("").append("<input type='text' readonly='true' class='form-control my-automatic input-sm' style='border-color: red'>");
                        blankReason++;
                    }
                }
            })
            if (blankReason !== 0) {
                _common.prompt("Difference Reason cannot be empty at the red mark!"); // 请输入店铺
                return false;
            }
            return true;
        }
    }

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num, dit) {
        if (num === 0) {
            return '0';
        }
        dit = typeof dit !== 'undefined' ? dit : 0;
        num = num + '';
        const reg = /\d{1,3}(?=(\d{3})+$)/g;
        let intNum = '';
        let decimalNum = '';
        if (num.indexOf('.') > -1) {
            if (num.indexOf(',') != -1) {
                num = num.replace(/\,/g, '');
            }
            num = parseFloat(num).toFixed(dit);
            // intNum = num.substring(0, num.indexOf('.'))
            // decimalNum = num.substring(num.indexOf('.') + 1, num.length)
            // return (intNum + '').replace(reg, '$&,') + '.' + decimalNum
            return (num + '').replace(reg, '$&,');
        } else {
            return (num + '').replace(reg, '$&,');
        }
    }

    function fmtIntDate(date) {
        var res = "";
        if (date == null || date == "") {
            return res;
        }
        res = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
        return res;
    }

    // 去除千分位
    function reThousands(num) {
        num += '';
        return num.replace(/,/g, '');
    }

    //字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date) {
        var res = "";
        date = date.replace(/\//g, "");
        res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
        return res;
    }

    function fmtStringTime(time) {
        var res = "";
        res = time.replace(/:/g, "");
        return res;
    }

    // 判断是否为null
    function checkNotNull(num) {
        var _return = false;
        if (num === null || num === '') {
            _return = true;
        }
        return _return;
    }

    // 判断是否为null
    function isNotNull(num) {
        if (num === null || num === '' || isNaN(num)) {
            num = 0;
        }
        return num;
    }

    // 栏位显示千分位
    function getThousands(tdObj, value) {
        return $(tdObj).text(toThousands(value));
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receiptEdit');
_start.load(function (_common) {
    _index.init(_common);
});
