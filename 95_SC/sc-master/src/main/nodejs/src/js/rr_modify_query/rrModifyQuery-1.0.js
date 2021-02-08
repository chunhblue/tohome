require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receipt', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        selectTrTemp = null,
        approvalRecordsParamGrid = null,
        tempTrObjValue = {},//临时行数据存储
        common=null;
    const KEY = 'RECEIVING_AND_RETURN_DOCUMENT_CORRECTION_QUERY';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        searchJson:null,
        main_box : null,
        // 查询部分
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        xz_start_date:null,
        xz_end_date:null,
        md_voucher_no:null,
        or_voucher_no:null,
        modificationType:null,
        vendorId:null,
        reviewStatus:null,
        search:null,
        reset:null,
        typeId:null,//审核类型id
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left=_common.config.surl+"/rrModifyQuery";
        // 首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        getSelectValue();
        initTable();//列表初始化
        // 初始化店铺运营组织检索
        _common.initOrganization();
        // 表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.xz_start_date,m.xz_end_date);
        // 初始化参数
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

        m.xz_start_date.val(obj.modifyStartDate);
        m.xz_end_date.val(obj.modifyEndDate);
        m.md_voucher_no.val(obj.orderId);
        m.or_voucher_no.val(obj.orgOrderId);
        m.modificationType.val(obj.orderType);
        m.vendorId.val(obj.vendorId);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/getList");
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
        obj.modifyStartDate=m.xz_start_date.val();
        obj.modifyEndDate=m.xz_end_date.val();
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 表格内按钮事件绑定
    var table_event = function(){
        // view
        $("#view").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else {
                var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,orderId");
                var param = "storeCd=" + cols["storeCd"] + "&orderId=" + cols["orderId"];
                saveParamToSession();
                top.location = url_left + "/view?" + param;
            }
        })

        // 修改
        $("#update").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else {
                var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,orderId,orgOrderId");
                //获取数据审核状态
                _common.getRecordStatus(cols["orderId"],m.typeId.val(),function (result) {
                    if(result.success||result.data==10){
                        /*//验证订货日是否为当前业务日
                        _common.checkBusinessDate(tempTrOrderDate,null,function (result) {
                            if(result.success){
                            }else{//禁用
                                _common.prompt("Selected document is not created within today and cannot be modified!",5,"error");
                            }
                        })*/
                        getCountOrderId(cols["orgOrderId"],function (e) {
                            if(e > 0){
                                _common.prompt("The Original Document No has entered the returns process!",5,"Info");
                            }else {
                                saveParamToSession();
                                var param = "storeCd=" + cols["storeCd"] + "&orderId=" + cols["orderId"];
                                top.location = url_left + "/edit?" + param;
                            }
                        })

                    }else{
                        _common.prompt(result.message,5,"error");
                    }
                });
            }
        });

        /*// 删除
        $("#delete").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select the data to delete!",5,"error");
                return false;
            }
        });*/

        //审核记录
        $("#approvalRecords").on("click",function(){
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,orderId");
            approvalRecordsParamGrid = "id="+cols["orderId"]+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
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
        })
    };

// 统计收货单号已进行退货流程的数量
    var getCountOrderId = function (receiveId,callback) {
        $.myAjaxs({
            url:url_left + "/countOrgOrderId",
            async:true,
            cache:false,
            type:"post",
            data:"receiveId=" + receiveId,
            dateType:"json",
            success:function(e){
                let count = e.o;
                callback(count);
            },
            error:function(){
                prompt("The count failed, Please try again!",5,"error");// 请求失败
            }
        })
    };

    // 按钮权限验证
    var isButPermission = function () {
        let viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#view").remove();
        }
        var editBut = $("#editBut").val();
        if (Number(editBut) != 1) {
            $("#update").remove();
        }
        var exportBut = $("#exportBut").val();
        if (Number(exportBut) != 1) {
            $("#exportBut").remove();
        }
        var approvalBut = $("#approvalBut").val();
        if (Number(approvalBut) != 1) {
            $("#approvalRecords").remove();
        }
    }

    // 画面按钮点击事件
    var but_event = function(){
        // 清空按钮
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.xz_start_date.val("");
            m.xz_end_date.val("");
            m.md_voucher_no.val("");
            m.or_voucher_no.val("");
            m.modificationType.val("");
            m.vendorId.val("");
            selectTrTemp = null;
            m.searchJson.val("");
            _common.clearTable();
        });

        // 检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/getList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#xz_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#xz_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#xz_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#xz_start_date").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#xz_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#xz_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#xz_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#xz_end_date").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            $("#xz_end_date").focus();
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue >62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#xz_end_date").focus();
            return false;
        }
        return true;
    }

    //拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        let _mdStartDate = subfmtDate(m.xz_start_date.val())||null;
        let _mdEndDate = subfmtDate(m.xz_end_date.val())||null;
        // 创建请求字符串
        let searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            modifyStartDate:_mdStartDate,
            modifyEndDate:_mdEndDate,
            orderId:m.md_voucher_no.val().trim(),
            orgOrderId:m.or_voucher_no.val().trim(),
            orderType:m.modificationType.val().trim(),
            vendorId:m.vendorId.val().trim(),
            reviewStatus:m.reviewStatus.val().trim(),
            oc:$("#oc").attr("k"),
            am:$("#am").attr("k"),
            om:$("#om").attr("k")
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //表格初始化样式
    var initTable = function(){
        tableGrid = $("#zgGridTable").zgGrid({
            title:"Document Correction Details",
            param:paramGrid,
            localSort: true,
            colNames:["Modification No.","Modification Date","Vendor Code","Vendor Name","Store No.","Store Name","Area Manager Name","Operation Manager Name","Operation Controller Name",
                "Modification Type","Original Document No.","Document Status","Description"],
            colModel:[
                {name:"orderId",type:"text",text:"right",width:"130",ishide:false},
                {name:"orderDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"vendorId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"vendorName",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"storeCd",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"am",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"om",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"oc",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"orderType",type:"text",text:"left",width:"180",ishide:false,css:"",getCustomValue:getOrderType},
                {name:"orgOrderId",type:"text",text:"right",width:"160",ishide:false,css:""},
                {name:"reviewStatus",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getStatus},
                {name:"orderRemark",type:"text",text:"left",width:"200",ishide:false,css:""},
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
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
            },
            buttonGroup:[
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                },//查看
                {
                    butType: "update",
                    butId: "update",
                    butText: "Modify",
                    butSize: ""
                },//修改
                {
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                },
                {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
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
            ajaxSuccess:function(resData){
                return resData;
            }
        });
    }
    // 请求加载下拉列表
    function getSelectValue(){

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
    }
    // 为0返回'0'
    function getString0(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 获取类型名称
    function getOrderType(tdObj, value){
        var _value = "";
        switch (value) {
            case "07":
                _value = "[07] Receiving Document Correction";
                break;
            case "08":
                _value = "[08] Return Document Correction";
                break;
        }
        return $(tdObj).text(_value);
    }

    // 获取审核状态
    var getStatus = function(tdObj, value){
        let temp = '' + value;
        switch (temp) {
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
            case "15":
                value = "Receiving Pending";
                break;
            case "20":
                value = "Received";
                break;
            case "30":
                value = "Paid";
                break;
            default:
                value = "";
        }
        return $(tdObj).text(value);
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    // 日期处理
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

    // 格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receipt');
_start.load(function (_common) {
    _index.init(_common);
});
