require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('itemTransfersEdit', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        paramGrid = null,
        itemInput = null,
        InItemInput = null,
        aStore = null,
        adjustReason = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        selectTrTemp = null,
        table = null,
        submitFlag=false,
        tempTrObjValue = {},//临时行数据存储
        //附件
        attachmentsParamGrid = null,
        common=null;
    const KEY = 'ITEM_TRANSFER_ENTRY';
    var m = {
        toKen : null,
        use : null,
        error_pcode : null,
        identity : null,
        viewSts : null,
        searchJson:null,
        // 基础信息
        tf_cd : null,
        tf_date : null,
        dj_status : null,
        aStore : null,
        cRemark : null,
        creater : null,
        ca_date : null,
        modifier : null,
        md_date : null,
        ap_name : null,
        ap_date : null,
        saveBtn:null,
        resetBtn:null,
        returnsViewBut : null,
        // 弹窗
        item_input : null,
        item_input_cd : null,
        item_input_uom : null,
        item_input_spec : null,
        item_input_price : null,
        tax_rate : null,
        In_item_input : null,
        In_item_input_cd : null,
        In_item_input_uom : null,
        In_item_input_spec : null,
        In_item_input_price : null,
        In_tax_rate : null,
        on_hand_qty : null,
        actualQty : null,
        adjustReason:null,
        inventoryQty:null,
        isChange:null,
        dialog_cancel : null,
        dialog_affirm : null,
        // 主键参数
        _storeCd : null,
        _voucherNo : null,
        _voucherType : null,
        _voucherDate : null,
        _storeCd1 : null,
        //审核
        approvalBut:null,
        approval_dialog:null,
        audit_cancel:null,
        audit_affirm:null,
        typeId:null,
        Inqty:null,
        reviewId:null
    }

    // 创建js对象
    var  createJqueryObj = function(){
        for (x in m)  {
            m[x] = $("#"+x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_root = _common.config.surl;
        url_left = url_root + "/itemTransfer";
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        // 加载下拉列表
        // getSelectValue();
        // 首先验证当前操作人的权限是否混乱，
        if(m.use.val()=="0"){
            but_event();
        }else{
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 根据登录人的不同身份权限来设定画面的现实样式
        initPageBytype(m.identity.val());
        // 表格内按钮事件
        table_event();
        //附件事件
        attachments_event();
        // 根据跳转加载数据，设置操作模式
        setValueByType();
        //审核事件
        approval_event();
        // 初始化选择
        initAutomatic();
        bodyScroll();
    }
    // 冻结表头时，表头可以横向移动
    var bodyScroll  = function(){
        $(document).ready(function() {
            if (table.defaults.freezeHeader) {
                var initHeaderWidth = table.table.width();
                table.zgGridHeaderTable.width(initHeaderWidth);
                table.zgGridBodyBox.scroll(function () {
                    var left = $(table.zgGridBodyBox).scrollLeft();
                    if (left < 0) {
                        left = 0;
                    }
                    left = (~left) + "px";
                    table.zgGridHeaderTable.stop().animate({
                        marginLeft: left
                    }, 0);
                });
            }
        });
    };

    //附件
    var attachments_event = function () {
        //附件一览表格
        attachmentsTable = $("#attachmentsTable").zgGrid({
            title:"Attachments",
            param:attachmentsParamGrid,
            colNames:["File Name","Download","PreView"],
            colModel:[
                {name:"fileName",type:"text",text:"center",width:"130",ishide:false,css:""},
                {name:"filePath",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
                        var obj = value.split(",");
                        var html = '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>';
                        return $(tdObj).html(html);
                    }
                },
                {name:"filePath1",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
                        var obj = value.split(",");
                        var html = '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>';
                        return $(tdObj).html(html);
                    }
                }
            ],//列内容
            width:"max",//宽度自动
            isPage:false,//是否需要分页
            isCheckbox:false,
            loadEachBeforeEvent:function(trObj){
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            loadCompleteEvent:function(self){
                table = self;
                selectTrTempFile = null;//清空选择的行
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTempFile = trObj;
            },
            buttonGroup:[
                {butType: "add",butId: "addByFile",butText: "Add",butSize: ""},//新增
                {butType: "update",butId: "updateByFile",butText: "Modify",butSize: ""},//修改
                {butType: "delete",butId: "deleteByFile",butText: "Delete",butSize: ""},//删除
            ],
        });
        //附件表格新增
        var appendTrByFile = function (fileName,filePath) {
            var rowindex = 0;
            var trId = $("#attachmentsTable>.zgGrid-tbody tr:last").attr("id");
            if(trId!=null&&trId!=''){
                rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
            }
            var tr = '<tr id="attachmentsTable_'+rowindex+'_tr" class="">' +
                '<td tag="fileName" width="130" title="'+fileName+'" align="center" id="attachmentsTable_'+rowindex+'_tr_fileName" tdindex="attachmentsTable_fileName">'+fileName+'</td>' +
                '<td tag="filePath" width="100" title="Download" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath" tdindex="attachmentsTable_filePath">'+
                '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>'+
                '</td>' +
                '<td tag="filePath1" width="100" title="Preview" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath1" tdindex="attachmentsTable_filePath1">'+
                '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>'+
                '</td>' +
                '</tr>';
            $("#attachmentsTable>.zgGrid-tbody").append(tr);
        }

        //附件一览显示
        $("#attachments").on("click",function () {
            $('#attachments_dialog').modal("show");
        })
        //附件一览关闭
        $("#cancelByAttachments").on("click",function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if (result=="true"){
                    $('#attachments_dialog').modal("hide");
                }
            })
        })
        //添加文件
        $("#addByFile").on("click", function () {
            $('#fileUpload_dialog').modal("show");
            $("#operateFlgByFile").val("1");
            $("#file_name").val("");
            $("#fileData").val("");
            $("#fileData").parent().parent().show();
        });
        //修改文件名称
        $("#updateByFile").on("click", function () {
            if(selectTrTempFile == null){
                _common.prompt("Please select at least one row of data!",5,"info");
                return;
            }
            $('#fileUpload_dialog').modal("show");
            $("#operateFlgByFile").val("2");
            $("#fileData").parent().parent().hide();
            var cols = attachmentsTable.getSelectColValue(selectTrTempFile,"fileName");
            var fileName = cols["fileName"];
            $("#file_name").val(fileName);
        });
        //删除文件
        $("#deleteByFile").on("click",function(){
            if(selectTrTempFile == null){
                _common.prompt("Please select at least one row of data!",5,"info");
                return;
            }
            _common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){
                if(result=="true"){
                    $(selectTrTempFile[0]).remove();
                    selectTrTempFile = null;
                }
            });
        });
        //提交按钮点击事件 文件上传
        $("#affirmByFile").on("click",function(){
            if($("#file_name").val()==null||$("#file_name").val()==''){
                $("#file_name").css("border-color","red");
                _common.prompt("File name cannot be empty!",5,"error");
                $("#file_name").focus();
                return;
            }else {
                $("#file_name").css("border-color","#CCCCCC");
            }
            _common.myConfirm("Are you sure you want to upload？",function(result){
                if(result=="true"){
                    var flg = $("#operateFlgByFile").val();
                    if(flg=="1"){
                        if($('#fileData')[0].files[0]==undefined||$('#fileData')[0].files[0]==null){
                            $("#fileData").css("border-color","red");
                            _common.prompt("File cannot be empty!",5,"error");
                            $("#fileData").focus();
                            return;
                        }else {
                            $("#fileData").css("border-color","#CCCCCC");
                        }
                        var formData = new FormData();
                        formData.append("fileData",$('#fileData')[0].files[0]);
                        formData.append("toKen",m.toKen.val());
                        $.myAjaxs({
                            url:_common.config.surl+"/file/upload",
                            async:false,
                            cache:false,
                            type :"post",
                            data :formData,
                            dataType:"json",
                            processData:false,
                            contentType:false,
                            success:function(data,textStatus, xhr){
                                var resp = xhr.responseJSON;
                                if( resp.result == false){
                                    top.location = resp.s+"?errMsg="+resp.errorMessage;
                                    return ;
                                }
                                if(data.success==true){
                                    var fileName = $("#file_name").val();
                                    var filePath = data.data.filePath;
                                    appendTrByFile(fileName,filePath);
                                    _common.prompt("Operation Succeeded!",2,"success");
                                    $('#fileUpload_dialog').modal("hide");
                                }else{
                                    _common.prompt(data.message,5,"error");
                                }
                                m.toKen.val(data.toKen);
                            },
                            complete:_common.myAjaxComplete
                        });
                    }else if(flg=="2"){
                        var fileName = $("#file_name").val();
                        $(selectTrTempFile[0]).find('td').eq(0).text(fileName);
                        _common.prompt("Operation Succeeded!",2,"success");
                        $('#fileUpload_dialog').modal("hide");
                    }
                }
            });
        });

        $("#cancelByFile").on("click",function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if (result=="true"){
                    $("#file_name").css("border-color","#CCCCCC");
                    $("#fileData").css("border-color","#CCCCCC");
                    $('#fileUpload_dialog').modal("hide");
                }
            })
        })

        //下载
        $("#attachments_dialog").on("click","a[class*='downLoad']",function(){
            var fileName = $(this).attr("fileName");
            var filePath = $(this).attr("filePath");
            window.open(_common.config.surl+"/file/download?fileName="+fileName+"&filePath="+filePath,"_self");
        });
        //预览
        $("#attachments_dialog").on("click","a[class*='preview']",function(){
            var fileName = $(this).attr("fileName");
            var filePath = $(this).attr("filePath");
            var url = _common.config.surl+"/file/preview?fileName="+fileName+"&filePath="+filePath;
            window.open(encodeURI(url),"toPreview");
        });
    };

    // 表格内按钮事件
    var table_event = function(){
        //返回一览
        m.returnsViewBut.on("click",function(){
            var bank = $("#saveBtn").attr("disabled");
            if (submitFlag===false && bank!='disabled' ) {
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
                    if (result==="true") {
                        _common.updateBackFlg(KEY);
                        top.location = url_left;
                    }});
            }
            if (submitFlag===true){
                _common.updateBackFlg(KEY);
                top.location=url_left;
            }
            if (submitFlag===false && bank==='disabled' ) {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
        });
        // 添加
        $("#addItemDetails").on("click", function () {
            let flg = m.viewSts.val();
            if(flg!="add"&&flg!="edit"){return false;}
            let _storeCd = $("#aStore").attr('k');
            if(!_storeCd){
                _common.prompt("Please select the store first!",3,"info");
                $("#aStore").css("border-color","red");
                $("#aStore").focus();
                return false;
            }else {
                $("#aStore").css("border-color","#CCC");
            }
            clearDialog(true);
            clearInItemialog(true);
            setDialogDisable(0);
            $('#update_dialog').attr("flg","add");
            $('#update_dialog').modal("show");
        });

        // 修改
        $("#updateItemDetails").on("click",function(){
            let _list = tableGrid.getCheckboxTrs();
            if((_list == null || _list.length == 0) && selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",3,"info");
                return false;
            }
            if(_list.length >= 2){
                _common.prompt("Please select only one row of data!",3,"info");
                return false;
            }
            if(_list.length == 1){
                selectTrTemp = _list[0];
            }
            let _storeCd = $("#aStore").attr('k');
            if(!_storeCd){
                _common.prompt("Please select the store first!",3,"info");
                $("#aStore").css("border-color","red");
                $("#aStore").focus();
                return false;
            }else {
                $("#aStore").css("border-color","#CCC");
            }
            let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,uom,spec,priceNoTax,taxRate,bqty1,adjustReason,adjustReasonText" +
                ",barcode1,articleId1,articleName1,uom1,spec1,priceNoTax1,taxRate1,transferInQty");
            $("#item_input_cd").val(cols['barcode']);
            $.myAutomatic.setValueTemp(itemInput,cols['articleId'],cols['articleName']);
            $("#item_input_uom").val(cols['uom']);
            $("#item_input_spec").val(cols['spec']);
            $("#item_input_price").val(cols['priceNoTax']);
            $("#tax_rate").val(cols['taxRate']);
            $("#actualQty").val(cols['bqty1']);
            $("#transferInQty").val(cols['transferInQty']);
            $.myAutomatic.setValueTemp(adjustReason,cols['adjustReason'],cols['adjustReasonText']);
            $("#In_item_input_cd").val(cols['barcode1']);
            $.myAutomatic.setValueTemp(InItemInput,cols['articleId1'],cols['articleName1']);
            $("#In_item_input_uom").val(cols['uom1']);
            $("#In_item_input_spec").val(cols['spec1']);
            $("#In_item_input_price").val(cols['priceNoTax1']);
            $("#In_tax_rate").val(cols['taxRate1']);
            let flg = 0;
            let e = m.actualQty.attr("e");
            if(e=='1'){flg = 2;}
            setDialogDisable(flg);
            $('#update_dialog').attr("flg","upt");
            $('#update_dialog').modal("show");
            // 查询实时库存
            getStock(_storeCd, cols['articleId'],'out');
            getStock(_storeCd, cols['articleId1'],'in');
        });

        // 查看
        $("#viewItemDetails").on("click",function(){
            let _list = tableGrid.getCheckboxTrs();
            if((_list == null || _list.length == 0) && selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",3,"info");
                return false;
            }
            if(_list.length >= 2){
                _common.prompt("Please select only one row of data!",3,"info");
                return false;
            }
            if(_list.length == 1){
                selectTrTemp = _list[0];
            }
            let _storeCd = $("#aStore").attr('k');
            let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,uom,spec,priceNoTax,bqty1,adjustReasonText" +
                ",barcode1,articleId1,articleName1,uom1,spec1,priceNoTax1,taxRate1,transferInQty");
            $("#item_input_cd").val(cols['barcode']);
            $.myAutomatic.setValueTemp(itemInput,cols['articleId'],cols['articleName']);
            $("#item_input_uom").val(cols['uom']);
            $("#item_input_spec").val(cols['spec']);
            $("#item_input_price").val(cols['priceNoTax']);
            $("#actualQty").val(cols['bqty1']);
            $("#transferInQty").val(cols['transferInQty']);
            $.myAutomatic.setValueTemp(adjustReason,'',cols['adjustReasonText']);
            $("#In_item_input_cd").val(cols['barcode1']);
            $.myAutomatic.setValueTemp(InItemInput,cols['articleId1'],cols['articleName1']);
            $("#In_item_input_uom").val(cols['uom1']);
            $("#In_item_input_spec").val(cols['spec1']);
            $("#In_item_input_price").val(cols['priceNoTax1']);
            $("#In_tax_rate").val(cols['taxRate1']);
            setDialogDisable(1);
            $('#update_dialog').attr("flg","view");
            $('#update_dialog').modal("show");
            // 查询实时库存
            getStock(_storeCd, cols['articleId'],'out');
            getStock(_storeCd, cols['articleId1'],'in');

        });

        // 删除按钮
        $("#deleteItemDetails").on("click",function(){
            let flg = m.viewSts.val();
            if(flg!="add"&&flg!="edit"){return false;}
            let _list = tableGrid.getCheckboxTrs();
            if(_list == null || _list.length == 0){
                _common.prompt("Please select the data you want to delete！",3,"info"); // 请选择要确认删除的数据
                return false;
            }else{
                _common.myConfirm("Are you sure to delete?",function(result){
                    if(result == "true"){
                        for(let i = 0; i < _list.length; i++){
                            $(_list[i].selector).remove();
                        }
                    }
                });
            }
        });
    };

    //审核事件
    var approval_event = function () {
        //点击审核按钮
        m.approvalBut.on("click",function(){
            var recordId = $("#tf_cd").val();
            if(recordId!=null&&recordId!=""){
                $("#approval_dialog").modal("show");
                //获取审核记录
                _common.getStep(recordId,m.typeId.val());
            }else{
                _common.prompt("Record fetch failed, Please try again!",5,"error");
            }
        });
        //审核提交
        $("#audit_affirm").on("click",function () {
            //审核意见
            let auditContent = $("#auditContent").val();
            if(auditContent.length>200){
                _common.prompt("Approval comments cannot exceed 200 characters!",5,"error");
                return false;
            }
            //审核记录id
            let auditStepId = $("#auditId").val();
            //用户id
            let auditUserId = $("#auditUserId").val();
            //审核结果
            let auditStatus = $("#auditStatusGroup").find("input[type='radio']:checked").val();
            // 先更新记录信息
            let _storeCd = $("#aStore").attr('k');
            let _voucherNo = $("#tf_cd").val();
            let _date = subfmtDate($("#tf_date").val());
            let _storeCd1 = $("#aStore").attr('k');
            let itemDetail = [],inItemDetail=[], num = 0;
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                ++num;
                // 转出商品
                let _price = $(this).find('td[tag=priceNoTax]').text();
                let _qty = parseInt(reThousands($(this).find('td[tag=qty2]').text()))||0;
                let _actualQty = (-parseInt(reThousands($(this).find('td[tag=bqty1]').text())))||0; // 取负数
                let _rate = reThousands($(this).find('td[tag=taxRate]').text());
                let _amtNoTax = accMul(_price, Math.abs(_actualQty));
                let _amt = Number(accMul(_amtNoTax, accAdd(1,_rate))).toFixed(2);
                let _tax = Number(accMul(_amtNoTax, _rate)).toFixed(2);
                // 转入商品
                let _price1 = $(this).find('td[tag=priceNoTax1]').text();
                let _qty1 = parseInt(reThousands($(this).find('td[tag=qty2]').text()))||0;
                // let _actualQty1 = parseInt(reThousands($(this).find('td[tag=transferInQty]').text()))||0;
                let _actualQty1 = reThousands($(this).find('td[tag=transferInQty]').text())||0;
                let _rate1 = reThousands($(this).find('td[tag=taxRate1]').text())||0;
                let _amtNoTax1 = accMul(_price, Math.abs(_actualQty1));
                let _amt1 = Number(accMul(_amtNoTax1, accAdd(1,_rate1))).toFixed(2);
                let _tax1 = Number(accMul(_amtNoTax1, _rate1)).toFixed(2);
                let orderOutItem = {
                    storeCd:_storeCd,
                    voucherNo:_voucherNo,
                    voucherDate:_date,
                    voucherType:'602',
                    articleId:$(this).find('td[tag=articleId]').text(),
                    storeCd1:_storeCd1,
                    adjustReason:$(this).find('td[tag=adjustReason]').text(),
                    barcode:$(this).find('td[tag=barcode]').text(),
                    salesUnitId:$(this).find('td[tag=uom]').text(),
                    qty2:_qty,
                    priceNoTax:_price,
                    amtNoTax:_amtNoTax,
                    amt:_amt,
                    amtTax:_tax,
                    taxRate:_rate,
                    qty1:_actualQty,
                    actualAmt:_amt,
                    displaySeq:num
                }
                itemDetail.push(orderOutItem);

                let orderInItem = {
                    storeCd:_storeCd,
                    voucherNo:_voucherNo,
                    voucherDate:_date,
                    voucherType:'601',
                    articleId:$(this).find('td[tag=articleId1]').text(),
                    storeCd1:_storeCd1,
                    adjustReason:$(this).find('td[tag=adjustReason]').text(),
                    barcode:$(this).find('td[tag=barcode1]').text(),
                    salesUnitId:$(this).find('td[tag=uom1]').text(),
                    qty2:_qty1,
                    priceNoTax:_price1,
                    amtNoTax:_amtNoTax1,
                    amt:_amt1,
                    amtTax:_tax1,
                    taxRate:_rate1,
                    qty1:_actualQty1,
                    actualAmt:_amt1,
                    displaySeq:num
                }
                inItemDetail.push(orderInItem);

            });
            if(itemDetail.length < 1 || inItemDetail.length < 1){
                _common.prompt("Commodity information cannot be empty!",5,"error"); // 传票商品信息不能为空
                return false;
            }
            let _data = {
                listJson : JSON.stringify(itemDetail),
                flg : m.isChange.val()
            }
            let _inItem = {
                listJson : JSON.stringify(inItemDetail),
                flg : m.isChange.val()
            }
            _common.myConfirm("Are you sure to save?", function (result) {
                if (result != "true") {
                    return false;
                }
                $("#audit_affirm").prop("disabled",true);
                // var detailType = "tmp_transfer_out";
                $.myAjaxs({
                    url: url_left + '/update',
                    async: true,
                    cache: false,
                    type: "post",
                    data: _data,
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            $.myAjaxs({
                                url: url_left + '/update',
                                async: true,
                                cache: false,
                                type: "post",
                                data: _inItem,
                                dataType: "json",
                                success: function (result) {
                                    if (result.success) {
                                        // 店内转移 走库存调整
                                        var detailType = "tmp_adjustment";
                                        // 提交后实际调拨数量不能再编辑
                                        m.actualQty.attr("e", "0");
                                        $("#updateItemDetails").prop("disabled", true);
                                        // 执行审核提交
                                        $.myAjaxs({
                                            url: _common.config.surl + "/audit/submit",
                                            async: true,
                                            cache: false,
                                            type: "post",
                                            data: {
                                                auditStepId: auditStepId,
                                                auditUserId: auditUserId,
                                                auditStatus: auditStatus,
                                                detailType:detailType,
                                                auditContent: auditContent,
                                                toKen: m.toKen.val()
                                            },
                                            dataType: "json",
                                            success: function (result) {
                                                if (result.success) {
                                                    $("#approval_dialog").modal("hide");
                                                    //更新主档审核状态值
                                                    //_common.modifyRecordStatus(auditStepId,auditStatus);
                                                    m.approvalBut.prop("disabled", true);
                                                    _common.prompt("Saved Successfully!", 3, "success");// 保存审核信息成功
                                                } else {
                                                    m.approvalBut.prop("disabled", false);
                                                    _common.prompt("Saved Failure!", 5, "error");// 保存审核信息失败
                                                }
                                                m.toKen.val(result.toKen);
                                            },
                                            complete: _common.myAjaxComplete
                                        });
                                    }
                                }
                            })
                        } else {
                            _common.prompt("Data update failed, Please try again!", 2, "error");
                        }
                    },
                    complete: _common.myAjaxComplete
                });
            })
        })
    }

    // 根据跳转方式，设置画面可否编辑、加载内容
    var setValueByType = function(){
        var _sts = m.viewSts.val();
        if(_sts == "add"){
            // 设置业务日期
            getBusinessDate();
            // 新增默认未审核
            m.dj_status.val("1");
            //禁用审核按钮
            m.approvalBut.prop("disabled",true);
        }else if(_sts == "edit"){
            setDisable(false);
            // 查询加载数据
            getInventoryVouchers();
            //禁用审核按钮
            m.approvalBut.prop("disabled",true);
        }else if(_sts == "view"){
            setDisable(true);
            // 查询加载数据
            getInventoryVouchers();
            //检查是否允许审核
            _common.checkRole(m._voucherNo.val(),m.typeId.val(),function (success) {
                if(success){
                    // 审核状态可以更改实际调拨量，因此编辑按钮可用
                    m.actualQty.attr("e","1");
                    m.approvalBut.prop("disabled",false);
                    $("#updateItemDetails").prop("disabled", false);
                }else{
                    m.approvalBut.prop("disabled",true);
                }
            });
        }
    }

    // 设置为查看模式
    var setDisable = function (flag) {
        $("#tf_cd").prop("disabled", true);
        $("#tf_date").prop("disabled", true);
        $("#dj_status").prop("disabled", true);
        $("#aStore").prop("disabled", true);
        $("#creater").prop("disabled", true);
        $("#ca_date").prop("disabled", true);
        $("#modifier").prop("disabled", true);
        $("#md_date").prop("disabled", true);
        $("#resetBtn").prop("disabled", true);
        $("#aStoreRefresh").hide();
        $("#aStoreRemove").hide();
        $("#cRemark").prop("disabled", flag);
        $("#saveBtn").prop("disabled", flag);
        $("#addItemDetails").prop("disabled", flag);
        $("#updateItemDetails").prop("disabled", flag);
        $("#deleteItemDetails").prop("disabled", flag);
        //附件按钮
        $("#addByFile").prop("disabled",flag);
        $("#updateByFile").prop("disabled",flag);
        $("#deleteByFile").prop("disabled",flag);
    }

    // 设置弹窗内容是否允许编辑
    var setDialogDisable = function (flag) {
        let flg = false;
        if(flag>0){flg = true;}
        $("#actualQty").prop("disabled", flg);
        $("#adjustReason").prop("disabled", flg);
        $("#item_input").prop("disabled", flg);
        $("#In_item_input").prop("disabled", flg);
        if(flag==2){ // modify
            $("#dialog_affirm").prop("disabled", false);
            $("#actualQty").prop("disabled", false);
            // $("#transferInQty").prop("disabled", false);
            $("#item_input").prop("disabled", false);
            $("#In_item_input").prop("disabled", false);
            $("#itemRefresh").show();
            $("#itemRemove").show();
            $("#In_itemRefresh").show();
            $("#In_itemRemove").show();
        }else{
            $("#dialog_affirm").prop("disabled", flg);
            $("#actualQty").prop("disabled", flg);
            // $("#transferInQty").prop("disabled", flg);
            $("#itemRefresh").hide();
            $("#itemRemove").hide();
            $("#In_itemRefresh").hide();
            $("#In_itemRemove").hide();
        }
        if(flag){
            $("#reasonRefresh").hide();
            $("#reasonRemove").hide();
        }else{
            $("#itemRefresh").show();
            $("#itemRemove").show();
            $("#In_itemRefresh").show();
            $("#In_itemRemove").show();
            $("#reasonRefresh").show();
            $("#reasonRemove").show();
        }
    }

    // 清空转出商品弹窗内容
    var clearDialog = function (flag) {
        if(flag){
            $("#item_input").val("").attr("k","").attr("v","");
            $.myAutomatic.cleanSelectObj(adjustReason);
        }
        $("#actualQty").val("");
        $("#item_input_cd").val("");
        $("#item_input_uom").val("");
        $("#item_input_spec").val("");
        $("#item_input_price").val("");
        $("#on_hand_qty").val("");
        $("#tax_rate").val("");
        $('#inventoryQty').val("");
    };

    var clearInItemialog = function (flag) {
        if(flag){
            $.myAutomatic.cleanSelectObj(InItemInput);
        }
        $("#In_item_input_cd").val("");
        $("#In_item_input_uom").val("");
        $("#In_item_input_spec").val("");
        $("#In_item_input_price").val("");
        $("#In_tax_rate").val("");
        $("#transferInQty").val("");
        $('#inventoryInQty').val("");
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
        switch(flgType) {
            case "1":
                initTable1();//列表初始化
                break;
            case "2":
                break;
            default:
                m.error_pcode.show();
                m.main_box.hide();
        }
    }

    // 验证录入项是否符合
    var verifySearch = function(){
        let temp = null;
        if(m.viewSts.val() == 'edit'){
            temp = m.tf_cd.val();
            if(temp == null || $.trim(temp)==""){
                _common.prompt("The Document No. cannot be empty, please refresh the page!",3,"error"); // 传票编号不能为空
                $("#tf_cd").css("border-color","red");
                m.tf_cd.focus();
                return false;
            }else {$("#tf_cd").css("border-color","#CCC");}
        }
        temp = m.tf_date.val();
        if(temp == null || $.trim(temp)==""){
            _common.prompt("The Document Date cannot be empty, please refresh the page!",3,"error"); // 传票时间不能为空
            $("#tf_date").css("border-color","red");
            m.tf_date.focus();
            return false;
        }else {$("#tf_date").css("border-color","#CCC");}

        temp = $("#aStore").attr('k');
        if(temp == null || $.trim(temp)==""){
            _common.prompt("Please check whether the store is correct!",5,"error"); // 请检查门店是否正确
            $("#aStore").css("border-color","red");
            m.aStore.focus();
            return false;
        }else {$("#aStore").css("border-color","#CCC");}


        return true;
    }

    // 验证弹窗页面录入项是否符合
    var verifyDialogSearch = function(){
        let flg = $('#update_dialog').attr("flg");
        if(flg!="add"&&flg!="upt"){return false;}
        let temp = $("#item_input").attr('k');
        if(temp == null || $.trim(temp)==""){
            _common.prompt("The transfer out item cannot be empty!",3,"error");
            $("#item_input").css("border-color","red");
            $("#item_input").focus();
            return false;
        }else {
            $("#item_input").css("border-color","#CCC");
        }

        let InTemp = $("#In_item_input").attr('k');
        if(InTemp == null || $.trim(InTemp)==""){
            _common.prompt("The transfer in item cannot be empty!",3,"error");
            $("#In_item_input").css("border-color","red");
            $("#In_item_input").focus();
            return false;
        }else {
            $("#In_item_input").css("border-color","#CCC");
        }

        flg = m.actualQty.attr("e");
        let handQty = reThousands(m.inventoryQty.val());
        // if(parseInt(handQty) <= 0 ){
        //     $("#item_input").focus();
        //     _common.prompt("Target item is out of stock, cannot transfer out!",3,"info"); // 转出商品库存为0，不能转移
        //     return false;
        // }else {
        if (flg != '1') {
            temp = reThousands($("#actualQty").val());
            if (temp == null || $.trim(temp) == "") {
                _common.prompt("The Tranfer Qty cannot be empty!", 3, "error");
                $("#actualQty").css("border-color", "red");
                $("#actualQty").focus();
                return false;
            } else if (temp == '0') {
                _common.prompt("Transfer Qty cannot be 0, please enter again!", 3, "error");
                $("#actualQty").css("border-color", "red");
                $("#actualQty").focus();
                return false;
            } else {
                let reg = /^[0-9]*$/;
                if (!reg.test(Math.abs(temp))) {
                    _common.prompt("Tranfer Qty can only be integers!", 3, "error");
                    $("#actualQty").css("border-color", "red");
                    $("#actualQty").focus();
                    return false;
                }
                // else {
                //     if (Math.abs(parseInt(temp)) > Math.abs(parseInt(handQty))) {
                //         _common.prompt("Tranfer Qty cannot be more than actual stock quantity!", 3, "error");
                //         $("#actualQty").css("border-color", "red");
                //         $("#actualQty").focus();
                //         return false;
                //     } else {
                //         $("#actualQty").css("border-color", "#CCC");
                //     }
                // }
            }
        }

        temp = m.adjustReason.attr("k");
        if (temp == null || $.trim(temp) == "") {
            _common.prompt("Please select the write-off reason!", 3, "error");
            $("#adjustReason").css("border-color", "red");
            $("#adjustReason").focus();
            return false;
        } else {
            $("#adjustReason").css("border-color", "#CCC");
        }
        return true;
        // }
    }


    // 画面按钮点击事件
    var but_event = function(){
        $("#reasonRemove").on("click",function (e) {
            $.myAutomatic.cleanSelectObj(adjustReason);
        });

        $("#actualQty").blur(function () {
            $("#actualQty").val(toThousands(this.value));
            var Q=  m.Inqty.val()*m.actualQty.val();
            $("#transferInQty").val( Q.toFixed(4));
        });
        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#actualQty").focus(function(){
            $("#actualQty").val(reThousands(this.value));
        });

        $("#transferInQty").blur(function () {
            //  $("#transferInQty").val(toThousands(this.value));
            $("#transferInQty").val(this.value);
        });
        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#transferInQty").focus(function(){
            // $("#transferInQty").val(reThousands(this.value));
            $("#transferInQty").val(this.value);
        });

        // 重置按钮
        m.resetBtn.on("click",function(){
            $("#tf_cd").css("border-color","#CCC");
            $("#tf_date").css("border-color","#CCC");
            $("#aStore").css("border-color","#CCC");
            _common.myConfirm("Are you sure to reset?",function(result){
                if(result=="true"){
                    getBusinessDate();
                    m.tf_cd.val("");
                    m.dj_status.val("1");
                    m.cRemark.val("");
                    $.myAutomatic.cleanSelectObj(aStore);
                    $.myAutomatic.cleanSelectObj(adjustReason);

                }
            });
        });

        // 保存按钮
        m.saveBtn.on("click",function(){
            if(verifySearch()){
                let _storeCd = $("#aStore").attr('k');
                let _voucherNo = $("#tf_cd").val();
                let _type = '602'; // or '601'
                let _date = subfmtDate($("#tf_date").val());
                let _storeCd1 = $("#aStore").attr('k');
                let itemDetail = [],inItemDetail=[], num = 0, _amount = 0, _amountNoTax = 0, _taxAmt = 0;
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    ++num;
                    // 转出商品
                    let _price = $(this).find('td[tag=priceNoTax]').text();
                    let _qty = parseInt(reThousands($(this).find('td[tag=qty2]').text()))||0;
                    let _actualQty = (-parseInt(reThousands($(this).find('td[tag=bqty1]').text())))||0; // 取负数
                    let _rate = reThousands($(this).find('td[tag=taxRate]').text());
                    let _amtNoTax = accMul(_price, Math.abs(_actualQty));
                    let _amt = Number(accMul(_amtNoTax, accAdd(1,_rate))).toFixed(2);
                    let _tax = Number(accMul(_amtNoTax, _rate)).toFixed(2);
                    // 转入商品
                    let _price1 = $(this).find('td[tag=priceNoTax1]').text();
                    let _qty1 = parseInt(reThousands($(this).find('td[tag=qty2]').text()))||0;
                    // let _actualQty1 = parseInt(reThousands($(this).find('td[tag=transferInQty]').text()))||0;
                    let _actualQty1 = reThousands($(this).find('td[tag=transferInQty]').text())||0;
                    let _rate1 = reThousands($(this).find('td[tag=taxRate1]').text())||0;
                    let _amtNoTax1 = accMul(_price, Math.abs(_actualQty1));
                    let _amt1 = Number(accMul(_amtNoTax1, accAdd(1,_rate1))).toFixed(2);
                    let _tax1 = Number(accMul(_amtNoTax1, _rate1)).toFixed(2);
                    let orderOutItem = {
                        storeCd:_storeCd,
                        voucherNo:_voucherNo,
                        voucherDate:_date,
                        voucherType:'602',
                        articleId:$(this).find('td[tag=articleId]').text(),
                        storeCd1:_storeCd1,
                        adjustReason:$(this).find('td[tag=adjustReason]').text(),
                        barcode:$(this).find('td[tag=barcode]').text(),
                        salesUnitId:$(this).find('td[tag=uom]').text(),
                        qty2:_qty,
                        priceNoTax:_price,
                        amtNoTax:_amtNoTax,
                        amt:_amt,
                        amtTax:_tax,
                        taxRate:_rate,
                        qty1:_actualQty,
                        actualAmt:_amt,
                        displaySeq:num
                    }
                    itemDetail.push(orderOutItem);

                    let orderInItem = {
                        storeCd:_storeCd,
                        voucherNo:_voucherNo,
                        voucherDate:_date,
                        voucherType:'601',
                        articleId:$(this).find('td[tag=articleId1]').text(),
                        storeCd1:_storeCd1,
                        adjustReason:$(this).find('td[tag=adjustReason]').text(),
                        barcode:$(this).find('td[tag=barcode1]').text(),
                        salesUnitId:$(this).find('td[tag=uom1]').text(),
                        qty2:_qty1,
                        priceNoTax:_price1,
                        amtNoTax:_amtNoTax1,
                        amt:_amt1,
                        amtTax:_tax1,
                        taxRate:_rate1,
                        qty1:_actualQty1,
                        actualAmt:_amt1,
                        displaySeq:num
                    };
                    inItemDetail.push(orderInItem);

                    _amountNoTax = accAdd(_amountNoTax, _amtNoTax);
                    _amount = accAdd(_amount, _amt);
                    _taxAmt = accAdd(_taxAmt, _tax);
                });
                if(itemDetail.length < 1 || inItemDetail.length < 1){
                    _common.prompt("Commodity information cannot be empty!",5,"error"); // 传票商品信息不能为空
                    return false;
                }
                //附件信息
                var fileDetail = [],fileDetailJson = "";
                $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                    var file = {
                        informCd:_voucherNo,
                        fileType:'08',//文件类型 - 店内商品调拨
                        fileName:$(this).find('td[tag=fileName]').text(),//文件名称
                        filePath:$(this).find('td>a').attr("filepath"),//文件路径
                    }
                    fileDetail.push(file);
                });
                if(fileDetail.length>0){
                    fileDetailJson = JSON.stringify(fileDetail)
                }

                let bean = {
                    storeCd:_storeCd,
                    voucherNo:_voucherNo,
                    voucherType:'602',
                    voucherDate:_date,
                    storeCd1:_storeCd1,
                    voucherAmtNoTax:_amountNoTax,
                    voucherTax:_taxAmt,
                    voucherAmt:_amount,
                    remark:$("#cRemark").val()
                };
                let bean1 = {
                    storeCd:_storeCd,
                    voucherNo:_voucherNo,
                    voucherType:'601',
                    voucherDate:_date,
                    storeCd1:_storeCd1,
                    voucherAmtNoTax:_amountNoTax,
                    voucherTax:_taxAmt,
                    voucherAmt:_amount,
                    remark:$("#cRemark").val()
                };
                let _data = {
                    searchJson : JSON.stringify(bean),
                    listJson : JSON.stringify(itemDetail),
                    fileDetailJson : fileDetailJson,
                    pageType : '0'
                };
                let _inItem = {
                    searchJson : JSON.stringify(bean1),
                    listJson : JSON.stringify(inItemDetail),
                    pageType : '0'
                }
                let _url = '/inventoryVoucher/modify';
                let _inurl = '/modify';
                let _sts =  m.viewSts.val();
                if(_sts == 'add'){
                    _url = '/inventoryVoucher/save';
                    _inurl = '/insert';
                }
                _common.myConfirm("Are you sure to save?",function(result){
                    if(result!="true"){return false;}
                    setDisable(true);
                    m.returnsViewBut.prop("disabled",true);
                    $.myAjaxs({
                        url:url_root+_url,
                        async:true,
                        cache:false,
                        type :"post",
                        data :_data,
                        dataType:"json",
                        success:function(result){
                            if(result.success){
                                $.myAjaxs({
                                    url:url_left+_inurl,
                                    async:true,
                                    cache:false,
                                    type :"post",
                                    data :_inItem,
                                    dataType:"json",
                                    success:function(result){
                                        console.log("第二条明细保存成功");
                                    }
                                });
                                // 变为查看模式
                                setDisable(true);
                                m.viewSts.val("view");
                                m.ca_date.val(_common.formatCreateDate(result.date+result.hms));
                                getUserNameById(result.createUserId,function(res){
                                    if(res.success){
                                        m.creater.val(res.o);
                                    }
                                });
                                if(_sts == 'add'){
                                    $("#tf_cd").val(result.o);
                                }
                                _common.prompt("Data saved successfully！",2,"success",function(){/*保存成功*/
                                    m.returnsViewBut.prop("disabled",false);
                                    submitFlag=true;
                                    //发起审核
                                    let typeId =m.typeId.val();
                                    let	nReviewid =m.reviewId.val();
                                    let	recordCd = $("#tf_cd").val();
                                    _common.initiateAudit(_storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
                                        //审核按钮禁用
                                        m.approvalBut.prop("disabled",true);
                                        m.toKen.val(token);
                                    })
                                }, true);
                            }else{
                                _common.prompt(result.msg,5,"error");
                            }
                        },
                        error : function(e){
                            _common.prompt("Data saved failed！",5,"error"); // 传票保存失败
                        }
                    });
                })
            }
        });



        // 详情弹窗Close按钮
        m.dialog_cancel.on("click",function(){
            var  flg = $('#update_dialog').attr("flg");
            if(flg=="view"){
                $('#update_dialog').modal("hide");
                return false;
            }
            _common.myConfirm("Are you sure to close?",function(result){
                $("#item_input").css("border-color","#CCC");
                $("#actualQty").css("border-color","#CCC");
                $("#adjustReason").css("border-color","#CCC");
                if(result == "true"){
                    $('#update_dialog').modal("hide");
                }
            });
        });

        // 详情弹窗Submit按钮
        m.dialog_affirm.on("click",function(){
            if(!verifyDialogSearch()){return false;}
            // 判断商品是否存在
            let flg = $('#update_dialog').attr("flg");
            let _itemId = m.item_input.attr('k');
            let _inItemId = m.In_item_input.attr('k');
            let _select = null;
            let _inselect = null;
            if(selectTrTemp!=null){
                let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
                let incols = tableGrid.getSelectColValue(selectTrTemp,"articleId1");
                _select = cols['articleId'];
                _inselect = cols['articleId1'];
            }
            if(flg==='add' || _select !== _itemId && _inselect !== _inItemId){
                let _addFlg = true;
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    let articleId = $(this).find('td[tag=articleId]').text();
                    let articleId1 = $(this).find('td[tag=articleId1]').text();
                    if (articleId === _itemId && articleId1 === _inItemId) {
                        _addFlg = false;
                        return false; // 结束遍历
                    }
                });
                if(!_addFlg){
                    _common.prompt("Transfer Item already exists!", 3, "info"); // 转移商品已经存在
                    return false; // 停止执行
                }
            }
            _common.myConfirm("Are you sure to submit?",function(result){
                if(result == "true"){
                    if(flg=='add'){
                        let rowindex = 0;
                        let trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
                        if(trId!=null&&trId!=''){
                            rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
                        }
                        let tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
                            '<td tag="ckline" align="center" style="color:#428bca;" id="zgGridTtable_'+rowindex+'_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_'+rowindex+'_tr" name="zgGridTtable"> </td>' +
                            '<td align="right" width="120" tag="barcode" title="'+m.item_input_cd.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+m.item_input_cd.val()+'</td>' +
                            '<td align="right" width="120" tag="articleId" title="'+m.item_input.attr('k')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+m.item_input.attr('k')+'</td>' +
                            '<td align="left" width="110" tag="articleName" title="'+m.item_input.attr('v')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+m.item_input.attr('v')+'</td>' +
                            '<td align="left" width="60" tag="uom" title="'+m.item_input_uom.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+m.item_input_uom.val()+'</td>' +
                            '<td align="left" width="100" tag="spec" title="'+m.item_input_spec.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_spec" tdindex="zgGridTtable_spec">'+m.item_input_spec.val()+'</td>' +
                            '<td align="right" tag="priceNoTax" style="display: none;" title="'+m.item_input_price.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_priceNoTax" tdindex="zgGridTtable_priceNoTax">'+m.item_input_price.val()+'</td>' +
                            '<td align="right" tag="taxRate" style="display: none;" title="'+m.tax_rate.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+m.tax_rate.val()+'</td>' +

                            '<td align="right" width="110" tag="barcode1" title="'+m.In_item_input_cd.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode1" tdindex="zgGridTtable_barcode1">'+m.In_item_input_cd.val()+'</td>' +
                            '<td align="right" width="110" tag="articleId1" title="'+m.In_item_input.attr('k')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId1" tdindex="zgGridTtable_articleId1">'+m.In_item_input.attr('k')+'</td>' +
                            '<td align="left" width="110" tag="articleName1" title="'+m.In_item_input.attr('v')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleName1" tdindex="zgGridTtable_articleName1">'+m.In_item_input.attr('v')+'</td>' +
                            '<td align="left" width="60" tag="uom1" title="'+m.In_item_input_uom.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_uom1" tdindex="zgGridTtable_uom1">'+m.In_item_input_uom.val()+'</td>' +
                            '<td align="left" width="100" tag="spec1" title="'+m.In_item_input_spec.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_spec1" tdindex="zgGridTtable_spec1">'+m.In_item_input_spec.val()+'</td>' +
                            '<td align="right" tag="priceNoTax1" style="display: none;" title="'+m.In_item_input_price.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_priceNoTax1" tdindex="zgGridTtable_priceNoTax1">'+m.In_item_input_price.val()+'</td>' +
                            '<td align="right" tag="taxRate1" style="display: none;" title="'+m.In_tax_rate.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_taxRate1" tdindex="zgGridTtable_taxRate1">'+m.In_tax_rate.val()+'</td>' +

                            '<td align="right" width="120" tag="bqty1" title="'+m.actualQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_bqty1" tdindex="zgGridTtable_bqty1">'+m.actualQty.val()+'</td>' +
                            '<td align="right" width="100" tag="inventoryQty" style="display: none;" title="'+m.inventoryQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_inventoryQty" tdindex="zgGridTtable_inventoryQty">'+m.inventoryQty.val()+'</td>' +
                            '<td align="right" width="120" tag="transferInQty" title="'+$('#transferInQty').val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_transferInQty" tdindex="zgGridTtable_transferInQty">'+$('#transferInQty').val()+'</td>' +
                            '<td align="right" width="100" tag="inventoryInQty" style="display: none;" title="'+$('#inventoryInQty').val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_inventoryInQty" tdindex="zgGridTtable_inventoryInQty">'+$('#inventoryInQty').val()+'</td>' +
                            '<td align="left" tag="adjustReason" style="display: none;" title="'+m.adjustReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReason" tdindex="zgGridTtable_adjustReason">'+m.adjustReason.attr("k")+'</td>' +
                            '<td align="left" width="130" tag="adjustReasonText" title="'+m.adjustReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReasonText" tdindex="zgGridTtable_adjustReasonText">'+m.adjustReason.attr("v")+'</td>' +
                            '</tr>';
                        $("#zgGridTtable>.zgGrid-tbody").append(tr);
                    }else{
                        if (selectTrTemp==null){
                            _common.prompt("Please select a record first!",5,"error"); // 未选中商品
                            return false;
                        }else{
                            let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
                            let incols = tableGrid.getSelectColValue(selectTrTemp,"articleId1");
                            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                                let articleId = $(this).find('td[tag=articleId]').text();
                                let articleId1 = $(this).find('td[tag=articleId1]').text();
                                if(articleId===cols["articleId"] && articleId1===incols["articleId1"]){
                                    $(this).find('td[tag=barcode]').text(m.item_input_cd.val());
                                    $(this).find('td[tag=articleId]').text(m.item_input.attr('k'));
                                    $(this).find('td[tag=articleName]').text(m.item_input.attr('v'));
                                    $(this).find('td[tag=uom]').text(m.item_input_uom.val());
                                    $(this).find('td[tag=spec]').text(m.item_input_spec.val());
                                    $(this).find('td[tag=priceNoTax]').text(m.item_input_price.val());
                                    $(this).find('td[tag=taxRate]').text(m.tax_rate.val());

                                    $(this).find('td[tag=barcode1]').text(m.In_item_input_cd.val());
                                    $(this).find('td[tag=articleId1]').text(m.In_item_input.attr('k'));
                                    $(this).find('td[tag=articleName1]').text(m.In_item_input.attr('v'));
                                    $(this).find('td[tag=uom1]').text(m.In_item_input_uom.val());
                                    $(this).find('td[tag=spec1]').text(m.In_item_input_spec.val());
                                    $(this).find('td[tag=priceNoTax1]').text(m.In_item_input_price.val());
                                    $(this).find('td[tag=taxRate1]').text(m.In_tax_rate.val());
                                    $(this).find('td[tag=bqty1]').text(m.actualQty.val());
                                    $(this).find('td[tag=inventoryQty]').text(m.inventoryQty.val());
                                    $(this).find('td[tag=transferInQty]').text($("#transferInQty").val());
                                    $(this).find('td[tag=inventoryInQty]').text($("#inventoryInQty").val());
                                    $(this).find('td[tag=adjustReason]').text(m.adjustReason.attr("k"));
                                    $(this).find('td[tag=adjustReasonText]').text(m.adjustReason.attr("v"));
                                }
                            });
                        }
                    }
                    m.isChange.val('1');
                    $('#update_dialog').modal("hide");
                }
            });
        });
    }

    // 获取传票头档信息
    var getInventoryVouchers = function(){
        let _data = {
            storeCd:m._storeCd.val(),
            voucherNo:m._voucherNo.val(),
            voucherType:m._voucherType.val(),
            voucherDate:m._voucherDate.val(),
            storeCd1:m._storeCd1.val()
        }
        let param = JSON.stringify(_data);
        $.myAjaxs({
            url:url_root+"/inventoryVoucher/get",
            async:true,
            cache:false,
            type :"post",
            data :"searchJson="+param,
            dataType:"json",
            success: function(result){
                if(result.success){
                    // 获取商品详情
                    getDetails(param);
                    // 加载数据
                    m.tf_cd.val(result.o.voucherNo);
                    m.tf_date.val(fmtIntDate(result.o.voucherDate));
                    m.dj_status.val(result.o.reviewSts);
                    $.myAutomatic.setValueTemp(aStore,result.o.storeCd,result.o.storeName);
                    m.cRemark.val(result.o.remark);
                    m.md_date.val(_common.formatCreateDate(result.o.updateYmd));
                    m.ca_date.val(_common.formatCreateDate(result.o.createYmd));
                    // 获取名称
                    let _val = result.o.updateUserId;
                    if(!!_val){
                        getUserNameById(_val,function(res){
                            if(res.success){
                                m.modifier.val(res.o);
                            }
                        });
                    }
                    _val = result.o.createUserId;
                    if(!!_val){
                        getUserNameById(_val,function(res){
                            if(res.success){
                                m.creater.val(res.o);
                            }
                        });
                    }
                    // 替换商品下拉查询参数
                    let str = "&storeCd=" + result.o.storeCd;
                    $.myAutomatic.replaceParam(itemInput, str);
                    $.myAutomatic.replaceParam(InItemInput, str);

                    //加载附件信息
                    attachmentsParamGrid = "recordCd="+result.o.voucherNo+"&fileType=08";
                    attachmentsTable.setting("url", url_root + "/file/getFileList");//加载附件
                    attachmentsTable.setting("param", attachmentsParamGrid);
                    attachmentsTable.loadData(null);
                }else{
                    _common.prompt(result.msg,3,"info");
                    let _sts = m.viewSts.val();
                    if(_sts == "edit"){
                        setDisable();
                        m.viewSts.val("view");
                    }
                }
            },
            error : function(e){
                _common.prompt("Failed to load voucher info!",5,"error"); // 传票信息加载失败
                var _sts = m.viewSts.val();
                if(_sts == "edit"){
                    setDisable();
                    m.viewSts.val("view");
                }
            }
        });
    }

    // 获取传票商品详情
    var getDetails = function(param){
        paramGrid = "searchJson=" + param;
        tableGrid.setting("url", url_left + "/getDetails");
        tableGrid.setting("param", paramGrid);
        tableGrid.loadData();
    };

    // 根据Item Code取得该商品的详细对象
    var getItemInfoByItem = function(store, item, fun){
        $.myAjaxs({
            url:url_root+"/inventoryVoucher/getItem",
            async:true,
            cache:false,
            type :"post",
            data :"itemCode="+item+"&storeCd="+store,
            dataType:"json",
            success: fun
        });
    };

    // 判断商品是否为母货号
    var checkParent = function(item, fun){
        $.myAjaxs({
            url:url_root+"/ma1200/check",
            async:true,
            cache:false,
            type :"post",
            data :"itemId="+item,
            dataType:"json",
            success: fun
        });
    };

    // 根据Store No.取得该店铺信息
    var initAutomatic = function(){
        adjustReason = $("#adjustReason").myAutomatic({
            // url:url_root+"/cm9010/getReasonCode",
            url:url_root+"/cm9010/getItemOutInReasonCode",
            ePageSize:10,
            startCount:0,
        });

        // 门店选择
        aStore = $("#aStore").myAutomatic({
            url: url_root+"/ma1000/getStoreByPM",
            ePageSize: 5,
            startCount: 0,
            cleanInput: function() {
                var trList = $("#zgGridTtable  tr:not(:first)");
                trList.remove();
            },
            selectEleClick: function (thisObject) {
                var trList = $("#zgGridTtable  tr:not(:first)");
                trList.remove();

                // 替换商品下拉查询参数
                let str = "&storeCd=" + thisObject.attr("k");
                $.myAutomatic.replaceParam(itemInput, str);
                // $.myAutomatic.replaceParam(InItemInput, str);
            }
        });


        // 商品选择
        itemInput = $("#item_input").myAutomatic({
            //url: url_root+"/inventoryVoucher/getItemList",
            url: url_root+"/inventoryVoucher/getMa1172OutItemList",
            ePageSize: 5,
            startCount: 0,
            cleanInput: function() {
                clearDialog(false);
            },
            selectEleClick: function (thisObject) {
                clearDialog(false);
                $.myAutomatic.setValueTemp(itemInput,thisObject.attr("k"),thisObject.text());


                if(thisObject.attr('k') == $("#In_item_input").attr('k')){
                    $("#item_input").focus();
                    $.myAutomatic.cleanSelectObj(itemInput);
                    _common.prompt("A transfer out of a item cannot be the same as a transfer into a Item!",3,"info"); // 转出商品不能和转入商品相同
                    return false;
                }
                let _storeCd = $("#aStore").attr('k');
                $.myAutomatic.setValueTemp(itemInput,thisObject.attr("k"),thisObject.text());
                let str = "&storeCd=" + $("#aStore").attr("k")+"&outArticleId="+thisObject.attr("k");
                $.myAutomatic.replaceParam(InItemInput, str);
                let _itemId = thisObject.attr('k');
                if(!!_storeCd && !!_itemId){
                    checkParent(_itemId, function(res){
                        if(res.success){
                            getItemInfoByItem(_storeCd, _itemId,function(res){
                                if(res.success){
                                    $("#item_input_cd").val(res.o.barcode);
                                    $("#item_input_uom").val(res.o.uom);
                                    $("#item_input_spec").val(res.o.spec);
                                    $("#item_input_price").val(res.o.baseOrderPrice);
                                    $("#tax_rate").val(res.o.taxRate);
                                    // 查询实时库存
                                    getStock(_storeCd, _itemId,'out');
                                }else{
                                    $.myAutomatic.cleanSelectObj(itemInput);
                                    _common.prompt(res.msg,3,"info");
                                }
                            });
                        }else{
                            $.myAutomatic.cleanSelectObj(itemInput);
                            _common.prompt(res.message,3,"info");
                        }
                    });
                }

            }
        });

        // 商品选择
        InItemInput = $("#In_item_input").myAutomatic({
            // url: url_root+"/inventoryVoucher/getItemList",
            url: url_root+"/inventoryVoucher/getMa1172InItemList",
            ePageSize: 5,
            startCount: 0,
            cleanInput: function() {
                clearInItemialog(false);
            },
            selectEleClick: function (thisObject) {

                clearInItemialog(false);
                if(thisObject.attr('k') === $("#item_input").attr('k')){
                    $("#In_item_input").focus();
                    $.myAutomatic.cleanSelectObj(InItemInput);
                    _common.prompt("A transfer out of a item cannot be the same as a transfer into a Item!",3,"info"); // 转出商品不能和转入商品相同
                    return false;
                }
                let _storeCd = $("#aStore").attr('k');
                $.myAutomatic.setValueTemp(InItemInput,thisObject.attr("k"),thisObject.text());
                m.Inqty.val(thisObject.attr("hidek"));
                let _itemId = thisObject.attr('k');
                if(!!_storeCd && !!_itemId){
                    checkParent(_itemId, function(res){
                        if(res.success){
                            getItemInfoByItem(_storeCd, _itemId,function(res){
                                if(res.success){
                                    $("#In_item_input_cd").val(res.o.barcode);
                                    $("#In_item_input_uom").val(res.o.uom);
                                    $("#In_item_input_spec").val(res.o.spec);
                                    $("#In_item_input_price").val(res.o.baseOrderPrice);
                                    $("#In_tax_rate").val(res.o.taxRate);
                                    // 查询实时库存
                                    getStock(_storeCd, _itemId,'in');
                                }else{
                                    $.myAutomatic.cleanSelectObj(InItemInput);
                                    _common.prompt(res.msg,3,"info");
                                }
                            });
                        }else{
                            $.myAutomatic.cleanSelectObj(InItemInput);
                            _common.prompt(res.message,3,"info");
                        }
                    });
                }
            }
        });
    };

    // 根据User Id取得用户名称
    var getUserNameById = function(userId, fun){
        $.myAjaxs({
            url:url_root+"/inventoryVoucher/getuser",
            async:true,
            cache:false,
            type :"post",
            data :"userId="+userId,
            dataType:"json",
            success: fun
        });
    }

    // 查询商品库存数量
    var getStock = function(storeCd, itemId,flg){
        $.myAjaxs({
            url:url_root+"/inventoryVoucher/getStock",
            async:true,
            cache:false,
            type :"post",
            data :"storeCd="+storeCd+"&itemId="+itemId,
            dataType:"json",
            success: function (result) {
                if(result.success){
                    if(flg === 'out'){
                        m.on_hand_qty.val(toThousands(parseInt(result.o.realtimeQty)));
                        m.inventoryQty.val(toThousands(parseInt(result.o.realtimeQty)));
                    }else if(flg === 'in'){
                        $("#inventoryInQty").val(toThousands(parseInt(result.o.realtimeQty)));
                    }
                }else{
                    _common.prompt(result.msg, 3, "error");
                }
            },
            error : function(e){
                _common.prompt("Query inventory quantity failed, please try again!",3,"error");
            }
        });
    }

    // 表格初始化
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Document Details",
            param:paramGrid,
            localSort: true,
            colNames:["Out Item Barcode","Transfer Out Item","Out Item Name","UOM","Specification","Price","Rate",
                "In Item Barcode","Transfer In Item","In Item Name","UOM","Specification","Price","Rate",
                "Transfer Out Qty","Out Inventory Qty","Transfer In Qty","In Inventory Qty","adjustReason","Transfer Reason"],
            colModel:[
                /*转移的A商品*/
                {name:"barcode",type:"text",text:"right",width:"120",ishide:false,css:""},
                {name:"articleId",type:"text",text:"right",width:"120",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"110",ishide:false,css:""},
                {name:"uom",type:"text",text:"left",width:"60",ishide:false,css:""},
                {name:"spec",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"priceNoTax",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
                {name:"taxRate",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
                /*转移到的B商品*/
                {name:"barcode1",type:"text",text:"right",width:"110",ishide:false,css:""},
                {name:"articleId1",type:"text",text:"right",width:"110",ishide:false,css:""},
                {name:"articleName1",type:"text",text:"left",width:"110",ishide:false,css:""},
                {name:"uom1",type:"text",text:"left",width:"60",ishide:false,css:""},
                {name:"spec1",type:"text",text:"left",width:"100",ishide:false,css:""},
                {name:"priceNoTax1",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
                {name:"taxRate1",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
                /*转出的数量*/
                {name:"bqty1",type:"text",text:"right",width:"120",ishide:false,css:"",getCustomValue:getThousands},
                {name:"inventoryQty",type:"text",text:"right",width:"120",ishide:true,css:"",getCustomValue:getThousands},
                /*转入的数量*/
                {name:"transferInQty",type:"text",text:"right",width:"120",ishide:false,css:""},
                {name:"inventoryInQty",type:"text",text:"right",width:"120",ishide:true,css:"",getCustomValue:getThousands},
                {name:"adjustReason",type:"text",text:"left",ishide:true},
                {name:"adjustReasonText",type:"text",text:"left",width:"130",ishide:false,css:""},
            ],//列内容
            width:"max",//宽度自动
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:false,//是否需要分页
            // sidx:"bm.bm_type,order.bm_code",//排序字段
            // sord:"asc",//升降序
            isCheckbox:true,
            freezeHeader:true,

            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            loadCompleteEvent: function (self) {
                table = self;
                isDisabledBtn();
                return self;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                isDisabledBtn();
                let _list = tableGrid.getCheckboxTrs();
                if(_list.length<1){
                    selectTrTemp = null;//清空选择的行
                }
            },
            buttonGroup:[
                {
                    butType: "add",
                    butId: "addItemDetails",
                    butText: "Add",
                    butSize: ""
                },{
                    butType: "update",
                    butId: "updateItemDetails",
                    butText: "Modify",
                    butSize: ""
                },{
                    butType: "view",
                    butId: "viewItemDetails",
                    butText: "View",
                    butSize: ""//,
                },{
                    butType: "delete",
                    butId: "deleteItemDetails",
                    butText: "Delete",
                    butSize: ""//,
                },
                {butType:"custom",butHtml:"<button id='attachments' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>"},//附件
            ],
        });
    };
    // 多选时查看、编辑禁用
    function isDisabledBtn(){
        let flg = m.viewSts.val();
        let _list = tableGrid.getCheckboxTrs();
        if(_list == null || _list.length > 1){
            $("#updateItemDetails").prop("disabled",true);
            $("#viewItemDetails").prop("disabled",true);
        }else{
            if(flg=="add" || flg=="edit"){
                $("#updateItemDetails").prop("disabled",false);
            }
            $("#viewItemDetails").prop("disabled",false);
        }
    }
    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtIntDate(value);
        }
        return $(tdObj).text(fmtIntDate(value));
    }

    // 格式化数字类型的日期
    function fmtIntDate(date){
        var res = "";
        if(date!=null&&date.trim()!=''&&date.length==8) {
            res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        }
        return res;
    }

    // 格式化数字类型的日期
    function fmtStrToInt(strDate){
        var res = "";
        res = strDate.replace(/-/g,"");
        return res;
    }

    // 运算
    function accAdd(arg1,arg2){
        var r1,r2,m;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        return (accMul(arg1,m)+accMul(arg2,m))/m;
    }
    function accMul(arg1,arg2){
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    }

    /**
     * 判断传票类型
     * @param v
     * @returns {string}
     */
    function ifType(v) {
        if($("#dj_type").attr('k') == "10"){
            v = "602";
        }else if($("#dj_type").attr('k') == "20"){
            v = "601";
        }
        return v;
    }

    // 日期转换
    function subfmtDate(date){
        let res = "";
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    // 为0返回'0'
    function getString0(tdObj, value){
        let _value = value == 0 ? "0" : value;
        return $(tdObj).text(_value);
    }

    // 请求下拉数据
    function getSelectOptions(title, url, param, fun) {
        $.myAjaxs({
            url:url_root+url,
            async:true,
            cache:false,
            type :"post",
            data : param,
            dataType:"json",
            success : fun,
            error : function(e){
                _common.prompt(title+" Failed to load data!",3,"info");
            }
        });
    }


    // 获取业务日期
    function getBusinessDate(){
        getSelectOptions("Document Date","/cm9060/getDate", null,function(res){
            let date = "";
            if(!!res && res.toString().length==8){
                res = res.toString();
                date = res.substring(6,8)+"/"+res.substring(4,6)+"/"+res.substring(0,4);
            }
            $('#tf_date').val(date);
        });
    }

    // 请求加载下拉列表
    // function getSelectValue(){
    // 	// 加载select
    // 	getReasonSelect("Transfer Reason","adjustReason", "00235");
    // }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('itemTransfersEdit');
_start.load(function (_common) {
    _index.init(_common);
});
