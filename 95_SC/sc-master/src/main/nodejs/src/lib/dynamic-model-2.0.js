/* 
* 动态显示上传模板2.0
* 
* 
*/
(function($) {
	
	var xhr = null;
	
	//初始化xhr对象
	function initXhr(){
		//创建XMLHttpRuquest对象
		   if(window.XMLHttpRequest){  
			   xhr=new XMLHttpRequest();  
		       if(xhr.overrideMimeType){  
		    	   xhr.overrideMimeType("text/xml");  
		       }  
		   }else if(window.ActiveXObject){  
		       var activeName=["MSXML2.XMLHTTP","Microsoft.XMLHTTP"];  
		       for(var i=0;i<activeName.length;i++){  
		           try{  
		        	   xhr=new ActiveXObject(activeName[i]);  
		               break;  
		           }catch(e){  
		                        
		           }  
		       }  
		   }  
		   if(xhr==undefined || xhr==null){  
		       alert("XMLHttpRequest对象创建失败！！");  
		   }else{  
		       this.xhr=xhr;  
		   } 
	}
	$.uploadFun = $.uploadFun || {};//创建对象
	//装载扩展方法,内部方法外部无法调
	$.extend($.uploadFun,{
		initUploadHtmlDom:function(self){//初始化上传附件dom模块
			var str = "<form class='form-horizontal' id='uploadForm"+self.defaults.customId+"' action='"+self.defaults.url+"' method='post'  enctype='multipart/form-data' role='form'>"+
							"<div class='modal fade' id='uploadDialog"+self.defaults.customId+"' tabindex='-1'  role='dialog' aria-labelledby='myModalLabel"+self.defaults.customId+"' aria-hidden='false'>"+
								"<div class='modal-dialog'>"+
									"<div class='modal-content'>"+
										"<div class='modal-header'>"+
												"<h5 class='modal-title' id='myModalLabel"+self.defaults.customId+"'>上传</h5>"+
											"</div>"+
											"<div id='danger' class='alert alert-danger alert-dismissable' style='display:none;'>"+
												"<button type='button' class='close' onclick=\"$('.alert').hide();\" aria-hidden='true'>&times;</button>"+
												"<span class='msg'></span>"+
											"</div>"+
											"<div id='success' class='alert alert-success alert-dismissable' style='display:none;'>"+
												"<button type='button' class='close' onclick=\"$('.alert').hide();\" aria-hidden='true'>&times;</button>"+
												"<span class='msg'></span>"+
											"</div>"+
										"<div class='modal-body'>"+
											"<div class='form-group'>"+
												"<div class='col-sm-6 col-xs-8 col-md-9 col-lg-9'>"+
														"<input type='file' id='path"+self.defaults.customId+"' class='file-upload' name='file' multiple='multiple' >"+
												"</div>"+
												"<div class='col-sm-6 col-xs-4 col-md-3 col-lg-3'>"+
													"<button id='start_upload"+self.defaults.customId+"' type='button' class='btn btn-success btn-xs' disabled='true'><span class='glyphicon glyphicon-floppy-open'></span>&nbsp;开始上传</button>"+
												"</div>"+
											"</div>"+
											"<div class='form-group'>"+
												"<div class='col-sm-12'>"+
													"<div class='progress'>"+
														"<div id='progress_div"+self.defaults.customId+"' class='progress-bar progress-bar-striped active' role='progressbar' aria-valuenow='0' aria-valuemin='0' aria-valuemax='100' style='width: 0%'></div>"+
													"</div>"+
													"<div style='text-align:center;width:100%;color:#000;'>- <span id='progress_status"+self.defaults.customId+"'>0%</span> -</div>"+
												"</div>"+
											"</div>"+
											"<div class='form-group'>"+
												"<div class='col-sm-12'>"+
													"<table class='table table-hover table-condensed'>"+
														"<thead><tr><th width='80%'>名称</th><th>状态</th></tr></thead>"+
														"<tbody id='up_tbody"+self.defaults.customId+"'></tbody>"+
													"</table>"+
												"</div>"+
											"</div>"+
										"</div>"+
										"<div class='modal-footer'>"+
											"<button id='but_close"+self.defaults.customId+"' type='button' class='btn btn-default btn-xs' data-dismiss='modal'><span class='glyphicon glyphicon-off'></span>&nbsp;Close</button>"+
											"<button id='button_reset"+self.defaults.customId+"' type='button' class='btn btn-warning btn-xs'><span class='glyphicon glyphicon-repeat'></span>&nbsp;重置</button>"+
											"<button id='confirm"+self.defaults.customId+"' type='button' class='btn btn-primary btn-xs'><span class='glyphicon glyphicon-ok'></span>&nbsp;确认</button>"+
										"</div>"+
									"</div>"+
								"</div>"+
							"</div>"+
						"</form>";
			$("body").append(str);
			self.fromObj = $("#uploadForm"+self.defaults.customId);
		},
		clearUpload:function(fromObj,defaults){//初始化上传控件模块中的所有内容
			fromObj.find("#path"+defaults.customId).val("").prop("disabled",false);
			fromObj.find("#progress_div"+defaults.customId).css("width",0).addClass("active").removeClass("progress-bar-danger progress-bar-success");
			fromObj.find("#progress_status"+defaults.customId).text("0%");
			fromObj.find("#up_tbody"+defaults.customId).empty();
			fromObj.find("#confirm"+defaults.customId+",#start_upload"+defaults.customId+",#button_reset"+defaults.customId).prop("disabled",true);
			xhr.abort();
		},
		selectFile:function(path,htmlThis,formObj,defaults){//选择了文件后处理
			var up_tbody = formObj.find("#up_tbody"+defaults.customId);
			if(htmlThis.files!=null&&htmlThis.files.length>0){
				formObj.data = htmlThis.files;
				var file_name = "",
					file_format = "",
					file_format_error = "",
					file_size = 0;
					strs = "";
				$.each(htmlThis.files,function(i){
                    //上传的文件格式
					var thisObj = this;//一定是html对象，jquery对象不好使！！
					//文件总容量
                    file_size += thisObj.size;
                    //文件名称
            		file_name = thisObj.name;
            		if(file_format != ""){
            			file_format += ","+file_name.substr(file_name.lastIndexOf("."),file_name.length);
            		}else{
            			file_format = file_name.substr(file_name.lastIndexOf("."),file_name.length);
            		}
					strs += "<tr><td title='"+thisObj.name+"' >"+thisObj.name+"</td><td class='state' val='wait'>等待上传</td></tr>";
				});
				if(file_size>defaults.fileSize){
					$.uploadFun.clearUpload(formObj,defaults);
					formObj.find("#progress_status"+defaults.customId).text("附件超过最大上传文件限制，最大文件限制为"+(defaults.fileSize/1024/1024)+"M");
					formObj.find("#up_tbody"+defaults.customId).find("td[class='state']").text("失败");
					return;
				}else{
					up_tbody.append(strs);
				}
				
				//查出不支持的文件格式
            	if(defaults.format == null){
            		defaults.format = [];
		    	}
            	var file_format_arry = file_format.split(",");
        		for(index in file_format_arry){
        			if(defaults.format.indexOf(file_format_arry[index]) == -1){
            			if(file_format_error != ""){
            				if(file_format_error.indexOf(file_format_arry[index]) == -1){
            					file_format_error += " "+file_format_arry[index];
            				}
            			}else{
            				file_format_error = file_format_arry[index];
            			}
            		}
        		}
        		//长度为0就默认支持所有格式
            	if(defaults.format.length == 0){
            		file_format_error = "";
            	}
            	//文件中有不支持的格式就不再上传
        		if(file_format_error != ""){
					$.uploadFun.clearUpload(formObj,defaults);
					formObj.find("#progress_status"+defaults.customId).text("上传内容不支持"+file_format_error+"格式");
					formObj.find("#up_tbody"+defaults.customId).find("td[class='state']").text("失败");
             		return;
        		}
				$.uploadFun.waitUpload(formObj,defaults);
			}
		},
		uploadAjax:function(self,fileDatas){//上传
			//console.log("init："+xhr.readyState +" >->->-> "+xhr.status);//未开始
	        xhr.open("POST", self.defaults.url, true);//开启
	        xhr.upload.timeout = 2000;//延时设定
	        //超时
	        xhr.upload.ontimeout = function (event){
	        	$.uploadFun.errorUploadStatus(self.fromObj,self.defaults,-1);
	        };
	        //请求成功
	        xhr.upload.addEventListener("load", function(event){
	        	//console.log("请求成功，开始上传："+xhr.readyState +" >->->-> "+xhr.status);
	        }, false);
	        //进度
	        xhr.upload.addEventListener("progress",function(e){
	        	//console.log("progress："+xhr.readyState +" >->->-> "+xhr.status);
	        	$.uploadFun.exeUpload(self.fromObj,self.defaults,parseFloat(((e.loaded / e.total) * 100).toFixed(2)));
	        }, false);
	        //执行中
	        xhr.onreadystatechange = function () {
	        	//console.log("onreadystatechange："+xhr.readyState +" >->->-> "+xhr.status);
	        	if(xhr.status>200){
	        		$.uploadFun.errorUploadStatus(self.fromObj,self.defaults,xhr.status);
	        	}else{
	        		if(xhr.readyState==4){
	        			var rest = xhr.responseText;
	        			if(rest==null||rest==""){
	        				$.uploadFun.errorUploadStatus(self.fromObj,self.defaults,-2);
	        			}else{
	        				rest = JSON.parse(rest);
	        				if(rest.success==true){
	        					$.uploadFun.successUploadStatus(self.fromObj,self.defaults);
	        					self.restObj = rest;
	        				}else{
	        					$.uploadFun.errorUploadStatus(self.fromObj,self.defaults,-2,rest.message);
	        				}
	        			}
	        		}
	        	}
	        };
	        //失败
	        xhr.upload.addEventListener("error", function(event){
	        	$.uploadFun.errorUploadStatus(self.fromObj,self.defaults,-2);
	        }, false);
	        
	        xhr.send(fileDatas);//提交
	        return xhr;
		},
		errorUploadStatus:function(fromObj,defaults,flg,message){//上传失败后的状态
			//type:-1 请求超时，404 未找到地址 ，500 内部服务器错误
			var progress_status_val = "",
				td_status_val="失败";
			if(flg==-1){
				progress_status_val = "上传失败，请求服务器超时！";
				td_status_val = "超时";
			}else if(flg==404){
				progress_status_val = "无法连接服务器！";
			}else if(flg==500){
				progress_status_val = "上传失败，服务器内部发送错误！";				
			}else{
				progress_status_val = "上传失败！"+message;
			}
			xhr.abort();
			//progress-bar-danger
			fromObj.find("#progress_div"+defaults.customId).removeClass("active").addClass("progress-bar-danger");
			fromObj.find("#progress_status"+defaults.customId).text(progress_status_val);
			fromObj.find("#up_tbody"+defaults.customId).find("td[class='state']").text(td_status_val);
			//按钮： Close与重置可用
			fromObj.find("#but_close"+defaults.customId+",#button_reset"+defaults.customId).prop("disabled",false);
		},
		successUploadStatus:function(fromObj,defaults){//上传成功后的状态
			fromObj.find("#progress_div"+defaults.customId).removeClass("active").addClass("progress-bar-success");
			fromObj.find("#progress_status"+defaults.customId).text("上传成功");
			fromObj.find("#up_tbody"+defaults.customId).find("td[class='state']").text("成功");
			//按钮： 关闭/重置/确认可用
			fromObj.find("#confirm"+defaults.customId+",#but_close"+defaults.customId+",#button_reset"+defaults.customId).prop("disabled",false);
		},
		waitUpload:function(formObj,defaults){//等待上传
			formObj.find("#progress_div"+defaults.customId).css("width",0);
			formObj.find("#progress_status"+defaults.customId).text("0 %");
			formObj.find("#start_upload"+defaults.customId+",#button_reset"+defaults.customId).prop("disabled",false);
		},
		startUpload:function(self,fileDatas){//开始上传，及点击“开始上传按钮”
			self.fromObj.find("#start_upload"+self.defaults.customId+",#button_reset"+self.defaults.customId+",#but_close"+self.defaults.customId+",#path"+self.defaults.customId).prop("disabled",true);
			$.uploadFun.uploadAjax(self,fileDatas);//开启数据传输
		},
		exeUpload:function(fromObj,defaults,pro){//执行上传中
			pro = pro+"%";
			fromObj.find("#progress_status"+defaults.customId).text(pro);
			fromObj.find("#progress_div"+defaults.customId).css("width",pro);
			fromObj.find("#up_tbody"+defaults.customId).find("td[class='state']").text(pro);
        	//console.log("progress:"+xhr.readyState +" >->->-> "+xhr.status+" ->->->->->" );
		},
//		completeUpload:function(formObj){//上传完成
//			formObj.find("#button_reset,#but_close,#confirm").prop("disabled",false);
//		},
		initUploadHangClick:function(self){
			//打开 上传窗口
			self.click(function(){
				self.fromObj.find('#uploadDialog'+self.defaults.customId).modal({
					backdrop:'static',
					keyboard:'false'
				});
			});
			if(self.fromObj!=null){
				var fromObj = self.fromObj,
					but_close = fromObj.find("#but_close"+self.defaults.customId),
					button_reset = fromObj.find("#button_reset"+self.defaults.customId),
					confirm = fromObj.find("#confirm"+self.defaults.customId),
					start_upload = fromObj.find("#start_upload"+self.defaults.customId),
					path = fromObj.find("#path"+self.defaults.customId);
				
				path.on('change',function(){//选择了文件
					fromObj.find("#progress_div"+self.defaults.customId).css("width",0);
					fromObj.find("#progress_status"+self.defaults.customId).text("0%");
					fromObj.find("#up_tbody"+self.defaults.customId).empty();
					fromObj.find("#confirm"+self.defaults.customId+",#start_upload"+self.defaults.customId).prop("disabled",true);
					$.uploadFun.selectFile(path,this,fromObj,self.defaults);
				});
				but_close.on('click',function(){//关闭
					$.uploadFun.clearUpload(fromObj,self.defaults);//初始化后在关闭
				});
				button_reset.on('click',function(){//重置既初始化
					$.uploadFun.clearUpload(fromObj,self.defaults);
				});
				confirm.on('click',function(){//确认
					var isFun = $.isFunction(self.defaults.uploadSuccComplete) ? true : false;
					if(isFun){
						self.defaults.uploadSuccComplete.call(this,self.restObj);
					}else{
						var selfObj = self;
						var forid=selfObj.attr("forid"),
							forname=selfObj.attr("forname");
						var forIdsObj = $("#"+forid),
							forNamesObj = $("#"+forname),
							files = self.restObj.files,
							strIds = "",
							strNames="";
//						console.log(files);
						
						$.each(files,function(i,obj){
//							console.log(obj.fileId+"-"+obj.fileName);
							if(strIds!=""){strIds = strIds+",";}
							if(strNames!=""){strNames =strNames+",";}
							strIds =strIds+obj.fileId;
							strNames =strNames+obj.fileName;
						});
						console.log(forid+"   "+strIds+"----"+strNames);
						forIdsObj.val(strIds);
						forNamesObj.val(strNames);
					}
					$.uploadFun.clearUpload(self.fromObj,self.defaults);
					self.fromObj.find('#uploadDialog'+self.defaults.customId).modal("hide");
					//初始化Xhr对象
					initXhr();
				});
				start_upload.on('click',function(){//开始上传
					//取得表单数据
					var formDatas = new FormData(fromObj[0]);  
					$.uploadFun.startUpload(self,formDatas);
				});
			}
		}
	});
	//扩展方法，外部方法，外部调用 
	//$.uploadFun.extend({	 });	
	//初始化 最后执行
	$.fn.uploadFun = function( options ) {
		var self = $(this);
		var defaults  = {
			url : "",//上传指定路径
			name:"fileObj",//上传文件对象名称
			isMultiple : true,//是否单上传 'false':否 'true':是
			format : [],//允许的文件格式，值为空等于默认所有
			fileSize : 50*1024*1024,//总的文件大小上限 50M
			customId : "",
			uploadSuccComplete : null //确认表单后执行的函数
		};
		//初始化Xhr对象
		initXhr();
		
		self.defaults = $.extend(false,defaults, options);//合并参数
		self.fromObj=null;//上传HTML模块对象
		self.restObj=null;//返回结果集
		$.uploadFun.initUploadHtmlDom(self);//初始化
		$.uploadFun.clearUpload(self.fromObj,self.defaults);
		$.uploadFun.initUploadHangClick(self);//事件挂载
	};
})(jQuery);