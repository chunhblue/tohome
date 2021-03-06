require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");

define('cardPaymentReport', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        reThousands = null,
        toThousands = null,
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
        store: null,
        author: null,
        searchJson: null,

    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //拼接检索参数
    var setParamJson = function () {
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
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
        url_left = _common.config.surl + "/cardPaymentReport";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // 初始化店铺运营组织检索
        _common.initOrganization();
        _common.initCategoryAutoMatic();
        initAutoMatic();
        but_event();
        _common.initDate(m.startDate,m.endDate);
    }

    //初始化下拉
    var initAutoMatic = function () {
    }

    var but_event = function () {
        m.reset.click(function () {
            $("#aRegionaRegion").val("");
            $("#aCity").val("");
            $("#aDistrict").val("");
            $("#aStore").val("");
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
                        let cardType = isEmpty(item.cardType);
                        let tempTrHtml =
                            '<tr style="text-align: center;">' +
                            '<td title="'+isEmpty(item.storeCd)+'" style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                            '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td title="'+cardType+'" style="text-align: left">'+cardType+'</td>' +
                            '<td title="'+fmtDate(item.businessDate)+'" style="text-align: left">'+fmtDate(item.businessDate)+'</td>' +
                            '<td title="'+fmtIntDateTime(item.transDate)+'" style="text-align: center">'+fmtIntDateTime(item.transDate)+'</td>' +
                            '<td title="'+isEmpty(item.traceNo)+'" style="text-align: center">'+isEmpty(item.traceNo)+'</td>' +
                            '<td title="'+isEmpty(item.approveCode)+'" style="text-align: center">'+isEmpty(item.approveCode)+'</td>' +
                            '<td title="'+isEmpty(item.cardNum)+'" style="text-align: center">'+isEmpty(item.cardNum)+'</td>' +
                            '<td title="'+isEmpty(item.receiptNo)+'" style="text-align: left">'+isEmpty(item.receiptNo)+'</td>' +
                            '<td title="'+isEmpty(item.transcationNumber)+'" style="text-align: center">'+isEmpty(item.transcationNumber)+'</td>' +
                            '<td title="'+isEmpty(item.desciption)+'" style="text-align: center">'+isEmpty(item.desciption)+'</td>' +
                            '<td title="'+isEmpty(item.amountCk)+'" style="text-align: center">'+isEmpty(item.amountCk)+'</td>' +
                            '<td title="'+isEmpty(item.amountVcb)+'" style="text-align: center">'+isEmpty(item.amountVcb)+'</td>' +
                            '<td title="'+isEmpty(item.depositAmount)+'" style="text-align: center">'+isEmpty(item.depositAmount)+'</td>' +
                            '<td title="'+isEmpty(item.freeAmount)+'" style="text-align: center">'+isEmpty(item.freeAmount)+'</td>' +
                            '<td title="'+isEmpty(item.freePrecent)+'" style="text-align: center">'+isEmpty(item.freePrecent)+'</td>' +
                            '<td title="'+isEmpty(item.vat)+'" style="text-align: center">'+isEmpty(item.vat)+'</td>' +
                            '<td title="'+isEmpty(item.depositDate)+'" style="text-align: center">'+isEmpty(item.depositDate)+'</td>' +
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

    //number格式化
    function fmtIntNum(val) {
        if (val == null || val == "") {
            return "0";
        }
        return val;
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }
    //格式化数字类型的日期
    function fmtIntDateTime(date) {
        var res = "";
        res = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4)  +" "+date.substring(10,20);
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('cardPaymentReport');
_start.load(function (_common) {
    _index.init(_common);
});
