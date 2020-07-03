require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('rtInventoryQuery', function (_common) {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common = null;

    var m = {
        toKen: null,
        use: null,
        error_pcode: null,
        identity: null,
        searchJson: null,
        viewSts: null,
        cancel: null,
        affirm: null,
        returnsViewBut: null,
        saveBtn: null,
        // 查询部分
        inventory_date: null,
        search_code_input: null,
        search_barcode_input: null,
        search_itemname_input: null,
        search_inventory_quantity: null,
        search_sales_quantity: null,
        search_receiving_quantity: null,
        item_code: null,
        item_barcode: null,
        item_name: null,
        inventory_quantity: null,
        sales_quantity: null,
        receiving_quantity: null,
        Stock_Quantity: null,
        search: null,
        reset: null,
        // 库存信息
        ItemCode: null,
        ItemBarcode: null,
        ItemName: null,
        Specification: null,
        UOM: null,
        InventoryQuantityPreviousDay: null,
        SalesQuantityTheday: null,
        ReceivingQuantityTheday: null,
        InventoryAdjustmentQuantityTheday: null,
        QuantityInTransit: null,
        StockOnHandQuantity: null,
        WriteOffQuanity: null,
        TransferQuantity: null,
        //弹窗信息
        code: null,
        barcode: null,
        name: null,
        specification: null,
        uom: null,
        inventoryQty: null,
        salesQty: null,
        receivingQty: null,
        adjustmentQty: null,
        transitQty: null,
        handQty: null,
        writeOffQty: null,
        transferQty: null
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
        url_left = _common.config.surl + "/rtInventoryQuery";
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 加载下拉列表
        // getSelectValue();
        //根据登录人的不同身份权限来设定画面的现实样式
        console.log(identity);
        initPageBytype(m.identity.val());
        //表格内按钮事件
        table_event();
        // 初始化查询加载数据
        m.search.click();
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function (flgType) {
        switch (flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                break;
            default:
                // m.error_pcode.show();
                // m.main_box.hide();
        }

    }

    //表格内按钮点击事件
    var table_event = function () {

        // add 打开模态框
        $("#add").on("click", function () {
            var flg = m.viewSts.val();
            if(flg!="edit"){
                return false;
            }
            clearDialogValue(true);  // 清理弹窗内容
            setDialogIsDisable(false); // 设置弹窗是否允许编辑
            $('#myModal').attr("flg","add");
            $('#myModal').modal("show");
        });

        // view
        $("#view").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
                return false;
            }
            clearDialogValue(true);
            setDialogIsDisable(true);
            // 获取值，设置显示
            var cols = tableGrid.getSelectColValue(selectTrTemp, "Item Code", "Item Barcode", "Item Name", "Specification", "UOM", "Inventory Quantity(Previous Day)",
                "Sales Quantity the day", "Receiving Quantity the day", "Inventory Adjustment Quantity the day",
                "Quantity in Transit", "Stock on Hand Quantity", "Write_off Quanity", "Transfer Quantity");
            $("#ItemCode").val(cols['Item Code']);
            $("#ItemBarcode").val(cols['Item Barcode']);
            $("#ItemName").val(cols['Item Name']);
            $("#Specification").val(cols['Specification']);
            $("#UOM").val(cols['UOM']);
            $("#InventoryQuantityPreviousDay").val(cols['Inventory Quantity(Previous Day)']);
            $("#SalesQuantityTheday").val(cols['Sales Quantity the day']);
            $("#ReceivingQuantityTheday").val(cols['Receiving Quantity the day']);
            $("#InventoryAdjustmentQuantityTheday").val(cols['Inventory Adjustment Quantity the day']);
            $("#QuantityInTransit").val(cols['Quantity in Transit']);
            $("#StockOnHandQuantity").val(cols['Stock on Hand Quantity']);
            $("#WriteOffQuanity").val(cols['Write_off Quanity']);
            $("#TransferQuantity").val(cols['Transfer Quantity"']);
            $('#myModal').attr("flg", "upt");
            $('#myModal').modal("show");
        });

        // update
        $("#update").on("click", function () {
            var flg = m.viewSts.val();
            if (flg != "edit") {
                return false;
            }
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
                return false;
            }
            clearDialogValue(true);
            setDialogIsDisable(false);
            // 获取值，设置显示
            var cols = tableGrid.getSelectColValue(selectTrTemp, "Item Code", "Item Barcode", "Item Name", "Specification", "UOM", "Inventory Quantity(Previous Day)",
                "Sales Quantity the day", "Receiving Quantity the day", "Inventory Adjustment Quantity the day",
                "Quantity in Transit", "Stock on Hand Quantity", "Write_off Quanity", "Transfer Quantity");
            $("#ItemCode").val(cols['Item Code']);
            $("#ItemBarcode").val(cols['Item Barcode']);
            $("#ItemName").val(cols['Item Name']);
            $("#Specification").val(cols['Specification']);
            $("#UOM").val(cols['UOM']);
            $("#InventoryQuantityPreviousDay").val(cols['Inventory Quantity(Previous Day)']);
            $("#SalesQuantityTheday").val(cols['Sales Quantity the day']);
            $("#ReceivingQuantityTheday").val(cols['Receiving Quantity the day']);
            $("#InventoryAdjustmentQuantityTheday").val(cols['Inventory Adjustment Quantity the day']);
            $("#QuantityInTransit").val(cols['Quantity in Transit']);
            $("#StockOnHandQuantity").val(cols['Stock on Hand Quantity']);
            $("#WriteOffQuanity").val(cols['Write_off Quanity']);
            $("#TransferQuantity").val(cols['Transfer Quantity"']);
            $('#myModal').attr("flg", "upt");
            $('#myModal').modal("show");
        });
  }


    // 画面按钮点击事件
    var but_event = function () {

        // // 返回一览
        // m.returnsViewBut.on("click",function(){
        //     top.location = url_left;
        // });
        //
        // // 保存按钮
        // m.saveBtn.on("click",function() {
        //     // 判断是否允许保存
        //     var _sts = m.viewSts.val();
        //     if (_sts != "edit") {
        //         return;
        //     }
        //     // 数据检查
        //     if(verifySearch()){
        //         _common.myConfirm("Are you sure you want to save?",function(result){
        //             if(result!="true"){return false;}
        //             $.myAjaxs({
        //                 url:url_left+'/editInventory',
        //                 async:true,
        //                 cache:false,
        //                 type :"post",
        //                 data :_data,
        //                 dataType:"json",
        //                 success:function(result){
        //                     if(result.success){
        //                         // 变为查看模式
        //                         setDisable();
        //                         m.viewSts.val("view");
        //                         _common.prompt("保存成功",5,"info");
        //                     }else{
        //                         _common.prompt(result.msg,5,"error");
        //                     }
        //                 },
        //                 error : function(e){
        //                     _common.prompt("保存失败",5,"error");
        //                 }
        //             });
        //         })
        //     }
        // });
        //
        // // 详情弹窗Close按钮
        // m.cancel.on("click",function(){
        //     var  flg = $('#myModal').attr("flg");
        //     if(flg=="view"){
        //         $('#myModal').modal("hide");
        //         return false;
        //     }
        //     _common.myConfirm("Are you sure you want to close?",function(result){
        //         if(result == "true"){
        //             $('#myModal').modal("hide");
        //         }
        //     });
        // });
        //
        // // 详情弹窗Submit按钮
        // m.affirm.on("click",function(){
        //     if(!verifyDialogSearch()){return false;}
        //     // 判断商品是否存在
        //     var flg = $('#myModal').attr("flg");
        //     var _itemCode = m.item_code.val();
        //     var _select = null;
        //     if(selectTrTemp!=null){
        //         var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
        //         _select = cols['articleId'];
        //     }
        //     if(flg=='add' || _select != _itemCode){
        //         var _addFlg = true;
        //         $("#zgGridTable>.zgGrid-tbody tr").each(function () {
        //             var articleId = $(this).find('td[tag=articleId]').text();
        //             if (articleId == _itemCode) {
        //                 _addFlg = false;
        //                 return false; // 结束遍历
        //             }
        //         });
        //         if(!_addFlg){
        //             _common.prompt("该商品已经存在!", 5, "error");
        //             return false; // 停止执行
        //         }
        //     }
        //     _common.myConfirm("Are you sure you want to submit?",function(result){
        //         if(result == "true"){
        //             if(flg=='add'){
        //                 var rowindex = 0;
        //                 var trId = $("#zgGridTable>.zgGrid-tbody tr:last").attr("id");
        //                 if(trId!=null&&trId!=''){
        //                     rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
        //                 }
        //                 var tr = '<tr id="zgGridTable_'+rowindex+'_tr" class="">' +
        //                     '<td tag="articleId" title="'+m.code.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_articleId" tdindex="zgGridTable_articleId">'+m.code.val()+'</td>' +
        //                     '<td tag="barcode" title="'+m.barcode.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_barcode" tdindex="zgGridTable_barcode">'+m.barcode.val()+'</td>' +
        //                     '<td tag="name" title="'+m.name.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_name" tdindex="zgGridTable_name">'+m.name.val()+'</td>' +
        //                     '<td tag="spec" title="'+m.specification.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_spec" tdindex="zgGridTable_spec">'+m.specification.val()+'</td>' +
        //                     '<td tag="unitName" title="'+m.uom.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_unitName" tdindex="zgGridTable_unitName">'+m.uom.val()+'</td>' +
        //                     '<td tag="inventoryQty" title="'+m.inventoryQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_inventoryQty" tdindex="zgGridTable_inventoryQty">'+m.inventoryQty.val()+'</td>' +
        //                     '<td tag="salesQty" title="'+m.salesQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_salesQty" tdindex="zgGridTable_salesQty">'+m.salesQty.val()+'</td>' +
        //                     '<td tag="receivingQty" title="'+m.receivingQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_receivingQty" tdindex="zgGridTable_receivingQty">'+m.receivingQty.val()+'</td>' +
        //                     '<td tag="adjustmentQty" title="'+m.adjustmentQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_adjustmentQty" tdindex="zgGridTable_adjustmentQty">+m.adjustmentQty.val()+\'</td>' +
        //                     '<td tag="transitQty" title="'+m.transitQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_transitQty" tdindex="zgGridTable_transitQty">+m.transitQty.val()+\'</td>' +
        //                     '<td tag="handQty" title="'+m.handQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_handQty" tdindex="zgGridTable_handQty">+m.handQty.val()+\'</td>' +
        //                     '<td tag="writeOffQty" title="'+m.writeOffQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_writeOffQty" tdindex="zgGridTable_writeOffQty">+m.writeOffQty.val()+\'</td>' +
        //                     '<td tag="transferQty" title="'+m.transferQty.val()+'" align="center" id="zgGridTable_'+rowindex+'_tr_transferQty" tdindex="zgGridTable_transferQty">+m.transferQty.val()+\'</td>' +
        //                     '</tr>';
        //                 $(".zgGrid-tbody").append(tr);
        //             }else{
        //                 if (selectTrTemp==null){
        //                     _common.prompt("未选中商品",5,"error");
        //                     return false;
        //                 }else{
        //                     var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
        //                     $("#zgGridTable>.zgGrid-tbody tr").each(function () {
        //                         var articleId = $(this).find('td[tag=articleId]').text();
        //                         if(articleId==cols["articleId"]){
        //                             $(this).find('td[tag=barcode]').text(m.barcode.val());
        //                             $(this).find('td[tag=articleId]').text(m.code.val());
        //                             $(this).find('td[tag=name]').text(m.name.val());
        //                             $(this).find('td[tag=spec]').text(m.specification.val());
        //                             $(this).find('td[tag=unitName]').text(m.uom.val());
        //                             $(this).find('td[tag=inventoryQty]').text(m.inventoryQty.val());
        //                             $(this).find('td[tag=salesQty]').text(m.salesQty.val());
        //                             $(this).find('td[tag=receivingQty]').text(m.receivingQty.val());
        //                             $(this).find('td[tag=adjustmentQty]').text(m.adjustmentQty.val());
        //                             $(this).find('td[tag=transitQty]').text(m.transitQty.val());
        //                             $(this).find('td[tag=handQty]').text(m.handQty.val());
        //                             $(this).find('td[tag=writeOffQty]').text(m.writeOffQty.val());
        //                             $(this).find('td[tag=transferQty]').text(m.transferQty.val());
        //                         }
        //                     });
        //                 }
        //             }
        //             $('#update_dialog').modal("hide");
        //         }
        //     });
        // });
        //
        // // 商品编号改变事件
        // m.code.on("change",function () {
        //     clearDialogValue(false);
        // });

        //清空按钮
        m.reset.on("click", function () {
            m.inventory_date.val("");
            m.item_code.val("");
            m.item_barcode.val("");
            m.item_name.val("");
            m.inventory_quantity.val("");
            m.sales_quantity.val("");
            m.receiving_quantity.val("");
            m.Stock_Quantity.val("");
        });

        //点击查找商品编号按钮事件
        m.search_code_input.on("click", function () {
            var inputVal = m.vendor_code.val();
            m.item_code.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("商品编号不能为空", 5, "error");
                m.item_code.attr("status", "2").focus();
                return false;
            }
            var reg = /^[0-9]*$/;
            if (!reg.test(inputVal)) {
                _common.prompt("商品编号必须是纯数字", 5, "error");
                m.item_code.attr("status", "2").focus();
                return false;
            }
            // getItemInfoByItem(inputVal, function (res) {
            //     if (res.success) {
            //         m.item_code.text($.trim(res.data.itemName));
            //         return true;
            //     } else {
            //         m.vendor_code.text("未取得商品编号");
            //         _common.prompt(res.message, 5, "error");
            //         return false;
            //     }
            // });
            m.vendor_code.val('胡志明门店');
        });

        //点击查找商品条码按钮事件
        m.search_barcode_input.on("click", function () {
            var inputVal = m.item_barcode.val();
            m.item_barcode.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("商品条码不能为空", 5, "error");
                m.item_barcode.attr("status", "2").focus();
                return false;
            }
            var reg = /^[0-9]*$/;
            if (!reg.test(inputVal)) {
                _common.prompt("商品条码必须是纯数字", 5, "error");
                m.item_barcode.attr("status", "2").focus();
                return false;
            }
        });


        //点击查找商品名称按钮事件
        m.search_itemname_input.on("click", function () {
            var inputVal = m.item_name.val();
            m.item_name.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("商品名称不能为空", 5, "error");
                m.item_name.attr("status", "2").focus();
                return false;
            }

        });

        //点击查找昨日库存数量按钮事件
        m.search_inventory_quantity.on("click", function () {
            var inputVal = m.vendor_code.val();
            m.inventory_quantity.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("昨日库存数量不能为空", 5, "error");
                m.inventory_quantity.attr("status", "2").focus();
                return false;
            }
            var reg = /^[0-9]*$/;
            if (!reg.test(inputVal)) {
                _common.prompt("昨日库存数量必须是纯数字", 5, "error");
                m.inventory_quantity.attr("status", "2").focus();
                return false;
            }
            getItemInfoByItem(inputVal, function (res) {
                if (res.success) {
                    m.inventory_quantity.text($.trim(res.data.itemName));
                    return true;
                } else {
                    m.vendor_code.text("未取得昨日库存数量");
                    _common.prompt(res.message, 5, "error");
                    return false;
                }
            });
            m.vendor_code.val('胡志明门店');
        });

        //点击查找当日销售数量按钮事件
        m.search_sales_quantity.on("click", function () {
            var inputVal = m.vendor_code.val();
            m.sales_quantity.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("当日销售数量不能为空", 5, "error");
                m.sales_quantity.attr("status", "2").focus();
                return false;
            }
            var reg = /^[0-9]*$/;
            if (!reg.test(inputVal)) {
                _common.prompt("当日销售数量必须是纯数字", 5, "error");
                m.sales_quantity.attr("status", "2").focus();
                return false;
            }
            getItemInfoByItem(inputVal, function (res) {
                if (res.success) {
                    m.sales_quantity.text($.trim(res.data.itemName));
                    return true;
                } else {
                    m.vendor_code.text("未取得当日销售数量");
                    _common.prompt(res.message, 5, "error");
                    return false;
                }
            });
            m.vendor_code.val('胡志明门店');
        });

        //点击查找当日库存调整数量按钮事件
        m.search_receiving_quantity.on("click", function () {
            var inputVal = m.vendor_code.val();
            m.receiving_quantity.text("");
            if ($.trim(inputVal) == "") {
                _common.prompt("当日库存调整数量不能为空", 5, "error");
                m.receiving_quantity.attr("status", "2").focus();
                return false;
            }
            var reg = /^[0-9]*$/;
            if (!reg.test(inputVal)) {
                _common.prompt("当日库存调整数量必须是纯数字", 5, "error");
                m.receiving_quantity.attr("status", "2").focus();
                return false;
            }
            getItemInfoByItem(inputVal, function (res) {
                if (res.success) {
                    m.receiving_quantity.text($.trim(res.data.itemName));
                    return true;
                } else {
                    m.vendor_code.text("未取得当日库存调整数量");
                    _common.prompt(res.message, 5, "error");
                    return false;
                }
            });
            m.vendor_code.val('胡志明门店');
        });

        //检索按钮点击事件
        m.search.on("click", function () {
            // if (verifySearch()) {
            //拼接检索参数
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            tableGrid.setting("url", url_left + "/getInventory");
            tableGrid.setting("inventory", paramGrid);
            tableGrid.setting("page", 1);

            tableGrid.loadData();
        });


    }

    //验证检索项是否合法
    var verifySearch = function () {
        var _temp = m.item_code.val();
        if (_temp == null || _temp == "") {
            _common.prompt("商品编码不能为空", 5, "error");
            return false;
        }
        _temp = m.item_barcode.val();
        if (_temp == null || _temp == "") {
            _common.prompt("商品条码不能为空", 5, "error");
            return false;
        }
        _temp = m.item_name.val();
        if (_temp == null || _temp == "") {
            _common.prompt("商品名称不能为空", 5, "error");
            return false;
        }
        return true;
    }
    //拼接检索参数
    var setParamJson = function () {
        // 创建请求字符串
        var searchJsonStr = {
            ItemCode: m.item_code.val(),
            ItemBarcode: m.item_barcode.val(),
            ItemName: m.item_name.val(),
            InventoryQuantityPreviousDay: m.inventory_quantity.val(),
            SalesQuantityTheday: m.sales_quantity.val(),
            ReceivingQuantityTheday: m.receiving_quantity.val(),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }


    // 清空弹窗内容
    // var clearDialogValue = function (flag) {
    //     if (flag) {
    //         $("#ItemCode").val("");
    //         $("#ItemBarcode").val("");
    //         $("#ItemName").val("");
    //     }
    //     $("#Specification").val("");
    //     $("#UOM").val("");
    //     $("#InventoryQuantityPreviousDay").val("");
    //     $("#SalesQuantityTheday").val("");
    //     $("#ReceivingQuantityTheday").val("");
    //     $("#InventoryAdjustmentQuantityTheday").val("");
    //     $("#QuantityInTransit").val("");
    //     $("#StockOnHandQuantity").val("");
    //     $("#WriteOffQuanity").val("");
    //     $("#TransferQuantity").val("");
    // }

    // 设置弹窗是否允许编辑
    // var setDialogIsDisable = function (flag) {
    //     var _sts = m.viewSts.val();
    //     if (!flag && _sts != "view") {
    //         $("#search_item_input").show();
    //     } else {
    //         $("#search_item_input").hide();
    //     }
    //     $("#ItemCode").prop("disabled", flag);
    //     $("#ItemBarcode").prop("disabled", flag);
    //     $("#ItemName").prop("disabled", flag);
    //     $("#Specification").prop("disabled", flag);
    //     $("#UOM").prop("disabled", flag);
    //     $("#InventoryQuantityPreviousDay").prop("disabled", flag);
    //     $("#SalesQuantityTheday").prop("disabled", flag);
    //     $("#ReceivingQuantityTheday").prop("disabled", flag);
    //     $("#InventoryAdjustmentQuantityTheday").prop("disabled", flag);
    //     $("#QuantityInTransit").prop("disabled", flag);
    //     $("#StockOnHandQuantity").prop("disabled", flag);
    //     $("#WriteOffQuanity").prop("disabled", flag);
    //     $("#TransferQuantity").prop("disabled", flag);
    // }

    // 获取详情List
    var getDetails = function (inventory) {
        paramGrid = "searchJson=" + inventory;
        tableGrid.setting("url", url_left + "/rtInventoryQuery/");
        tableGrid.setting("inventory", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData();
    }

    //表格初始化-库存样式
    var initTable1 = function () {

        tableGrid = $("#zgGridTable").zgGrid({
            inventory: paramGrid,
            title: "Query Result",
            localSort: true,
            colNames: ["Item Code", "Item Barcode", "Item Name", "Specification", "UOM", "Inventory Quantity(Previous Day)",
                "Sales Quantity the day", "Receiving Quantity the day", "Inventory Adjustment Quantity the day",
                "Quantity in Transit", "Stock on Hand Quantity", "Write_off Quanity", "Transfer Quantity"],
            colModel: [
                {name: "ItemCode", type: "text", text: "center", width: "130", ishide: true, css: ""},
                {name: "ItemBarcode", type: "text", text: "center", width: "130", ishide: false, css: ""},
                {name: "ItemName", type: "text", text: "center", width: "130", ishide: false, css: "", getCustomValue: formatNum},
                {name: "Specification", type: "text", text: "center", width: "100", ishide: false, css: "", getCustomValue: formatNum},
                {
                    name: "UOM",
                    type: "text",
                    text: "center",
                    width: "100",
                    ishide: false,
                    css: "",
                    getCustomValue: formatNum

                },
                {
                    name: "InventoryQuantityPreviousDay",
                    type: "text",
                    text: "center",
                    width: "230",
                    ishide: false,
                    css: ""
                },
                {
                    name: "SalesQuantityTheday",
                    type: "text",
                    text: "center",
                    width: "200",
                    ishide: false,
                    css: ""
                },
                {
                    name: "ReceivingQuantityTheday",
                    type: "text",
                    text: "center",
                    width: "200",
                    ishide: false,
                    css: ""
                },
                {
                    name: "InventoryAdjustmentQuantityTheday",
                    type: "text",
                    text: "center",
                    width: "300",
                    ishide: false,
                    css: ""
                },
                {
                    name: "QuantityInTransit",
                    type: "text",
                    text: "center",
                    width: "130",
                    ishide: false,
                    css: ""
                },
                {
                    name: "StockOnHandQuantity",
                    type: "text",
                    text: "center",
                    width: "180",
                    ishide: false,
                    css: ""
                },
                {
                    name: "WriteOffQuanity",
                    type: "text",
                    text: "center",
                    width: "130",
                    ishide: false,
                    css: ""
                },
                {
                    name: "TransferQuantity",
                    type: "text",
                    text: "center",
                    width: "130",
                    ishide: false,
                    css: ""
                }
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
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
                    butType: "custom",
                    butHtml: "<button id='add' type='button'  class='btn btn-info btn-sm' data-toggle=\"modal\" data-target=\"#myModal\"><span class='glyphicon glyphicon-plus'></span> Add</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-list-alt'></span> View</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='update' type='button' class='btn btn-info btn-sm''><span class='glyphicon glyphicon-pencil'></span> Modify</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='printStoreDetails' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"
                }
            ],
        });
    }


    //number格式化
    var formatNum = function(tdObj,value){
        return $(tdObj).text(fmtIntNum(value));
    }
    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
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

    //根据商品条码取得该商品的详细对象，
    // var getItemInfoByItem = function (title, item1, fun) {
    //     $.myAjaxs({
    //         url: url_left + "",
    //         async: true,
    //         cache: false,
    //         type: "post",
    //         data: "ItemCode=" + item1,
    //         dataType: "json",
    //         success: function (result) {
    //             if (result.success) {
    //                 // 获取库存详情
    //                 getDetails(inventory);
    //                 // 加载数据
    //                 m.ItemCode.val(result.o.ItemCode);
    //                 m.ItemBarcode.val(result.o.ItemBarcode);
    //                 m.ItemName.val(result.o.ItemName);
    //                 m.Specification.val(result.o.Specification);
    //                 m.UOM.val(result.o.UOM);
    //                 m.InventoryQuantityPreviousDay.val(result.o.InventoryQuantityPreviousDay);
    //                 m.SalesQuantityTheday.val(result.o.SalesQuantityTheday);
    //                 m.ReceivingQuantityTheday.val(result.o.ReceivingQuantityTheday);
    //                 m.InventoryAdjustmentQuantityTheday.val(result.o.InventoryAdjustmentQuantityTheday);
    //                 m.QuantityInTransit.val(result.o.QuantityInTransit);
    //                 m.StockOnHandQuantity.val(result.o.StockOnHandQuantity);
    //                 m.WriteOffQuanity.val(result.o.WriteOffQuanity);
    //                 m.TransferQuantity.val(result.o.TransferQuantity);
    //             } else {
    //                 _common.prompt(result.msg, 5, "error");
    //                 var _sts = m.viewSts.val();
    //                 if (_sts == "edit") {
    //                     setDisable();
    //                     m.viewSts.val("view");
    //                 }
    //                 return;
    //             }
    //         },
    //         error: function (e) {
    //             _common.prompt(title + "数据加载失败", 5, "error");
    //         },
    //         complete: _common.myAjaxComplete
    //     });
    // }

    // 请求加载下拉列表
    // function getSelectValue(){
    //     // 加载select
    //     initSelectOptions("Inventory Quantity(Previous Day","InventoryQuantityPreviousDay", "00225");
    //     initSelectOptions("Stock on Hand Quantity","StockOnHandQuantity", "00245");
    // }


    // 验证弹窗页面录入项是否符合
    // var verifyDialogSearch = function(){
    //     var flg = $('#myModal').attr("flg");
    //     if(flg!="add"&&flg!="upt"){return false;}
    //     var _temp = $("#itemName").val();
    //     if(_temp == null || $.trim(_temp)==""){
    //         _common.prompt("商品不能为空",5,"error");
    //         return false;
    //     }
    //     _temp = $("#specification").val();
    //     if(_temp == null || $.trim(_temp)==""){
    //         _common.prompt("商品数量不能为空",5,"error");
    //         return false;
    //     }
    //     return true;
    // }


    // 设置为查看模式
    var setDisable = function () {
        $("#ItemCode").prop("disabled", true);
        $("#ItemBarcode").prop("disabled", true);
        $("#ItemName").prop("disabled", true);
        $("#Specification").prop("disabled", true);
        $("#UOM").prop("disabled", true);
        $("#InventoryQuantityPreviousDay").prop("disabled", true);
        $("#SalesQuantityTheday").prop("disabled", true);
        $("#ReceivingQuantityTheday").prop("disabled", true);
        $("#InventoryAdjustmentQuantityTheday").prop("disabled", true);
        $("#QuantityInTransit").prop("disabled", true);
        $("#StockOnHandQuantity").prop("disabled", true);
        $("#WriteOffQuanity").prop("disabled", true);
        $("#TransferQuantity").prop("disabled", true);
    }


        self.init = init;
        return self;
});

var _start = require('start');
var _index = require('rtInventoryQuery');
_start.load(function (_common) {
    _index.init(_common);
});
