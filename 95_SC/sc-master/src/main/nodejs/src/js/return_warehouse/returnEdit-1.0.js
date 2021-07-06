require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax = require("myAjax");
define('returnWarehouseEdit', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        a_item = null,
        a_item_direct=null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        submitFlag = false,
        tempTrObjValue = {},//临时行数据存储
        tempTrBarcode = null,// 选中的商品条码
        tempTrItemCode = null,//选中的商品编号
        tempTrItemName = null,//选中的商品名称
        tempTrUnitId = null,//选中的商品单位id
        tempTrUom = null,//选中的商品单位
        tempTrSpec = null,//选中的商品规格
        tempTrPrice = null,//选中的商品单价
        tempTrItemAmt = null,//选中的商品退货总金额
        tempTrReasonId = null,//选中的退货原因
        tempTrReasonName = null,//选中的退货原因
        tempTrOrderQty = null,//选中的退货量
        tempTrOrderNochargeQty = null,//选中的退货量
        orderRemarkflg = null,
        tempTrOrderNochargeQtynew = null,
        auditNum = 0,//该订单正在审核的数量
        //附件
        returnType = null,
        attachmentsParamGrid = null,
        selectTrTempFile = null,
        common = null;
    const KEY = 'ITEM_RETURN_REQUEST_ENTRY_TO_DC';
    var m = {
        toKen: null,
        searchJson: null,
        // 基本信息
        order_id: null,//订单号
        org_order_id: null,//原订单号
        order_date: null,//退货时间
        order_type: null,
        order_sts: null,
        dialog_flg: null,//弹出框状态
        store_cd: null,
        store_name: null,
        update_ymd: null,
        total_qty: null,
        total_qty_in: null,
        total_amt: null,
        order_remark: null,
        returnflag: null,
        search: null,
        returnsViewBut: null,
        returnsSubmitBut: null,
        orderNochargeQty: null,
        receiveNochargeQty: null,
        userId: null,
        flag: null,
        orderId: null,
        //后面追加
        update_user: null,
        create_user: null,
        create_date: null,
        warehouseCd: null,
        //编辑
        hidStore:null,
        update_dialog: null,
        barcode: null,
        itemCode: null,
        itemName: null,
        unitId: null,
        unitName: null,
        spec: null,
        dc_id: null,//dc
        dc_name: null,//dc
        dc_name_in: null,
        vendorId: null,
        orderQty: null,
        dc_id_in: null,
        orgOrderQty: null,//原订单订货量
        orgOrderNochargeQty: null,//原订单搭赠量
        receiveQty: null,//收货数量
        returnedQty: null,//已退货数量
        realtimeQty: null,//实时库存量
        orderAmount: null,
        orderPrice: null,
        reasonId: null,
        onHandQty: null,
        order_remark_in: null,
        //弹窗按钮
        close: null,
        submit: null,
        //审核
        approvalBut: null,
        approval_dialog: null,
        audit_cancel: null,
        audit_affirm: null,
        typeId: null,
        reType: null,
        aStore: null,
        indirectReturn: null,
        directDC: null,
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
        url_left = _common.config.surl + "/returnWarehouse";
        systemPath = _common.config.surl;
        reThousands = _common.reThousands;
        toThousands = _common.toThousands;
        getThousands = _common.getThousands;
        //_common.initStoreform();
        //画面按钮点击事件
        but_event();
        //店铺 可选仓库（供应商） 下拉
        initAutoMatic();
        //列表初始化
        initTable1();
        //加载可选退货理由
        getSelectValue();
        //附件事件
        attachments_event();
        //加载页面
        initPageByType();
        //表格内按钮事件
        table_event();
        //审核事件
        approval_event();
        $("#org_order_id").attr("disabled", true);
        $("#org_order_id_refresh").hide();
        $("#org_order_id_clear").hide();
        $.myAutomatic.cleanSelectObj(a_orderId);
        setButton();
    }

//设置 是否禁用
    var setButton = function () {
        $("#reType").on("change", function () {
            var reTypeValue = $("#reType").val();
            if (reTypeValue == '10') {
                $("#directReturn").show();
                $("#indirectReturn").hide();
                $("#org_order_id").attr("disabled", true);
                $("#org_order_id_refresh").hide();
                $("#org_order_id_clear").hide();
                $.myAutomatic.cleanSelectObj(a_orderId);
                $("#directDC").show();
                $("#regionRemove").click();
                $("#returnedQtyhi").hide();
                $("#storeRefresh").show();
                $("#storeRemove").show();
            } else {
                $("#org_order_id").attr("disabled", false);
                $("#org_order_id_refresh").show();
                $("#org_order_id_clear").show();
                $("#directReturn").hide();
                $("#indirectReturn").show();
                $("#directDC").hide();
                $("#regionRemove").click();

            }
        })
    }
    //初始化下拉
    var initAutoMatic = function () {
        aStore = $("#aStore").myAutomatic({  //加上名字可以存入自动填充
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $("#hidStore").val($("#aStore").attr("k"));
            }
        });

        $("#storeRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(aStore);
        });


        warehouseCd = $("#warehouseCd").myAutomatic({
            url: systemPath + "/ma5321/getList",
            ePageSize: 10,
            startCount: 0
        });
        a_orderId = $("#org_order_id").myAutomatic({
            url: url_left + "/getOrgOrderIdList",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
                $(".zgGrid-tbody").empty();
                m.store_cd.val("");
                m.store_name.val("");
                m.dc_id.val("");
                m.dc_name_in.val("");
                gridTotal();
            },
            selectEleClick: function (thisObj) {
                $(".zgGrid-tbody").empty();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $.myAjaxs({
                        url: url_left + "/getOrderInfo",
                        async: true,
                        cache: false,
                        data: {
                            orgOrderId: thisObj.attr("k"),
                            returnDiff: "1"
                        },
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                var data = result.data;
                                m.store_cd.val(data.storeCd);
                                if (data.storeName == null) {
                                    data.storeName = "";
                                }
                                m.store_name.val(data.storeCd + " " + data.storeName);
                                m.dc_id.val(data.dcId);
                                if (data.dcName == null) {
                                    data.dcName = "";
                                }
                                m.dc_name_in.val(data.dcId + " " + data.dcName);
                                auditNum = data.auditNum;
                            } else {
                                m.store_cd.val('');
                                m.store_name.val('');
                                m.dc_id.val('');
                                m.dc_name_in.val('');
                                _common.prompt(result.message, 3, "error");
                            }
                        },
                        complete: _common.myAjaxComplete
                    });
                }
            }
        });
        var str = "&orderDifferentiate=1";
        $.myAutomatic.replaceParam(a_orderId, str);

        //商品
        a_item = $("#item_input").myAutomatic({
            url: url_left + "/getItems",
            ePageSize: 10,
            startCount:0,
            param: [{
                'k': 'returnType',
                'v': 'reType'
            }, {
                'k': 'storeCd',
                'v': 'hidStore'
            }],
            cleanInput: function (thisObj) {
                itemClear();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    if ($("#org_order_id").attr("k") != null && $("#org_order_id").attr("k") != '') {
                        getItemInfo(thisObj.attr("k"), $("#org_order_id").attr("k"));
                        // 直接退货
                    } else if (returnType = '10' && ($("#org_order_id").attr("k") == null || $("#org_order_id").attr("k") == '')) {
                        getItemInfo(thisObj.attr("k"), null);
                    }
                } else {
                    itemClear();
                }
            }
        });
        a_item_direct = $("#a_item_direct").myAutomatic({
            url: url_left + "/getItems",
            ePageSize: 10,
            startCount:3,
            param: [{
                'k': 'returnType',
                'v': 'reType'
            }, {
                'k': 'storeCd',
                'v': 'hidStore'
            }],
            cleanInput: function (thisObj) {
                itemClear();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    if ($("#org_order_id").attr("k") != null && $("#org_order_id").attr("k") != '') {
                        getItemInfo(thisObj.attr("k"), $("#org_order_id").attr("k"));
                        // 直接退货
                    } else if (returnType = '10' && ($("#org_order_id").attr("k") == null || $("#org_order_id").attr("k") == '')) {
                        getItemInfo(thisObj.attr("k"), null);
                    }
                } else {
                    itemClear();
                }
            }
        });
    }
    var getItemInfo = function (articleId, orgOrderId) {
        $.myAjaxs({
            url: url_left + "/getItemInfo",
            async: true,
            cache: false,
            type: "get",
            data: {
                returnType: m.reType.val(),
                orderId: orgOrderId,
                orderDate: subfmtDate(m.order_date.val()),
                articleId: articleId,
                storeCd: m.aStore.attr("k"),
            },
            dataType: "json",
            success: function (result) {
                if (result.success && result.flag == "2") {
                    var data = result.data;
                    m.barcode.val(data.barcode);
                    m.itemName.val(data.articleName);
                    m.unitName.val(data.unitName);
                    m.unitId.val(data.unitId);
                    m.spec.val(data.spec);
                    m.orderPrice.val(data.orderPrice);
                    m.orgOrderQty.val(data.orderQty);//原订单量
                    m.orgOrderNochargeQty.val(data.orderNochargeQty);//原订单搭赠量
                    m.receiveQty.val(toThousands(data.receiveQty));//收货数量
                    m.returnedQty.val(toThousands(data.returnedQty));//已退货数量
                    m.vendorId.val(data.vendorId);
                    m.realtimeQty.val(data.realtimeQty);//实时在库量
                    m.receiveNochargeQty.val(data.receiveNochargeQty);
                } else if (result.success && result.flag == "1") {
                    var data = result.data;
                    m.barcode.val(data.barcode);
                    m.itemName.val(data.articleName);
                    m.unitName.val(data.unitName);
                    m.unitId.val(data.unitId);
                    m.spec.val(data.spec);
                    m.receiveQty.val(toThousands(data.onHandQty));
                } else {
                    _common.prompt(result.message, 3, "error");
                    itemClear();
                }
            },
            error: function (e) {
                _common.prompt("Failed to load item data!", 5, "error"); // 商品数据加载失败
                itemClear();
            },
            complete: _common.myAjaxComplete
        });
    }

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };

    var initOrderData = function () {

        //设置订单号
        m.order_id.val(m.orderId.val());
        //Ajax请求头档事件
        $.myAjaxs({
            url: url_left + "/getOrderDetails",
            async: true,
            cache: false,
            type: "get",
            data: {
                orderId: m.orderId.val()
            },
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    $.myAutomatic.setValueTemp(a_orderId, result.data.orgOrderId, result.data.orgOrderId);
                    m.order_date.val(fmtStringDate(result.data.orderDate));
                    m.store_cd.val(result.data.storeCd);
                    m.hidStore.val(result.data.storeCd);
                    m.store_name.val(result.data.storeCd + " " + result.data.storeName);
                    m.dc_id.val(isEmpty(result.data.deliveryCenterId));
                    m.dc_name.val(result.data.deliveryCenterId + " " + isEmpty(result.data.deliveryCenterName));
                    m.dc_name_in.val(result.data.deliveryCenterId + " " + isEmpty(result.data.deliveryCenterName));
                    m.create_date.val(_common.formatCreateDate(result.data.createDate));
                    m.update_ymd.val(_common.formatCreateDate(result.data.updateDate));
                    m.create_user.val(result.data.createBy);
                    m.update_user.val(result.data.modifiedBy);
                    $.myAutomatic.setValueTemp(aStore, result.data.storeCd, result.data.storeName);
                    if(result.data.deliveryCenterId !== "" && result.data.deliveryCenterId != null) {
                        $.myAutomatic.setValueTemp(warehouseCd, result.data.deliveryCenterId, result.data.deliveryCenterName);
                    }else {
                        $.myAutomatic.setValueTemp(warehouseCd, '', ' ');
                    }
                      // if (result.data.returnType != "" || result.data.returnType != null) {
                      // m.reType.val(result.data.returnType);
                        if (result.data.returnType == "10") {
                            m.reType.val(result.data.returnType);
                            //$('#reType').find("option:first-child").prop("selected", true);
                            $("#a_item").hide();
                            $("#a_item_direct").show();
                            $("#indirectReturn").hide();
                            $("#directDC").show();
                            // m.directDC.show();
                            // $("#returnedQtyhi").hide();
                            // m.indirectReturn.hide();
                        } else if (result.data.returnType == "20") {
                            m.reType.val(result.data.returnType);
                            // $('#reType').find("option:last-child").prop("selected", true);
                            $("#a_item").show();
                            $("#a_item_direct").hide();
                            $("#indirectReturn").show();
                            $("#directDC").hide();
                            // m.indirectReturn.show();
                            // $("#returnedQtyhi").show();
                            // m.directDC.hide();
                        }
                    // }


                    //8/17
                    m.order_remark.val(result.data.orderRemark);
                    m.order_remark_in.val(result.data.orderRemark);
                    //加载商品信息
                    paramGrid = "orderId=" + m.orderId.val();
                    tableGrid.setting("url", url_left + "/getItemDetailByOrderId");
                    tableGrid.setting("param", paramGrid);
                    tableGrid.setting("page", 1);
                    tableGrid.loadData(null);

                    attachmentsParamGrid = "recordCd=" + m.orderId.val() + "&fileType=02";//附件信息
                    attachmentsTable.setting("url", systemPath + "/file/getFileList");//加载附件
                    attachmentsTable.setting("param", attachmentsParamGrid);
                    attachmentsTable.loadData(null);
                } else {
                    _common.prompt(result.message, 5, "error"); // 退货单数据加载失败
                }
            },
            complete: _common.myAjaxComplete
        });
    }


    var initPageByType = function () {
        //判断Add还是View或Modify
        if (m.flag.val() == '2') { //Add
            m.order_date.val(fmtStringDate($("#orderDate").val()));
            disableNotModify();
            m.reType.prop("disabled", false);

            //禁用审核按钮
            m.approvalBut.prop("disabled", true);
        }
        if (m.flag.val() == '1') {//Modify

            m.org_order_id.val();
            initOrderData();
            disableNotModify();
            //禁用原订单编号选择
            m.org_order_id.prop('disabled', true);
            $("#org_order_id_refresh").hide();
            $("#org_order_id_clear").hide();
            //禁用审核按钮
            m.reType.prop("disabled", true);
            m.approvalBut.prop("disabled", true);
        }
        if (m.flag.val() == '0') {//View
            initOrderData();
            disableAll();
            $("#org_order_id").attr("disabled", false);
            $("#directReturn").hide();
            $("#indirectReturn").show();
            $("#directDC").hide();
            $("#regionRemove").click();
            //检查是否允许审核
            _common.checkRole(m.orderId.val(), m.typeId.val(), function (success) {
                if (success) {
                    m.approvalBut.prop("disabled", false);
                } else {
                    m.approvalBut.prop("disabled", true);
                }
            });
        }
    }

    //禁用不可修改栏位
    var disableNotModify = function () {
        if (m.reType.val()==="20"){
            $("#aStore").attr("disabled", true);
            $("#warehouseCd").attr("disabled", true);
        }else if(m.reType.val()==="10"){
            $("#aStore").attr("disabled", false);
            $("#warehouseCd").attr("disabled", false);
        }
        m.order_id.prop('disabled', true);
        m.order_date.prop('disabled', true);
        m.total_qty_in.prop('disabled', true);
        m.total_amt.prop('disabled', true);
        m.store_cd.prop('disabled', true);
        m.store_name.prop('disabled', true);
        m.dc_id.prop('disabled', true);
        m.dc_name.prop('disabled', true);
        m.update_ymd.prop('disabled', true);
        m.update_user.prop('disabled', true);
        m.create_user.prop('disabled', true);
        m.create_date.prop('disabled', true);

    }

    //禁用弹窗不可修改栏位
    var disableDialog = function () {
        m.barcode.prop('disabled', true);
        m.itemCode.prop('disabled', true);
        m.unitName.prop('disabled', true);
        m.spec.prop('disabled', true);
        m.orderPrice.prop('disabled', true);
        m.orderAmount.prop('disabled', true);
    }

    //View时禁用所有栏位
    var disableAll = function () {
        m.warehouseCd.prop("disabled", true);
        m.order_id.prop('disabled', true);
        m.order_date.prop('disabled', true);
        m.total_qty.prop('disabled', true);
        m.total_qty_in.prop('disabled', true);
        m.total_amt.prop('disabled', true);
        m.store_cd.prop('disabled', true);
        m.store_name.prop('disabled', true);
        m.dc_id.prop('disabled', true);
        m.dc_name.prop('disabled', true);
        m.update_ymd.prop('disabled', true);
        //后面追加
        m.update_user.prop('disabled', true);
        m.create_user.prop('disabled', true);
        m.create_date.prop('disabled', true);
        m.order_remark.prop('disabled', true);
        m.order_remark_in.prop('disabled', true);
        m.returnsSubmitBut.prop('disabled', true);
        $("#add").prop('disabled', true);
        $("#modify").prop('disabled', true);
        $("#delete").prop('disabled', true);
        m.org_order_id.prop('disabled', true);
        $("#org_order_id_refresh").hide();
        $("#org_order_id_clear").hide();
        $("#reType").prop("disabled", true);
        $("#aRegion").prop("disabled", true);
        $("#aCity").prop("disabled", true);
        $("#aDistrict").prop("disabled", true);
        $("#aStore").prop("disabled", true);
        $("#storeRefresh").hide();
        $("#storeRemove").hide();
        $("#vendorIdRefresh").hide();
        $("#vendorIdRemove").hide();
        //附件按钮
        $("#addByFile").prop("disabled", true);
        $("#updateByFile").prop("disabled", true);
        $("#deleteByFile").prop("disabled", true);
        $("#order_remark_in").prop("disabled", true);

        $("#storeRefresh").prop("disabled",true);
        $("#storeRemove").prop("disabled",true);
        $("#vendorIdRemove").prop("disabled",true);
        $("#vendorIdRefresh").prop("disabled",true);
    }
    //表格按钮事件
    var table_event = function () {

        //禁用商品信息框
        disableDialog();
        $("#add").on("click", function () {
            resertValue();
            if (m.reType.val() == "10") {
                switchOver('0');
            }
            //根据原订单退货
            if (m.reType.val() == "20") {
                switchOver('1');
            }

            var orgFlag = true;
            // 直接退货
            if (orgFlag && $("#reType").val() === '10') {
                if (m.aStore.attr('k') === "" || m.aStore.attr('k') == null) {
                    _common.prompt("please select a store !");
                    m.aStore.css("border-color","red");
                    m.aStore.focus();
                    return false;
                }else {
                    m.aStore.css("border-color","#CCC");
                }
                if ($("#warehouseCd").attr("k") === "" || $("#warehouseCd").attr("k") == null) {
                    _common.prompt("please select a supplier !");
                    $("#warehouseCd").css("border-color","red");
                    $("#warehouseCd").focus();
                    return false;
                }else {
                    $("#warehouseCd").css("border-color","#CCC");
                }

                var str = "&orderDifferentiate=1&orderDate=" + subfmtDate(m.order_date.val());
                // $.myAutomatic.replaceParam(a_item, str);
                $.myAutomatic.replaceParam(a_item_direct, str);
                // 原订单号退货
            } else if (!orgFlag && $("#reType").val() === '20') {
                var str = "&orderId=" + $("#org_order_id").attr("k") + "&orderDifferentiate=0&orderDate=" + subfmtDate(m.order_date.val());
                $.myAutomatic.replaceParam(a_item, str);
                disableDialog();
                //清空商品信息
                itemClear();
                //店铺不能为空 或店铺失效
                judgement();
                //该订单还有审核中或未确认退货
                if (auditNum > 0) {
                    _common.prompt("This order is still under review or not confirmed return!", 3, "info");
                    m.org_order_id.focus();
                    return false;
                }
                // m.dialog_flg.val("add");
                // m.update_dialog.modal("show");
            } else if (m.reType.val() == "20" && orgFlag == true) {
                var str = "&orderId=" + $("#org_order_id").attr("k") + "&orderDifferentiate=0&orderDate=" + subfmtDate(m.order_date.val());
                $.myAutomatic.replaceParam(a_item, str);
                disableDialog();
                //清空商品信息
                itemClear();
                //店铺不能为空
                judgement();
                //该订单还有审核中或未确认退货
                if (auditNum > 0) {
                    _common.prompt("This order is still under review or not confirmed return!", 3, "info");
                    m.org_order_id.focus();
                    return false;
                }
            }
            m.dialog_flg.val("add");
            m.update_dialog.modal("show");
        });

        $("#modify").on("click", function () {
            // 根据原定单修改
            if (m.reType.val() === "20") {
                switchOver('1');

                var str = "&orderId=" + $("#org_order_id").attr("k") + "&orderDifferentiate=0&orderDate=" + subfmtDate(m.order_date.val());
                $.myAutomatic.replaceParam(a_item_direct, str);

                if (selectTrTemp == null) {
                    _common.prompt("Please select at least one row of data!", 5, "error");
                    return false;
                }
                //店铺不能为空
                judgement();
                //该订单还有审核中或未确认退货
                if (auditNum > 0) {
                    _common.prompt("This order is still under review or not confirmed return!", 3, "info");
                    m.org_order_id.focus();
                    return false;
                }
            } else if (m.reType.val() == "10") {
                switchOver('0');
                if (selectTrTemp == null) {
                    _common.prompt("Please select at least one row of data!", 5, "error");
                    return false;
                }
            }

            // if (m.reType.val() == "20") {
            //     var str = "&orderId=" + $("#org_order_id").attr("k") + "&orderDifferentiate=1&orderDate=" + subfmtDate(m.order_date.val());
            //     $.myAutomatic.replaceParam(a_item, str);
            //     if (selectTrTemp == null) {
            //         _common.prompt("Please select at least one row of data!", 5, "error");
            //         return false;
            //     }
            //     //店铺名不能为空
            //     var storeName = m.store_name.val();
            //     if (storeName == null || storeName.trim() === "") {
            //         _common.prompt("Please enter the correct Store No.!", 3, "info");
            //         m.org_order_id.focus();
            //         m.org_order_id.css("border-color", "red");
            //         return false;
            //     }else if(storeName.trim() === m.store_cd.val()){
            //         _common.prompt("The store is no longer valid!", 3, "info");
            //         m.org_order_id.focus();
            //         m.org_order_id.css("border-color", "red");
            //         return false;
            //     }else {
            //         m.org_order_id.css("border-color", "#CCC");
            //     }
            //     //仓库失效为空
            //     //var dcName = m.dc_name.val();
            //     var dcName = m.dc_name_in.val();
            //
            //     if (dcName == null || dcName.trim() == "") {
            //         _common.prompt("Please enter the correct DC No.!", 3, "info");
            //         m.org_order_id.focus();
            //         m.org_order_id.css("border-color", "red");
            //         return false;
            //     }else if(dcName.trim() == m.dc_id.val()){
            //         _common.prompt("The DC is no longer valid!", 3, "info");
            //         m.org_order_id.focus();
            //         m.org_order_id.css("border-color", "red");
            //         return false;
            //     }else {
            //         m.org_order_id.css("border-color", "#CCC");
            //     }
            //     //该订单还有审核中或未确认退货
            //     if (auditNum > 0) {
            //         _common.prompt("This order is still under review or not confirmed return!", 3, "info");
            //         m.org_order_id.focus();
            //         return false;
            //     }
            // } else if (m.reType.val() == "10") {
            //     $("#returnedQty").hide();
            //     $("#itemDivOrg").hide();
            //     $("#itemDivDirect").show();
            //     $("#a_role_clear").show();
            //     $("#item_input").hide();
            //     if (selectTrTemp == null) {
            //         _common.prompt("Please select at least one row of data!", 5, "error");
            //         return false;
            //     }
            // }
            itemClear();
            //弹出框赋值
            dialogSetData();
            m.dialog_flg.val("update");
            m.update_dialog.modal("show");


        });

        $("#delete").on("click", function () {
            var _list = tableGrid.getCheckboxTrs();
            if (_list == null || _list.length == 0) {
                if (selectTrTemp == null) {
                    _common.prompt("Please select at least one row of data!", 5, "error");
                    return false;
                } else {
                    _common.myConfirm("Please confirm whether you want to delete the selected item？", function (result) {
                        if (result == "true") {
                            $(selectTrTemp).remove();
                            selectTrTemp = null;
                            gridTotal();
                            _common.prompt("Data deleted successfully！", 2, "success"); // 商品删除成功
                        }
                    })
                }
            } else {
                _common.myConfirm("Please confirm whether you want to delete the selected item？", function (result) {
                    if (result == "true") {
                        for (var i = 0; i < _list.length; i++) {
                            $(_list[i].selector).remove();
                        }
                        selectTrTemp = null;
                        gridTotal();
                        _common.prompt("Data deleted successfully！", 2, "success"); // 商品删除成功
                    }
                })
            }
        });
        //返回一览
        m.returnsViewBut.on("click", function () {
            var bank = $("#returnsSubmitBut").attr("disabled");
            if (submitFlag === false && bank != 'disabled') {
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?", function (result) {
                    if (result === "true") {
                        _common.updateBackFlg(KEY);
                        top.location = url_left;
                    }
                });
            }
            if (submitFlag === true) {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
            if (submitFlag === false && bank === 'disabled') {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
        });

        //更新退供应商头档信息
        m.returnsSubmitBut.on("click", function () {
            var orgOrderId = m.org_order_id.val();
            if ((orgOrderId == null || orgOrderId == "") && m.reType.val() == "20") {
                _common.prompt("Please select Source Document No.!", 5, "error"); // 请原单据号
                m.org_order_id.focus();
                return false;
            }

            var storeCd = m.store_cd.val();
            if (storeCd == null || storeCd == "") {
                if (m.reType.val() != '10') {
                    _common.prompt("Store can not be empty!", 5, "error"); // 店铺不能为空
                    m.store_cd.focus();
                    return false;
                }
            }


            var itemDetail = [], itemStatus = true;
            if (m.reType.val() == "10") {
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var orderItem = {
                        orderId: $("#order_id").val(),
                        storeCd: m.aStore.attr('k'),
                        articleId: $(this).find('td[tag=articleId]').text(),
                        barcode: $(this).find('td[tag=barcode]').text(),
                        orderUnit: $(this).find('td[tag=unitId]').text(),//订货单位
                        orderQty: reThousands($(this).find('td[tag=orderQty]').text()),//订购数量
                        // orderNochargeQty: reThousands($(this).find('td[tag=orderNochargeQty]').text()),//搭赠数量
                        orderNochargeQty: 0,//搭赠数量为 0
                        orderPrice: 0,//订货单价
                        orderAmtNotax: reThousands($(this).find('td[tag=orderAmount]').text()),//退货价格（不含税）
                        // orderAmt: reThousands($(this).find('td[tag=orderAmount]').text()),//退货价格（含税）
                        reasonId: $(this).find('td[tag=reasonId]').text(),//退货原因
                        returnType: m.reType.val(),
                    }
                    if (orderItem.orderQty == "" || orderItem.orderQty == 0) {
                        itemStatus = false;
                    }
                    itemDetail.push(orderItem);
                });
            } else {
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var orderItem = {
                        orderId: $("#order_id").val(),
                        storeCd: m.store_cd.val(),
                        articleId: $(this).find('td[tag=articleId]').text(),
                        barcode: $(this).find('td[tag=barcode]').text(),
                        orderUnit: $(this).find('td[tag=unitId]').text(),//订货单位
                        orderQty: reThousands($(this).find('td[tag=orderQty]').text()),//订购数量
                        // orderNochargeQty: reThousands($(this).find('td[tag=orderNochargeQty]').text()),//搭赠数量
                        orderNochargeQty: 0,//搭赠数量为 0
                        orderPrice: reThousands($(this).find('td[tag=orderPrice]').text()),//订货单价
                        orderAmtNotax: reThousands($(this).find('td[tag=orderAmount]').text()),//退货价格（不含税）
                        // orderAmt: reThousands($(this).find('td[tag=orderAmount]').text()),//退货价格（含税）
                        reasonId: $(this).find('td[tag=reasonId]').text(),//退货原因
                        returnType: m.reType.val(),
                    }
                    if (orderItem.orderQty == "" || orderItem.orderQty == 0) {
                        itemStatus = false;
                    }
                    itemDetail.push(orderItem);
                });
            }

            //	var itemDetail = [],itemStatus = true;
            //8/17

            if (!itemStatus) {//退货数量不能为空
                _common.prompt("Returning Quantity can not be empty!", 5, "info");
                return false;
            }

            var orderDetail = "";
            if (itemDetail.length > 0) {//退货商品
                orderDetail = JSON.stringify(itemDetail)
            } else {
                _common.prompt("Return Item can not be empty!", 5, "info");
                return false;
            }

            //附件信息
            var fileDetail = [], fileDetailJson = "";
            $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                var file = {
                    informCd: m.order_id.val(),
                    fileType: '02',//文件类型 - 退货
                    fileName: $(this).find('td[tag=fileName]').text(),//文件名称
                    filePath: $(this).find('td>a').attr("filepath"),//文件路径
                }
                fileDetail.push(file);
            });
            if (fileDetail.length > 0) {
                fileDetailJson = JSON.stringify(fileDetail)
            }
            if (m.reType.val() == "10") {
                orderRemarkflg = m.order_remark[0].value;
            } else if (m.reType.val() == "20") {
                orderRemarkflg = m.order_remark_in[0].value;
            }

            var returnOrder = {
                storeCd: m.store_cd.val(),
                storeCd1: $("#aStore").attr('k'),
                orderDate: subfmtDate(m.order_date.val()),//退货日期
                orderId: m.order_id.val(),
                orgOrderId: $("#org_order_id").attr("k"),
                vendorId: m.dc_id.val(),
                vendorId1: $("#warehouseCd").attr('k'),
                deliveryTypeCd: "000",  //配送类型CD 无关紧要
                deliveryCenterId: m.dc_id.val(),//物流中心Id 无关紧要
                deliveryCenterId1: $("#warehouseCd").attr('k'),//物流中心Id 无关紧要
                deliveryAreaCd: "000000",  //配送区域CD 无关紧要
                orderRemark: orderRemarkflg,
                orderAmt: reThousands(m.total_amt.val()),//总退货价格（页面暂不含税）
                orderDetailJson: orderDetail,
                fileDetailJson: fileDetailJson,//附件
                use: m.flag.val(),
                toKen: m.toKen.val(),
                returnType: $("#reType").val(),
            }
            _common.myConfirm("Are you sure you want to save？", function (result) {

                if (result != "true") {
                    return false;
                }
                $.myAjaxs({
                    url: url_left + "/save",
                    async: true,
                    cache: false,
                    type: "post",
                    data: returnOrder,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            if (m.flag.val() == "1") {
                                m.update_ymd.val(_common.formatCreateDate(result.data.updateYmd + result.data.updateHms));//修改时间
                                m.update_user.val(result.data.userName);//用户
                            } else if (m.flag.val() == "2") {
                                m.order_id.val(result.data.orderId);
                                m.create_date.val(_common.formatCreateDate(result.data.createYmd + result.data.createHms));//创建时间
                                m.create_user.val(result.data.userName);//用户
                                m.flag.val("0");//变为查看模式
                            }
                            m.toKen.val(result.toKen);
                            _common.prompt("Submitted successfully！", 2, "success", function () {
                                //发起审核
                                var typeId = m.typeId.val();
                                var nReviewid = m.reviewId.val();
                                var recordCd = m.order_id.val();
                                var _storeCd = m.store_cd.val();
                                if ($("#reType").val() == "10") {
                                    _storeCd = $("#aStore").attr('k');
                                }
                                _common.initiateAudit(_storeCd, recordCd, typeId, nReviewid, m.toKen.val(), function (token) {
                                    disableAll();
                                    //审核按钮禁用
                                    m.approvalBut.prop("disabled", true);
                                    m.toKen.val(token);
                                })
                            }, true);
                        } else {
                            _common.prompt(result.message, 5, "error");
                        }
                    },
                    complete: _common.myAjaxComplete
                });
            })
        });

        m.submit.on("click", function () {
            if (m.reType.val()=="20") {
                let temp = $("#item_input").attr('k');
                if (temp == null || $.trim(temp) == "") {
                    _common.prompt("The item cannot be empty!", 3, "error");
                    $("#item_input").css("border-color", "red");
                    $("#item_input").focus();
                    return false;
                } else {
                    $("#item_input").css("border-color", "#CCCCCC");
                }
            }
            if (m.reType.val()=="10") {
                let temp = $("#a_item_direct").attr('k');
                if (temp == null || $.trim(temp) == "") {
                    _common.prompt("The item cannot be empty!", 3, "error");
                    $("#a_item_direct").css("border-color", "red");
                    $("#a_item_direct").focus();
                    return false;
                } else {
                    $("#a_item_direct").css("border-color", "#CCCCCC");
                }
            }
            //计算商品退货金额
            var orderQty = reThousands(m.orderQty.val());
            var price = reThousands(m.orderPrice.val());
            if (!!orderQty) {
                m.orderAmount.val(parseFloat(orderQty * price).toFixed(2));//保留两位小数
            }
            //20201027
            if (m.reType.val() == "10") {
                var articleId = $("#a_item_direct").attr("k");
                if (articleId == null || articleId == "") {
                    $("#a_item_direct").css("border-color", "red");
                    _common.prompt("Please select an item!", 5, "info"); // 请选择商品
                    $("#a_item_direct").focus();
                    return false;
                } else {
                    $("#a_item_direct").css("border-color", "#CCCCCC");
                }
            }


            if (m.reType.val() == "20") {
                var articleId = $("#item_input").attr("k");
                if (articleId == null || articleId == "") {
                    $("#item_input").css("border-color", "red");
                    _common.prompt("Please select an item!", 5, "info"); // 请选择商品
                    $("#item_input").focus();
                    return false;
                } else {
                    $("#item_input").css("border-color", "#CCCCCC");
                }
            }

            var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
            // var orderQty = reThousands(m.orderQty.val().trim());//退货数量
            var orderQty = m.orderQty.val().trim();//退货数量
            if (!reg.test(orderQty) || orderQty.indexOf(",")>1) {
                $("#orderQty").css("border-color", "red");
                _common.prompt("Please enter with correct data type!", 3, "info");
                m.orderQty.focus();
                return false;
            } else {
                $("#orderQty").css("border-color", "#CCCCCC");
            }
            var _orderQty = parseFloat(orderQty);//退货量
            //8/17
            if (_orderQty == 0) {//退货量不为空
                $("#orderQty").css("border-color", "red");
                _common.prompt("Returning Quantity can not be empty!", 3, "info");
                m.orderQty.focus();
                return false;
            } else {
                $("#orderQty").css("border-color", "#CCCCCC");
            }

            // var receiveQty = parseFloat(reThousands(m.receiveQty.val()));//收货数量
            // if (!isNaN(receiveQty)) {
            //     if (_orderQty > receiveQty) {//不得高于收货数量
            //         $("#orderQty").css("border-color", "red");
            //         _common.prompt("Return Qty can not be greater than Received Qty!", 3, "info");
            //         m.orderQty.focus();
            //         return false;
            //     } else {
            //         $("#orderQty").css("border-color", "#CCCCCC");
            //     }
            // } else {
            //     _common.prompt("There is an error in quantity type Received Qty!", 5, "error");
            //     return false;
            // }


            var returnedQty = parseFloat(reThousands(m.returnedQty.val()));//已退货数量
            if (!isNaN(returnedQty)) {
                let rtQty = receiveQty - returnedQty;
                if (_orderQty > rtQty) {//不得高于可退货数量
                    $("#orderQty").css("border-color", "red");
                    _common.prompt("Return Qty can not be greater than " + rtQty + "!", 3, "info");
                    m.orderQty.focus();
                    return false;
                } else {
                    $("#orderQty").css("border-color", "#CCCCCC");
                }
            }

            var receiveNochargeQty = reThousands(m.receiveNochargeQty.val());
            var orderNochargeQty = reThousands(m.orderNochargeQty.val());
            tempTrOrderNochargeQtynew = orderNochargeQty;
            if (!isNaN(orderNochargeQty)) {
                if (parseInt(orderNochargeQty) > parseInt(receiveNochargeQty)) {//不得高于实际退搭赠量数量
                    $("#orderNochargeQty").css("border-color", "red");
                    _common.prompt("Return Free Qty can not be greater than Received Free Qty!", 3, "info");
                    m.orderNochargeQty.focus();
                    return false;
                } else {
                    $("#orderNochargeQty").css("border-color", "#CCCCCC");
                }
            }
            var reasonId = m.reasonId.val();
            if (reasonId == null || reasonId == '') {
                $("#reasonId").css("border-color", "red");
                _common.prompt("The returning reason cannot be empty!", 5, "error");
                m.reasonId.focus();//聚焦退货量
                return false;
            } else {
                $("#reasonId").css("border-color", "#CCCCCC");
            }

            var flg = m.dialog_flg.val();
            var appendFlg = true;
            if (flg == 'add') {
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    let _articleId = $(this).find('td[tag=articleId]').text();
                    if (_articleId == $("#item_input").attr("k")) {
                        appendFlg = false;
                    }
                });
            } else if (flg == 'update') {
                if (selectTrTemp == null) {
                    $("#item_input").css("border-color", "red");
                    _common.prompt("Please select an item!", 5, "info"); // 选择的商品为空
                    return false;
                } else {
                    var cols = tableGrid.getSelectColValue(selectTrTemp, "articleId");
                    $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                        var _articleId = $(this).find('td[tag=articleId]').text();
                        if (articleId != cols["articleId"]) {//选择商品id不等于新录入商品id
                            if (_articleId == articleId) {//商品已存在
                                appendFlg = false;
                                return false;
                            }
                        }
                    })
                    $("#item_input").css("border-color", "#CCCCCC");
                }
            }
            if (!appendFlg) {
                $("#item_input").css("border-color", "red");
                _common.prompt("Selected item already exists in order list,please select a new item!", 5, "info"); // 该商品已经存在
                return false;
            } else {
                $("#item_input").css("border-color", "#CCCCCC");
            }
            _common.myConfirm("Are you sure to Submit?", function (result) {
                if (result == "true") {
                    if (flg == 'add') {
                        var rowindex = 0;
                        var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
                        if (trId != null && trId != '') {
                            rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
                        }
                        var tr = '<tr id="zgGridTtable_' + rowindex + '_tr" class="">' +
                            '<td tag="ckline" align="center" style="color:#428bca;"  id="zgGridTtable_' + rowindex + '_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_' + rowindex + '_tr" name="zgGridTtable"> </td>' +
                            '<td tag="barcode" width="100" title="' + m.barcode.val() + '" align="right" id="zgGridTtable_' + rowindex + '_tr_barcode" tdindex="zgGridTtable_barcode">' + m.barcode.val() + '</td>' +
                            '<td tag="articleId" width="100" title="' + articleId + '" align="right" id="zgGridTtable_' + rowindex + '_tr_articleId" tdindex="zgGridTtable_articleId">' + articleId + '</td>' +
                            '<td tag="articleName" width="150" title="' + m.itemName.val() + '" align="left" id="zgGridTtable_' + rowindex + '_tr_articleName" tdindex="zgGridTtable_articleName">' + m.itemName.val() + '</td>' +
                            '<td tag="unitId" hidden width="80" title="' + m.unitId.val() + '" align="left" id="zgGridTtable_' + rowindex + '_tr_unitId" tdindex="zgGridTtable_unitId">' + m.unitId.val() + '</td>' +
                            '<td tag="vendorId" hidden width="80" align="left" id="zgGridTtable_' + rowindex + '_tr_vendorId" tdindex="zgGridTtable_vendorId">1</td>' +
                            '<td tag="orderPrice" hidden width="80" title="' + m.orderPrice.val() + '" align="right" id="zgGridTtable_' + rowindex + '_tr_orderPrice" tdindex="zgGridTtable_orderPrice">' + m.orderPrice.val() + '</td>' +
                            '<td tag="unitName" width="50" title="' + m.unitName.val() + '" align="left" id="zgGridTtable_' + rowindex + '_tr_unitName" tdindex="zgGridTtable_unitName">' + m.unitName.val() + '</td>' +
                            '<td tag="spec" width="100" title="' + m.spec.val() + '" align="left" id="zgGridTtable_' + rowindex + '_tr_spec" tdindex="zgGridTtable_spec">' + m.spec.val() + '</td>' +
                            '<td tag="orderQty" width="130" title="' + m.orderQty.val() + '" align="right" id="zgGridTtable_' + rowindex + '_tr_orderQty" tdindex="zgGridTtable_orderQty">' + toThousands(m.orderQty.val()) + '</td>' +
                            // '<td tag="orderNochargeQty" width="130" title="'+m.orderNochargeQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderNochargeQty" tdindex="zgGridTtable_orderNochargeQty">'+toThousands(m.orderNochargeQty.val())+'</td>' +
                            '<td tag="orderAmount" hidden width="130" title="' + m.orderAmount.val() + '" align="right" id="zgGridTtable_' + rowindex + '_tr_orderAmount" tdindex="zgGridTtable_orderAmount">' + m.orderAmount.val() + '</td>' +
                            '<td tag="reasonId" hidden width="80" title="' + m.reasonId.val() + '" align="right" id="zgGridTtable_' + rowindex + '_tr_reasonId" tdindex="zgGridTtable_reasonId">' + m.reasonId.val() + '</td>' +
                            '<td tag="reasonName" width="180" title="' + m.reasonId.find("option:selected").text() + '" align="left" id="zgGridTtable_' + rowindex + '_tr_reasonName" tdindex="zgGridTtable_reasonName">' + m.reasonId.find("option:selected").text() + '</td>' +
                            //	'<td tag="reType" width="200" title="'+m.reType.find("option:selected").text()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_reasonName" tdindex="zgGridTtable_reasonName">'+m.reType.find("option:selected").text()+'</td>' +
                            '</tr>';
                        tableGrid.append(tr)
                        _common.prompt("Data added successfully！", 2, "success"); // 商品添加成功
                    } else if (flg == 'update') {
                        if (m.reType.val() == "10") {
                            articleId = $("#a_item_direct").attr("k")
                        } else {
                            articleId = $("#a_item").attr("k")
                        }
                        $(selectTrTemp).find('td[tag=barcode]').text(m.barcode.val());
                        $(selectTrTemp).find('td[tag=articleId]').text(articleId);
                        $(selectTrTemp).find('td[tag=articleName]').text(m.itemName.val());
                        $(selectTrTemp).find('td[tag=unitId]').text(m.unitId.val());
                        $(selectTrTemp).find('td[tag=vendorId]').text(m.vendorId.val());
                        $(selectTrTemp).find('td[tag=unitName]').text(m.unitName.val());
                        $(selectTrTemp).find('td[tag=spec]').text(m.spec.val());
                        $(selectTrTemp).find('td[tag=orderPrice]').text(m.orderPrice.val());
                        $(selectTrTemp).find('td[tag=orderQty]').text(m.orderQty.val());
                        // $(selectTrTemp).find('td[tag=orderNochargeQty]').text(m.orderNochargeQty.val());
                        var orderAmount = reThousands(m.orderAmount.val());
                        $(selectTrTemp).find('td[tag=orderAmount]').text(toThousands(orderAmount));
                        _common.prompt("Data updated successfully！", 2, "success"); // 商品修改成功
                    }
                    //计算合计
                    gridTotal();
                    $('#update_dialog').modal("hide");
                }
            });
        });


        //弹出窗 确认取消按钮
        m.close.on("click", function () {
            _common.myConfirm("Are you sure you want to cancel?", function (result) {
                if (result == "true") {
                    var a_ = $("#item_input").val();
                    $("#item_input").css("border-color", "#CCCCCC");
                    var orderQty = $("#orderQty").val();
                    $("#orderQty").css("border-color", "#CCCCCC");
                    $("#orderNochargeQty").css("border-color", "#CCCCCC");
                    var reasonId = $("#reasonId").val();
                    $("#reasonId").css("border-color", "#CCCCCC");
                    $("#update_dialog").modal("hide");
                }
            });
        });
    }

    var itemClear = function () {
        $("#item_input").val('').attr("k", '').attr("v", '');
        m.barcode.val('');
        m.unitId.val('');
        m.unitName.val('');
        m.spec.val('');
        m.orderPrice.val('');
        m.orderAmount.val('');
        m.receiveQty.val('');
        m.returnedQty.val('');
        m.orderQty.val('');
        m.reasonId.val('');
        m.orderNochargeQty.val('');
        m.receiveNochargeQty.val('');
    };

    //弹窗赋值
    var dialogSetData = function () {
        if (m.reType.val() == "20") {
            $.myAutomatic.setValueTemp(a_item, tempTrItemCode, tempTrItemCode + " " + tempTrItemName);
        } else {
            $.myAutomatic.setValueTemp(a_item_direct, tempTrItemCode, tempTrItemCode + " " + tempTrItemName);
        }
      //$.myAutomatic.setValueTemp(a_item, tempTrItemCode, tempTrItemCode + " " + tempTrItemName);
        getItemInfo(tempTrItemCode, $("#org_order_id").attr("k"));
        m.barcode.val(tempTrBarcode);
        // m.itemName.val(tempTrItemName);
        // m.unitId.val(tempTrUnitId);
        // m.unitName.val(tempTrUom);
        // m.spec.val(tempTrSpec);
        // m.orderPrice.val(tempTrPrice);
        m.orderAmount.val(tempTrItemAmt);
        m.orderQty.val(reThousands(tempTrOrderQty));
        m.orderNochargeQty.val(tempTrOrderNochargeQty);
        m.reasonId.val(tempTrReasonId);
    }

    //临时存值置空
    var clearTempTr = function () {
        tempTrBarcode = null;// 选中的商品条码
        tempTrItemCode = null;//选中的商品编号
        tempTrItemName = null;//选中的商品名称
        tempTrUnitId = null;//选中的商品单位id
        tempTrUom = null;//选中的商品单位
        tempTrSpec = null;//选中的商品规格
        tempTrOrderQty = null;//选中的退货量
        tempTrPrice = null;//选中的商品单价
        tempTrItemAmt = null;//选中的商品退货总金额
        tempTrReasonId = null;//选中的退货原因
        tempTrReasonName = null;//选中的退货原因
        tempTrOrderNochargeQty = null;//选中的退货原因
    }

    //画面按钮点击事件
    var but_event = function () {
        //盘点日期
        m.order_date.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });
        m.update_ymd.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });
        m.create_date.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });
        m.search.on("click", function () {

        });


        // 2021/3/29
        //退货数量失去焦点计算总退货金额
        // $("#orderQty").blur(function () {
        //     var orderQty = reThousands(m.orderQty.val());
        //     var price = reThousands(m.orderPrice.val());
        //     if (!!orderQty) {
        //         m.orderAmount.val(parseFloat(orderQty * price).toFixed(2));//保留两位小数
        //     }
        //     $("#orderQty").val(toThousands(this.value));
        // });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        // $("#orderQty").focus(function () {
        //     $("#orderQty").val(reThousands(this.value));
        // });

        //退货搭赠数量失去焦点计算总退货金额
        $("#orderNochargeQty").blur(function () {
            $("#orderNochargeQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#orderNochargeQty").focus(function () {
            $("#orderNochargeQty").val(reThousands(this.value));
        });
    }

    var gridTotal = function () {
        var orderPriceTotal = 0;
        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
            var orderTotalAmt = parseFloat(reThousands($(this).find('td[tag=orderAmount]').text()));
            if (!isNaN(orderTotalAmt))
                orderPriceTotal += parseFloat(orderTotalAmt);
        });
        return orderPriceTotal;
    }

    // 请求加载下拉列表
    function getSelectValue() {
        // 加载select
        initSelectOptions("Return Reason", "reasonId", "00400");
        //	initSelectOptions("reType ","reType", "00700");
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
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error: function (e) {
                _common.prompt(title + " Failed to load data!", 5, "error");
            },
            complete: _common.myAjaxComplete
        });
    }

    // 选项选中事件
    var trClick_table1 = function () {
        //选中时先清空临时值
        clearTempTr();
        var cols = tableGrid.getSelectColValue(selectTrTemp, "barcode,articleId,articleName,unitId,unitName,spec,orderQty,orderPrice,orderNochargeQty,orderAmount,reasonId,reasonName");
        tempTrBarcode = cols["barcode"];
        tempTrItemCode = cols["articleId"];
        tempTrItemName = cols["articleName"];
        tempTrUnitId = cols["unitId"];
        tempTrUom = cols["unitName"];
        tempTrSpec = cols["spec"];
        tempTrPrice = cols["orderPrice"];
        tempTrItemAmt = cols["orderAmount"];
        // tempTrOrderNochargeQty = cols["orderNochargeQty"];
        tempTrOrderQty = cols["orderQty"];
        tempTrReasonId = cols["reasonId"];
        tempTrReasonName = cols["reasonName"];
    }

    //表格初始化-退供应商申请单样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Return Detail",
            param: paramGrid,
            colNames: ["Item Barcode", "Item Code", "Item Name", "UOM ID", "Vendor Id", "Price", "UOM", "Spec", "Return Qty", "Return Free Qty", "Amount", "Reason Id", "Reason Name", "Return Type"],
            colModel: [
                {name: "barcode", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "articleId", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {name: "articleName", type: "text", text: "left", width: "150", ishide: false, css: ""},
                {name: "unitId", type: "text", text: "right", width: "80", ishide: true, css: ""},
                {name: "vendorId", type: "text", text: "right", width: "80", ishide: true, css: ""},
                {
                    name: "orderPrice",
                    type: "text",
                    text: "right",
                    width: "80",
                    ishide: true,
                    css: "",
                    getCustomValue: getThousands
                },
                {name: "unitName", type: "text", text: "left", width: "50", ishide: false, css: ""},
                {name: "spec", type: "text", text: "right", width: "100", ishide: false, css: ""},
                {
                    name: "orderQty",
                    type: "text",
                    text: "right",
                    width: "130",
                    ishide: false,
                    css: "",
                    getCustomValue: getThousands
                },
                {
                    name: "orderNochargeQty",
                    type: "text",
                    text: "right",
                    width: "130",
                    ishide: true,
                    css: "",
                    getCustomValue: getThousands
                },
                {
                    name: "orderAmount",
                    type: "text",
                    text: "right",
                    width: "130",
                    ishide: true,
                    css: "",
                    getCustomValue: getThousands
                },
                {name: "reasonId", type: "text", text: "left", width: "80", ishide: true, css: ""},
                {name: "reasonName", type: "text", text: "left", width: "180", ishide: false, css: ""},
                {name: "returnType", type: "text", text: "left", width: "200", ishide: true, css: ""},
            ],//列内容
            width: "max",//宽度自动
            height: 300,
            page: 1,//当前页

            rowPerPage: 10,//每页数据量

            isPage: false,//是否需要分页

            isCheckbox: true,
            freezeHeader:true,

            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },

            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                gridTotal();//合计
                return self;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
                trClick_table1();
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
                },
                {
                    butType: "custom",
                    butHtml: "<button id='attachments' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>"
                },//附件
            ],


        });
    }

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
            window.open(encodeURI(url),"toPreview");
        });
    };

    //审核事件
    var approval_event = function () {
        //点击审核按钮
        m.approvalBut.on("click", function () {
            var recordId = m.order_id.val();
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
                $("#audit_affirm").prop("disabled",true);
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
    var resertValue=function () {
        $("#orderQty").val("");
        $("#a_item_direct").val("");
        $("#item_input").val("");
        $("#itemName").val("");
        $("#barcode").val("");
        $("#vendorId").val("");
        $("#unitName").val("");
        $("#unitId").val("");
        $("#spec").val("");
        $("#orderPrice").val("");
        $("#orderAmount").val("");
        $("#receiveQty").val("");
        $("#returnedQty").val("");
        $("#orgOrderQty").val("");
        $("#orgOrderNochargeQty").val("");
        $("#realtimeQty").val("");
        $("#receiveNochargeQty").val("");
        $("#orderNochargeQty").val("");
        $("#reasonId").val("");
    }
    var gridTotal = function () {
        var orderPriceTotal = 0, totalQty = 0;
        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
            var orderAmount = parseFloat(reThousands($(this).find('td[tag=orderAmount]').text()));
            var orderQty = parseFloat(reThousands($(this).find('td[tag=orderQty]').text()));
            if (!isNaN(orderAmount))
                orderPriceTotal += parseFloat(orderAmount);
            if (!isNaN(orderQty))
                totalQty += parseFloat(orderQty);
        });
        m.total_amt.val(orderPriceTotal);
        m.total_qty.val(toThousands(totalQty));
        m.total_qty_in.val(toThousands(totalQty));
    }

    //小数格式化
    var floatFmt = function (tdObj, value) {
        if (value != null && value.trim() != '') {
            value = parseFloat(value);
        }
        return $(tdObj).text(value);
    }
    var switchOver = function (flag) {
        if(flag === '0'){
            $("#itemDivDirect").show();
            $("#itemDivOrg").hide();
            $("#returnedQtyhi").hide();
            $("#receiveTitle").html("Inventry Qty");
        }else {
            $("#itemDivOrg").show();
            $("#itemDivDirect").hide();
            $("#receiveTitle").html("Received Qty");
            $("#returnedQtyhi").show();
        }
    };
    var judgement = function () {
        var storeName = m.store_name.val();
        if (storeName == null || storeName.trim() === "") {
            _common.prompt("Please enter the correct Store No.!", 3, "info");
            m.org_order_id.focus();
            m.org_order_id.css("border-color", "red");
            return false;
        } else if (storeName.trim() === m.store_cd.val()) {
            _common.prompt("The store is no longer valid!", 3, "info");
            m.org_order_id.focus();
            m.org_order_id.css("border-color", "red");
            return false;
        } else {
            m.org_order_id.css("border-color", "#CCC");
        }
    }
    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        if (value != null && value.trim() != '' && value.length == 8) {
            value = fmtStringDate(value);
        }
        return $(tdObj).text(value);
    }

    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date) {
        var res = "";
        if (date != null && date.trim() != '' && date.length == 8) {
            res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        }
        return res;
    }

    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function subfmtDate(date) {
        var res = "";
        if (date != null && date != '')
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('returnWarehouseEdit');
_start.load(function (_common) {
    _index.init(_common);
});
