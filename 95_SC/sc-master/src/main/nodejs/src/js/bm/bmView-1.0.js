require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("myAutomatic");
var _myAjax=require("myAjax");
var _datetimepicker = require("datetimepicker");
define('bmView', function () {
    var self = {},
	    url_left = "",
    	_common=null;
    //在此处添加的对象 全都变为jquery对象
    var m = {
    		toKen:null,
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
    		checkFlg:null,
    		rejectreason:null,
    		reject_dialog:null,
    		startDate:null,
    		endDate:null,
    		bmStatus:null,//前置页面选择的bm商品状态，新签约、已生效、手工删除。。。等等
    		n:null
    };
  //class对象
    var mclass = {
    		affirm:null,
    		reject_aff:null,
    		check:null,
    		reject:null,
    		returnsViewBut:null,
    		del:null
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
    	//返回一览
    	m.returnsViewBut.on("click",function(){
    		top.location = url_left;
    	});
    	formattingTable();
    	//判断当前数据是否需要评审，并且根据当前登录人的角色来显示特定按钮
    	isReview();
    	
    	
    	m.bmTypeText.text(getBmType($.trim(m.bmTypeText.text())));
    	
    	m.startDate.text(fmtIntDate($.trim(m.startDate.text())));
    	m.endDate.text(fmtIntDate($.trim(m.endDate.text())));
    	
    	//确认
    	m.affirm.on("click",function(){
    		commAffirmAndReject(m.pass.val());
    	});
    	
    	//驳回
    	m.reject_aff.on("click",function(){
    		m.reject_dialog.modal("show");
    		m.rejectreason.focus();
    		//commAffirmAndReject("08");
    	});
    	//审核
    	m.check.on("click",function(){
    		commCheckAndReject(m.pass.val());
    	});
    	
    	//驳回
    	m.reject.on("click",function(){
    		m.reject_dialog.modal("show");
    		m.rejectreason.focus();
    	});
    	
    	//是否显示删除按钮,如果不是手工删除 就不显示删除按钮
    	if(m.bmStatus.val()!="4"){
    		m.del.remove();
    	}
    	
    	//删除按钮
    	m.del.on("click",function(){
    		_common.myConfirm("请确认是否要删除当前BM？",function(result){
				if(result=="true"){
					var c_bmCode = m.bmCode.val();
					var c_bmType = m.bmType.val();
					var identity = m.identity.val();
					//var tableType = 0;//正是表数据，因为只有是手工删除时才有删除按钮，然而手工删除检索的数据是正是表数据所以直接设定0
					var _data = "bmType="+c_bmType+"&bmCode="+c_bmCode+"&tableType=0&identity="+identity+"&toKen="+m.toKen.val();
					$.myAjaxs({
						url:url_left+"/delbm",
						async:true,
						cache:false,
						type :"get",
						data :_data,
						dataType:"json",
						success:showResponse,
						complete:_common.myAjaxComplete
					}); 
				}
			});
    	});
    	//驳回弹出窗-确认按钮事件
    	m.reject_dialog_affirm.on("click",function(){
    		m.reject_dialog.modal("hide");
    		if(m.identity.val()=="1"){
    			commAffirmAndReject(m.notPass.val());
    		}else{
    			commCheckAndReject(m.notPass.val());
    		}
    	});
    	//驳回弹出窗-关闭按钮事件
    	m.reject_dialog_cancel.on("click",function(){
    		m.reject_dialog.modal("hide");
    		m.rejectreason.val("");
    	});
    }
    
    
    //审核和驳回
    var commCheckAndReject = function(opFlg){
    	var _data = "bmCode="+m.bmCode.val()+
	    	"&bmType="+m.bmType.val()+
	    	"&staffResource="+m.myCheckResources.val()+
	    	"&opFlg="+opFlg+
	    	"&rejectreason="+(m.rejectreason.val()||"")+
	    	"&identity="+m.identity.val()+
	    	"&toKen="+m.toKen.val();
    	$.myAjaxs({
    		url:url_left+"/checkandreject",
    		async:false,
    		cache:false,
    		type :"get",
    		data :_data,
    		dataType:"json",
    		success:showResponse,
    		complete:_common.myAjaxComplete
    	}); 
    }
    //确认和驳回
    var commAffirmAndReject = function(opFlg){
    	var _data = "bmCode="+m.bmCode.val()+
					"&bmType="+m.bmType.val()+
					"&staffResource="+m.myCheckResources.val()+
					"&dataResource="+m.allItemAndDpt.val()+
					"&opFlg="+opFlg+
					"&rejectreason="+(m.rejectreason.val()||"")+
					"&identity="+m.identity.val()+
					"&toKen="+m.toKen.val();
		$.myAjaxs({
			url:url_left+"/affirmbmandreject",
			async:false,
			cache:false,
			type :"get",
			data :_data,
			dataType:"json",
			success:showResponse,
			complete:_common.myAjaxComplete
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
  //判断当前数据是否需要评审，并且根据当前登录人的角色来显示特定按钮
    var isReview = function(){
    	
    	if(m.canReview.val()=="0"){
    		
    		//操作人的资源与数据中可以审核的资源是否有匹配项，如果有则返回true
    		var isReviewStatus = false;
    		//操作人资源
    		var myCheckResourcesStr = m.myCheckResources.val().replace(/9/g,"");
    		var myCheckResourcesObj = myCheckResourcesStr.split(",");
    		//数据资源
    		var checkResourcesStr = m.checkResources.val().replace(/9/g,"");
    		var checkResourcesObj = checkResourcesStr.split(",");
    		if(myCheckResourcesObj.length>0){
    			for(var i=0;i<myCheckResourcesObj.length;i++){
    				var temp = myCheckResourcesObj[i];
    				var lastIndex = temp.length;
    				for(var j=0;j<checkResourcesObj.length;j++){
    					if(temp == checkResourcesObj[j].substring(0,lastIndex)){
    						isReviewStatus = true;
    						break; 
    					}
    				}
    				if(isReviewStatus){
    					break;
    				}
    			}
    		}else{
    			//操作人自愿999 视为系统部人员，直接可以审核
    			isReviewStatus = true;
    		}
    		//需要评审
    		switch(m.identity.val()) {
    		case "1"://采购身份
    			//4-采购员确认中,才显示按钮
    			if(m.checkFlg.val()=="4"&&isReviewStatus){
    				//采购-需要确认按钮,验证资源
    				m.affirm.show();
    				m.reject.show();
    			}
    			break;
    		case "2"://事业部长（商品部长）
    			//0-所有采购员均已确认，商品部长未审核,才显示按钮
    			if(m.checkFlg.val()=="0"&&isReviewStatus){
    				// 事业部长（商品部长)
    				m.check.show();
    				m.reject.show();
    			}
    			break;
    		case "3"://系统部
    			//1-商品部长已审核（通过），系统部未审核,才显示按钮
    			if(m.checkFlg.val()=="1"&&isReviewStatus){
    				// 系统部需要审核按钮
    				m.check.show();
    				m.reject.show();
    			}
    			break;
    		} 
    	}else{
    		//不需要
    		m.affirm.hide();
    		m.check.hide();
    		m.reject.hide();
    	}
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
var _index = require('bmView');
_start.load(function (_common) {
	_index.init(_common);
});
