/* 
* myAutomatic封装1.0
* 输入4个字符后自动向后台发送请求，返回集合后展示
*/
;(function($) {
	$.myAutomatic = $.myAutomatic || {};//创建对象
	//装载扩展方法,内部方法外部无法调
	$.extend($.myAutomatic,{
		initModel:function(self){
			var k="";v="";
			if(self.defaults.defaultsVal!=null){
				k = self.defaults.defaultsVal["k"];
				v = self.defaults.defaultsVal["v"];
			}
			var selectId = self.attr("id")+"_select_div",
				selectCountId = selectId+"_count",
				selectPagerId = selectId+"_pager_box",
				selectItemBoxId = selectId+"_item_box",
				autoMsgId = selectId+"_msg_box",
				htmlDom = ''+
						'<div class="select-div" id="'+selectId+'">'+
					    	'<ul class="select-item-box" id="'+selectItemBoxId+'">'+
							'</ul>'+
					    	'<div class="auto-pager" id="'+selectPagerId+'">'+
							  	'<a class="to-l" href="javascript:void(0);">&larr;</a>'+
							  	'<span class="auto-count" id="'+selectCountId+'">0/N</span>'+
								'<a class="to-r" href="javascript:void(0);">&rarr;</a>'+
					    	'</div>'+
						'</div>';
			var  htmlDomMsg = '<div flg="hide" class="auto-msg" id="'+autoMsgId+'">'+
								'</div>';
			self.before(htmlDomMsg);//装载下拉dom
			self.after(htmlDom);//装载下拉dom
			self.autoMsg = $("#"+autoMsgId);//提示区对象
			self.selectObj = $("#"+selectId);//存放下拉大盒子对象
			self.selectItemBox = $("#"+selectItemBoxId);//存放下拉对象
			self.selectPagerObj = $("#"+selectPagerId);//分页对象
			self.selectCountObj = $("#"+selectCountId);//存放数量对象
			self.inputObj = $("#"+self.attr("id"));//输入对象
			self.butLeft = self.selectObj.find("a[class*='to-l']");//分页向左
			self.butRight = self.selectObj.find("a[class*='to-r']");//分页向右
			self.butRefresh = self.parent().find("a[class*='refresh']");//刷新按钮
			self.butCircle = self.parent().find("a[class*='circle']");//清理按钮
			self.selectObj.hide();
			$(self.inputObj).val(v).attr("k",k).attr("v",v);
		},
		cleanSelectObj:function(self){//清理所有内容均清空
			self.selectObj.data = null;
			self.selectObj.hide();
			self.selectItemBox.empty();//清空下拉项中所有内容
			
			self.selectCountObj.text("0/N");
			self.autoMsg.text("").hide().attr("flg","hide");
			$(self.inputObj).val("").attr("k","").attr("v","");//输入框置空
			self.tempK = "";//临时key
			self.tempV = "";//临时value
			self.tempSelectK = "";//临时选择的key
			self.tempSelectV = "";//临时选择的value
			self.defaults.pages=1;
			self.sumPager = 0;
			//self.val("");//输入框
			var isFun = $.isFunction(self.defaults.cleanInput) ? true : false;
			if(isFun){
				self.defaults.cleanInput.call(this);
			}
		},
		refreshSelectObj:function(self){//刷新
			self.selectItemBox.empty();//清空下拉项中所有内容
			self.selectCountObj.text("0/N");
			self.defaults.pages=1;
		},
		setValue:function(self,valObj){
			$(self.inputObj).val(valObj.attr("v")).attr("k",valObj.attr("k")).attr("v",valObj.attr("v"));
			self.tempSelectK = valObj.attr("k");//临时选择的key
			self.tempSelectV = valObj.attr("v");//临时选择的value
			self.tempK = "";//临时key
			self.tempV = "";//临时value
		},
		setValueTemp:function(self,k,v){
			$(self.inputObj).val(v).attr("k",k).attr("v",v);
			self.tempSelectK ="";//临时选择的key
			self.tempSelectV = "";//临时选择的value
			self.tempK = "";//临时key
			self.tempV = "";//临时value
		},
		getData:function(self){//获取数据
			var _date = "";
			if(self.defaults.isTrim){
				_date = "v="+encodeURI($.trim($(self.inputObj).val()));
			}else{
				_date = "v="+encodeURI($(self.inputObj).val());
			}
			_date+=self.addParam;
			$.ajax({
				url: self.defaults.url,
				cache: false,
				async: false,
				dataType:"JSON",
				data:_date,
				type:"GET",
				success:function(result,status,xhr){
					self.data = result;
				},
				error:function(xhr,status,error){
					if(status!="parsererror"){
						$.myAutomatic.msg(self,self.defaults.errorMsg);
					}
				},
				complete:function(xhr,status){
					$.myAutomatic.refreshSelectObj(self);//
					//遍历
					$.myAutomatic.traverseData(self);//事件挂载
				}
			});
		},
		msg:function(self,text){
			var msgObj = self.autoMsg;
			if(msgObj.attr("flg")=="hide"){
				msgObj.attr("flg","show").text(text).show(100,function(){
					setTimeout(function () { 
						msgObj.text("").hide(100).attr("flg","hide");
				    }, 2000);
				});
			}
		},
		traverseData:function(self){//遍历数据
			var data = self.data,
				selectItemBox = self.selectItemBox;
			var dCount = 0;
			if(data!=null){
				dCount = data.length;
			}
			if(data!=null&&data!=""&&dCount>0){
				var pages = self.defaults.pages;//当前页号
				var sumCount = dCount;//总数据量
				var ePageSize = self.defaults.ePageSize;
				if(ePageSize>0){
					var count = Math.ceil(sumCount/Number(ePageSize));//得到总页数
					count = count<=0?1:count;
					self.selectCountObj.text(pages+"/"+count);//设定显示内容
					self.sumPager = count;
					self.selectPagerObj.show();
				}else{
					self.selectPagerObj.hide();
				}
				//其实数据位置
				var start = ((pages-1)*ePageSize);
				var end = ((pages-1)*ePageSize)+ePageSize;
				end = end<=0?dCount:end;
				$.each(data,function(i,n){
					if(i>=start&&i<end){
						selectItemBox.append("<li><a href='javascript:void(0);' hidek='"+n.hidek+"' k='"+n.k+"' v='"+n.v+"' >"+n.v+"</a></li>");
					}
				});
			}else{
				selectItemBox.append("<span>"+self.defaults.nullMsg+"</span>");
			}
		},
		startSelect:function(self){//开始检索
			var param = self.defaults.param; 
			self.addParam = "";
			if(param!=null&&param.length>0){
				var p = "";
				for(var i=0;i<param.length;i++){
					p+="&"+param[i].k+"="+$("#"+param[i].v).val();
				}
				self.addParam = p;
			}
			if(self.replaceParam!=null&&self.replaceParam.length>0){
				self.addParam = self.addParam+self.replaceParam;
			}
			$.myAutomatic.getData(self);//事件挂载
			self.selectObj.show();
		},
		replaceParam:function(self,paramStr){//替换检索参数
			self.replaceParam = "";
			if(paramStr!=null&&paramStr.length>0){
				self.replaceParam = paramStr;
			}
		},
		initClick:function(self){//事件挂载
			var  isIE =  (document.all && window.ActiveXObject && !window.opera) ? true : false;
			//取消空格执行 改用输入到指定数量内容后就执行
			$(self.inputObj).bind('input propertychange click', function(e) {
				$.myAutomatic.refreshSelectObj(self);//
				//如果k是空不将数据放入临时变量中
				var k = $(this).attr("k");
				var v = $(this).attr("v");
				var nowValue = $(this).val();
				if(k!=""&&self.tempK==""){
					self.tempK = k;
					self.tempV = v;
					self.tempSelectK = "";//临时选择的key
					self.tempSelectV = "";//临时选择的value
				}
				if(self.defaults.isTrim){
					if(/*$.trim(nowValue)!=""&&*/$.trim(nowValue).length>=self.defaults.startCount){
						$.myAutomatic.startSelect(self);//事件挂载
					}
				}else{
					if(/*nowValue!=""&&*/nowValue.length>=self.defaults.startCount){
						$.myAutomatic.startSelect(self);//事件挂载
					}
				}
				
				e.stopPropagation();
			});
			
			//input事件
			$(self.inputObj).on("keydown",function(e){
				var eCode = e.keyCode;
		        	if(eCode>=37&&eCode<=40){
		        		if(self.selectItemBox.find("li").length>0){
			        			switch(eCode){  //判断按键
			        			case 37: 	//左
			        				$(self.butLeft).click();
			        				break;
			        			case 38:	//上	
			        				self.selectItemBox.find("li:last").find("a:first").focus();
			        				break;
			        			case 39:	//右
			        				$(self.butRight).click();
			        				break;
			        			case 40:	//下
			        				self.selectItemBox.find("li:first").find("a:first").focus();
			        				break;
			        			default:
			        				break;
			        			}
			        		}
			        }
		        e.stopPropagation();
			});
			//刷新事件(重新获取)
			$(self.butRefresh).on("click",function(e){
				
				var thisVal = $(self.inputObj).val();
				if($.trim(thisVal).length>=self.defaults.startCount){
					//清空下拉项中所有内容
					$.myAutomatic.refreshSelectObj(self);//
					$.myAutomatic.startSelect(self);//开始
				}else{
					$.myAutomatic.msg(self,"At least "+self.defaults.startCount+" character is required！");
				}
				e.stopPropagation();
			});
			//清理事件(清空所有内容)
			$(self.butCircle).on("click",function(e){
				$.myAutomatic.cleanSelectObj(self);
				e.stopPropagation();
			});
			
			//分页向左事件(向前翻页)
			$(self.butLeft).on("click",function(e){
				self.selectItemBox.empty();//清空下拉项中所有内容
				self.defaults.pages--;
				self.defaults.pages = self.defaults.pages<=0?1:self.defaults.pages;
				$.myAutomatic.traverseData(self);
				self.selectItemBox.find("li:first").find("a:first").focus();
				e.stopPropagation();
			});
			
			//分页向左事件(向后翻页)
			$(self.butRight).on("click",function(e){
				self.selectItemBox.empty();//清空下拉项中所有内容
				self.defaults.pages++;
				self.defaults.pages = self.defaults.pages<self.sumPager?self.defaults.pages:self.sumPager;
				
				$.myAutomatic.traverseData(self);
				self.selectItemBox.find("li:first").find("a:first").focus();
				e.stopPropagation();
			});
			//下拉项点击选择事件
			$(self.selectItemBox).on("click","a",function(e){
				var thisObj = $(this);
				$.myAutomatic.setValue(self,thisObj);//赋值
				var isFun = $.isFunction(self.defaults.selectEleClick) ? true : false;
				if(isFun){
					self.defaults.selectEleClick.call(this,thisObj);
				}
				self.selectObj.hide(function(){
					self.inputObj.focus();
					$.myAutomatic.refreshSelectObj(self);//
				},0);
				e.stopPropagation();
			});
			//下拉项选择事件
			$(self.selectItemBox).on("keydown","a",function(e){
				e.stopPropagation(); 
				var thisA = $(this);
				if(self.selectItemBox.find("li").length>0){
					var eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		        	switch(eCode){  //判断按键
		            case 37: 	//左
		            	$(self.butLeft).click();
		            	break;
		            case 38:	//上	
		            	thisA.parent().prev().find("a:first").focus();
		            	break;
		            case 39:	//右
		            	$(self.butRight).click();
		            	break;
		            case 40:	//下
		            	 thisA.parent().next().find("a:first").focus();
		            	break;
		            case 13:	//回车
		            	break;
		            default:
		            	$(self.inputObj).focus();
		                break;
		        	}
	        	}
				e.stopPropagation();
			});
			//焦点离开了self父级元素后
			$(document).on("click",function(e){
				$.myAutomatic.refreshSelectObj(self);//
				self.selectObj.hide();
				e.stopPropagation();
				if(self.tempSelectK==""&&self.tempK!=""){
					$.myAutomatic.setValueTemp(self,self.tempK,self.tempV);//赋值
				}
				if(self.tempSelectK==""&&self.tempK==""){
					var k = $(self.inputObj).attr("k")
					var v = $(self.inputObj).attr("v");
					if(k==""&&v==""){
						$.myAutomatic.setValueTemp(self,"","");//赋值
					}
				}
			});
			//
		}
		
	});
	//扩展方法，外部方法，外部调用 
	//$.myAjax.extend({	 });
	//初始化 最后执行
	$.fn.myAutomatic = function( options ) {
		var self = $(this),
			htmlSelf = this;
		var defaults = {
			isTrim:true,//是否清楚收尾空格，默认true清楚	
			url:"",//规定发送请求的 URL。默认是当前页面
			param:[],//附加参数  [{k:"name",v:"text"},{...}]
			nullMsg:"No Data",//数据为空的提示，默认“”
			errorMsg:"error",//请求失败的提示，默认“”
			startCount:4,//多少字符才可以执行方法
			ePageSize:0,//结果集每页的数据量，默认0，代表不将结果集分页，所有内容均排列显示。若实际数值，则按照指定值进行分页，10、20、30 等表示页面数据条数
			defaultsVal:null,//初始化预设值 defaultsVal:{'k':'admin','v':'超级admin'},//初始化预设值
			pages:1,//初始化页号
			cleanInput:null,//清空内容后事件
			selectEleClick:null //选择点击结果集事件
		};
		self.data = null;
		self.selectObj = null;//存放下拉对象大盒子
		self.autoMsg = null;//提示区对象
		self.selectPagerObj = null;//分页对象
		self.selectItemBox = null;//下拉对象集合对象
		self.selectCountObj = null;//存放数量对象
		self.butLeft = null;//分页向左
		self.butRight = null;//分页向右
		self.butRefresh = null;//刷新按钮
		self.butCircle = null;//清理按钮
		self.sumPager = 0;//总页数
		self.inputObj = null;//输入对象
		self.addParam = "";// 附加参数
		self.replaceParam = "";// 替换附加参数
		self.tempK = "";//临时key
		self.tempV = "";//临时value
		self.tempSelectK = "";//临时选择的key
		self.tempSelectV = "";//临时选择的value
		
		self.htmlDom = htmlSelf;
		self.defaults = $.extend(false,defaults, options);//合并参数
		$.myAutomatic.initModel(self);//初始化原型
		$.myAutomatic.initClick(self);//事件挂载
		return self;
	};
})(jQuery);