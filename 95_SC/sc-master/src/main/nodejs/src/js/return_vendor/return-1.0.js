require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax=require("myAjax");
define('returnVendor', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        approvalRecordsParamGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        Sts='1',
        tempTrObjValue = {},//临时行数据存储
        tempTrOrderId = null,// 选中的单据编号
        tempTrOrderDate = null,//选择的退货时间
        common=null;
    const KEY = 'ITEM_RETURN_REQUEST_ENTRY_TO_SUPPLIER';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        searchJson:null,
        // 查询部分
        rt_date:null,
        order_id:null,
        org_order_id:null,
        vendor_id:null,
        vendor_name:null,
        reviewStatus:null,
        search:null,
        reset:null,
        reType: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        typeId:null,//审核类型id
        rt_start_date:null,
        rt_end_date:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/returnVendor";
        url_root=_common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        but_event();
        initTable1();//列表初始化
        //加载下拉
        getSelectValue();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        setButton();
        m.org_order_id.prop("disabled",true);
        // 初始化检索日期
        _common.initDate(m.rt_start_date,m.rt_end_date);
     //   $("#org_order_id").attr("disabled",true);
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

        m.rt_start_date.val(obj.orderStartDate);
        m.rt_end_date.val(obj.orderEndDate);
        m.order_id.val(obj.orderId);
        m.org_order_id.val(obj.orgOrderId);
        m.vendor_id.val(obj.vendorId);
        m.vendor_name.val(obj.vendorName);
        m.reviewStatus.val(obj.reviewStatus);
        $("#reType").val(obj.returnType);
        isDisabled();
        // 设置组织架构回显
        _common.setAutomaticVal(obj);
        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/query");
        tableGrid.setting("param", paramGrid);
        tableGrid.setting("page", obj.page);
        tableGrid.loadData();
    };
    // //设置 是否禁用
    var setButton=function () {
        $("#reType").on("change",function () {
            isDisabled();
        })
    };

    var isDisabled = function () {
        var reTypeValue = $("#reType").val();
        if (reTypeValue=='10'){// 直接退货
            m.org_order_id.val("");
            m.org_order_id.prop("disabled",true);
        }else if(reTypeValue=='20'){
            m.org_order_id.prop("disabled",false);
        }
    };

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
        obj.orderStartDate=m.rt_start_date.val();
        obj.orderEndDate=m.rt_end_date.val();
        obj.returnType=$("#reType").val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 请求加载下拉列表
    function getSelectValue(){
        // 加载select
        initSelectOptions("Document Status","00450", function (result) {
            let selectObj = $("#reviewStatus");
            selectObj.find("option:not(:first)").remove();
            for (let i = 0; i < result.length; i++) {
                let optionValue = result[i].codeValue;
                // if(optionValue == '20'){
                //     continue;
                // }
                let optionText = result[i].codeName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });
        initSelectOptions("reType","00700", function (result) {
            let selectObj = $("#reType");
          //  selectObj.find("option:not(:first)").remove();
            for (let i = 0; i < result.length; i++) {
                let optionValue = result[i].codeValue;
                let optionText = result[i].codeName;
                selectObj.append(new Option(optionText, optionValue));
            }
        });
    }

    // 初始化下拉列表
    function initSelectOptions(title, code, fun) {
        // 共通请求
        $.myAjaxs({
            url:url_root+"/cm9010/getCode",
            async:false,
            cache:false,
            type :"post",
            data :"codeValue="+code,
            dataType:"json",
            success : fun,
            error : function(e){
                _common.prompt(title+" Failed to load data!",5,"error");
            },
            complete:_common.myAjaxComplete
        });
    }

    //表格按钮事件
    var table_event = function(){
        $("#add").on("click", function (e) {
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
            saveParamToSession();
            top.location = url_left+"/cancelOrderDetail?flag=2";
        });
        $("#modify").on("click", function (e) {
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
            }else{
                //获取数据审核状态
                _common.getRecordStatus(tempTrOrderId,m.typeId.val(),function (result) {
                    if(result.success){
                        //验证订货日是否为当前业务日
                        // _common.checkBusinessDate(tempTrOrderDate,null,function (result) {
                        //    if(result.success){
                                saveParamToSession();
                                top.location = url_left+"/cancelOrderDetail?flag=1&orderId="+ tempTrOrderId ;
                       //     }else{//禁用
                       //         _common.prompt("Selected document is not created within today and cannot be modified!",5,"error");
                       //     }
                       // })
                   }else if(result.data=='20'){
                       _common.prompt("The document has been returned",5,"error");
                   }else{
                       _common.prompt(result.message,5,"error");
                   }
               });
           }
        });

        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            approvalRecordsParamGrid = "id="+tempTrOrderId+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });

        $("#view").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                saveParamToSession();
                top.location = url_left+"/cancelOrderDetail?flag=0&orderId="+ tempTrOrderId;
            }
        });

        $("#print").on("click",function(){
            setParamJson();
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/print?"+ paramGrid+"&returnType="+m.reType.val();
            window.open(encodeURI(url), "excelprint", "width=1400,height=600,scrollbars=yes");
        });
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
        $("#exportBySupplier").on("click",function () {
            setParamJson();
            paramGrid = "searchJson=" + m.searchJson.val();
            var url = url_left + "/exportBySupplier?" + paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
        })
    }
    //拼接检索参数

    var setParamJson = function(){
        // 创建请求字符串
        var	_reviewStatus = null;
        if(m.reviewStatus.val()!=""){
            _reviewStatus = m.reviewStatus.val().trim();
        }
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            // orderDate : fmtIntDate(String(m.rt_date.val())),
            orderStartDate:fmtIntDate(String(m.rt_start_date.val())),
            orderEndDate:fmtIntDate(String(m.rt_end_date.val())),
            orderId :m.order_id.val().trim(),
            orgOrderId :m.org_order_id.val().trim(),
            vendorId :m.vendor_id.val().trim(),
            vendorName :m.vendor_name.val().trim(),
            reviewStatus:_reviewStatus,
            returnType:$("#reType").val(),

        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }




    function judgeNaN (value) {
        return value !== value;
    }

    //验证检索项是否合法
    var verifySearch = function () {

        let _StartDate = null;
        if(!$("#rt_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#rt_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#rt_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#rt_start_date").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#rt_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#rt_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#rt_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#rt_end_date").focus();
                return false;
            }
        }

        if(_StartDate>_EndDate){
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            $("#rt_end_date").focus();
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue >62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#rt_end_date").focus();
            return false;
        }
        // 20210726
        if (m.vendor_id.val()!=null && m.vendor_id.val()!==''){
            $("#exportBySupplier").attr("disabled",false);
        }else {
            $("#exportBySupplier").attr("disabled",true);
        }
        // 20210726
  // if (m.vendor_id.val()!=null && m.vendor_id.val()!==''){
  //           $("#exportBySupplier").removeClass('disabled');
  //       }
  //       if (m.vendor_id.val()==null && m.vendor_id.val()==''){
  //           $("#exportBySupplier").attr('disabled',true);
  //       }
        return true;
    }

    //画面按钮点击事件
    var but_event = function(){
        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/query");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData(null);
            }
        });
   
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.rt_start_date.val("");
            m.rt_end_date.val("");
            m.order_id.val("");
            m.org_order_id.val("");
            m.vendor_id.val("");
            m.vendor_name.val("");
            m.reviewStatus.val("");
            m.reType.find("option:first").prop("selected",true);
            Sts='1';
            $("#org_order_id").attr("disabled",true);
            m.searchJson.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
        $("#vendor_id").on("blur",function () {
           if (m.vendor_id.val()!=null && m.vendor_id.val()!==" "){
               $("#exportBySupplier").attr("disabled",true);
           }
    });
    }

    // 选项选中事件
    var trClick_table1 = function(){
        var cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId,storeCd");
        tempTrOrderId = cols["orderId"];
        tempTrOrderDate =fmtIntDate(cols["orderDate"]);
    }

    //表格初始化-退供应商申请单样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Returning to Supplier Request Note Query",
            param:paramGrid,
            colNames:["Date Returned","Document No.","Original Document No."," Supplier No."," Supplier Name",
                "Store No.","Store Name","Document Status","Return Qty","Return Amount","Return Type"],
            colModel:[
                {name:"orderDate",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"orderId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"orgOrderId",type:"text",text:"right",width:"160",ishide:false,css:""},
                {name:"vendorId",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"vendorName",type:"text",text:"left",width:"220",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"100",ishide:true,css:""},
                {name:"storeName",type:"text",text:"left",width:"180",ishide:false,css:""},
                {name:"reviewStatus",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getStatus},
                {name:"orderQty",type:"text",text:"right",width:"150",ishide:false,css:"",getCustomValue:getThousands},
                {name:"orderAmt",type:"text",text:"right",width:"150",ishide:true,css:"",getCustomValue:getThousands},
                {name:"returnType",type:"text",text:"right",width:"150",ishide:false,css:"",getCustomValue:getSts}
               ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            sidx:"orderDate",//排序字段
            sord:"desc",//升降序
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                if (m.reType.val() == "10") {
                    tableGrid.hideColumn("orgOrderId");
                }else if(m.reType.val() == "20"){
                    tableGrid.showColumn("orgOrderId")
                }

            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj){//正常左侧点击
                selectTrTemp = trObj;
                // 选中选项后的事件
                trClick_table1();
            },
            buttonGroup:[
                {
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""
                },
                {
                    butType: "update",
                    butId: "modify",
                    butText: "Modify",
                    butSize: ""
                },
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                },
                {
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='exportBySupplier' type='button' class='btn btn-info btn-sm'  disabled='disabled'><span class='glyphicon glyphicon-export'></span> Export By Supplier</button>"
                }
            ],

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
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                if (m.reType.val() == "10") {
                    tableGrid.hideColumn("orgOrderId");
                }else if(m.reType.val() == "20"){
                    tableGrid.showColumn("orgOrderId")
                }

            },
            ajaxSuccess:function(resData){
                return resData;
            }
        });
    }

    // 获取审核状态
    var getStatus = function(tdObj, value){
        switch (value) {
            case "1":
                value = "Pending";
                break;
            case "5":
                value = "Rejected";
                break;
            case "6":
                value = "Withdrawn";
                break;
            case "7":
                value = "Expired";
                break;
            case "10":
                value = "Approved";
                break;
            case "20":
                value = "Returned";
                break;
            default:
                value = "";
        }
        return $(tdObj).text(value);
    }
    var getSts=function(tdObj,value){
		if (value=='10'){
			return $(tdObj).text("Direct Return");
		}else {
			return $(tdObj).text("Original Document Based  Return");
		}


    }
    // 按钮权限验证
    var isButPermission = function () {
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#view").remove();
        }
        var editButPm = $("#editButPm").val();
        if (Number(editButPm) != 1) {
            $("#modify").remove();
        }
        var addButPm = $("#addButPm").val();
        if (Number(addButPm) != 1) {
            $("#add").remove();
        }
        var exportButPm = $("#exportButPm").val();
        if (Number(exportButPm) != 1) {
            $("#export").remove();
        }
        var printButPm = $("#printButPm").val();
        if (Number(printButPm) != 1) {
            $("#print").remove();
        }
        var approvalRecordButPm = $("#approvalRecordButPm").val();
        if (Number(approvalRecordButPm) != 1) {
            $("#approvalRecords").remove();
        }
    }

    //小数格式化
    var floatFmt = function(tdObj, value){
        if(!!value){value = parseFloat(value);}
        return $(tdObj).text(value);
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtStringDate(value);
        }
        return $(tdObj).text(value);
    }
    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date){
        if(!!date) {
            return date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        }
    }
    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
        }
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
var _index = require('returnVendor');
_start.load(function (_common) {
    _index.init(_common);
});
