require("select2");
require("select2.css");
require("myAutoComplete.css");
require("myAutomatic");
require("myValidator.css");
var _myValidator = require("myValidator");
define('login', function () {
    var self = {},
    	common=null;
    
    function init(_common) {
        // 验证码刷新
    	common = _common;
        $("#codelink").on("click",function(){
        	$("#codeimg").attr("src",common.config.surl+"/captcha?date=" + new Date());
        });
        console.log($("#errMsg").val()=='')
        if($("#errMsg").val()!=''){
        	$("#msg").show();
			$("#msgTest").text($("#errMsg").val());
		}else{
			$("#msg").hide();
		}
       // _myValidator.init(); //验证
    }
    // 验证码刷新
    /*function resh(){
        // 获取现在时间的原始值
        var timestamp = (new Date()).valueOf();
        console.log(timestamp);
        // 取出原来的src
        // var url = $("#vimg").attr("src");
         var url = common.config.surl;
        // 在原src后面拼接时间戳
        url = url + "?timestamp=" + timestamp;
        // 将改变后的url赋值给src
        $("#vimg").attr("src",url);
    }*/
    
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('login');
_start.setSelect2Status("store","true");
_start.load(function (_common) {
    _index.init(_common);
});
