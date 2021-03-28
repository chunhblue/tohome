require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('dailySaleReport', function () {
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
        export: null,
        reset: null,
        storeName: null,
        dailyTable: null,
        startDate: null,
        endDate: null,
        articleId: null,
        pma: null,
        author: null,
        am: null,
        searchJson: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        include_services:null,
        PosstartDate:null,
        PosendDate:null,
        typeDate:null,
        effendDate:null,
        effstartDate:null,
        dailyPosTable:null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //拼接检索参数
    var setParamJson = function () {
        var typeDate=m.typeDate.val();
        let _startDate ='';
        let _endDate=' ';
        if (typeDate=='1'){
            _startDate = formatDate(m.effstartDate.val());
           _endDate = formatDate(m.effendDate.val());
        }else {
            _startDate = formatDate(m.PosstartDate.val());
            _endDate = formatDate(m.PosendDate.val());
        }
        let articleId = m.articleId.val();

        var includeService = m.include_services.val();

        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'articleId': articleId,
            'am': $("#am").attr("k"),
            'effectiveStartDate': _startDate,
            'effectiveEndDate': _endDate,
            'page': page,
            'rows': rows,
            'includeService':includeService,
            'typeDate':typeDate,
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
        url_left = _common.config.surl + "/sellDayReport";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        // 初始化店铺运营组织检索
        _common.initOrganization();
        initAutoMatic();
        but_event();
        // m.search.click();
        // 初始化检索日期
        _common.initDate(m.effstartDate,m.effendDate);
        $("#show_status").find("input[type='radio']").eq(0).click();
        $("#dailyTable").show();
        $("#dailyPosTable").hide();
    }
    //初始化下拉
    var initAutoMatic = function () {
        // 获用户名字
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
        am=$("#am").myAutomatic({
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize:10,
            startCount:0,
        })
    }
    var but_event = function () {
        m.PosstartDate.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true,
            PosstartDate: new Date(fmtIntDate($('#PosstartDate').val()))
        });
        m.PosendDate.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true,
            PosendDate: new Date(fmtIntDate($('#PosendDate').val()))
        });
        $("#show_status").find("input[type='radio']").on("click",function() {
            var thisObj = $(this);
            var thisVal = thisObj.val();
            if(thisVal=="1"){
               m.typeDate.val(thisVal);
              m.PosendDate.attr("disabled",true);
              m.PosstartDate.attr("disabled",true);
              m.PosstartDate.css("border-color","#CCCCCC");
              m.PosstartDate.val('');
              m.PosendDate.val('');
              m.PosendDate.css("border-color","#CCCCCC");
              m.effstartDate.attr("disabled",false);
              m.effstartDate.css("border-color","#CCCCCC");
              m.effendDate.attr("disabled",false);
              m.effendDate.css("border-color","#CCCCCC");
                $("#dailyPosTable").hide();
                $("#dailyTable").show();
            }else{
                m.typeDate.val(thisVal);
                 m.effendDate.attr("disabled",true);
                m.effstartDate.attr("disabled",true);
                m.PosendDate.attr("disabled",false);
                m.PosstartDate.attr("disabled",false);
                m.effendDate.css("border-color","#CCCCCC");
                m.PosendDate.css("border-color","#CCCCCC");
                m.PosstartDate.css("border-color","#CCCCCC");
                m.PosendDate.css("border-color","#CCCCCC");
                $("#dailyPosTable").show();
                $("#dailyTable").hide();
            }
        });
        m.reset.click(function () {
            $("#regionRemove").click();
            m.am.val('');
            m.effendDate.val('');
            m.effstartDate.val('');
            m.PosstartDate.val('');
            m.PosendDate.val('');
            m.am.val("")
            $("#effendDate").css("border-color","#CCC");
            $("#effstartDate").css("border-color","#CCC");
            $("#amRemove").click();
            page=1;
            // 清除分页
            _common.resetPaging();
            $("#dailyTable").find("tr:not(:first)").remove();
            $("#dailyPosTable").find("tr:not(:first)").remove();
        });
        $("#amRemove").on("click",function (e) {
            $.myAutomatic.cleanSelectObj(am);
        })
        $("#pmaRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_category);
            $.myAutomatic.cleanSelectObj(a_subCategory);
            $("#category").attr("disabled", true);
            $("#subCategory").attr("disabled", true);
            $("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
            $("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
        });
        m.search.click(function () {
            if(verifySearch()){
                page=1;
                setParamJson();
                var typeDate=m.typeDate.val();
                getData(page,rows,typeDate);
            }
        })

        // 导出按钮点击事件
        m.export.on("click",function(){
            if(verifySearch()) {
                // 拼接检索参数2
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
    var getData = function (page,rows,typeDate) {
        let record = m.searchJson.val();
        $.myAjaxs({
            url: url_left + "/search",
            async: true,
            cache: false,
            type: "post",
            data: 'SearchJson='+record+"&page="+page+"&rows="+rows,
            dataType: "json",
            success: function (result) {
                var trList = $("#dailyTable  tr:not(:first)");
                var trPosList = $("#dailyPosTable  tr:not(:first)");
                trList.remove();
                trPosList.remove();
                if (result.success) {
                  var  dataList = result.o.data;

                    // 总页数
                    var  totalPage = result.o.totalPage;
                    // 总条数
                    var count = result.o.count;
                    if (typeDate=='2'){
                        for (let i = 0; i < dataList.length; i++) {
                            var item = dataList[i];
                            let tempTrHtml = '<tr style="text-align: center;">' +
                                '<td title="'+isEmpty(item.storeCd)+'" style="text-align: left">'+isEmpty(item.storeCd)+'</td>' +
                                '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                                '<td title="'+isEmpty(fmtIntDate(item.saleDate))+'" style="text-align: center">'+isEmpty(fmtIntDate(item.saleDate))+'</td>' +
                                '<td title="'+toThousands(item.avgCustomerNo)+'" style="text-align:right">'+toThousands(item.avgCustomerNo)+'</td>' +
                                '<td title="'+toThousands(item.time68)+'" style="text-align:right">'+toThousands(item.time68)+'</td>' +
                                '<td title="'+toThousands(item.time810)+'" style="text-align:right">'+toThousands(item.time810)+'</td>' +
                                '<td title="'+toThousands(item.time1012)+'" style="text-align:right">'+toThousands(item.time1012)+'</td>' +
                                '<td title="'+toThousands(item.time1214)+'" style="text-align:right">'+toThousands(item.time1214)+'</td>' +
                                '<td title="'+toThousands(item.shift1)+'" style="text-align:right">'+toThousands(item.shift1)+'</td>' +
                                '<td title="'+toThousands(item.time1416)+'" style="text-align:right">'+toThousands(item.time1416)+'</td>' +
                                '<td title="'+toThousands(item.time1618)+'" style="text-align:right">'+toThousands(item.time1618)+'</td>' +
                                '<td title="'+toThousands(item.time1820)+'" style="text-align:right">'+toThousands(item.time1820)+'</td>' +
                                '<td title="'+toThousands(item.time2022)+'" style="text-align:right">'+toThousands(item.time2022)+'</td>' +
                                '<td title="'+toThousands(item.shift2)+'" style="text-align:right">'+toThousands(item.shift2)+'</td>' +
                                '<td title="'+toThousands(item.time2224)+'" style="text-align:right">'+toThousands(item.time2224)+'</td>' +
                                '<td title="'+toThousands(item.time02)+'" style="text-align:right">'+toThousands(item.time02)+'</td>' +
                                '<td title="'+toThousands(item.time24)+'" style="text-align:right">'+toThousands(item.time24)+'</td>' +
                                '<td title="'+toThousands(item.time46)+'" style="text-align:right">'+toThousands(item.time46)+'</td>' +
                                '<td title="'+toThousands(item.shift3)+'" style="text-align:right">'+toThousands(item.shift3)+'</td>' +
                                '<td title="'+toThousands(item.totalAmt)+'" style="text-align:right">'+toThousands(item.totalAmt)+'</td>' +
                                '<td title="'+isEmpty(item.amName)+'" style="text-align: left">'+isEmpty(item.amName)+'</td>' +
                                '</tr>';
                            m.dailyPosTable.append(tempTrHtml);
                        }
                    }
                    if (typeDate=='1'){
                        // dataList = result.o.data;
                        //
                        // // 总页数
                        // totalPage = result.o.totalPage;
                        // // 总条数
                        // count = result.o.count;
                        for (let i = 0; i < dataList.length; i++) {

                            var item = dataList[i];
                            let tempTrHtml = '<tr style="text-align: center;">' +
                                '<td title="'+item.storeCd+'" style="text-align: left">'+item.storeCd+'</td>' +
                                '<td title="'+isEmpty(item.storeName)+'" style="text-align: left">'+isEmpty(item.storeName)+'</td>' +
                                '<td title="'+isEmpty(fmtIntDate(item.saleDate))+'" style="text-align: center">'+isEmpty(fmtIntDate(item.saleDate))+'</td>' +
                                '<td title="'+toThousands(item.avgCustomerNo)+'" style="text-align:right">'+toThousands(item.avgCustomerNo)+'</td>' +
                                '<td title="'+toThousands(item.time1214)+'" style="text-align:right">'+toThousands(item.time1214)+'</td>' +
                                '<td title="'+toThousands(item.time1416)+'" style="text-align:right">'+toThousands(item.time1416)+'</td>' +
                                '<td title="'+toThousands(item.time1618)+'" style="text-align:right">'+toThousands(item.time1618)+'</td>' +
                                '<td title="'+toThousands(item.time1820)+'" style="text-align:right">'+toThousands(item.time1820)+'</td>' +
                                '<td title="'+toThousands(item.shift1)+'" style="text-align:right">'+toThousands(item.shift1)+'</td>' +
                                '<td title="'+toThousands(item.time2022)+'" style="text-align:right">'+toThousands(item.time2022)+'</td>' +
                                '<td title="'+toThousands(item.time2224)+'" style="text-align:right">'+toThousands(item.time2224)+'</td>' +
                                '<td title="'+toThousands(item.time02)+'" style="text-align:right">'+toThousands(item.time02)+'</td>' +
                                '<td title="'+toThousands(item.time24)+'" style="text-align:right">'+toThousands(item.time24)+'</td>' +
                                '<td title="'+toThousands(item.shift2)+'" style="text-align:right">'+toThousands(item.shift2)+'</td>' +
                                '<td title="'+toThousands(item.time46)+'" style="text-align:right">'+toThousands(item.time46)+'</td>' +
                                '<td title="'+toThousands(item.time68)+'" style="text-align:right">'+toThousands(item.time68)+'</td>' +
                                '<td title="'+toThousands(item.time810)+'" style="text-align:right">'+toThousands(item.time810)+'</td>' +
                                '<td title="'+toThousands(item.time1012)+'" style="text-align:right">'+toThousands(item.time1012)+'</td>' +
                                '<td title="'+toThousands(item.shift3)+'" style="text-align:right">'+toThousands(item.shift3)+'</td>' +
                                '<td title="'+toThousands(item.totalAmt)+'" style="text-align:right">'+toThousands(item.totalAmt)+'</td>' +
                                '<td title="'+isEmpty(item.amName)+'" style="text-align: left">'+isEmpty(item.amName)+'</td>' +
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
                var typeDate = m.typeDate.val();
                // 分页获取数据
                getData(page,rows,typeDate);
            }
        });
    }

    //验证检索项是否合法
    var verifySearch = function(){
        var typeDate=m.typeDate.val();
        let _startDate ='';
        let _endDate=' ';
        if (typeDate=='1'){
            _startDate = formatDate(m.effstartDate.val());
            _endDate = formatDate(m.effendDate.val());
        }else {
            _startDate = formatDate(m.PosstartDate.val());
            _endDate = formatDate(m.PosendDate.val());
        }

        if (typeDate=='1'){
            if(m.effstartDate.val()==""||m.effstartDate.val()==null){
                _common.prompt("Please enter a Sales Date!",5,"error"); // 开始日期不可以为空
                $("#effstartDate").focus();
                $("#effstartDate").css("border-color","red");
                return false;
            }else {
                $("#effstartDate").css("border-color","#CCC");
            }
            if(m.effendDate.val()==""||m.effendDate.val()==null){
                _common.prompt("Please enter a Sales Date!",5,"error"); // 结束日期不可以为空
                $("#effendDate").focus();
                $("#effendDate").css("border-color","red");
                return false;
            }else {
                $("#effendDate").css("border-color","#CCC");
            }
        }
        if (m.typeDate.val()=='2'){
            if(m.PosstartDate.val()==""||m.PosstartDate.val()==null){
                _common.prompt("Please enter a Sales Date!",5,"error"); // 开始日期不可以为空
                $("#PosstartDate").focus();
                $("#PosstartDate").css("border-color","red");
                return false;
            }else {
                $("#PosstartDate").css("border-color","#CCC");
            }
            if(m.PosendDate.val()==""||m.PosendDate.val()==null){
                _common.prompt("Please enter a Sales Date!",5,"error"); // 结束日期不可以为空
                $("#PosendDate").focus();
                $("#PosendDate").css("border-color","red");
                return false;
            }else {
                $("#PosendDate").css("border-color","#CCC");
            }
        }

        if(new Date(fmtDate(fmtIntDate( _startDate))).getTime()>new Date(fmtDate(fmtIntDate(  _endDate))).getTime()){
            _common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
            return false;
        }
        if(_startDate!=""&&_endDate!=""){
            var _StartDate =new Date(fmtDate(fmtIntDate(_startDate))).getTime();
            var _EndDate = new Date(fmtDate(fmtIntDate(_endDate))).getTime();
            var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
            if(difValue >62){
                _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
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
    // 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
    function fmtIntDate(date){
        if(date==null||date.length!=8){return "";}
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
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
var _index = require('dailySaleReport');
_start.load(function (_common) {
    _index.init(_common);
});
