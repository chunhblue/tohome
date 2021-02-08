require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('dcReceiptDaily', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        reThousands = null,
        toThousands = null,
        getThousands = null,
        page = 1, // 当前页数, 初始为 1
        rows = 10; // 每页显示条数, 默认为 10;

    var m = {
        toKen: null,
        use: null,
        up: null,
        search: null,
        reset: null,
        export: null,
        print: null,
        receiveId: null,
        storeName: null,
        dc: null,
        dailyTable: null,
        startDate: null,
        endDate: null,
        articleId: null,
        articleName: null,
        author: null,
        am: null,
        searchJson: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        dep : null,
        pma : null,
        om: null,
        category : null,
        subCategory : null,
        barcode:null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //拼接检索参数
    var setParamJson = function () {
        let receiveId = m.receiveId.val().trim();
        let barcode = m.barcode.val().trim();
        let dc = m.dc.val().trim();
        let articleName = m.articleName.val().trim();
        let am = m.am.attr('k');
        let om = m.om.attr('k');
        let dep = m.dep.attr('k');
        let pma = m.pma.attr('k');
        let category = m.category.attr('k');
        let subCategory = m.subCategory.attr('k');
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        // 创建请求字符串
        let searchJsonStr = {
            'receiveId': receiveId,
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'barcode': barcode,
            'articleName': articleName,
            'am': am,
            'om': om,
            'dc': dc,
            'depCd':dep,
            'pmaCd': pma,
            'categoryCd': category,
            'subCategoryCd': subCategory,
            'startDate': _startDate,
            'endDate': _endDate,
            'page': page,
            'rows': rows,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/dcReceiptDaily";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 初始化 大中小分类
        _common.initCategoryAutoMatic();
        initAutoMatic();

        but_event();
        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
    }

    //初始化下拉
    var initAutoMatic = function () {
        // 获取区域经理
        am = $("#am").myAutomatic({
            url: url_left + "/getAMList",
            ePageSize: 10,
            startCount: 0,
        });
        // 获取OM
        om = $("#om").myAutomatic({
            url: url_left +"/getOMList",
            ePageSize: 10,
            startCount: 0,
        });
    }



    var but_event = function () {
        m.reset.click(function () {
            $("#regionRemove").click();
            $("#depRemove").click();
            m.articleId.val('');
            m.dc.val('');
            m.am.val('').attr('k','').attr('v','');
            m.om.val('').attr('k','').attr('v','');
            m.startDate.val('');
            m.endDate.val('');
            m.articleName.val('');
            m.receiveId.val('');
            $("#startDate").css("border-color","#CCC");
            $("#endDate").css("border-color","#CCC");
            page=1;
            // 清除分页
            _common.resetPaging();
            $("#dailyTable").find("tr:not(:first)").remove();
        });

        m.search.click(function () {
            if(verifySearch()) {
                _common.loadPaging(1,1,1,10);
                page=1;
                setParamJson();
                getData(page,rows);
            }
        })

        // 打印按钮点击事件
        m.print.on("click",function(){
            if(verifySearch()) {
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "excelprint", "width=1400,height=600,scrollbars=yes");
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
    };

    /**
     * 分页获取数据
     * @param page 当前页数
     * @param rows 每页显示条数
     */
    var getData = function (page,rows) {
        let record = m.searchJson.val();
        $.myAjaxs({
            url: url_left + "/search",
            async: true,
            cache: false,
            type: "post",
            data: 'jsonStr=' + record,
            dataType: "json",
            success: function (result) {
                let trList = $("#dailyTable  tr:not(:first)");
                trList.remove();
                if (result.success) {

                    let dataList = result.o.data;
                    // 总页数
                    let totalPage = result.o.totalPage;
                    // 总条数
                    let count = result.o.count;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr>' +
                            '<td title="' + isEmpty(item.receiveId) + '" style="text-align:right;">' + isEmpty(item.receiveId) + '</td>' +
                            '<td title="' + isEmpty(item.storeCd) + '" style="text-align:left;">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align:left;">' + isEmpty(item.storeName) + '</td>' +
                            '<td title="' + isEmpty(item.receiveDate) + '" style="text-align:center;">' + isEmpty(item.receiveDate) + '</td>' +
                            '<td title="' + isEmpty(item.pmaName) + '" style="text-align:left;">' + isEmpty(item.pmaName) + '</td>' +
                            '<td title="' + isEmpty(item.categoryName) + '" style="text-align:left;">' + isEmpty(item.categoryName) + '</td>' +
                            '<td title="' + isEmpty(item.subCategoryName) + '" style="text-align:left;">' + isEmpty(item.subCategoryName) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align:right;">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align:left;">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align:right;">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.dc) + '" style="text-align:left;">' + isEmpty(item.dc) + '</td>' +
                            '<td title="' + isEmpty(item.amName) + '" style="text-align:left;">' + isEmpty(item.amName) + '</td>' +
                            '<td title="' + isEmpty(item.omName) + '" style="text-align:left;">' + isEmpty(item.omName) + '</td>' +
                            '<td title="' + toThousands(item.receiveTotalQty) + '" style="text-align:right;">' + toThousands(item.receiveTotalQty) + '</td>' +
                            '<td title="' + isEmpty(item.userName) + '" style="text-align:left;">' + isEmpty(item.userName) + '</td>' +
                            '<td title="' + isEmpty(item.receivedRemark) + '" style="text-align:left;">' + isEmpty(item.receivedRemark) + '</td>' +
                            '</tr>';
                        m.dailyTable.append(tempTrHtml);
                    }
                    var totalQty= "<tr style='text-align:right' id='total_qty'>"
                        +"<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td style='text-align:right'>Total:</td>" +
                        "<td style='text-align:right'>total Item</td>" +
                        "<td style='text-align:right'>"+result.o.totalItemSKU+"</td>"+
                        "<td></td>"+
                        "<td style='text-align:right'>total qty </td>"+
                        "<td style='text-align:right'>"+result.o.totalReceiveQty+"</td>"+
                        "<td></td>" +
                        "<td></td>" +
                        "</tr>";
                    if (totalPage==page){
                        $("#dailyTable").append(totalQty)
                    }
                    // 加载分页条数据
                        _common.loadPaging(totalPage,count,page,rows);

                }
                // 激活 分页按钮点击
                but_paging();
            },
            error: function (e) {

            }
        });
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

    //验证检索项是否合法
    var verifySearch = function(){
        if(m.startDate.val()==""||m.startDate.val()==null){
            _common.prompt("Please enter a Date!",5,"error"); // 开始日期不可以为空
            $("#startDate").css("border-color","red");
            $("#startDate").focus();
            return false;
        }else {
            $("#startDate").css("border-color","#CCC");
        }
        if(m.endDate.val()==""||m.endDate.val()==null){
            _common.prompt("Please enter a Date!",5,"error"); // 结束日期不可以为空
            $("#endDate").css("border-color","red");
            $("#endDate").focus();
            return false;
        }else {
            $("#endDate").css("border-color","#CCC");
        }
        if(new Date(fmtDate(m.startDate.val())).getTime()>new Date(fmtDate(m.endDate.val())).getTime()){
            $("#endDate").focus();
            _common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
            return false;
        }
        if(m.startDate.val()!=""&&m.endDate.val()!=""){
            var _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
            var _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
            var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
            if(difValue >62){
                _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
                $("#endDate").focus();
                return false;
            }
        }
        return true;
    }

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    //number格式化
    function fmtIntNum(val) {
        if (val == null || val == "") {
            return "0";
        }
        return val;
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('dcReceiptDaily');
_start.load(function (_common) {
    _index.init(_common);
});