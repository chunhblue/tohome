require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('invoiceEntryEdit', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        receiptGridTable = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},
        systemPath = '',
        a_store = null;
    var dataForm = [];
    const KEY = 'INVOICE_ENTRY_TO_CUSTOMER';
    var m = {
        toKen: null,
        use: null,
        up: null,
        submitter: null,
        submitDate: null,
        issuer: null,
        searchFormJson:null,
        issueDate: null,
        accIdParam: null,
        storeNoParam: null,
        customerName: null,
        companyName: null,
        tax: null,
        address: null,
        phone: null,
        email: null,
        startDate: null,
        endDate: null,
        saveBut: null,
        issueBut: null,
        storeNo: null,
        searchJson: null,
        searchReceiptNo: null,
        search: null,
        box:null,
        reset: null,
        phone2:null,
        email2:null,
        returnsViewBut: null,
        enterFlag: null, // 操作状态
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/invoiceEntry";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        initTable1();
        but_event();
        // store 下拉
        initAutoMatic();

        if (m.enterFlag.val()) {
            setDisable(true);
            if (m.enterFlag.val() != 'view') {
                // 禁用所有
                return;
            }
            // 查询数据
            getData(m.accIdParam.val(), m.storeNoParam.val());
        } else {
            // 新增状态
            m.issueBut.prop('disabled', true);
        }

    }

    function setDisable(flag) {
        m.customerName.prop('disabled', flag);
        m.companyName.prop('disabled', flag);
        m.tax.prop('disabled', flag);
        m.address.prop('disabled', flag);
        m.phone.prop('disabled', flag);
        m.email.prop('disabled', flag);
        m.phone2.prop('disabled', flag);
        m.email2.prop('disabled', flag);
        m.startDate.prop('disabled', flag);
        m.endDate.prop('disabled', flag);
        m.searchReceiptNo.prop('disabled', flag);
        m.storeNo.prop('disabled', flag);
        m.saveBut.prop('disabled', flag);
        m.search.prop('disabled', flag);
        m.reset.prop('disabled', flag);

        let elem = flag?"none":"auto";
        $("#storeRefresh").attr("disabled",flag).css("pointer-events",elem);
        $("#storeRemove").attr("disabled",flag).css("pointer-events",elem);
        if(flag === true){
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        }else {
            $("#storeRefresh").show();
            $("#storeRemove").show();
        }
    }

    // 查询明细
    function getData(accId, storeNo) {
        if (!accId || !storeNo) {
            return;
        }

        $.myAjaxs({
            url: url_left + '/getData',
            async: true,
            cache: false,
            type: "get",
            data: 'accId='+accId+'&storeNo='+storeNo,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    // 变为查看模式
                    // setDisable();
                    let record = result.o;
                    m.customerName.val(record.customerName);
                    m.companyName.val(record.companyName);
                    m.tax.val(record.tax);
                    m.address.val(record.address);
                    m.phone.val(record.phone);
                    m.email.val(record.email);
                    m.email2.val(record.email2);
                    m.phone2.val(record.phone2);
                    m.submitter.val(record.createUserId);
                    m.issuer.val(record.issueUserId);
                    m.submitDate.val(record.createYmd);
                    m.issueDate.val(record.issueYmd);
                    if (m.enterFlag.val()=="view"){
                        $("#searchForm").hide();
                        $("#searchFormJson").hide();
                        $("#box").hide();
                    }
                    if (record.status != '01') {
                        m.issueBut.prop('disabled', true);
                    }

                    let obj = {
                        'receiptNo': record.receiptNo,
                        'storeNo': record.storeNo,
                    };

                    // 查询小票信息
                    let paramGrid = "searchJson=" + JSON.stringify(obj);
                    tableGrid.setting("url", url_left + "/getInvoiceByReceiptNo");
                    tableGrid.setting("param", paramGrid);
                    tableGrid.setting("page", 1);
                    tableGrid.loadData();
                } else {
                    _common.prompt(result.msg, 5, "error");
                    setDisable(true);
                    m.issueBut.prop('disabled', true);
                }
            },
            error: function (e) {
                _common.prompt("request failed!", 5, "error");/*保存失败*/
                setDisable(true);
                m.issueBut.prop('disabled', true);
            }
        });
    }

    // 点击头档后执行查询明细商品方法
    function searchInvoiceItem() {
        if (!selectTrTemp) {
            return;
        }
        let col = tableGrid.getSelectColValue(selectTrTemp, 'receiptNo,storeNo');

        if (!col) {
            return;
        }
        let obj = {
            'receiptNo': col['receiptNo'],
            'storeNo': col['storeNo'],
        };
        let paramGrid = 'searchJson=' + JSON.stringify(obj);
        receiptGridTable.setting("url", url_left + "/searchInvoiceItem");
        receiptGridTable.setting("param", paramGrid);
        receiptGridTable.setting("page", 1);
        receiptGridTable.loadData();
    }

    function setParamJson() {
        let startDate = formatDate(m.startDate.val());
        let endDate = formatDate(m.endDate.val());
        let storeNo = m.storeNo.attr('k');
        let receiptNo = m.searchReceiptNo.val();

        let obj = {
            'startDate': startDate,
            'endDate': endDate,
            'storeNo': storeNo,
            'receiptNo': receiptNo,
        };
        m.searchJson.val(JSON.stringify(obj));
    }

    function verifySave() {
        let customerName = m.customerName.val();
        let companyName = m.companyName.val();
        let address = m.address.val();
        let phone = m.phone.val();
        let phone2 = m.phone2.val();
        let email = m.email.val();
        let email2 = m.email2.val();

        if (!customerName) {
            _common.prompt('Please enter Customer’s name!', 5, 'error');
            $("#customerName").css("border-color", "red");
            m.customerName.focus();
            return false;
        } else {
            $("#customerName").css("border-color", "#CCCCCC");
        }
        if (!companyName) {
            _common.prompt('Please enter Company’s name!', 5, 'error');
            $("#companyName").css("border-color", "red");
            m.companyName.focus();
            return false;
        } else {
            $("#companyName").css("border-color", "#CCCCCC");
        }
        if (!address) {
            _common.prompt('Please enter Address', 5, 'error');
            $("#address").css("border-color", "red");
            m.address.focus();
            return false;
        } else {
            $("#address").css("border-color", "#CCCCCC");
        }
        if (!phone) {
            _common.prompt('Please enter Telephone Number', 5, 'error');
            $("#phone").css("border-color", "red");
            m.phone.focus();
            return false;
        }

        // let phone_reg = /^1(3|4|5|6|7|8|9)\d{9}$/;
        let phone_reg = /^[0-9]\d{4,15}$/;    // 5-16位的非负整数

        if (!phone_reg.test(phone)) {
            _common.prompt('Please enter the correct telephone number format!', 5, 'error');
            $("#phone").css("border-color", "red");
            m.phone.focus();
            return false;
        }
        if (phone2) {
            if (!phone_reg.test(phone2)) {
                _common.prompt('Please enter the correct telephone number format!', 5, 'error');
                $("#phone2").css("border-color", "red");
                m.phone2.focus();
                return false;
            }
        }
        $("#phone").css("border-color", "#CCCCCC");
        $("#phone2").css("border-color", "#CCCCCC");

        if (!email) {
            _common.prompt('Please enter E-mail', 5, 'error');
            $("#email").css("border-color", "red");
            m.email.focus();
            return false;
        }
        let email_reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
        if (!email_reg.test(email)) {
            _common.prompt('Please enter the correct email format!', 5, 'error');
            $("#email").css("border-color", "red");
            m.email.focus();
            return false;
        }

        if(email2){
            if (!email_reg.test(email2)) {
                _common.prompt('Please enter the correct email format!', 5, 'error');
                $("#email2").css("border-color", "red");
                m.email2.focus();
                return false;
            }
        }
        $("#email").css("border-color", "#CCCCCC");
        $("#email2").css("border-color", "#CCCCCC");
        return true;
    }
    function verifySearch() {
        let startDate = m.startDate.val();
        let endDate = m.endDate.val();
        let storeNo = m.storeNo.attr('k');

        if (!storeNo) {
            _common.prompt('Please select a store first!', 5, 'error');
            m.storeNo.css("border-color","red");
            m.storeNo.focus();
            return false;
        }else {
            m.storeNo.css("border-color","#CCC");
        }
        if (!startDate) {
            _common.prompt('Please select start date!', 5, 'error');
            m.startDate.focus();
            m.startDate.css("border-color","red");
            return false;
        }else {
            m.startDate.css("border-color","#CCC");
        }
        if (!endDate) {
            _common.prompt('Please select end date!', 5, 'error');
            m.endDate.css("border-color","red");
            m.endDate.focus();
            return false;
        }else {
            m.endDate.css("border-color","#CCC");
        }
        var _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
        var _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
        var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
            $("#endDate").focus();
            return false;
        }
        return true;
    }

    //画面按钮点击事件
    var but_event = function () {
        // 修改发票状态
        m.issueBut.on('click', function () {
            let accId = m.accIdParam.val();
            let storeNo = m.storeNoParam.val();
            if (!accId || !storeNo) {
                _common.prompt('Data exception!', 5, 'error');
                return;
            }

            _common.myConfirm("Are you sure you want to invoice?", function (result) {
                if (result != "true") {
                    return false;
                }
                $.myAjaxs({
                    url: url_left + '/updateStatus',
                    async: true,
                    cache: false,
                    type: "post",
                    data: 'accId='+accId+'&storeNo='+storeNo,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            // 变为查看模式
                            // setDisable(true);
                            let record = result.o;
                            _common.prompt("Data modify succeed！", 5, "info");/*保存成功*/
                            getData(record.accId, record.storeNo);
                            m.issueBut.prop('disabled', true);
                        } else {
                            _common.prompt(result.msg, 5, "error");
                        }
                    },
                    error: function (e) {
                        _common.prompt("Data modify failed！", 5, "error");/*保存失败*/
                    }
                });
            });
        });

        // 保存
        m.saveBut.on('click', function () {
            if (!verifySave()) {
                return;
            }

            // 获得选中的头档
            let checkboxTrs = tableGrid.getCheckboxTrs();

            if (checkboxTrs == null || checkboxTrs.length < 1) {
                // 请选择发票信息
                _common.prompt('Please select invoice information!', 5, 'error');
                return;
            }

            let receiptNo = '';
            let amt = 0;
            // 拼接 Receipt No. , 用'; ' 分割
            for (let i = 0; i < checkboxTrs.length; i++) {
                let item = checkboxTrs[i];
                let col = tableGrid.getSelectColValue(item, 'receiptNo,amt');
                amt += parseFloat(reThousands(col['amt']))
                if (i == checkboxTrs.length - 1) {
                    receiptNo += col['receiptNo'];
                } else {
                    receiptNo += col['receiptNo'] + ';';
                }
            }

            let storeNo = m.storeNo.attr('k');

            let customerName = m.customerName.val();
            let companyName = m.companyName.val();
            let tax = m.tax.val();
            let address = m.address.val();
            let phone = m.phone.val();
            let phone2=m.phone2.val();
            let email = m.email.val();
            let email2=m.email2.val();

            let obj = {
                'phone2':phone2,
                'storeNo': storeNo,
                'customerName': customerName,
                'companyName': companyName,
                'tax': tax,
                'email2':email2,
                'address': address,
                'phone': phone,
                'email': email,
                'status': '01', // 开票状态, 保存为 '已提交未开票'
                'receiptNo': receiptNo,
                'amt': amt,
            };

            let _data = JSON.stringify(obj);
            _common.myConfirm("Are you sure you want to save?", function (result) {
                if (result != "true") {
                    return false;
                }
                $.myAjaxs({
                    url: url_left + '/save',
                    async: true,
                    cache: false,
                    type: "post",
                    data: 'record=' + _data,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            // 变为查看模式
                            setDisable(true);
                            _common.prompt("Data saved successfully！", 5, "info");/*保存成功*/
                            let record = result.o;
                            getData(record.accId, record.storeNo);
                        } else {
                            _common.prompt(result.msg, 5, "error");
                        }
                    },
                    error: function (e) {
                        _common.prompt("Data saved failed！", 5, "error");/*保存失败*/
                    }
                });
            });

        });

        m.reset.on("click",function(){
            m.startDate.val("");
            m.endDate.val("");
            m.storeNo.val('');
            m.searchReceiptNo.val('');
            m.storeNo.removeAttr('v');
            m.storeNo.removeAttr('k');
            m.storeNo.css("border-color","#CCC");
            m.startDate.css("border-color","#CCC");
            m.endDate.css("border-color","#CCC");
            selectTrTemp = null;
            _common.clearTable();
        });

        // 查询发票头档信息
        m.search.on('click', function () {
            if (!verifySearch()) {
                return;
            }
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            tableGrid.setting("url", url_left + "/searchInvoice");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData();
        });

        //返回一览
        m.returnsViewBut.on("click", function () {
            _common.updateBackFlg(KEY);
            top.location = url_left;
        });
        //开始时间
        m.startDate.datetimepicker(
            {
                language: 'en',
                format: 'dd/mm/yyyy',
                maxView: 4,
                startView: 2,
                minView: 2,
                autoclose: true,
                todayHighlight: true,
                todayBtn: true,
                initialDate: new Date(),
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#endDate").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
            } else {
                $("#endDate").datetimepicker('setStartDate', null);
            }
        });
        //结束时间
        m.endDate.datetimepicker(
            {
                language: 'en',
                format: 'dd/mm/yyyy',
                maxView: 4,
                startView: 2,
                minView: 2,
                autoclose: true,
                todayHighlight: true,
                todayBtn: true,
                initialDate: new Date(),
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#startDate").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
            } else {
                $("#startDate").datetimepicker('setEndDate', new Date());
            }
        });
    };

    //初始化店铺下拉
    var initAutoMatic = function () {
        a_store = $("#storeNo").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0
        });
    };
    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }

    //表格初始化
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Invoice Information",
            param: paramGrid,
            localSort: true,
            colNames: ["Store No.", "Date and Time", "Receipt No.", "Amount"],
            colModel: [
                {name: "storeNo", type: "text", text: "right", width: "120", ishide: false, css: ""},
                {name: "date", type: "text", text: "left", width: "120", ishide: false, getCustomValue: dateFmt},
                {name: "receiptNo", type: "text", text: "left", width: "120", ishide: false, css: ""},
                {name: "amt", type: "text", text: "left", width: "120", ishide: false, css: "",getCustomValue: getThousands},
            ],//列内容
            // traverseData:data,
            width: "max",//宽度自动
            isPage: true,//是否需要分页
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
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
                selectTrTemp = trObj;
                searchInvoiceItem();
            }
        });

        receiptGridTable = $("#receiptGridTable").zgGrid({
            title: "Invoice Detail",
            param: paramGrid,
            localSort: true,
            colNames: ["No.", "Item Code", "Item Name", "Qty", "Amount"],
            colModel: [
                {name: "no", type: "text", text: "right", width: "120", ishide: false, css: ""},
                {name: "articleId", type: "text", text: "left", width: "120", ishide: false, css: ""},
                {name: "articleName", type: "text", text: "left", width: "120", ishide: false, css: ""},
                {name: "qty", type: "text", text: "left", width: "120", ishide: false, css: "",getCustomValue: getThousands},
                {name: "amt", type: "text", text: "left", width: "120", ishide: false, css: "",getCustomValue: getThousands},
            ],//列内容
            // traverseData:data1,
            width: "max",//宽度自动
            isPage: true,//是否需要分页
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isCheckbox: false,
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
            }
        });
    }

    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }

    //格式化数字类型的日期
    function fmtIntDate(date) {
        if (!date) {
            return '';
        }
        date=date.replace(/-/g,'/');
        return new Date(date).Format('dd/MM/yyyy hh:mm');
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('invoiceEntryEdit');
_start.load(function (_common) {
    _index.init(_common);
});
