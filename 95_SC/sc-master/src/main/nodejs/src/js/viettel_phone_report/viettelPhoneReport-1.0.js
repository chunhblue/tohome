require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");


define('viettelPhoneReport',function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        startTime = '',
        endTime = '',
        toThousands = null,
        page = 1, // 当前页数, 初始为 1
        rows = 10; // 每页显示条数, 默认为 10;

  var m={
      toKen: null,
      use: null,
      search: null,
      startDate:null,
      endDate:null,
      pd_date:null,
      pd_start_time:null,
      pd_end_time:null,
      searchJson:null,
      reset:null,
      dailyTable:null,
      export:null,
  }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }
    function init(_common){
        createJqueryObj();
        url_left = _common.config.surl + "/viettelPhoneReport";
        toThousands=_common.toThousands;
        // 初始化检索日期
        _common.initDate(m.startDate,m.endDate);
        m.pd_start_time.val("06:00:00");
        m.pd_end_time.val("23:59:59");
        but_event();
    }
     var but_event=function () {

         m.startDate.on('change',function () {
             let piDate = formatDate(m.startDate.val());
             piDate = fmtIntDate(piDate);
             startTime = piDate+' 06:00:00';
             endTime = piDate+' 23:59:59';
             m.pd_start_time.val("");
             clearStartTime();
             if (piDate) {
                 m.pd_start_time.prop('disabled',false);
             } else {
                 m.pd_start_time.prop('disabled',true);
             }
         })
         m.endDate.on('change',function () {
             let piDate = formatDate(m.endDate.val());
             piDate = fmtIntDate(piDate);
             startTime = piDate+' 06:00:00';
             endTime = piDate+' 23:59:59';
             clearTime();
             if (piDate) {
                 m.pd_end_time.prop('disabled',false);
             } else {
                 m.pd_end_time.prop('disabled',true);
             }
         })
         m.search.click(function () {
             if(verifySearch()) {
                 _common.loadPaging(1,1,1,10);
                 page=1;
                 setParamJson();
                 getData(page,rows);
             }
         })
         m.reset.click(function () {

         m.startDate.val("");
         m.endDate.val("");
         m.pd_start_time.val("");
         m.pd_end_time.val("");
             page=1;
         _common.resetPaging();
         $("#dailyTable").find("tr:not(:first)").remove();
         })
         m.export.click(function () {
             if(verifySearch()) {
                 // 拼接检索参数
                 setParamJson();
                 paramGrid = "searchJson=" + m.searchJson.val();
                 var url = url_left + "/export?" + paramGrid;
                 window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
             }
         })

 }
 var setParamJson=function () {
    startDate=formatDate(m.startDate.val());
    pd_start_time=formatTime(m.pd_start_time.val());
    startDateTime=startDate+pd_start_time;
     endDate=formatDate(m.endDate.val());
     pd_end_time=formatTime(m.pd_end_time.val());
     endDateTime=endDate+pd_end_time;
     let searchJsonStr = {
         'page': page,
         'rows': rows,
         'startDateTime': startDateTime,
         'endDateTime': endDateTime,
     };
     m.searchJson.val(JSON.stringify(searchJsonStr));


 }
 var getData=function () {
     let record = m.searchJson.val();
     $.myAjaxs({
         url: url_left + "/search",
         async: true,
         cache: false,
         type: "post",
         data: 'jsonStr=' + record,
         dataType: "json",
         success: function (result) {
             let trList = $("#dailyTable  tr:not(:first)");
             trList.remove();
             if (result.success) {
                 let dataList = result.o.data;
                 // 总页数
                 let totalPage = result.o.totalPage;
                 // 总条数
                 let count = result.o.count;
                 for (let i = 0; i < dataList.length; i++) {
                     var item = dataList[i];
                     let tempTrHtml = '<tr>' +
                         '<td title="' + fmtIntDateTime(item.requestDate) + '" style="text-align:center;">' + fmtIntDateTime(item.requestDate) + '</td>' +
                         '<td title="' + isEmpty(item.requestId) + '" style="text-align:right;">' + isEmpty(item.requestId) + '</td>' +
                         '<td title="' + isEmpty(item.orderId) + '" style="text-align:right;">' + isEmpty(item.orderId) + '</td>' +
                         '<td title="' + isEmpty(item.receiptNo) + '" style="text-align:right;">' + isEmpty(item.receiptNo) + '</td>' +
                         '<td title="' + toThousands(item.transAmt) + '" style="text-align:right;">' + toThousands(item.transAmt) + '</td>' +
                         '<td title="' + isSuccess(item.status) + '" style="text-align:right;">' + isSuccess(item.status) + '</td>' +
                         '<td title="' + isEmpty(item.message) + '" style="text-align:right;">' + isEmpty(item.message) + '</td>' +
                         '</tr>';
                     m.dailyTable.append(tempTrHtml);
                 }
                 // 加载分页条数据
                 _common.loadPaging(totalPage,count,page,rows);
             }
             // 激活 分页按钮点击
             but_paging();
         },
         error: function (e) {

         }
     });
 }    // 分页按钮事件
    var but_paging = function () {
        $('.pagination li').on('click',function () {
            page = $($(this).context).val();
            if(verifySearch()){
                // 拼接检索参数
                setParamJson();
                // 分页获取数据
                getData(page,rows);
            }
        });
    }

 var  verifySearch=function () {
     if(m.startDate.val()==""||m.startDate.val()==null){
         _common.prompt("Please enter a start date!",5,"error"); // 开始日期不可以为空
         $("#startDate").focus();
         $("#startDate").css("border-color","red");
         return false;
     }else {
         $("#startDate").css("border-color","#CCC");
     }
     if(m.endDate.val()==""||m.endDate.val()==null){
         _common.prompt("Please enter a end date!",5,"error"); // 结束日期不可以为空
         $("#endDate").focus();
         $("#endDate").css("border-color","red");
         return false;
     }else {
         $("#endDate").css("border-color","#CCC");
     }
     if(new Date(fmtDate(m.startDate.val())).getTime()>new Date(fmtDate(m.endDate.val())).getTime()){
         $("#endDate").focus();
         _common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
         return false;
     }
     if(m.startDate.val()!=""&&m.endDate.val()!=""){
         var _StartDate = new Date(fmtDate($("#startDate").val())).getTime();
         var _EndDate = new Date(fmtDate($("#endDate").val())).getTime();
         var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
         if(difValue >62){
             _common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
             $("#endDate").focus();
             return false;
         }
     }
     return true ;
 }
    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }
    var isEmpty = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
        return str;
    };
    var isSuccess = function (str) {
        if (str == null || str == undefined || str == '') {
            return '';
        }
       if (str=="10"){
        return "Success";
       }else {
        return "Fail";
       }
    };
 var clearStartTime=function () {
    m.pd_start_time.val("");
     m.pd_start_time.datetimepicker('remove');
     //开始时间
     m.pd_start_time.datetimepicker(
         {
             language: 'en',
             autoclose: true,//选中之后自动隐藏日期选择框
             clearBtn: false,//清除按钮
             todayBtn: false,//今日按钮
             format: 'hh:ii:ss',
             startView: 'day',// 进来是月
             minView: 'hour',// 可以看到小时
             maxView: 1,
             minuteStep:1, //分钟间隔为1分
             todayHighlight: false,
             forceParse: true,
             startDate: new Date(startTime),
             endDate: new Date(endTime),
         }).on('changeDate', function (ev) {
         if (ev.date) {
             $("#pd_end_time").datetimepicker('setStartDate', ev.date)
         } else {
             $("#pd_end_time").datetimepicker('setStartDate', null);
         }
     });
 }
    //清空日期
  var clearTime = function () {
        m.pd_end_time.val("");
        m.pd_end_time.datetimepicker('remove');
        //结束时间
        m.pd_end_time.datetimepicker(
            {
                language: 'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: false,//今日按钮
                format: 'hh:ii:ss',
                startView: 'day',// 进来是月
                minView: 'hour',// 可以看到小时
                maxView: 1,
                minuteStep:1, //分钟间隔为1分
                todayHighlight: false,
                forceParse: true,
                startDate: new Date(startTime),
                endDate: new Date(endTime),
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#pd_start_time").datetimepicker('setEndDate', ev.date)
            } else {
                $("#pd_start_time").datetimepicker('setEndDate', null);
            }
        });
    }
    // 06/03/2020 -> 20200306
    var formatDate = function (dateStr) {
        dateStr = dateStr.replace(/\//g, '');
        var day = dateStr.substring(0, 2);
        var month = dateStr.substring(2, 4);
        var year = dateStr.substring(4, 8);
        return year + month + day;
    }
    //格式化数字类型的日期
    function fmtIntDateTime(date) {
        var res = "";
        // res = date.substring(0, 4) + "/" + date.substring(5, 7) + "/" + date.substring(8, 10)+" "+date.substring(10,20);
        res = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4)  +" "+date.substring(10,20);
        return res;
    }
    // 17:05:16 -> 170516
    var formatTime = function (str) {
        return str.replace(/:/g,'');
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('viettelPhoneReport');
_start.load(function (_common) {
    _index.init(_common);
});

