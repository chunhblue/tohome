define('msg', function () {
    var self = {},
    	common=null,
    	url = null,
    	countdown = null,
    	pointObj = null;
    
    function init(_common) {
    	common = _common;
    	url=$("#url").val();
		countdown=null;
		pointObj=null;
			defaults();
			if(url!=""){
				setIntervalTime();
			}else{
				$("#box_2").hide();
			}
			$("#toUrlButton").click(function(){
				//跳转到指定页面
				toPage();
			});
			var text=$("#msg-text-span").text().replace(/●/g,"<br/>");
			$("#msg-text-span").html(text);
			
    }
    //默认提示文字
    function defaults(){
    	var objText = $("#msg-text-span");
    	if(objText.text()==""){
    		objText.text("Operation failed!");
    	}
    }
    //是否开启
    function setIntervalTime(){
    	countdown = setInterval(timer,1000);
    	pointObj = setInterval(pointFunc,200);
    }
    //计算
    function timer(){
    	var obj = $("#second");
    	var time = Number(obj.text());
    	time--;
    	if(time<0){
    		clearInterval(countdown);
    		clearInterval(pointObj);
    		toPage();
    	}else{
    		obj.text(time);
    	}
    }
    function pointFunc(){
    	var obj = $("#point");
    	var str = obj.text()+".";
    	if(str.length>15){
    		str = "."
    	}
    	obj.text(str);
    }
    function toPage(){
    	var bool = url.substr(0,4);
    	if(bool=="http"){
    		//window.location = url;
    		top.window.location = url;
    	}else{
    		window.location=url;
    	}
    }
    
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('msg');
_start.load(function (_common) {
    _index.init(_common);
});
