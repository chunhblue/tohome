require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('orderDetails', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        approvalRecordsParamGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        a_vendor = null,
        common=null;
    var m = {
        order_vendor:null,
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        tableGrid : null,
        search:null,
        reset:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        od_start_date: null,
        od_end_date: null,
        clear_od_date:null,
        orderId:null, //单据编号
        orderSts:null, //单据状态
        reviewSts:null, //审核状态
        store:null, //输入店铺
        search_store_input:null, //查询店铺
        typeId:null//审核类型id
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left=_common.config.surl+"/orderDetails";
        getThousands=_common.getThousands;
        toThousands=_common.toThousands;
        reThousands=_common.reThousands;
        //初始化页面按钮
        but_event();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        getSelectValue();
        //加载模糊下拉
        initAutoMatic();

        initPageBytype("1");
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        m.search.click();
    }

    // 请求加载下拉列表
    function getSelectValue(){
        // 加载select
        // initSelectOptions("Voucher Status","orderSts", "00150");
        // initSectionVendor("order_vendor");
    }

    function initSectionVendor(selected) {
        $.myAjaxs({
            url:systemPath+"/ma2000/getVendorList",
            async:true,
            cache:false,
            type :"get",
            success:function (result) {
                var selectObj=$("#"+selected);
                for (let i = 0; i <result.length; i++) {
                    var vendorId = result[i].vendorId;
                    var vendorName1 = result[i].vendorId +' '+ result[i].vendorName;
                    if (vendorName1.length>20) {
                        vendorName1=vendorName1.substring(0,20)+'...';
                    }
                    // selectObj.append(new Option(vendorId+" "+ vendorName1,vendorId));
                    let html = '<option class="vendors" title="'+result[i].vendorId +' '+ result[i].vendorName+'" value="'+vendorId+'">'+vendorName1+'</option>';
                    selectObj.append(html);
                }
            }
        })
    }
    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:true,
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
                    selectObj.append(new Option(optionValue+" "+optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+"Failed to load data!",5,"error");
            }
        });
    }

    var table_event = function(){
        //进入添加
        $("#addOrderDetails").on("click", function (e) {
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
            top.location = url_left+"/add";
        });

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
            var cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId,orderType,orderSts,vendorId,vendorName");
            //获取数据审核状态
            _common.getRecordStatus(cols["orderId"],m.typeId.val(),function (result) {
                if(result.success||result.data=='10'){//审核通过只要没有上传可修改
                    var orderDate = '';
                    if(cols["orderDate"]!=null&&cols["orderDate"]!=''){
                        orderDate = subfmtDate(cols["orderDate"]);
                    }
                    //验证订货日是否为当前业务日
                    _common.checkBusinessDate(orderDate,cols["orderId"],function (result) {
                        if(result.success){
                            var param = "use=1&orderId="+cols["orderId"];
                            top.location = url_left+"/edit?"+param;
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

        $("#viewOrderDetails").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId,orderType,orderSts,vendorId,vendorName");
            var orderDate = '';
            if(cols["orderDate"]!=null&&cols["orderDate"]!=''){
                orderDate = subfmtDate(cols["orderDate"]);
            }
            var param = "use=0&orderId="+cols["orderId"];
            top.location = url_left+"/edit?"+param;
        });

        $("#printOrderDetails").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });

        $("#outExcel").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });

        var table_ck_all = $("#table_ck_all");
        table_ck_all.on("change",function(event){
            event.preventDefault();
            var ck = $(this).prop("checked");
            $("#zgGridTtable_tbody").find("input[type='checkbox']").prop("checked",ck);
        });
        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"orderId");
            approvalRecordsParamGrid = "id="+cols["orderId"]+
                // "&userCode="+$("#userCode").val()+
                // "&startDate="+subfmtDate($("#subStartDate").val())+
                // "&endDate="+subfmtDate($("#subEndDate").val())+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",systemPath+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });
    }


    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    //画面按钮点击事件
    var but_event = function(){
        //开始日
        m.od_start_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#od_end_date").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
            } else {
                $("#od_end_date").datetimepicker('setStartDate', null);
            }
        });
        //结束日
        m.od_end_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#od_start_date").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
            } else {
                $("#od_start_date").datetimepicker('setEndDate', new Date());
            }

        });
        //清空日期
        m.clear_od_date.on("click",function(){
            m.od_start_date.val("");
            m.od_end_date.val("");
        });
        m.reset.on("click",function () {
            $("#regionRemove").click();
            m.od_start_date.val("");
            m.od_end_date.val("");
            m.orderSts.val("");
            m.orderId.val("");
            m.reviewSts.val("");
            $.myAutomatic.cleanSelectObj(a_vendor);
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                var regionCd = $("#aRegion").attr("k");
                var cityCd = $("#aCity").attr("k");
                var districtCd = $("#aDistrict").attr("k");
                var storeCd = $("#aStore").attr("k");
                var vendorId=$("#a_vendor").attr("k");
                var orderStartDate = m.od_start_date.val();
                var orderEndDate = m.od_end_date.val();
                var orderId = m.orderId.val();
                var reviewSts = m.reviewSts.val();
                paramGrid = "regionCd="+regionCd+"&cityCd="+cityCd+"&districtCd="+districtCd+"&storeCd="+storeCd;
                if(orderId!=null&&orderId!=''){
                    paramGrid += "&orderId="+orderId;
                }
                if (vendorId != null && vendorId != '') {
                    paramGrid +="&vendorId="+vendorId;
                }
                if(orderStartDate!=null&&orderStartDate!=''){
                    orderStartDate = subfmtDate(orderStartDate);
                    paramGrid += "&orderStartDate="+orderStartDate;
                }
                if(orderEndDate!=null&&orderEndDate!=''){
                    orderEndDate = subfmtDate(orderEndDate);
                    paramGrid += "&orderEndDate="+orderEndDate;
                }
                if(reviewSts!=null&&reviewSts!=''){
                    paramGrid += "&reviewStatus="+reviewSts;
                }
                tableGrid.setting("url",url_left+"/getList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });
        //验证检索项是否合法
        var verifySearch = function(){
            // var storeCd = $("#a_store").attr("k");
            // if(storeCd==null||storeCd==""){
            //     _common.prompt("Please select a store first!",5,"error"); // 请输入店铺编号
            //     $("#a_store").focus();
            //     return false;
            // }
            var _startNewDate = new Date(fmtDate($("#od_start_date").val())).getTime();
            var _endNewDate = new Date(fmtDate($("#od_end_date").val())).getTime();
            var difValue = parseInt(Math.abs((_endNewDate-_startNewDate)/(1000*3600*24)));
            if(difValue>62){
                $("#od_end_date").focus();
                _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
            return false;
            }
            return true;
        }
        //点击查找供应商编号按钮事件
        m.search_store_input.on("click",function(){
            var inputVal = m.store.val();
            m.vendor_code.text("");
            if($.trim(inputVal)==""){
                _common.prompt("Vendor code cannot be empty!",5,"error"); // 供应商编号不能为空
                m.vendor_code.attr("status","2").focus();
                return false;
            }
        });
    }

    //初始化下拉
    var initAutoMatic = function () {
        //供应商
        a_vendor = $("#a_vendor").myAutomatic({
            url: systemPath + "/ma2000/getVendorList",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
            },
            selectEleClick: function (thisObj) {
            }
        });
    }

    //根据商品条码取得该商品的详细对象，
    var getItemInfoByItem1 = function(item1,fun){
        $.myAjaxs({
            url:url_left+"/getiteminfo",
            async:true,
            cache:false,
            type :"get",
            data :"itemCode="+item1,
            dataType:"json",
            success:fun,
            complete:_common.myAjaxComplete
        });
    }

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Order Entry(To Supplier) Order List",
            param:paramGrid,
            colNames:["Vendor Id","Vendor Name","Order Type","Order Date","PO No.","Supplier No.","Supplier Name","Order Quantity","Order Amount","Received Amount","PO Status","Created By","Remarks"],
            colModel:[
                {name:"vendorId",type:"text",text:"right",width:"100",ishide:true,css:""},
                {name:"vendorName",type:"text",text:"left",width:"100",ishide:true,css:""},
                {name:"orderType",type:"text",text:"left",width:"100",ishide:true,css:""},
                {name:"orderDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"orderId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"vendorId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"vendorName",type:"text",text:"left",width:"180",ishide:false,css:""},
                {name:"orderQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
                /*隐藏订货总金额*/
                {name:"orderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                /*隐藏收货总金额*/
                {name:"receiveAmt",type:"text",text:"right",width:"130",ishide:true,getCustomValue:getThousands},

                {name:"reviewStatus",type:"text",text:"left",width:"100",ishide:false,getCustomValue:getReviewStatus},
                {name:"createUserName",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:null},
                {name:"orderRemark",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null}
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            sidx:"o.create_ymd desc,o.create_hms",//排序字段
            sord:"desc",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                {
                    butType: "add",
                    butId: "addOrderDetails",
                    butText: "Add",
                    butSize: ""
                },
                {
                    butType: "update",
                    butId: "updateOrderDetails",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType: "view",
                    butId: "viewOrderDetails",
                    butText: "View",
                    butSize: ""
                },{
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                }
            ],
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

    // 按钮权限验证
    var isButPermission = function () {
        var orderVendorButView = $("#orderVendorButView").val();
        if (Number(orderVendorButView) != 1) {
            $("#viewOrderDetails").remove();
        }
        var orderVendorButAdd = $("#orderVendorButAdd").val();
        if (Number(orderVendorButAdd) != 1) {
            $("#addOrderDetails").remove();
        }
        var orderVendorButEdit = $("#orderVendorButEdit").val();
        if (Number(orderVendorButEdit) != 1) {
            $("#updateOrderDetails").remove();
        }
        var ApprovalRecords = $("#ApprovalRecords").val();
        if (Number(ApprovalRecords) != 1) {
            $("#approvalRecords").remove();
        }
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
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
            case '20':
                str='Received';
                break;
            case '30':
                str='Paid';
                break;
        }
        return $(tdObj).text(str);
    }

    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    //格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('orderDetails');
_start.load(function (_common) {
    _index.init(_common);
});
