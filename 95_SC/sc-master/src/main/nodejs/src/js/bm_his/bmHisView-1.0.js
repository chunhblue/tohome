require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("myAutomatic");
var _myAjax=require("myAjax");
var _datetimepicker = require("datetimepicker");
define('bmHisView', function () {
    var self = {},
	    url_left = "",
    	_common=null;
    //在此处添加的对象 全都变为jquery对象
    var m = {
    		returnsViewBut:null,
    		bmType:null,
    		bmTypeText:null,
    		pass:null,
    		notPass:null,
    		bmCode:null,
    		tableTbody:null,
    		promotion_store:null,
    		table_hidden_input:null,
    		identity:null,
    		canReview:null,
    		checkResources:null,
    		allItemAndDpt:null,
    		myCheckResources:null,
    		reject_dialog_affirm:null,
    		reject_dialog_cancel:null,
    		toKen:null,
    		affirm:null,
    		check:null,
    		reject_aff:null,
    		reject:null,
    		checkFlg:null,
    		rejectreason:null,
    		reject_dialog:null,
    		startDate:null,
    		endDate:null,
    		n:null
    };
    // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(common) {
    	_common = common;
    	url_left=_common.config.surl+"/bmhistory";
    	createJqueryObj();
    	//返回一览
    	m.returnsViewBut.on("click",function(){
    		top.location = url_left;
    		//window.history.go(-1);
    	});
    	m.bmTypeText.text(getBmType($.trim(m.bmTypeText.text())));
    	m.startDate.text(fmtIntDate($.trim(m.startDate.text())));
    	m.endDate.text(fmtIntDate($.trim(m.endDate.text())));
    	formattingTable();
    }
    //格式化明细数据
    var formattingTable = function(){
    	switch(m.bmType.val()) {
		case "01":
		case "02":
		case "03":
			createTableTr_123();
			break;
		case "04":
			createTableTr_4();
			break;
		case "05":
			createTableTr_5();
			break;
		} 
    }
    
  //创建table tr 内容 向 5表格进行操作
    var createTableTr_5 = function(){
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
    	m.tableTbody.append(allTr);
    }
    
  //创建table tr 内容 向 4表格进行操作
    var createTableTr_4 = function(){
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
	    		eachTr +='<td class="text-r '+bmpriceClass+'">'+bmprice+'</td>';
	    		eachTr +='<td class="text-l">'+itemcode+'</td>';
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
    		m.tableTbody.append(eachTr);
    	});
    
    	
    }
 // 创建table tr 的内容，向table_123操作
    var createTableTr_123 = function(){
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
    		m.tableTbody.append(eachTr);
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
	//返回 商品状态 0：未确认  1：已确认  2：驳回
	var getBmType = function(status){
		switch(status) {
		case "01":
			return "01 捆绑";
		case "02":
			return "02 混合";
		case "03":
			return "03 固定组合";
		case "04":
			return "04 阶梯折扣";
		case "05":
			return "05 AB组";
		default:
			return "未知状态";
		} 
	}
	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		return res;
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('bmHisView');
_start.load(function (_common) {
	_index.init(_common);
});
