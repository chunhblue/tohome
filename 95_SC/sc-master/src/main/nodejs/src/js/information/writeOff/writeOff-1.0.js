require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('writeOff', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
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
		itemName:null,
		writeOffStartDate : null,
		writeOffEndDate : null,
		reason : null,
		itemId : null,
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
    	url_left = url_root + "/writeOff";
		_common.initCategoryAutoMatic();
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
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
		initAutoMatic();
		// 初始化店铺检索、部门下拉
		//initSelectValue();
		// 初始化检索日期
		_common.initDate(m.writeOffStartDate,m.writeOffEndDate);
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
    }
	var initAutoMatic = function () {
		// 获取区域经理
		am=$("#am").myAutomatic({
			url: url_root + "/ma1000/getAMByPM",
			ePageSize:10,
			startCount:0,
		});
		reason=$("#reason").myAutomatic({
			url:url_root+"/cm9010/getWriteOffReasonValue",
			ePageSize:10,
			startCount:0,
		})
	}

    // 画面按钮点击事件
    var but_event = function(){
		// $("#am").prop("disabled",true);
    	// $("#dep").prop("disabled",true);
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
			$.myAutomatic.cleanSelectObj(a_subCategory);
		});
		$("#reasonRemove").on("click",function (e) {
			$.myAutomatic.cleanSelectObj(reason);
		});

    	// 重置按钮点击事件
    	m.reset.on("click",function(){
			m.writeOffStartDate.val("");
			m.writeOffEndDate.val("");
			m.itemId.val("");
			m.topDepCd.val("");
			m.depCd.val("");
			m.itemName.val("");
			// clearOptions("depCd");
			$("#daily_table").find("tr:not(:first)").remove();
			$("#reasonRemove").click();
			$("#amRemove").click();
			$("#regionRemove").click();
			$("#depRemove").click();
			$("#writeOffStartDate").css("border-color","#CCC");
			$("#writeOffEndDate").css("border-color","#CCC");
			page=1;
			// 清除分页
			_common.resetPaging();
    	});

    	// 检索按钮点击事件
		m.search.click(function () {
			if(verifySearch()) {
				_common.loadPaging(1,1,1,10);
				page=1;
				setParamJson();
				getData(page,rows);
				total_detail(m.searchJson.val());
			}
		})

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

		// // 查询类型监听事件
		// $('input:radio[name="queryType"]').on('change',function(){
		// 	var checkValue = $('input:radio[name="queryType"]:checked').val();
		// 	if(checkValue == '1'){
		// 		setDisable(true);
		// 	}else{
		// 		setDisable(false);
		// 	}
		// });
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
							'<td title="' + isEmpty(item.storeCd) + '" style="text-align: right">' + isEmpty(item.storeCd) + '</td>' +
							'<td title="' + isEmpty(item.storeName) + '" style="text-align: left">' + isEmpty(item.storeName) + '</td>' +
							'<td title="' + isEmpty(item.writeOffDate) + '" style="text-align: center">' + fmtIntDate(item.writeOffDate) + '</td>' +
							'<td title="' + isEmpty(item.depName) + isEmpty(item.depCd) + '" style="text-align: left">' + isEmpty(item.depCd) + "&nbsp;&nbsp;" + isEmpty(item.depName) + '</td>' +
							'<td title="' + isEmpty(item.pmaName) + isEmpty(item.pmaCd) + '" style="text-align: left">' + isEmpty(item.pmaCd) + "&nbsp;&nbsp;" + isEmpty(item.pmaName) + '</td>' +
							'<td title="' + isEmpty(item.categoryName) + isEmpty(item.categoryCd) + '" style="text-align: left">' + isEmpty(item.categoryCd) + "&nbsp;&nbsp;" + isEmpty(item.categoryName) + '</td>' +
							'<td title="' + isEmpty(item.subCategoryCd) + isEmpty(item.subCategoryName) + '" style="text-align: left">' + isEmpty(item.subCategoryCd) + "&nbsp;&nbsp;" + isEmpty(item.subCategoryName) + '</td>' +
							'<td title="' + isEmpty(item.articleId) + '" style="text-align: right">' + isEmpty(item.articleId) + '</td>' +
							'<td title="' + isEmpty(item.articleName) + '" style="text-align: left">' + isEmpty(item.articleName) + '</td>' +
							'<td title="' + isEmpty(item.barcode) + '" style="text-align: right">' + isEmpty(item.barcode) + '</td>' +
							'<td title="' + isEmpty(item.uom) + '" style="text-align: left">' + isEmpty(item.uom) + '</td>' +
							'<td title="' + toThousands(item.writeOffQty) + '" style="text-align:right;">' + toThousands(item.writeOffQty) + '</td>' +
							// sale_qty
							'<td title="' + toThousands(item.saleQty) + '" style="text-align:right;">' + toThousands(item.saleQty) + '</td>' +
							"<td style='text-align:left;'>" + isEmpty(item.adjustReason) + "</td>" +
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
							"<td style='text-align:right'>"+toThousands($("#totalDoc").val())+"</td>"+
							"<td></td>"+
							"<td></td>"+
							"<td style='text-align:right'>total qty </td>"+
							"<td style='text-align:right'>"+toThousands($("#toTotalQty").val())+"</td>"+
							"<td style='text-align:right'>"+toThousands($("#itemSaleQty").val())+"</td>" +
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

	var total_detail = function (data) {
	    $.myAjaxs({
	        url: url_left+"/getOffQty",
	        async: false,
	        cache: false,
	        type: "post",
	        data: 'searchJson=' + data,
	        dataType: "json",
	        success: function (result) {
	            if(result.success){
	                $("#toTotalQty").val(result.o.writeOffQty);
	                $("#itemSaleQty").val(result.o.saleQty);
	                $("#totalDoc").val(result.o.records);
	            }else {
	                $("#toTotalQty").val('');
	                $("#itemSaleQty").val('');
	                $("#totalDoc").val('');
	            }
	        },
	        error:function () {
	            $("#toTotalQty").val('');
	            $("#totalDoc").val('');
	        }
	    })
	};

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

    // 禁用/可用
	// var setDisable = function(flag){
	// 	m.itemId.val("");
	// 	m.topDepCd.val("");
	// 	m.depCd.val("");
	// 	// clearOptions("depCd");
	// 	$("#depRemove").click();
	// 	m.itemId.prop("disabled",flag);
	// 	// m.topDepCd.prop("disabled",!flag);
	// 	m.dep.prop("disabled",!flag);
	// 	// m.pma.prop("disabled",flag);
	// 	m.pma.prop("disabled",true);
	// 	// m.category.prop("disabled",flag);
	// 	m.category.prop("disabled",true);
	// 	// m.subCategory.prop("disabled",flag);
	// 	m.subCategory.prop("disabled",true);
    //     m.am.prop("disabled",!flag);
	// 	// m.depCd.prop("disabled",!flag);
	// 	if(!flag){
	// 		$("#depRefresh").hide();
	// 		$("#depRemove").hide();
	// 		$("#amRefresh").hide();
	// 		$("#amRemove").hide();
	// 	}else {
	// 		$("#depRefresh").show();
	// 		$("#depRemove").show();
	// 		$("#amRefresh").show();
	// 		$("#amRemove").show();
	// 	}
	// };

    // 验证检索项是否合法
    var verifySearch = function(){
		if(m.writeOffStartDate.val()==""||m.writeOffStartDate.val()==null){
			_common.prompt("Please enter a Date!",5,"error"); // 开始日期不可以为空
			$("#writeOffStartDate").focus();
			$("#writeOffStartDate").css("border-color","red");
			return false;
		}else if(_common.judgeValidDate(m.writeOffStartDate.val())){
			_common.prompt("Please enter a valid date!",3,"info");
			$("#writeOffStartDate").focus();
			return false;
		}else {
			$("#writeOffStartDate").css("border-color","#CCC");
		}
		if(m.writeOffEndDate.val()==""||m.writeOffEndDate.val()==null){
			_common.prompt("Please enter a Date!",5,"error"); // 结束日期不可以为空
			$("#writeOffEndDate").focus();
			$("#writeOffEndDate").css("border-color","red");
			return false;
		}else if(_common.judgeValidDate(m.writeOffEndDate.val())){
			_common.prompt("Please enter a valid date!",3,"info");
			$("#writeOffEndDate").focus();
			return false;
		}else {
			$("#writeOffEndDate").css("border-color","#CCC");
		}

		if(m.writeOffStartDate.val()!=""&&m.writeOffEndDate.val()!=""){
			var intStartDate = fmtStringDate(m.writeOffStartDate.val());
			var intEndDate = fmtStringDate(m.writeOffEndDate.val());
			if(intStartDate>intEndDate){
				// _common.prompt("[报废开始日]不能大于[报废结束日]",5,"info");
				$("#writeOffEndDate").focus();
				_common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
				return false;
			}
			var _StartDate = new Date(fmtDate($("#writeOffStartDate").val())).getTime();
			var _EndDate = new Date(fmtDate($("#writeOffEndDate").val())).getTime();
			var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
			if(difValue >62){
				_common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
				$("#writeOffEndDate").focus();
				return false;
			}
		}
      //2021/1/30
		// var temp = m.itemId.val().trim();
		// if(temp!=null && $.trim(temp)!=''){
		// 	var reg = /^[0-9]*$/;
		// 	if(!reg.test(temp)){
		// 		m.itemId.focus();
		// 		_common.prompt("Item Code must be pure Numbers!",5,"info");/*商品编号必须是纯数字*/
		// 		return false;
		// 	}
		// }
    	return true;
    }
	var isEmpty = function (str) {
		if (str == null || str == undefined || str == '') {
			return '';
		}
		return str;
	};
    // 拼接检索参数
    var setParamJson = function(){
    	// 日期格式转换
		var _startDate = fmtStringDate(m.writeOffStartDate.val())||null;
		var _endDate = fmtStringDate(m.writeOffEndDate.val())||null;
		// 按类型取值

	// //	var checkValue = $('input:radio[name="queryType"]:checked').val();
	// 	// if(checkValue == '1'){
	// 	// 	_depCd = m.dep.attr("k");
	// 	// 	_pmaCd = m.pma.attr("k");
	// 	// 	_subCategory=m.subCategory.attr("k");
	// 	// 	_category=m.category.attr("k")
	// 	// }else{
	// 	var    _articleId = m.itemId.val().trim();
	// 	// }
 		// 创建请求字符串
 		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			writeOffStartDate: _startDate,
			writeOffEndDate: _endDate,
			// adjustReason:m.reason.val(),
			adjustReason:m.reason.attr("k"),
			barcode:m.barcode.val().trim(),
			depCd:m.dep.attr("k"),
			am: $("#am").attr("k"),
			pmaCd:$("#pma").attr("k"),
			subCategoryCd:$("#subCategory").attr("k"),
			categoryCd:$("#category").attr("k"),
			articleName:m.itemName.val().trim(),
			// articleId:m.itemName.val().trim(),
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

	// 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
	function fmtIntDate(date){
		if(date==null||date.length!=8){return "";}
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
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

	// 清空下拉
	function clearOptions(selectId) {
		var selectObj = $("#" + selectId);
		selectObj.find("option:not(:first)").remove();
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
var _index = require('writeOff');
_start.load(function (_common) {
	_index.init(_common);
});
