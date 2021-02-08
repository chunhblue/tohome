require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('cashierDetail', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,//销售头档grid参数
        paramGrid1 = null,//支付类型grid参数
        paramGrid2 = null,//销售详细grid参数
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        reThousands = null,
        toThousands = null,
        getThousands = null;
        common = null;
    var m = {
        toKen: null,
        use: null,
        up: null,
        tableGrid: null,
        tableGrid1: null,
        tableGrid2: null,
        selectTrObj: null,
        error_pcode: null,
        reject_dialog: null,
        search: null,
        reset: null,
        export: null,
        aStore: null,
        clear_sale_date: null,
        sale_start_date: null,
        sale_end_date: null,
        shift: null,//班次
        cashierId: null,//营业员id
        memberId: null,//会员卡号
        posId: null,//pos号
        barcode: null,//商品号
        articleShortName: null,//商品名
        payType: null,//支付类型
        calType: null,
        payAmt: null//支付金额
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/cashierDetail";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //初始按钮
        but_event();
        //初始化表格
        initTable();
        // 初始化店铺运营组织检索
        // _common.initOrganization();
        // 初始化检索日期
        _common.initDate(m.sale_start_date, m.sale_end_date);
        //初始化店铺名称下拉
        _common.initStoreform();
        // 验证权限
        isButPermission();
    }


    var initTable = function () {
        initTable1();
        initTable2();
        initTable3();
        $("#zgGridTtable > .zgGrid-tbody").empty();
        $("#payDetailTable > .zgGrid-tbody").empty();
        $("#itemDetailTable > .zgGrid-tbody").empty();
    };

    //画面按钮点击事件
    var but_event = function () {
        // 导出按钮事件
        m.export.on("click", function () {
            if (!verifySearch()) {
                return;
            }

            var storeCd = $("#aStore").attr("k");
            var startDate = subfmtDate(m.sale_start_date.val());
            var endDate = subfmtDate(m.sale_end_date.val());
            var posId = m.posId.val().trim();
            var cashierId = m.cashierId.val().trim();
            var shift = m.shift.val().trim();
            var payType, calType, payAmt;
            paramGrid = "storeCd=" + storeCd + "&startDate=" + startDate + "&endDate="
                + endDate + "&cashierId=" + cashierId + "&shift=" + shift + "&posId=" + posId;
            if ((m.payType.val() != null && m.payType.val() !== '')  || (m.payAmt.val() != null && m.payAmt.val() !== '')) {
                payType = m.payType.val().trim();
                calType = m.calType.val().trim();
                payAmt = reThousands(m.payAmt.val().trim());
                paramGrid += "&payType=" + payType + "&calType=" + calType + "&payAmt=" + payAmt;
            }
            var barcode = m.barcode.val().trim();
            paramGrid += "&barcode=" + barcode;
            var articleShortName = m.articleShortName.val().trim();
            paramGrid += "&articleShortName=" + articleShortName;

            let url = url_left + "/export?" + paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
        });

        m.posId.on('change', function () {
            let posId = m.posId.val().trim();
            let storeCd = m.aStore.attr('k');
            if (!posId || !storeCd) {
                m.cashierId.find("option:not(:first)").remove();
                return;
            }
            getCashier(posId, storeCd)
        });

        m.aStore.on('blur', function () {
            let storeCd = m.aStore.attr('k');
            if (!storeCd) {
                m.posId.find("option:not(:first)").remove();
                return;
            }
            getSelectPosId(storeCd);
        });

        //清空日期
        m.clear_sale_date.on("click", function () {
            m.sale_start_date.val("");
            m.sale_end_date.val("");
        });
        //检索按钮点击事件
        m.search.on("click", function () {
            if (verifySearch()) {
                var storeCd = $("#aStore").attr("k");
                var startDate = subfmtDate(m.sale_start_date.val());
                var endDate = subfmtDate(m.sale_end_date.val());
                var posId = m.posId.val().trim();
                var cashierId = m.cashierId.val().trim();
                var shift = m.shift.val().trim();
                var payType, calType, payAmt;
                paramGrid = "storeCd=" + storeCd + "&startDate=" + startDate + "&endDate="
                    + endDate + "&cashierId=" + cashierId + "&shift=" + shift + "&posId=" + posId;
                if ((m.payType.val() != null && m.payType.val() !== '') || (m.payAmt.val() != null && m.payAmt.val() !== '')) {
                    payType = m.payType.val().trim();
                    calType = m.calType.val().trim();
                    payAmt = reThousands(m.payAmt.val().trim());
                    paramGrid += "&payType=" + payType + "&calType=" + calType + "&payAmt=" + payAmt;
                }
                var barcode = m.barcode.val().trim();
                paramGrid += "&barcode=" + barcode;
                var articleShortName = m.articleShortName.val().trim();
                paramGrid += "&articleShortName=" + articleShortName;
                tableGrid.setting("url", url_left + "/getSaleHeadList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
                $("#payDetailTable > .zgGrid-tbody").empty();
                $("#itemDetailTable > .zgGrid-tbody").empty();
            }
        });

        m.reset.on("click", function () {
            $("#storeRemove").click();
            m.sale_start_date.val("");
            m.sale_end_date.val("");
            m.cashierId.val("");
            m.payType.val("");
            m.calType.val("");
            m.payAmt.val("");
            m.memberId.val("");
            m.barcode.val("");
            m.articleShortName.val("");
            m.posId.val("");
            m.cashierId.val("");
            m.shift.val("");
            $("#aStore").css("border-color", "#CCC");
            selectTrTemp = null;
            _common.clearTable();
        });

        // $('input[type=number]').keypress(function (e) {
        //     if (!String.fromCharCode(e.keyCode).match(/[0-9\.]/)) {
        //         return false;
        //     }
        // });

        m.payAmt.blur(function(){
            m.payAmt.val(toThousands(this.value));
        });
        //光标进入，去除金额千分位，并去除小数后面多余的0
        m.payAmt.focus(function(){
            m.payAmt.val(reThousands(this.value));
        });
    }

    // 获得收银人员
    function getCashier(posId, storeCd) {
        $.myAjaxs({
            url: url_left + "/getCashier",
            async: true,
            cache: false,
            type: "post",
            data: 'storeCd=' + storeCd + '&posId=' + posId,
            dataType: "json",
            success: function (result) {
                m.cashierId.find("option:not(:first)").remove();
                if (result.success) {
                    let data = result.o;
                    for (var i = 0; i < data.length; i++) {
                        var optionValue = data[i].cashierId;
                        var optionText = data[i].cashierId + ' ' + data[i].cashierName;
                        m.cashierId.append(new Option(optionText, optionValue));
                    }
                    m.cashierId.find("option:first").prop("selected", true);
                }
            },
            error: function (e) {
                _common.prompt("request failed!", 5, "error");
            },
            complete: _common.myAjaxComplete
        });
    }

    //获取pos机号下拉
    var getSelectPosId = function (storeCd, val) {
        if (storeCd != null && storeCd != '') {
            $.myAjaxs({
                url: systemPath + "/paymentMode/edit/getPosId",
                async: true,
                cache: false,
                type: "post",
                data: "storeCd=" + storeCd,
                dataType: "json",
                success: function (result) {
                    m.posId.find("option:not(:first)").remove();
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


    //验证检索项是否合法
    var verifySearch = function () {
        var _storeCd = $("#aStore").attr("k");
        if (_storeCd == null || _storeCd == "") {
            $("#aStore").css("border-color", "red");
            $("#aStore").focus();
            _common.prompt("Please select a store first!", 3, "info");
            return false;
        } else {
            $("#aStore").css("border-color", "#CCC");
        }
        if (m.sale_start_date.val() == null || m.sale_start_date.val() == "") {
            _common.prompt("Please select the start date of the sale!", 5, "error"); // 请选择销售开始时间
            m.sale_start_date.focus();
            return false;
        }
        if (m.sale_end_date.val() == null || m.sale_end_date.val() == "") {
            _common.prompt("Please select the end date of the sale!", 5, "error"); // 请选择销售结束时间
            m.sale_end_date.focus();
            return false;
        }
        if (m.sale_start_date.val() != null && m.sale_start_date.val() != "" &&
            m.sale_end_date.val() != null && m.sale_end_date.val() != "") {
            var startDate = parseInt(subfmtDate(m.sale_start_date.val()));
            var endDate = parseInt(subfmtDate(m.sale_end_date.val()));
            if (startDate > endDate) {
                // 销售开始时间不能大于结束时间
                _common.prompt("The sales start date must be greater than or equal to the sales end date!", 5, "error");
                m.sale_start_date.focus();
                return false;
            }
            var _StartDate = new Date(fmtDate($("#sale_start_date").val())).getTime();
            var _EndDate = new Date(fmtDate($("#sale_end_date").val())).getTime();
            var difValue = parseInt(Math.abs((_EndDate - _StartDate) / (1000 * 3600 * 24)));
            if (difValue > 62) {
                _common.prompt("Query Period cannot exceed 62 days!", 5, "error"); // 日期期间取值范围不能大于62天
                $("#sale_end_date").focus();
                return false;
            }
        }

        if (m.payType.val() != null && m.payType.val() !== "") {
            if (m.payType.val() == null || m.payType.val() === "") {
                _common.prompt("Please fill in the payment method completely!", 5, "error"); // 请将支付方式填写完整
                m.payType.focus();
                return false;
            }
            if (m.calType.val() == null || m.calType.val() === "") {
                _common.prompt("Please fill in the payment method completely!", 5, "error"); // 请将支付方式填写完整
                m.calType.focus();
                return false;
            }
            if (m.payAmt.val() == null || m.payAmt.val() === "") {
                _common.prompt("Please fill in the payment method completely!", 5, "error"); // 请将支付方式填写完整
                m.payAmt.focus();
                return false;
            } else {
                var reg = /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
                if (!reg.test(reThousands(m.payAmt.val()))) {
                    _common.prompt("Wrong format of payment amount!", 5, "error"); // 支付方式金额填写格式错误
                    m.payAmt.focus();
                    return false;
                }
            }
        }
        if (m.payAmt.val() != null && m.payAmt.val() !== "") {
            var reg = /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
            if (!reg.test(reThousands(m.payAmt.val()))) {
                _common.prompt("Wrong format of payment amount!", 5, "error"); // 支付方式金额填写格式错误
                m.payAmt.focus();
                return false;
            }
            if (m.calType.val() == null || m.calType.val() === "") {
                _common.prompt("Please fill in the payment method completely!", 5, "error"); // 请将支付方式填写完整
                m.calType.focus();
                return false;
            }
        }

        return true;
    }

    //表格初始化-销售头档样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Selling List(Click a record to view the item details.)",
            param: paramGrid,
            colNames: ["Store No.", "Tran Serial No", "Acc Date", "Sales Date", "POS No.", "Cashier", "Shift", "Receipt No.", "Selling Time", "Membership ID", "Subtotal", "Discount", "Total"],
            colModel: [
                {name: "storeCd", type: "text", text: "right", width: "100", ishide: true, css: ""},
                {name: "tranSerialNo", type: "text", text: "right", width: "100", ishide: true, css: ""},
                {name: "accDate", type: "text", text: "center", width: "100", ishide: true, css: ""},
                {name: "tranDate", type: "text", text: "center", width: "130", ishide: false, css: "", getCustomValue: dateFmt},
                {name: "posId", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "cashierId", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "shift", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "saleSerialNo", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "tranTime", type: "text", text: "center", width: "130", ishide: false, css: ""},
                {name: "memberId", type: "text", text: "right", width: "150", ishide: false, css: ""},
                {name: "saleAmount", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands},
                {name: "overAmount", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands},
                {name: "payAmount", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands}
            ],//列内容
            width: "max",//宽度自动
            height: "250px",
            page: 1,//当前页
            rowPerPage: 8,//每页数据量
            isPage: true,//是否需要分页
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
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
                trClick_table();
            },
        });
    }

    //表格初始化-支付类型样式
    var initTable2 = function () {
        tableGrid1 = $("#payDetailTable").zgGrid({
            title: "Payment Details",
            param: paramGrid1,
            colNames: ["Pay Cd", "Payment Type", "Amount"],
            colModel: [
                {name: "payCd", type: "text", text: "right", width: "100", ishide: true, css: ""},
                {name: "payNamePrint", type: "text", text: "left", width: "150", ishide: false, css: ""},
                {name: "payAmount", type: "text", text: "right", width: "130", ishide: false, css: "", getCustomValue: _common.getThousands}
            ],//列内容
            width: "max",//宽度自动
            height: "276px",
            page: 1,//当前页
            rowPerPage: 8,//每页数据量
            isPage: false,//是否需要分页
            // sidx:"",//排序字段
            // sord:"asc",//升降序
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击

            },
        });
    }

    //表格初始化-商品详细样式
    var initTable3 = function () {
        tableGrid2 = $("#itemDetailTable").zgGrid({
            title: "Item Details",
            param: paramGrid2,
            colNames: ["No.", "Item Barcode", "Item Name", "Specification", "UOM", "Original Selling Price", "Selling Price", "Quantity", "Amount", "Discount Amount"],
            colModel: [
                {name: "saleSeqNo", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "barcode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "articleShortName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "spec", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "unitName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "priceOriginal", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands},
                {name: "priceActual", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands},
                {name: "saleQty", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands},
                {name: "saleAmount", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands},
                {name: "discountAmount", type: "text", text: "right", width: "150", ishide: false, css: "", getCustomValue: _common.getThousands}
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            // sidx:"",//排序字段
            // sord:"asc",//升降序
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                total();
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
            },
        });
    }

    // 选项选中事件
    var trClick_table = function () {
        var cols = tableGrid.getSelectColValue(selectTrTemp, "accDate,storeCd,posId,tranSerialNo,barcode,articleShortName");
        var accDate = cols["accDate"];
        var storeCd = cols["storeCd"];
        var posId = cols["posId"];
        var tranSerialNo = cols["tranSerialNo"];
        var barcode = cols["barcode"];
        var articleShortName = cols["articleShortName"];
        paramGrid1 = "accDate=" + accDate + "&storeCd=" + storeCd + "&posId=" + posId + "&tranSerialNo=" + tranSerialNo + "&barcode=" + barcode + "&articleShortName=" + articleShortName;
        tableGrid1.setting("url", url_left + "/getPayDetail");
        tableGrid1.setting("param", paramGrid1);
        tableGrid1.loadData(null);
        paramGrid2 = paramGrid1;
        total_detail(paramGrid2);
        tableGrid2.setting("url", url_left + "/getSaleDetailList");
        tableGrid2.setting("param", paramGrid2);
        tableGrid2.setting("page", 1);
        tableGrid2.loadData(null);

    };

    // 获取销售总数量，总金额
    var total_detail = function (paramGrid2) {
        $.myAjaxs({
            url: url_left + "/getSaleDetailTotal",
            async: false,
            cache: false,
            type: "post",
            data: paramGrid2,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    $("#toSaleQty").val(result.o.saleQty);
                    $("#toSaleAmount").val(result.o.saleAmount);
                    $("#toDiscountAmount").val(result.o.discountAmount);
                }
            },
            error:function () {
                $("#toSaleQty").val('');
                $("#toSaleAmount").val('');
                $("#toDiscountAmount").val('');
            }
        })
    };

    var total = function () {
        let td_saleQty = $("#toSaleQty").val();
        let td_saleAmount = $("#toSaleAmount").val();
        let td_discountAmount = $("#toDiscountAmount").val();
        var total = "<tr style='text-align:right' id='total_qty'><td></td><td></td><td></td><td></td><td></td><td></td>" +
            "<td>Total:</td>" +
            "<td>" + _common.toThousands(td_saleQty) + "</td>" +
            "<td>" + _common.toThousands(td_saleAmount) + "</td>" +
            "<td>"+_common.toThousands(td_discountAmount)+"</td>" +
            "</tr>";
        $("#total_qty").remove();
        $("#itemDetailTable_tbody").append(total);
    }

    // 按钮权限验证
    var isButPermission = function () {
        var exportBut = $("#exportBut").val();
        if (Number(exportBut) != 1) {
            $("#export").remove();
        }
    }

    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }

    //number格式化
    var formatNum = function (tdObj, value) {
        return $(tdObj).text(fmtIntNum(value));
    }

    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    //格式化数字类型的日期（->yyyy-mm-dd）
    function fmtIntNumber(dateString) {
        // dateString = '19930701';
        var pattern = /(\d{4})(\d{2})(\d{2})/;
        return dateString.replace(pattern, '$1-$2-$3');
    }

    //格式化数字带千分位
    function fmtIntNum(val) {
        if (val == null || val == "") {
            return "0";
        }
        return val;
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    //格式化数字类型的日期（->dd/mm/yyyy）
    function fmtIntDate(date) {
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, "$3-$2-$1");
        return res;
    }

    // -> "mmddyyyy"
    function subfmtDate(date) {
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    //格式化数字类型的日期(-> "yyyymmdd")
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('cashierDetail');
_start.load(function (_common) {
    _index.init(_common);
});
