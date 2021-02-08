require("zgGrid.css");
require("myValidator.css");
require('bootstrap.css');
require('bootstrap-ie8.css');
require('bootstrap-datetimepicker.css');

var _zgGrid = require("zgGrid");
var _datetimepicker = require('datetimepicker');
define('rolelist', function () {
    var self = {},
        common = null,
        systemPath = '',
        paramGrid;
    const KEY = 'PRIVILEGE_MANAGEMENT';
    function init(_common) {
        common = _common;
        systemPath = common.config.surl;
        listPageInit();

        // butPermissions("");
        isButPermission();
    }
    // 初始化参数, back 的时候 回显 已选择的检索项
    var initParam = function () {
        let json = sessionStorage.getItem(KEY);
        if (!json) {
            return;
        }
        let obj = eval("("+json+")");
        sessionStorage.removeItem(KEY);
        // 不是通过 back 正常方式返回, 清除不加载数据
        if (obj.flg=='0') {
            return;
        }
        $('#name').val(obj.name);
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
        obj.flg='0';
        obj.page=grid.getting('page');
        sessionStorage.setItem(KEY,JSON.stringify(obj));
    }
    //管理页面初始化
    var listPageInit = function () {
        initParam(); // 初始化参数
        loadZgGrid();//初始化页面数据
        initListPageBut();
        initListPageButEvent();
    }
    //管理页面初始化按钮显示方式
    var initListPageBut = function () {
        //$("#add").attr("disabled","disabled");
        $("#del").attr("disabled", "disabled");
        $("#update").attr("disabled", "disabled");
        $("#view").attr("disabled", "disabled");
        trRoleId = "";
    }
    //管理页面初始化按钮事件挂接
    var initListPageButEvent = function () {
        //进入编辑页面
        $("#add").on("click", function (e) {
            openNewPage(systemPath + "/role/edit", "");
        });
        //进入编辑页面
        $("#update").on("click", function (e) {

            openNewPage(systemPath + "/role/edit", "?id=" + trRoleId);
        });
        //进入查看页面
        $("#view").on("click", function (e) {
            openNewPage(systemPath + "/role/view", "?id=" + trRoleId);
        });
        //执行检索
        $("#search").on("click", function (e) {
            setParamGrid();
            againLoadZgGrid();
        });
        //删除角色
        $("#del").on("click", function (e) {
            if (trRoleId == "") {
                _common.prompt("Please select the role information to modify!", 5, "error");/*请选择要修改的角色信息*/
                return;
            }
            _common.myConfirm("Please confirm whether you want to delete the selected role information？", function (rest) {/*请确认是否要删除已选中的角色信息*/
                if (rest == "true") {
                    deleteRoleById(trRoleId);
                }
            });
        });

        //--------end
    }

    var deleteRoleById = function (trRoleId) {
        $.myAjaxs({
            url: systemPath + "/role/deleteData",
            async: false,
            cache: false,
            type: "post",
            dataType: "json",
            data: "&roleId=" + trRoleId,
            success: showResponse,
            complete: _common.myAjaxComplete
        });
    }

    //提交回调函数
    function showResponse(data, textStatus, xhr) {
        $("#toKen").val(data.toKen);
        //_common.loading_close();
        if (data.success == true) {
            _common.prompt("Operation Succeeded!", 2, "success", function () {
                againLoadZgGrid();
            }, true);
        } else {
            _common.prompt(data.message, 10, "error", null, true);
        }
    }


    var openNewPage = function (url, param) {
        saveParamToSession();
        param = param || "";
        location.href = url + param;
    }

    var setParamGrid = function () {
        var name = $.trim($("#name").val());
        paramGrid = 'name=' + name;
    }

    //再一次加载表格数据
    var againLoadZgGrid = function () {
        //grid.setParam(paramGrid); 方法被即将淘汰，将由 setting("param",paramGrid)代替
        grid.setting("param", paramGrid);
        grid.setting("page", 1);
        grid.loadData(null);
        trRoleId = "";
        initListPageBut();
    }


    //加载表格
    var loadZgGrid = function () {
        setParamGrid()
        grid = $("#zgGrid").zgGrid({
            url: systemPath + "/role/list",
            title: "Privilege  Details",
            param: paramGrid,
            lineNumber: false,//是否显示行序号，true是
            // colNames: ["id", "角色名称", "角色描述"],// 列头名称
            colNames: ["id", "Privilege Name", "Privilege Description"],// 列头名称
            colModel: [
                {name: "id", type: "int", sort: false, text: "right", width: "20", ishide: true},
                {
                    name: "name",
                    type: "text",
                    sort: true,
                    sortindex: "name",
                    text: "left",
                    width: "100",
                    ishide: false
                },
                {
                    name: "remark",
                    type: "text",
                    sort: false,
                    text: "left",
                    width: "300",
                    ishide: false
                }
            ],//列内容
            width: "max",//宽度自动
            localSort: false,
            sidx: "id",//排序字段
            sord: "desc",//升降序
            page: 1,//当前页,
            rowPerPage: 10,//每页数据量
            eachTrClick: function (trObj) {//正常左侧点击
                butPermissions(trObj);
            },
            loadCompleteEvent: function (self) {
                //数据加载完成后
                initListPageBut();//按钮与选中的tr初始化
                isButPermission();
                return self;
            },
            loadComplete: function (xhr, status) {
                if (xhr.responseJSON.result == false) {
                    _common.prompt(xhr.responseJSON.errorMessage, 15, "error");
                }
            },
            buttonGroup: [
                {
                    butType: "view",
                    butId: "view",
                    butText: "View",
                    butSize: ""//,
                    //customProperty: "pcode='P-ROLE-002'"
                },//查看
                {
                    butType: "add",
                    butId: "add",
                    butText: "Add",
                    butSize: ""//,
                    //customProperty: "pcode='P-ROLE-003'"
                },//增加 默认："",lg:最大，sm小，xs超小
                {
                    butType: "update",
                    butId: "update",
                    butText: "Modify",
                    butSize: ""//,
                    //customProperty: "pcode='P-ROLE-003'"
                },//修改
                {
                    butType: "delete",
                    butId: "del",
                    butText: "Delete",
                    butSize: ""//,
                    // customProperty: "pcode='P-ROLE-004'"
                }
            ]
        });
    }

    var butPermissions = function (trObj) {
        var tr = grid.getSelectColValue(trObj, "id,permissions");
        trRoleId = tr["id"];
        var permissions = tr["permissions"];
        //permissions = permissions.split(",");
        var buts = $("#zgGrid_but_box button");
        buts.prop("disabled", "disabled");
        buts.each(function () {
            var thisBut = $(this);
            // var thisButPcode = thisBut.attr("pcode");
            // if (permissions.indexOf(thisButPcode) != -1) {
            thisBut.removeAttr("disabled");
            //}
            // if (trRoleId == 1) {//此处只为是超级admin角色才用到的判断，因为超级admin的内容是不可以进行 编辑的
            //     $("#update,#del").attr("disabled", "disabled");
            // }
        });

    }

    // 按钮权限验证
    var isButPermission = function () {
        var butEditPermission = $("#butEditPermission").val();
        if (Number(butEditPermission) != 1) {
            $("#add,#update").remove();
        }
        var butViewPermission = $("#butViewPermission").val();
        if (Number(butViewPermission) != 1) {
            $("#view").remove();
        }
        var butDelPermission = $("#butDelPermission").val();
        if (Number(butDelPermission) != 1) {
            $("#del").remove();
        }
    }


    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('rolelist');
_start.load(function (_common) {
    _index.init(_common);
});
