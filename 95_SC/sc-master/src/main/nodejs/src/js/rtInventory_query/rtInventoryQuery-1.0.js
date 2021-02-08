require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('rtInventoryQuery', function (_common) {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        reThousands = null,
        // toThousands = null,
        // getThousands = null,
        selectTrTemp = null,
        url_root = "",
        tempTrObjValue = {},//临时行数据存储
        common = null;

    var m = {
        toKen: null,
        use: null,
        error_pcode: null,
        identity: null,
        _common: null,
        searchJson: null,
        myUrl: null,
        viewSts: null,
        cancel: null,
        returnsViewBut: null,
        businessDate: null,
        myConfirm : null, // 确认提示框
        // 查询部分
        aStore : null,
        inventory_date: null,
        search_code_input: null,
        stock_date: null,
        item_code:null,
        item_barcode: null,
        item_name: null,
        search: null,
        reset: null,
        // 库存信息
        itemCode: null,
        itemBarcode: null,
        itemName: null,
        dep: null,
        pma: null,
        category: null,
        subCategory: null,
    }

    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    // 页面初始化
    function init(_common) {
        createJqueryObj();
        common = _common;
        url_root = _common.config.surl;
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/rtInventoryQuery";
        reThousands=_common.reThousands;
        // toThousands=_common.toThousands;
        // getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 加载下拉列表
        // getSelectValue();
        // 初始化店铺运营组织检索
        // _common.initOrganization();
        // 初始化店铺名称下拉
        _common.initStoreform();
        _common.initCategoryAutoMatic();
        //根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        //表格内按钮事件
        table_event();
        getSelectValue();
        // 初始化查询加载数据
        // m.search.click();

    }
    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function (flgType) {
        switch (flgType) {
            case "1":
                var businessDate = m.businessDate.val();
                if(businessDate!=null&&businessDate!=''){
                    businessDate = fmtIntDate(businessDate);
                    m.stock_date.val(businessDate);
                }
                initTable1();//列表初始化
                break;
            case "2":
                break;
            default:
            m.error_pcode.show();
            m.main_box.hide();
        }

    }

    //表格内按钮点击事件
    var table_event = function () {
        // 导出按钮点击事件
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

    // 画面按钮点击事件
    var but_event = function () {
        //清空按钮
        m.reset.on("click", function () {
            $("#storeRemove").click();
            $("#depRemove").click();
            $("#VendorRemove").click();
            m.item_code.val("");
            m.item_barcode.val("");
            m.item_name.val("");
            m.stock_date.css("border-color","#CCC");
            $("#aStore").css("border-color","#CCC");
            $("#amRemove").click();
            selectTrTemp = null;
            common.clearTable();
        });

        //检索按钮点击事件
        m.search.on("click", function () {
            if (verifySearch()) {
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/getInventory");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });

  }

    //验证检索项是否合法
    var verifySearch = function () {
        var _stockDate = m.stock_date.val();
        var _storeCd = $("#aStore").attr("k");
        if (_stockDate == null || _stockDate == "") {
            common.prompt("Please select the Stock Date!", 3, "info");
            m.stock_date.focus();
            m.stock_date.css("border-color","red");
            return false;
        }else {
            m.stock_date.css("border-color","#CCC");
        }
        if (_storeCd == null || _storeCd == "") {
            common.prompt("Please select a store first!", 3, "info");
            $("#aStore").focus();
            $("#aStore").css("border-color","red");
            return false;
        }else {
            $("#aStore").css("border-color","#CCC");
        }
        return true;
    }

    //拼接检索参数
    var setParamJson = function () {
        var _StockDate = subfmtDate(m.stock_date.val())||null;
        // 创建请求字符串
        var searchJsonStr = {
            storeCd:$("#aStore").attr("k"),
            stockDate: _StockDate,
            itemCode: m.item_code.val().trim(),
            itemBarcode: m.item_barcode.val().trim(),
            depId:m.dep.attr("k"),
            pmaId:m.pma.attr("k"),
            categoryId: m.category.attr("k"),
            omCode:$("#om").attr("k"),
            ocCode:$("#oc").attr("k"),
            ofcCode:$("#am").attr("k"),
            subCategoryId: m.subCategory.attr("k"),
            vendorId: $("#vendorId").attr("k")
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //表格初始化-库存样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTable").zgGrid({
            param: paramGrid,
            title: "Query Result",
            colNames: ["Item Barcode", "Item Code",  "Item Name", "Specification", "UOM","Department","Category","Sub Category","Vendor Code","Vendor Name","Real Time Stock Quantity",
                "Inventory Quantity(Previous Day)","Sales Quantity the day", "Receiving Quantity the day", "Return Quantity the day", "Received Modification Quantity the day", "Returned Modification Quantity the day", "Inventory Adjustment Quantity the day",
                "Stock on Order Quantity","Write-off Quantity the day","Transfer In Quantity","Transfer Out Quantity","Area Manager Code","Area Manager Name","Operation Controller Code","Operation Controller Name","Operation Manager Code","Operation Manager Name"],
            colModel: [
                {name: "itemBarcode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "itemCode", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "itemName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "specification", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "uom", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "pmaName", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "categoryName", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "subCategoryName", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "vendorId", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {name: "vendorName", type: "text", text: "left", width: "100", ishide: false, css: ""},
                {
                    name: "realtimeQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "onHandQty",
                    type: "text",
                    text: "right",
                    width: "230",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "saleQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "receiveQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "storeReturnQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "receiveCorrQty",//收货更正
                    type: "text",
                    text: "right",
                    width: "260",
                    ishide: true,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "returnCorrQty",//退货更正
                    type: "text",
                    text: "right",
                    width: "260",
                    ishide: true,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "adjustQty",
                    type: "text",
                    text: "right",
                    width: "300",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "onOrderQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "scrapQty", // 报废数量
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "transferInQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "transferOutQty",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",
                    getCustomValue:getThousands
                },
                {
                    name: "ofc",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: ""

                },
                {
                    name: "ofcName",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",

                },
                {
                    name: "oc",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",

                },
                {
                    name: "ocName",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",

                },
                {
                    name: "om",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",

                },
                {
                    name: "omName",
                    type: "text",
                    text: "right",
                    width: "200",
                    ishide: false,
                    css: "",

                }
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox: false,
            freezeHeader:false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                if(resData.message != null && resData.message !== ""){
                    // 提示 没有连接上ES(库存异动)
                    common.prompt(resData.message, 5, "info");
                }
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行

                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
            ],
        });
    }

    // 获取传票类型名称
    function getTypeName(tdObj, value){
        var _value = "";
        switch (value) {
            case "601":
                _value = "In-store Transfer In";
                break;
            case "602":
                _value = "In-store Transfer Out";
                break;
            case "603":
                _value = "Inventory Write-off";
                break;
            case "604":
                _value = "Stock Adjustment";
                break;
            case "605":
                _value = "Take Stock";
                break;
            case "500":
                _value = "Transfer Instructions";
                break;
            case "501":
                _value = "Store Transfer In";
                break;
            case "502":
                _value = "Store Transfer Out";
                break;
        }
        return $(tdObj).text(_value);
    }
  function getSelectValue() {
      am = $("#am").myAutomatic({
          url: url_root + "/ma1000/getAMByPM",
          ePageSize: 10,
          startCount: 0,
      });
      om = $("#om").myAutomatic({
          url: url_root + "/ma1000/getOM",
          ePageSize: 10,
          startCount: 0,
      });
      oc = $("#oc").myAutomatic({
          url: url_root + "/ma1000/getOC",
          ePageSize: 10,
          startCount: 0,
      });
      vendorId = $("#vendorId").myAutomatic({
          url: url_root + "/vendorMaster/get",
          ePageSize: 10,
          startCount: 0
      });
  }
    //number格式化
    var formatNum = function(tdObj,value){
        return $(tdObj).text(fmtIntNum(value));
    }
    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

    //格式化数字带千分位
    function fmtIntNum(val){
        if(val==null||val==""){
            return "0";
        }
        var reg=/\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    //字符串日期格式转换：dd/mm/yyyy → yyyymmdd
    function fmtStringDate(date) {
        var res = "";
        date = date.replace(/\//g, "");
        res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 日期处理
    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }
    // 去除千分位
    function reThousands(num) {
        num+='';
        return num.replace(/,/g, '');
    }

    function getThousands(tdObj, value) {
        var dit = 0;
        if(value !== ""){
            var str = value + "";
            if (str.indexOf(".") !== -1) {
                dit = value.toString().split(".")[1].length;
            }
        }
        var num = toThousands(value,dit);
        // 省略小数点后为0的数
        return $(tdObj).text(num);
    }

    // 设置千分位, 四舍五入 保留小数位 dit
    function toThousands(num,dit) {
        if (!num) {
            return '0';
        }
        dit = typeof dit !== 'undefined' ? dit : 0;
        num = num + '';
        if(dit > 4){
            dit = 4;
        }
        num = parseFloat(num).toFixed(dit);

        var source = String(num).split(".");//按小数点分成2部分
        source[0] = source[0].replace(new RegExp('(\\d)(?=(\\d{3})+$)', 'ig'), "$1,");//只将整数部分进行都好分割
        num = source.join(".");//再将小数部分合并进来
        return num;
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('rtInventoryQuery');
_start.load(function (_common) {
    _index.init(_common);
});
