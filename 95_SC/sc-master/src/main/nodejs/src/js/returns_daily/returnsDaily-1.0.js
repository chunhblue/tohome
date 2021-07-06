require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('returnsDaily', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        a_cashier = "",
        am = "",
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
        returnDate: null,
        shift: null,
        a_cashier: null,
        vendorCd: null,
        dailyTable: null,
        articleId: null,
        articleName: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        author: null,
        businessDate: null,
        userStoreCd: null,
        userStoreName: null,
        am: null,
        searchJson: null,
        dep: null,
        pma: null,
        category: null,
        subCategory: null,
        totalAmt: null,
        calType: null,
        nonSaleType:null,
        barcode:null
    };
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    //拼接检索参数
    var setParamJson = function () {
        // let barcode = m.barcode.val().trim();
        let articleName = m.articleName.val().trim();
        let returnDate = m.returnDate.val().trim();
        let cashierCd = m.a_cashier.attr('k');
        let am = m.am.attr('k');
        let dep = m.dep.attr('k');
        let pma = m.pma.attr('k');
        let category = m.category.attr('k');
        let subCategory = m.subCategory.attr('k');
        let shift = m.shift.val().trim();
        let calType = m.calType.val();
        let nonSaleType = m.nonSaleType.val();
        let totalAmt = reThousands(m.totalAmt.val().trim());
        // 创建请求字符串
        let searchJsonStr = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            // 'barcode': barcode,
            'articleName': articleName,
            'returnDate': formatDate2(returnDate),
            'cashierCd': cashierCd,
            'am': am,
            'depCd': dep,
            'pmaCd': pma,
            'categoryCd': category,
            'subCategoryCd': subCategory,
            'shift': shift,
            'calType': calType,
            'nonSaleType':nonSaleType,
            'totalAmt': totalAmt,
            'page': page,
            'rows': rows,
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 格式化日期
    var formatDate = function (dateStr) {
        if (!dateStr) {
            return '';
        }
        return new Date(dateStr).Format('dd/MM/yyyy hh:mm:ss')
    };

    // 06/03/2020 -> 20200306
    var formatDate2 = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/returnsDaily";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        // 初始化店铺运营组织检索
        _common.initOrganization();
        _common.initCategoryAutoMatic();
        initAutoMatic();
        but_event();
        m.returnDate.val(formatBusinessDate(m.businessDate.val()))
        // m.search.click();
    }

    var formatBusinessDate = function (businessDate) {
        if (!businessDate) {
            return '';
        }
        let year = businessDate.substring(0,4);
        let month = businessDate.substring(4,6);
        let day = businessDate.substring(6,8);
        return new Date(year+'/'+month+'/'+day).Format('dd/MM/yyyy');
    };

    var initAutoMatic = function () {
        //收银员
        a_cashier = $("#a_cashier").myAutomatic({
            url: systemPath + "/cashierAmount/getCashierList",
            ePageSize: 10,
            startCount: 0,
        });

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
            $("#a_cashier_clear").click();
            $("#amRemove").click();
            m.articleId.val('');
            m.articleName.val('');
            m.returnDate.val(formatBusinessDate(m.businessDate.val()))
            m.shift.val('');
            m.calType.val('');
            $("#returnDate").css("border-color","#CCC");
            page=1;
            // 清除分页
            _common.resetPaging();
        });

        m.returnDate.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });

        m.returnDate.focus(function(){
            if ($("#checkFlg").val() === "1") {
                $("#returnDate").datetimepicker('setStartDate', new Date(new Date(fmtDate(m.businessDate.val())).getTime() - 24 * 60 * 60 * 1000));
            } else {
                $("#returnDate").datetimepicker('setStartDate', null);
            }
        });

        // 验证检索项是否合法
        var verifySearch = function(){
            if (!m.returnDate.val()) {
                _common.prompt("Please select the sales date!",5,"info");/*请选择销售日期*/
                $("#returnDate").css("border-color","red");
                $("#returnDate").focus();
                return false;
            }else if(_common.judgeValidDate(m.returnDate.val())){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#returnDate").focus();
                return false;
            }else {
                $("#returnDate").css("border-color","#CCC");
            }
            let totalAmt = reThousands(m.totalAmt.val());
            if(m.calType.val() != null && m.calType.val() !== ''){
                if (!m.totalAmt.val()) {
                    _common.prompt("Please enter the Total Amount!",3,"info");
                    m.totalAmt.focus();
                    return false;
                }
            }
            if(m.totalAmt.val() != null && m.totalAmt.val() !== ''){
                if (!m.calType.val()) {
                    _common.prompt("Please select the comparison method!",3,"info");
                    m.calType.focus();
                    return false;
                }
                if(!checkNum(totalAmt)){
                    m.totalAmt.focus();
                    m.totalAmt.css("border-color","red");
                    _common.prompt("Cash can only be a Positive number!",5,"error");
                    return false;
                }else {
                    m.totalAmt.css("border-color","#CCC");
                }
            }
            return true;
        };
        // 判断是否是数字
        var checkNum = function(value){
            var reg = /^[0-9]*$/;
            return reg.test(value);
        }
        m.search.click(function () {
            if(verifySearch()) {
                _common.loadPaging(1,1,1,10);
                page=1;
                setParamJson();
                getData(page,rows);
            }
        });

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
                        let dataList =  result.o.data;
                        // 总页数
                        let totalPage = result.o.totalPage;
                        // 总条数
                        let count = result.o.count;

                        let orderQty = 0; // 总数量
                        let totalAmt = 0; // 总金额
                        if (dataList.length < 1) {
                            _common.prompt("No data found!", 5, "info");/*查询数据为空*/
                            return;
                        }
                        for (let i = 0; i < dataList.length; i++) {
                            var item = dataList[i];
                            orderQty += parseFloat(item.orderQty);
                            totalAmt += parseFloat(item.totalAmt);

                            let tempTrHtml = '<tr>' +
                                "<td title='"+isEmpty(item.storeCd)+"' style='text-align:left;'>" + isEmpty(item.storeCd) + "</td>" +
                                "<td title='"+isEmpty(item.storeName)+"' style='text-align:left;'>" + isEmpty(item.storeName) + "</td>" +
                                "<td title='"+isEmpty(item.articleId)+"' style='text-align:right;'>" + isEmpty(item.articleId) + "</td>" +
                                "<td title='"+isEmpty(item.articleName) +"' style='text-align:left;'>" + isEmpty(item.articleName)  + "</td>" +
                                // "<td title='"+isEmpty(item.barcode) +"' style='text-align:right;'>" + isEmpty(item.barcode)  + "</td>" +
                                "<td title='"+formatDate(isEmpty(item.tranDate)) +"' style='text-align:center;'>" + formatDate(isEmpty(item.tranDate))  + "</td>" +
                                "<td title='"+isEmpty(item.posId) +"' style='text-align:right;'>" + isEmpty(item.posId) + "</td>" +
                                "<td title='"+isEmpty(item.tranSerialNo) +"' style='text-align:right;'>" + isEmpty(item.tranSerialNo) + "</td>" +
                                "<td title='"+isEmpty(item.nonSaleType) +"' style='text-align:left;'>" + isEmpty(item.nonSaleType) + "</td>" +
                                "<td title='"+toThousands(item.orderQty)  +"' style='text-align:right;'>" + toThousands(item.orderQty)  + "</td>" +
                                "<td title='"+toThousands(item.totalAmt)  +"' style='text-align:right;'>" + toThousands(item.totalAmt)  + "</td>" +
                                "<td title='"+  isEmpty(item.cashierId) +"' style='text-align:right;'>" +   isEmpty(item.cashierId)  + "</td>" +

                                "<td title='"+ isEmpty(item.cashierName) +"' style='text-align:left;'>" +  isEmpty(item.cashierName)  + "</td>" +
                                // "<td title='"+ isEmpty(item.amCd) +"' style='text-align:left;'>" +  isEmpty(item.amCd)  + "</td>" +
                                "<td title='"+ isEmpty(item.amName) +"' style='text-align:left;'>" +  isEmpty(item.amName)  + "</td>" +
                                "<td title='"+ isEmpty(item.mode) +"' style='text-align:left;'>" +  isEmpty(item.mode)  + "</td>" +
                                '</tr>';
                            m.dailyTable.append(tempTrHtml);
                        }

                        // 合计行
                        let totalTempTrHtml = '<tr style="background-color: #87CEFF">' +
                            "<td></td>" +
                            "<td></td>" +
                            // "<td></td>" +
                            "<td></td>" +
                            "<td></td>" +
                            "<td></td>" +
                            "<td></td>" +
                            "<td></td>" +
                            "<td title='Total:'>Total:</td>" +
                            "<td title='" + toThousands(result.o.totalQty)  +"' style='text-align:right;'>" + toThousands(result.o.totalQty) + "</td>" +
                            "<td title='" + toThousands(result.o.totalAmount)  +"' style='text-align:right;'>" + toThousands(result.o.totalAmount) + "</td>" +
                            "<td></td>" +
                            "<td></td>" +
                            '<td></td>' +
                            '<td></td>' +
                            '</tr>';
                        if (totalPage===page){
                            $("#dailyTable").append(totalTempTrHtml)
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
        };

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

        m.totalAmt.blur(function(){
            if (m.totalAmt.val()==""){
                m.totalAmt.val(this.value);
            }else {
             m.totalAmt.val(toThousands(this.value));
            }

        });
        //光标进入，去除金额千分位，并去除小数后面多余的0
        m.totalAmt.focus(function(){
            if (m.totalAmt.val()==""){
                m.totalAmt.val(this.value);
            }else {
                m.totalAmt.val(toThousands(this.value));
            }
        });
    };

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };

    var isEmpty2 = function (str) {
        if (str == null || str == undefined || str == '') {
            return '0.00';
        }
        return parseFloat(str).toFixed(2);
    };
    // yyyyMMdd --> yyyy/MM/dd 06:00:00
    function fmtDate(date){
        var res = "";
        res = date.substring(0,4)+"/"+date.substring(4,6)+"/"+date.substring(6,8);
        res = res + " 06:00:00";
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
var _index = require('returnsDaily');
_start.load(function (_common) {
    _index.init(_common);
});
