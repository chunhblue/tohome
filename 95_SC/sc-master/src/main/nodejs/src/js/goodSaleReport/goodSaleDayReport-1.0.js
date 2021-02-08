require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");


var _myAjax = require("myAjax");
define('goodsSaleDayReport' , function (){
    var self = {};
    var url_left = "",
        systemPath = "",
        reThousands = null,
        toThousands = null,
        getThousands = null,
        page = 1, // 当前页数, 初始为 1
        rows = 10; // 每页显示条数, 默认为 10;

    var m={
        toKen: null,
        use: null,
        up: null,
        startDate: null,
        endDate: null,
        am: null,
        storeName: null,
        searchJson: null,
        search: null,
        reset: null,
        export: null,
        dailyTable: null,
        author: null,
        dep: null,
        pma: null,
        category: null,
        subCategory: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        articleId : null,
        articleName : null,
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
        let barcode=m.barcode.val();
        let am = m.am.attr('k');
        let dep = m.dep.attr('k');
        let pma = m.pma.attr('k');
        let category = m.category.attr('k');
        let subCategory = m.subCategory.attr('k');
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        let articleName = m.articleName.val();
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'am': am,
            'depCd': dep,
            'pmaCd': pma,
            'categoryCd': category,
            'subCategoryCd': subCategory,
            'startDate': _startDate,
            'endDate': _endDate,
            'barcode': barcode,
            'articleName': articleName,
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
        url_left = _common.config.surl + "/GoodsSaleReport";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        // 初始化店铺运营组织检索
        _common.initOrganization();
        _common.initCategoryAutoMatic();
        initAutoMatic();

        but_event();
        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
    }

    var initAutoMatic=function () {
        // 用户
        $.myAjaxs({
            url: url_left + "/getUserName",
            async: true,
            cache: false,
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let user = result.o;
                    m.author.text(m.author.text()+user.userName)
                }
            }
        });
        // 获取区域经理
        am = $("#am").myAutomatic({
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize: 10,
            startCount: 0,
        });
    }
    var but_event=function () {
        m.reset.click(function () {
            $("#regionRemove").click();
            $("#depRemove").click();
            $("#amRemove").click();
            m.startDate.val('');
            m.endDate.val('');
            m.articleId.val('');
            m.articleName.val('');
            $("#startDate").css("border-color","#CCC");
            $("#endDate").css("border-color","#CCC");
            page=1;
            // 清除分页
            _common.resetPaging();
            _common.initDate(m.startDate,m.endDate);
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
            data: 'SearchJson=' + record,
            dataType: "json",
            success: function (result) {
                var trList = $("#dailyTable  tr:not(:first)");
                trList.remove();
                if (result.success) {
                    let dataList = result.o.data;
                    // 总页数
                    let totalPage = result.o.totalPage;
                    $("#totalItemSKU").val(result.o.totalItemSKU);
                    $("#totalSaleAmount").val(result.o.totalSaleAmount);
                    // 总条数
                    let count = result.o.count;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: left" title="'+item.storeCd+'">' + isEmpty(item.storeCd) + '</td>' +
                            '<td style="text-align: left" title="'+item.storeName+'">' + isEmpty(item.storeName) + '</td>' +
                            '<td style="text-align: center" title="'+ isEmpty(item.saleDate)+'">' + isEmpty(item.saleDate) + '</td>' +
                            '<td style="text-align: left" title=" '+item.depName+' ">' + isEmpty(item.depName) + '</td>' +
                            '<td style="text-align: left" title="'+item.pmaName+'">' + isEmpty(item.pmaName) + '</td>' +
                            '<td style="text-align: left" title="'+item.categoryName+'">' + isEmpty(item.categoryName) + '</td>' +
                            '<td style="text-align: left" title="'+item.subCategoryName+'">' + isEmpty(item.subCategoryName) + '</td>' +
                            '<td style="text-align: right" title=" '+item.articleId+' ">' + isEmpty(item.articleId) + '</td>' +
                            '<td style="text-align: left" title="'+item.articleName+'">' + isEmpty(item.articleName) + '</td>' +
                            '<td style="text-align: right" title="'+item.barcode+'">' + isEmpty(item.barcode) + '</td>' +
                            '<td style="text-align: right" title="'+toThousands(item.saleQty)+'">' + toThousands(item.saleQty) + '</td>' +
                            '<td style="text-align: right" title="'+toThousands(item.priceActual)+'">' + toThousands(item.priceActual) + '</td>' +
                            '<td style="text-align: right" title="'+ toThousands(item.saleAmount)+'">' + toThousands(item.saleAmount) + '</td>' +
                            '<td style="text-align: left" title="'+isEmpty(item.amCd)+'">' + isEmpty(item.amCd) + '</td>' +
                            '<td style="text-align: left" title="'+isEmpty(item.amName)+'">' + isEmpty(item.amName) + '</td>' +
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
                        "<td style='text-align:right' title='total Item'>total Item</td>" +
                        "<td style='text-align:right'>"+result.o.totalItemSKU+"</td>"+
                        "<td style='text-align:right' title='total Sale Amount'>total Sale Amount </td>"+
                        "<td style='text-align:right'>"+result.o.totalSaleAmount+"</td>"+
                        "<td style='text-align:right' title='total Sale Qty'>total Sale Qty </td>"+
                        "<td style='text-align:right'>"+result.o.totalSaleQty+"</td>"+
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

    // 验证检索项是否合法
    var verifySearch = function() {
        if(m.startDate.val()==""||m.startDate.val()==null){
            _common.prompt("Please enter a Date!",5,"error"); // 开始日期不可以为空
            $("#startDate").focus();
            $("#startDate").css("border-color","red");
            return false;
        }else {
            $("#startDate").css("border-color","#CCC");
        }
        if(m.endDate.val()==""||m.endDate.val()==null){
            _common.prompt("Please enter a Date!",5,"error"); // 结束日期不可以为空
            $("#endDate").focus();
            $("#endDate").css("border-color","red");
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
var _index = require('goodsSaleDayReport');
_start.load(function (_common) {
    _index.init(_common);
})