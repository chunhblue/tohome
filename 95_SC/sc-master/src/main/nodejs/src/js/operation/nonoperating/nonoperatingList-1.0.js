require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('nonoperatingList', function () {
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
		error_pcode : null,
		identity : null,
		searchJson: null,
		businessStartDate : null,
		businessEndDate : null,
		incomeStartCode : null,
		incomeEndCode: null,
		topDepCd: null,
		paymentType: null,
		incomeStatus: null,
		incomeDescription : null,
		descriptionName : null,
		operatorId : null,
		operatorName : null,
		search: null,
		reset: null,
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/nonoperating";
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
		//表格内按钮事件
		table_event();

		// $("#update").attr("disabled", "disabled");
		// $("#view").attr("disabled", "disabled");
	}

	//表格内按钮点击事件
	var table_event = function(){
		//查看按钮
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to view!",5,"error");/*请选择要查看的数据*/
				return false;
			}

			var cols = tableGrid.getSelectColValue(selectTrTemp,"sc1,sc2");
			var bussinessDate = cols['sc1'];
			var incomeCode = cols['sc1'];
			var identity = m.identity.val();

			top.location = url_left+"/view?&incomeCode="+incomeCode+"&identity="+identity;
		});
		//编辑按钮
		$("#modify").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to edit!",5,"error");/*请选择要编辑的数据*/
				return false;
			}

			var cols = tableGrid.getSelectColValue(selectTrTemp,"sc1,sc2");
			var bussinessDate = cols['sc1'];
			var incomeCode = cols['sc1'];
			var identity = m.identity.val();

			top.location = url_left+"/edit?&incomeCode="+incomeCode+"&identity="+identity;
		});
		//删除按钮
		$("#delete").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to delete!",5,"error");/*请选择要删除的数据*/
				return false;
			}
			_common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){/*请确认是否要删除选中的数据*/
				if(result=="true"){
					_common.prompt("Perform delete operation",5,"error");/*执行删除操作*/
					return;

					var cols = tableGrid.getSelectColValue(selectTrTemp,"sc1,sc2");
					var bussinessDate = cols['sc1'];
					var incomeCode = cols['sc1'];
					var identity = m.identity.val();

					$.myAjaxs({
						url:url_left+"/deleteVoucher",
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
	}

	//画面按钮点击事件
	var but_event = function(){
		//业务开始日期
		m.businessStartDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//业务结束日期
		m.businessEndDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//清空按钮
		m.reset.on("click",function(){
			m.businessStartDate.val("");
			m.businessEndDate.val("");
			m.incomeStartCode.val("");
			m.incomeEndCode.val("");
			m.topDepCd.val("");
			m.paymentType.val("");
			m.incomeStatus.val("");
			m.incomeDescription.val("");
			m.descriptionName.val("");
			m.operatorId.val("");
			m.operatorName.val("");
		});
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();
				// tableGrid.setting("url",url_left+"/getdata");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);

			}else{
				$("#search").prop("disabled",true);
			}
			//判断当前操作人是否为事业部长或系统部，如果是，曾增加待审核数量等内容的刷新机制，其他身份不参与此操作
			if(m.identity.val()=="2"||m.identity.val()=="3"){
				refreshWaitingReview();
			}
		});
	}
	//验证检索项是否合法
	var verifySearch = function(){
		return true;
	}
	//拼接检索参数
	var setParamJson = function(){
		if(m.businessStartDate.val()!=""&&m.businessEndDate.val()!=""){
			var intStartDate = fmtStringDate(m.businessStartDate.val());
			var intEndDate = fmtStringDate(m.businessEndDate.val());
			console.log(intStartDate+"---"+intEndDate);
			if(intStartDate>intEndDate){
				_common.prompt("Business start date cannot be greater than Business end date",5,"error");/*[业务开始日]不能大于[业务结束日]*/
				return false;
			}
		}
		// 创建请求字符串
		var searchJsonStr ={

		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}
	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable1();//列表初始化
				break;
			case "2":
				initTable2();//列表初始化
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}
	//表格初始化-采购样式
	var initTable1 = function(){
		var data = '[{"sc1":"20/12/2019","sc2":"0000002","sc3":"Vegetables","sc4":"Shop income","sc5":"1000.00","sc6":"Cash","sc7":"Carton fee","sc8":"","sc9":"Effective","sc10":"Test","sc11":"20/12/2019","sc12":"System"}]';
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Business Date","Income Code","Top Department","Income Subject","Income Amount","Payment Type","Income Description","Income Remarks","Income Status","Operator","Entry Date","Entry Staff"],
			colModel:[
				{name:"sc1",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc2",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc3",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc4",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc5",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc6",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc7",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc8",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc9",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc10",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc11",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc12",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null}
			],//列内容
			traverseData:data,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			// sidx:"bm.bm_type,order.bm_code",//排序字段
			// sord:"asc",//升降序
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
				{butType:"custom",butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-list-alt'></span>View</button>"},//增加 默认："",lg:最大，sm小，xs超小
				{butType:"custom",butHtml:"<button id='modify' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-edit'></span>Modify</button>"},//增加 默认："",lg:最大，sm小，xs超小
				{butType:"custom",butHtml:"<button id='delete' type='button' class='btn btn-danger btn-sm'><span class='glyphicon glyphicon-trash'></span>Delete</button>"}//增加 默认："",lg:最大，sm小，xs超小
			],
		});
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
var _index = require('nonoperatingList');
_start.load(function (_common) {
	_index.init(_common);
});
