require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('informReplyEdit', function () {
	var self = {};
	var url_left = "",
		url_back = "",
		systemPath = "",
		paramGrid1 = null,
		paramGrid2 = null,
		selectTrTemp1 = null,
		selectTrTemp2 = null,
		common=null;
	var m = {
		toKen : null,
		main_box : null,
		reset: null,
		operateFlgByDialog:null,//Dialog操作类型 by 门店范围
		fileOperateFlgByDialog:null,//Dialog操作类型 by file
		operateFlg: null,//页面操作类型
		h_store_cd : null,//跳转页面通报店铺cd
		h_inform_cd : null,//跳转页面通报cd
		store_name : null,//店铺名称
		inform_reply_date : null,//回复时间
		inform_reply_content : null,//回复内容
		inform_cd : null,//通报cd
		inform_title : null,//通报标题
		inform_content : null,//通报内容
		file_name : null,//文件名称
		returnsViewBut : null,//返回
		submitBtn : null,//提交按钮
		affirmByReply:null //回复按钮
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_back = _common.config.surl+"/informReply";
		url_left =_common.config.surl+"/informReply/edit";
		systemPath = _common.config.surl;
		//事件绑定
		but_event();
		//列表初始化
		initTable();
		// 根据跳转加载数据，设置操作模式
		setValueByType();
	}

	//画面按钮点击事件
	var but_event = function(){
		//返回一览
		m.returnsViewBut.on("click",function(){
			top.location = url_back;
		});
		//下载
		m.main_box.on("click","a[class*='downLoad']",function(){
			var fileName = $(this).attr("fileName");
			var filePath = $(this).attr("filePath");
			window.open(systemPath+"/file/download?fileName="+fileName+"&filePath="+filePath,"_self");
		});
		//预览
		m.main_box.on("click","a[class*='preview']",function(){
			var fileName = $(this).attr("fileName");
			var filePath = $(this).attr("filePath");
			window.open(systemPath+"/file/preview?fileName="+fileName+"&filePath="+filePath,"toPreview");
		});
		m.submitBtn.on("click",function(){
			m.inform_reply_content.val("");
			$('#informReply_dialog').modal("show");
		})
		//提交
		m.affirmByReply.on("click",function(){
			var informCd = m.h_inform_cd.val();//通报编号
			var storeCd = m.h_store_cd.val();//店铺号
			var informReplyContent = m.inform_reply_content.val();//回复内容
			if(informReplyContent==null||informReplyContent.trim()==""){
				_common.prompt("The reply cannot be empty！",5,"error");
				m.inform_reply_content.focus();
				return;
			}
			_common.myConfirm("Please confirm whether to reply？",function(result){
				if(result == "true"){
					$.myAjaxs({
						url:url_left+"/reply",
						async:true,
						cache:false,
						type :"post",
						data :{
							informCd:informCd,
							storeCd:storeCd,
							informReplyContent:informReplyContent,
							toKen: m.toKen.val()
						},
						dataType:"json",
						success:function(result){
							if(result.success){
								_common.prompt("Reply to success！",3,"info");
								loadReply();
								$('#informReply_dialog').modal("hide");
							}else{
								_common.prompt(result.message,5,"error");
							}
							m.toKen.val(result.toKen);
						},
						error : function(e){
							_common.prompt("Reply to failed！",5,"error");/*回复失败*/
						},
						complete:_common.myAjaxComplete
					});
				}
			});

		});
	}

	//禁用页面按钮
	var disableAll = function () {
		m.inform_reply_content.prop("disabled",true);
		m.submitBtn.prop("disabled",true);
	}

	//启用页面按钮
	var enableAll = function () {
		m.inform_reply_content.prop("disabled",false);
		m.submitBtn.prop("disabled",false);
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		var _sts = m.operateFlg.val();
		if(_sts == "add"){

		}else if(_sts == "edit"){
			// 查询加载数据
			dataInitByPayCd();
		}else if(_sts == "view"){
			// 查询加载数据
			dataInitByPayCd();
			//禁用页面
			disableAll();
		}
	}

	//页面数据赋值
	var dataInitByPayCd = function () {
		//获取头档信息
		$.myAjaxs({
			url:url_left+"/getInformReply",
			async:true,
			cache:false,
			type :"post",
			data :{
				informCd:m.h_inform_cd.val(),
				storeCd:m.h_store_cd.val()
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					var ma4310ResultDto = result.data;
					if(ma4310ResultDto!=null){
						//赋值通报信息基本信息
						m.inform_cd.val(ma4310ResultDto.informCd);
						m.inform_title.val(ma4310ResultDto.informTitle);
						m.inform_content.val(ma4310ResultDto.informContent);
						m.store_name.val(ma4310ResultDto.storeName);
						// disableAll();
						paramGrid1 = "recordCd="+m.h_inform_cd.val();
						tableGrid1.setting("url", systemPath + "/file/getFileList");
						tableGrid1.setting("param", paramGrid1);
						tableGrid1.loadData(null);

						//加载回复信息
						loadReply();
					}
				}else{
					_common.prompt(result.message,5,"error");
				}
			},
			error : function(e){
				_common.prompt("request failed!",5,"error");
			},
			complete:_common.myAjaxComplete
		});
	}

	/**
	 * 加载回复信息列表
	 */
	var loadReply = function () {
		paramGrid2 = "informCd="+m.h_inform_cd.val()+"&storeCd="+m.h_store_cd.val()+"&informReply=1";
		tableGrid2.setting("url", systemPath + "/informReply/getReplyDetailList");
		tableGrid2.setting("param", paramGrid2);
		tableGrid2.loadData(null);
	}

	//表格初始化-列表样式
	var initTable = function(){
		tableGrid1 = $("#zgGridTtable1").zgGrid({
			title:"Attachments",
			param:paramGrid1,
			colNames:["File Name","Operation"],
			colModel:[
				{name:"fileName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"filePath",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:pathFmt}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp1 = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp1 = trObj;
			},
		});
		//回复信息
		tableGrid2 = $("#zgGridTtable2").zgGrid({
			title:"Reply List",
			param:paramGrid2,
			colNames:["Inform Cd","Reply Date","Reply Staff Id","Reply Staff","Reply Content","Store No.","Store Name"],
			colModel:[
				{name:"informCd",type:"text",text:"center",width:"100",ishide:true,css:""},
				{name:"informReplyDate",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"userId",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"userName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"informReplyContent",type:"text",text:"center",width:"200",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"storeName",type:"text",text:"center",width:"100",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:true,//是否需要分页
			page:1,//当前页
			sidx:"ma4315.inform_reply_date",//排序字段
			sord:"desc",//升降序
			rowPerPage:10,//每页数据量
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp2 = null;
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp2 = trObj;
			},
		});
	}


	//下载路径格式化
	var pathFmt = function(tdObj, value){
		var obj = value.split(",");
		var html = '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>';
		html += '<a href="javascript:void(0);" style="margin-left: 15px" title="DownLoad" class="downLoad" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>';
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

	/**
	 * 自定义函数名：PrefixZero
	 * @param num： 被操作数
	 * @param n： 固定的总位数
	 */
	function PrefixZero(num, n) {
		return (Array(n).join(0) + num).slice(-n);
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
	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('informReplyEdit');
_start.load(function (_common) {
	_index.init(_common);
});
