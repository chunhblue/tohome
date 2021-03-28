require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('invoiceEntryList', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},//临时行数据存储
        dataForm=[],
        systemPath='';
    const KEY = 'INVOICE_ENTRY_TO_CUSTOMER';
    var m = {
        toKen : null,
        dialogBox : null,
        use : null,
        error_pcode : null,
        main_box:null,
        identity : null,
        searchJson:null,
        searchStartDate:null,
        searchCustomer:null,
        searchTelephone:null,
        searchEndDate:null,
        storeNo: null,
        clearDate:null,
        search : null,//检索按钮
        reset:null,
        searchStatus:null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/invoiceEntry";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        but_event();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        //列表初始化
        initTable1();
        //表格内按钮事件
        table_event();
        initAutoMatic();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.searchStartDate,m.searchEndDate);
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

        m.searchStartDate.val(obj.startDate);
        m.searchEndDate.val(obj.endDate);
        m.searchStatus.val(obj.status);
        m.searchCustomer.val(obj.customerName);
        m.searchTelephone.val(obj.phone);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_left+"/searchInvoiceList");
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
        obj.startDate=m.searchStartDate.val();
        obj.endDate=m.searchEndDate.val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    var table_event = function(){
        // 修改发票状态
        $("#issueBut").on('click', function () {

            if (!selectTrTemp) {
                _common.prompt("Please select one row data!",5,"info");/*请选择一行数据*/
                return;
            }

            let col = tableGrid.getSelectColValue(selectTrTemp,'accId,storeNo,status');
            let accId = col['accId'];
            let storeNo = col['storeNo'];
            let status = col['status'];

            if (status!='01') {
                _common.prompt("Please select not invoiced data!",5,"error");/*请选择没开具发票的数据*/
                return;
            }

            if (!accId || !storeNo) {
                _common.prompt('Data exception!', 5, 'error');
                return;
            }

            _common.myConfirm("Are you sure you want to invoice?", function (result) {
                if (result != "true") {
                    return false;
                }
                $.myAjaxs({
                    url: url_left + '/updateStatus',
                    async: true,
                    cache: false,
                    type: "post",
                    data: 'accId='+accId+'&storeNo='+storeNo,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            // 变为查看模式
                            _common.prompt("Data modify succeed！", 5, "info");/*保存成功*/
                            m.search.click();
                        } else {
                            _common.prompt(result.msg, 5, "error");
                        }
                    },
                    error: function (e) {
                        _common.prompt("Data modify failed！", 5, "error");/*保存失败*/
                    }
                });
            });
        });

        //进入添加
        $("#addBut").on("click", function (e) {
            saveParamToSession();
            top.location = url_left+"/edit";
        });

        $("#viewBut").on("click", function (e) {
            if (!selectTrTemp) {
                _common.prompt("Please select one row data!",5,"info");/*请选择一行数据*/
                return;
            }
            let col = tableGrid.getSelectColValue(selectTrTemp,'accId,storeNo');
            saveParamToSession();
            top.location = url_left+'/edit?enterFlag=view&accId='+col['accId']+'&storeNo='+col['storeNo'];
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
    }

    function judgeNaN (value) {
        return value !== value;
    }

    //验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#searchStartDate").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#searchStartDate").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#searchStartDate").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#searchStartDate").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#searchEndDate").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#searchEndDate").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#searchEndDate").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#searchEndDate").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#searchEndDate").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#searchEndDate").focus();
            return false;
        }
        return true;
    }

    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g,'');
        var day = dateStr.substring(0,2);
        var month = dateStr.substring(2,4);
        var year = dateStr.substring(4,8);
        return year+month+day;
    }

    function setParamJson() {
        let searchCustomer = m.searchCustomer.val().trim();
        let searchStartDate = formatDate(m.searchStartDate.val());
        let searchEndDate = formatDate(m.searchEndDate.val());
        let searchStatus = m.searchStatus.val().trim();
        let searchTelephone = m.searchTelephone.val().trim();

        let obj = {
            'regionCd':$("#aRegion").attr("k"),
            'cityCd':$("#aCity").attr("k"),
            'districtCd':$("#aDistrict").attr("k"),
            'storeCd':$("#aStore").attr("k"),
            'startDate': searchStartDate,
            'endDate': searchEndDate,
            'status': searchStatus,
            'customerName': searchCustomer,
            'phone': searchTelephone,
        }
        m.searchJson.val(JSON.stringify(obj));
    }

    //画面按钮点击事件
    var but_event = function(){
        //清空日期
        m.clearDate.on("click",function(){
            m.searchStartDate.val("");
            m.searchEndDate.val("");
        });

        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                tableGrid.setting("url",url_left+"/searchInvoiceList");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
        m.reset.on("click",function(){
            m.searchStartDate.val("");
            m.searchEndDate.val("");
            m.searchCustomer.val("");
            m.searchTelephone.val("");
            m.searchStatus.val("");
            $("#regionRemove").click();
            selectTrTemp = null;
            _common.clearTable();
        });
    }

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Invoice List",
            param:paramGrid,
            localSort: true,
            colNames:["accId","Store No.","Store Name","Date and Time","Receipt No.","Amount","Customer's Name","Company's Name","TAX CODE","Address (which register with the Tax government)","Telephone Number 1","Telephone Number 2","E-mail 1","E-mail 2","status","Status"],
            colModel:[
                {name:"accId",type:"text",text:"center",width:"130",ishide:true,css:""},
                {name:"storeNo",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"160",ishide:false,css:""},
                {name:"accDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue: dateFmt},
                {name:"saleSerialNo",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"amt",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue: getThousands},
                {name:"customerName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"companyName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"tax",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"address",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"phone",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"phone2",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"email",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"email2",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"status",type:"text",text:"left",width:"130",ishide:true,css:""},
                {name:"statusName",type:"text",text:"left",width:"130",ishide:false,css:""},
            ],//列内容
            // traverseData:dataForm,
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
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                {
                    butType: "add",
                    butId: "addBut",
                    butText: "Add",
                    butSize: ""
                }, {
                    butType: "view",
                    butId: "viewBut",
                    butText: "view",
                    butSize: ""
                }, {
                    butType:"custom",
                    butHtml:"<button id='issueBut' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-pencil'></span>Issue Invoice</button>"
                }, {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                }
            ],
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#viewBut").remove();
        }
        var issueButPm = $("#issueButPm").val();
        if (Number(issueButPm) != 1) {
            $("#issueBut").remove();
        }
        var addButPm = $("#addButPm").val();
        if (Number(addButPm) != 1) {
            $("#addBut").remove();
        }
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }
    //初始化店铺下拉
    var initAutoMatic = function () {
        // 获取status 下拉
        $.myAjaxs({
            url: url_left + '/getStatus',
            async: false,
            cache: false,
            type: "get",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    let list = result.o;
                    list.forEach(function (item) {
                        let temp = '<option value="'+item.codeValue+'">'+item.codeName+'</option>';
                        $('#searchStatus').append(temp);
                    })
                }
            }
        });
    }
    //格式化数字类型的日期
    function fmtIntDate(date) {
        if (!date) {
            return '';
        }
        let year = date.substring(0,4);
        let month = date.substring(4,6);
        let day = date.substring(6,8);
        let hour = date.substring(8,10);
        let minute = date.substring(10,12);
        let sec = date.substring(12,14);
        date=year+'/'+month+'/'+day+' '+hour+':'+minute+':'+sec;
        return new Date(date).Format('dd/MM/yyyy hh:mm');
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
var _index = require('invoiceEntryList');
_start.load(function (_common) {
    _index.init(_common);
});
