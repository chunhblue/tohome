require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
define('stocktakeQueryEdit', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrListTemp = [];//临时行数据存储
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        common=null;
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        searchJson:null,

        //弹窗
        update_dialog:null,
        barcode:null,
        article_id:null,
        article_name:null,
        uom:null,
        spec:null,
        area_cd:null,
        first_qty:null,
        second_qty:null,
        close:null,
        submit:null,
        piCd:null,
        piDate:null,
        returnsViewBut:null,
        //隐藏项
        search:null,
        flag:null,
        userId:null,
        unitId:null,
        list:null,
        saveBut:null,
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/stocktakeQuery";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        initPageBytype(m.flag.val());
        //表格内按钮事件
        table_event();

        m.search.click();
    }

    //表格按钮事件
    var table_event = function(){
        $("#first_qty").blur(function () {
            $("#first_qty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#first_qty").focus(function(){
            $("#first_qty").val(reThousands(this.value));
        });

        $("#second_qty").blur(function () {
            $("#second_qty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#second_qty").focus(function(){
            $("#second_qty").val(reThousands(this.value));
        });


        $("#modify").on("click", function () {
            var _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length == 0){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                let item=tableGrid.getSelectColValue(_list[0],"partCd,partName,barcode,articleId,articleName,spec,uom,areaCd,firstQty,secondQty");
                $('#barcode').val(item.barcode);
                $('#article_id').val(item.articleId);
                $('#article_name').val(item.articleName);
                $('#uom').val(item.uom);
                $('#spec').val(item.spec);
                // $('#area_cd').val(item.areaCd);
                $('#first_qty').val(item.firstQty);
                $('#second_qty').val(item.secondQty);
                m.update_dialog.modal("show");
            }
        });
       /* $("#delete").on("click", function () {
            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }else{
                trClick_table1();
                _common.myConfirm("Are you sure to delete",function(result){
                    if(result=="true"){

                    }
                });
            }
        });*/
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "0":
                initTable1();//列表初始化
                $('#modify').prop('disabled',true); // 查看模式
                $('#saveBut').prop('disabled',true);
                break;
            case "1":
                m.second_qty.prop('disabled', true);//不允许修改复盘量
                initTable1();//列表初始化
                break;
            case "2":
                m.first_qty.prop('disabled', true);//不允许修改初盘量
                initTable1();//列表初始化
                break;
            default:
                m.error_pcode.show();
                //m.main_box.hide();
        }
    }

    //拼接检索参数
    var setParamJson = function(){
        // var searchJsonStr = m.list.val();//后端传来的分类list
        let piCd=m.piCd.val();
        let piDate=m.piDate.val();
        let obj={
            'piCd':piCd,
            'accDate': piDate
        }
        m.searchJson.val(JSON.stringify(obj));
    }

    //验证检索项是否合法
    var verifySearch = function () {
        return true;
    }

    //画面按钮点击事件
    var but_event = function(){
        m.saveBut.on('click',function () {
            let arr = [];
            let piCd=m.piCd.val();
            let piDate=m.piDate.val();
            $("#zgGridTtable>.zgGrid-tbody tr").each(function (index) {
                // 循环列表的数据
                let articleId = $(this).find('td[tag=articleId]').text();
                let firstQty = reThousands($(this).find('td[tag=firstQty]').text());
                let secondQty = reThousands($(this).find('td[tag=secondQty]').text());
                let obj = {
                    'piCd': piCd,
                    'accDate': piDate,
                    'articleId': articleId,
                    'firstQty': firstQty,
                    'secondQty': secondQty,
                }
                arr.push(obj);
            });
            if (arr.length<1) {
                return;
            }
            let record = JSON.stringify(arr);
            _common.myConfirm("Are you sure you want to save?",function(result){
                if(result!="true"){return false;}
                $.myAjaxs({
                    url:url_left+"/save",
                    async:true,
                    cache:false,
                    type :"post",
                    data :'record='+record,
                    dataType:"json",
                    success:function(result){
                        $('#modify').prop('disabled',true); // 查看模式
                        $('#saveBut').prop('disabled',true);
                        if(result.success){
                            // 变为查看模式
                            _common.prompt("Data saved successfully！",5,"info");/*保存成功*/
                        }else{
                            _common.prompt(result.msg,5,"error");
                        }
                    },
                    error : function(e){
                        $('#modify').prop('disabled',true); // 查看模式
                        $('#saveBut').prop('disabled',true);
                        _common.prompt("Data saved failed！",5,"error");/*保存失败*/
                    }
                });
            })

        });

        //检索按钮点击事件
        m.search.on("click",function(){
            if(verifySearch()){
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson=" + m.searchJson.val();
                tableGrid.setting("url", url_left + "/queryItems");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);
                tableGrid.loadData();
            }
        });

        //返回一览
        m.returnsViewBut.on("click",function(){
            top.location = url_left;
        });
        m.close.on("click", function () {
            m.update_dialog.modal("hide");
        });

        m.submit.on("click", function () {
            let articleId = $('#article_id').val();
            let firstQty = $('#first_qty').val();
            let secondQty = $('#second_qty').val();

            $("#zgGridTtable>.zgGrid-tbody tr").each(function (index) {
                // 循环列表的数据
                let temp = $(this).find('td[tag=articleId]').text();
                if (articleId==temp) {
                    $(this).find('td[tag=firstQty]').text(firstQty);
                    $(this).find('td[tag=secondQty]').text(secondQty);
                }
            });
            m.update_dialog.modal("hide");
        });
    };

    //表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Stocktaking List",
            param:paramGrid,
            localSort: true,
            colNames:[
                "Group Cd",
                "Group Name",
                "Barcode",
                "Item Code",
                "Item Name",
                "Spec",
                "Uom",
                // "Position Code",
                "First Stock Qty",
                "Second Stock Qty"],
            colModel:[
                {name:"partCd",type:"text",text:"right",width:"150",ishide:false,css:""},
                {name:"partName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"barcode",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"articleId",type:"text",text:"right",width:"130",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name:"spec",type:"text",text:"left",width:"50",ishide:false,css:""},
                {name:"uom",type:"text",text:"left",width:"50",ishide:false,css:""},
                {name:"firstQty",index:"firstQty",text:"right",width:"130",ishide:false,getCustomValue:getThousands},
                {name:"secondQty",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands}
            ],//列内容
            // traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:true,
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
                    butType: "update",
                    butId: "modify",
                    butText: "Modify",
                    butSize: ""
                }
            ],
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
var _index = require('stocktakeQueryEdit');
_start.load(function (_common) {
    _index.init(_common);
});
