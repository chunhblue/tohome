/*******************************
 * zgGrid
 * 轻量级表格插件。
 *
 * 多选框和行选择样式保持一致
 * *****************************
 * *****************************
 * *****************************
 * 
 * 仅支持json格式数据
 * author:songxz
 * time:2018-3-7
 * version:2.2.0 beta
 * 
 * *****************************
********************************/
;(function($) {
	$.zgGrid = $.zgGrid || {};//创建对象
	//装载扩展方法,内部方法外部无法调，初始化顺序 1
	$.extend($.zgGrid,{
		setTableContainer:function(self){//初始化设定table整体结构（默认结构）
			var thisId = self.id,//当前对象id
				thisObjTheadId = thisId+"_thead",//标题容器id
				thisObjTbodyId = thisId+"_tbody",//标题容器id
				thisObjTfootId = thisId+"_tfoot",//标题容器id
				thisObjTt = thisId+"_caption",//标题容器id
				thisObjMainBody = thisId+"_main_body",//标题容器id
				thisObjMainBodyLeft = thisId+"_main_body_left",//标题容器id
				thisObjMainBodyRight = thisId+"_main_body_right",//标题容器id
				thisObjMainBodyRight_box = thisId+"_main_body_right_box",//标题容器id
				thisObjMainfoot = thisId+"_main_foot",//标题容器id
				thisLodingId = thisId+"_loading",//加载中容器id
				thisLodingTextId = thisId+"_loading_text",//加载中文字器id
				thisLodingProgressbarId = thisId+"_loading_progressbar",//加载中滚动条id
				obj = $(self),
				parentObj = obj.parent(),
				strLoding = '<div role="progressbar" id="'+thisLodingProgressbarId+'" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%;" class="progress-bar progress-bar-striped active zgGrid-loding"></div>',
				zgGridBoxObj = parentObj,//最大的div，当前table父级div
				zgGridTitleBox='<div class="zgGrid-caption" id="'+thisObjTt+'" >'+self.defaults.title+'</div>',//标题（原：caption）
				zgGridFootBox='<div class="zgGrid-main-foot" id="'+thisObjMainfoot+'"></div>',//页脚（原：tfoot）
				zgGridBodyBox = '<div class="zgGrid-main-body" id="'+thisObjMainBody+'">'+'<div class="zgGrid-main-body-left" id="'+thisObjMainBodyLeft+'"></div>'+'<div class="zgGrid-main-body-right" id="'+thisObjMainBodyRight+'"><div id="'+thisObjMainBodyRight_box+'"></div></div>'+'</div>',//内容区，包含：冻结div与实际tableDiv（原：tbody）
				zgGridLoading="<div id='"+thisLodingId+"'  lock='false' class='loding-box'><div class='loding-box-prompt'><div id='"+thisLodingTextId+"' class='loading-text'>loading ... ... </div>"+strLoding+"</div><div class='loding-box-mask'></div></div>";
			
			parentObj.addClass("zgrid")//	
			parentObj.append(zgGridTitleBox);//加载头
			parentObj.append(zgGridBodyBox);//加载内容
			parentObj.append(zgGridFootBox);//加载脚
			parentObj.append(zgGridLoading);//等待加载loadig			
			
			self.eventstatus = true;//事件状态 默认true 当执行翻页等操作会变为false， 执行完成后才变回true
			self.zgGridLeftBox = $("#"+thisObjMainBodyLeft);
			self.zgGridRightBox = $("#"+thisObjMainBodyRight);
			self.zgGridRightBox_Box = $("#"+thisObjMainBodyRight_box);
			self.zgGridTitleBox = $("#"+thisObjTt);
			self.zgGridBodyBox = $("#"+thisObjMainBody);
			self.zgGridFootBox = $("#"+thisObjMainfoot);
			self.zgGridLoading = $("#"+thisLodingId);
			self.thisLodingText = $("#"+thisLodingTextId);
			self.thisLodingProgressbar = $("#"+thisLodingProgressbarId);
			$("#"+thisId).prependTo($("#"+thisObjMainBodyRight_box));
			self.table = $("#"+thisId);
			//初始表格内容
			self.table.append("<thead class='zgGrid-thead' id='"+thisObjTheadId+"'></thead>");//加载标题容器
			self.table.append("<tbody class='zgGrid-tbody' id='"+thisObjTbodyId+"'></tbody>");//加载内容容器
			self.table.addClass("table table-hover table-striped table-condensed table-bordered zgrid-table");//设置列表样式
			self.table.parent().parent().addClass("table-responsive");
		},	 
		setTableTh:function(self){//设置table标题
			var colNames = self.defaults.colNames,
				colModel = self.defaults.colModel,
				freezeIndex = self.defaults.freezeIndex,//冻结列坐标
				sortColMode = self.defaults.sortColMode,//排序列
				thead = (self.table).find("thead"),
				tableId = self.id,
				insterThStr = "";
			if(colNames.length==colModel.length){
				if(self.defaults.isCheckbox==true||self.defaults.isCheckbox=="true"){
					insterThStr+="<th tag='ckline' width='50' style='color:#428bca;' thIndex='"+tableId+"_ckline'><input type='checkbox' value='0' name='"+tableId+"' /></th>";
				}
				if(self.defaults.lineNumber==true||self.defaults.lineNumber=="true"){
					insterThStr+="<th tag='line' width='50' style='color:#428bca;' thIndex='"+tableId+"_line'>#</th>";
				}
				var freeCount = -1;
				for(var i=0;i<colNames.length;i++){
					var col = colModel[i];
					var tdNale = col.name,	
						width = col.width || "30px",//默认宽度
						type = col.type || "text",//默认宽度
						ishide = col.ishide || false,//是否隐藏列
						sortindex = col.sortindex||tdNale;//排序标记
					
					var isSort = $.inArray(tdNale, sortColMode);  //返回 3,不包含在数组中,则返回 -1;
					var tdHide = "";
					if(ishide){tdHide="hidden='true'";}else{tdHide="";freeCount++;}//列是否隐藏
					if(freezeIndex>=0&&freeCount<=freezeIndex&&!ishide){
						self.defaults.freezeCol.push(tdNale);
					}
					var x = width.substr(width.length-1,1);
					var ut="px";
					if(x=="%"){
						ut = "%";
					}else if(x=="x"){
						ut="";
					}
					var style = "style=width:"+width+ut;
					if(isSort>-1){
						
						var sortHtml = "<span class='zGgridSotrIcon glyphicon glyphicon-sort'></span>";
						var textHtml = "<span class='zGgridSotrText' >"+colNames[i]+"</span>";
						sortHtml = "<a href='javascript:void(0);' sord='asc' class='zGgridSotrA' valuetype='"+type+"' tag='"+tdNale+"' sortindex='"+sortindex+"' thIndex='"+tableId+"_"+tdNale+"' >"+textHtml+sortHtml+"</a>";
						insterThStr+="<th "+tdHide+" tag='"+tdNale+"'  sortindex='"+sortindex+"' width='"+width+"' "+style+" >"+sortHtml+"</th>";
					}else{
						insterThStr+="<th "+tdHide+" width='"+width+"'  "+style+"   tag='"+tdNale+"'  sortindex='"+sortindex+"' thIndex='"+tableId+"_"+tdNale+"'>"+colNames[i]+"</th>";
					}	
					
				}
			}else{
				insterThStr+="<th><span style='color:red;'>列标题与列内容的坐标名数量不相等！</span></th>";
			}
			insterThStr = insterThStr || '<th></th>';
			thead.append("<tr>"+insterThStr+"</tr>");//加载列标题
			insterThStr="";
			
		},
		setFreeze:function(self){//冻结列设定
			if(self.defaults.freezeIndex>=0){//存在冻结列的设定
				var freeTableId = self.id+"_freeze_table";
				var freeTableObj = $("#"+freeTableId),
					freeTableTheadId = self.id+"_freeze_table_thead_id",
					freeTableBodyId = self.id+"_freeze_table_tbody_id",
					freeTableTheadObj=null,
					freeTableBodyObj=null,
					freeColObjs = self.defaults.freezeCol,//冻结头名称
					tableObj = $(self.table);//当前table的对象
				if(freeTableObj.length<=0){
					var freezeDiv = $(self.zgGridLeftBox),//放入冻结列的专用div
						freeTable = '<table id="'+freeTableId+'" class="table table-hover table-striped table-condensed table-bordered zgrid-table freeze-table " style="width: 100%;"></table>';
					freezeDiv.append(freeTable);
					freeTableObj = $("#"+freeTableId);
					freeTableObj.append("<thead class='zgGrid-thead freeze-table-thead' id='"+freeTableTheadId+"'><tr></tr></thead>");//加载标题容器
					freeTableObj.append("<tbody class='zgGrid-tbody freeze-table-tbody' id='"+freeTableBodyId+"'></tbody>");//加载内容容器
				}
				freeTableTheadObj = $("#"+freeTableTheadId);
				freeTableBodyObj = $("#"+freeTableBodyId);
				
				freeTableTheadObj.find("tr:eq(0)").empty();
				var thSumWidth=0;
				//遍历动静th内容
				$.each(freeColObjs,function(index,node){
					var tableTh = tableObj.find("thead").find("th[tag='"+node+"']:eq(0)");
					tableTh.removeAttr("hidden").addClass("freeHide");
					var tempw = Number(tableTh.prop("width") || tableTh.width());
					var tempWidth = tableTh.outerWidth()||0;
					if(tempw>tempWidth){
						thSumWidth+=tempw;
					}else{
						thSumWidth+=tempWidth;
					}
					freeTableTheadObj.find("tr:eq(0)").append(tableTh.clone());
				});
				//处理每一行数据
				
				freeTableBodyObj.empty();
				// 得到 并且遍历每一行数据
				tableObj.find("tbody").find("tr:not([hidden])").each(function(index){
					var thisTrObj = $(this);
					var freetarget = thisTrObj.prop("id");
					var freeTrId = freetarget+"_free";
					freeTableBodyObj.append('<tr id="'+freeTrId+'" freetarget="'+freetarget+'" ></tr>');
					var trObj = $("#"+freeTrId);
					thisTrObj.find("td").each(function(td_Index){
						var td_obj = $(this);
						var tdTag = td_obj.attr("tag");
						if($.inArray(tdTag,freeColObjs)>-1){
							td_obj.removeAttr("hidden").addClass("freeHide");
							trObj.append(td_obj.clone());
						}
					});
					
				});
				
				this.setFreezeWidth(self,thSumWidth);
				var trHeight = tableObj.find("thead:eq(0)").find("tr:eq(0)").height();
				freeTableTheadObj.find("tr:eq(0)").height(trHeight);
			}
		},
		setFreezeWidth:function(self,thSumWidth){//当存在冻结列等数据时进行表格内的高度与宽度的设定
			
			var leftDivId = self.id+"_main_body_left",
				rightDivId = self.id+"_main_body_right",
				freeTableBodyId = self.id+"_freeze_table_tbody_id";
			// 设定左右容器div的宽度等，主要使用与冻结等
			$("#"+leftDivId).width(thSumWidth);
			$("#"+rightDivId).css("padding-left",thSumWidth-2);
			//内容高度设定
			$("#"+freeTableBodyId).find("tr").each(function(){
				var thisObj = $(this);
				var freetargetObj = $("#"+thisObj.attr("freetarget"));
				var outerHeigth_f = freetargetObj.outerHeight()||0;
				if(outerHeigth_f<freetargetObj.height()){
					outerHeigth_f = freetargetObj.height();
				}
				
				var outerHeigth_t = thisObj.outerHeight()||0;
				if(outerHeigth_t<thisObj.height()){
					outerHeigth_t = thisObj.height();
				}
				
				if(outerHeigth_t>outerHeigth_f){
					//freetargetObj.height(outerHeigth_t);
				}else{
					thisObj.height(outerHeigth_f);
				}
			});
			
		},
		setTableBody:function(self){//设置table内容
			try {
				var colModel = self.defaults.colModel,
					tbody = $(self.table).find("tbody");
					datas = self.defaults.traverseData,
					tableId = self.id,
					insterTdStr = "",
					trId = "";
				if(typeof datas == "string") {  datas = JSON.parse(datas);}
				tbody.empty();
				for(var i=0;i<datas.length;i++){
					trId = tableId+"_"+i+"_tr";
					if(self.defaults.isCheckbox==true||self.defaults.isCheckbox=="true"){
						self.defaults.line+=1;
						insterTdStr+="<td tag='ckline' trnumber='"+i+"' align='center' style='color:#428bca;' id='"+trId+"_ckline' tdIndex='"+tableId+"_ckline'><input type='checkbox' value='"+trId+"' name='"+tableId+"' /> </td>";
					}
					if(self.defaults.lineNumber==true||self.defaults.lineNumber=="true"){
						self.defaults.line+=1;
						insterTdStr+="<td tag='line' trnumber='"+i+"' align='center' style='color:#428bca;' id='"+trId+"_line' tdIndex='"+tableId+"_line'>"+self.defaults.line+"</td>";
					}
					for(var j=0;j<colModel.length;j++){
						var colObj = colModel[j],//列对象
							ishide = colObj.ishide || false,//是否隐藏列
							jsonKey = colObj.name,//jsonkey
							css = colObj.css || "",//css样式
							type = colObj.type || "text",//当前列内容属性 默认text（number）
							sort = colObj.sort || false,//默认不排序
							text = colObj.text || "left",//默认文本对齐方式
							width = colObj.width || "30",//默认宽度
							getCustomValue = colObj.getCustomValue;//自定义方法
							
						var tdIndex = tableId+"_"+jsonKey,//tdId
							value = datas[i][jsonKey],//实际值
							tdId = trId+"_"+jsonKey;
						if(value == 0){
							value = value + '';
						}else{
							value = value || '';
						}
						if(sort){
							self.defaults.sortColMode.push(jsonKey);
						}
						var tdHide = "";
						if(css!=""){css="class= '"+css+"'";}//列是否隐藏
						if(ishide){tdHide="hidden='true'";}else{tdHide="";}//列是否隐藏
						var stt = "<td tag='"+jsonKey+"' width='"+width+"'title='"+value+"' align='"+text+"' trnumber='"+i+"' "+css+" "+tdHide+" id='"+tdId+"' tdIndex='"+tdIndex+"'>"+value+"</td>";
						var resCustomObj = "",
							tdObj = $(stt),
							air = $.isFunction(getCustomValue) ? true : false;
						if(air){
							var rest = getCustomValue.call(this,tdObj,value);
							rest.prop("title",rest.text());
							if(rest){stt = rest[0].outerHTML}
						}
						insterTdStr+=stt;
					}
					insterTdStr = insterTdStr || '<td></td>';
					var trObj = $("<tr id='"+trId+"' >"+insterTdStr+"</tr>");
					//每行数据加载之前 loadEachBeforeEvent
					var loadEachBeforeEventFun = $.isFunction(self.defaults.loadEachBeforeEvent) ? true : false;
					if(loadEachBeforeEventFun){
						trObj = self.defaults.loadEachBeforeEvent.call(this,trObj) || trObj;
					}
					//方法调用todo
					tbody.append(trObj[0].outerHTML);//加载列数据
					insterTdStr="";
					// 每行数据加载后 loadEachAfterEvent
					var loadEachAfterEventFun = $.isFunction(self.defaults.loadEachAfterEvent) ? true : false;
					if(loadEachAfterEventFun){
						tbody = self.defaults.loadEachAfterEvent.call(this,tbody) || tbody;
					}
				}
				var loadCompleteFun = $.isFunction(self.defaults.loadCompleteEvent) ? true : false;
				if(loadCompleteFun){
					self = self.defaults.loadCompleteEvent.call(this,self)||self;
				}
				
			} catch (e){
				//记载异常时
				//加载数据失败时 loadErrorEvent
				var loadErrorEventFun = $.isFunction(self.defaults.loadErrorEvent) ? true : false;
				if(loadErrorEventFun){
					self.defaults.loadErrorEvent.call(this,e);
				}
			}
		},
		setTableFoot:function(self){//表格脚设置详细结构，包含初始化分页
			//创建脚结构
			var colModel = self.defaults.colModel,
				lt = colModel.length,
//				tfoot = $(self.table).find("tfoot");
				tfoot = $(self.zgGridFootBox);
			if(self.defaults.isCheckbox){lt=colModel.length+1;}//如果需要序号，则在合并一列，否则默认列数
			if(self.defaults.lineNumber){lt=colModel.length+1;}//如果需要序号，则在合并一列，否则默认列数
			tfoot.append("<div class='zgGrid-tfoot-td' colspan='"+lt+"' id='"+self.id+"_tfoot_box'></div>");
		},
		buttonGroup:function(self){//附加按钮设定
			var buttonGroup = self.defaults.buttonGroup,
				ulId = self.id+"_but_box";
			//判断是否有附加按钮设定
			if(buttonGroup.length>0){
//				var tfoot = $(self.table).find("tfoot").find("td");
				var tfoot = $(self.zgGridFootBox);
				tfoot.append('<div class="zgGrid-but-box" id="'+ulId+'"></div>');
				var tfootButDiv = tfoot.find("#"+ulId);
				for(var i=0;i<buttonGroup.length;i++){
					var butObj = buttonGroup[i];
					var butType = butObj.butType||"add",
						butId = butObj.butId||"but_"+Math.floor(Math.random()*10),
						butText = butObj.butText||"未知名按钮",
						butSize = butObj.butSize||"sm",
						customProperty = butObj.customProperty||"",
						butHtml = butObj.butHtml||"";
					var isCustom = false;
					var butIcon = '',
						butBackColor = '';
					// 按钮图标,和按钮颜色
					switch(butType)
					{
					case "add":
						butIcon = ' glyphicon glyphicon-plus ';
						butBackColor = ' btn-primary ';
					  break;
					case "delete":
						butIcon = ' glyphicon glyphicon-trash ';
						butBackColor = ' btn-danger ';
					  break;
					case "update":
						butIcon = ' glyphicon glyphicon-edit ';
						butBackColor = ' btn-primary ';
						break;
					case "view":
						butIcon = ' glyphicon glyphicon-list-alt ';
						butBackColor = ' btn-info ';
						break;
					case "upload":
						butIcon = ' glyphicon glyphicon-open-file ';
						butBackColor = ' btn-primary ';
						break;
					case "download":
						butIcon = ' glyphicon glyphicon-save-file ';
						butBackColor = ' btn-primary ';
						break;
					case "review":
						butIcon = ' glyphicon glyphicon-paperclip ';
						butBackColor = ' btn-warning ';
						break;
					case "custom":
						isCustom = true;
						break;
					default:
						butIcon = '';
						butBackColor = '';
					}
					//按钮大小
					switch(butSize)
					{
					case "lg":
						butSize = ' btn-lg ';
					  break;
					case "sm":
						butSize = ' btn-sm ';
					  break;
					case "xs":
						butSize = ' btn-xs ';
						break;
					default:
						butSize = '';
					}
					var butStr = '';
					if(isCustom){
						//是自定义
						butStr = butHtml+" ";
					}else{
						//不是自定义
						butStr = '<button type="button" '+customProperty+' id="'+butId+'" class="btn '+butBackColor+' '+butSize+'"><span class="'+butIcon+'"></span> '+butText+'</button>';
					}
					
					tfootButDiv.append(butStr);
				}
			}
			
		},
		pagingSet:function(self){//分页设定
			//判断是否需要分页
			if(self.defaults.isPage==true||self.defaults.isPage=="true"){
				var container = $("#"+self.id+"_tfoot_box"),//容器
				 	rowPerPage = self.defaults.rowPerPage,//每页条数
				 	rowPerPageGroup = self.defaults.rowPerPageGroup,//每页条数组
					records = self.defaults.records,//总数据量
					total = self.defaults.total,//总页数
					page = self.defaults.page||1,// 当前页
					pageBoxId = self.id+"_tfoot_box_page",
					butUiBox = '',//分页主结构
					selectPage='',//下拉分页结构
					butMostLeft = '<a pageto="ml" class="zg-page-but" href="javascript:void(0)"><span class="glyphicon glyphicon-fast-backward"></span></a>',//首页
					butLeft = '<a pageto="l" class="zg-page-but" href="javascript:void(0)"><span class="glyphicon glyphicon-chevron-left"></span></a>',//往左
					butMostRight = '<a pageto="mr" class="zg-page-but" href="javascript:void(0)"><span class="glyphicon glyphicon-fast-forward"></span></a>',//尾页
					butRight = '<a pageto="r" class="zg-page-but" href="javascript:void(0)"><span class="glyphicon glyphicon-chevron-right"></span></a>',//往右
					butGo = '<a pageto="go" class="zg-page-but zg-page-go" href="javascript:void(0)">GO</a>',
					sum = '<span class="zg-page-records" >total <span id="records" >0</span> items, </span><span class="zg-page-sum" >total <span id="page_sum" >1</span> page</span>',
					searchInput='<input value="1" id="'+pageBoxId+'_search" class="zg-page-search zg-page-input" />';
				if(rowPerPageGroup.length>0){
					var opts = '';
					for(var i=0;i<rowPerPageGroup.length;i++){
						opts+='<option value="'+rowPerPageGroup[i]+'">'+rowPerPageGroup[i]+'bar</option>';
					}
					selectPage = '<span class="zgGrid-row-group-span">each page</span><select class="zg-page-select-page" id="">'+opts+'</select>';
				}
				//结构生成
				butUiBox = '<div class="zgGrid-pages-box" id="'+pageBoxId+'" >'+
								butMostLeft+
								butLeft+
								searchInput+
								butGo+
								butRight+
								butMostRight+
								selectPage+
								sum+
							'</div>';
				container.append(butUiBox);
			}
		},
		pagingSetButCilck:function(self){//设定分页按钮事件
			if(self.defaults.isPage==true||self.defaults.isPage=="true"){
				var pageBoxId = self.id+"_tfoot_box_page";
				var pageBox = $("#"+pageBoxId);
				$(pageBox).on("keydown","input[class*='zg-page-search']",function(e){
					if(e.keyCode==13){
						if(self.eventstatus){
							self.eventstatus = false;
						}else{
							return;
						}
						
						var thisVal = $(this).val();
						var toPage = 1;
						if(thisVal>=1&&thisVal<=self.defaults.total){
							toPage = thisVal;
						}
						self.defaults.line = (self.defaults.page-1)*self.defaults.rowPerPage;
						self.defaults.page = toPage;
						$(self).loadDataInternal(null);//加载数据
					}
				});
				$(pageBox).on("change","select",function(){
					var pageRrow = $(this).val();
					self.defaults.page = 1;
					self.defaults.rowPerPage = pageRrow;
					$(self).loadDataInternal(null);//加载数据
				});
				$(pageBox).on("click","a",function(){
					if(self.eventstatus){
						self.eventstatus = false;
					}else{
						return;
					}
					var pageto = $(this).attr("pageto");
					var toPage = self.defaults.page;
					switch(pageto)
					{
					case "ml":
						//页头
						toPage = 1;
					  break;
					case "l":
						//向左
						var p = self.defaults.page-1;
						if(p>0){
							toPage = p;
						}else{
							toPage = 1;
						}
						break;
					case "mr":
						//最右
						toPage = self.defaults.total;
						break;
					case "r":
						var p = self.defaults.page+1;
						if(p>self.defaults.total){
							toPage = self.defaults.total;
						}else{
							toPage = p;
						}
						break;
					case "go":
						var obj = $("#"+pageBoxId+"_search");
						if(obj.val()>=1&&obj.val()<=self.defaults.total){
							toPage = obj.val();
						}else{
							toPage = 1;
						}
						break;
					default:
						toPage = 1;
					}
					self.defaults.line = (self.defaults.page-1)*self.defaults.rowPerPage;
					self.defaults.page = toPage;
					$(self).loadDataInternal(null);//加载数据
				});
			}
		},
		pagingSetVal:function(self){//数据加载
			var pageBoxId = self.id+"_tfoot_box_page";
			var pageBox = $("#"+pageBoxId);
			pageBox.find("#page_sum").text(self.defaults.total);
			pageBox.find("#records").text(self.defaults.records);
			pageBox.find(".zg-page-input").val(self.defaults.page);
		},
		compareInt:function(prop,sord){//number类型排序
			return function(a,b){
				var value1 = a[prop];
				var value2 = b[prop];
				var rest = 0;
				if(sord=="desc"){
					rest = value2 - value1;
				}else{
					rest = value1 - value2;
				}
				return rest;
			}
		},
		compareString:function(prop,sord){//字符串类型排序
			return function(a,b){
				var value1 = a[prop];
				var value2 = b[prop];
				var rest = 0;
				if(sord=="desc"){
					rest = value2.localeCompare( value1 )
				}else{
					rest = value1.localeCompare( value2 )
				}
				
				return rest;
			}
		},
		sotrTable:function(obj,self){
			self.defaults.line=0;
			//-------排序处理--------------------
			//首先判断当前对象是否为本地排序，如果是则使用data对象，否则使用ajax方式
			
			var data = [],//数据
				sidx = $(obj).attr("sortindex"),//排序列
				sord = $(obj).attr("sord"),//排序列
				valuetype = $(obj).attr("valuetype")||"text", //文本类型
				sord = sord || "asc", //默认标题排序方式
				url=self.defaults.url  || "",//ajaxUrl
				localSort=self.defaults.localSort || false;//当前对象的排序数据获取方式，false：ajax，true：本地
			if(sord=="desc"){sord = "asc"}else{sord = "desc"}
			
			if(url!=null&&url!=""){
				
				if(localSort==true||localSort=="true"){//如果是本地排序
					data = self.defaults.traverseData;
					var sortData=[];
					if(valuetype=="number"||valuetype=="int"){
						sortData = data.sort($.zgGrid.compareInt(sidx,sord));
					}else if(valuetype=="text"){
						sortData = data.sort($.zgGrid.compareString(sidx,sord));
					}
					self.defaults.traverseData = sortData;
					$(self.zgGridBodyBox).find("thead a[sortindex='"+sidx+"']").attr("sord",sord);//$(obj).attr("sord",sord);
				}else{
					//ajax方式排序
					self.defaults.sidx = sidx;
					if(self.defaults.sord=="desc"){sord = "asc"}else{sord = "desc"}
					self.defaults.sord = sord;	
					$.zgGrid.loadTableData(self);//数据处理
					$(self.zgGridBodyBox).find("thead a[sortindex='"+sidx+"']").attr("sord",sord);//$(obj).attr("sord",sord);
				}
				$.zgGrid.setTableBody(self);//表格内容
			}else{
				data = self.defaults.data;
				//本地数据
				var sortData=[];
				if(valuetype=="number"){
					sortData = data.sort($.zgGrid.compareInt(sidx,sord));
				}else if(valuetype=="text"){
					sortData = data.sort($.zgGrid.compareString(sidx,sord));
				}
				self.defaults.data = sortData;
				$(self.zgGridBodyBox).find("thead a[sortindex='"+sidx+"']").attr("sord",sord);//$(obj).attr("sord",sord);
				$.zgGrid.loadTableData(self);//数据处理
				$.zgGrid.setTableBody(self);//表格内容
			}
			this.setFreeze(self);//设定冻结列
		},
		traverseList:function(start,end,data){//数组截取筛选数据（起始位置，结束位置）
			var jsonStr = null;
			if(typeof data == "string") {  data = JSON.parse(data);}
			jsonStr = data.slice(start,end);
			return jsonStr;
		},
		tableTrBackageHover:function(self){
			//阻止右键系统菜单的弹出
			// $(self.table).find("tbody").find("tr").bind("contextmenu",function(e){
			// 	return false;
			// });
			//是否有冻结列的标记
			var freeze = self.defaults.freezeIndex;//>=0 视为存在冻结列
			$(self.table).find("tbody").on('click','td', function (e) {
				var thisObj = $(this),
					thisParent = $(this).parent();
				if(3 == e.which){ //鼠标右键
					thisParent.addClass("info").siblings().removeClass("info");
					var eachTrRightclickFun = $.isFunction(self.defaults.eachTrRightclick) ? true : false;
					if(eachTrRightclickFun){
						self.defaults.eachTrRightclick.call(this,thisParent,thisObj);
					}
					if(freeze>=0){
						var freeToTr = thisParent.prop("id");
						$("#"+freeToTr+"_free").addClass("info").siblings().removeClass("info");
					}
				}else if(1 == e.which){ //鼠标左键
					if(self.defaults.isCheckbox==true||self.defaults.isCheckbox=="true"){
						if(thisParent.hasClass("info")){
							thisParent.removeClass("info");
							thisParent.find("input[type='checkbox']").prop("checked",false);
						}else{
							thisParent.addClass("info");
							thisParent.find("input[type='checkbox']").prop("checked",true);
						}
					}else{
						thisParent.addClass("info").siblings().removeClass("info");
					}
					if(freeze>=0){
						var freeToTr = thisParent.prop("id");
						if(self.defaults.isCheckbox==true||self.defaults.isCheckbox=="true"){
							if($("#"+freeToTr+"_free").hasClass("info")){
								$("#"+freeToTr+"_free").removeClass("info");
							}else{
								$("#"+freeToTr+"_free").addClass("info");
							}
						}else{
							$("#"+freeToTr+"_free").addClass("info").siblings().removeClass("info");
						}
					}
					$(".zgGrid-modal").hide();
					var eachTrClickFun = $.isFunction(self.defaults.eachTrClick) ? true : false;
					if(eachTrClickFun){
						self.defaults.eachTrClick.call(this,thisParent,thisObj);
					}
				} 
			});		
			//存在冻结列时，挂载冻结列的事件，
			if(freeze>=0){
				$(self.zgGridLeftBox).on('click','td', function (e) {
					var thisObj = $(this);
						thisParent = $(this).parent();
					if(3 == e.which){ //鼠标右键
						thisParent.addClass("info").siblings().removeClass("info");
						$("#"+thisParent.attr("freetarget")).addClass("info").siblings().removeClass("info");
						var eachTrRightclickFun = $.isFunction(self.defaults.eachTrRightclick) ? true : false;
						if(eachTrRightclickFun){
							self.defaults.eachTrRightclick.call(this,$("#"+thisParent.attr("freetarget")),thisObj);//传入的是 住table的行和当前冻结部分的td
						}
					}else if(1 == e.which){ //鼠标左键
						if(self.defaults.isCheckbox==true||self.defaults.isCheckbox=="true"){
							if($("#"+thisParent+"_free").hasClass("info")){
								thisParent.removeClass("info");
								$("#"+thisParent.attr("freetarget")).removeClass("info");
								thisParent.find("input[type='checkbox']").prop("checked",false);
							}else{
								thisParent.addClass("info");
								$("#"+thisParent.attr("freetarget")).addClass("info");
								thisParent.find("input[type='checkbox']").prop("checked",true);
							}
						}else{
							thisParent.addClass("info").siblings().removeClass("info");
							$("#"+thisParent.attr("freetarget")).addClass("info").siblings().removeClass("info");
						}
						$(".zgGrid-modal").hide();
						var eachTrClickFun = $.isFunction(self.defaults.eachTrClick) ? true : false;
						if(eachTrClickFun){
							self.defaults.eachTrClick.call(this,$("#"+thisParent.attr("freetarget")),thisObj);
						}
					} 
				});	
			}
		},
		loadTableData:function(self){//加载数据，
			var url = self.defaults.url,//访问路径
				param = self.defaults.param,//其他参数
				rowPerPage = self.defaults.rowPerPage,//每页条数
				records = self.defaults.records,//总数据量
				total = self.defaults.total,//总页数
				page = self.defaults.page||1,// 当前页
				sidx = self.defaults.sidx,// 排序列
				sord = self.defaults.sord,// 排序方式 asx / desc
				datas = self.defaults.data,
				ajaxRestData = null;
			if(typeof datas == "string") { datas = JSON.parse(datas);}
			//首先确认是否是有默认数据传入，如果有则使用默认数据，忽略ajax，方式
			if(datas!=null&&typeof datas=="object"&&datas.length>=0&&url==null){
				//计算得出总页数，总数据量，设定当前页面，设定需要遍历的数据
				self.defaults.records = datas.length//总数据量
				self.defaults.data = datas;//数据
				self.defaults.total = Math.ceil(self.defaults.records/self.defaults.rowPerPage);//总页数=数据量/每页条数，存在余数+1；
				//var arr = [1,2,3,4,5,6,7,8,9];alert(arr.slice(0,2));//1,2
				var start = (self.defaults.page-1)*self.defaults.rowPerPage,
					end = self.defaults.page*self.defaults.rowPerPage;
				self.defaults.traverseData = $.zgGrid.traverseList(start,end,datas);//需要遍历的数据
			}else{
				//如果没有默认参数，则判断url是否存在，如果存在url，进行ajax取值等操作，如果不存在，则无视，并初始化
				if(url!=null&&url!=""){
					var ajaxRestObj = $.zgGrid.toAjax(url,page,rowPerPage,sidx,sord,param,self.defaults,self.thisLodingText,self.thisLodingProgressbar);
					if(ajaxRestObj!=null){
						//赋值
						self.defaults.records = ajaxRestObj.records//总数据量
						self.defaults.total = ajaxRestObj.total;//总页数
						self.defaults.page = ajaxRestObj.page||1;// 当前页
						//self.defaults.data = ajaxRestObj.rows;//数据源
						self.defaults.traverseData = ajaxRestObj.rows;//数据
					}
				}
			}
		},
		toAjax:function(url,page,rowPerPage,sidx,sord,param,defaults,thisLodingText,thisLodingProgressbar){//ajax方法
			var restObj = null;
			var getData = "page="+page+"&rows="+rowPerPage+"&sidx="+sidx+"&sord="+sord;
			if(param!=null&&param!=""){
				if(param.charAt(0)!="&"){param="&"+param;}
				getData+=param;
			}
			var ajaxBeforeSendFun = $.isFunction(defaults.ajaxBeforeSend) ? true : false;
			if(ajaxBeforeSendFun){
				getData = defaults.ajaxBeforeSend.call(this,getData)||getData;
			}
			thisLodingText.text("loading ... ...");
			thisLodingProgressbar.addClass("active").removeClass("progress-bar-danger progress-bar-success");
			$.ajax({
				url: url,
				cache: false,
				async: false,
				dataType:'json',
				data:encodeURI(getData),
				success:function(data){
					restObj = data;
					var ajaxSuccessFun = $.isFunction(defaults.ajaxSuccess) ? true : false;
					if(ajaxSuccessFun){
						restObj = defaults.ajaxSuccess.call(this,restObj)||restObj;
					}
				},
				error:function(xhr,st,err){
					if($.isFunction(defaults.loadError)) { defaults.loadError.call(this,xhr,st,err); }
					xhr=null;
					//thisLodingText,thisLodingProgressbar text-success
					thisLodingText.text("load failed！");
					thisLodingProgressbar.removeClass("active").addClass("progress-bar-danger");
				},
				beforeSend: function(xhr){
					if(!typeof(xhr.responseJSON)=="undefined"){
						if($.isFunction(defaults.loadBeforeSend)) { defaults.loadBeforeSend.call(this,xhr); }
					}
				},
				complete:function(xhr,status){
					if(!typeof(xhr.responseJSON)=="undefined"){
						if($.isFunction(defaults.loadComplete)) { defaults.loadComplete.call(this,xhr,status); }
					}
				}
			});
			return restObj
		},
		tableStyle:function(self){
			//表格风格设定
			//此方法，用于当table发生了结构等变化时，进行优化渲染（如：表格宽度被调整）
			//1.验证当前表格宽度是否可以将当前表格的所有结构都现实出来，如果不能，则考虑将部分混乱的结构隐藏
			var tableObj = $(self.table),
				tableObjFootUl = tableObj.find("tfoot").find("ul");
			if(tableObj.width()<=500){
				//小于500
				tableObjFootUl.find("li[class*='zgGrid-page-go']").hide();
			}else{
				tableObjFootUl.find("li[class*='zgGrid-page-go']").show();
			}
		},
		removeTable:function(self){//清理table
			
			$(self.table).removeClass("table-responsive zgrid").empty();
			return null;
		},
		
		extend: function(methods) {
			$.extend($.fn.zgGrid,methods);
			if (!this.no_legacy_api) {
				$.fn.extend(methods);
			}
		}
	});
	// 对Date的扩展，将 Date 转化为指定格式的String
	// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
	// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
	// 例子： 
	// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
	// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
	Date.prototype.Format = function (fmt) { //author: meizz 
		var o = {
			"M+": this.getMonth() + 1, //月份 
			"d+": this.getDate(), //日 
			"h+": this.getHours(), //小时 
			"m+": this.getMinutes(), //分 
			"s+": this.getSeconds(), //秒 
			"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
			"S": this.getMilliseconds() //毫秒 
		};
		if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	}
	//扩展方法，外部方法，外部调用，初始化顺序 2   
	$.zgGrid.extend({			
		setTableMainTitle:function(title){//设定表格主题头内容
			return this.each(function (){
				var $t = this;
				$($t).find("caption").html(title);
			});
		},
		setInitTableHeight:function(self){//初始化时设定表高度
			var height = self.defaults.height;
			height = height||"";
			if(height!=""){
				self.zgGridBodyBox.height(height);
			}
		},
		setInitTableWidth:function(self){//初始化时设定表宽度
			var width = self.defaults.width,
				tableObj = $(self.table);
				
			if(width=="auto"){//自动表示 内容多宽 表格就多宽
				var thSumWidth = 0;
				tableObj.find("thead").find("th:not([hidden])[class!='freeHide']").each(function(index){
					var thisObj = $(this);
					thSumWidth+= Number(thisObj.prop("width")-2 || thisObj.width()-2);
				});
				tableObj.css("width",thSumWidth);
				tableObj.parent().css("width",thSumWidth);
			}else if(width=="max"){//父级宽度
				tableObj.css("width","100%");
				tableObj.parent().css("width","100%");
			}else{
				tableObj.css("width",width);
				tableObj.parent().css("width",width);
			}
			$.zgGrid.tableStyle(self);//表格风格
		},
		setTableWidth:function(width){//直接设定表格宽度
			return this.each(function (){
				var table = $(this.table);
				$(table).css("width",width);
				$.zgGrid.tableStyle(this);//表格风格
			});
		},
		loadData:function(data){//数据加载
			return this.each(function (){
				var self = this;
				if(self.eventstatus){
					self.eventstatus = false;
				}else{
					return;
				}
				 $(self).loadDataInternal(data);
			});
		},
		loadDataInternal:function(data){//数据加载
			return this.each(function (){
				var self = this;
				$(self).zgGridMask(self,function(){
					$.zgGrid.loadTableData(self);//数据处理
					//加载数据之前 loadBeforeEvent
					var loadBeforeFun = $.isFunction(self.defaults.loadBeforeEvent) ? true : false;
					if(loadBeforeFun){
						self = self.defaults.loadBeforeEvent.call(this,self)|| self;
					}
					$.zgGrid.setTableBody(self);//表格内容
					$.zgGrid.pagingSetVal(self);//表格内容
					$(self.table).find("thead").find("input[type='checkbox']").prop("checked",false);
					$.zgGrid.setFreeze(self);//设定冻结列
				});
			});
		},
		//设定属性
		setting:function(property,method){
			return this.each(function (){
				var self = this;
				 switch (property) {
	                 case ("title"):
	                     //标题设定
	                	 self.defaults.title = method;
	                     break;
	                 case ("lineNumber"):
	                	//是否有行序号
	                	 self.defaults.lineNumber = method;
	                     break;
	                 case ("isCheckbox"):
	                	 //是否有多选列
	                	 self.defaults.isCheckbox = method;
	                 break;
	                 case ("width"):
	                	 self.defaults.width = method;
	                     break;
	                 case ("url"):
	                	 //ajax-url
	                	 self.defaults.url = method;
	                	 break;
	                 case ("param"):
	                	 //ajax 其他参数 由&起始
	                	 self.defaults.param = method;
	                	 break;
	                 case ("sidx")://排序字段
	                	 self.defaults.sidx = method;
	                	 break;
	                 case ("sord")://升降序
	                	 self.defaults.sord = method;
	                	 break;
	                 case ("localSort")://本地排序(true，既：使用当前页面数据进行排序)。默认关闭
	                	 self.defaults.localSort = method;
	                	 break;
	                 case ("data")://预设数据
	                	 self.defaults.data = method;
	                	 break;
	                 case ("rowPerPage")://页面多少条数据
	                	 self.defaults.rowPerPage = method;
	                	 break;
	                 case ("page")://当前页
	                	 self.defaults.page = method;
	                	 break;
	                 case ("isPage")://是否使用分页(默认使用)
	                	 self.defaults.isPage = method;
	                	 break;
	                 case ("loadBeforeEvent")://加载数据之前
	                	 self.defaults.loadBeforeEvent = method;
	                	 break;
	                 case ("loadCompleteEvent")://加载完成之后
	                	 self.defaults.loadCompleteEvent = method;
	                	 break;
	                 case ("loadErrorEvent")://加载失败之后
	                	 self.defaults.loadErrorEvent = method;
	                	 break;
	                 case ("loadEachBeforeEvent")://每行数据加载之前
	                	 self.defaults.loadEachBeforeEvent = method;
	                	 break;
	                 case ("loadEachAfterEvent")://每行数据加载之后
	                	 self.defaults.loadEachAfterEvent = method;
	                	 break;
	                 case ("loadBeforeSend")://ajax：beforeSend调用
	                	 self.defaults.loadBeforeSend = method;
	                	 break;
	                 case ("loadError")://ajax：error 调用
	                	 self.defaults.loadError = method;
	                 break;
	                 case ("ajaxSuccess")://执行ajax时成功后调用
	                	 self.defaults.ajaxSuccess = method;
	                	 break;
	                 case ("eachTrClick")://每行点击事件
	                	 self.defaults.eachTrClick = method;
	                	 break;
	                 case ("buttonGroup")://附加按钮
	                	 self.defaults.buttonGroup = method;
	                	 break;
	                 default:
	                    break;
	             }
			});
		},
		//取得属性
		getting:function(property){
			var ret = null;
			this.each(function (){
				var self = this;
				switch (property) {
				case ("title"):
					//标题设定
					ret = self.defaults.title;
				break;
				case ("lineNumber"):
					//是否有行序号
					ret = self.defaults.lineNumber;
				break;
				case ("isCheckbox"):
					//是否有行序号
					ret = self.defaults.isCheckbox;
				break;
				case ("width"):
					ret = self.defaults.width;
				break;
				case ("url"):
					//ajax-url
					ret = self.defaults.url;
				break;
				case ("param"):
					//ajax 其他参数 由&起始
					ret = self.defaults.param;
				break;
				case ("sidx")://排序字段
					ret = self.defaults.sidx;
				break;
				case ("sord")://升降序
					ret = self.defaults.sord;
				break;
				case ("localSort")://本地排序(true，既：使用当前页面数据进行排序)。默认关闭
					ret = self.defaults.localSort;
				break;
				case ("data")://预设数据
					ret = self.defaults.data;
				break;
				case ("rowPerPage")://页面多少条数据
					ret = self.defaults.rowPerPage;
				break;
				case ("page")://当前页
					ret = self.defaults.page;
				break;
				case ("isPage")://是否使用分页(默认使用)
					ret = self.defaults.isPage;
				break;
				case ("loadBeforeEvent")://加载数据之前
					ret = self.defaults.loadBeforeEvent;
				break;
				case ("loadCompleteEvent")://加载完成之后
					ret = self.defaults.loadCompleteEvent;
				break;
				case ("loadErrorEvent")://加载失败之后
					ret = self.defaults.loadErrorEvent;
				break;
				case ("loadEachBeforeEvent")://每行数据加载之前
					ret = self.defaults.loadEachBeforeEvent;
				break;
				case ("loadEachAfterEvent")://每行数据加载之后
					ret = self.defaults.loadEachAfterEvent;
				break;
				case ("loadBeforeSend")://ajax：beforeSend调用
					ret = self.defaults.loadBeforeSend;
				break;
				case ("loadError")://ajax：error 调用
					ret = self.defaults.loadError;
				break;
				case ("ajaxSuccess")://执行ajax时成功后调用
					ret = self.defaults.ajaxSuccess;
				break;
				case ("eachTrClick")://每行点击事件
					ret = self.defaults.eachTrClick;
				break;
				case ("buttonGroup")://附加按钮
					ret = self.defaults.buttonGroup;
				break;
				default:
					break;
				}
			});
			return ret;
		},
		setParam:function(param){//动态设定参数
			return this.each(function (){
				var self = this;
				self.defaults.param = param;
				return self;
			});
		},
		zgGridMask:function(self,startFun){//遮罩 
			var maskObj = self.zgGridLoading;
			var dfd = $.Deferred();
			dfd.done(maskObj.fadeIn(100,function(){
				$.isFunction(startFun) ? startFun.call(this,self) : null
			}))
			.done(maskObj.fadeOut(500,function(){
				self.eventstatus = true;
			}));
		},
		destructionTable:function(){//刷新表格
			this.each(function (){
				return $.zgGrid.removeTable(this);//表格风格
			});
			
		},
		setColNames:function(colName,nameVal){//改变列标题名称
			return this.each(function (){
				var self = this,//当前对象
					thisId = self.id;
				var thindex = thisId+"_"+colName;
				var theadId = thisId+"_thead";
				var ths = $("#"+theadId+" [thindex='"+thindex+"']" );
				$.each(ths,function(ob){
					var obj = $(this),objDom = this;
					if(objDom.tagName.toUpperCase()=="A"){
						obj.find("[class*='zGgridSotrText']").html(nameVal);
					}else if(objDom.tagName.toUpperCase()=="TH"){
						obj.html(nameVal);
					}
				});
				return self;
			});
		},
		zgGridModal:function(zgGridModal,trObj){
			zgGridModal.fadeOut(100,function(){
				var x = $(trObj).offset().top+trObj.height()+10;
				var y = $(this).offset().left+50;
				zgGridModal.css({"top":x,"left":y}).fadeIn(700);
			});
		},
		getSelectColValue:function(colNames,colNamesS){//取得选择行的所有列值（包括隐藏列）（id,name,ss,...,..,..）
			var restList =[],seltObj=null;
			if(typeof colNames == "object"){
				seltObj = colNames;
				colNames = colNamesS;
			}
			if(colNames!=""){
				if(colNames.indexOf("line")<=0){colNames="line,"+colNames;}
				colNames = colNames.split(",");
				this.each(function (){
					var self = this,//当前对象
						thisId = self.id;
						tbodyId = thisId+"_tbody",
						trs=null;
					if(seltObj!=null){
						trs = seltObj;
					}else{
						trs = $("#"+tbodyId).find("tr[class*='warning']");
					}	
					trs.each(function(){
						var thisTrObj = $(this);
						var trid = thisTrObj.attr("id");
						var oneTrVal = "";
						
						if(colNames!=null&&colNames!=""&&colNames.length>0){//筛选取
							for(var i=0;i<colNames.length;i++){
								if(oneTrVal!=""){oneTrVal+=",";}
								var tdId =trid +"_"+colNames[i];
								oneTrVal+= "\""+colNames[i]+"\":"+"\""+$.trim($("#"+tdId).text())+"\"";
							}
						}
						restList.push("{"+oneTrVal+"}");
					});
				});
			}
		    return $.parseJSON(restList);
		},
		getCheckboxTrs:function(){
			var restList = [];
			this.each(function (){
				var self = this;
				restList = new Array();
				if(self.defaults.isCheckbox){
					var tbody = $(self.table).find("tbody");
					var checkboxs = tbody.find("input[type='checkbox']:checked");
					$.each(checkboxs,function(index,node){
						var trid = $(this).val();
						restList.push($("#"+trid));
					});
				}
				return restList;
			});
			return restList;
		},
		hideColumn:function(column){// 隐藏列column="tdname1,tdname2,tdname3"
			return this.each(function (){
				var self = this;
				var cols = column.split(",");
				var thead = $(self.table).find("thead");
				var tbody = $(self.table).find("tbody");
				var currentTableWidth = 0;
				$.each(cols,function(index,node){
					thead.find("th").each(function(){
						var thTag = $(this).attr("tag");
						if(node==thTag){
							$(this).prop("hidden",true);
						}
					});
					tbody.find("td").each(function(){
						var thTag = $(this).attr("tag");
						if(node==thTag){
							$(this).prop("hidden",true);
						}
					});
				});
				if(self.defaults.freezeIndex>-1){// 如果存在冻结列
					var freeze_th = $(self.zgGridLeftBox).find("th");
					var freeze_td = $(self.zgGridLeftBox).find("td");
					$.each(freeze_th,function(index,node){
						var thTag = $(this).attr("tag");
						if($.inArray(thTag,self.defaults.freezeCol)>-1){
							$(this).prop("hidden",true);
						}
					});
					$.each(freeze_td,function(index,node){
						var tdTag = $(this).attr("tag");
						if($.inArray(tdTag,self.defaults.freezeCol)>-1){
							$(this).prop("hidden",true);
						}
					});
				}
				$(self).setInitTableWidth(self);//设定表宽度
			
				return self;
			});
		},
		showColumn:function(column){//显示列
			return this.each(function (){
				var self = this;
				var cols = column.split(",");
				var thead = $(self.table).find("thead");
				var tbody = $(self.table).find("tbody");
				$.each(cols,function(index,node){
					thead.find("th").each(function(){
						var thTag = $(this).attr("tag");
						if(node==thTag){
							$(this).prop("hidden",false);
						}
					});
					tbody.find("td").each(function(){
						var thTag = $(this).attr("tag");
						if(node==thTag){
							$(this).prop("hidden",false);
						}
					});
				});
				if(self.defaults.freezeIndex>-1){// 如果存在冻结列
					var freeze_th = $(self.zgGridLeftBox).find("th");
					var freeze_td = $(self.zgGridLeftBox).find("td");
					$.each(freeze_th,function(index,node){
						var thTag = $(this).attr("tag");
						if($.inArray(thTag,self.defaults.freezeCol)>-1){
							$(this).prop("hidden",false);
						}
					});
					$.each(freeze_td,function(index,node){
						var tdTag = $(this).attr("tag");
						if($.inArray(tdTag,self.defaults.freezeCol)>-1){
							$(this).prop("hidden",false);
						}
					});
				}
				$(self).setInitTableWidth(self);//设定表宽度
			
				return self;
			});
		}
		
   });
	//初始化 最后执行，初始化顺序 3
	$.fn.zgGrid = function( options ) {
		return this.each( function() {
			//this.tableId = $(this.table).attr("id");
			var self = this;
			var defaults = {
				title:"",//表格标题
				lineNumber:false,//是否有行序号
				width:"max",
				height:"",
				url:"",//ajax-url
				param:"",//ajax 其他参数 由&起始
				
				sidx:"id",//排序字段
				sord:"desc",//升降序
				localSort:false,//本地排序(true，既：使用当前页面数据进行排序)。默认关闭
				
				colNames:[],//"ID,XXXXID,邮件,名称",
				colModel:[],//,
				data:[],//预设数据
				
				rowPerPage:10,//页面多少条数据
				rowPerPageGroup:[],//存在该内容后则会出现每页多少条的下拉项:[10,20,30]，并且：rowPerPage属性失效，使用数组中的第一个做为预定值，
				page:1,//当前页
				isPage:true,//是否使用分页(默认使用)
				isCheckbox:true,//是否启动多选列（true，会默认将在该表格之前增加多选一列）
				
				loadBeforeEvent:null,//加载数据之前
				loadCompleteEvent:null,//加载完成之后
				loadErrorEvent:null,//加载失败之后
				loadEachBeforeEvent:null,//每行数据加载之前
				loadEachAfterEvent:null,//每行数据加载之后
				loadBeforeSend:null,//ajax：beforeSend调用
				loadError:null,//ajax：error 调用
				ajaxBeforeSend:null,//执行ajax时调用
				ajaxSuccess:null,//执行ajax时成功后调用
				eachTrClick:null,//每行点击事件
				loadComplete:null,//请求完成时运行的函数（在请求成功或失败之后均调用，即在 success 和 error 函数之后）。
				buttonGroup:[],//附加按钮
				
				sortColMode:[],//可以有的排序列(内部使用)
				records:0,//总数据量(内部使用)
				total:1,//总页数(内部使用)
				line:0,//行序号起始序号(内部使用)
				traverseData:[],//遍历用数据(内部使用)
				freezeCol:[],//冻结列名称(内部使用)
				freezeColTable:null,//冻结对象
				freezeIndex:-1,//默认是-1 没有冻结，若值为>=0， 则视为从td下标为开始0——>N 均是冻结列，不可跳跃冻结,注意：初始化时使用的隐藏列不包含
				kernel:""//浏览器内核 (内部使用)
			};
			
			var explorer =navigator.userAgent ;
			var kernel = "";
			//ie 
			if (explorer.indexOf("MSIE") >= 0) {
				kernel = "ie";
			}
			//firefox 
			else if (explorer.indexOf("Firefox") >= 0) {
				kernel= "Firefox";
			}
			//Chrome
			else if(explorer.indexOf("Chrome") >= 0){
				kernel= "Chrome";
			}
			//Opera
			else if(explorer.indexOf("Opera") >= 0){
				kernel= "Opera";
			}
			//Safari
			else if(explorer.indexOf("Safari") >= 0){
				kernel= "Safari";
			} 
			//Netscape
			else if(explorer.indexOf("Netscape")>= 0) { 
				kernel= 'Netscape'; 
			}else{
				kernel= 'other'; 
			} 
			defaults.kernel = kernel;//设定浏览器内核
			self.defaults = $.extend(false,defaults, options);//合并参数
			if(self.defaults.rowPerPageGroup.length>0){
				self.defaults.rowPerPage=self.defaults.rowPerPageGroup[0];
			}
			$.zgGrid.setTableContainer(self);//生成表格结构
			//开启遮罩-----------
			//$(self).zgGridMask(self,function(){
				$.zgGrid.loadTableData(self);//数据处理
				//加载数据之前 loadBeforeEvent
				var loadBeforeFun = $.isFunction(self.defaults.loadBeforeEvent) ? true : false;
				if(loadBeforeFun){
					self = self.defaults.loadBeforeEvent.call(this,self)|| self;
				}
				$.zgGrid.setTableBody(self);//表格内容
				$.zgGrid.setTableTh(self);
				$.zgGrid.setTableFoot(self);//表格脚创建结构
				$.zgGrid.pagingSet(self);//设定分页
				$.zgGrid.pagingSetButCilck(self);//设定分页按钮事件
				$.zgGrid.pagingSetVal(self);//设定分页值
				$(self).setInitTableWidth(self);//设定表宽度
				$(self).setInitTableHeight(self);//设定表高度
				
				$.zgGrid.buttonGroup(self);//设定附加按钮
				$.zgGrid.setFreeze(self);//设定冻结列
				$.zgGrid.tableTrBackageHover(self);//鼠标滑动点击事件
				
				$("*").not(".zgGrid-modal").click(function(){
					$(".zgGrid-modal").hide();
				});
				//如果是使用复选列则挂载复选列列头的点击事件
				if(self.defaults.isCheckbox==true||self.defaults.isCheckbox=="true"){
					var thead = $(self.table).find("thead");
					var tbody = $(self.table).find("tbody");
					thead.find("input[type='checkbox']").on("click",function(e){
						let _flg = this.checked;
						tbody.find("input[type='checkbox']").prop("checked",_flg);
						let trs = tbody.find("tr");
						trs.each(function () {
							if(_flg){
								$(this).addClass("info");
							}else{
								$(this).removeClass("info");
							}
						})
					});
				}
				//列排序
				$("#"+self.id+"_main_body").on("click","a[class*='zGgridSotrA']",function(){
					if(self.eventstatus){
						self.eventstatus = false;
					}else{
						return;
					}
					var clickObj = $(this); 
					$(self).zgGridMask(self,function(){
						$.zgGrid.sotrTable(clickObj,self);
					});
				});
			//});
			// 关闭遮罩---------
			return self;
		});
	};
	
})(jQuery);