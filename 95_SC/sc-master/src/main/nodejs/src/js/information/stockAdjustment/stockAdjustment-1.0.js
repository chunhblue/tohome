require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('stockAdjustment', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
		am = null,
		reason = null,
		generalReason = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		page = 1, // 当前页数, 初始为 1
		rows = 10; // 每页显示条数, 默认为 10;

    var m = {
		toKen : null,
		use : null,
		main_box : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		// 栏位
		dep:null,
		am:null,
		pma:null,
		category:null,
		subCategory:null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		adjustmentStartDate : null,
		adjustmentEndDate : null,
		adjustmentReason : null,
		generalReason : null,
		itemId : null,
		itemName:null,
		topDepCd : null,
		depCd : null,
		// 按钮
		reset : null,
		search : null,
		print : null,
		export : null,
		barcode:null,
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
    	url_left = url_root + "/adjustmentDaily";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		_common.initCategoryAutoMatic();
    	// 首先验证当前操作人的权限是否混乱，
		if(m.use.val()=="0"){
			but_event();
		}else{
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 初始化店铺运营组织检索
		_common.initOrganization();
		// 初始化店铺检索、部门下拉
	//	initSelectValue();
		initAutoMatic();
		// 初始化检索日期
		_common.initDate(m.adjustmentStartDate,m.adjustmentEndDate);
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
    	switch(flgType) {
			case "1":
				break;
			case "2":
			   break;
			case "3":
				break;
			case "4":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
    };
	var initAutoMatic = function () {
		// 获取区域经理
		am=$("#am").myAutomatic({
			url: url_root + "/ma1000/getAMByPM",
			ePageSize:10,
			startCount:0,
		});


		generalReason = $("#generalReason").myAutomatic({
			url:url_root+"/inventoryVoucher/generalReason",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {
				$.myAutomatic.replaceParam(reason,null);

			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") !== null && thisObj.attr("k") !== "") {
					$("#adjustmentReason").prop("disabled", false);
					$("#reasonRefresh").show();
					$("#reasonRemove").show();
					$.myAutomatic.cleanSelectObj(reason);
					var strm ="&generalLevelCd="+ m.generalReason.attr('k');
					$.myAutomatic.replaceParam(reason,strm);
				}
			},
		});
		reason=$("#adjustmentReason").myAutomatic({
			url:url_root+"/inventoryVoucher/detailReason",
			ePageSize:10,
			startCount:0,
			cleanInput:function () {

			},
		})


	};
    // 画面按钮点击事件
    var but_event = function(){
		// setDisable(false);
		$("#depRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_pma);
			$.myAutomatic.cleanSelectObj(a_category);
			$.myAutomatic.cleanSelectObj(a_subCategory);
		});
		$("#pmaRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_category);
			$.myAutomatic.cleanSelectObj(a_subCategory);

		});
		$("#categoryRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_subCategory);;
		});
		$("#amRemove").on("click",function (e) {
			$.myAutomatic.cleanSelectObj(am);
		});
		$("#reasonRemove").on("click",function (e) {
			$.myAutomatic.cleanSelectObj(reason);
		});
    	    	// 重置按钮点击事件
    	m.reset.on("click",function(){
			m.adjustmentStartDate.val("");
			m.adjustmentEndDate.val("");
			$.myAutomatic.cleanSelectObj(generalReason);
			// m.itemId.val("");
			m.itemName.val("");
			m.topDepCd.val("");
			m.depCd.val("");
			// clearOptions("depCd");
			$("#daily_table").find("tr:not(:first)").remove();
			$("#amRemove").click();
			$("#regionRemove").click();
			$("#depRemove").click();
			$("#adjustmentStartDate").css("border-color","#CCC");
			$("#adjustmentEndDate").css("border-color","#CCC");
			page=1;
			// 清除分页
			_common.resetPaging();
			$("#daily_table").find("tr:not(:first)").remove();
    	});
    	// 检索按钮点击事件
		m.search.click(function () {
			if(verifySearch()) {
				_common.loadPaging(1,1,1,10);
				page=1;
				setParamJson();
				getData(page,rows);
			}
		})
    	// 打印按钮事件
		m.print.on("click",function () {
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/print?" + paramGrid;
				window.open(encodeURI(url), "excelprint", "width=1400,height=600,scrollbars=yes");
			}
		});
    	// 导出按钮事件
		m.export.on("click",function () {
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/export?" + paramGrid;
				window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
			}
		});
    }

	/**
	 * 分页获取数据
	 * @param page 当前页数
	 * @param rows 每页显示条数
	 */
	var getData = function (page,rows) {
		let record = m.searchJson.val();
		$.myAjaxs({
			url: url_left + "/getData",
			async: true,
			cache: false,
			type: "post",
			data: 'SearchJson=' + record,
			dataType: "json",
			success: function (result) {
				var trList = $("#daily_table  tr:not(:first)");
				trList.remove();
				if (result.success) {
					let dataList = result.o.data;
					// 总页数
					let totalPage = result.o.totalPage;
					// 总条数
					let count = result.o.count;
					for (let i = 0; i < dataList.length; i++) {
						var item = dataList[i];
						let tempTrHtml = '<tr style="text-align: center;">' +
							'<td title="' + isEmpty(item.storeCd) + '" style="text-align: left">' + isEmpty(item.storeCd) + '</td>' +
							'<td title="' + isEmpty(item.storeName) + '" style="text-align: left">' + isEmpty(item.storeName) + '</td>' +
							'<td title="' + fmtIntDate(item.adjustmentDate) + '" style="text-align: center">' + fmtIntDate(item.adjustmentDate) + '</td>' +
							'<td title="' + isEmpty(item.depCd) + isEmpty(item.depName) + '" style="text-align: left">' + isEmpty(item.depCd) + "&nbsp;&nbsp;" + isEmpty(item.depName) + '</td>' +
							'<td title="' + isEmpty(item.pmaCd) + isEmpty(item.pmaName) + '" style="text-align: left">' + isEmpty(item.pmaCd) + "&nbsp;&nbsp;" + isEmpty(item.pmaName) + '</td>' +
							'<td title="' + isEmpty(item.categoryCd) + isEmpty(item.categoryName) + '" style="text-align: left">' + isEmpty(item.categoryCd) + "&nbsp;&nbsp;" + isEmpty(item.categoryName) + '</td>' +
							'<td title="' + isEmpty(item.subCategoryCd) + isEmpty(item.subCategoryName) + '" style="text-align: left">' + isEmpty(item.subCategoryCd) + "&nbsp;&nbsp;" + isEmpty(item.subCategoryName) + '</td>' +
							'<td title="' + isEmpty(item.articleId) + '" style="text-align: right">' + isEmpty(item.articleId) + '</td>' +
							'<td title="' + isEmpty(item.articleName) + '" style="text-align: left">' + isEmpty(item.articleName) + '</td>' +
							'<td title="' + isEmpty(item.barcode) + '" style="text-align: right">' + isEmpty(item.barcode) + '</td>' +
							'<td title="' + isEmpty(item.uom) + '" style="text-align:left;">' + isEmpty(item.uom) + '</td>' +
							'<td title="' + toThousands(item.adjustmentQty) + '" style="text-align:right;">' + toThousands(item.adjustmentQty) + '</td>' +
							'<td title="' + isEmpty(item.adjustReasonText) + '" style="text-align: left">' + isEmpty(item.adjustReasonText) + '</td>' +
							'<td title="' + isEmpty(item.ofc) + '" style="text-align: left">' + isEmpty(item.ofc) + '</td>' +
							'<td title="' + isEmpty(item.ofcName) + '" style="text-align: left">' + isEmpty(item.ofcName) + '</td>' +
							'</tr>';
						$("#daily_table").append(tempTrHtml);
					}
					var totalQty= "<tr style='text-align:right' id='total_qty'>"
						+"<td></td>" +
						"<td></td>" +
						"<td></td>" +
						"<td></td>" +
						"<td></td>" +
						"<td style='text-align:right'>Total:</td>" +
						"<td style='text-align:right'>total Item</td>" +
						"<td style='text-align:right'>"+toThousands(result.o.totalItemSKU1)+"</td>"+
						"<td></td>"+
						"<td></td>" +
						"<td style='text-align:right'>total qty </td>"+
						"<td style='text-align:right'>"+toThousands(result.o.ItemQty)+"</td>"+
						"<td></td>" +"<td></td>" +
						"<td></td>" +
						"</tr>";
					if (totalPage===page){
						$("#daily_table").append(totalQty)
					}
					// 加载分页条数据
					_common.loadPaging(totalPage,count,page,rows);
				}
				// 激活 分页按钮点击
				but_paging();
			},
			error: function (e) {

			}
		});
	}

	// 分页按钮事件
	var but_paging = function () {
		$('.pagination li').on('click',function () {
			page = $($(this).context).val();
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				// 分页获取数据
				getData(page,rows);
			}
		});
	}
	// 验证检索项是否合法
    var verifySearch = function(){
		if(m.adjustmentStartDate.val()==""||m.adjustmentStartDate.val()==null){
			_common.prompt("Please enter a Date!",5,"error"); // 开始日期不可以为空
			$("#adjustmentStartDate").focus();
			$("#adjustmentStartDate").css("border-color","red");
			return false;
		}else {
			$("#adjustmentStartDate").css("border-color","#CCC");
		}
		if(m.adjustmentEndDate.val()==""||m.adjustmentEndDate.val()==null){
			_common.prompt("Please enter a Date!",5,"error"); // 结束日期不可以为空
			$("#adjustmentEndDate").focus();
			$("#adjustmentEndDate").css("border-color","red");
			return false;
		}else {
			$("#adjustmentEndDate").css("border-color","#CCC");
		}
		if(m.adjustmentStartDate.val()!=""&&m.adjustmentEndDate.val()!=""){
			var intStartDate = fmtStringDate(m.adjustmentStartDate.val());
			var intEndDate = fmtStringDate(m.adjustmentEndDate.val());
			if(intStartDate>intEndDate){
				// _common.prompt("[调整开始日]不能大于[调整结束日]",5,"info");
				_common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
				return false;
			}
			var _StartDate = new Date(fmtDate($("#adjustmentStartDate").val())).getTime();
			var _EndDate = new Date(fmtDate($("#adjustmentEndDate").val())).getTime();
			var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
			if(difValue >62){
				_common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
				$("#adjustmentEndDate").focus();
				return false;
			}
		}
    	return true;
    }
    // 拼接检索参数
    var setParamJson = function(){
    	// 日期格式转换
		var _startDate = fmtStringDate(m.adjustmentStartDate.val())||null;
		var _endDate = fmtStringDate(m.adjustmentEndDate.val())||null;
 		// 创建请求字符串
		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			adjustmentStartDate: _startDate,
			adjustmentEndDate: _endDate,
			// generalReason:m.generalReason.attr("k"),
			adjustReason:m.adjustmentReason.attr("k"),
			barcode:m.barcode.val().trim(),
			articleName:m.itemName.val().trim(),
			am: $("#am").attr("k"),
			depCd:$("#dep").attr("k"),
			pmaCd:$("#pma").attr("k"),
			subCategoryCd:$("#subCategory").attr("k"),
			categoryCd:$("#category").attr("k"),
			page: page,
			rows: rows,
 		};
 		m.searchJson.val(JSON.stringify(searchJsonStr));
    }

	// 格式化百分比
	function fmtRate(rate){
		if(rate==null||rate==0){return "0%";}
		rate = Number(accMul(rate,100)).toFixed(2);
		return rate+"%";
	}
	var isEmpty = function (str) {
		if (str == null || str == undefined || str == '') {
			return '';
		}
		return str;
	};
	// 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
	function fmtIntDate(date){
		if(date==null||date.length!=8){return "";}
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


	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}

	// 运算
	function accAdd(arg1,arg2){
		var r1,r2,m;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return (accMul(arg1,m)+accMul(arg2,m))/m;
	}
	function accSub(arg1,arg2){
		var r1,r2,m,n;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return accMul(arg1,m)-accMul(arg2,m);
	}
	function accMul(arg1,arg2){
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}
	function accDiv(arg1,arg2){
		var t1=0,t2=0,r1,r2;
		try{t1=arg1.toString().split(".")[1].length}catch(e){}
		try{t2=arg2.toString().split(".")[1].length}catch(e){}
		with(Math){
			r1=Number(arg1.toString().replace(".",""))
			r2=Number(arg2.toString().replace(".",""))
			return (r1/r2)*pow(10,t2-t1);
		}
	}

	// 判断是否为空
	function isNotNull(value) {
		if(value==null||$.trim(value)==""){
			return false;
		}
		return true;
	}

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('stockAdjustment');
_start.load(function (_common) {
	_index.init(_common);
});
