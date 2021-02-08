require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('newProductList', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
    var m = {
		reset: null,
		search: null,
		product_type : null,//商品类型
		launching_time : null,
		start_order_date : null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		informType: null,
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left =_common.config.surl+"/informNewProduct";
		systemPath = _common.config.surl;
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		//初始化检索部分内容
		initSearchArea();
    	//事件绑定
		but_event();
		//列表初始化
		initTable1();
		loadTableTh();
		// 初始化店铺运营组织检索
		_common.initOrganization();
		var inform = getUrlParam("inform");//判断是否是通知
		// if(inform!=null){
			m.informType.val("2");
		// }
    	// m.search.click();
    }

	// 初始化检索部分内容
	var initSearchArea = function(){
		m.launching_time.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		m.start_order_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
	}

    //画面按钮点击事件
    var but_event = function(){
		//重置搜索
		m.reset.on("click",function(){
			m.start_order_date.val("");
			m.launching_time.val("");
			m.product_type.val("");
			selectTrTemp = null;
			_common.clearTable();
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			paramGrid = "productType="+m.product_type.val()+
				"&startOrderDate="+subfmtDate(m.start_order_date.val())+
				"&launchingTime="+subfmtDate(m.launching_time.val())+
				"&regionCd="+$("#aRegion").attr("k")+
				"&cityCd="+$("#aCity").attr("k")+
				"&districtCd="+$("#aDistrict").attr("k")+
				"&storeCd="+$("#aStore").attr("k")+
				"&informType="+m.informType.val();
			if(m.informType.val()=="2"){
				_common.getInform();
			}
			tableGrid.setting("url",url_left+"/getList");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});

    }

    //表头合并
    var loadTableTh = function () {
		var th = $("#zgGridTtable_thead").find("th");
		var trHtml = "<tr>";
		for (var i = 0; i < 5; i++) {
			trHtml+=th.eq(i).attr("rowspan","2").prop("outerHTML");
		}
		trHtml+="<th thindex=\"zgGridTtable_Product_Information\" style='width: 1570px' colspan=\"13\">PRODUCT INFORMATION</th>";
		trHtml+="<th thindex=\"zgGridTtable_Product_Description\" style='width: 650px' colspan=\"5\">Item Description (or)BOM of Food Service</th>";
		for (var j = 23; j < 32; j++) {
		  trHtml+=th.eq(j).attr("rowspan","2").prop("outerHTML");
		}
		trHtml+="</tr>";
		th.slice(0,5).remove();
		th.slice(23,32).remove();
		$("#zgGridTtable_thead").prepend(trHtml);
		$("#zgGridTtable_thead th").css({"display":"table-cell","vertical-align":"middle"});
    }

    //表格初始化-新品信息列表样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"New item information list",
    		param:paramGrid,
    		colNames:["Store No.","Store Name","Launching time","Start order date","Type of product","Vendor code","Vendor Name","Item code","Barcode","Product's name","Department","Photo/Picture","Selling price","Orgin","Shelf-life","MOQ","MOA","POSM","Item code","Barcode","Ingredient's name","Order","Vendor name","Order Method","Order schedule","Display position","Branding","Bad Merchandise","Returnable","Exchange goods","SOP","Selling method"],
    		colModel:[
				{name:"storeCd",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"launchingTime",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"startOrderDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"productType",type:"text",text:"left",width:"200",ishide:false,css:""},
		  		{name:"vendorId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"vendorName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"itemCode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"barcode",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:fmtStrToWrap},
				{name:"productName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"photo",type:"text",text:"right",width:"180",ishide:false,css:"",getCustomValue:addImg},
				{name:"sellingPrice",type:"text",text:"right",width:"130",ishide:false,getCustomValue:getThousands},
				{name:"orgin",type:"text",text:"right",width:"130",ishide:false,css:""},
			   	{name:"shelfLife",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"moq",type:"text",text:"right",width:"100",ishide:false,getCustomValue:getThousands},
				{name:"moa",type:"text",text:"right",width:"100",ishide:false,getCustomValue:getThousands},
				{name:"posm",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"descriptionItemCode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"descriptionBarcode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"descriptionProductName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"descriptionOrder",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"descriptionVendorName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"orderMethod",type:"text",text:"left",width:"160",ishide:false,css:""},
				{name:"orderSchedule",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"displayPosition",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"brand",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"badMerchandise",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"returnable",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"exchangeGoods",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"sop",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"sellingMethod",type:"text",text:"right",width:"300",ishide:false,css:""}
	          ],//列内容
	          width:"max",//宽度自动
	          isPage:true,//是否需要分页
			  page:1,//当前页
			  sidx:"launching_time,start_order_date",//排序字段
			  sord:"desc",//升降序
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
	          }
    	});
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }



  //格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
		return res;
	}

	// 格式化数字类型的日期
	function fmtDate(date){
		var res = "";
		res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		return res;
	}

	function subfmtDate(date){
		var res = "";
		if(date!=null&&date!=""){
			res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		}
		return res;
	}

	//格式化数字类型的日期
	function fmtStrToInt(strDate){
		var res = "";
		res = strDate.replace(/-/g,"");
		return res;
	}

	//,号分割字符串换行
	function fmtStrToWrap(tdObj,value){
		var dates = value.split(",");
		var text = "";
		for (let i = 0; i < dates.length; i++) {
			text += dates[i]+"<br>";
		}
		return $(tdObj).html(text);
	}

	//获取地址栏参数
	function getUrlParam(name) {
		const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		const urlObj = window.location;
		var r = urlObj.href.indexOf('#') > -1 ? urlObj.hash.split("?")[1].match(reg) : urlObj.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]); return null;
	}

	//通过地址添加图片
	function addImg(tdObj,url){
    	var text = "<img src='"+url+"' alt=' ' style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>";
    	return $(tdObj).html(text);
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('newProductList');
_start.load(function (_common) {
	_index.init(_common);
});
