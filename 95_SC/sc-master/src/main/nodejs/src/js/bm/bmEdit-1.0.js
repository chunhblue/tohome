require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("myAutomatic");
var _myAjax=require("myAjax");
var _datetimepicker = require("datetimepicker");
define('bmEdit', function () {
    var self = {},
	    url_left = "",
	    page_type_0 = "0",//Add
	    page_type_1 = "1",//修改
	    page_type_2 = "2",//审核
	    page_type_3 = "3",//查看
	    setting_flg_0 = "0",//初始化，隐藏控件
	    setting_flg_1 = "1",//初始化，显示控件
	    setting_flg_2 = "2",//初始化，禁用控件
	    
	    itemTempObj =  null,//临时检索到的商品数据
    	_common=null;
    //在此处添加的对象 全都变为jquery对象
    var m = {
    	use:null,
    	toKen:null,
    	edit_detail_box:null,
    	view_detail_box:null,
    	view_detail_box_123:null,
    	view_detail_box_4:null,
    	view_detail_box_5:null,
    	view_bm_count:null,
    	view_bm_discount:null,
    	view_bm_price:null,
    	error_pcode:null,
		user_dpt:null,
		user_stroe:null,
		alert_div_box:null,
		alert_1:null,
		alert_2:null,
		page_type:null,
		bm_type_box:null,
		bm_type:null,
		bm_code_box:null,
		bm_code_p:null,
		bm_date_box:null,
		start_date:null,
		end_date:null,
		bm_promotion_box:null,
		select_store_but:null,
		promotion_show_store:null,
		detail_goods_code_box:null,
		input_item:null,
		item_name:null,
		item_name_box:null,
		search_item_but:null,
		add_item_box:null,
		add_item_but:null,
		affirm_item_box:null,
		affirm_item_but:null,
		add_ab_group_box:null,
		add_a_but_but:null,
		add_b_but_but:null,
		bm_count_box:null,
		bm_count:null,
		discounts_box:null,
		select_discounts:null,
		bm_dis_box:null,
		bm_dis:null,
		bm_dis_but:null,
		bm_price_box:null,
		bm_price:null,
		bm_price_but:null,
		buy_count_discount_box:null,
		buy_count:null,
		discount_rate:null,
		discount_rate_but:null,
		atwill_item_a_b_box:null,
		atwill_item_a_count:null,
		atwill_item_b_count:null,
		detail_a_b_discount:null,
		atwill_item_a_b_but:null,
		detail_table_123_box:null,
		detail_table_123:null,
		detail_table_123_tbody:null,
		detail_table_4_box:null,
		detail_table_4:null,
		detail_table_4_tbody:null,
		detail_table_5_box:null,
		detail_table_5:null,
		detail_table_5_tbody:null,
		detail_box:null,
		bm_code:null,
		selectStoreDiv:null,
		selectStoreDivall_ck:null,
		selectStoreDivTbody:null,
		selectStoreDivaffirm:null,
		promotion_store:null,
		items_input_list:null,
		items_input_a_list:null,
		items_input_b_list:null,
		table_hidden_input:null,
		//returnsViewBut:null,
		//submitBut:null,
		addDisCount_04:null,
		newNo:null,
		dptFlg:null,
		user_id:null,
		identity:null,//操作人身份 ；1采购，2商品部长,3系统部,4店铺
		//预设值，查看修改审核都使用
		def_company:null,
		def_newNo:null,
		def_store:null,
		def_dpt:null,
		def_buyer:null,
		def_bmType:null,
		def_bmCode:null,
		def_bmName:null,
		def_itemPrice:null,
		def_bmNumber:null,
		def_bmPrice:null,
		def_bmEffFrom:null,
		def_bmEffTo:null,
		def_bmDiscountRate:null,
		def_bmGross:null,
		def_numA:null,
		def_numB:null,
		def_dptFlg:null,
		def_rightFlg:null,
		def_checkFlg:null,
		def_opFlg:null,
		def_userid:null,
		def_rejectreason:null,
		def_firstFlg:null,
		def_dptAll:null,
		def_newFlg:null,
		def_updateFlg:null,
		my_dpt_flg:null,
		now_date:null
    };
    //class对象
    var mclass = {
    		submitBut:null,
    		returnsViewBut :null
    }
    // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
    	}  
    	for (x in mclass)  {
    		m[x] = $("[class*='"+x+"']");
    	}  
    }
    

    function init(common) {
    	_common = common;
    	url_left=_common.config.surl+"/order";
    	createJqueryObj();
    	//首先验证当前操作人的权限是否混乱，
    	if(m.use.val()=="1"){
    		m.error_pcode.show();
    	}
    	
    	switch(m.page_type.val()) {
		case page_type_0:
			executeType(m.bm_type.val());
	    	eventLoad();
			//Add
			m.returnsViewBut.show();
			m.submitBut.prop("disabled",true).show();
			m.alert_div_box.show();
			m.alert_1.show();
			m.alert_2.hide();
			break;
		case page_type_1:
			executeType(m.bm_type.val());
	    	eventLoad();
			//修改
			m.returnsViewBut.show();
			m.submitBut.prop("disabled",true).show();
			m.alert_div_box.show();
			m.alert_2.show();
			m.alert_1.hide();
			break;
		case page_type_2:
			//审核
			m.returnsViewBut.hide();
			m.submitBut.hide();
			m.alert_div_box.hide();
			break;
		case page_type_3:
			page_type_3_init();
			executeType(m.bm_type.val());
			executeDetailType(m.bm_type.val());
			//查看
			m.returnsViewBut.show();
			m.submitBut.hide();
			m.alert_div_box.hide();
			break;
		default:
			
		} 
    	//返回一览
    	m.returnsViewBut.on("click",function(){
    		top.location = url_left;
    	});
    	//当输入了bm数量 焦点离开 将禁用提交按钮
    	m.bm_count.on("blur",function(){
    		m.submitBut.prop("disabled",true);
    	});
    	// 焦点离开 将禁用提交按钮
    	m.bm_dis.on("blur",function(){
    		m.submitBut.prop("disabled",true);
    	});
    	// 焦点离开 将禁用提交按钮
    	m.bm_price.on("blur",function(){
    		m.submitBut.prop("disabled",true);
    	});
    	// 焦点离开 将禁用提交按钮
    	m.atwill_item_a_count.on("blur",function(){
    		m.submitBut.prop("disabled",true);
    	});
    	// 焦点离开 将禁用提交按钮
    	m.atwill_item_b_count.on("blur",function(){
    		m.submitBut.prop("disabled",true);
    	});
    	// 焦点离开 将禁用提交按钮
    	m.detail_a_b_discount.on("blur",function(){
    		m.submitBut.prop("disabled",true);
    	});
    }
    
    //查看画面的展示效果，所有按钮均隐藏
    var page_type_3_init = function(){
    	//基础部分
    	m.select_store_but.hide();
    	//赋值给基础部分内容
    	setValueToElementBase();
    	//禁用基础信息部分
		m.bm_type.prop("disabled",true);
		m.start_date.prop("disabled",true);
		m.end_date.prop("disabled",true);
		
		//明细部分
		m.edit_detail_box.hide();
		setValueToElementDetail();
    }
    //赋值给基础部分内容
    var setValueToElementDetail = function(){
    	
    }
  //赋值给基础部分内容
    var setValueToElementBase = function(){
    }
    //查看时使用的方法，主要用于显示bm商品明细时的头部样式
    var executeDetailType = function(bmType){
    	m.view_detail_box.show();
    	switch(bmType) {
		case "01":
		case "02":
		case "03":
			m.view_detail_box_123.show();
			m.view_bm_count.prop("disabled",true);
			m.view_bm_discount.prop("disabled",true);
			m.view_bm_price.prop("disabled",true);
			break;
		case "04":
			m.view_detail_box_4.show();
			break;
		case "05":
			m.view_detail_box_5.show();
			break;
		default:
    	}
    }
    
    
    
    
    //根据类型开启不同的画面类型
    var executeType = function(value){
    	m.addDisCount_04.val(0);
    	m.items_input_list.val("");
    	m.items_input_a_list.val("");
    	m.items_input_b_list.val("");
    	m.table_hidden_input.empty();
    	
    	
    	switch(value) {
		case "01":
			getBmTypeSettingWeb_1();
			break;
		case "02":
			getBmTypeSettingWeb_2();
			break;
		case "03":
			getBmTypeSettingWeb_3();
			break;
		case "04":
			getBmTypeSettingWeb_4();
			break;
		case "05":
			getBmTypeSettingWeb_5();
			break;
		default:
			getBmTypeSettingWeb_0();
		} 
    	settingButIsDisbale("1");
    }
    var getBmTypeCode = function(bmtype,fun){
    	$.myAjaxs({
			  url:url_left+"/getbmcode",
			  async:true,
			  cache:false,
			  type :"get",
			  data :"type="+bmtype,
			  dataType:"json",
			  beforeSend:function(){
				  m.bm_code_p.text("正在获取BM编码，请稍等...");
			  },
			  success:fun,//getBmCodeResponse
			  complete:_common.myAjaxComplete
		  }); 
    }
    //bm类型下拉处理事件
    var bm_type_select_change = function(befValue,value,thisObj){
    	if(value!=""){
    		getBmTypeCode(value,function(rest){
    			thisObj.attr("befValue",value);
    			if(rest.use){
    				m.bm_code_p.text(rest.code);
    				m.bm_code.val(rest.code);
    				executeType(value);
    			}else{
    				m.bm_code_p.text(rest.msg);
    				m.bm_code.val("");
    				//编号满了
    				executeType(null);
    			}
    		});
		}else{
			thisObj.attr("befValue","");
			m.bm_code_p.text("");
			m.bm_code.val("");
			executeType(null);
		}
    	refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    }
   
  //计算date日期的前n天的日期
	
    //得到默认销售开始日期 当日+2
    var getDefStartDate = function(){
    	var inputDefVal = fmtIntDate(m.now_date.val());
    	var temp = gapDay(new Date(),2,1);
    	return temp.Format("yyyy-MM-dd");
    }
    //得到默认销售开始日期 当日+3
    var getDefStartDate3 = function(){
    	var inputDefVal = fmtIntDate(m.now_date.val());
    	var temp = gapDay(new Date(),3,1);
    	return temp.Format("yyyy-MM-dd");
    }
    
    // 
    
    //画面全局事件加载
    var eventLoad = function(){
    	//BM类型事件处理
    	m.bm_type.on("change",function(){
    		var thisObj = $(this);
    		var value = thisObj.val();//当前选择
    		var befValue = thisObj.attr("befValue");//上一个选择
    		var is = detailIsNotNull(befValue);
    		//判断切换内容之前是否有编辑的内容，如果有需要给出提示，没有直接进行操作
    		if(is){
    			//如果更改BM区分，其他填入项将清空，是否进行修改操作？
    			_common.myConfirm("如果更改BM区分，其他填入项将清空，是否进行修改操作？",function(result){
    				if(result=="true"){
    					m.addDisCount_04.val(0);
    		    		m.items_input_list.val("");
    		    		m.items_input_a_list.val("");
    		    		m.items_input_b_list.val("");
    					bm_type_select_change(befValue,value,thisObj);
    				}else{
    					thisObj.val(befValue);//回制 选择参数
    				}
    			});
    		}else{
    			bm_type_select_change(befValue,value,thisObj);
    		}
    	});
    	//销售开始日期和结束日期
    	//如果销售开始日期和结束日期均的值 均为null则需要赋值默认时间，
    	var defStatrDate = "";
    	var startVal = m.start_date.val();
    	if(startVal==""){
    		//得到默认的销售开始日日期
    		defStatrDate = getDefStartDate();
    	}
    	// 待删除
    	m.start_date.val(defStatrDate);
//    	m.start_date.val("2016-05-01");
//    	m.end_date.val("2017-12-31");
    	//销售开始日
    	m.start_date.datetimepicker({
    		 language:'en',
    		 format: 'yyyy-mm-dd',
    		 maxView:4,
    		 startView:2,
    		 minView:2,
    		 autoclose:true,
    		 todayHighlight:true,
    		 todayBtn:true
		}).on('changeDate', function(ev){//回调
			//切换了内容需要清理明细内容
			executeType(m.bm_type.val());
			var strDate = ev.date;
			//如果开始日<当日+2天，则不允许填入并提示
			if(Number(strDate.Format("yyyyMMdd"))<Number(new Date(defStatrDate).Format("yyyyMMdd"))){
				m.bm_date_box.addClass("has-error");
				_common.prompt("销售开始日不可小于"+defStatrDate,5,"error");
			}else{
				m.bm_date_box.removeClass("has-error");
			}
		});
    	//销售结束日期
    	m.end_date.datetimepicker({
    		language:'en',
    		format: 'yyyy-mm-dd',
    		maxView:4,
    		startView:2,
    		minView:2,
    		autoclose:true,
    		todayHighlight:true,
    		todayBtn:true
    	}).on('changeDate', function(ev){//回调
			var start_date_val = m.start_date.val();
			var strDate = ev.date;
			//如果开始日<当日+2天，则不允许填入并提示
			if(Number(strDate.Format("yyyyMMdd"))<Number(new Date(start_date_val).Format("yyyyMMdd"))){
				m.bm_date_box.addClass("has-error");
				_common.prompt("销售结束日不可小于销售开始日",5,"error");
			}else{
				m.bm_date_box.removeClass("has-error");
			}
		});
    	
    	//选择店铺
    	m.select_store_but.on("click",function(event){
    		event.preventDefault();
    		m.selectStoreDivall_ck.prop("checked",false);
    		m.selectStoreDivTbody.find("input[type='checkbox']").prop("checked",false);
    		m.selectStoreDiv.modal("show");
    	});
    	//弹出窗口的店铺全选 复选按钮事件
    	m.selectStoreDivall_ck.on("change",function(event){
    		event.preventDefault();
    		var ck = $(this).prop("checked");
    		m.selectStoreDivTbody.find("input[type='checkbox']").prop("checked",ck);
    	});
    	//弹出窗口的店铺 确认
    	m.selectStoreDivaffirm.on("click",function(event){
    		event.preventDefault();
    		var selectStores = m.selectStoreDivTbody.find("input[type='checkbox']:checked");
    		if(selectStores.length>0){
    			
    			m.addDisCount_04.val(0);
    			m.items_input_list.val("");
    			m.items_input_a_list.val("");
    			m.items_input_b_list.val("");
    			
    			m.promotion_show_store.empty();
    			var storeCode = [];
    			var str =""; 
    			$(selectStores).each(function(index,node){
    				storeCode.push($(node).val());
    				str+='<span class="text-muted p-line-height">'+$(node).val()+' '+$(node).attr("tvalue")+'</span>';
    			});
    			m.promotion_show_store.html(str);
    			m.promotion_store.val(storeCode.join(","));
    			m.selectStoreDiv.modal("hide");
    			clearTableByBmType(m.bm_type.val());
    			input_item_clear();
    			refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    			m.bm_promotion_box.removeClass("has-error");
    		}else{
    			_common.prompt("Please select store！",5,"error");
    		}
    	});
    	//店铺选择 tr点击事件
    	m.selectStoreDivTbody.find("td[class*='ck']").on("click",function(event){
    		var thisObj = $(this);
    		var checkboxObj = thisObj.parent().find("input[type='checkbox']").eq(0);
    		checkboxObj.prop("checked",!(checkboxObj.prop("checked")));
    	});
    	// Item Barcode输入口 焦点离开事件
    	m.input_item.on("keydown",function(event){
    		if(event.keyCode == "13"){
    			var thisObj = $(this);
    			thisObj.val($.trim(thisObj.val()));
    			settingButIsDisbale("1");
    			if(thisObj.val()==""){
    				input_item_clear();
    				m.detail_goods_code_box.removeClass("has-error");
    			}else{
    				m.detail_goods_code_box.removeClass("has-error");
    				m.search_item_but.click().focus();
    			}
    		}
    	});
    	// bm数量输入口 焦点离开事件
    	m.bm_count.on("blur",function(){
    		var thisObj = $(this);
    		if(thisObj.val()==""){
    			m.bm_count_box.removeClass("has-error");
    		}else{
    			if(Number($.trim(thisObj.val()))==0){
    				_common.prompt("BM数量只能录入1~99的整数",5,"error");
        			m.bm_count_box.addClass("has-error");
        			m.bm_count.focus();
    			}else{
    				m.bm_count_box.removeClass("has-error");
    				var reg = /^[0-9]*$/;
    				if(!reg.test(thisObj.val())){
    					_common.prompt("BM数量只能录入1~99的整数",5,"error");
    					m.bm_count_box.addClass("has-error");
    					m.bm_count.focus();
    				}else{
    					m.bm_count_box.removeClass("has-error");
    				}
    			}
    		}
    	});
    	//点击查找Item Barcode按钮事件
    	m.search_item_but.on("click",function(){
    		m.input_item.val($.trim(m.input_item.val()))
    		var inputVal = m.input_item.val();
    		itemTempObj = null;
    		m.item_name.text("");
    		if($.trim(inputVal)==""){
    			_common.prompt("Item Barcode cannot be empty!",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		var reg = /^[0-9]*$/;
    		if(!reg.test(inputVal)){
    			_common.prompt("Item Barcode must be a number!",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		
    		var sub = inputVal.substring(0, 2);
    		if(sub=="20"||sub=="22"||sub=="23"||sub=="24"||sub=="25"||sub=="26"){
    			// _common.prompt("以‘20’‘22’‘23’‘24’‘25’‘26’开头的条码不允许登录",5,"error");
    			_common.prompt("Barcode beginning with '20','22','23','24','25' and '26' are not allowed to log in",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		m.item_name.text("Loading...");
    		getItemInfoByItem1(inputVal,m.promotion_store.val(),function(res){
    			if(res.success){
    				m.detail_goods_code_box.removeClass("has-error");
    				itemTempObj = new Object();
    				itemTempObj.item = res.item;
    				itemTempObj.name = res.name;
    				itemTempObj.itemSystem = res.itemSystem;
    				m.item_name.text(itemTempObj.name);
    				settingButIsDisbale("0");
    				return true;
    			}else{
    				itemTempObj = null;
    				m.detail_goods_code_box.addClass("has-error");
    				m.item_name.text("");
    				_common.prompt(res.message,5,"error");
    				settingButIsDisbale("1");
        			return false;
    			}
    		});
    		
    	});
    	
    	//单品追加 按钮事件
    	m.add_item_but.on("click",function(){
    		var bmType = m.bm_type.val();
    		if(bmType=="01"){
    			//01捆绑类型只能录入一个商品，给出相应提示。
    			var itemCount = getTrCount_By_Table123();
    			if(itemCount>1){
    				_common.prompt("01捆绑类型只能录入一个商品",5,"error");
    				m.input_item.focus();
    				return false;
    			}
    		}
    		//共通的验证
    		if(!commonItemVerify()){
    			return false;
    		}
    		// 全部验证通过，获取该商品对应每一个店铺的详细数据，进价和售价使用控制日期最近的数据
    		var restInfo = getItemStoreInfo(itemTempObj.itemSystem,m.promotion_store.val(),$.trim(m.start_date.val()),$.trim(m.end_date.val()));
    		if(restInfo.success){
    			createCustomInput(restInfo.data,"");
    			refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    			emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x
    			createTableTr_123();//创建table tr 的内容，向table_123操作
    			_common.prompt("Operation Succeeded!",2,"success");
    			input_item_clear();
    			return true;
    		}else{
    			_common.prompt(restInfo.message,5,"error");
    			m.input_item.focus();
    			return false;
    		}
    	});
    	// 优惠方法事件
    	m.select_discounts.on("change",function(){
    		m.bm_dis_box.removeClass("has-error");
    		m.bm_price_box.removeClass("has-error");
    		var value = $(this).val();
    		if(value!=""){
    			if(value=="1"){
    				setting_bm_dis_box(setting_flg_1);
    				setting_bm_price_box(setting_flg_2);
    				m.bm_dis.focus();
    			}else{
    				setting_bm_price_box(setting_flg_1);
    				setting_bm_dis_box(setting_flg_2);
    				m.bm_price.focus();
    			}
    		}else{
    			setting_bm_dis_box(setting_flg_2);
    			setting_bm_price_box(setting_flg_2);
    		}
    		// 切换了优惠方法 就要重置表格内容
			emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x
			createTableTr_123();//创建table tr 的内容，向table_123操作
    	});
    	//计算折扣价格 事件
    	m.bm_dis_but.on("click",function(){
    		var bmdisVal = m.bm_dis.val();
    		var reg =  /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
    		 //var reg = /^[0-9]*$/;
    		 if(!reg.test(bmdisVal)){
    			// _common.prompt("BM折扣只能录入0.01-99.99的正数。例如1折录入10，9折录入90",5,"error");
    			_common.prompt("BM discount can only enter positive Numbers between 0.01 and 99.99." +
					"For example, 10% discount entry 10, 90% discount entry 90",5,"error");
    			m.bm_dis_box.addClass("has-error");
    			m.bm_s.focus();
    			return false;
    		}
    		 if(bmdisVal==""||bmdisVal<=0||bmdisVal>100){
     			// _common.prompt("BM折扣不能为空，且要大于0小于100",5,"error");
     			_common.prompt("BM discount cannot be empty and must be greater than 0 and less than 100!",5,"error");
     			m.bm_dis_box.addClass("has-error");
     			m.bm_dis.focus();
     			return false;
     		}
    		 m.bm_dis_box.removeClass("has-error");
    		
    		var bmcount = m.bm_count.val();
    		if(bmcount==""){
    			_common.prompt("BM quantity cannot be empty!",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		if(bmcount<=1){
    			_common.prompt("BM quantity to be greater than 1!",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		m.bm_count_box.removeClass("has-error");
    		 
    		var addItems = m.items_input_list.val();
    		if(addItems==""){
    			_common.prompt("Additional product cannot be empty!",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		m.detail_goods_code_box.removeClass("has-error");
    		
    		var storeObj = m.promotion_store.val().split(",");
    		var itemObj = addItems.split(",");
    		//拼接为隐藏域中的所有的input id
    		var ids = [];
    		$.each(storeObj,function(i,storeCode){
    			$.each(itemObj,function(ix,itemCode){
    				ids.push("in_"+storeCode+"_"+itemCode);
    			});
    		});
    		var inputList = m.table_hidden_input.find("input");
    		var bmType = m.bm_type.val();
    		//循环每一个id 进行计算
    		$.each(inputList,function(i,inputObj){
    			var obj = $(inputObj);
    			var disPrice = 0;//折扣销售单价
    			var profitRate = 0;//毛利率
    			var bmPrice = 0;//BM销售价格
    			var price_tax = obj.attr("pricetax");//销售单价
    			var cost_tax = obj.attr("costtax");//进货单价
    			//折扣销售单价=销售单价*折扣/100（保留一位小数）如 ≤ 进价，显示为红色。
    			disPrice = _common.decimal(((price_tax*bmdisVal)/100),1);
    			disPrice = round_fmt(disPrice,2);//仅仅为了补个分位0
    			
    			//毛利率=（折扣销售单价-进货单价）/折扣销售单价（保留二位小数）如毛利率 ≤ 0，显示为红色。
    			if(disPrice==0){
    				profitRate = Number(0).toFixed(2);
    			}else{
    				profitRate = ((((disPrice-cost_tax)/disPrice).toFixed(5))*100).toFixed(2);
    			}
    			//BM销售价格：
        		// 01捆绑类型：BM销售价格=折扣销售单价*BM数量 显示为篮色。
    			bmPrice = _common.decimal((disPrice*bmcount),1);
    			bmPrice = round_fmt(bmPrice,2);//仅仅为了补个分位0
    			obj.attr("disprice",disPrice).attr("profitrate",profitRate).attr("bmprice",bmPrice);
    			
    		});
			switch(bmType) {
		        case "02":
		        	//* 02混合类型：BM销售价格为空
		        	m.table_hidden_input.find("input").attr("bmprice","");
		        	break;
		        case "03":
		        	// 03固定组合类型：BM销售价格=各个商品的折扣销售单价之和 显示为篮色。
		        	$.each(storeObj,function(i,storeCode){
		        		var eachStoreBmPrice = 0;
		        		var objInput = m.table_hidden_input.find("input[store='"+storeCode+"']");
		        		objInput.each(function(i,n){
		        			eachStoreBmPrice+=Number($(n).attr("disprice"));
		        			
		        		});
		        		eachStoreBmPrice = _common.decimal(eachStoreBmPrice,1);
		        		eachStoreBmPrice = round_fmt(eachStoreBmPrice,2);//仅仅为了补个分位0
		        		objInput.attr("bmprice",eachStoreBmPrice);
		        	});
		        	break;
			}
    		
			refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    		createTableTr_123();//创建table tr 的内容，向table_123操作
    		m.submitBut.prop("disabled",false);
    	});
    	
    	//更新BM价格-按钮事件
    	m.bm_price_but.on("click",function(){
    		
    		var bmpriceVal = m.bm_price.val();
    		if(bmpriceVal==""||bmpriceVal<=0){
    			// _common.prompt("BM商品价格不能为空且大于0",5,"error");
    			_common.prompt("The price of BM commodity cannot be empty and greater than 0!",5,"error");
    			m.bm_price_box.addClass("has-error");
    			m.bm_price.focus();
    			return false;
    		}
    		var bm_price_val = m.bm_price.val();
    		var re=/^\d*\.{0,1}\d{0,1}$/;
    		if(!re.test(bm_price_val)){
    			// _common.prompt("BM商品价格只能有一位小数",5,"error");
    			_common.prompt("The price of BM can only be one decimal place!",5,"error");
    			m.bm_price_box.addClass("has-error");
    			m.bm_price.focus();
    			return false;
    		}
    		m.bm_price_box.removeClass("has-error");
    		
    		
    		var bmcount = m.bm_count.val();
    		if(bmcount==""){
    			_common.prompt("BM quantity cannot be empty!",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		if(bmcount<=1){
    			_common.prompt("BM quantity to be greater than 1!",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		m.bm_count_box.removeClass("has-error");
    		
    		var addItems = m.items_input_list.val();
    		if(addItems==""){
    			_common.prompt("Additional product cannot be empty!",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		m.detail_goods_code_box.removeClass("has-error");
    		var inputList = m.table_hidden_input.find("input");
    		//BM销售价格=BM商品价格 显示为篮色。
    		bm_price_val = _common.decimal(bm_price_val,1);//真正的四舍五入
    		bm_price_val = round_fmt(bm_price_val,2);//仅仅为了补个分位0
    		inputList.attr("bmprice",bm_price_val);
    		var bmType = m.bm_type.val();
    		//折扣销售单价
    		switch(bmType) {
		        case "01":
		        	var disprice  = _common.decimal((bm_price_val/bmcount),1);
		        	disprice = round_fmt(disprice,2);//仅仅为了补个分位0
		        	inputList.attr("disprice",disprice);
		        	break;
		        case "03":
		        	var storeObj = m.promotion_store.val().split(",");
		        	//取得各个店铺的商品进价和
		    		$.each(storeObj,function(i,storeCode){
		        		var costtaxSum = 0;
		        		var objInput = m.table_hidden_input.find("input[store='"+storeCode+"']");
		        		var objInputCount = objInput.length-1;// 该店铺下的商品数量
		        		//取得进价并累加
		        		objInput.each(function(ix,n1){
		        			costtaxSum+=Number($(n1).attr("costtax"));
		        		});
		        		var addUpDisprice = 0;
		        		//折扣销售单价=（商品进价/各个商品进价之和）*BM商品价格
		        		objInput.each(function(ixx,n2){
		        			var thisInput = $(n2),
		        				c = thisInput.attr("costtax"),
			        			newDisprice = 0;
		        			if(ixx>=objInputCount){
		        				//最后一个单品的BM销售价格=BM商品价格-前边单品的折扣销售单价之和，以此保证各单品的折扣销售单价之和=BM商品价格
		        				newDisprice = bm_price_val-addUpDisprice;
		        			}else{
		        				newDisprice = (c/costtaxSum)*bm_price_val;
		        				addUpDisprice+=newDisprice;
		        			}
		        			
		        			newDisprice = _common.decimal(newDisprice,1);
		        			newDisprice = round_fmt(newDisprice,2);//仅仅为了补个分位0
		        			thisInput.attr("disprice",newDisprice);
		        		});
		        	});
	        	break;
    		}
    		//毛利率
    		$.each(inputList,function(ix,node){
    			var obj = $(this);
    			var disprice = obj.attr("disprice");
    			var costtax = obj.attr("costtax");
    			var profit_rate =0;
    			if(disprice==0){
    				profit_rate = Number(0).toFixed(2);
    			}else{
    				profit_rate = ((((disprice-costtax)/disprice).toFixed(5))*100).toFixed(2);
    			}
    			obj.attr("profitrate",profit_rate);
    		});
    		refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    		createTableTr_123();//创建table tr 的内容，向table_123操作
    		m.submitBut.prop("disabled",false);
    	});
    	// table列表删除按钮事件_仅仅针对 表格 123 进行操作
    	m.detail_table_123.find("tbody").on("click","a",function(){
    		var thisObj = $(this);
    		var itemcode = thisObj.attr("itemcode");
    		m.table_hidden_input.find("input[itemcode='"+itemcode+"']").remove();
    		//刷新表格
    		refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    		emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x
    		createTableTr_123();//创建table tr 的内容，向table_123操作
    		m.submitBut.prop("disabled",true);
    	});
    	
    	
    	
    	//确认商品 按钮事件
    	m.affirm_item_but.on("click",function(){
    		//共通的验证
    		if(!commonItemVerify()){
    			return false;
    		}
    		//验证列表内是否存在数据，如果存在给出提示
    		if(m.items_input_list.val()!=""){
    			_common.prompt("如要变更商品，请先删除列表内商品！",5,"error");
    			return false;
    		}
    		// 全部验证通过，获取该商品对应每一个店铺的详细数据，进价和售价使用控制日期最近的数据
    		var restInfo = getItemStoreInfo(itemTempObj.itemSystem,m.promotion_store.val(),$.trim(m.start_date.val()),$.trim(m.end_date.val()));
    		if(restInfo.success){
    			//清空条码、商品名称、临时变量对象
    			input_item_clear();
    			createCustomInput(restInfo.data,"");
    			refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    			m.addDisCount_04.val(0);//每次Add商品都重置该字段，用来进行设定商品的折扣种类数量，最多32 最少2
    			emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x
    			createTableTr_4();
    			_common.prompt("Operation Succeeded!",2,"success");
    			return true;
    		}else{
    			_common.prompt(restInfo.message,5,"error");
    			m.input_item.focus();
    			return false;
    		}
    	});
    	
    	// 04 购买数量
    	m.buy_count.on("blur",function(){
    		var thisObj = $(this);
    		if(thisObj.val()==""){
    			m.buy_count_discount_box.removeClass("has-error");
    		}else{
    			if(Number($.trim(thisObj.val()))==0){
    				_common.prompt("购买数量只能录入1~99的整数",5,"error");
        			m.buy_count_discount_box.addClass("has-error");
        			m.buy_count.focus();
    			}else{
    				m.buy_count_discount_box.removeClass("has-error");
	    			var reg = /^[0-9]*$/;
	        		if(!reg.test(thisObj.val())){
	        			_common.prompt("购买数量只能录入1~99的整数",5,"error");
	        			m.buy_count_discount_box.addClass("has-error");
	        			m.buy_count.focus();
	        		}else{
	        			m.buy_count_discount_box.removeClass("has-error");
	        		}
    			}
    		}
    	});
    	// 04 折扣
    	m.discount_rate.on("blur",function(){
    		var thisObj = $(this);
    		if(thisObj.val()==""){
    			m.buy_count_discount_box.removeClass("has-error");
    		}else{
    			if(Number($.trim(thisObj.val()))==0){
    				_common.prompt("折扣只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
					m.buy_count_discount_box.addClass("has-error");
					m.discount_rate.focus();
    			}else{
    				m.buy_count_discount_box.removeClass("has-error");
    				var reg = /^[0-9]*$/;
    				if(!reg.test(thisObj.val())){
    					_common.prompt("折扣只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
    					m.buy_count_discount_box.addClass("has-error");
    					m.discount_rate.focus();
    				}else{
    					m.buy_count_discount_box.removeClass("has-error");
    				}
    			}
    		}
    	});
    	//添加折扣
    	m.discount_rate_but.on("click",function(){
    		
    		//商品的折扣数量 最多32，最少2个
	   		 if(m.addDisCount_04.val()>=32){
	   			 _common.prompt("折扣最多添加32种",5,"error");
	   			 return false;
	   		 }
	   		
	   		if(m.items_input_list.val()==""){
	   			_common.prompt("请先填入商品，确认后再继续操作",5,"error");
	   			m.detail_goods_code_box.addClass("has-error");
	   			m.input_item.focus();
	   			return false;
	   		}
	   		m.detail_goods_code_box.removeClass("has-error");
	   		
    		
    		//验证购买数量是否填入
    		var buy_count = m.buy_count.val();
    		if(buy_count==""){
    			_common.prompt("请填入购买数量",5,"error");
    			m.buy_count_discount_box.addClass("has-error");
    			m.buy_count.focus();
    			return false;
    		}
    		if(buy_count<=1){
    			_common.prompt("购买数量不能小于2",5,"error");
    			m.buy_count_discount_box.addClass("has-error");
    			m.buy_count.focus();
    			return false;
    		}
    		//验证购买数量是否填入
    		var discount_rate = m.discount_rate.val();
    		if(discount_rate==""||Number($.trim(discount_rate))==0){
    			_common.prompt("请填入折扣,只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
    			m.buy_count_discount_box.addClass("has-error");
    			m.discount_rate.focus();
    			return false;
    		}
    		var reg = /^[0-9]*$/;
    		if(!reg.test(discount_rate)){
    			_common.prompt("折扣只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
    			m.buy_count_discount_box.addClass("has-error");
    			m.discount_rate.focus();
    			return false;
    		}
    		 // 验证输入的购买数量是否已经存在了，存在就不能添加
    		 var buycountInput = m.table_hidden_input.find("input[buycount='"+buy_count+"']").length;
    		 if(buycountInput>0){
    			 _common.prompt("购买数量每行不能相同",5,"error");
    			 m.buy_count_discount_box.addClass("has-error");
    			 m.buy_count.focus();
    			 return false;
    		 }
    		 // 验证输入的折扣是否已经存在了，存在就不能添加
    		 var discountInput = m.table_hidden_input.find("input[discount='"+discount_rate+"']").length;
    		 if(discountInput>0){
    			 _common.prompt("折扣每行不能相同",5,"error");
    			 m.buy_count_discount_box.addClass("has-error");
    			 m.discount_rate.focus();
    			 return false;
    		 }
    		 m.buy_count_discount_box.removeClass("has-error");
    		 //-----验证通过----
    		 //增加商品折扣种类数量
    		 var addDisCount_num = Number(m.addDisCount_04.val())+1;
    		 m.addDisCount_04.val(addDisCount_num);
    		 var storeObj = m.promotion_store.val().split(",");
        	//根据店铺取得对应的对象input集合
    		$.each(storeObj,function(i,storeCode){
        		var costtaxSum = 0;
        		var objInput = m.table_hidden_input.find("input[store='"+storeCode+"']").eq(0);
        		var tempInput = null;
        		if(Number(m.addDisCount_04.val())>1){
        			var tempid = objInput.attr("id");
        			//当大于1个折扣种类时，复制eq(0)的对象计算后，生成到table_hidden_input中
        			tempInput = objInput.clone();
        			m.table_hidden_input.append(tempInput);
        			tempInput.attr("id",tempid+"_"+m.addDisCount_04.val());
        		}else{
        			tempInput = objInput;
        			//只有1个折扣种类时 自己在当前input进行计算
        		}
        		//进货单价
        		var costtax = $(tempInput).attr("costtax");
        		//销售单价
        		var pricetax = $(tempInput).attr("pricetax");
        		//开始计算 - 购买数量=录入数量。购买数量每行不能相同。
        		//开始计算 - 折扣=录入折扣。折扣每行不能相同。
        		
        		//开始计算 - 折扣销售单价=销售单价x折扣÷100。舍去分位。
        		var disprice = _common.decimal(((pricetax*discount_rate)/100),1);
        		disprice = round_fmt(disprice,2);//仅仅为了补个分位0
        		//开始计算 - 毛利率=（折扣单价-进货单价）÷折扣单价。如毛利率 ≤ 0，显示为红色。
        		var profitrate =0;
        		if(disprice==0){
        			profitrate = Number(0).toFixed(2);
    			}else{
    				profitrate = ((((disprice-costtax)/disprice).toFixed(5))*100).toFixed(2);
    			}
        		//开始计算 - BM销售价格=折扣单价x购买数量。
        		var bmprice =  _common.decimal((disprice*buy_count),1);
        		bmprice = round_fmt(bmprice,2);//仅仅为了补个分位0
        		$(tempInput).attr("bmprice",bmprice).attr("buycount",buy_count).attr("discount",discount_rate).attr("disprice",disprice).attr("profitrate",profitrate);
    		});
    		refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    		createTableTr_4();
    		claerBuyAndDis();
    		m.buy_count.focus();
    		if(addDisCount_num>=2){
    			m.submitBut.prop("disabled",false);
    		}else{
    			m.submitBut.prop("disabled",true);
    		}
    	});
    	// table列表删除按钮事件_仅仅针对 表格 4 进行操作
    	m.detail_table_4.find("tbody").on("click","a",function(){
    		var thisObj = $(this);
    		var buycount = thisObj.attr("buycount");
    		m.table_hidden_input.find("input[buycount='"+buycount+"']").remove();
    		var addDisCount_num = Number(m.addDisCount_04.val())-1;
    		m.addDisCount_04.val(addDisCount_num);
    		//刷新表格
    		refreshItemsInputList();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    		//emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x 04类型不需要重置内容
    		createTableTr_4();
    		if(addDisCount_num>=2){
    			m.submitBut.prop("disabled",false);
    		}else{
    			m.submitBut.prop("disabled",true);
    		}
    	});
    	
    	//添加为A组 商品 按钮事件
    	m.add_a_but_but.on("click",function(){
    		var groupType = $(this).attr("grouptype");
    		add_ab_common_event(groupType);
    	});
    	//添加为B组 商品 按钮事件
    	m.add_b_but_but.on("click",function(){
    		var groupType = $(this).attr("grouptype");
    		add_ab_common_event(groupType);
    	});
    	//计算折扣价格 ab
    	m.atwill_item_a_b_but.on("click",function(){
    		var reg = /^[0-9]*$/;
    		
    		var atwill_item_a_count = m.atwill_item_a_count.val();
    		if(atwill_item_a_count==""||Number(atwill_item_a_count)==0){
    			_common.prompt("请填入A组购买数量,只能录入整数",5,"error");
    			m.atwill_item_a_b_box.addClass("has-error");
    			m.atwill_item_a_count.focus();
    			return false;
    		}
			if(!reg.test(atwill_item_a_count)){
				_common.prompt("组合购买A组任意商品数量只能录入整数",5,"error");
				m.atwill_item_a_b_box.addClass("has-error");
    			m.atwill_item_a_count.focus();
				return false;
			}
    		var atwill_item_b_count = m.atwill_item_b_count.val();
    		if(atwill_item_b_count==""||Number(atwill_item_b_count)==0){
    			_common.prompt("请填入B组购买数量,只能录入整数",5,"error");
    			m.atwill_item_a_b_box.addClass("has-error");
    			m.atwill_item_b_count.focus();
    			return false;
    		}
    		
    		if(!reg.test(atwill_item_b_count)){
				_common.prompt("组合购买B组任意商品数量只能录入整数",5,"error");
				m.atwill_item_a_b_box.addClass("has-error");
    			m.atwill_item_b_count.focus();
				return false;
			}
    		var detail_a_b_discount = m.detail_a_b_discount.val();
    		if(!reg.test(detail_a_b_discount)){
    			_common.prompt("折扣只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
    			m.atwill_item_a_b_box.addClass("has-error");
    			m.detail_a_b_discount.focus();
    			return false;
    		}
    		if(detail_a_b_discount==""||detail_a_b_discount<=0){
    			_common.prompt("请填入折扣,折扣只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
    			m.atwill_item_a_b_box.addClass("has-error");
    			m.detail_a_b_discount.focus();
    			return false;
    		}
    		m.atwill_item_a_b_box.removeClass("has-error");
    		if(m.items_input_a_list.val()!=""){
    			var count = m.items_input_a_list.val().split(",").length;
    			if(count<1||count>16){
    				_common.prompt("请填入A组商品，每组商品最多16个，最少1个",5,"error");
    				m.detail_goods_code_box.addClass("has-error");
    				m.input_item.focus();
        			return false;
        		}
    		}else{
    			_common.prompt("请填入A组商品，每组商品最多16个，最少1个",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		if(m.items_input_b_list.val()!=""){
    			var count = m.items_input_b_list.val().split(",").length;
    			if(count<1||count>16){
    				_common.prompt("请填入B组商品，每组商品最多16个，最少1个",5,"error");
    				m.detail_goods_code_box.addClass("has-error");
    				m.input_item.focus();
    				return false;
    			}
    		}else{
    			_common.prompt("请填入B组商品，每组商品最多16个，最少1个",5,"error");
    			m.detail_goods_code_box.addClass("has-error");
    			m.input_item.focus();
    			return false;
    		}
    		m.detail_goods_code_box.removeClass("has-error");
    		// 验证全部通过后开始计算各个列段值
    		m.table_hidden_input.find("input").each(function(){
    			var thisInput = $(this),
    				pricetax = thisInput.attr("pricetax"),//销售单价
    				costtax = thisInput.attr("costtax"),//进货单价
	    			inputByCount = 0;
    			/*
    		      * 折扣=录入折扣。
    		      * 折扣单价=销售单价x折扣÷100。舍去分位。
    		      * 毛利率=（折扣单价-进货单价）÷折扣单价。如毛利率 ≤ 0，显示为红色。
    		      */
    			if(thisInput.attr("ab")=="A"){
    				inputByCount = atwill_item_a_count;
    			}else{
    				inputByCount = atwill_item_b_count;
    			}
    			//购买数量=录入数量。
    			thisInput.attr("buycount",inputByCount);
    			//折扣=录入折扣。
    			thisInput.attr("discount",detail_a_b_discount);
    			//折扣单价=销售单价x折扣÷100。舍去分位。
    			var disprice = _common.decimal(((pricetax*detail_a_b_discount)/100),1);
    			disprice = round_fmt(disprice,2);//仅仅为了补个分位0

    			thisInput.attr("disprice",disprice);
    			var profitrate = 0;
    			if(disprice==0){
    				profitrate = Number(0).toFixed(2);
    			}else{
    				profitrate = ((((disprice-costtax)/disprice).toFixed(5))*100).toFixed(2);
    			}
    			thisInput.attr("profitrate",profitrate);
    		});
    		//赋值完成后 重新生产table
    		refreshItemsInput_AB_List();//刷新ab组的单品条码
			createTableTr_5();//根据现有的input创建table结构
			//清空 输入的其他项
			m.submitBut.prop("disabled",false);
    	});
    	
    	//AB组table删除事件
    	// table列表删除按钮事件_仅仅针对 表格 4 进行操作
    	m.detail_table_5.find("tbody").on("click","a",function(){
    		var thisObj = $(this);
    		var itemcode = thisObj.attr("itemcode");
    		var ab = thisObj.attr("ab");
    		m.table_hidden_input.find("input[itemcode='"+itemcode+"'][ab='"+ab+"']").remove();
    		//刷新表格
    		refreshItemsInput_AB_List();//重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    		emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x 04类型不需要重置内容
    		createTableTr_5();
    		
    		var count = 0;
    		if(m.items_input_a_list.val()!=""){
    			count += m.items_input_a_list.val().split(",").length;
    			
    		}
    		if(m.items_input_b_list.val()!=""){
    			count += m.items_input_b_list.val().split(",").length;
    		}
    		if(count<=1){
    			m.submitBut.prop("disabled",true);
    		}else{
    			m.submitBut.prop("disabled",false);
    		}
    	});
    	//提交按钮事件
    	m.submitBut.on("click",function(){
    		setDptFlg();
    		//开启提交验证，根据不同bm类型 进行验证
    		if(verify_submit_by_type()){//verify_submit_by_type
        		var temp = {
        				newNo:m.newNo.val(),
        				baseBmList:getBaseBmCkObject(),//主档
        				bmItems:getBmItemsCkObject(),//明细
        				identity:m.identity.val(),//提交人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
        				pageType:1
        		}
        		var _url = url_left+"/editsubmitpro";
        		if(m.identity.val()=="3"){
        			_url = url_left+"/editsubmitsys";
        		}
        		//identity //提交人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
    			$.myAjaxs({
  				  url:_url,
  				  async:false,
  				  cache:false,
  				  type :"post",
  				  data :"paramJson="+JSON.stringify(temp)+"&identity="+m.identity.val()+"&toKen="+m.toKen.val(),
  				  dataType:"json",
  				  success:showResponse,
  				  complete:_common.myAjaxComplete
  			  }); 
    		}
    	});
    }
    var showResponse = function(data,textStatus, xhr){
//	  	  var resp = xhr.responseJSON;
//	  	  if( resp.result == false){
//	  		  //top.location = resp.s+"?errMsg="+resp.message;
//	  		  return ;
//	  	  }
	  	  if(data.success==true){
	  		  _common.prompt("Operation Succeeded!",2,"success",function(){
	  			top.location = url_left;
	  		  },true);
	  	  }else{
	  		  _common.prompt(data.message,5,"error");
	  	  }
	  	  m.toKen.val(data.toKen);
	    }
    
    //得到去重dpt
    var getDpts = function(){
    	var mySet = new Set();
    	var inputObjs = m.table_hidden_input.find("input");
    	$.each(inputObjs,function(index,node){
    		mySet.add($(node).attr("dpt"));
    	})
    	return _common.setToArray(mySet).join(",");
    }
    //得到bm商品明细
    var getBmItemsCkObject = function(){
    	var bmItemCkList = new Array(),
    		inputObjs = m.table_hidden_input.find("input"),//所有单品
	    	_newNo = m.newNo.val(),//登录序列号
	    	_bmType = m.bm_type.val(),//bm类型
    		_bmCode = m.bm_code.val(),//bm编码
	    	i=null;
    	// 循环所有单品
    	$.each(inputObjs,function(ix,node){
    		var thisInput = $(node);
    			_stroe = thisInput.attr("store"),
    			_item = thisInput.attr("itemcode"),
    			_price = (thisInput.attr("pricetax")*100).toFixed(0),
    			_priceDisc = (thisInput.attr("disprice")*100).toFixed(0),
    			_price2nd = _priceDisc,
    			_discRate=0,//商品折扣
    			_abFlg = 0,
    			_numA =0,
    			_numB =0,
    			_bmItemFlg=thisInput.attr("status"),
    			cc = null;
    			switch(_bmType) {
	                case "01":
	                case "02":
	                case "03":
	                	_discRate = m.bm_dis.val();
	                	if(_discRate==""){
	                		_discRate = ((_priceDisc/_price)*100).toFixed(0);
	                	}
	                	_abFlg = 0;
	                	_numA = 0;
	                	_numB = 0;
	                	break;
	                case "04":
	                	_discRate = thisInput.attr("discount");
	                	var buycount = thisInput.attr("buycount");
	                	_abFlg = 0;
	                	_numA = buycount;
	                	_numB = 0;
	                	break;
	                case "05":
	                	_discRate = m.detail_a_b_discount.val();
	                	_abFlg = (thisInput.attr("ab")).toLowerCase();
	                	_numA = m.atwill_item_a_count.val();
	                	_numB = m.atwill_item_b_count.val();
	                	break;
	                default:
    			}
    			
    		if(_price==0){
    			_price = null;
    		}	
    		//bm明细档
    		var bmItmeCk = {
    				newNo:_newNo,
    				bmType:_bmType,
    				bmCode:_bmCode,
    				store:_stroe,
    				item:_item,
    				price:_price,
    				priceDisc:_priceDisc,
    				discRate:_discRate,
    				price2nd:_price2nd,
    				abFlg:_abFlg,
    				numA:_numA,
    				numB:_numB,
    				bmItemFlg:_bmItemFlg
    		}
    		bmItemCkList.push(bmItmeCk);
    	});
    	return bmItemCkList;
    }
    /*
     * 创建bm基础信息，根据店铺数量创建
     */
    var getBaseBmCkObject = function(){
    	var bmCkList = new Array(),
    		stores = m.promotion_store.val().split(","),
    		_newNo = m.newNo.val(),//登录序列号
    		_company = "095",//公司
    		_dpt = m.user_dpt.val(),//所属资源dpt，例如 有111和112的dpt，应小刚说法，将设置为119,
    		_buyer = m.user_id.val(),//操作人，Add时的操作人
    		_bmType = m.bm_type.val(),//bm类型
    		_bmCode = m.bm_code.val(),//bm编码
    		_bmEffFrom =fmtStrToInt(m.start_date.val()),//bm编码
    		_bmEffTo = fmtStrToInt(m.end_date.val()),//bm编码
    		_numA = 0,//05 AB组=A组购买数量 其它情况=0
    		_numB = 0,//05 AB组=B组购买数量 其它情况=0
    		_dptFlg = m.dptFlg.val(),//0-自DPT   1-同一事业部跨DPT  2-不同事业部跨DPT
    		_rightFlg = "00000",//查看权限标志位
    		_checkFlg = "0",//审核状态
    		_opFlg = "01",//操作类型01-采购员提交，07-采购员确认BM明细单品，08-采购员驳回BM明细单品，17-商品部长审核通过，18-商品部长驳回，21-系统部提交，27-系统部审核通过，28-系统部驳回
    		_dptAll = getDpts(),//逗号分隔所有商品的dpt
    		_newFlg = "0",//0-Add 1-修改 2-删除
    		_firstFlg = "0";//0-正常 1-紧急
    		temp=null;
    	
    	if(_bmType=="05"){
    		_numA = m.atwill_item_a_count.val();
    		_numB = m.atwill_item_b_count.val();
    	}
    	if(m.dptFlg.val()=="0"){
    		_checkFlg = "0";
    	}else{
    		_checkFlg = "4";
    	}
    	if(m.identity.val()=="1"){
    		_opFlg = "01";
    	}else if(m.identity.val()=="3"){
    		_opFlg = "21";
    	}
    	
    	//判断当前bm是否为紧急 计算逻辑为 开始日=当日+2日 就为紧急
    	 var nowDateInt = Number(new Date().Format("yyyyMMdd"));//当日转成数字
    	 if(nowDateInt == Number(_bmEffFrom)){
    		 _firstFlg="1";
    	 }
    	$.each(stores,function(ix,store){
    			//BM类型是04 阶梯折扣=商品原售价；05 AB组=0；其他与现有一致（(店铺+单品).eq（0）单品的第一个售价）
    		var _itemPrice = 0,
	    		//BM类型是;04 阶梯折扣=最大购买数量;05 AB组=A组购买数量+B组购买数量;其他与现有一致（(店铺+单品).eq（0）单品数量
    			_bmNumber = 0,
    			//BM类型是;04 阶梯折扣=0;05 AB组=0;其他与现有一致（(店铺+单品).eq（0）单品的第一个
    			_bmPrice = 0,
    			//商品的折扣率，BM类型=01,02,03录入折扣率时=录入值，不录入时=折扣售价/原售价*100（四舍五入保留整数）
    			//Add：BM类型是
    			//04 阶梯折扣=0
    			//05 AB组=画面录入折扣，例如9折=90
    			_bmDiscountRate = 0,
    			//旧有
    			//对于固定捆绑商品来说，BM商品毛利率=BM明细单品毛利率
    			//对于不固定捆绑商品来说，BM商品毛利率=BM明细单品毛利率最低值
    			//Add：BM类型是
    			//04 阶梯折扣=最低单品毛利率
    			//05 AB组=最低单品毛利率
    			_bmGross = 0,
    			b=null;
    		
    		var inputObjs = m.table_hidden_input.find("input[store='"+store+"']");
    		var input = inputObjs.eq(0);
    		switch(_bmType) {
            case "01":
            case "02":
            	
            case "03":
            	var bm_dis = m.bm_dis.val();
            	if(bm_dis==""){
            		//不录入时=折扣售价/原售价*100（四舍五入保留整数）
            		bm_dis = ((input.attr("disprice")/input.attr("pricetax"))*100).toFixed(0);
            	}
            	_itemPrice = (input.attr("pricetax")*100).toFixed(0);//BM商品中的单品售价
            	_bmNumber = m.bm_count.val();
            	var tempBmPrice = input.attr("bmprice"); 
            	if(tempBmPrice!=""){
            		_bmPrice = (input.attr("bmprice")*100).toFixed(0);//BM商品售价
            	}else{
            		_bmPrice = null;//BM商品售价
            	}
            	_bmDiscountRate = bm_dis;//折扣率(毛利率)
            	_bmGross = (input.attr("profitrate")*100).toFixed(0);
            	break;
            case "04":
            	_itemPrice = ((input.attr("pricetax"))*100).toFixed(0);//BM商品中的单品售价
            	
            	var bmGrossList = new Array();
            	 var objectList = new Array();
            	$.each(inputObjs,function(ix,node){
            		objectList.push($(node).attr("buycount"));
            		bmGrossList.push($(node).attr("profitrate"));
            	})
            	objectList.sort(function(a,b){return b-a});
            	bmGrossList.sort(function(a,b){return a-b});
            	_bmNumber = Number(objectList[0]).toFixed(0);//BM商品中的单品数量
            	_bmPrice = 0;//BM商品售价
            	_bmDiscountRate = 0;//折扣率(毛利率)
            	_bmGross = (bmGrossList[0]*100).toFixed(0);
            	break;
            case "05":
            	_itemPrice = 0;//BM商品中的单品售价
            	_bmNumber = Number(m.atwill_item_a_count.val())+Number(m.atwill_item_b_count.val());//BM商品中的单品数量
            	_bmPrice = 0;//BM商品售价
            	_bmDiscountRate = m.detail_a_b_discount.val();//折扣率(毛利率)
            	var bmGrossList = new Array();
	           	$.each(inputObjs,function(ix,node){
	           		bmGrossList.push($(node).attr("profitrate"));
	           	})
	           	bmGrossList.sort(function(a,b){return a-b});
            	_bmGross = (bmGrossList[0]*100).toFixed(0);
            	break;
            default:
            	_itemPrice = 0;
	        	_bmNumber = 0;
	        	_bmPrice = 0;
	        	_bmDiscountRate = 0;
	        	_bmGross = 0;
        	} 
    		
    		
    		//bm主档
    		var bmCk = {
    			company:_company,
    			newNo:_newNo,
    			store:store,
    			dpt:_dpt,
    			buyer:_buyer,
    			bmType:_bmType,
    			bmCode:_bmCode,
    			bmName:null,
    			itemPrice:_itemPrice,
    			bmNumber:_bmNumber,
    			bmPrice:_bmPrice,
    			bmEffFrom:_bmEffFrom,
    			bmEffTo:_bmEffTo,
    			bmDiscountRate:_bmDiscountRate,
    			bmGross:_bmGross,
    			numA:_numA,
    			numB:_numB,
    			dptFlg:_dptFlg,
    			rightFlg:_rightFlg,
    			checkFlg:_checkFlg,
    			opFlg:_opFlg,
//    			updateDate:null,
//    			userid:null,
//    			rejectreason:null,
    			firstFlg:_firstFlg,
    			dptAll:_dptAll,
    			newFlg:_newFlg,
    			updateFlg:null//0-修改BM价格/折扣 1-修改BM生效期间 2-修改价格和期间
    		};
    		bmCkList.push(bmCk);
    	});
    	return bmCkList;
    }
    //得到店铺集合
    var getStroesObject = function(){
    }
    
    //设定自DPT/跨DPT区分 0-自DPT   1-同一事业部跨DPT  2-不同事业部跨DPT
    var setDptFlg = function(){
    	//计算当前bm是否为跨部门，如果bm明细中的商品都是自己负责的则为0不是跨部门，否则就是跨部门1，只有在Addbm时才计算，其他类型的画面均为后台赋值
    	var inputs = m.table_hidden_input.find("input[status!='1']");
    	var inputsIsMyDpt = m.table_hidden_input.find("input[status='1']");
    	if(inputsIsMyDpt.length==0){
    		//如果没有已确认的商品，则视为没有当前登陆人的资源商品，设置my_dpt_flg为0，提交验证时，如果是采购，就必须要验证当前my_dpt_flg值，如果是0 就给出对应提示
    		m.my_dpt_flg.val(0);
    	}else{
    		m.my_dpt_flg.val(1);
    	}
    	
    	if(inputs.length>0){
    		var mySet = new Set();
    		var inputAll = m.table_hidden_input.find("input");
        	$.each(inputAll,function(ix,node){
        		var div = $(node).attr("dpt").substring(0,1);
        		mySet.add(div);
        	})
        	var myArr = _common.setToArray(mySet);
        	if(myArr.length>1){
        		m.dptFlg.val(2);
        	}else{
        		m.dptFlg.val(1);
        	}
    	}else{
    		m.dptFlg.val(0);
    	}
    }
    
    //提交验证
    var verify_submit_by_type = function(){
    	var bmType = m.bm_type.val();
    	if(bmType==""){
    		_common.prompt("请选择BM类型",5,"error");
			m.bm_type_box.addClass("has-error");
			m.input_item.focus();
			return false;
    	}
    	m.bm_type_box.removeClass("has-error");
    	var bm_code = m.bm_code.val();
    	if(bm_code==""){
    		_common.prompt("当前BM类型的编码全部在使用中，无法创建新的BM ",5,"error");
			m.bm_code_box.addClass("has-error");
			return false;
    	}
    	// 销售日期计算
    	var startDate = m.start_date.val();
    	if(startDate==""){
			_common.prompt("Please select the start date of the sale!",5,"error"); // 请选择销售开始时间
    		m.bm_date_box.addClass("has-error");
    		return false;
    	}
    	var endDate = m.end_date.val();
    	if(endDate==""){
			_common.prompt("Please select the end date of the sale!",5,"error"); // 请选择销售结束时间
    		m.bm_date_box.addClass("has-error");
    		return false;
    	}
    	
    	//如果是采购，需要判断当前填入的商品是否存在采购所属的dpt资源，my_dpt_flg =0 代表没有dpt资源
    	if(m.identity.val()=="1"){
    		if(m.my_dpt_flg.val()=="0"){
    			_common.prompt("当前录入的商品中不能没有自己负责的商品",5,"error");
        		return false;
    		}
    	}
    	
    	//计算当前bm是否为跨部门，如果bm明细中的商品都是自己负责的则为0不是跨部门，否则就是跨部门1，只有在Addbm时才计算，其他类型的画面均为后台赋值
    	if(m.dptFlg.val()=="0"){
    		//跨部门 销售开始日：如果是跨部门的BM，其开始日<当日+3天，则不允许提交并提示。
    		vDate = getDefStartDate();
    	}else{
    		if(m.identity.val()!="3"){
    			vDate = getDefStartDate3();
    		}else{
    			//如果是系统部提交，不需要+3日的验证 仅需+2验证
    			vDate = getDefStartDate();
    		}
    	}
    	//跨部门 销售开始日：如果是跨部门的BM，其开始日<当日+3天，则不允许提交并提示。
		if(Number(new Date(startDate).Format("yyyyMMdd"))<Number(new Date(vDate).Format("yyyyMMdd"))){
			m.bm_date_box.addClass("has-error");
			_common.prompt("销售开始日不能小于"+vDate,5,"error");
			m.bm_date_box.addClass("has-error");
			return false;
		}
		
		//销售开始日和结束日验证
		//跨部门 销售开始日：如果是跨部门的BM，其开始日<当日+3天，则不允许提交并提示。
		if(Number(new Date(endDate).Format("yyyyMMdd"))<Number(new Date(startDate).Format("yyyyMMdd"))){
			m.bm_date_box.addClass("has-error");
			_common.prompt("销售结束日不能小于销售开始日",5,"error");
			m.bm_date_box.addClass("has-error");
			return false;
		}
		m.bm_date_box.removeClass("has-error");
		
		//每日17:00后不允许采购员进行Add、修改、删除的提交。  
		//identity
//		if(m.identity.val()=="1"){
//			var nowTime = Number(new Date().Format("hhmmss"));
//			if(nowTime>=1700){
//				_common.prompt("每日17:00后不允许进行Add、修改、删除的提交",5,"error");
//				return false;
//			}
//		}
    	return true;
    }
    
    
    
    
    //添加A组B组的按钮共通事件处理
    var add_ab_common_event = function(groupType){
    	var addItemABcount = 0;//添加的商品数量
    	if(groupType=="A"){
    		//a商品数量
    		count = m.items_input_a_list.val().split(",").length;
    	}else{
    		//b商品数量
    		count = m.items_input_b_list.val().split(",").length;
    	}
    	if(addItemABcount>=16){
    		_common.prompt("每组商品最多16个最少1个",5,"error");
    		m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
    		return false;
    	}
    	m.detail_goods_code_box.removeClass("has-error");
    	
    	//共通的验证
		if(!commonItemVerify_AB(groupType)){
			return false;
		}
		// 验证a组商品数量是多少
		// 全部验证通过，获取该商品对应每一个店铺的详细数据，进价和售价使用控制日期最近的数据
		var restInfo = getItemStoreInfo(itemTempObj.itemSystem,m.promotion_store.val(),$.trim(m.start_date.val()),$.trim(m.end_date.val()));
		if(restInfo.success){
			createCustomInput(restInfo.data,groupType);//增加新的input内容
			emptyEach();//当添加商品或者删除商品时 清理 由js计算得出的结果,,此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x
			refreshItemsInput_AB_List();//刷新ab组的单品条码
			createTableTr_5();//根据现有的input创建table结构
			_common.prompt("Operation Succeeded!",2,"success");
			input_item_clear();
			m.submitBut.prop("disabled",true);
			return true;
		}else{
			_common.prompt(restInfo.message,5,"error");
			m.input_item.focus();
			return false;
		}
    }
    //清空购买数量和折扣
    var claerBuyAndDis = function(){
    	//清空 购买数量
    	m.buy_count.val("");
    	//清空 折扣
    	m.discount_rate.val("");
    }
    
    //当添加商品或者删除商品时 清理 由js计算得出的结果,
    //此方法要在重新生成表格的方法之前调用 否则失效：createTableTr_x
    var emptyEach = function(){
    	var inputs = m.table_hidden_input.find("input");
    	if(inputs.length>0){
	    	inputs.attr("bmprice","")
				.attr("disprice","")
				.attr("profitrate","")
				.attr("buycount","")
				.attr("discount","");
    	}
    	if(m.bm_type.val()=="03"){
    		//如果类型是03时 需要重置bm数量
			var list = m.items_input_list.val();
			var count = "";
			if(list!=""){
				count = list.split(",").length;
			}
			m.bm_count.val(count);
		}
    }
    
    //创建table tr 内容 向 5表格进行操作
    var createTableTr_5 = function(){
    	//得到店铺集合
    	var stores = m.promotion_store.val().split(",");
    	setting_table_5(setting_flg_1);//清理table内容
    	// 循环店铺集合 开始金额tr拼接
    	var allTr = "";
    	$.each(stores,function(i,node){
    		var no = i+1;
    		//拿到属于当前店铺的所有单品集合
    		var inputs_a = m.table_hidden_input.find("input[store='"+node+"'][ab='A']");
    		var inputs_b = m.table_hidden_input.find("input[store='"+node+"'][ab='B']");
    		var rowspanA = inputs_a.length;//A组合并数
    		var rowspanB = inputs_b.length;//B组合并数
    		
    		if(rowspanA<=0&&rowspanB<=0){
    			allTr = "";
    			return false;
    		}
    		
    		var t1 = 1;
    		var t2 = 1;
    		if(rowspanA>1){
    			t1 = rowspanA;
    		}
    		if(rowspanB>1){
    			t2 = rowspanB;
    		}
    		var rowspanStore = t1+t2;
    		var eachTr = "";
    		
    		//A组载入
			if(rowspanA>0){
				$.each(inputs_a,function(ix,inp){
					eachTr += "<tr>";
					if(ix==0){
			    		eachTr +='<td rowspan="'+rowspanStore+'" class="text-c">'+no+'</td>';
						eachTr +='<td rowspan="'+rowspanStore+'"  class="text-c">'+node+'</td>';
						eachTr +='<td rowspan="'+rowspanA+'" class="text-c">A</td>';
					}
					
					var obj = $(inp),
						itemcode=obj.attr("itemcode"),
		    			bmprice=obj.attr("bmprice"),
		    			itemsystem=obj.attr("itemsystem"),
		    			itemcode=obj.attr("itemcode"),
		    			itemname=obj.attr("itemname"),
		    			dpt=obj.attr("dpt"),
		    			costtax=obj.attr("costtax"),
		    			pricetax=obj.attr("pricetax"),
		    			status=obj.attr("status"),
		    			disprice=obj.attr("disprice"),
		    			buycount=obj.attr("buycount"),
		    			discount=obj.attr("discount"),
		    			profitrate=obj.attr("profitrate"),
		    			ab=obj.attr("ab");
					var profitrateClass = "";
		    		if(profitrate<=0){
		    			// 毛利率
		    			profitrateClass = "text-red";
		    		}
		    		var dispriceClass = "";
		    		if(Number(disprice)<=Number(costtax)){
		    			// 折扣销售单价 如 ≤ 进价，显示为红色。
		    			dispriceClass = "text-red";
		    		}
					eachTr +='<td class="text-l">'+itemcode+'</td>';
					eachTr +='<td class="text-c"><a href="javascript:void(0);" ab="'+ab+'" itemcode="'+itemcode+'" class="glyphicon glyphicon-remove-sign remove-tr"></a></td>';
					eachTr +='<td class="text-l">'+itemname+'</td>';
					eachTr +='<td class="text-c">'+dpt+'</td>';
					eachTr +='<td class="text-r">'+costtax+'</td>';
					eachTr +='<td class="text-r">'+pricetax+'</td>';
					eachTr +='<td class="text-r">'+buycount+'</td>';
					eachTr +='<td class="text-r">'+discount+'</td>';
					eachTr +='<td class="text-r '+dispriceClass+'">'+disprice+'</td>';
					eachTr +='<td class="text-r '+profitrateClass+'">'+profitrate+'%</td>';
					eachTr +='<td class="text-c">'+getStatus(status)+'</td>';
					//载入A组 各个商品数据
					eachTr +='</tr>';
				});
			}else{
				eachTr = "<tr>";
	    		eachTr +='<td rowspan="'+rowspanStore+'" class="text-c">'+no+'</td>';
				eachTr +='<td rowspan="'+rowspanStore+'"  class="text-c">'+node+'</td>';
				eachTr +='<td class="text-c">A</td>';
				for(var i=0;i<11;i++){
					eachTr +='<td></td>';
				}
				eachTr +='</tr>';
			}
			
			if(rowspanB>0){
				$.each(inputs_b,function(ix,inp){
					eachTr += "<tr>";
	    			if(ix==0){
						eachTr +='<td rowspan="'+rowspanB+'" class="text-c">B</td>';
					}
	    			var obj = $(inp),
						itemcode=obj.attr("itemcode"),
		    			bmprice=obj.attr("bmprice"),
		    			itemsystem=obj.attr("itemsystem"),
		    			itemcode=obj.attr("itemcode"),
		    			itemname=obj.attr("itemname"),
		    			dpt=obj.attr("dpt"),
		    			costtax=obj.attr("costtax"),
		    			pricetax=obj.attr("pricetax"),
		    			status=obj.attr("status"),
		    			disprice=obj.attr("disprice"),
		    			buycount=obj.attr("buycount"),
		    			discount=obj.attr("discount"),
		    			profitrate=obj.attr("profitrate"),
		    			ab=obj.attr("ab");
	    			var profitrateClass = "";
		    		if(profitrate<=0){
		    			// 折扣销售单价 如 ≤ 进价，显示为红色。
		    			profitrateClass = "text-red";
		    		}
		    		var dispriceClass = "";
		    		if(Number(disprice)<=Number(costtax)){
		    			// 折扣销售单价 如 ≤ 进价，显示为红色。
		    			dispriceClass = "text-red";
		    		}
					eachTr +='<td class="text-l">'+itemcode+'</td>';
					eachTr +='<td class="text-c"><a href="javascript:void(0);" ab="'+ab+'" itemcode="'+itemcode+'"  class="glyphicon glyphicon-remove-sign remove-tr"></a></td>';
					eachTr +='<td class="text-l">'+itemname+'</td>';
					eachTr +='<td class="text-c">'+dpt+'</td>';
					eachTr +='<td class="text-r">'+costtax+'</td>';
					eachTr +='<td class="text-r">'+pricetax+'</td>';
					eachTr +='<td class="text-r">'+buycount+'</td>';
					eachTr +='<td class="text-r">'+discount+'</td>';
					eachTr +='<td class="text-r '+dispriceClass+'">'+disprice+'</td>';
					eachTr +='<td class="text-r '+profitrateClass+'">'+profitrate+'%</td>';
					eachTr +='<td class="text-c">'+getStatus(status)+'</td>';
	    			//载入B组 各个商品数据
	    			eachTr +='</tr>';
	    		});
			}else{
				eachTr += "<tr>";
				eachTr +='<td class="text-c">B</td>';
				for(var i=0;i<11;i++){
					eachTr +='<td></td>';
				}
				eachTr +='</tr>';
			}
			allTr+=eachTr
    	});
    	m.detail_table_5_tbody.append(allTr);
    	
    	
    }
    //创建table tr 内容 向 4表格进行操作
    var createTableTr_4 = function(){
    	//得到店铺集合
    	var stores = m.promotion_store.val().split(",");
    	setting_table_4(setting_flg_1);//清理table内容
    	// 循环店铺集合 开始金额tr拼接
    	$.each(stores,function(i,node){
    		var no = i+1;
    		//拿到属于当前店铺的所有单品集合
    		var inputs = m.table_hidden_input.find("input[store='"+node+"']");
    		var rowspan = inputs.length;//数量 涉及到合并行
    		var eachTr = "";
    		$.each(inputs,function(ix,inp){
    			var obj = $(inp),
	    			store=obj.attr("store"),
	    			bmprice=obj.attr("bmprice"),
	    			itemsystem=obj.attr("itemsystem"),
	    			itemcode=obj.attr("itemcode"),
	    			itemname=obj.attr("itemname"),
	    			dpt=obj.attr("dpt"),
	    			costtax=obj.attr("costtax"),
	    			pricetax=obj.attr("pricetax"),
	    			status=obj.attr("status"),
	    			disprice=obj.attr("disprice"),
	    			buycount=obj.attr("buycount"),
	    			discount=obj.attr("discount"),
	    			profitrate=obj.attr("profitrate");
    			eachTr +='<tr>';
	    		if(ix==0){
	    			eachTr +='<td rowspan="'+rowspan+'" class="text-c">'+no+'</td>';
	    			eachTr +='<td rowspan="'+rowspan+'"  class="text-c">'+store+'</td>';
	    		}	
	    		var bmpriceClass = "";
	    		if(bmprice!=""){
	    			// 有值就是蓝色
	    			bmpriceClass = "text-blue";
	    		}
	    		eachTr +='<td class="text-r '+bmpriceClass+'">'+bmprice+'</td>';
	    		eachTr +='<td class="text-l">'+itemcode+'</td>';
	    		eachTr +='<td class="text-c"><a href="javascript:void(0);" buycount="'+buycount+'"  itemcode="'+itemcode+'" class="glyphicon glyphicon-remove-sign remove-tr"></a></td>';
	    		eachTr +='<td class="text-l">'+itemname+'</td>';
	    		eachTr +='<td class="text-c">'+dpt+'</td>';
	    		eachTr +='<td class="text-r">'+costtax+'</td>';
	    		eachTr +='<td class="text-r">'+pricetax+'</td>';
	    		
	    		eachTr +='<td class="text-r">'+buycount+'</td>';
	    		eachTr +='<td class="text-r">'+discount+'</td>';
	    		var dispriceClass = "";
	    		if(Number(disprice)<=Number(costtax)){
	    			// 折扣销售单价 如 ≤ 进价，显示为红色。
	    			dispriceClass = "text-red";
	    		}
	    		eachTr +='<td class="text-r '+dispriceClass+'">'+disprice+'</td>';
	    		var profitrateClass = "";
	    		if(profitrate<=0){
	    			// 折扣销售单价 如 ≤ 进价，显示为红色。
	    			profitrateClass = "text-red";
	    		}
	    		eachTr +='<td class="text-r '+profitrateClass+'">'+profitrate+'%</td>';
	    		eachTr +='<td class="text-c">'+getStatus(status)+'</td>';
	    		eachTr +='</tr>';	
    		});
    		m.detail_table_4_tbody.append(eachTr);
    	});
    
    	
    }
    // 创建table tr 的内容，向table_123操作
    var createTableTr_123 = function(){
    	//得到店铺集合
    	var stores = m.promotion_store.val().split(",");
    	setting_table_123(setting_flg_1);//清理table内容
    	// 循环店铺集合 开始金额tr拼接
    	$.each(stores,function(i,node){
    		var no = i+1;
    		//拿到属于当前店铺的所有单品集合
    		var inputs = m.table_hidden_input.find("input[store='"+node+"']");
    		var rowspan = inputs.length;//数量 涉及到合并行
    		var eachTr = "";
    		$.each(inputs,function(ix,inp){
    			var obj = $(inp),
	    			store=obj.attr("store"),
	    			bmcode=obj.attr("bmcode"),
	    			bmprice=obj.attr("bmprice"),
	    			itemsystem=obj.attr("itemsystem"),
	    			itemcode=obj.attr("itemcode"),
	    			itemname=obj.attr("itemname"),
	    			dpt=obj.attr("dpt"),
	    			costtax=obj.attr("costtax"),
	    			pricetax=obj.attr("pricetax"),
	    			status=obj.attr("status"),
	    			disprice=obj.attr("disprice"),
	    			profitrate=obj.attr("profitrate");
    			eachTr +='<tr>';
	    		if(ix==0){
	    			eachTr +='<td rowspan="'+rowspan+'" class="text-c">'+no+'</td>';
	    			eachTr +='<td rowspan="'+rowspan+'"  class="text-c">'+store+'</td>';
	    			eachTr +='<td rowspan="'+rowspan+'"  class="text-c">'+bmcode+'</td>';
	    			var bmpriceClass = "";
		    		if(bmprice!=""){
		    			// 有值就是蓝色
		    			bmpriceClass = "text-blue";
		    		}
	    			eachTr +='<td rowspan="'+rowspan+'"  class="text-r '+bmpriceClass+'">'+bmprice+'</td>';
	    		}	
	    		eachTr +='<td class="text-l">'+itemcode+'</td>';
	    		eachTr +='<td class="text-c"><a href="javascript:void(0);" itemcode="'+itemcode+'" class="glyphicon glyphicon-remove-sign remove-tr"></a></td>';
	    		eachTr +='<td class="text-l">'+itemname+'</td>';
	    		eachTr +='<td class="text-c">'+dpt+'</td>';
	    		eachTr +='<td class="text-r">'+costtax+'</td>';
	    		eachTr +='<td class="text-r">'+pricetax+'</td>';
	    		var dispriceClass = "";
	    		if(Number(disprice)<=Number(costtax)){
	    			// 折扣销售单价 如 ≤ 进价，显示为红色。
	    			dispriceClass = "text-red";
	    		}
	    		eachTr +='<td class="text-r '+dispriceClass+'">'+disprice+'</td>';
	    		var profitrateClass = "";
	    		if(profitrate<=0){
	    			// 折扣销售单价 如 ≤ 进价，显示为红色。
	    			profitrateClass = "text-red";
	    		}
	    		eachTr +='<td class="text-r '+profitrateClass+'">'+profitrate+'%</td>';
	    		eachTr +='<td class="text-c">'+getStatus(status)+'</td>';
	    		eachTr +='</tr>';	
    		});
    		m.detail_table_123_tbody.append(eachTr);
    	});
    }
    //重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    var refreshItemsInputList = function(){
    	var mySet = new Set();
    	var inputObjs = m.table_hidden_input.find("input");
    	$.each(inputObjs,function(index,node){
    		mySet.add($.trim($(node).val()));
    	})
//    	var myArr = _common.setToArray(mySet);
    	var myArr = _common.setToArray(mySet);
    	if(myArr.length>0){
    		m.items_input_list.val(myArr.join(","));
    	}else{
    		m.items_input_list.val("");
    	}
    }
    ///重载 表格内容的单品号码 排除重复的内容，给是否载入重复条码做准备
    var refreshItemsInput_AB_List = function(){
    	var mySet = new Set();
    	//a集合
    	var inputObjs_a = m.table_hidden_input.find("input[ab='A']");
    	$.each(inputObjs_a,function(index,node){
    		mySet.add($(node).val());
    	})
    	var myArr = _common.setToArray(mySet);
    	if(myArr.length>0){
    		m.items_input_a_list.val(myArr.join(","));
    	}else{
    		m.items_input_a_list.val("");
    	}
    	//b集合
    	var mySet_b = new Set();
    	var inputObjs_b = m.table_hidden_input.find("input[ab='B']");
    	$.each(inputObjs_b,function(index,node){
    		mySet_b.add($(node).val());
    	})
    	var myArr_b = _common.setToArray(mySet_b);
    	if(myArr_b.length>0){
    		m.items_input_b_list.val(myArr_b.join(","));
    	}else{
    		m.items_input_b_list.val("");
    	}
    }
    //向临时隐藏域中增加 table表格的内容，
    var createCustomInput =function(list,ab_type){
    	var tempTr = "";
    	$.each(list,function(index,node){
    		var store = node.store,
    			itemSystem = node.itemSystem,
    			itemCode = node.item,
    			itemName = node.itemName,
    			dpt = node.dpt,
    			costTax = round_fmt(node.costTax,2),//进货单价
    			priceTax = round_fmt(node.priceTax,2),//销售单价
    			buycount = node.buyCount,//04 购买数量
    			discount = node.discount,//04 折扣
    			status = node.status,//状态
    			bmCode = m.bm_code.val();
    		var group = "in_"+store+"_"+itemCode;
    		var inputHtml = '';
    			inputHtml += '<input type="hidden" id="'+group+'"';
    			inputHtml += ' store="'+store+'" ';
    			inputHtml += ' bmcode="'+bmCode+'" ';
    			inputHtml += ' itemsystem="'+itemSystem+'" ';
    			inputHtml += ' itemcode="'+itemCode+'" ';
    			inputHtml += ' itemname="'+itemName+'" ';
    			inputHtml += ' dpt="'+dpt+'" ';
    			inputHtml += ' costtax="'+costTax+'" ';
    			inputHtml += ' pricetax="'+priceTax+'" ';
    			inputHtml += ' status="'+status+'" ';
    			inputHtml += ' bmprice="" ';
    			inputHtml += ' disprice="" ';
    			inputHtml += ' profitrate="" ';
    			inputHtml += ' buycount="" ';
    			inputHtml += ' discount="" ';
    			inputHtml += ' ab="'+ab_type+'" ';
    			inputHtml += ' value="'+itemCode+'" />';
    		m.table_hidden_input.append(inputHtml);
    	});
    }
    //取得商品数据，
    var getItemStoreInfo = function(itemSystem,stores,startDate,endDate){
    	var tempStart = fmtStrToInt(startDate);
    	var tempEnd = fmtStrToInt(endDate);
    	var rest = null;
    	//返回结构为每个店铺的不同商品信息
    	$.myAjaxs({
			  url:url_left+"/getitemstoreinfo",
			  async:false,
			  cache:false,
			  type :"get",
			  data :"itemSystem="+itemSystem+"&stores="+stores+"&startDate="+tempStart+"&endDate="+tempEnd,
			  dataType:"json",
			  success:function(res){
				  rest = res;
			  },
			  complete:_common.myAjaxComplete
		  }); 
    	return rest;
    }
    
  //判断该商品在各选择店铺是否有BM生效期间内的控制记录,此方法 仅仅用于验证使用，不做数据获取
    var verdictItemIndate = function(itemSystem,stores,startDate,endDate){
    	var tempStart = fmtStrToInt(startDate);
    	var tempEnd = fmtStrToInt(endDate);
    	var rest = null;
    	//返回结构为每个店铺的不同商品信息
    	$.myAjaxs({
			  url:url_left+"/verdictitemindate",
			  async:false,
			  cache:false,
			  type :"get",
			  data :"itemSystem="+itemSystem+"&stores="+stores+"&startDate="+tempStart+"&endDate="+tempEnd,
			  dataType:"json",
			  success:function(res){
				  rest = res;
			  },
			  complete:_common.myAjaxComplete
		  }); 
    	return rest;
    }
    
    // 验证该商品+选择的店铺在其他BM中是否存在，使用item+store进行检索,存在返回true
    var verdictItemOtherBm = function(item1,stores){
    	var rest = null;
    	//返回结构为每个店铺的不同商品信息
    	$.myAjaxs({
			  url:url_left+"/isitembmexist",
			  async:false,
			  cache:false,
			  type :"get",
			  data :"item1="+item1+"&stores="+stores,
			  dataType:"json",
			  success:function(res){
				  rest = res;
			  },
			  complete:_common.myAjaxComplete
		  }); 
    	return rest;
    }
    
    //根据Item Barcode取得该商品的详细对象，
    var getItemInfoByItem1 = function(item1,stores,fun){
    	//返回结构为每个店铺的不同商品信息
    	$.myAjaxs({
			  url:url_left+"/getiteminfobyitem1",
			  async:true,
			  cache:false,
			  type :"get",
			  data :"item1="+item1,
			  dataType:"json",
			  success:fun,
			  complete:_common.myAjaxComplete
		  }); 
    }
    
    //--------------------------------
  //明细是否有值，有返回true,没有返回false
    var detailIsNotNull = function(type){
    	var trs = 0; 
    	switch(type) {
        case "01":
        case "02":
        case "03":
        	trs = m.detail_table_123_tbody.find("tr").length;
        	break;
        case "04":
        	trs = m.detail_table_4_tbody.find("tr").length;
        	break;
        case "05":
        	var trs = m.detail_table_5_tbody.find("tr").length;
        	break;
        default:
        	trs = 0;
    	} 
    	if(trs>0){
    		return true;
    	}else{
    		return false;
    	}
    }
  //根据bm类型清空不同的table内容
    var clearTableByBmType = function(bmType){
    	switch(bmType) {
        case "01":
        case "02":
        case "03":
        	setting_table_123(setting_flg_1);
        	break;
        case "04":
        	setting_table_4(setting_flg_1);
        	break;
        case "05":
        	setting_table_5(setting_flg_1);
        	break;
    	} 
    	//清理表格专属隐藏域的所有input内容
    	m.table_hidden_input.empty();
    }
    //-------------- 画面各个对象组合处理方式  0：初始化后隐藏，1：初始化后显示，2初始化后禁用显示 ------------------------------------------
    
    //根据bm类型设定画面显示内容 未选择
    var getBmTypeSettingWeb_0 = function(){
    	setting_detail_box(setting_flg_0);
    	setting_detail_goods_code_box(setting_flg_1);
    	setting_bm_count_box(setting_flg_1);
    	setting_discounts_box(setting_flg_1);
    	setting_bm_dis_box(setting_flg_1);
    	setting_bm_price_box(setting_flg_1);
    	setting_buy_count_discount(setting_flg_1);
    	setting_atwill(setting_flg_1);
    	setting_table_123(setting_flg_1);
    	setting_table_4(setting_flg_1);
    	setting_table_5(setting_flg_1);
    }
    //根据bm类型设定画面显示内容1 
    var getBmTypeSettingWeb_1 = function(){
    	setting_detail_box(setting_flg_1);
    	setting_detail_goods_code_box(setting_flg_1);
    	setting_bm_count_box(setting_flg_1);
    	setting_discounts_box(setting_flg_1);
    	setting_bm_dis_box(setting_flg_2);
    	setting_bm_price_box(setting_flg_2);
    	setting_buy_count_discount(setting_flg_0);
    	setting_atwill(setting_flg_0);
    	setting_table_123(setting_flg_1);
    	setting_table_4(setting_flg_0);
    	setting_table_5(setting_flg_0);
    }
    //根据bm类型设定画面显示内容 2
    var getBmTypeSettingWeb_2 = function(){
    	setting_detail_box(setting_flg_1);
    	setting_detail_goods_code_box(setting_flg_1);
    	setting_bm_count_box(setting_flg_1);
    	setting_discounts_box("3");
    	setting_bm_dis_box(setting_flg_2);
    	setting_bm_price_box(setting_flg_2);
    	setting_buy_count_discount(setting_flg_0);
    	setting_atwill(setting_flg_0);
    	setting_table_123(setting_flg_1);
    	setting_table_4(setting_flg_0);
    	setting_table_5(setting_flg_0);
    }
    //根据bm类型设定画面显示内容 3
    var getBmTypeSettingWeb_3 = function(){
    	setting_detail_box(setting_flg_1);
    	setting_detail_goods_code_box(setting_flg_1);
    	setting_bm_count_box(setting_flg_2);//标准不可人工录入
    	setting_discounts_box(setting_flg_1);
    	setting_bm_dis_box(setting_flg_2);
    	setting_bm_price_box(setting_flg_2);
    	setting_buy_count_discount(setting_flg_0);
    	setting_atwill(setting_flg_0);
    	setting_table_123(setting_flg_1);
    	setting_table_4(setting_flg_0);
    	setting_table_5(setting_flg_0);
    }
    //根据bm类型设定画面显示内容 4
    var getBmTypeSettingWeb_4 = function(){
    	setting_detail_box(setting_flg_1);
    	setting_detail_goods_code_box("2");// 显示按钮
    	setting_bm_count_box(setting_flg_0);
    	setting_discounts_box(setting_flg_0);
    	setting_bm_dis_box(setting_flg_0);
    	setting_bm_price_box(setting_flg_0);
    	setting_buy_count_discount(setting_flg_1);
    	setting_atwill(setting_flg_0);
    	setting_table_123(setting_flg_0);
    	setting_table_4(setting_flg_1);
    	setting_table_5(setting_flg_0);
    }
    //根据bm类型设定画面显示内容 5
    var getBmTypeSettingWeb_5 = function(){
    	setting_detail_box(setting_flg_1);
    	setting_detail_goods_code_box("3");
    	setting_bm_count_box(setting_flg_0);
    	setting_discounts_box(setting_flg_0);
    	setting_bm_dis_box(setting_flg_0);
    	setting_bm_price_box(setting_flg_0);
    	setting_buy_count_discount(setting_flg_0);
    	setting_atwill(setting_flg_1);
    	setting_table_123(setting_flg_0);
    	setting_table_4(setting_flg_0);
    	setting_table_5(setting_flg_1);
    }
    
    var setting_detail_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.detail_box.hide();
    		break;
    	case "1":
    		m.detail_box.show();
    		break;
    	default:
    	} 
    }
    
    //设定bm类型
    var setting_bm_type_box = function(flg,select){
    	switch(flg) {
    	case "0":
    		m.bm_type_box.hide();
    		m.bm_type.prop("disabled",false).val("");
    		break;
    	case "1":
    		m.bm_type_box.show();
    		m.bm_type.prop("disabled",false).val(select);
    		break;
    	case "2":
    		m.bm_type_box.show();
    		m.bm_type.prop("disabled",true).val(select);
    		break;
    	default:
    	} 
    }
    //设定销售日期
    var setting_bm_date_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.bm_date_box.hide();
    		m.start_date.prop("disabled",false).val("");
    		m.end_date.prop("disabled",false).val("");
    		break;
    	case "1":
    		m.bm_date_box.show();
    		m.start_date.prop("disabled",false).val("");
    		m.end_date.prop("disabled",false).val("");
    		break;
    	case "2":
    		m.bm_date_box.show();
    		m.start_date.prop("disabled",true).val("");
    		m.end_date.prop("disabled",true).val("");
    		break;
    	default:
    	} 
    }
  //设定促销店铺
    var setting_bm_promotion_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.bm_promotion_box.hide();
    		m.select_store_but.prop("disabled",false);
    		m.promotion_store.val("");
    		m.promotion_show_store.html('<span class="text-muted p-line-height">Please select store！</span>');
    		break;
    	case "1":
    		m.bm_promotion_box.show();
    		m.select_store_but.prop("disabled",false);
    		m.promotion_store.val("");
    		m.promotion_show_store.html('<span class="text-muted p-line-height">Please select store！</span>');
    		break;
    	case "2":
    		m.bm_promotion_box.hide();
    		m.select_store_but.prop("disabled",true);
    		m.promotion_store.val("");
    		m.promotion_show_store.html("");
    		break;
    	default:
    	} 
    }
    // 清理Item Barcode和商品名称的内容，包含单品临时变量
    var input_item_clear = function(){
    	m.input_item.val("");
    	m.item_name.text("");
    	itemTempObj = null;
    	settingButIsDisbale("1");
    }
    //设定Item Barcode右侧所有按钮的禁用和启用-不是隐藏，
    var settingButIsDisbale = function(flg){
    	switch(flg) {
    	case "0":
    		m.add_item_but.prop("disabled",false);
    		m.affirm_item_but.prop("disabled",false);
    		m.add_a_but_but.prop("disabled",false);
    		m.add_b_but_but.prop("disabled",false);
    		break;
    	case "1":
    		m.add_item_but.prop("disabled",true);
    		m.affirm_item_but.prop("disabled",true);
    		m.add_a_but_but.prop("disabled",true);
    		m.add_b_but_but.prop("disabled",true);
    		break;
    	}
    		
    }
    //设定 Item Barcode
    var setting_detail_goods_code_box = function(flg){
    	itemTempObj = null;
    	switch(flg) {
    	case "0":
    		m.detail_goods_code_box.hide();
    		m.input_item.val("");
    		m.item_name.text("");
    		m.add_item_box.show();
    		m.affirm_item_box.show();
    		m.add_ab_group_box.show();
    		break;
    	case "1":
    		m.detail_goods_code_box.show();
    		m.input_item.val("");
    		m.item_name.text("");
    		m.add_item_box.show();
    		m.affirm_item_box.hide();
    		m.add_ab_group_box.hide();
    		break;
    	case "2":
    		m.detail_goods_code_box.show();
    		m.input_item.val("");
    		m.item_name.text("");
    		m.add_item_box.hide();
    		m.affirm_item_box.show();
    		m.add_ab_group_box.hide();
    		break;
    	case "3":
    		m.detail_goods_code_box.show();
    		m.input_item.val("");
    		m.item_name.text("");
    		m.add_item_box.hide();
    		m.affirm_item_box.hide();
    		m.add_ab_group_box.show();
    		break;
    	default:
    	} 
    }
    //设定 order  数量
    var setting_bm_count_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.bm_count_box.hide();
    		m.bm_count.prop("disabled",false).val("");
    		break;
    	case "1":
    		m.bm_count_box.show();
    		m.bm_count.prop("disabled",false).val("");
    		break;
    	case "2":
    		m.bm_count_box.show();
    		m.bm_count.prop("disabled", true).val("");
    		break;
    	default:
    	} 
    }
    //设定 order  优惠方式
    var setting_discounts_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.discounts_box.hide();
    		m.select_discounts.find("option").show();
    		m.select_discounts.prop("disabled",false).val("");
    		break;
    	case "1":
    		m.discounts_box.show();
    		m.select_discounts.find("option").show();
    		m.select_discounts.prop("disabled",false).val("");
    		break;
    	case "2":
    		m.discounts_box.show();
    		m.select_discounts.find("option").show();
    		m.select_discounts.prop("disabled",true).val("");
    		break;
    	case "3":
    		// 特殊类型 02 时
    		m.discounts_box.show();
    		m.select_discounts.find("option[value='2']").hide();
    		m.select_discounts.prop("disabled",false).val("");
    		break;
    	default:
    	} 
    }
    //设定 order 折扣
    var setting_bm_dis_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.bm_dis_box.hide();
    		m.bm_dis.prop("disabled",false).val("");
    		m.bm_dis_but.prop("disabled",false);
    		break;
    	case "1":
    		m.bm_dis_box.show();
    		m.bm_dis.prop("disabled",false).val("");
    		m.bm_dis_but.prop("disabled",false);
    		break;
    	case "2":
    		m.bm_dis_box.show();
    		m.bm_dis.prop("disabled",true).val("");
    		m.bm_dis_but.prop("disabled",true);
    		break;
    	default:
    	} 
    }
    //设定 order 商品价格
    var setting_bm_price_box = function(flg){
    	switch(flg) {
    	case "0":
    		m.bm_price_box.hide();
    		m.bm_price.prop("disabled",false).val("");
    		m.bm_price_but.prop("disabled",false);
    		break;
    	case "1":
    		m.bm_price_box.show();
    		m.bm_price.prop("disabled",false).val("");
    		m.bm_price_but.prop("disabled",false);
    		break;
    	case "2":
    		m.bm_price_box.show();
    		m.bm_price.prop("disabled",true).val("");
    		m.bm_price_but.prop("disabled",true);
    		break;
    	default:
    	} 
    }
    //设定 购买数量和折扣
    var setting_buy_count_discount = function(flg){
    	switch(flg) {
    	case "0":
    		m.buy_count_discount_box.hide();
    		m.buy_count.prop("disabled",false).val("");
    		m.discount_rate.prop("disabled",false).val("");
    		m.discount_rate_but.prop("disabled",false);
    		break;
    	case "1":
    		m.buy_count_discount_box.show();
    		m.buy_count.prop("disabled",false).val("");
    		m.discount_rate.prop("disabled",false).val("");
    		m.discount_rate_but.prop("disabled",false);
    		break;
    	case "2":
    		m.buy_count_discount_box.show();
    		m.buy_count.prop("disabled",true).val("");
    		m.discount_rate.prop("disabled",true).val("");
    		m.discount_rate_but.prop("disabled",true);
    		break;
    	default:
    	} 
    }
    //设定 任意样式
    var setting_atwill = function(flg){
    	switch(flg) {
    	case "0":
    		m.atwill_item_a_b_box.hide();
    		m.atwill_item_a_count.prop("disabled",false).val("");
    		m.atwill_item_b_count.prop("disabled",false).val("");
    		m.detail_a_b_discount.prop("disabled",false).val("");
    		m.atwill_item_a_b_but.prop("disabled",false);
    		break;
    	case "1":
    		m.atwill_item_a_b_box.show();
    		m.atwill_item_a_count.prop("disabled",false).val("");
    		m.atwill_item_b_count.prop("disabled",false).val("");
    		m.detail_a_b_discount.prop("disabled",false).val("");
    		m.atwill_item_a_b_but.prop("disabled",false);
    		break;
    	case "2":
    		m.atwill_item_a_b_box.show();
    		m.atwill_item_a_count.prop("disabled",true).val("");
    		m.atwill_item_b_count.prop("disabled",true).val("");
    		m.detail_a_b_discount.prop("disabled",true).val("");
    		m.atwill_item_a_b_but.prop("disabled",true);
    		break;
    	default:
    	} 
    }
    
    //设定123table样式
    var setting_table_123 = function(flg){
    	switch(flg) {
    	case "0":
    		m.detail_table_123_box.hide();
    		m.detail_table_123_tbody.empty();
    		break;
    	case "1":
    		m.detail_table_123_box.show();
    		m.detail_table_123_tbody.empty();
    		break;
    	case "2":
    		//table 没有禁用属性
    		m.detail_table_123_box.show();
    		m.detail_table_123_tbody.empty();
    		break;
    	default:
    	}
    	m.submitBut.prop("disabled",true);
    }
    //设定4table样式
    var setting_table_4 = function(flg){
    	switch(flg) {
    	case "0":
    		m.detail_table_4_box.hide();
    		m.detail_table_4_tbody.empty();
    		break;
    	case "1":
    		m.detail_table_4_box.show();
    		m.detail_table_4_tbody.empty();
    		break;
    	case "2":
    		//table 没有禁用属性
    		m.detail_table_4_box.show();
    		m.detail_table_4_tbody.empty();
    		break;
    	default:
    	}
    	m.submitBut.prop("disabled",true);
    }
    //设定5 table样式
    var setting_table_5 = function(flg){
    	switch(flg) {
    	case "0":
    		m.detail_table_5_box.hide();
    		m.detail_table_5_tbody.empty();
    		break;
    	case "1":
    		m.detail_table_5_box.show();
    		m.detail_table_5_tbody.empty();
    		break;
    	case "2":
    		//table 没有禁用属性
    		m.detail_table_5_box.show();
    		m.detail_table_5_tbody.empty();
    		break;
    	default:
    	} 
    	m.submitBut.prop("disabled",true);
    }
 // 对Date的扩展，将 Date 转化为指定格式的String
	// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
	// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
	// 例子： 
	// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
	// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
	Date.prototype.Format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	function gapDay(date,n,t){
        var now=new Date(date);
        if(t>0){
        	now = now.setDate(now.getDate()+n);
        }else{
        	now = now.setDate(now.getDate()-n);
        }
        now=new Date(now);
        return now;
    }
	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		return res;
	}
	//格式化数字类型的日期
	function fmtStrToInt(strDate){
		var res = "";
		res = strDate.replace(/-/g,"");
		return res;
	}
	//是否重复添加
	//true 重复
	// false 不重复
	var isRepeatToAdd = function(item){
		var str = m.items_input_list.val();
		if(str.indexOf(item)==-1){
			return false;
		}else{
			return true;
		}
	} 
	//拿到table123的tr数量
	function getTrCount_By_Table123(){
		var trs = m.detail_table_123.find("tr");
		return trs.length;
	} 
	
	//返回 商品状态 0：未确认  1：已确认  2：驳回
	var getStatus = function(status){
		var strCn = "";
		status = Number(status);
		switch(status) {
        case 0:
        	strCn = "未确认";
        	break;
        case 1:
        	strCn = "已确认";
        	break;
        case 2:
    		strCn = "驳回";
        	break;
        default:
        	strCn = "未知状态";
    	} 
		return strCn;
	}
	
	//验证ab组中是否有重复商品
	var  repetition_ab =function(groupType,itemCode){
		if(groupType=="A"){
			var str = m.items_input_a_list.val();
			if(str.indexOf(itemCode)==-1){
				return false;
			}else{
				return true;
			}
		}else if(groupType=="B"){
			//B组合
			var str = m.items_input_b_list.val();
			if(str.indexOf(itemCode)==-1){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	//共通部分的验证 ab组合的验证共同方法
	var commonItemVerify_AB = function(groupType){
		if($.trim(m.promotion_store.val())==""){
			_common.prompt("请先选择店铺，再进行操作",5,"error");
			m.bm_promotion_box.addClass("has-error");
			return false;
		}
		m.bm_promotion_box.removeClass("has-error");
		if($.trim(m.start_date.val())==""){
			_common.prompt("请先填入销售开始日，再进行操作",5,"error");
			m.bm_date_box.addClass("has-error");
			m.start_date.focus();
			return false;
		}
		if($.trim(m.end_date.val())==""){
			_common.prompt("请先填入销售结束日，再进行操作",5,"error");
			m.bm_date_box.addClass("has-error");
			m.end_date.focus();
			return false;
		}
		m.bm_date_box.removeClass("has-error");
		
		if(m.input_item.val()==null){
			_common.prompt("请先填入Item Barcode，再进行操作",5,"error");
			m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
			return false;
		}
		if(itemTempObj==null){
			_common.prompt("请先查找商品，在进行操作",5,"error");
			m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
			return false;
		}
		var sub = itemTempObj.item.substring(0, 2);
		if(sub=="20"||sub=="22"||sub=="23"||sub=="24"||sub=="25"||sub=="26"){
			_common.prompt("以‘20’‘22’‘23’‘24’‘25’‘26’开头的条码不允许登录",5,"error");
			m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
			return false;
		}
		//* 判断该商品在该店铺是否有参加其他BM（审核中、已生效），不允许同一商品在同一店铺参加多个BM，有则提示"不允许一个单品在同一个店铺中参加多项捆绑！"。  
		var v_item_is_in = verdictItemOtherBm(itemTempObj.item,m.promotion_store.val());
		if(v_item_is_in.success){
			_common.prompt(v_item_is_in.message,5,"error");
			m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
			return false;
		}
		
		//判断该商品在各选择店铺是否有BM生效期间内的控制记录，如果有，则选择离生效期间最近的控制记录的进、售价；否则提示“该单品在XXXXX店没有对应期间的控制记录。” 
		var v_item_is_indate = verdictItemIndate(itemTempObj.itemSystem,m.promotion_store.val(),$.trim(m.start_date.val()),$.trim(m.end_date.val()));
		if(!v_item_is_indate.success){
			_common.prompt(v_item_is_indate.message,5,"error");
			m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
			return false;
		}
		//不能重复添加商品。根据载入的组别进行判断
		var v_repetition_a = repetition_ab("A",itemTempObj.item);
		var v_repetition_b = repetition_ab("B",itemTempObj.item);
		if(v_repetition_a||v_repetition_b){
			_common.prompt("A组或B组中，已有此 "+itemTempObj.item+" 商品，不能重复添加",5,"error");
			m.detail_goods_code_box.addClass("has-error");
			m.input_item.focus();
			return false;
		}
		m.detail_goods_code_box.removeClass("has-error");
		return true;
	}
	
	
	//共通部分的验证
	var commonItemVerify = function(){
		if($.trim(m.promotion_store.val())==""){
			_common.prompt("请先选择店铺，再进行操作",5,"error");
			m.bm_promotion_box.addClass("has-error");			
			return false;
		}
		m.bm_promotion_box.removeClass("has-error");		
		if($.trim(m.start_date.val())==""){
			_common.prompt("请先填入销售开始日，再进行操作",5,"error");
			m.bm_date_box.addClass("has-error");
			m.start_date.focus();
			return false;
		}
		if($.trim(m.end_date.val())==""){
			_common.prompt("请先填入销售结束日，再进行操作",5,"error");
			m.bm_date_box.addClass("has-error");
			m.end_date.focus();
			return false;
		}
		m.bm_date_box.removeClass("has-error");
		if(m.input_item.val()==null){
			_common.prompt("请先填入Item Barcode，再进行操作",5,"error");
			m.detail_goods_code_box.addClass("has-error");	
			m.input_item.focus();
			return false;
		}
		if(itemTempObj==null){
			_common.prompt("请先查找商品，在进行操作",5,"error");
			m.detail_goods_code_box.addClass("has-error");	
			m.input_item.focus();
			return false;
		}
		var sub = itemTempObj.item.substring(0, 2);
		if(sub=="20"||sub=="22"||sub=="23"||sub=="24"||sub=="25"||sub=="26"){
			_common.prompt("以‘20’‘22’‘23’‘24’‘25’‘26’开头的条码不允许登录",5,"error");
			m.detail_goods_code_box.addClass("has-error");	
			m.input_item.focus();
			return false;
		}
		
		//* 判断该商品在该店铺是否有参加其他BM（审核中、已生效），不允许同一商品在同一店铺参加多个BM，有则提示"不允许一个单品在同一个店铺中参加多项捆绑！"。  
		var v_item_is_in = verdictItemOtherBm(itemTempObj.item,m.promotion_store.val());
		if(v_item_is_in.success){
			_common.prompt(v_item_is_in.message,5,"error");
			m.detail_goods_code_box.addClass("has-error");	
			m.input_item.focus();
			return false;
		}
		
		//判断该商品在各选择店铺是否有BM生效期间内的控制记录，如果有，则选择离生效期间最近的控制记录的进、售价；否则提示“该单品在XXXXX店没有对应期间的控制记录。” 
		var v_item_is_indate = verdictItemIndate(itemTempObj.itemSystem,m.promotion_store.val(),$.trim(m.start_date.val()),$.trim(m.end_date.val()));
		if(!v_item_is_indate.success){
			_common.prompt(v_item_is_indate.message,5,"error");
			m.detail_goods_code_box.addClass("has-error");	
			m.input_item.focus();
			return false;
		}
		//不能重复添加商品。
		var isRepeat = isRepeatToAdd(itemTempObj.item);
		if(isRepeat){
			_common.prompt("不能重复添加商品",5,"error");
			m.detail_goods_code_box.addClass("has-error");	
			m.input_item.focus();
			return false;
		}
		m.detail_goods_code_box.removeClass("has-error");	
		return true;
	}
	
	//四舍五入,v=值，p小数点
	var round_fmt = function(v,p){
		if(v==null||v==""){return "";}
		return Number(v).toFixed(p);
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('bmEdit');
_start.load(function (_common) {
	_index.init(_common);
});
