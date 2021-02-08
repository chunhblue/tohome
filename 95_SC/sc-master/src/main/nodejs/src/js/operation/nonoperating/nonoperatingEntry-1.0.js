require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('nonoperatingEntry', function () {
    var self = {};
    var url_left = "",
		url_back = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;

	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		isEdit: true,
		businessDate : null,
		incomeCode: null,
		topDepCd: null,
		incomeAmount: null,
		paymentType: null,
		incomeStatus: null,
		incomeSubject: null,
		subjectName: null,
		incomeDescription: null,
		incomeRemarks: null,
		operatorId: null,
		operatorName: null,
		add: null,
		delete: null,
		save: null,
		cancel: null,
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
		url_left = _common.config.surl + "/nonoperatingEntry";
		url_back = _common.config.surl + "/nonoperating";
		//首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// console.log(m.use.val());
		//根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// initPageBytype("1");

		// $("#update").attr("disabled", "disabled");
		// $("#view").attr("disabled", "disabled");
	}

	//画面按钮点击事件
	var but_event = function(){
		//业务日期
		m.businessDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//新增按钮
		m.add.on("click",function(){
			_common.myConfirm("Please confirm whether to add a new record？",function(result){/*请确认是否要新增记录*/
				if(result == "true"){
					clearFields();
					if(!m.isEdit){
						setEnable();
					}
				}
			});
		});
		//删除按钮
		m.delete.on("click",function(){
			if(!m.isEdit){
				return;
			}
			_common.myConfirm("Please confirm whether to delete data？",function(result){/*请确认是否要删除数据*/
				if(result == "true"){

				}
			});
		});
		//保存按钮
		m.save.on("click",function(){
			if(m.isEdit && verifySearch()){
				//拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();

			}else{
				$("#save").prop("disabled",true);
			}
		});
		//取消按钮
		m.cancel.on("click",function(){
			if(!m.isEdit){
				return;
			}
			_common.myConfirm("Please confirm if you want to clear the data？",function(result){/*请确认是否要清除数据*/
				if(result == "true"){
					clearFields();
				}
			});
		});
		//返回按钮
		m.back.on("click",function(){
			top.location = url_back;
		});
	}

	//清空字段值
	var clearFields = function(){
		m.businessDate.val("");
		m.incomeCode.val("");
		m.topDepCd.val("");
		m.incomeAmount.val("");
		m.paymentType.val("");
		m.incomeStatus.val("");
		m.incomeSubject.val("");
		m.subjectName.val("");
		m.incomeDescription.val("");
		m.incomeRemarks.val("");
		m.operatorId.val("");
		m.operatorName.val("");
	}

	//验证检索项是否合法
	var verifySearch = function(){
		return true;
	}

	//拼接参数
	var setParamJson = function(){
		// 创建请求字符串
		var searchJsonStr ={

		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "0":
				m.isEdit = false;
				setDisabled();
				break;
			case "1":
				break;
			case "2":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	//设置画面查看模式
	var setDisabled = function(){
		$("#businessDate").prop("disabled",true);
		$("#incomeCode").prop("disabled",true);
		$("#topDepCd").prop("disabled",true);
		$("#incomeAmount").prop("disabled",true);
		$("#paymentType").prop("disabled",true);
		$("#incomeStatus").prop("disabled",true);
		$("#incomeSubject").prop("disabled",true);
		$("#subjectName").prop("disabled",true);
		$("#incomeDescription").prop("disabled",true);
		$("#incomeRemarks").prop("disabled",true);
		$("#operatorId").prop("disabled",true);
		$("#operatorName").prop("disabled",true);
		$("#save").prop("disabled",true);
		$("#cancel").prop("disabled",true);
		$("#delete").prop("disabled",true);
	}

	//设置画面编辑模式
	var setEnable = function(){
		m.isEdit = true;
		$("#businessDate").prop("disabled",false);
		$("#incomeCode").prop("disabled",false);
		$("#topDepCd").prop("disabled",false);
		$("#incomeAmount").prop("disabled",false);
		$("#paymentType").prop("disabled",false);
		$("#incomeStatus").prop("disabled",false);
		$("#incomeSubject").prop("disabled",false);
		$("#subjectName").prop("disabled",false);
		$("#incomeDescription").prop("disabled",false);
		$("#incomeRemarks").prop("disabled",false);
		$("#operatorId").prop("disabled",false);
		$("#operatorName").prop("disabled",false);
		$("#save").prop("disabled",false);
		$("#cancel").prop("disabled",false);
		$("#delete").prop("disabled",false);
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
var _index = require('nonoperatingEntry');
_start.load(function (_common) {
	_index.init(_common);
});
