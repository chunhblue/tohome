/*********************/
/*** Index func ******/
/*** Ver:20180117 ****/
/** ****************** */
var $ = require("jquery");
define('search', function() {
			var self = {};
			function initWin() {
			}
			//回车事件挂载 
			//按照元素顺序依次排列往下循环寻找对应的可用的元素，并且设置改元素的焦点。
			function enterClickSearchElement(){
				var elements = $("#searchBox").find('.select2-focusser');
				elements.each(function(i,element){
					$(element).unbind('keyup');
					$(element).bind("keyup",function(e){
						e.stopPropagation();
							if(e.keyCode == 13 && !e.ctrlKey){
								e.preventDefault();
								if($(this).hasClass('add-search-code-input')){
									if($(this).val()!=""){
										var toelement = $(this).attr("toelement");
										$("#"+toelement).click();
									}else{
										var isgo = true,ns=i;
										while(isgo){
											ns++;
											if(typeof ($(elements[ns]).attr("disabled")) == "undefined"){
												$(elements[ns]).focus();
												isgo = false;
											}
											if(ns>=elements.length){
												$("#search").focus();
												isgo = false;
											}
										}
									}
								}else{
									var isgo = true,ns=i;
									while(isgo){
										ns++;
										if(typeof ($(elements[ns]).attr("disabled")) == "undefined"){
											$(elements[ns]).focus();
											isgo = false;
										}
										if(ns>=elements.length){
											$("#search").focus();
											isgo = false;
										}
									}
								}
							}else if(e.keyCode == 13 && e.ctrlKey){
								//提交表单
								$("#search").click();
								return false;
							}
							return true;
						});
					});
			}
			self.initWin = initWin;//初始化
			//self.enterClickSearchElement = enterClickSearchElement;//回车事件
			return self;
		});

var _start = require('start');
var _search = require('search');
_start.load(function () {
    _search.initWin();
});
