require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('mmPromotionSaleDaily', function () {
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
        storeName: null,
        dailyTable: null,
        startDate: null,
        endDate: null,
        barcode: null,
        articleName: null,
        author: null,
        am: null,
        searchJson: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        promotionCd : null,
        promotionType : null,
        promotionPattern : null,
        distributionType : null,
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
        let am = m.am.attr('k');
        let promotionCd = m.promotionCd.val().trim();
        let promotionType = m.promotionType.attr('k');
        let distributionType = m.distributionType.attr('k');
        let promotionPattern = m.promotionPattern.attr('k');
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
            'promotionPattern': promotionPattern,
            'promotionType': promotionType,
            'distributionType': distributionType,
            'promotionCd': promotionCd,
            'am': am,
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
        url_left = _common.config.surl + "/mmPromotionSaleDaily";
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

        // 获取  Promotion Pattern
        promotionPattern = $("#promotionPattern").myAutomatic({
            url: url_left + "/getPromotionPattern",
            ePageSize: 10,
            startCount: 0,
        });

        // 获取  Promotion Type
        promotionType = $("#promotionType").myAutomatic({
            url: url_left + "/getPromotionType",
            ePageSize: 10,
            startCount: 0,
        });

        // 获取  Distribution Type
        distributionType = $("#distributionType").myAutomatic({
            url: url_left + "/getDistributionType",
            ePageSize: 10,
            startCount: 0,
        });
    }


    var but_event = function () {
        m.reset.click(function () {
            $("#regionRemove").click();
            $("#patternRemove").click();
            $("#typeRemove").click();
            $("#distributionTypeRemove").click();
            m.promotionCd.val('');
            m.barcode.val('');
            m.am.val('').attr('k','').attr('v','');
            m.startDate.val('');
            m.endDate.val('');
            m.articleName.val('');
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

                        // 有多条明细, 需要合并单元格
                        if (item.list.length>1) {
                            for (let j = 0; j < item.list.length; j++) {
                                let tempTrHtml = '';
                                if (j==0) {
                                    tempTrHtml = '<tr>' +
                                    '<td title="'+isEmpty(item.storeCd)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                                    '<td title="'+isEmpty(item.storeName)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.storeName)+'</td>' +
                                    '<td title="'+fmtIntDate(item.accDate)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: center">'+fmtIntDate(item.accDate)+'</td>' +
                                    '<td title="'+isEmpty(item.promotionCd)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.promotionCd)+'</td>' +
                                    '<td title="'+isEmpty(item.promotionTheme)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.promotionTheme)+'</td>' +
                                    '<td title="'+isEmpty(item.promotionPattern)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.promotionPattern)+'</td>' +
                                    '<td title="'+isEmpty(item.promotionType)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.promotionType)+'</td>' +
                                    '<td title="'+isEmpty(item.distributionType)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: left">'+isEmpty(item.distributionType)+'</td>' +
                                    '<td title="'+toThousands(item.totalSellingPrice)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: right">'+toThousands(item.totalSellingPrice)+'</td>' +
                                    '<td title="'+toThousands(item.totalSaleQty)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: right">'+toThousands(item.totalSaleQty)+'</td>' +
                                    '<td title="'+toThousands(item.totalSaleAmt)+'" rowspan="'+item.list.length+'" style="vertical-align: middle;text-align: right">'+toThousands(item.totalSaleAmt)+'</td>' +
                                    '<td title="'+isEmpty(item.list[j].barcode)+'" style="text-align: right">'+isEmpty(item.list[j].barcode)+'</td>' +
                                    '<td title="'+isEmpty(item.list[j].articleId)+'" style="text-align: right">'+isEmpty(item.list[j].articleId)+'</td>' +
                                    '<td title="'+isEmpty(item.list[j].articleName)+'" style="text-align: left">'+isEmpty(item.list[j].articleName)+'</td>' +
                                    '<td title="'+toThousands(item.list[j].sellingPrice)+'" style="text-align: right">'+toThousands(item.list[j].sellingPrice)+'</td>' +
                                    '<td title="'+toThousands(item.list[j].saleQty)+'" style="text-align: right">'+toThousands(item.list[j].saleQty)+'</td>' +
                                    '<td title="'+toThousands(item.list[j].saleAmt)+'" style="text-align: right">'+toThousands(item.list[j].saleAmt)+'</td>' +
                                    '</tr>';
                                } else {
                                     tempTrHtml = '<tr>' +
                                         '<td title="'+isEmpty(item.list[j].barcode)+'" style="text-align: right">'+isEmpty(item.list[j].barcode)+'</td>' +
                                         '<td title="'+isEmpty(item.list[j].articleId)+'" style="text-align: right">'+isEmpty(item.list[j].articleId)+'</td>' +
                                         '<td title="'+isEmpty(item.list[j].articleName)+'" style="text-align: left">'+isEmpty(item.list[j].articleName)+'</td>' +
                                         '<td title="'+toThousands(item.list[j].sellingPrice)+'" style="text-align: right">'+toThousands(item.list[j].sellingPrice)+'</td>' +
                                         '<td title="'+toThousands(item.list[j].saleQty)+'" style="text-align: right">'+toThousands(item.list[j].saleQty)+'</td>' +
                                         '<td title="'+toThousands(item.list[j].saleAmt)+'" style="text-align: right">'+toThousands(item.list[j].saleAmt)+'</td>' +
                                        '</tr>';
                                }
                                m.dailyTable.append(tempTrHtml);
                            }
                        } else {
                            // 只有一条明细, 按 一条显示就行了
                            let tempTrHtml = '<tr>' +
                                '<td title="'+isEmpty(item.storeCd)+'" style="text-align: right">'+isEmpty(item.storeCd)+'</td>' +
                                '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                                '<td title="'+fmtIntDate(item.accDate)+'" style="text-align: center">'+fmtIntDate(item.accDate)+'</td>' +
                                '<td title="'+isEmpty(item.promotionCd)+'" style="text-align: right">'+isEmpty(item.promotionCd)+'</td>' +
                                '<td title="'+isEmpty(item.promotionTheme)+'" style="text-align: left">'+isEmpty(item.promotionTheme)+'</td>' +
                                '<td title="'+isEmpty(item.promotionPattern)+'" style="text-align: left">'+isEmpty(item.promotionPattern)+'</td>' +
                                '<td title="'+isEmpty(item.promotionType)+'" style="text-align: left">'+isEmpty(item.promotionType)+'</td>' +
                                '<td title="'+isEmpty(item.distributionType)+'" style="text-align: left">'+isEmpty(item.distributionType)+'</td>' +
                                '<td title="'+toThousands(item.totalSellingPrice)+'" style="text-align: right">'+toThousands(item.totalSellingPrice)+'</td>' +
                                '<td title="'+toThousands(item.totalSaleQty)+'" style="text-align: right">'+toThousands(item.totalSaleQty)+'</td>' +
                                '<td title="'+toThousands(item.totalSaleAmt)+'" style="text-align: right">'+toThousands(item.totalSaleAmt)+'</td>' +
                                '<td title="'+isEmpty(item.list[0].barcode)+'" style="text-align: right">'+isEmpty(item.list[0].barcode)+'</td>' +
                                '<td title="'+isEmpty(item.list[0].articleId)+'" style="text-align: right">'+isEmpty(item.list[0].articleId)+'</td>' +
                                '<td title="'+isEmpty(item.list[0].articleName)+'" style="text-align: left">'+isEmpty(item.list[0].articleName)+'</td>' +
                                '<td title="'+toThousands(item.list[0].sellingPrice)+'" style="text-align: right">'+toThousands(item.list[0].sellingPrice)+'</td>' +
                                '<td title="'+toThousands(item.list[0].saleQty)+'" style="text-align: right">'+toThousands(item.list[0].saleQty)+'</td>' +
                                '<td title="'+toThousands(item.list[0].saleAmt)+'" style="text-align: right">'+toThousands(item.list[0].saleAmt)+'</td>' +
                                '</tr>';
                            m.dailyTable.append(tempTrHtml);
                        }
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
        }else {
            $("#startDate").css("border-color","#CCC");
        }
        if(m.endDate.val()==""||m.endDate.val()==null){
            _common.prompt("Please enter a Sales Date!",5,"error"); // 结束日期不可以为空
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

    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res =  date.substring(6, 8) + "/" + date.substring(4, 6) + '/' +date.substring(0, 4);
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
var _index = require('mmPromotionSaleDaily');
_start.load(function (_common) {
    _index.init(_common);
});
