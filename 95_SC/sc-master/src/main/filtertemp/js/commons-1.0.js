define('common',['messenger'],function(messenger) {
	var self = {};

	var config = {
		baseurl:"${m.syspath}",
		surl : "${m.syspath}/a"
	}
	var myConfirmFunction = null;
	$._messengerDefaults = {
		extraClasses: 'messenger-fixed messenger-theme-ice messenger-on-bottom'
	}

	var getAuditFlowChart = function (recordId,typeId,startDate) {
		$.myAjaxs({
			url:config.surl+"/audit/getStatus",
			async:true,
			cache:false,
			type :"post",
			data :{
				recordId : recordId,
				startDate : startDate,
				typeId : typeId
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					if(result.data){
						// $("#audit_process")
						$("#auditFlowChart").html(result.data)
					}
				}
			},
			error : function(e){
				prompt("审核流程图加载失败",5,"error");
			}
		});
	}

	/**
	 * 验证日期是否为业务日期  true为业务日 false则不是
	 * 判断订单号是否上传 为空则不判断
	 * @param date 验证日期
	 * @param orderId 订单号
	 * @param callback
	 */
	var checkBusinessDate = function (date,orderId,callback) {
		$.myAjaxs({
			url:config.surl+"/urgentOrderDc/checkBusinessDate",
			async:true,
			cache:false,
			data :{
				orderId : orderId,
				date : date
			},
			type :"get",
			dataType:"json",
			success:function(result){
				callback(result);
			},
			complete:myAjaxComplete
		});
	}

	/**
	 * 验证当前时间是否禁用
	 * @param callback
	 */
	var timeCheck = function (callback) {
		$.myAjaxs({
			url:config.surl+"/orderDetails/timeCheck",
			async:true,
			cache:false,
			type :"get",
			dataType:"json",
			success:function(result){
				callback(result);
			},
			complete:myAjaxComplete
		});
	}

	/**
	 * 验证当前时间是否禁用（batch处理时不能使用）
	 * @param callback
	 */
	var batchCheck = function (callback) {
		$.myAjaxs({
			url:config.surl+"/batchCheck",
			async:false,
			cache:false,
			type :"get",
			dataType:"json",
			success:function(result){
				callback(result);
			},
			complete:myAjaxComplete
		});
	}

	/**
	 * 初始化大中小分类
	 */
	var initCategoryAutoMatic = function () {
		// $("#pma").attr("disabled", true);
		// $("#category").attr("disabled", true);
		// $("#subCategory").attr("disabled", true);
		// $("#pmaRefresh").hide();
		// $("#pmaRemove").hide();
		// $("#categoryRefresh").hide();
		// $("#categoryRemove").hide();
		// $("#subCategoryRefresh").hide();
		// $("#subCategoryRemove").hide();
		a_dep = $("#dep").myAutomatic({
			url: config.surl + "/roleStore/deps",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_pma);
				$.myAutomatic.cleanSelectObj(a_category);
				$.myAutomatic.cleanSelectObj(a_subCategory);
				// $("#pma").attr("disabled", true);
				// $("#category").attr("disabled", true);
				// $("#subCategory").attr("disabled", true);
				// $("#pmaRefresh").hide();
				// $("#pmaRemove").hide();
				// $("#categoryRefresh").hide();
				// $("#categoryRemove").hide();
				// $("#subCategoryRefresh").hide();
				// $("#subCategoryRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999") {
				// 	$("#pma").attr("disabled", false);
				// 	$("#pmaRefresh").show();
				// 	$("#pmaRemove").show();
				// }
				var dinput = thisObj.attr("k");
				var str = "&depId=" + dinput;
				$.myAutomatic.replaceParam(a_pma, str);//赋值
				$.myAutomatic.replaceParam(a_category, str);
				$.myAutomatic.replaceParam(a_subCategory, str);
			}
		});
		a_pma = $("#pma").myAutomatic({
			url: config.surl + "/roleStore/pmas",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_category);
				$.myAutomatic.cleanSelectObj(a_subCategory);
				// $("#category").attr("disabled", true);
				// $("#subCategory").attr("disabled", true);
				// $("#categoryRefresh").hide();
				// $("#categoryRemove").hide();
				// $("#subCategoryRefresh").hide();
				// $("#subCategoryRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#category").attr("disabled", false);
				// 	$("#categoryRefresh").show();
				// 	$("#categoryRemove").show();
				// }
				var dinput = $("#dep").attr("k");
				var pinput = thisObj.attr("k");
				var str = "&depId=" + dinput+"&pmaId=" + pinput;
				$.myAutomatic.replaceParam(a_category, str);//赋值
				$.myAutomatic.replaceParam(a_subCategory, str);
			}
		});
		a_category = $("#category").myAutomatic({
			url: config.surl + "/roleStore/categorys",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_subCategory);
				// $("#subCategory").attr("disabled", true);
				// $("#subCategoryRefresh").hide();
				// $("#subCategoryRemove").hide();
				// if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
				// 	$("#subCategory").attr("disabled", false);
				// 	$("#subCategoryRefresh").show();
				// 	$("#subCategoryRemove").show();
				// }
				var dinput = $("#dep").attr("k");
				var pinput = $("#pma").attr("k");
				var cinput = thisObj.attr("k");
				var str = "&depId=" + dinput+"&pmaId=" + pinput+"&categoryId=" + cinput;
				$.myAutomatic.replaceParam(a_subCategory, str);//赋值
			}
		});
		a_subCategory = $("#subCategory").myAutomatic({
			url: config.surl + "/roleStore/subCategorys",
			ePageSize: 10,
			startCount: 0,
		});
		// $.myAutomatic.cleanSelectObj(a_dep);
		// $.myAutomatic.cleanSelectObj(a_pma);
		// $.myAutomatic.cleanSelectObj(a_category);
		// $.myAutomatic.cleanSelectObj(a_subCategory);

		$("#depRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_pma);
			$.myAutomatic.cleanSelectObj(a_category);
			$.myAutomatic.cleanSelectObj(a_subCategory);
			$.myAutomatic.replaceParam(a_pma, null);
			$.myAutomatic.replaceParam(a_category, null);
			$.myAutomatic.replaceParam(a_subCategory, null);
		// 	$("#pma").attr("disabled", true);
		// 	$("#category").attr("disabled", true);
		// 	$("#subCategory").attr("disabled", true);
		// 	$("#pmaRefresh").hide();
		// 	$("#pmaRemove").hide();
		// 	$("#categoryRefresh").hide();
		// 	$("#categoryRemove").hide();
		// 	$("#subCategoryRefresh").hide();
		// 	$("#subCategoryRemove").hide();
		});
		$("#pmaRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_category);
			$.myAutomatic.cleanSelectObj(a_subCategory);
			$.myAutomatic.replaceParam(a_category, null);
			$.myAutomatic.replaceParam(a_subCategory, null);
		// 	$("#category").attr("disabled", true);
		// 	$("#subCategory").attr("disabled", true);
		// 	$("#categoryRefresh").hide();
		// 	$("#categoryRemove").hide();
		// 	$("#subCategoryRefresh").hide();
		// 	$("#subCategoryRemove").hide();
		});
		$("#categoryRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_subCategory);
			$.myAutomatic.replaceParam(a_subCategory, null);
		// 	$("#subCategory").attr("disabled", true);
		// 	$("#subCategoryRefresh").hide();
		// 	$("#subCategoryRemove").hide();
		});
	};

	/**
	 * 初始化店铺运营组织检索
	 *
	 */
	var initOrganization = function () {
		// 初始化，子级禁用
		// $("#aCity").attr("disabled", true);
		// $("#aDistrict").attr("disabled", true);
		// $("#aStore").attr("disabled", true);
		// $("#cityRefresh").hide();
		// $("#cityRemove").hide();
		// $("#districtRefresh").hide();
		// $("#districtRemove").hide();
		// $("#storeRefresh").hide();
		// $("#storeRemove").hide();
		// 输入框事件绑定
		a_region = $("#aRegion").myAutomatic({
			url: config.surl + "/roleStore/getRegion",
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
				// 替换子级查询参数
				var str = "&regionCd=" + rinput;
				$.myAutomatic.replaceParam(a_city, str);
				$.myAutomatic.replaceParam(a_district, str);
				$.myAutomatic.replaceParam(a_store, str);
			}
		});
		a_city = $("#aCity").myAutomatic({
			url: config.surl + "/roleStore/getCity",
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
				// 替换子级查询参数
				var str = "&regionCd=" + rinput + "&cityCd=" + cinput;
				$.myAutomatic.replaceParam(a_district, str);
				$.myAutomatic.replaceParam(a_store, str);
			}
		});
		a_district = $("#aDistrict").myAutomatic({
			url: config.surl + "/roleStore/getDistrict",
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
				// 替换子级查询参数
				var str = "&regionCd=" + rinput + "&cityCd=" + cinput + "&districtCd=" + dinput;
				$.myAutomatic.replaceParam(a_store, str);
			}
		});
		a_store = $("#aStore").myAutomatic({
			url: config.surl + "/roleStore/getStore",
			ePageSize: 8,
			startCount: 0,
			selectEleClick: function (thisObj){
				$('#hidStore').val(thisObj.attr('k'));
			}
		});

		// 选值栏位清空按钮事件绑定
		$("#regionRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_city);
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_store);
			$.myAutomatic.replaceParam(a_city, null);
			$.myAutomatic.replaceParam(a_district, null);
			$.myAutomatic.replaceParam(a_store, null);
		// 	$("#aCity").attr("disabled", true);
		// 	$("#aDistrict").attr("disabled", true);
		// 	$("#aStore").attr("disabled", true);
		// 	$("#cityRefresh").hide();
		// 	$("#cityRemove").hide();
		// 	$("#districtRefresh").hide();
		// 	$("#districtRemove").hide();
		// 	$("#storeRefresh").hide();
		// 	$("#storeRemove").hide();
		});
		$("#cityRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_store);
			$.myAutomatic.replaceParam(a_district, null);
			$.myAutomatic.replaceParam(a_store, null);
		// 	$("#aDistrict").attr("disabled", true);
		// 	$("#aStore").attr("disabled", true);
		// 	$("#districtRefresh").hide();
		// 	$("#districtRemove").hide();
		// 	$("#storeRefresh").hide();
		// 	$("#storeRemove").hide();
		});
		$("#districtRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_store);
			$.myAutomatic.replaceParam(a_store, null);
		// 	$("#aStore").attr("disabled", true);
		// 	$("#storeRefresh").hide();
		// 	$("#storeRemove").hide();
		});

		// 如果只有一条店铺权限，则默认选择
		$.myAjaxs({
			url:config.surl+"/roleStore/getStoreByRole",
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
					// 替换子级查询参数
					var str = "&regionCd=" + obj.regionCd;
					$.myAutomatic.replaceParam(a_city, str);
					str = str + "&cityCd=" + obj.cityCd;
					$.myAutomatic.replaceParam(a_district, str);
					str = str + "&districtCd=" + obj.districtCd;
					$.myAutomatic.replaceParam(a_store, str);
				}
			}
		});
	}

	/**
	 * 初始化店铺名称下拉
	 */
	var initStoreform = function () {
		aStore = $("#aStore").myAutomatic({  //加上名字可以存入自动填充
			url: config.surl + "/ma1000/getStoreByPM",
			ePageSize: 6,
			startCount: 0
		});
		// 栏位清空
		$("#storeRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(aStore);
		});
		return aStore;
	}

	/**
	 * 获取通知信息
	 */
	var getInform = function () {
		$.myAjaxs({
			url:config.surl+"/getNotificationsCount",
			async:true,
			cache:false,
			type :"get",
			dataType:"json",
			success:function(result){
				if(result){
					if(result.success){
						$("#notifications").show();
						let count = result.data.count;
						let notificationCount = result.data.notificationCount;
						let mmPromotionCount = result.data.mmPromotionCount;
						let newItemCount = result.data.newItemCount;
						if(count==0){
							$("#notifications").hide();
						}else if(count>99){
							$("#count").text("99+");
							$("#notifications").show();
						}else{
							$("#count").text(count);
							$("#notifications").show();
						}
						if(notificationCount==0){
							$("#notification").hide();
						}else if(notificationCount>99){
							$("#notification").text("99+");
							$("#notification").show();
						}else{
							$("#notification").text(notificationCount);
							$("#notification").show();
						}
						if(newItemCount==0){
							$("#newItem").hide();
						}else if(newItemCount>99){
							$("#newItem").text("99+");
							$("#newItem").show();
						}else{
							$("#newItem").text(newItemCount);
							$("#newItem").show();
						}
						if(mmPromotionCount==0){
							$("#mmPromotion").hide();
						}else if(mmPromotionCount>99){
							$("#mmPromotion").text("99+");
							$("#mmPromotion").show();
						}else{
							$("#mmPromotion").text(mmPromotionCount);
							$("#mmPromotion").show();
						}
					}else{
						$("#notifications").hide();
						$("#notification").hide();
						$("#newItem").hide();
						$("#mmPromotion").hide();
					}
				}
			},
			error : function(e){
				_common.prompt("Notifications failed to load data!",5,"error");
			},
			complete:myAjaxComplete
		});
	}

	// 清除分页
	var resetPaging = function () {
		let html = '<li class="disabled"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>' +
			'<li class="active" value="1"><a href="javascript:void(0)">1</a></li>' +
			'<li class="disabled"><a href="javascript:void(0)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
		$(".pagination").html(html);
	}

	/**
	 * 加载分页条数据
	 * @param totalPage 总页数
	 * @param totalCount 总条数
	 * @param page 当前页数
	 * @param rows 每页显示条数
	 */
	var loadPaging = function (totalPage,totalCount,page,rows) {
		// 定义开始位置begin,结束位置 end
		var begin; // 开始位置
		var end; //  结束位置

		//1.要显示10个页码
		if (totalPage < 10) {
			//总页码不够10页
			begin = 1;
			end = totalPage;
		} else {
			//总页码超过10页
			begin = page - 5;
			end = page + 4;

			//2.如果前边不够5个，后边补齐10个
			if (begin < 1) {
				begin = 1;
				end = begin + 9;
			}

			//3.如果后边不足4个，前边补齐10个
			if (end > totalPage) {
				end = totalPage;
				begin = end - 9;
			}
		}

		if (page < 0) {
			page = 1;
		}

		// 添加上一页
		var pages = "";
		if (page <= 1) {
			var prePage = '<li value="'+1+'" class="disabled"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
		} else {
			var prePage = '<li value="'+(page-1)+'"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
		}
		pages += prePage;
		for (var i = begin; i <= end; i++) {
			var pageHtml;
			if (i == page) {
				pageHtml = '<li class="active" value="'+i+'"><a href="javascript:void(0)">'+i+'</a></li>';
			} else {
				pageHtml = '<li value="'+i+'"><a href="javascript:void(0)">'+i+'</a></li>';
			}
			pages += pageHtml;
		}

		// 添加下一页
		if (page >= totalPage) {
			var nextPage = '<li value="'+totalPage+'" class="disabled"><a href="javascript:void(0)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
		} else {
			var nextPage = '<li value="'+(page+1)+'"><a href="javascript:void(0)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
		}
		pages += nextPage;
		// 添加数据进列表
		$(".pagination").html(pages);
	}

	// 设置千分位, 四舍五入 保留小数位 dit
	function toThousands(num,dit) {
		if (!num) {
			return '0';
		}
		dit = typeof dit !== 'undefined' ? dit : 0;
		num = num + ''
		const reg = /\d{1,3}(?=(\d{3})+$)/g
		let intNum = ''
		let decimalNum = ''
		if (num.indexOf('.') > -1) {
			if (num.indexOf(',') != -1) {
				num = num.replace(/\,/g, '');
			}
			num = parseFloat(num).toFixed(dit);
			// intNum = num.substring(0, num.indexOf('.'))
			// decimalNum = num.substring(num.indexOf('.') + 1, num.length)
			// return (intNum + '').replace(reg, '$&,') + '.' + decimalNum
			return (num + '').replace(reg, '$&,');
		} else {
			return (num + '').replace(reg, '$&,');
		}
	}

	// 去除千分位
	function reThousands(num) {
	    num+='';
		return num.replace(/,/g, '');
	}

	// 栏位显示千分位
	function getThousands(tdObj, value) {
		return $(tdObj).text(toThousands(value));
	}

	/**
	 * 获取审核记录信息
	 * @param recordId 数据id
	 * @param typeId 类型id
	 */
	var getStep = function (recordId,typeId) {
		$("#auditStatusGroup").find("input[type='radio']").eq(0).click();
		$("#auditContent").val("");
		$.myAjaxs({
			url:config.surl+"/audit/getStep",
			async:true,
			cache:false,
			type :"post",
			data :{
				recordId : recordId,
				typeId : typeId
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					if(result.data){
						// 设置审核记录ID
						$("#auditId").val(result.data);
						$("#audit_affirm").prop('disabled', false);
					}
				}else{
					if(result.data == 'Param'){
						prompt('Record fetch failed, Please try again!',5,"error"); // 没有查询到记录（参数为空）
						return ;
					}
					if(result.data == 'Null'){
						prompt('No Matching!',5,"error"); // 没有查询到记录（返回结果为空）
						return ;
					}
					if(result.data == 'Inconformity'){
						prompt('You do not have permission to process!',5,"error"); // 角色不符合
						return ;
					}
					prompt('Query failed, Please try again!',5,"error"); // 查询失败
					$("#audit_affirm").prop('disabled', true);
				}
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
			}
		});
	}

	/**
	 * 修改主档审核状态
	 * @param auditStepId 审核步骤id
	 * @param auditStatus 审核状态 1：通过  0：不通过
	 */
	var modifyRecordStatus = function (auditStepId,auditStatus) {
		$.myAjaxs({
				url:config.surl+"/audit/setRecordStatus",
				async:true,
				cache:false,
				type :"post",
				data :{
					auditStepId : auditStepId,
					auditStatus : auditStatus
				},
				dataType:"json",
				success:function(result){
					if(result.success){
					}else{
						prompt("Update record audit status value failed!",5,"error");
					}
				},
				error : function(e){
					prompt("The request failed, Please try again!",5,"error");// 请求失败
				}
		});
	}

	/**
	 * 发起审核
	 * @param 店铺cd 数据id
	 * @param recordCd 数据id
	 * @param typeId 类型id
	 * @param nReviewid 流程id
	 * @param toKen
	 * @param callback
	 */
	var initiateAudit = function (storeCd,recordCd,typeId,nReviewid,toKen,callback) {
		if(!storeCd){
			storeCd = "";
		}
		$.myAjaxs({
			url:config.surl+"/audit/saveAudit",
			async:true,
			cache:false,
			type :"put",
			data :{
				storeCd:storeCd,
				typeId:typeId,
				nReviewid:nReviewid,
				recordCd:recordCd,
				toKen:toKen
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					//回调
					callback(result.toKen);
				}else{
					prompt("Data saved successfully, But audit process submission failed, Please try again!",5,"error");// 请求失败
				}
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
			}
		});
	}
	/**
	 * 检查角色
	 * @param recordId 数据id
	 * @param typeId 类型id
	 * @param callback
	 */
var checkOnlyRole=function (recordId,typeId,callback) {
		$.myAjaxs({
			url:config.surl+"/audit/checkOnlyRole",
			async:true,
			cache:false,
			type :"post",
			data :{
				recordId : recordId,
				typeId : typeId
			},
			dataType:"json",
			success:function(result){
				$("#roleId").val(result.data);
				//回调
				callback(result.success);
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
				callback(false);
			}
		});
	}
	/**
	 * 检查审核
	 * @param recordId 数据id
	 * @param typeId 类型id
	 * @param callback
	 */
	var checkRole = function (recordId,typeId,callback) {
		$.myAjaxs({
			url:config.surl+"/audit/checkRole",
			async:true,
			cache:false,
			type :"post",
			data :{
				recordId : recordId,
				typeId : typeId
			},
			dataType:"json",
			success:function(result){
				$("#roleId").val(result.data);
				//回调
				callback(result.success);
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
				callback(false);
			}
		});
	}
	/**
	 * 判断审核是否完成
	 * @param
	 * @return
	 */
	var checkFinish=function (recordId, typeId,callback) {
		$.myAjaxs({
			url:config.surl+"/audit/checkFinish",
		cache:false,
			type :"post",
			data :{
				recordId : recordId,
				typeId : typeId
			},
			dataType:"json",
			success:function(result){
				//回调
				callback(result.success);
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
				callback(false);
			}
		})

	}
	 /**
	 * 获取审核状态
	 * @param recordId 数据id
	 * @param typeId 类型id
	  * @param reviewId 流程id
	 * @param callback
	 */
	var getRecordStatus = function (recordId,typeId,callback,reviewId) {
		if(!reviewId){
			reviewId = 0;
		}
		$.myAjaxs({
			url:config.surl+"/audit/getRecordStatus",
			async:true,
			cache:false,
			type :"get",
			data :{
				recordId : recordId,
				typeId : typeId,
				reviewId : reviewId
			},
			dataType:"json",
			success:function(result){
				//回调
				callback(result);
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
			}
		});
	}

	//box容器 展开收起事件挂载，适用box内 下拉按钮
	var boxScaling = function(){
		$(".box-tt div[class*='tt-icon']").click(function(){
			var obj = $(this);
			var isHasDropup = obj.hasClass("dropup");
			var uncle = obj.parent().next("div[class*='box-content']");
			if(isHasDropup){//收起
				uncle.slideUp(500,function(){
					obj.removeClass("dropup");
				});
			}else {//展开
				uncle.slideDown(500,function(){
					obj.addClass("dropup");
				});
			}
		});
	}
	//监听页面 button[class=date-clean] 对象 挂载时间
	var listenCleanDateBut = function(){
		$("button[class*='date-clean']").on("click",function(){
			var target = $(this).attr("target");
			cleanTargetVal(target);
		});
	}
	// 清空目标值
	var cleanTargetVal = function(tar){
		var ids = tar.split(",");
		for(var i=0;i<ids.length;i++){
			$("#"+ids[i]).val("");
		}
	}
	//隐藏所有提示信息
	var promptHideAll = function(){
		$.globalMessenger().hideAll();
	}
	//提示-弹出窗
	// area :文本内容
	//se :几秒关闭
	// type: "success","error","info",默认info
	// fun:回调函数
	// mask:遮罩是否开启，默认false,不开启
	var prompt = function(area,se,type,fun,mask){
		mask=mask||false;
		if(mask){
			openMask();
		}
		se= se || 5;//默认5秒自动关闭
		type= type ||"info";
		area+='<span style="color: #ff8008;float: right;">Close in '+se+'s</span>';
		$.globalMessenger().post({
			message: area,
			hideAfter: se,
			type:type,
			showCloseButton: true,
			hideCallback:function(){//关闭时的回调
				if(mask){
					closeMask();
				}
				var isfun = $.isFunction(fun) ? true : false;
				if(isfun){
					fun.call(this);
				}else{
					return null;
				}
			}
		});
	}
	// 确认是否
	var myConfirm = function(text,fun){
		//创建弹出结构
		var result = false;
		$("#myConfirm_text").html(text);
		$("#myConfirm").modal("show");
		myConfirmFunction=null;
		if($.isFunction(fun)){
			 myConfirmFunction = fun;
		  }
	}
	//初始化确认窗口
	var initMyConfirm = function(){
		$("#myConfirm a").on("click",function(){
			var result = $(this).attr("status");
			$("#myConfirm_status").val(result);
			$('#myConfirm').modal("hide");
		})
		$('#myConfirm').on('hidden.bs.modal', function (e) {
			  if($.isFunction(myConfirmFunction)){
				 var result =  $("#myConfirm_status").val();
				 myConfirmFunction.call(this,result);
			  }
		})
	}
	//加载提示框（带有遮罩效果）;isMask：默认存在遮罩
	var loading = function(isMask){
		$(".loading-mask-div").fadeIn(100,function(){
			$(".init-loading-box").fadeIn(200);
		 });
	}
	//关闭加载提示框
	var loading_close = function(){
		$(".init-loading-box").fadeOut(300,function(){
			$(".loading-mask-div").fadeOut(100);
		 });
	}
	// 只开启 遮罩层
	var openMask = function(){
		$(".loading-mask-div").fadeIn(100);
	}
	// 只关闭 遮罩层
	var closeMask = function(){
		$(".loading-mask-div").fadeOut(100);
	}
	//非法字符为空处理,将 null、undefined、NAN等非正常数据转换为空，如果不是则进行正常返回
	var isIllegalVal = function(text){
		if(text==null||text==""||typeof(text)=="undefined"){
			return "";
		}else{
			return text;
		}
	}
	// 对Date的扩展，将 Date 转化为指定格式的String
	// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
	// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	// 例子：
	// dateFormat(new Date(),"yyyy/MM/dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
	// dateFormat(new Date(),"yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
	var dateFormat = function (date,fmt) { //author: meizz
		var o = {
			"M+": date.getMonth() + 1, //月份
			"d+": date.getDate(), //日
			"h+": date.getHours(), //小时
			"m+": date.getMinutes(), //分
			"s+": date.getSeconds(), //秒
			"q+": Math.floor((date.getMonth() + 3) / 3), //季度
			"S": date.getMilliseconds() //毫秒
		};
		if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	}

	//四舍五入(只进行四舍五入)
	//text：数值,p：位数
	var round = function(text,p){
		var iVal = isIllegalVal(text);
		iVal = iVal.replace(/,/g,"");
		if(iVal!=""){
			p = p || 2;//默认2位
			var ro = Number(iVal).toFixed(p);
			return ro;
		}else{
			return "";
		}
	}
	// 更精确的四舍五入 保留小数位,num=数值，v 小数位
	var decimal = function(num,v){
		var vv = Math.pow(10,v);
		return Math.round(num*vv)/vv;
	}
	//取整
	//text：数值,
	var floor = function(text){
		var iVal = isIllegalVal(text);
		iVal = iVal.replace(/,/g,"");
		if(iVal!=""){
			var ro = Math.floor(iVal);
			return Number(ro);
		}else{
			return "";
		}
	}
	//数值格式化（千分位等）
	//flg:r四舍五入,f：取整不入:n:不进行舍位
	var fmtAmount = function(flg,money,p) {
		return fmoney(money,p);
	}
	/**
	 * 金额格式化
	 * s:金額
	 * n：保留小数位数
	 */
	var fmoney = function(s, n) {
	   n = n > 0 && n <= 20 ? n : 2;
	   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	   var l = s.split(".")[0].split("").reverse(),
	   r = s.split(".")[1];
	   t = "";
	   for(i = 0; i < l.length; i ++ )
	   {
		  t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	   }
	   return t.split("").reverse().join("") + "." + r;
	}

	//文本格式化
	var textFmt = function(text){
		text = text.replace(/\n/g,"<br>")
					.replace(/ /g,"&nbsp;")
					.replace(/\r\n/g,"<br>")
					.replace(/\r/g,"<br>");
		return text;
	}
	//清除文本格式化（将textFmt方法，格式化后的文本进行拆解，剔除无用标记）
	var clearTextFmt = function(text){
		text = text.replace(/<br>/g,"\n")
					.replace(/&nbsp;/g," ")
					.replace(/<br>/g,"\r\n")
					.replace(/<br>/g,"\r");
		return text;
	}
	// ajax Completeh 回调函数,因为内容提示需要用到封装好的prompt式样，所有将ajax.Complete方法移到外部实现，其他业务使用myAjax的必须要回调此函数。
	var myAjaxComplete = function(xhr,status){
		var header = xhr.getResponseHeader("content-type");
		if(header==null||header==""){
			prompt("Abnormal operation, request header resolution failed, please contact administrator",5);
		}
		if(header==null){
			header="";
		}
		header = header.split(";");
		if(header[0]=="text/html"){
			prompt("You have logged out of the system, please login again",5,"error",function(){
				location.href = config.surl+"/login?errMsg=You have logged out of the system, please login again";
			},true);
		}else if(header[0]=="text/json"){
			if(xhr.responseJSON.result==false){
				prompt(xhr.responseJSON.errorMessage,10);
			}
		}
	}

	var FloatAdd = function(arg1, arg2) {
		var r1, r2, m, n;
		try {
			r1 = arg1.toString().split(".")[1].length;
		} catch (e) {
			r1 = 0;
		}
		try {
			r2 = arg2.toString().split(".")[1].length;
		} catch (e) {
			r2 = 0;
		}
		n = Math.max(r1, r2);
		m = Math.pow(10, n);
		return Number(((arg1 * m + arg2 * m) / m).toFixed(n));
	}

	var FloatSub = function(arg1, arg2) {
		var r1, r2, m, n;
		try {
			r1 = arg1.toString().split(".")[1].length;
		} catch (e) {
			r1 = 0;
		}
		try {
			r2 = arg2.toString().split(".")[1].length;
		} catch (e) {
			r2 = 0;
		}
		m = Math.pow(10, Math.max(r1, r2));
		n = (r1 >= r2) ? r1 : r2;
		return ((arg1 * m - arg2 * m) / m).toFixed(n);
	}

	var FloatMul = function(arg1, arg2) {
		var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
		try {
			m += s1.split(".")[1].length;
		} catch (e) {
		}
		try {
			m += s2.split(".")[1].length;
		} catch (e) {
		}
		return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
	}

	var FloatDiv = function(arg1, arg2) {
		var t1 = 0, t2 = 0, r1, r2;
		try {
			t1 = arg1.toString().split(".")[1].length;
		} catch (e) {
		}
		try {
			t2 = arg2.toString().split(".")[1].length;
		} catch (e) {
		}
		with (Math) {
			r1 = Number(arg1.toString().replace(".", ""));
			r2 = Number(arg2.toString().replace(".", ""));
			return (r1 / r2) * pow(10, t2 - t1);
		}
	}

	var clearElement =function(){
		$("input").val("");
	}

	//将对象转成ajax提交用的url 参数
	/*
	 * var a = new Object();对象
	 * a.a=1;
	 * a.b="03";
	 *
	 * var ls = =  new Array();集合
	 * 			var l = new Object();集合内对象
	 * 				l.x=?
	 * 		ls.push(l);对象载入集合
	 * 	a.c=ls;将集合载入对象
	 *
	 * parseParam(a); 对象转换为url
	 */
	var parseParam=function(param, key){
		var paramStr="";
		if(param instanceof String||param instanceof Number||param instanceof Boolean){
			paramStr+="&"+key+"="+encodeURIComponent(param);
		}else{
			$.each(param,function(i){
				var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
				console.log(k);
				//paramStr+='&'+parseParam(this, k);
			});
		}
		return paramStr.substr(1);
	};

	//回车换行与eachFocus联动使用
	var nextElement = function(){
		$("[nextele]").on("keydown",function(event){
			event.stopPropagation()
			if(event.keyCode==13){
				var nextObj = $(this).attr("nextele");
				eachFocus($("#"+nextObj));
			}
		})
	}
	//焦点寻找,与nextElement联动使用
	var eachFocus = function(thisObj){
		if(thisObj.is(":hidden")||thisObj.is(":disabled")){
			var nextObj = thisObj.attr("nextele");
			eachFocus($("#"+nextObj));
		}else{
			var type = thisObj[0].type.toLowerCase();
			switch(type) {
			 case "radio":
				 //有缺陷，后期研究
				 thisObj.click().focus();
				break;
			 default:
				 thisObj.focus();
			}

		}
	}

	var setToArray = function(set){
		var arr=new Array();
		set.forEach(function (element, sameElement, setObj) {
			arr.push(element)
		});
		return arr;
	}

	// 清空表格
	var clearTable = function(){
		$(".zgrid-table>.zgGrid-tbody tr").empty();
		$(".zgGrid-pages-box #records").text("0");
		$(".zgGrid-pages-box #page_sum").text("1");
		$(".zgGrid-pages-box .zg-page-search").val("1");
	}

	// 根据key 获取参数值
	var getValByKey = function(key){
		let r = '';
		$.myAjaxs({
			url:config.surl+"/cm9060/getValByKey",
			async:false,
			cache:false,
			type :"post",
			data : "key="+key,
			dataType:"json",
			success : function (result) {
				r = result
			},
		});
		return r;
	}

	// 修改缓存中的状态(设置为 1 表示 从back 按钮返回的,需要回填用户录入的参数)
	var updateBackFlg = function(key) {
		let searchJson=sessionStorage.getItem(key);
		if (searchJson) {
			let obj = eval("("+searchJson+")");
			obj.flg='1';
			// 改变flg 状态
			sessionStorage.setItem(key,JSON.stringify(obj));
		}
	};

	// 回显 组织架构 值
	var setAutomaticVal = function(obj) {
		if (obj.regionCd) {
			$.myAutomatic.setValueTemp(a_region,obj.regionCd,obj.regionName);
			$("#aCity").attr("disabled", false);
			$("#cityRefresh").show();
			$("#cityRemove").show();
			// 替换子级查询参数
			let str = "&regionCd=" + obj.regionCd;
			$.myAutomatic.replaceParam(a_city, str);
		}
		if (obj.cityCd) {
			$.myAutomatic.setValueTemp(a_city,obj.cityCd,obj.cityName);
			$("#aDistrict").attr("disabled", false);
			$("#districtRefresh").show();
			$("#districtRemove").show();
			// 替换子级查询参数
			let str = "&regionCd=" + obj.regionCd + "&cityCd=" + obj.cityCd;
			$.myAutomatic.replaceParam(a_district, str);
		}
		if (obj.districtCd) {
			$.myAutomatic.setValueTemp(a_district,obj.districtCd,obj.districtName);
			$("#aStore").attr("disabled", false);
			$("#storeRefresh").show();
			$("#storeRemove").show();
			// 替换子级查询参数
			var str = "&regionCd=" + obj.regionCd + "&cityCd=" + obj.cityCd + "&districtCd=" + obj.districtCd;
			$.myAutomatic.replaceParam(a_store, str);
		}
		if (obj.storeCd) {
			$.myAutomatic.setValueTemp(a_store,obj.storeCd,obj.storeName);
		}
	}

	// 回显 大中小分类 值
	var setCategoryAutomaticVal = function(obj) {
		if (obj.depCd) {
			$.myAutomatic.setValueTemp(a_dep,obj.depCd,obj.depName);
			$("#pma").attr("disabled", false);
			$("#pmaRefresh").show();
			$("#pmaRemove").show();
			// 替换子级查询参数
			let str = "&depId=" + obj.depCd;
			$.myAutomatic.replaceParam(a_pma, str);
		}
		if (obj.pmaCd) {
			$.myAutomatic.setValueTemp(a_pma,obj.pmaCd,obj.pmaName);
			$("#category").attr("disabled", false);
			$("#categoryRefresh").show();
			$("#categoryRemove").show();
			// 替换子级查询参数
			let str = "&depId=" + obj.depCd + "&pmaId=" + obj.pmaCd;
			$.myAutomatic.replaceParam(a_category, str);
		}
		if (obj.categoryCd) {
			$.myAutomatic.setValueTemp(a_category,obj.categoryCd,obj.categoryName);
			$("#subCategory").attr("disabled", false);
			$("#subCategoryRefresh").show();
			$("#subCategoryRemove").show();
			// 替换子级查询参数
			var str = "&depId=" + obj.depCd + "&pmaId=" + obj.pmaCd + "&categoryId=" + obj.categoryCd;
			$.myAutomatic.replaceParam(a_subCategory, str);
		}
		if (obj.subCategoryCd) {
			$.myAutomatic.setValueTemp(a_subCategory,obj.subCategoryCd,obj.subCategoryName);
		}
	}

	var forCreateTime=function (tdObj, str) {
		let hour = str.substring(0,2);
		let minute = str.substring(2,4);
		let second = str.substring(4,6);
		let date = +hour+":"+minute+":"+second
		return(tdObj).text(date);

	}
	var forCreateNewDate=function (tdObj,str) {
		if (str.length!=8) {
			return '';
		}
		let  day = str.substring(0,2);
		let  month  = str.substring(2,4);
		let year= str.substring(4,8);
		let date = day+"/"+month+"/"+year;
		return $(tdObj).text(date);
	};
	var forCreateDate=function (tdObj,str) {
		if (str.length!=8) {
			return '';
		}
		let year = str.substring(0,4);
		let month = str.substring(4,6);
		let day = str.substring(6,8);
		let date = day+"/"+month+"/"+year
		return $(tdObj).text(date);
	};
	var formatCreateDate = function (str) {
		if (str.length!=14) {
			return '';
		}
		let year = str.substring(0,4);
		let month = str.substring(4,6);
		let day = str.substring(6,8);
		let hour = str.substring(8,10);
		let minute = str.substring(10,12);
		let second = str.substring(12,14);
		let date = day+"/"+month+"/"+year+" "+hour+":"+minute+":"+second;
		return date;
	}

	// 20200716104908 --> 16/07/2020 10:49:08
	var formatDateAndTime = function (tdObj, str) {
		if (str.length!=14) {
			return '';
		}
		return $(tdObj).text(formatCreateDate(str));
	}

	var initDate = function(startDate,endDate) {
		if (startDate) {
			startDate.datetimepicker({
				language:'en',
				format: 'dd/mm/yyyy',
				maxView: 4,
				startView: 2,
				minView: 2,
				autoclose: true,
				todayHighlight: true,
				todayBtn: true,
			}).on('changeDate', function (ev) {
				if (endDate) {
					if (ev.date) {
						endDate.datetimepicker('setStartDate', new Date(ev.date.valueOf()))
					} else {
						endDate.datetimepicker('setStartDate', null);
					}
				}
			});
			// 倒推两个月
			let _start = new Date(new Date().setMonth(new Date().getMonth()-2));
			if (!endDate) {
				_start = new Date();
			}
			startDate.val(_start.Format('dd/MM/yyyy'));
		}

		if (endDate) {
			endDate.datetimepicker({
				language:'en',
				format: 'dd/mm/yyyy',
				maxView: 4,
				startView: 2,
				minView: 2,
				autoclose: true,
				todayHighlight: true,
				todayBtn: true,
			}).on('changeDate', function (ev) {
				if (startDate) {
					if (ev.date) {
						startDate.datetimepicker('setEndDate', new Date(ev.date.valueOf()))
					} else {
						startDate.datetimepicker('setEndDate', null);
					}
				}
			});
			// 默认日期当天
			endDate.val(new Date().Format('dd/MM/yyyy'));
		}
	}

	self.setToArray = setToArray;// 将set对象内容遍历成数组
	self.decimal = decimal;// 回车换下一个元素
	self.nextElement = nextElement;// 回车换下一个元素
	self.parseParam = parseParam;// 将对象转成ajax提交用的url 参数
	self.clearElement = clearElement;// 清空全部元素内容
	self.myAjaxComplete = myAjaxComplete;//初始化确认框(系统加载)
	self.initMyConfirm = initMyConfirm;//初始化确认框(系统加载)
	self.myConfirm = myConfirm;//确认框提示
	self.textFmt = textFmt;//文本格式化
	self.clearTextFmt = clearTextFmt;//清除文本格式化
	self.fmtAmount = fmtAmount;//格式化（金额）
	self.fmoney = fmoney;//格式化（金额）
	self.floor = floor;//取整
	self.round = round;//四舍五入
	self.dateFormat = dateFormat;//日期格式化
	self.isIllegalVal = isIllegalVal;//非法值处理
	self.loading_close = loading_close;//关闭等待窗口
	self.loading = loading;//等待框开启
	self.config = config;//配置变量
	self.boxScaling = boxScaling;//box容器 展开收起
	self.prompt = prompt;//提示-弹出窗
	self.promptHideAll = promptHideAll;//隐藏所有提示信息
	self.cleanTargetVal = cleanTargetVal;//清空目标对象的val值
	self.listenCleanDateBut = listenCleanDateBut;//监听时间清空按钮事件
	self.FloatAdd = FloatAdd;//计算求和
	self.FloatSub = FloatSub;//计算求差
	self.FloatMul = FloatMul;//计算求积
	self.FloatDiv = FloatDiv;//计算求商
	self.getAuditFlowChart = getAuditFlowChart;//获取审核流程图
	self.getStep = getStep; //获取审核记录信息
	self.modifyRecordStatus = modifyRecordStatus;//更新主档审核状态值
	self.initiateAudit = initiateAudit;//发起审核
	self.checkFinish=checkFinish;//检查审核是否完成
	self.checkRole = checkRole;//检查是否允许审核
	self.checkOnlyRole=checkOnlyRole;//只检查角色
	self.getRecordStatus = getRecordStatus;//获取审核状态
	self.getThousands = getThousands; // 栏位显示千分位
	self.reThousands = reThousands; // 去除千分位
	self.toThousands = toThousands; // 设置千分位
	self.initOrganization = initOrganization; // 初始化店铺运营组织检索
	self.initStoreform = initStoreform; // 初始化店铺下拉
	self.initCategoryAutoMatic = initCategoryAutoMatic; // 初始化大中小分类
	self.timeCheck = timeCheck; // 验证当前时间订货是否可操作
	self.batchCheck = batchCheck; // 验证当前时间是否batch处理
	self.checkBusinessDate = checkBusinessDate; // 验证日期是否为业务日
	self.loadPaging = loadPaging; // 加载分页条数据
	self.resetPaging = resetPaging; // 清除分页条数据
	self.clearTable = clearTable; // 清空表格
	self.getInform = getInform;//获取通知信息
	self.getValByKey = getValByKey;//根据key 获取参数值
	self.updateBackFlg = updateBackFlg;//修改缓存中的状态
	self.setAutomaticVal = setAutomaticVal;//设置组织架构回显
	self.setCategoryAutomaticVal = setCategoryAutomaticVal;//设置大中小分类回显
	self.formatDateAndTime = formatDateAndTime;//格式话日期和时间
	self.formatCreateDate = formatCreateDate;//格式话日期和时间
	self.forCreateDate = forCreateDate;//格式话日期和时间
	self.forCreateNewDate =forCreateNewDate;//格式话日期和时间
    self.forCreateTime=forCreateTime;

	self.initDate = initDate;
	return self;
});
