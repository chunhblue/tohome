require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("JsBarcode");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('priceLabelPrint', function () {
    var self = {};
    var url_left = "",
        systemPath = "";
    var m = {
        reset: null,
        params:null,
        toKen: null,
        use: null,
        searchJson: null,
        flg: null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/priceLabel";
        // 获取需要打印的数据
        getPrintData();
    }
    // 获取需要打印的数据
    var getPrintData = function() {
        let searchJson = m.searchJson.val();
        let params = m.params.val();

        // 解码
        let listJson = decodeURIComponent(params);
        searchJson = decodeURIComponent(searchJson);

        let list = null;

        // 转json
        if (listJson !== '') {
            list = JSON.parse(listJson);
        }
        if (searchJson !== '') {
            searchJson = JSON.parse(searchJson);
        }

        // 根据选中的数据生成价签, 并打印
        if (m.flg.val()=='print') {
            // 将结果生成价钱数据
            appendPriceLabelHtml(list);
            window.print();
        }
        // 根据条件打印所有的价签数据
        if (m.flg.val()=='printAll') {
            getPriceLabe(searchJson);
            window.print();
        }
        // 根据选中的数据生成价签, 查看
        if (m.flg.val()=='preview') {
            appendPriceLabelHtml(list);
        }
    }

    // 根据条件查询价钱数据, 并生成价签
    var getPriceLabe = function (searchJson) {
        $.myAjaxs({
            url:url_left+"/print/getPrintData",
            async:false,
            cache:false,
            type :"post",
            data :"searchJson="+searchJson,
            dataType:"json",
            success : function (result) {
                if (result==null || result.length < 1) {
                    _common.prompt("No data found!",5,"error");
                    return;
                }
                // 将结果生成价钱数据
                appendPriceLabelHtml(result);
            },
            complete:_common.myAjaxComplete
        });
    }

    // 遍历生成价签 result.length
    var appendPriceLabelHtml = function(list) {

        for (let i = 0; i < list.length; i++) {
            let item = list[i];

            let priceLabel = '<div class="price_label_content" style="min-width: 250px;width: 300px; height: 180px;margin: 5px;padding: 3px;border: #999999 1px dotted;display: inline-block;">' +
                '<h4 style="text-align: center;font-weight: bold;"><span>'+_common.toThousands(item.newPrice)+'</span> VND</h4>' +
                '<h6 style="padding-left: 3px">DC//</h6>' +
                '<h5 style="text-align: center;font-weight: bold">'+item.priceLabelVnName+'</h5>' +
                '<h6 style="text-align: center;">'+item.priceLabelEnName+'</h6>' +
                '<div style="text-align: center">' +
                '    <img id="'+(item.storeCd +item.barcode+i)+'"/>' +
                '</div>' +
                '</div>';

            $('.price_label_box').append(priceLabel);

            $('#'+(item.storeCd + item.barcode+i )).JsBarcode(item.barcode,{
                width: 1.5,
                height: 30,
                fontOptions: 'bold',
                fontSize: 10,
            });
        }
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('priceLabelPrint');
_start.load(function (_common) {
    _index.init(_common);
});
