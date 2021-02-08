require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("myAutomatic");
var _myAjax=require("myAjax");
var _datetimepicker = require("datetimepicker");
define('bmUpdate', function () {
    var self = {},
	    url_left = "",
    	_common=null;
    //在此处添加的对象 全都变为jquery对象
    var m = {
    		toKen:null,
    		main_box:null,
    		dptFlg:null,
    		user_dpt:null,
    		bm_type:null,
    		bm_code:null,
    		update_flg:null,
    		start_date:null,
    		start_date_text:null,
    		end_date:null,
    		detail_box:null,
    		promotion_show_store:null,
    		detail_table_123_box:null,
    		detail_table_4_box:null,
    		detail_table_5_box:null,
    		detail_table_123_tbody:null,
    		detail_table_4_tbody:null,
    		detail_table_5_tbody:null,
    		edit_detail_box_123:null,
    		edit_detail_box_4:null,
    		edit_detail_box_5:null,
    		select_discounts:null,
    		bm_dis:null,
    		bm_price:null,
    		bm_dis_but:null,
    		bm_price_but:null,
    		table_hidden_input:null,
    		promotion_store:null,
    		bm_count:null,
    		items_input_list:null,
    		bm_dis_box:null,
    		bm_count_box:null,
    		bm_price_box:null,
    		buy_count:null,
    		discount_rate:null,
    		discount_rate_but:null,
    		buy_count_discount_box:null,
    		addDisCount_04:null,
    		atwill_item_a_count:null,
    		atwill_item_b_count:null,
    		detail_a_b_discount:null,
    		atwill_item_a_b_box:null,
    		atwill_item_a_b_but:null,
    		items_input_a_list:null,
    		items_input_b_list:null,
    		item_code_dpt:null,
//    		submitBut:null,
//    		returnsViewBut:null,
    		bm_type_box:null,
    		bm_code_box:null,
    		update_flg_box:null,
    		user_id:null,
    		identity:null,
    		bm_date_box:null,
    		myCheckResources:null,
    		n:null
    };
    var getBmItemsCkObject = function(){
    	var bmItemCkList = new Array(),
		inputObjs = m.table_hidden_input.find("input"),//所有单品
    	_newNo = null,//登录序列号
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
    //class对象
   
    function init(common) {
    	_common = common;
    	url_left=_common.config.surl+"/order";
    	createJqueryObj();
    	eventLoad();
    	initEffects();
    }
    //全局按钮事件加载
    var eventLoad = function(){
    	//bm类型下拉切换事件,只要是被切换了 就必须变为初始化样式
    	m.bm_type.on("change",function(){
    		initEffects();
    		m.bm_type_box.removeClass("has-error");
    		var thisObj = $(this);
    		if(thisObj.val()!=""){
    			m.bm_code.prop("disabled",false).focus();
    		}
    	});
    	//bm编码输入后焦点离开事件
    	m.bm_code.on("blur",function(){
    		var thisObj = $(this);
    		m.bm_code_box.removeClass("has-error");
    		if(thisObj.val()!=""){
    			if(thisObj.val().length<3){
    				thisObj.val(('000'+thisObj.val()).slice(-3));
    			}
    			var reg = /^[0-9]*$/;
    			if(!reg.test(thisObj.val())){
    				_common.prompt("BM编码必须是0~9的数字",5,"error");
    				m.bm_code.prop("disabled",false).focus();
    				return false;
    			}
    			if(m.bm_type.val()==""){
    				_common.prompt("请选择BM类型",5,"error");
    				m.bm_type.focus();
    				return false;
    			}
    			// 根据 类型和编码取得数据
    			var rest = getData(thisObj.val(),m.bm_type.val());
    			if(rest.data!=null){
    				m.bm_code.prop("disabled",true);
    				m.update_flg.prop("disabled",false);
    				settingData(rest.data);
    			}else{
    				m.bm_code.prop("disabled",false);
    				m.update_flg.val("").prop("disabled",true);
    				settingData(null);
    				_common.prompt(rest.message,5,"error");
    				m.bm_code.focus();
    			}
    		}
    	});
    	
    	//操作区分下拉切换事件
    	m.update_flg.on("change",function(){
    		var thisObj = $(this);
    		var selected = thisObj.val();
    		m.update_flg_box.removeClass("has-error");
    		if(selected!=""){
    			thisObj.prop("disabled",true);
    			if(selected=="0"){
    				m.main_box.find("[updateflg='0']").prop("disabled",false);
    				m.main_box.find("a[updateflg='0']").show();
    				m.main_box.find("[updateflg='1']").prop("disabled",true);
    			}else if(selected=="1"){
    				m.main_box.find("[updateflg='0']").prop("disabled",true);
    				m.main_box.find("a[updateflg='0']").hide();
    				m.main_box.find("[updateflg='1']").prop("disabled",false);
    			}else{
    				m.main_box.find("*[updateflg]").prop("disabled",false);
    				m.main_box.find("a[updateflg]").show();
    			}
    		}else{
    			thisObj.prop("disabled",false);
    			m.main_box.find("*[updateflg]").prop("disabled",false);
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
    		m.submitBut.prop("disabled",false);
    		var strDate = ev.date;
			//如果开始日<当日+2天，则不允许填入并提示
    		var _strDate = _common.dateFormat(strDate,"yyyyMMdd");
			if(Number(_strDate)<Number(m.start_date.val())){
				m.bm_date_box.addClass("has-error");
				_common.prompt("销售结束日不可小于销售开始日",5,"error");
			}else{
				m.bm_date_box.removeClass("has-error");
			}
		});
    	// 优惠方法事件
    	m.select_discounts.on("change",function(){
    		var value = $(this).val();
    		m.submitBut.prop("disabled",true);
    		if(value!=""){
    			if(value=="1"){
    				m.bm_dis.val("").prop("disabled",false).focus();
    				m.bm_dis_but.prop("disabled",false);
    				m.bm_price.val("").prop("disabled",true);
    				m.bm_price_but.prop("disabled",true);
    			}else{
    				m.bm_price.val("").prop("disabled",false).focus();
    				m.bm_price_but.prop("disabled",false);
    				m.bm_dis.val("").prop("disabled",true);
    				m.bm_dis_but.prop("disabled",true);
    			}
    		}else{
    			m.bm_price.val("").prop("disabled",true);
    			m.bm_dis.val("").prop("disabled",true);
    			m.bm_price_but.prop("disabled",true);
    			m.bm_dis_but.prop("disabled",true);
    		}
    	});
    	//计算折扣价格 事件
    	m.bm_dis_but.on("click",function(){
    		
    		var reg =  /((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
	   		 //var reg = /^[0-9]*$/;
	   		 if(!reg.test(bmdisVal)){
	   			_common.prompt("BM折扣只能录入0.01-99.99的正数。例如1折录入10，9折录入90",5,"error");
	   			m.bm_dis_box.addClass("has-error");
	   			m.bm_s.focus();
	   			return false;
	   		}
	   		 if(bmdisVal==""||bmdisVal<=0||bmdisVal>100){
	    			_common.prompt("BM折扣不能为空，且要大于0小于100",5,"error");
	    			m.bm_dis_box.addClass("has-error");
	    			m.bm_dis.focus();
	    			return false;
	    		}
    		 m.bm_dis_box.removeClass("has-error");
    		
    		var bmcount = m.bm_count.val();
    		if(bmcount==""||Number(bmcount)==0){
    			_common.prompt("BM数量不能为空,只能录入1~99的整数",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		if(bmcount<=1){
    			_common.prompt("BM数量必须大于1",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		m.bm_count_box.removeClass("has-error");
    		 
    		var storeObj = m.promotion_store.val().split(",");
    		var itemObj = m.items_input_list.val().split(",");
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
    			profitRate = 0;
    			if(disPrice==0){
    				profitRate = Number(0).toFixed(2);
    			}else{
    				profitRate = (((disPrice-cost_tax)/disPrice)*100).toFixed(2);
    			}
    			//BM销售价格：
        		// 01捆绑类型：BM销售价格=折扣销售单价*BM数量 显示为篮色。
    			bmPrice = _common.decimal((disPrice*bmcount),1);//真正的四舍五入
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
    		createTableTr_123();//创建table tr 的内容，向table_123操作
    		m.submitBut.prop("disabled",false);
    	});
    	//更新BM价格-按钮事件
    	m.bm_price_but.on("click",function(){
    		
    		var bmpriceVal = m.bm_price.val();
    		if(bmpriceVal==""||bmpriceVal<=0){
    			_common.prompt("BM商品价格不能为空且大于0",5,"error");
    			m.bm_price_box.addClass("has-error");
    			m.bm_price.focus();
    			return false;
    		}
    		var bm_price_val = m.bm_price.val();
    		var re=/^\d*\.{0,1}\d{0,1}$/;
    		if(!re.test(bm_price_val)){
    			_common.prompt("BM商品价格只能有一位小数",5,"error");
    			m.bm_price_box.addClass("has-error");
    			m.bm_price.focus();
    			return false;
    		}
    		m.bm_price_box.removeClass("has-error");
    		
    		
    		var bmcount = m.bm_count.val();
    		if(bmcount==""||Number(bmcount)==0){
    			_common.prompt("BM数量不能为空,只能录入1~99的整数",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		if(bmcount<=1){
    			_common.prompt("BM数量必须大于1",5,"error");
    			m.bm_count_box.addClass("has-error");
    			m.bm_count.focus();
    			return false;
    		}
    		m.bm_count_box.removeClass("has-error");
    		
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
		        			newDisprice = _common.decimal(newDisprice,1);//真正的四舍五入
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
    			var profit_rate = 0;
    			if(disprice==0){
    				profit_rate = Number(0).toFixed(2);
    			}else{
    				profit_rate = (((disprice-costtax)/disprice)*100).toFixed(2);
    			}
    			obj.attr("profitrate",profit_rate);
    		});
    		createTableTr_123();//创建table tr 的内容，向table_123操作
    		m.submitBut.prop("disabled",false);
    	});
    	//添加折扣
    	m.discount_rate_but.on("click",function(){
    		
    		//商品的折扣数量 最多32，最少2个
	   		 if(m.addDisCount_04.val()>=32){
	   			 _common.prompt("折扣最多添加32种",5,"error");
	   			 return false;
	   		 }
    		
    		//验证购买数量是否填入
    		var buy_count = m.buy_count.val();
    		if(buy_count==""||Number(buy_count)==0){
    			_common.prompt("请填入购买数量,只能输入整数",5,"error");
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
    		if(discount_rate==""||Number(discount_rate)==0){
    			_common.prompt("请填入折扣，只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
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
        		var profitrate = (((disprice-costtax)/disprice)*100).toFixed(2);
        		if(disprice==0){
        			profitrate = Number(0).toFixed(2);
    			}else{
    				profitrate = (((disprice-costtax)/disprice)*100).toFixed(2);
    			}
        		//开始计算 - BM销售价格=折扣单价x购买数量。
        		var bmprice = _common.decimal((disprice*buy_count),1);
        		bmprice = round_fmt(bmprice,2);//仅仅为了补个分位0
        		$(tempInput).attr("bmprice",bmprice).attr("buycount",buy_count).attr("discount",discount_rate).attr("disprice",disprice).attr("profitrate",profitrate);
    		});
    		createTableTr_4(true);
    		m.discount_rate.val("");
    		m.buy_count.val("").focus();
    		m.submitBut.prop("disabled",false);
    	});
    	// table列表删除按钮事件_仅仅针对 表格 4 进行操作
    	m.detail_table_4_tbody.on("click","a[class*='remove-tr']",function(){
    		var thisObj = $(this);
    		var buycount = thisObj.attr("buycount");
    		var inputObj = m.table_hidden_input.find("input[buycount='"+buycount+"']");
    		if(m.addDisCount_04.val()>1){
    			inputObj.remove();
    			var addDisCount_num = Number(m.addDisCount_04.val())-1;
    			m.addDisCount_04.val(addDisCount_num);
    		}else{
    			m.addDisCount_04.val(0);
    			inputObj.attr("bmprice","").attr("buycount","").attr("discount","").attr("disprice","").attr("profitrate","");
    		}
    		
    		createTableTr_4(false);
    	});
    	//计算折扣价格 ab
    	m.atwill_item_a_b_but.on("click",function(){
    		var reg = /^[0-9]*$/;
    		var atwill_item_a_count = m.atwill_item_a_count.val();
    		if(atwill_item_a_count==""||Number(atwill_item_a_count)==0){
    			_common.prompt("请填入A组购买数量,A组任意商品数量只能录入整数",5,"error");
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
    		if(atwill_item_b_count==""||Number(atwill_item_a_count)==0){
    			_common.prompt("请填入B组购买数量,B组任意商品数量只能录入整数",5,"error");
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
    		if(!reg.test(detail_a_b_discount)||Numver(detail_a_b_discount)==0){
    			_common.prompt("折扣只能录入1~99的整数。例如1折录入10，9折录入90",5,"error");
    			m.atwill_item_a_b_box.addClass("has-error");
    			m.detail_a_b_discount.focus();
    			return false;
    		}
    		if(detail_a_b_discount==""||detail_a_b_discount<0){
    			_common.prompt("请填入折扣",5,"error");
    			m.atwill_item_a_b_box.addClass("has-error");
    			m.detail_a_b_discount.focus();
    			return false;
    		}
    		m.atwill_item_a_b_box.removeClass("has-error");
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
    			var disprice = _common.decimal(((pricetax*detail_a_b_discount)/100),1);//真正的四舍五入
    			disprice = round_fmt(disprice,2);//仅仅为了补个分位0
    			thisInput.attr("disprice",disprice);
    			var profitrate = (((disprice-costtax)/disprice)*100).toFixed(2);
    			if(disprice==0){
    				profitrate = Number(0).toFixed(2);
    			}else{
    				profitrate = (((disprice-costtax)/disprice)*100).toFixed(2);
    			}
    			thisInput.attr("profitrate",profitrate);
    		});
    		//赋值完成后 重新生产table
			createTableTr_5(false);//根据现有的input创建table结构
			m.submitBut.prop("disabled",false);
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
    	
    	//返回一览
    	m.returnsViewBut.on("click",function(){
    		top.location = url_left;
    	});
    	//提交
    	m.submitBut.on("click",function(){
    		
    		//每日17:00后不允许采购员进行Add、修改、删除的提交。  
    		//identity
//    		if(m.identity.val()=="1"){
//    			var nowTime = Number(new Date().Format("hhmmss"));
//    			if(nowTime>=1700){
//    				_common.prompt("每日17:00后不允许进行Add、修改、删除的提交",5,"error");
//    				return false;
//    			}
//    		}
    		
    		if(m.bm_type.val()==""){
    			_common.prompt("请选择BM类型",5,"error");
    			m.bm_type_box.addClass("has-error");
    			m.bm_type.focus();
    			return false;
    		}
    		m.bm_type_box.removeClass("has-error");
    		if(m.bm_code.val()==""){
    			_common.prompt("请输入BM编码",5,"error");
    			m.bm_code_box.addClass("has-error");
    			m.bm_code.focus();
    			return false;
    		}
    		m.bm_code_box.removeClass("has-error");
    		if(m.update_flg.val()==""){
    			_common.prompt("请选择操作区分",5,"error");
    			m.update_flg.addClass("has-error");
    			m.update_flg.focus();
    			return false;
    		}
    		m.update_flg_box.removeClass("has-error");
    		
    		//时间区分确认
    		var _bmEffTo = fmtStrToInt(m.end_date.val());//
    		if(Number(_bmEffTo)<Number(m.start_date.val())){
				m.bm_date_box.addClass("has-error");
				_common.prompt("销售结束日不可小于销售开始日",5,"error");
				m.end_date.focus();
				return false;
			}
			m.bm_date_box.removeClass("has-error");
			if(m.bm_type.val()=="04"){
				if(m.addDisCount_04.val()<2){
					_common.prompt("折扣不能少于2种",5,"error");
					m.buy_count_discount_box.addClass("has-error");
					m.buy_count.focus();
					return false;
				}
			}
			m.buy_count_discount_box.removeClass("has-error");
			
    		setDptFlg();
    		var temp = {
    				newNo:null,
    				baseBmList:getBaseBmCkObject(),//主档
    				bmItems:getBmItemsCkObject(),//明细
    				identity:m.identity.val(),//提交人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
    				pageType:1
    		}
    		//identity //提交人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
    		var _url = url_left+"/editsubmitpro";
    		if(m.identity.val()=="3"){
    			_url = url_left+"/editsubmitsys";
    		}
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
    	});
    	
    }
    var showResponse = function(data,textStatus, xhr){
//	  	  var resp = xhr.responseJSON;
//	  	  if( resp.result == false){
//	  		  top.location = resp.s+"?errMsg="+resp.errorMessage;
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
  //设定自DPT/跨DPT区分 0-自DPT   1-同一事业部跨DPT  2-不同事业部跨DPT
    var setDptFlg = function(){
    	//计算当前bm是否为跨部门，如果bm明细中的商品都是自己负责的则为0不是跨部门，否则就是跨部门1，只有在Addbm时才计算，其他类型的画面均为后台赋值
    	var inputs = m.table_hidden_input.find("input[status!='1']");
    	var vDate = "";
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
    /*
     * 创建bm基础信息，根据店铺数量创建
     */
    var getBaseBmCkObject = function(){
    	var bmCkList = new Array(),
    		stores = m.promotion_store.val().split(","),
    		_newNo = null,//登录序列号
    		_company = "095",//公司
    		_dpt = m.user_dpt.val(),//所属资源dpt，例如 有111和112的dpt，应小刚说法，将设置为119,
    		_buyer = m.user_id.val(),//操作人，Add时的操作人
    		_updateFlg = m.update_flg.val(),//修改类型
    		_bmType = m.bm_type.val(),//bm类型
    		_bmCode = m.bm_code.val(),//bm编码
    		_bmEffFrom =m.start_date.val(),//
    		_bmEffTo = fmtStrToInt(m.end_date.val()),//
    		_numA = 0,//05 AB组=A组购买数量 其它情况=0
    		_numB = 0,//05 AB组=B组购买数量 其它情况=0
    		_dptFlg = m.dptFlg.val(),//0-自DPT   1-同一事业部跨DPT  2-不同事业部跨DPT
    		_checkFlg = null,//审核状态
    		_rightFlg = "00000",//查看权限标志位
    		_opFlg = "",//操作类型01-采购员提交，07-采购员确认BM明细单品，08-采购员驳回BM明细单品，17-商品部长审核通过，18-商品部长驳回，21-系统部提交，27-系统部审核通过，28-系统部驳回
    		_dptAll = getDpts(),//逗号分隔所有商品的dpt
    		_newFlg = "1",//0-Add 1-修改 2-删除
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
            	if(tempBmPrice!=""&&tempBmPrice!=""){
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
    			firstFlg:"0",
    			dptAll:_dptAll,
    			newFlg:_newFlg,
    			updateFlg:_updateFlg//0-修改BM价格/折扣 1-修改BM生效期间 2-修改价格和期间
    		};
    		bmCkList.push(bmCk);
    	});
    	return bmCkList;
    }
    //设定返回的bm数据
    var settingData = function(data){
    	if(data!=null){
    		//主档数据
    		var baseBm = data.baseBm;
    		m.start_date_text.text(fmtIntDate(baseBm.bmEffFrom+""));
    		m.start_date.val(baseBm.bmEffFrom);
    		m.end_date.val(fmtIntDate(baseBm.bmEffTo+""));
    		var stroesHtml = '';
    		$.each(data.stroes,function(index,node){
    			stroesHtml+='<span class="text-muted p-line-height item-li">'+node+'</span>';
    		});
    		m.promotion_show_store.html(stroesHtml);
    		//额外数据
    		m.detail_box.show();
    		
    		m.items_input_list.val(data.itemCodeStr);
    		m.promotion_store.val(data.stroeStr);
    		//明细档
    		var baseItems = data.bmItemInput;
    		var itemInputHtml = '';
    		var itemCodeAndDpt = new Set();
    		var myCheckResources = m.myCheckResources.val();
    		var cDpt = myCheckResources.substring(0,2);
    		$.each(baseItems,function(index,node){
    			var nodeDpt = node.dpt;
    			var _status = node.status;
    			if(myCheckResources=="999"){
    				//0：未确认  1：已确认  2：驳回
    				_status = 1;
    			}else{
    				var subDpt = nodeDpt.substring(0,2);
    				if(cDpt==subDpt){
    					_status = 1;
    				}else{
    					_status = 0;
    				}
    			}
    			var costTax = round_fmt(node.costtax,2);//进货单价
    			var priceTax = round_fmt(node.pricetax,2);//销售单价
    			itemInputHtml+='<input type="hidden" '+
    					'id="in_'+node.id+'" '+
    					'store="'+node.store+'" '+
    					'bmcode="'+node.bmcode+'" '+
    					'itemsystem="'+node.itemsystem+'"'+ 
    					'itemcode="'+node.itemcode+'" '+
    					'itemname="'+node.itemname+'" '+
    					'dpt="'+nodeDpt+'" '+
    					'costtax="'+costTax+'"'+ 
    					'pricetax="'+priceTax+'"'+ 
    					'status="'+_status+'" '+
    					'bmprice="'+(node.bmprice!=null?node.bmprice.toFixed(2):"")+'"'+ 
    					'disprice="'+(node.disprice!=null?node.disprice.toFixed(2):"")+'" '+
    					'profitrate="'+(node.profitrate!=null?node.profitrate.toFixed(2):"")+'"'+ 
    					'buycount="'+(node.buycount!=null?node.buycount:"")+'" '+
    					'discount="'+(node.discount!=null?node.discount:"")+'" '+
    					'ab="'+((node.ab||"").toUpperCase())+'" '+
    					'value="'+node.value+'" />';
    			itemCodeAndDpt.add(node.value+"-"+node.dpt);
    		});
			m.item_code_dpt.val(_common.setToArray(itemCodeAndDpt).join(","));
    		
    		m.table_hidden_input.html(itemInputHtml);
    		
    		switch(m.bm_type.val()) {
    		case "01":
    		case "02":
    		case "03":
    			if(m.bm_type.val()=="02"){
    				m.select_discounts.find("option[value='2']").hide();
    			}else{
    				m.select_discounts.find("option[value='2']").show();
    			}
    			m.bm_count.val(data.bmCount);
    			m.bm_dis.val(data.bmDiscount);
    			m.edit_detail_box_123.show();
    			m.detail_table_123_box.show();
    			createTableTr_123();
    			break;
    		case "04":
    			m.buy_count.val("");
    			m.discount_rate.val("");
    			m.edit_detail_box_4.show();
    			m.detail_table_4_box.show();
    			createTableTr_4(true);
    			break;
    		case "05":
    			m.atwill_item_a_count.val(data.numA);
    			m.atwill_item_b_count.val(data.numB);
    			m.detail_a_b_discount.val(data.bmDiscount);
    			
    			m.edit_detail_box_5.show();
    			m.detail_table_5_box.show();
    			createTableTr_5(true);
    			break;
    		default:
    		}
    		
    	}else{
    		m.detail_box.hide();
    	}
    }
    
    //根据编码和类型得到正是表的bm数据
    var getData = function(bmCode,bmType){
    	_data = "bmCode="+bmCode+"&bmType="+bmType;
    	var _restData = null;
    	$.myAjaxs({
			  url:url_left+"/getdatabycodetype",
			  async:false,
			  cache:false,
			  type :"get",
			  data :_data,
			  dataType:"json",
			  success:function(rest){
				  _restData = rest;
			  },
			  complete:_common.myAjaxComplete
		  }); 
    	return _restData;
    }
    
  //创建table tr 内容 向 5表格进行操作
    var createTableTr_5 = function(flg){
    	m.detail_table_5_tbody.empty();
    	//得到店铺集合
    	var stores = m.promotion_store.val().split(",");
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
    		if(i==0&&flg){
    			var aSet = new Set();
    			$.each(inputs_a,function(index_a,node_a){
    				aSet.add($(node_a).val());
    			});
    			var bSet = new Set();
    			$.each(inputs_b,function(index_b,node_b){
    				bSet.add($(node_b).val());
    			});
    			m.items_input_a_list.val(_common.setToArray(aSet).join(","));
    			m.items_input_b_list.val(_common.setToArray(bSet).join(","));
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
		    			// 折扣销售单价 如 ≤ 进价，显示为红色。
		    			profitrateClass = "text-red";
		    		}	
		    		var dispriceClass = "";
		    		if(Number(disprice)<=Number(costtax)){
		    			// 折扣销售单价 如 ≤ 进价，显示为红色。
		    			dispriceClass = "text-red";
		    		}
					eachTr +='<td class="text-l">'+itemcode+'</td>';
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
				for(var i=0;i<10;i++){
					eachTr +='<td>&nbsp;</td>';
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
				for(var i=0;i<10;i++){
					eachTr +='<td>&nbsp;</td>';
				}
				eachTr +='</tr>';
			}
			allTr+=eachTr
    	});
    	m.detail_table_5_tbody.append(allTr);
    }
    
  //创建table tr 内容 向 4表格进行操作
    var createTableTr_4 = function(flg){
    	m.detail_table_4_tbody.empty();
    	//得到店铺集合
    	var stores = m.promotion_store.val().split(",");
    	// 循环店铺集合 开始金额tr拼接
    	$.each(stores,function(i,node){
    		var no = i+1;
    		//拿到属于当前店铺的所有单品集合
    		var inputs = m.table_hidden_input.find("input[store='"+node+"']");
    		var rowspan = inputs.length;//数量 涉及到合并行
    		if(i==0&&flg){
    			m.addDisCount_04.val(rowspan);
    		}
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
	    		if(bmprice!=""||bmprice!="&nbsp;"){
	    			// 有值就是蓝色
	    			bmpriceClass = "text-blue";
	    		}
	    		var dispaly_none = "display:none;";
	    		if(m.update_flg.val()!=""&&update_flg!="1"){
	    			dispaly_none = "";
	    		}
	    		
	    		eachTr +='<td class="text-r '+bmpriceClass+'">'+bmprice+'</td>';
	    		eachTr +='<td class="text-l">'+itemcode+'</td>';
	    		eachTr +='<td class="text-c"><a updateflg="0" href="javascript:void(0);" style="'+dispaly_none+'" buycount="'+buycount+'"  itemcode="'+itemcode+'" class="glyphicon glyphicon-remove-sign remove-tr"  ></a></td>';
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
    	m.detail_table_123_tbody.empty();
    	//得到店铺集合
    	var stores = m.promotion_store.val().split(",");
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
		    		if(bmprice!=""||bmprice!="&nbsp;"){
		    			// 有值就是蓝色
		    			bmpriceClass = "text-blue";
		    		}
	    			eachTr +='<td rowspan="'+rowspan+'"  class="text-r '+bmpriceClass+'">'+bmprice+'</td>';
	    		}	
	    		eachTr +='<td class="text-l">'+itemcode+'</td>';
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
    
    //修改画面标砖的初始化事件
    var initEffects = function(){
    	m.bm_code.val("");
    	m.update_flg.val("");
    	m.select_discounts.val("").change();
    	m.start_date_text.html("&nbsp;");
    	m.start_date.val("");
    	m.end_date.val("").prop("disabled",true);
    	m.promotion_show_store.empty();
    	m.bm_code.prop("disabled",true);
    	m.submitBut.prop("disabled",true);
    	m.update_flg.prop("disabled",true);
    	m.main_box.find("*[updateflg]").prop("disabled",true);
    	m.detail_box.hide();
    	m.edit_detail_box_123.hide();
    	m.edit_detail_box_4.hide();
    	m.edit_detail_box_5.hide();
    	m.detail_box.find("div[class*='hide-table']").hide();
    	//AUTO
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
	//四舍五入,v=值，p小数点
	var round_fmt = function(v,p){
		if(v==null||v==""){return "";}
		return Number(v).toFixed(p);
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('bmUpdate');
_start.load(function (_common) {
	_index.init(_common);
});
