require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('cashier', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
		a_store = null,
    	common=null;
    var m = {
		a_store:null,
		inital_password:null,
		toKen : null,
		use : null,
		reset: null,
		search: null,
		cashierId : null,//搜索 收银员id
		cashierName : null,//搜索 收银员名称
		storeCd :null,
		storeName :null,
		i_cashierEmail:null,
		operateFlg : null,//判断新增修改 1新增 2修改
		//新增
		cashierLevel : null,//权限等级
		effectiveSts : null,//状态
		duty : null,//职务
		i_cashierId : null,//录入 收银员id
		i_cashierName : null,//录入 收银员名称
		cancelByAdd : null,// 录入 取消
		affirmByAdd : null,// 录入 确认

		//设置密码
		pwd_cashierLevel : null,// 修改密码 权限等级
		pwd_effectiveSts : null,//修改密码 状态
		pwd_duty : null,//修改密码 职务
		pwd_cashierId : null,//修改密码 收银员id
		pwd_cashierName : null,//修改密码 收银员名称
		oldPassword : null,//旧密码
		newPassword : null,//新密码
		repeatPassword : null,//确认新密码
		cancelByUpdatePwd : null,// 修改密码 取消
		affirmByUpdatePwd : null,// 修改密码 确认
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left =_common.config.surl+"/cashier";
		systemPath = _common.config.surl;
    	//初始化下拉
		initAutoMatic();
		getSelectValue();
    	//事件绑定
		but_event();
		//列表初始化
		initTable1();
    	//表格内按钮事件
    	table_event();
		//权限验证
		isButPermission();
		initStoreCd();
		m.search.click();
    }
    var  initStoreCd=function () {
        $.myAjaxs({
			url:url_left+"/ma1000"+"/getStore",
			async:true,
			cache:false,
			type :"get",
			dataType:'json',
			success:function (result) {
				var htmlStr = '<option value="">--Please Select--</option>';
				$.each(result,function(ix,node){
					htmlStr+='<option value="'+node.storeCd+'">'+node.storeCd+'</option>';
				});
				m.storeCd.html(htmlStr);
			},
			complete:_common.myAjaxComplete
		});
			}

    
    var table_event = function(){

		$("#add").on("click", function () {
			$("#store_clear").click();
			m.i_cashierId.val("");
			m.i_cashierName.val("");
			m.i_cashierEmail.val("");
			m.duty.val("");
			m.inital_password.val("");
			m.cashierLevel.val("");
			m.i_cashierId.attr("disabled",false);
			m.i_cashierName.attr("disabled",false);
			m.duty.attr("disabled",false);
			m.cashierLevel.attr("disabled",false);
			m.cashierLevel.find("option:first").prop("selected",true);
			m.operateFlg.val("1");
			$("#a_store").parent().show();
			$("#store_text").parent().hide();
			$('#cashier_dialog').modal("show");
		});
		//修改权限
		$("#updateAuth").on("click", function () {
			if(selectTrTemp == null){
				_common.prompt("Please select cashier！",5,"info"); // 请选择收银员
				return;
			}
			m.i_cashierEmail.val("");
			m.inital_password.val("");
			m.cashierLevel.val("");
			var cols = tableGrid.getSelectColValue(selectTrTemp,"storeName,effectiveSts,cashierId,cashierName,duty,cashierLevelCd,effectiveSts");
			var effectiveSts = cols["effectiveSts"];
			var cashierName = cols["cashierName"];
			if(effectiveSts=="90"){
				// 收银员 ** 为非正常状态, 不可修改
				_common.prompt("Selected " + cashierName + "is not effective and cannot be modified!\n",5,"info");
				return;
			}
			$("#store_text").val(cols["storeName"]);
			m.i_cashierId.val(cols["cashierId"]);
			m.i_cashierName.val(cols["cashierName"]);
			m.duty.val(cols["duty"]);
			m.cashierLevel.val(cols["cashierLevelCd"]);
			m.effectiveSts.val(cols["effectiveSts"]);
			m.i_cashierId.attr("disabled",true);
			m.i_cashierName.attr("disabled",true);
			m.duty.attr("disabled",true);
			m.operateFlg.val("2");
			$("#a_store").parent().hide();
			$("#store_text").parent().show();
			$('#cashier_dialog').modal("show");
		});
    	//注销
    	$("#del").on("click",function(){
			if(selectTrTemp == null){
				_common.prompt("Please select cashier！",5,"info"); // 请选择收银员
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"effectiveSts,cashierId,cashierName");
			if(cols["effectiveSts"]=="90"){
				// _common.prompt("收银员 '" + cols["cashierName"] + "'已为注销状态，请确认！",5,"info");
				_common.prompt("Cashier '" + cols["cashierName"] + "'has logged off, please confirm!",5,"info");
				return;
			}
			// 请确认是否要注销选中的收银员
			_common.myConfirm("Please confirm whether you want to logout the selected cashier？",function(result){
				if(result=="true"){
					updateCashierSts(cols["cashierId"],"90")
				}
			});
    	});
		//恢复
		$("#normal").on("click",function(){
			if(selectTrTemp == null){
				_common.prompt("Please select cashier！",5,"info"); // 请选择收银员
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"effectiveSts,cashierId,cashierName");
			if(cols["effectiveSts"]=="10"){
				// _common.prompt("收银员 '" + cols["cashierName"] + "'已为正常状态，请确认！",5,"info");
				_common.prompt("selected '" + cols["cashierName"] + "'is already effective!, please confirm!",5,"info");
				return;
			}
			// 请确认是否要恢复选中的收银员
			_common.myConfirm("Please confirm whether you want to restore the selected cashier？",function(result){
				if(result=="true"){
					updateCashierSts(cols["cashierId"],"10")
				}
			});
		});
		//设置未初始化密码
		$("#initPass").on("click",function(){
			if(selectTrTemp == null){
				_common.prompt("Please select cashier！",5,"info"); // 请选择收银员
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"cashierId,cashierName");
			var cashierId = cols["cashierId"];
			var cashierName = cols["cashierName"];
			if(cashierId==null||cashierId==""){
				// 收银员id 为空
				_common.prompt("The cashier id is empty！",5,"info");
				return;
			}
			// _common.myConfirm("确认要将收银员'" + cashierName + "'的密码设置为初始密码吗？",function(result){
			_common.myConfirm("Are you sure to set cashier'" + cashierName + "'password as the initial password?",function(result){
				if(result=="true"){
					$.myAjaxs({
						url:url_left+"/initCashierPwd",
						async:true,
						cache:false,
						type :"post",
						data :{
							cashierId:cashierId
						},
						dataType:"json",
						success:function(result){
							if(result.success){
								// _common.prompt("收银员'" + cashierName + "'已成功设置为初始密码！",5,"info");
								_common.prompt("Cashier'" + cashierName + "'Has successfully set the initial password！",5,"info");
							}else{
								_common.prompt("Setup failed！",5,"error");
							}
						},
						error : function(e){
							_common.prompt("Setup failed！",5,"error"); // 设置失败
						},
						complete:_common.myAjaxComplete
					});
				}
			});
		});
		//修改密码
		$("#updatePass").on("click", function () {
			if(selectTrTemp == null){
				_common.prompt("Please select cashier！",5,"info"); // 请选择收银员
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"effectiveSts,cashierId,cashierName,duty,cashierLevel,effectiveStsName");
			m.pwd_cashierId.val(cols["cashierId"]);
			m.pwd_cashierName.val(cols["cashierName"]);
			m.pwd_duty.val(cols["duty"]);
			m.pwd_cashierLevel.val(cols["cashierLevel"]);
			m.pwd_effectiveSts.val(cols["effectiveStsName"]);
			m.oldPassword.val("");
			m.newPassword.val("");
			m.repeatPassword.val("");
			$('#cashierPass_dialog').modal("show");
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
	  		  _common.prompt("Operation Succeeded!",2,"success",function(){ // 操作成功
	  			m.search.click();
	  		  },true);
	  	  }else{
	  		  _common.prompt(data.message,5,"error");
	  	  }
	  	  m.toKen.val(data.toKen);
	    }

    //画面按钮点击事件
    var but_event = function(){
    	m.cancelByAdd.on('click',function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				$("#a_store").css("border-color","#CCCCCC");
				$("#i_cashierId").css("border-color","#CCCCCC");
				$("#i_cashierName").css("border-color","#CCCCCC");
				$("#i_cashierEmail").css("border-color","#CCCCCC");
				$("#duty").css("border-color","#CCCCCC");
				$("#inital_password").css("border-color","#CCCCCC");
				$("#cashierLevel").css("border-color","#CCCCCC");
				if(result!="true"){return false;}
				$('#cashier_dialog').modal('hide');
			})
		});
    	m.cancelByUpdatePwd.on('click',function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				$("#oldPassword").css("border-color","#CCCCCC");
				$("#newPassword").css("border-color","#CCCCCC");
				$("#repeatPassword").css("border-color","#CCCCCC");
				if(result!="true"){return false;}
				$('#cashierPass_dialog').modal('hide');
			})
		});
		//清空事件
		m.reset.on("click",function(){
			m.cashierId.val("");
			m.cashierName.val("");
			m.storeCd.val("");
			m.storeName.val("");
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			paramGrid = "cashierId="+m.cashierId.val()+"&cashierName="+m.cashierName.val()+"&storeCd="+m.storeCd.val()+"&storeName="+m.storeName.val();
			tableGrid.setting("url",url_left+"/getCashierList");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});



    	//新增确认
    	m.affirmByAdd.on("click",function(){
			var storeCd = $("#a_store").attr("k");
			var cashierId = m.i_cashierId.val();
			var cashierName = m.i_cashierName.val();
			var duty = m.duty.val();
			var cashierLevel = m.cashierLevel.val();
			var effectiveSts = m.effectiveSts.val();
			var operateFlg = m.operateFlg.val();
			var cashierPassword= m.inital_password.val();
			var cashierEmail=m.i_cashierEmail.val();

			var data = {
				cashierPassword:cashierPassword,
				storeCd:storeCd,
				cashierId:cashierId,
				cashierName:cashierName,
				duty:duty,
				cashierLevel:cashierLevel,
				cashierEmail:cashierEmail,
				effectiveSts:effectiveSts
			}
			if(operateFlg=="1"){
				if(storeCd==null||storeCd==""){
					$("#a_store").css("border-color","red");
					_common.prompt("Please select a store first!",5,"error");
					$("#a_store").focus();
					return false;
				}else {
					$("#a_store").css("border-color","#CCCCCC");
				}

				if(cashierId==null||cashierId==""){
					$("#i_cashierId").css("border-color","red");
					_common.prompt("Please enter cashier No.!",5,"error"); // 请输入Staff ID！
					m.i_cashierId.focus();
					return false;
				}else{
					if(cashierId.length>10){
						$("#i_cashierId").css("border-color","red");
						_common.prompt("The maximum length of Staff ID is 10 digits!",5,"error"); // Staff ID最大长度为10！
						m.i_cashierId.focus();
						return false;
					}
					$("#i_cashierId").css("border-color","#CCCCCC");
				}
				if(cashierName==null||cashierName==""){
					$("#i_cashierName").css("border-color","red");
					_common.prompt("Please enter cashier name!",5,"error");
					m.i_cashierName.focus();
					return false;
				}else{
					if(cashierName.length>30){
						$("#i_cashierName").css("border-color","red");
						_common.prompt("The maximum length of Staff Name is 30 digits!",5,"error"); // Staff Name最大长度为30！
						m.i_cashierName.focus();
						return false;
					}
					$("#i_cashierName").css("border-color","#CCCCCC");
				}
				if(cashierEmail==null||cashierEmail==""){
					$("#i_cashierEmail").css("border-color","red");
					_common.prompt("Please enter Email Address!",5,"error");
					$("#i_cashierEmail").focus();
					return false;
				}else {
					$("#i_cashierEmail").css("border-color","#CCCCCC");
				}

				if(duty==null||duty==""){
					$("#duty").css("border-color","red");
					_common.prompt("Please enter Position!",5,"error");
					$("#duty").focus();
					return false;
				}else {
					$("#duty").css("border-color","#CCCCCC");
				}

				if(cashierPassword==null||cashierPassword==""){
					m.inital_password.css("border-color","red");
					_common.prompt("Please fill your initpassword",5,"error");
					m.inital_password.focus();
					return false;
				}else {
					m.inital_password.css("border-color","#CCCCCC");
				}
				if(duty!=""&&duty.length>30){
					$("#duty").css("border-color","red");
					_common.prompt("The maximum length of Position is 30 digits!",5,"error"); // Position最大长度为30
					m.duty.focus();
					return false;
				}else {
					$("#duty").css("border-color","#CCCCCC");
				}
					_common.myConfirm("Are you sure you want to submit?", function (result) {
						$.myAjaxs({
							url: url_left + "/getCashier",
							async: true,
							cache: false,
							type: "post",
							data: "cashierId=" + cashierId,
							dataType: "json",
							success: function (result) {
								if (result.success) {
									_common.prompt("The cashier already exists!", 5, "error"); // 该收银员已存在
								} else {
									$.myAjaxs({
										url: url_left + "/add",
										async: true,
										cache: false,
										type: "post",
										data: data,
										dataType: "json",
										success: function (result) {
											if (result.success) {
												_common.prompt("Data saved successfully!", 5, "info"); // 添加收银员成功
												m.i_cashierId.val("");
												m.i_cashierName.val("");
												m.duty.val("");
												m.search.click();

												$("#cashier_dialog").modal("hide");

											} else {
												_common.prompt("Please fill in the detailed data!", 5, "error"); // 添加收银员失败
											}
										},
										error: function (e) {
											_common.prompt("Data saved failed!", 5, "error"); // 添加收银员失败
										}
									});
								}
							},
							error: function (e) {
								_common.prompt("request failed!", 5, "error"); // 请求失败
							}
						});
					})
			}else if(operateFlg=="2"){
				_common.myConfirm("Are you sure you want to save?",function(result) {
				$.myAjaxs({
					url:url_left+"/update",
					async:true,
					cache:false,
					type :"post",
					data :{
						cashierId:cashierId,
						cashierLevel:cashierLevel
					},
					dataType:"json",
					success:function(result){

							if (result.success) {
								_common.prompt("Data saved successfully!", 5, "info"); // 修改收银员权限成功
								m.search.click();
								$('#cashier_dialog').modal("hide");
							} else {
								_common.prompt("Data saved failed!", 5, "error"); // 修改收银员权限失败
							}
					},
					error : function(e){
						_common.prompt("Data saved failed!",5,"error"); // 添加收银员失败
					}
				});
			})
				$("#cashier_dialog").modal("hide");
			}

		});

    	//修改密码确认
		m.affirmByUpdatePwd.on("click",function() {
			var cashierId = m.pwd_cashierId.val();
			var oldPassword = m.oldPassword.val();
			var newPassword = m.newPassword.val();
			var repeatPassword = m.repeatPassword.val();
			if(cashierId==null||cashierId==""){
				_common.prompt("The cashier id is empty！",5,"error"); // 收银员id为空
				$("#pwd_cashierId").focus();
				return false;
			}
			if(oldPassword==null||oldPassword==""){
				$("#oldPassword").css("border-color","red");
				_common.prompt("Please enter the originally password！",5,"error"); // 请输入原密码
				m.oldPassword.focus();
				return false;
			}else {
				$("#oldPassword").css("border-color","#CCCCCC");
			}
			if(newPassword==null||newPassword==""){
				$("#newPassword").css("border-color","red");
				_common.prompt("Please enter a new password！",5,"error"); // 请输入新密码
				m.newPassword.focus();
				return false;
			}else {
				$("#newPassword").css("border-color","#CCCCCC");
			}
			var reg = /^[0-9]{1,6}$/;
			if(!reg.test(newPassword)){
				$("#newPassword").css("border-color","red");
				_common.prompt("The password is 1-6 digits. Please enter a new password！",5,"error"); // 密码为1-6位数字,请输入新密码
				m.newPassword.focus();
				return false;
			}else {
				$("#newPassword").css("border-color","#CCCCCC");
			}
			if(repeatPassword==null||repeatPassword==""){
				$("#repeatPassword").css("border-color","red");
				_common.prompt("Please enter a confirmation password！",5,"error"); // 请输入确认密码
				m.repeatPassword.focus();
				return false;
			}else {
				$("#repeatPassword").css("border-color","#CCCCCC");
			}
			if(newPassword!=repeatPassword){
				$("#repeatPassword").css("border-color","red");
				_common.prompt("The new password is different for two times. Please enter it again！",5,"error"); // 新密码两次录入不同，请重新输入
				m.repeatPassword.focus();
				return false;
			}else {
				$("#repeatPassword").css("border-color","#CCCCCC");
			}
			var oldEqNewPwdFlg = false;
			$.myAjaxs({
				url:url_left+"/getCashierPwd",
				async:false,
				cache:false,
				type :"post",
				data :{
					cashierId:cashierId
				},
				dataType:"json",
				success:function(result){
					if(result.success){
						if(result.data==oldPassword){
							oldEqNewPwdFlg = true;
						}
					}
				},
				error : function(e){
					_common.prompt("request error！",5,"error"); // 请求出错
				}
			});
			if(!oldEqNewPwdFlg){
				$("#oldPassword").css("border-color","red");
				_common.prompt("The original password is incorrect, please enter again!",5,"error"); // 原密码不正确，请重新输入！
				m.oldPassword.focus();
				return false;
			}else {
				$("#oldPassword").css("border-color","#CCCCCC");
			}
			// 请确认是否要修改密码
			_common.myConfirm("Please confirm whether you want to change the password？",function(result){
				if(result=="true"){
					$.myAjaxs({
						url:url_left+"/updateCashierPwd",
						async:false,
						cache:false,
						type :"post",
						data :{
							cashierId:cashierId,
							newPassword:newPassword
						},
						dataType:"json",
						success:function(result){
							if(result.success){
								_common.prompt("Password changed succeeded！",5,"info"); // 修改密码成功
								$('#cashierPass_dialog').modal("hide");
							}else{
								_common.prompt(result.message,5,"error");
							}
						},
						error : function(e){
							_common.prompt("request error！",5,"error");
						}
					});
				}
			});
		})
    }



	//初始化自动下拉
	var initAutoMatic = function () {
		a_store = $("#a_store").myAutomatic({
			url: systemPath + "/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					getSelectCashierPrivilege(thisObj.attr("k"))
				}
			}
		});
	}

	function getSelectCashierPrivilege(storeCd,val) {
		if(storeCd!=null&&storeCd!=''){
			m.cashierLevel.find("option:first").remove();
			$.myAjaxs({
				url:systemPath+"/ma1000/getSm",
				async:true,
				type:"post",
				data:'storeCd='+storeCd,
				dataType:"json",
				success:function (result) {
					if (result.success) {
						for (var i=0;i<result.data.length;i++){
							var optionVaule=result.data[i].storeCd;
							// var optionText=result.data[i].sm;
							// m.cashierLevel.append(new Option(optionText,optionText));
						}
						// m.cashierLevel.find("option:first").prop("selected",true);
						// if(val!=null&&val!=""){
						// 	m.cashierLevel.val(val);
						// }
					}
				}

			})
		}

	}


	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		initSelectOptions("Cashier Privilege","cashierLevel", "00215");
		initSelectOptions("Status","effectiveSts", "00220");
		m.effectiveSts.val("10");
    }

	// 初始化下拉列表
	function initSelectOptions(title, selectId, code) {
		// 共通请求
		$.myAjaxs({
			url:systemPath+"/cm9010/getCode",
			async:true,
			cache:false,
			type :"post",
			data :"codeValue="+code,
			dataType:"json",
			success:function(result){
				var selectObj = $("#" + selectId);
				selectObj.find("option:not(:first)").remove();
				for (var i = 0; i < result.length; i++) {
					var optionValue = result[i].codeValue;
					var optionText = result[i].codeName;
					selectObj.append(new Option(optionText, optionValue));
				}
			},
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			}
		});
	}

	// 修改收银员状态
	function updateCashierSts(cashierId, effectiveSts) {
		if(cashierId==null||cashierId==""){
			_common.prompt("The cashier id is empty！",5,"info"); // 收银员id为空
			return;
		}
		if(effectiveSts==null||effectiveSts==""){
			_common.prompt("The effective status is empty！",5,"info"); // 生效状态为空
			return;
		}
		var effectiveStsName = '';
		if(effectiveSts=="10"){
			effectiveStsName = "recover";
		}else if(effectiveSts=="90"){
			effectiveStsName = "logout";
		}
		$.myAjaxs({
			url:url_left+"/updateCashierSts",
			async:true,
			cache:false,
			type :"post",
			data :{
				cashierId:cashierId,
				effectiveSts:effectiveSts
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					_common.prompt(effectiveStsName+" success！",5,"info");
					m.search.click();
				}else{
					_common.prompt(result.message,5,"error");
				}
			},
			error : function(e){
				_common.prompt(effectiveStsName+" failed！",5,"error");
			}
		});
	}

    //表格初始化-收银员列表样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Cashier Details",
    		param:paramGrid,
    		colNames:[/*"Store No.","Store Name",*/ "Store","effectiveSts","Status","Cashier ID","Cashier Name","Email","Position","Cashier Level Cd","Cashier Privilege"],
    		colModel:[
    		  // {name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
			  // {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"store",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"effectiveSts",type:"text",text:"left",width:"130",ishide:true,css:""},
	          {name:"effectiveStsName",type:"text",text:"left",width:"130",ishide:false,css:""},
	          {name:"cashierId",type:"text",text:"right",width:"130",ishide:false,css:""},
	          {name:"cashierName",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"cashierEmail",type:"text",text:"left",width:"130",ishide:false,css:""},
	          {name:"duty",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"cashierLevelCd",type:"text",text:"right",width:"100",ishide:true,css:""},
	          {name:"cashierLevel",type:"text",text:"left",width:"150",ishide:false,css:""}
	          ],//列内容
	          width:"max",//宽度自动
	          page:1,//当前页
	          rowPerPage:10,//每页数据量
	          isPage:true,//是否需要分页
	          sidx:"effective_sts,cashier_id",//排序字段
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
	        	  return self;
	          },
	          eachTrClick:function(trObj,tdObj){//正常左侧点击
	        	  selectTrTemp = trObj;
	          },
			  buttonGroup:[
				{
					butType: "add",
					butId: "add",
					butText: "Add",
					butSize: ""//,
				},//新增
				{
					butType: "update",
					butId: "updateAuth",
					butText: "Modify",
					butSize: ""//,
				},//修改
				  {butType:"custom",butHtml:"<button id='del' type='button' class='btn  btn-danger   btn-sm '><span class='glyphicon glyphicon-off'></span>Eliminate</button>"},
				{butType:"custom",butHtml:"<button id='normal' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-ok-circle'></span> Reactivate</button>"},
	  			{butType:"custom",butHtml:"<button id='initPass' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-import'></span> Restore Initial Password</button>"},
				  {butType:"custom",butHtml:"<button id='updatePass' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-pencil'></span> Reset Password</button>"},
			],
    	});
    }

	// 按钮权限验证
	var isButPermission = function () {
		var addBut = $("#addBut").val();
		if (Number(addBut) != 1) {
			$("#add").remove();
		}
		var editBut = $("#editBut").val();
		if (Number(editBut) != 1) {
			$("#updateAuth").remove();
		}
		var eliminateBut = $("#eliminateBut").val();
		if (Number(eliminateBut) != 1) {
			$("#del").remove();
		}
		var reactivateBut = $("#reactivateBut").val();
		if (Number(reactivateBut) != 1) {
			$("#normal").remove();
		}
		var initPasswordBut = $("#initPasswordBut").val();
		if (Number(initPasswordBut) != 1) {
			$("#initPass").remove();
		}
		var resetPasswordBut = $("#resetPasswordBut").val();
		if (Number(resetPasswordBut) != 1) {
			$("#updatePass").remove();
		}
	}

    //bm编码格式化
    var operateFmt = function(tdObj, value){
    	var sp = value.split("#");
    	var html = '<input type="checkbox" bmtype="'+sp[0]+'" bmcode="'+sp[1]+'" name="table_ck" />';
    	return $(tdObj).html(html);
    }
    
    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }
	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
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
var _index = require('cashier');
_start.load(function (_common) {
	_index.init(_common);
});
