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
        storeCd1: null, // 对方店铺
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
        let vendorCd = m.vendorCd.val();
        let am = m.am.attr('k');
        let reason = m.reason.attr('k');
        let dep = m.dep.attr('k');
        let pma = m.pma.attr('k');
        let category = m.category.attr('k');
        let subCategory = m.subCategory.attr('k');
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        let storeCd1 = m.storeCd1.attr('k');
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'barcode': barcode,
            'articleName': articleName,
            'vendorCd': vendorCd,
            'am': am,
            'depCd':dep,
            'pmaCd': pma,
            'categoryCd': category,
            'subCategoryCd': subCategory,
            'startDate': _startDate,
            'endDate': _endDate,
            'storeCd1': storeCd1,
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
        url_left = _common.config.surl + "/storeTransferDaily";
        systemPath = _common.config.surl;

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
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize: 10,
            startCount: 0,
        });

        storeCd1 = $("#storeCd1").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0
        });

        reason=$("#reason").myAutomatic({
            url:systemPath+"/cm9010/getReasonCode",
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
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
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
        if(m.storeCd1.attr('k') && $("#aStore").attr("k")){
            if(m.storeCd1.attr('k') == $("#aStore").attr("k")){
                _common.prompt("A transfer out of a store cannot be the same as a transfer into a store!",5,"error");
                $("#storeCd1").focus();
                return false;
            }
        }
        return true;
    }

    var but_event = function () {
        m.reset.click(function () {
            $("#regionRemove").click();
            $("#depRemove").click();
            $("#reasonRemove").click();
            m.articleId.val('');
            m.articleName.val('');
            m.vendorCd.val('');
            m.am.val('').attr('k','').attr('v','');
            m.startDate.val('');
            m.endDate.val('');
            m.storeCd1.val('').attr('k','').attr('v','');
            $("#startDate").css("border-color","#CCC");
            $("#endDate").css("border-color","#CCC");
            page=1;
            // 清除分页
            _common.resetPaging();
            $("#dailyTable").find("tr:not(:first)").remove();
        });

        m.search.click(function () {
            if(verifySearch()) {
                page=1;
                setParamJson();
                total_detail(m.searchJson.val());
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
                            // '<td title="' + isEmpty(item.storeCd) + '" style="text-align:left;">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align:left;">' + isEmpty(item.storeName) + '</td>' +
                            // '<td title="' + isEmpty(item.storeCd1) + '" style="text-align:left;">' + isEmpty(item.storeCd1) + '</td>' +
                            '<td title="' + isEmpty(item.storeName1) + '" style="text-align:left;">' + isEmpty(item.storeName1) + '</td>' +
                            '<td title="' + isEmpty(item.voucherDate) + '" style="text-align:center;">' + isEmpty(item.voucherDate) + '</td>' +
                            '<td title="' + isEmpty(item.depName) + '" style="text-align:left;">' + isEmpty(item.depName) + '</td>' +
                            '<td title="' + isEmpty(item.pmaName) + '" style="text-align:left;">' + isEmpty(item.pmaName) + '</td>' +
                            '<td title="' + isEmpty(item.categoryName) + '" style="text-align:left;">' + isEmpty(item.categoryName) + '</td>' +
                            '<td title="' + isEmpty(item.subCategoryName) + '" style="text-align:left;">' + isEmpty(item.subCategoryName) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align:right;">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align:left;">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align:right;">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.uom) + '" style="text-align:left;">' + isEmpty(item.uom) + '</td>' +
                            '<td title="' + toThousands(item.qty2) + '" style="text-align:right;">' + toThousands(item.qty2) + '</td>' +
                            '<td title="' + toThousands(item.qty1) + '" style="text-align:right;">' + toThousands(item.qty1) + '</td>' +
                            '<td title="' + isEmpty(item.tranInDate) + '" style="text-align:center;">' + isEmpty(item.tranInDate) + '</td>' +
                            '<td title="' + isEmpty(item.differenceReasonText) + '" style="text-align:left;">' + isEmpty(item.differenceReasonText) + '</td>' +
                            // '<td title="' + toThousands(item.amtNoTax) + '" style="text-align:right;">' + toThousands(item.amtNoTax) + '</td>' +
                            '<td title="' + isEmpty(item.adjustReasonText) + '" style="text-align:left;">' + isEmpty(item.adjustReasonText) + '</td>' +
                            '<td title="' + isEmpty(item.amName) + '" style="text-align:left;">' + isEmpty(item.amName) + '</td>' +
                            '</tr>';
                    }
                    $('#dailyTable').append(tempTrHtml);
                    // 合计行
                    var totalOutQty = $("#toTotalOutQty").val();
                    var totalInQty = $("#toTotalInQty").val();
                    var totalDoc = $("#totalDoc").val();
                    var totalTr = "<tr>" +
                        "<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +
                        "<td>Total Document:</td>" +
                        "<td style='text-align:right;'>" + toThousands(totalDoc) + "</td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td>Total Qty</td>" +
                        "<td style='text-align:right;'>" + toThousands(totalOutQty) + "</td>" +
                        "<td style='text-align:right;'>" + toThousands(totalInQty) + "</td>" +
                        "<td></td>" +"<td></td>" +"<td></td>" +"<td></td>" +
                        "</tr>";
                    if(totalPage === page){
                        $('#dailyTable').append(totalTr);
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
                total_detail(m.searchJson.val());
                // 分页获取数据
                getData(page,rows);
            }
        });
    }

    // 获取库存调拨总数量
    var total_detail = function (data) {
        $.myAjaxs({
            url: url_left + "/getTranferQty",
            async: false,
            cache: false,
            type: "post",
            data: 'jsonStr=' + data,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    $("#toTotalOutQty").val(result.o.qty2);
                    $("#toTotalInQty").val(result.o.qty1);
                    $("#totalDoc").val(result.o.records);
                }
            },
            error:function () {
                $("#toTotalOutQty").val('');
                $("#toTotalInQty").val('');
                $("#totalDoc").val('');
            }
        })
    };

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

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num,dit) {
        if (!num) {
            return '';
        }
        dit = typeof dit !== 'undefined' ? dit : 0;
        num = num + ''
        const reg = /\d{1,3}(?=(\d{3})+$)/g
        let intNum = ''
        let decimalNum = ''
        if (num.indexOf('.') > -1) {
            if (num.indexOf(',') != -1) {
                num = num.replace(/\,/g, '');
            }
            num = parseFloat(num).toFixed(dit);
            return (num + '').replace(reg, '$&,');
        } else {
            return (num + '').replace(reg, '$&,');
        }
    }



    // 运算
    function accAdd(arg1,arg2){
        var r1,r2,m;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        return (accMul(arg1,m)+accMul(arg2,m))/m;
    }

    function accMul(arg1,arg2){
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        let result = Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
        if (!isFinite(result)) {
            result = 0;
        }
        return result;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeTransferDaily');
_start.load(function (_common) {
    _index.init(_common);
});
