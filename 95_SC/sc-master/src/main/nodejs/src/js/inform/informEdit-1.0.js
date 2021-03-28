require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('informEdit', function () {
	var self = {};
	var url_left = "",
		url_back = "",
		systemPath = "",
		paramGrid = null,
		paramGrid1 = null,
		paramGrid2 = null,
		paramGrid3 = null,
		selectTrTemp = null,
		selectTrTemp1 = null,
		selectTrTemp3 = null,
		i_store = null,
		a_role = null,
		a_region = null,
		a_city = null,
		a_district = null,
		a_store = null,
		num = 1,
		common=null;
	const KEY = 'NOTIFICATIONS_MANAGEMENT';
	var m = {
		toKen : null,
		main_box : null,
		reset: null,
		operateFlgByStore:null,//Dialog操作类型 by 门店范围
		operateFlgByRole:null,//Dialog操作类型 by 角色范围
		operateFlgByFile:null,//Dialog操作类型 by file
		operateFlg: null,//页面操作类型
		h_inform_cd : null,//跳转页面通报cd
		inform_cd : null,//通报cd
		inform_title : null,//通报标题
		inform_start_date : null,//通报显示开始时间
		inform_end_date : null,//通报显示结束时间
		inform_content : null,//通报内容
		file_name : null,//文件名称
		cancelByStore : null,//取消 店铺范围
		affirmByStore : null,// 确认 店铺范围
		cancelByRole : null,//取消 角色范围
		affirmByRole : null,// 确认 角色范围
		cancelByFile : null,//取消 文件上传
		affirmByFile : null,// 确认 文件上传
		returnsViewBut : null,//返回
		submitBtn : null//提交按钮
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_back = _common.config.surl+"/informHQ";
		url_left =_common.config.surl+"/informHQ/edit";
		systemPath = _common.config.surl;
		// 店铺权限分配
		initOrganization();
		// 下拉初始化
		initAutoMatic();
		//事件绑定
		but_event();
		// initTable1();
		// store_event();
		//列表初始化
		initTable2();
		initTable3();
		//表格内按钮事件
		table_event();
		// 根据跳转加载数据，设置操作模式
		setValueByType();
	}

	var initOrganization = function () {
		// 初始化，子级禁用
		$("#a_city").attr("disabled", true);
		$("#a_district").attr("disabled", true);
		$("#a_store").attr("disabled", true);
		$("#cityRefresh").attr("disabled",true).css("pointer-events","none");
		$("#districtRefresh").attr("disabled",true).css("pointer-events","none");
		$("#storeRefresh").attr("disabled",true).css("pointer-events","none");
		$("#cityRefresh").hide();
		$("#cityRemove").hide();
		$("#districtRefresh").hide();
		$("#districtRemove").hide();
		$("#storeRefresh").hide();
		$("#storeRemove").hide();

		// 输入框事件绑定
		a_region = $("#a_region").myAutomatic({
			url: systemPath + "/organizationalStructure/getStructureByLevel?level=0",
			ePageSize: 10,
			startCount: 0,
			cleanInput:function(self){
				$.myAutomatic.cleanSelectObj(a_city);
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				$("#a_city").attr("disabled", true);
				$("#a_district").attr("disabled", true);
				$("#a_store").attr("disabled", true);
				$("#cityRefresh").hide();
				$("#cityRemove").hide();
				$("#districtRefresh").hide();
				$("#districtRemove").hide();
				$("#storeRefresh").hide();
				$("#storeRemove").hide();
			},
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_city);
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				$("#a_city").attr("disabled", true);
				$("#a_district").attr("disabled", true);
				$("#a_store").attr("disabled", true);
				if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999999") {
					$("#a_city").attr("disabled", false);
					$("#cityRefresh").attr("disabled",false).css("pointer-events","auto");
					// 2021/02/23
					$("div[name='resStoreRow']").each(function (i, ev) {
						$("#storeFlgVal").val(num);
						++num;
					})
				}
				var rinput = thisObj.attr("k");
				// 替换子级查询参数
				var str = "&level=1&parentId=" + rinput;
				$.myAutomatic.replaceParam(a_city, str);
				$("#cityRefresh").show();
				$("#cityRemove").show();
			}
		});
		a_city = $("#a_city").myAutomatic({
			url: systemPath + "/organizationalStructure/getStructureByLevel",
			ePageSize: 10,
			startCount: 0,
			cleanInput:function(self){
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				$("#a_district").attr("disabled", true);
				$("#a_store").attr("disabled", true);
				$("#districtRefresh").hide();
				$("#districtRemove").hide();
				$("#storeRefresh").hide();
				$("#storeRemove").hide();
			},
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_store);
				$("#a_district").attr("disabled", true);
				$("#a_store").attr("disabled", true);
				if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999999") {
					$("#a_district").attr("disabled", false);
					$("#districtRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var cinput = thisObj.attr("k");
				// 替换子级查询参数
				var str = "&level=2&parentId=" + cinput;
				$.myAutomatic.replaceParam(a_district, str);
				$("#districtRefresh").show();
				$("#districtRemove").show();
			}
		});
		a_district = $("#a_district").myAutomatic({
			url: systemPath + "/organizationalStructure/getStructureByLevel",
			ePageSize: 10,
			startCount: 0,
			cleanInput:function(self){
				$.myAutomatic.cleanSelectObj(a_store);
				$("#a_store").attr("disabled", true);
				$("#storeRefresh").hide();
				$("#storeRemove").hide();
			},
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_store);
				$("#a_store").attr("disabled", true);
				if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999999") {
					$("#a_store").attr("disabled", false);
					$("#storeRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = thisObj.attr("k");
				// 替换子级查询参数
				var str = "&level=3&parentId=" + dinput;
				$.myAutomatic.replaceParam(a_store, str);
				$("#storeRefresh").show();
				$("#storeRemove").show();
			}
		});
		a_store = $("#a_store").myAutomatic({
			url: systemPath + "/organizationalStructure/getStructureByLevel",
			ePageSize: 10,
			startCount: 0,
		});

		// 添加按钮事件
		$("#addStoreResource").on("click", function (e) {
			// 获取选择值
			var rinput = $("#a_region").attr("k");
			var cinput = $("#a_city").attr("k");
			var dinput = $("#a_district").attr("k");
			var sinput = $("#a_store").attr("k");
			var rinputName = $("#a_region").attr("v");
			var cinputName = $("#a_city").attr("v");
			var dinputName = $("#a_district").attr("v");
			var sinputName = $("#a_store").attr("v");

			if (rinput == null || rinput === "") {
				rinput = "999999";
				rinputName = "All Region";
			}
			if (cinput == null || cinput === "") {
				cinput = "999999";
				cinputName = "All City";
			}
			if (dinput == null || dinput === "") {
				dinput = "999999";
				dinputName = "All District";
			}
			if (sinput == null || sinput === "") {
				sinput = "999999";
				sinputName = "All Store";
			}
			// 遍历已选择值，判断包含和重复
			var count = 0;
			$("div[name='resStoreRow']").each(function (i, ev) {
				var region = $(this).find("input[name='regionRes']").val();
				var city = $(this).find("input[name='cityRes']").val();
				var district = $(this).find("input[name='districtRes']").val();
				var store = $(this).find("input[name='storeRes']").val();
				// modify by lch 2021/02/23
				// 已选择 All Region
				if(region === '999999'){
					_common.prompt("Has the full store permission, cannot submit!", 5, "info");/* 已经拥有全店铺权限，不可提交 */
					count++;
					return false;
				}
				if(rinput === '999999' && region != null){
					_common.prompt("Has the full store permission, cannot submit!", 5, "info");/* 已经拥有全店铺权限，不可提交 */
					count++;
					return false;
				}
				// 已选择 NationWide：包含南北区，如果子级选择全部，则效果等同于All Region
				if(region === '000001' && city === '999999'){
					_common.prompt("Has the full store permission, cannot submit!", 5, "info");/* 已经拥有全店铺权限，不可提交 */
					count++;
					return false;
				}
				if(rinput === '000001' && cinput === '999999' && city != null){
					_common.prompt("Has the full store permission, cannot submit!", 5, "info");/* 已经拥有全店铺权限，不可提交 */
					count++;
					return false;
				}
				// 大区相同或大区选择000001，城市已选择All City
				if ((region === rinput || region==='000001') && city === '999999') {
					_common.prompt("Already has the permission of the entire store in the City, cannot submit!", 5, "info");/* 已经拥有该大区全店铺权限，不可提交 */
					count++;
					return false;
				}
				// 大区相同或大区输入框选择000001，城市已选择All City 且大区和城市输入框同在南方或北方
				if ((region === rinput || rinput==='000001') && city === '999999'
						&& ((region!=='000002' && cinput.substring(0,1) === 'N') || (region!=='000003' && cinput.substring(0,1) === 'S'))) {
					_common.prompt("Already has the permission of the entire store in the City, cannot submit!", 5, "info");/* 已经拥有该大区全店铺权限，不可提交 */
					count++;
					return false;
				}
				// 大区相同或大区输入框选择000001，城市输入框已选择All City 且大区和城市输入框同在南方或北方
				if ((region === rinput || region==='000001') && cinput === '999999'
					&& ((rinput!=='000002' && city.substring(0,1) === 'N') || (rinput!=='000003' && city.substring(0,1) === 'S'))) {
					_common.prompt("Already has the permission of the entire store in the City, cannot submit!", 5, "info");/* 已经拥有该大区全店铺权限，不可提交 */
					count++;
					return false;
				}
				// 大区相同或大区输入框选择000001，城市已选择All City
				if ((region === rinput || rinput==='000001') && cinput === '999999') {
					_common.prompt("Already has the permission of the entire store in the City, cannot submit!", 5, "info");/* 已经拥有该大区全店铺权限，不可提交 */
					count++;
					return false;
				}

				// 大区、城市相同，区域已选择All District
				if (city === cinput && district === '999999') {
					_common.prompt("Already has the permission of the entire store in the district, cannot submit!", 5, "info");/* 已经拥有该城市全店铺权限，不可提交 */
					count++;
					return false;
				}
				if (city === cinput && dinput === '999999' && district != null) {
					_common.prompt("Already has the permission of the entire store in the district, cannot submit!", 5, "info");/* 已经拥有该城市全店铺权限，不可提交 */
					count++;
					return false;
				}

				// 大区、城市、区域相同，店铺已选择All District
				if (city === cinput && district === dinput && store === '999999') {
					_common.prompt("Already has the permission of the entire store in the district, cannot submit!", 5, "info");/* 已经拥有该区域全店铺权限，不可提交 */
					count++;
					return false;
				}
				if (city === cinput && district === dinput && sinput === '999999' && store != null) {
					_common.prompt("Already has the permission of the entire store in the district, cannot submit!", 5, "info");/* 已经拥有该区域全店铺权限，不可提交 */
					count++;
					return false;
				}

				// 选择相同店铺
				if (city === cinput && district === dinput && store === sinput) {
					_common.prompt("The selected resource group is the same as the selected resource group and cannot be submitted!", 5, "info");/* 选择的资源组与已选的资源组重复，不可提交 */
					count++;
					return false;
				}
			});
			if (count > 0) {
				return;
			}
			// 添加、显示
			var storeFlgVal = $("#storeFlgVal").val();
			if(storeFlgVal != null && storeFlgVal!== ''){
				store_index = parseInt(storeFlgVal) + 1;
			}else {
				store_index = storeFlgVal + 1;
			}
			var resourceGroup = "<div class='row' name='resStoreRow'>" +
				"    <div class='col-xs-12 col-sm-12 col-md-11 col-lg-11'>" +
				"        <div class='form-horizontal'>" +
				"            <div class='form-group form-group-resource'>" +
				"                <label for='iyPost' class='col-sm-1 control-label'>Region</label>" +
				"                <div class='col-sm-2'>" +
				"                    <div class='res-info'>" + rinputName + "</div>" +
				"                    <input type='hidden' class='form-control input-sm'" +
				"                           name='regionRes' value='" + rinput + "'>" +
				"                </div>" +
				"                <label for='iyPost' class='col-sm-1 control-label'>City</label>" +
				"                <div class='col-sm-2'>" +
				"                    <div class='res-info'>" + cinputName + "</div>" +
				"                    <input type='hidden' class='form-control input-sm'" +
				"                           name='cityRes' value='" + cinput + "'>" +
				"                </div>" +
				"                <label for='iyPost' class='col-sm-1 control-label'>District</label>" +
				"                <div class='col-sm-2'>" +
				"                    <div class='res-info'>" + dinputName + "</div>" +
				"                    <input type='hidden' class='form-control input-sm'" +
				"                           name='districtRes' value='" + dinput + "'>" +
				"                </div>" +
				"                <label for='iyPost' class='col-sm-1 control-label'>Store</label>" +
				"                <div class='col-sm-2'>" +
				"                    <div class='res-info'>" + sinputName + "</div>" +
				"                    <input type='hidden' class='form-control input-sm'" +
				"                           name='storeRes' value='" + sinput + "'>" +
				"                </div>" +
				"            </div>" +
				"        </div>" +
				"    </div>" +
				"    <div class='col-xs-12 col-sm-12 col-md-1 col-lg-1'>" +
				"        <div class='col-sm-1'>" +
				"            <a class='btn btn-default btn-sm' href='javascript:void(0);'" +
				"               role='button' id='delStoreResource" + store_index + "'>" +
				"                <div class='glyphicon glyphicon-minus'></div>" +
				"            </a>" +
				"        </div>" +
				"    </div>" +
				"</div>";
			$("#storeSplitLine").before(resourceGroup);
			// 重置栏位，准备下一次选值
			$.myAutomatic.cleanSelectObj(a_region);

		})

		// 绑定删除按钮事件
		$("#resStore").on("click", "a[id*='delStoreResource']", function (e) {
			var aId;
			if (e.target.tagName === "A") {
				aId = $(e.target).attr("id");
			} else {
				aId = $(e.target).parent().attr("id");
			}
			$("#" + aId).closest("div[name='resStoreRow']").remove();
		})
	}

	var integrationPermissionsDataToHideStore = function() {
		var storeRangeDetail = [], num = 0;
		$("#from_hide_stores").text("");
		$("div[name='resStoreRow']").each(function (e) {
			++num;
			var storeRange = {
				regionCd:$(this).find("input[name='regionRes']").val(),
				cityCd:$(this).find("input[name='cityRes']").val(),
				districtCd:$(this).find("input[name='districtRes']").val(),
				storeCd:$(this).find("input[name='storeRes']").val(),
				serialNo:num
			}
			storeRangeDetail.push(storeRange);
		});
		$("#from_hide_stores").val(JSON.stringify(storeRangeDetail));
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


	var store_event = function () {
		$("#addByStore").on("click", function () {
			$("#i_store_clear").click();
			m.operateFlgByStore.val("1");
			$('#informEditStore_dialog').modal("show");
		});
		//修改门店范围
		$("#updateByStore").on("click", function () {
			if(selectTrTemp == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,storeName");
			var storeCd = cols["storeCd"];
			var storeName = cols["storeName"];
			$("#i_store").attr("k",storeCd).attr("v",storeName).val(storeName);
			$("#oldStoreCd").val(storeCd);
			m.operateFlgByStore.val("2");
			$('#informEditStore_dialog').modal("show");
		});
		//删除门店范围
		$("#deleteByStore").on("click",function(){
			if(selectTrTemp == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd");
			_common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){
				if(result=="true"){
					$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
						var storeCd = $(this).find('td[tag=storeCd]').text();
						if(storeCd==cols["storeCd"]){
							$(this).remove();
							return;
						}
					});
				}
			});
		});
		//提交按钮点击事件 店铺范围
		m.affirmByStore.on("click",function(){
			var storeCd = $("#i_store").attr("k");
			var storeName = $("#i_store").attr("v");
			var operateFlgByStore = m.operateFlgByStore.val();
			if(storeCd==null||storeCd==""){
				_common.prompt("Please select Store！",5,"error");
				$("#i_store").focus();
				return false;
			}
			var repArticle = 0;
			$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
				var storeCd_text = $(this).find('td[tag=storeCd]').text();
				if(operateFlgByStore=="1"&&storeCd==storeCd_text){
					repArticle++;
				}
				if(operateFlgByStore=="2"&&storeCd==storeCd_text&&storeCd!=$("#oldStoreCd").val()){
					repArticle++;
				}
			});
			if(repArticle>0){
				_common.prompt("The data already exists！",5,"error");
				return false;
			}
			_common.myConfirm("Are you sure to save the data?",function(result){
				if(result == "true"){
					if(operateFlgByStore=="1"){
						appendTrByStore(storeCd,storeName);
					}else if(operateFlgByStore=="2"){
						var updateFlg = false;
						$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
							var storeCd_text = $(this).find('td[tag=storeCd]').text();
							if(storeCd_text==$("#oldStoreCd").val()){
								$(this).find('td[tag=storeCd]').text(storeCd);
								$(this).find('td[tag=storeName]').text(storeName);
								updateFlg = true;
							}
						});
						if(!updateFlg){
							appendTrByStore(storeCd,storeName);
						}
					}
					$('#informEditStore_dialog').modal("hide");
				}
			});
		});

		m.cancelByStore.on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$("#i_store").css("border-color","#CCCCCC");
					$('#informEditStore_dialog').modal("hide");
				}
			})
		})
	}
	//门店范围表格新增
	var appendTrByStore = function (storeCd,storeName) {
		var rowindex = 0;
		var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
		if(trId!=null&&trId!=''){
			rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
		}
		var tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
			'<td tag="storeCd" width="100" title="'+storeCd+'" align="center" id="zgGridTtable_'+rowindex+'_tr_storeCd" tdindex="zgGridTtable_storeCd">'+storeCd+'</td>' +
			'<td tag="storeName" width="130" title="'+storeName+'" align="center" id="zgGridTtable_'+rowindex+'_tr_storeName" tdindex="zgGridTtable_storeName">'+storeName+'</td>' +
			'</tr>';
		$("#zgGridTtable>.zgGrid-tbody").append(tr);
	}

	var table_event = function(){
		//----------------------角色范围
		$("#addByRole").on("click", function () {
			$("#a_role_clear").click();
			m.operateFlgByRole.val("1");
			$('#informEditRole_dialog').modal("show");
		});
		//修改角色范围
		$("#updateByRole").on("click", function () {
			if(selectTrTemp1 == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			var cols = tableGrid1.getSelectColValue(selectTrTemp1,"roleId,roleName");
			var roleId = cols["roleId"];
			var roleName = cols["roleName"];
			$("#a_role").attr("k",roleId).attr("v",roleName).val(roleName);
			$("#oldRoleId").val(roleId);
			m.operateFlgByRole.val("2");
			$('#informEditRole_dialog').modal("show");
		});
		//删除角色范围
		$("#deleteByRole").on("click",function(){
			if(selectTrTemp1 == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			var cols = tableGrid1.getSelectColValue(selectTrTemp1,"roleId");
			_common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){
				if(result=="true"){
					$("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
						var roleId = $(this).find('td[tag=roleId]').text();
						if(roleId==cols["roleId"]){
							$(this).remove();
							return;
						}
					});
				}
			});
		});
		//提交按钮点击事件 店铺范围
		m.affirmByRole.on("click",function(){
			var roleId = $("#a_role").attr("k");
			var roleName = $("#a_role").attr("v");
			var operateFlgByRole = m.operateFlgByRole.val();
			if(roleId==null||roleId==""){
				$("#a_role").css("border-color","red");
				_common.prompt("Please select Role！",5,"error");
				$("#a_role").focus();
				return false;
			}else {
				$("#a_role").css("border-color","#CCCCCC");
			}
			var repArticle = 0;
			$("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
				var roleId_text = $(this).find('td[tag=roleId]').text();
				if(operateFlgByRole=="1"&&roleId_text==roleId){
					repArticle++;
				}
				if(operateFlgByRole=="2"&&roleId==roleId_text&&roleId!=$("#oldRoleId").val()){
					repArticle++;
				}
			});
			if(repArticle>0){
				_common.prompt("The data already exists！",5,"error");
				return false;
			}
			_common.myConfirm("Are you sure to save the data?",function(result){
				if(result == "true"){
					if(operateFlgByRole=="1"){
						appendTrByRole(roleId,roleName);
					}else if(operateFlgByRole=="2"){
						var updateFlg = false;
						$("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
							var roleId_text = $(this).find('td[tag=roleId]').text();
							if(roleId_text==$("#oldRoleId").val()){
								$(this).find('td[tag=roleId]').text(roleId);
								$(this).find('td[tag=roleName]').text(roleName);
								updateFlg = true;
							}
						});
						if(!updateFlg){
							appendTrByRole(roleId,roleName);
						}
					}
					$('#informEditRole_dialog').modal("hide");
				}
			});
		});

		m.cancelByRole.on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$("#a_role").css("border-color","#CCCCCC");
					$('#informEditRole_dialog').modal("hide");
				}
			})
		})

		//--------------------------附件
		//添加文件
		$("#addByFile").on("click", function () {
			$('#fileUpload_dialog').modal("show");
			m.operateFlgByFile.val("1");
			m.file_name.val("");
			$("#fileData").val("");
			$("#fileData").parent().parent().show();
		});
		//修改文件名称
		$("#updateByFile").on("click", function () {
			if(selectTrTemp2 == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			$('#fileUpload_dialog').modal("show");
			m.operateFlgByFile.val("2");
			$("#fileData").parent().parent().hide();
			var cols = tableGrid2.getSelectColValue(selectTrTemp2,"fileName");
			var fileName = cols["fileName"];
			m.file_name.val(fileName);
		});
		//删除文件
		$("#deleteByFile").on("click",function(){
			if(selectTrTemp2 == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}

			_common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){
				if(result=="true"){
					$(selectTrTemp2[0]).remove();
					selectTrTemp2 = null;
				}
			});
		});
		//提交按钮点击事件 文件上传
		m.affirmByFile.on("click",function(){
			if(m.file_name.val()==null||m.file_name.val()==''){
				m.file_name.css("border-color","red");
				_common.prompt("File name cannot be empty!",5,"error");
				m.file_name.focus();
				return;
			}else {
				m.file_name.css("border-color","#CCCCCC");
			}
			_common.myConfirm("Are you sure you want to upload？",function(result){
				if(result=="true"){
					var flg = m.operateFlgByFile.val();
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
							url:systemPath+"/file/upload",
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
						$(selectTrTemp2[0]).find('td').eq(0).text(fileName);
						_common.prompt("Operation Succeeded!",2,"success");
						$('#fileUpload_dialog').modal("hide");
					}
				}
			});
		});

		m.cancelByFile.on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$("#file_name").css("border-color","#CCCCCC");
					$("#fileData").css("border-color","#CCCCCC");
					$('#fileUpload_dialog').modal("hide");
				}
			})
		})

		// $('#fileData').on("change",function(){
		// 	var fileName = m.file_name.val();
		// 	if(fileName!=null&&fileName!=''){
		// 		m.file_name.val($(this)[0].name)
		// 	}
		// })
	}

	//角色范围表格新增
	var appendTrByRole = function (roleId,roleName) {
		var rowindex = 0;
		var trId = $("#zgGridTtable1>.zgGrid-tbody tr:last").attr("id");
		if(trId!=null&&trId!=''){
			rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
		}
		var tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
			'<td tag="roleId" width="100" title="'+roleId+'" align="center" id="zgGridTtable1_'+rowindex+'_tr_roleId" tdindex="zgGridTtable1_roleId">'+roleId+'</td>' +
			'<td tag="roleName" width="130" title="'+roleName+'" align="center" id="zgGridTtable1_'+rowindex+'_tr_roleName" tdindex="zgGridTtable1_roleName">'+roleName+'</td>' +
			'</tr>';
		$("#zgGridTtable1>.zgGrid-tbody").append(tr);
	}

	//附件表格新增
	var appendTrByFile = function (fileName,filePath) {
		var rowindex = 0;
		var trId = $("#zgGridTtable2>.zgGrid-tbody tr:last").attr("id");
		if(trId!=null&&trId!=''){
			rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
		}
		var tr = '<tr id="zgGridTtable2_'+rowindex+'_tr" class="">' +
			'<td tag="fileName" width="130" title="'+fileName+'" align="center" id="zgGridTtable2_'+rowindex+'_tr_fileName" tdindex="zgGridTtable2_fileName">'+fileName+'</td>' +
			'<td tag="filePath" width="100" title="" align="center" id="zgGridTtable2_'+rowindex+'_tr_filePath" tdindex="zgGridTtable2_filePath">'+
			'<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>'+
			'<a href="javascript:void(0);" style="margin-left: 15px" title="DownLoad" class="downLoad" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>'+
			'</td>' +
			'</tr>';
		$("#zgGridTtable2>.zgGrid-tbody").append(tr);
	}

	//画面按钮点击事件
	var but_event = function(){
		m.inform_start_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		m.inform_end_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		m.returnsViewBut.on("click",function(){
			var bank = $("#submitBtn").attr("disabled");
			if (bank!='disabled' ) {
				_common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
					if (result==="true") {
						_common.updateBackFlg(KEY);
						top.location = url_back;
					}});
			}
			if (bank==='disabled' ) {
				_common.updateBackFlg(KEY);
				top.location = url_back;
			}
		});

		//返回一览
		// m.returnsViewBut.on("click",function(){
		// 	top.location = url_back;
		// });
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
		//提交
		m.submitBtn.on("click",function(){
			var informCd = m.inform_cd.val();
			var informTitle = m.inform_title.val();
			var informStartDate = subfmtDate(m.inform_start_date.val());
			var informEndDate = subfmtDate(m.inform_end_date.val());
			var informContent = m.inform_content.val();
			var operateFlg = m.operateFlg.val();
			if(informTitle==null||informTitle==""){
				m.inform_title.css("border-color","red");
				_common.prompt("Please enter a notification title！",5,"info");/*请输入通报标题*/
				m.inform_title.focus();
				return false;
			}else {
				m.inform_title.css("border-color","#CCCCCC");
			}
			if(informStartDate==null||informStartDate==""){
				m.inform_start_date.css("border-color","red");
				_common.prompt("Please enter the display start time！",5,"info");/*请输入显示开始时间*/
				m.inform_start_date.focus();
				return false;
			}else {
				m.inform_start_date.css("border-color","#CCCCCC");
			}
			if(informEndDate==null||informEndDate==""){
				m.inform_end_date.css("border-color","red");
				_common.prompt("Please enter the display end time！",5,"info");/*请输入显示结束时间*/
				m.inform_end_date.focus();
				return false;
			}else {
				m.inform_end_date.css("border-color","#CCCCCC");
			}
			if(informContent==null||informContent==""){
				m.inform_content.css("border-color","red");
				_common.prompt("Please enter the content of the notice！",5,"info");/*请输入通报内容*/
				m.inform_content.focus();
				return false;
			}else {
				m.inform_content.css("border-color","#CCCCCC");
			}


			var roleDetail = [],fileDetail = [];
			// var storeDetail = [];
			// $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
			// 	var store = {
			// 		informCd:informCd,
			// 		storeCd:$(this).find('td[tag=storeCd]').text(),//店铺号
			// 	}
			// 	storeDetail.push(store);
			// });
			$("#zgGridTtable1>.zgGrid-tbody tr").each(function () {
				var store = {
					informCd:informCd,
					roleId:$(this).find('td[tag=roleId]').text(),//角色id
				}
				roleDetail.push(store);
			});
			$("#zgGridTtable2>.zgGrid-tbody tr").each(function () {
				var file = {
					informCd:informCd,
					fileType:'01',//文件类型 - 通知
					fileName:$(this).find('td[tag=fileName]').text(),//文件名称
					filePath:$(this).find('td>a').attr("filepath"),//文件路径
				}
				fileDetail.push(file);
			});
			var roleDetailJson = "",fileDetailJson = "";
			// var storeDetailJson = "";

			// if(storeDetail.length>0){
			// 	storeDetailJson = JSON.stringify(storeDetail)
			// }else{
			// 	_common.prompt("Please add a store range！",5,"error");/*请添加门店范围*/
			// 	return false;
			// }
			var storelen = $("div[name='resStoreRow']").length;
			if(storelen>0){
				//整合店铺范围信息
				integrationPermissionsDataToHideStore()
			}else{
				_common.prompt("Please add a store range！",5,"error");/*请添加门店范围*/
				return false;
			}
			if(roleDetail.length>0){
				roleDetailJson = JSON.stringify(roleDetail)
			}else{
				_common.prompt("Please add a role range！",5,"error");/*请添加角色范围*/
				return false;
			}
			if(fileDetail.length>0){
				fileDetailJson = JSON.stringify(fileDetail)
			}
			_common.myConfirm("Are you sure to save the data?",function(result){
				if(result == "true"){
					$.myAjaxs({
						url:url_left+"/save",
						async:true,
						cache:false,
						type :"post",
						data :{
							informCd:informCd,
							informTitle:informTitle,
							informContent:informContent,
							informStartDate:informStartDate,
							informEndDate:informEndDate,
							storeDetailJson:$("#from_hide_stores").val(),
							// storeDetailJson:storeDetailJson,
							fileDetailJson:fileDetailJson,
							roleDetailJson:roleDetailJson,
							toKen:m.toKen.val(),
							operateFlg:operateFlg//操作状态
						},
						dataType:"json",
						success:function(result){
							if(result.success){
								_common.prompt("Data saved successfully！",5,"info");/*保存成功*/
								//设置通报编号
								m.inform_cd.val(result.data.informCd);
								//禁用页面按钮
								disableAll();
							}else{
								_common.prompt(result.message,5,"error");
							}
							m.toKen.val(result.toKen);
						},
						error : function(e){
							_common.prompt("Data saved failed！",5,"error");/*保存失败*/
						},
						complete:_common.myAjaxComplete
					});
				}
			});

		});
	}

	//禁用页面按钮
	var disableAll = function () {
		m.inform_title.prop("disabled",true);
		m.inform_start_date.prop("disabled",true);
		m.inform_end_date.prop("disabled",true);
		m.inform_content.prop("disabled",true);
		// $("#addByStore").prop("disabled",true);
		// $("#updateByStore").prop("disabled",true);
		// $("#deleteByStore").prop("disabled",true);
		$("a[id*='delStoreResource']").prop("disabled",true);
		$("a[id*='delStoreResource']").hide();
		$("#addStoreResource").prop("disabled",true);
		$("#storeSplitLine").hide();
		$("#stores").hide();
		$("#addByRole").prop("disabled",true);
		$("#updateByRole").prop("disabled",true);
		$("#deleteByRole").prop("disabled",true);
		$("#addByFile").prop("disabled",true);
		$("#updateByFile").prop("disabled",true);
		$("#deleteByFile").prop("disabled",true);
		m.submitBtn.prop("disabled",true);
	}

	//启用页面按钮
	var enableAll = function () {
		m.inform_title.prop("disabled",false);
		m.inform_start_date.prop("disabled",false);
		m.inform_end_date.prop("disabled",false);
		m.inform_content.prop("disabled",false);
		// $("#addByStore").prop("disabled",false);
		// $("#updateByStore").prop("disabled",false);
		// $("#deleteByStore").prop("disabled",false);
		$("a[id*='delStoreResource']").prop("disabled",false);
		$("a[id*='delStoreResource']").show();
		$("#addStoreResource").prop("disabled",false);
		$("#storeSplitLine").show();
		$("#stores").show();
		$("#addByRole").prop("disabled",false);
		$("#updateByRole").prop("disabled",false);
		$("#deleteByRole").prop("disabled",false);
		$("#addByFile").prop("disabled",false);
		$("#updateByFile").prop("disabled",false);
		$("#deleteByFile").prop("disabled",false);
		m.submitBtn.prop("disabled",false);
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		var _sts = m.operateFlg.val();
		if(_sts == "add"){
			//隐藏回复记录信息
			$("#replyTab").hide();
			$("#card4").hide();
		}else if(_sts == "edit"){
			// 查询加载数据
			dataInitByPayCd();
			//隐藏回复记录信息
			$("#replyTab").hide();
			$("#card4").hide();
		}else if(_sts == "view"){
			// 查询加载数据
			dataInitByPayCd();
			//记载回复信息grid
			initTable4();
			//加载回复信息列表
			loadReply();
			//禁用页面
			disableAll();
		}
	}

	//页面数据赋值
	var dataInitByPayCd = function () {
		//获取头档信息
		$.myAjaxs({
			url:url_left+"/getMa4300",
			async:true,
			cache:false,
			type :"post",
			data :"informCd="+m.h_inform_cd.val(),
			dataType:"json",
			success:function(result){
				if(result.success){
					var ma4300Dto = result.data;
					//赋值通报信息基本信息
					m.inform_cd.val(ma4300Dto.informCd);
					m.inform_title.val(ma4300Dto.informTitle);
					m.inform_content.val(ma4300Dto.informContent);
					m.inform_start_date.val(fmtIntDate(ma4300Dto.informStartDate));
					m.inform_end_date.val(fmtIntDate(ma4300Dto.informEndDate));
					paramGrid = "informCd="+m.h_inform_cd.val();
					paramGrid1 = "informCd="+m.h_inform_cd.val();
					paramGrid2 = "recordCd="+m.h_inform_cd.val();//附件信息
					//查询门店范围信息
					// tableGrid.setting("url", url_left + "/getMa4310List");
					// tableGrid.setting("param", paramGrid);
					// tableGrid.loadData(null);
					tableGrid1.setting("url", url_left + "/getMa4305List");
					tableGrid1.setting("param", paramGrid1);
					tableGrid1.loadData(null);
					tableGrid2.setting("url", systemPath + "/file/getFileList");//加载附件
					tableGrid2.setting("param", paramGrid2);
					tableGrid2.loadData(null);
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
	//初始化自动下拉
	var initAutoMatic = function () {
		i_store = $("#i_store").myAutomatic({
			url: systemPath + "/ma1000/getStoreAll",
			ePageSize: 10,
			startCount: 0
		});
		a_role = $("#a_role").myAutomatic({
			url: systemPath + "/informHQ/getRoleListAll",
			ePageSize: 10,
			startCount: 0
		});
	}

	//表格初始化-门店范围列表样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Store Range",
			param:paramGrid,
			colNames:["Store No.","Store Name"],
			colModel:[
				{name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""}
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
				selectTrTemp = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
			buttonGroup:[
				{
					butType: "add",
					butId: "addByStore",
					butText: "Add",
					butSize: ""//,
				},//新增
				{
					butType: "update",
					butId: "updateByStore",
					butText: "Modify",
					butSize: ""//,
				},//修改
				{
					butType: "delete",
					butId: "deleteByStore",
					butText: "Delete",
					butSize: ""//,
				},//删除
			],
		});
	}

	//表格初始化-角色范围列表样式
	var initTable2 = function(){
		tableGrid1 = $("#zgGridTtable1").zgGrid({
			title:"Privilege Range",
			param:paramGrid1,
			colNames:["Privilege Cd","Privilege Name"],
			colModel:[
				{name:"roleId",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"roleName",type:"text",text:"left",width:"130",ishide:false,css:""}
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
			buttonGroup:[
				{
					butType: "add",
					butId: "addByRole",
					butText: "Add",
					butSize: ""//,
				},//新增
				{
					butType: "update",
					butId: "updateByRole",
					butText: "Modify",
					butSize: ""//,
				},//修改
				{
					butType: "delete",
					butId: "deleteByRole",
					butText: "Delete",
					butSize: ""//,
				},//删除
			],
		});
	}

	//表格初始化-附件列表样式
	var initTable3 = function(){
		tableGrid2 = $("#zgGridTtable2").zgGrid({
			title:"Attachments",
			param:paramGrid2,
			colNames:["File Name","Operation"],
			colModel:[
				{name:"fileName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"filePath",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:pathFmt},
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
				selectTrTemp2 = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp2 = trObj;
			},
			buttonGroup:[
				{
					butType: "add",
					butId: "addByFile",
					butText: "Add",
					butSize: ""//,
				},//新增
				{
					butType: "update",
					butId: "updateByFile",
					butText: "Modify",
					butSize: ""//,
				},//修改
				{
					butType: "delete",
					butId: "deleteByFile",
					butText: "Delete",
					butSize: ""//,
				},//删除
			],
		});
	}

	//表格初始化-回复信息列表样式
	var initTable4 = function(){
		//回复信息
		tableGrid3 = $("#zgGridTtable3").zgGrid({
			title:"Reply List",
			param:paramGrid3,
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
				selectTrTemp3 = null;
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp3 = trObj;
			},
		});
	}

	/**
	 * 加载回复信息列表
	 */
	var loadReply = function () {
		paramGrid3 = "informCd="+m.h_inform_cd.val();
		tableGrid3.setting("url", systemPath + "/informReply/getReplyDetailList");
		tableGrid3.setting("param", paramGrid3);
		tableGrid3.loadData(null);
	}


	//路径格式化
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
var _index = require('informEdit');
_start.load(function (_common) {
	_index.init(_common);
});
