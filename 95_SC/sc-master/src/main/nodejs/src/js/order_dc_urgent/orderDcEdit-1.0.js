require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('orderDcEdit', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    tempTrObjValue = {},//临时行数据存储
		a_region = null,
		a_city = null,
		a_district = null,
		a_store = null,
		a_item = null,
		a_dc = null,
		submitFlag=false,
    	common=null;
	const KEY = 'ORDER_QUERY_TO_DC';
    var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson:null,
		// 查询部分
        search: null,
        reset: null,
		order_id:null,
		od_date:null,
		businessDate:null,
		store:null,
		search_store_input:null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		orderQty:null,
		storeName:null,
		item_refresh:null,
		item_clear:null,
		dc_refresh:null,
		dc_clear:null,
		vendorId:null,
		vendorName:null,
		orderId:null,
		reviewSts:null,
		orderType:null,
		order_sts:null,
		order_type:null,
		auto_import_item:null,//自动导入商品按钮
		vendor:null,
		minOrderQty:null,//最低订货数量
		orderRemark:null,//备注
		ca_date:null,//提交时间
		ca_user:null,//提交用户
		md_date:null,//修改时间
		md_user:null,//修改用户
		ap_name:null,
		ap_date:null,
		returnsViewBut:null,
		// 输入部分
		item_input_barcode:null,//商品barcode
		item_input_name:null,//名称
		item_input_uom:null,//单位
		item_input_uomCd:null,//单位cd
		item_input_spec:null,//规格
		item_input_converter:null,
		item_input_orderPrice:null,//订货价格
		item_input_orderBatchQty:null,//订货批量
		item_input_salePrice:null,//销售价格
		item_input_minOrderQty:null,//最低订货量
		item_input_minDisplayQty:null,//最低陈列量
		item_input_psd:null,//过去平均4周销售量
		item_input_orderTotalAmt:null,//单个商品订货金额
		item_input_autoOrderQty:null,//推荐订货量
		item_input_orderNochargeQty:null,//搭赠量
		item_input_orderTotalQty:null,//总数量
		inventoryQty:null,//实时库存数量
		item_input_vendorId:null,
		purchaseVatCd:null,//税区分
		taxRate:null,//税名称
		returnsSubmitBut:null,//提交按钮
		ietm_clear:null,
		cancel:null,
		affirm:null,
		//审核
		approvalBut:null,
		approval_dialog:null,
		audit_cancel:null,
		audit_affirm:null,
		typeId:null,
		reviewId:null
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left=_common.config.surl+"/urgentOrderDc";
		systemPath = _common.config.surl;
		getThousands=_common.getThousands;
		toThousands=_common.toThousands;
		reThousands=_common.reThousands;
    	//初始化 按钮事件
		but_event();
		//加载自动下拉
		initAutoMatic();
    	//加载下拉
		getSelectValue();
		//列表初始化
		initTable1();
		//表格内按钮事件
		table_event();
    	//根据状态设定不同画面的样式
		initPageByType(m.use.val());
		//隐藏搭赠量 总订货量
		m.item_input_orderNochargeQty.parent().parent().hide();
		//审核事件
		approval_event();
    }

	function selbox(a){
		var val = a.val();
		a.attr("title",val);
	}

    var table_event = function(){
		$("#orderQty").blur(function () {
			$("#orderQty").val(toThousands(this.value));
			//计算单商品金额
			var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
			var orderQty = reThousands($(this).val().trim());
			var orderPrice = reThousands(m.item_input_orderPrice.val());
			var orderNochargeQty = reThousands(m.item_input_orderNochargeQty.val());
			if(reg.test(orderQty)){
				if(!!orderPrice){
					var orderTotalAmt = parseFloat(orderPrice)*parseFloat(orderQty);
					m.item_input_orderTotalAmt.val(orderTotalAmt);
				}
				//计算合计数量
				if(!!orderNochargeQty){
					var orderTotalQty = Number(orderNochargeQty)+Number(orderQty);
					m.item_input_orderTotalQty.val(toThousands(orderTotalQty));
				}
			}else{
				m.item_input_orderTotalAmt.val(0)
				m.item_input_orderTotalQty.val(0);
			}
		});

		//计算金额
		$("#orderQty").on("change",function(){
			var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
			var orderQty = reThousands($(this).val().trim());
			var orderPrice = reThousands(m.item_input_orderPrice.val());
			var orderNochargeQty = reThousands(m.item_input_orderNochargeQty.val());
			if(reg.test(orderQty)){
				if(!!orderPrice){
					var orderTotalAmt = parseFloat(orderPrice)*parseFloat(orderQty);
					m.item_input_orderTotalAmt.val(orderTotalAmt);
				}
				//计算合计数量
				if(!!orderNochargeQty){
					var orderTotalQty = Number(orderNochargeQty)+Number(orderQty);
					m.item_input_orderTotalQty.val(toThousands(orderTotalQty));
				}
			}else{
				m.item_input_orderTotalAmt.val(0)
				m.item_input_orderTotalQty.val(0);
			}
		})

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#orderQty").focus(function(){
			$("#orderQty").val(reThousands(this.value));
		});

		// $("#item_input_orderNochargeQty").blur(function () {
		// 	$("#item_input_orderNochargeQty").val(toThousands(this.value));
		// });
        //
		// //光标进入，去除金额千分位，并去除小数后面多余的0
		// $("#item_input_orderNochargeQty").focus(function(){
		// 	$("#item_input_orderNochargeQty").val(reThousands(this.value));
		// });

		$("#addOrderDetails").on("click", function () {
			if(verifySearch()){
				itemClear();
				$('#update_dialog').attr("flg","add");
				//启用用窗口信息
				m.orderQty.attr("disabled",false);
				m.affirm.attr("disabled",false);
				$("#a_item").removeAttr("disabled");
				m.item_clear.show();
				m.item_refresh.show();
				m.item_input_orderNochargeQty.val(0);
				m.item_input_orderTotalQty.val(0);
				$('#update_dialog').modal("show");
			}
		});

		$("#updateOrderDetails").on("click",function(){
			if(verifySearch()){
				if(selectTrTemp==null){
					_common.prompt("Please select at least one row of data!",5,"error");
					return false;
				}
				itemClear();
				$('#update_dialog').attr("flg","update");
				//设置窗口商品信息
				setDialogValue();
				//启用用窗口信息
				m.orderQty.attr("disabled",false);
				m.affirm.attr("disabled",false);
				$("#a_item").removeAttr("disabled");
				m.item_clear.show();
				m.item_refresh.show();

				$('#update_dialog').modal("show");
			}
		});

		$("#viewOrderDetails").on("click",function(){
			if(verifySearch()){
				if(selectTrTemp==null){
					_common.prompt("Please select at least one row of data!",5,"error");
					return false;
				}
				itemClear();
				$('#update_dialog').attr("flg","view");
				//设置商品信息
				setDialogValue();
				//禁用窗口信息
				m.orderQty.attr("disabled",true);
				m.affirm.attr("disabled",true);
				$("#a_item").attr("disabled","disabled");
				m.item_clear.hide();
				m.item_refresh.hide();
				$('#update_dialog').modal("show");
			}
		});

		m.auto_import_item.on("click",function(){
			var storeCd = $("#aStore").attr("k");
			var dcId = $("#delivery_center_id").val();
			if(storeCd==null||storeCd==''){
				_common.prompt("Please select a store first!",3,"info");
				return false;
			}
			if(dcId==null||dcId==''){
				_common.prompt("DC can not be empty!",3,"info");
				return false;
			}
			_common.myConfirm("DC cannot be changed after click 'Confirm', are you sure to confirm?",function(result) {
				if (result == "true"&&verifySearch()) {
					paramGrid = "orderDate=" + subfmtDate(m.od_date.val())+
						"&storeCd="+$("#aStore").attr("k")+
						"&dcId="+dcId;
					tableGrid.setting("url", url_left + "/getItemsByDcAuto");
					tableGrid.setting("param", paramGrid);
					tableGrid.loadData(null);
					m.auto_import_item.attr("disabled","disabled").css("pointer-events","none");
					setStoreInputDisable();
				}
			})
		});

		//删除数据
		$("#deleteOrderDetails").on("click",function(){
			var _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length == 0){
				if(selectTrTemp==null){
					_common.prompt("Please select at least one row of data!",5,"error");
					return false;
				}else{
					_common.myConfirm("Please confirm whether you want to delete the selected item？",function(result) {
						if (result == "true") {
                            /*$.myAjaxs({
                                url:url_left+"/getFreeItemInfo",
                                async:true,
                                cache:false,
                                type :"get",
                                data :{
                                    articleId:$(selectTrTemp).find('td[tag=articleId]').text(),
                                    storeCd:$("#aStore").attr("k"),
                                    orderDate:subfmtDate(m.od_date.val()),
                                    vendorId:$(selectTrTemp).find('td[tag=vendorId]').text(),
                                    orderQty:0,
                                    oldOrderQty:reThousands($(selectTrTemp).find('td[tag=orderQty]').text()),
                                    orderFlg:"2" //修改
                                },
                                dataType:"json",
                                success:function(result){
                                    //减少搭赠商品
                                    if(result.success&&result.data.length>0){
                                        result.data.forEach(function(item, i){
                                            var itemAppendFlg = true;
                                            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                                                var _articleId = $(this).find('td[tag=articleId]').text();
                                                if(_articleId==item.articleId){//商品已存在 修改搭赠量
                                                    let _orderQty = $(this).find('td[tag=orderQty]').text();
                                                    let _orderNochargeQty = $(this).find('td[tag=orderNochargeQty]').text();
                                                    let newOrderNochargeQty = toThousands(Number(reThousands(_orderNochargeQty))+item.orderNochargeQty);
                                                    $(this).find('td[tag=orderNochargeQty]').text(newOrderNochargeQty);
                                                    //搭赠量 订货量 为0 删除搭赠商品
                                                    if(_orderQty=="0"&&newOrderNochargeQty=="0"){
                                                        $(this).remove();
                                                    }
                                                    itemAppendFlg = false;
                                                }
                                            });
                                        });
                                    }
                                },
                                complete:_common.myAjaxComplete
                            });*/
							$(selectTrTemp).remove();
							selectTrTemp = null;
							//计算合计
							gridTotal();
							_common.prompt("Data deleted successfully！",2,"success"); // 商品删除成功
							//更新总条数(注减一是去除标题行)
							var total = tableGrid.find("tr").length-1;
							$("#records").text(total);
						}
					})
				}
			}else{
				_common.myConfirm("Please confirm whether you want to delete the selected items？",function(result) {
					if (result == "true") {
						for(var i = 0; i < _list.length; i++){
                            var orderNochargeQty = reThousands($(_list[i]).find('td[tag=orderNochargeQty]').text());
                            if(orderNochargeQty!=0){//跳过有搭赠量的商品
                                continue;
                            }
                            /*$.myAjaxs({
                                url:url_left+"/getFreeItemInfo",
                                async:false,
                                cache:false,
                                type :"get",
                                data :{
                                    articleId:$(_list[i]).find('td[tag=articleId]').text(),
                                    storeCd:$("#aStore").attr("k"),
                                    orderDate:subfmtDate(m.od_date.val()),
                                    vendorId:$(_list[i]).find('td[tag=vendorId]').text(),
                                    orderQty:0,
                                    oldOrderQty:reThousands($(_list[i]).find('td[tag=orderQty]').text()),
                                    orderFlg:"2" //修改
                                },
                                dataType:"json",
                                success:function(result){
                                    //减少搭赠商品
                                    if(result.success&&result.data.length>0){
                                        result.data.forEach(function(item, i){
                                            var itemAppendFlg = true;
                                            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                                                var _articleId = $(this).find('td[tag=articleId]').text();
                                                if(_articleId==item.articleId){//商品已存在 修改搭赠量
                                                    let _orderQty = $(this).find('td[tag=orderQty]').text();
                                                    let _orderNochargeQty = $(this).find('td[tag=orderNochargeQty]').text();
                                                    let newOrderNochargeQty = toThousands(Number(reThousands(_orderNochargeQty))+item.orderNochargeQty);
                                                    $(this).find('td[tag=orderNochargeQty]').text(newOrderNochargeQty);
                                                    //搭赠量 订货量 为0 删除搭赠商品
                                                    if(_orderQty=="0"&&newOrderNochargeQty=="0"){
                                                        $(this).remove();
                                                    }
                                                    itemAppendFlg = false;
                                                }
                                            });
                                        });
                                    };
                                },
                                complete:_common.myAjaxComplete
                            });*/
							$(_list[i]).remove();
						}
                        selectTrTemp = null;
                        //计算合计
                        gridTotal();
                        _common.prompt("Data deleted successfully！",2,"success"); // 商品删除成功
                        //更新总条数(注减一是去除标题行)
                        var total = tableGrid.find("tr").length-1;
                        $("#records").text(total);
					}
				})
			}
		})

		//返回一览
		m.returnsViewBut.on("click",function(){
			let back = $("#returnsSubmitBut").attr("disabled");
			let flg = m.use.val();
			if(back!='disabled'&&flg!='0'){
				_common.myConfirm("Crrent change is not submitted yet,are you sure to exit?",function(result) {
					if (result == "true") {
						_common.updateBackFlg(KEY);
						top.location = systemPath + "/cdOrder";
					}
				})
			}else{
				_common.updateBackFlg(KEY);
				top.location = systemPath + "/cdOrder";
			}
		});
		//提交按钮点击事件
		m.affirm.on("click",function(){
			var articleId = $("#a_item").attr("k");
			if(articleId==null||articleId==""){
				$("#a_item").css("border-color","red");
				_common.prompt("Please select an item!",5,"info"); // 请选择商品
				$("#a_item").focus();
				return false;
			}else{
				$("#a_item").css("border-color","#CCCCCC");
			}
			/*if($("#zgGridTtable>.zgGrid-tbody tr").length>0){
				let _taxRate = $("#zgGridTtable>.zgGrid-tbody tr:eq(0)").find('td[tag=taxRate]').text();
				if(m.taxRate.val()!=_taxRate){
					//该商品税率不同，请重新选择商品
					_common.prompt("You can only add items with same VAT rate for one PO, please select item again!",3,"info");
					return false;
				}
			}*/
			var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
			var orderQty = reThousands(m.orderQty.val().trim());
			var minOrderQty = reThousands(m.item_input_minOrderQty.val());
			var orderBatchQty = reThousands(m.item_input_orderBatchQty.val());
			if(!reg.test(orderQty)){
				$("#orderQty").css("border-color","red");
				_common.prompt("Please enter with correct data type!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}else{
				$("#orderQty").css("border-color","#CCCCCC");
			}
			var _orderQty = parseFloat(orderQty),
				_minOrderQty = parseFloat(minOrderQty),
				_orderBatchQty = parseFloat(orderBatchQty);
			if(_orderQty==0){//订货量不为空
				$("#orderQty").css("border-color","red");
				_common.prompt("Order Qty can not be 0!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}else{
				$("#orderQty").css("border-color","#CCCCCC");
			}
			if(_orderQty>999999){//不得大于最高订货量
				_common.prompt("Order Qty can not be greater than 999999!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}
			if(!isNaN(_minOrderQty)&&_orderQty<_minOrderQty){//不得低于最低订货量
				$("#orderQty").css("border-color","red");
				_common.prompt("Order Qty can not be less than DC MOQ!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}else{
				$("#orderQty").css("border-color","#CCCCCC");
			}
			if(isNaN(_minOrderQty)){
				_minOrderQty = 0;
			}
			if(isNaN(_orderBatchQty)) {//必须为正确的订货批量
				// _orderBatchQty = 0;
				_common.prompt("The Incremental Quantity error!",3,"error");
				return false;
			}
			if(_orderBatchQty != 0){
				var remainder = (_orderQty-_minOrderQty)%_orderBatchQty;
				if(remainder!=0){
					$("#orderQty").css("border-color","red");
					_common.prompt("Order Qty need to be multiples of Incremental Quantity!",3,"info");
					$('#i_orderQty').focus();
					return false;
				}else{
					$("#orderQty").css("border-color","#CCCCCC");
				}
			}

			var orderNochargeQty = reThousands(m.item_input_orderNochargeQty.val());
			if(orderNochargeQty!=null&&orderNochargeQty!=''){
				if(!reg.test(orderNochargeQty)){
					$("#item_input_orderNochargeQty").css("border-color","red");
					_common.prompt("Please enter the correct Free Order Quantity format!",5,"info"); // 请输入正确格式搭赠量
					m.item_input_orderNochargeQty.focus();
					return false;
				}else if(orderNochargeQty>_orderQty){
					$("#item_input_orderNochargeQty").css("border-color","red");
					_common.prompt("Free Order Quantity cannot be greater than Order Qty!",5,"info"); // 搭赠量不能大于订货量
					m.item_input_orderNochargeQty.focus();
					return false;
				}else{
					$("#item_input_orderNochargeQty").css("border-color","#CCC");
				}
			}else{
				//设置搭赠量默认为0
				m.item_input_orderNochargeQty.val("0");
				$("#item_input_orderNochargeQty").css("border-color","#CCCCCC");
			}

			var  flg = $('#update_dialog').attr("flg");
			var appendFlg = true;//检查商品是否重复
			if(flg=='add') {
				$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
					let _articleId = $(this).find('td[tag=articleId]').text();
					if (_articleId == $("#a_item").attr("k")) {
						appendFlg = false;
					}
				});
			}else if(flg =='update'){
				if (selectTrTemp==null){
					$("#a_item").css("border-color","red");
					_common.prompt("Please select an item!",5,"info"); // 选择的商品为空
					return false;
				}else{
					var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
					$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
						var _articleId = $(this).find('td[tag=articleId]').text();
						if(articleId!=cols["articleId"]){//选择商品id不等于新录入商品id
							if(_articleId==articleId) {//商品已存在
								appendFlg = false;
								return false;
							}
						}
					});
					$("#a_item").css("border-color","#CCC");
				}
			}
			if(!appendFlg){
				$("#a_item").css("border-color","red");
				_common.prompt("Selected item already exists in order list,please select a new item!", 5, "info"); // 该商品已经存在
				return false;
			}else{
				$("#a_item").css("border-color","#CCCCCC");
			}
			_common.myConfirm("Are you sure to Submit?",function(result){
				if(result=="true"){
					var rowindex = 0;
					var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
					if(trId!=null&&trId!=''){
						rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
					}
					if(flg=='add'){
						var tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
							'<td tag="barcode" width="130" title="'+m.item_input_barcode.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+m.item_input_barcode.val()+'</td>' +
							'<td tag="articleId" width="130" title="'+articleId+'" align="right" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+articleId+'</td>' +
							'<td tag="articleName" width="180" title="'+m.item_input_name.val()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+m.item_input_name.val()+'</td>' +
							'<td tag="unitId" hidden width="130" title="'+m.item_input_uomCd.val()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_unitId" tdindex="zgGridTtable_unitId">'+m.item_input_uomCd.val()+'</td>' +
							'<td tag="unitName" width="130" title="'+m.item_input_uom.val()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_unitName" tdindex="zgGridTtable_unitName">'+m.item_input_uom.val()+'</td>' +
							'<td tag="spec" width="130" title="'+m.item_input_spec.val()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_spec" tdindex="zgGridTtable_spec">'+m.item_input_spec.val()+'</td>' +
							'<td tag="converter" hidden width="130" title="'+m.item_input_converter.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_converter" tdindex="zgGridTtable_converter">'+m.item_input_converter.val()+'</td>' +
							'<td tag="purchaseVatCd" hidden width="80" title="'+m.purchaseVatCd.val()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_purchaseVatCd" tdindex="zgGridTtable_purchaseVatCd">'+m.purchaseVatCd.val()+'</td>' +
							'<td tag="taxRate" hidden width="80" title="'+m.taxRate.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+m.taxRate.val()+'</td>' +
							'<td tag="orderPrice" hidden width="130" title="'+m.item_input_orderPrice.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderPrice" tdindex="zgGridTtable_orderPrice">'+m.item_input_orderPrice.val()+'</td>' +
							'<td tag="salePrice" hidden width="130" title="'+m.item_input_salePrice.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_salePrice" tdindex="zgGridTtable_salePrice">'+m.item_input_salePrice.val()+'</td>' +
							'<td tag="psd" width="130" title="'+m.item_input_psd.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_psd" tdindex="zgGridTtable_psd">'+m.item_input_psd.val()+'</td>' +
							'<td tag="minDisplayQty" width="130" title="'+m.item_input_minDisplayQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_minDisplayQty" tdindex="zgGridTtable_minDisplayQty">'+m.item_input_minDisplayQty.val()+'</td>' +
							'<td tag="minOrderQty" width="130" title="'+m.item_input_minOrderQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_minOrderQty" tdindex="zgGridTtable_minOrderQty">'+m.item_input_minOrderQty.val()+'</td>' +
							'<td tag="orderBatchQty" hidden width="130" title="'+m.item_input_orderBatchQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderBatchQty" tdindex="zgGridTtable_orderBatchQty">'+m.item_input_orderBatchQty.val()+'</td>' +
							'<td tag="autoOrderQty" width="230" title="'+m.item_input_autoOrderQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_autoOrderQty" tdindex="zgGridTtable_autoOrderQty">'+m.item_input_autoOrderQty.val()+'</td>' +
							'<td tag="orderQty" width="160" title="'+m.orderQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderQty" tdindex="zgGridTtable_orderQty">'+toThousands(m.orderQty.val())+'</td>' +
							'<td tag="orderTotalAmt" hidden width="160" title="'+m.item_input_orderTotalAmt.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderTotalAmt" tdindex="zgGridTtable_orderTotalAmt">'+m.item_input_orderTotalAmt.val()+'</td>' +
							'<td tag="orderNochargeQty" hidden width="190" title="'+m.item_input_orderNochargeQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderNochargeQty" tdindex="zgGridTtable_orderNochargeQty">'+m.item_input_orderNochargeQty.val()+'</td>' +
							'<td tag="orderTotalQty" hidden width="190" title="'+m.item_input_orderTotalQty.val()+'" align="right" id="zgGridTtable_'+rowindex+'_tr_orderTotalQty" tdindex="zgGridTtable_orderTotalQty">'+m.item_input_orderTotalQty.val()+'</td>' +
							'<td tag="vendorId" hidden width="80" title="'+m.item_input_vendorId.val()+'" align="left" id="zgGridTtable_'+rowindex+'_tr_vendorId" tdindex="zgGridTtable_vendorId">'+m.item_input_vendorId.val()+'</td>' +
							'</tr>';
						//新增商品
						tableGrid.append(tr);
						//计算合计
						gridTotal();
						$('#update_dialog').modal("hide");
						//更新总条数
						var total = tableGrid.find("tr").length-1;
						$("#records").text(total);
						_common.prompt("Data added successfully！",2,"success"); // 商品添加成功
						/*$.myAjaxs({
							url:url_left+"/getFreeItemInfo",
							async:true,
							cache:false,
							type :"get",
							data :{
								articleId:articleId,
								storeCd:$("#aStore").attr("k"),
								orderDate:subfmtDate(m.od_date.val()),
								vendorId:m.item_input_vendorId.val(),
								orderQty:reThousands(m.orderQty.val()),
								orderFlg:"1" //新增
							},
							dataType:"json",
							success:function(result){
								//新增搭赠商品
								if(result.success&&result.data.length>0){
									result.data.forEach(function(item, i){
										var itemAppendFlg = true;
										$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
											var _articleId = $(this).find('td[tag=articleId]').text();
											let _orderQty = reThousands($(this).find('td[tag=orderQty]').text());
											let orderTotalQty = Number(_orderQty)+item.orderNochargeQty;
											if(_articleId==item.articleId){//商品已存在 修改搭赠量
												$(this).find('td[tag=orderNochargeQty]').text(toThousands(item.orderNochargeQty));
												$(this).find('td[tag=orderTotalQty]').text(toThousands(orderTotalQty));
												itemAppendFlg = false;
											}
										});
										if(itemAppendFlg){//新增搭赠商品
											let freeRowIndex = rowindex+1;
											var freeItemTr = '<tr id="zgGridTtable_'+freeRowIndex+'_tr" class="">' +
												'<td tag="ckline" align="center" style="color:#428bca;" id="zgGridTtable_'+freeRowIndex+'_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_'+freeRowIndex+'_tr" name="zgGridTtable"> </td>' +
												'<td tag="barcode" width="130" title="'+item.barcode+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
												'<td tag="articleId" width="130" title="'+item.articleId+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
												'<td tag="articleName" width="180" title="'+item.articleName+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
												'<td tag="unitId" hidden width="130" title="'+item.unitId+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_unitId" tdindex="zgGridTtable_unitId">'+item.unitId+'</td>' +
												'<td tag="unitName" width="130" title="'+item.unitName+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_unitName" tdindex="zgGridTtable_unitName">'+item.unitName+'</td>' +
												'<td tag="spec" width="130" title="'+item.spec+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_spec" tdindex="zgGridTtable_spec">'+item.spec+'</td>' +
												'<td tag="converter" hidden width="130" title="'+item.converter+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_converter" tdindex="zgGridTtable_converter">'+item.converter+'</td>' +
												'<td tag="purchaseVatCd" hidden width="80" title="'+item.purchaseVatCd+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_purchaseVatCd" tdindex="zgGridTtable_purchaseVatCd">'+item.purchaseVatCd+'</td>' +
												'<td tag="taxRate" hidden width="80" title="'+item.taxRate+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+item.taxRate+'</td>' +
												'<td tag="orderPrice" hidden width="130" title="'+item.orderPrice+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderPrice" tdindex="zgGridTtable_orderPrice">'+item.orderPrice+'</td>' +
												'<td tag="salePrice" hidden width="130" title="'+item.salePrice+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_salePrice" tdindex="zgGridTtable_salePrice">'+item.salePrice+'</td>' +
												'<td tag="psd" width="230" title="'+toThousands(item.psd)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_psd" tdindex="zgGridTtable_psd">'+toThousands(item.psd)+'</td>' +
												'<td tag="minDisplayQty" width="130" title="'+toThousands(item.minDisplayQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_minDisplayQty" tdindex="zgGridTtable_minDisplayQty">'+toThousands(item.minDisplayQty)+'</td>' +
												'<td tag="minOrderQty" width="160" title="'+toThousands(item.minOrderQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_minOrderQty" tdindex="zgGridTtable_minOrderQty">'+toThousands(item.minOrderQty)+'</td>' +
												'<td tag="orderBatchQty" hidden width="160" title="'+item.orderBatchQty+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderBatchQty" tdindex="zgGridTtable_orderBatchQty">'+item.orderBatchQty+'</td>' +
												'<td tag="autoOrderQty" width="230" title="'+toThousands(item.autoOrderQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_autoOrderQty" tdindex="zgGridTtable_autoOrderQty">'+toThousands(m.item_input_autoOrderQty.val())+'</td>' +
												'<td tag="orderQty" width="160" title="0" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderQty" tdindex="zgGridTtable_orderQty">0</td>' +
												'<td tag="orderTotalAmt" hidden width="160" title="0" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderTotalAmt" tdindex="zgGridTtable_orderTotalAmt">0</td>' +
												'<td tag="orderNochargeQty" width="190" title="'+toThousands(item.orderNochargeQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderNochargeQty" tdindex="zgGridTtable_orderNochargeQty">'+toThousands(item.orderNochargeQty)+'</td>' +
												'<td tag="orderTotalQty" width="190" title="'+toThousands(item.orderNochargeQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderTotalQty" tdindex="zgGridTtable_orderTotalQty">'+toThousands(item.orderNochargeQty)+'</td>' +
												'<td tag="vendorId" hidden width="80" title="'+item.vendorId+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_vendorId" tdindex="zgGridTtable_vendorId">'+item.vendorId+'</td>' +
												'</tr>';
											tableGrid.append(freeItemTr);
										}
									});
								}
							},
							complete:_common.myAjaxComplete
						});*/
					}else if(flg =='update'){

						//修改商品信息
						$(selectTrTemp).find('td[tag=barcode]').text(m.item_input_barcode.val());
						$(selectTrTemp).find('td[tag=articleId]').text($("#a_item").attr("k"));
						$(selectTrTemp).find('td[tag=articleName]').text(m.item_input_name.val());
						$(selectTrTemp).find('td[tag=unitId]').text(m.item_input_uomCd.val());
						$(selectTrTemp).find('td[tag=unitName]').text(m.item_input_uom.val());
						$(selectTrTemp).find('td[tag=spec]').text(m.item_input_spec.val());
						$(selectTrTemp).find('td[tag=converter]').text(m.item_input_converter.val());
						$(selectTrTemp).find('td[tag=purchaseVatCd]').text(m.purchaseVatCd.val());
						$(selectTrTemp).find('td[tag=taxRate]').text(m.taxRate.val());
						$(selectTrTemp).find('td[tag=orderPrice]').text(m.item_input_orderPrice.val());
						$(selectTrTemp).find('td[tag=salePrice]').text(m.item_input_salePrice.val());
						$(selectTrTemp).find('td[tag=psd]').text(m.item_input_psd.val());
						$(selectTrTemp).find('td[tag=minDisplayQty]').text(m.item_input_minDisplayQty.val());
						$(selectTrTemp).find('td[tag=minOrderQty]').text(m.item_input_minOrderQty.val());
						$( selectTrTemp).find('td[tag=orderBatchQty]').text(m.item_input_orderBatchQty.val());
						$(selectTrTemp).find('td[tag=autoOrderQty]').text(m.item_input_autoOrderQty.val());
						$(selectTrTemp).find('td[tag=orderQty]').text(m.orderQty.val());
						$(selectTrTemp).find('td[tag=orderNochargeQty]').text(m.item_input_orderNochargeQty.val());
						$(selectTrTemp).find('td[tag=orderTotalQty]').text(m.item_input_orderTotalQty.val());
						$(selectTrTemp).find('td[tag=vendorId]').text(m.item_input_vendorId.val());
						var totalQty = reThousands(m.item_input_orderTotalAmt.val());
						$(selectTrTemp).find('td[tag=orderTotalAmt]').text(toThousands(totalQty));
						//计算合计
						gridTotal();
						$('#update_dialog').modal("hide");
						//更新总条数
						var total = tableGrid.find("tr").length-1;
						$("#records").text(total);
						_common.prompt("Data updated successfully！",2,"success"); // 商品修改成功
						/*$.myAjaxs({
							url:url_left+"/getFreeItemInfo",
							async:true,
							cache:false,
							type :"get",
							data :{
								articleId:articleId,
								storeCd:$("#aStore").attr("k"),
								orderDate:subfmtDate(m.od_date.val()),
								vendorId:m.item_input_vendorId.val(),
								orderQty:reThousands(m.orderQty.val()),
								oldOrderQty:reThousands($(selectTrTemp).find('td[tag=orderQty]').text()),
								orderFlg:"2" //修改
							},
							dataType:"json",
							success:function(result){
								//新增搭赠商品
								if(result.success&&result.data.length>0){
									result.data.forEach(function(item, i){
										var itemAppendFlg = true;
										$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
											var _articleId = $(this).find('td[tag=articleId]').text();
											if(_articleId==item.articleId){//商品已存在 修改搭赠量
                                                let _orderQty = reThousands($(this).find('td[tag=orderQty]').text());
												let _orderNochargeQty = reThousands($(this).find('td[tag=orderNochargeQty]').text());
                                                let newOrderNochargeQty = Number(_orderNochargeQty)+item.orderNochargeQty;
												let orderTotalQty = Number(_orderQty)+Number(newOrderNochargeQty);
                                                $(this).find('td[tag=orderNochargeQty]').text(toThousands(newOrderNochargeQty));
												$(this).find('td[tag=orderTotalQty]').text(toThousands(orderTotalQty));
												//搭赠量 订货量 为0 删除搭赠商品
												// if(_orderQty=="0"&&newOrderNochargeQty=="0"){
                                                //     $(this).remove();
                                                // }
												itemAppendFlg = false;
											}
										});
										if(itemAppendFlg){//新增搭赠商品
											let freeRowIndex = rowindex+1;
											var freeItemTr = '<tr id="zgGridTtable_'+freeRowIndex+'_tr" class="">' +
												'<td tag="ckline" align="center" style="color:#428bca;" id="zgGridTtable_'+freeRowIndex+'_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_'+freeRowIndex+'_tr" name="zgGridTtable"> </td>' +
												'<td tag="barcode" width="130" title="'+item.barcode+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+item.barcode+'</td>' +
												'<td tag="articleId" width="130" title="'+item.articleId+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+item.articleId+'</td>' +
												'<td tag="articleName" width="180" title="'+item.articleName+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+item.articleName+'</td>' +
												'<td tag="unitId" hidden width="130" title="'+item.unitId+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_unitId" tdindex="zgGridTtable_unitId">'+item.unitId+'</td>' +
												'<td tag="unitName" width="130" title="'+item.unitName+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_unitName" tdindex="zgGridTtable_unitName">'+item.unitName+'</td>' +
												'<td tag="spec" width="130" title="'+item.spec+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_spec" tdindex="zgGridTtable_spec">'+item.spec+'</td>' +
												'<td tag="converter" hidden width="130" title="'+item.converter+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_converter" tdindex="zgGridTtable_converter">'+item.converter+'</td>' +
												'<td tag="purchaseVatCd" hidden width="80" title="'+item.purchaseVatCd+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_purchaseVatCd" tdindex="zgGridTtable_purchaseVatCd">'+item.purchaseVatCd+'</td>' +
												'<td tag="taxRate" hidden width="80" title="'+item.taxRate+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+item.taxRate+'</td>' +
												'<td tag="orderPrice" hidden width="130" title="'+item.orderPrice+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderPrice" tdindex="zgGridTtable_orderPrice">'+item.orderPrice+'</td>' +
												'<td tag="salePrice" hidden width="130" title="'+item.salePrice+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_salePrice" tdindex="zgGridTtable_salePrice">'+item.salePrice+'</td>' +
												'<td tag="psd" width="230" title="'+toThousands(item.psd)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_psd" tdindex="zgGridTtable_psd">'+toThousands(item.psd)+'</td>' +
												'<td tag="minDisplayQty" width="130" title="'+toThousands(item.minDisplayQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_minDisplayQty" tdindex="zgGridTtable_minDisplayQty">'+toThousands(item.minDisplayQty)+'</td>' +
												'<td tag="minOrderQty" width="160" title="'+toThousands(item.minOrderQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_minOrderQty" tdindex="zgGridTtable_minOrderQty">'+toThousands(item.minOrderQty)+'</td>' +
												'<td tag="orderBatchQty" hidden width="160" title="'+item.orderBatchQty+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderBatchQty" tdindex="zgGridTtable_orderBatchQty">'+item.orderBatchQty+'</td>' +
												'<td tag="autoOrderQty" width="230" title="'+toThousands(item.autoOrderQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_autoOrderQty" tdindex="zgGridTtable_autoOrderQty">'+toThousands(m.item_input_autoOrderQty.val())+'</td>' +
												'<td tag="orderQty" width="160" title="0" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderQty" tdindex="zgGridTtable_orderQty">0</td>' +
												'<td tag="orderTotalAmt" hidden width="160" title="0" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderTotalAmt" tdindex="zgGridTtable_orderTotalAmt">0</td>' +
												'<td tag="orderNochargeQty" width="190" title="'+toThousands(item.orderNochargeQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderNochargeQty" tdindex="zgGridTtable_orderNochargeQty">'+toThousands(item.orderNochargeQty)+'</td>' +
												'<td tag="orderTotalQty" width="190" title="'+toThousands(item.orderNochargeQty)+'" align="right" id="zgGridTtable_'+freeRowIndex+'_tr_orderTotalQty" tdindex="zgGridTtable_orderTotalQty">'+toThousands(item.orderNochargeQty)+'</td>' +
												'<td tag="vendorId" hidden width="80" title="'+item.vendorId+'" align="left" id="zgGridTtable_'+freeRowIndex+'_tr_vendorId" tdindex="zgGridTtable_vendorId">'+item.vendorId+'</td>' +
												'</tr>';
											tableGrid.append(freeItemTr);
										}
									});
								}
							},
							complete:_common.myAjaxComplete
						});*/
					}
				}
			});
		});

		//弹出窗 确认取消按钮
		m.cancel.on("click",function(){
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				$("#a_item").css("border-color", "#CCCCCC");
				$("#orderQty").css("border-color", "#CCCCCC");
				$("#item_input_orderNochargeQty").css("border-color", "#CCCCCC");
				if(result=="true") {
					var a_item = $("#a_item").val();
					var orderQty = $("#orderQty").val();
					var item_input_orderNochargeQty = $("#item_input_orderNochargeQty").val();
					$("#update_dialog").modal("hide");
				}
			});
		});

		m.returnsSubmitBut.on("click",function(){
			if(!verifySearch()){
				return false;
			}
			//合计金额
			var totalAmt = parseFloat(reThousands($("#order_total_amt").val()));
			var taxRateDiff  = false;
			var itemDetail = [],itemStatus = false;
			if($("#zgGridTtable>.zgGrid-tbody tr").length>0){
				/*let _taxRate = $("#zgGridTtable>.zgGrid-tbody tr:eq(0)").find('td[tag=taxRate]').text();
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    if(reThousands($(this).find('td[tag=orderQty]').text())!=0){
                        _taxRate = $(this).find('td[tag=taxRate]').text();
                        return false
                    }
                });*/
				$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
					var orderItem = {
						orderId:$("#order_id").val(),
						storeCd:$("#aStore").attr("k"),
						vendorId:$(this).find('td[tag=vendorId]').text().trim(),
						articleId:$(this).find('td[tag=articleId]').text(),
						barcode:$(this).find('td[tag=barcode]').text(),
						orderUnit:$(this).find('td[tag=unitId]').text(),//订货单位
						orderQty:parseInt(reThousands($(this).find('td[tag=orderQty]').text())),//订购数量
						purchaseVatCd:$(this).find('td[tag=purchaseVatCd]').text(),//税区分CD
						taxRate:parseFloat($(this).find('td[tag=taxRate]').text()),//税率
						orderNochargeQty:parseInt(reThousands($(this).find('td[tag=orderNochargeQty]').text())),//搭赠数量
						orderPrice: parseFloat(reThousands($(this).find('td[tag=orderPrice]').text())),//订货单价（不含税）
						orderAmtNotax: parseFloat(reThousands($(this).find('td[tag=orderTotalAmt]').text())),//订货价格（不含税）
						isFreeItem:'0',
						// orderAmt: reThousands($(this).find('td[tag=orderTotalAmt]').text()),//订货价格（页面暂不含税）
					}
                    /*if(_taxRate!=$(this).find('td[tag=taxRate]').text()&&orderItem.orderQty!=0){
                        //税率不同 订货量不为空
                        taxRateDiff = true;
                    }*/
					if(orderItem.orderQty!==0){
					    //订货量不为空   搭赠量不为空
						itemStatus = true;
						itemDetail.push(orderItem);
					}
				});
			}
			/*if(taxRateDiff){
				//该商品税率不同，请重新选择商品
				_common.prompt("You can only add items with same VAT rate for one PO, please select item again!",3,"info");
				return false;
			}*/
			if(!itemStatus){//订货数量不能为空
				$("#orderQty").css("border-color","red");
				_common.prompt("Order Qty can not be empty!",5,"info");
				return false;
			}
			var orderDetail = "";
			if(itemDetail.length>0){//订货商品
				orderDetail = JSON.stringify(itemDetail)
			}else{
				_common.prompt("Order Item can not be empty!",5,"info");
				return false;
			}
			var order = {
				storeCd:$("#aStore").attr("k"),
				orderId:$("#order_id").val(),
				vendorId:$("#delivery_center_id").val().trim(),
				orderType:$("#orderType").val(),
				orderDate:subfmtDate($("#od_date").val()),
				orderRemark:$("#orderRemark").val(),
				orderAmtNotax:reThousands($("#order_total_amt").val()),//总订货价格（页面暂不含税）
				orderDifferentiate:"1",//dc订货
				orderDetailJson:orderDetail,
				use:m.use.val(),
				toKen:m.toKen.val()
			}
			_common.myConfirm("Are you sure you want to save？",function(result){
				if(result!="true"){return false;}
				$.myAjaxs({
					url:url_left+"/save",
					async:true,
					cache:false,
					type :"post",
					data :order,
					dataType:"json",
					success:function(result){
						if(result.success){
							if(m.use.val()=="1"){
								m.md_date.val(fmtIntDate(result.data.updateYmd));//修改时间
								m.md_user.val(result.data.userName);//用户
							}else if(m.use.val()=="2"){
								m.order_id.val(result.data.orderId);
								m.ca_date.val(fmtIntDate(result.data.createYmd));//创建时间
								m.ca_user.val(result.data.userName);//用户
								m.use.val("0");//变为查看模式
							}
							m.toKen.val(result.toKen);

							var submitFlag=true;
							//发起审核
							var typeId =m.typeId.val();
							var	nReviewid =m.reviewId.val();
							var	recordCd = m.order_id.val();
							_common.initiateAudit($("#aStore").attr("k"),recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
								view();
								//审核按钮禁用
								m.approvalBut.prop("disabled",true);
								m.toKen.val(token);

								_common.prompt("Submitted successfully！",2,"success");
							})
						}else{
							_common.prompt(result.message,5,"error");
						}
					},
					complete:_common.myAjaxComplete
				});
			})
		})
    }

	//获取实时库存数量
    var setInventoryQty = function(articleId){
		m.inventoryQty.val('');
		$.myAjaxs({
			url:systemPath+"/inventoryVoucher/getStock",
			async:true,
			cache:false,
			type :"get",
			data :{
				storeCd:$("#aStore").attr("k"),
				itemId:articleId
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					m.inventoryQty.val(toThousands(result.o.realtimeQty));
				}else{
					_common.prompt(result.message,5,"error");// 获取实时库存失败
				}
			},
			complete:_common.myAjaxComplete
		});
	}

    var setDialogValue = function () {
		var cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,unitId,unitName,spec,converter,orderPrice,orderBatchQty,salePrice,orderTotalAmt,psd,minDisplayQty,minOrderQty,autoOrderQty,orderQty,orderNochargeQty,orderTotalQty,purchaseVatCd,taxRate,vendorId");
		$.myAutomatic.setValueTemp(a_item,cols['articleId'],cols['articleId']+" "+cols['articleName']);
		m.item_input_barcode.val(cols['barcode']);
		m.item_input_name.val(cols['articleName']);
		m.item_input_uomCd.val(cols['unitId']);
		m.item_input_uom.val(cols['unitName']);
		m.item_input_spec.val(cols['spec']);
		m.item_input_converter.val(cols['converter']);
		m.item_input_orderPrice.val(cols['orderPrice']);
		m.item_input_orderBatchQty.val(cols['orderBatchQty']);
		m.item_input_salePrice.val(cols['salePrice']);
		m.item_input_minOrderQty.val(cols['minOrderQty']);
		m.item_input_minDisplayQty.val(cols['minDisplayQty']);
		m.item_input_psd.val(cols['psd']);
		m.item_input_autoOrderQty.val(cols['autoOrderQty']);
		m.orderQty.val(cols['orderQty']);
		m.item_input_orderTotalAmt.val(cols['orderTotalAmt']);
		m.item_input_orderNochargeQty.val(cols['orderNochargeQty']);
		m.item_input_orderTotalQty.val(cols['orderTotalQty']);
		m.item_input_vendorId.val(cols['vendorId']);
		m.purchaseVatCd.val(cols['purchaseVatCd']);
		m.taxRate.val(cols['taxRate']);
		setInventoryQty(cols['articleId']);
	};

	//验证加载表格是否合法
	var verifySearch = function(){
		//验证当前batch处理是否禁用
		let batchFlg = false;
		_common.batchCheck(function (result) {
			if(!result.success){//禁用
				_common.prompt(result.message,5,"info");
				batchFlg = false;
			}else{
				batchFlg = true;
			}
		})
		if(!batchFlg){return batchFlg};
		var storeCd = $("#aStore").attr("k");
		if(storeCd==null||storeCd==""){
			_common.prompt("Please select a store first!",3,"error"); // 请选择店铺
			$("#aStore").css("border-color", "red");
			$("#aStore").focus();
			return false;
		}else {
			$("#aStore").css("border-color", "#CCC");
		}
		if(m.od_date.val()==null||m.od_date.val()==""){
			_common.prompt("Please enter the order date!",3,"error"); // 请输入订货日期
			m.od_date.css("border-color", "red");
			m.od_date.focus();
			return false;
		}else {
			m.od_date.css("border-color", "#CCC");
		}
		var dcId = $("#delivery_center_id").val();
		if(dcId==null||dcId==""){
			_common.prompt("DC can not be empty!",3,"info"); // dc不能为空
			return false;
		}
		var str = "&storeCd=" + storeCd+"&dcId="+dcId+"&orderDate="+subfmtDate(m.od_date.val());
		$.myAutomatic.replaceParam(a_item, str);
		return true;
	};

	var itemClear = function () {
		$("#a_item").val('').attr("k",'').attr("v",'');
		m.item_input_barcode.val('');
		m.item_input_name.val('');
		m.item_input_uom.val('');
		m.item_input_uomCd.val('');
		m.item_input_spec.val('');
		m.item_input_converter.val('');
		m.item_input_orderPrice.val('');
		m.item_input_salePrice.val('');
		m.item_input_minOrderQty.val('');
		m.item_input_minDisplayQty.val('');
		m.item_input_psd.val('');
		m.item_input_autoOrderQty.val('');
		m.item_input_orderBatchQty.val('');
		m.orderQty.val('');
		m.item_input_orderNochargeQty.val(0);
		m.item_input_orderTotalQty.val(0);
		m.item_input_orderTotalAmt.val('');
		m.inventoryQty.val('');
		m.item_input_vendorId.val('');
	};

	// 设置店铺选择禁用
	var setStoreInputDisable = function(){
		// 店铺按钮input禁用
		$("#aRegion").attr("disabled","disabled");
		$("#aCity").attr("disabled","disabled");
		$("#aDistrict").attr("disabled","disabled");
		$("#aStore").attr("disabled","disabled");
		$("#regionRefresh").hide();
		$("#regionRemove").hide();
		$("#cityRefresh").hide();
		$("#cityRemove").hide();
		$("#districtRefresh").hide();
		$("#districtRemove").hide();
		$("#storeRefresh").hide();
		$("#storeRemove").hide();
	}

	//画面查看模式
	var view = function () {
		//dc信息，重置按钮禁用
		// m.search.attr("disabled","disabled");
		m.reset.attr("disabled","disabled");
		//按钮自动加载订货商品禁用
		m.auto_import_item.attr("disabled","disabled").css("pointer-events","none");
		$("#orderRemark").attr("disabled","disabled");
		// 下拉选项禁用
		m.orderType.attr("disabled","disabled");
		m.od_date.attr("disabled","disabled");
		//table按钮禁用
		$("#addOrderDetails").attr("disabled","disabled");
		$("#updateOrderDetails").attr("disabled","disabled");
		$("#deleteOrderDetails").attr("disabled","disabled");
		//提交按钮
		$("#returnsSubmitBut").attr("disabled","disabled");
		setStoreInputDisable();
	}
	// 根据权限类型的不同初始化不同的画面样式
	var initPageByType = function(flgType){
		switch(flgType) {
			case "0"://查看
				initView();
				view();
                //检查是否允许审核
                _common.checkRole(m.order_id.val(),m.typeId.val(),function (success) {
                    if(success){
						m.approvalBut.prop("disabled",false);
                        //可以审核显示进货价
                        // tableGrid.showColumn("orderPrice");
                    }else{
                        m.approvalBut.prop("disabled",true);
                    }
                });
				break;
			case "1"://修改
				initView();
				view();
				$("#orderRemark").removeAttr("disabled");
				//按钮自动加载订货商品启用
				m.auto_import_item.removeAttr("disabled").css("pointer-events","auto");
				//table按钮启用
				$("#addOrderDetails").removeAttr("disabled");
				$("#updateOrderDetails").removeAttr("disabled");
				$("#deleteOrderDetails").removeAttr("disabled");
				//提交按钮启用
				$("#returnsSubmitBut").removeAttr("disabled");
				//审核按钮禁用
				m.approvalBut.prop("disabled",true);
				break;
			case "2"://新增
				var businessDate = m.businessDate.val();
				if(businessDate!=null&&businessDate!==''){
					businessDate = fmtIntDate(businessDate);
					m.od_date.val(businessDate);
					m.od_date.attr("disabled","disabled");
				}
				//订单状态默认订单待收
				m.reviewSts.val("1");
				//订单类型默认进货
				m.orderType.val("01");
				//下拉选项禁用
				m.orderType.attr("disabled","disabled");
				m.approvalBut.prop("disabled",true);
		}
	}

	//初始化 画面（修改/查看）
	var initView = function () {
		if (m.orderId.val() != null && m.orderId.val() != "") {
			$.myAjaxs({
				url:url_left+"/getOrderDcInfo",
				async:true,
				cache:false,
				type :"post",
				data :"orderId="+m.orderId.val(),
				dataType:"json",
				success:function(result){
					if(result.success){
						var interval = setInterval(function () {
							var reviewStsLen=m.reviewSts.find("option").length;
							var orderTypeLen=m.orderType.find("option").length;
							if(orderTypeLen>=1&&reviewStsLen>1){
								m.orderType.val(result.data.orderType);
								m.reviewSts.val(result.data.reviewStatus);
								clearInterval(interval);//停止
							}
						}, 200); //启动
						setTimeout(function () {
							clearInterval(interval);//停止
						},2000);
						m.od_date.val(fmtIntDate(result.data.orderDate));//订货时间
						m.md_date.val(fmtIntDate(result.data.updateYmd));//修改时间
						m.md_user.val(result.data.updateUserName);//修改用户
						m.ca_date.val(fmtIntDate(result.data.createYmd));//修改时间
						m.ca_user.val(result.data.createUserName);//修改用户
						m.orderRemark.val(result.data.orderRemark);//备注
						$.myAutomatic.setValueTemp(a_store,result.data.storeCd,result.data.storeCd+" "+result.data.storeName);
						$("#delivery_center_id").val(result.data.dcId);
						$("#delivery_center_name").val(result.data.dcName);
						//详细数据
						paramGrid = "orderDate=" + subfmtDate(m.od_date.val())+
							"&storeCd="+$("#aStore").attr("k")+
							"&dcId="+$("#delivery_center_id").val()+
							"&orderId="+m.order_id.val();
						tableGrid.setting("url", url_left + "/getItemsByDc");
						tableGrid.setting("param", paramGrid);
						tableGrid.loadData(null);
					}else{
						_common.prompt("Failed to load data!",5,"error"); // 供应商数据加载失败
						view();//页面查看模式
					}
				},
				error : function(e){
					_common.prompt("Failed to load data!",5,"error"); // 供应商数据加载失败
					view();//页面查看模式
				}
			});
		}
	}
    
    //画面按钮点击事件
    var but_event = function(){
		//要货日期
		m.od_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
        // 重置
        m.reset.on("click",function(){
        	//选择店铺启用以及清空
			m.aStore.val("");
			m.aStore.prop("disabled",false);
			$("#storeRefresh").show();
			$("#storeRemove").show();
			//按钮自动加载订货商品启用
			m.auto_import_item.removeAttr("disabled").css("pointer-events","auto");
			//清空查询数据
			$("#zgGridTtable  tr:not(:first)").remove();
			//清空备注
            m.orderRemark.val("");
        });
    }

    var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click",function(){
			var recordId = m.order_id.val();
			if(recordId!=null&&recordId!=""){
				$("#approval_dialog").modal("show");
				//获取审核记录
				_common.getStep(recordId,m.typeId.val());
			}else{
				_common.prompt("Record fetch failed, Please try again!",5,"error");
			}
		});
		//审核提交
		$("#audit_affirm").on("click",function () {
			//审核意见
			var auditContent = $("#auditContent").val();
			if(auditContent.length>200){
				_common.prompt("Approval comments cannot exceed 200 characters!",5,"error");
				return false;
			}
			//审核记录id
			var auditStepId = $("#auditId").val();
			//用户id
			var auditUserId = $("#auditUserId").val();
			//审核结果
			var auditStatus = $("#auditStatusGroup").find("input[type='radio']:checked").val();
			_common.myConfirm("Are you sure to save?", function (result) {
				if (result != "true") {
					return false;
				}
				var detailType = "tmp_on_order";
				$.myAjaxs({
					url:systemPath+"/audit/submit",
					async:true,
					cache:false,
					type :"post",
					data :{
						auditStepId:auditStepId,
						auditUserId:auditUserId,
						auditStatus:auditStatus,
						detailType:detailType,
						auditContent:auditContent,
						toKen:m.toKen.val()
					},
					dataType:"json",
					success:function(result){
						if(result.success){
							$("#approval_dialog").modal("hide");
							//更新主档审核状态值
							//_common.modifyRecordStatus(auditStepId,auditStatus);
							m.approvalBut.prop("disabled",true);
							_common.prompt("Saved Successfully!",3,"success");// 保存审核信息成功
						}else{
							m.approvalBut.prop("disabled",false);
							_common.prompt("Saved Failure!",5,"error");// 保存审核信息失败
						}
						m.toKen.val(result.toKen);
					},
					complete:_common.myAjaxComplete
				});
			})
		})
	}

	//初始化下拉
	var initAutoMatic = function () {
		a_store = $("#aStore").myAutomatic({
			url: systemPath + "/urgentOrderDc/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function(){
				$(".zgGrid-tbody").empty();
                $("#delivery_center_id").val('');
                $("#delivery_center_name").val('');
			},
			selectEleClick: function (thisObj) {
				$(".zgGrid-tbody").empty();
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					//获取dc信息
					getDcInfo();
				}
			}
		});

		// 如果只有一条店铺权限，则默认选择
		$.myAjaxs({
			url:systemPath+"/roleStore/getStoreByRole",
			async:true,
			cache:false,
			type :"post",
			data :null,
			dataType:"json",
			success:function(re){
				if(re.success){
					var obj = re.o;
					$("#aStore").attr("disabled", false);
					$("#storeRefresh").show();
					$("#storeRemove").show();
					$.myAutomatic.setValueTemp(a_store,obj.storeCd,obj.storeName);
                    //获取dc信息
                    getDcInfo();
				}
			}
		});
		//dc
		/*a_dc = $("#delivery_center_id").myAutomatic({
			url: systemPath+"/returnWarehouse/getDc",
			ePageSize: 10,
			startCount: 0
		});*/
		//商品
		a_item = $("#a_item").myAutomatic({
			url: url_left + "/getItems",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function (thisObj) {
				itemClear();
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					getItemInfo(thisObj.attr("k"));
					//设置实时库存数量
					setInventoryQty(thisObj.attr("k"));
				}else{
					itemClear();
				}
			}
		});
	}

	function getDcInfo() {
        $("#delivery_center_id").val('');
        $("#delivery_center_name").val('');
		$.myAjaxs({
			url:url_left+"/getDcInfo",
			async:true,
			cache:false,
			type :"get",
			data :{
				storeCd:$("#aStore").attr("k")
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					$("#delivery_center_id").val(result.data.deliveryCenterId);
					$("#delivery_center_name").val(result.data.deliveryCenterName);
				}else{
					_common.prompt(result.message,5,"error");
				}
			},
			complete:_common.myAjaxComplete
		});
	}

	function getItemInfo(articleId) {
		$.myAjaxs({
			url:url_left+"/getItemInfo",
			async:true,
			cache:false,
			type :"get",
			data :{
				articleId:articleId,
				storeCd:$("#aStore").attr("k"),
				orderDate:subfmtDate(m.od_date.val())
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					var data = result.data;
					m.item_input_barcode.val(data.barcode);
					m.item_input_name.val(data.articleName);
					m.item_input_uom.val(data.unitName);
					m.item_input_uomCd.val(data.unitId);
					m.item_input_spec.val(data.spec);
					m.item_input_converter.val(data.converter);
					m.item_input_orderBatchQty.val(toThousands(data.orderBatchQty));
					m.item_input_orderPrice.val(data.orderPrice);
					m.item_input_salePrice.val(toThousands(data.salePrice));
					m.item_input_minOrderQty.val(toThousands(data.minOrderQty));
					m.item_input_minDisplayQty.val(toThousands(data.minDisplayQty));
					m.item_input_psd.val(toThousands(data.psd));
					m.item_input_autoOrderQty.val(toThousands(data.autoOrderQty));
					m.item_input_vendorId.val(data.vendorId.trim());
					m.purchaseVatCd.val(data.purchaseVatCd);
					m.taxRate.val(data.taxRate);
				}else{
					_common.prompt(result.message,3,"error");
					itemClear();
				}
			},
			error : function(e){
				_common.prompt("Failed to load item data!",5,"error"); // 商品数据加载失败
				itemClear();
			},
			complete:_common.myAjaxComplete
		});
	}

	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		// initSelectOptions("Voucher Status","orderSts", "00150");
		initSelectOptions("Voucher Type","orderType", "00145");
	}

	// 初始化下拉列表
	function initSelectOptions(title, selectId, code ,value) {
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
					if(code=='00120'){
						var str = Number(parseFloat(optionText) * 100).toFixed(2);
						str += "%";
						optionText = str;
					}
					selectObj.append(new Option(optionValue +" "+ optionText, optionValue));
				}
				if(value)
					selectObj.val(value);

			},
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			},
			complete:_common.myAjaxComplete
		});
	}

	//表格初始化
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Order List",
			param:paramGrid,
			colNames:["Item Barcode","Item Code","Item Name","Unit Id","UOM","Specification","Converter","Purchase Vat Cd","VAT IN(%)","Purchasing Price",
				"Price","PSD","Display on Shelf","DC MOQ","Incremental Quantity",
				"SOQ","Order Quantity","Order Amount","Free Order Quantity","Order Total Quantity","Supplier Id"],
			colModel:[
				{name:"barcode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"unitId",type:"text",text:"center",width:"130",ishide:true,css:""},
				{name:"unitName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"spec",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"converter",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:zeroFmt},
				{name:"purchaseVatCd",type:"text",text:"left",width:"80",ishide:true,css:""},
				{name:"taxRate",type:"text",text:"right",width:"80",ishide:true,css:""},
				{name:"orderPrice",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"salePrice",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"psd",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"minDisplayQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"minOrderQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderBatchQty",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"autoOrderQty",type:"text",text:"right",width:"230",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderQty",type:"text",text:"right",width:"160",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderTotalAmt",type:"text",text:"right",width:"160",ishide:true,css:"",getCustomValue:getThousands},
				{name:"orderNochargeQty",type:"text",text:"right",width:"190",ishide:true,css:"",getCustomValue:getThousands},
				{name:"orderTotalQty",type:"text",text:"right",width:"190",ishide:true,css:"",getCustomValue:getThousands},
				{name:"vendorId",type:"text",text:"left",width:"80",ishide:true,css:""}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:false,//是否需要分页
			isCheckbox:false,
			height:300,
			freezeHeader:true,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},

			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				//计算合计
				gridTotal();
				// tableGrid.hideColumn("orderPrice");
				//更新总条数(注减一是去除标题行)
				var total = tableGrid.find("tr").length-1;
				$("#records").text(total);
				return self;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				if(m.use.val()=="1"||m.use.val()=="2"){
                    let orderQty = $(selectTrTemp).find('td[tag=orderQty]').text();
                    let orderNochargeQty = $(selectTrTemp).find('td[tag=orderNochargeQty]').text();
                    if(orderNochargeQty!="0"){
                        $("#deleteOrderDetails").attr("disabled","disabled");
                    }else{
                        $("#deleteOrderDetails").removeAttr("disabled");
                    }
                }
			},
			buttonGroup:[
				{
					butType: "add",
					butId: "addOrderDetails",
					butText: "Add",
					butSize: ""
				},
				{
					butType: "update",
					butId: "updateOrderDetails",
					butText: "Modify",
					butSize: ""
				},
				{
					butType: "view",
					butId: "viewOrderDetails",
					butText: "View",
					butSize: ""//,
				},
				{
					butType: "delete",
					butId: "deleteOrderDetails",
					butText: "Delete",
					butSize: ""//,
				}
			],
		});
		$("#zgGridTtable_main_foot").append("<div class='zgGrid-tfoot-td' id='zgGridTtable_tfoot_box'><span class='zg-page-records'>Total: <span id='records'>0</span> items</span></div>");
	}

	var gridTotal = function(){
    	var orderPriceTotal = 0;
		var orderQtyTotal = 0;
		$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
			var orderTotalAmt = parseFloat(reThousands($(this).find('td[tag=orderTotalAmt]').text()));
			if(!isNaN(orderTotalAmt))
				orderPriceTotal +=  parseFloat(orderTotalAmt);
			var orderQty = parseFloat(reThousands($(this).find('td[tag=orderQty]').text()));
			if(!isNaN(orderQty))
				orderQtyTotal +=  parseFloat(orderQty);
			// var orderNochargeQty = parseFloat(reThousands($(this).find('td[tag=orderNochargeQty]').text()));
			// if(!isNaN(orderNochargeQty))
			// 	orderQtyTotal +=  parseFloat(orderNochargeQty);
		});
		//总订货金额
		$("#order_total_amt").val(toThousands(orderPriceTotal));
		//总订货数量
		$("#order_total_qty").val(toThousands(orderQtyTotal));

	}

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }

	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
	}

	//数值null判断
	var zeroFmt = function(tdObj, value){
		if(value==null||value==""){
			return $(tdObj).text("0");
		}
		return $(tdObj).text(value);
	}


	//表格显示计算税率
	var tdToPercent = function(tdObj, value){
		if(value==null||value==""){
			return $(tdObj).text(toPercent(0));
		}
		return $(tdObj).text(toPercent(value));
	}

	//计算税率
	function toPercent(point){
		var str=Number(point*100).toFixed(2);
		str+="%";
		return str;
	}
//格式化数字带千分位
	function fmtIntNum(val){
		if(val==null||val==""){
			return "0";
		}
		var reg=/\d{1,3}(?=(\d{3})+$)/g;
		return (val + '').replace(reg, '$&,');
	}
  //格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		if(date!=null&&date.trim()!=''&&date.length==8) {
			res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
		}
		return res;
	}

	function subfmtDate(date){
		var res = "";
		if(date!=null&&date!='')
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('orderDcEdit');
_start.load(function (_common) {
	_index.init(_common);
});
