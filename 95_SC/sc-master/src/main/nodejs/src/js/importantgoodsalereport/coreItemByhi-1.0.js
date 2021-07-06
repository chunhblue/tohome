require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");

define('coreItemByhi' , function (){
    var self = {};
    var url_left = "",
        url_root="",
        systemPath = "",
        reThousands = null,
        toThousands = null,
        getThousands = null,
        a=null,
        page = 1, // 当前页数, 初始为 1
        rows = 10; // 每页显示条数, 默认为 10;
    var m={
        search:null,
        core_type:null,
        startDate:null,
        endDate:null,
        coreItemType:null,
        am:null,
        dep: null,
        pma: null,
        category: null,
        subCategory: null,
        searchJson:null,
        dailyTable:null,
        aRegion:null,
        aDistrict: null,
        aCity: null,
        aStore:null,
        reset:null,
        paramGrid:null,

    }
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }
    function init(_common) {
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;

        createJqueryObj();
        url_root= _common.config.surl;
        url_left = _common.config.surl + "/coreItemByhi";
        // 初始化 大中小分类
        _common.initCategoryAutoMatic();
        _common.initDate(m.startDate,m.endDate);
        // 初始化店铺运营组织检索
          initOrganization();
          getSelectValue();
         but_event();
       //  $("#am").prop("disabled",true);
        $("#amRemove").click();

    }
    $("#amRemove").on("click",function (e) {
        $.myAutomatic.cleanSelectObj(am);
    })
    var initOrganization = function () {
        // 初始化，子级禁用
        $("#aRegion").attr("disabled", true);
        $("#aCity").attr("disabled", true);
        $("#aDistrict").attr("disabled", true);
        $("#aStore").attr("disabled", true);
        $("#regionRefresh").hide();
        $("#regionRemove").hide();
        $("#cityRefresh").hide();
        $("#cityRemove").hide();
        $("#districtRefresh").hide();
        $("#districtRemove").hide();
        $("#storeRefresh").hide();
        $("#storeRemove").hide();
        // 输入框事件绑定
        a_region = $("#aRegion").myAutomatic({
            url:  url_root + "/roleStore/getRegion",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_city);
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                var rinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&regionCd=" + rinput;
                $.myAutomatic.replaceParam(a_city, str);
                $.myAutomatic.replaceParam(a_district, str);
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_city = $("#aCity").myAutomatic({
            url:  url_root + "/roleStore/getCity",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                var rinput = $("#aRegion").attr("k");
                var cinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&cityCd=" + cinput;
                $.myAutomatic.replaceParam(a_district, str);
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_district = $("#aDistrict").myAutomatic({
            url:  url_root + "/roleStore/getDistrict",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_store);
                var rinput = $("#aRegion").attr("k");
                var cinput = $("#aCity").attr("k");
                var dinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&districtCd=" + dinput;
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_store = $("#aStore").myAutomatic({
            url:  url_root + "/roleStore/getStore",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj){
                $('#hidStore').val(thisObj.attr('k'));
            }
        });

        // 选值栏位清空按钮事件绑定
        $("#regionRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_city);
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $.myAutomatic.replaceParam(a_city, null);
            $.myAutomatic.replaceParam(a_district, null);
            $.myAutomatic.replaceParam(a_store, null);
        });
        $("#cityRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $.myAutomatic.replaceParam(a_district, null);
            $.myAutomatic.replaceParam(a_store, null);
        });
        $("#districtRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_store);
            $.myAutomatic.replaceParam(a_store, null);
        });

        // 如果只有一条店铺权限，则默认选择
        $.myAjaxs({
            url:url_root+"/roleStore/getStoreByRole",
            async:true,
            cache:false,
            type :"post",
            data :null,
            dataType:"json",
            success:function(re){
                if(re.success){
                    var obj = re.o;
                    $("#aCity").attr("disabled", false);
                    $("#aDistrict").attr("disabled", false);
                    $("#aStore").attr("disabled", false);
                    $("#cityRefresh").show();
                    $("#cityRemove").show();
                    $("#districtRefresh").show();
                    $("#districtRemove").show();
                    $("#storeRefresh").show();
                    $("#storeRemove").show();
                    $.myAutomatic.setValueTemp(a_region,obj.regionCd,obj.regionName);
                    $.myAutomatic.setValueTemp(a_city,obj.cityCd,obj.cityName);
                    $.myAutomatic.setValueTemp(a_district,obj.districtCd,obj.districtName);
                    $.myAutomatic.setValueTemp(a_store,obj.storeCd,obj.storeName);
                    // 替换子级查询参数
                    var str = "&regionCd=" + obj.regionCd;
                    $.myAutomatic.replaceParam(a_city, str);
                    str = str + "&cityCd=" + obj.cityCd;
                    $.myAutomatic.replaceParam(a_district, str);
                    str = str + "&districtCd=" + obj.districtCd;
                    $.myAutomatic.replaceParam(a_store, str);
                }
            }
        });
    }
    var verifySearch = function(){
        if(m.startDate.val()==""||m.startDate.val()==null){
            _common.prompt("Please enter a Date!",5,"error"); // 开始日期不可以为空
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
            _common.prompt("Please enter a Date!",5,"error"); // 结束日期不可以为空
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
        if(m.core_type.val()==null || m.core_type.val()=="" || m.core_type.val()=="00"){
            _common.prompt("Please select a core item type!",5,"error");
            $("#core_type").focus();
            $("#core_type").css("border-color","red");
            return  false;
        }else {
            $("#core_type").css("border-color","#CCC");
        }

   if (m.core_type.val()=="01"){
       if ($("#aRegion").attr("k") == null || $("#aRegion").attr("k") == "") {
           _common.prompt("Please select a Region!",5,"error");
           $("#aRegion").focus();
           return false;
       }
   }
   if (m.core_type.val()=="02") {
       if ($("#aCity").attr("k") == null || $("#aCity").attr("k") == "") {
           _common.prompt("Please select a City!",5,"error");
           $("#aRegion").focus();
           return false;
       }
   }
        if (m.core_type.val()=="03") {
            if ($("#aDistrict").attr("k") == null || $("#aDistrict").attr("k") == "") {
                _common.prompt("Please select a Distrit",5,"error");
                $("#aRegion").focus();
                return false;
            }
        }
        if(m.core_type.val()=="04"){
            if ($("#aStore").attr("k") == null || $("#aStore").attr("k") == "") {
                _common.prompt("Please select a Store",5,"error");
                $("#aRegion").focus();
                return  false;
            }
        }
        return true;
    }

    var setParamJson=function () {
        let am = m.am.attr('k');
        let dep = m.dep.attr('k');
        let pma = m.pma.attr('k');
        let regionCd=m.aRegion.attr('k');
        let cityCd=m.aCity.attr("k");
        let districtCd=m.aDistrict.attr("k");
        let category = m.category.attr('k');
        let storeCd= m.aStore.attr('k');
        let subCategory = m.subCategory.attr('k');
        let _startDate = formatDate(m.startDate.val());
        let _endDate = formatDate(m.endDate.val());
        let searchJsonStr = {
            'coreItemType': m.core_type.val(),
            'regionCd':regionCd,
            'cityCd':cityCd,
            'districtCd':districtCd,
            'amCd': am,
            'depCd':dep,
            'pmaCd': pma,
            'categoryCd': category,
            'storeCd':storeCd,
            'subCategoryCd': subCategory,
            'startDate': _startDate,
            'endDate': _endDate,
            'page': page,
            'rows': rows,
        }
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }
    var but_event=function () {
          $("#core_type").on("change", function () {
              var thisObj = $(this);
              var thisVal = thisObj.val();
              // region
              if (thisVal == "01") {
                    $("#regionRemove").click();
                    $("#aRegion").attr("disabled", false);
                    $("#regionRefresh").show();
                    $("#regionRemove").show();
                    $("#aCity").attr("disabled", true);
                    $("#cityRefresh").hide();
                    $("#cityRemove").hide();
                    $("#aStore").attr("disabled",true);
                    $("#aDistrict").attr("disabled",true);
                    $("#districtRefresh").hide();
                    $("#districtRemove").hide();
                    $("#storeRefresh").hide();
                    $("#storeRemove").hide();
              }
              // city
              if (thisVal == "02") {
                  $("#regionRemove").click();
                  if ($("#aRegion").attr("k") != null || $("#aRegion").attr("k") != "") {
                      $("#aRegion").attr("disabled", false);
                      $("#regionRefresh").show();
                      $("#regionRemove").show();
                      $("#aCity").attr("disabled", false);
                      $("#cityRefresh").show();
                      $("#cityRemove").show();
                  }
              }
              //district
              if (thisVal == "03") {
                  $("#regionRemove").click();
                //  $("#cityRemove").click();
                  if ($("#aCity").attr("k") != null || $("#aCity").attr("k") != "") {
                      $("#aRegion").attr("disabled", false);
                      $("#regionRefresh").show();
                      $("#regionRemove").show();
                      $("#aCity").attr("disabled", false);
                      $("#cityRefresh").show();
                      $("#cityRemove").show();
                      $("#aDistrict").attr("disabled",false);
                      $("#districtRefresh").show();
                      $("#districtRemove").show();
                  }
              }
              if (thisVal == "04") {
                    $("#regionRemove").click();
                //  $("#cityRemove").click();
               //   $("#districtRemove").click();
                  if ($("#aDistrict").attr("k") != null || $("#aDistrict").attr("k") != "") {
                      $("#aRegion").attr("disabled", false);
                      $("#regionRefresh").show();
                      $("#regionRemove").show();
                      $("#aStore").attr("disabled",false);
                      $("#aCity").attr("disabled", false);
                      $("#cityRefresh").show();
                      $("#cityRemove").show();
                      $("#aDistrict").attr("disabled",false);
                      $("#districtRefresh").show();
                      $("#districtRemove").show();
                      $("#storeRefresh").show();
                      $("#storeRemove").show();
                  }
              }
              if (thisVal=="00"){
                  $("#aRegion").attr("disabled", true);
                  $("#aCity").attr("disabled", true);
                  $("#aDistrict").attr("disabled", true);
                  $("#aStore").attr("disabled", true);
                  $("#regionRefresh").hide();
                  $("#regionRemove").hide();
                  $("#cityRefresh").hide();
                  $("#cityRemove").hide();
                  $("#districtRefresh").hide();
                  $("#districtRemove").hide();
                  $("#storeRefresh").hide();
                  $("#storeRemove").hide();
              }

          });
          m.search.on("click",function () {
              if (verifySearch()) {
                  page=1;
                  setParamJson();
                  getData(page,rows);
              }

          })
          m.reset.on("click",function () {
            m.core_type.val("");
            m.aCity.val("");
            m.aDistrict.val("");
            m.aStore.val("");
            m.aRegion.val("");
            $("#aDistrict").prop("disabled",true);
            $("#aStore").prop("disabled",true);
            $("#aCity").prop("disabled",true);
            $("#depRemove").click();
            $("#amRemove").click();
            m.startDate.val('');
            m.endDate.val('');
            $("#core_type").find("option:first-child").prop("selected",true);
           $("#regionRemove").click();
              _common.initDate(m.startDate,m.endDate);
              page=1;
              // 清除分页
              _common.resetPaging();
              $("#dailyTable").find("tr:not(:first)").remove();
         })
        $("#export").on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });

      }
    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }
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
                    // 总条数
                    let count = result.o.count;
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                         //   '<td title="' + isEmpty(item.seqNo) + '" style="text-align: right">' + isEmpty(item.seqNo) + '</td>' +
                            '<td title="' + isEmpty(item.storeCd) + '" style="text-align: left">' + isEmpty(item.storeCd) + '</td>' +
                            '<td title="' + isEmpty(item.storeName) + '" style="text-align: left">' + isEmpty(item.storeName) + '</td>' +
                            '<td title="' + formatBusinessDate(item.accDate) + '" style="text-align: center">' + formatBusinessDate(item.accDate) + '</td>' +
                            '<td title="' + isEmpty(item.depName) + '" style="text-align: left">' + isEmpty(item.depName) + '</td>' +
                            '<td title="' + isEmpty(item.pmaName) + '" style="text-align: left">' + isEmpty(item.pmaName) + '</td>' +
                            '<td title="' + isEmpty(item.categoryName) + '" style="text-align: left">' + isEmpty(item.categoryName) + '</td>' +
                            '<td title="' + isEmpty(item.subCategoryName) + '" style="text-align: left">' + isEmpty(item.subCategoryName) + '</td>' +
                            '<td title="' + isEmpty(item.articleId) + '" style="text-align: right">' + isEmpty(item.articleId) + '</td>' +
                            '<td title="' + isEmpty(item.articleName) + '" style="text-align: left">' + isEmpty(item.articleName) + '</td>' +
                            '<td title="' + isEmpty(item.barcode) + '" style="text-align: right">' + isEmpty(item.barcode) + '</td>' +
                            '<td title="' + isEmpty(item.seqNo) + '" style="text-align: right">' + isEmpty(item.seqNo) + '</td>' +
                            '<td title="' + toThousands(item.saleQty) + '" style="text-align: right">' + toThousands(item.saleQty) + '</td>' +
                            '<td title="' + toThousands(item.saleAmt) + '" style="text-align: right">' + toThousands(item.saleAmt) + '</td>' +
                            '<td title="' + isEmpty(item.ofc) + '" style="text-align: left">' + isEmpty(item.ofc) + '</td>' +
                            '<td title="' + isEmpty(item.amName) + '" style="text-align: left">' + isEmpty(item.amName) + '</td>' +
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
    };

    var formatBusinessDate = function (businessDate) {
        if (!businessDate) {
            return '';
        }
        let year = businessDate.substring(0,4);
        let month = businessDate.substring(4,6);
        let day = businessDate.substring(6,8);
        return new Date(year+'/'+month+'/'+day).Format('dd/MM/yyyy');
    };
 var isEmpty = function (str) {
            if (str == null || str === '') {
                return '';
            }
            return str;
        };
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

    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:url_root+"/cm9010/getCode",
            async:false,
            cache:false,
            type :"post",
            data :"codeValue="+code,
            dataType:"json",
            success:function(result){
                var selectObj = $("#" + selectId);
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+"Failed to load data!",5,"error"); // 数据加载失败
            }
        });
    }
    function getSelectValue(){
        am = $("#am").myAutomatic({
            url: url_root + "/ma1000/getAMByPM",
            ePageSize: 10,
            startCount: 0,
        });
        // 加载select
        initSelectOptions("重点商品类型选择","core_type", "00620");
    }
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
var _index = require('coreItemByhi');
_start.load(function (_common) {
    _index.init(_common);
})