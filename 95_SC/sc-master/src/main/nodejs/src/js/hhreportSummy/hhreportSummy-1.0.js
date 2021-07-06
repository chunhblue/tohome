require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");


var _myAjax = require("myAjax");
define('hhreportSummy',function () {
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
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/hhtReport"
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 初始化检索日期
        initDate(m.startDate,m.endDate);
        but_event();
    }
    // 初始化查询日期
    var initDate = function(startDate,endDate) {
        if (startDate) {
            startDate.datetimepicker({
                language:'en',
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
            endDate.val(_start.Format('dd/MM/yyyy'));
        }
        if (endDate) {
            endDate.datetimepicker({
                language:'en',
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
            // let sumdate = new Date().getTime() - (86400000 * 3);
            let sumdate = new Date().getTime() - (86400000 * 7);
            startDate.val(new Date(sumdate).Format('dd/MM/yyyy'));
        }
    }
    var but_event=function(){
        m.search.click(function () {
            // _common.loading_close();
            if(verifySearch()){
                _common.loadPaging(1,1,1,10);
                page=1;
                setParamJson();
                // _common.loading();
                getData(page,rows);
                // _common.loading_close();
            }
        })
        $("#export").on("click",function(){
            if(verifySearch()) {
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });
        m.reset.click(function () {
            m.am.val('');
            m.startDate.val('');
            m.endDate.val('');
            $("#regionRemove").click();
            $("#depRemove").click();
            $("#startDate").css("border-color","#CCC");
            $("#endDate").css("border-color","#CCC");
            page=1;
            // 清除分页
            _common.resetPaging();
            $("#dailyTable").find("tr:not(:first)").remove();
        });
    }
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
                        let tempTrHtml =
                            '<tr style="text-align: center;">' +
                            '<td title="'+isEmpty(item.storeCd)+'" style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                            '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                            '<td title="'+isNull(item.totalOpenPo)+'" style="text-align: left">'+isNull(item.totalOpenPo)+'</td>' +
                            '<td title="'+isNull(item.grpoOfQty)+'" style="text-align: left">'+isNull(item.grpoOfQty)+'</td>'+
                            '<td title="'+isNull(item.noOfConfiPo)+'" style="text-align: left">'+isNull(item.noOfConfiPo)+'</td>'+
                            '<td title="'+isNull(item.noOfPendingGrpo)+'" style="text-align: left">'+isNull(item.noOfPendingGrpo)+'</td>'+
                            '</tr>';
                        m.dailyTable.append(tempTrHtml);
                    }
                      // 加载分页条数据
                    _common.loadPaging(totalPage,count,page,rows);
                    // _common.loading_close();

                }

                but_paging();
                // _common.loading_close();

                // 激活 分页按钮点击

            },
            error: function (e) {
                // _common.loading_close();
            }
        });
        // _common.loading_close();
    }
    // 分页按钮事件
    var but_paging = function () {
        $('.pagination li').on('click',function () {
            page = $($(this).context).val();
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                // _common.loading();
                // 分页获取数据
                getData(page,rows);
                // _common.loading_close();
            }
        });
    }
    var  setParamJson=function () {
        let searchJsonStr={
            'page': page,
            'rows': rows,
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'hhstartDate':formatDate(m.startDate.val()),
            'hhendDate':formatDate(m.endDate.val()),
        }
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
    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };
    var isNull = function (str) {
        if (str == null  || str == '') {
            return 0;
        }
        return str;
    };
    var verifySearch=function(){
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
            if(difValue >7){
                _common.prompt("Query Period cannot exceed 7 days!",5,"error"); // 日期期间取值范围不能大于62天
                $("#endDate").focus();
                return false;
            }
        }
          return  true;
    };
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    self.init = init;
    return self
});
var _start = require('start');
var _index = require('hhreportSummy');
_start.load(function (_common) {
    _index.init(_common);
})