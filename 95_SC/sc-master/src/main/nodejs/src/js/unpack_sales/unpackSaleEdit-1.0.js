require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('unpackEdit', function () {
	var self = {};
	var url_left = "",
		url_root = "",
		a_store = "",
		a_item = "",
		paramGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		tempTrObjValue = {},//临时行数据存储
		bmCodeOrItem = 0,//0 bmCode 1：item
		common=null;
	const KEY = 'UNPACKING_SALES';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson:null,
		main_box : null,
		viewSts : null,
		isHide : null,
		// 栏位
		_storeCd : null,
		_articleId : null,
		storeName : null,
		unpackId : null,
		unpackDate : null,
		articleId : null,
		barcode : null,
		unpackQty : null,
		unpackAmt : null,
		remarks : null,
		// 按钮
		refreshIcon : null,
		resetBtn : null,
		saveBtn : null,
		returnsViewBut : null
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
		url_left = url_root + "/unpackSale";
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
		// 初始化店铺
		initAutomatic();
		// 判断画面模式
		setValueByType();
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable(); // 列表初始化
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

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		let _sts = m.viewSts.val();
		if(m.isHide.val() == '0'){
			$("#saveBtn").show();
			$("#resetBtn").show();
		}else{
			_sts = _sts=='edit'?'view':_sts;
			$("#saveBtn").hide();
			$("#resetBtn").hide();
		}
		if(_sts == "add"){
			getBusinessDate();
			$('#itemRefresh').hide();
			$('#itemRemove').hide();
			$('#articleId').prop("disabled", true);
			$('#resetBtn').prop("disabled", false);
		} else {
			$('#resetBtn').prop("disabled", true);
			if(_sts == "edit"){
				setSomeDisable(false);
			} else {
				setSomeDisable(true);
			}
			let _data = {
				storeCd:m._storeCd.val(),
				unpackId:m.unpackId.val(),
				parentArticleId:m._articleId.val()
			}
			let param = JSON.stringify(_data);
			getRecord(param,false);
		}
	}

	// 设置栏位是否允许编辑
	var setSomeDisable = function (flag) {
		$('#refreshIcon').hide();
		$('#clearIcon').hide();
		$('#itemRefresh').hide();
		$('#itemRemove').hide();
		$('#storeName').prop("disabled", true);
		$('#unpackDate').prop("disabled", true);
		$('#articleId').prop("disabled", true);
		$('#unpackQty').prop("disabled", flag);
		$('#unpackAmt').prop("disabled", flag);
		$('#remarks').prop("disabled", flag);
		$('#saveBtn').prop("disabled", flag);
	}

	// 重置栏位
	var clearForm = function (flg) {
		if(flg){
			m.storeName.val("").attr("k","").attr("v","");
			m.remarks.val("");
		}
		m.articleId.val("").attr("k","").attr("v","");
		m.unpackId.val("");
		m.barcode.val("");
		m.unpackQty.val("1");
		m.unpackAmt.val("");
	}

	// 画面按钮点击事件
	var but_event = function(){
		// $("#unpackQty").blur(function () {
		// 	$("#unpackQty").val(toThousands(this.value));
		// });
		//
		// //光标进入，去除金额千分位，并去除小数后面多余的0
		// $("#unpackQty").focus(function(){
		// 	$("#unpackQty").val(reThousands(this.value));
		// });

		// 母货号拆包数量监听事件
		m.unpackQty.on("change",function(){
			var inputValue = reThousands(m.unpackQty.val());
			if(inputValue==null||inputValue==''){
				$("#unpackQty").css("border-color","red");
				_common.prompt("Unpacking quantity cannot be empty!",5,"info");
				$('#unpackQty').focus();
				m.unpackAmt.val('0');
				$("#unpackQty").css("border-color","#CCCCCC");
				return;
			}
			if(!checkNum((inputValue))){
				$("#unpackQty").css("border-color","red");
				_common.prompt("Unpacking quantity can only be an integer!",5,"info");
				$('#unpackQty').focus();
				m.unpackAmt.val('0');
				$("#unpackQty").css("border-color","#CCCCCC");
				return;
			}
			if(parseFloat(inputValue)>999999){
				$("#unpackQty").css("border-color","red");
				_common.prompt("Unpacking quantity can not be greater than 999999!",3,"info");
				$('#unpackQty').focus();
				m.unpackAmt.val('0');
				$("#unpackQty").css("border-color","#CCCCCC");
				return false;
			}
			m.unpackAmt.val(toThousands(getAmount()));
		});

		// 重置按钮
		m.resetBtn.on("click",function(){
			_common.myConfirm("Are you sure you want to Reset?",function(result){
				if(result!="true"){return false;}
				var _sts = m.viewSts.val();
				if(_sts!="add"){return;}
				clearForm(true);
				getBusinessDate();
				$('#refreshIcon').show();
				$('#clearIcon').show();
				$('#itemRefresh').hide();
				$('#itemRemove').hide();
				$('#storeName').prop("disabled", false);
				$('#unpackDate').prop("disabled", false);
				$('#articleId').prop("disabled", true);
				$("#zgGridTable>.zgGrid-tbody tr").empty();
				$("#storeName").css("border-color","#CCCCCC");
				$("#unpackDate").css("border-color","#CCCCCC");
				$("#articleId").css("border-color","#CCCCCC");
				$("#unpackQty").css("border-color","#CCCCCC");
			})
		});

		// 保存
		m.saveBtn.on("click",function(){
			if(m.isHide.val() != '0'){ return false; }
			// 判断当前操作状态
			var _sts = m.viewSts.val();
			if(_sts!="add"&&_sts!="edit"){return;}
			// 数据验证
			if(!verifySearch()){return;}
			// 处理子商品详情
			var itemDetail = [], _amount = 0;
			var _unpackQty = reThousands(m.unpackQty.val());
			$("#zgGridTable>.zgGrid-tbody tr").each(function () {
				var _unitQty = reThousands($(this).find('td[tag=unitQty]').text());
				var _price = reThousands($(this).find('td[tag=salesPrice]').text());
				var _childQty = accMul(_unitQty, _unpackQty);
				var orderItem = {
					childArticleId:$(this).find('td[tag=childArticleId]').text(),
					serialNo:$(this).find('td[tag=serialNo]').text(),
					barcode:$(this).find('td[tag=barcode]').text(),
					unitQty:_unitQty,
					unpackQty:_childQty,
					salesPrice:_price
				}
				var _amt = Number(accMul(_childQty, _price)).toFixed(2);
				_amount = accAdd(_amount, _amt);
				itemDetail.push(orderItem);
			});
			if(itemDetail.length < 1){
				_common.prompt("Sub product information cannot be empty!",5,"error");
				return false;
			}
			var _listJson = JSON.stringify(itemDetail);
			// 处理基础数据
			var bean = {
				storeCd : m._storeCd.val(),
				unpackId : m.unpackId.val(),
				parentArticleId : m.articleId.attr('k'),
				unpackDate : subfmtDate(m.unpackDate.val()),
				barcode : m.barcode.val(),
				unpackQty : _unpackQty,
				unpackAmt : _amount,
				remarks : m.remarks.val()
			}
			var _searchJson = JSON.stringify(bean);
			var _data = {
				searchJson : _searchJson,
				listJson : _listJson,
				flag : m.viewSts.val()
			}
			_common.myConfirm("Are you sure you want to save?",function(result){
				if(result!="true"){return false;}
				$.myAjaxs({
					url:url_left+'/save',
					async:true,
					cache:false,
					type :"post",
					data :_data,
					dataType:"json",
					success:function(result){
						if(result.success){
							// 变为查看模式
							setSomeDisable(true);
							m.viewSts.val("view");
							m.unpackId.val(result.o);
							m.unpackQty.val(toThousands(m.unpackQty.val()));
							$('#resetBtn').prop("disabled", true);
							_common.prompt("Data saved successfully！",5,"success");/*保存成功*/
						}else{
							_common.prompt(result.msg,5,"error");
						}
					},
					error : function(e){
						_common.prompt("Data saved failed！",5,"error");/*保存失败*/
					}
				});
			})
		});

		// 返回一览
		m.returnsViewBut.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});
	}

	// 验证检索项是否合法
	var verifySearch = function(){
		var temp = null;
		var _sts = m.viewSts.val();
		if(_sts=='add'){
			temp = $("#storeName").attr("k");
			if(temp==null||$.trim(temp)==''){
				$("#storeName").css("border-color","red");
				_common.prompt("Please select a store first!",5,"info");
				$("#storeName").focus();
				return false;
			}
			$("#storeName").css("border-color","#CCCCCC");
			// 设置url,存入栏位方便取值
			m._storeCd.val(temp);
		}else if(_sts=='edit'){
			temp = m._storeCd.val();
			if(temp==null||$.trim(temp)==''){
				_common.prompt("Failed to load Store No.!",5,"info");
				return false;
			}
		}else{
			return false;
		}
		temp = m.unpackDate.val();
		if(temp==null||$.trim(temp)==''){
			$("#unpackDate").css("border-color","red");
			_common.prompt("Unpacking date cannot be empty!",5,"error");
			$("#unpackDate").focus();
			return false;
		}
		$("#unpackDate").css("border-color","#CCCCCC");

		temp = m.articleId.attr("k");
		if(temp==null||$.trim(temp)==''){
			$("#articleId").css("border-color","red");
			_common.prompt("Parent item code cannot be empty!",5,"info");
			$("#articleId").focus();
			return false;
		}else{
			if(!checkNum(temp)){
				$("#articleId").css("border-color","red");
				_common.prompt("Item code can only be numbers!",5,"info");
				$("#articleId").focus();
				return false;
			}
		}
		$("#articleId").css("border-color","#CCCCCC");

		temp = reThousands(m.unpackQty.val());
		var temp1 = m.unpackQty.val();
		if(temp==null||$.trim(temp)==''){
			$("#unpackQty").css("border-color","red");
			_common.prompt("Unpacking quantity cannot be empty!",5,"info");
	   	$("#unpackQty").focus();
			return false;
		}else{
			if(!checkNum(temp) || temp1.indexOf(",")>0){
					$("#unpackQty").css("border-color","red");
					_common.prompt("Unpacking quantity can only be an integer!",5,"info");
					$("#unpackQty").focus();
					return false;
			}
			if(parseFloat(temp)>999999){
				$("#unpackQty").css("border-color","red");
				_common.prompt("Unpacking quantity can not be greater than 999999!",3,"info");
				$('#unpackQty').focus();
				return false;
			}
		}
		$("#unpackQty").css("border-color","#CCCCCC");
		return true;
	}

	// 拼接检索参数
	var setParamJson = function(){
		// 创建请求字符串
		var searchJsonStr ={
			store:m.store.val(),
			identity:m.identity.val(),
			tableType:_tableType,
			bmStatus:m.entiretyBmStatus.val(),
			newFlg:_newFlg,
			updateFlg:_updateFlg,
			checkFlg:_checkFlg,
			div:_div,
			dpt:_dpt,
			bmBePart:_bmBePart,
			bmType:_bmType,
			bmCode:_bmCode,
			itemCode:_itemCode
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 获取详情信息
	var getRecord = function(params,flag){
		var _sts = m.viewSts.val();
		var _data = {
			searchJson : params,
			flag : _sts
		}
		$.myAjaxs({
			url:url_left+"/get",
			async:true,
			cache:false,
			type :"post",
			data : _data,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 加载数据
					if(flag){
						let temp = {
							storeCd:$("#storeName").attr("k"),
							unpackDate:subfmtDate(m.unpackDate.val()),
							effectiveDate:result.o.unpackDate,
							parentArticleId:result.o.parentArticleId,
						    // parentArticleId:result.o.unpackId
						}
						params = JSON.stringify(temp);
						m.unpackQty.val('1');
					}else{
						$.myAutomatic.setValueTemp(a_item,result.o.parentArticleId,result.o.articleName);
						$.myAutomatic.setValueTemp(a_store,result.o.storeCd,result.o.storeName);
						m.unpackId.val(result.o.unpackId);
						m.unpackDate.val(fmtIntDate(result.o.unpackDate));
						m.unpackQty.val(toThousands(result.o.unpackQty));
						m.unpackAmt.val(toThousands(result.o.unpackAmt));
						m.remarks.val(result.o.remarks);
					}
					m.barcode.val(result.o.barcode);
					// 获取详情
					getDetails(params,_sts);
				}else{
					_common.prompt(result.msg,5,"info");
					return;
				}
			},
			error : function(e){
				_common.prompt("Failed to load data!",5,"info");
				return;
			}
		});
	}

	// 获取List
	var getDetails = function(param, flag){
		paramGrid = "searchJson="+param+"&flag="+flag;
		tableGrid.setting("url", url_left+"/getDetails");
		tableGrid.setting("param", paramGrid);
		tableGrid.loadData();
	}

	// 计算金额
	var getAmount = function(){
		var _unpackQty = m.unpackQty.val();
		if(!checkNum(_unpackQty)){return 0;}
		var _amount = 0;
		$("#zgGridTable>.zgGrid-tbody tr").each(function () {
			// 金额计算: 转换母货号数量*子货号单位数量*售价
			var _price = reThousands($(this).find('td[tag=salesPrice]').text());
			var _unitQty = reThousands($(this).find('td[tag=unitQty]').text());
			var _childQty = accMul(_unitQty, _unpackQty);
			$(this).find('td[tag=unpackQty]').text(toThousands(_childQty));
			var _amt = Number(accMul(_childQty, _price)).toFixed(2);
			_amount = accAdd(_amount, _amt);
		});
		return _amount;
	}

	// 判断是否是数字
	var checkNum = function(value){
		var reg = /^[0-9]*$/;
		return reg.test(value);
	}

	// 表格初始化
	var initTable = function(){
		tableGrid = $("#zgGridTable").zgGrid({
			title:"Item Details",
			param:paramGrid,
			localSort: true,
			colNames:["No.","Item Barcode","Child Item Code","Child Item Name","Unit Qty","Total Qty","Sales Price"],
			colModel:[
				{name:"serialNo",type:"text",text:"right",ishide:true},
				{name:"barcode",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"childArticleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"160",ishide:false,css:""},
				{name:"unitQty",type:"text",text:"right",width:"130",ishide:false,getCustomValue:getThousands},
				{name:"unpackQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"salesPrice",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:false,//是否需要分页
			// sidx:"bm.bm_type,Plan.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:false,
			freezeHeader:true,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self) {
				if(m.viewSts.val()=='add'){m.unpackAmt.val(getAmount());}
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
			}
		});
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
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

	// 初始化店铺选择
	function initAutomatic() {
		a_store = $("#storeName").myAutomatic({
			url: url_root + "/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function(){
				$.myAutomatic.cleanSelectObj(a_item);
				$('#itemRefresh').hide();
				$('#itemRemove').hide();
				$('#articleId').prop("disabled", true);
			},
			selectEleClick: function (thisObject) {
				$.myAutomatic.cleanSelectObj(a_item);
				if(thisObject.attr('k')){
					$('#itemRefresh').show();
					$('#itemRemove').show();
					$('#articleId').prop("disabled", false);
				}else{
					$('#itemRefresh').hide();
					$('#itemRemove').hide();
					$('#articleId').prop("disabled", true);
				}
			}
		});

		// 输入框事件绑定
		a_item = $("#articleId").myAutomatic({
			url: url_root + "/ma1200/getList",
			ePageSize: 10,
			startCount: 3,
			cleanInput: function () {
				clearForm(false);
				$("#zgGridTable>.zgGrid-tbody tr").empty();
			},
			selectEleClick: function (thisObject) {
				m.barcode.val("");
				m.unpackQty.val("1");
				m.unpackAmt.val("");
				$("#zgGridTable>.zgGrid-tbody tr").empty();
				let _data = {
					unpackDate:subfmtDate(m.unpackDate.val()),
					parentArticleId:thisObject.attr('k')
				}
				let param = JSON.stringify(_data);
				getRecord(param,true);
			}
		});
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
				_common.prompt(title+" Failed to load data!",5,"error");
			}
		});
	}

	// 获取业务日期
	function getBusinessDate(){
		getSelectOptions("Unpacking Date","/cm9060/getDate", null,function(res){
			let date = "";
			if(!!res && res.toString().length==8){
				res = res.toString();
				date = res.substring(6,8)+"/"+res.substring(4,6)+"/"+res.substring(0,4);
			}
			$('#unpackDate').val(date);
		});
	}

	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('unpackEdit');
_start.load(function (_common) {
	_index.init(_common);
});