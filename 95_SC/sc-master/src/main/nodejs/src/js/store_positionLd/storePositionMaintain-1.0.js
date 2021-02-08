require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("bootstrapValidator");
require("bootstrapValidator.css");
var _datetimepicker = require("datetimepicker");
var _myAjax = require("myAjax");
define("myAjax");
define('storePositionMaintain', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        tempTrObjValue = {},//临时行数据存储
        a_store=null,
        common = null;

    var m = {
        toKen : null,
        a_store:null,
        empNumId:null,//搜索用 用户编号
        empName:null,//搜索用 用户名称
        jobTypeCd: null,//搜索用 职位
        emp_num_id:null,//用户编号
        emp_name:null,//用户名称
        emp_password:null,//用户密码
        emp_country: null,
        emp_education: null,
        telephone_no: null,
        mobile_phone_no: null,
        emp_address: null,
        emp_date: null,
        emp_birthdate: null,
        emp_leave_date: null,
        emp_email: null,
        emp_comment: null,
        emp_postal_code: null,
        operateFlg:null,//表单操作状态
        resetForm:null,
        affirm: null,
        cancel: null,
        cancelByAdd: null,
        add: null,
        saveNewPassWord: null,
        saveNewPassWordReset: null,

        storeCd:null,//店铺
        job_type_cd:null,//职位
        effective_status:null,//用户状态
        emp_gender:null,//性别
        alert_div: null,//页面提示框
        search: null,//检索按钮
        reset: null,//清空按钮
        main_box: null,//检索按钮
        addSearch: null,
        searchJson: null,
        checkCount: null,
        jobCatagoryName: null,
        empPassword: null,
        effectiveStatus: null,
    }
    // 创建js对象
    var createJqueryObj = function () {
        for (x in m) {
            m[x] = $("#" + x);
        }
    }

    function init(_common) {
        createJqueryObj();
        systemPath = _common.config.surl;
        url_left = _common.config.surl + "/employesInformation";
        initTable1();
        //下拉初始化
        initSelectOptions("User Status","effectiveStatus", "00220");
        initSelectOptions("User Status","effective_status", "00220");
        initstorePosition("jobTypeCd");
        initSelectOptions("Gender","emp_gender","00270");
        initstorePosition("job_type_cd");
        table_event();
        but_event();

        validator();
        passwordValidator();
        //权限验证
        isButPermission();
        initAutoMatic();
        // m.search.click();
    }
    var initAutoMatic = function (){
        // $("#empDepart").myAutomatic({
        //      url: systemPath + "/ma1000/getStoreAll",
        //      ePageSize: 10,
        //      startCount: 0
        // });
        $("#storeCd").myAutomatic({
            url: systemPath + "/ma1000/getStoreAll",
            ePageSize: 10,
            startCount: 3
        });
        a_store = $("#a_store").myAutomatic({  //加上名字可以存入自动填充
            url: systemPath+"/roleStore/getStore",
            ePageSize: 10,
            startCount: 0
        });
    }
    var passwordValidator = function () {
        $('#passwordForm').bootstrapValidator({
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
                        stringLength: {
                            min: 6,
                            max: 8,
                            message: 'The Old Password must be more than 6 and less than 8 characters long'
                        },
                        threshold: 6,//有6字符以上才发送ajax请求
                        remote: {
                            type:"get",
                            message: 'The passwords do not match!',
                            url: url_left + "/confirmPassword",
                            data:{
                                userId:function() {return tempTrObjValue.empNumId }
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
        $('#userform').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                // valid: 'glyphicon glyphicon-ok',
                // invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                empNumId: {
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
                            message: 'The User ID can only consist of alphabetical, number, dot and underscore'
                        },
                        threshold: 1,//有1字符以上才发送ajax请求
                        remote: {
                            type:"get",
                            message: 'User ID already exists!',
                            url: url_left + "/getUserId",
                            data : '',//这里默认会传递该验证字段的值到后端,
                            delay: 1000,//设置1秒发送一次ajax
                        }
                    }
                },
                empName: {
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
                password: {
                    message: 'The Password is not valid',
                    validators: {
                        // notEmpty: {
                        //     message: 'The Password is required and can\'t be empty'
                        // },
                        stringLength: {
                            min: 6,
                            max: 8,
                            message: 'The User Name must be more than 6 and less than 8 characters long'
                        }
                    }
                },
                position: {
                    message: 'The Position is not valid',
                    validators: {
                        notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        }
                    }
                },
                userStatus: {
                    message: 'The User Status is not valid',
                    validators: {
                        notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        }
                    }
                },
                gender: {
                    message: 'The Gender is not valid',
                    validators: {
                        notEmpty: {
                            message: 'Please enter all required fields(marked with red color)!'
                        }
                    }
                },
                country: {
                    message: 'The Country is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 20,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                education: {
                    message: 'The Education is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 20,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                telNo: {
                    message: 'The Telephone No. is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 20,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                mobileNo: {
                    message: 'The Mobile Phone No. is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 20,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                address: {
                    message: 'The Address is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 200,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                postalCode: {
                    message: 'The Postal Code is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 6,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
                email: {
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
                },
                comment: {
                    message: 'The Comment is not valid',
                    validators: {
                        stringLength: {
                            min: 0,
                            max: 255,
                            message: 'Entered data exceeded the maximum length!'
                        }
                    }
                },
            }
        })
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
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+"Failed to load data!",5,"error");
            }
        });
    }
    // 下拉职位
    function initstorePosition(selectId) {
        // 共通请求
        $.myAjaxs({
            url: "ma0060ReferMa4200/getJobcatagoryName",
            async:true,
            cache:false,
            type :"get",
            data :"",
            dataType:"json",
            success:function(result){
                var selectObj = $("#" + selectId);
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].jobTypeCd;
                    var optionText = result[i].jobCatagoryName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+"Failed to load data!",5,"error");
            },
            complete: _common.myAjaxComplete
        });
    }

    var table_event = function () {
        $("#add").on("click", function () {
            m.operateFlg.val("add");
            //显示重置保存按钮
            $("#resetForm").show();
            $("#affirm").show();
            $("#storeCdRefresh").show();
            $("#storeCdClear").show();
            //重新发起校验
            $("#userform").data('bootstrapValidator').destroy();
            $('#userform').data('bootstrapValidator', null);
            validator();
            clear_input();
            $("fieldset").prop("disabled",false);
            m.emp_num_id.prop("disabled",false);
            $("#password").show();
            $('#user_dialog').modal("show");
        });
        $("#Modify").on("click", function (e) {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return false;
            }
            //显示重置保存按钮
            $("#resetForm").show();
            $("#affirm").show();
            $("#storeCdRefresh").show();
            $("#storeCdClear").show();
            //重新发起校验
            $("#userform").data('bootstrapValidator').destroy();
            $('#userform').data('bootstrapValidator', null);
            validator();
            m.operateFlg.val("edit");
            //表单赋值
            $("fieldset").prop("disabled",false);
            m.emp_num_id.prop("disabled",true);
            $("#password").hide();
            userFormSet();
            $('#user_dialog').modal("show");
        });
        $("#View").on("click", function (e) {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return false;
            }
            //隐藏重置保存按钮
            $("#resetForm").hide();
            $("#affirm").hide();
            $("#storeCdRefresh").hide();
            $("#storeCdClear").hide();

            $("#userform").data('bootstrapValidator').resetForm();
            $("fieldset").prop("disabled",true);
            $("#password").hide();
            userFormSet();
            $('#user_dialog').modal("show");
        })
        m.resetForm.on("click", function () {
            _common.myConfirm("Are you sure you want to reset?",function(result){
                if (result=="true"){
                   clear_input();
                }
            })
        });
        m.saveNewPassWordReset.on("click",function () {
            _common.myConfirm("Are you sure you want to reset?",function(result){
                if (result=="true"){
                    $("#passwordForm").data('bootstrapValidator').resetForm(true);
                }
            })
        })

        m.cancel.on("click",function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if (result=="true"){
                    $('#user_dialog').modal("hide");
                }
            })
        })

        m.cancelByAdd.on("click",function () {
            _common.myConfirm("Are you sure you want to cancel?",function(result){
                if (result=="true"){
                    $('#updatePassword').modal("hide");
                }
            })
        })

        m.affirm.on("click", function (e) {
            var bootstrapValidator = $("#userform").data('bootstrapValidator');
            bootstrapValidator.validate();
            if(bootstrapValidator.isValid()){
                _common.myConfirm("Are you sure you want to save?", function (result) {
                    if (result !== "true") {
                        return false;
                    }
                    let empId = 0;
                    if(m.operateFlg.val()=='edit'){
                        empId = tempTrObjValue.empId;
                    }
                    let userInfo = {
                        'empId':empId,
                        'empNumId': m.emp_num_id.val().trim(),
                        'empName': m.emp_name.val().trim(),
                        'empPassword': m.emp_password.val().trim(),
                        'storeCd': $("#storeCd").attr("k"),
                        'jobTypeCd': m.job_type_cd.val(),
                        'effectiveStatus': m.effective_status.val(),
                        'empGender': m.emp_gender.val(),
                        'empCountry': m.emp_country.val().trim(),
                        'empEducation': m.emp_education.val().trim(),
                        'telephoneNo': m.telephone_no.val().trim(),
                        'mobilePhoneNo': m.mobile_phone_no.val().trim(),
                        'empAddress': m.emp_address.val().trim(),
                        'empBirthdate': subfmtDate(m.emp_birthdate.val()),
                        'empDate': subfmtDate(m.emp_date.val()),
                        'empLeaveDate': subfmtDate(m.emp_leave_date.val()),
                        'empPostalCode': m.emp_postal_code.val().trim(),
                        'empEmail': m.emp_email.val().trim(),
                        'empComment': m.emp_comment.val().trim(),
                        'operateFlg':m.operateFlg.val(),
                        'toKen': m.toKen.val().trim()
                    }
                    $.myAjaxs({
                        url: url_left + "/save",
                        async: true,
                        cache: false,
                        type: "post",
                        data: userInfo,
                        dataType: "json",
                        success: function (result) {
                            if(result.success){
                                _common.prompt("Save Successfully!", 3, "info");
                                $('#user_dialog').modal("hide");
                                m.search.click()
                            }else{
                                _common.prompt(result.message,5,"error");
                            }
                            m.toKen.val(result.toKen);
                        },
                        complete: _common.myAjaxComplete
                    })
                })
            }
            else return;
        })

        $("#updateEmpPassword").on("click", function (e) {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return false;
            }
            //重新发起校验
            $("#passwordForm").data('bootstrapValidator').destroy();
            $('#passwordForm').data('bootstrapValidator', null);
            passwordValidator();
            $("#passwordForm").data('bootstrapValidator').resetForm(true);
            $("#userId").val(tempTrObjValue.empNumId);
            $("#userName").val(tempTrObjValue.empName);
            $("#updatePassword").modal("show");
        })

        m.saveNewPassWord.on("click", function () {
            var bootstrapValidator = $("#passwordForm").data('bootstrapValidator');
            bootstrapValidator.validate();
            if(bootstrapValidator.isValid()){
                let passwordInfo = {
                    'empNumId': tempTrObjValue.empNumId,
                    'empOldPassword': $("#emp_old_password").val().trim(),
                    'empNewPassword': $("#emp_new_password").val().trim(),
                    'toKen': m.toKen.val()
                };
                _common.myConfirm("Are you sure you want to save?", function (result) {
                    if (result !== "true") {
                        return false;
                    }
                    $.myAjaxs({
                        url: url_left + "/updatePassword",
                        async: true,
                        cache: false,
                        type: "post",
                        data: passwordInfo,
                        dataType: "json",
                        success: function (result) {
                            if(result.success){
                                _common.prompt("Modify Successfully!", 3, "info");
                                $("#updatePassword").modal("hide");
                                m.search.click()
                            }else{
                                _common.prompt(result.message,5,"error");
                            }
                            m.toKen.val(result.toKen);
                        },
                        error: function (result) {
                            _common.prompt(result.msg, 5, "error");
                        }
                    })
                })
            }
        })
        $("#delete").on("click",function (e) {
            if (selectTrTemp == null) {
                _common.prompt("Please select at least one row of data!", 5, "info");
                return false;
            }
            let userInfo = {
                'empId':tempTrObjValue.empId,
                'empNumId':tempTrObjValue.empNumId,
                'toKen': m.toKen.val()
            }
           _common.myConfirm("Are you sure you want to delete?", function (result) {
                if (result !== "true") {
                    return false;
                }
                $.myAjaxs({
                    url: url_left + "/delete",
                    async: true,
                    cache: false,
                    type: "post",
                    data: userInfo,
                    dataType: "json",
                    success: function (result) {
                        if(result.success){
                            _common.prompt("Delete Successfully!", 3, "info");
                            m.search.click()
                        }else{
                            _common.prompt(result.message,5,"error");
                        }
                        m.toKen.val(result.toKen);
                    },
                    complete: _common.myAjaxComplete
                })
           })
        })
    }

    var userFormSet = function () {
        clear_input();
        $("#emp_num_id").val(tempTrObjValue.empNumId);
        $("#emp_name").val(tempTrObjValue.empName);
        $("#emp_password").val(tempTrObjValue.empPassword);
        $("#storeCd").val(tempTrObjValue.storeName).attr("k",tempTrObjValue.storeCd).attr("v",tempTrObjValue.storeName);
        $("#job_type_cd").val(tempTrObjValue.jobTypeCd);
        $("#effective_status").val(tempTrObjValue.effectiveStatus);
        $("#emp_gender").val(tempTrObjValue.empGender);
        $("#emp_country").val(tempTrObjValue.empCountry);
        $("#emp_education").val(tempTrObjValue.empEducation);
        $("#telephone_no").val(tempTrObjValue.telephoneNo);
        $("#mobile_phone_no").val(tempTrObjValue.mobilePhoneNo);
        $("#emp_address").val(tempTrObjValue.empAddress);
        $("#emp_birthdate").val(tempTrObjValue.empBirthdate);
        $("#emp_date").val(tempTrObjValue.empDate);
        $("#emp_leave_date").val(tempTrObjValue.empLeaveDate);
        $("#emp_postal_code").val(tempTrObjValue.empPostalCode);
        $("#emp_email").val(tempTrObjValue.empEmail);
        $("#emp_comment").val(tempTrObjValue.empComment);
    }
    var clear_input = function (){
        $("#userform").data('bootstrapValidator').resetForm(true);
        $("#storeCd").val("").attr("k","").attr("v","");
        m.emp_birthdate.val("");
        m.emp_date.val("");
        m.emp_leave_date.val("");
        if(m.operateFlg.val()=="edit"){
            m.emp_num_id.val(tempTrObjValue.empNumId)
        }
    }
    var but_event = function () {
          m.emp_birthdate.datetimepicker({
              language:'en',
              format: 'dd/mm/yyyy',
              maxView: 4,
              startView: 2,
              minView: 2,
              autoclose: true,
              todayHighlight: true,
              todayBtn: true
          })
        m.emp_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        })
        m.emp_leave_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView: 4,
            startView: 2,
            minView: 2,
            autoclose: true,
            todayHighlight: true,
            todayBtn: true
        })
        //点击reset清空数据
        m.reset.on("click", function () {
            $.myAutomatic.cleanSelectObj(a_store);
            $("#empNumId").val("");
            $("#empName").val("");
            $("#jobTypeCd").val("");
            $("#effectiveStatus").val("");
            selectTrTemp = null;
            _common.clearTable();
        });
        m.search.on("click", function () {
            paramGrid = "empNumId="+m.empNumId.val().trim()+
                "&empName="+m.empName.val().trim()+
                "&jobTypeCd="+m.jobTypeCd.val().trim()+
                "&storeCd="+$("#a_store").attr("k")+
                "&effectiveStatus="+m.effectiveStatus.val().trim();
            tableGrid.setting("url", url_left + "/search");
            tableGrid.setting("param", paramGrid);
            tableGrid.setting("page", 1);
            tableGrid.loadData(null);
        });
    }


    var initTable1 = function () {
        tableGrid = $("#zgGridTtable").zgGrid({
            title: "Query Result",
            param: paramGrid,
            colNames: ["ID","User ID","User Name","Store No.","Store Name","Position Cd","Position","Gender Cd","Gender",
                "User Status Cd","User Status","Telephone No.","Mobile Phone No.","Country","Education","Address",
                "Birth Date","Start Date","Leave Date", "Postal Code","Email","Comment","md"],
            colModel: [
                {name: "empId", type: "text", text: "center", width: "160", ishide: true, css: ""},
                {name:"empNumId",type:"text", text: "right", width: "160", ishide: false, css: ""},
                {name: "empName", type: "text", text: "left", width: "160", ishide: false, css: ""},
                {name:"storeCd",type: "text", text: "right", width: "160", ishide:false, css: ""},
                {name:"storeName",type: "text", text: "left", width: "160", ishide:false, css: ""},
                {name:"jobTypeCd",type: "text", text: "right", width: "160", ishide: true, css: ""},
                {name:"jobCatagoryName",type: "text", text: "left", width: "160", ishide:false, css: ""},
                {name:"empGender",type: "text", text: "right", width: "160", ishide:true, css: ""},
                {name:"empGenderName",type: "text", text: "left", width: "160", ishide:false, css: ""},
                {name:"effectiveStatus",type: "text", text: "right", width: "160", ishide: true, css: ""},
                {name:"effectiveStatusName",type: "text", text: "left", width: "160", ishide: false, css: ""},
                {name: "telephoneNo", type: "text", text: "left", width: "110", ishide: false, css: ""},
                {name: "mobilePhoneNo", type: "text", text: "left", width: "160", ishide: false, css: ""},
                {name: "empCountry", type: "text", text: "left", width: "110", ishide: false, css: ""},
                {name: "empEducation", type: "text", text: "left", width: "110", ishide: false, css: ""},
                {name: "empAddress", type: "text", text: "left", width: "110", ishide: false, css: ""},
                {name: "empBirthdate", type: "text", text: "center", width: "100", ishide: false, css: "",getCustomValue:dateFmt},
                {name: "empDate", type: "text", text: "center", width: "100", ishide: false, css: "",getCustomValue:dateFmt},
                {name: "empLeaveDate", type: "text", text: "center", width: "150", ishide: false, css: "",getCustomValue:dateFmt},
                {name: "empPostalCode", type: "text", text: "right", width: "120", ishide: false, css: ""},
                {name: "empEmail", type: "text", text: "left", width: "130", ishide: false, css: ""},
                {name: "empComment", type: "text", text: "left", width: "130", ishide:false, css: ""},
                {name: "md", type: "text", text: "left", width: "130", ishide:true, css: ""},
            ],//列内容
            width: "max",//宽度自动
            page: 1,//当前页
            rowPerPage: 10,//每页数据量
            isPage: true,//是否需要分页
            isCheckbox: false,
            loadEachBeforeEvent: function (trObj) {
                tempTrObjValue = {};
                return trObj;
            },
            ajaxSuccess: function (resData) {
                return resData;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                return self;
            },
            eachTrClick: function (trObj, tdObj) {//正常左侧点击
                selectTrTemp = trObj;
                var cols = tableGrid.getSelectColValue(selectTrTemp,
                    "empNumId,empId,empName,storeCd,storeName,jobTypeCd,jobCatagoryName," +
                    "empGender,empGenderName,effectiveStatus,effectiveStatusName,telephoneNo,mobilePhoneNo," +
                    "empCountry,empEducation,empAddress,empBirthdate,empDate,empLeaveDate,empPostalCode," +
                    "empEmail,empComment,md");
                tempTrObjValue.empNumId = cols["empNumId"];
                tempTrObjValue.empId = cols["empId"];
                tempTrObjValue.empName = cols["empName"];
                tempTrObjValue.storeCd = cols["storeCd"];
                tempTrObjValue.storeName = cols["storeName"];
                tempTrObjValue.jobTypeCd = cols["jobTypeCd"];
                tempTrObjValue.empGender = cols["empGender"];
                tempTrObjValue.effectiveStatus = cols["effectiveStatus"];
                tempTrObjValue.telephoneNo = cols["telephoneNo"];
                tempTrObjValue.mobilePhoneNo = cols["mobilePhoneNo"];
                tempTrObjValue.empCountry = cols["empCountry"];
                tempTrObjValue.empEducation = cols["empEducation"];
                tempTrObjValue.empAddress = cols["empAddress"];
                tempTrObjValue.empBirthdate = cols["empBirthdate"];
                tempTrObjValue.empDate = cols["empDate"];
                tempTrObjValue.empLeaveDate = cols["empLeaveDate"];
                tempTrObjValue.empPostalCode = cols["empPostalCode"];
                tempTrObjValue.empEmail = cols["empEmail"];
                tempTrObjValue.empComment = cols["empComment"];
                if(cols["md"]=="1"){
                    $("#updateEmpPassword").prop("disabled",true);
                    $("#Modify").prop("disabled",true);
                    $("#delete").prop("disabled",true);
                }else{
                    $("#updateEmpPassword").prop("disabled",false);
                    $("#Modify").prop("disabled",false);
                    $("#delete").prop("disabled",false);
                }
            },
            buttonGroup: [
                {
                    butType: "add",
                    butText: "Add",
                    butId:  "add",
                    butSize: ""
                },
                {
                    butType: "update",
                    butText: "Reset Password",
                    butId:  "updateEmpPassword",
                    butSize: ""
                },
                {
                    butType: "update",
                    butText: "Modify",
                    butId:  "Modify",
                    butSize: ""
                },
                {
                    butType: "view",
                    butText: "View",
                    butId:  "View",
                    butSize: ""
                },
                {
                    butType: "delete",
                    butText: "Delete",
                    butId:  "delete",
                    butSize: ""
                },
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
            $("#Modify").remove();
        }
        var resetBut = $("#resetBut").val();
        if (Number(resetBut) != 1) {
            $("#updateEmpPassword").remove();
        }
        var viewBut = $("#viewBut").val();
        if (Number(viewBut) != 1) {
            $("#View").remove();
        }
        var delBut = $("#delBut").val();
        if (Number(delBut) != 1) {
            $("#delete").remove();
        }
    }

    var checkNum = function(value){
        var reg = /^[0-9]*$/;
        return reg.test(value);
    }
    //number格式化
    var formatNum = function (tdObj, value) {
        return $(tdObj).text(fmtIntNum(value));
    }
    //日期字段格式化格式
    var dateFmt = function (tdObj, value) {
        return $(tdObj).text(fmtIntDate(value));
    }
    var openNewPage = function (url, param) {
        param = param || "";
        location.href = url + param;
    }

    //格式化数字带千分位
    function fmtIntNum(val) {
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        return (val + '').replace(reg, '$&,');
    }

    //格式化数字类型的日期
    function fmtIntDate(date) {
        var res = "";
        res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        return res;
    }

    function subfmtDate(date) {
        var res = "";
        if(date!=null&&date!='')
        res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    //格式化数字类型的日期
    function fmtStrToInt(strDate) {
        var res = "";
        res = strDate.replace(/-/g, "");
        return res;
    }

    self.init = init;
    return self;

});
var _start = require('start');
var _index = require('storePositionMaintain');
_start.load(function (_common) {
    _index.init(_common);
});
