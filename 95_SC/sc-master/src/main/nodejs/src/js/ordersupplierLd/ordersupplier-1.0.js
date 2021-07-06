require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("jqprint");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");


define('orderDirectSupplier', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        vendorId = null,
        tempTrObjValue = {},//临时行数据存储
        url_root = '';
    const KEY = 'ORDER_QUERY_TO_SUPPLIER';
    var m = {
        print:null,
        tableGrid: null,
        searchJson: null,
        barcode: null,
        reset: null,
        str: null,
        search: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        // store_Cd: null,
        toKen: null,
        use: null,
        error_pcode: null,
        vendorId: null,
        orderId: null,
        main_box: null,
        identity: null,
        orderStatus:null,
        order_Date: null,
        delivery_Date: null,
        orderMethod: null,
        orderDirectSupplierDateStartDate: null,
        orderDirectSupplierDateEndDate: null,
        typeId:null//审核类型id
    }
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = _common.config.surl + "/directSupplierOrder";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        but_event();
        //初始化选择框
        // initOptionList();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        initTable1();
        table_event();
        //权限验证
        isButPermission();
        // 初始化下拉
        initSearchArea();
        // 初始化检索日期
        _common.initDate(m.orderDirectSupplierDateStartDate,m.orderDirectSupplierDateEndDate);
        initParam();

    }

    // 初始化参数, back 的时候 回显 已选择的检索项
    var initParam = function () {
        let searchJson=sessionStorage.getItem(KEY);
        if (!searchJson) {
            return;
        }
        let obj = eval("("+searchJson+")");
        // 取出后就清除
        sessionStorage.removeItem(KEY);
        // 不是通过 back 正常方式返回, 清除不加载数据
        if (obj.flg=='0') {
            return;
        }

        m.orderDirectSupplierDateStartDate.val(obj.orderDirectSupplierDateStartDate);
        m.orderDirectSupplierDateEndDate.val(obj.orderDirectSupplierDateEndDate);
        m.orderMethod.val(obj.orderMethod);
        m.orderStatus.val(obj.reviewStatus);
        m.orderId.val(obj.orderId);
        $("input[name='order_type'][value='"+obj.optionTime+"']").click();
        $.myAutomatic.setValueTemp(vendorId,obj.vendorId,obj.vendorName);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamGrid();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/getOrderInformation");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", obj.page);
        tableGrid.loadData();
    }

    // 保存 参数信息
    var saveParamToSession = function () {
        let searchJson = m.searchJson.val();
        if (!searchJson) {
            return;
        }

        let obj = eval("("+searchJson+")");
        obj.regionName=$('#aRegion').attr('v');
        obj.cityName=$('#aCity').attr('v');
        obj.districtName=$('#aDistrict').attr('v');
        obj.storeName=$('#aStore').attr('v');
        obj.vendorName=$('#vendorId').attr('v');
        obj.page=tableGrid.getting("page");
        obj.orderDirectSupplierDateStartDate=m.orderDirectSupplierDateStartDate.val();
        obj.orderDirectSupplierDateEndDate=m.orderDirectSupplierDateEndDate.val();
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    var initSearchArea = function(){
        // initSelectOptions("Order Status", "orderStatus", "00430");
        initSelectOptions("Order Method", "orderMethod", "00065");

        //供应商模糊下拉
        vendorId = $("#vendorId").myAutomatic({
            url: url_left + "/getSupplier",
            ePageSize: 5,
            startCount: 0
        });
    }

    var table_event = function () {
        $("#updateOrderDetails").on("click", function (e) {
            //验证当前batch处理是否禁用
            let batchFlg = false;
            _common.batchCheck(function (result) {
                if(!result.success){//禁用
                    _common.prompt(result.message,5,"info");
                    batchFlg = false;
                }else{
                    batchFlg = true;
                }
            })
            if(!batchFlg){return batchFlg};
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            let cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId");
            //获取数据审核状态
            _common.getRecordStatus(cols["orderId"],m.typeId.val(),function (result) {
                if(result.success||result.data=='10'){//审核通过只要没有上传可修改
                    //验证订货日是否为当前业务日
                    _common.checkBusinessDate(fmtStringDate(cols["orderDate"]),cols["orderId"],function (result) {
                        if(result.success){
                            let param = "use=1&orderId="+cols["orderId"];
                            saveParamToSession();
                            top.location = url_root+"/orderDetails/edit?"+param;
                        }else{//禁用
                            _common.prompt(result.message,5,"error");
                        }
                    })
                }else if(result.data=='20'){
                    _common.prompt("The order has been received",5,"error");
                }else{
                    _common.prompt(result.message,5,"error");
                }
            });
        });

        $("#viewOrderDetails").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            let cols = tableGrid.getSelectColValue(selectTrTemp,"orderId");
            let param = "use=0&orderId="+cols["orderId"];
            saveParamToSession();
            top.location = url_root+"/orderDetails/edit?"+param;
        });

        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            let cols = tableGrid.getSelectColValue(selectTrTemp,"orderId");
            approvalRecordsParamGrid = "id="+cols["orderId"]+"&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",url_root+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });

        $("#export").on("click",function () {
            if(verifySearch()) {
                setParamGrid();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        })

        // 进入打印画面
        $("#print").on("click",function () {
            if(verifySearch()) {
                var _startDate = m.orderDirectSupplierDateStartDate.val().trim() || null;
                var _endDate = m.orderDirectSupplierDateEndDate.val().trim() || null;
                var searchJsonStr = {
                    regionCd:$("#aRegion").attr("k"),
                    cityCd:$("#aCity").attr("k"),
                    districtCd:$("#aDistrict").attr("k"),
                    storeCd:$("#aStore").attr("k"),
                    orderId: m.orderId.val().trim(),
                    orderMethod: m.orderMethod.val().trim(),
                    reviewStatus:m.orderStatus.val().trim(),
                    // vendorId: $("#vendorId").val(),
                    orderDifferentiate: '0', // 订货区分(0:供货商 1：物流)
                    orderDirectSupplierDateStartDate: _startDate,
                    orderDirectSupplierDateEndDate: _endDate,
                    optionTime: $("input[name='order_type']:checked").val(),
                };
                m.searchJson.val(JSON.stringify(searchJsonStr));
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "供应商订单查询打印", 'top=10,left=300,width=2000px,toolbar=no,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no,center:yes');
            }
        })
    }

    //加载下拉选择框
    //画面按钮点击事件
    var but_event = function () {
        $("#orderDirectSupplierDateStartDate").blur(function () {
            if (m.orderDirectSupplierDateStartDate.val() != null || m.orderDirectSupplierDateStartDate.val() != null) {
                $("#orderDirectSupplierDateStartDate").css("border-color","#CCCCCC");
            }
        });
        $("#orderDirectSupplierDateEndDate").blur(function () {
            if (m.orderDirectSupplierDateEndDate.val() != null || m.orderDirectSupplierDateEndDate.val() != null) {
                $("#orderDirectSupplierDateEndDate").css("border-color","#CCCCCC");
            }
        });

        m.search.on("click", function () {
           if(verifySearch()){
            setParamGrid();
            paramGrid = "searchJson=" + m.searchJson.val();
            tableGrid.setting("url", url_left + "/getOrderInformation");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData(null);
           }
        });



        m.barcode.on("blur", function () {
            var inputVal = $(this).val();
            if ($.trim(inputVal) == "") {
                m.itemName.text("");
                m.itemName.text('');
            } else {
                // var reg = /^[0-9]*$/;
                // if (!reg.test(inputVal)) {
                //     m.itemName.text('');
                //     _common.prompt("Item Barcode must be a number!", 5, "error"); // Item Barcode必须是纯数字
                //     return false;
                // }
                getItemInfoByItem1(inputVal, function (res) {
                    if (res.success) {
                        m.itemName.text($.trim(res.data.itemName));
                        return true;
                    } else {
                        m.itemName.text("Item Name is not obtained!"); // 未取得商品名称
                        _common.prompt(res.message, 5, "error");
                        return false;
                    }
                });
            }
        });

        m.reset.on("click", function () {
            $("#regionRemove").click();
            m.orderId.val("");
            // m.vendorId.val("");
            m.orderDirectSupplierDateEndDate.val("");
            m.orderDirectSupplierDateStartDate.val("");
            // m.order_Date.val("");
            // m.delivery_Date.val("");
            m.orderMethod.val("");
            m.orderStatus.val("");
            selectTrTemp = null;
            m.searchJson.val('');
            _common.clearTable();
        });
    }

    var setParamGrid = function () {
        var _startDate = fmtStringDate(m.orderDirectSupplierDateStartDate.val()) || null;
        var _endDate = fmtStringDate(m.orderDirectSupplierDateEndDate.val()) || null;
        var searchJsonStr = {
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            orderId: m.orderId.val().trim(),
            orderMethod: m.orderMethod.val().trim(),
            reviewStatus:m.orderStatus.val().trim(),
            vendorId:$('#vendorId').attr('k'),
            orderDifferentiate: '0', // 订货区分(0:供货商 1：物流)
            orderDirectSupplierDateStartDate: _startDate,
            orderDirectSupplierDateEndDate: _endDate,
            optionTime: $("input[name='order_type']:checked").val(),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 按钮权限验证
    var isButPermission = function () {
        var printButPm = $("#printButPm").val();
        if (Number(printButPm) != 1) {
            $("#print").remove();
        }
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
    }

    // 为0返回'0'
    function getString0(tdObj, value) {
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(Number(_value).toFixed(3));
    }

    // 去除小数位
    function getIntNumber(tdObj, value) {
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(Number(_value).toFixed(0));
    }

    //初始化下拉选择框
    // delete by ty 20200403 start
    /*function initOptionList() {
        $.myAjaxs({
            url: "od0010ReferMa1010/selectStore",
            async: true,
            cache: false,
            type: "get",
            data: "",
            dataType: "json",
            success: function (res) {
                //  var data=null;
                var htmlStr = '<option value="">-- All/Please Select --</option>';
                if (res.valueOf() != null) {
                    $.each(res, function (ix, node) {
                        htmlStr += '<option value="' + node.storeCd + '">' + node.storeCd + '</option>'
                    });
                    m.store_Cd.html(htmlStr);
                    // getStoreName();
                } else {
                    m.store_Cd.html(htmlStr);
                }

            },
            complete: _common.myAjaxComplete

        })
    }*/
    /*function getStoreName() {
        $("#storeCd").find("option:selected").change(function () {
            $.myAjaxs({
                url: "od0010ReferMa1010/selectStore",
                async: true,
                cache: false,
                type: "get",
                data: {},
                dataType: "json",
                success: function (res) {
                    var htmlStr1 = '<input value="" placeholder="All"/>';
                    $.each(res, function (ix, node) {
                        if (node.storeCd === $("#storeCd").val()) {
                            htmlStr1 += '<input  value=" ' + node.storeName + '"/>' + node.storeName +
                                m.store_Name.text(node.storeCd);
                            m.store_Name.html(htmlStr1)
                        }

                    })
                }
            })
        })
    }*/
    // delete by ty end

    var initTable1 = function () {
        tableGrid = $("#zgGridTable").zgGrid({
            title: "Query Result",//查询结果
            param: paramGrid,
            localSort: true,
            rownumbers: true,
            rownumWidth:35,
            colNames: ["PO No.","Order Date","Expected Delivery Date","Store No.","Store Name","Vendor Code","Vendor Name","Order Method","PO Status"],
            colModel: [
                {name: "orderId", type: "text", text: "right", width: "150", ishide: false, css: ""},
                {name: "orderDate", type: "text", text: "center", width: "130", ishide:false, css: "",getCustomValue:dateFmt},
                {name: "deliveryDate", type: "text", text: "center", width: "150", ishide:false, css: "",getCustomValue:dateFmt},
                {name: "storeCd", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "storeName", type: "text", text: "left", width: "150", ishide: false, css: ""},
                {name: "vendorId", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "vendorName", type: "text", text: "left", width: "180", ishide: false, css: ""},
                {name: "orderMethod", type: "text", text: "left", width: "180", ishide: false, css: "",getCustomValue:getMenthod},
                {name: "reviewStatus", type: "text", text: "left", width: "180", ishide: false, css: "",getCustomValue:getReviewStatus},

            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                // {
                //     butType: "update",
                //     butId: "updateOrderDetails",
                //     butText: "Modify",
                //     butSize: ""
                // },
                {
                    butType: "view",
                    butId: "viewOrderDetails",
                    butText: "View",
                    butSize: ""
                },
                // {
                //     butType:"custom",
                //     butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                // },
                {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"},
                {butType:"custom",butHtml:"<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"}
            ]
        });
        //审核记录
        approvalRecordsTableGrid = $("#approvalRecordsTable").zgGrid({
            title:"Approval Records",
            param:approvalRecordsParamGrid,
            localSort:true,
            colNames:["Approval Type","Result","User Code","User Name","Date Time","Comments"],
            colModel:[
                {name:"typeName",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"result",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"userCode",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"userName",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"dateTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"comments",type:"text",text:"center",width:"200",ishide:false,css:""},
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            sidx:"",//排序字段
            sord:"",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            }
        });
    }

    var dateFmt = function (tdObj, value) {
        if (value != null && value.trim() != '' && value.length == 8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

    //拼接参数时候使用 格式化日期
    function fmtStringDate(date) {
        var res = "";
        date = date.replace(/\//g, "");
        res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
        return res;
    }
    var getReviewStatus = function(tdObj, value){
        let str = '';
        switch (value) {
            case '1':
                str = 'Pending';
                break;
            case '5':
                str='Rejected';
                break;
            case '6':
                str='Withdrawn';
                break;
            case '7':
                str = "Expired";
                break;
            case '10':
                str='Approved';
                break;
            case "15":
                str = "Receiving Pending";
                break;
            case '20':
                str='Received';
                break;
            case '30':
                str='Paid';
                break;
            default:
                value = "";
        }
        return $(tdObj).text(str);
    }
    var getMenthod = function(tdObj, value){
        let str = '';
        switch (value) {
            case '10':
                str = 'DC Order';
                break;
            case '30':
                str='DC Allocation Order';
                break;
            case '20':
                str = 'Direct Store Purchase Order';
                break;
            case '40':
                str='Direct Store Purchase Allocation';
                break;
        }
        return $(tdObj).text(str);
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
        return res;
    }

    function judgeNaN (value) {
        return value !== value;
    }

    var verifySearch = function() {
        let _StartDate = null;
        if(!$("#orderDirectSupplierDateStartDate").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#orderDirectSupplierDateStartDate").focus();
            return false;
        }else if(_common.judgeValidDate($("#orderDirectSupplierDateStartDate").val())){
            _common.prompt("Please enter a valid date!",3,"info");
            $("#orderDirectSupplierDateStartDate").css("border-color","red");
            $("#orderDirectSupplierDateStartDate").focus();
            return false;
        }else {
            $("#orderDirectSupplierDateStartDate").css("border-color","#CCCCCC");
        }
        let _EndDate = null;
        if(!$("#orderDirectSupplierDateEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#orderDirectSupplierDateEndDate").focus();
            return false;
        }else if(_common.judgeValidDate($("#orderDirectSupplierDateEndDate").val())){
            _common.prompt("Please enter a valid date!",3,"info");
            $("#orderDirectSupplierDateEndDate").css("border-color","red");
            $("#orderDirectSupplierDateEndDate").focus();
            return false;
        }else {
            $("#orderDirectSupplierDateEndDate").css("border-color","#CCCCCC");
        }
        if(_StartDate>_EndDate){
            $("#orderDirectSupplierDateEndDate").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#orderDirectSupplierDateEndDate").focus();
            return false;
        }
        return true;
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
                    if(optionValue=='40'||optionValue=='20'){
                        selectObj.append(new Option(optionText, optionValue));
                    }
                }
            },
            error : function(e){
                _common.prompt(title+" Failed to load data!",5,"error");
            }
        });
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('orderDirectSupplier');
_start.load(function (_common) {
    _index.init(_common);
});