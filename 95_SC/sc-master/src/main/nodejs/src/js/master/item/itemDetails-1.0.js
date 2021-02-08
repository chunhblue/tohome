require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('itemDetails', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
		getThousands = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
	const KEY = 'ITEM_MASTER_QUERY';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		_articleId : null,
		_effectiveStartDate : null,
		back: null
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  	}
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/itemMaster";
		getThousands = _common.getThousands;
		// 首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 查询加载数据
		getArticleMaster();
	}

	// 画面按钮点击事件
	var but_event = function(){
		// 返回按钮
		m.back.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});
	}

	// 获取主档基本信息
	var getArticleMaster = function(){
		var articleId = m._articleId.val();
		var startDate = m._effectiveStartDate.val();
		if(articleId == null ||$.trim(articleId) == ''
			|| startDate == null ||$.trim(startDate) == ''){
			_common.prompt("Parameter is empty!",5,"error");
			return false;
		}
		var _data = {
			articleId : articleId,
			effectiveStartDate : startDate
		}
		var param = JSON.stringify(_data);
		$.myAjaxs({
			url:url_left+"/getData",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 获取厨打关系
					// getFoodService(param);
					// 获取包装规格信息
					getCartonSpecification(param);
					// 获取口味关系
					// getFlavor(param);
					// 获取条码信息
					getBarcode(param);
					// 获取进货控制信息
					getOrderControl(param);
					// 获取销售控制信息
					getSalesControl(param);
					// 加载基本信息
					$('#itemCode').val(result.o.articleId);
					$('#effectiveStartDate').val(fmtIntDate(result.o.effectiveStartDate));
					$('#effectiveEndDate').val(fmtIntDate(result.o.effectiveEndDate));
					$('#approvalRegion').val(result.o.regionName);
					$('#qosExpireDate').val(fmtIntDate(result.o.qosExpireDate));
					$('#itemNameVietnamese').val(result.o.articleNameVietnamese);
					$('#itemNameEnAb').val(result.o.articleShortNameEn);
					$('#vietnameseShortName').val(result.o.vietnameseShortName);
					$('#itemShortNameVietnamese').val(result.o.articleShortNameVietnamese);
					$('#itemNameEn').val(result.o.articleNameEn);
					$('#manufacturer').val(result.o.manufacturer);
					$('#specification').val(result.o.spec);
					$('#salesUom').val(result.o.salesUnitId);
					$('#topDepCd').val(result.o.depName);
					$('#depCd').val(result.o.pmaName);
					$('#category').val(result.o.categoryName);
					$('#subCategory').val(result.o.subCategoryName);
					$('#materialType').val(result.o.materialType);
					$('#brand').val(result.o.brandName);
					$('#origin').val(result.o.productionLocationName);
					$('#itemStatus').val(result.o.productStatus);
					$('#phoneCard').val(result.o.phoneCodeValue);
					$('#servicePartner').val(result.o.servicePartner);
					$('#remark').val(result.o.remark);
					$('#isPrivate').val(result.o.pbFlg);
					$('#specialItem').val(result.o.especialFlg);
					$('#isFoodService').val(result.o.isFoodServiceItem);
					$('#isNew').val(result.o.articleNewFlag);
					$('#labelType').val(result.o.tagType);
					$('#seasonalItem').val(result.o.seasonCd);
					$('#converter').val(result.o.converter);
					$('#purchaseItem').val(result.o.purchaseItem);
					$('#salesItem').val(result.o.salesItem);
					$('#inventoryItem').val(result.o.inventoryItem);
					$('#costItem').val(result.o.costItem);
					$('#isDefaultFree').val(result.o.isDefaultFreeItem);
					$('#isStockCount').val(result.o.isFoodServiceStockCount);
					$('#income').val(result.o.income);
					$('#customer').val(result.o.customer);
					$('#minDisplayQty').val(result.o.minDisplayQty);
					$('#purchasingUnit').val(result.o.receiveUnitId);
					$('#orderType').val(result.o.orderType);
					$('#oplMethod').val(result.o.oplMethod);
					$('#barcodeType').val(result.o.barcodeType);
					$('#warrantyDays').val(result.o.warrantyDays);
					$('#preserveType').val(result.o.preserveType);
					$('#packageOsWeight').val(result.o.packageOsWeight);
					$('#packageSizeLength').val(result.o.packageOsSizeLength);
					$('#widthCm').val(result.o.packageOsSizeWidth);
					$('#heightCm').val(result.o.packageOsSizeHeight);
					$('#packageIsWeight').val(result.o.packageIsWeight);
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}

	// 获取厨打信息
	var getFoodService = function(param){
		$.myAjaxs({
			url:url_left+"/getFoodService",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 加载查询结果
					$('#kitChoose').val(result.o.kitchenChoose);
					$('#schemeOne').val(result.o.schemeOneName);
					$('#schemeTwo').val(result.o.schemeTwoName);
					$('#schemeThree').val(result.o.schemeThreeName);
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}

	// 获取包装规格信息
	var getCartonSpecification = function(param){
		$.myAjaxs({
			url:url_left+"/getCartonSpecification",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 加载查询结果
					$('#netWeight').val(result.o.netWeightG);
					$('#grossWeight').val(result.o.grossWeightG);
					$('#bundlePer').val(result.o.bundlePer);
					$('#unitsPerCarton').val(result.o.unitsPerCarton);
					$('#cartonsPerLayer').val(result.o.cartonsPerLayer);
					$('#cartonsPerPallet').val(result.o.cartonsPerPallet);
					$('#lengthForDC').val(result.o.length);
					$('#widthForDC').val(result.o.width);
					$('#heightForDC').val(result.o.height);
					$('#unitsPer').val(result.o.unitsPer);
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}

	// 获取口味关系
	var getFlavor = function(param){
		paramGrid = "searchJson=" + param;
		flavorTableGrid.setting("url", url_left + "/getFlavor");
		flavorTableGrid.setting("param", paramGrid);
		flavorTableGrid.loadData();
	}

	// 获取条码信息
	var getBarcode = function(param){
		paramGrid = "searchJson=" + param;
		barcodeTableGrid.setting("url", url_left + "/getBarcode");
		barcodeTableGrid.setting("param", paramGrid);
		barcodeTableGrid.loadData();
	}

	// 获取进货控制信息
	var getOrderControl = function(param){
		paramGrid = "searchJson=" + param;
		orderingTableGrid.setting("url", url_left + "/getOrderControl");
		orderingTableGrid.setting("param", paramGrid);
		orderingTableGrid.loadData();
	}

	// 获取销售控制信息
	var getSalesControl = function(param){
		paramGrid = "searchJson=" + param;
		salesTableGrid.setting("url", url_left + "/getSalesControl");
		salesTableGrid.setting("param", paramGrid);
		salesTableGrid.loadData();
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initFlavorTable();// 口味关系列表初始化
				initBarcodeTable();// 条码信息列表初始化
				initOrderingTable();// 订货控制列表初始化
				initSalesTable();// 销售控制列表初始化
				break;
			case "2":
				initTable2();//列表初始化
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	// 表格初始化-口味关系
	var initFlavorTable = function(){
		flavorTableGrid = $("#flavorGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Flavor Code","Flavor Description","Flavor Category Code","Flavor Category"],
			colModel:[
				{name:"tastingId",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"tastingName",type:"text",text:"center",width:"60",ishide:false,css:""},
				{name:"tastingCategoryId",type:"text",text:"center",width:"60",ishide:false,css:""},
				{name:"tastingCategoryName",type:"text",text:"center",width:"60",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-条码信息
	var initBarcodeTable = function(){
		barcodeTableGrid = $("#barcodeGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Barcode Code"],
			colModel:[
				{name:"barcode",type:"text",text:"left",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-订货信息
	var initOrderingTable = function(){
		orderingTableGrid = $("#orderingGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["City","Vendor Code","Vendor Short Name","Product Group Name","VAT In(%)","Default Barcode",
				"Refer Sales Price","Default","Terminate","Min. Order Quantity","Min. Order Amount",
				"Max. Order Quantity","DC Item","DC MOQ","Incremental Quantity","Can Order to DC",
				"Can Order to Supplier","Returnable","Return Remaining Shelf-life","Exchangeable",
				"Exchange Remaining Shelf-life","Exchange/Return within(day)","Vendor Item Code","Delivery Sequence"],
			colModel:[
				{name:"structureName",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"vendorId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"vendorShortName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"productGroupName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"orderTaxTypeName",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"defaultBarcode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"adviseSalePrice",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"isDefault",type:"text",text:"left",width:"60",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"orderTerminated",type:"text",text:"left",width:"80",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"minOrderQty",type:"text",text:"right",width:"140",ishide:false,css:"",getCustomValue:getThousands},
				{name:"minOrderAmtTax",type:"text",text:"right",width:"140",ishide:false,getCustomValue:getThousands},
				{name:"maxOrderQty",type:"text",text:"right",width:"140",ishide:false,css:"",getCustomValue:getThousands},
				{name:"dcItem",type:"text",text:"left",width:"90",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"dcMOrderQty",type:"text",text:"right",width:"90",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderBatchQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderPauseDcFlg",type:"text",text:"left",width:"140",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"orderPauseFlg",type:"text",text:"left",width:"150",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"returnPauseFlg",type:"text",text:"left",width:"110",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"remainingShelfLife",type:"text",text:"right",width:"200",ishide:false,css:"",getCustomValue:getThousands},
				{name:"exchangablePauseFlg",type:"text",text:"left",width:"110",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"exchangableShelfLife",type:"text",text:"right",width:"200",ishide:false,css:"",getCustomValue:getThousands},
				{name:"notificationShelfLife",type:"text",text:"right",width:"200",ishide:false,css:"",getCustomValue:getThousands},
				{name:"vendorArticleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"shipmentFlg",type:"text",text:"right",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",// 宽度自动
			isPage:false// 是否需要分页
		});
	}

	// 表格初始化-销售信息
	var initSalesTable = function(){
		salesTableGrid = $("#salesGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["City","Price Group","VAT Out(%)","Refer Sales Price","Selling Price(+VAT)","Member Price","Sale Pause","Inactive"],
			colModel:[
				{name:"structureName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"maName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"saleTaxTypeName",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"adviseSalePrice",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"baseSalePrice",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"memberPrice",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"saleFlg",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:isYesOrNo},
				{name:"saleInactive",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:isYesOrNo}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 判断是否标识
	function isYesOrNo(tdObj, value){
		var _value = "";
		switch (value) {
			case "0":
				_value = "No";
				break;
			case "1":
				_value = "Yes";
				break;
		}
		return $(tdObj).text(_value);
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		if(date == null||$.trim(date) == ''||date.length!=8){
			return date;
		}
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	//字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		date = date.replace(/\//g,"");
		res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
		return res;
	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('itemDetails');
_start.load(function (_common) {
	_index.init(_common);
});
