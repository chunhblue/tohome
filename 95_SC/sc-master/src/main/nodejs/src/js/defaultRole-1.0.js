require("zgGrid");
require("zgGrid.css");
require("myAutomatic");
require("myAutoComplete.css");
var _myAjax=require("myAjax");
define('defaultRole', function () {
    var self = {},
    	url_left = "defaultrole",
    	tableGrid = null,
    	roleName = null,
    	iyPost = null,
    	token = null,
    	e_id = null,
    	e_role = null,
    	e_iyPost = null,
    	selectTrObj = null,
    	paramGrid = "",
    	common=null;
    
    function init(_common) {
    	token = $("#toKen");
    	e_id = $("#e_id");
    	common = _common;
    	initSearchAutomatic();
    	setParamGrid();
    	initTable();
    	execute();
    	initListPageBut();
    	
    	if($("#editPer").val()=="2"){
    		$('#add').remove();
    		$('#update').remove();
    	}
    	if($('#cancelPer').val()=="2"){
    		$('#del').remove();
    	}
    }
    var setParamGrid = function(){
    	paramGrid="";
    	var roleId = roleName.attr("k");
    	if(roleId!=""){
    		paramGrid+="&roleId="+roleId;
    	}
    	var iyPostCode = iyPost.attr("k");
    	if(iyPostCode!=""){
    		paramGrid+="&postCode="+iyPostCode;
    	}
	}
    var initListPageBut = function(){
		$("#del").prop("disabled","disabled");
		$("#update").prop("disabled","disabled");
	}
    //设定弹出窗中 元素是否可用，添加和修改分别有不同的展示效果
    var diaLogStyleSet = function(flg){
    	clearEditDialog();
    	if(flg=="add"){
    		//添加
    		e_iyPost.removeClass("disabled").removeAttr("disabled");
    		$("#edit_dialog").find("a[class*='a-up-hide']").show();
    	}else{
    		//修改
    		$("#edit_dialog").find("a[class*='a-up-hide']").hide();
    		e_iyPost.addClass("disabled").prop("disabled","disabled");
    	}
    }
    //事件挂载
    var execute = function(){
    	$("#search").on("click",function(){
    		setParamGrid();
    		tableGrid.setting("param",paramGrid);
    		tableGrid.setting("page",1);
    		tableGrid.loadData(null);
    	});
    	// 添加授权
    	$("#add").on("click",function(){
    		diaLogStyleSet("add");
    		$('#edit_dialog').modal("show");
    		
    	});
    	// 修改授权
    	$("#update").on("click",function(){
    		if(selectTrObj == null){
				_common.prompt("Please select content to edit!",5,"info");
        	}else{
        		diaLogStyleSet("up");
        		var cols = tableGrid.getSelectColValue(selectTrObj,"id,roleId,postCode,roleName,postName");
        		e_id.val(cols['id']);
        		$.myAutomatic.setValueTemp(e_role,cols['roleId'],cols['roleName']);//赋值
        		$.myAutomatic.setValueTemp(e_iyPost,cols['postCode'],cols['postName']);//赋值
        		$('#edit_dialog').modal("show");
        	}
    	});
    	// 取消授权
    	$("#del").on("click",function(){
    		if(selectTrObj == null){
				_common.prompt("Please select content to edit!",5,"info");
        	}else{
        		common.myConfirm("Are you sure to cancel the authorization? Data cannot be recovered after cancellation!",function(result){/*确认取消该授权么？取消后数据将无法恢复!*/
        			if(result=="true"){
        				var cols = tableGrid.getSelectColValue(selectTrObj,"id");
        				$.myAjaxs({
        	  				  url:url_left+"/cancel",
        	  				  async:false,
        	  				  cache:false,
        	  				  type :"post",
        	  				  data :"id="+cols['id']+"&toKen="+token.val(),
        	  				  dataType:"json",
        	  				  success:showResponse,
        	  				  complete:_common.myAjaxComplete
        	  			  }); 
        			}
        		});
        	}
    	});

    	//编辑弹出窗 确认提交按钮
    	$("#affirm").on("click",function(){
    		//验证
    		if(verifyDialog()){
				common.myConfirm("Are you sure you want to submit?",function(result){
					if(result=="true"){
						$.myAjaxs({
							url:url_left+"/editdate",
							async:false,
							cache:false,
							type :"post",
							data :getAffirmParam(),
							dataType:"json",
							success:showResponse,
							complete:_common.myAjaxComplete
						});
					}
				})
    		}
    	});
		//弹出窗 确认取消按钮
		$("#cancel").on("click",function(){
				common.myConfirm("Are you sure you want to close?",function(result){
					if(result=="true") {
						var e_iyPost = $("#e_iyPost").val();
						$("#e_iyPost").css("border-color", "#CCCCCC");
						var e_role = $("#e_role").val();
						$("#e_role").css("border-color", "#CCCCCC");
						$("#edit_dialog").modal("hide");
					}
				});
		});
    }
    function showResponse(data,textStatus, xhr){
	  	  var resp = xhr.responseJSON;
	  	  if( resp.result == false){
	  		  top.location = resp.s+"?errMsg="+resp.errorMessage;
	  		  return ;
	  	  }
	  	  if(data.success==true){
	  		  _common.prompt("Operation Succeeded!",2,"success",function(){
	  			$('#edit_dialog').modal("hide");
	    		clearEditDialog();
	  			$("#search").click();
	  		  },true);
	  	  }else{
	  		  _common.prompt(data.message,5,"error");
	  	  }
	  	  $("#toKen").val(data.toKen);
	    }
    //整理提交参数
    var getAffirmParam = function(){
    	var param = "";
    	param +="&id="+e_id.val();
    	param +="&roleId="+e_role.attr("k");
    	param +="&postCode="+e_iyPost.attr("k");
    	param +="&toKen="+token.val();
    	return param;
    }
    
    // 验证弹出窗内是否还有未完成输入的内容，如果存在则给出提交，并终止执行
    var verifyDialog = function(){
    	var inputs = $("#edit_dialog").find("input[type='text']");
    	var rest = true;
    	//验证元素对象是否有空内容
    	$.each(inputs,function(index,node){
			var e_iyPost = $("#e_iyPost").val();
			if(e_iyPost == null || $.trim(e_iyPost)==""){
				$("#e_iyPost").focus();
				$("#e_iyPost").css("border-color","red");
			}else{
				$("#e_iyPost").css("border-color","#CCCCCC");
			}

			var e_role = $("#e_role").val();
			if(e_role == null || $.trim(e_role)==""){
				$("#e_role").focus();
				$("#e_role").css("border-color","red");
			}else{
				$("#e_role").css("border-color","#CCCCCC");
			}

    		var k = $(node).attr("k");
    		if(k==""){
    			_common.prompt("You still have items not filled in!",5,"error");/*您还有未填写的项目*/
    			$(node).focus();
    			rest = false;
    			return false;
    		}
    	});
    	if(!rest){
    		return rest;
    	}
    	return rest;
    }
    
    //生成随机id
    var createRandomId = function() {
        return "new_create_"+(new Date()).getTime()+'_'+Math.random().toString().substr(2,5);
    }
    
    //清空编辑弹出框的所有内容
    var clearEditDialog = function(){
    	$.myAutomatic.cleanSelectObj(e_role);
    	$.myAutomatic.cleanSelectObj(e_iyPost);
    	$("#edit_dialog").find("div[class*='add-to']").remove();
    	e_id.val("");
    }
    
    //检索项 自动下拉 初始化
    var initSearchAutomatic = function(){
    	roleName = $("#roleName").myAutomatic({
    		url:url_left+"/getrolename",
    		ePageSize:10,
    		startCount: 0,
    	});
    	iyPost = $("#iyPost").myAutomatic({
    		url:url_left+"/getpost",
    		ePageSize:10,
    		startCount: 0,
    	});
    	e_role = $("#e_role").myAutomatic({
    		url:url_left+"/getrolename",
    		ePageSize:10,
    		startCount: 0,
    	});
    	e_iyPost = $("#e_iyPost").myAutomatic({
    		url:url_left+"/getpost",
    		ePageSize:10,
    		startCount: 0,
    	});
    }
    
    //表格初始化
    var initTable = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    	    // title:"默认角色授权",
    	 //   title:"Default role authorization",
    	    title:"Privilege Details",
    	    url :url_left+"/getdate",
			param:paramGrid,
    	    // colNames:["ID","roleId","postCode","store","职务","店铺","角色","Role Description"],
    	    colNames:["ID","roleId","postCode","Position","Privilege","Privilege Description"],
    	    colModel:[
    	              {name:"id",type:"int",text:"left",width:"1",ishide:true},
    	              {name:"roleId",type:"int",text:"left",width:"1",ishide:true},
    	              {name:"postCode",type:"text",text:"left",width:"1",ishide:true},

    	              {name:"postName",type:"text",sort:true,sortindex:"post_code",text:"left",width:"100",ishide:false,css:"",getCustomValue:null},
    	              {name:"roleName",type:"text",sort:true,sortindex:"role_id",text:"left",width:"100"},
    	              {name:"remark",type:"text",text:"left",width:"150",ishide:false,css:"",getCustomValue:null}
    	             ],//列内容
    	    width:"max",//宽度自动
    	    rowPerPage:30,//每页数据量
    	    isPage:true,//是否需要分页
    	    sidx:"id",//排序字段
    	    sord:"desc",//升降序
    	    page:1,//当前页
    	    loadCompleteEvent:function(self){
				//数据加载完成后
				initListPageBut();//按钮与选中的tr初始化
				selectTrObj = null;
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				butPermissions(trObj);
			},
    	    buttonGroup:[
                 {butType:"custom",butHtml:"<button id='add' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-plus'></span>Add</button>"},//增加 默认："",lg:最大，sm小，xs超小
                  {butType:"update",butId:"update",butText:"Modify",butSize:""},//修改
                  {butType:"delete",butId:"del",butText:"Delete",butSize:""}
             ],
             
    	});
    }
  //按钮初始化
	var butPermissions = function(trObj){
//		//根据当前的数据 设定可显示的按钮
//		var tr = grid.getSelectColValue(trObj,"id,permissions");
//		trob = trObj;
//		var permissions  = tr["permissions"];
//		//permissions = permissions.split(",");
//		var buts = $("#zgGrid_but_box button");
//		buts.prop("disabled","disabled");
//		buts.each(function(){
//			var thisBut = $(this);
//			var thisButPcode = thisBut.attr("pcode");
//		　　	if(permissions.indexOf(thisButPcode)!=-1||thisButPcode==undefined){
//		　　		thisBut.removeAttr("disabled");
//		　　	}
//		});
		selectTrObj = trObj;//将选择的行赋给外部变量待用
		var buts = $("#zgGridTtable_but_box button");
		buts.prop("disabled","disabled");
		buts.each(function(){
			var thisBut = $(this);
		　　	thisBut.removeAttr("disabled");
		});
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('defaultRole');
_start.load(function (_common) {
	_index.init(_common);
});
