require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receiptEdit', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        bmCodeOrItem = 0,//0 bmCode 1：item
        common=null;
    var m = {
        toKen : null,
        use : null,
        up : null,
        entiretyBmStatus : null,
        item_name : null,
        bm_division : null,
        bm_department : null,
        tableGrid : null,
        selectTrObj : null,
        error_pcode : null,
        identity : null,
        reject_dialog : null,
        show_user_info : null,//检索中用户部分
        show_status_all : null,//bm商品状态-全部单选按钮
        show_status_del : null,//手工删除单选按钮
        show_status_reject : null,//驳回选按钮
        show_select_div : null,//事业部auto
        show_select_dpt : null,//DPTauto
        bm_be_part : null,//BM所属下拉项
        show_div : null,//操作人员信息-事业部
        show_store : null,//操作人员信息-店铺
        show_status : null,//BM商品状态
        bm_be_part_div : null,//bm所属
        bm_type_box : null,//bm类型-box
        bm_type : null,//bm类型
        show_bm_code_input : null,
        input_item : null,
        division : null,
        dpt: null,
        pd_date:null,
        pd_start_time:null,
        pd_end_time:null,
        clear_pd_time:null,
        alert_div : null,//页面提示框
        search : null,//检索按钮
        main_box : null,//检索按钮
        p_code_buyer_del : null,
        p_code_excel : null,
        p_code_buyer_affirm : null,
        rejectreason : null,
        reject_dialog_cancel : null,
        reject_dialog_affirm : null,
        returnsViewBut : null,
        store : null,
        search_item_but : null,
        p_code_div_check:null,
        p_code_sys_del:null,
        p_code_sys_list_del:null,
        checkResources:null,
        tempTableType:null,
        searchJson:null,
        checkCount:null,
        urgencyCount:null,
        del_alert:null,
        bm_status_5_select : null//bm商品状态下拉项
    }
    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }
    function init(_common) {
        createJqueryObj();
        url_left=_common.config.surl+"/stocktakePlanEntry";
        //首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        console.log(m.use.val())
        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        initPageBytype("1");
        //表格内按钮事件
        table_event();
        m.search.click();

        // $("#update").attr("disabled", "disabled");
        // $("#view").attr("disabled", "disabled");
    }


    var table_event = function(){
        $("#addPlanDetails").on("click", function () {
            $('#update_dialog').modal("show");
        });
        $("#addMPlanDetails").on("click",function () {
            paramGrid = "searchJson="+m.searchJson.val();
            var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
        });
        $("#updatePlanDetails").on("click",function(){
            var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
            if(selectObj==null||selectObj.length==0){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                checkOrRejectClick(1,null,selectObj);
            }
        });
        $("#deletePlanDetails").on("click",function(){
            var selectObj = $("#zgGridTtable_tbody").find("input[type='checkbox']:checked");
            if(selectObj==null||selectObj.length==0){
                _common.prompt("Please select at least one row of data!",5,"error");
            }else{
                checkOrRejectClick(1,null,selectObj);
            }
        });
        //返回一览
        m.returnsViewBut.on("click",function(){
            top.location = url_left;
        });

        //批量删除过期商品
        $("#listDel").on("click",function(){
            var date = new Date();
            var endDateStr = _common.dateFormat(date,"yyyy-MM-dd");
            var endDate = _common.dateFormat(date,"yyyyMMdd");
            // _common.myConfirm("请确认是否需要删除过期BM<br>本次将要删除 销售结束日小于“"+endDateStr+"”的BM数据<br>删除后将无法恢复",function(result){
            _common.myConfirm("Please confirm whether it is necessary to delete expired BM<br>BM data whose sales end date is less than“"+endDateStr+"”will not be recovered after deletion!",function(result){
                if(result=="true"){
                    $.myAjaxs({
                        url:url_left+"/dellistbm",
                        async:true,
                        cache:false,
                        type :"get",
                        data :"endDate="+endDate+"&toKen="+m.toKen.val(),
                        dataType:"json",
                        success:showResponse,
                        complete:_common.myAjaxComplete
                    });
                }
            });
        });

    }

    //审核和驳回的按钮事件
    var checkOrRejectClick = function(_flg,_rejectreason,selectObj){
        var _selectList = new Array();
        var bmTypeCode = "";
        $.each(selectObj,function(ix,node){
            _selectList.push($(node).attr("bmcode")+"-"+$(node).attr("bmtype"));
        });
        bmTypeCode = _selectList.join(',');
        _rejectreason= _rejectreason||"";
        _flg = _flg==1?"17":"18";

        _data = "bmTypeCode="+bmTypeCode+
            "&rejectreason="+_rejectreason+
            "&opFlg="+_flg+
            "&staffResource="+m.checkResources.val()+
            "&identity="+m.identity.val()+
            "&toKen="+m.toKen.val();

        $.myAjaxs({
            url:url_left+"/checkandreject",
            async:true,
            cache:false,
            type :"get",
            data :_data,
            dataType:"json",
            success:showResponse,
            complete:_common.myAjaxComplete
        });
    }


    var showResponse = function(data,textStatus, xhr){
        selectTrTemp = null;
        var resp = xhr.responseJSON;
        if( resp.result == false){
            top.location = resp.s+"?errMsg="+resp.errorMessage;
            return ;
        }
        if(data.success==true){
            _common.prompt("Operation Succeeded!",2,"success",function(){
                m.search.click();
            },true);
        }else{
            _common.prompt(data.message,5,"error");
        }
        m.toKen.val(data.toKen);
    }
    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                m.show_div.show();
                m.show_store.hide();
                m.show_status_all.hide();
                m.show_status_reject.show();
                m.show_user_info.show();
                m.show_select_div.hide();
                m.show_select_dpt.hide();
                initTable1();//列表初始化
                //列表按钮
                $("#show_status").find("input[type='radio']").eq(1).click();
                if(m.p_code_buyer_del.val()!=1){
                    $("#del").remove();
                    m.del_alert.hide();
                }
                break;
            case "2":
                m.show_div.hide();
                m.show_store.hide();
                m.show_user_info.hide();
                m.show_status_del.hide();
                m.show_status_reject.hide();
                m.alert_div.show();
                initTable2();//列表初始化
                $("#show_status").find("input[type='radio']").eq(0).click();
                if(m.p_code_div_check.val()!=1){
                    $("#check").remove();
                    $("#reject").remove();
                }
                break;
            case "3":
                m.show_div.hide();
                m.show_store.hide();
                m.show_status_all.hide();
                m.show_status_reject.hide();
                m.show_user_info.hide();
                m.bm_be_part.prepend('<option value=""></option>');
                m.bm_be_part.val("");
                m.bm_be_part.attr("disabled","disabled");
                m.alert_div.show();
                initTable3();//列表初始化
                $("#show_status").find("input[type='radio']").eq(1).click();
                $("#del").hide();
                m.del_alert.hide();
                //列表按钮
                if(m.p_code_sys_del.val()!=1){
                    $("#del").remove();
                }
                break;
            case "4":
                m.show_div.hide();
                m.show_store.show();
                m.show_status_reject.hide();
                m.show_status.hide();
                m.show_user_info.show();
                m.show_select_dpt.hide();
                m.bm_be_part_div.hide();
                m.bm_type_box.show();
                initTable4();//列表初始化
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
        //验证权限设定是否存在 按钮
        if(m.p_code_excel.val()!="1"){
            //没有excel权限
            // $("#outExcel").remove();
        }
    }

    //画面按钮点击事件
    var but_event = function(){
        //盘点日期
        m.pd_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        //开始时间
        m.pd_start_time.datetimepicker(
            {
                language:'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: true,//今日按钮
                format: 'dd/mm/yyyy hh:ii:ss',
                startView: 'month',// 进来是月
                minView: 'hour',// 可以看到小时
                minuteStep:1, //分钟间隔为1分
                todayHighlight: true,
                forceParse: true,
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#pd_end_time").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
            } else {
                $("#pd_end_time").datetimepicker('setStartDate', null);
            }
        });
        //结束时间
        m.pd_end_time.datetimepicker(
            {
                language:'en',
                autoclose: true,//选中之后自动隐藏日期选择框
                clearBtn: false,//清除按钮
                todayBtn: true,//今日按钮
                format: 'dd/mm/yyyy hh:ii:ss',
                startView: 'month',// 进来是月
                minView: 'hour',// 可以看到小时
                minuteStep:1, //分钟间隔为1分
                todayHighlight: true,
                forceParse: true,
            }).on('changeDate', function (ev) {
            if (ev.date) {
                $("#pd_start_time").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
            } else {
                $("#pd_start_time").datetimepicker('setEndDate', new Date());
            }

        });
        //清空日期
        m.clear_pd_time.on("click",function(){
            m.pd_start_time.val("");
            m.pd_end_time.val("");
        });
        //检索按钮点击事件
        m.search.on("click",function(){
            // if(verifySearch()){
            if(true){
                //BM商品状态
                var tempRadioValue = m.show_status.find("input[type='radio']:checked").val();
                m.entiretyBmStatus.val(tempRadioValue);
                //拼接检索参数
                setParamJson();
                paramGrid = "searchJson="+m.searchJson.val();
                // tableGrid.setting("url",url_left+"/getdata");
                tableGrid.setting("param", paramGrid);
                tableGrid.setting("page", 1);

                var data = '[{"sc1":"胡志明店","sc2":"门店要货单","sc3":"已审核","sc4":"蔬菜","sc5":"5","6":"5","7":"0","8":"管理员","9":"10/12/2019","10":"管理员","11":"管理员","12":"10/12/2019","13":"50000","14":""}]';
                tableGrid.loadData(data);

                //各个角色的不同检索bm商品状态的按钮显示
                switch(m.identity.val()) {
                    case "1":
                        //采购
                        if(m.entiretyBmStatus.val()!=3){
                            $("#del").prop("disabled",true).show();
                            m.del_alert.show();
                        }else{
                            $("#del").hide();
                            m.del_alert.hide();
                        }
                        break;
                    case "2":
                        //商品部长
                        if(m.entiretyBmStatus.val()!=3){
                            $("#check").show();
                            $("#reject").show();
                        }else{
                            $("#check").hide();
                            $("#reject").hide();
                        }
                        break;
                    case "3":
                        if(m.entiretyBmStatus.val()==4){
                            $("#del").prop("disabled",true).show();
                            m.del_alert.show();
                        }else{
                            $("#del").hide();
                            m.del_alert.hide();
                        }
                        break;
                }
                $("#outExcel").prop("disabled",false);
            }else{
                $("#outExcel").prop("disabled",true);
            }
            //判断当前操作人是否为事业部长或系统部，如果是，曾增加待审核数量等内容的刷新机制，其他身份不参与此操作
            if(m.identity.val()=="2"||m.identity.val()=="3"){
                refreshWaitingReview();
            }

        });

        // 商品条码输入口 焦点离开事件
        m.input_item.on("blur",function(){
            if($(this).val()==""){
                m.input_item.attr("status","0");
                m.item_name.text("");
            }
        });
        m.input_item.on("keydown",function(){
            if(event.keyCode == "13"){
                var thisObj = $(this);
                if(thisObj.val()==""){
                    m.item_name.text("");
                }else{
                    m.search_item_but.click();
                }
            }
        });
        //点击查找商品条码按钮事件
        m.search_item_but.on("click",function(){
            var inputVal = m.input_item.val();
            m.input_item.attr("status","0");
            itemTempObj = null;
            m.item_name.text("");
            if($.trim(inputVal)==""){
                _common.prompt("barcode cannot be empty!",5,"error");
                m.input_item.attr("status","2").focus();
                return false;
            }
            var reg = /^[0-9]*$/;
            if(!reg.test(inputVal)){
                _common.prompt("Barcode must be a pure number!",5,"error");
                m.input_item.attr("status","2").focus();
                return false;
            }
            m.item_name.text("Loading...");
            getItemInfoByItem1(inputVal,function(res){
                if(res.success){
                    m.item_name.text($.trim(res.data.itemName));
                    m.input_item.attr("status","1");
                    return true;
                }else{
                    m.item_name.text("Item Name is not obtained!");// 未取得Item Name
                    m.input_item.attr("status","2");
                    _common.prompt(res.message,5,"error");
                    return false;
                }
            });

        });
    }

    //刷新待审核数量
    var refreshWaitingReview = function(){
        $.myAjaxs({
            url:url_left+"/refreshWaitingReview",
            async:true,
            cache:false,
            type :"get",
            data:"identity="+m.identity.val(),
            dataType:"json",
            success:function(rest){
                if(rest.success){
                    m.checkCount.text(rest.data.checkCount);
                    m.urgencyCount.text(rest.data.urgencyCount);
                }else{
                    m.checkCount.text(0);
                    m.urgencyCount.text(0);
                }
            },
            complete:_common.myAjaxComplete
        });
    }

    //验证检索项是否合法
    var verifySearch = function(){
        var tempRadioValue = m.show_status.find("input[type='radio']:checked").val();
        if(tempRadioValue=="5"){
            if(m.bm_status_5_select.val()==""){
                _common.prompt("Please select modification type!",5,"error");
                m.bm_status_5_select.focus();
                return false;
            }
        }
        // if(m.sell_start_date.val()!=""&&m.sell_end_date.val()!=""){
        // 	var intStartDate = fmtStrToInt(m.sell_start_date.val());
        // 	var intEndDate = fmtStrToInt(m.sell_end_date.val());
        // 	if(intStartDate>intEndDate){
        // 		_common.prompt("[销售开始日]不能大于[销售结束日]",5,"error");
        // 		return false;
        // 	}
        // }
        var inputVal = m.show_bm_code_input.val();
        if(inputVal!=""){
            var reg = /^[0-9]*$/;
            if(!reg.test(inputVal)){
                _common.prompt("BM编码必须是0~9的数字",5,"error");
                m.show_bm_code_input.focus();
                return false;
            }
        }
        var inputVal = m.input_item.val();
        if(inputVal!=""){
            var status = m.input_item.attr("status");
            if(status=="2"){
                _common.prompt("Incorrect item code, please enter again!",5,"error");
                m.input_item.focus();
                return false;
            }
        }
        return true;
    }

    //拼接检索参数
    var setParamJson = function(){
        var _newFlg = null;
        var _updateFlg = null;
        var _checkFlg= null;
        //数据来源表 0 正是表，1ck表，2历史表
        var _tableType = 0;
        switch(m.entiretyBmStatus.val()) {
            case "1":
                //全部
                _checkFlg = "0";//0-所有采购员均已确认，商品部长未审核
                _tableType = 1;
                break;
            case "2":
                //新签约 ck
                _newFlg = "0";//0-新增 1-修改 2-删除
                if(m.identity.val()=="2"){
                    //如果是 事业部长（商品部长）
                    _checkFlg = "0";
                }else if(m.identity.val()=="3"){
                    //如果是 系统部
                    _checkFlg = "1";
                }
                _tableType = 1;
                break;
            case "3":
                //已经生效 正式
                _tableType = 0;
                break;
            case "4":
                //手工删除 正式
                _tableType = 0;
                break;
            case "5":
                //修改 ck
                _newFlg = "1";
                _updateFlg = m.bm_status_5_select.val();//0-修改BM价格/折扣 1-修改BM生效期间 2-修改价格和期间
                _tableType = 1;
                break;
            case "6":
                //驳回 ck
                _checkFlg = "3"; //3-已驳回(包括采购员驳回，部长驳回，系统部驳回)
                _tableType = 1;
                break;
            default:
                //已经生效 正式
                _tableType = 0;
        }
        //事业部
        var _div = m.bm_division.val()||null;
        //dpt
        var _dpt = m.bm_department.val()||null;
        //bm所属
        var _bmBePart = m.bm_be_part.val()||null;
        //bm类型
        var _bmType = m.bm_type.val()||null;

        //bm编码
        var _bmCode = m.show_bm_code_input.val()||null;
        bmCodeOrItem = 0;
        //单品条码是否被选中
        if($("#bm_item").is(':checked')){
            bmCodeOrItem = 1;
        }
        //单品条码（商品号码）
        var _itemCode = m.input_item.val()||null;

        // 销售开始日期
        // var _sellStartDate = fmtStrToInt(m.sell_start_date.val())||null;
        // // 销售结束日期
        // var _sellEndDate = fmtStrToInt(m.sell_end_date.val())||null;
        m.tempTableType.val(_tableType);
        // 创建请求字符串
        var searchJsonStr ={
            store:m.store.val(),
            identity:m.identity.val(),
            tableType:_tableType,
            bmStatus:m.entiretyBmStatus.val(),
            newFlg:_newFlg,
            updateFlg:_updateFlg,
            checkFlg:_checkFlg,
            div:_div,
            dpt:_dpt,
            bmBePart:_bmBePart,
            bmType:_bmType,
            bmCode:_bmCode,
            itemCode:_itemCode
        };
        m.searchJson.val(JSON.stringify(searchJsonStr));
    }

    //根据商品条码取得该商品的详细对象，
    var getItemInfoByItem1 = function(item1,fun){
        $.myAjaxs({
            url:url_left+"/getiteminfo",
            async:true,
            cache:false,
            type :"get",
            data :"itemCode="+item1,
            dataType:"json",
            success:fun,
            complete:_common.myAjaxComplete
        });
    }

    //点击采购tr后事件
    var trClick_table1 = function(){
        var delBut =  $("#del");//
        delBut.prop("disabled",true);
        var cols = tableGrid.getSelectColValue(selectTrTemp,"statusFlg,checkFlg");

        if(cols["checkFlg"]=="3"||m.entiretyBmStatus.val()=="4"){
            delBut.prop("disabled",false);
        }else{
            delBut.prop("disabled",true);
        }
    }
    //点击采购tr后事件
    var trClick_table3 = function(){
        var delBut =  $("#del");//
        delBut.prop("disabled",true);
        if(m.entiretyBmStatus.val()=="4"){
            delBut.prop("disabled",false);
        }else{
            delBut.prop("disabled",true);
        }
    }

    //表格初始化-采购样式
    var initTable1 = function(){
        var data = '[{"typeCode":"DS180109000205","typeName":"Food Service"}]';
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Details",
            param:paramGrid,
            localSort: true,
            colNames:["Type Code","Type Name"],
            colModel:[
                {name:"typeCode",type:"text",text:"center",width:"400",ishide:false,css:""},
                {name:"typeName",type:"text",text:"center",width:"400",ishide:false,css:""},
            ],//列内容
            traverseData:data,
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:true,//是否需要分页
            // sidx:"bm.bm_type,Plan.bm_code",//排序字段
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
            footerrow:true,
            loadCompleteEvent:function(self){
                // selectTrTemp = null;//清空选择的行
                //列段显示
                // if(m.entiretyBmStatus.val()=="2"||m.entiretyBmStatus.val()=="5"||m.entiretyBmStatus.val()=="6"){
                //   tableGrid.showColumn("opFlgText,buyer,buyerName,rejectreason");
                // }else if(m.entiretyBmStatus.val()=="3"||m.entiretyBmStatus.val()=="4"){
                //   tableGrid.hideColumn("opFlgText,buyer,buyerName,rejectreason");
                // }
                // return self;


                /*统计功能 */
                var sum_BuilingdArea = $("#zgGridTtable").getCol('sc13', false, 'sum');
                var sum_TotalRoom = $("#zgGridTtable").getCol('sc14', false, 'sum');
                $("#zgGridTtable").footerData('set', {  需要汇总的列名: sum_BuilingdArea, 需要汇总的列名: sum_TotalRoom }
                );
            },
            userDataOnFooter: true,
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                trClick_table1();
            },
            buttonGroup:[

                {
                    butType:"custom",
                    butHtml:"<button id='addPlanDetails' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-plus'></span> Add</button>"
                },{
                    butType: "upload",
                    butId: "addMPlanDetails",
                    butText: "Batch Add",
                    butSize: "",
                },{
                    butType:"custom",
                    butHtml:"<button id='updatePlanDetails' type='button' class='btn btn-info btn-sm''><span class='glyphicon glyphicon-pencil'></span> Modify</button>"
                },{
                    butType:"custom",
                    butHtml:"<button id='deletePlanDetails' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-remove'></span> Delete</button>"
                }




            ],



        });
    }

    //bm编码格式化
    var operateFmt = function(tdObj, value){
        var sp = value.split("#");
        var html = '<input type="checkbox" bmtype="'+sp[0]+'" bmcode="'+sp[1]+'" name="table_ck" />';
        return $(tdObj).html(html);
    }

    //bm编码格式化
    var bmCodeFmt = function(tdObj, value){
        bmtype = tempTrObjValue.bmType;
        var html = '<a href="javascript:void(0);" title="进入详情" class="view" tabletype="'+m.tempTableType.val()+'" bmcode="'+value+'" bmtype="'+bmtype+'" ><span class="glyphicon glyphicon-expand icon-right"></span>'+value+'</a>';
        return $(tdObj).html(html);
    }

    // 操作列复选和删除按钮
    var editButtonFmt = function(tdObj, value){
        var cks = '<input type="checkbox" />';
        var but = '<button type="button" class="btn btn-primary btn-xs">删除</button>';
        var insertHtml = "";
        insertHtml+=cks;
        if(m.entiretyBmStatus.val()==4){
            insertHtml=but;
        }
        return $(tdObj).html(insertHtml);
    }
    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        return $(tdObj).text(fmtIntDate(value));
    }
    //newFlgFmt 0-新增 1-修改 2-删除
    var newFlgFmt = function(tdObj, value){
        switch(value) {
            case "0":
                return $(tdObj).text("新增");
                break;
            case "1":
                return $(tdObj).text("修改");
                break;
            case "2":
                return $(tdObj).text("删除");
                break;
            default:
                return $(tdObj).text("未知");
        }
    }
    //bmType 字段格式化中文
    var bmTypeFmt = function(tdObj, value){
        tempTrObjValue.bmType = value;
        switch(value) {
            case "01":
                return $(tdObj).text("01 捆绑");
                break;
            case "02":
                return $(tdObj).text("02 混合");
                break;
            case "03":
                return $(tdObj).text("03 固定组合");
                break;
            case "04":
                return $(tdObj).text("04 阶梯折扣");
                break;
            case "05":
                return $(tdObj).text("05 AB组");
                break;
        }
    }
    //OP_FLG 字段格式化中文
    var statusFmt = function(tdObj, value){
        //01-采购员提交，07-采购员确认BM明细单品，08-采购员驳回BM明细单品，17-商品部长审核通过，18-商品部长驳回，21-系统部提交，27-系统部审核通过，28-系统部驳回
        switch(value) {
            case "01":
                return $(tdObj).text("采购员提交");
                break;
            case "07":
                return $(tdObj).text("采购员确认BM明细单品");
                break;
            case "08":
                return $(tdObj).text("采购员驳回BM明细单品");
                break;
            case "17":
                return $(tdObj).text("商品部长审核通过");
                break;
            case "18":
                return $(tdObj).text("商品部长驳回");
                break;
            case "21":
                return $(tdObj).text("系统部提交");
                break;
            case "27":
                return $(tdObj).text("系统部审核通过");
                break;
            case "28":
                return $(tdObj).text("系统部驳回");
                break;
            case "":
                return $(tdObj).text("已生效");
                break;
            default:
                return $(tdObj).text("未知状态");
        }
    }

    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    //格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
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
var _index = require('receiptEdit');
_start.load(function (_common) {
    _index.init(_common);
});
