require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myValidator = require("myValidator");
define('saleReconciliation', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
		staff="",
		am="",
		// a_region="",
		// a_city="",
		// a_district="",
		// a_store="",
    	common=null;
	const KEY = 'CASHIER_SHIFT_REPORT_QUERY';
    var m = {
		toKen : null,
		use : null,
		up : null,
		clear_bs_date : null,
		bs_start_date: null,
		bs_end_date: null,
		staffId:null,//员工id
		staffName:null,//员工名称
		payAmtFlg:null,//差异类型
		payAmtDiff:null,//差异金额
		shift:null,//班次
		posId:null,//posId
		alert_div : null,//页面提示框
		search : null,//检索按钮
		reset : null,//清空按钮
		main_box : null,//检索按钮
		searchJson:null,
		checkCount:null,
		urgencyCount:null,
		del_alert:null,
		aRegion : null,
		aCity : null,
		am:null,
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
		systemPath = _common.config.surl;
    	url_left=_common.config.surl+"/saleReconciliation";
    	//画面按钮点击事件
		but_event();
		// 初始化下拉
		initAutoMatic();
		initTable1();
		$("#update").hide();
    	table_event();
		// 初始化店铺运营组织检索
		initOrganization();
		//权限验证
		_myValidator.init();
		isButPermission();
		// m.search.click();
		// 初始化检索日期
		_common.initDate(m.bs_start_date,m.bs_end_date);
		initParam();
    }
	var initAutoMatic=function () {
		staff = $("#staffId").myAutomatic({
			url: systemPath +  "/cashierAmount/getCashierList",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function (thisObj) {

			},
			selectEleClick: function (thisObj) {

			}
		});
		am = $("#am").myAutomatic({
			url: systemPath + "/ma1000/getAMByPM",
			ePageSize: 10,
			startCount: 0,
		});

	};

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
		// 设置组织架构回显
		_common.setAutomaticVal(obj);
		$.myAutomatic.setValueTemp(am,obj.am,obj.amName);
		$.myAutomatic.setValueTemp(staff,obj.staffId,obj.userId);
		m.bs_start_date.val(obj.vStartDate);
		m.bs_end_date.val(obj.vEndDate);
		m.payAmtFlg.val(obj.payAmtFlg);
		m.payAmtDiff.val(obj.payAmtDiff);

		var StartDate = "";
		if(m.bs_start_date.val()!=null&&m.bs_start_date.val()!==""){
			StartDate = subfmtDate(m.bs_start_date.val())
		}
		var EndDate = "";
		if(m.bs_end_date.val()!=null&&m.bs_end_date.val()!==""){
			EndDate = subfmtDate(m.bs_end_date.val())
		}
		// 创建请求字符串
		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"), cityCd:$("#aCity").attr("k"), districtCd:$("#aDistrict").attr("k"), storeCd:$("#aStore").attr("k"), vStartDate:StartDate,
			vEndDate:EndDate,  payAmtFlg:m.payAmtFlg.val(), payAmtDiff:m.payAmtDiff.val(), am:$("#am").attr("k")
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));

		paramGrid = "regionCd="+$("#aRegion").attr("k")+"&cityCd="+$("#aCity").attr("k")+"&districtCd="+$("#aDistrict").attr("k")+"&storeCd="+$("#aStore").attr("k")+
			"&vStartDate="+StartDate+"&vEndDate="+EndDate+"&payAmtFlg="+m.payAmtFlg.val()+
			"&payAmtDiff=" + m.payAmtDiff.val()  +  "&am=" + $("#am").attr("k");
		tableGrid.setting("url",url_left+"/getCashDetailyList");
		tableGrid.setting("param", paramGrid);
		tableGrid.setting("page", 1);
		tableGrid.loadData();
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
		obj.am=$("#am").attr("k");
		obj.amName=$('#am').attr('v');
		obj.vStartDate=m.bs_start_date.val();
		obj.vEndDate=m.bs_end_date.val();
		obj.payAmtFlg=m.payAmtFlg.val();
		obj.payAmtDiff=m.payAmtDiff.val();
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

    var table_event = function(){
		// 只能输入数字
		$('input[type=number]').keypress(function(e) {
			if (!String.fromCharCode(e.keyCode).match(/[0-9\.]/)) {
				return false;
			}
		});

		$("#view").on("click",function(e){
			if (selectTrTemp==null) {
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			} else{
				let cols = tableGrid.getSelectColValue(selectTrTemp,"payId,storeCd,payDate");
				let payDate = '';
				if(cols["payDate"]!=null&&cols["payDate"]!=''){
					payDate = subfmtDate(cols["payDate"]);
				}
				let param = "storeCd="+cols["storeCd"]+"&payDate="+payDate+"&payId="+cols["payId"]+"&flag=view";
				saveParamToSession();
				top.location = systemPath+"/cashierAmount?"+param;
			}
		});
		// 修改权限
		$("#update").on("click", function (e) {
			if (selectTrTemp==null) {
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			} else{
				let cols = tableGrid.getSelectColValue(selectTrTemp,"payId,storeCd,payDate");
				let payId = subfmtDate(cols['payId']);
				let payDate = subfmtDate(cols['payDate']);
				//验证订货日是否为当前业务日
				_common.checkBusinessDate(payDate, payId,function (result) {
					if(result.success){
						let param = "storeCd="+cols["storeCd"]+"&payDate="+payDate+"&payId="+cols["payId"]+"&flag=update";
						saveParamToSession();
						top.location = systemPath+"/cashierAmount?"+param;
					}else{//禁用
						_common.prompt("Selected document is not created within today and cannot be modified!",3,"info");
					}
				})
			}
		});

		// 导出按钮点击事件
		$("#export").on("click",function(){
			if(verifySearch()) {
				// 拼接检索参数
				var regionCd = $("#aRegion").attr("k");
				var cityCd = $("#aCity").attr("k");
				var districtCd = $("#aDistrict").attr("k");
				var storeCd = $("#aStore").attr("k");
				var am = $("#am").attr("k");
				var vStartDate = "";
				if (m.bs_start_date.val() != null && m.bs_start_date.val() != "") {
					vStartDate = subfmtDate(m.bs_start_date.val())
				}
				var vEndDate = "";
				if (m.bs_end_date.val() != null && m.bs_end_date.val() != "") {
					vEndDate = subfmtDate(m.bs_end_date.val())
				}
				var payAmtFlg = m.payAmtFlg.val();
				var payAmtDiff = m.payAmtDiff.val();
				if (payAmtFlg == null || payAmtFlg == "") {
					payAmtDiff = "";

					var searchJsonStr = {
						am: am,
						regionCd: regionCd,
						cityCd: cityCd,
						districtCd: districtCd,
						storeCd: storeCd,
						vStartDate: vStartDate,
						vEndDate: vEndDate,
						payAmtFlg: payAmtFlg,
						payAmtDiff: payAmtDiff
					};
					paramGrid = "searchJson=" + JSON.stringify(searchJsonStr);
					var url = url_left + "/export?" + paramGrid;
					window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
				}
			}
		});
    };



    //画面按钮点击事件
    var but_event = function(){
		$("#bs_start_date").blur(function () {
			if (m.bs_start_date.val() != null || m.bs_start_date.val() != null) {
				$("#bs_start_date").css("border-color","#CCCCCC");
			}
		});
		$("#bs_end_date").blur(function () {
			if (m.bs_end_date.val() != null || m.bs_end_date.val() != null) {
				$("#bs_end_date").css("border-color","#CCCCCC");
			}
		});
		$("#am").blur(function () {
			if (m.am.val() != null || m.am.val() != null) {
				$("#am").css("border-color","#CCCCCC");
			}
		});
		$("#amRemove").on("click",function (e) {
			$.myAutomatic.cleanSelectObj(am);
		});
		//清空日期
		m.clear_bs_date.on("click",function(){
			m.bs_start_date.val("");
			m.bs_end_date.val("");
		});
    	m.reset.on("click",function(){
    		m.bs_start_date.val("");
    		m.bs_end_date.val("");
			m.payAmtFlg.val("");
			m.payAmtDiff.val("");
			$("#regionRemove").click();
			$("#amRemove").click();
			$("#bs_start_date").css("border-color","#CCCCCC");
			$("#bs_end_date").css("border-color","#CCCCCC");
			$("#am").css("border-color","#CCCCCC");
			selectTrTemp = null;
			_common.clearTable();
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			if(verifySearch()){
				$("#bs_start_date").css("border-color","#CCCCCC");
				$("#bs_end_date").css("border-color","#CCCCCC");
				$("#am").css("border-color","#CCCCCC");
				var regionCd = $("#aRegion").attr("k");
				var cityCd = $("#aCity").attr("k");
				var districtCd = $("#aDistrict").attr("k");
				var storeCd = $("#aStore").attr("k");
				var am = $("#am").attr("k");
				var vStartDate = "";
				if(m.bs_start_date.val()!=null&&m.bs_start_date.val()!=""){
					vStartDate = subfmtDate(m.bs_start_date.val())
				}
				var vEndDate = "";
				if(m.bs_end_date.val()!=null&&m.bs_end_date.val()!=""){
					vEndDate = subfmtDate(m.bs_end_date.val())
				}
				var payAmtFlg = m.payAmtFlg.val();
				var payAmtDiff = m.payAmtDiff.val();
				if(payAmtFlg==null||payAmtFlg==""){
					payAmtDiff = "";
				}
				// 创建请求字符串
				var searchJsonStr ={
					regionCd:regionCd, cityCd:cityCd, districtCd:districtCd, storeCd:storeCd, vStartDate:vStartDate,
					vEndDate:vEndDate, payAmtFlg:payAmtFlg, payAmtDiff:payAmtDiff,am:am,
				};
				m.searchJson.val(JSON.stringify(searchJsonStr));
				paramGrid = "regionCd="+regionCd+"&cityCd="+cityCd+"&districtCd="+districtCd+"&storeCd="+storeCd+
					"&vStartDate="+vStartDate+"&vEndDate="+vEndDate+"&payAmtFlg="+payAmtFlg+
						"&payAmtDiff=" + payAmtDiff + "&am=" + am;
				tableGrid.setting("url",url_left+"/getCashDetailyList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
    	});
    };

    
    //表格初始化
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Query Result",
    		param:paramGrid,
			colNames:["Pay ID","Store No.","Store Name","Area Manager ID","Area Manager Name","Business Date",
				"Customer Count","PSD","Shift1","Shift2","Shift3","Additional","Offset Claim","Total Amount Received",
				"Shift1 Received","Shift2 Received","Shift3 Received","Variance Amount","Total Amount"],
			colModel:[
				{name:"payId",type:"text",text:"left",width:"150",ishide:true,css:""},
				{name:"storeCd",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"ofc",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"amName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"payDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"customerQty",type:"number",text:"right",width:"130",ishide:false,css:"",getCustomValue:formatNum},
				{name:"customerAvgPrice",type:"number",text:"right",width:"130",ishide:true,css:"",getCustomValue:formatNum},
				{name:"payInAmt0",type:"number",text:"right",width:"130",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payInAmt1",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payInAmt2",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"additional",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"offsetClaim",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payAmt",type:"number",text:"right",width:"170",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payAmt0",type:"number",text:"right",width:"130",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payAmt1",type:"number",text:"right",width:"170",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payAmt2",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payAmtDiff",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"payInAmt",type:"number",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
	          ],//列内容
	          width:"max",//宽度自动
	          page:1,//当前页
	          rowPerPage:10,//每页数据量
	          isPage:false,//是否需要分页
	          isCheckbox:false,
			  eachTrClick: function (trObj) {//正常左侧点击
				  $("#update").removeAttr("disabled");
			   },
	          loadEachBeforeEvent:function(trObj){
	        	  tempTrObjValue={};
	        	  return trObj;
	          },
	          ajaxSuccess:function(resData){
	        	  return resData;
	          },
	          loadCompleteEvent:function(self){
	        	  selectTrTemp = null;//清空选择的行
				  //更新总条数(注减一是去除标题行)
				  var total = tableGrid.find("tr").length-1;
				  $("#records").text(total);

				  if($("#zgGridTtable>.zgGrid-tbody tr").length>0){
					  var payInAmt0 = 0,payInAmt1 = 0,payInAmt2 = 0,payInAmt3 = 0,payInAmt4 = 0,payInAmt5 = 0,additional=0,offsetClaim=0;
					  var payAmt0 = 0,payAmt1 = 0,payAmt2 = 0,payAmt3 = 0,payAmt4 = 0,payAmt5 = 0;
					  var payInAmt = 0,payAmt = 0,payAmtDiff = 0;
					  var customerQty = 0,customerAvgPrice = 0;
					  $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
						  var td_payInAmt0 = parseFloat($(this).find('td[tag=payInAmt0]').text().replace(/,/g, ""));
						  var td_payInAmt1 = parseFloat($(this).find('td[tag=payInAmt1]').text().replace(/,/g, ""));
						  var td_payInAmt2 = parseFloat($(this).find('td[tag=payInAmt2]').text().replace(/,/g, ""));
						  var td_additional = parseFloat($(this).find('td[tag=additional]').text().replace(/,/g, ""));
						  var td_offsetClaim = parseFloat($(this).find('td[tag=offsetClaim]').text().replace(/,/g, ""));
						  if(!isNaN(td_payInAmt0))
							  payInAmt0 +=  parseFloat(td_payInAmt0);
						  if(!isNaN(td_payInAmt1))
							  payInAmt1 +=  parseFloat(td_payInAmt1);
						  if(!isNaN(td_payInAmt2))
							  payInAmt2 +=  parseFloat(td_payInAmt2);
						  if(!isNaN(td_additional))
							  additional +=  parseFloat(td_additional);
						  if(!isNaN(td_offsetClaim))
							  offsetClaim +=  parseFloat(td_offsetClaim);

						  var td_payAmt0 = parseFloat($(this).find('td[tag=payAmt0]').text().replace(/,/g, ""));
						  var td_payAmt1 = parseFloat($(this).find('td[tag=payAmt1]').text().replace(/,/g, ""));
						  var td_payAmt2 = parseFloat($(this).find('td[tag=payAmt2]').text().replace(/,/g, ""));
						  if(!isNaN(td_payAmt0))
							  payAmt0 +=  parseFloat(td_payAmt0);
						  if(!isNaN(td_payAmt1))
							  payAmt1 +=  parseFloat(td_payAmt1);
						  if(!isNaN(td_payAmt2))
							  payAmt2 +=  parseFloat(td_payAmt2);
						  var td_payInAmt = parseFloat($(this).find('td[tag=payInAmt]').text().replace(/,/g, ""));
						  var td_payAmt = parseFloat($(this).find('td[tag=payAmt]').text().replace(/,/g, ""));
						  var td_payAmtDiff = parseFloat($(this).find('td[tag=payAmtDiff]').text().replace(/,/g, ""));
						  if(!isNaN(td_payInAmt))
							  payInAmt +=  parseFloat(td_payInAmt);
						  if(!isNaN(td_payAmt))
							  payAmt +=  parseFloat(td_payAmt);
						  if(!isNaN(td_payAmtDiff))
							  payAmtDiff +=  parseFloat(td_payAmtDiff);
					  });
					  var total = "<tr style='text-align:right' id='total'><td></td><td></td><td></td><td></td><td></td>" +
						  "<td>Total:</td>" +
						  "<td>"+fmtIntNum(payInAmt0)+"</td>" +
						  "<td>"+fmtIntNum(payInAmt1)+"</td>"+
						  "<td>"+fmtIntNum(payInAmt2)+"</td>" +
						  "<td>"+fmtIntNum(additional)+"</td>" +
						  "<td>"+fmtIntNum(offsetClaim)+"</td>" +
						  // "<td>"+fmtIntNum(payInAmt)+"</td>" +
						  "<td>"+fmtIntNum(payAmt)+"</td>" +
						  "<td>"+fmtIntNum(payAmt0)+"</td>" +
						  "<td>"+fmtIntNum(payAmt1)+"</td>" +
						  "<td>"+fmtIntNum(payAmt2)+"</td>" +
						  "<td>"+fmtIntNum(payAmtDiff)+"</td>" +
						  "<td>"+fmtIntNum(payInAmt)+"</td>" +
						  "</tr>"
					  $("#zgGridTtable_tbody").append(total);
				  }
	        	  return self;
	          },
	          eachTrClick:function(trObj,tdObj){//正常左侧点击
				  let cols = tableGrid.getSelectColValue(trObj,"storeCd");
				  if(cols["storeCd"]!=""){
					  selectTrTemp = trObj;
				  }else{
					  selectTrTemp = null;
				  }
	          },
			buttonGroup:[
				{
					butType: "update",
					butId: "update",
					butText: "Modify",
					butSize: ""
				},
				{
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: "",
				},
				{
					butType:"custom",
					butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
				}
			]
    	});
		$("#zgGridTtable_main_foot").append("<div class='zgGrid-tfoot-td' id='zgGridTtable_tfoot_box'><span class='zg-page-records'>Total: <span id='records'>0</span> items</span></div>");


	}

	// 按钮权限验证
	var isButPermission = function () {
		var exportBut = $("#exportBut").val();
		if (Number(exportBut) != 1) {
			$("#export").remove();
		}
	}

	function judgeNaN (value) {
		return value !== value;
	}

	//验证检索项是否合法
	var verifySearch = function() {
		let _StartDate = null;
		if(!$("#bs_start_date").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#bs_start_date").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#bs_start_date").val())).getTime();
			if(judgeNaN(_StartDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#bs_start_date").focus();
				return false;
			}
		}
		let _EndDate = null;
		if(!$("#bs_end_date").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#bs_end_date").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#bs_end_date").val())).getTime();
			if(judgeNaN(_EndDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#bs_end_date").focus();
				return false;
			}
		}
		if(_StartDate>_EndDate){
			$("#bs_end_date").focus();
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue>62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#bs_end_date").focus();
			return false;
		}
		if(m.payAmtFlg.val() != "") {
			if (m.payAmtDiff.val() == "") {
				_common.prompt("The difference amount cannot be empty!", 5, "error"); // 差异金额不能为空
				m.payAmtDiff.focus();
				return false;
			} else {
				var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
				if (!reg.test(m.payAmtDiff.val())) {
					_common.prompt("Discrepancy amount format error!", 5, "error"); // 差异金额格式错误
					m.payAmtDiff.focus();
					return false;
				}
			}
			return true;
		}
		return true;
	}
	var initOrganization = function () {
		// 初始化，子级禁用
		// $("#aCity").attr("disabled", true);
		// $("#aDistrict").attr("disabled", true);
		// $("#aStore").attr("disabled", true);
		// $("#cityRefresh").hide();
		// $("#cityRemove").hide();
		// $("#districtRefresh").hide();
		// $("#districtRemove").hide();
		// $("#storeRefresh").hide();
		// $("#storeRemove").hide();
		// 输入框事件绑定
		a_region = $("#aRegion").myAutomatic({
			url: systemPath + "/roleStore/getRegion",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_city);
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				// $("#aCity").attr("disabled", true);
				// $("#aDistrict").attr("disabled", true);
				// $("#aStore").attr("disabled", true);
				// $("#cityRefresh").hide();
				// $("#cityRemove").hide();
				// $("#districtRefresh").hide();
				// $("#districtRemove").hide();
				// $("#storeRefresh").hide();
				// $("#storeRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#aCity").attr("disabled", false);
				// 	$("#cityRefresh").show();
				// 	$("#cityRemove").show();
				// }
				var rinput = thisObj.attr("k");
				// 替换子级查询参数
				var str = "&regionCd=" + rinput;
				$.myAutomatic.replaceParam(a_city, str);
			}
		});
		a_city = $("#aCity").myAutomatic({
			url: systemPath + "/roleStore/getCity",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				// $("#aDistrict").attr("disabled", true);
				// $("#aStore").attr("disabled", true);
				// $("#districtRefresh").hide();
				// $("#districtRemove").hide();
				// $("#storeRefresh").hide();
				// $("#storeRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#aDistrict").attr("disabled", false);
				// 	$("#districtRefresh").show();
				// 	$("#districtRemove").show();
				// }
				var rinput = $("#aRegion").attr("k");
				var cinput = thisObj.attr("k");
				// 替换子级查询参数
				var str = "&regionCd=" + rinput + "&cityCd=" + cinput;
				$.myAutomatic.replaceParam(a_district, str);
			}
		});
		a_district = $("#aDistrict").myAutomatic({
			url: systemPath + "/roleStore/getDistrict",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_store);
				// $("#aStore").attr("disabled", true);
				// $("#storeRefresh").hide();
				// $("#storeRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#aStore").attr("disabled", false);
				// 	$("#storeRefresh").show();
				// 	$("#storeRemove").show();
				// }
				var rinput = $("#aRegion").attr("k");
				var cinput = $("#aCity").attr("k");
				var dinput = thisObj.attr("k");
				// 替换子级查询参数
				var str = "&regionCd=" + rinput + "&cityCd=" + cinput + "&districtCd=" + dinput;
				$.myAutomatic.replaceParam(a_store, str);
			}
		});
		a_store = $("#aStore").myAutomatic({
			url: systemPath + "/roleStore/getStore",
			ePageSize: 8,
			startCount: 0,
			cleanInput: function (thisObj) {
				m.posId.find("option:not(:first)").remove();
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					getSelectPosId(thisObj.attr("k"))
				}
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
			// $("#aCity").attr("disabled", true);
			// $("#aDistrict").attr("disabled", true);
			// $("#aStore").attr("disabled", true);
			// $("#cityRefresh").hide();
			// $("#cityRemove").hide();
			// $("#districtRefresh").hide();
			// $("#districtRemove").hide();
			// $("#storeRefresh").hide();
			// $("#storeRemove").hide();
		});
		$("#cityRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_store);
			$.myAutomatic.replaceParam(a_district, null);
			$.myAutomatic.replaceParam(a_store, null);
			// $("#aDistrict").attr("disabled", true);
			// $("#aStore").attr("disabled", true);
			// $("#districtRefresh").hide();
			// $("#districtRemove").hide();
			// $("#storeRefresh").hide();
			// $("#storeRemove").hide();
		});
		$("#districtRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_store);
			// $("#aStore").attr("disabled", true);
			// $("#storeRefresh").hide();
			// $("#storeRemove").hide();
		});

		// 如果只有一条店铺权限，则默认选择
		$.myAjaxs({
			url:systemPath+"/roleStore/getStoreByRole",
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
	};
	var shiftFmt = function(tdObj, value){
    	var shiftName = "";
    	if(value=="00"){
			shiftName = "morning shift"
		}else if(value=="01"){
			shiftName = "middle shift"
		}else if(value=="02"){
			shiftName = "night shift"
		}
		return $(tdObj).text(shiftName);
	}

	//number格式化
	var formatNum = function(tdObj,value){
		return $(tdObj).text(fmtIntNum(value));
	}
    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }
	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
	}
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}

	//格式化数字带千分位
	function fmtIntNum(val){
		if(val==null||val==""){
			return "0";
		}
		var reg=/\d{1,3}(?=(\d{3})+$)/g;
		return (val + '').replace(reg, '$&,');
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
var _index = require('saleReconciliation');
_start.load(function (_common) {
	_index.init(_common);
});
