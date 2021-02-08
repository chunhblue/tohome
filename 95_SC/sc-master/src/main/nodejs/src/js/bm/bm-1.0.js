require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('bm', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
    var m = {
    		toKen : null,
    		use : null,
    		up : null,
    		entiretyBmStatus : null,
    		clear_sell_date : null,
    		item_name : null,
    		bm_division : null,
    		bm_department : null,
        	tableGrid : null,
        	selectTrObj : null,
        	error_pcode : null,
        	identity : null,
        	reject_dialog : null,
        	show_user_info : null,//检索中用户部分
        	show_status_all : null,//bm商品状态-全部单选按钮
        	show_status_del : null,//手工删除单选按钮
        	show_status_reject : null,//驳回选按钮
        	show_select_div : null,//事业部auto
        	show_select_dpt : null,//DPTauto
        	bm_be_part : null,//BM所属下拉项
        	show_div : null,//操作人员信息-事业部
        	show_store : null,//操作人员信息-店铺
        	show_status : null,//BM商品状态
        	bm_be_part_div : null,//bm所属
        	bm_type_box : null,//bm类型-box
        	bm_type : null,//bm类型
        	show_bm_code_input : null,
        	input_item : null,
        	division : null,
        	dpt: null,
        	sell_start_date: null,
        	sell_end_date: null,
        	alert_div : null,//页面提示框
        	search : null,//检索按钮
        	main_box : null,//检索按钮
        	p_code_buyer_del : null,
        	p_code_excel : null,
        	p_code_buyer_affirm : null,
        	rejectreason : null,
        	reject_dialog_cancel : null,
        	reject_dialog_affirm : null,
        	store : null,
        	search_item_but : null,
        	p_code_div_check:null,
        	p_code_sys_del:null,
        	p_code_sys_list_del:null,
        	checkResources:null,
        	tempTableType:null,
        	searchJson:null,
        	checkCount:null,
        	urgencyCount:null,
        	del_alert:null,
        	bm_status_5_select : null//bm商品状态下拉项
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left=_common.config.surl+"/order";
    	//首先验证当前操作人的权限是否混乱，
		console.log(m.use.val())
    	if(m.use.val()=="0"){
    		but_event();
    	}else{
    		m.error_pcode.show();
    		m.main_box.hide();
    	}
    	//根据登录人的不同身份权限来设定画面的现实样式
		console.log(m.identity.val())
    	initPageBytype(m.identity.val());
    	//表格内按钮事件
    	table_event();
    	m.search.click();
    }

    
    var table_event = function(){
    	
    	$("#outExcel").on("click",function(){
    		//setParamJson();
			paramGrid = "searchJson="+m.searchJson.val();
    		var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
    	});
    	
    	//view
    	m.main_box.on("click","a[class*='view']",function(){
    		var bmCode = $(this).attr("bmcode");
    		var bmtype = $(this).attr("bmtype");
    		var tabletype = $(this).attr("tabletype");
    		var identity = m.identity.val();
    		var status = m.entiretyBmStatus.val();
    		//xx
    		top.location = url_left+"/view?&bmCode="+bmCode+"&bmType="+bmtype+"&tabletype="+tabletype+"&identity="+identity+"&status="+status;
    	})
    	//删除
    	$("#del").on("click",function(){
    		//每日17:00后不允许采购员进行Add、修改、删除的提交。
    		//identity
//    		if(m.identity.val()=="1"){
//    			var nowTime = Number(new Date().Format("hhmmss"));
//    			if(nowTime>=1700){
//    				_common.prompt("每日17:00后不允许进行Add、修改、删除的提交",5,"error");
//    				return false;
//    			}
//    		}
    		
    		if(selectTrTemp==null){
    			// _common.prompt("请选择要删除的BM数据",5,"error");
    			_common.prompt("Please select BM data to delete!",5,"error");
    			return false;
    		}
    		_common.myConfirm("Please confirm whether to delete the selected BM!？",function(result){
				if(result=="true"){
					var cols = tableGrid.getSelectColValue(selectTrTemp,"bmCode,bmType");
					var c_bmCode = cols['bmCode'];
					var c_bmType = cols['bmType'];
					var identity = m.identity.val();
					
					$.myAjaxs({
						url:url_left+"/delbm",
						async:true,
						cache:false,
						type :"get",
						data :"bmType="+c_bmType+"&bmCode="+c_bmCode+"&tableType="+m.tempTableType.val()+"&identity="+identity+"&toKen="+m.toKen.val(),
						dataType:"json",
						success:showResponse,
						complete:_common.myAjaxComplete
					}); 
				}
			});
    		
    	});
    	var table_ck_all = $("#table_ck_all");
    	table_ck_all.on("change",function(event){
    		event.preventDefault();
    		var ck = $(this).prop("checked");
    		$("#zgGridTtable_tbody").find("input[type='checkbox']").prop("checked",ck);
    	});
    	//审核
    	$("#check").on("click",function(){
    		var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
    		if(selectObj==null||selectObj.length==0){
    			 _common.prompt("Please select a record first!",5,"error"); // 请勾选要操作的数据
    		}else{
    			checkOrRejectClick(1,null,selectObj);
    		}
    	});
    	//驳回
    	$("#reject").on("click",function(){
    		var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
    		if(selectObj==null||selectObj.length==0){
    			 _common.prompt("Please select a record first!",5,"error"); // 请勾选要操作的数据
    			return false;
    		}else{
    			m.reject_dialog.modal("show");
    			m.rejectreason.focus();
    		}
    	});
    	//驳回弹出窗-确认按钮事件
    	m.reject_dialog_affirm.on("click",function(){
    		m.reject_dialog.modal("hide");
    		var text = m.rejectreason.val();
    		var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
    		checkOrRejectClick(2,text,selectObj);
    	});
    	//驳回弹出窗-关闭按钮事件
    	m.reject_dialog_cancel.on("click",function(){
    		m.reject_dialog.modal("hide");
    		m.rejectreason.val("");
    	});
    	
    	//批量删除过期商品
    	$("#listDel").on("click",function(){
    		var date = new Date();
    		var endDateStr = _common.dateFormat(date,"yyyy-MM-dd");
    		var endDate = _common.dateFormat(date,"yyyyMMdd");
    		// _common.myConfirm("请确认是否需要删除过期BM<br>本次将要删除 销售结束日小于“"+endDateStr+"”的BM数据<br>删除后将无法恢复",function(result){
			_common.myConfirm("Please confirm whether it is necessary to delete expired BM<br>BM data whose sales end date is less than“"+endDateStr+"”will not be recovered after deletion!",function(result){
				if(result=="true"){
					$.myAjaxs({
						url:url_left+"/dellistbm",
						async:true,
						cache:false,
						type :"get",
						data :"endDate="+endDate+"&toKen="+m.toKen.val(),
						dataType:"json",
						success:showResponse,
						complete:_common.myAjaxComplete
					}); 
				}
			});
    	});
    	
    }
    
    //审核和驳回的按钮事件
    var checkOrRejectClick = function(_flg,_rejectreason,selectObj){
		var _selectList = new Array();
		var bmTypeCode = "";
		$.each(selectObj,function(ix,node){
			_selectList.push($(node).attr("bmcode")+"-"+$(node).attr("bmtype"));
		});
		bmTypeCode = _selectList.join(',');
		_rejectreason= _rejectreason||"";
		_flg = _flg==1?"17":"18";
		
		_data = "bmTypeCode="+bmTypeCode+
				"&rejectreason="+_rejectreason+
				"&opFlg="+_flg+
				"&staffResource="+m.checkResources.val()+
				"&identity="+m.identity.val()+
				"&toKen="+m.toKen.val();
		
		$.myAjaxs({
			  url:url_left+"/checkandreject",
			  async:true,
			  cache:false,
			  type :"get",
			  data :_data,
			  dataType:"json",
			  success:showResponse,
			  complete:_common.myAjaxComplete
		  }); 
    }
    
    
    var showResponse = function(data,textStatus, xhr){
    	selectTrTemp = null;
	  	  var resp = xhr.responseJSON;
	  	  if( resp.result == false){
	  		  top.location = resp.s+"?errMsg="+resp.errorMessage;
	  		  return ;
	  	  }
	  	  if(data.success==true){
	  		  _common.prompt("Operation Succeeded!",2,"success",function(){
	  			m.search.click();
	  		  },true);
	  	  }else{
	  		  _common.prompt(data.message,5,"error");
	  	  }
	  	  m.toKen.val(data.toKen);
	    }
    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
    	
    	switch(flgType) {
    	case "1":
    		m.show_div.show();
    		m.show_store.hide();
    		m.show_status_all.hide();
    		m.show_status_reject.show();
    		m.show_user_info.show();
    		m.show_select_div.hide();
    		m.show_select_dpt.hide();
    		initTable1();//列表初始化
    		//列表按钮
    		$("#show_status").find("input[type='radio']").eq(1).click();
    		if(m.p_code_buyer_del.val()!=1){
    			$("#del").remove();
    			m.del_alert.hide();
    		}
    		break;
        case "2":
        	m.show_div.hide();
        	m.show_store.hide();
        	m.show_user_info.hide();
        	m.show_status_del.hide();
        	m.show_status_reject.hide();
        	m.alert_div.show();
        	initTable2();//列表初始化
    		$("#show_status").find("input[type='radio']").eq(0).click();
    		if(m.p_code_div_check.val()!=1){
    			$("#check").remove();
    			$("#reject").remove();
    		}
           break;
        case "3":
        	m.show_div.hide();
        	m.show_store.hide();
        	m.show_status_all.hide();
        	m.show_status_reject.hide();
        	m.show_user_info.hide();
        	m.bm_be_part.prepend('<option value=""></option>');
        	m.bm_be_part.val("");
        	m.bm_be_part.attr("disabled","disabled");
        	m.alert_div.show();
        	initTable3();//列表初始化
        	$("#show_status").find("input[type='radio']").eq(1).click();
        	$("#del").hide();
        	m.del_alert.hide();
        	//列表按钮
        	if(m.p_code_sys_del.val()!=1){
    			$("#del").remove();
    		}
        	break;
        case "4":
        	m.show_div.hide();
        	m.show_store.show();
        	m.show_status_reject.hide();
        	m.show_status.hide();
        	m.show_user_info.show();
        	m.show_select_dpt.hide();
        	m.bm_be_part_div.hide();
        	m.bm_type_box.show();
        	initTable4();//列表初始化
        	break;
        default:
        	m.error_pcode.show();
        	m.main_box.hide();
	   } 
    	//验证权限设定是否存在 按钮
		if(m.p_code_excel.val()!="1"){
			//没有excel权限
			$("#outExcel").remove();
		}
    }
    
    //画面按钮点击事件
    var but_event = function(){
    	//BM商品状态按钮事件
    	$("#show_status").find("input[type='radio']").on("change",function(){
    		var thisObj = $(this);
    		var thisVal = thisObj.val();
    		if(thisVal=="5"){//修改
    			m.bm_status_5_select.removeAttr("disabled");
    			m.bm_status_5_select.find("option[value='-1']").text("-- All/Please Select --").prop("selected",true);
    			m.bm_status_5_select.focus();
    		}else{
    			m.bm_status_5_select.prop("disabled","disabled");
    			m.bm_status_5_select.find("option[value='-1']").text("").prop("selected",true);
    		}
    	});
    	//清空日期
    	m.clear_sell_date.on("click",function(){
    		m.sell_start_date.val("");
    		m.sell_end_date.val("");
    	});
    	//销售开始日
    	m.sell_start_date.datetimepicker({
    		 language:'en',
    		 format: 'yyyy-mm-dd',
    		 maxView:4,
    		 startView:2,
    		 minView:2,
    		 autoclose:true,
    		 todayHighlight:true,
    		 todayBtn:true
		});
    	//销售结束日
    	m.sell_end_date.datetimepicker({
    		language:'en',
    		format: 'yyyy-mm-dd',
    		maxView:4,
    		startView:2,
    		minView:2,
    		autoclose:true,
    		todayHighlight:true,
    		todayBtn:true
    	});
    	
    	//Add按钮事件
    	m.main_box.on("click","#add",function(){
    		top.location = url_left+"/edit?identity="+m.identity.val();
    	})
    	//修改按钮
    	m.up.on("click",function(){
    		top.location = url_left+"/bmupdate?identity="+m.identity.val();
    	});
    	
    
    	//事业部下拉事件
    	m.bm_division.on("change",function(){
    		var thisObj = $(this);
    		var selectVal = thisObj.val();
    		var htmlStr = '<option value="">-- All/Please Select --</option>';
    		if(selectVal!=""){
    			$.myAjaxs({
    				  url:url_left+"/getdepartments",
    				  async:false,
    				  cache:false,
    				  type :"post",
    				  data :"division="+selectVal+"&identity="+m.identity.val(),
    				  dataType:"json",
    				  success:function(res){
    					  $.each(res,function(ix,node){
    						  htmlStr+='<option value="'+node.k+'">'+node.k+' '+node.v+'</option>';
    					  });
    					  m.bm_department.html(htmlStr);
    				  },
    				  complete:_common.myAjaxComplete
    			  }); 
    		}else{
    			m.bm_department.html(htmlStr);
    		}
    		m.bm_department.val("");
    	});
    	
    	//检索按钮点击事件
    	m.search.on("click",function(){
    		if(verifySearch()){
    			//BM商品状态
    			var tempRadioValue = m.show_status.find("input[type='radio']:checked").val();
    			m.entiretyBmStatus.val(tempRadioValue);
    			//拼接检索参数
    			setParamJson();
    			paramGrid = "searchJson="+m.searchJson.val();
    			tableGrid.setting("url",url_left+"/getdata");
    			tableGrid.setting("param", paramGrid);
    			tableGrid.setting("page", 1);
    			tableGrid.loadData(null);
    			
    			//各个角色的不同检索bm商品状态的按钮显示
    			switch(m.identity.val()) {
    			case "1":
    				//采购
    				if(m.entiretyBmStatus.val()!=3){
    					$("#del").prop("disabled",true).show();
    					m.del_alert.show();
    				}else{
    					$("#del").hide();
    					m.del_alert.hide();
    				}
    				break;
    			case "2":
    				//商品部长
    				if(m.entiretyBmStatus.val()!=3){
    					$("#check").show();
    					$("#reject").show();
    				}else{
    					$("#check").hide();
    					$("#reject").hide();
    				}
    				break;
    			case "3":
    				if(m.entiretyBmStatus.val()==4){
    					$("#del").prop("disabled",true).show();
    					m.del_alert.show();
    				}else{
    					$("#del").hide();
    					m.del_alert.hide();
    				}
    				break;
    			} 
    			$("#outExcel").prop("disabled",false);
    		}else{
    			$("#outExcel").prop("disabled",true);
    		}
    		//判断当前操作人是否为事业部长或系统部，如果是，曾增加待审核数量等内容的刷新机制，其他身份不参与此操作
    		if(m.identity.val()=="2"||m.identity.val()=="3"){
    			refreshWaitingReview();
    		}
    		
    	});
    	
    	// Item Barcode输入口 焦点离开事件
    	m.input_item.on("blur",function(){
			if($(this).val()==""){
				m.input_item.attr("status","0");
				m.item_name.text("");
			}
    	});
    	m.input_item.on("keydown",function(){
    		if(event.keyCode == "13"){
    			var thisObj = $(this);
        		if(thisObj.val()==""){
        			m.item_name.text("");
        		}else{
        			m.search_item_but.click();
        		}
    		}
    	});
    	//点击查找Item Barcode按钮事件
    	m.search_item_but.on("click",function(){
    		var inputVal = m.input_item.val();
    		m.input_item.attr("status","0");
    		itemTempObj = null;
    		m.item_name.text("");
    		if($.trim(inputVal)==""){
    			_common.prompt("Item Barcode cannot be empty!",5,"error");
    			m.input_item.attr("status","2").focus();
    			return false;
    		}
    		var reg = /^[0-9]*$/;
    		if(!reg.test(inputVal)){
    			_common.prompt("Item Barcode must be a number!",5,"error");
    			m.input_item.attr("status","2").focus();
    			return false;
    		}
    		m.item_name.text("Loading...");
    		getItemInfoByItem1(inputVal,function(res){
    			if(res.success){
    				m.item_name.text($.trim(res.data.itemName));
    				m.input_item.attr("status","1");
    				return true;
    			}else{
    				m.item_name.text("Item Name is not obtained!");// 未取得Item Name
    				m.input_item.attr("status","2");
    				_common.prompt(res.message,5,"error");
        			return false;
    			}
    		});
    		
    	});
    }
    
    //刷新待审核数量
    var refreshWaitingReview = function(){
    	$.myAjaxs({
			  url:url_left+"/refreshWaitingReview",
			  async:true,
			  cache:false,
			  type :"get",
			  data:"identity="+m.identity.val(),
			  dataType:"json",
			  success:function(rest){
				  if(rest.success){
					  m.checkCount.text(rest.data.checkCount);
					  m.urgencyCount.text(rest.data.urgencyCount);
				  }else{
					  m.checkCount.text(0);
					  m.urgencyCount.text(0);
				  }
			  },
			  complete:_common.myAjaxComplete
		  }); 
    }
    
    //验证检索项是否合法
    var verifySearch = function(){
    	var tempRadioValue = m.show_status.find("input[type='radio']:checked").val();
		if(tempRadioValue=="5"){
			if(m.bm_status_5_select.val()==""){
				_common.prompt("Please select the modification type!",5,"error");
				m.bm_status_5_select.focus();
				return false;
			}
		}
		if(m.sell_start_date.val()!=""&&m.sell_end_date.val()!=""){
			var intStartDate = fmtStrToInt(m.sell_start_date.val());
			var intEndDate = fmtStrToInt(m.sell_end_date.val());
			if(intStartDate>intEndDate){
				_common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
				return false;
			}
		}
		var inputVal = m.show_bm_code_input.val();
		if(inputVal!=""){
			var reg = /^[0-9]*$/;
			if(!reg.test(inputVal)){
				_common.prompt("BM codes must be a number!",5,"error");
				 m.show_bm_code_input.focus();
				return false;
			}
		}
		var inputVal = m.input_item.val();
		if(inputVal!=""){
			var status = m.input_item.attr("status");
			if(status=="2"){
				_common.prompt("Incorrect item code, please enter again!",5,"error");
				m.input_item.focus();
				return false;
			}
		}
    	return true;
    }
    
    //拼接检索参数
    var setParamJson = function(){
    	var _newFlg = null;
		var _updateFlg = null;
		var _checkFlg= null;
		//数据来源表 0 正是表，1ck表，2历史表
		var _tableType = 0;
 		switch(m.entiretyBmStatus.val()) {
		case "1":
			//全部
			_checkFlg = "0";//0-所有采购员均已确认，商品部长未审核
			_tableType = 1;
			break;
		case "2":
			//新签约 ck
			_newFlg = "0";//0-Add 1-修改 2-删除
			if(m.identity.val()=="2"){
				//如果是 事业部长（商品部长）
				_checkFlg = "0";
			}else if(m.identity.val()=="3"){
				//如果是 系统部
				_checkFlg = "1";
			}
			_tableType = 1;
			break;
		case "3":
			//已经生效 正式
			_tableType = 0;
			break;
		case "4":
			//手工删除 正式
			_tableType = 0;
			break;
		case "5":
			//修改 ck
			_newFlg = "1";
			_updateFlg = m.bm_status_5_select.val();//0-修改BM价格/折扣 1-修改BM生效期间 2-修改价格和期间
			_tableType = 1;
			break;
		case "6":
			//驳回 ck
			_checkFlg = "3"; //3-已驳回(包括采购员驳回，部长驳回，系统部驳回)
			_tableType = 1;
			break;
		default:
			//已经生效 正式
			_tableType = 0;
		} 
 		//事业部
 		var _div = m.bm_division.val()||null;
 		//dpt
 		var _dpt = m.bm_department.val()||null;
		//bm所属
 		var _bmBePart = m.bm_be_part.val()||null;
		//bm类型
 		var _bmType = m.bm_type.val()||null;
 		
 		//bm编码
 		var _bmCode = m.show_bm_code_input.val()||null;
 		bmCodeOrItem = 0;
 		//单品条码是否被选中
 		if($("#bm_item").is(':checked')){
 			bmCodeOrItem = 1;
 		}
 		//单品条码（商品号码）
 		var _itemCode = m.input_item.val()||null;
 		
 		// 销售开始日期
 		var _sellStartDate = fmtStrToInt(m.sell_start_date.val())||null;
 		// 销售结束日期
 		var _sellEndDate = fmtStrToInt(m.sell_end_date.val())||null;
 		m.tempTableType.val(_tableType);
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
 				itemCode:_itemCode,	
 				sellStartDate:_sellStartDate,	
 				sellEndDate:_sellEndDate	
 		};
 		m.searchJson.val(JSON.stringify(searchJsonStr));
    }
    
  //根据Item Barcode取得该商品的详细对象，
    var getItemInfoByItem1 = function(item1,fun){
    	$.myAjaxs({
			  url:url_left+"/getiteminfo",
			  async:true,
			  cache:false,
			  type :"get",
			  data :"itemCode="+item1,
			  dataType:"json",
			  success:fun,
			  complete:_common.myAjaxComplete
		  }); 
    }
    
    //点击采购tr后事件
    var trClick_table1 = function(){
    	var delBut =  $("#del");//
    	delBut.prop("disabled",true);
    	var cols = tableGrid.getSelectColValue(selectTrTemp,"statusFlg,checkFlg");
    	
    	if(cols["checkFlg"]=="3"||m.entiretyBmStatus.val()=="4"){
    		delBut.prop("disabled",false);
    	}else{
    		delBut.prop("disabled",true);
    	}
    }
    //点击采购tr后事件
    var trClick_table3 = function(){
    	var delBut =  $("#del");//
    	delBut.prop("disabled",true);
    	if(m.entiretyBmStatus.val()=="4"){
    		delBut.prop("disabled",false);
    	}else{
    		delBut.prop("disabled",true);
    	}
    }
    
    //表格初始化-采购样式
    var initTable1 = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    		title:"BM结果一览",
    		param:paramGrid,
    		colNames:["审核状态flg","审核状态","状态hide","状态","区分hide","区分","BM编码","BM数量","销售开始日","销售结束日","发起部门DPT","采购员编码","采购员名称","驳回原因"],
    		colModel:[
	          {name:"checkFlg",type:"text",ishide:true},
	          {name:"checkFlgText",type:"text",ishide:true},
    		  {name:"opFlg",type:"text",ishide:true},
	          {name:"opFlgText",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:statusFmt},
	          {name:"bmType",type:"text",text:"center",width:"100",ishide:true},
	          {name:"bmTypeText",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:bmTypeFmt},
	          {name:"bmCode",type:"text",text:"center",width:"50",ishide:false,css:"",getCustomValue:bmCodeFmt},
	          {name:"bmCount",type:"text",text:"center",width:"50",ishide:false,css:"",getCustomValue:null},
	          {name:"startDate",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:dateFmt},
	          {name:"endDate",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:dateFmt},
	          {name:"createDpt",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
	          {name:"buyer",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
	          {name:"buyerName",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
	          {name:"rejectreason",type:"text",text:"left",width:"250",ishide:false,css:"",getCustomValue:null}
	          ],//列内容
	          width:"max",//宽度自动
	          page:1,//当前页
	          rowPerPage:10,//每页数据量
	          isPage:true,//是否需要分页
	          sidx:"bm.bm_type,order.bm_code",//排序字段
	          sord:"asc",//升降序
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
	        	  //列段显示
	        	  if(m.entiretyBmStatus.val()=="2"||m.entiretyBmStatus.val()=="5"||m.entiretyBmStatus.val()=="6"){
	        		  tableGrid.showColumn("opFlgText,buyer,buyerName,rejectreason");
	        	  }else if(m.entiretyBmStatus.val()=="3"||m.entiretyBmStatus.val()=="4"){
	        		  tableGrid.hideColumn("opFlgText,buyer,buyerName,rejectreason");
	        	  }
	        	  return self;
	          },
	          eachTrClick:function(trObj,tdObj){//正常左侧点击
	        	  selectTrTemp = trObj;
	        	  trClick_table1();
	          },
	          buttonGroup:[
                   {butType:"custom",butHtml:"<button id='outExcel' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span> Export</button>"},
                   {butType:"custom",butHtml:"<button id='del' type='button' class='btn  btn-danger   btn-sm ' disabled ><span class='glyphicon glyphicon-trash'></span> 删 除</button>"}
              ],
    		                       
    	});
    }    
	//表格初始化-事业部长
    var initTable2 = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    	    title:"BM结果一览",
			param:paramGrid,
    	    colNames:["<label for='table_ck_all'><input type='checkbox' name='table_ck' id='table_ck_all' /> 操作</label>",
    	              "新登BM优先度",
    	              "状态",
    	              "操作类型",
    	              "区分hide",
    	              "区分",
    	              "BM编码",
    	              "BM数量",
    	              "销售开始日",
    	              "销售结束日",
    	              "发起部门DPT",
    	              "毛利率",
    	              "采购员编码",
    	              "采购员名称",
    	              "可以评审标记",
    	              "可以评审的资源"
    	              ],
    	    colModel:[
    	              {name:"trKey",type:"text",text:"center",width:"50"},
    	              {name:"firstFlg",type:"text",text:"center",width:"80",getCustomValue:firstFlgFmt},
    	              {name:"opFlgText",type:"text",text:"center",width:"80",getCustomValue:statusFmt},
    	              {name:"newFlgText",type:"text",text:"center",width:"80",getCustomValue:newFlgFmt},
    	              {name:"bmType",type:"text",text:"center",ishide:true},
    	              {name:"bmTypeText",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:bmTypeFmt},
    	              {name:"bmCode",type:"text",text:"center",width:"80",getCustomValue:bmCodeFmt},
    	              {name:"bmCount",type:"text",text:"center",width:"80"},
    	              {name:"startDate",type:"text",text:"center",width:"100",getCustomValue:dateFmt},
    	              {name:"endDate",type:"text",text:"center",width:"100",getCustomValue:dateFmt},
    	              {name:"createDpt",type:"text",text:"center",width:"100"},
    	              {name:"bmGross",type:"text",text:"center",width:"100",getCustomValue:bmGrossFmt},
    	              {name:"buyer",type:"text",text:"center",width:"100"},
    	              {name:"buyerName",type:"text",text:"center",width:"100"},
    	              {name:"canReview",type:"text",text:"center",width:"100",ishide:true},
    	              {name:"checkResources",type:"text",text:"center",width:"100",ishide:true}
    	             ],//列内容
    	    width:"max",//宽度自动
    	    page:1,//当前页
	          rowPerPage:10,//每页数据量
    	    isPage:true,//是否需要分页
    	    sidx:"bm.bm_type,order.bm_code",//排序字段
	          sord:"asc",//升降序
    	    loadCompleteEvent:function(self){
    	    	selectTrTemp = null;//清空选择的行
    	    	//列段显示
	        	  if(m.entiretyBmStatus.val()=="3"){
	        		  tableGrid.hideColumn("trKey,firstFlg,newFlgText,bmGross,buyer,buyerName");
	        	  }else{
	        		  tableGrid.showColumn("trKey,firstFlg,newFlgText,bmGross,buyer,buyerName");
	        	  }
				return self;
			},
			loadEachBeforeEvent:function(trObj){
				var canReview = trObj.find("td[tag='canReview']").text();
				var checkResources = trObj.find("td[tag='checkResources']").text();
				var trKey = trObj.find("td[tag='trKey']");
				if(canReview=="0"){
					if(m.checkResources.val()=="999"){
						operateFmt(trKey,trKey.text());
					}else{
						var arr1 = m.checkResources.val().split(",");
						var arr2 = checkResources.split(",");
						var intersection = arr1.filter(function (val) { return arr2.indexOf(val) > -1 });
						if(intersection!=null&&intersection.length>0){
							operateFmt(trKey,trKey.text());
						}else{
							trKey.text("");
						}
					}
				}else{
					trKey.text("");
				}
				
		        return trObj;
		    },
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				//trClick_table2();
			},
    	    buttonGroup:[
                 {butType:"custom",butHtml:"<button id='outExcel' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span> EXPORT</button>"},//增加 默认："",lg:最大，sm小，xs超小
                 {butType:"custom",butHtml:"<button id='check' type='button' class='btn  btn-primary   btn-sm '  ><span class='glyphicon glyphicon-edit'></span> 审 核</button>"},//增加 默认："",lg:最大，sm小，xs超小
                 {butType:"custom",butHtml:"<button id='reject' type='button' class='btn  btn-danger   btn-sm '  ><span class='glyphicon glyphicon-share'></span> 驳 回</button>"}//增加 默认："",lg:最大，sm小，xs超小
             ],
             
    	});
    }

  //表格初始化-系统部样式
    var initTable3 = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    	    title:"BM结果一览",
			param:paramGrid,
    	    colNames:["新登BM优先度","状态","操作类型","区分hide","区分","BM编码","BM数量","销售开始日","销售结束日","发起部门DPT","采购员编码","采购员名称"],
    	    colModel:[
    	              {name:"firstFlg",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:firstFlgFmt},
    	              {name:"opFlgText",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:statusFmt},
    	              {name:"newFlgText",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:newFlgFmt},
    	              {name:"bmType",type:"text",text:"center",width:"80",ishide:true},
    	              {name:"bmTypeText",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:bmTypeFmt},
    	              {name:"bmCode",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:bmCodeFmt},
    	              {name:"bmCount",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
    	              {name:"startDate",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:dateFmt},
    	              {name:"endDate",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:dateFmt},
    	              {name:"createDpt",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
    	              {name:"buyer",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
    	              {name:"buyerName",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null}
    	             ],//列内容
    	    width:"max",//宽度自动
    	    rowPerPage:30,//每页数据量
    	    isPage:true,//是否需要分页
    	    sidx:"bm.bm_type,order.bm_code",//排序字段
	          sord:"asc",//升降序
    	    page:1,//当前页
    	    loadCompleteEvent:function(self){
    	    	selectTrTemp = null;//清空选择的行
    	    	 if(m.entiretyBmStatus.val()=="2"||m.entiretyBmStatus.val()=="5"){
    	    		 tableGrid.showColumn("firstFlg,newFlgText,buyer,buyerName");
	        	  }else{
	        		  tableGrid.hideColumn("firstFlg,newFlgText,buyer,buyerName");
	        		  
	        	  }
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				trClick_table3();
			},
    	    buttonGroup:[
                 {butType:"custom",butHtml:"<button id='outExcel' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span> EXPORT</button>"},
                 {butType:"custom",butHtml:"<button id='del' type='button' class='btn  btn-danger   btn-sm ' disabled ><span class='glyphicon glyphicon-trash'></span> 删 除</button>"}
             ],
             
    	});
    }
    //表格初始化-店铺
    var initTable4 = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    		title:"BM结果一览",
    		param:paramGrid,
    		colNames:["Item Barcode","区分hide","区分","店铺号","BM编码","BM数量","销售开始日","销售结束日","发起部门"],
    		colModel:[
    		          {name:"itemCode",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
    		          {name:"bmType",type:"text",text:"center",width:"80",ishide:true},
    		          {name:"bmTypeText",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:bmTypeFmt},
    		          {name:"stroe",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
    		          {name:"bmCode",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:bmCodeFmt},
    		          {name:"bmCount",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null},
    		          {name:"startDate",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:dateFmt},
    		          {name:"endDate",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:dateFmt},
    		          {name:"createDpt",type:"text",text:"center",width:"80",ishide:false,css:"",getCustomValue:null}
    		          ],//列内容
    		          width:"max",//宽度自动
    		          page:1,//当前页
    		          rowPerPage:10,//每页数据量
    		          isPage:true,//是否需要分页
    		          sidx:"bm.bm_type,order.bm_code",//排序字段
    		          sord:"asc",//升降序
    		          loadCompleteEvent:function(self){
    		        	  selectTrTemp = null;//清空选择的行
    		        	  if(bmCodeOrItem==1){
    		        		  tableGrid.showColumn("itemCode");
    		        	  }else{
    		        		  tableGrid.hideColumn("itemCode");
    		        	  }
    		        	  return self;
    		          },
    		          buttonGroup:[
    		              {butType:"custom",butHtml:"<button id='outExcel' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span> EXPORT</button>"}//增加 默认："",lg:最大，sm小，xs超小
                       ]
    		                       
    	});
    }
    
    //bm编码格式化
    var operateFmt = function(tdObj, value){
    	var sp = value.split("#");
    	var html = '<input type="checkbox" bmtype="'+sp[0]+'" bmcode="'+sp[1]+'" name="table_ck" />';
    	return $(tdObj).html(html);
    }
    
    //bm编码格式化
    var bmCodeFmt = function(tdObj, value){
    	bmtype = tempTrObjValue.bmType;
    	var html = '<a href="javascript:void(0);" title="进入详情" class="view" tabletype="'+m.tempTableType.val()+'" bmcode="'+value+'" bmtype="'+bmtype+'" ><span class="glyphicon glyphicon-expand icon-right"></span>'+value+'</a>';
    	return $(tdObj).html(html);
    }
    
    // 操作列复选和删除按钮 
    var editButtonFmt = function(tdObj, value){
    	var cks = '<input type="checkbox" />';
    	var but = '<button type="button" class="btn btn-primary btn-xs">Delete</button>';
    	var insertHtml = "";
    	insertHtml+=cks;
    	if(m.entiretyBmStatus.val()==4){
    		insertHtml=but;
    	}
    	return $(tdObj).html(insertHtml);
    }
    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }
    //日期字段格式化格式
    var bmGrossFmt = function(tdObj, value){
    	value = (value/100).toFixed(1);
    	if(value<0){
    		return $(tdObj).text("存在负毛利率");
    	}else{
    		return $(tdObj).text(value);
    	}
    }
    //newFlgFmt 0-Add 1-修改 2-删除
    var firstFlgFmt = function(tdObj, value){
    	switch(value) {
    	case "0":
    		return $(tdObj).text("正常");
    		break;
    	case "1":
    		return $(tdObj).text("紧急");
    		break;
    	default:
    		return $(tdObj).text("正常");
    	}
    }
    //newFlgFmt 0-Add 1-修改 2-删除
    var newFlgFmt = function(tdObj, value){
    	switch(value) {
    	case "0":
    		return $(tdObj).text("Add");
    		break;
    	case "1":
    		return $(tdObj).text("修改");
    		break;
    	case "2":
    		return $(tdObj).text("删除");
    		break;
    	default:
    		return $(tdObj).text("未知");
    	}
    }
    //bmType 字段格式化中文
    var bmTypeFmt = function(tdObj, value){
    	tempTrObjValue.bmType = value;
    	switch(value) {
    	case "01":
    		return $(tdObj).text("01 捆绑");
    		break;
    	case "02":
    		return $(tdObj).text("02 混合");
    		break;
    	case "03":
    		return $(tdObj).text("03 固定组合");
    		break;
    	case "04":
    		return $(tdObj).text("04 阶梯折扣");
    		break;
    	case "05":
    		return $(tdObj).text("05 AB组");
    		break;
    	}
    }
    //OP_FLG 字段格式化中文
    var statusFmt = function(tdObj, value){
    	//01-采购员提交，07-采购员确认BM明细单品，08-采购员驳回BM明细单品，17-商品部长审核通过，18-商品部长驳回，21-系统部提交，27-系统部审核通过，28-系统部驳回
    	switch(value) {
		case "01":
			return $(tdObj).text("采购员提交");
			break;
		case "07":
			return $(tdObj).text("采购员确认BM明细单品");
			break;
		case "08":
			return $(tdObj).text("采购员驳回BM明细单品");
			break;
		case "17":
			return $(tdObj).text("商品部长审核通过");
			break;
		case "18":
			return $(tdObj).text("商品部长驳回");
			break;
		case "21":
			return $(tdObj).text("系统部提交");
			break;
		case "27":
			return $(tdObj).text("系统部审核通过");
			break;
		case "28":
			return $(tdObj).text("系统部驳回");
			break;
		case "":
			return $(tdObj).text("已生效");
			break;
		default:
			return $(tdObj).text("未知状态");
    	}
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
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('bm');
_start.load(function (_common) {
	_index.init(_common);
});
