define('roleview', function () {
    var self = {},
        common = null,
        systemPath = '',
        a_grandDiv = null,
        a_department = null,
        a_dpt = null;
    const KEY = 'PRIVILEGE_MANAGEMENT';
    function init(_common) {
        common = _common;
        systemPath = common.config.surl;
        // 权限
        initPermission();

        // 返回
        backEvent();
    }

    var initPermission = function () {
        $.myAjaxs({
            url: systemPath + "/role/loadperm?roleId=" + $("#roleId").val(),
            async: false,
            cache: false,
            type: "get",
            // data: "id=" + cols['id'] + "&toKen=" + token.val(),
            dataType: "json",
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    var parentMenu = "<div class='row'>" +
                        "    <div class='col-xs-12 col-sm-12 col-md-2 col-lg-2'>" +
                        "        <div class='form-horizontal'>" +
                        "            <div class='form-group form-group-resource'>" +
                        "                <label for='iyPost'" +
                        "                       class='col-sm-12 control-label perm-parent-menu'>" + data[i].menuName + "</label>" +
                        "            </div>" +
                        "        </div>" +
                        "    </div>" +
                        "    <div class='col-xs-12 col-sm-12 col-md-10 col-lg-10 perm-background ' id='parentMenu" + data[i].menuId + "'>" +
                        "    </div>" +
                        "</div>";
                    $("#permissionDiv").append(parentMenu);
                    var sonMenus = data[i].sonMenus;
                    if(sonMenus!=null&&sonMenus.length>0)
                    for (var j = 0; j < sonMenus.length; j++) {
                        var childMenu =
                            "<div class='form-horizontal perm-line-box'>" +
                            "    <div class='form-group form-group-resource '>" +
                            "        <div class='col-xs-12 col-sm-12 col-md-2 col-lg-2'>" +
                            "            <div class='form-horizontal'>" +
                            "                <div class='form-group form-group-resource'>" +
                            "                    <label for='iyPost'" +
                            "                           class='col-sm-12 control-label perm-parent-menu'>" + sonMenus[j].menuName + "</label>" +
                            "                </div>" +
                            "            </div>" +
                            "        </div>" +
                            "        <div class='col-xs-12 col-sm-12 col-md-10 col-lg-10'>" +
                            "            <div class='form-horizontal perm-div'>" +
                            "                <div class='form-group form-group-resource' id='childMenu" + sonMenus[j].menuId + "'>" +
                            "                </div>" +
                            "            </div>" +
                            "        </div>" +
                            "    </div>" +
                            "</div>";
                        $("#parentMenu" + data[i].menuId).append(childMenu);
                        var perms = sonMenus[j].perms;
                        if (perms != null && perms.length > 0) {
                            for (var k = 0; k < perms.length; k++) {
                                var classSel = "";
                                if (perms[k].flg == 1) {
                                    classSel = "sel"
                                }
                                var perm =
                                    "<label class='col-xs-6 col-sm-4 col-md-4 col-lg-3 " + classSel + "'>" + perms[k].permName + "</label>";
                                $("#childMenu" + sonMenus[j].menuId).append(perm);

                            }
                        }
                    }
                }
            },
            complete: _common.myAjaxComplete
        });
    }

    var backEvent = function () {
        $("#returnPage").on("click", function () {
            openNewPage(systemPath + "/role", "");
        });
    }


    var openNewPage = function (url, param) {
        _common.updateBackFlg(KEY);
        param = param || "";
        location.href = url + param;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _roleview = require('roleview');
_start.load(function (_common) {
    _roleview.init(_common);
});
