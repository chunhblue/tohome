require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('fsInventoryDataEntry', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        file = $('<input type="file"/>'),
        tempTrObjValue = {},//临时行数据存储
        dataForm = [],
        systemPath = null,
        subFlag='';
    const KEY = 'FS_STOCK_MANAGEMENT';
    var m = {
        toKen: null,
        use: null,
        up: null,
        entiretyBmStatus: null,
        item_name: null,
        articleId: null,
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
        cancel: null,
        remarks: null,
        inventoryQty: null, // 实时库存
        piCd: null, // FS库存单号
        search_item_but: null,
        tempTableType: null,
        searchJson: null,
        enterFlag: null,
        piCdParam: null,
        searchItemInp: null, // 商品定位输入框
        searchItemBtn: null, // 商品定位按钮
        i_storeCd:null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/fsInventoryEntry";
        systemPath = _common.config.surl;
        but_event();
        // 初始化store下拉
        initAutoMatic();
        $("#create_ymd").prop("disabled", true);
        $("#create_user").prop("disabled", true);
        initTable1();
        //表格内按钮事件
        table_event();
        if (m.enterFlag.val()) {
            // 获取数据
            getData(m.piCdParam.val());
            if (m.enterFlag.val() == 'view') {
                setDisable(true);
                // $("#saveBut").prop("disabled", false);
                // $("#addPlanDetails").prop("disabled", false);
                // $("#updatePlanDetails").prop("disabled", false);
                // $("#deletePlanDetails").prop("disabled", false);
                // $("#remarks").prop("disabled", false);
            }
        }
    }


    var table_event = function () {
        // submit 按钮
        $('#affirm').on('click', function () {
            if (!verifySearch()) {
                return;
            }
            _common.myConfirm("Are you sure you want to save?", function (result) {
                if (result != "true") {
                    return false;
                }
                let barcode = $('#barcode').val();
                let articleId = $('#a_article').attr("k");
                let articleName = $('#a_article').attr("v");
                let uom = $('#uom').val();
                let qty = $('#qty').val();
                let stockQty = $('#inventoryQty').val();
                let lastUpdateTime = new Date().Format("dd/MM/yyyy hh:mm:ss");
                let note = $('#note').val();

                var _addFlg = false;
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var _articleId = $(this).find('td[tag=articleId]').text();
                    if (_articleId == articleId) {
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
                    if (trId != null && trId != '') {
                        rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
                    }
                    var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
                    var html = '<tr data-index="' + obj.rowIndex + '">' +
                        '<td tag="num" width="50" title="' + num + '" align="center" id="zgGridTtable_' + rowindex + '_tr_barcode" tdindex="zgGridTtable_barcode">' + num + '</td>' +
                        '<td tag="barcode" width="130" title="' + barcode + '" align="center" id="zgGridTtable_' + rowindex + '_tr_barcode" tdindex="zgGridTtable_barcode">' + barcode + '</td>' +
                        '<td tag="articleId" width="130" title="' + articleId + '" align="center" id="zgGridTtable_' + rowindex + '_tr_articleId" tdindex="zgGridTtable_articleId">' + articleId + '</td>' +
                        '<td tag="articleName" width="130" title="' + articleName + '" align="left" id="zgGridTtable_' + rowindex + '_tr_articleName" tdindex="zgGridTtable_articleName">' + articleName + '</td>' +
                        '<td tag="uom" width="80" title="' + uom + '" align="left" id="zgGridTtable_' + rowindex + '_tr_uom" tdindex="zgGridTtable_uom">' + uom + '</td>' +
                        '<td tag="qty" width="80" title="' + qty + '" align="right" id="zgGridTtable_' + rowindex + '_tr_qty" tdindex="zgGridTtable_qty">' + qty + '</td>' +
                        '<td tag="stockQty" width="80" title="'+ stockQty +'" align="right" id="zgGridTtable_'+rowindex+'_tr_stockQty" tdindex="zgGridTtable_stockQty">'+stockQty+'</td>'+
                        '<td tag="lastUpdateTime" width="130" title="'+lastUpdateTime+'" align="center" id="zgGridTtable_'+rowindex+'_tr_lastUpdateTime" tdindex="zgGridTtable_lastUpdateTime">'+lastUpdateTime+'</td>' +
                        '<td tag="note" width="130" title="'+isEmpty(note)+'" align="left" id="zgGridTtable_'+rowindex+'_tr_note" tdindex="zgGridTtable_note">'+isEmpty(note)+'</td>' +
                        '</tr>';
                    tableGrid.append(html);
                    _common.prompt("Data added successfully", 3, "success");
                } else if (subFlag == 'update') {

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
                            $(this).find('td[tag=lastUpdateTime]').text(lastUpdateTime);
                            $(this).find('td[tag=note]').text(note);

                            var obj = {
                                'barcode': barcode,
                                'articleId': articleId,
                                'articleName': articleName,
                                'uom': uom,
                                'qty': qty,
                                'lastUpdateTime': lastUpdateTime,
                                'note': note,
                                'rowIndex': selIdx,
                            }

                            for (let i = 0; i < dataForm.length; i++) {
                                let item = dataForm[i];
                                if (selIdx == item.rowIndex) {
                                    dataForm[i] = obj;
                                }
                            }
                            _common.prompt("Data updated successfully", 3, "success");
                        }
                    });
                }

                $('#update_dialog').modal("hide");
            })
        });

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
            clearInput();
            $('#update_dialog').modal("show");
        });
        $("#updatePlanDetails").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
            } else {
                subFlag = 'update';
                let record = getSelectVal();
                $('#update_dialog').modal("show");
                $('#barcode').val(record.barcode);
                $('#a_article').val(record.articleName).attr("k",record.articleId).attr("v",record.articleName);
                $('#articleId').val(record.articleId);
                $('#uom').val(record.uom);
                $('#qty').val(record.qty);
                $('#inventoryQty').val(record.stockQty);
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
                            if (item.rowIndex===selIdx) {
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
            if (bank==='disabled') {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
        });

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
        $("#qty").blur(function () {
            $("#qty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#qty").focus(function(){
            $("#qty").val(reThousands(this.value));
        });

        m.cancel.on("click",function () {
            _common.myConfirm("Are you sure you want to cancel",function(result){
                    $("#a_article").css("border-color","#CCCCCC");
                    $("#qty").css("border-color","#CCCCCC");
                if (result=="true"){
                    $('#update_dialog').modal("hide");
                }
            })
        });

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
                        loading_close()
                    }
                });
            });
        });

        // 保存
        m.saveBut.on('click',function () {
            let piCd = m.piCd.val();
            let remarks = m.remarks.val();
            let storeCd = m.store.attr('k');
            if (!storeCd) {
                _common.prompt("Please enter the Store!",5,"error");/*请录入费用录入商品数据*/
                $('#store').focus();
                $('#store').css("border-color","red");
                return;
            }else {
                $('#store').css("border-color","#CCC");
            }
            if (dataForm.length<1) {
                _common.prompt("Please enter the inventory data!",5,"error");/*请录入FS库存商品数据*/
                return;
            }
            // 头档信息
            let data = {
                'piCd': piCd,
                'remarks': remarks,
                'storeCd': storeCd,
            }
            dataForm.forEach(function (item) {
                item.qty=reThousands(item.qty);
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
                            setDisable(true);
                            m.enterFlag='view';
                            _common.prompt("Data saved successfully！",2,"success");
                        }else{
                            _common.prompt(result.msg,5,"error");
                        }
                    },
                    error:function(e){
                        _common.prompt("Data save failed!",5,"error");/*FS库存商品保存失败*/
                    },
                });
            })
        });

        // 输入商品id定位功能
        m.searchItemBtn.on('click',function () {
            searchItem();
        });
        //FS库存创建日期
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
            if ($(this).val() === "") {
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
    };

    var loading_close = function () {
        $(".loading-mask-div").fadeOut(100,function(){
            $(".init-loading-box").fadeOut(200);
        });
    };

    // 保存导入的商品到表格里
    var addItemList = function (list) {
        if (!list) {
            return;
        }
        let lastUpdateTime = new Date().Format("dd/MM/yyyy hh:mm:ss");
        $("#zgGridTtable  tr:not(:first)").remove();
        dataForm=[];
        var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
        var i =-1;
        list.forEach(function (item) {
            item.rowIndex=uuid();
            item.lastUpdateTime=lastUpdateTime;
            if(i<list.length){
                i=i+1
            }
            var html = '<tr data-index="'+item.rowIndex+'">' +
                '<td tag="#" width="50" title="'+(num+i)+'" align="center" tdindex="zgGridTtable_num">'+(num+i)+'</td>' +
                '<td tag="barcode" width="130" title="'+item.barcode+'" align="center" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
                '<td tag="articleId" width="130" title="'+item.articleId+'" align="center" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
                '<td tag="articleName" width="130" title="'+item.articleName+'" align="left" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
                '<td tag="uom" width="80" title="'+item.uom+'" align="left" tdindex="zgGridTtable_uom">'+item.uom+'</td>' +
                '<td tag="qty" width="80" title="'+toThousands(item.qty)+'" align="right" tdindex="zgGridTtable_qty">'+toThousands(item.qty)+'</td>' +
                '<td tag="stockQty" width="80" title="'+toThousands(item.stockQty)+'" align="right"  tdindex="zgGridTtable_stockQty">'+toThousands(item.stockQty)+'</td>'+
                '<td tag="lastUpdateTime" width="130" title="'+lastUpdateTime+'" align="right"  tdindex="zgGridTtable_lastUpdateTime">'+lastUpdateTime+'</td>'+
                '<td tag="note" width="130" title="'+isEmpty(item.note)+'" align="left" tdindex="zgGridTtable_note">'+isEmpty(item.note)+'</td>' +
                '</tr>';
            tableGrid.append(html);
            dataForm.push(item);
        });
        loading_close();
    };

    var searchItem = function () {
        // 还没有添加商品
        if (dataForm.length<1) {
            return;
        }
        var trList = $("#zgGridTtable  tr:not(:first)");
        trList.show();
        var searchItemCode = m.searchItemInp.val().trim();
        if (searchItemCode==='') {
            trList.show();
        } else {
            for (let i = 0; i < dataForm.length; i++) {
                let data = dataForm[i];
                if (data.articleId.indexOf(searchItemCode)===-1) {
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
        $('#lastUpdateTime').val('');
        $('#note').val('');
    }

    // 获得选中的数据
    var getSelectVal = function() {
        if (!selectTrTemp) {
            return null;
        }
        let obj = {
            'barcode': $($(selectTrTemp[0]).find('td')[1]).text(),
            'articleId': $($(selectTrTemp[0]).find('td')[2]).text(),
            'articleName': $($(selectTrTemp[0]).find('td')[3]).text(),
            'uom': $($(selectTrTemp[0]).find('td')[4]).text(),
            'qty': $($(selectTrTemp[0]).find('td')[5]).text(),
            'stockQty': $($(selectTrTemp[0]).find('td')[6]).text(),
            'lastUpdateTime': $($(selectTrTemp[0]).find('td')[7]).text(),
            'note': $($(selectTrTemp[0]).find('td')[8]).text(),
        }
        return obj;
    }

    var getData = function (piCd) {
        if (!piCd) {
            clearAll();
            return;
        }

        $.myAjaxs({
            url: url_left + "/getData",
            async: true,
            cache: false,
            type: "get",
            data: "piCd=" + piCd,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    dataForm = [];
                    $("#piCd").val(record.piCd);
                    $("#create_ymd").val(_common.formatCreateDate(record.createYmd));
                    $("#create_user").val(record.createUserName);
                    $("#remarks").val(record.remarks);
                    m.i_storeCd.val(record.storeCd);
                    $.myAutomatic.setValueTemp(a_store, record.storeCd, record.storeName);//赋值
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
                        var num = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
                        item.rowIndex=uuid();
                        var html = '<tr data-index="'+item.rowIndex+'">' +
                            '<td tag="#" width="50" title="'+num+'" align="center" id="zgGridTtable_'+rowindex+'_tr_num" tdindex="zgGridTtable_num">'+num+'</td>' +
                            '<td tag="barcode" width="130" title="'+item.barcode+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
                            '<td tag="articleId" width="130" title="'+item.articleId+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
                            '<td tag="articleName" width="130" title="'+item.articleName+'" align="left" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
                            '<td tag="uom" width="80" title="'+item.uom+'" align="left" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+item.uom+'</td>' +
                            '<td tag="qty" width="80" title="'+toThousands(item.qty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_qty" tdindex="zgGridTtable_qty">'+toThousands(item.qty)+'</td>' +
                            '<td tag="stockQty" width="80" title="'+toThousands(item.stockQty)+'" align="right" id="zgGridTtable_'+rowindex+'_tr_stockQty" tdindex="zgGridTtable_stockQty">'+toThousands(item.stockQty)+'</td>' +
                            '<td tag="lastUpdateTime" width="130" title="'+formatDateAndTime(item.lastUpdateTime)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_lastUpdateTime" tdindex="zgGridTtable_lastUpdateTime">'+formatDateAndTime(item.lastUpdateTime)+'</td>' +
                            '<td tag="note" width="120" title="'+isEmpty(item.note)+'" align="left" id="zgGridTtable_'+rowindex+'_tr_note" tdindex="zgGridTtable_note">'+isEmpty(item.note)+'</td>' +
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
        $("#addPlanDetails").prop("disabled", flag);
        $("#importFiles").prop("disabled", flag);
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
                    $.myAutomatic.replaceParam(a_article);
                }
            }
        });
        //商品选择
        a_article = $("#a_article").myAutomatic({
            url: systemPath + "/ma1100/getArticleAll",
            ePageSize: 10,
            startCount: 3,
            param: [{
                'k': 'storeCd',
                'v': 'i_storeCd'
            }],
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
    };


    //验证检索项是否合法
    var verifySearch = function () {
        let articleId = $('#articleId').val();
        let qty = $('#qty').val().trim();
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
                return false;
            }
            $("#qty").css("border-color","#CCCCCC");
        }
        return true;
    }

    //表格初始化-fs库存样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Details",
            param: paramGrid,
            colNames: ["Item Barcode", "Item Code", "Item Name", "UOM", "FS Qty","Inventory Qty","Last Update Time","Notes"],
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
                    getCustomValue:getThousands
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
                    name: "lastUpdateTime",
                    type: "text",
                    text: "left",
                    width: "130",
                    ishide: false,
                    css: "",
                    getCustomValue:formatDate
                },
                {
                    name: "note",
                    type: "text",
                    text: "left",
                    width: "120",
                    ishide: false,
                    css: ""
                }
            ],//列内容
            lineNumber:true,
            traverseData: dataForm,
            height: 300,
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
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
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {
                    butType: "add",
                    butId: "addPlanDetails",
                    butText: "Add",
                    butSize: ""
                },
                {
                    butType: "update",
                    butId: "updatePlanDetails",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType: "upload",
                    butId: "importFiles",
                    butText: "Import Result",
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

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
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

    //格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }

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
var _index = require('fsInventoryDataEntry');
_start.load(function (_common) {
    _index.init(_common);
});
