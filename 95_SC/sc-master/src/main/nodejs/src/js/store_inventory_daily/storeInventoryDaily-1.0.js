require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('storeInventoryDaily', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		aStore=null,
		page = 1, // 当前页数, 初始为 1
		rows = 10; // 每页显示条数, 默认为 10
    var m = {
		toKen : null,
		use : null,
		main_box : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		// 栏位
		aStore : null,
		writeOffStartDate : null,
		writeOffEndDate : null,
		itemId : null,
		itemName : null,
		am : null,
		dep : null,
		pma : null,
		category : null,
		subCategory : null,
		// 按钮
		reset : null,
		search : null,
		print : null,
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
		url_root = _common.config.surl;
    	url_left = url_root + "/storeInventoryDaily";
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
		$('input:radio[name="queryType"]').change();
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		initAutoMatic();
		// // 初始化店铺运营组织检索
		// _common.initOrganization();
		// 初始化店铺名称下拉
		_common.initStoreform();
		// 初始化大中小分类
		_common.initCategoryAutoMatic();
		// 初始化检索日期
		m.writeOffStartDate.val(fmtIntDate($('#businessDate').val()))
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

	//初始化下拉
	var initAutoMatic = function () {
		// 获取区域经理
		am = $("#am").myAutomatic({
			url: url_root + "/ma1000/getAMByPM",
			ePageSize: 10,
			startCount: 0,
		});
	}

	/**
	 * 分页获取数据
	 * @param page 当前页数
	 * @param rows 每页显示条数
	 */
	var getData = function (page,rows) {
		// 查询数据
		$.myAjaxs({
			url:url_left + "/getData",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+m.searchJson.val()+"&page="+page+"&rows="+rows,
			dataType:"json",
			success:function(result){
				if(result.success){
					let data = result.o.data;
					getTrVal(data);
					// 总页数
					let totalPage = result.o.totalPage;
					// 总条数
					let count = result.o.count;
					// 加载分页条数据
					_common.loadPaging(totalPage,count,page,rows);
				}else{
					$("#daily_table  tr:not(:first)").remove();
					_common.prompt(result.msg,5,"info");
				}
				// 激活 分页按钮点击
				but_paging();
			},
			error : function(e){
				_common.prompt("Failed to query inventory data!",5,"info");/*查询库存数据失败*/
			}
		});
	};
    
    // 画面按钮点击事件
    var but_event = function(){
    	// 日期按钮
    	m.writeOffStartDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
    	m.writeOffEndDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
    	// 重置按钮点击事件
    	m.reset.on("click",function(){
			$("#storeRemove").click();
			m.writeOffStartDate.val("");
			m.writeOffEndDate.val("");
			m.itemId.val("");
			m.itemName.val("");
			$("#aStore").css("border-color","#CCC");
			$("#writeOffStartDate").css("border-color","#CCC");
			$("#writeOffEndDate").css("border-color","#CCC");
			$("#daily_table").find("tr:not(:first)").remove();
			// 清除分页条
			_common.resetPaging();
			page=1;
    	});
    	// 检索按钮点击事件
    	m.search.on("click",function(){
    		if(verifySearch()){
				_common.loadPaging(1,1,1,10);
    			page=1;
				// 拼接检索参数
				setParamJson();
				// 分页获取数据
				getData(page,rows);
    		}
    	});
    	// 打印按钮事件
		m.print.on("click",function () {
			if(verifySearch()) {
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val()+"&page="+page+"&rows="+rows;
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
		// 查询类型监听事件
		$('input:radio[name="queryType"]').on('change',function(){
			var checkValue = $('input:radio[name="queryType"]:checked').val();
			if(checkValue == '1'){
				setDisable(true);
			}else{
				setDisable(false);
			}
		});
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
	var setDisable = function(flag){
		m.itemId.val("");
		m.itemName.val("");
		$("#depRemove").click();
		m.itemId.prop("disabled",flag);
		m.itemName.prop("disabled",flag);

		let elem = !flag?"none":"auto";
		$('#dep').prop('disabled',!flag);
		$('#pma').prop('disabled',!flag);
		$('#category').prop('disabled',!flag);
		$('#subCategory').prop('disabled',!flag);
		$('#depRefresh').prop('disabled',!flag).css("pointer-events",elem);
		if(flag === true){
			$('#depRefresh').show();
			$("#depRemove").show();
			$('#pmaRefresh').show();
			$("#pmaRemove").show();
			$('#categoryRefresh').show();
			$("#categoryRemove").show();
			$('#subCategoryRefresh').show();
			$("#subCategoryRemove").show();
		}else {
			$('#depRefresh').hide();
			$("#depRemove").hide();
			$('#pmaRefresh').hide();
			$("#pmaRemove").hide();
			$('#categoryRefresh').hide();
			$("#categoryRemove").hide();
			$('#subCategoryRefresh').hide();
			$("#subCategoryRemove").hide();
		}
	};

    // 生成表格
    var getTrVal = function(data){
		if(data==null||data.length==0) {
			$("#daily_table  tr:not(:first)").remove();
			return false;
		}
		// 拼接tr
		var _str = "";
		for(var i=0; i<data.length; i++){
			var re = data[i];
			_str = _str + "<tr>" +
				"<td title='"+re.storeCd+"' style='text-align:left;'>" + re.storeCd + "</td>" +
				"<td title='"+re.storeName+"' style='text-align:left;'>" + re.storeName + "</td>" +
				"<td title='"+re.voucherDate+"' style='text-align:center;'>"  + re.voucherDate + "</td>" +
				"<td title='"+re.depName+"' style='text-align:left;'>" + re.depName + "</td>" +
				"<td title='"+re.pmaName+"' style='text-align:left;'>"+ re.pmaName +"</td>" +
				"<td title='"+re.categoryName+"' style='text-align:left;'>"+ re.categoryName +"</td>" +
				"<td title='"+re.subCategoryName+"' style='text-align:left;'>"+ re.subCategoryName +"</td>" +
				"<td title='"+re.barcode+"' style='text-align:right;'>"+ re.barcode +"</td>" +
				"<td title='"+re.articleId+"' style='text-align:right;'>" + re.articleId + "</td>" +
				"<td title='"+re.articleName+"' style='text-align:left;'>" + re.articleName + "</td>" +
				"<td title='"+re.uom+"' style='text-align:left;'>" + re.uom + "</td>" +
				"<td title='"+re.amName+"' style='text-align:left;'>" + re.amName + "</td>" +
				"<td title='"+toThousands(re.qty1)+"' style='text-align:right;'>" + toThousands(re.qty1) + "</td>" +
				"</tr>";
		}
		$("#daily_table").find("tr:not(:first)").remove();
		$("#daily_table").append(_str);
	}
    
    // 验证检索项是否合法
    var verifySearch = function(){
    	if (m.aStore.attr('k')==''||m.aStore.attr('k')==null) {
			_common.prompt("Please select a store first!",5,"error"); // 请先选择店铺
			$("#aStore").focus();
			$("#aStore").css("border-color","red");
			return false;
		} else {
			$("#aStore").css("border-color","#CCC");
		}
		if(m.writeOffStartDate.val()==""||m.writeOffStartDate.val()==null){
			_common.prompt("Please enter a Date!",5,"error"); // 开始日期不可以为空
			$("#writeOffStartDate").focus();
			$("#writeOffStartDate").css("border-color","red");
			return false;
		}else {
			$("#writeOffStartDate").css("border-color","#CCC");
		}
		/*if(m.writeOffEndDate.val()==""||m.writeOffEndDate.val()==null){
			_common.prompt("Please enter a Date!",5,"error"); // 结束日期不可以为空
			$("#writeOffEndDate").focus();
			$("#writeOffEndDate").css("border-color","red");
			return false;
		}else {
			$("#writeOffEndDate").css("border-color","#CCC");
		}*/
		/*if(m.writeOffStartDate.val()!=""&&m.writeOffEndDate.val()!=""){
			var intStartDate = fmtStringDate(m.writeOffStartDate.val());
			var intEndDate = fmtStringDate(m.writeOffEndDate.val());
			if(intStartDate>intEndDate){
				_common.prompt("The start date cannot be greater than the end date!",5,"error");/!*开始时间不能大于结束时间*!/
				$("#writeOffEndDate").focus();
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
		}*/
    	return true;
    }
    
    // 拼接检索参数
    var setParamJson = function(){
    	// 日期格式转换
		var _startDate = fmtStringDate(m.writeOffStartDate.val())||null;
		var _endDate = fmtStringDate(m.writeOffEndDate.val())||null;
		// 按类型取值
		var _articleId,
			_articleName,
			_depCd,
			_pmaCd,
			_categoryCd,
			_subCategoryCd;
		var checkValue = $('input:radio[name="queryType"]:checked').val();
		if(checkValue == '1'){
			_depCd = m.dep.attr('k');
			_pmaCd = m.pma.attr('k');
			_categoryCd = m.category.attr('k');
			_subCategoryCd = m.subCategory.attr('k');
		}else{
			_articleId = m.itemId.val().trim();
			_articleName = m.itemName.val().trim();
		}
 		// 创建请求字符串
 		var searchJsonStr ={
			'storeCd':$("#aStore").attr("k"),
			'startDate': _startDate,
			'endDate': _endDate,
			'articleId':_articleId,
			'articleName':_articleName,
			'am':m.am.attr('k'),
			'depCd':_depCd,
			'pmaCd':_pmaCd,
			'categoryCd':_categoryCd,
			'subCategoryCd':_subCategoryCd,
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

	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}
	// 判断是否为空
	function isNotNull(value) {
		if(value==null||$.trim(value)==""){
			return false;
		}
		return true;
	}

	// 请求下拉数据
	function getSelectOptions(title, url, param, fun) {
		$.myAjaxs({
			url:url_root+url,
			async:true,
			cache:false,
			type :"post",
			data : param,
			dataType:"json",
			success : fun,
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			}
		});
	}

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeInventoryDaily');
_start.load(function (_common) {
	_index.init(_common);
});
