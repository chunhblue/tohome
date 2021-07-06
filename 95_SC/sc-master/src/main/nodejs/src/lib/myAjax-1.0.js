/* 
* ajax封装1.0
* 返回json结构：AjaxResultDto
*  状态 :success
*  消息:message;
*  状态码 :toKen
*  重复提交:repeat
*  返回的数据 rows<T>;
* 
*/
;(function($) {
	$.extend({
		myAjaxs:function(options){
			var defaults = {
				url:"",//规定发送请求的 URL。默认是当前页面
				data:"",//规定要发送到服务器的数据
				type:"GET",//规定请求的类型（GET 或 POST）。默认GET
				dataType:"JSON",//预期的服务器响应的数据类型。默认json
				cache:false,//布尔值，表示浏览器是否缓存被请求页面。默认是 false。
				async:true,//布尔值，表示请求是否异步处理。默认是 true。
                processData:true,//布尔值，用于对data参数进行序列化处理。
                contentType: "application/x-www-form-urlencoded; charset=utf-8",//发送信息至服务器时内容编码类型
                mimeType: null,//模拟类型
				timeout:30000, //设置本地的请求超时时间（以毫秒计）。30秒
				beforeSend:null,//发送请求前运行的函数
				complete:null,//请求完成时运行的函数（在请求成功或失败之后均调用，即在 success 和 error 函数之后）。
				error:null,//如果请求失败要运行的函数。
				success:null //当请求成功时运行的函数
			};
			
			defaults = $.extend(false,defaults, options);//合并参数
			$.ajax({
				url: defaults.url,//规定发送请求的 URL。默认是当前页面
				cache: defaults.cache,//布尔值，表示浏览器是否缓存被请求页面。默认是 true。
				async: defaults.async,//布尔值，表示请求是否异步处理。默认是 true。
				dataType:defaults.dataType,//预期的服务器响应的数据类型
				contentType:defaults.contentType,
                processData:defaults.processData,
                mimeType: defaults.mimeType,
				data:defaults.data,//规定要发送到服务器的数据
				type:defaults.type,//规定请求的类型（GET 或 POST）。默认GET
				timeout:defaults.timeout,//设置本地的请求超时时间（以毫秒计）。
				beforeSend:function(xhr){//发送请求前运行的函数
					var isFun = $.isFunction(defaults.beforeSend) ? true : false;
					if(isFun){
						defaults.beforeSend.call(this,xhr);
					}
				},
				complete:function(xhr,status){//请求完成时运行的函数（在请求成功或失败之后均调用，即在 success 和 error 函数之后）。
					var isFun = $.isFunction(defaults.complete) ? true : false;
					if(isFun){
						defaults.complete.call(this,xhr,status);
					}
				},
				success:function(result,status,xhr){
					//success
					var isFun = $.isFunction(defaults.success) ? true : false;
					if(isFun){
						defaults.success.call(this,result,status,xhr);
					}else{
						return null;
					}
				},
				error:function(xhr,status,error){
					//options
					var isFun = $.isFunction(defaults.error) ? true : false;
					if(isFun){
						defaults.error.call(this,xhr,status,error);
					}else{
						return null;
					}
				}
			});
		}
	});
})(jQuery);