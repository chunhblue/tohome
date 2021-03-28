require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("checkboxGrid");
require("myAutomatic");
require("JsBarcode");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('priceLabel', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        aItem = null,
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {};
    var m = {
        reset: null,
        toKen: null,
        use: null,
        articleId: null,
        type: null,
        tableGrid: null,
        search: null,//检索按钮
        searchJson: null,
        startDate: null,
        endDate: null,
        //大分类
        dep: null,
        pma: null,
        category: null,
        subCategory: null,
        a_item_refresh: null,
        a_item_clear: null,
        aRegion: null,
        aCity: null,
        aDistrict: null,
        aStore: null,
        toPdf: null,
        regionRemove: null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //验证检索项是否合法
    var verifySearch = function () {
        if (!$("#startDate").val()) {
            _common.prompt("Please select a start date!", 3, "info");/*请选择开始日期*/
            $("#startDate").focus();
            return false;
        }
        if (!$("#endDate").val()) {
            _common.prompt("Please select a end date!", 3, "info");/*请选择结束日期*/
            $("#endDate").focus();
            return false;
        }

        let start = new Date(fmtDate($("#startDate").val())).getTime();
        let end = new Date(fmtDate($("#endDate").val())).getTime();

        if (start > end) {
            $("#endDate").focus();
            _common.prompt("The start date cannot be greater than the end date!", 3, "info");/*开始时间不能大于结束时间*/
            return false;
        }

        let difValue = parseInt(Math.abs((end - start) / (1000 * 3600 * 24)));
        if (difValue > 62) {
            _common.prompt("Query Period cannot exceed 62 days!!", 3, "info"); // 日期期间取值范围不能大于62天
            $("#endDate").focus();
            return false;
        }
        return true;
    }

    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/priceLabel";
        // 初始化列表
        initTable1();
        // 初始化下拉选项
        getSelectVal();
        // 初始化组织架构
        _common.initOrganization();
        _common.initCategoryAutoMatic();
        // 初始化事件
        but_event();
        // 初始化检索日期
        initDate(m.startDate, m.endDate);
        isDisabledBtn();
    }

    var getSelectVal = function () {
        initSelectOptions("Price Label Type", "00830", function (result) {
            let selectObj = $("#type");
            for (let i = 0; i < result.length; i++) {
                let optionValue = result[i].codeValue;
                let optionText = result[i].codeName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });

        aItem = $("#articleId").myAutomatic({
            url: url_left + "/getItemList",
            ePageSize: 10,
            startCount: 0
        });
    }

    // 初始化下拉列表
    function initSelectOptions(title, code, fun) {
        // 共通请求
        $.myAjaxs({
            url: systemPath + "/cm9010/getCode",
            async: false,
            cache: false,
            type: "post",
            data: "codeValue=" + code,
            dataType: "json",
            success: fun,
            error: function (e) {
                _common.prompt(title + " Failed to load data!", 5, "error");
            },
            complete: _common.myAjaxComplete
        });
    }

    // 拼接搜素参数
    var but_event = function () {
        m.reset.on("click", function () {
            m.a_item_clear.click();
            m.regionRemove.click();
            m.type.val("");
            m.startDate.val("");
            m.endDate.val("");
            selectTrTemp = null;
            _common.clearTable();
        });

        m.search.on("click", function () {
            if (!verifySearch()) {
                return;
            }
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            tableGrid.setting("url", url_left + '/search');
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData();
        });

        $("#printAll").on("click", function () {
            if (!verifySearch()) {
                return;
            }
            setParamJson();

            let params = encodeURIComponent(JSON.stringify(m.searchJson.val()));

            var url = url_left + "/print?&flg=printAll&searchJson=" + params;
            let me = document;
            _common.myConfirm("Are you sure you want to print all the price labels?",function(result){
                if (result=="true"){
                    // 采用post 打开页面
                    post(me,url,null);
                }
            })
        });

        $("#print").on("click", function () {
            if (!verifySearch()) {
                return;
            }
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            let _params = getSelItems();

            if (_params.length < 1) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }

            let params = encodeURIComponent(JSON.stringify(_params));

            var url = url_left + "/print?&flg=print&params=" + params;
            // 采用post 打开页面
            post(document,url,null)
        });

        $("#preview").on("click", function () {
            if (!verifySearch()) {
                return;
            }
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();

            let _params = getSelItems();

            if (_params.length < 1) {
                _common.prompt("Please select at least one row of data!",5,"error");
                return;
            }

            let params = encodeURIComponent(JSON.stringify(_params));
            var url = url_left + "/print?&flg=preview" + "&params=" + params;
            post(document,url,null)
        });
        $("#export").on("click", function () {
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            var url = url_left + "/export?" + paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
        });
        $("#toPdf").on("click", function () {
            if (!verifySearch()) {
                return;
            }
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            var url = url_left + "/toPdf?" + paramGrid;
            window.open(encodeURI(url), "toPdf");
            // $.myAjaxs({
            //     url: url_left + "/toPdf",
            //     async: true,
            //     cache: false,
            //     type: "post",
            //     data: "searchJson="+m.searchJson.val(),
            //     dataType: "json",
            //     success: function (result) {
            //         if (result.success){
            //             if(result.success){
            //                 window.open(url_left + "/writerPdf", "toPdf");
            //             }else{
            //                 _common.prompt(result.msg,5,"info");
            //             }
            //         }
            //
            //     },
            //     error : function(e){
            //         _common.prompt("Search for receipt failed!",5,"error"); // 查询小票失败
            //     }
            // })
        })
    }

    // 初始化查询日期
    var initDate = function (startDate, endDate) {
        if (startDate) {
            startDate.datetimepicker({
                language: 'en',
                format: 'dd/mm/yyyy',
                maxView: 4,
                startView: 2,
                minView: 2,
                autoclose: true,
                todayHighlight: true,
                todayBtn: true,
            }).on('changeDate', function (ev) {
                if (endDate) {
                    if (ev.date) {
                        endDate.datetimepicker('setStartDate', new Date(ev.date.valueOf()))
                    } else {
                        endDate.datetimepicker('setStartDate', null);
                    }
                }
            });
            // 默认当天
            let _start = new Date();
            startDate.val(_start.Format('dd/MM/yyyy'));
        }

        if (endDate) {
            endDate.datetimepicker({
                language: 'en',
                format: 'dd/mm/yyyy',
                maxView: 4,
                startView: 2,
                minView: 2,
                autoclose: true,
                todayHighlight: true,
                todayBtn: true,
            }).on('changeDate', function (ev) {
                if (startDate) {
                    if (ev.date) {
                        startDate.datetimepicker('setEndDate', new Date(ev.date.valueOf()))
                    } else {
                        startDate.datetimepicker('setEndDate', null);
                    }
                }
            });
            // 结束日期 当前日期 加 三天
            let sumdate = new Date().getTime() + (86400000 * 3);
            endDate.val(new Date(sumdate).Format('dd/MM/yyyy'));
        }
    }

    var getSelItems = function () {
        var checkboxTrsList = tableGrid.getCheckboxTrs();
        let _params = [];
        for (var i = 0; i < checkboxTrsList.length; i++) {
            let cols = tableGrid.getSelectColValue(checkboxTrsList[i], "storeCd,priceLabelVnName,priceLabelEnName,depCd,pmaCd,categoryCd,subCategoryCd,articleName,barcode,articleId,oldPrice,newPrice,effectiveStartDate");
            let _param = {
                storeCd: cols['storeCd'],
                priceLabelVnName: cols['priceLabelVnName'],
                priceLabelEnName: cols['priceLabelEnName'],
                depCd: cols['depCd'],
                pmaCd: cols['pmaCd'],
                categoryCd: cols['categoryCd'],
                subCategoryCd: cols['subCategoryCd'],
                articleName: cols['articleName'],
                barcode: cols['barcode'],
                articleId: cols['articleId'],
                oldPrice: cols['oldPrice'],
                newPrice: cols['newPrice'],
                effectiveStartDate: cols['effectiveStartDate'],
            };
            _params.push(_param);
        }

        return _params;
    }

    function post(document,url, params) {
        // 创建form元素
        var temp_form = document.createElement("form");
        // 设置form属性
        temp_form.action = url;
        temp_form.target = "_blank";
        temp_form.method = "post";
        temp_form.style.display = "none";
        // 处理需要传递的参数
        for (var x in params) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = params[x];
            temp_form .appendChild(opt);
        }
        document.body.appendChild(temp_form);
        // 提交表单
        temp_form .submit();
    }

    var setParamJson = function () {
        let aRegion = m.aRegion.attr('k');
        let aCity = m.aCity.attr('k');
        let aDistrict = m.aDistrict.attr('k');
        let aStore = m.aStore.attr('k');
        let type = m.type.val();
        let articleId = m.articleId.attr('k');
        let startYmd = formatDate(m.startDate.val());
        let endYmd = formatDate(m.endDate.val());
        let searchJson = {
            'pmaCd': $("#pma").attr("k"),
            'depCd': m.dep.attr("k"),
            'categoryCd': m.category.attr("k"),
            'subCategoryCd': m.subCategory.attr("k"),
            'regionCd': aRegion,
            'cityCd': aCity,
            'districtCd': aDistrict,
            'storeCd': aStore,
            'type': type,
            'articleId': articleId,
            'startYmd': startYmd,
            'endYmd': endYmd,
        }
        m.searchJson.val(JSON.stringify(searchJson));
    }

    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }

    function isDisabledBtn() {
        var _list = tableGrid.getCheckboxTrs();
        if (_list.length == 0) {
            $("#preview").prop("disabled", false);
            $("#print").prop("disabled", false);
            // $("#print").prop("disabled",true);
            // $("#preview").prop("disabled",true);
        } else if (_list.length != 0) {
            $("#preview").prop("disabled", false);
            $("#print").prop("disabled", false);
        }
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, "$3-$2-$1");
        return res;
    }

    // 日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        if (value != null && value.trim() != '' && value.length == 8) {
            value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        }
        return $(tdObj).text(value);
    }

    var getThousands = function (tdObj, value) {
        if (value == null || value == '') {
            return $(tdObj).text('');
        }
        return $(tdObj).text(_common.toThousands(value));
    }
    // 获取复选框的内容  存入数组

    //表格初始
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Query Result",
            param: paramGrid,
            localSort: true,
            lineNumber: false,
            colNames: ["Store No.", "Store Name", "Price Label VN Name", "Price Label EN Name", "Top Department Cd", "Top Department", "Department Cd", "Department", "Category", "CategoryCd", "Sub Category", "Sub CategoryCd", "Item Name", "Item Barcode", "Item Code", "Old Price", "New Price", "Effective Start Date"],
            colModel: [
                {name: "storeCd", type: "text", text: "left", width: "90", ishide: false,},
                {name: "storeName", type: "text", text: "left", width: "130", ishide: false,},
                {name: "priceLabelVnName", type: "text", text: "left", width: "90", ishide: false,},
                {name: "priceLabelEnName", type: "text", text: "left", width: "90", ishide: false,},

                {name: "depCd", type: "text", text: "left", width: "130", ishide: true,},
                {name: "depName", type: "text", text: "left", width: "130", ishide: false,},
                {name: "pmaCd", type: "text", text: "left", width: "130", ishide: true,},
                {name: "pmaName", type: "text", text: "left", width: "130", ishide: false,},
                {name: "categoryName", type: "text", text: "left", width: "130", ishide: false,},
                {name: "categoryCd", type: "text", text: "left", width: "130", ishide: true,},
                {name: "subCategoryName", type: "text", text: "left", width: "130", ishide: false,},
                {name: "subCategoryCd", type: "text", text: "left", width: "130", ishide: true,},
                {name: "articleName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "barcode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "articleId", type: "text", text: "right", width: "130", ishide: false,},
                {
                    name: "oldPrice",
                    type: "text",
                    text: "right",
                    width: "130",
                    ishide: false,
                    getCustomValue: getThousands
                },
                {
                    name: "newPrice",
                    type: "text",
                    text: "right",
                    width: "130",
                    ishide: false,
                    getCustomValue: getThousands
                },
                {
                    name: "effectiveStartDate",
                    type: "text",
                    text: "center",
                    width: "130",
                    ishide: false,
                    getCustomValue: dateFmt
                },
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: true,
            buttonGroup: [
                {
                    butType: "custom",
                    butHtml: "<button id='printAll' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print All</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print Price Label</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='preview' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-list-alt'></span> Preview</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span>Export</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='toPdf' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-log-out icon-right'></span>To PDF</button>"
                },
            ],
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                isDisabledBtn()
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常isDisabledBtn左侧点击
                selectTrTemp = trObj;
                // isDisabledBtn()
            }
        });
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('priceLabel');
_start.load(function (_common) {
    _index.init(_common);
});
