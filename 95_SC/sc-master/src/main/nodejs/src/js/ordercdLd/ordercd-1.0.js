require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");

define('orderCd', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        approvalRecordsParamGrid = null,
        url_root = '';
    const KEY = 'ORDER_QUERY_TO_DC';
    const  ORDERMETHOD='Direct Order';
    var m = {
        barcode:null,
        reset:null,
        str: null,
        search: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        store_Cd: null,
        toKen: null,
        use: null,
        error_pcode: null,
        dc:null,
        orderStatus:null,
        orderMethod:null,
        orderId: null,
        main_box: null,
        identity: null,
        searchJson: null,
        order_Date:null,
        orderSts:null,
        delivery_Date:null,
        orderDirectSupplierDateStartDate: null,
        orderDirectSupplierDateEndDate: null,
        typeId:null,//审核类型id
    }
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left=_common.config.surl+ "/cdOrder";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        but_event();
        //初始化选择框
        // initOptionList();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        initTable2();
        //权限验证
        isButPermission();
        table_event();
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
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamGrid();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/getOrderCdInfor");
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
        obj.page=tableGrid.getting("page");
        obj.orderDirectSupplierDateStartDate=m.orderDirectSupplierDateStartDate.val();
        obj.orderDirectSupplierDateEndDate=m.orderDirectSupplierDateEndDate.val();
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    var initSearchArea = function(){
        // initSelectOptions("Order Status", "orderStatus", "00430");
        initSelectOptions("Order Method", "orderMethod", "00065");
    }

    var setParamGrid = function () {
        //拼接参数
        var _startDate = fmtStringDate(m.orderDirectSupplierDateStartDate.val()) || null;
        var _endDate = fmtStringDate(m.orderDirectSupplierDateEndDate.val()) || null;
        var searchJsonStr = {
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            orderId: m.orderId.val().trim(),
            reviewStatus:m.orderStatus.val().trim(),
            orderMethod:m.orderMethod.val().trim(),
            // dc: $("#dc").val(),
            orderDifferentiate: '1', // 订货区分(0:供货商1：物流)
            orderDirectSupplierDateStartDate: _startDate,
            orderDirectSupplierDateEndDate: _endDate,
            optionTime: $("input[name='order_type']:checked").val(),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    var table_event=function(){
        //进入打印页面
        $("#print").on("click",function () {
            if(verifySearch()) {
                var _startNewDate = new Date(fmtDate($("#orderDirectSupplierDateStartDate").val())).getTime();
                var _endNewDate = new Date(fmtDate($("#orderDirectSupplierDateEndDate").val())).getTime();
                var difValue = parseInt(Math.abs((_endNewDate - _startNewDate) / (1000 * 3600 * 24)));
                if (difValue > 62) {
                    _common.prompt("Query Period cannot exceed 62 days!", 5, "error"); // 日期期间取值范围不能大于62天
                    $("#orderDirectSupplierDateEndDate").focus();
                    return false;
                }
                //拼接参数
                var _startDate = m.orderDirectSupplierDateStartDate.val() || null;
                var _endDate = m.orderDirectSupplierDateEndDate.val() || null;
                var searchJsonStr = {
                    regionCd:$("#aRegion").attr("k"),
                    cityCd:$("#aCity").attr("k"),
                    districtCd:$("#aDistrict").attr("k"),
                    storeCd:$("#aStore").attr("k"),
                    orderId: m.orderId.val(),
                    reviewStatus:m.orderStatus.val(),
                    orderMethod:m.orderMethod.val(),
                    // dc: $("#dc").val(),
                    orderDifferentiate: '1', // 订货区分(0:供货商1：物流)
                    orderDirectSupplierDateStartDate: _startDate,
                    orderDirectSupplierDateEndDate: _endDate,
                    optionTime: $("input[name='order_type']:checked").val(),
                };
                m.searchJson.val(JSON.stringify(searchJsonStr));
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/print?" + paramGrid;
                window.open(encodeURI(url), "总部CD订单查询打印", 'top=10,left=300,width=2000px,toolbar=no,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no,center:yes');
            }
        });

        $("#view").on("click",function () {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                var cols = tableGrid.getSelectColValue(selectTrTemp,"orderId,orderDate,storeCd,storeName,vendorId,warehouseName,orderMethod,orderMethod1");
                var paramGrid="storeCd="+cols['storeCd']+"&storeName="+cols['storeName']+"&orderId="+ cols['orderId']+"&orderDate="+cols['orderDate'] +"&vendorId="+cols['vendorId']+"&warehouseName="+cols['warehouseName']
                saveParamToSession();
                // if('Store Order'==cols['orderMethod'].trim()){
                if(cols['orderId'].length < 13){
                    top.location = url_left+"/cdOrderView?"+paramGrid;
                }else {
                    let param = "use=0&orderId="+cols["orderId"].trim();
                    top.location = url_root+"/urgentOrderDc/edit?"+param;
                }
            }
        });

        $("#update").on("click", function (e) {
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
            var cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId");
            //获取数据审核状态
            getRecordStatus(cols["orderId"],m.typeId.val(),function (result) {
                if(result.success){//审核没通过可修改
                    var orderDate = '';
                    if(cols["orderDate"]!=null&&cols["orderDate"]!=''){
                        orderDate = subfmtDate(cols["orderDate"]);
                    }
                    //验证订货日是否为当前业务日
                    _common.checkBusinessDate(orderDate,cols["orderId"],function (result) {
                        if(result.success){
                            saveParamToSession();
                            var param = "use=1&orderId="+cols["orderId"];
                            top.location = url_root+"/urgentOrderDc/edit?"+param;
                        }else{//禁用
                            _common.prompt(result.message,5,"error");
                        }
                    })
                }else if(result.data=='20'){
                    _common.prompt("Selected PO is during Returning process and cannot modify!",5,"error");
                }else{
                    _common.prompt(result.message,5,"error");
                }
            });
        });

        $("#export").on("click",function () {
            if(verifySearch()) {
                var _startNewDate = new Date(fmtDate($("#orderDirectSupplierDateStartDate").val())).getTime();
                var _endNewDate = new Date(fmtDate($("#orderDirectSupplierDateEndDate").val())).getTime();
                var difValue = parseInt(Math.abs((_endNewDate - _startNewDate) / (1000 * 3600 * 24)));
                if (difValue > 62) {
                    _common.prompt("Query Period cannot exceed 62 days!", 5, "error"); // 日期期间取值范围不能大于62天
                    $("#orderDirectSupplierDateEndDate").focus();
                    return false;
                }
                setParamGrid();
                paramGrid = "searchJson=" + m.searchJson.val();
                var url = url_left + "/export?" + paramGrid;
                window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
            }
        });

        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"orderId");
            approvalRecordsParamGrid = "id="+cols["orderId"]+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });
    };

    //加载下拉选择框
    //画面按钮点击事件
    var but_event = function () {
        $("#orderDirectSupplierDateStartDate").blur(function () {
            if (m.orderDirectSupplierDateStartDate.val() != null || m.orderDirectSupplierDateStartDate.val() != null) {
                $("#orderDirectSupplierDateStartDate").css("border-color","#CCCCCC");
            }
        })
        $("#orderDirectSupplierDateEndDate").blur(function () {
            if (m.orderDirectSupplierDateEndDate.val() != null || m.orderDirectSupplierDateEndDate.val() != null) {
                $("#orderDirectSupplierDateEndDate").css("border-color","#CCCCCC");
            }
        })

        m.search.on("click",function () {
            if(verifySearch()) {
                var _startNewDate = new Date(fmtDate($("#orderDirectSupplierDateStartDate").val())).getTime();
                var _endNewDate = new Date(fmtDate($("#orderDirectSupplierDateEndDate").val())).getTime();
                var difValue = parseInt(Math.abs((_endNewDate - _startNewDate) / (1000 * 3600 * 24)));
                if (difValue > 62) {
                    _common.prompt("Query Period cannot exceed 62 days!", 5, "error"); // 日期期间取值范围不能大于62天
                    $("#orderDirectSupplierDateEndDate").focus();
                    return false;
                }
                setParamGrid();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/getOrderCdInfor");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });


        m.barcode.on("blur",function(){
            var inputVal = $(this).val();
            if($.trim(inputVal)==""){
                m.itemName.text("");
                m.itemName.text('');
            }else{
                // var reg = /^[0-9]*$/;
                // if(!reg.test(inputVal)){
                //     m.itemName.text('');
                //     _common.prompt("Item Barcode must be a number!",5,"error"); // Item Barcode必须是纯数字
                //     return false;
                // }
                getItemInfoByItem1(inputVal,function(res){
                    if(res.success){
                        m.itemName.text($.trim(res.data.itemName));
                        return true;
                    }else{
                        m.itemName.text("Item Name is not obtained!"); // 未取得商品名称
                        _common.prompt(res.message,5,"error");
                        return false;
                    }
                });
            }
        });


        m.reset.on("click", function () {
            $("#regionRemove").click();
            m.orderId.val("");
            m.orderStatus.val("");
            // m.dc.val("");
            m.orderDirectSupplierDateEndDate.val("");
            m.orderDirectSupplierDateStartDate.val("");
            // m.order_Date.val("");
            // m.delivery_Date.val("");
            m.orderMethod.val("");
            selectTrTemp = null;
            m.searchJson.val('');
            _common.clearTable();
        });
    }

    /*//初始化下拉选择框
    function initOptionList() {
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

    /*// 去除小数位
    function getIntNumber(tdObj, value) {
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(Number(_value).toFixed(0));
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


    /*var initTable1 = function(){
       // var data = '[{"sc1":"1.00","sc2":"1.00","sc3":"1.00","sc4":"1.00","sc5":"1.00","sc6":"1.00","sc7":"1.00","sc8":"1.00","sc9":"1.00","sc10":"1.00"}]';
        tableGrid = $("#zgGridTable").zgGrid({
            title:"Query Result",//查询结果
            param:paramGrid,
            localSort: true,
            // serial_no 序号  article_id  商品货号   barcode 商品条码 product_name 商品名字   order_unit 订货单位 order_qty 订购数量 order_nocharge_qty 搭赠数量
            colNames:["No.","Item Code","Barcode ","Item Name","UOM","Order Quantity",
                "Free Order Quantity","Receiving differences"],
            colModel:[
                {name:"serialNo",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"articleId",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"barcode",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"productName",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"orderUnit",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"orderQty",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"orderNochargeQty",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"receivingDifferences",type:"text",text:"center",width:"130",ishide:false,css:""},
            ],//列内容
           // traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            }
        });
    }*/


    // var initTable3 = function () {
    //     tableGrid = $("#zgGridTable").zgGrid({
    //         title: "Query Result",//查询结果
    //         param: paramGrid,
    //         localSort: true,
    //         rownumbers: true,
    //         rownumWidth:35,
    //         colNames: ["PO No.","Store No.","Store Name","DC No.","DC Name", "Item Code", "Barcode ", "Item Name", "UOM", "Min. Order Quantity", "Order Quantity",
    //            "order Date","Free Order Quantity", "Receive Quantity", "Receiving Differences","PO Status"],
    //         colModel: [
    //             {name: "orderId", type: "text", text: "right", width: "130", ishide: false, css: ""},
    //             {name: "storeCd", type: "text", text: "left", width: "130", ishide: false, css: ""},
    //             {name: "storeName", type: "text", text: "left", width: "130", ishide: false, css: ""},
    //             {name: "vendorId", type: "text", text: "left", width: "130", ishide: false, css: ""},
    //             {name: "vendorName", type: "text", text: "left", width: "130", ishide: false, css: ""},
    //             {name: "articleId", type: "text", text: "right", width: "130", ishide: true, css: ""},
    //             {name: "barcode", type: "text", text: "right", width: "130", ishide: true, css: ""},
    //             {name: "productName", type: "text", text: "left", width: "130", ishide: true, css: ""},
    //             {name: "orderUnit",type: "text",text: "left",width: "130",ishide: true,css: "",getCustomValue: null},
    //             {name: "minOrderQty",type: "text",text: "right",width: "160",ishide: true,css: "",getCustomValue: getThousands},
    //             {name: "orderQty",type: "text",text: "right",width: "130",ishide: true,css: "",getCustomValue: getThousands},
    //             {name: "orderDate",type: "text",text: "right",width: "130",ishide: true,css: ""},
    //             {name: "orderNochargeQty",type: "text",text: "right",width: "160",ishide: true,css: "",getCustomValue: getThousands},
    //             {name: "receiveQty",type: "text",text: "right",width: "130",ishide: true,css: "",getCustomValue: getThousands},
    //             {name: "receivingDifferences",type: "number",text: "right",width: "200",ishide: true,css: "",getCustomValue: getThousands},
    //             {name: "reviewStatus",type: "text",text: "right",width: "200",ishide:false,css: "",getCustomValue:getReviewStatus},
    //         ],//列内容
    //         // traverseData:data,
    //         width: "max",//宽度自动
    //         page: 1,//当前页
    //         rowPerPage: 10,//每页数据量
    //         isPage: true,//是否需要分页
    //         // sidx:"bm.bm_type,order.bm_code",//排序字段
    //         // sord:"asc",//升降序
    //         isCheckbox: false,
    //         loadEachBeforeEvent: function (trObj) {
    //             tempTrObjValue = {};
    //             return trObj;
    //         },
    //         ajaxSuccess: function (resData) {
    //             return resData;
    //         },
    //         loadCompleteEvent: function (self) {
    //             selectTrTemp = null;//清空选择的行
    //             return self;
    //         },
    //         eachTrClick: function (trObj, tdObj) {//正常左侧点击
    //             selectTrTemp = trObj;
    //         },
    //         buttonGroup:[
    //             {
    //                 butType: "view",
    //                 butId: "view",
    //                 butText: "View",
    //                 butSize: ""
    //             },
    //             {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
    //             // {butType:"custom",butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-view'></span> View</button>"}
    //         ]
    //     });
    // }
    var initTable2 = function () {
        tableGrid = $("#zgGridTable").zgGrid({
            title: "Query Result",//查询结果
            param: paramGrid,
            localSort: true,
            rownumbers: true,
            rownumWidth:35,
            colNames: ["PO No.","Order Date","Store No.","Store Name","DC No.","DC Name","PO Status","Order Method","Order Method"],
            colModel: [
                {name: "orderId", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "orderDate", type: "text", text: "center", width: "130", ishide: false, css: "",getCustomValue:dateFmt},
                {name: "storeCd", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "storeName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "vendorId", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "warehouseName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "reviewStatus",type: "text",text: "left",width: "200",ishide:false,css: "",getCustomValue:getReviewStatus},
                {name: "orderMethod",type: "text",text: "left",width: "200",ishide:false,css: "",getCustomValue:getMenthod},
                {name: "orderMethod1",type: "text",text: "left",width: "200",ishide:true,css: ""},
            ],//列内容
            // traverseData:data,
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
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
                $("#approvalRecords").prop("disabled",true);
                $("#update").prop("disabled",true);
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
                let cols = tableGrid.getSelectColValue(selectTrTemp,"orderMethod,orderMethod1");
                //判断是否是dc紧急订货
                // if('Store Order'==cols['orderMethod'].trim()){
                if('40'===cols['orderMethod1']){
                    $("#approvalRecords").prop("disabled",false);
                    $("#update").prop("disabled",false);
                }else{
                    $("#approvalRecords").prop("disabled",true);
                    $("#update").prop("disabled",true);
                }
            },
            buttonGroup:[
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                },
                {
                    butType: "update",
                    butId: "update",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                },
                {butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
                // {butType:"custom",butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-view'></span> View</button>"}
            ]
        });

        //审核记录
        approvalRecordsTableGrid = $("#approvalRecordsTable").zgGrid({
            title:"Approval Records",
            param:approvalRecordsParamGrid,
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
    var getMenthod = function(tdObj, value){
        let str = '';
        switch (value) {
            case '40':
                str = 'DC Store Order';
                break;
            case '30':
                str='DC Allocation Order';
                break;
            case '20':
                str = 'Direct Store Purchase Order';
                break;
            case '10':
                str='Direct Store Purchase Allocation';
                break;
        }
        return $(tdObj).text(str);
    }
    var dateFmt = function (tdObj, value) {
        if (value != null && value.trim() != '' && value.length == 8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
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
                    if(optionValue=='30'||optionValue=='40'){
                        selectObj.append(new Option(optionText, optionValue));
                    }
                }
            },
            error : function(e){
                _common.prompt(title+" Failed to load data!",5,"error");
            }
        });
    }
    //拼接参数时候使用 格式化日期
    function fmtStringDate(date) {
        var res = "";
        date = date.replace(/\//g, "");
        res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
        return res;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
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
        }else{
            _StartDate = new Date(fmtDate($("#orderDirectSupplierDateStartDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#orderDirectSupplierDateStartDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#orderDirectSupplierDateEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#orderDirectSupplierDateEndDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#orderDirectSupplierDateEndDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#orderDirectSupplierDateEndDate").focus();
                return false;
            }
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
    };

    /**
     * 获取审核状态
     * @param recordId 数据id
     * @param typeId 类型id
     * @param reviewId 流程id
     * @param callback
     */
    var getRecordStatus = function (recordId,typeId,callback,reviewId) {
        if(!reviewId){
            reviewId = 0;
        }
        $.myAjaxs({
            url:url_left+"/getRecordStatus",
            async:true,
            cache:false,
            type :"get",
            data :{
                recordId : recordId,
                typeId : typeId,
                reviewId : reviewId
            },
            dataType:"json",
            success:function(result){
                //回调
                callback(result);
            },
            error : function(e){
                prompt("The request failed, Please try again!",5,"error");// 请求失败
            }
        });
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('orderCd');
_start.load(function (_common) {
    _index.init(_common);
});