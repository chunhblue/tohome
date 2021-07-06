require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('cashierAmount', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        paramGrid1 = null,
        selectTrTemp = null,
        selectTrTemp1 = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        a_store = null,
        a_cashier = null,
        dateStr = null,
        dateTime = null,
        _sts = null,
        submitFlag = false,
        tempTrObjValue = {},//临时行数据存储
        num = 1,
        common = null;
    const KEY = 'CASHIER_SHIFT_REPORT_QUERY';
    var m = {
        toKen: null,
        use: null,
        up: null,
        payList:null,
        viewSts: null,
        bs_date: null,
        addBut: null,
        submitBut: null,
        payId: null,
        a_cashier: null,
        staffId: null,//员工id
        staffName: null,//员工名称
        payAmtFlg: null,//差异类型
        payAmtDiff: null,//差异金额
        cash_split_flag: null,//现金是否拆分
        shift: null,//班次
        posId: null,//posId
        businessDate: null,//业务日期
        cancelByPay: null,
        affirmByPay: null,
        cancelByCash: null,
        affirmByCash: null,
        alert_div: null,//页面提示框
        search: null,//检索按钮
        reset: null,//清空按钮
        main_box: null,//检索按钮
        returnsViewBut: null,//返回按钮
        searchJson: null,
        checkCount: null,
        del_alert: null,
        submissionDate:null,
        // 头档传入信息
        storeCd1: null,
        storeName1: null,
        payDate: null,
        userName: null,
        userId: null,
        expense: null,
        submitter:null,
        remark: null,
        userName1:null,
        submitter1:null,
        submissionDate1:null,
        storeCd2:null,
        shift1: null,//班次
        posId1: null,//posId
    };
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/cashierAmount";
        reThousands = _common.reThousands;
        toThousands = _common.toThousands;
        getThousands = _common.getThousands;
        // 画面按钮点击事件
        but_event();

        // 初始化表格
        initTable1();
        initTable2();
        // 事件绑定
        table_but();
        // 根据跳转加载数据，设置操作模式
        setValueByType();
        // 初始化下拉
        initAutoMatic();
        $("#updateByPay").hide();
    }

    // 根据跳转方式，设置画面可否编辑、加载内容
    var setValueByType = function () {
        _sts = m.viewSts.val();
        if (_sts == "update") {
            //加载支付类型
            tocashInit();
            togetPay();
            topayTypeInit();
            btnDisable(false);
            $('#a_store_refresh').prop('disabled', true);
            $('#a_store_clear').prop('disabled', true);
            $('#search').prop('disabled', true);
            $('#reset').prop('disabled', true);
            inputDisable(true);
        } else if (_sts == "view") {
            //12 17
            // tableGrid.showColumn('payAmt');
            // tableGrid.showColumn('payAmtDiff');
           // topayTypeInit();
            tocashInit();
            togetPay();
           // topayTypeInit();
            $('#a_store_refresh').prop('disabled', true);
            $('#a_store_clear').prop('disabled', true);
            $('#search').prop('disabled', true);
            $('#reset').prop('disabled', true);
            btnDisable(true);
            inputDisable(true);
        } else {
            m.submissionDate.hide();
            m.submissionDate1.hide();
            m.submitter.hide();
            m.submitter1.hide();
            // var format = new Date().Format("dd/MM/yyyy hh:mm:ss.S");
            // m.submissionDate.val(format);
            // m.submitter.val(m.userName1.val());
            // 设置业务日期
            setBusinessDate();
            // 初始化表格数据
            payTypeInit();
            cashInit();
            btnDisable(true);
        }
    }

    var topayTypeInit = function () {
        var payString =[],  payListA="", payListEx="";
          $("input[name='resexpend']").each(function () {
                  payListEx=payListEx+","+$(this).val();
                  var  expendString={
                      storeCd: $("#a_store").attr("k"),
                      payDate: subfmtDate(m.bs_date.val()),
                      cashSplitFlag: $("input[name='cash_split_flag']:checked").val(),
                      voucherNo:$(this).val()
                  }
                  payString.push(expendString);
              });
        payListA=JSON.stringify(payString);
        $("#from_hide_expend").val(payListEx);
        //拼接检索参数
         setParamJson();
        paramGrid = "searchJson=" +m.searchJson.val()+"&payList="+payListA;
        tableGrid.setting("url", url_left + "/getPayList");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData();
    }

    var tocashInit = function () {
        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson=" + m.searchJson.val();
        tableGrid1.setting("url", url_left + "/getCashList");
        tableGrid1.setting("param", paramGrid);
        tableGrid1.setting("page", 1);
        tableGrid1.loadData();
    }

    var togetPay = function () {
        setParamJson();
        let param = "searchJson=" + m.searchJson.val();
        $.myAjaxs({
            url: url_left + "/getCashDetail",
            async: true,
            cache: false,
            type: "post",
            data: param,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    $.myAutomatic.setValueTemp(a_store, result.data.storeCd, result.data.storeName);
                    // m.submissionDate();
                    checkUserName(result.data.createUserId);
                    makeUpTime(result.data.createYmd,result.data.createHms);
                    // $.myAutomatic.setValueTemp(expenditureNo, result.data.voucherNo, result.data.voucherNo);
                   $('#expenditureNoA').hide();
                    for (let i = 0; i <result.data.voucherNo.length; i++) {
                        if (result.data.voucherNoList[i]!=null &&  result.data.voucherNoList[i]!=""){
                            var voNo=result.data.voucherNoList[i];
                            let accDate = result.data.payDate;
                            var resourceGroup = "<div class='row' name='resExpendRow' >" +
                                "                <label for='expenditureNo' class='col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label' style='white-space: nowrap'>Expenditure No.</label>" +
                                "                <div class='col-xs-12 col-sm-4 col-md-3 col-lg-2'>" +
                                "                    <div class='res-info' style='margin-left:10px'>" +voNo+ "</div>" +
                                "                    <input type='hidden' class='form-control input-sm'" +
                                "                           name='resexpend' value='" + voNo + "'>" +
                                "                </div>" +
                                "                <label for='description'  class='col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label' style='white-space: nowrap;'>Expenditure Description</label>" +
                                "                <div class='col-xs-12 col-sm-4 col-md-3 col-lg-2'>" +
                                "                    <div class='res-info'  id="+voNo+" name='descriptionFor'></div>" +
                                "                    <input  name='descriptionForInput' class='form-control input-sm'" +
                                "                          value='" +  + "'>" +
                                "                </div>" ;
                            $("#expendlitLine").before(resourceGroup);
                            getDescription(voNo,$("#a_store").attr("k"),accDate);
                        }


                    }
                    topayTypeInit();
                    m.bs_date.val(fmtIntDate(result.data.payDate));
                    m.remark.val(result.data.remark);
                    /*if (result.data.cashSplitFlag == '1') {
                        $("#cash_split_flag_1").prop("checked", true);
                        //显示Banknotes Entry表格
                        $("#zgGridTtable1Div").show();
                    } else {
                        $("#cash_split_flag_2").prop("checked", true);
                        //隐藏Banknotes Entry表格
                        $("#zgGridTtable1Div").hide();
                    }*/
                } else {
                    _common.prompt("result failed!", 5, "error");
                }
            },
        });
    }

    //拼接检索参数
    var setParamJson = function () {

        // 创建请求字符串
        let searchJsonStr = {
            storeCd: $("#a_store").attr("k"),
            payDate: subfmtDate(m.bs_date.val()),
            cashSplitFlag: $("input[name='cash_split_flag']:checked").val(),
             voucherNo:$("#expenditureNo").val()
            // voucherNo:$("#expenditureNo").attr("k")
};
        if (m.viewSts.val() !== null && $.trim(m.viewSts.val()) !== "") {
            searchJsonStr.payDate = m.payDate.val();
            searchJsonStr.payId = m.payId.val();
            searchJsonStr.storeCd = m.storeCd1.val();
        }
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }
    var checkUserName=function (data) {
        $.myAjaxs({
            url: systemPath+"/inventoryVoucher/getuser",
            async: true,
            cache: false,
            type: "post",
            data :"userId="+data,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let user = result.o;
                    m.submitter.val(user);
                }
            }
        });
    }
    var makeUpTime=function (date,time) {

         var year=date.substring(0,4);
        var month=date.substring(4,6);
        var day=date.substring(6,8);
        var hour=time.substring(0,2);
        var min=time.substring(2,4);
        var sec=time.substring(4,6);
        var timeStr=day+"/"+month+"/"+year+" "+hour+":"+min+":"+sec;
        m.submissionDate.val(timeStr);
    }
    //初始化下拉
    var initAutoMatic = function () {

        m.bs_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true,
            endDate:new Date(dateTime)
        });

        $("#expenditureNo").on("focus",function () {
            let accDate = subfmtDate(m.bs_date.val());
            let storeCd =  $("#a_store").attr("k");
            $.myAutomatic.replaceParam(expenditureNo,"&accDate="+accDate+"&storeCd="+storeCd);
        })

        $("#expenditureNo").prop("disabled",true);
        $("#expenditureNo_refresh").hide();
        $("#expenditureNo_clear").hide();
        $("#expenditureDescription").hide();
        $("#addexpenditureNo").addClass("disabled");
        initSelectOptions("Difference Reason", "differenceReason", "00390");
        //隐藏下拉
        $('#differenceReason').parent().hide();
        //店铺
        a_store = $("#a_store").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            async: false,
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
                $.myAutomatic.cleanSelectObj(expenditureNo);
                $("#expenditureNo").prop("disabled",true);
                // $("#addexpenditureNo").attr("disabled",true);
                $("#addexpenditureNo").addClass("disabled");
                $("#expenditureNo_refresh").hide();
                $("#expenditureNo_clear").hide();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    let accDate = subfmtDate(m.bs_date.val());
                    let storeCd = thisObj.attr("k");
                    $.myAutomatic.replaceParam(expenditureNo,"&accDate="+accDate+"&storeCd="+storeCd);
                    $("#expenditureNo").prop("disabled",false);
                    $("#expenditureNo_refresh").show();
                    $("#expenditureNo_clear").show();
                }
            }
        });

        // 经费管理序号下拉
        expenditureNo = $("#expenditureNo").myAutomatic({
            url: systemPath + "/fundEntry/getExpenditureList",
            // param:[{
            //     'k':'bs_date',
            //     'v':'accDate'
            // },{
            //     'k':'storeCd2',
            //     'v':'storeCd'
            // }],
            async:false,
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
                $("#addexpenditureNo").addClass("disabled");
                $("#expenditureDescription").hide();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    let accDate = subfmtDate(m.bs_date.val());
                    let storeCd = $("#a_store").attr("k");
                    $("#expenditurehide").val($(thisObj).attr("k"));
                    getDescription(thisObj.attr("k"),storeCd,accDate);
                    $("#addexpenditureNo").removeClass("disabled");
                    //2020年11月30日
                    $('div[name="resExpendRow"]').each(function () {
                        $("#expendFlgVal").val(num);
                        ++num;
                       var rinputN= $(this).find("input[name='resexpend']").val();
                        if (rinputN===$(thisObj).attr("k")){
                            $("#addexpenditureNo").addClass("disabled");
                            $(this).find("div[name='res-info']").css("border-color", "red");
                            $("#expenditureNo").css("border-color", "red");
                            $("#expenditureNo").focus();
                            _common.prompt("the select expendNo has been  repeat  !!", 3, "error");
                            return false;
                        }else {
                            $("#expenditureNo").css("border-color", "#CCC");
                            return  true;
                        }
                    });
                    $("#expenditureDescription").show();
                }
            }
        });
    };

    var getDescription = function (expenditureNo,storeCd,accDate) {
        $.myAjaxs({
            url: systemPath + "/fundEntry/getDescription",
            async: false,
            cache: false,
            type: "post",
            data: "voucherNo="+expenditureNo+"&storeCd="+storeCd+"&accDate="+accDate,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    $("#description").val(result.o.description);
                        $('#'+expenditureNo).append('<span>'+result.o.description+'</span>');
                }
            },
            error: function (e) {
                _common.prompt("request failed!", 5, "error");
            },
            complete: _common.myAjaxComplete
        });
    };

    var table_but = function () {
        //返回一览
        m.returnsViewBut.on("click", function () {
            if (m.viewSts.val()=="view"){
                _common.updateBackFlg(KEY);
                top.location = systemPath + "/saleReconciliation";
            }
            var submitbank = $("#returnsSubmitBut").attr("disabled");
            if (m.viewSts.val()!="view" && submitbank !='disabled'){
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?", function (result) {
                            if (result === "true") {
                    _common.updateBackFlg(KEY);
                    top.location = systemPath + "/saleReconciliation";
                }
            });
            }
            // var bank = $("#returnsSubmitBut").attr("disabled");
            // if (submitFlag === false && bank != 'disabled') {
            //     _common.myConfirm("Current change is not submitted yet，are you sure to exit?", function (result) {
            //         if (result === "true") {
            //             _common.updateBackFlg(KEY);
            //             top.location = systemPath + "/saleReconciliation";
            //         }
            //     });
            // }
            // if (submitFlag === true) {
            //     _common.updateBackFlg(KEY);
            //     top.location = systemPath + "/saleReconciliation";
            // }
            // if (submitFlag === false && bank === 'disabled') {
            //     _common.updateBackFlg(KEY);
            //     top.location = systemPath + "/saleReconciliation";
            // }
        });
        $("#updateByPay").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select payment type!", 5, "error");/*请选择支付类型*/
                return false;
            }
            // 指定的行不可以修改
            var cols = tableGrid.getSelectColValue(selectTrTemp,"payCd,payName");
            if(cols['payCd'] === '04' || cols['payCd'] === '05'){
                return false;
            }
            //正在修改的行不能再修改
            if ($("#i_payInAmt").length == 1) {
                return false;
            }
            //获取旧值
            var cols = tableGrid.getSelectColValue(selectTrTemp, "payCd,payName,payAmt,payInAmt,payAmtDiff,differenceReason");
            //添加input框(禁用输入框自动提示)
            tableGrid.find(".info [tag=payInAmt]").text("").append("<input type='text' class='form-control my-automatic input-sm' id='i_payInAmt' placeholder='' autocomplete='off'>");
            tableGrid.find(".info [tag=differenceReason]").text("").append("<select id='i_differenceReason' class='form-control input-sm' autocomplete='off'>" +
                "<option value=''>-- All/Please Select --</option>" +
                "</select>");
            initSelectOptions("Difference Reason", "i_differenceReason", "00390");
            //赋值
            $('#i_payInAmt').val(cols["payInAmt"]);
            $("#i_differenceReason option:contains('" + cols["differenceReason"] + "')").attr("selected", true);
            if (cols["differenceReason"] == "") {
                $("#i_differenceReason").val("");
            }
            $("#i_payInAmt").blur(function () {
                let payInAmt = reThousands(this.value); // 实收金额
                $("#i_payInAmt").val(toThousands(payInAmt));
            });

            //光标进入，去除金额千分位，并去除小数后面多余的0
            $("#i_payInAmt").focus(function () {
                $("#i_payInAmt").val(reThousands(this.value));
            });

        });

        $("#updateByCash").on("click", function () {
            if (selectTrTemp1 == null) {
                _common.prompt("Please select currency!", 5, "error");/*请选择币种*/
                return false;
            }

            //正在修改的行不能再修改
            if ($("#i_shift1Qty").length === 1) {
                return false;
            }
            if ($("#i_shift2Qty").length === 1) {
                return false;
            }
            if ($("#i_shift3Qty").length === 1) {
                return false;
            }

            //获取旧值
            var cols = tableGrid1.getSelectColValue(selectTrTemp1, "cashCd,cashName,shift1,shift2,shift3");
            //添加input框
            tableGrid1.find(".info [tag=shift1]").text("").append("<input type='text' class='form-control my-automatic input-sm' id='i_shift1Qty' placeholder='' autocomplete='off'>");
            tableGrid1.find(".info [tag=shift2]").text("").append("<input type='text' class='form-control my-automatic input-sm' id='i_shift2Qty' placeholder='' autocomplete='off'>");
            tableGrid1.find(".info [tag=shift3]").text("").append("<input type='text' class='form-control my-automatic input-sm' id='i_shift3Qty' placeholder='' autocomplete='off'>");
            //赋值
            $('#i_shift1Qty').val(cols["shift1"]);
            $('#i_shift2Qty').val(cols["shift2"]);
            $('#i_shift3Qty').val(cols["shift3"]);

            $("#i_shift1Qty").blur(function () {
                $("#i_shift1Qty").val(toThousands(this.value));
            });
            $("#i_shift2Qty").blur(function () {
                $("#i_shift2Qty").val(toThousands(this.value));
            });
            $("#i_shift3Qty").blur(function () {
                $("#i_shift3Qty").val(toThousands(this.value));
            });

            //光标进入，去除金额千分位，并去除小数后面多余的0
            $("#i_shift1Qty").focus(function () {
                $("#i_shift1Qty").val(reThousands(this.value));
            });
            $("#i_shift2Qty").focus(function () {
                $("#i_shift2Qty").val(reThousands(this.value));
            });
            $("#i_shift3Qty").focus(function () {
                $("#i_shift3Qty").val(reThousands(this.value));
            });
        });

        //是否有冻结列的标记
        var freeze = tableGrid.getting("freezeIndex");
        //重写选择背景变色事件
        tableGrid.find("tbody").unbind('mousedown').on('mousedown', 'td', function (e) {
            var thisObj = $(this),
                thisParent = $(this).parent();
            if (3 === e.which) { //鼠标右键
                thisParent.addClass("info").siblings().removeClass("info");
                var eachTrRightclickFun = !!$.isFunction(tableGrid.getting("eachTrRightclick"));
                if (eachTrRightclickFun) {
                    tableGrid.getting("eachTrRightclick").call(this, thisParent, thisObj);
                }
                if (freeze >= 0) {
                    var freeToTr = thisParent.prop("id");
                    $("#" + freeToTr + "_free").addClass("info").siblings().removeClass("info");
                }
            } else if (1 === e.which) { //鼠标左键
                //还原input输入框
                var cols = tableGrid.getSelectColValue(thisParent, "payCd,payAmt");
                //还原表格中的input框
                var inputRowPayCd = tableGrid.find(".info [tag=payCd]");
                var payInAmtRow = $("#i_payInAmt");
                if (cols["payCd"] !== inputRowPayCd.text() && payInAmtRow.length === 1) {
                    var payInAmt = reThousands(payInAmtRow.val());
                    //添加判断
                    var reg = /^(\-|\+)?\d+(\.\d+)?$/;  // 正数 负数，小数
                    if (payInAmt !== "" && !reg.test(payInAmt)) {
                        _common.prompt("Please enter the correct paid-in amount!", 5, "error");/*请输入正确实收金额*/
                        $('#i_payInAmt').focus();
                        return false;
                    }
                    var payAmt = reThousands(cols["payAmt"]);
                    // 12
                    var payAmtDiff = payInAmt - payAmt;
                    tableGrid.find(".info [tag=payInAmt]").text(toThousands(payInAmt));
                    tableGrid.find(".info [tag=payAmtDiff]").text(toThousands(payAmtDiff));
                    //合计
                    pay_total();
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
        });
        //是否有冻结列的标记
        freeze = tableGrid1.getting("freezeIndex");
        //重写选择背景变色事件
        tableGrid1.find("tbody").unbind('mousedown').on('mousedown', 'td', function (e) {
            var thisObj = $(this),
                thisParent = $(this).parent();
            if (3 === e.which) { //鼠标右键
                thisParent.addClass("info").siblings().removeClass("info");
                var eachTrRightclickFun = !!$.isFunction(tableGrid1.getting("eachTrRightclick"));
                if (eachTrRightclickFun) {
                    tableGrid1.getting("eachTrRightclick").call(this, thisParent, thisObj);
                }
                if (freeze >= 0) {
                    var freeToTr = thisParent.prop("id");
                    $("#" + freeToTr + "_free").addClass("info").siblings().removeClass("info");
                }
            } else if (1 === e.which) { //鼠标左键
                //还原input输入框
                var cols = tableGrid1.getSelectColValue(thisParent, "cashCd");
                //还原表格中的input框
                var inputRowCashCd = tableGrid1.find(".info [tag=cashCd]");
                var cashQtyRow1 = $("#i_shift1Qty");
                var cashQtyRow2 = $("#i_shift2Qty");
                var cashQtyRow3 = $("#i_shift3Qty");
                if ((cols["cashCd"] !== inputRowCashCd.text() && cashQtyRow1.length === 1)
                || (cols["cashCd"] !== inputRowCashCd.text() && cashQtyRow2.length === 1)
                || (cols["cashCd"] !== inputRowCashCd.text() && cashQtyRow3.length === 1)) {
                    var cashQty1 = reThousands(cashQtyRow1.val());
                    var cashQty2 = reThousands(cashQtyRow2.val());
                    var cashQty3 = reThousands(cashQtyRow3.val());
                    //添加判断
                    var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
                    if (cashQty1 !== "" && !reg.test(cashQty1)) {
                        _common.prompt("Please enter the correct quantity!", 5, "error");/*请输入正确数量*/
                        $('#i_shift1Qty').focus();
                        return false;
                    }
                    if (cashQty2 !== "" && !reg.test(cashQty2)) {
                        _common.prompt("Please enter the correct quantity!", 5, "error");/*请输入正确数量*/
                        $('#i_shift2Qty').focus();
                        return false;
                    }
                    if (cashQty3 !== "" && !reg.test(cashQty3)) {
                        _common.prompt("Please enter the correct quantity!", 5, "error");/*请输入正确数量*/
                        $('#i_shift3Qty').focus();
                        return false;
                    }
                    tableGrid1.find(".info [tag=shift1]").text(toThousands(cashQty1));
                    tableGrid1.find(".info [tag=shift2]").text(toThousands(cashQty2));
                    tableGrid1.find(".info [tag=shift3]").text(toThousands(cashQty3));
                    //赋值
                    $('#i_shift1Qty').val(cols["shift1"]);
                    $('#i_shift2Qty').val(cols["shift2"]);
                    $('#i_shift3Qty').val(cols["shift3"]);
                    //现金合计
                    cash_total();
                }
                //改变背景颜色
                thisParent.addClass("info").siblings().removeClass("info");
                if (freeze >= 0) {
                    var freeToTr = thisParent.prop("id");
                    $("#" + freeToTr + "_free").addClass("info").siblings().removeClass("info");
                }
                $(".zgGrid-modal").hide();
                var eachTrClickFun = !!$.isFunction(tableGrid1.getting("eachTrClick"));
                if (eachTrClickFun) {
                    tableGrid1.getting("eachTrClick").call(this, thisParent, thisObj);
                }
            }
        });
    }
    var btnDisable = function (flag) {
        $('#updateByCash').prop('disabled', flag);
        $('#updateByPay').prop('disabled', flag);
        $('#submitBut').prop('disabled', flag);
    }

    var inputDisable = function (flag) {
        $('#remark').prop('disabled', flag);
        $('#a_store').prop('disabled', flag);
        $('#bs_date').prop('disabled', flag);
        $('#expenditureNo').prop('disabled', flag);
        $('#cash_split_flag_1').prop('disabled', flag);
        $('#cash_split_flag_2').prop('disabled', flag);
        if (flag) {
            $("#expenditureNo_refresh").hide();
            $("#expenditureNo_clear").hide();
            $("#a_store_refresh").hide();
            $("#a_store_clear").hide();
        } else {
            $("#expenditureNo_refresh").show();
            $("#expenditureNo_clear").show();
            $("#a_store_refresh").show();
            $("#a_store_clear").show();
        }
    }

    //画面按钮点击事件
    var but_event = function () {
        $("#i_cashQty").blur(function () {
            $("#i_cashQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#i_cashQty").focus(function () {
            $("#i_cashQty").val(reThousands(this.value));
        });

        $("#i_payInAmt").blur(function () {
            let payInAmt = reThousands(this.value); // 实收金额
            let payAmt = reThousands($("#i_payAmt").val()); // 应收金额
            let payAmtDiff = payInAmt - payAmt;
            $("#i_payAmtDiff").val(toThousands(payAmtDiff));
            $("#i_payInAmt").val(toThousands(payInAmt));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#i_payInAmt").focus(function () {
            $("#i_payInAmt").val(reThousands(this.value));
        });

        $("#i_payAmtDiff").blur(function () {
            $("#i_payAmtDiff").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#i_payAmtDiff").focus(function () {
            $("#i_payAmtDiff").val(reThousands(this.value));
        });
        $("#expenditureNo_clear").on("click",function () {
         $("#description").val("");
        });
        m.reset.on("click", function () {
            _common.myConfirm("Are you want to reset?", function (result) {
                $('div[id="resExpend"]').each(function () {
                       $('div[name="resExpendRow"]').remove();
                       $('div[id*="delExpendNo"]').remove();
                })
                $("#expenditureNo").css("border-color", "#CCC");
                $("#expenditureNo").attr("disabled",true);
                $("#expenditureNo_clear").click();
                $("#a_store").css("border-color", "#CCC");
                m.bs_date.css("border-color", "#CCC");
                if (result === "true") {
                    $("#a_store_clear").click();
                    $("#cash_split_flag_1").prop("checked", true);
                    setBusinessDate();
                    payTypeInit();
                    cashInit();
                    btnDisable(true);
                    inputDisable(false);
                }
            });
        });

        //检索条件
        m.search.on("click", function () {
            if (verifySearch("1")) {
                _common.myConfirm("Entry conditions cannot be changed after click 'Confirm, are you sure to continue?", function (result) {
                    if (result == "true") {
                        // expenditureNo 移除
                        if ($("#expenditureNo").attr("k") == null || $("#expenditureNo").attr("k") == "") {
                            $("#expenditureNoA").hide();
                            $("#expenditureNo").val($("#expenditurehide").val());

                        }
                        //所有‘减号’禁掉  11//27
                        $('a[id*="delExpendNo"]').each(function () {
                            $(this).addClass("disabled")
                        })
                        // 2020/11//26日增加
                        $("#expenditureNoDiv").hide();
                        var expendFlgVal = $("#expendFlgVal").val();
                        if(expendFlgVal != null && expendFlgVal!== ''){
                            expend_index = parseInt(expendFlgVal) + 1;
                        }else {
                            expend_index = expendFlgVal + 1;
                        }
                        if ($("#expenditureNo").attr("k")!=null && $("#expenditureNo").attr("k")!=""){
                            var rinput = $("#expenditureNo").attr("k");
                            var cinput = $("#description").val();
                            var resourceGroup = "<div class='form-group' name='resExpendRow' style='margin-left:0px'>" +
                                "                <label for='expenditureNo' class='col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label' style='white-space: nowrap'>Expenditure No.</label>" +
                                "                <div class='col-xs-12 col-sm-4 col-md-3 col-lg-2 '>" +
                                "                    <div class='res-info'>" + rinput + "</div>" +
                                "                    <input type='hidden' class='form-control input-sm'" +
                                "                           name='resexpend' value='" + rinput + "'>" +
                                "                </div>" +
                                "                <label for='description' class='col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label' style='white-space: nowrap'>Expenditure Description</label>" +
                                "                <div class='col-xs-12 col-sm-4 col-md-3 col-lg-2'>" +
                                "                    <div class='res-info'>" + cinput + "</div>" +
                                "                    <input type='hidden' class='form-control input-sm'" +
                                "                          value='" + cinput + "'>" +
                                "                </div>" +
                                "      <div class='col-xs-12 col-sm-12 col-md-1 col-lg-1'>" +
                                "        <div class='col-sm-1'>" +
                                "            <a class='btn btn-default btn-sm' href='javascript:void(0);'" +
                                "               role='button' id='delExpendNo" + expend_index + "'>" +
                                "                <div class='glyphicon glyphicon-minus'></div>" +
                                "            </a>" +
                                "        </div>" +
                                "    </div>";
                            $("#expendlitLine").after(resourceGroup);
                        }

                        m.search.attr("disabled",true);
                        m.reset.attr("disabled",true);
                        $('a[id*="delExpendNo"]').each(function () {
                            $(this).addClass("disabled")
                        })
                        inputDisable(true);
                        // 判断是否存在
                        getPay();
                    }
                });
            }
        });

        //Cash split change事件
        $('input:radio[name="cash_split_flag"]').change(function () {

            if ($("#cash_split_flag_1").is(":checked")) {
                $("#choose2").show();
            }
            if ($("#cash_split_flag_2").is(":checked")) {
                //隐藏Banknotes Entry表格
                $("#choose2").hide();
            }
        });
        m.submitBut.on("click", function () {
            $("#expenditureNo_clear").click();
            if (verifySearch("1")) {
               var thisParent = $(tableGrid1.find("tbody")).parent();
                //还原input输入框
                var cols = tableGrid1.getSelectColValue(thisParent, "cashCd");
                //还原表格中的input框
                var inputRowCashCd = tableGrid1.find(".info [tag=cashCd]");
                var cashQtyRow1 = $("#i_shift1Qty");
                var cashQtyRow2 = $("#i_shift2Qty");
                var cashQtyRow3 = $("#i_shift3Qty");
                if ((cols["cashCd"] !== inputRowCashCd.text() && cashQtyRow1.length === 1)
                    || (cols["cashCd"] !== inputRowCashCd.text() && cashQtyRow2.length === 1)
                    || (cols["cashCd"] !== inputRowCashCd.text() && cashQtyRow3.length === 1)) {
                    var cashQty1 = reThousands(cashQtyRow1.val());
                    var cashQty2 = reThousands(cashQtyRow2.val());
                    var cashQty3 = reThousands(cashQtyRow3.val());
                    //添加判断
                    var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
                    if (cashQty1 !== "" && !reg.test(cashQty1)) {
                        _common.prompt("Please enter the correct quantity!", 5, "error");/*请输入正确数量*/
                        $('#i_shift1Qty').focus();
                        return false;
                    }
                    if (cashQty2 !== "" && !reg.test(cashQty2)) {
                        _common.prompt("Please enter the correct quantity!", 5, "error");/*请输入正确数量*/
                        $('#i_shift2Qty').focus();
                        return false;
                    }
                    if (cashQty3 !== "" && !reg.test(cashQty3)) {
                        _common.prompt("Please enter the correct quantity!", 5, "error");/*请输入正确数量*/
                        $('#i_shift3Qty').focus();
                        return false;
                    }
                    tableGrid1.find(".info [tag=shift1]").text(toThousands(cashQty1));
                    tableGrid1.find(".info [tag=shift2]").text(toThousands(cashQty2));
                    tableGrid1.find(".info [tag=shift3]").text(toThousands(cashQty3));
                    //赋值
                    $('#i_shift1Qty').val(cols["shift1"]);
                    $('#i_shift2Qty').val(cols["shift2"]);
                    $('#i_shift3Qty').val(cols["shift3"]);
                    //现金合计
                    cash_total();
                }
                // if ($("#cash_split_flag_1").is(":checked")) {
                    if ($("#zgGridTtable>.zgGrid-tbody tr").length > 0) {
                        var payAmt0=0,payAmt1=0,payAmt2=0
                        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                            if ($(this).find('td[tag=payCd]').text() === "01") {
                                $(this).find('td[tag=payInAmt]').text($("#totalShift1").val());
                                payAmt0=  reThousands($(this).find('td[tag=payAmt]').text());
                            }
                            if ($(this).find('td[tag=payCd]').text() === "02") {
                                $(this).find('td[tag=payInAmt]').text($("#totalShift2").val());
                                payAmt1=  reThousands($(this).find('td[tag=payAmt]').text());
                            }
                            if ($(this).find('td[tag=payCd]').text() === "03") {
                                $(this).find('td[tag=payInAmt]').text($("#totalShift3").val());
                                payAmt2=  reThousands($(this).find('td[tag=payAmt]').text());
                            }
                        });
                        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                            if ($(this).find('td[tag=payCd]').text() === "01") {
                                $(this).find('td[tag=payAmtDiff]').text(toThousands(reThousands($("#totalShift1").val())-payAmt0));
                            }
                            if ($(this).find('td[tag=payCd]').text() === "02") {
                                $(this).find('td[tag=payAmtDiff]').text(toThousands(reThousands($("#totalShift2").val())-payAmt1));
                            }
                            if ($(this).find('td[tag=payCd]').text() === "03") {
                                $(this).find('td[tag=payAmtDiff]').text(toThousands(reThousands($("#totalShift3").val())-payAmt2));
                            }
                        });
                    }
                var payArr = [];
                var cashArr = [];
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var isTotal = $(this).attr("tag");
                    if (isTotal !== 'total') {
                        var pay = {
                            payCd: $(this).find('td[tag=payCd]').text(),
                            payName: $(this).find('td[tag=payName]').text(),
                            payAmt: reThousands($(this).find('td[tag=payAmt]').text()),//应收金额
                            payInAmt: reThousands($(this).find('td[tag=payInAmt]').text()),//实收金额
                            payAmtDiff: reThousands($(this).find('td[tag=payAmtDiff]').text()),//差异金额
                            differenceReasonCd: reThousands($(this).find('td[tag=differenceReasonCd]').text()),//差异原因cd
                        };
                        payArr.push(pay);
                    }
                });
                //获取 Cash split 选则的值
                var cashSplitCheckVal = $("[name=cash_split_flag]:checked").val();
                //选择Yes的时候才提交数据到后台
                if (cashSplitCheckVal === "1") {
                    $("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
                        var isTotal = $(this).attr("tag");
                        if (isTotal !== 'total') {
                            var cash = {
                                cashCd: $(this).find('td[tag=cashCd]').text(),
                                cashName: $(this).find('td[tag=cashName]').text(),
                                shift1: reThousands($(this).find('td[tag=shift1]').text()),//早班币种数量
                                shift2: reThousands($(this).find('td[tag=shift2]').text()),//中班币种数量
                                shift3: reThousands($(this).find('td[tag=shift3]').text()),//晚班币种数量
                            };
                            cashArr.push(cash);
                        }
                    })
                }
                var cashList = "", payList = "";
                if (payArr.length > 0) {
                    payList = JSON.stringify(payArr);
                }
                if (cashArr.length > 0) {
                    cashList = JSON.stringify(cashArr);
                }
                var param = {
                    expense: m.expense.val(),
                    remark: m.remark.val(),
                    cashList: cashList,
                    payList: payList,
                    payId: m.payId.val(),
                    storeCd: $("#a_store").attr("k"),
                    payDate: subfmtDate(m.bs_date.val()),
                    cashSplitFlag: $("input[name='cash_split_flag']:checked").val(),
                    toKen: m.toKen.val(),
                    // voucherNo: $("#expenditureNo").attr("k")
                    voucherNo: $('#from_hide_expend').val()
                };
                _common.myConfirm("Are you want to save", function (result) {
                    if (result === "true") {
                        tableGrid.showColumn('payAmt');
                        tableGrid.showColumn('payAmtDiff');
                        tableGrid.showColumn('salesByHht');
                        $("#payAmtDiff").show();
                        $('#payAmt').show();
                        btnDisable(true);
                        inputDisable(true);
                        $.myAjaxs({
                            url: url_left + "/save",
                            async: true,
                            cache: false,
                            type: "post",
                            data: param,
                            dataType: "json",
                            success: function (result) {
                                if (result.success) {

                                    m.remark.prop("disabled", true);
                                    m.expense.prop("disabled", true);
                                    m.payId.val(result.data);
                                    m.toKen.val(result.toKen);

                                    m.viewSts.val("view");
                                    _sts="view";
                                    pay_total();
                                    $('#a_store_refresh').prop('disabled', true);
                                    $('#a_store_clear').prop('disabled', true);
                                    $('#search').prop('disabled', true);
                                    $('#reset').prop('disabled', true);

                                    btnDisable(true);
                                    inputDisable(true);

                                    _common.prompt("Submitted successfully！", 2, "success");
                                } else {
                                    _common.prompt(result.message, 5, "error");
                                }
                            },
                            error: function (e) {
                                _common.prompt("Data saved failed！", 5, "error");
                            }
                        });
                    }
                })
            }
        })
        $("#addexpenditureNo").on("click",function (e) {
            $("#addexpenditureNo").val("");
            var rinput = $("#expenditureNo").attr("k");
            var cinput = $("#description").val();
            var expendFlgVal = $("#expendFlgVal").val();
            if(expendFlgVal != null && expendFlgVal!== ''){
                expend_index = parseInt(expendFlgVal) + 1;
            }else {
                expend_index = expendFlgVal + 1;
            }
            // var resourceGroup = "<div class='row' name='resExpendRow'>" +
            var resourceGroup = "<div class='form-group' name='resExpendRow' style='margin-left:0px'>" +
                // "    <div class='col-xs-12 col-sm-12 col-md-12 col-lg-12'>" +
                // "        <div class='form-horizontal'>" +
                // "            <div class='form-group'>" +
                "                <label for='expenditureNo' class='col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label' style='white-space: nowrap;margin-left:0px'>Expenditure No.</label>" +
                "                <div class='col-xs-12 col-sm-4 col-md-3 col-lg-2'>" +
                "                    <div class='res-info' style='margin-left:10px' >" + rinput + "</div>" +
                "                    <input type='hidden' class='form-control input-sm'" +
                "                           name='resexpend' value='" + rinput + "'>" +
                "                </div>" +
                "                <label for='description' class='col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label' style='white-space: nowrap'>Expenditure Description</label>" +
                "                <div class='col-xs-12 col-sm-4 col-md-3 col-lg-2'>" +
                "                    <div class='res-info'>" + cinput + "</div>" +
                "                    <input type='hidden' class='form-control input-sm'" +
                "                                       value='" + cinput + "'>" +
                "                </div>" +
                "      <div class='col-xs-12 col-sm-12 col-md-1 col-lg-1'>" +
                "        <div class='col-sm-1'>" +
                "            <a class='btn btn-default btn-sm' href='javascript:void(0);'" +
                "               role='button' id='delExpendNo" + expend_index + "'>" +
                "                <div class='glyphicon glyphicon-minus'></div>" +
                "            </a>" +
                "        </div>" +
                "    </div>";
                // "  </div>" +
                // " </div>" +
                // " </div>" +
                // "</div>";
            $("#expendlitLine").before(resourceGroup);
            $.myAutomatic.cleanSelectObj(expenditureNo);
          $("#description").val("");

        })
        // 绑定删除按钮事件
        $("#resExpend").on("click", "a[id*='delExpendNo']", function (e) {
            var aId;
            if (e.target.tagName == "A") {
                aId = $(e.target).attr("id");
            } else {
                aId = $(e.target).parent().attr("id");
            }
            $("#" + aId).closest("div[name='resExpendRow']").remove();
            $("#addexpenditureNo").removeClass("disabled");
            $('div[name="resExpendRow"]').each(function () {
                var rinputN = $(this).find("input[name='resexpend']").val();
                if ($("#expenditureNo").attr('k') === rinputN)
                    $("#addexpenditureNo").addClass("disabled");
            })
        })
    };

    var getPay = function () {
        // 创建参数
        let searchJsonStr = {
            storeCd: $("#a_store").attr("k"),
            payDate: subfmtDate(m.bs_date.val()),
            cashSplitFlag: $("input[name='cash_split_flag']:checked").val(),
            voucherNo:$("#expenditureNo").attr("k")
        };
        let param = "searchJson=" + JSON.stringify(searchJsonStr);
        $.myAjaxs({
            url: url_left + "/getCashDetail",
            async: true,
            cache: false,
            type: "post",
            data: param,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let payId = result.data.payId;
                    let payDate = result.data.payDate;
                    m.storeCd1.val(result.data.storeCd);
                    m.payDate.val(payDate);
                    m.payId.val(payId);
                    $('#search').prop('disabled', false);
                    $('#reset').prop('disabled', false);
                    inputDisable(false);
                    _common.prompt("Cash Balancing entry already finished for target store & date!", 5, "info");
                    return false;
                    //验证订货日是否为当前业务日
                    /*_common.checkBusinessDate(payDate, payId, function (result) {
                        if (result.success) {
                            btnDisable(false);
                        } else {
                            btnDisable(true);
                            _common.prompt("The document is not created within today and cannot be modified!", 3, "info");
                        }
                    })*/
                } else {
                    m.payId.val("");
                    btnDisable(false);
                }
                topayTypeInit();
                tocashInit();
            }
        });
    }

    var payTypeInit = function () {
        paramGrid = null;
        tableGrid.setting("url", url_left + "/getPayList");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", 1);
        tableGrid.loadData(null);
    }

    var cashInit = function () {
        paramGrid = null;
        tableGrid1.setting("url", url_left + "/getCashList");
        tableGrid1.setting("param", paramGrid);
        tableGrid1.setting("page", 1);
        tableGrid1.loadData(null);
    }

    //表格样式 支付类型
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Variance Details",
            param: paramGrid,
            colNames: ["Pay Cd", "Payment Type", "Total Amount","Sales By HHT","Actual Counted Amount(User Entered)", "Variance Amount", "Difference Reason Code", "Variance Reason"],
            colModel: [
                {name: "payCd", type: "text", text: "right", width: "100", ishide: true, css: ""},
                {name: "payName", type: "text", text: "left", width: "110", ishide: false, css: ""},
                {
                    name: "payAmt",
                    type: "text",
                    text: "right",
                    width: "90",
                    ishide: true,
                    css: "",
                    getCustomValue: getThousands
                },
                {name: "salesByHht", type: "text", text: "right", width: "90", ishide: true, css: "",getCustomValue: getThousands},
                {
                    name: "payInAmt",
                    type: "text",
                    text: "right",
                    width: "90",
                    ishide: false,
                    css: "",
                    getCustomValue: getThousands
                },
                {
                    name: "payAmtDiff",
                    type: "text",
                    text: "right",
                    width: "90",
                    ishide: true,
                    css: "",
                    getCustomValue: getThousands
                },
                {name: "differenceReasonCd", type: "text", text: "right", width: "50", ishide: true, css: ""},
                {
                    name: "differenceReason",
                    type: "text",
                    text: "left",
                    width: "100",
                    ishide: true,
                    css: "",
                    getCustomValue: fmtDifference
                },
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: false,//是否需要分页
            sidx: "",//排序字段
            sord: "asc",//升降序
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
                if (_sts == 'view') {
                    tableGrid.showColumn('payAmt');
                    tableGrid.showColumn('payAmtDiff');
                    tableGrid.showColumn('salesByHht');
                }
                pay_total();
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                var cols = tableGrid.getSelectColValue(trObj, "payCd");
                if (cols["payCd"] != "") {
                    selectTrTemp = trObj;
                } else {
                    selectTrTemp = null;
                }
            },
            buttonGroup: [
                {
                    butType: "update",
                    butId: "updateByPay",
                    butText: "Update",
                    butSize: ""//,
                },//修改
            ]
        });
    }

    //支付类型合计
    var pay_total = function () {
        if ($("#zgGridTtable>.zgGrid-tbody tr").length > 0) {
            var payAmt = 0;
            var payInAmt = 0;
            var payAmtDiff = 0;
            var paySalesByHht = 0;
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                var td_payAmt = parseFloat($(this).find('td[tag=payAmt]').text().replace(/,/g, ""));
                var td_payInAmt = parseFloat($(this).find('td[tag=payInAmt]').text().replace(/,/g, ""));
                var td_payAmtDiff = parseFloat($(this).find('td[tag=payAmtDiff]').text().replace(/,/g, ""));
                var td_salesByHht = parseFloat($(this).find('td[tag=salesByHht]').text().replace(/,/g, ""));
                if (!isNaN(td_payAmt))
                    payAmt += parseFloat(td_payAmt);
                if (!isNaN(td_payInAmt))
                    payInAmt += parseFloat(td_payInAmt);
                if (!isNaN(td_payAmtDiff))
                    payAmtDiff += parseFloat(td_payAmtDiff);
                if (!isNaN(td_salesByHht))
                    paySalesByHht += parseFloat(td_salesByHht);
            });
            var total = "<tr style='text-align: left' id='pay_total' tag='total'>" +
                "<td title='Total'>Total:</td>" +
                "<td align='right' id='payAmt' title='" + toThousands(payAmt) + "'>" + toThousands(payAmt) + "</td>" +
                "<td align='right' id='salesByHht' title='" + toThousands(paySalesByHht) + "'>" + toThousands(paySalesByHht) + "</td>" +
                "<td align='right'  id='payInAmt' title='" + toThousands(payInAmt) + "'>" + toThousands(payInAmt) + "</td>" +
                "<td align='right'   id='payAmtDiff' title='" + toThousands(payAmtDiff) + "'>" + toThousands(payAmtDiff) + "</td>" +
                //"<td></td>" +
                "</tr>";
            $("#pay_total").remove();

            $("#zgGridTtable_tbody").append(total);
            if (_sts == 'view') {
                $("#payAmtDiff").show();
                $('#payAmt').show();
                $('#salesByHht').show();
            } else {
                $('#payAmt').hide();
                $("#payAmtDiff").hide();
                $("#salesByHht").hide();
            }
        }

    }
    //币种合计
    var cash_total = function () {
        var totalShift=0;
        if ($("#zgGridTtable1>.zgGrid-tbody tr").length > 0) {
            var amount1 = 0,amount2 = 0,amount3 = 0;

            $("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
                var cashCd = $(this).find('td[tag=cashCd]').text();
                var shift1 = reThousands($(this).find('td[tag=shift1]').text());
                var shift2 = reThousands($(this).find('td[tag=shift2]').text());
                var shift3 = reThousands($(this).find('td[tag=shift3]').text());

                if (!isNaN(shift1)) {
                    switch (cashCd) {
                        case "00":
                            amount1 += shift1 * 500000;
                            amount2 += shift2 * 500000;
                            amount3 += shift3 * 500000;
                            break;
                        case "01":
                            amount1 += shift1 * 200000;
                            amount2 += shift2 * 200000;
                            amount3 += shift3 * 200000;
                            break;
                        case "02":
                            amount1 += shift1 * 100000;
                            amount2 += shift2 * 100000;
                            amount3 += shift3 * 100000;
                            break;
                        case "03":
                            amount1 += shift1 * 50000;
                            amount2 += shift2 * 50000;
                            amount3 += shift3 * 50000;
                            break;
                        case "04":
                            amount1 += shift1 * 20000;
                            amount2 += shift2 * 20000;
                            amount3 += shift3 * 20000;
                            break;
                        case "05":
                            amount1 += shift1 * 10000;
                            amount2 += shift2 * 10000;
                            amount3 += shift3 * 10000;
                            break;
                        case "06":
                            amount1 += shift1 * 5000;
                            amount2 += shift2 * 5000;
                            amount3 += shift3 * 5000;
                            break;
                        case "07":
                            amount1 += shift1 * 2000;
                            amount2 += shift2 * 2000;
                            amount3 += shift3 * 2000;
                            break;
                        case "08":
                            amount1 += shift1 * 1000;
                            amount2 += shift2 * 1000;
                            amount3 += shift3 * 1000;
                            break;
                        case "09":
                            amount1 += shift1 * 500;
                            amount2 += shift2 * 500;
                            amount3 += shift3 * 500;
                            break;
                        case "10":
                            amount1 += shift1 * 200;
                            amount2 += shift2 * 200;
                            amount3 += shift3 * 200;
                            break;
                        case "11":
                            amount1 += shift1 * 100;
                            amount2 += shift2 * 100;
                            amount3 += shift3 * 100;
                            break;
                    }
                }
                totalShift=amount1+amount2+amount3;
            });

            var reg = /\d{1,3}(?=(\d{3})+$)/g;
            var amountshift1 = (amount1 + '').replace(reg, '$&,');
            var amountText1 = (amount1 + '').replace(reg, '$&,');
            var amountshift2 = (amount2 + '').replace(reg, '$&,');
            var amountText2 = (amount2 + '').replace(reg, '$&,');
            var amountshift3 = (amount3 + '').replace(reg, '$&,');
            var amountText3 = (amount3 + '').replace(reg, '$&,');

            amountshift1 += " VND";
            amountshift2 += " VND";
            amountshift3 += " VND";
            var total = "<tr style='text-align: left' id='cash_total'  tag='total'>" +
                "<td title='Total'>Total:</td>" +
                "<td align='right'  title='" + amountshift1 + "'>" + amountshift1 + "</td>" +
                "<td align='right'  title='" + amountshift2 + "'>" + amountshift2 + "</td>" +
                "<td align='right'  title='" + amountshift3 + "'>" + amountshift3 + "</td>" +
                "</tr>"+
            "<tr style='text-align: left;text-align: left;font-size:20px' id='cash_total2'  tag='total'>" +
             "<td colspan='4'>3 Shifts Total:" +toThousands(totalShift) + "</td>"+
            // "<td title='Total'></td>" +
            // "<td></td>"+
            // "<td></td>"+
            // "3 Shifts Total:" +totalShift +
            "</tr>";
            $("#totalShift1").val(amountText1);
            $("#totalShift2").val(amountText2);
            $("#totalShift3").val(amountText3);
            $("#cash_total").remove();
            $("#cash_total2").remove();
            $("#zgGridTtable1_tbody").append(total);
        }
    }
    //表格样式  币种
    var initTable2 = function () {
        tableGrid1 = $("#zgGridTtable1").zgGrid({
            // title: "Banknotes Entry",
            title: "Banknotes Entry",
            param: paramGrid1,
            colNames: ["Cash Cd", "Currency","Shift1","Shift2","Shift3"],
            colModel: [
                {name: "cashCd", type: "text", text: "right", width: "100", ishide: true, css: ""},
                {name: "cashName", type: "text", text: "left", width: "150", ishide: false, css: ""},
                {name: "shift1",type: "text",text: "right",width: "150",ishide: false,css: "",getCustomValue: getThousands},
                {name: "shift2",type: "text",text: "right",width: "150",ishide: false,css: "",getCustomValue: getThousands},
                {name: "shift3",type: "text",text: "right",width: "150",ishide: false,css: "",getCustomValue: getThousands},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: false,//是否需要分页
            sidx: "",//排序字段
            sord: "asc",//升降序
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp1 = null;//清空选择的行
                cash_total();
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                var cols = tableGrid1.getSelectColValue(trObj, "cashCd");
                if (cols["cashCd"] != "") {
                    selectTrTemp1 = trObj;
                } else {
                    selectTrTemp1 = null;
                }
            },
            buttonGroup: [
                {
                    butType: "update",
                    butId: "updateByCash",
                    butText: "Update",
                    butSize: ""//,
                },//修改
            ]
        });
    }

    function fmtNull(val) {
        if (val == null) {
            return "";
        }
    }

    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url: systemPath + "/cm9010/getCode",
            async: false,
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
                _common.prompt(title + "Failed to load data!", 5, "error");
            }
        });
    }

    //验证检索项是否合法 flg为1判断非空
    var verifySearch = function (flg) {
        if ($("#a_store").attr("k") == null || $("#a_store").attr("k") == "") {
            if (flg == "1") {
                _common.prompt("Please select a store first!", 5, "error");
                $("#a_store").focus();
                $("#a_store").css("border-color", "red");
            }
            return false;
        } else {
            $("#a_store").css("border-color", "#CCC");
        }
        if (!m.bs_date.val()) {
            if (flg == "1") {
                _common.prompt("The business date cannot be empty!", 5, "error");/*业务日期不能为空*/
                m.bs_date.focus();
                m.bs_date.css("border-color", "red");
            }
            return false;
        } else {
            if(_common.judgeValidDate(m.bs_date.val())){
                _common.prompt("Please enter a valid date!",3,"info");
                m.bs_date.focus();
                return false;
            }else if(parseInt(formatDate(m.bs_date.val()))>parseInt(formatDate(dateStr))){
                _common.prompt("Only dates prior to today can be selected!",3,"info");
                m.bs_date.focus();
                return false;
            }
            else {
                m.bs_date.css("border-color", "#CCC");
            }
        }

        if (m.remark.val().length > 200) {
            _common.prompt("Remark cannot exceed 200 characters!", 5, "error");
            return false;
        }

        if (checkReceipt()>0){
            _common.prompt("the select expendNo has been  repeat  !!", 5, "error");
            $("#expenditureNo").css("border-color", "red");
            $("#expenditureNo").focus();
            return  false;
        }
       if (subfmtDate(m.bs_date.val())>m.businessDate.val()){
           _common.prompt("The business date cannot be greater today!", 5, "error");
           return  false;
       }
        return  true;
    }
    var checkReceipt=function () {
        var count=0;
        $('div[name="resExpendRow"]').each(function () {
            var rinputN = $(this).find("input[name='resexpend']").val();
            if (rinputN === $("#expenditureNo").attr("k")) {
                $("#addexpenditureNo").addClass("disabled");
                $("#expenditureNo").css("border-color", "red");
                $("#expenditureNo").focus();
                // $("#delExpendDiv").show();
                // _common.prompt("the select expendNo has been  repeat  !!", 5, "error");
                count++
            } else {
                $("#expenditureNo").css("border-color", "#CCC");
            }
        });
        return count;
    }

    var fmtDifference = function (tdObj, value) {
        var difference = "";
        $('#differenceReason>option').each(function () {
            if (value && $(this).val() == value) {
                difference = $(this).text();
            }
        })
        return $(tdObj).text(difference);
    }

    //number格式化
    var formatNum = function (tdObj, value) {
        return $(tdObj).text(fmtIntNum(value));
    }
    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }
    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    function fmtIntNum(val) {
        if (val == null || val == "") {
            return "0";
        }
        return val;
    }

    function subfmtDate(date) {
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    //格式化数字类型的日期
    function dateInfmt(date) {
        var res = "";
        res = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        return res;
    }

    // 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
    function fmtIntDate(date) {
        if (date == null || date.length != 8) {
            return "";
        }
        var res = "";
        res = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }
    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g,'');
        var day = dateStr.substring(0,2);
        var month = dateStr.substring(2,4);
        var year = dateStr.substring(4,8);
        return year+month+day;
    }


    // 设置业务日期
    function setBusinessDate() {
        let businessDate = m.businessDate.val();
        if (businessDate != null && businessDate != '') {
            let date = new Date(new Date(dateInfmt(businessDate))-1000*60*60*24);
            var mouth = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? "0" + (date.getDate()) : date.getDate();
            dateStr = day+"/"+mouth+"/"+date.getFullYear();
            dateTime = date.getFullYear()+"-"+mouth+"-"+day;
            m.bs_date.val(dateStr);
        } else {
            _common.prompt("Failed to get business date, please refresh!", 3, "error");
        }
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('cashierAmount');
_start.load(function (_common) {
    _index.init(_common);
});
