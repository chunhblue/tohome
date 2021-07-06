require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax = require("myAjax");
define('receiptEdit', function () {
    var self = {};
    var url_left = "",
        paramGrid = null,
        selectTrTemp = null,
        itemInput = null,
        file = $('<input type="file"/>'),
        reviewStatus = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},//临时行数据存储
        dataForm = [],
        systemPath = null,
        //附件
        attachmentsParamGrid = null,
        subFlag='';
    const KEY = 'STOCKTAKING_RESULT_AND_QUERY';
    var m = {
        toKen: null,
        use: null,
        up: null,
        entiretyBmStatus: null,
        item_name: null,
        itemInput: null,
        tableGrid: null,
        selectTrObj: null,
        error_pcode: null,
        identity: null,
        reject_dialog: null,
        input_item: null,
        pd_date: null,
        pd_start_time: null,
        pd_end_time: null,
        clear_pd_time: null,
        saveBut: null,
        searchbtn: null,//检索按钮
        returnsViewBut: null,
        store: null,
		storeNo: null, // 盘点单号
        piStoreParam: null, // 隐藏域 store 参数
        tempTableType: null,
        searchJson: null,
        enterFlag: null,
        piCdParam: null,
        piDateParam: null,
        remarks: null,
		search_item_code_but: null,
        searchItemInp: null, // 商品定位输入框
        searchItemBtn: null, // 商品定位按钮
        resetItemBtn: null, // 商品定位reset按钮
        //审核
        typeId:null,
        reviewId:null
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/stocktakeEntry";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //首先验证当前操作人的权限是否混乱，
        if (m.use.val() == "0") {
            but_event();
        } else {
            m.error_pcode.show();
            m.main_box.hide();
        }
        // 初始化附件列表
        attachments_event();
        // 初始化store下拉
        initAutoMatic();
        // 下拉框数据
        getComboxData();
		$("#createUser").prop("disabled", true);
        //根据登录人的不同身份权限来设定画面的现实样式
        // initPageBytype(m.identity.val());
        initTable1();
        //表格内按钮事件
        table_event();
        setDisable(true);
        if (m.enterFlag.val() == 'update') {
            $("#saveBut").prop("disabled", false);
            $("#updatePlanDetails").prop("disabled", false);
            $("#importResult").prop("disabled", false);
            // $("#deletePlanDetails").prop("disabled", false);
            //附件按钮
            $("#addByFile").prop("disabled",false);
            $("#updateByFile").prop("disabled",false);
            $("#deleteByFile").prop("disabled",false);
        }
        // 获取数据
        getData(m.piCdParam.val(), m.piDateParam.val());
    }


    var table_event = function () {

        /*$("#zgGridTtable").on('mousewheel',function (event) {
            console.log(event);
        })*/

        //附件一览显示
        $("#attachments_view").on("click",function () {
            $('#attachments_dialog').modal("show");
        })

        $('#cancel').on('click',function () {
            $('#itemInput').css("border-color","#CCC");
            $('#firstQty').css("border-color","#CCC");
            _common.myConfirm("Are you sure to Cancel?",function(result) {
                if (result == "true") {
                    $('#update_dialog').modal("hide");
                }
            })
        });

        // submit 按钮
        $('#affirm').on('click', function () {
            if (!verifySearch()) {
                return;
            }

            let barcode = $('#barcode').val();
            let articleId = $('#itemInput').attr('k');
            let articleName = $('#itemInput').attr('v');
            let uom = $('#uom').val();
            let badQty = $('#badQty').val();
            let spec = $('#spec').val();
            let firstQty = $('#firstQty').val();
            let secondQty = $('#secondQty').val();

            _common.myConfirm("Are you sure to submit?", function (result) {
                if (result != "true") {
                    return false;
                }
                var _addFlg = false;
                $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                    var _articleId = $(this).find('td[tag=articleId]').text();
                    if (_articleId == articleId) {
                        _addFlg = true;
                        return false; // 结束遍历
                    }
                });

                if (subFlag=='update') {
                    var selIdx = selectTrTemp.attr('data-index');
                    $("#zgGridTtable>.zgGrid-tbody tr").each(function (index) {
                        var temp = $(this).find('td[tag=articleId]').text();
                        var _rowIndex = $(this).attr('data-index');
                        if (articleId==temp) {
                            if (_rowIndex!=selIdx) {
                                _common.prompt("Selected item already exists!", 5, "error");
                                return false; // 退出循环
                            }
                        }
                        if(_rowIndex==selIdx){
                            $(this).find('td[tag=barcode]').text(barcode);
                            $(this).find('td[tag=articleId]').text(articleId);
                            $(this).find('td[tag=articleName]').text(articleName);
                            $(this).find('td[tag=spec]').text(spec);
                            $(this).find('td[tag=uom]').text(uom);
                            $(this).find('td[tag=badQty]').text(badQty);
                            $(this).find('td[tag=firstQty]').text(firstQty);
                            $(this).find('td[tag=secondQty]').text(secondQty);

                            for (let i = 0; i < dataForm.length; i++) {
                                let item = dataForm[i];
                                if (selIdx==item.rowIndex) {
                                    dataForm[i].badQty=badQty;
                                    dataForm[i].firstQty=firstQty;
                                    dataForm[i].secondQty=secondQty;
                                }
                            }
                            _common.prompt("Data updated successful",3,"success"); //  修改成功
                        }
                    });
                }
                $('#update_dialog').modal("hide");
            })
        });

        $("#importResult").on("click",function(){
            file.click();
        });

        file.change(function(e){
            var select_file = file[0].files[0];
            let suffix = new RegExp(/[^\.]+$/).exec(select_file.name)[0];
            // let excel_reg = /([xX][lL][sS][xX]){1}$|([xX][lL][sS]){1}$/;
            let excel_reg = /([cC][sS][vV]){1}$/;
            if (!excel_reg.test(suffix)) {
                _common.prompt('Upload file format error!',5,"error");
                file.val('');
                return;
            }
            let storeCd = m.store.attr('k');
            var formData = new FormData();
            formData.append('fileData',select_file);
            formData.append('storeCd',storeCd);
            formData.append('piCd',$("#storeNo").val());

            // 确定要导入吗？
            _common.myConfirm("Are you sure you want to import?",function(result) {
                if (result != "true") {
                    file.val('');
                    return false;
                }
                _common.loading();
                $.ajax({
                    url:url_left+"/uploadResult",
                    dataType:'json',
                    type:'POST',
                    async: true,
                    data: formData,
                    processData : false, // 使数据不做处理
                    contentType : false, // 不要设置Content-Type请求头
                    success: function(data){
                        file.val('');
                        if (data.success) {
                            if(data.message !== null && data.message !== 'undefined'){
                                _common.prompt(data.message,5,"success");
                            }
                            addItemList(data.data)
                        } else {
                            _common.prompt(data.message,5,"errer");
                        }
                        _common.loading_close();
                    },
                    error: function () {
                        _common.loading_close();
                    }
                });
            });
        })

        $("#updatePlanDetails").on("click", function () {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "error");
            } else {
                setDialogDisable(true);
                subFlag = 'update';
                let record = getSelectVal();
                $('#update_dialog').modal("show");
                $('#barcode').val(record.barcode);
                $.myAutomatic.setValueTemp(itemInput,record.articleId,record.articleName)
                $('#uom').val(record.uom);
                $('#badQty').val(record.badQty);
                $('#spec').val(record.spec);
                $('#firstQty').val(record.firstQty);
                $('#secondQty').val(record.secondQty);
            }
        });
        // 删除按钮
        $("#deletePlanDetails").on("click", function () {
            if(selectTrTemp == null){
                _common.prompt("Please select the data to confirm deletion!",5,"error");/*请选择要确认删除的数据*/
                return false;
            }else{
                _common.myConfirm("Are you sure you want to delete?",function(result){
                    if(result == "true"){
                        // 选中的行号
                        let selIdx = $(selectTrTemp).attr('data-index');
                        var trList = $("#zgGridTtable  tr:not(:first)");
                        for (let i = 0; i < dataForm.length; i++) {
                            let item = dataForm[i];
                            if (item.rowIndex==selIdx) {
                                dataForm.splice(i,1); // 删除数组中数据
                                trList[i].remove();
                            }
                        }
                        _common.prompt("Data deleted successful",3,"success"); //  删除成功
                    }
                });
            }
        });
        // 导出按钮点击事件
        $("#exportInfo").on("click",function(){
            let _storeCd = m.store.attr('k');
            let itemDetail = [], num = 0;
            $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
                num++;
                let stockItem = {
                    barcode:$(this).find('td[tag=barcode]').text(),
                    articleId:$(this).find('td[tag=articleId]').text(),
                    articleName:$(this).find('td[tag=articleName]').text(),
                    spec:$(this).find('td[tag=spec]').text(),
                    uom:$(this).find('td[tag=uom]').text(),
                    firstQty:$(this).find('td[tag=firstQty]').text()
                }
                itemDetail.push(stockItem);
            })
            if (itemDetail.length<1) {
                _common.prompt("Please enter the inventory data!",5,"error");/*请录入盘点商品数据*/
                return;
            }
            var _storeName = "";
            if(m.store.attr('v')!=null){
                _storeName= m.store.attr('v').substring(6);
            }
            let bean = {
                piCd:m.piCdParam.val(),
                piDate:m.piDateParam.val(),
                storeCd:m.store.attr('k'),
                storeName:_storeName
            };
            let _data = {
                searchJson : JSON.stringify(bean),
                listJson : JSON.stringify(itemDetail)
            };
            _common.loading();
            $.myAjaxs({
                url:url_left+"/exportDetail",
                async:true,
                cache:false,
                type :"post",
                data :_data,
                dataType:"json",
                success:function(result){
                    if(result.success){
                        //导出excle
                        window.location.href=url_left+"/download?store="+_storeCd;
                    } else {
                        _common.prompt(result.msg,5,"error");
                    }
                    _common.loading_close();
                },
                error : function(e){
                    _common.prompt("Data export failed！",5,"error"); // 传票保存失败
                    common.loading_close();
                }
            });
        });

        //返回一览
        m.returnsViewBut.on("click",function(){
            var bank = $("#saveBut").attr("disabled");
            if (bank!='disabled' ) {
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
                    if (result==="true") {
                        _common.updateBackFlg(KEY);
                        top.location = url_left;
                    }});
            }
            if (bank==='disabled' ) {
                _common.updateBackFlg(KEY);
                top.location = url_left;
            }
        });
    }

    //附件
    var attachments_event = function () {
        //加载附件信息
        let piCd = m.piCdParam.val();
        attachmentsParamGrid = "recordCd="+piCd+"&fileType=07";
        //附件一览表格
        attachmentsTable = $("#attachmentsTable").zgGrid({
            title:"Attachments",
            url: systemPath + "/file/getFileList",
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
                if(result==="true"){
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

    // 设置弹窗内容是否允许编辑
    var setDialogDisable = function (flag) {
        $("#itemInput").prop("disabled", flag);
        let elem = flag?"none":"auto";
        $("#itemRefresh").attr("disabled",flag).css("pointer-events",elem);
        $("#itemRemove").attr("disabled",flag).css("pointer-events",elem);
        if(flag === true){
            $("#itemRefresh").hide();
            $("#itemRemove").hide();
        }else {
            $("#itemRefresh").show();
            $("#itemRemove").show();
        }
    }

    // 保存导入的商品到表格里
    var addItemList = function (list) {
        if (!list) {
            return;
        }

        $("#zgGridTtable  tr:not(:first)").remove();
        dataForm=[];
        let htmls = '';
        list.forEach(function (item) {
            item.rowIndex=uuid();
            var html = '<tr data-index="'+item.rowIndex+'">' +
                '<td tag="barcode" width="130" title="'+isEmpty(item.barcode)+'" align="center" tdindex="zgGridTtable_barcode">'+isEmpty(item.barcode)+'</td>' +
                '<td tag="articleId" width="130" title="'+isEmpty(item.articleId)+'" align="center" tdindex="zgGridTtable_articleId">'+isEmpty(item.articleId)+'</td>' +
                '<td tag="articleName" width="130" title="'+isEmpty(item.articleName)+'" align="center" tdindex="zgGridTtable_articleName">'+isEmpty(item.articleName)+'</td>' +
                '<td tag="spec" width="130" title="'+isEmpty(item.spec)+'" align="center" tdindex="zgGridTtable_spec">'+isEmpty(item.spec)+'</td>' +
                '<td tag="uom" width="130" title="'+isEmpty(item.uom)+'" align="center" tdindex="zgGridTtable_uom">'+isEmpty(item.uom)+'</td>' +
                '<td tag="badQty" width="130" title="'+toThousands(item.badQty)+'" align="center" tdindex="zgGridTtable_badQty">'+toThousands(item.badQty)+'</td>' +
                '<td tag="firstQty" width="130" title="'+toThousands(item.firstQty)+'" align="center" tdindex="zgGridTtable_firstQty">'+toThousands(item.firstQty)+'</td>' +
                '<td tag="secondQty" width="130" title="" hidden align="center" tdindex="zgGridTtable_secondQty"></td>' +
                '</tr>';
            htmls+=html;
            dataForm.push(item);
        });
        tableGrid.append(htmls);
    };

    function uuid() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    }

    var isEmpty = function (str) {
        if (str == null || str == undefined || str === '') {
            return '';
        }
        return str;
    };

    //画面按钮点击事件
    var but_event = function () {
        $("#firstQty").blur(function () {
            if (this.value==null||this.value=='') {
                return;
            }
            $("#firstQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#firstQty").focus(function(){
            $("#firstQty").val(reThousands(this.value));
        });

        $("#secondQty").blur(function () {
            if (this.value==null||this.value=='') {
                return;
            }
            $("#secondQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#secondQty").focus(function(){
            $("#secondQty").val(reThousands(this.value));
        });

        // 保存
        m.saveBut.on('click',function () {
            let piCd = m.piCdParam.val();
            let piDate = m.piDateParam.val();
            let storeCd = m.store.attr('k');
            let remarks = m.remarks.val();

            if (dataForm.length<1) {
                _common.prompt("Please enter the inventory data!",5,"error");/*请录入盘点商品数据*/
                return;
            }




            //附件信息
            var fileDetail = [],fileDetailJson = "";
            $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                var file = {
                    fileType:'07',//文件类型 - 盘点
                    fileName:$(this).find('td[tag=fileName]').text(),//文件名称
                    filePath:$(this).find('td>a').attr("filepath"),//文件路径
                }
                fileDetail.push(file);
            });
            if(fileDetail.length>0){
                fileDetailJson = JSON.stringify(fileDetail)
            }

            // 设置头档数据
            let param = {
                'piCd': piCd,
                'piDate': piDate,
                'storeCd': storeCd,
                'remarks': remarks,
                'reviewStatus': reviewStatus,
                'fileDetailJson': fileDetailJson,
            }

            dataForm.forEach(function (item) {

                // 删除 articleName属性, 有特殊符号后台解码报错
                delete item.articleName;
                item.piCd=piCd;
                item.piDate=piDate;
                item.piStatus='02';
                item.storeCd=storeCd;
                if (item.firstQty!=null) {
                    item.firstQty=reThousands(item.firstQty);
                }
                if (item.secondQty!=null) {
                    item.secondQty=reThousands(item.secondQty);
                }
            });
            var record = encodeURIComponent(JSON.stringify(dataForm));
            _common.myConfirm("Are you sure you want to save?",function(result){
                if(result!=="true"){return false;}
                _common.loading();
                $.myAjaxs({
                    url:url_left+"/save",
                    async:true,
                    cache:false,
                    type :"post",
                    data :'record='+record+'&param='+JSON.stringify(param),
                    dataType:"json",
                    success:function(result){
                        if(result.success){
                            // 变为查看模式
                            setDisable(true);
                            m.enterFlag='view';
                            _common.prompt("Data saved successfully！",5,"success",function(){/*保存成功*/
                                // 为 null 代表 第一次 提交, 没有submit , 需要手动submit,
                                // 不为null 表示 审核驳回后的修改, 自动 submit
                                if (reviewStatus==null||reviewStatus=='') {
                                    return;
                                }
                                _common.loading_close();
                                //发起审核
                                var typeId =m.typeId.val();
                                var	nReviewid =m.reviewId.val();
                                var	recordCd = piCd;
                                _common.initiateAudit(storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
                                    //审核按钮禁用
                                    // m.approvalBut.prop("disabled",true);
                                    m.toKen.val(token);
                                })
                            }, true);
                        }else{
                            _common.prompt(result.msg,5,"error");
                        }
                        _common.loading_close();
                    },
                    error : function(e){
                        // setTimeout(" _common.prompt(\"Data saved failed！\",5,\"error\")",30000)
                        _common.prompt("Data saved failed！",5,"error");/*保存失败*/
                        _common.loading_close();
                    }
                });
            })
        });

        // 输入商品id定位功能
        m.searchItemBtn.on('click',function () {
            searchItem();
        });
        m.resetItemBtn.on('click',function () {
            m.searchItemInp.val("");
            searchItem();
        })
        //盘点日期
        m.pd_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });
        //开始时间
        m.pd_start_time.datetimepicker({
            language:'en',
            autoclose: true,//选中之后自动隐藏日期选择框
            clearBtn: false,//清除按钮
            todayBtn: true,//今日按钮
            format: 'dd/mm/yyyy hh:ii:ss',
            startView: 'month',// 进来是月
            minView: 'hour',// 可以看到小时
            minuteStep: 1, //分钟间隔为1分
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
        m.pd_end_time.datetimepicker({
            language:'en',
            autoclose: true,//选中之后自动隐藏日期选择框
            clearBtn: false,//清除按钮
            todayBtn: true,//今日按钮
            format: 'dd/mm/yyyy hh:ii:ss',
            startView: 'month',// 进来是月
            minView: 'hour',// 可以看到小时
            minuteStep: 1, //分钟间隔为1分
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
        m.clear_pd_time.on("click", function () {
            m.pd_start_time.val("");
            m.pd_end_time.val("");
        });
    }

    var searchItem = function () {
        // 还没有添加商品
        if (dataForm.length<1) {
            return;
        }
        var trList = $("#zgGridTtable  tr:not(:first)");
        trList.show();
        var searchItemCode = m.searchItemInp.val().trim().toLowerCase();
        if (searchItemCode=='') {
            trList.show();
        } else {
            for (let i = 0; i < trList.length; i++) {
                let articleId = $(trList[i]).find('td[tag=articleId]').text().toLowerCase();
                let articleName = $(trList[i]).find('td[tag=articleName]').text().toLowerCase();
                if (articleId.indexOf(searchItemCode)===-1&&articleName.indexOf(searchItemCode)===-1) {
                    $(trList[i]).hide();
                }
            }
        }
    };

    var getItemList = function (articleId,piCd,piDate) {
        $.myAjaxs({
            url: url_left + "/getItemInfo",
            async: true,
            cache: false,
            type: "get",
            data: 'itemCode='+articleId+'&piCd='+piCd+'&piDate='+piDate,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    $('#barcode').val(record.barcode);
                    $('#uom').val(record.uom);
                    $('#spec').val(record.spec);
                    $('#pmaCd').val(record.pmaCd);
                } else {
                    clearInput();
                    _common.prompt('No relevant product information was found!',5,"error");/*查询不到商品*/
                    return false;
                }
            }
        });
    }

    // 清空弹窗
    var clearInput = function () {
        $('#barcode').val('');
        $('#itemInput').val('').attr('k','').attr('v','');
        $('#uom').val('');
        $('#spec').val('');
        $('#badQty').val('');
        $('#firstQty').val('');
        $('#secondQty').val('');
    }

    // 获得选中的数据
    var getSelectVal = function() {
        if (!selectTrTemp) {
            return null;
        }
        let obj = {
            'barcode': $($(selectTrTemp[0]).find('td')[0]).text(),
            'articleId': $($(selectTrTemp[0]).find('td')[1]).text(),
            'articleName': $($(selectTrTemp[0]).find('td')[2]).text(),
            'spec': $($(selectTrTemp[0]).find('td')[3]).text(),
            'uom': $($(selectTrTemp[0]).find('td')[4]).text(),
            'badQty': $($(selectTrTemp[0]).find('td')[5]).text(),
            'firstQty': $($(selectTrTemp[0]).find('td')[6]).text(),
            'secondQty': $($(selectTrTemp[0]).find('td')[7]).text(),
        }
        return obj;
    }

    var getComboxData = function () {
        $.myAjaxs({
            url: systemPath + "/stocktakePlanEntry/getComboxData",
            async: false,
            cache: false,
            type: "get",
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    var piMetList = record.piMethod;
                    var piStsList = record.piStatus;
                    var piTypeList = record.piType;
                    piMetList.forEach(function (item) {
                        var temp = '<option value="' + item.codevalue + '">' + item.codename + '</option>'
                        $('#skMethod').append(temp);
                    });

                    piStsList.forEach(function (item) {
                        var temp = '<option value="' + item.codevalue + '">' + item.codename + '</option>'
                        $('#skStatus').append(temp);
                    });

                    piTypeList.forEach(function (item) {
                        var temp = '<option value="' + item.codevalue + '">' + item.codename + '</option>'
                        $('#skType').append(temp);
                    });
                }
            }
        });
    }

    var getData = function (piCd, piDate) {
        if (!piCd || !piDate) {
            clearAll();
            return;
        }
        _common.loading();
        $.myAjaxs({
            url: url_left + "/getData",
            async: true,
            cache: false,
            type: "get",
            data: "piCd=" + piCd + "&piDate=" + piDate,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    var record = result.o;
                    dataForm = [];
                    $("#storeNo").val(record.piCd);
                    $("#pd_date").val(record.piDate);
                    $("#skType").val(record.piType);
                    $("#skMethod").val(record.piMethod);
                    $("#skStatus").val(record.piStatus);
                    $("#pd_start_time").val(record.piStartTime);
                    $("#pd_end_time").val(record.piEndTime);
                    $("#createUser").val(record.createUserId);
                    $("#updateUser").val(record.updateUserId);
                    $("#remarks").val(record.remarks);
                    $.myAutomatic.setValueTemp(a_store, record.storeCd, record.storeName);//赋值
                    // 隐藏域设置 门店编号
                    m.piStoreParam.val(record.storeCd);
                    // 获取审核状态
                    reviewStatus = record.reviewStatus;

                    let businessDate = $("#businessDate").val();
                    // 不在当前盘点日期 reviewStatus==null -> 审核驳回可以跨天修改
                    if (m.enterFlag.val()=='update'&&reviewStatus==null&&businessDate!=piDate) {
                        m.enterFlag.val('view');
                        setDisable(true);
                    }

                    let startTime = formatTime(record.piStartTime);
                    let endTime = formatTime(record.piEndTime);
                    let nowTime = new Date().Format("hhmmss");
                    let timeFlg = (nowTime < startTime || nowTime > endTime);
                    // 不在当前盘点时间
                    if (m.enterFlag.val()=='update'&&reviewStatus==null&&timeFlg) {
                        m.enterFlag.val('view');
                        setDisable(true);
                    }

                    let auditFlg = reviewStatus == 5 || reviewStatus == 6 || reviewStatus == 10 || reviewStatus == null;
                    // 审核状态有误, 审核通过, 被驳回, 撤销, 没submit 的才可以修改
                    if (m.enterFlag.val()=='update'&&!auditFlg) {
                        m.enterFlag.val('view');
                        setDisable(true);
                    }

                    // 审核通过只能修改数量 注释 2021/03/19 by lch
                    /*if (m.enterFlag.val()=='update'&&reviewStatus!=null) {
                        $('#importResult').attr('disabled', true);
                        $('#deletePlanDetails').attr('disabled', true);
                    }*/

                    // 没有导出
                    if (m.enterFlag.val()=='update'&&record.exportFlg.trim()=='0') {
                        setDisable(true);
                    }

                    // 判断日结状态
                    var flg = _common.getValByKey('0005');
                    if (m.enterFlag.val()=='update'&&flg=='1') {
                        setDisable(true);
                    }

                    // 根据 status 设置初盘量 复盘量 栏位的 状态
                    if (m.enterFlag.val()=='update') {
                        if (reviewStatus==null) {
                            // 显示初盘量
                            $("#firstQty").prop("readonly",false);
                            $("#secondQty").prop("readonly",true);
                        } else {
                            // 显示复盘量
                            $("#firstQty").prop("readonly",false);
                            $("#secondQty").prop("readonly",false);
                            $("#remarks").prop("disabled",false);
                        }
                    }

                    dataForm=record.itemList;
                    // 封装明细数据
                    var trList = $("#zgGridTtable  tr:not(:first)");
                    trList.remove();
                    var htmls = '';
                    dataForm.forEach(function (item) {
                        var rowindex = 0;
                        var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
                        if(trId!=null&&trId!=''){
                            rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
                        }
                        item.rowIndex=uuid();

                        item.badQty = item.badQty != null ? toThousands(item.badQty) : '';
                        item.firstQty = item.firstQty != null ? toThousands(item.firstQty) : '';
                        item.secondQty = item.secondQty != null ? toThousands(item.secondQty) : '';

                        var html = '<tr data-index="'+item.rowIndex+'">' +
                            '<td tag="barcode" width="130" title="'+isEmpty(item.barcode)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+isEmpty(item.barcode)+'</td>' +
                            '<td tag="articleId" width="130" title="'+isEmpty(item.articleId)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+isEmpty(item.articleId)+'</td>' +
                            '<td tag="articleName" width="130" title="'+isEmpty(item.articleName)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+isEmpty(item.articleName)+'</td>' +
                            '<td tag="spec" width="130" title="'+isEmpty(item.spec)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_spec" tdindex="zgGridTtable_spec">'+isEmpty(item.spec)+'</td>' +
                            '<td tag="uom" width="130" title="'+isEmpty(item.uom)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+isEmpty(item.uom)+'</td>' +
                            '<td tag="badQty" width="130" title="'+toThousands(item.badQty)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_badQty" tdindex="zgGridTtable_badQty">'+toThousands(item.badQty)+'</td>' +
                            '<td tag="firstQty" width="130" title="'+toThousands(item.firstQty)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_firstQty" tdindex="zgGridTtable_firstQty">'+toThousands(item.firstQty)+'</td>' +
                            '<td tag="secondQty" hidden width="130" title="'+toThousands(item.secondQty)+'" align="center" id="zgGridTtable_'+rowindex+'_tr_secondQty" tdindex="zgGridTtable_secondQty">'+toThousands(item.secondQty)+'</td>' +
                            '</tr>'
                        htmls += html;
                    });
                    tableGrid.append(htmls);
                }
                _common.loading_close()
            }
        });
    };

    // 17:05:16 -> 170516
    var formatTime = function (str) {
        return str.replace(/:/g,'');
    }

	// 清空值
	var clearAll = function () {
		$("#storeNo").val('');
		$("#pd_date").val('');
		$("#store").val('');
		$("#skType").val('');
		$("#skMethod").val('');
		$("#skStatus").val('');
		$("#pd_start_time").val('');
		$("#pd_end_time").val('');
		$("#createUser").val('');
		$("#remarks").val('');
	}

    // 设置为查看模式
    var setDisable = function (flag) {
        $("#storeNo").prop("disabled", flag);
        $("#pd_date").prop("disabled", flag);
        $("#store").prop("disabled", flag);
        $("#skType").prop("disabled", flag);
        $("#pd_start_time").prop("disabled", flag);
        $("#pd_end_time").prop("disabled", flag);
        $("#clear_pd_time").prop("disabled", flag);
        $("#remarks").prop("disabled", flag);
        $("#saveBut").prop("disabled", flag);
        $("#updatePlanDetails").prop("disabled", flag);
        $("#importResult").prop("disabled", flag);
        // $("#deletePlanDetails").prop("disabled", flag);
        //附件按钮
        $("#addByFile").prop("disabled",flag);
        $("#updateByFile").prop("disabled",flag);
        $("#deleteByFile").prop("disabled",flag);
        if (flag) {
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        } else {
            $("#storeRefresh").show();
            $("#storeRemove").show();
        }
    }

    //初始化店铺下拉
    var initAutoMatic = function () {
        a_store = $("#store").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0
        });

        // 初始化 商品下拉
        itemInput = $("#itemInput").myAutomatic({
            url: url_left + "/getItemList",
            param: [
                {
                    'k': 'piCd',
                    'v': 'storeNo'
                },{
                    'k': 'piDate',
                    'v': 'pd_date'
                },{
                    'k': 'piStoreCd',
                    'v': 'piStoreParam'
                }
            ],
            ePageSize: 5,
            startCount: 0,
            cleanInput: function (thisObj) {
                clearInput();
            },
            selectEleClick: function (thisObj) {
                let articleId = thisObj.attr('k');
                checkParent(articleId, function(res){
                    if(res.success){
                        getItemList(articleId,m.piCdParam.val(),m.piDateParam.val());
                    }else{
                        $.myAutomatic.cleanSelectObj(itemInput);
                        _common.prompt(res.message,3,"info");
                    }
                });
            }
        });
    };

    // 判断商品是否为母货号
    var checkParent = function(item, fun){
        $.myAjaxs({
            url:systemPath+"/ma1200/check",
            async:true,
            cache:false,
            type :"post",
            data :"itemId="+item,
            dataType:"json",
            success: fun
        });
    };


    //验证检索项是否合法
    var verifySearch = function () {
        let articleId = $('#itemInput').attr('k');
        let articleName = $('#itemInput').attr('v');
        if (!articleId || !articleName || articleId===articleName) {
            $('#itemInput').focus();
            $('#itemInput').css("border-color","red");
            _common.prompt("Exceptional item code,can not modify!",5,"error");
            return false;
        }else {
            $('#itemInput').css("border-color","#CCC");
        }

        let firstIsDisable = $('#firstQty').prop('readonly');
        let secondIsDisable = $('#secondQty').prop('readonly');
        let firstQty = $('#firstQty').val();
        let secondQty = $('#secondQty').val();

        if (!firstIsDisable&&!firstQty) {
            $('#firstQty').focus();
            $('#firstQty').css("border-color","red");
            return false;
        } else if (!secondIsDisable&&!secondQty) {
            $('#secondQty').focus();
            $('#secondQty').css("border-color","red");
            return false;
        } else if (!firstIsDisable&&firstQty<1) {
            _common.prompt("Stocktaking Qty(Initial) cannot be less than 1!",3,"error");
            $("#firstQty").css("border-color","red");
            $("#firstQty").focus();
            return false;
        } else {
            $('#firstQty').css("border-color","#CCC");
        }
        return true;
    }


    // 栏位的值如果为null 返回 空
    function getStringEmpty(tdObj, value) {
        value = !value?'':value;
        return $(tdObj).text(value);
    };

    //表格初始化-采购样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Details",
            param: paramGrid,
            localSort: true,
            colNames: ["Item Barcode", "Item Code", "Item Name", "Spec", "UOM","Bad Merchandising", "Stocktaking Qty(Initial)", "Adjust Qty"],
            colModel: [
                {name: "barcode",type: "text",text: "right",width: "130",ishide: false,getCustomValue: getStringEmpty,},
                {name: "articleId",type: "text",text: "right",width: "130",ishide: false,getCustomValue: getStringEmpty,},
                {name: "articleName",type: "text",text: "left",width: "130",ishide: false,getCustomValue: getStringEmpty,},
                {name: "spec",type: "text",text: "left",width: "130",ishide: false,getCustomValue: getStringEmpty,},
                {name: "uom", type: "text", text: "left", width: "130", ishide: false, getCustomValue: getStringEmpty,},
                {name: "badQty", type: "text", text: "right", width: "130", ishide: false, getCustomValue: getThousands,},
                {name: "firstQty", type: "text", text: "right", width: "130", ishide: false, getCustomValue: getThousands,},
                {name: "secondQty", type: "text", text: "right", width: "130", ishide: true, getCustomValue: getThousands,},
                // 隐藏域
                // {name: "stocktakeTime", type: "text", text: "right", width: "130", ishide: true,},
            ],//列内容
            traverseData: dataForm,
            width: "max",//宽度自动
            height: 300,
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: false,//是否需要分页
            isCheckbox: false,
            freezeHeader:true,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            footerrow: true,
            loadCompleteEvent: function (self) {


            },
            userDataOnFooter: true,
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
            },
            buttonGroup: [
                {butType: "update", butId: "updatePlanDetails", butText: "Modify", butSize: ""},
                {butType: "upload", butId: "importResult", butText: "Import Stocktake Result", butSize: ""},
                // {butType: "delete", butId: "deletePlanDetails", butText: "Delete", butSize: ""},
                {butType:"custom",butHtml:"<button id='attachments_view' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>"},//附件
                {
                    butType:"custom",
                    butHtml:"<button id='exportInfo' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                },
            ],
        });
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receiptEdit');
_start.load(function (_common) {
    _index.init(_common);
});
