require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('stockScrap', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
        reason = null,
        approvalRecordsParamGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
    const KEY = 'INVENTORY_WRITE_OFF_ENTRY';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        main_box : null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        // 查询部分
        search:null,
        reset:null,
        voucherNo:null,
        td_start_date:null,
        td_end_date:null,
        clear_td_date:null,
        dj_status:null,
        itemInfo:null,
        reason : null,
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
		url_root=_common.config.surl;
    	url_left=url_root+"/stockScrap";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
    	// 首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 加载下拉列表
		getSelectValue();
    	// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
        // 初始化店铺运营组织检索
        _common.initOrganization();
    	// 表格内按钮事件
    	table_event();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.td_start_date,m.td_end_date);
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
        if (obj.reviewSts=='-1') {
            obj.reviewSts='';
        }
        m.td_start_date.val(obj.voucherStartDate);
        m.td_end_date.val(obj.voucherEndDate);
        m.dj_status.val(obj.reviewSts);
        m.itemInfo.val(obj.itemInfo);
        m.voucherNo.val(obj.voucherNo);
        $.myAutomatic.setValueTemp(reason,obj.reason,obj.reasonName);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        //拼接检索参数
        setParamJson();
        paramGrid = "searchJson="+m.searchJson.val();
        tableGrid.setting("url",url_root + "/inventoryVoucher/getDataByType");
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
        obj.voucherStartDate=m.td_start_date.val();
        obj.voucherEndDate=m.td_end_date.val();
        obj.reasonName=$('#reason').attr('v');
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 表格内按钮事件
    var table_event = function(){
        // add按钮
        $("#addStoreDetails").on("click", function (e) {
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
            top.location = url_left+"/edit?flag=add";
        });

        // modify按钮
        $("#updateStoreDetails").on("click", function (e) {
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
                var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,voucherNo,voucherDate,voucherType,storeCd1");
                //获取数据审核状态
                _common.getRecordStatus(cols["voucherNo"],m.typeId.val(),function (result) {
                    if(result.success){
                        var voucherDate = '';
                        if(cols["voucherDate"]!=null&&cols["voucherDate"]!=''){
                            voucherDate = subfmtDate(cols["voucherDate"]);
                        }
                        var param = "storeCd=" + cols["storeCd"] + "&voucherNo=" + cols["voucherNo"]+
                            "&voucherDate=" + voucherDate + "&voucherType=" + cols["voucherType"]+
                            "&storeCd1=" + cols["storeCd1"] + "&flag=edit";
                        saveParamToSession();
                        top.location = url_left+"/edit?"+param;
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
            var cols = tableGrid.getSelectColValue(selectTrTemp,"voucherNo");
            approvalRecordsParamGrid = "id="+cols["voucherNo"]+
                "&typeIdArray="+m.typeId.val();
            approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
            approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
            approvalRecordsTableGrid.setting("page", 1);
            approvalRecordsTableGrid.loadData(null);
            $('#approvalRecords_dialog').modal("show");
        });

        // view按钮
        $("#viewStoreDetails").on("click", function (e) {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,voucherNo,voucherDate,voucherType,storeCd1");
                var voucherDate = '';
                if(cols["voucherDate"]!=null&&cols["voucherDate"]!=''){
                    voucherDate = subfmtDate(cols["voucherDate"]);
                }
                var param = "storeCd=" + cols["storeCd"] + "&voucherNo=" + cols["voucherNo"]+
                    "&voucherDate=" + voucherDate + "&voucherType=" + cols["voucherType"]+
                    "&storeCd1=" + cols["storeCd1"];
                saveParamToSession();
                top.location = url_left+"/view?"+param;
            }
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

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                initTable2();//列表初始化
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    function judgeNaN (value) {
        return value !== value;
    }

    //验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#td_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#td_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#td_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#td_start_date").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#td_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#td_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#td_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#td_end_date").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#td_end_date").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#td_end_date").focus();
            return false;
        }
        return true;
    }

    //拼接检索参数
    var setParamJson = function(){
        // 日期格式转换
        var _startDate = fmtStringDate(m.td_start_date.val())||null;
        var _endDate = fmtStringDate(m.td_end_date.val())||null;

        // 创建请求字符串
        var searchJsonStr ={
            regionCd:$("#aRegion").attr("k"),
            cityCd:$("#aCity").attr("k"),
            districtCd:$("#aDistrict").attr("k"),
            storeCd:$("#aStore").attr("k"),
            voucherNo:m.voucherNo.val(),
            voucherStartDate:_startDate,
            voucherEndDate:_endDate,
            itemInfo:m.itemInfo.val().trim(),
            reviewSts:m.dj_status.val().trim()||'-1',
            voucherType:'603',
            reason:$('#reason').attr('k'),
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    // 画面按钮点击事件
    var but_event = function(){
        // 清空日期
        m.clear_td_date.on("click",function(){
            m.td_start_date.val("");
            m.td_end_date.val("");
        });
        // 重置按钮
        m.reset.on("click",function(){
            m.voucherNo.val("");
            m.td_start_date.val("");
            m.td_end_date.val("");
            m.dj_status.val("");
            m.itemInfo.val("");
            m.searchJson.val("");
            $("#regionRemove").click();
            $("#reasonRemove").click();
            selectTrTemp = null;
            _common.clearTable();
        });
        // 检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()) {
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_root + "/inventoryVoucher/getDataByType");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
    }

    //表格初始化
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Inventory Write-off Query",
    		param:paramGrid,
			localSort: true,
    		colNames:["Document Date","Document No.","Store No.","Store Name","Store No.","Document Type","Document Type","Status",
				"Remarks","Amount","Date Created","Created By"],
    		colModel:[
				{name:"voucherDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"voucherNo",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"right",width:"100",ishide:true,css:""},
				{name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"storeCd1",type:"text",text:"right",ishide:true},
				{name:"voucherType",type:"text",text:"left",ishide:true},
				{name:"voucherType",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getTypeName},
				{name:"stsName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"remark",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"voucherAmtNoTax",type:"text",text:"right",width:"160",ishide:true,getCustomValue:getThousands},
				{name:"createYmd",type:"text",text:"center",width:"120",ishide:false,getCustomValue:_common.formatDateAndTime},
				{name:"createUser",type:"text",text:"left",width:"130",ishide:false}
			],//列内容
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
			},
			buttonGroup:[
                {
                    butType: "add",
                    butId: "addStoreDetails",
                    butText: "Add",
                    butSize: ""
                },{
                    butType: "update",
                    butId: "updateStoreDetails",
                    butText: "Modify",
                    butSize: ""
                },{
                    butType: "view",
                    butId: "viewStoreDetails",
                    butText: "View",
                    butSize: ""//,
                },{
                    butType:"custom",
                    butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
                },{
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
                {name:"typeName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"result",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"userCode",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"userName",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"dateTime",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"comments",type:"text",text:"left",width:"200",ishide:false,css:""},
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
        var addButPm = $("#addButPm").val();
        if (Number(addButPm) != 1) {
            $("#addStoreDetails").remove();
        }
        var editButPm = $("#editButPm").val();
        if (Number(editButPm) != 1) {
            $("#updateStoreDetails").remove();
        }
        var viewButPm = $("#viewButPm").val();
        if (Number(viewButPm) != 1) {
            $("#viewStoreDetails").remove();
        }
        var approvalrecordButPm = $("#approvalrecordButPm").val();
        if (Number(approvalrecordButPm) != 1) {
            $("#approvalRecords").remove();
        }
    }

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(value);
    }

	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		date = date.replace(/\//g,"");
		res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
		return res;
	}

    // 日期处理
    function subfmtDate(date){
        var res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
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

	// 获取传票类型名称
	function getTypeName(tdObj, value){
		var _value = "";
		switch (value) {
			case "603":
				// _value = "Scrap";
				_value = "Inventory Write-off";
				break;
		}
		return $(tdObj).text(_value);
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 初始化下拉列表
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
				_common.prompt(title+"Failed to load data!",5,"error");
			}
		});
	}

	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		initSelectOptions("Document Status","dj_status", "00430");
        //原因
        reason=$("#reason").myAutomatic({
            url:url_root+"/cm9010/getWriteOffReasonValue",
            ePageSize:10,
            startCount:0,
        })
	}

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('stockScrap');
_start.load(function (_common) {
	_index.init(_common);
});
