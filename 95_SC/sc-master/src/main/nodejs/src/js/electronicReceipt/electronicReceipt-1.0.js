require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('electronicReceipt', function () {
    var self = {};
    var url_left = "",
        url_root = "",
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
        // 检索项
        aRegion: null,
        aCity: null,
        aDistrict: null,
        aStore: null,
        hidStore: null,
        saleStartDate: null,
        saleEndDate: null,
        posNo: null,
        saleNo: null,
        keywords: null,
        keywordTemp: null,
        _startIndex: null,
        _oldContent: null,
        transSaleNo: null,
        tranType: null,
        receiptType: null,
        // 按钮
        search: null,
        reset: null,
        nextBtn: null,
        toPdf: null
    };
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = url_root + "/electronicReceipt";
        // 按钮事件绑定
        but_event();

        // 初始化店铺运营组织检索
        initOrganization();
        initAutoMatic();
        // 初始化检索日期
        _common.initDate(m.saleStartDate, m.saleEndDate);
    }

    var initOrganization = function () {
        /**
         *@Author Ldd
         * 2020/9/21
         */

        // 初始化，子级禁用
        // $("#aCity").attr("disabled", true);
        // $("#aDistrict").attr("disabled", true);
        // $("#aStore").attr("disabled", true);
        // $("#cityRefresh").hide();
        // $("#cityRemove").hide();
        // $("#districtRefresh").hide();
        // $("#districtRemove").hide();
        // $("#storeRefresh").hide();
        // $("#storeRemove").hide();
        // 输入框事件绑定
        a_region = $("#aRegion").myAutomatic({
            url: url_root + "/roleStore/getRegion",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_city);
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                //$("#aCity").attr("disabled", true);
                //	$("#aDistrict").attr("disabled", true);
                //$("#aStore").attr("disabled", true);
                //$("#cityRefresh").hide();
                //$("#cityRemove").hide();
                //$("#districtRefresh").hide();
                //$("#districtRemove").hide();
                //$("#storeRefresh").hide();
                //	$("#storeRemove").hide();
                // 	if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                // 		$("#aCity").attr("disabled", false);
                // 		$("#cityRefresh").show();
                // 		$("#cityRemove").show();
                // 	}
                var rinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&regionCd=" + rinput;
                $.myAutomatic.replaceParam(a_city, str);
                $.myAutomatic.replaceParam(a_district, str);
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_city = $("#aCity").myAutomatic({
            url: url_root + "/roleStore/getCity",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                // $("#aDistrict").attr("disabled", true);
                // $("#aStore").attr("disabled", true);
                // $("#districtRefresh").hide();
                // $("#districtRemove").hide();
                // $("#storeRefresh").hide();
                // $("#storeRemove").hide();
                // if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                // 	$("#aDistrict").attr("disabled", false);
                // 	$("#districtRefresh").show();
                // 	$("#districtRemove").show();
                // }
                var rinput = $("#aRegion").attr("k");
                var cinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&cityCd=" + cinput;
                $.myAutomatic.replaceParam(a_district, str);
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_district = $("#aDistrict").myAutomatic({
            url: url_root + "/roleStore/getDistrict",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_store);
                // $("#aStore").attr("disabled", true);
                // $("#storeRefresh").hide();
                // $("#storeRemove").hide();
                // if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                // 	$("#aStore").attr("disabled", false);
                // 	$("#storeRefresh").show();
                // 	$("#storeRemove").show();
                // }
                var rinput = $("#aRegion").attr("k");
                var cinput = $("#aCity").attr("k");
                var dinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&districtCd=" + dinput;
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_store = $("#aStore").myAutomatic({
            url: url_root + "/roleStore/getStore",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $('#hidStore').val(thisObj.attr('k'));
                getSelectPosId(thisObj.attr('k'));
                m.posNo.attr("disabled", false);
            },
            cleanInput: function () {
                m.posNo.attr("disabled", true);
                m.posNo.find("option:not(:first)").remove();
            }
        });

        // 选值栏位清空按钮事件绑定
        $("#regionRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_city);
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $.myAutomatic.replaceParam(a_city, null);
            $.myAutomatic.replaceParam(a_district, null);
            $.myAutomatic.replaceParam(a_store, null);
            // $("#aCity").attr("disabled", true);
            // $("#aDistrict").attr("disabled", true);
            // $("#aStore").attr("disabled", true);
            // $("#cityRefresh").hide();
            // $("#cityRemove").hide();
            // $("#districtRefresh").hide();
            // $("#districtRemove").hide();
            // $("#storeRefresh").hide();
            // $("#storeRemove").hide();
        });

        $("#cityRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $.myAutomatic.replaceParam(a_district, null);
            $.myAutomatic.replaceParam(a_store, null);
            // $("#aDistrict").attr("disabled", true);
            // $("#aStore").attr("disabled", true);
            // $("#districtRefresh").hide();
            // $("#districtRemove").hide();
            // $("#storeRefresh").hide();
            // $("#storeRemove").hide();
        });
        $("#districtRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_store);
            $.myAutomatic.replaceParam(a_store, null);
            // $("#aStore").attr("disabled", true);
            // $("#storeRefresh").hide();
            // $("#storeRemove").hide();
        });

        // 如果只有一条店铺权限，则默认选择
        $.myAjaxs({
            url: url_root + "/roleStore/getStoreByRole",
            async: true,
            cache: false,
            type: "post",
            data: null,
            dataType: "json",
            success: function (re) {
                if (re.success) {
                    var obj = re.o;
                    $("#aCity").attr("disabled", false);
                    $("#aDistrict").attr("disabled", false);
                    $("#aStore").attr("disabled", false);
                    $("#cityRefresh").show();
                    $("#cityRemove").show();
                    $("#districtRefresh").show();
                    $("#districtRemove").show();
                    $("#storeRefresh").show();
                    $("#storeRemove").show();
                    $.myAutomatic.setValueTemp(a_region, obj.regionCd, obj.regionName);
                    $.myAutomatic.setValueTemp(a_city, obj.cityCd, obj.cityName);
                    $.myAutomatic.setValueTemp(a_district, obj.districtCd, obj.districtName);
                    $.myAutomatic.setValueTemp(a_store, obj.storeCd, obj.storeName);
                    // 替换子级查询参数
                    var str = "&regionCd=" + obj.regionCd;
                    $.myAutomatic.replaceParam(a_city, str);
                    str = str + "&cityCd=" + obj.cityCd;
                    $.myAutomatic.replaceParam(a_district, str);
                    str = str + "&districtCd=" + obj.districtCd;
                    $.myAutomatic.replaceParam(a_store, str);
                }
            }
        });
    }
    // 根据商店CD 获取小票类型
    var initAutoMatic = function () {
        /*receiptType = $("#receiptType").myAutomatic({
            url: url_left + "/getReceiptType",
            ePageSize: 5,
            startCount: 0,
            param: [
                {
                    'k': 'storeCd',
                    'v': 'hidStore'

                }, {
                    'k': 'posId',
                    'v': 'posNo'
                }, {
                    'k': 'startDate',
                    'v': 'saleStartDate'
                }, {
                    'k': 'endDate',
                    'v': 'saleEndDate'
                },
            ],
        })*/

        itemInfo = $("#itemInfo").myAutomatic({
            url: url_left + "/getSa0020Item",
            ePageSize: 5,
            startCount: 0,
            param: [
                {
                    'k': 'storeCd',
                    'v': 'hidStore'
                }, {
                    'k': 'posId',
                    'v': 'posNo'
                },
                {
                    'k': 'startDate',
                    'v': 'saleStartDate'
                },
                {
                    'k': 'endDate',
                    'v': 'saleEndDate'
                }
            ]
        });
    }

    // 画面按钮点击事件
    var but_event = function () {
        /*$("#receiptRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(receiptType);

        });*/
        $("#itemInfoRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(itemInfo);

        });
        // 检索按钮点击事件
        m.search.on("click", function () {
            if (verifySearch()) {
                var _temp = m._oldContent.val("");
                $("#receipts").html(_temp);
                // 拼接检索参数
                setParamJson();
                // 查询数据
                $.myAjaxs({
                    url: url_left + "/getData",
                    async: true,
                    cache: false,
                    type: "post",
                    data: "searchJson=" + m.searchJson.val(),
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            var _result = "";
                            var _list = result.o;
                            for (var i = 0; i < _list.length; i++) {
                                var temp = _list[i].receiptContent;
                                _result = _result + "<pre>" + temp + "</pre>";
                            }
                            $("#receipts").html(_result);
                            // 重置关键字检索记录参数
                            m.keywordTemp.val("");
                            m._startIndex.val("0");
                            m._oldContent.val("");
                        } else {
                            _common.prompt(result.msg, 5, "info");
                        }
                    },
                    error: function (e) {
                        _common.prompt("Search for receipt failed!", 5, "error"); // 查询小票失败
                    }
                });
            }
        });
        // 清空按钮事件
        m.reset.on("click", function () {
            _common.myConfirm("Are you sure you want to reset?", function (result) {
                if (result == "true") {
                    $("#receiptRemove").click();
                    $("#itemInfoRemove").click();
                    $("#regionRemove").click();
                    m.saleStartDate.val("");
                    m.saleEndDate.val("");
                    m.posNo.val("");
                    m.saleNo.val("");
                    m.tranType.val("");
                    //$("#receipts").remove();
                    var _temp = m._oldContent.val("");
                    $("#receipts").html(_temp);
                }
            })
        });
        // 关键字监听事件
        m.keywords.on("change", function () {
            // 查询结果还原，记录参数清空
            var _temp = m._oldContent.val();
            if (_temp == null || _temp == '') {
                return false;
            }
            $("#receipts").html(_temp);
            m.keywordTemp.val("");
            m._startIndex.val("0");
            m._oldContent.val("");
        });
        // 关键字检索事件
        m.nextBtn.on("click", function () {
            // 获取检索内容
            var _sel = m.keywords.val();
            if (_sel == null || $.trim(_sel) == '') {
                m.keywords.focus();
                _common.prompt("The need to find cannot be empty!", 3, "info"); // Keywords 不能为空
                return false;
            } else {
                _sel = $.trim(_sel);
            }
            // 获取内容
            var _content = $("#receipts").html().trim();
            if (_content == null || _content == '') {
                _common.prompt("The receipt is empty!", 3, "info"); // 没有内容可以查找
                return false;
            }
            // 获取查找下标、查找内容长度
            var _length = _sel.length;
            var _startIndex = m._startIndex.val();
            if (_sel != m.keywordTemp.val()) {
                // 记录key、初始内容
                m.keywordTemp.val(_sel);
                m._oldContent.val(_content);
                _startIndex = _content.indexOf(_sel, _startIndex);
                if (_startIndex == -1) {
                    _common.prompt("No data was queried!", 3, "info"); // 未查找到 Keywords
                    return false;
                }
            } else if (_sel == m.keywordTemp.val()) {
                // 获取初始内容
                _content = m._oldContent.val();
                var _index = parseInt(_startIndex) + parseInt(_length);
                _startIndex = _content.indexOf(_sel, _index);
                if (_startIndex == -1) {
                    _common.prompt("Searched out!", 5, "info"); // 已查找完毕
                    return false;
                }
            } else {
                return false;
            }
            // 记录当前查找位置下标
            m._startIndex.val(_startIndex);
            // 截取、拼接
            var _start, _end;
            _start = _content.substring(0, _startIndex);
            _end = _content.substring(_startIndex + _length);
            _sel = "<span style='background-color:orangered;'>" + _sel + "</span>";
            var newContent = _start + _sel + _end;
            $("#receipts").html(newContent);
            var mainContainer = $("#receipts"),
                scrollToContainer = mainContainer.find("span");
            //动画效果
            mainContainer.animate({
                scrollTop: scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop() - 200
            }, 50);//2秒滑动到指定位置
        });

        m.toPdf.on("click", function () {
            if (verifySearch()) {
                // 拼接检索参数
                setParamJson();
                $.myAjaxs({
                    url: url_left + "/toPdf",
                    async: true,
                    cache: false,
                    type: "post",
                    data: "searchJson=" + m.searchJson.val(),
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            window.open(url_left + "/writerPdf", "toPdf");
                        } else {
                            _common.prompt(result.msg, 5, "info");
                        }
                    },
                    error: function (e) {
                        _common.prompt("Search for receipt failed!", 5, "error"); // 查询小票失败
                    }
                });
            }
        });
    };

    //获取pos机号下拉
    var getSelectPosId = function (storeCd) {
        if (storeCd != null && storeCd != '') {
            $.myAjaxs({
                url: url_root + "/paymentMode/edit/getPosId",
                async: true,
                cache: false,
                type: "post",
                data: "storeCd=" + storeCd,
                dataType: "json",
                success: function (result) {
                    m.posNo.find("option:not(:first)").remove();
                    if (result.success) {
                        for (var i = 0; i < result.data.length; i++) {
                            var optionValue = result.data[i].posId;
                            var optionText = result.data[i].posName;
                            m.posNo.append(new Option(optionText, optionValue));
                        }
                        m.posNo.find("option:first").prop("selected", true);
                    }
                },
                error: function (e) {
                    _common.prompt("request failed!", 5, "error");
                },
                complete: _common.myAjaxComplete
            });
        }
    }

    function judgeNaN(value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function () {
        let _StartDate = null;
        if (!$("#saleStartDate").val()) {
            _common.prompt("Please select a start date!", 3, "info");/*请选择开始日期*/
            $("#saleStartDate").focus();
            return false;
        } else {
            _StartDate = new Date(fmtDate($("#saleStartDate").val())).getTime();
            if (judgeNaN(_StartDate)) {
                _common.prompt("Please enter a valid date!", 3, "info");
                $("#saleStartDate").focus();
                return false;
            }
        }

        if (m.aStore.attr('k') === null || m.aStore.attr('k') === '') {
            _common.prompt("Please select a store!", 3, "info");
            m.aStore.focus();
            return false;
        }

        if (m.saleNo.val() === null || m.saleNo.val() === '') {
            _common.prompt("Please enter the receipt no!", 3, "info");
            m.saleNo.focus();
            return false;
        }

        let _EndDate = null;
        if (!$("#saleEndDate").val()) {
            _common.prompt("Please select a end date!", 3, "info");/*请选择结束日期*/
            $("#saleEndDate").focus();
            return false;
        } else {
            _EndDate = new Date(fmtDate($("#saleEndDate").val())).getTime();
            if (judgeNaN(_EndDate)) {
                _common.prompt("Please enter a valid date!", 3, "info");
                $("#saleEndDate").focus();
                return false;
            }
        }
        if (_StartDate > _EndDate) {
            $("#saleEndDate").focus();
            _common.prompt("The start date cannot be greater than the end date!", 3, "info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate - _StartDate) / (1000 * 3600 * 24)));
        if (difValue > 62) {
            _common.prompt("Query Period cannot exceed 62 days!", 3, "info"); // 日期期间取值范围不能大于62天
            $("#saleEndDate").focus();
            return false;
        }
        let reg = /^[0-9]*$/;
        /*let temp = m.posNo.val();
        if(temp!=null && $.trim(temp)!='' && !reg.test(temp)){
            m.posNo.focus();
            _common.prompt("The POS number must be a pure number!",3,"info"); // POS机号必须是纯数字
            return false;
        }*/
        let temp = m.saleNo.val();
        if (temp != null && $.trim(temp) != '' && !reg.test(temp)) {
            m.saleNo.focus();
            _common.prompt("The sales sequence must be a pure number!", 3, "info"); // 销售序号必须是纯数字
            return false;
        }
        return true;
    }

    // 拼接检索参数
    var setParamJson = function () {
        // 日期格式转换
        var _startDate = fmtStringDate(m.saleStartDate.val()) || null;
        var _endDate = fmtStringDate(m.saleEndDate.val()) || null;
        var _no = m.saleNo.val().trim() || 0;
        // 创建请求字符串
        var searchJsonStr = {
            articleId: $("#ItemInfo").attr("k"),
            // tranType: $("#receiptType").attr("v"),
            tranType: $("#receiptType").val(),
            regionCd: $("#aRegion").attr("k"),
            cityCd: $("#aCity").attr("k"),
            districtCd: $("#aDistrict").attr("k"),
            storeCd: $("#aStore").attr("k"),
            saleStartDate: _startDate,
            saleEndDate: _endDate,
            posNo: m.posNo.val().trim(),
            saleNo: _no
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, "$3-$2-$1");
        return res;
    }

    // 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date) {
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
        return res;
    }

    // 格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('electronicReceipt');
_start.load(function (_common) {
    _index.init(_common);
});
