require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('custDataEntry', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        file = $('<input type="file"/>'),
        tempTrObjValue = {},//临时行数据存储
        dataForm = [],
        systemPath = null,
        subFlag='';
    const KEY = 'COST_ITEM_ADJUSTMENT_MANAGEMENT';
    var m = {
        toKen: null,
        use: null,
        up: null,
        entiretyBmStatus: null,
        item_name: null,
        articleId: null,
        qty: null,
        lastUpdateTime: null,
        tableGrid: null,
        selectTrObj: null,
        error_pcode: null,
        identity: null,
        reject_dialog: null,
        input_item: null,
        create_ymd: null,
        saveBut: null,
        searchbtn: null,//检索按钮
        returnsViewBut: null,
        store: null,
        remarks: null,
        cancel: null,
        piCd: null, // 费用录入单号
        inventoryQty: null, // 实时库存
        search_item_but: null,
        tempTableType: null,
        searchJson: null,
        enterFlag: null,
        piCdParam: null,
        searchItemInp: null, // 商品Code定位输入框
        searchItemBcd: null, // 商品Barcode定位输入框
        searchItemBtn: null, // 商品定位按钮
        restItemBtn: null,   // 商品重置按钮
        //审核
        approvalBut:null,
        approval_dialog:null,
        audit_cancel:null,
        audit_affirm:null,
        typeId:null,
        i_storeCd:null,
        piDateParam:null,
        ParamstoreCd:null,
        ParamstoreName: null,
        create_By:null,
        searchJson1:null,
        main_box:null,
        reviewId:null
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/custEntry";
        systemPath = _common.config.surl;
        but_event();
        // 初始化store下拉
        initAutoMatic();
        $("#create_ymd").prop("disabled", true);
        $("#create_user").prop("disabled", true);
        m.approvalBut.prop("disabled",true);
        initTable1();
        //表格内按钮事件
        table_event();
        initPage();

        //审核事件
        approval_event();
    }
     var initPage=function () {
         if (m.enterFlag.val()) {
             $("#store").attr("disabled",true);
             // 获取数据
             getData(m.piCdParam.val());
             if (m.enterFlag.val()=="update"){
                 $("#updatePlanDetails").show();
             }
             if (m.enterFlag.val() == 'view') {
                 $("#updatePlanDetails").hide();
                 setDisable(true);
                 //检查是否允许审核
                 _common.checkRole(m.piCdParam.val(),m.typeId.val(),function (success) {
                     if(success){
                         m.approvalBut.prop("disabled",false);
                     }else{
                         m.approvalBut.prop("disabled",true);
                     }
                 });
             }

         }else {
             $("#updatePlanDetails").hide();
         }
     }
    // 保存导入的商品到表格里
    var addItemList = function (list) {
        if (!list) {
            return;
        }

        let lastUpdateTime = new Date().Format("dd/MM/yyyy hh:mm:ss");
        $("#zgGridTtable  tr:not(:first)").remove();
        dataForm=[];
        let htmls = "";
        var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
        var i =-1;
        list.forEach(function (item) {
            item.rowIndex=uuid();
            item.lastUpdateTime=lastUpdateTime;
            item.qty=reThousands(item.qty);
            item.stockQty=reThousands(item.stockQty);
            if(i<list.length){
                i=i+1
            }
            htmls += '<tr data-index="'+item.rowIndex+'">' +
                '<td tag="barcode" width="130" title="'+item.barcode+'" align="center" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
                '<td tag="articleId" width="130" title="'+item.articleId+'" align="center" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
                '<td tag="articleName" width="130" title="'+item.articleName+'" align="left" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
                '<td tag="uom" width="130" title="'+item.uom+'" align="left" tdindex="zgGridTtable_uom">'+item.uom+'</td>' +
                '<td tag="qty" width="130" title="'+toThousands(item.qty)+'" align="right" tdindex="zgGridTtable_qty">'+toThousands(item.qty)+'</td>' +
                '<td tag="stockQty" width="130" title="'+toThousands(item.stockQty)+'" align="right" tdindex="zgGridTtable_stockQty">'+toThousands(item.stockQty)+'</td>' +
                // '<td tag="lastUpdateTime" width="130" title="'+lastUpdateTime+'" align="center" tdindex="zgGridTtable_lastUpdateTime">'+lastUpdateTime+'</td>' +
                // '<td tag="note" width="130" title="'+isEmpty(item.note)+'" align="center" tdindex="zgGridTtable_note">'+isEmpty(item.note)+'</td>' +
              '</tr>';
            dataForm.push(item);

        });
        tableGrid.append(htmls);
        loading_close();
    };

    var table_event = function () {
        // submit 按钮
        $('#affirm').on('click', function () {
            if (!verifySearch()) {
                return;
            }
            let barcode = $('#barcode').val();
            let articleId = $('#a_article').attr("k");
            let articleName = $('#a_article').attr("v");
            let uom = $('#uom').val();
            let qty = $('#qty').val();
            let stockQty = toThousands($('#inventoryQty').val());
            let lastUpdateTime = new Date().Format("dd/MM/yyyy hh:mm:ss");
            let note = $('#note').val();

            var _addFlg = false;
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                var _articleId = $(this).find('td[tag=articleId]').text();
                if (_articleId === articleId) {
                    _addFlg = true;
                    return false; // 结束遍历
                }
            });

            if (subFlag == 'add') {
                if (_addFlg) { // 商品相同
                    _common.prompt("Item already exists!", 5, "error");
                    return;
                }
            }
            _common.myConfirm("Are you sure you want to submit?", function (result) {
                if (result !== "true") {
                    return false;
                }

                if (subFlag == 'add') {
                    var trList = $("#zgGridTtable  tr:not(:first)");
                    var obj = {
                        'barcode': barcode,
                        'articleId': articleId,
                        'articleName': articleName,
                        'uom': uom,
                        'qty': qty,
                        'stockQty': stockQty,
                        'lastUpdateTime': lastUpdateTime,
                        'note': note,
                        'rowIndex': uuid(), // 设置一个唯一标识
                    }
                    dataForm.push(obj);
                    var rowindex = 0;
                    var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
                    var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
                    if (trId != null && trId != '') {
                        rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
                    }
                    var html = '<tr data-index="' + obj.rowIndex + '">' +
                        '<td tag="barcode" width="130" title="' + barcode + '" align="center" id="zgGridTtable_' + rowindex + '_tr_barcode" tdindex="zgGridTtable_barcode">' + barcode + '</td>' +
                        '<td tag="articleId" width="130" title="' + articleId + '" align="center" id="zgGridTtable_' + rowindex + '_tr_articleId" tdindex="zgGridTtable_articleId">' + articleId + '</td>' +
                        '<td tag="articleName" width="130" title="' + articleName + '" align="left" id="zgGridTtable_' + rowindex + '_tr_articleName" tdindex="zgGridTtable_articleName">' + articleName + '</td>' +
                        '<td tag="uom" width="80" title="' + uom + '" align="left" id="zgGridTtable_' + rowindex + '_tr_uom" tdindex="zgGridTtable_uom">' + uom + '</td>' +
                        '<td tag="qty" width="80" title="' + qty + '" align="right" id="zgGridTtable_' + rowindex + '_tr_qty" tdindex="zgGridTtable_qty">' + qty + '</td>' +
                        '<td tag="stockQty" width="80" title="' + stockQty + '" align="right" id="zgGridTtable_' + rowindex + '_tr_stockQty" tdindex="zgGridTtable_stockQty">' + stockQty + '</td>' +
                        // '<td tag="lastUpdateTime" width="130" title="'+lastUpdateTime+'" align="center" id="zgGridTtable_'+rowindex+'_tr_lastUpdateTime" tdindex="zgGridTtable_lastUpdateTime">'+lastUpdateTime+'</td>' +
                        // '<td tag="note" width="130" title="'+isEmpty(note)+'" align="left" id="zgGridTtable_'+rowindex+'_tr_note" tdindex="zgGridTtable_note">'+isEmpty(note)+'</td>' +
                        '</tr>';
                    tableGrid.append(html);

                } else if (subFlag === 'update') {

                    var selIdx = selectTrTemp.attr('data-index');
                    $("#zgGridTtable>.zgGrid-tbody tr").each(function (index) {
                        var temp = $(this).find('td[tag=articleId]').text();
                        var _rowIndex = $(this).attr('data-index');
                        if (articleId === temp) {
                            if (_rowIndex !== selIdx) {
                                _common.prompt("Item already exists!", 5, "error");
                                return false; // 推出循环
                            }
                        }
                        if (_rowIndex == selIdx) {
                            $(this).find('td[tag=barcode]').text(barcode);
                            $(this).find('td[tag=articleId]').text(articleId);
                            $(this).find('td[tag=articleName]').text(articleName);
                            $(this).find('td[tag=uom]').text(uom);
                            $(this).find('td[tag=qty]').text(qty);
                            $(this).find('td[tag=stockQty]').text(stockQty);
                            $(this).find('td[tag=lastUpdateTime]').text(lastUpdateTime);
                            $(this).find('td[tag=note]').text(note);

                            var obj = {
                                'barcode': barcode,
                                'articleId': articleId,
                                'articleName': articleName,
                                'uom': uom,
                                'qty': qty,
                                'stockQty': stockQty,
                                'lastUpdateTime': lastUpdateTime,
                                'note': note,
                                'rowIndex': selIdx,
                            };

                            for (let i = 0; i < dataForm.length; i++) {
                                let item = dataForm[i];
                                if (selIdx === item.rowIndex) {
                                    dataForm[i] = obj;
                                }
                            }
                        }
                    });
                }
                $('#update_dialog').modal("hide");
            })
        });


        file.change(function(e){
            var select_file = file[0].files[0];
            let suffix = new RegExp(/[^\.]+$/).exec(select_file.name)[0];
            let excel_reg = /([xX][lL][sS][xX]){1}$|([xX][lL][sS]){1}$/;
            if (!excel_reg.test(suffix)) {
                _common.prompt('The uploaded file should be in Excel format!',5,"error");
                file.val('');
                return;
            }
            var formData = new FormData();
            formData.append('fileData',select_file);
            formData.append('storeCd',$("#store").attr("k"));

            // 确定要导入吗？
            _common.myConfirm("Are you sure you want to import?",function(result) {
                if (result != "true") {
                    file.val('');
                    return false;
                }
                _common.loading();
                $.ajax({
                    url:url_left+"/upload",
                    dataType:'json',
                    type:'POST',
                    async: false,
                    data: formData,
                    processData : false, // 使数据不做处理
                    contentType : false, // 不要设置Content-Type请求头
                    success: function(data){
                        file.val('');
                        if (data.success) {
                            _common.prompt(data.message,5,"success");
                            addItemList(data.data)
                        } else {
                            _common.prompt(data.message,5,"errer");
                        }
                    },
                    error: function () {
                        loading_close();
                    }
                });
            });
        })

        $("#importFiles").on("click",function(){
            file.click();
        });

        $("#addPlanDetails").on("click", function () {
            let storeCd = m.store.attr('k');
            if (!storeCd) {
                _common.prompt("Please enter the Store!",5,"error");/*请录入费用录入商品数据*/
                $('#store').focus();
                $('#store').css("border-color","red");
                return;
            }else {
                $('#store').css("border-color","#CCC");
            }
            subFlag='add';
            $("#a_article").prop("disabled", false);
            $("#article_refresh").show();
            $("#article_clear").show();
            clearInput();
            $('#update_dialog').modal("show");
        });
        $("#updatePlanDetails").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
            } else {
                subFlag = 'update';
                let record = getSelectVal();
                $("#a_article").prop("disabled", true);
                $("#article_refresh").hide();
                $("#article_clear").hide();
                $('#update_dialog').modal("show");
                $('#barcode').val(record.barcode);
                $('#a_article').val(record.articleName).attr("k",record.articleId).attr("v",record.articleName);
                $('#articleId').val(record.articleId);
                $('#uom').val(record.uom);
                $('#qty').val(record.qty);
                $('#inventoryQty').val(toThousands(record.stockQty));
                $('#lastUpdateTime').val(record.lastUpdateTime);
                $('#note').val(record.note);
            }
        });
        // 删除按钮
        $("#deletePlanDetails").on("click", function () {
            if(selectTrTemp == null){
                _common.prompt("Please select the data to confirm deletion!",5,"error");/*请选择要确认删除的数据*/
                return false;
            }else{
                _common.myConfirm("Are you sure you want to delete?",function(result){
                    if(result == "true"){
                        // 选中的行号
                        let selIdx = $(selectTrTemp).attr('data-index');
                        var trList = $("#zgGridTtable  tr:not(:first)");
                        for (let i = 0; i < dataForm.length; i++) {
                            let item = dataForm[i];
                            if (item.rowIndex==selIdx) {
                                dataForm.splice(i,1); // 删除数组中数据
                                trList[i].remove();
                            }
                        }
                        _common.prompt("Data deleted successfully", 3, "success");
                    }
                });
            }
        });
        //返回一览
        // m.returnsViewBut.on("click", function () {
        //     top.location = url_left;
        // });
        m.returnsViewBut.on("click",function(){
            var bank = $("#saveBut").attr("disabled");
            if (bank!='disabled' ) {
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
                    if (result==="true") {
                        _common.updateBackFlg(KEY);
                        top.location = url_left;
                    }});
            }
            if (bank==='disabled' ) {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
        });
        if(!m.enterFlag.val()){
            //是否有冻结列的标记
            var freeze = tableGrid.getting("freezeIndex");
            //重写选择背景变色事件
            tableGrid.find("tbody").unbind('mousedown').on('mousedown','td', function (e) {
                var thisObj = $(this),
                    thisParent = $(this).parent();
                if(3 == e.which){ //鼠标右键
                    thisParent.addClass("info").siblings().removeClass("info");
                    var eachTrRightclickFun = $.isFunction(tableGrid.getting("eachTrRightclick")) ? true : false;
                    if(eachTrRightclickFun){
                        tableGrid.getting("eachTrRightclick").call(this,thisParent,thisObj);
                    }
                    if(freeze>=0){
                        var freeToTr = thisParent.prop("id");
                        $("#"+freeToTr+"_free").addClass("info").siblings().removeClass("info");
                    }
                }else if(1 == e.which){ //鼠标左键
                    var cols = tableGrid.getSelectColValue(thisParent,"articleId");
                    var articleId = $(selectTrTemp).find('td[tag=articleId]').text();
                    if(cols["articleId"] != articleId && $('#grid_orderQty').length == 1){
                        var flg = checkOrderQty();
                        if(flg){
                            //还原表格中的input框
                            let qty = toThousands($('#grid_orderQty').val().trim());
                            $(selectTrTemp).find('td[tag=qty]').text(qty);
                        }else{
                            let qty = $('#grid_orderQty').attr("oldValue");
                            $(selectTrTemp).find('td[tag=qty]').text(qty);
                            return false;
                        }
                    }
                    //改变背景颜色
                    thisParent.addClass("info").siblings().removeClass("info");
                    if(freeze>=0){
                        var freeToTr = thisParent.prop("id");
                        $("#"+freeToTr+"_free").addClass("info").siblings().removeClass("info");
                    }
                    $(".zgGrid-modal").hide();
                    var eachTrClickFun = $.isFunction(tableGrid.getting("eachTrClick")) ? true : false;
                    if(eachTrClickFun){
                        tableGrid.getting("eachTrClick").call(this,thisParent,thisObj);
                    }
                }
            });
            // //光标移出
            m.main_box.on("blur","#grid_orderQty",function(){
                $("#grid_orderQty").val(this.value);
                // var flg = checkOrderQty();
                // if(flg){
                // 	//还原表格中的input框
                // 	var orderQty = toThousands($('#grid_orderQty').val().trim());
                // 	$(selectTrTemp).find('td[tag=orderQty]').text(orderQty);
                // }else{
                // 	return false;
                // }
            });

            //光标进入，去除金额千分位，并去除小数后面多余的0
            m.main_box.on("focus","#grid_orderQty",function(){
                $("#grid_orderQty").val(this.value);
            })

            //回车保存
            m.main_box.on("keydown","#grid_orderQty",function(e){
                if(e.keyCode==13){
                    var flg = checkOrderQty();
                    if(flg){
                        //还原表格中的input框
                        var qty = toThousands($('#grid_orderQty').val().trim());
                        $(selectTrTemp).find('td[tag=qty]').text(qty);
                    }else{
                        let qty = $('#grid_orderQty').attr("oldValue");
                        $(selectTrTemp).find('td[tag=qty]').text(qty);
                        return false;
                    }
                }
            })
        }

    }
    //  3/20开始
    var checkOrderQty = function () {
        var qty = reThousands($('#grid_orderQty').val().trim());
        var qty1 = $('#grid_orderQty').val().trim();
        var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
        if(!reg.test(qty) || qty1.indexOf(",")>0){
            _common.prompt("Please enter with correct data type!",3,"info");
            $('#grid_orderQty').focus();
            return false;
        }
        return true;
    }
    var addInputValue=function () {
        var cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,uom,qty,stockQty");
        $(selectTrTemp).find('td[tag=qty]').text("").append("<input type='text' class='form-control my-automatic input-sm' id='grid_orderQty' oldValue='"+cols["qty"]+"' value='"+cols["qty"]+"'>");
    }
    var loading_close = function () {
        $(".loading-mask-div").fadeOut(100,function(){
            $(".init-loading-box").fadeOut(200);
        });
    }

    var approval_event = function () {
        //点击审核按钮
        m.approvalBut.on("click",function(){
            var recordId = m.piCdParam.val();
            if(recordId!=null&&recordId!=""){
                $("#approval_dialog").modal("show");
                //获取审核记录
                _common.getStep(recordId,m.typeId.val());
            }else{
                _common.prompt("Record fetch failed, Please try again!",5,"error");
            }
        });
        //审核提交
        $("#audit_affirm").on("click",function () {
            //审核意见
            var auditContent = $("#auditContent").val();
            if(auditContent.length>200){
                _common.prompt("Approval comments cannot exceed 200 characters!",5,"error");
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
                var detailType = "tmp_ci_adjustment";
                $.myAjaxs({
                    url: systemPath + "/audit/submit",
                    async: true,
                    cache: false,
                    type: "post",
                    data: {
                        auditStepId: auditStepId,
                        auditUserId: auditUserId,
                        auditStatus: auditStatus,
                        detailType:detailType,
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
                            _common.prompt("Saved Successfully!",3,"success");// 保存审核信息成功
                        } else {
                            m.approvalBut.prop("disabled", false);
                            _common.prompt("Saved Failure!",5,"error");// 保存审核信息失败
                        }
                        m.toKen.val(result.toKen);
                    },
                    complete:_common.myAjaxComplete
                });
            })
        })
    }

    function uuid() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    }

    // 06/03/2020 -> 20200306 HH:mm:ss
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }

    //画面按钮点击事件
    var but_event = function () {
        var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;

        $("#qty").blur(function () {
         var qty= $("#qty").val();
            if(reg.test(qty) || qty.indexOf(",")<0){
                $("#qty").val(toThousands(this.value));
          }
        });
        //
        // //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#qty").focus(function () {
            var qty= $("#qty").val();
            if(reg.test(qty) || qty.indexOf(",")<0){
                $("#qty").val(toThousands(this.value));
            }
        });

        m.cancel.on("click",function () {
            _common.myConfirm("Are you sure you want to cancel",function(result){
                if (result=="true"){
                    $('#update_dialog').modal("hide");
                }
                $("#a_article").css("border-color","#CCCCCC");
                $("#qty").css("border-color","#CCCCCC");
            })
        })

        // 保存
        m.saveBut.on('click',function () {
            let piCd = m.piCd.val();
            let remarks = m.remarks.val();
            let storeCd = m.store.attr('k');
            if (!storeCd) {
                _common.prompt("Please enter the Store!",5,"error");/*请录入费用录入商品数据*/
                $('#store').css("bordor-color","red");
                $('#store').focus();
                return;
            }else {
                $('#store').css("bordor-color","#CCC")
            }
            if (!m.enterFlag.val()) {
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var _articleId = $(this).find('td[tag=articleId]').text();
                    var barcode = $(this).find('td[tag=barcode]').text();
                    var articleName = $(this).find('td[tag=articleName]').text();
                    var uom = $(this).find('td[tag=uom]').text();
                    var qty = reThousands($(this).find('td[tag=qty]').text());
                    var stockQty = reThousands($(this).find('td[tag=stockQty]').text());
                    var reasonCode = $(this).find('td[tag=reasonCode]').text();
                    var fnStockQty = $(this).find('td[tag=fnStockQty]').text();
                    var obj = {
                        'barcode': barcode,
                        'articleId': _articleId,
                        'articleName': articleName,
                        'uom': uom,
                        'qty': qty,
                        'reasonCode':reasonCode,
                        'stockQty': stockQty,
                        'fnStockQty':fnStockQty,
                        'rowIndex': uuid(), // 设置一个唯一标识
                    }
                    if (obj.qty != 0) {
                        dataForm.push(obj);
                    }

                });
                if (dataForm.length < 1) {
                    _common.prompt("Please enter the inventory data!", 5, "error");/*请录入费用录入商品数据*/
                    return;
                }
                // 头档信息

            }
            let data = {
                'piCd': piCd,
                'remarks': remarks,
                'storeCd': storeCd,
            }
            dataForm.forEach(function (item) {
                item.qty = reThousands(item.qty);
                delete item.articleName;
            });
            var record = encodeURIComponent(JSON.stringify(dataForm));
            data = encodeURIComponent(JSON.stringify(data));
            _common.myConfirm("Are you sure you want to save?",function(result){
                if(result!="true"){return false;}
                $.myAjaxs({
                    url:url_left+"/save",
                    async:true,
                    cache:false,
                    type :"post",
                    data :'record='+record+'&data='+data,
                    dataType:"json",
                    success:function(result){
                        if(result.success){
                            // 变为查看模式
                            if(m.create_ymd.val()==''){
                                m.create_ymd.val(_common.formatCreateDate(result.o.createYmd+result.o.createHms));
                            }
                            m.piCd.val(result.o.piCd);
                            _common.prompt("Data saved successfully！",2,"success",function(){/*保存成功*/
                                //发起审核
                                var typeId =m.typeId.val();
                                var	nReviewid =m.reviewId.val();
                                var	recordCd = m.piCd.val();
                                _common.initiateAudit(storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
                                    setDisable(true);
                                    m.enterFlag='view';
                                    //审核按钮禁用
                                    m.approvalBut.prop("disabled",true);
                                    m.toKen.val(token);
                                })
                            }, true);
                        }else{
                            _common.prompt(result.msg,5,"error");
                        }
                    },
                    error:function(e){
                        _common.prompt("Data save failed!",5,"error");/*费用录入商品保存失败*/
                    },
                });
            })
        });

        // 输入商品id定位功能
        m.searchItemBtn.on('click',function () {

            let storeCd = m.store.attr('k');
            let articleId=m.searchItemInp.val();
            let barcode=m.searchItemBcd.val();
            if (!storeCd){
                _common.prompt("Please enter the Store!",5,"error");/*请录入费用录入商品数据*/
                $('#store').focus();
                $('#store').css("border-color","red");
                return;
            }else {
                $('#store').css("border-color","#CCC");
            }

           if (m.piCd.val()!= null && m.piCd.val()!=" "){
                if (m.enterFlag.val()=="view" || m.enterFlag.val()=="update"){
                    getDataIn(m.piCd.val());
                }else {
                    var searchJsonStr={
                        storeCd:$("#store").attr("k"),
                        articleId:articleId,
                        barcode:barcode,
                    }
                    m.searchJson1.val(JSON.stringify(searchJsonStr));
                    paramGrid = "searchJson="+ m.searchJson1.val();
                    tableGrid.setting("url",url_left+"/getStoreCustItem");
                    tableGrid.setting("param", paramGrid);
                    tableGrid.setting("page", 1);
                    tableGrid.loadData(null);
                }

           }
            // searchItem();
        });

        // 商品重置
        m.restItemBtn.on('click',function () {
            $("#searchItemBcd").val("");
            $("#searchItemInp").val("");
            searchItem();
        });

        //费用录入创建日期
        m.create_ymd.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });

        // 商品条码输入口 焦点离开事件
        m.input_item.on("blur", function () {
            if ($(this).val() == "") {
                m.input_item.attr("status", "0");
                m.item_name.text("");
            }
        });
        m.input_item.on("keydown", function () {
            if (event.keyCode == "13") {
                var thisObj = $(this);
                if (thisObj.val() == "") {
                    m.item_name.text("");
                } else {
                    m.search_item_but.click();
                }
            }
        });
    }

    var searchItem = function () {
        // 还没有添加商品
        if (dataForm.length<1) {
            return;
        }
        var trList = $("#zgGridTtable  tr:not(:first)");
        trList.show();
        var searchItemCode = m.searchItemInp.val().trim();
        var searchItemBarcode = m.searchItemBcd.val().trim();
        if (searchItemCode==''&&searchItemBarcode=='') {
            trList.show();
        } else {
            for (let i = 0; i < dataForm.length; i++) {
                let data = dataForm[i];
                if (data.articleId.indexOf(searchItemCode)===-1||data.barcode.indexOf(searchItemBarcode)===-1) {
                    $(trList[i]).hide();
                }
            }
        }
    };

    var clearInput = function () {
        $('#barcode').val('');
        $('#articleId').val('');
        $('#article_clear').click();
        $('#uom').val('');
        $('#qty').val('');
        $('#inventoryQty').val('');
        $('#lastUpdateTime').val('');
        $('#note').val('');
    }

    // 获得选中的数据
    var getSelectVal = function() {
        if (!selectTrTemp) {
            return null;
        }
        let obj = {
            'barcode': $($(selectTrTemp[0]).find('td')[0]).text(),
            'articleId': $($(selectTrTemp[0]).find('td')[1]).text(),
            'articleName': $($(selectTrTemp[0]).find('td')[2]).text(),
            'uom': $($(selectTrTemp[0]).find('td')[3]).text(),
            'qty': $($(selectTrTemp[0]).find('td')[4]).text(),
            'stockQty': $($(selectTrTemp[0]).find('td')[5]).text(),
            // 'lastUpdateTime': $($(selectTrTemp[0]).find('td')[7]).text(),
            // 'note': $($(selectTrTemp[0]).find('td')[8]).text(),
        }
        // let obj = {
        //     'barcode': $($(selectTrTemp[0]).find('td')[1]).text(),
        //     'articleId': $($(selectTrTemp[0]).find('td')[2]).text(),
        //     'articleName': $($(selectTrTemp[0]).find('td')[3]).text(),
        //     'uom': $($(selectTrTemp[0]).find('td')[4]).text(),
        //     'qty': $($(selectTrTemp[0]).find('td')[5]).text(),
        //     'stockQty': $($(selectTrTemp[0]).find('td')[6]).text(),
        //     'lastUpdateTime': $($(selectTrTemp[0]).find('td')[7]).text(),
        //     'note': $($(selectTrTemp[0]).find('td')[8]).text(),
        // }
        return obj;
    }

    // 在业务日期查询商品库存数量
    var getStock = function(storeCd, itemId){
        $.myAjaxs({
            url:systemPath+"/inventoryVoucher/getStock",
            async:true,
            cache:false,
            type :"post",
            data :"storeCd="+storeCd+"&itemId="+itemId,
            dataType:"json",
            success: function (result) {
                if(result.success){
                    m.inventoryQty.val(toThousands(parseInt(result.o.realtimeQty)));
                }else{
                    _common.prompt(result.msg, 3, "error");
                }
            },
            error : function(e){
                _common.prompt("Query inventory quantity error!",3,"error");
            }
        });
    }

    var getData = function (piCd) {
        if (!piCd) {
            clearAll();
            return;
        }
        m.piCd.val(piCd);
        $("#create_user").val(m.create_By.val());
        $("#create_ymd").val(m.piDateParam.val());
        $.myAutomatic.setValueTemp(a_store,m.ParamstoreCd.val(),m.ParamstoreCd.val()+' '+m.ParamstoreName.val());//赋值
        $.myAjaxs({
            url: url_left + "/getData",
            async: true,
            cache: false,
            type: "get",
            data: "piCd=" + piCd+"&storeCd="+$("#store").attr("k")+"&createUser="+$("#create_user").val()+"&createYmd="+$("#create_ymd").val(),
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    dataForm = [];
                    // $("#piCd").val(record.piCd);
                    // $("#create_ymd").val(_common.formatCreateDate(record.createYmd));
                    // $("#create_user").val(record.createUserName);
                    $("#remarks").val(record.remarks);
                    m.i_storeCd.val(record.storeCd);
                    // $.myAutomatic.setValueTemp(a_store, record.storeCd, record.storeName);//赋值
                    dataForm=record.itemList;
                    // 封装明细数据
                    var trList = $("#zgGridTtable  tr:not(:first)");
                    trList.remove();
                    dataForm.forEach(function (item) {
                        var rowindex = 0;
                        var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
                        if(trId!=null&&trId!=''){
                            rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
                        }
                        item.rowIndex=uuid();
                        var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
                        var html = '<tr data-index="'+item.rowIndex+'">' +
                            // '<td tag="#" width="50" title="'+num+'" align="center" tdindex="zgGridTtable_num">'+num+'</td>'+
                            '<td tag="barcode" width="130" title="'+item.barcode+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
                            '<td tag="articleId" width="130" title="'+item.articleId+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
                            '<td tag="articleName" width="130" title="'+item.articleName+'" align="left" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
                            '<td tag="uom" width="80" title="'+item.uom+'" align="left" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+item.uom+'</td>' +
                            '<td tag="qty" width="80" title="'+toThousands(item.qty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_qty" tdindex="zgGridTtable_qty">'+toThousands(item.qty)+'</td>' +
                            '<td tag="stockQty" width="80" title="'+toThousands(item.stockQty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_stockQty" tdindex="zgGridTtable_stockQty">'+toThousands(item.stockQty)+'</td>' +
                            '<td tag="reason" width="80" title="'+item.reason+'" align="right" id="zgGridTtable_'+rowindex+'_tr_stockQty" tdindex="zgGridTtable_reason">'+item.reason+'</td>' +
                            '<td class="hide" tag="fnStockQty" width="130" title="'+toThousands(item.fnStockQty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_fnStockQty" tdindex="zgGridTtable_fnStockQty">'+toThousands(item.fnStockQty)+'</td>' +
                            // '<td tag="lastUpdateTime" width="130" title="'+formatDateAndTime(item.lastUpdateTime)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_lastUpdateTime" tdindex="zgGridTtable_lastUpdateTime">'+formatDateAndTime(item.lastUpdateTime)+'</td>' +
                            // '<td tag="note" width="130" title="'+isEmpty(item.note)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_note" tdindex="zgGridTtable_note">'+isEmpty(item.note)+'</td>' +
                            '</tr>';
                        item.lastUpdateTime=formatDateAndTime(item.lastUpdateTime);
                        tableGrid.append(html);
                    });
                }
            }
        });
    };
    var getDataIn = function (piCd) {
        if (!piCd) {
            clearAll();
            return;
        }
        let articleId=m.searchItemInp.val();
        let barcode=m.searchItemBcd.val();
        m.piCd.val(piCd);
        $("#create_user").val(m.create_By.val());
        $("#create_ymd").val(m.piDateParam.val());

        $.myAutomatic.setValueTemp(a_store,m.ParamstoreCd.val(),m.ParamstoreCd.val()+' '+m.ParamstoreName.val());//赋值
        $.myAjaxs({
            url: url_left + "/getInData",
            async: true,
            cache: false,
            type: "get",
            data: "piCd=" + piCd+"&storeCd="+$("#store").attr("k")+"&createUser="+$("#create_user").val()+"&createYmd="+$("#create_ymd").val()+"&articleId="+articleId+"&barcode="+barcode,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    dataForm = [];
                    // $("#piCd").val(record.piCd);
                    // $("#create_ymd").val(_common.formatCreateDate(record.createYmd));
                    // $("#create_user").val(record.createUserName);
                    $("#remarks").val(record.remarks);
                    m.i_storeCd.val(record.storeCd);
                    // $.myAutomatic.setValueTemp(a_store, record.storeCd, record.storeName);//赋值
                    dataForm=record.itemList;
                    // 封装明细数据
                    var trList = $("#zgGridTtable  tr:not(:first)");
                    trList.remove();
                    dataForm.forEach(function (item) {
                        var rowindex = 0;
                        var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
                        if(trId!=null&&trId!=''){
                            rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
                        }
                        item.rowIndex=uuid();
                        var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
                        var html = '<tr data-index="'+item.rowIndex+'">' +
                            // '<td tag="#" width="50" title="'+num+'" align="center" tdindex="zgGridTtable_num">'+num+'</td>'+
                            '<td tag="barcode" width="130" title="'+item.barcode+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
                            '<td tag="articleId" width="130" title="'+item.articleId+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
                            '<td tag="articleName" width="130" title="'+item.articleName+'" align="left" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
                            '<td tag="uom" width="80" title="'+item.uom+'" align="left" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+item.uom+'</td>' +
                            '<td tag="qty" width="80" title="'+toThousands(item.qty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_qty" tdindex="zgGridTtable_qty">'+toThousands(item.qty)+'</td>' +
                            '<td tag="stockQty" width="80" title="'+toThousands(item.stockQty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_stockQty" tdindex="zgGridTtable_stockQty">'+toThousands(item.stockQty)+'</td>' +
                            '<td tag="reason" width="80" title="'+item.reason+'" align="right" id="zgGridTtable_'+rowindex+'_tr_stockQty" tdindex="zgGridTtable_reason">'+item.reason+'</td>' +
                            // '<td tag="lastUpdateTime" width="130" title="'+formatDateAndTime(item.lastUpdateTime)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_lastUpdateTime" tdindex="zgGridTtable_lastUpdateTime">'+formatDateAndTime(item.lastUpdateTime)+'</td>' +
                            // '<td tag="note" width="130" title="'+isEmpty(item.note)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_note" tdindex="zgGridTtable_note">'+isEmpty(item.note)+'</td>' +
                            '</tr>';
                        item.lastUpdateTime=formatDateAndTime(item.lastUpdateTime);
                        tableGrid.append(html);
                    });
                }
            }
        });
    };
    // 清空值
    var clearAll = function () {
        $("#piCd").val('');
        $("#store").val('');
        $("#create_ymd").val('');
        $("#create_user").val('');
        $("#remarks").val('');
    }

    // 设置为查看模式
    var setDisable = function (flag) {
        $("#store").prop("disabled", flag);
        $("#remarks").prop("disabled", flag);
        $("#saveBut").prop("disabled", flag);
        $("#importFiles").prop("disabled", flag);
        $("#addPlanDetails").prop("disabled", flag);
        $("#updatePlanDetails").prop("disabled", flag);
        $("#deletePlanDetails").prop("disabled", flag);
        if(flag){
            $("#refresh").hide();
            $("#clear").hide();
        }
    }

    //初始化店铺下拉
    var initAutoMatic = function () {
        a_store = $("#store").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function () {
                $.myAutomatic.cleanSelectObj(a_article);
            },
            selectEleClick: function (thisObj) {
                let _storeCd = "&storeCd=" + thisObj.attr("k");
                m.i_storeCd.val(thisObj.attr("k"));
                if(thisObj.attr("k") != null && thisObj.attr("k") !== ""){
                    // $.myAutomatic.replaceParam(a_article,_storeCd);
                    $.myAutomatic.replaceParam(a_article);
                }
            }
        });
        //商品选择
        a_article = $("#a_article").myAutomatic({
            url: systemPath + "/ma1100/getArticleAll",
            param: [{
                'k': 'storeCd',
                'v': 'i_storeCd'
            }],
            ePageSize: 10,
            startCount: 3,

            cleanInput: function (thisObj) {
                $('#barcode').val('');
                $('#articleId').val('');
                $('#uom').val('');
                $('#qty').val('');
                $('#inventoryQty').val('');
                $('#lastUpdateTime').val('');
            },
            selectEleClick: function (thisObj) {
                let _storeCd = $("#store").attr('k');
                let itemId = thisObj.attr("k");
                // 点击iten code 搜索商品
                let articleId = thisObj.attr("k");
                if (!articleId) {
                    _common.prompt("Please select item", 5, "info");/*请选择商品*/
                    clearInput();
                    return;
                }
                $.myAjaxs({
                    url: systemPath + "/fsInventoryEntry/getItemInfo",
                    async: true,
                    cache: false,
                    type: "get",
                    data: 'itemCode='+articleId+'&storeCd='+_storeCd,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            var record = result.o;
                            $('#barcode').val(record.barcode);
                            $('#articleId').val(record.articleId);
                            $('#uom').val(record.uom);
                            $('#qty').val(0);
                            // 业务日期查询实时库存
                            getStock(_storeCd, itemId);
                        } else {
                            clearInput();
                            _common.prompt('No relevant product information was found!',5,"error");/*查询不到商品*/
                            return false;
                        }
                    }
                });
            }
        });
    };

    // 20200716104908 --> 16/07/2020 10:49:08
    var formatDateAndTime = function (str) {
        if (str.length!=14) {
            return '';
        }
        let year = str.substring(0,4);
        let month = str.substring(4,6);
        let day = str.substring(6,8);
        let hour = str.substring(8,10);
        let minute = str.substring(10,12);
        let second = str.substring(12,14);
        return day+"/"+month+"/"+year+" "+hour+":"+minute+":"+second;
    }

    //验证检索项是否合法
    var verifySearch = function () {
        let articleId = $('#articleId').val();
        let qty = reThousands($('#qty').val());
        let articleName = $('#a_article').val();
        if(articleName==null||$.trim(articleName)==''){
            $("#a_article").css("border-color","red");
            _common.prompt("Article Name cannot be empty!",5,"error");
            $("#a_article").focus();
            return false;
        }else {
            $("#a_article").css("border-color","#CCCCCC");
        }
        if (articleId==null||articleId=='') {
            return false;
        }
        if (qty==null||qty=='') {
            $("#qty").css("border-color","red");
            _common.prompt("Qty cannot be empty!",5,"error");
            $("#qty").focus();
            return false;
        }else if(qty == '0'){
            $("#qty").css("border-color","red");
            _common.prompt("Qty cannot be 0!",5,"error");
            $("#qty").focus();
            return false;
        }else if(reThousands(qty) > 9999999999999){
            $("#qty").css("border-color","red");
            _common.prompt("Qty cannot be greater more than 9999999999999!",5,"error");
            $("#qty").focus();
            return false;
        }else {
            var reg = /^[0-9]*$/;
            var fractions = /^\d+\.\d+$/; // 验证是否为小数
            if(!reg.test(reThousands(qty)) && !fractions.test(reThousands(qty))){
                $("#qty").focus();
                $("#qty").css("border-color","red");
                _common.prompt("Qty must be a pure number!",5,"error");/*必须是纯数字*/
                $("#qty").focus();
                return false;
            }
            $("#qty").css("border-color","#CCCCCC");
        }
        return true;
    }

    //表格初始化-费用登录样式
    var initTable1 = function () {lastUpdateTime
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Details",
            param: paramGrid,
            colNames: ["Item Barcode", "Item Code", "Item Name", "UOM", "Qty","Inventory Qty","Reason Code","Reason","fnStockQty"],
            colModel: [
                {
                    name: "barcode",
                    type: "text",
                    text: "center",
                    width: "130",
                    ishide: false
                },
                {
                    name: "articleId",
                    type: "text",
                    text: "center",
                    width: "130",
                    ishide: false,
                    css: ""
                },
                {
                    name: "articleName",
                    type: "text",
                    text: "left",
                    width: "130",
                    ishide: false,
                    css: ""
                },
                {
                    name: "uom",
                    type: "text",
                    text: "left",
                    width: "80",
                    ishide: false,
                    css: ""
                },
                {
                    name: "qty",
                    type: "text",
                    text: "right",
                    width: "80",
                    ishide: false,
                    // getCustomValue:getThousands
                },
                {
                    name: "stockQty",
                    type: "text",
                    text: "right",
                    width: "80",
                    ishide: false,
                    getCustomValue:getThousands
                },
                {
                    name: "reasonCode",
                    type: "text",
                    text: "right",
                    width: "80",
                    ishide: true,
                },
                {
                    name: "reason",
                    type: "text",
                    text: "right",
                    width: "80",
                    ishide: false,
                },
                {
                    name: "fnStockQty",
                    type: "text",
                    text: "right",
                    width: "130",
                    ishide: true,
                    getCustomValue:getThousands
                }
            ],//列内容
            traverseData: dataForm,
            width: "max",//宽度自动
            page: 1,//当前页
            height: 300,
            rowPerPage: 10,//每页数据量
            isPage: false,//是否需要分页
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                if (!m.enterFlag.val()){
                    tableGrid.find("tr").on('dblclick', function (e) {
                        addInputValue();
                    });
                    return self;
                }
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                // {
                //     butType: "add",
                //     butId: "addPlanDetails",
                //     butText: "Add",
                //     butSize: ""
                // },
                {
                    butType: "update",
                    butId: "updatePlanDetails",
                    butText: "Modify",
                    butSize: ""
                },
                // {
                //     butType: "upload",
                //     butId: "importFiles",
                //     butText: "Import Result",
                //     butSize: ""
                // },
                /*{
                    butType: "delete",
                    butId: "deletePlanDetails",
                    butText: "Delete",
                    butSize: ""
                }*/
            ],
        });
    }

    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }

    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        if(date!=null&&date!='')
            res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }
    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };

    // 去除千分位
    function reThousands(num) {
        num+='';
        return num.replace(/,/g, '');
    }

    function getThousands(tdObj, value) {
        var num = toThousands(value);
        // 省略小数点后为0的数
        return $(tdObj).text(num);
    }

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num) {
        var dit = 0;
        if (!num) {
            return '0';
        }
        if(num !== ""){
            var str = num + "";
            if (str.indexOf(".") !== -1) {
                dit = num.toString().split(".")[1].length;
            }
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
var _index = require('custDataEntry');
_start.load(function (_common) {
    _index.init(_common);
});
