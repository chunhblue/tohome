/**
 * 系统初始化基础js，包含了jquery和一些其他必要资源的加载，并提供js加载顺序，可根据具体需要进行修改。
 */
require("bootstrap.css");
require("messenger.css");
require("messenger-theme-future.css");
require("basis.css");
//require("zgGrid.css");

define('start', ['bootstrap', 'ajaxIntercept', 'myAjax'], function (_bootstrap, _ajaxIntercept, _myAjax) {
    var self = {},
        todoIntervalObj = null,
        todoBox = null,
        todoCountObj = null;
    //function load(核心业务js,其他js方法【可省略】)
    //加载
    function load(startJsObj, endJsObj) {
        $(document).ready(function () {
            var errorStr = null;
            try {
                //_common:默认配置
                var dfd = $.Deferred();
                dfd
                    .done(_common.boxScaling())
                    .done(_common.listenCleanDateBut())
                    .done(_common.initMyConfirm())
                    .done($.isFunction(startJsObj) ? startJsObj.call(this, _common) : null)
                    .done($.isFunction(endJsObj) ? endJsObj.call(this, _common) : null);
                rest = true;
                // 去掉所有input的autocomplete, 显示指定的除外
                $('input:not([autocomplete]),textarea:not([autocomplete]),select:not([autocomplete])').attr('autocomplete', 'off');
            } catch (err) {
                errorStr = err
                console.log("X- Error name: ", err.name);
                console.log("X- Error message: ", err.message);
                console.log("X- Error err: ", err);
                alert("'JS'加载失败，请刷新重试！");
            } finally {
                _common.loading_close();
                console.log("loading initLoad() run ok ! rest: " + errorStr);
            }
        });
    }

    //设置控件默认选中值
    function setSelectOption(id,value){
        $("#"+id).val(value).trigger("change");
    }

    //设置select2的可用禁用状态
    function setSelect2Status(id,status){
        var obj = $("#"+id);
        var width_select = $("#s2id_"+id).width();
        if(status=="false"){
            obj.val("").trigger("change").prop("disabled",true);
            //判断浏览器是否为IE内核
            // if($.browser.msie) {
            //     obj.select2({width:width_select,dropdownAutoWidth:true});
            // }
        }else{
            obj.val("").trigger("change").prop("disabled",false);
        }
    }

    //备用
    var initLoad = function () {
        _common.loading();
        _common.boxScaling();
        return true;
    }
    self.load = load;
    self.setSelectOption = setSelectOption;
    self.setSelect2Status = setSelect2Status;

    return self;
});
