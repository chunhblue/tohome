require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("bootstrapValidator");
require("bootstrapValidator.css");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('cashier', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        a_store = null,
        info_store = null,
        common = null;
    var m = {
        a_store: null,
        info_store: null,
        inital_password: null,
        toKen: null,
        use: null,
        reset: null,
        search: null,
        cashierId: null,//搜索 收银员id
        cashierName: null,//搜索 收银员名称
        storeCd: null,
        storeName: null,
        i_cashierEmail: null,
        operateFlg: null,//判断新增修改 1新增 2修改
        //新增
        cashierLevel: null,//权限等级
        effectiveSts: null,//状态
        duty: null,//职务
        i_cashierId: null,//录入 收银员id
        i_storeCd:null,
        i_cashierName: null,//录入 收银员名称
        cancelByAdd: null,// 录入 取消
        affirmByAdd: null,// 录入 确认
        searchJson:null,
        //设置密码
        pwd_cashierLevel: null,// 修改密码 权限等级
        pwd_effectiveSts: null,//修改密码 状态
        pwd_duty: null,//修改密码 职务
        pwd_cashierId: null,//修改密码 收银员id
        pwd_cashierName: null,//修改密码 收银员名称
        pwdStoreCd:null,
        oldPassword: null,//旧密码
        newPassword: null,//新密码
        repeatPassword: null,//确认新密码
        cancelByUpdatePwd: null,// 修改密码 取消
        affirmByUpdatePwd: null,// 修改密码 确认
    }

    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        url_left = _common.config.surl + "/cashier";
        systemPath = _common.config.surl;
        _common.initOrganization();
        //初始化下拉
        initAutoMatic();
        getSelectValue();
        //事件绑定
        but_event();
        validator();
        passwordValidator();
        //列表初始化
        initTable1();
        //表格内按钮事件
        table_event();
        //权限验证
        isButPermission();
    }

    // var initStoreCd = function () {
    //     $.myAjaxs({
    //         url: url_left + "/ma1000" + "/getStore",
    //         async: true,
    //         cache: false,
    //         type: "get",
    //         dataType: 'json',
    //         success: function (result) {
    //             var htmlStr = '<option value="">--Please Select--</option>';
    //             $.each(result, function (ix, node) {
    //                 htmlStr += '<option value="' + node.storeCd + '">' + node.storeCd + '</option>';
    //             });
    //             m.storeCd.html(htmlStr);
    //         },
    //         complete: _common.myAjaxComplete
    //     });
    // }

    var passwordValidator = function () {
        // // var cashierId = m.pwd_cashierId.val();
        // // var cashierPassword= m.oldPassword.val();
        // alert(cashierId)
        $('#passwordform').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                // valid: 'glyphicon glyphicon-ok',
                // invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                oldPassword: {
                    message: 'The Old Password is not valid',
                    validators: {
                        notEmpty: {
                            message: 'The Old Password is required and can\'t be empty'
                        },
                        // stringLength: {
                        //     min: 6,
                        //     max: 8,
                        //     message: 'The Old Password must be more than 6 and less than 8 characters long'
                        // },
                        threshold: 1,//有6字符以上才发送ajax请求
                        remote: {
                            type:"get",
                            message: 'The passwords do not match!',
                            url: url_left + "/confirmPassword",
                            data:{
                                cashierId:function() {return tempTrObjValue.cashierId },
                                storeCd:function () {return tempTrObjValue.storeCd }
                            },
                            delay: 1000,//设置1秒发送一次ajax
                        },
                    }
                },
                newPassword: {
                    message: 'The New Password is not valid',
                    validators: {
                        notEmpty: {
                            message: 'The New Password is required and can\'t be empty'
                        },
                        stringLength: {
                            min: 6,
                            max: 8,
                            message: 'The New Password must be more than 6 and less than 8 characters long'
                        },
                        different: {
                            field: 'oldPassword',
                            message: 'New password need to be different from old password!'
                        }
                    }
                },
                newPassword2: {
                    message: 'The Repeat New Password is not valid',
                    validators: {
                        notEmpty: {
                            message: 'The Repeat New Password is required and can\'t be empty'
                        },
                        stringLength: {
                            min: 6,
                            max: 8,
                            message: 'New password must be more than 6 and less than 8 characters!'
                        },
                        identical: {//判断两次密码是否相同
                            field: 'newPassword',
                            message: 'The two passwords you typed do not match!'
                        },
                        different: {
                            field: 'oldPassword',
                            message: 'New password need to be different from old password!'
                        }
                    }
                }
            }
        })
    }
    var validator = function () {
        $('#cashierform').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                // valid: 'glyphicon glyphicon-ok',
                // invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                cashierId: {
                    message: 'The User ID is not valid',
                    validators: {
                        notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        },
                        stringLength: {
                            min: 1,
                            max: 10,
                            message: 'Entered data exceeded the maximum length!'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_\.]+$/,
                            message: 'The Cashier ID can only consist of alphabetical, number, dot and underscore'
                        },
                        threshold: 1,//有1字符以上才发送ajax请求
                        remote: {
                            type: "get",
                            message: 'Cashier ID already exists!',
                            url: url_left + "/getCashierId",
                            // data: '',//这里默认会传递该验证字段的值到后端,
                         data:{
                          cashierId:function() {return tempTrObjValue.cashierId },
                          storeCd:function () {return tempTrObjValue.storeCd }
        },
                            delay: 1000,//设置1秒发送一次ajax
                        }
                    }
                },
                cashierName: {
                    message: 'The username is not valid',
                    validators: {
                        notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        },
                        stringLength: {
                            min: 1,
                            max: 30,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                initalPassword: {
                    message: 'The Password is not valid',
                    validators: {
                        notEmpty: {
                            message: 'The Password is required and can\'t be empty'
                        },
                        stringLength: {
                            min: 6,
                            max: 8,
                            message: 'The Password is must be more than 6 and less than 8 characters long'
                        }


                    }
                },
                /*cashierEmail: {
                    message: 'The Email is not valid',
                    validators: {
                        emailAddress: {
                            message: 'The input is not a valid email address'
                        },
                        stringLength: {
                            min: 0,
                            max: 50,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },*/
                //12 18
                // duty: {
                //     message: 'The Postation is not empty',
                //     validators: {
                //         notEmpty: {
                //             message: 'Please enter all required fields(marked with red color)!'
                //         }
                //     }
                // },
                info_store: {
                    message: 'The Postation is not empty',
                    validators: {
                      notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        }
                    }
                },
                cashierLevel:{
                    message: 'The Gender is not valid',
                    validators: {
                        notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        }
                    }
                },
            }
        })
    }

    var table_event = function () {
        $("#add").on("click", function () {
            clear_input();
            m.i_cashierId.val("");
            m.i_cashierName.val("");
            m.i_cashierEmail.val("");
            m.inital_password.val("");
            m.cashierLevel.val("");
            m.cashierLevel.val("");
            $("#cashierform").data('bootstrapValidator').destroy();
            $("#cashierform").data('bootstrapValidator', null);
            validator();
            m.operateFlg.val("1");
            m.info_store.attr("disabled",false);
            $('#info_store_clear').show();
            $('#info_store_refresh').show();
            m.i_cashierId.attr("disabled",false);
            m.i_cashierName.attr("disabled",false);
            $('#cashier_dialog').modal("show");
        });

        $("#updateAuth").on("click", function () {
            if(selectTrTemp == null){
                _common.prompt("Please select cashier!",5,"info"); // 请选择收银员
                return false;
            }
            m.i_cashierEmail.val("");
            m.inital_password.val("");
            m.cashierLevel.val("");
            m.info_store.attr("disabled",true);
            $("#cashierform").data('bootstrapValidator').destroy();
            $("#cashierform").data('bootstrapValidator', null);
            validator();
            let cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,storeName,effectiveSts,cashierId,cashierName,cashierEmail,duty,cashierLevelCd,effectiveSts");
            if(cols["effectiveSts"] == "90"){
                // 收银员 ** 为非正常状态, 不可修改
                _common.prompt("Selected " + cols["cashierName"] + "is not effective and cannot be modified!",5,"info");
                return;
            }
            m.operateFlg.val("2");
            $.myAutomatic.setValueTemp(info_store, cols["storeCd"], cols["storeCd"]+' '+cols["storeName"]);
            m.i_cashierId.val(cols["cashierId"]);
            m.i_storeCd.val(cols["storeCd"]);
            m.i_cashierName.val(cols["cashierName"]);
            m.i_cashierEmail.val(cols["cashierEmail"]);

            m.cashierLevel.val(cols["cashierLevelCd"]);
            m.effectiveSts.val(cols["effectiveSts"]);

            $('#info_store_clear').show();
            $('#info_store_refresh').show();
            m.i_cashierId.attr("disabled",true);
            $('#cashier_dialog').modal("show");
        });

        $("#deleteBtn").on("click",function(){
            if(selectTrTemp == null){
                _common.prompt("Please select cashier!",5,"info"); // 请选择收银员
                return;
            }
            _common.myConfirm("Are you sure you want to delete?",function(result){
                if(result=="true"){
                    let cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,cashierId");
                    $.myAjaxs({
                        url:url_left+"/delete",
                        async:true,
                        cache:false,
                        type :"post",
                        data :"cashierId="+cols["cashierId"]+"&storeCd="+cols["storeCd"],
                        dataType:"json",
                        success:function(re){
                            if(re.success){
                                m.search.click();
                                _common.prompt("Deleted successfully!",5,"success");
                            }else{
                                _common.prompt(re.msg,5,"info");
                            }
                        },
                        error : function(e){
                            _common.prompt("Deleted failed!",5,"error");
                        }
                    });
                }
            })
        });

        $("#del").on("click",function(){
            if(selectTrTemp == null){
                _common.prompt("Please select cashier!",5,"info"); // 请选择收银员
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"effectiveSts,cashierId,cashierName,storeCd");
            if(cols["effectiveSts"]=="90"){
                // _common.prompt("收银员 '" + cols["cashierName"] + "'已为注销状态，请确认！",5,"info");
                _common.prompt("Cashier '" + cols["cashierName"] + "'has logged off, please confirm!",5,"info");
                return;
            }
            // 请确认是否要注销选中的收银员
            _common.myConfirm("Please confirm whether you want to logout the selected cashier？",function(result){
                if(result=="true"){
                    updateCashierSts(cols["cashierId"],cols["storeCd"],"90")
                }
            });
        });

        $("#normal").on("click",function(){
            if(selectTrTemp == null){
                _common.prompt("Please select cashier!",5,"info"); // 请选择收银员
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"effectiveSts,cashierId,cashierName");
            if(cols["effectiveSts"]=="10"){
                // _common.prompt("收银员 '" + cols["cashierName"] + "'已为正常状态，请确认!",5,"info");
                _common.prompt("selected '" + cols["cashierName"] + "' is already effective!, please confirm!",5,"info");
                return;
            }
            // 请确认是否要恢复选中的收银员
            _common.myConfirm("Please confirm whether you want to restore the selected cashier？",function(result){
                if(result=="true"){
                    updateCashierSts(cols["cashierId"],"10")
                }
            });
        });

        //设置未初始化密码
        $("#initPass").on("click",function(){
            if(selectTrTemp == null){
                _common.prompt("Please select cashier!",5,"info"); // 请选择收银员
                return;
            }
            var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,cashierId,cashierName");
            var cashierId = cols["cashierId"];
            var cashierName = cols["cashierName"];
            var storeCd = cols["storeCd"];
            if(cashierId==null||cashierId==""){
                // 收银员id 为空
                _common.prompt("The cashier id is empty!",5,"info");
                return;
            }
            // _common.myConfirm("确认要将收银员'" + cashierName + "'的密码设置为初始密码吗？",function(result){
            _common.myConfirm("Are you sure to set cashier'" + cashierName + "'password as the initial password?",function(result){
                if(result=="true"){
                    $.myAjaxs({
                        url:url_left+"/initCashierPwd",
                        async:true,
                        cache:false,
                        type :"post",
                        data :{
                            cashierId:cashierId,
                            storeCd:storeCd

                        },
                        dataType:"json",
                        success:function(result){
                            if(result.success){
                                // _common.prompt("收银员'" + cashierName + "'已成功设置为初始密码！",5,"info");
                                _common.prompt("Cashier'" + cashierName + "'Has successfully set the initial password!",5,"info");
                            }else{
                                _common.prompt("Setup failed!",5,"error");
                            }
                        },
                        error : function(e){
                            _common.prompt("Setup failed!",5,"error"); // 设置失败
                        },
                        complete:_common.myAjaxComplete
                    });
                }
            });
        });

        //修改密码
        $("#updatePass").on("click", function () {
            if(selectTrTemp == null){
                _common.prompt("Please select cashier!",5,"info"); // 请选择收银员
                return;
            }
            $("#passwordform").data('bootstrapValidator').destroy();
            $("#passwordform").data('bootstrapValidator', null);
            passwordValidator();
            m.pwd_cashierId.val("");
            m.pwd_cashierName.val("");
            m.pwd_duty.val("");
            m.pwd_cashierLevel.val("");
            m.pwd_effectiveSts.val("");
            var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,effectiveSts,cashierId,cashierName,duty,cashierLevel,effectiveStsName");
            m.pwd_cashierId.val(cols["cashierId"]);
            m.pwdStoreCd.val(cols["storeCd"]);
            m.pwd_cashierName.val(cols["cashierName"]);
            m.pwd_duty.val(cols["duty"]);
            m.pwd_cashierLevel.val(cols["cashierLevel"]);
            m.pwd_effectiveSts.val(cols["effectiveStsName"]);
            $('#cashierPass_dialog').modal("show");
        });
        var setParamJson = function () {
            let cashierId= m.cashierId.val().trim();
            let cashierName=m.cashierName.val().trim();
            let storeCd=$("#aStore").attr("k");
            let ofc=$("#am").attr("k");

            let searchJsonStr={
                'regionCd':$("#aRegion").attr("k"),
                'cityCd':$("#aCity").attr("k"),
                'districtCd':$("#aDistrict").attr("k"),
                'cashierId':cashierId,
                'cashierName':cashierName,
                'storeCd':storeCd,
                'ofc':ofc,
            };
            m.searchJson.val(JSON.stringify(searchJsonStr));

        }
       $("#export").on("click",function () {
           setParamJson();
           paramGrid = "searchJson=" + m.searchJson.val();
           var url = url_left + "/export?" + paramGrid;
           window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
       })

    }

    var clear_input = function (){
        $("#cashierform").data('bootstrapValidator').resetForm(true);
        $("#info_store_clear").click();
        $("#cashierLevel").val("");
    }

    //画面按钮点击事件
    var but_event = function () {
        m.cancelByAdd.on('click',function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if(result!="true"){return false;}
                $('#cashier_dialog').modal('hide');
            })
        });

        m.cancelByUpdatePwd.on('click',function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                $("#oldPassword").css("border-color","#CCCCCC");
                $("#newPassword").css("border-color","#CCCCCC");
                $("#repeatPassword").css("border-color","#CCCCCC");
                if(result!="true"){return false;}
                $('#cashierPass_dialog').modal('hide');
            })
        });

        //检索按钮点击事件
        m.search.on("click", function () {
            paramGrid = "cashierId=" + m.cashierId.val() + "&cashierName=" + m.cashierName.val() + "&regionCd=" + $("#aRegion").attr("k")+"&cityCd=" + $("#aCity").attr("k")+
                "&districtCd=" + $("#aDistrict").attr("k")+"&storeCd=" + $("#aStore").attr("k")+"&ofc="+$("#am").attr("k");
            tableGrid.setting("url", url_left + "/getCashierList");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData(null);
        });

        m.reset.on("click",function(){
             $("#a_store_clear").click();
            $("#amRemove").click();
            $("#regionRemove").click();
             m.cashierId.val("");
             m.cashierName.val("");
            selectTrTemp = null;
            _common.clearTable();
        });
       
        //新增确认
        m.affirmByAdd.on("click", function () {
            var bootstrapValidator = $("#cashierform").data('bootstrapValidator');
            bootstrapValidator.validate();
            if (bootstrapValidator.isValid()) {
                _common.myConfirm("Are you sure you want to save?", function (result) {
                    if (result !== "true") {
                        return false;
                    }
                    let operateFlg = m.operateFlg.val();
                    if(operateFlg=="1"){
                        // 验证编号
                        $.myAjaxs({
                            url: url_left + "/getCashier1",
                            async: true,
                            cache: false,
                            type: "post",
                            data: "cashierId=" + m.i_cashierId.val()+"&storeCd="+m.info_store.attr("k"),
                            dataType: "json",
                            success: function (result) {
                                if (result.success) {
                                    _common.prompt("The cashier already exists!", 5, "error"); // 该收银员已存在
                                } else {
                                    // 提交保存
                                    let data = {
                                        cashierPassword:m.inital_password.val(),
                                        storeCd:$("#info_store").attr("k"),
                                        cashierId:m.i_cashierId.val(),
                                        cashierName:m.i_cashierName.val(),
                                        duty:m.duty.val(),
                                        cashierLevel:m.cashierLevel.val(),
                                        cashierEmail:m.i_cashierEmail.val(),
                                        effectiveSts:m.effectiveSts.val()
                                    }
                                    $.myAjaxs({
                                        url: url_left + "/add",
                                        async: true,
                                        cache: false,
                                        type: "post",
                                        data: data,
                                        dataType: "json",
                                        success: function (result) {
                                            if (result.success) {
                                                _common.prompt("Data saved successfully!", 5, "success"); // 添加收银员成功
                                                m.i_cashierId.val("");
                                                m.i_cashierName.val("");
                                                m.duty.val("");
                                                m.search.click();
                                                $("#cashier_dialog").modal("hide");
                                            } else {
                                                _common.prompt("Please fill in the detailed data!", 5, "error"); // 添加收银员失败
                                            }
                                        },
                                        error: function (e) {
                                            _common.prompt("Data saved failed!", 5, "error"); // 添加收银员失败
                                        }
                                    });
                                }
                            },
                            error: function (e) {
                                _common.prompt("request failed!", 5, "error"); // 请求失败
                            }
                        });
                    } else if (operateFlg=='2'){
                        $.myAjaxs({
                            url:url_left+"/update",
                            async:true,
                            cache:false,
                            type :"post",
                            data :{
                                cashierId:m.i_cashierId.val(),
                                storeCd:$("#info_store").attr("k"),
                                cashierName:m.i_cashierName.val(),
                                cashierEmail:m.i_cashierEmail.val(),
                                cashierLevel:m.cashierLevel.val()
                            },
                            dataType:"json",
                            success:function(result){
                                if (result.success) {
                                    _common.prompt("Data saved successfully!", 5, "success"); // 修改收银员权限成功
                                    m.search.click();
                                    $('#cashier_dialog').modal("hide");
                                } else {
                                    _common.prompt("Data saved failed!", 5, "error"); // 修改收银员权限失败
                                }
                            },
                            error : function(e){
                                _common.prompt("Data saved failed!",5,"error"); // 添加收银员失败
                            }
                        });
                    }
                });
            }
        })

        m.affirmByUpdatePwd.on("click",function() {
            var bootstrapValidator = $("#passwordform").data('bootstrapValidator');
            bootstrapValidator.validate();
            if(bootstrapValidator.isValid()){
                var cashierId = m.pwd_cashierId.val();
                var newPassword = m.newPassword.val();
                var storeCd=m.pwdStoreCd.val();

                // 请确认是否要修改密码
                _common.myConfirm("Please confirm whether you want to change the password？",function(result){
                    if(result=="true"){
                        $.myAjaxs({
                            url:url_left+"/updateCashierPwd",
                            async:false,
                            cache:false,
                            type :"post",
                            data :{
                                cashierId:cashierId,
                                storeCd:storeCd,
                                newPassword:newPassword,
                            },
                            dataType:"json",
                            success:function(result){
                                if(result.success){
                                    _common.prompt("Password changed succeeded!",5,"success"); // 修改密码成功
                                    $('#cashierPass_dialog').modal("hide");
                                }else{
                                    _common.prompt(result.message,5,"error");
                                }
                            },
                            error : function(e){
                                _common.prompt("request error!",5,"error");
                            }
                        });
                    }
                });
            }
        })
    }

    // 初始化自动下拉
    var initAutoMatic = function () {
        // 获取区域经理
        am=$("#am").myAutomatic({
            url: systemPath + "/ma1000/getAMByPM",
            ePageSize:10,
            startCount:0,
        })
        a_store = $("#a_store").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0
        });

        info_store = $("#info_store").myAutomatic({
            url: systemPath + "/ma1000/getStoreByPM",
            ePageSize: 10,
            startCount: 0
        });
    }

    // 请求加载下拉列表
    function getSelectValue() {
        // 加载select
        initSelectOptions("Cashier Privilege", "cashierLevel", "00215");
        initSelectOptions("Status", "effectiveSts", "00220");
        m.effectiveSts.val("10");
    }

    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url: systemPath + "/cm9010/getCode",
            async: true,
            cache: false,
            type: "post",
            data: "codeValue=" + code,
            dataType: "json",
            success: function (result) {
                var selectObj = $("#" + selectId);
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error: function (e) {
                _common.prompt(title + " Failed to load data!", 5, "error");
            }
        });
    }

    // 修改收银员状态
    function updateCashierSts(cashierId,storeCd, effectiveSts) {
        if (cashierId == null || cashierId == "") {
            _common.prompt("The cashier id is empty!", 5, "info"); // 收银员id为空
            return;
        }
        if (effectiveSts == null || effectiveSts == "") {
            _common.prompt("The effective status is empty!", 5, "info"); // 生效状态为空
            return;
        }
        var effectiveStsName = '';
        if (effectiveSts == "10") {
            effectiveStsName = "recover";
        } else if (effectiveSts == "90") {
            effectiveStsName = "logout";
        }
        $.myAjaxs({
            url: url_left + "/updateCashierSts",
            async: true,
            cache: false,
            type: "post",
            data: {
                cashierId: cashierId,
                storeCd:storeCd,
                effectiveSts: effectiveSts
            },
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    _common.prompt(effectiveStsName + " success!", 5, "info");
                    m.search.click();
                } else {
                    _common.prompt(result.message, 5, "error");
                }
            },
            error: function (e) {
                _common.prompt(effectiveStsName + " failed!", 5, "error");
            }
        });
    }

    //表格初始化-收银员列表样式
    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Cashier Details",
            param: paramGrid,
            colNames: ["Store No.","Store Name","Area Manager","effectiveSts", "Status", "Cashier ID", "Cashier Name", "Email",
                "Position", "Cashier Level Cd", "Cashier Privilege"],
            colModel: [
                {name: "storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
                {name: "storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name: "ofcName",type:"text",text:"left",width:"130",ishide:false,css:""},
                {name: "effectiveSts", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "effectiveStsName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "cashierId", type: "text", text: "right", width: "130", ishide: false, css: ""},
                {name: "cashierName", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "cashierEmail", type: "text", text: "left", width: "130", ishide: false, css: ""},
                // {name: "duty", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "duty", type: "text", text: "left", width: "130", ishide: true, css: ""},
                {name: "cashierLevelCd", type: "text", text: "right", width: "100", ishide: true, css: ""},
                {name: "cashierLevel", type: "text", text: "left", width: "150", ishide: false, css: ""}
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            sidx: "effective_sts,cashier_id",//排序字段
            sord: "asc",//升降序
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent: function (self) {
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
                let cols = tableGrid.getSelectColValue(selectTrTemp,"cashierId,storeCd");
                tempTrObjValue.cashierId = cols["cashierId"];
                tempTrObjValue.storeCd = cols["storeCd"];
            },
            buttonGroup: [
                {
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""//,
                },//新增
                {
                    butType: "update",
                    butId: "updateAuth",
                    butText: "Modify",
                    butSize: ""//,
                },//修改
                {
                    butType:"custom",
                    butHtml:"<button id='deleteBtn' type='button' class='btn btn-danger btn-sm'><span class='glyphicon glyphicon-trash'></span> Delete</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='del' type='button' class='btn btn-danger btn-sm'><span class='glyphicon glyphicon-off'></span> Eliminate</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='normal' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-ok-circle'></span> Reactivate</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='initPass' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-import'></span> Restore Initial Password</button>"
                },
                {
                    butType: "custom",
                    butHtml: "<button id='updatePass' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-pencil'></span> Reset Password</button>"
                },
                {
                  butType:"custom",
                  butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
                }
            ],
        });
    }

    // 按钮权限验证
    var isButPermission = function () {
        var addBut = $("#addBut").val();
        if (Number(addBut) != 1) {
            $("#add").remove();
        }
        var editBut = $("#editBut").val();
        if (Number(editBut) != 1) {
            $("#updateAuth").remove();
        }
        var eliminateBut = $("#eliminateBut").val();
        if (Number(eliminateBut) != 1) {
            $("#del").remove();
        }
        var reactivateBut = $("#reactivateBut").val();
        if (Number(reactivateBut) != 1) {
            $("#normal").remove();
        }
        var initPasswordBut = $("#initPasswordBut").val();
        if (Number(initPasswordBut) != 1) {
            $("#initPass").remove();
        }
        var resetPasswordBut = $("#resetPasswordBut").val();
        if (Number(resetPasswordBut) != 1) {
            $("#updatePass").remove();
        }
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('cashier');
_start.load(function (_common) {
    _index.init(_common);
});
