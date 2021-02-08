var $=require("jquery");
var _myAjax=require("myAjax");
require('bootstrap.css');

define('onlineExport',function(){
	var self={};
	var _common = null;
	var expKey = null;

	function init(common){
		_common = common;
		expKey = $("#expKey").val();
		expChk();
	}

	var chkTimer = '';
	// console.log("expKey",expKey);
	function expChk(){
		$.ajax({
			url:  _common.config.surl + "/expcheck",
			async: false,
            cache: false,
            type: "post",
            dataType: "json",
			data: 'key='+expKey,
		})
		.done(function(data, textStatus, jqXHR) {
			if( jqXHR.getResponseHeader("errStatus") == 2 ){
				//alert("登陆超时");
				clearInterval(todoCntTimer);
				return;
			}
			if( jqXHR.getResponseHeader("errStatus") == 1 ){
				//alert("没权限");
				clearInterval(todoCntTimer);
				return;
			}
			if( data == null ){
				if(chkTimer == ''){
					chkTimer = setInterval(expChk,5000);
				}
			}else{
				if(data.status == 2){
					if(chkTimer != ''){
						clearInterval(chkTimer);
						chkTimer = '';
					}
					$('#msg').html("Data loading sucessfully! <a class='downloadExpor' href= '"+_common.config.surl+"/export/"+expKey+"'>Download</a>");
					$("#msg").show();
					$("#loading").hide();
				}else if(data.status == 3){
					if(chkTimer != ''){
						clearInterval(chkTimer);
						chkTimer = '';
					}
					$('#msg').text("Data loading error, Please try again.");
					$("#msg").show();
					$("#loading").hide();
				}else{
					if(chkTimer == ''){
						chkTimer = setInterval(expChk,5000);
					}
				}
			}
		})
		.fail(function(data, textStatus, jqXHR) {
			console.log("error");
		})
	}

    self.init = init;
  	return self;	
});
var _onlineExport=require('onlineExport');
_start.load(function (common) {
    _onlineExport.init(common);
});