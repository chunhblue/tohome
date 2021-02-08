require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax=require("myAjax");
define('stockPLQuery', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储

        searchAccDate = null,
        searchDepCd = null,
        searchPmaCd = null,
        searchQNS = null;//query_no_stock

        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,
        // 查询部分
        sd_date:null,
        dep:null,
        pma:null,
        cat:null,
        scat:null,
        search:null,
        reset:null,

        first_st_complete:null,
        st_differ_confirm:null,
        query_no_stock:null,
        by_position:null,
        allocation_code:null,
        view:null,
        view_st_tale:null,
        last_qty_entry:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/stocktakePLQ";
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        initPageBytype("1");
        //表格内按钮事件
        table_event();

        getTDep();//加载部门下拉列表
        // m.search.click();

    }
    //表格按钮事件
    var table_event = function(){

    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                initTable1();//列表初始化
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    //拼接检索参数
    var setParamJson = function(){
        var accDate = m.sd_date.val();
        var depCd = m.dep.val();
        var pmaCd = m.pma.val();

        if(accDate == ""){
            searchAccDate = getFormatDate();
        }else{
            searchAccDate = fmtIntDate(String(accDate));
        }
        if(depCd == ""){
            searchDepCd = "01";
        }else{
            searchDepCd = depCd;
        }
        if(pmaCd == ""){
            searchPmaCd = "01";
        }else{
            searchPmaCd = pmaCd;
        }
        if($('#query_no_stock').is(':checked')){
            searchQNS = "0";
        }else{
            searchQNS = null;
        }
        // 创建请求字符串
        var searchJsonStr ={
            accDate : searchAccDate,
            depCd : searchDepCd,
            pmaCd : searchPmaCd,
            qns : searchQNS
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //验证检索项是否合法
    var verifySearch = function () {
        return true;
    }

    //画面按钮点击事件
    var but_event = function(){
        //盘点日期
        m.sd_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/query");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });
        m.reset.on("click",function(){
            m.sd_date.val("");
            m.dep.val("");
            m.pma.val("");
            m.query_no_stock.attr("checked", false);
            m.by_position.attr("checked", false);
            m.allocation_code.val("");
        });
    }

    //表格初始化
    var initTable1 = function(){
        // var data = '[{"depCd":"01","depName":"蔬菜","pmaCd":"11","pmaName":"蔬菜","categoryCd":"11","categoryName":"叶子菜","subCategoryCd":"01","subCategoryName":"叶子菜蔬菜","articleCount":"3","firstQty":"0","secondQty":"0","noQty":"3","piAccountFlgName":"是","piFirstFinishFlgName":"是","piCommitFlgName":"是"}]';
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Query Result",
            param:paramGrid,
            localSort: true,
            colNames:[
                "Item Code",
                "Barcode",
                "Item Name",
                "Spec",
                "Uom",
                "First StockTake Qty",
                "Second StockTake Qty",
                "Inventory",
                "First StockTake Differ Qty",
                "Sales Price",
                "Cost",
                "First StockTake Differ Amt"],
            colModel:[
                {name:"articleId",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"barcode",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"articleName",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"spec",type:"text",text:"center",width:"50",ishide:false,css:""},
                {name:"uom",type:"text",text:"center",width:"50",ishide:false,css:""},
                {name:"fStockQty",type:"text",text:"center",width:"160",ishide:false,css:""},
                {name:"sStockQty",type:"text",text:"center",width:"160",ishide:false,css:""},
                {name:"inventory",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"fStockDQty",type:"text",text:"center",width:"180",ishide:false,css:""},
                {name:"price",type:"text",text:"center",width:"100",ishide:false,css:""},
                {name:"cost",type:"text",text:"center",width:"80",ishide:false,css:""},
                {name:"fStockDAmt",type:"text",text:"center",width:"180",ishide:false,css:""},
            ],//列内容
            // traverseData:query_list,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:false,
            eachTrClick: function (trObj) {//正常左侧点击
                $("#update").removeAttr("disabled");
            },
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                {
                    butType:"custom",
                    butHtml:"<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"
                },{
                    butType: "download",
                    butId: "outExcel",
                    butText: "outExcel",
                    butSize: ""
                },

            ],
        });
    }

    var getTDep = function(){
        //Ajax请求大分类
        $.myAjaxs({
            url:url_left+"/getDep",
            async:true,
            cache:false,
            type :"get",
            dataType:"json",
            success:function(res){
                var htmlStr = '<option value="">--Please Select--</option>';
                $.each(res,function(ix,node){
                    htmlStr+='<option value="'+node.depCd+'">'+node.depName+'</option>';
                });
                m.dep.html(htmlStr);
            },
            complete:_common.myAjaxComplete
        });
    }

    //小数格式化
    var floatFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''){value = parseFloat(value);}
        return $(tdObj).text(value);
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtStringDate(value);
        }
        return $(tdObj).text(value);
    }
    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date){
        if(!!date) {
            return date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        }
    }
    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function fmtIntDate(date){
        if(!!date){
            return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
        }
    }

    //获取当前日期  20200106
    function getFormatDate() {
        var date = new Date();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentDate = date.getFullYear() + month + strDate;
        return currentDate;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('stockPLQuery');
_start.load(function (_common) {
    _index.init(_common);
});
