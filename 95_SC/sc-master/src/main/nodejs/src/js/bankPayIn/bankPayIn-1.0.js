require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('bankPayIn', function () {
	var self = {};
	var url_left = "",
		systemPath = "",
		paramGrid = null,
		selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		i_a_store = null,
		flg_add=null,
		tempTrObjValue = {},//临时行数据存储
		//附件
		attachmentsParamGrid = null,
		common=null;
	var m = {
		update_payPerson:null,
		toKen : null,
		use : null,
		up : null,
		businessDate:null,
		clear_bs_date : null,
		bs_start_date: null,
		bs_end_date: null,
		bs_date: null,
		depositDate: null,
		s_payPerson:null,//搜索 缴款人
		payPerson: null,//缴款人
		payAmt: null,//缴款金额
		deltaNo: null,//序号
		description: null,//备注
		search : null,//检索按钮
		reset : null,//清空按钮
		affirm:null,
		cancel:null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		msg:null,
		update_payPerSon_name:null,
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		systemPath = _common.config.surl;
		url_left=_common.config.surl+"/bankPayIn";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		//画面按钮点击事件
		but_event();
		//初始化下拉
		initAutoMatic();
		// 初始化店铺运营组织检索
	//	_common.initOrganization();
		initOrganization();
		initTable1();
		//附件事件
		attachments_event();
		table_event();
		//权限验证
		isButPermission();
		// m.search.click();
		//initStoreManage();
		// 初始化检索日期
		_common.initDate(m.bs_start_date,m.bs_end_date);
	}

	//附件
	var attachments_event = function () {
		//附件一览表格
		attachmentsTable = $("#attachmentsTable").zgGrid({
			title:"Attachments",
			param:attachmentsParamGrid,
			colNames:["File Name","Download","PreView"],
			colModel:[
				{name:"fileName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"filePath",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
						var obj = value.split(",");
						var html = '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>';
						return $(tdObj).html(html);
					}
				},
				{name:"filePath1",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
						var obj = value.split(",");
						var html = '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>';
						return $(tdObj).html(html);
					}
				}
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
				selectTrTempFile = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTempFile = trObj;
			},
			buttonGroup:[
				{butType: "add",butId: "addByFile",butText: "Add",butSize: ""},//新增
				{butType: "update",butId: "updateByFile",butText: "Modify",butSize: ""},//修改
				{butType: "delete",butId: "deleteByFile",butText: "Delete",butSize: ""},//删除
			],
		});
		//附件表格新增
		var appendTrByFile = function (fileName,filePath) {
			var rowindex = 0;
			var trId = $("#attachmentsTable>.zgGrid-tbody tr:last").attr("id");
			if(trId!=null&&trId!=''){
				rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
			}
			var tr = '<tr id="attachmentsTable_'+rowindex+'_tr" class="">' +
				'<td tag="fileName" width="130" title="'+fileName+'" align="center" id="attachmentsTable_'+rowindex+'_tr_fileName" tdindex="attachmentsTable_fileName">'+fileName+'</td>' +
				'<td tag="filePath" width="100" title="Download" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath" tdindex="attachmentsTable_filePath">'+
				'<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>'+
				'</td>' +
				'<td tag="filePath1" width="100" title="Preview" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath1" tdindex="attachmentsTable_filePath1">'+
				'<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>'+
				'</td>' +
				'</tr>';
			$("#attachmentsTable>.zgGrid-tbody").append(tr);
		}

		//附件一览显示
		$("#attachments").on("click",function () {
			$('#attachments_dialog').modal("show");
		})
		//附件一览关闭
		$("#cancelByAttachments").on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$('#attachments_dialog').modal("hide");
				}
			})
		})
		//添加文件
		$("#addByFile").on("click", function () {
			$('#fileUpload_dialog').modal("show");
			$("#operateFlgByFile").val("1");
			$("#file_name").val("");
			$("#fileData").val("");
			$("#fileData").parent().parent().show();
		});
		//修改文件名称
		$("#updateByFile").on("click", function () {
			if(selectTrTempFile == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			$('#fileUpload_dialog').modal("show");
			$("#operateFlgByFile").val("2");
			$("#fileData").parent().parent().hide();
			var cols = attachmentsTable.getSelectColValue(selectTrTempFile,"fileName");
			var fileName = cols["fileName"];
			$("#file_name").val(fileName);
		});
		//删除文件
		$("#deleteByFile").on("click",function(){
			if(selectTrTempFile == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			_common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){
				if(result=="true"){
					$(selectTrTempFile[0]).remove();
					selectTrTempFile = null;
				}
			});
		});
		//提交按钮点击事件 文件上传
		$("#affirmByFile").on("click",function(){
			if($("#file_name").val()==null||$("#file_name").val()==''){
				$("#file_name").css("border-color","red");
				_common.prompt("File name cannot be empty!",5,"error");
				$("#file_name").focus();
				return;
			}else {
				$("#file_name").css("border-color","#CCCCCC");
			}
			_common.myConfirm("Are you sure you want to upload？",function(result){
				if(result=="true"){
					var flg = $("#operateFlgByFile").val();
					if(flg=="1"){
						if($('#fileData')[0].files[0]==undefined||$('#fileData')[0].files[0]==null){
							$("#fileData").css("border-color","red");
							_common.prompt("File cannot be empty!",5,"error");
							$("#fileData").focus();
							return;
						}else {
							$("#fileData").css("border-color","#CCCCCC");
						}
						var formData = new FormData();
						formData.append("fileData",$('#fileData')[0].files[0]);
						formData.append("toKen",m.toKen.val());
						$.myAjaxs({
							url:_common.config.surl+"/file/upload",
							async:false,
							cache:false,
							type :"post",
							data :formData,
							dataType:"json",
							processData:false,
							contentType:false,
							success:function(data,textStatus, xhr){
								var resp = xhr.responseJSON;
								if( resp.result == false){
									top.location = resp.s+"?errMsg="+resp.errorMessage;
									return ;
								}
								if(data.success==true){
									var fileName = $("#file_name").val();
									var filePath = data.data.filePath;
									appendTrByFile(fileName,filePath);
									_common.prompt("Operation Succeeded!",2,"success");
									$('#fileUpload_dialog').modal("hide");
								}else{
									_common.prompt(data.message,5,"error");
								}
								m.toKen.val(data.toKen);
							},
							complete:_common.myAjaxComplete
						});
					}else if(flg=="2"){
						var fileName = $("#file_name").val();
						$(selectTrTempFile[0]).find('td').eq(0).text(fileName);
						_common.prompt("Operation Succeeded!",2,"success");
						$('#fileUpload_dialog').modal("hide");
					}
				}
			});
		});

		$("#cancelByFile").on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$("#file_name").css("border-color","#CCCCCC");
					$("#fileData").css("border-color","#CCCCCC");
					$('#fileUpload_dialog').modal("hide");
				}
			})
		})

		//下载
		$("#attachments_dialog").on("click","a[class*='downLoad']",function(){
			var fileName = $(this).attr("fileName");
			var filePath = $(this).attr("filePath");
			window.open(_common.config.surl+"/file/download?fileName="+fileName+"&filePath="+filePath,"_self");
		});
		//预览
		$("#attachments_dialog").on("click","a[class*='preview']",function(){
			var fileName = $(this).attr("fileName");
			var filePath = $(this).attr("filePath");
			var url = _common.config.surl+"/file/preview?fileName="+fileName+"&filePath="+filePath;
			window.open(encodeURI(url),"toPreview");
		});
	};

	/**
	 * 初始化店铺运营组织检索
	 *
	 */
	var initOrganization = function () {
		// $("#aCity").attr("disabled", true);
		// $("#aDistrict").attr("disabled", true);
		// $("#aStore").attr("disabled", true);
		// $("#s_payPerson").attr("disabled",true);
		// $("#cityRefresh").hide();
		// $("#cityRemove").hide();
		// $("#districtRefresh").hide();
		// $("#districtRemove").hide();
		// $("#storeRefresh").hide();
		// $("#storeRemove").hide();

		a_region = $("#aRegion").myAutomatic({
			url: "roleStore/getRegion",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_city);
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				// $("#aCity").attr("disabled", true);
				// $("#aDistrict").attr("disabled", true);
				// $("#aStore").attr("disabled", true);
				// $("#cityRefresh").hide();
				// $("#cityRemove").hide();
				// $("#districtRefresh").hide();
				// $("#districtRemove").hide();
				// $("#storeRefresh").hide();
				// $("#storeRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#aCity").attr("disabled", false);
				// 	$("#cityRefresh").show();
				// 	$("#cityRemove").show();
				// }
				var rinput = thisObj.attr("k");
				var str = "&regionCd=" + rinput;
				$.myAutomatic.replaceParam(a_city, str);
			}
		});
		a_city = $("#aCity").myAutomatic({
			url:  "roleStore/getCity",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				// $("#aDistrict").attr("disabled", true);
				// $("#aStore").attr("disabled", true);
				// $("#districtRefresh").hide();
				// $("#districtRemove").hide();
				// $("#storeRefresh").hide();
				// $("#storeRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#aDistrict").attr("disabled", false);
				// 	$("#districtRefresh").show();
				// 	$("#districtRemove").show();
				// }
				var rinput = $("#aRegion").attr("k");
				var cinput = thisObj.attr("k");
				var str = "&regionCd=" + rinput + "&cityCd=" + cinput;
				$.myAutomatic.replaceParam(a_district, str);
			}
		});
		a_district = $("#aDistrict").myAutomatic({
			url:"roleStore/getDistrict",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_store);
				// $("#aStore").attr("disabled", true);
				// $("#storeRefresh").hide();
				// $("#storeRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#aStore").attr("disabled", false);
				// 	$("#storeRefresh").show();
				// 	$("#storeRemove").show();
				// }
				var rinput = $("#aRegion").attr("k");
				var cinput = $("#aCity").attr("k");
				var dinput = thisObj.attr("k");
				var str = "&regionCd=" + rinput + "&cityCd=" + cinput + "&districtCd=" + dinput;
				$.myAutomatic.replaceParam(a_store, str);
			}
		});
		a_store = $("#aStore").myAutomatic({
			url:  "roleStore/getStore",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$("#s_payPerson").find("option:not(:first)").remove();
				$("#s_payPerson").prop("disabled",true);
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					getSelectCashierPrivilege(thisObj.attr("k"));
					$("#s_payPerson").prop("disabled",false);
				}
			},
			cleanInput : function(){
				$("#s_payPerson").find("option:not(:first)").remove();
				
				$("#s_payPerson").prop("disabled",true);
				$("#s_payPerson").val("");
			}
		});
		function getSelectCashierPrivilege(storeCd,val) {
			if(storeCd!=null&&storeCd!=''){
				m.s_payPerson.find("option:first").remove();
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
								var optionSm=result.data[i].sm;
								m.s_payPerson.append(new Option(optionSm,optionSm));
							}
							m.s_payPerson.find("option:first").prop("selected",true);
							if(val!=null&&val!=""){
								m.s_payPerson.val(val);
							}
						}
					}
				})
			}
		}
		$("#regionRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_city);
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_store);
			m.s_payPerson.val("");
			// $("#aCity").attr("disabled", true);
			// $("#aDistrict").attr("disabled", true);
			// $("#aStore").attr("disabled", true);
			// $("#cityRefresh").hide();
			// $("#cityRemove").hide();
			// $("#districtRefresh").hide();
			// $("#districtRemove").hide();
			// $("#storeRefresh").hide();
			// $("#storeRemove").hide();
		});
		$("#cityRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_store);
			m.s_payPerson.val("");
			// $("#aDistrict").attr("disabled", true);
			// $("#aStore").attr("disabled", true);
			// $("#districtRefresh").hide();
			// $("#districtRemove").hide();
			// $("#storeRefresh").hide();
			// $("#storeRemove").hide();
		});
		$("#districtRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_store);
			m.s_payPerson.val("");
			// $("#aStore").attr("disabled", true);
			// $("#storeRefresh").hide();
			// $("#storeRemove").hide();
		});
		$("#storeRemove").on("click", function (e) {
			$("#s_payPerson").find("option:not(:first)").remove();
			$("#s_payPerson").prop("disabled",true);
			m.s_payPerson.val("");
		});
		$.myAjaxs({
			url:"roleStore/getStoreByRole",
			async:true,
			cache:false,
			type :"post",
			data :null,
			dataType:"json",
			success:function(re){
				if(re.success){
					var obj = re.o;
					$("#aCity").attr("disabled", false);
					$("#aDistrict").attr("disabled", false);
					$("#aStore").attr("disabled", false);
					$("#cityRefresh").show();
					$("#cityRemove").show();
					$("#districtRefresh").show();
					$("#districtRemove").show();
					$("#storeRefresh").show();
					$("#storeRemove").show();
					$.myAutomatic.setValueTemp(a_region,obj.regionCd,obj.regionName);
					$.myAutomatic.setValueTemp(a_city,obj.cityCd,obj.cityName);
					$.myAutomatic.setValueTemp(a_district,obj.districtCd,obj.districtName);
					$.myAutomatic.setValueTemp(a_store,obj.storeCd,obj.storeName);
					var str = "&regionCd=" + obj.regionCd;
					$.myAutomatic.replaceParam(a_city, str);
					str = str + "&cityCd=" + obj.cityCd;
					$.myAutomatic.replaceParam(a_district, str);
					str = str + "&districtCd=" + obj.districtCd;
					$.myAutomatic.replaceParam(a_store, str);
					getSelectCashierPrivilege(obj.storeCd)
				}
			}
		});
	}
	//初始化下拉
	var initAutoMatic = function () {
		i_a_store = $("#i_a_store").myAutomatic({
			url: systemPath + "/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					getSelecStoreManage(thisObj.attr("k"))
				}
				$('#payPerson').attr('disable',false);
			}
		});
		am = $("#am").myAutomatic({
			url: systemPath + "/ma1000/getAMByPM",
			ePageSize: 10,
			startCount: 0,
		});
		om = $("#om").myAutomatic({
			url: systemPath + "/ma1000/getOM",
			ePageSize: 10,
			startCount: 0,
		});
		oc = $("#oc").myAutomatic({
			url: systemPath + "/ma1000/getOC",
			ePageSize: 10,
			startCount: 0,
		});
	}
	function getSelecStoreManage(storeCd,val) {
		if(storeCd!=null&&storeCd!=''){
			m.payPerson.find("option:first").remove();
			$.myAjaxs({
				url:systemPath+"/ma1000/getSm",
				async:true,
				type:"post",
				data:'storeCd='+storeCd,
				dataType:"json",
				success:function (result) {
					if (result.success) {
						for (var i=0;i<result.data.length;i++){
							var optionText=result.data[i].sm;
							m.payPerson.append(new Option(optionText,optionText));
						}
						m.payPerson.find("option:first").prop("selected",true);
						if(val!=null&&val!=""){
							m.payPerson.val(val);
						}
					}
				}
			})
		}
	}
	var table_event = function(){
		$("#add").on("click", function () {
			initDialog();
			flg_add=1;
			$('#payPerson').attr("disable",true);
			$('#add_store').show();
			$('#update_store').hide();
			$('#payPerson').show();
			$('#update_payPerSon').hide();
			// m.bs_date.prop("disabled",true);
			m.bs_date.val(fmtIntDate(m.businessDate.val()));
			m.depositDate.val(fmtIntDate(m.businessDate.val()));
			//m.bs_date.removeAttr("disabled");
			$("#attachmentsTable>.zgGrid-tbody").empty();
			$('#bankPayIn_dialog').modal("show");
		});
		//修改
		$("#update").on("click", function () {
			if(selectTrTemp != null){
				initDialog();
				$('#add_store').hide();
				$('#update_store').show();
				$('#update_payPerSon').show();
				// $('#payPerson').hide();
				$('#payPerson').hide();
				// m.bs_date.attr("disabled","disabled");
				var cols = tableGrid.getSelectColValue(selectTrTemp,"deltaNo,storeCd,storeName,accDate,depositDate,payPerson,payAmt,description");
				$('#i_a_store').val(cols["storeName"]).attr("k",cols["storeCd"]).attr("v",cols["storeName"]);
				$("#update_store_name").val(cols["storeName"]);
				$("#payPerson").hide();
				$("#update_payPerSon_name").val(cols["payPerson"]);
				m.update_payPerSon_name.val(cols["payPerson"]);
				m.deltaNo.val(cols["deltaNo"]);
				m.bs_date.val(cols["accDate"]);
				m.depositDate.val(cols["depositDate"]);
				m.payAmt.val(cols["payAmt"]);
				m.description.val(cols["description"]);
				//加载附件信息
				attachmentsParamGrid = "recordCd="+cols["deltaNo"]+"&fileType=05";
				attachmentsTable.setting("url", systemPath + "/file/getFileList");//加载附件
				attachmentsTable.setting("param", attachmentsParamGrid);
				attachmentsTable.loadData(null);
				//附件按钮
				$("#addByFile").prop("disabled",false);
				$("#updateByFile").prop("disabled",false);
				$("#deleteByFile").prop("disabled",false);
				$('#bankPayIn_dialog').modal("show");
			}else{
				_common.prompt("Please select one row data!",5,"info");/*请选择一行数据*/
			}
		});
		//delete
		$("#delete").on("click",function(){
			if(selectTrTemp != null){
				_common.myConfirm("Are you sure to delete?",function(result){/*请确认是否删除*/
					if(result=="true"){
						var cols = tableGrid.getSelectColValue(selectTrTemp,"deltaNo,storeCd,accDate");
						var accDate = subfmtDate(cols["accDate"]);
						var storeCd = cols["storeCd"];
						var deltaNo = cols["deltaNo"];
						$.myAjaxs({
							url:url_left+"/delete",
							async:true,
							cache:false,
							type :"post",
							data :{
								storeCd:storeCd,
								accDate:accDate,
								deltaNo:deltaNo
							},
							dataType:"json",
							success:function(result){
								if(result.success){
									m.search.click();
									_common.prompt("deleted succeed!",5,"info");/*删除成功*/
								}else{
									_common.prompt(result.message,5,"error");
								}
							},
							error : function(e){
								_common.prompt("deleted failed!",5,"error");/*删除失败*/
							}
						});
					}
				});
			}else{
				_common.prompt("Please select one row data!",5,"info");/*请选择一行数据*/
			}
		});
		//附件一览显示
		$("#attachments_view").on("click",function () {
			if(selectTrTemp != null){
				var cols = tableGrid.getSelectColValue(selectTrTemp,"deltaNo");
				//加载附件信息
				attachmentsParamGrid = "recordCd="+cols["deltaNo"]+"&fileType=05";
				attachmentsTable.setting("url", systemPath + "/file/getFileList");//加载附件
				attachmentsTable.setting("param", attachmentsParamGrid);
				attachmentsTable.loadData(null);
				//附件按钮
				$("#addByFile").prop("disabled",true);
				$("#updateByFile").prop("disabled",true);
				$("#deleteByFile").prop("disabled",true);
				$('#attachments_dialog').modal("show");
			}else{
				_common.prompt("Please select one row data!",5,"info");/*请选择一行数据*/
			}
		})
		// 导出按钮点击事件
		$("#export").on("click",function(){
			if(verifySearch()){
				var startDate = "";
				if(m.bs_start_date.val()!=null&&m.bs_start_date.val()!=""){
					startDate = subfmtDate(m.bs_start_date.val())
				}
				var endDate = "";
				if(m.bs_end_date.val()!=null&&m.bs_end_date.val()!=""){
					endDate = subfmtDate(m.bs_end_date.val())
				}
				var searchJsonStr ={
					regionCd:$("#aRegion").attr("k"),
					cityCd:$("#aCity").attr("k"),
					districtCd:$("#aDistrict").attr("k"),
					storeCd:$("#aStore").attr("k"),
					ofc:$("#am").attr("k"),
					oc:$("#oc").attr("k"),
					om:$("#om").attr("k"),
					startDate: startDate,
					endDate: endDate,
					payPerson:isEmpty(m.s_payPerson.val())
				};
				paramGrid = "searchJson=" + JSON.stringify(searchJsonStr);
				var url = url_left + "/export?" + paramGrid;
				window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
			}
		});

		$("#toPdf").on("click",function(){
			if(verifySearch()){
				var startDate = "";
				if(m.bs_start_date.val()!=null&&m.bs_start_date.val()!==""){
					startDate = subfmtDate(m.bs_start_date.val())
				}
				var endDate = "";
				if(m.bs_end_date.val()!=null&&m.bs_end_date.val()!==""){
					endDate = subfmtDate(m.bs_end_date.val())
				}
				var searchJsonStr ={
					regionCd:$("#aRegion").attr("k"),
					cityCd:$("#aCity").attr("k"),
					districtCd:$("#aDistrict").attr("k"),
					storeCd:$("#aStore").attr("k"),
					ofc:$("#am").attr("k"),
					oc:$("#oc").attr("k"),
					om:$("#om").attr("k"),
					startDate: startDate,
					endDate: endDate,
					payPerson:isEmpty(m.s_payPerson.val())
				};
				paramGrid = "searchJson=" + JSON.stringify(searchJsonStr);
				var url = url_left + "/toPdf?" + paramGrid;
				window.open(encodeURI(url), "toPdf");
			}
		});
	}

	//画面按钮点击事件
	var but_event = function(){
		$("#payAmt").blur(function () {
			$("#payAmt").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#payAmt").focus(function(){
			$("#payAmt").val(reThousands(this.value));
		});

		//清空日期
		m.clear_bs_date.on("click",function(){
			m.bs_start_date.val("");
			m.bs_end_date.val("");
		});
		//营业日期
		m.bs_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			startDate: fmtDate(m.businessDate.val()),
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});

		//存款日期
		m.depositDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			startDate: fmtDate(m.businessDate.val()),
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});

		m.reset.on("click",function(){
			m.bs_start_date.val("");
			m.bs_end_date.val("");
			$("#amRemove").click();
			$("#omRemove").click();
			$("#ocRemove").click();
			$("#regionRemove").click();
			selectTrTemp = null;
			_common.clearTable();
		});
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				var regionCd = $("#aRegion").attr("k");
				var cityCd = $("#aCity").attr("k");
				var districtCd = $("#aDistrict").attr("k");
				var storeCd = $("#aStore").attr("k");
				var ofc=$("#am").attr("k");
				var om =$("#om").attr("k");
				var oc=$("#oc").attr("k");

				var startDate = "";
				if(m.bs_start_date.val().trim()!=null&&m.bs_start_date.val().trim()!=""){
					startDate = subfmtDate(m.bs_start_date.val())
				}
				var endDate = "";
				if(m.bs_end_date.val()!=null&&m.bs_end_date.val()!=""){
					endDate = subfmtDate(m.bs_end_date.val())
				}
				var payPerson = isEmpty(m.s_payPerson.val());
				paramGrid = "regionCd="+regionCd+"&cityCd="+cityCd+"&districtCd="+districtCd+"&storeCd="+storeCd+
					"&startDate="+startDate+"&endDate="+endDate+"&payPerson="+payPerson+"&ofc="+ofc+"&om="+om+"&oc="+oc;
				tableGrid.setting("url",url_left+"/getList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
		});

		m.cancel.on("click",function () {
			_common.myConfirm("Are you sure you want to cancel",function(result){
				$("#i_a_store").css("border-color","#CCCCCC");
				$("#bs_date").css("border-color","#CCCCCC");
				$("#depositDate").css("border-color","#CCCCCC");
				$("#payPerson").css("border-color","#CCCCCC");
				$("#payAmt").css("border-color","#CCCCCC");
				if(result=="true"){
					$('#bankPayIn_dialog').modal("hide");
					initDialog();
				}
			});
		})

		m.affirm.on("click",function () {
			var storeCd = $("#i_a_store").attr("k");
			if(isNull(storeCd)){
				$("#i_a_store").focus();
				$("#i_a_store").css("border-color","red");
				_common.prompt("Please select a store first!",5,"info");/*店铺不能为空*/
				return;
			}else {
				$("#i_a_store").css("border-color","#CCCCCC");
			}
			var accDate = m.bs_date.val();
			var depositDate = m.depositDate.val();
			if(isNull(accDate)){
				m.bs_date.focus();
				$("#bs_date").css("border-color","red");
				_common.prompt("The business date cannot be empty!",5,"info");/*业务日期不能为空*/
				return;
			}else if(_common.judgeValidDate($("#bs_date").val())){
					_common.prompt("Please enter a valid date!",3,"info");
					$("#bs_date").css("border-color","red");
					$("#bs_date").focus();
					return false;
			}else {
				$("#bs_date").css("border-color","#CCC");
			}


			if(isNull(depositDate)){
				m.depositDate.focus();
				$("#depositDate").css("border-color","red");
				_common.prompt("The business date cannot be empty!",5,"info");/*业务日期不能为空*/
				return;
			}else if(_common.judgeValidDate($("#depositDate").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#depositDate").css("border-color","red");
				$("#depositDate").focus();
				return false;
			}else {
				$("#depositDate").css("border-color","#CCC");
			}

			var payPerson = m.payPerson.val();
			var updatePerson = m.update_payPerSon_name.val();
			if(isNull(payPerson)){
				if (isNull(updatePerson)) {
				m.payPerson.focus();
				$("#payPerson").css("border-color","red");
				_common.prompt("The payer cannot be empty!",5,"info");/*缴款人不能为空*/
				return;
				}
			}else {
				$("#payPerson").css("border-color","#CCCCCC");
			}
			if(isNull(payPerson)){
				payPerson=updatePerson;
			}
			//金额正则
			var reg = /((^[1-9]\d*)|^0)(\,\.\d{0,2}){0,1}$/;
			var payAmt = reThousands(m.payAmt.val());
			if(isNull(payAmt)){
				m.payAmt.focus();
				$("#payAmt").css("border-color","red");
				_common.prompt("Bank Payment Amount cannot be empty!",5,"info");/*银行缴款金额不能为空*/
				return;
			}else{
				$("#payAmt").css("border-color","#CCCCCC");
			}
			if(!isNull(payAmt)&&!reg.test(payAmt)){
				m.payAmt.focus();
				$("#payAmt").css("border-color","red");
				_common.prompt("Bank Payment Amount format error!",5,"info");/*银行缴款金额格式错误*/
				return;
			}else {
				if(parseFloat(payAmt)<=0){
					m.payAmt.focus();
					$("#payAmt").css("border-color","red");
					_common.prompt("Bank Payment Amount must be greater than 0!",5,"info");/*银行缴款金额必须大于0*/
				return;
				}else if(parseFloat(payAmt)>=99999999999.50){
					m.payAmt.focus();
					$("#payAmt").css("border-color","red");
					_common.prompt("Bank Payment Amount must be lower than 99999999999.50!",5,"info");/*银行缴款金额必须低于银行卡存款上限*/
					return;
				}else{
					$("#payAmt").css("border-color","#CCCCCC");
				}
			}
			var arr ;
			var description = "";
			if($("#description").val() != null){
				arr = $("#description").val().split('\n');
				if(arr != null ){
					for(var i=0;i<arr.length;i++){
						if(i==arr.length-1){
							description += arr[i];
						}else {
							description = description + arr[i]+'\\n';
						}
					}
				}
				// description = $("#description").val().replaceAll('\t','\\t');
				// description = description.replaceAll('\n','\\n');
			}
			// var description = m.description.val();
			// if(isNull(description)){
			// 	description = null;
			// }
			var deltaNo = m.deltaNo.val();
			if(isNull(deltaNo)){
				deltaNo = null;
			}

			//附件信息
			var fileDetail = [],fileDetailJson = "";
			$("#attachmentsTable>.zgGrid-tbody tr").each(function () {
				var file = {
					fileType:'05',//文件类型 - 银行缴款
					fileName:$(this).find('td[tag=fileName]').text(),//文件名称
					filePath:$(this).find('td>a').attr("filepath"),//文件路径
				};
				fileDetail.push(file);
			});
			if(fileDetail.length>0){
				fileDetailJson = JSON.stringify(fileDetail)
			}
			if (isNull(m.deltaNo.val()) ){
				var date = {
					storeCd:storeCd,
					businessDate:subfmtDate(m.bs_date.val()),
				}
				$.myAjaxs({
					url:url_left+"/searchStoresInsert",
					async:false,   //同步 异步
					cache:false,
					type :"post",
					data :date,
					dataType:"json",
					success:function(result){
						if (result.success){
							$("#msg").val(result.s);
						}
					}
				});

			}
			  var val =$("#msg").val();
			// 2021/6/8 添加开始
			_common.myConfirm(val+"Are you sure you want to save?",function(result){
				$("#msg").val("");
				if(result=="true"){
						//  2021./6/9
						var  deltaNo = m.deltaNo.val();
						var flg = "add";
						if(!isNull(deltaNo)){
							flg = "update";
						}
						var date = {
							storeCd:storeCd,
							accDate:subfmtDate(accDate),
							depositDate:subfmtDate(depositDate),
							payPerson:payPerson,
							payAmt:_common.reThousands(payAmt),
							description:description,
							deltaNo:deltaNo,
							fileDetailJson:fileDetailJson,
							flg:flg
						};
						$.myAjaxs({
							url:url_left+"/save",
							async:true,
							cache:false,
							type :"post",
							data :date,
							dataType:"json",
							success:function(result){
								if(result.success){
									$('#bankPayIn_dialog').modal("hide");
									m.search.click();
									_common.prompt("Data saved successfully！",5,"info");/*保存成功*/
								}else{
									_common.prompt(result.message,5,"error");
								}
							},
							error : function(e){
								_common.prompt("Data saved failed！",5,"error"); // 保存失败
							}
						});

					}
				});
		})

	}
	
	var initDialog = function () {
		$("#i_a_store_clear").click();
		//m.bs_date.val("");
		m.payPerson.val("");
		m.payAmt.val("");
		m.description.val("");
		m.deltaNo.val("");
	}

	//表格初始化
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Query Result",
			param:paramGrid,
			colNames:["delta No","Store No.","Store Name","Area Manager Code","Area Manager Name","Operation Manager Code","Operation Manager Name","Operation Controller Code","Operation Controller Name","Business Date","Deposit Date","Store Manager","Bank Deposit Amount","Remarks"],
			colModel:[
				{name:"deltaNo",type:"text",text:"right",width:"100",ishide:true,css:""},
				{name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"ofc",type:"text",text:"right",width:"150",ishide:false,css:""},
				{name:"ofcName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"om",type:"text",text:"right",width:"200",ishide:false,css:""},
				{name:"omName",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"oc",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"ocName",type:"text",text:"center",width:"200",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"accDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"depositDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"payPerson",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"payAmt",type:"text",text:"right",width:"150",ishide:false,css:"",getCustomValue:formatNum},
				{name:"description",type:"text",text:"right",width:"130",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"acc_date,store_cd,delta_no",//排序字段
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
					butId: "update",
					butText: "Modify",
					butSize: ""//,
				},//修改
				{
					butType: "delete",
					butId: "delete",
					butText: "Delete",
					butSize: ""//,
				},//删除,
				{butType:"custom",butHtml:"<button id='attachments_view' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>"},//附件
				{butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"},
				{butType:"custom",butHtml:"<button id='toPdf' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-log-out icon-right'></span>To PDF</button>"}
			],
		});
	}


	// 按钮权限验证
	var isButPermission = function () {
		var editBut = $("#editBut").val();
		if (Number(editBut) != 1) {
			$("#update").remove();
		}
		var delBut = $("#delBut").val();
		if (Number(delBut) != 1) {
			$("#delete").remove();
		}
		var addBut = $("#addBut").val();
		if (Number(addBut) != 1) {
			$("#add").remove();
		}
	}

	function judgeNaN (value) {
		return value !== value;
	}

	//验证检索项是否合法
	var verifySearch = function(){
		let _StartDate = null;
		if(!$("#bs_start_date").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#bs_start_date").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#bs_start_date").val())).getTime();
			if(_common.judgeValidDate($("#bs_start_date").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#bs_start_date").css("border-color","red");
				$("#bs_start_date").focus();
				return false;
			}else {
				$("#bs_start_date").css("border-color","#CCC");
			}
		}
		let _EndDate = null;
		if(!$("#bs_end_date").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#bs_end_date").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#bs_end_date").val())).getTime();
			if(_common.judgeValidDate($("#bs_end_date").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#bs_end_date").css("border-color","red");
				$("#bs_end_date").focus();
				return false;
			}else {
				$("#bs_end_date").css("border-color","#CCC");
			}
		}
		if(_StartDate>_EndDate){
			$("#bs_end_date").focus();
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue>62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#bs_end_date").focus();
			return false;
		}
		return true;
	}
	//number格式化
	var formatNum = function(tdObj,value){
		return $(tdObj).text(fmtIntNum(value));
	}
	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
	}
	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
	}

	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}

	//格式化数字带千分位
	function fmtIntNum(val){
		if(val==null||val==""){
			return "0";
		}
		var reg=/\d{1,3}(?=(\d{3})+$)/g;
		return (val + '').replace(reg, '$&,');
	}

	function isNull(val) {
		if(val==null||val==""){
			return true;
		}
		return false;
	}
	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
		return res;
	}
	var isEmpty = function (str) {
		if (str == null || str === undefined || str === '') {
			return '';
		}
		return str;
	};

	function subfmtDate(date){
		var res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
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
var _index = require('bankPayIn');
_start.load(function (_common) {
	_index.init(_common);
});
