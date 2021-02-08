require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('maItemSale', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        getThousands = null,
        common=null;
    const KEY = 'MATERIAL_ITEM_SALES_QUERY';
    var m = {
        reset: null,
        search: null,
        searchJson: null,
        aRegion : null,
        aCity : null,
        aDistrict : null,
        aStore : null,
        voucher_cd:null,//单据编号
        barcode:null,//barcode
        formulaArticleId:null,//商品id
        formulaArticleName:null,//商品名称
        tranSerialNo:null,//单号
        posId:null,//pos Id
        sale_start_date:null,//销售开始日期  查询
        sale_end_date:null,//销售结束日期  查询
        clear_sale_date:null//清空日期
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        getThousands=_common.getThousands;
        url_left =_common.config.surl+"/maItemSale";
        systemPath = _common.config.surl;
        //事件绑定
        but_event();
        //列表初始化
        initTable1();
        //表格内按钮事件
        table_event();
        // 初始化店铺运营组织检索
        _common.initOrganization();
        //权限验证
        isButPermission();
        // 初始化检索日期
        _common.initDate(m.sale_start_date,m.sale_end_date);
        initParam();

    }

    // 初始化参数, back 的时候 回显 已选择的检索项
    var initParam = function () {
        let searchJson=sessionStorage.getItem(KEY);
        if (!searchJson) {
            return;
        }
        let obj = eval("("+searchJson+")");
        // 取出后就清除
        sessionStorage.removeItem(KEY);
        // 不是通过 back 正常方式返回, 清除不加载数据
        if (obj.flg=='0') {
            return;
        }

        m.voucher_cd.val(obj.voucherCd);
        m.sale_start_date.val(obj.saleStartDate);
        m.sale_end_date.val(obj.saleEndDate);
        m.barcode.val(obj.barcode);
        m.formulaArticleId.val(obj.formulaArticleId);
        m.formulaArticleName.val(obj.formulaArticleName);
        m.tranSerialNo.val(obj.tranSerialNo);
        m.posId.val(obj.posId);
        // 设置组织架构回显
        _common.setAutomaticVal(obj);

        m.search.click();
    }

    // 保存 参数信息
    var saveParamToSession = function () {
        if (!paramGrid) {
            return;
        }
        let obj = {};
        paramGrid.replace(/([^?&]+)=([^?&]+)/g, function(s, v, k) {
            obj[v] = decodeURIComponent(k);//解析字符为中文
            return k + '=' +  v;
        });

        obj.regionName=$('#aRegion').attr('v');
        obj.cityName=$('#aCity').attr('v');
        obj.districtName=$('#aDistrict').attr('v');
        obj.storeName=$('#aStore').attr('v');
        obj.saleStartDate=m.sale_start_date.val();
        obj.saleEndDate=m.sale_end_date.val();
        obj.page=tableGrid.getting("page");
        obj.flg='0';
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }

    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:true,
            cache:false,
            type :"post",
            data :"codeValue="+code,
            dataType:"json",
            success:function(result){
                var selectObj = $("#" + selectId);
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+" Failed to load data!",5,"error");
            }
        });
    }

    function judgeNaN (value) {
        return value !== value;
    }

    // 验证检索项是否合法
    var verifySearch = function(){
        let _StartDate = null;
        if(!$("#sale_start_date").val()){
            _common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
            $("#sale_start_date").focus();
            return false;
        }else{
            _StartDate = new Date(fmtDate($("#sale_start_date").val())).getTime();
            if(judgeNaN(_StartDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#sale_start_date").focus();
                return false;
            }
        }
        let _EndDate = null;
        if(!$("#sale_end_date").val()){
            _common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
            $("#sale_end_date").focus();
            return false;
        }else{
            _EndDate = new Date(fmtDate($("#sale_end_date").val())).getTime();
            if(judgeNaN(_EndDate)){
                _common.prompt("Please enter a valid date!",3,"info");
                $("#sale_end_date").focus();
                return false;
            }
        }
        if(_StartDate>_EndDate){
            $("#sale_end_date").focus();
            _common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
            return false;
        }
        let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
        if(difValue>62){
            _common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
            $("#sale_end_date").focus();
            return false;
        }
        return true;
    }

    //画面按钮点击事件
    var but_event = function(){
        //时间重置
        m.clear_sale_date.on("click",function(){
            m.sale_start_date.val("");
            m.sale_end_date.val("");
        });
        //重置搜索
        m.reset.on("click",function(){
            $("#regionRemove").click();
            m.voucher_cd.val("");
            m.sale_start_date.val("");
            m.sale_end_date.val("");
            m.barcode.val("");
            m.formulaArticleId.val("");
            m.formulaArticleName.val("");
            m.tranSerialNo.val("");
            m.posId.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            if(!verifySearch()){return false;}
            var searchJsonStr ={
                voucherCd:m.voucher_cd.val().trim(),
                regionCd:$("#aRegion").attr("k"),
                cityCd:$("#aCity").attr("k"),
                districtCd:$("#aDistrict").attr("k"),
                storeCd:$("#aStore").attr("k"),
                saleStartDate:subfmtDate(m.sale_start_date.val()),
                saleEndDate:subfmtDate(m.sale_end_date.val()),
                barcode:m.barcode.val().trim(),
                formulaArticleId:m.formulaArticleId.val().trim(),
                formulaArticleName:m.formulaArticleName.val().trim(),
                tranSerialNo:m.tranSerialNo.val().trim(),
                posId:m.posId.val().trim()
            };
            m.searchJson.val(JSON.stringify(searchJsonStr));

            paramGrid = "voucherCd="+m.voucher_cd.val()+
                "&regionCd="+$("#aRegion").attr("k")+
                "&cityCd="+$("#aCity").attr("k")+
                "&districtCd="+$("#aDistrict").attr("k")+
                "&storeCd="+$("#aStore").attr("k")+
                "&saleStartDate="+subfmtDate(m.sale_start_date.val())+
                "&saleEndDate="+subfmtDate(m.sale_end_date.val())+
                "&barcode="+m.barcode.val().trim()+
                "&formulaArticleId="+m.formulaArticleId.val().trim()+
                "&formulaArticleName="+m.formulaArticleName.val().trim()+
                "&tranSerialNo="+m.tranSerialNo.val().trim()+
                "&posId="+m.posId.val().trim();
            tableGrid.setting("url",url_left+"/getList");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData(null);
        });

    }

    //表格按钮事件
    var table_event = function(){
        $("#view").on("click", function () {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"voucherCd");
            var param = "voucherCd="+cols["voucherCd"];
            saveParamToSession();
            top.location = url_left+"/view?"+param;
        })
        // 导出按钮点击事件
        $("#export").on("click",function(){
            if(!verifySearch()){return false;}
            var searchJsonStr ={
                regionCd:$("#aRegion").attr("k"),
                cityCd:$("#aCity").attr("k"),
                districtCd:$("#aDistrict").attr("k"),
                storeCd:$("#aStore").attr("k"),
                voucherCd:m.voucher_cd.val(),
                saleStartDate:subfmtDate(m.sale_start_date.val()),
                saleEndDate:subfmtDate(m.sale_end_date.val()),
                barcode:m.barcode.val(),
                formulaArticleId:m.formulaArticleId.val(),
                formulaArticleName:m.formulaArticleName.val(),
                tranSerialNo:m.tranSerialNo.val(),
                posId:m.posId.val()
            };
            paramGrid = "searchJson=" + JSON.stringify(searchJsonStr);
            var url = url_left + "/export?" + paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
        });
    }

    //表格初始化-配方销售单头档列表样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Formula Sales Order Detail",
            param:paramGrid,
            colNames:["Store No.","Store Name","Selling Date","Document No.","Item Barcode","Parent Item Code","Parent Item Name",
            "Tran Serial No.","POS ID","Sales Qty","Sales Amount"],
            colModel:[
                {name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"saleDate",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
                {name:"voucherCd",type:"text",text:"right",width:"110",ishide:false,css:""},
                {name:"barcode",type:"text",text:"right",width:"110",ishide:false,css:""},
                {name:"formulaArticleId",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"tranSerialNo",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name:"posId",type:"text",text:"right",width:"80",ishide:false,css:""},
                {name:"saleQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
                {name:"saleAmt",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
            ],//列内容
            width:"max",//宽度自动
            isPage:true,//是否需要分页
            page:1,//当前页
            sidx:"sale_date, ma4350.store_cd",//排序字段
            sord:"desc",//升降序
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup:[
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""
                },
                {
                    butType:"custom",
                    butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                }
            ]
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#view").remove();
        }
        var exportBut = $("#exportBut").val();
        if (Number(exportBut) != 1) {
            $("#export").remove();
        }
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }



    //格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    var zeroFmt =  function(tdObj, value){
        var _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // DD/MM/YYYY to YYYY-MM-DD  格式转换
    function fmtDate(date) {
        var res = '';
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
        return res;
    }

    function subfmtDate(date){
        var res = "";
        if(date!=null&&date!=""){
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
        return res;
    }


    function fmtYMDHMSDate(date){
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/, '$3/$2/$1 $4:$5:$6');
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('maItemSale');
_start.load(function (_common) {
    _index.init(_common);
});
