require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('storeTransferDaily', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
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
        storeName: null,
        vendorCd: null,
        dailyTable: null,
        reason: null,
        startDate: null,
        endDate: null,
        articleId: null,
        articleName: null,
        articleId1: null,
        articleName1: null,
        author: null,
        am: null,
        searchJson: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        dep : null,
        pma : null,
        category : null,
        subCategory : null,
        barcode:null,
        barcode1:null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //拼接检索参数
    var setParamJson = function () {
        let barcode = m.barcode.val().trim();
        let articleName = m.articleName.val().trim();
        let barcode1 = m.barcode1.val().trim();
        let articleName1 = m.articleName1.val().trim();
        let am = m.am.attr('k');
        let reason = m.reason.attr('k');
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'barcode': barcode,
            'articleName': articleName,
            'barcode1': barcode1,
            'articleName1': articleName1,
            'am': am,
            'startDate': _startDate,
            'endDate': _endDate,
            'reason': reason,
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
        url_left = _common.config.surl + "/itemTransferDaily";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        // 初始化店铺运营组织检索
        _common.initOrganization();
        initAutoMatic();

        but_event();

        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
    }

    //初始化下拉
    var initAutoMatic = function () {
        // 获取区域经理
        am = $("#am").myAutomatic({
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize: 10,
            startCount: 0,
        });

        reason=$("#reason").myAutomatic({
            url:systemPath+"/cm9010/getResonCode",
            ePageSize:10,
            startCount:0,
        })
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#startDate").val()){
            _common.prompt("Please select a start date!",5,"error");/*请选择开始日期*/
            $("#startDate").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",5,"error");
                $("#startDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#endDate").val()){
            _common.prompt("Please select a end date!",5,"error");/*请选择结束日期*/
            $("#endDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",5,"error");
                $("#endDate").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#endDate").focus();
            _common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
            $("#endDate").focus();
            return false;
        }
        return true;
    }

    var but_event = function () {
        m.reset.click(function () {
            $("#regionRemove").click();
            $("#reasonRemove").click();
            m.articleId.val('');
            m.articleName.val('');
            m.articleId1.val('');
            m.articleName1.val('');
            m.vendorCd.val('');
            m.am.val('').attr('k','').attr('v','');
            m.startDate.val('');
            m.endDate.val('');
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

        // 打印按钮事件
        m.print.on("click",function () {
            if(verifySearch()) {
                // 拼接检索参数
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
                var trList = $("#dailyTable  tr:not(:first)");
                trList.remove();
                if (result.success) {
                    let dataList = result.o.data;
                    // 总页数
                    let totalPage = result.o.totalPage;
                    // 总条数
                    let count = result.o.count;
                    let tempTrHtml = '';
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        tempTrHtml += '<tr>' +
                            '<td title="' + isEmpty(item.storeCd) + '" style="text-align:right;">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align:left;">' + isEmpty(item.storeName) + '</td>' +
                            '<td title="' + isEmpty(item.voucherDate) + '" style="text-align:center;">' + isEmpty(item.voucherDate) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align:right;">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align:left;">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align:right;">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.articleId1) + '" style="text-align:right;">' + isEmpty(item.articleId1) + '</td>' +
                            '<td title="' + isEmpty(item.articleName1) + '" style="text-align:left;">' + isEmpty(item.articleName1) + '</td>' +
                            '<td title="' + isEmpty(item.barcode1) + '" style="text-align:right;">' + isEmpty(item.barcode1) + '</td>' +
                            '<td title="' + toThousands(item.bqty1) + '" style="text-align:right;">' + toThousands(item.bqty1) + '</td>' +
                            // '<td title="' + toThousands(item.amtNoTax) + '" style="text-align:right;">' + toThousands(item.amtNoTax) + '</td>' +
                            '<td title="' + isEmpty(item.adjustReasonText) + '" style="text-align:left;">' + isEmpty(item.adjustReasonText) + '</td>' +
                            // '<td title="' + isEmpty(item.amCd) + '" style="text-align:left;">' + isEmpty(item.amCd) + '</td>' +
                            '<td title="' + isEmpty(item.amName) + '" style="text-align:left;">' + isEmpty(item.amName) + '</td>' +
                            '</tr>';
                    }
                    $('#dailyTable').append(tempTrHtml);
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

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeTransferDaily');
_start.load(function (_common) {
    _index.init(_common);
});
