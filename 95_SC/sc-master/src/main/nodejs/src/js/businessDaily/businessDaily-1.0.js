require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("jqprint");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('businessDaily', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
		reThousands = null,
		toThousands = null,
		getThousands = null,
		common=null;
    var m = {
		toKen : null,
		use : null,
		up : null,
		date : null,
		search : null,
		reset : null,
		bsDate : null,
		storeName : null,
		businessDate : null,//营业日期
		main_box : null,//检索按钮
		printHtml : null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		dep:null,
		pma:null,
		category:null,
		export : null,
    }
	// 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
		}
    }
    function init(_common) {
    	createJqueryObj();
		var businessDate = m.businessDate.val();
		if(businessDate!=null&&businessDate!='') {
			m.date.val(businessDate);
		}
    	url_left=_common.config.surl+"/businessDaily";
		url_root = _common.config.surl;
		systemPath =_common.config.surl;
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;

		//初始化事件
		but_event();
		//初始化营业日报
		initBusinessDaily();
		initAutoMatic();
		// // 初始化店铺运营组织检索
		// _common.initOrganization();
		// 初始化店铺名称下拉
		_common.initStoreform();
		//权限验证
		isButPermission();
    }
	var initAutoMatic=function(){
		$("#pma").attr("disabled", true);
		$("#category").attr("disabled", true);
		$("#subCategory").attr("disabled", true);
		$("#pmaRefresh").attr("disabled",true).css("pointer-events","none");
		$("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
		$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
		a_dep = $("#dep").myAutomatic({
			url: url_root + "/deps",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_pma);
				$.myAutomatic.cleanSelectObj(a_category);
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#pma").attr("disabled", true);
				$("#category").attr("disabled", true);
				$("#subCategory").attr("disabled", true);
				$("#pmaRefresh").attr("disabled",true).css("pointer-events","none");
				$("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
				$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
				if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999") {
					$("#pma").attr("disabled", false);
					$("#pmaRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = thisObj.attr("k");
				var str = "&depId=" + dinput;
				$.myAutomatic.replaceParam(a_pma, str);//赋值
			}
		});
		a_pma = $("#pma").myAutomatic({
			url: url_root + "/pmas",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_category);
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#category").attr("disabled", true);
				$("#subCategory").attr("disabled", true);
				$("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
				$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#category").attr("disabled", false);
					$("#categoryRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = $("#dep").attr("k");
				var pinput = thisObj.attr("k");
				var str = "&depId=" + dinput+"&pmaId=" + pinput;
				$.myAutomatic.replaceParam(a_category, str);//赋值
			}
		});
		a_category = $("#category").myAutomatic({
			url: url_root + "/categorys",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#subCategory").attr("disabled", true);
				$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#subCategory").attr("disabled", false);
					$("#subCategoryRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = $("#dep").attr("k");
				var pinput = $("#pma").attr("k");
				var cinput = thisObj.attr("k");
				var str = "&depId=" + dinput+"&pmaId=" + pinput+"&categoryId=" + cinput;
				$.myAutomatic.replaceParam(a_subCategory, str);//赋值
			}
		});
		a_subCategory = $("#subCategory").myAutomatic({
			url: url_root + "/subCategorys",
			ePageSize: 10,
			startCount: 0,
		});
		$.myAutomatic.cleanSelectObj(a_dep);
		$.myAutomatic.cleanSelectObj(a_pma);
		$.myAutomatic.cleanSelectObj(a_category);
		$.myAutomatic.cleanSelectObj(a_subCategory);
	}

	var but_event = function () {
		// 导出按钮事件
		m.export.on("click",function () {
			let store = m.aStore.attr('k');
			// 判断是否选择店铺
			if(store==null || store==''){
				_common.prompt("Please select a store first!",5,"info");
				$("#aStore").focus();
				$("#aStore").css("border-color","red");
				return;
			}else {
				$("#aStore").css("border-color","#CCC");
			}
			let obj = {
				'storeCd': store,
				'storeName': m.aStore.attr('v'),
				'date': m.date.val(),
				'businessDate': formatDate(m.date.val())
			}
			// 拼接检索参数
			let paramGrid = "searchJson="+JSON.stringify(obj);
			let url = url_left + "/export?" + paramGrid;
			window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
		});

		m.search.on('click',function () {
			var store = m.aStore.attr('k');
			// 判断是否选择店铺
			if(store==null || store==''){
				_common.prompt("Please select a store first!",5,"info");
				$("#aStore").focus();
				$("#aStore").css("border-color","red");
				return;
			}else {
				$("#aStore").css("border-color","#CCC");
			}
			var dep = m.dep.val();
			//delte by lyz 20200623 方便计算改为不能查询department
			// 判断是否选择Top Department
			/*if(dep==null || dep==''){
				_common.prompt("Please select Top Department!",5,"info");
				$("#dep").focus();
				$("#dep").css("border-color","red");
				return;
			}else {
				$("#dep").css("border-color","#CCC");
			}
			var pma = m.pma.val();*/
			// 判断是否选择Department
			/*if(dep != 'All Top Department'){
				if(pma==null || pma==''){
					_common.prompt("Please select Department!",5,"info");
					$("#pma").focus();
					$("#pma").css("border-color","red");
					return;
				}else {
					$("#pma").css("border-color","#CCC");
				}
			}*/
			// 判断是否选择Category
			/*var category = m.category.val();
			if(dep != 'All Top Department' && pma != 'All Department'){
				if(category==null || category==''){
					_common.prompt("Please select Category!",5,"info");
					$("#category").focus();
					$("#category").css("border-color","red");
					return;
				}else {
					$("#category").css("border-color","#CCC");
				}
			}*/
			var storeName = m.aStore.val();
			// 初始化营业日报
			initBusinessDaily();
			$.myAjaxs({
				url:url_left+"/getData",
				async:true,
				cache:false,
				type :"post",
				data :{
					storeCd:store,
					businessDate:formatDate(m.date.val())
				},
				dataType:"json",
				success:function(result){
					if(result!=null){
						m.storeName.text("Store："+storeName);
						m.bsDate.text(m.date.val());
						//打印日期
						let printDate = new Date(result.data.nowDate).Format("dd/MM/yyyy hh:mm:ss");
						$("#printDate").text("Print Date："+printDate);
						//销售日报
						var saleData = result.data.saleData;
						console.log(result.data.lastMonthSalesAmount);
						console.log(result.data.countCustomer);
						$("#grossSaleAmount").text(toThousands(saleData.grossSaleAmount));//销售金额
						$("#discountAmount").text(toThousands(saleData.discountAmount));//折扣金额
						$("#saleAmount").text(toThousands(saleData.saleAmount));//实际销售额
						$("#spillAmount").text(toThousands(saleData.spillAmount));//溢收金额
						$("#overAmount").text(toThousands(saleData.overAmount));//舍去金额
						$("#refundAmount").text(toThousands(saleData.refundAmount));//退款金额
						$("#serviceAmount").text(toThousands(saleData.serviceAmount));//服务费
						$("#chargeAmount").text(toThousands(saleData.chargeAmount));//充值费用
						$("#chargeRefundAmount").text(toThousands(saleData.chargeRefundAmount));//充值退款费用
						$("#countCustomer").text(toThousands(result.data.countCustomer));//统计该天早班顾客数目
						//统计过去28天的平均金额
						$("#Average4WeekSalesAmount").text(toThousands(((result.data.lastMonthSalesAmount/28).toFixed(2))));

						//支付方式
						var payAmt = result.data.payAmt;
						$("#payAmt0").text(toThousands(payAmt.payAmt0));
						$("#payAmt1").text(toThousands(payAmt.payAmt1));
						$("#payAmt2").text(toThousands(payAmt.payAmt2));
						$("#payAmt3").text(toThousands(payAmt.payAmt3));
						$("#payAmt4").text(toThousands(payAmt.payAmt4));
						$("#payAmt5").text(toThousands(payAmt.payAmt5));
						$("#payAmt").text(toThousands(payAmt.payAmt));


						//经费
						var expenditureAmt = result.data.expenditureAmt;
						var totalExpenseAmt = 0;
						if(expenditureAmt!=null&&expenditureAmt.length>0){
							for (let i = 0; i < expenditureAmt.length; i++) {
								if(expenditureAmt.length>6){
									//设置经费名称 金额
									$("#exponse_total").before("<tr class='exponse'>" +
										"<td>"+expenditureAmt[i].itemName+"</td>" +
										"<td>"+toThousands(expenditureAmt[i].expenseAmt)+"</td></tr>");
								}else{
									//设置经费名称
									$(".exponse").eq(i).find("td").eq(0).text(expenditureAmt[i].itemName);
									//设置经费金额
									$(".exponse").eq(i).find("td").eq(1).text(toThousands(expenditureAmt[i].expenseAmt));
								}
								totalExpenseAmt+=expenditureAmt[i].expenseAmt;
							}
							$("#exponse_total").find("td").eq(1).text(toThousands(totalExpenseAmt));
						}
						//银行缴款
						var bankDeposit = result.data.bankDeposit;
							//昨日留存金
							$("#retentionAmount").text(toThousands(bankDeposit.retentionAmount));
							//今日现金小计
							$("#cashAmount").text(toThousands(bankDeposit.cashAmount));
							//应收金额
							$("#receivablesAmount").text(toThousands(bankDeposit.receivablesAmount));
							//合计
							$("#bankDepositTotal").text(toThousands(bankDeposit.retentionAmount+bankDeposit.cashAmount/*+bankDeposit.receivablesAmount*/));

						//会员积分

						//会员充值方式

						//分类销售
						var saleAmountByPma = result.data.saleAmountByPma;
						if(saleAmountByPma!=null&&saleAmountByPma.length>0){
							var _index = 0,total = 0,saleAmount = 0,saleAmountTotal = 0;
							$.each(saleAmountByPma,function(n,obj) {
								$("#saleGroup_"+(n+1)).find("td").eq(0).text(obj.categoryName);
								$("#saleGroup_"+(n+1)).find("td").eq(1).text(toThousands(obj.saleAmount));
								_index = n+1;
								saleAmountTotal +=obj.saleAmount;
								saleAmount +=obj.saleAmount;
								if(_index==20){
									$("#saleGroupTotal_1").text(toThousands(saleAmount));
									saleAmount = 0;
								}else if(_index==40){
									$("#saleGroupTotal_2").text(toThousands(saleAmount));
									saleAmount = 0;
								}else if(_index==60){
									$("#saleGroupTotal_3").text(toThousands(saleAmount));
									saleAmount = 0;
								}else if(_index==80){
									$("#saleGroupTotal_4").text(toThousands(saleAmount));
									saleAmount = 0;
								}
								if(saleAmountByPma.length==_index&&_index>0&&_index<21){
									$("#saleGroupTotal_1").text(toThousands(saleAmount));
									saleAmount = 0;
								}else if(saleAmountByPma.length==_index&&_index>20&&_index<41){
									$("#saleGroupTotal_2").text(toThousands(saleAmount));
									saleAmount = 0;
								}else if(saleAmountByPma.length==_index&&_index>40&&_index<61){
									$("#saleGroupTotal_3").text(toThousands(saleAmount));
									saleAmount = 0;
								}else if(saleAmountByPma.length==_index&&_index>60&&_index<81){
									$("#saleGroupTotal_4").text(toThousands(saleAmount));
									saleAmount = 0;
								}
							});
							$("#saleAmountTotal").text(toThousands(saleAmountTotal));
						}
					}
				},
				error : function(e){
					_common.prompt("Failed to load data!",5,"error");/*营业日报加载失败*/
				}
			});
		});

		m.reset.on('click',function () {
			$("#storeRemove").click();
			// m.dep.val("");
			// m.pma.val("");
			// m.pma.prop("disabled",true);
			// m.category.val("");
			// m.category.prop("disabled",true);
			// m.subCategory.val("");
			// m.subCategory.prop('disabled',true)
			m.date.val('');
			$("#aStore").css("border-color","#CCC");
			// $("#dep").css("border-color","#CCC");
			// $("#pma").css("border-color","#CCC");
			// $("#category").css("border-color","#CCC");
			//默认为业务日期
			m.bsDate.text(m.businessDate.val());
			// 初始化营业日报
			initBusinessDaily();
		});
		m.printHtml.click(function () {
			var store = m.aStore.attr('k');
			//判断是否选择店铺
			if(store!=null && store!=''){
				//调用打印
				$("#print").print({
					globalStyles:true,
					mediaPrint:false,
					stylesheet:"",
					noPrintSelector:".no-print",
					iframe:false,
					append:null,
					prepend:"Print",
					deferred:
					$.Deferred()
				});
			}else{
				_common.prompt("Please select a store first!",5,"info");
			}
		});

		m.date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true,
		});

		m.date.focus(function(){
			if ($("#checkFlg").val() === "1") {
				$("#date").datetimepicker('setStartDate', new Date(new Date(fmtDate(formatDate(m.date.val()))).getTime() - 24 * 60 * 60 * 1000));
			} else {
				$("#date").datetimepicker('setStartDate', null);
			}
		});
	}

	//初始化销售日报
	var initBusinessDaily = function () {
    	$("#storeName").text("Store：");
		//销售日报
		$("#grossSaleAmount").text("0");
		$("#discountAmount").text("0");
		$("#saleAmount").text("0");
		$("#spillAmount").text("0");
		$("#serviceAmount").text("0");
		$("#chargeAmount").text("0");
		$("#chargeRefundAmount").text("0");

		//支付方式
		$("#payAmt0").text("0");
		$("#payAmt1").text("0");
		$("#payAmt2").text("0");
		$("#payAmt3").text("0");
		$("#payAmt4").text("0");
		$("#payAmt5").text("0");
		$("#payAmt").text("0");

		//经费
		$(".exponse").remove();
		for (let i = 0; i < 6; i++) {
			$("#exponse_total").before("<tr class='exponse'><td></td><td></td></tr>")
		}
		$("#exponse_total").find("td").eq(1).text("");

		//银行缴款
		$("#retentionAmount").text("0");
		$("#cashAmount").text("0");
		$("#receivablesAmount").text("0");
		$("#bankDepositTotal").text("0");

		//分类销售
		for (let i = 1; i < 5; i++) {
			$("#categoryTable_"+i).find("td").text("");
		}
		$(".categoryTable_total").text("Total");
		$("#saleAmountTotal").text("");
	}

	// 按钮权限验证
	var isButPermission = function () {
		var printBut = $("#printBut").val();
		var exportBut = $("#exportBut").val();
		if (Number(printBut) != 1) {
			$("#printHtml").remove();
		}
		if (Number(exportBut) != 1) {
			$("#export").remove();
		}
	}

	//number格式化
	function fmtIntNum(val){
		if(val==null||val==""){
			return "0";
		}
		// return val;
		var reg=/\d{1,3}(?=(\d{3})+$)/g;
		return (val + '').replace(reg, '$&,');
	}

	// 06/03/2020 -> 20200306
	var formatDate = function (dateStr) {
		dateStr = dateStr.replace(/\//g,'');
		var day = dateStr.substring(0,2);
		var month = dateStr.substring(2,4);
		var year = dateStr.substring(4,8);
		return year+month+day;
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	function fmtDate(date){
		var res = "";
		res = date.substring(0,4)+"/"+date.substring(4,6)+"/"+date.substring(6,8);
		res = res + " 06:00:00";
		return res;
	}


    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('businessDaily');
_start.load(function (_common) {
	_index.init(_common);
});
