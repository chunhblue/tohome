require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
var _myAjax=require("myAjax");
define('custPlanPrint', function () {
    var self = {};
    var url_left = "",
        systemPath='';
    var m = {
        identity : null,
        grid_table : null,
        searchJson:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/custEntry/print";
        systemPath=_common.config.surl;
        getList();
    }

    //画面按钮点击事件
    var getList = function(){
        $.myAjaxs({
            url:url_left+"/getprintData",
            async:true,
            cache:false,
            type :"post",
            data :'searchJson='+m.searchJson.val(),
            success:function(result){
                var trList = $("#grid_table_p  tr:not(:first)");
                trList.remove();
                if(result.success){
                    let dataList = result.o;
                    console.log(dataList)
                    for (let i = 0; i < dataList.length; i++) {
                        var item = dataList[i];
                        let tempTrHtml = '<tr style="text-align: center;">' +
                            '<td style="text-align: right">' + isEmpty(item.storeCd) + '</td>' +
                            '<td style="text-align: left">' + isEmpty(item.storeName) + '</td>' +
                            '<td style="text-align: right">' + isEmpty(item.piCd) + '</td>' +
                            '<td style="text-align: left">' + isEmpty(item.reviewSts) + '</td>' +
                            '<td style="text-align: left">' + isEmpty(item.remarks) + '</td>' +
                            '<td style="text-align: left">' + isEmpty(item.createUserName) + '</td>' +
                            '<td style="text-align: center">' + _common.formatCreateDate(item.createYmd) + '</td>' +
                            '</tr>';
                        m.grid_table.append(tempTrHtml);
                        }
                }else {
                    _common.prompt("Print data exception！",5,"info");/*打印数据异常*/
                }
                // 打印
                window.print();
            },
            error : function(e){
                _common.prompt("Failed to load data!",5,"error");
            }
        });
    }

    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('custPlanPrint');
_start.load(function (_common) {
    _index.init(_common);
});
