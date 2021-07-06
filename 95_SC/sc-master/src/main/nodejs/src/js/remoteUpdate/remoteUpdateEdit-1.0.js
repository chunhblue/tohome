require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("treeview");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('remoteUpdateEdit', function () {
    var self = {};
    var url_left = "",
        url_root = "",
        systemPath = "",
        attachmentsParamGrid = null,
        canCheckStoreTree = true;
    const KEY = 'POS_REMOTE_UPDATE_MANAGEMENT';
    var m = {
        reset: null,
        search: null,
        searchJson:null,
        toKen: null,
        // 栏位
        updateType : null,//分组
        startDate:null,//开始时间
        groupName:null,//分组名称
        StoreTree:null,//树状图
        id:null,//id
        oldFtpFilePath:null,//旧路径
        //按钮
        saveBtn:null,
        returnsViewBut:null,
        viewSts:null,
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
        url_left =_common.config.surl+"/remoteUpdate";
        systemPath = _common.config.surl;
        //初始化下拉
        initAutoMatic();
        //初始化下拉数据
        initStoreTree();
        //附件事件
        attachments_event();
        //事件绑定
        but_event();
        //初始化页面
        initParam();
    }

    // 初始化自动下拉
    var initAutoMatic = function () {
        //初始化日期
        m.startDate.datetimepicker({
            language: 'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        });
        // 获取区域经理
        initSelectOptions("Group Type","updateType","00920")
    }

    // 初始化参数, back 的时候 回显 已选择的检索项
    var initParam = function () {
        if(m.viewSts.val() == 'add'){
            m.startDate[0].disabled = false;
            m.updateType[0].disabled = false;
            m.groupName[0].disabled = false;
        }else if(m.viewSts.val() == 'view'){
            // 查询加载数据
            getRemoteUpdateEdit(m.id.val());
            // 设置不可点击
            setSomeDisable();
        }else if(m.viewSts.val() == 'edit'){
            // 查询加载数据
            getRemoteUpdateEdit(m.id.val());
            // 设置不可点击
            setSomeEnable();
        }
    }

    //画面按钮点击事件
    var but_event = function(){
        //保存事件
        m.saveBtn.on("click",function(){
            if (!verifySearch()) {
                return;
            }
            _common.myConfirm("Are you sure you want to save?", function (result) {
                if(result==="false"){
                   return;
                }
                //检测相同开始日相同店铺相同类型是否有同一设定
                var checkStoreCanAdd = false;
                var storeList = [];
                var storeTreeListChecked = m.StoreTree.treeview('getChecked');
                var storeTreeListExpanded = m.StoreTree.treeview('getExpanded');
                // 获取并集
                let storeTreeList = storeTreeListChecked.concat(storeTreeListExpanded.filter(v => !storeTreeListChecked.includes(v)))
                for(var i = 0;i<storeTreeList.length;i++){
                    var storeTreeNode = storeTreeList[i];
                    if(storeTreeNode.state.expanded){
                        if(storeTreeNode.state.checked){
                            storeList.push(storeTreeNode.structureCd+":1:1");
                        }else{
                            storeList.push(storeTreeNode.structureCd+":1:0");
                        }
                    }else{
                        if(storeTreeNode.state.checked){
                            storeList.push(storeTreeNode.structureCd+":0:1");
                        }else{
                            storeList.push(storeTreeNode.structureCd+":0:0");
                        }
                    }
                }
                var data = {
                    startDate:subfmtDate(m.startDate.val()),
                    updateType:m.updateType.val(),
                    storeCds:storeList,
                }
                if(m.viewSts.val() == "edit"){
                    data.id=m.id.val();
                }
                var formData = new FormData();
                formData.append("jsonStr", JSON.stringify(data));
                $.myAjaxs({
                    url: url_left + "/checkStoresCanAdd",
                    async: false,
                    cache: false,
                    type: "post",
                    data: formData,
                    dataType: "json",
                    processData: false,
                    contentType: false,
                    success: function (data, textStatus, xhr) {
                        var resp = xhr.responseJSON;
                        if (data.success == false) {
                            _common.prompt(data.message, 5, "error");
                            checkStoreCanAdd = false;
                        }else{
                            checkStoreCanAdd = true;
                        }
                        m.toKen.val(data.toKen);
                    },
                    complete: _common.myAjaxComplete,
                    error:function(){
                        _common.prompt("Check Stores Can Add Error", 5, "error");
                        checkStoreCanAdd = false;
                    }
                });
                if(!checkStoreCanAdd){
                    return;
                }
                //修改文件信息
                var fileDetail = [], fileDetailJson = "";
                var informCd = guid();
                $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                    var file = {
                        informCd: informCd,
                        fileType: '09',//文件类型 - 上传到的文件
                        fileName: $(this).find('td[tag=fileName]').text(),//文件名称
                        filePath: $(this).find('td>a').attr("filepath"),//文件路径
                    }
                    fileDetail.push(file);
                });
                if (fileDetail.length > 0) {
                    fileDetailJson = JSON.stringify(fileDetail)
                }

                var saveData = {
                    updateType:m.updateType.val(),
                    informCd: informCd,
                    startDate:subfmtDate(m.startDate.val()),
                    storeList:storeList,
                    groupName:m.groupName.val(),
                    fileDetailJson:fileDetailJson,
                    oldFtpFilePath:m.oldFtpFilePath.val(),
                }
                if(m.viewSts.val() == "edit"){
                    saveData.id = m.id.val();
                }

                var formData2 = new FormData();
                formData2.append("jsonStr", JSON.stringify(saveData));
                $.myAjaxs({
                    url: url_left + "/saveRemodeUpdate",
                    async: false,
                    cache: false,
                    type: "post",
                    data: formData2,
                    dataType: "json",
                    processData: false,
                    contentType: false,
                    success: function (data, textStatus, xhr) {
                        var resp = xhr.responseJSON;
                        if (data.success == false) {
                            _common.prompt(data.message, 5, "error");
                        }else {
                            _common.prompt("Operation Succeeded!",2,"success");
                            m.groupName.val(data.data.groupName);
                            setSomeDisable();
                        }
                        m.toKen.val(data.toKen);
                    },
                    complete: _common.myAjaxComplete
                });
            });
        });
        //检索按钮点击事件
        m.returnsViewBut.on("click",function(){
            _common.updateBackFlg(KEY);
            top.location = url_left;
        });

    }

    //验证保存项是否合法
    function verifySearch(){
        var groupName = m.groupName.val();
        if(groupName == null || groupName == ''){
            _common.prompt("The Group Name cannot be empty!",5,"error");
            m.groupName.focus();
            return false;
        }

        var updateType = m.updateType.val();
        if(updateType == null || updateType == ''){
            _common.prompt("Please select one Update Type!",5,"error");
            m.updateType.focus();
            return false;
        }

        var startDate = m.startDate.val();
        if(startDate == null || startDate == ''){
            _common.prompt("The Start Date cannot be empty!",5,"error");
            m.startDate.focus();
            return false;
        }

        var fmtStartDate = ddMMyyyyfmtDate(startDate);
        if(fmtStartDate <= new Date()){
            _common.prompt("The Start Date should be greater than today!",5,"error");
            m.startDate.focus();
            return false;
        }

        var storeTreeList = m.StoreTree.treeview('getChecked');
        var storeList = [];
        for(var i = 0;i<storeTreeList.length;i++){
            var storeTreeNode = storeTreeList[i];
            //只取最低层级
            if(storeTreeNode.text.indexOf("&nbsp;&nbsp;&nbsp;&nbsp;") != -1){
                storeList.push(storeTreeNode.text.substr(0,6));
            }
        }
        if(storeList.length == 0){
            _common.prompt("Pelese Choose the Remote Update Store at least one!",5,"error");
            m.StoreTree.focus();
            return false;
        }

        if($("#attachmentsTable>.zgGrid-tbody tr").length == 0){
            _common.prompt("Pelese Add Attachments at least one!",5,"error");
            m.StoreTree.focus();
            return false;
        }

        return true;
    }

    function getRemoteUpdateEdit(id) {
        $.myAjaxs({
            url:url_left+"/getRemodeUpdate",
            async:true,
            cache:false,
            type :"post",
            data :"id="+id,
            dataType:"json",
            success: function(result){
                if(result.success){
                    $('#updateType').val(result.o.updateType);
                    $('#startDate').val(fmtIntDate(result.o.startDate));
                    $('#groupName').val(result.o.groupName);
                    $('#oldFtpFilePath').val(result.o.oldFtpFilePath);
                    //加载附件信息
                    attachmentsParamGrid = "recordCd="+result.o.informCd+"&fileType=09";
                    attachmentsTable.setting("url", url_root + "/file/getFileList");//加载附件
                    attachmentsTable.setting("param", attachmentsParamGrid);
                    attachmentsTable.loadData(null);
                }else{
                    _common.prompt(result.msg,5,"error");
                }
            },
            error : function(e){
                _common.prompt("Request for funding details failed!",5,"error");/*请求经费详细信息失败*/
            }
        });
    }

    function initStoreTree() {
        $.myAjaxs({
            url:url_left+"/getStoreList?id="+m.id.val(),
            async:false,
            cache:false,
            type :"get",
            dataType:"json",
            success:showResponse,
            complete:_common.myAjaxComplete
        });
    }

    var setSomeDisable = function () {
        m.updateType.prop("disabled", true);
        m.startDate.prop("disabled", true);
        m.groupName.prop("disabled", true);
        //取消check事件
        canCheckStoreTree = false;
        //附件按钮
        $("#addByFile").prop("disabled",true);
        $("#updateByFile").prop("disabled",true);
        $("#deleteByFile").prop("disabled",true);
        $("#saveBtn").prop("disabled",true);
    }

    var setSomeEnable = function () {
        m.updateType.prop("disabled", false);
        m.startDate.prop("disabled", false);
        m.groupName.prop("disabled", false);
        //取消check事件
        canCheckStoreTree = true;
        //附件按钮
        $("#addByFile").prop("disabled",false);
        $("#updateByFile").prop("disabled",false);
        $("#deleteByFile").prop("disabled",false);
        $("#saveBtn").prop("disabled",false);
    }

    var showResponse = function(data,textStatus, xhr) {
        var resp = xhr.responseJSON;
        if (resp.result == false) {
            top.location = resp.s + "?errMsg=" + resp.errorMessage;
            return;
        }
        $('#StoreTree').treeview({
            data: data.list,         // 数据源
            showCheckbox: true,   //是否显示复选框
            highlightSelected: true,    //是否高亮选中
            levels : 1,
            preventUnselect: true,
            enableLinks : false,//必须在节点属性给出href属性
            color: "#010A0E",
            emptyIcon: "glyphicon glyphicon-ban-circle",
            onNodeChecked : function (event,node) {
                if(canCheckStoreTree){
                    var selectNodes = getChildNodeIdArr(node); //获取所有子节点
                    if (selectNodes) { //子节点不为空，则选中所有子节点
                        $('#StoreTree').treeview('checkNode', [selectNodes, { silent: true }]);
                    }
                    setParentNodeCheck(node);
                }else{
                    thisNode = $('#StoreTree').treeview("getNode", node.nodeId);
                    thisNode.state.checked = false;
                }
            },
            onNodeUnchecked : function(event, node) { //取消选中节点
                if(canCheckStoreTree){
                    var selectNodes = getChildNodeIdArr(node); //获取所有子节点
                    if (selectNodes) { //子节点不为空，则取消选中所有子节点
                        $('#StoreTree').treeview('uncheckNode', [selectNodes, {silent: true}]);
                    }
                    setParentNodeCheck(node);
                }else{
                    thisNode = $('#StoreTree').treeview("getNode", node.nodeId);
                    thisNode.state.checked = true;
                }
            },
            onNodeExpanded : function(event, data) {

            },
            onNodeSelected: function (event, data) {
                var select_node = $('#StoreTree').treeview('getSelected');
                if(select_node[0].state.expanded){
                    $('#StoreTree').treeview('collapseNode',select_node);
                    select_node[0].state.selected=false;
                }
                else{
                    $('#StoreTree').treeview('expandNode',select_node);
                    select_node[0].state.selected=false;
                }
            }
        });
    }

    //选中/取消父节点时选中/取消所有子节点
    function getChildNodeIdArr(node) {
        var ts = [];
        if (node.nodes) {
            for (x in node.nodes) {
                ts.push(node.nodes[x].nodeId);
                if (node.nodes[x].nodes) {
                    var getNodeDieDai = getChildNodeIdArr(node.nodes[x]);
                    for (j in getNodeDieDai) {
                        ts.push(getNodeDieDai[j]);
                    }
                }
            }
        } else {
            ts.push(node.nodeId);
        }
        return ts;
    }

    //展开父节点
    function setParentNodeExpand(node){
        var parentNode = $("#StoreTree").treeview("getNode", node.parentId);
        if(parentNode.nodeId != null){
            $('#StoreTree').treeview('expandNode',parentNode);
            setParentNodeExpand(parentNode);
        }
    }

    //选中所有子节点时选中父节点
    function setParentNodeCheck(node) {
        var parentNode = $("#StoreTree").treeview("getNode", node.parentId);
        if (parentNode.nodes) {
            var checkedNodes = $("#StoreTree").treeview('getChecked');
            var checkedNodesByNode = checkedNodes.filter(function(data,index,array){
                if(parentNode.nodeId == data.parentId){
                    return true;
                }else{
                    return false;
                }
            });
            if (checkedNodesByNode.length === parentNode.nodes.length) {
                parentNode.state.checked = true;
                setParentNodeCheck(parentNode);
            }else{
                parentNode.state.checked = false;
                setParentNodeCheck(parentNode);
            }
        }
    }


    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:false,
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

    //附件
    var attachments_event = function () {
        //附件一览表格
        attachmentsTable = $("#attachmentsTable").zgGrid({
            title: "Attachments",
            param: attachmentsParamGrid,
            colNames: ["File Name", "Download", "PreView"],
            colModel: [
                {name: "fileName", type: "text", text: "center", width: "130", ishide: false, css: ""},
                {
                    name: "filePath",
                    type: "text",
                    text: "center",
                    width: "100",
                    ishide: false,
                    css: "",
                    getCustomValue: function (tdObj, value) {
                        var obj = value.split(",");
                        var html = '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="' + obj[0] + '" filePath="' + obj[1] + '"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>';
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
            width: "max",//宽度自动
            isPage: false,//是否需要分页
            isCheckbox: false,
            freezeHeader:false,
            loadEachBeforeEvent: function (trObj) {
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTempFile = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTempFile = trObj;
            },
            buttonGroup: [
                {butType: "add", butId: "addByFile", butText: "Add", butSize: ""},//新增
                {butType: "update", butId: "updateByFile", butText: "Modify", butSize: ""},//修改
                {butType: "delete", butId: "deleteByFile", butText: "Delete", butSize: ""},//删除
            ],
        });
        //附件表格新增
        var appendTrByFile = function (fileName, filePath) {
            var rowindex = 0;
            var trId = $("#attachmentsTable>.zgGrid-tbody tr:last").attr("id");
            if (trId != null && trId != '') {
                rowindex = parseInt(trId.substring(trId.indexOf("_") + 1, trId.indexOf("_") + 2)) + 1;
            }
            var tr = '<tr id="attachmentsTable_' + rowindex + '_tr" class="">' +
                '<td tag="fileName" width="130" title="' + fileName + '" align="center" id="attachmentsTable_' + rowindex + '_tr_fileName" tdindex="attachmentsTable_fileName">' + fileName + '</td>' +
                '<td tag="filePath" width="100" title="Download" align="center" id="attachmentsTable_' + rowindex + '_tr_filePath" tdindex="attachmentsTable_filePath">' +
                '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="' + fileName + '" filePath="' + filePath + '"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>' +
                '</td>' +
                '<td tag="filePath1" width="100" title="Preview" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath1" tdindex="attachmentsTable_filePath1">'+
                '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>'+
                '</td>' +
                '</tr>';
            $("#attachmentsTable>.zgGrid-tbody").append(tr);
        }

        //附件一览显示
        $("#attachments").on("click", function () {
            $('#attachments_dialog').modal("show");
        })
        //附件一览关闭
        $("#cancelByAttachments").on("click", function () {
            //修改文件信息
            var fileDetail = [];
            $("#attachmentsTable>.zgGrid-tbody tr").each(function () {
                fileDetail.push($(this).find('td[tag=fileName]').text());
            });
            var checkList = refrain(fileDetail);
            if(checkList.length > 0){
                //提示文件名称重复
                _common.prompt("The file name "+checkList[0]+" can not repeat!",5,"error");
                return;
            }
            _common.myConfirm("Are you sure you want to cancel?", function (result) {
                if (result == "true") {
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
        //上传文件
        $("#fileData").on("change", function () {
            let resultFile = $(this)[0].files;
            $("#file_name").val(resultFile[0].name.split(".")[0]);
        });
        //修改文件名称
        $("#updateByFile").on("click", function () {
            if (selectTrTempFile == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }
            $('#fileUpload_dialog').modal("show");
            $("#operateFlgByFile").val("2");
            $("#fileData").parent().parent().hide();
            var cols = attachmentsTable.getSelectColValue(selectTrTempFile, "fileName");
            var fileName = cols["fileName"];
            $("#file_name").val(fileName);
        });
        //删除文件
        $("#deleteByFile").on("click", function () {
            if (selectTrTempFile == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return;
            }
            _common.myConfirm("Please confirm whether you want to delete the selected data？", function (result) {
                if (result == "true") {
                    $(selectTrTempFile[0]).remove();
                    selectTrTempFile = null;
                }
            });
        });
        //提交按钮点击事件 文件上传
        $("#affirmByFile").on("click", function () {
            if ($("#file_name").val() == null || $("#file_name").val() == '') {
                $("#file_name").css("border-color", "red");
                _common.prompt("File name cannot be empty!", 5, "error");
                $("#file_name").focus();
                return;
            } else {
                $("#file_name").css("border-color", "#CCCCCC");
            }
            _common.myConfirm("Are you sure you want to upload？", function (result) {
                if (result == "true") {
                    var flg = $("#operateFlgByFile").val();
                    if (flg == "1") {
                        if ($('#fileData')[0].files[0] == undefined || $('#fileData')[0].files[0] == null) {
                            $("#fileData").css("border-color", "red");
                            _common.prompt("File cannot be empty!", 5, "error");
                            $("#fileData").focus();
                            return;
                        } else {
                            $("#fileData").css("border-color", "#CCCCCC");
                        }
                        var formData = new FormData();
                        formData.append("fileData", $('#fileData')[0].files[0]);
                        formData.append("toKen", m.toKen.val());
                        $.myAjaxs({
                            url: _common.config.surl + "/file/upload",
                            async: false,
                            cache: false,
                            type: "post",
                            data: formData,
                            dataType: "json",
                            processData: false,
                            contentType: false,
                            success: function (data, textStatus, xhr) {
                                var resp = xhr.responseJSON;
                                if (resp.result == false) {
                                    top.location = resp.s + "?errMsg=" + resp.errorMessage;
                                    return;
                                }
                                if (data.success == true) {
                                    var fileName = $("#file_name").val();
                                    var filePath = data.data.filePath;
                                    appendTrByFile(fileName, filePath);
                                    _common.prompt("Operation Succeeded!", 2, "success");
                                    $('#fileUpload_dialog').modal("hide");
                                } else {
                                    _common.prompt(data.message, 5, "error");
                                }
                                m.toKen.val(data.toKen);
                            },
                            complete: _common.myAjaxComplete
                        });
                    } else if (flg == "2") {
                        var fileName = $("#file_name").val();
                        $(selectTrTempFile[0]).find('td').eq(0).text(fileName);
                        _common.prompt("Operation Succeeded!", 2, "success");
                        $('#fileUpload_dialog').modal("hide");
                    }
                }
            });
        });

        $("#cancelByFile").on("click", function () {
            _common.myConfirm("Are you sure you want to cancel?", function (result) {
                if (result == "true") {
                    $("#file_name").css("border-color", "#CCCCCC");
                    $("#fileData").css("border-color", "#CCCCCC");
                    $('#fileUpload_dialog').modal("hide");
                }
            })
        })

        //下载
        $("#attachments_dialog").on("click", "a[class*='downLoad']", function () {
            var fileName = $(this).attr("fileName");
            var filePath = $(this).attr("filePath");
            window.open(_common.config.surl + "/file/download?fileName=" + fileName + "&filePath=" + filePath, "_self");
        });
        //预览
        $("#attachments_dialog").on("click","a[class*='preview']",function(){
            var fileName = $(this).attr("fileName");
            var filePath = $(this).attr("filePath");
            var url = _common.config.surl+"/file/preview?fileName="+fileName+"&filePath="+filePath;
            window.open(url,"toPreview");
        });
    };

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

    // 格式化数字类型的日期
    function fmtDate(date){
        var res = "";
        res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        return res;
    }


    function subfmtDate(date){
        var res = "";
        if(date!=null&&date!=""){
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        }
        return res;
    }

    var dateFmtYMDHMS = function(tdObj, value){
        return $(tdObj).text(fmtYMDHMSDate(value));
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

    //dd/MM/yyyy 格式转化为日期
    function ddMMyyyyfmtDate(ddMMyyyyStr){
        var yyyyMMddStr = ddMMyyyyStr.substring(6,10)+"-"+ddMMyyyyStr.substring(3,5) +"-"+ ddMMyyyyStr.substring(0,2);
        return new Date(yyyyMMddStr);
    }

    // 获取唯一id
    function guid() {
        return Number(Math.random().toString().substr(3,6) + Date.now()).toString(36);
    }

    //获取数组重复的元素
    function refrain(arr) {
        var tmp = [];
        if(Array.isArray(arr)) {
            arr.concat().sort().sort(function(a,b) {
                if(a==b && tmp.indexOf(a) === -1) tmp.push(a);
            });
        }
        return tmp;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('remoteUpdateEdit');
_start.load(function (_common) {
    _index.init(_common);
});
