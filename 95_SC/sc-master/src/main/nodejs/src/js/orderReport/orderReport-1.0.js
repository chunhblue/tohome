require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");
define('orderReport', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        reThousands = null,
        toThousands = null,
        getThousands = null,
        page = 1, // 当前页数, 初始为 1
        rows = 10; // 每页显示条数, 默认为 10;
    var m = {
        businessDate: null,
        author: null,
        am: null,
        dep: null,
        pma: null,
        reset:null,
        category: null,
        subCategory: null,
        searchJson: null,
        articleId: null,
        articleName: null,
        dailyTable: null,
        order_date: null,
        export:null,
        search: null,
        barcode:null,
    }
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/orderReportQuery";
        systemPath = _common.config.surl;
        reThousands = _common.reThousands;
        toThousands = _common.toThousands;
        getThousands = _common.getThousands;
        initAutoMatic();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        _common.initCategoryAutoMatic();
        var businessDate = m.businessDate.val();
        m.order_date.val(fmtIntDate(businessDate));
        but_event();



    }

    var initAutoMatic = function () {
        m.order_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        })
        // 获取区域经理
        am = $("#am").myAutomatic({
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize: 10,
            startCount: 0,
        });
    }
    var but_event = function () {
        m.reset.click(function () {
            $("#regionRemove").click();
            $("#depRemove").click();
            $("#amRemove").click();

            m.articleId.val('');
            m.articleName.val('');
            page = 1;
            // 清除分页
            _common.resetPaging();
            $("#dailyTable").find("tr:not(:first)").remove();
            var businessDate = m.businessDate.val();
            m.order_date.val(fmtIntDate(businessDate));
        })
        m.search.on('click', function () {
            if (verifySearch()) {
                _common.loadPaging(1,1,1,10);
                page=1;
                setParamJson();
                getData(page, rows);
            }
        });
        // 导出按钮点击事件
        m.export.on("click",function(){
            if(verifySearch()) {
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });
    }
    var verifySearch = function () {
        // if ($("#aStore").attr("k") == null || $("#aStore").attr("k") == "") {
        //     _common.prompt("Please select a store!",5,"error"); // 结束日期不可以为空
        //     $("#aRegion").focus();
        //     $("#aRegion").css("border-color","red");
        // }else {
        //     $("#aRegion").css("border-color","#CCC");
        // }
        if(m.order_date.val()==""||m.order_date.val()==null){
            _common.prompt("Please enter a Order Date!",5,"error"); // 日期不可以为空
            $("#order_date").focus();
            $("#order_date").css("border-color","red");
            return false;
        }else if(_common.judgeValidDate(m.order_date.val())){
            _common.prompt("Please enter a valid date!",3,"info");
            $("#order_date").focus();
            return false;
        }else {
            $("#order_date").css("border-color","#CCC");
        }
        return true;
    }
    var getData = function (page, rows) {
        let record = m.searchJson.val();
        $.myAjaxs({
            url: url_left + "/search",
            async: true,
            cache: false,
            type: "post",
            data: 'SearchJson=' + record,
            dataType: "json",
            success: function (result) {
                var trList = $("#dailyTable  tr:not(:first)");
                trList.remove();
                if (result.success) {
                    let dataList = result.o.data;
                    // 总页数
                    let totalPage = result.o.totalPage;
                    // 总条数
                    let count = result.o.count;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td title="'+isEmpty(item.storeCd)+'" style="text-align: left">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="'+ isEmpty(item.storeName)+'" style="text-align: left">' + isEmpty(item.storeName) + '</td>' +
                            '<td title="'+isEmpty(item.orderDate) +'" style="text-align: center">' + isEmpty(item.orderDate) + '</td>' +
                            '<td title="'+  isEmpty(item.articleId)+'" style="text-align: right">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="'+isEmpty(item.articleName) +'" style="text-align: left">' + isEmpty(item.articleName) + '</td>' +
                            '<td  title="'+isEmpty(item.barcode) +'" style="text-align: right">' + isEmpty(item.barcode) + '</td>' +
                            '<td  title="'+isEmpty(item.uom) +'" style="text-align: left">' + isEmpty(item.uom) + '</td>' +
                        //    '<td style="text-align: left">' + isEmpty(item.articleName) + '</td>' +
                            '<td  title="'+ isNum(item.autoOrderQty) +'"style="text-align: right">' + isNum(item.autoOrderQty) + '</td>' +
                            '<td  title="'+isEmpty(item.realQty)+'" style="text-align: right">' + isEmpty(item.realQty) + '</td>' +
                            '<td   title="'+ isNum1(item.variance) +'%'+'" style="text-align: right">' + isNum1(item.variance) +'%'+ '</td>' +
                            '<td  title="'+isNum(item.psd) +'" style="text-align: right">' + isNum(item.psd) + '</td>' +
                            '</tr>';
                        m.dailyTable.append(tempTrHtml);
                    }
                    _common.loadPaging(totalPage,count,page,rows);
                }
                // 激活 分页按钮点击
                but_paging();
            },
            error: function (e) {

            }
        })
    }
    // 分页按钮事件
    var but_paging = function () {
        $('.pagination li').on('click',function () {
            page = $($(this).context).val();
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                // 分页获取数据
                getData(page,rows);
            }
        });
    }
    var setParamJson = function () {
        let am = m.am.attr('k');
        let dep = m.dep.attr('k');
        let pma = m.pma.attr('k');
        let category = m.category.attr('k');
        let subCategory = m.subCategory.attr('k');
        let orderDate = formatDate(m.order_date.val());
        let barcode = m.barcode.val().trim();
        let articleName = m.articleName.val().trim();
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd': $("#aRegion").attr("k"),
            'cityCd': $("#aCity").attr("k"),
            'districtCd': $("#aDistrict").attr("k"),
            'storeCd': $("#aStore").attr("k"),
            'am': am,
            'depCd': dep,
            'pmaCd': pma,
            'categoryCd': category,
            'subCategoryCd': subCategory,
            'orderDate': orderDate,
            'barcode': barcode,
            'articleName': articleName,
            'page': page,
            'rows': rows,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }
    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '0';
        }
        return str;
    };
    var isNum = function (str) {
        if (str == null || str == undefined || str == '') {
            return '0';
        }

        return str;
    }
    var isNum1 = function (str) {
        if (str == null || str == undefined || str == '') {
            return '0';
        }

        return str*100;
    };
    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }
    self.init = init;
    return self;
})
var _start = require('start');
var _index = require('orderReport');
_start.load(function (_common) {
    _index.init(_common);
})