/* ajax上传
	 */
(function($){
		$.fn.extend({
			ajax:function(opt){
		    	var xhr = new XMLHttpRequest();
		        opt = opt || {};
		        opt.method = opt.method.toUpperCase() || 'POST';
		        opt.url = opt.url || '';
		        opt.async = opt.async || "true";
		        opt.data = opt.data || null;
		        opt.success = opt.success || function () {};	
		        opt.timeout = opt.timeout || 2000;
		        if(opt.async == "true"){
		        	opt.async = true;
		        }else{
		        	opt.async = false;
		        }
		        xhr.open(opt.method, opt.url, opt.async);
		        //设置请求超时
		        xhr.upload.timeout = opt.timeout;
		        xhr.upload.ontimeout = function (event){
		        	xhr.abort(); 
		        };
		        //添加progress事件
		        xhr.upload.addEventListener("progress",function(e){
		           
		        }, false);
		        //上传完成
		        xhr.upload.addEventListener("load", function(event){
		        }, false);
		        xhr.upload.addEventListener("error", function(event){
		        }, false);
		        xhr.upload.addEventListener("abort", function(event){
		          }, false);
		        xhr.send(opt.data);
		        xhr.onreadystatechange = function () {
		            if (xhr.readyState == 4 && xhr.status == 200) {
		            	opt.success(xhr.responseText);
		            }
		            if (xhr.readyState == 4 && xhr.status != 200) {
		            	opt.error(xhr);
		            }
		        };
		        return xhr;
			}
		});
})(jQuery);
