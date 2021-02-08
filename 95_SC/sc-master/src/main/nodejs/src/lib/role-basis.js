/***
 * roleBasis 
 * 
 * 对应角色权限特定js，依赖jquery
 */
;(function($) {
	$.roleBasis = $.roleBasis || {};//创建对象
	//装载扩展方法,内部方法外部无法调，初始化顺序 1
	$.extend($.roleBasis,{
		toRoleAjaxData:function(url,data,type){
			type = type||"GET";
			var res  = null;
			$.ajax({
				url: url,//规定发送请求的 URL。默认是当前页面
				cache: false,//布尔值，表示浏览器是否缓存被请求页面。默认是 true。
				async: false,//布尔值，表示请求是否异步处理。默认是 true。
				dataType:"JSON",//预期的服务器响应的数据类型
				data:data,
				type: type,
				success:function(restObjs){
					res = restObjs;
				}
			});
			return res;
		},
		submitAjaxData:function(url){
			var res  = null;
			$.ajax({
				url: url,//规定发送请求的 URL。默认是当前页面
				cache: false,//布尔值，表示浏览器是否缓存被请求页面。默认是 true。
				async: false,//布尔值，表示请求是否异步处理。默认是 true。
				dataType:"JSON",//预期的服务器响应的数据类型
				data:$('#submitFrom').serialize(),
				type: "POST",
				success:function(restObjs){
					res = restObjs;
				}
			});
			return res;
		},
		//装载页面对象
		loadDomObj:function(self){
			self.domObj.roleBox = $("#role_basic_box");//角色Box对象
			self.domObj.parentBox= $("#parent_box");//父级Box对象
			self.domObj.resourcesBox= $("#resources_box");//资源Box对象
			self.domObj.permissionsBox= $("#permissions_box");//权限Box对象
			self.domObj.resourcesDialogBox= $("#resourcesDialogBox");//资源选择弹出窗对象
			self.domObj.parentDialogBox= $("#parentDialogBox");//选择父级角色弹出窗对象
			self.domObj.parentInfoDialogBox= $("#parentInfoDialogBox");//父级角色查看详情弹出窗对象
			return self;
		},
		//取得角色数据信息
		loadRoleData:function(self){
			var url = "",
				param = "";
			url = self.defaults.url+"/"+self.defaults.roleUrl;
			if(self.defaults.roleId>0){
				param = "id="+self.defaults.roleId;
			}
			self.data.roleData = $.roleBasis.toRoleAjaxData(url,param,"GET");
			return self;
		},
		//取得角色所属数据信息
		loadBelongData:function(self){
			var	url = self.defaults.url+"/"+self.defaults.belongUrl,
				param="";
			self.data.belongData = $.roleBasis.toRoleAjaxData(url,param,"GET");
			return self;
		},
		//取得父级角色信息
		loadParentData:function(self){
			var	url = self.defaults.url+"/"+self.defaults.roleUrl,
			param="";
			
			if(self.data.roleData!=null&&self.data.roleData!=""){
				if(self.data.roleData.superId!=null&&self.data.roleData.superId!=""){
					param = "id="+self.data.roleData.superId;
					self.data.parentData = $.roleBasis.toRoleAjaxData(url,param,"GET");
				}
			}
			return self;
		},
		//取得资源信息
		loadResourcesData:function(self){
			var	url = self.defaults.url+"/"+self.defaults.resourcesUrl;
			self.data.resourcesData = $.roleBasis.toRoleAjaxData(url,"","GET");
			return self;
		},
		//取得角色自身资源信息
		loadResourcesGroupData:function(self){
			var	url = self.defaults.url+"/"+self.defaults.resourcesGroupUrl;
				param="";
			if(self.defaults.roleId!=null&&self.defaults.roleId>0){
				param = "id="+self.defaults.roleId;
				self.data.resourcesGroupData = $.roleBasis.toRoleAjaxData(url,param,"GET");
			}
			return self;
		},
		//取得菜单信息
		loadMenuData:function(self){
			var	url = self.defaults.url+"/"+self.defaults.menuUrl;
			self.data.menuData = $.roleBasis.toRoleAjaxData(url,"","GET");
			return self;
		},
		//取得权限信息
		loadPermissionsData:function(self){
			var	url = self.defaults.url+"/"+self.defaults.permissionUrl;
			if(self.defaults.roleId!=null&&self.defaults.roleId>0){
				param = "id="+self.defaults.roleId;
			}
			self.data.permissionsData = $.roleBasis.toRoleAjaxData(url,param,"GET");
			return self;
		},
		//dom 角色所属信息设定
		setBelongToDom:function(self){
			if(self.data.belongData!=null&&self.data.belongData!=""){
				var role_basic_belong = $("#role_basic_belong");
				if(self.data.belongData.length==1){
					var str = '<option superid="'+self.data.belongData[0].superId+'" value="'+self.data.belongData[0].id+'">'+self.data.belongData[0].name+'</option>';
					role_basic_belong.append(str);
				}else{
					role_basic_belong.append('<option superid="0" value="0">--请选择--</option>');
					$.each(self.data.belongData,function(index,item){
						var str = '<option superid="'+item.superId+'" value="'+item.id+'">'+item.name+'</option>';
						role_basic_belong.append(str);
					});
				}
				
				
			}
			return self;
		},
		//dom 角色信息设定
		setRoleToDom:function(self){
			if(self.data.roleData!=null&&self.data.roleData!=""){
				var roleBox = self.domObj.roleBox,
					roleData = self.data.roleData;
//				console.log("self.domObj.roleBox:"+self.domObj.roleBox);
//				console.log("roleBox:"+roleBox);
				$(roleBox).find("#role_basic_name").val(roleData.name);
				var ck = 2;
				//$(roleBox).find("#blankCheckbox").attr("checked", roleData.isuse);
				if(roleData.isuse){
					ck = 1;
				}
				var ckobj = $(roleBox).find("#blankCheckbox input[name='grantFlg'][value='"+ck+"']");
				ckobj.prop("checked", true);
				ckobj.parent().addClass('active').siblings().removeClass("active");
				//设定角色所属信息
				var option = $(roleBox).find("#role_basic_belong").find("option");
				option.each(function(index){
					var thisOpt = $(this);
					if(thisOpt.val()==roleData.rid){
						thisOpt.attr("selected",true);
						return self;
					}
				});
			}
			return self;
		},
		//清空继承信息
		cleanParentToDom:function(self){
			$("#parent_name").text("");
			$("#parentId").val("");
			$("#parent_belong").text("");
			self.domObj.parentBox.find("div[class*='not-parent']").show();
			self.domObj.parentBox.find("div[class*='is-parent']").hide();
		},
		//dom 角色继承信息设定
		setParentToDom:function(self){
			var parentData = self.data.parentData,
				parentBox = self.domObj.parentBox;
			if(parentData!=null&&parentData!=""){
				$("#parent_name").text(parentData.name);
				$("#parent_id").val(parentData.id);
				var belongData = self.data.belongData;
				$.each(belongData,function(index,item){
					if(item.id == parentData.rid){
						$("#parent_belong").text(item.name);
						return false;
					}
				});
				parentBox.find("div[class*='not-parent']").hide();
				parentBox.find("div[class*='is-parent']").show();
			}else{
				parentBox.find("div[class*='not-parent']").show();
				parentBox.find("div[class*='is-parent']").hide();
			}
			return self;
		},
		//dom 资源组信息设定
		setResourcesGroupToDom:function(self){
			var resourcesGroupData = self.data.resourcesGroupData,
				groupDiv = $("#resources_box_group"),
				groupCount = $("#resources_group_count").val();
			if(resourcesGroupData!=null&&resourcesGroupData!=""&&resourcesGroupData.length>0){
				$.each(resourcesGroupData,function(index,group){
					// 按钮html
					 var butStr = '<div class="row">'+
										 '<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">'+
										      '<div class="resources-edit">'+
											       '<a class="view-resources-gropu" todiv="resources_box_group_item_'+groupCount+'" href="javascript:void(0);" title="查看更多"><span class="glyphicon glyphicon-info-sign "></span> 查看更多</a>&nbsp;'+
											       '<a class="del-resources-group" todiv="resources_box_group_item_'+groupCount+'" href="javascript:void(0);" title="Delete"><span class="glyphicon glyphicon-trash "></span> Delete</a>'+
										      '</div>'+
									     '</div>'+
								    '</div>';
					 var textStr = '';// 默认显示部分的html
					 var textHtdeStr = '';//全部隐藏的部分
					 
					 //资源内容遍历
					 $.each(group.item,function(ix,node){
						 if(ix==0){
							 textStr='<div class="row">'+
									 	'<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-m ">'+
									      '<div class="row resources-group-line">'+
									       '<div class="col-xs-12 col-sm-4 col-md-4 col-lg-3 ">'+
									        '<div class="resources-tt">'+
									         node.typeName+
									        '</div>'+
									       '</div>'+
									       '<div class="col-xs-12 col-sm-8 col-md-8 col-lg-9">'+
									        '<div class="resources-area">'+
									        node.orgChain+
									        '</div>'+
									       '</div>'+
									      '</div>'+
									     '</div>'+
									    '</div>';//
						 }
						 textHtdeStr+='<div class="row ">'+
									      '<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-m resources-item-lines">'+
									       '<div class="row resources-group-line">'+
									        '<div class="col-xs-12 col-sm-4 col-md-4 col-lg-3 ">'+
									         '<div class="resources-tt">'+
									         node.typeName+
									         '</div>'+
									        '</div>'+
									        '<div class="col-xs-12 col-sm-8 col-md-8 col-lg-9">'+
									         '<div class="resources-area">'+
									         node.orgChain+
									         '</div>'+
									        '</div>'+
									        '<input type="hidden" class="resources-group-input-val" value="'+node.orgChain+'" role_id="" rtype="'+node.type+'" resource_id="'+node.rid+'" group_code="'+groupCount+'" />'+
									       '</div>'+
									      '</div>'+
									     '</div>';
					 });
					 textHtdeStr='<div class="r-b-g-i-box  resources-group-hide-box" id="resources_box_group_item_'+groupCount+'_hidebox">'+
								     '<div class="row">'+
								      '<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">'+
								       '<div class="resources-edit">'+
								        '<a class="close-resources-group" todiv="resources_box_group_item_'+groupCount+'" href="javascript:void(0);" title="Close"><span class="glyphicon glyphicon-off"></span> Close</a>'+
								       '</div>'+
								      '</div>'+
								     '</div>'+
								     textHtdeStr+
								    '</div>';
					 
					 var boxHtml = '<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 resources-box-group-item" id="resources_box_group_item_'+groupCount+'">'+
									   '<div class="resources-group-box">'+
									   butStr+
									   textStr+
									   textHtdeStr+
									   '</div>'+
								   '</div>';// 主体结构
					
					groupDiv.append(boxHtml);
					groupCount++;
				});
			}
			$("#resources_group_count").val(groupCount);
			return self;
		},
		//dom 菜单信息设定
		setMenuToDom:function(self){
			var menus = self.data.menuData;
			if(menus!=null&&menus!=""){
				groupDiv = $("#permissions_box_group");
				$.each(menus,function(index,obj){
					var cHtml = '';
					$.each(obj.childMenu,function(cindex,cobj){
						cHtml += $.roleBasis.createPermissionsHTML(cobj.id,cobj.name,cobj.groupCode,true,"");
					});
					var htmlBox = $.roleBasis.createPermissionsHTML(obj.id,obj.name,obj.groupCode,false,cHtml);
					groupDiv.append(htmlBox);
				});
			}
			return self;
		},
		/*
		 * 创建权限页面html字符串，name：菜单名称，groupCode：目标标记，isChild:是否为子菜单 true:是，false不是,area=""
		 */
		createPermissionsHTML:function(id,name,groupCode,isChild,area){
			var child = "";
			if(!isChild){
				child = "child";
			}
			var strHtml = '<div class="row permissions-line-box">'+
							'<div class="col-xs-12 col-sm-4 col-md-2 col-lg-2" >'+
								'<p class="form-control-static">'+name+'</p>'+
								'</div>'+
								'<div class="col-xs-12 col-sm-8 col-md-10 col-lg-10 '+child+'" menuid="'+id+'" target="'+groupCode+'" >'+area+
								'</div>'+
							'</div>';
			return strHtml;
		},
		//权限设定
		setPermissionToDom:function(self){
			var permissionsData = self.data.permissionsData;
			if(permissionsData!=null&&permissionsData!=""){
				$.each(permissionsData,function(index,obj){
					var storeLine = 0;
					var groupCode = obj.groupCode,
									targetObj=null;
					if(groupCode==null||groupCode==""){
						//如果groupCode为空，视为独自的权限，直接创建结构载入即可
						groupCode = 'other-box'
					}
					targetObj = $("#permissions_box_group div[target='"+groupCode+"']");
					if(targetObj==null||targetObj.length<=0){
						targetObj = $("#permissions_box_group div[target='other-box']");
						storeLine = 1;
					}
					if(targetObj==null||targetObj.length<=0){
						//创建新的结构模型
						var htmlBox = $.roleBasis.createPermissionsHTML("0","其他权限","other-box",false,"");
						$("#permissions_box_group").append(htmlBox);
						targetObj = $("#permissions_box_group div[target='other-box']");
						storeLine = 1;
					}
					var menuId = targetObj.attr("menuid");
					var cHtml = "";
					$.each(obj.item,function(index,cobj){
						var sel = "",classSel="";
						if(cobj.isuse){
							sel = 'checked=checked ';
							classSel = 'sel';
						}
						cHtml+= '<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 '+classSel+'" groupCode="'+groupCode+'" ><input  groupCode="'+groupCode+'" menuid="'+menuId+'"  type="checkbox" value="'+cobj.id+'" '+sel+' code="'+cobj.code+'" />'+cobj.name+'</label>';
					});
					if(storeLine>0){
						targetObj.append('<div class="row"><div class="checkbox">'+cHtml+'</div></div>');
					}else{
						targetObj.prepend('<div class="row"><div class="checkbox">'+cHtml+'</div></div>');
					}
				});
			}
			return self;
		},
		//资源设定Dom
		setResourcesToDom:function(self){
			var resourcesData = self.data.resourcesData,//资源数据对象
				resourcesDialogBox = self.domObj.resourcesDialogBox,//资源页面dmo对象
				setResourcesMap = self.defaults.setResourcesMap;//资源类型设定区域
			if(resourcesData!=null&&resourcesData.length>0){
				//console.log("setResourcesType-------------------"+setResourcesMap[0].code)
				$.each(resourcesData,function(index,obj){
					//类型处理
					var mapObj = setResourcesMap[obj.id];
					switch (mapObj.type)
					{
					case "custom":
						var loadFun = $.isFunction(mapObj.loadFun) ? true : false;
						if(loadFun){
							mapObj.loadFun.call(this,resourcesDialogBox,obj);
						}
					  break;
					case "checkbox":
						var loadFun = $.isFunction(mapObj.loadFun) ? true : false;
						if(loadFun){
							mapObj.loadFun.call(this,obj);
						}else{
							$.roleBasis.setDomCheckbox(resourcesDialogBox,obj);
						}
					  break;
					case "radio":
						var loadFun = $.isFunction(mapObj.loadFun) ? true : false;
						if(loadFun){
							mapObj.loadFun.call(this,obj);
						}else{
							$.roleBasis.setDomRadio(resourcesDialogBox,obj);
						}
						break;
					case "select":
						var loadFun = $.isFunction(mapObj.loadFun) ? true : false;
						if(loadFun){
							mapObj.loadFun.call(this,obj);
						}else{
							$.roleBasis.setDomSelect(resourcesDialogBox,obj);
						}
						break;
					case "input":
						var loadFun = $.isFunction(mapObj.loadFun) ? true : false;
						if(loadFun){
							mapObj.loadFun.call(this,obj);
						}else{
							$.roleBasis.setDomInput(resourcesDialogBox,obj);
						}
						break;
					default:
						break;
					}
					
				});
				
			}else{
				var msgStr = '<div class="row r-box-item">'+
								'<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">'+
									'<p class="form-control-static col-m text-center">未取得资源数据</p>'+
								'</div>'+
							'</div>';
				resourcesDialogBox.html(msgStr);
			}
				
		},
		//设定复选框样式
		setDomCheckbox:function(box,data){
			
			var itemHtml = '';
			$.each(data.item,function(index,itm){
				itemHtml+='<label><input type="checkbox" value="'+itm.id+'">'+itm.name+'</label>';
			});
			
			var str='<div class="row r-box-item">'+
						'<div class="col-xs-12 col-sm-4 col-md-4 col-lg-2 ">'+
							'<p class="form-control-static col-m dialog-tt">'+data.name+'</p>'+
						'</div>'+
						'<div class="col-xs-12 col-sm-8 col-md-8 col-lg-10">'+
							'<div class="dialog-text form-control">'+
								'<div class="checkbox">'+
									itemHtml+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>';
			box.append(str);
		},
		//设定单选框样式
		setDomRadio:function(box,data){
			var itemHtml = '';
			$.each(data.item,function(index,itm){
				itemHtml+='<label><input type="radio" name="radio_'+data.id+'" value="'+itm.id+'">'+itm.name+'</label>';
			});
			
			var str='<div class="row r-box-item">'+
						'<div class="col-xs-12 col-sm-4 col-md-4 col-lg-2 ">'+
							'<p class="form-control-static col-m dialog-tt">'+data.name+'</p>'+
						'</div>'+
						'<div class="col-xs-12 col-sm-8 col-md-8 col-lg-10">'+
							'<div class="dialog-text form-control">'+
								'<div class="radio">'+
									itemHtml+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>';
			box.append(str);
		},
		//设定输入样式
		setDomInput:function(box,data){
			var itemHtml = '';
			$.each(data.item,function(index,itm){
				itemHtml += '<input class="form-control col-m" value="'+itm.name+'" id="'+itm.id+'" />';
			});
			var str='<div class="row r-box-item">'+
						'<div class="col-xs-12 col-sm-4 col-md-4 col-lg-2 ">'+
							'<p class="form-control-static col-m dialog-tt">'+data.name+'</p>'+
						'</div>'+
						'<div class="col-xs-12 col-sm-8 col-md-8 col-lg-10">'+
							'<div class="dialog-text ">'+
									itemHtml+
							'</div>'+
						'</div>'+
					'</div>';
			box.append(str);
		},
		//设定下拉项样式
		setDomSelect:function(box,data){
			var options = '';
			$.each(data.item,function(index,itm){
				options += '<option id="'+itm.id+'" >'+itm.name+'</option>';
			});
			var selectHtml = '<select class="form-control" id="" >'+
									options+
							  '</select>';
			var str='<div class="row r-box-item">'+
						'<div class="col-xs-12 col-sm-4 col-md-4 col-lg-2 ">'+
							'<p class="form-control-static col-m dialog-tt">'+data.name+'</p>'+
						'</div>'+
						'<div class="col-xs-12 col-sm-8 col-md-8 col-lg-10">'+
							'<div class="dialog-text ">'+
								selectHtml+
							'</div>'+
						'</div>'+
					'</div>';
			box.append(str);
		},
		//事件设定
		eventSet:function(self){
			var selectParentTbody = $("#selectParentTbody");
			parentBox = self.domObj.parentBox;
			//取消选择继承角色
			$("#delSelectParentDialogBut").on("click",function(){
				$("#parent_name").text("");
				$("#parent_belong").text("");
				$("#parent_id").val("");
				parentBox.find("div[class*='not-parent']").show();
				parentBox.find("div[class*='is-parent']").hide();
				$("#selectParentValueHide_ID").val("");
				$("#selectParentValueHide_NAME").val("");
				$("#selectParentValueHide_SUOSHU").val("");
				$("#selectParent_msg").text("");
			});
			//选择继承角色
			$("#selectParentDialogBut").on("click",function(){
				//取得所有可以继承的角色数据
				var param = "";
				if($("#roleId").val()!=""){
					param = "roleId="+$("#roleId").val();
				}
				var roles = $.roleBasis.toRoleAjaxData(self.defaults.url+"/getPmgrUiAccreditRoleList",param,"GET");
				selectParentTbody.empty();
				$.each(roles,function(ix,item){
					var id = item.id;
					var name = item.name;
					//var suoshu = item.rtype+'>'+ item.rtype;
					var suoshu = (item.ridName!=""&&item.ridName!=null&&item.ridName!="null") ? item.ridName : "";
					//var aTsr = '<a selname="'+name+'" selid="'+id+'" suoshu="'+suoshu+'"  href="#" class="selectParentButA">选择</a>';
					var aTsr = '<input type="radio" name="selectParentRadio" id="selectParentRadio'+id+'"  selname="'+name+'" selid="'+id+'" suoshu="'+suoshu+'"  >';
					var str = '<tr title="'+name+'" index="selectParentRadio'+id+'">'+
								'<td>'+aTsr+'</td>'+
				          		'<td>'+name+'</td>'+
					          	'<td>'+suoshu+'</td>'+
							'</tr>';
					
					selectParentTbody.append(str);
				});
				$("#selectParentValueHide_ID").val("");
				$("#selectParentValueHide_NAME").val("");
				$("#selectParentValueHide_SUOSHU").val("");
				$("#screening_input").val("");
				$("#selectParentDialog").modal("show");
				$("#selectParent_msg").text("");

			});
			
			//确认选择继承的角色
			selectParentTbody.on("click","input[name='selectParentRadio']", function() {
				var thisParentTr = $(this).parent().parent();
				thisParentTr.addClass('selected').siblings().removeClass('selected');
				$("#selectParentValueHide_ID").val($(this).attr("selid"));
				$("#selectParentValueHide_NAME").val($(this).attr("selname"));
				$("#selectParentValueHide_SUOSHU").val($(this).attr("suoshu"));
				$("#selectParent_msg").text("");

			});
			selectParentTbody.on("click","td", function() {
				var thisParentTr = $(this).parent();
				thisParentTr.addClass('selected').siblings().removeClass('selected');
				var obj = $("#"+thisParentTr.attr("index"));
				$("#selectParentValueHide_ID").val($(obj).attr("selid"));
				$("#selectParentValueHide_NAME").val($(obj).attr("selname"));
				$("#selectParentValueHide_SUOSHU").val($(obj).attr("suoshu"));
				obj.prop("checked",true);
				$("#selectParent_msg").text("");
			});
			
			
			//选择角色继承 确认按钮
			$("#selectParentValueHide_confirm_but").on("click",function(e){
				var id = $("#selectParentValueHide_ID").val();
				if(id!=""){
					$("#selectParent_msg").text("");
					var name = $("#selectParentValueHide_NAME").val();
					var suoshu = $("#selectParentValueHide_SUOSHU").val();
					$("#parent_name").text(name);
					$("#parent_belong").text(suoshu);
					$("#parent_id").val(id);
					parentBox.find("div[class*='not-parent']").hide();
					parentBox.find("div[class*='is-parent']").show();
					$("#selectParentDialog").modal("hide");
				}else{
					$("#selectParent_msg").text("请选择角色！");
				}
				
			});
		}
	});
	
	//初始化 最后执行，初始化顺序 3
	$.fn.roleBasis = function( options ) {
		return this.each( function() {
			var self = this;
			var defaults = {
				roleId:"",// 角色id	
				url:"",//根路径
				roleUrl:"getPmgrUiRole",//取得角色数据url/取得父级角色数据
				belongUrl:"getPmgrUiBelongList",//取得所属集合url
				roleUseListUrl:"getPmgrUiAccreditRoleList",//取得可授权的角色集合，
				resourcesUrl:"getPmgrUiResources",//取得资源集合url
				resourcesGroupUrl:"getPmgrUiResourcesGroup",//取得角色资源集合url
				menuUrl:"getPmgrUiMenuList",//取得菜单集合url
				permissionUrl:"getPmgrUiPermissionGroupList",//取得权限集合url
				setResourcesType:[],//设定资源类型(必须要有)
				setResourcesMap:null
			};
			//页面对象
			var domObj = {
				roleBox:null,//角色Box对象
				parentBox:null,//父级Box对象
				resourcesBox:null,//资源Box对象
				permissionsBox:null,//权限Box对象
				resourcesDialogBox:null,//资源选择弹出窗对象
				parentDialogBox:null,//选择父级角色弹出窗对象
				parentInfoDialogBox:null//父级角色查看详情弹出窗对象
			};
			//数据对象
			var data = {
				roleData:null,//角色数据
				belongData:null,//所属数据
				parentData:null,//父级数据
				resourcesData:null,//资源数据
				resourcesGroupData:null,//角色自身资源对象
				menuData:null,//资源数据
				permissionsData:null//权限数据
			};
			self.defaults = $.extend(false,defaults, options);//合并参数
			self.domObj = domObj;
			self.data = data;
			//拆解设定资源结构的json对象 为map
			var map = {};
			var setResourcesType = self.defaults.setResourcesType;
			$.each(setResourcesType,function(index,obj){
				map[obj.code] = obj;
			});
			self.defaults.setResourcesMap = map;
			
			
			// 装载DOM对象
			$.roleBasis.loadDomObj(self);
			//取得角色信息
			$.roleBasis.loadRoleData(self);
			//取得角色所属信息
			$.roleBasis.loadBelongData(self);
			//取得父级角色信息
			$.roleBasis.loadParentData(self);
			//取得资源信息
			$.roleBasis.loadResourcesData(self);
			//取得角色自身资源信息
			$.roleBasis.loadResourcesGroupData(self);
			//取得菜单信息
			$.roleBasis.loadMenuData(self);
			//取得权限信息
			$.roleBasis.loadPermissionsData(self);
			//----- dom 显示---------------------------------------
			//角色所属
			$.roleBasis.setBelongToDom(self);
			//角色
			$.roleBasis.setRoleToDom(self);
			//角色继承
			$.roleBasis.setParentToDom(self);
			//资源设定
			$.roleBasis.setResourcesToDom(self);
			//角色自身资源设定
			$.roleBasis.setResourcesGroupToDom(self);
			//菜单设定
			$.roleBasis.setMenuToDom(self);
			//权限设定
			$.roleBasis.setPermissionToDom(self);
			//事件设定
			$.roleBasis.eventSet(self);
			
			$("input[type='checkbox']").on("click",function(e){
				var thisObj = $(this);
				if(thisObj.is(':checked')){
					thisObj.parent().addClass("sel");
				}else{
					thisObj.parent().removeClass("sel");
				}
				
			});
			$("input[type='radio']").on("click",function(e){
				var thisObj = $(this);
				if(thisObj.is(':checked')){
					thisObj.parent().addClass("sel").siblings().removeClass("sel");
				}
				
			});
			
//			$("#submitData").on("click",function(){
//				//各个部分进行验证
//				var r = $.roleBasis.combinationFrom();
//				//各个部分进行拼装
//				if(r){
//					var	url = self.defaults.url+"/PmgrUiSubmitData";
//					$.roleBasis.submitAjaxData(url);
//				}
//			});
			return self;
		});
	}
	
})(jQuery);