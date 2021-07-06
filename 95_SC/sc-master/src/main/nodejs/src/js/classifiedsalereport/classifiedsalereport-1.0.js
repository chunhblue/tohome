require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('classifiedSaleReport', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        reThousands = null,
        toThousands = null,
        url_root=null,
        getThousands = null,
        page = 1, // 当前页数, 初始为 1
        rows = 10; // 每页显示条数, 默认为 10;

    var m = {
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        toKen: null,
        use: null,
        up: null,
        search: null,
        reset: null,
        export: null,
        storeName: null,
        dailyTable: null,
        startDate: null,
        endDate: null,
        articleId: null,
        store: null,
        author: null,
        am: null,
        searchJson: null,
        city: null,
        dep:null,
        pma:null,
        subCategory:null,
        totalSaleAmount:null,
        category:null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //拼接检索参数
    var setParamJson = function () {
        let articleId = m.articleId.val();
        let am = m.am.val();
        let city = m.city.val();
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        let storeCd = m.store.attr('k');
        let depCd=m.dep.attr("k");
        let pmaCd=m.pma.attr("k");
        let subCategoryCd=m.subCategory.attr("k");
        let categoryCd=m.category.attr("k");
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'articleId': articleId,
            'am': $("#am").attr("k"),
            'startDate': _startDate,
            'endDate': _endDate,
            'depCd':depCd,
            'categoryCd':categoryCd,
            'subCategoryCd':subCategoryCd,
            'pmaCd':pmaCd,
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
        url_left = _common.config.surl + "/classifiedSaleReport";
        url_root= _common.config.surl + "/sellDayReport";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // _common.initOrganization();

        // 初始化店铺运营组织检索
        _common.initOrganization();
        _common.initCategoryAutoMatic();
        initAutoMatic();
        but_event();
        // m.search.click();
        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
    }

    //初始化下拉
    var initAutoMatic = function () {
        // 获取区域经理
        am=$("#am").myAutomatic({
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize:10,
            startCount:0,
        })
        // 大分类
        $.myAjaxs({
            url: url_left + "/getUserName",
            async: true,
            cache: false,
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let user = result.o;
                    m.author.text(user.userName)
                }
            }
        });
    }

    var but_event = function () {
        $("#amRemove").on("click",function (e) {
            $.myAutomatic.cleanSelectObj(am);
        });

        m.reset.click(function () {
            m.am.val('');
            m.startDate.val('');
            m.endDate.val('');
            m.dep.val("");
            m.pma.val('');
            m.pma.prop("disabled",true);
            m.category.val("");
            m.category.prop("disabled",true);
            m.subCategory.val("");
            m.subCategory.prop("disabled",true);
            $("#amRemove").click();
            $("#totalSaleAmount").val("");
            $("#regionRemove").click();
            $("#depRemove").click();
            $("#startDate").css("border-color","#CCC");
            $("#endDate").css("border-color","#CCC");
            page=1;
            // 清除分页
            _common.resetPaging();
            $("#dailyTable").find("tr:not(:first)").remove();
        });

        m.search.click(function () {
            if(verifySearch()){
                _common.loadPaging(1,1,1,10);
                page=1;
                setParamJson();
                getData(page,rows);
                let record = m.searchJson.val();
                $.myAjaxs({
                    url: url_left  + "/getTotalSaleAmount",
                    async: true,
                    cache: false,
                    type: "post",
                    data: 'SearchJson='+record,
                    dataType: "json",
                    success: function (result) {
                        m.totalSaleAmount.val(toThousands(result.o.data));
                    },
                    complete: _common.myAjaxComplete
                });

            }

        })
        // 导出按钮点击事件
        $("#export").on("click",function(){
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
            data: 'SearchJson='+record,
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
                        let dep = isEmpty(item.depCd)+" "+isEmpty(item.depName);
                        let pma = isEmpty(item.pmaCd)+" "+isEmpty(item.pmaName);
                        let category = isEmpty(item.categoryCd)+" "+isEmpty(item.categoryName);
                        let subCategory = isEmpty(item.subCategoryCd)+" "+isEmpty(item.subCategoryName);
                        let tempTrHtml =
                            '<tr style="text-align: center;">' +
                            '<td title="'+isEmpty(item.storeCd)+'" style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                            '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            // '<td title="'+isEmpty(item.accDate)+'" style="text-align: center">'+isEmpty(item.accDate)+'</td>' +
                            '<td title="'+dep+'" style="text-align: left">'+dep+'</td>' +
                            '<td title="'+pma+'" style="text-align: left">'+pma+'</td>'+
                            '<td title="'+category+'" style="text-align: left">'+category+'</td>'+
                            '<td title="'+subCategory+'" style="text-align: left">'+subCategory+'</td>'+
                            '<td title="'+toThousands(item.saleQty)+'" style="text-align: right">'+toThousands(item.saleQty)+'</td>' +
                            '<td title="'+toThousands(item.saleAmount)+'" style="text-align: right">'+toThousands(item.saleAmount)+'</td>' +
                            '<td title="'+isEmpty(item.saleDate)+'" style="text-align: center">'+isEmpty(item.saleDate)+'</td>' +
                            /*'<td title="'+isEmpty(item.amCd)+'" style="text-align: left">'+isEmpty(item.amCd)+'</td>' +*/
                            '<td title="'+isEmpty(item.amName)+'" style="text-align: left">'+isEmpty(item.amName)+'</td>' +
                            '</tr>';
                        m.dailyTable.append(tempTrHtml);
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
            _common.prompt("Please enter a Sales Date!",5,"error"); // 开始日期不可以为空
            $("#startDate").focus();
            $("#startDate").css("border-color","red");
            return false;
        }else if(_common.judgeValidDate(m.startDate.val())){
            _common.prompt("Please enter a valid date!",3,"info");
            $("#startDate").focus();
            return false;
        }else {
            $("#startDate").css("border-color","#CCC");
        }
        if(m.endDate.val()==""||m.endDate.val()==null){
            _common.prompt("Please enter a Sales Date!",5,"error"); // 结束日期不可以为空
            $("#endDate").focus();
            $("#endDate").css("border-color","red");
            return false;
        }else if(_common.judgeValidDate(m.endDate.val())){
            _common.prompt("Please enter a valid date!",3,"info");
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
var _index = require('classifiedSaleReport');
_start.load(function (_common) {
    _index.init(_common);
});
