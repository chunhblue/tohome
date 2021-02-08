require("myValidator.css");
require("myAutomatic");
require("myAutoComplete.css");

// var _selectOrganization = require("selectOrganization");
var _myAjax = require("myAjax");
var _myValidator = require("myValidator");
define('roleedit', function () {
    var self = {},
        common = null,
        systemPath = '',
        a_dep = null,
        a_pma = null,
        a_category = null,
        a_subCategory = null,
        a_region = null,
        a_city = null,
        submitFlag=false,
        a_district = null,
        a_store = null;
    const KEY = 'PRIVILEGE_MANAGEMENT';
    function init(_common) {
        common = _common;
        systemPath = common.config.surl;
        _myValidator.init();
        // 资源
        initAutoMatic();
        // 店铺权限分配
        initOrganization();
        // 权限
        initPermission();
        choicePermission();

        // 提交
        submitCheck();

        // 返回
        backEvent();
    }

    var initAutoMatic = function () {
        $("#pma").attr("disabled", true);
        $("#category").attr("disabled", true);
        $("#subCategory").attr("disabled", true);
        $("#pmaRefresh").hide();
        $("#pmaRemove").hide();
        $("#categoryRefresh").hide();
        $("#categoryRemove").hide();
        $("#subCategoryRefresh").hide();
        $("#subCategoryRemove").hide();
        a_dep = $("#dep").myAutomatic({
            url: systemPath + "/deps",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_pma);
                $.myAutomatic.cleanSelectObj(a_category);
                $.myAutomatic.cleanSelectObj(a_subCategory);
                $("#pma").attr("disabled", true);
                $("#category").attr("disabled", true);
                $("#subCategory").attr("disabled", true);
                $("#pmaRefresh").hide();
                $("#pmaRemove").hide();
                $("#categoryRefresh").hide();
                $("#categoryRemove").hide();
                $("#subCategoryRefresh").hide();
                $("#subCategoryRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999") {
                    $("#pma").attr("disabled", false);
                    $("#pmaRefresh").show();
                    $("#pmaRemove").show();
                }
                var dinput = thisObj.attr("k");
                var str = "&depId=" + dinput;
                $.myAutomatic.replaceParam(a_pma, str);//赋值
            }
        });
        a_pma = $("#pma").myAutomatic({
            url: systemPath + "/pmas",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_category);
                $.myAutomatic.cleanSelectObj(a_subCategory);
                $("#category").attr("disabled", true);
                $("#subCategory").attr("disabled", true);
                $("#categoryRefresh").hide();
                $("#categoryRemove").hide();
                $("#subCategoryRefresh").hide();
                $("#subCategoryRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $("#category").attr("disabled", false);
                    $("#categoryRefresh").show();
                    $("#categoryRemove").show();
                }
                var dinput = $("#dep").attr("k");
                var pinput = thisObj.attr("k");
                var str = "&depId=" + dinput+"&pmaId=" + pinput;
                $.myAutomatic.replaceParam(a_category, str);//赋值
            }
        });
        a_category = $("#category").myAutomatic({
            url: systemPath + "/categorys",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_subCategory);
                $("#subCategory").attr("disabled", true);
                $("#subCategoryRefresh").hide();
                $("#subCategoryRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $("#subCategory").attr("disabled", false);
                    $("#subCategoryRefresh").show();
                    $("#subCategoryRemove").show();
                }
                var dinput = $("#dep").attr("k");
                var pinput = $("#pma").attr("k");
                var cinput = thisObj.attr("k");
                var str = "&depId=" + dinput+"&pmaId=" + pinput+"&categoryId=" + cinput;
                $.myAutomatic.replaceParam(a_subCategory, str);//赋值
            }
        });
        a_subCategory = $("#subCategory").myAutomatic({
            url: systemPath + "/subCategorys",
            ePageSize: 10,
            startCount: 0,
        });
        $.myAutomatic.cleanSelectObj(a_dep);
        $.myAutomatic.cleanSelectObj(a_pma);
        $.myAutomatic.cleanSelectObj(a_category);
        $.myAutomatic.cleanSelectObj(a_subCategory);

        $("#depRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_pma);
            $.myAutomatic.cleanSelectObj(a_category);
            $.myAutomatic.cleanSelectObj(a_subCategory);
            $("#pma").attr("disabled", true);
            $("#category").attr("disabled", true);
            $("#subCategory").attr("disabled", true);
            $("#pmaRefresh").hide();
            $("#pmaRemove").hide();
            $("#categoryRefresh").hide();
            $("#categoryRemove").hide();
            $("#subCategoryRefresh").hide();
            $("#subCategoryRemove").hide();
        });
        $("#pmaRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_category);
            $.myAutomatic.cleanSelectObj(a_subCategory);
            $("#category").attr("disabled", true);
            $("#subCategory").attr("disabled", true);
            $("#categoryRefresh").hide();
            $("#categoryRemove").hide();
            $("#subCategoryRefresh").hide();
            $("#subCategoryRemove").hide();
        });
        $("#categoryRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_subCategory);
            $("#subCategory").attr("disabled", true);
            $("#subCategoryRefresh").hide();
            $("#subCategoryRemove").hide();
        });

        $("#addResource").on("click", function (e) {
            var dinput = $("#dep").attr("k");
            var pinput = $("#pma").attr("k");
            var cinput = $("#category").attr("k");
            var sinput = $("#subCategory").attr("k");
            var dinputName = $("#dep").attr("v");
            var pinputName = $("#pma").attr("v");
            var cinputName = $("#category").attr("v");
            var sinputName = $("#subCategory").attr("v");

            if (dinput == null || dinput == "") {
                dinput = "999";
                // dinputName = "全部";
                dinputName = "All";
            }
            if (pinput == null || pinput == "") {
                pinput = "";
                // pinputName = "全大分类";
                pinputName = "All Department";
            }
            if (cinput == null || cinput == "") {
                cinput = "";
                // cinputName = "全中分类";
                cinputName = "All Category";
            }
            if (sinput == null || sinput == "") {
                sinput = "";
                // sinputName = "全小分类";
                sinputName = "All Sub Category";
            }

            var count = 0;
            $("div[name='resRow']").each(function (i, ev) {
                var dep = $(this).find("input[name='depRes']").val();
                var pma = $(this).find("input[name='pmaRes']").val();
                var category = $(this).find("input[name='categoryRes']").val();
                var subCategory = $(this).find("input[name='subCategoryRes']").val();
                if (dep == dinput && pma == pinput && category == cinput && subCategory == sinput) {
                    _common.prompt("The selected resource group is the same as the selected resource group and cannot be submitted!", 5, "info");/*选择的资源组与已选的资源组重复，不可提交*/
                    count++;
                    return;
                }
            });
            if (count > 0) {
                return;
            }
            a_index = $("#flgVal").val() + 1;
            var resourceGroup = "<div class='row' name='resRow'>" +
                "                                <div class='col-xs-12 col-sm-12 col-md-11 col-lg-11'>" +
                "                                    <div class='form-horizontal'>" +
                "                                        <div class='form-group form-group-resource'>" +
                "                                            <label for='iyPost'" +
                "                                                   class='col-sm-1 control-label'>Top Department</label>" +
                "                                            <div class='col-sm-2'>" +
                "                                                <div class='res-info'>" + dinputName + "</div>" +
                "                                                <input type='hidden'" +
                "                                                       class='form-control my-automatic input-sm'" +
                "                                                       name='depRes' value='" + dinput + "'>" +
                "                                            </div>" +
                "                                            <label for='iyPost'" +
                "                                                   class='col-sm-1 control-label'>Department</label>" +
                "                                            <div class='col-sm-2'>" +
                "                                                <div class='res-info'>" + pinputName + "</div>" +
                "                                                <input type='hidden'" +
                "                                                       class='form-control my-automatic input-sm'" +
                "                                                       name='pmaRes' value='" + pinput + "'>" +
                "                                            </div>" +
                "                                            <label for='iyPost'" +
                "                                                   class='col-sm-1 control-label'>Category</label>" +
                "                                            <div class='col-sm-2'>" +
                "                                                <div class='res-info'>" + cinputName + "</div>" +
                "                                                <input type='hidden'" +
                "                                                       class='form-control my-automatic input-sm' name='categoryRes' value='" + cinput + "'>" +
                "                                            </div>" +
                "                                            <label for='iyPost'" +
                "                                                   class='col-sm-1 control-label'>Sub Category</label>" +
                "                                            <div class='col-sm-2'>" +
                "                                                <div class='res-info'>" + sinputName + "</div>" +
                "                                                <input type='hidden'" +
                "                                                       class='form-control my-automatic input-sm' name='subCategoryRes' value='" + sinput + "'>" +
                "                                            </div>" +
                "                                        </div>" +
                "                                    </div>" +
                "                                </div>" +
                "                                <div class='col-xs-12 col-sm-12 col-md-1 col-lg-1'>" +
                "                                    <div class='col-sm-1'>" +
                "                                        <a class='btn btn-default btn-sm'" +
                "                                           href='javascript:void(0);'" +
                "                                           role='button' id='delResource" + a_index + "'>" +
                "                                            <div class='glyphicon glyphicon-minus'></div>" +
                "                                        </a>" +
                "                                    </div>" +
                "                                </div>" +
                "                            </div>";
            $("#splitLine").before(resourceGroup);
            $.myAutomatic.cleanSelectObj(a_dep);
            $.myAutomatic.cleanSelectObj(a_pma);
            $.myAutomatic.cleanSelectObj(a_category);
            $.myAutomatic.cleanSelectObj(a_subCategory);
            $("#pma").attr("disabled", true);
            $("#category").attr("disabled", true);
            $("#subCategory").attr("disabled", true);
            $("#pmaRefresh").hide();
            $("#pmaRemove").hide();
            $("#categoryRefresh").hide();
            $("#categoryRemove").hide();
            $("#subCategoryRefresh").hide();
            $("#subCategoryRemove").hide();
        })

        $("#resPost").on("click", "a[id*='delResource']", function (e) {
            var aId;
            if (e.target.tagName == "A") {
                aId = $(e.target).attr("id");
            } else {
                aId = $(e.target).parent().attr("id");
            }
            $("#" + aId).closest(".row").remove();
        })
    }

    var initOrganization = function () {
        // 初始化，子级禁用
        $("#a_city").attr("disabled", true);
        $("#a_district").attr("disabled", true);
        $("#a_store").attr("disabled", true);
        $("#cityRefresh").hide();
        $("#cityRemove").hide();
        $("#districtRefresh").hide();
        $("#districtRemove").hide();
        $("#storeRefresh").hide();
        $("#storeRemove").hide();
        // 输入框事件绑定
        a_region = $("#a_region").myAutomatic({
            url: systemPath + "/organizationalStructure/getStructureByLevel?level=0",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_city);
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                $("#a_city").attr("disabled", true);
                $("#a_district").attr("disabled", true);
                $("#a_store").attr("disabled", true);
                $("#cityRefresh").hide();
                $("#cityRemove").hide();
                $("#districtRefresh").hide();
                $("#districtRemove").hide();
                $("#storeRefresh").hide();
                $("#storeRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999999") {
                    $("#a_city").attr("disabled", false);
                    $("#cityRefresh").show();
                    $("#cityRemove").show();
                }
                var rinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&level=1&parentId=" + rinput;
                $.myAutomatic.replaceParam(a_city, str);
            }
        });
        a_city = $("#a_city").myAutomatic({
            url: systemPath + "/organizationalStructure/getStructureByLevel",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_district);
                $.myAutomatic.cleanSelectObj(a_store);
                $("#a_district").attr("disabled", true);
                $("#a_store").attr("disabled", true);
                $("#districtRefresh").hide();
                $("#districtRemove").hide();
                $("#storeRefresh").hide();
                $("#storeRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999999") {
                    $("#a_district").attr("disabled", false);
                    $("#districtRefresh").show();
                    $("#districtRemove").show();
                }
                var cinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&level=2&parentId=" + cinput;
                $.myAutomatic.replaceParam(a_district, str);
            }
        });
        a_district = $("#a_district").myAutomatic({
            url: systemPath + "/organizationalStructure/getStructureByLevel",
            ePageSize: 10,
            startCount: 0,
            selectEleClick: function (thisObj) {
                $.myAutomatic.cleanSelectObj(a_store);
                $("#a_store").attr("disabled", true);
                $("#storeRefresh").hide();
                $("#storeRemove").hide();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999999") {
                    $("#a_store").attr("disabled", false);
                    $("#storeRefresh").show();
                    $("#storeRemove").show();
                }
                var dinput = thisObj.attr("k");
                // 替换子级查询参数
                var str = "&level=3&parentId=" + dinput;
                $.myAutomatic.replaceParam(a_store, str);
            }
        });
        a_store = $("#a_store").myAutomatic({
            url: systemPath + "/organizationalStructure/getStructureByLevel",
            ePageSize: 10,
            startCount: 0,
        });

        // 选值栏位清空按钮事件绑定
        $("#regionRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_city);
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $("#a_city").attr("disabled", true);
            $("#a_district").attr("disabled", true);
            $("#a_store").attr("disabled", true);
            $("#cityRefresh").hide();
            $("#cityRemove").hide();
            $("#districtRefresh").hide();
            $("#districtRemove").hide();
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        });
        $("#cityRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $("#a_district").attr("disabled", true);
            $("#a_store").attr("disabled", true);
            $("#districtRefresh").hide();
            $("#districtRemove").hide();
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        });
        $("#districtRemove").on("click", function (e) {
            $.myAutomatic.cleanSelectObj(a_store);
            $("#a_store").attr("disabled", true);
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        });

        // 添加按钮事件
        $("#addStoreResource").on("click", function (e) {
            // 获取选择值
            var rinput = $("#a_region").attr("k");
            var cinput = $("#a_city").attr("k");
            var dinput = $("#a_district").attr("k");
            var sinput = $("#a_store").attr("k");
            var rinputName = $("#a_region").attr("v");
            var cinputName = $("#a_city").attr("v");
            var dinputName = $("#a_district").attr("v");
            var sinputName = $("#a_store").attr("v");

            if (rinput == null || rinput == "") {
                rinput = "999999";
                rinputName = "All Region";
            }
            if (cinput == null || cinput == "") {
                cinput = "999999";
                cinputName = "All City";
            }
            if (dinput == null || dinput == "") {
                dinput = "999999";
                dinputName = "All District";
            }
            if (sinput == null || sinput == "") {
                sinput = "999999";
                sinputName = "All Store";
            }
            // 遍历已选择值，判断包含和重复
            var count = 0;
            $("div[name='resStoreRow']").each(function (i, ev) {
                var region = $(this).find("input[name='regionRes']").val();
                var city = $(this).find("input[name='cityRes']").val();
                var district = $(this).find("input[name='districtRes']").val();
                var store = $(this).find("input[name='storeRes']").val();
                // 已选择 All Region
                if(region == '999999'){
                    _common.prompt("Has the full store permission, cannot submit!", 5, "info");/* 已经拥有全店铺权限，不可提交 */
                    count++;
                    return false;
                }
                // 已选择 NationWide：包含南北区，如果子级选择全部，则效果等同于All Region
                if(region == '000001' && city == '999999'){
                    _common.prompt("Has the full store permission, cannot submit!", 5, "info");/* 已经拥有全店铺权限，不可提交 */
                    count++;
                    return false;
                }
                // 大区相同，城市已选择All City
                if (region == rinput && city == '999999') {
                    _common.prompt("Already has the permission of the entire store in the region, cannot submit!", 5, "info");/* 已经拥有该大区全店铺权限，不可提交 */
                    count++;
                    return false;
                }
                // 大区、城市相同，区域已选择All District
                if (region == rinput && city == cinput && district == '999999') {
                    _common.prompt("Already has the permission of the entire store in the city, cannot submit!", 5, "info");/* 已经拥有该城市全店铺权限，不可提交 */
                    count++;
                    return false;
                }
                // 大区、城市、区域相同，店铺已选择All District
                if (region == rinput && city == cinput && district == dinput && store == '999999') {
                    _common.prompt("Already has the permission of the entire store in the district, cannot submit!", 5, "info");/* 已经拥有该区域全店铺权限，不可提交 */
                    count++;
                    return false;
                }
                // 选择相同店铺
                if (region == rinput && city == cinput && district == dinput && store == sinput) {
                    _common.prompt("The selected resource group is the same as the selected resource group and cannot be submitted!", 5, "info");/* 选择的资源组与已选的资源组重复，不可提交 */
                    count++;
                    return false;
                }
            });
            if (count > 0) {
                return;
            }
            // 添加、显示
            store_index = $("#storeFlgVal").val() + 1;
            var resourceGroup = "<div class='row' name='resStoreRow'>" +
                "    <div class='col-xs-12 col-sm-12 col-md-11 col-lg-11'>" +
                "        <div class='form-horizontal'>" +
                "            <div class='form-group form-group-resource'>" +
                "                <label for='iyPost' class='col-sm-1 control-label'>Region</label>" +
                "                <div class='col-sm-2'>" +
                "                    <div class='res-info'>" + rinputName + "</div>" +
                "                    <input type='hidden' class='form-control input-sm'" +
                "                           name='regionRes' value='" + rinput + "'>" +
                "                </div>" +
                "                <label for='iyPost' class='col-sm-1 control-label'>City</label>" +
                "                <div class='col-sm-2'>" +
                "                    <div class='res-info'>" + cinputName + "</div>" +
                "                    <input type='hidden' class='form-control input-sm'" +
                "                           name='cityRes' value='" + cinput + "'>" +
                "                </div>" +
                "                <label for='iyPost' class='col-sm-1 control-label'>District</label>" +
                "                <div class='col-sm-2'>" +
                "                    <div class='res-info'>" + dinputName + "</div>" +
                "                    <input type='hidden' class='form-control input-sm'" +
                "                           name='districtRes' value='" + dinput + "'>" +
                "                </div>" +
                "                <label for='iyPost' class='col-sm-1 control-label'>Store</label>" +
                "                <div class='col-sm-2'>" +
                "                    <div class='res-info'>" + sinputName + "</div>" +
                "                    <input type='hidden' class='form-control input-sm'" +
                "                           name='storeRes' value='" + sinput + "'>" +
                "                </div>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "    <div class='col-xs-12 col-sm-12 col-md-1 col-lg-1'>" +
                "        <div class='col-sm-1'>" +
                "            <a class='btn btn-default btn-sm' href='javascript:void(0);'" +
                "               role='button' id='delStoreResource" + store_index + "'>" +
                "                <div class='glyphicon glyphicon-minus'></div>" +
                "            </a>" +
                "        </div>" +
                "    </div>" +
                "</div>";
            $("#storeSplitLine").before(resourceGroup);
            // 重置栏位，准备下一次选值
            $.myAutomatic.cleanSelectObj(a_region);
            $.myAutomatic.cleanSelectObj(a_city);
            $.myAutomatic.cleanSelectObj(a_district);
            $.myAutomatic.cleanSelectObj(a_store);
            $("#a_city").attr("disabled", true);
            $("#a_district").attr("disabled", true);
            $("#a_store").attr("disabled", true);
            $("#cityRefresh").hide();
            $("#cityRemove").hide();
            $("#districtRefresh").hide();
            $("#districtRemove").hide();
            $("#storeRefresh").hide();
            $("#storeRemove").hide();
        })

        // 绑定删除按钮事件
        $("#resStore").on("click", "a[id*='delStoreResource']", function (e) {
            var aId;
            if (e.target.tagName == "A") {
                aId = $(e.target).attr("id");
            } else {
                aId = $(e.target).parent().attr("id");
            }
            $("#" + aId).closest("div[name='resStoreRow']").remove();
        })
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
                        "                <label class='col-sm-12 control-label perm-parent-menu'>" +
                        "                   <input type='checkbox' class='parent_all' parentMenu='" + data[i].menuId +
                        "                       '/>" + data[i].menuName + "</label>" +
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
                            "                    <label class='col-sm-12 control-label perm-parent-menu'>" +
                            "                       <input type='checkbox' class='child_all' childMenu='" + sonMenus[j].menuId +
                            "                           '/>" + sonMenus[j].menuName + "</label>" +
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
                                var sel = "", classSel = "";
                                if (perms[k].flg == 1) {
                                    sel = "checked=checked";
                                    classSel = "sel"
                                }
                                var perm =
                                    "<label class='col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox " + classSel + "'" +
                                    "        ><input " +
                                    "        type='checkbox' name='permId' parentMenu='" + data[i].menuId + "' childMenu='" + sonMenus[j].menuId + "' value='" + perms[k].permId + "'" +
                                    "         " + sel + "/>" + perms[k].permName + "</label>";
                                $("#childMenu" + sonMenus[j].menuId).append(perm);

                            }
                        }
                    }
                }
            },
            complete: _common.myAjaxComplete
        });
    }

    var choicePermission = function () {
        // $("#permissionDiv input:checkbox").on("click", function (e) {
        $("#permissionDiv input[name='permId']").on("click", function (e) {
            var flag = $(this).attr("checked");
            flag = $(this).is(":checked");
            if (flag) {
                $(this).parent().addClass("sel");
            } else {
                $(this).parent().removeClass("sel");
            }
        });
        $("#permissionDiv .child_all").on("click", function (e) {
            var flag = $(this).is(":checked");
            var _id = $(this).attr("childMenu");
            var _list = $("#childMenu" + _id).find('input');
            _list.each(function(){
                $(this).prop("checked",flag);
                if (flag) {
                    $(this).parent().addClass("sel");
                } else {
                    $(this).parent().removeClass("sel");
                }
            });
        });
        $("#permissionDiv .parent_all").on("click", function (e) {
            var flag = $(this).is(":checked");
            var _id = $(this).attr("parentMenu");
            var _list = $("#parentMenu" + _id).find('input');
            _list.each(function(){
                $(this).prop("checked",flag);
                if (flag) {
                    $(this).parent().addClass("sel");
                } else {
                    $(this).parent().removeClass("sel");
                }
            });
        });
    }

    var submitCheck = function () {

        //提交整体角色数据
        $("#submitData").on("click", function () {
            var isValIdtion = _myValidator.startValidator();
            if (isValIdtion) {
                _common.myConfirm("Please confirm the content of this editing？", function (rest) {/*请确认提交本次编辑的内容*/
                    if (rest == "true") {
                        //整合角色基本信息
                        integrationRoleDataToHideDiv();
                        //整合资源信息
                        // integrationResourcesDataToHideDiv();
                        //整合店铺权限信息
                        // integrationPermissionsDataToHideStore();
                        //整合权限信息
                        integrationPermissionsDataToHideDiv();
                        //整合菜单信息
                        //formattingMenusDataToHideDiv();
                        // 提交
                        $.myAjaxs({
                            url: systemPath + "/role/submitData",
                            async: false,
                            cache: false,
                            type: "post",
                            dataType: "json",
                            data: $("#submitFrom").serialize(),
                            success: showResponse,
                            complete: _common.myAjaxComplete
                        });
                    }
                });
            }
        });
    }

    //提交回调函数
    function showResponse(data, textStatus, xhr) {
        $("#toKen").val(data.toKen);
        //_common.loading_close();
        if (data.success == true) {
            _common.prompt("Operation Succeeded!", 2, "success", function () {
                openNewPage(systemPath + "/role", "");
            }, true);
        } else {
            _common.prompt(data.message, 10, "error", null, true);
        }
    }

    var backEvent = function () {

        $("#returnPage").on("click", function () {
            if (!submitFlag) {
                _common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
                    if (result==="true") {
                        openNewPage(systemPath + "/role", "");
                    }

                });
            }
        });

    }

    var openNewPage = function (url, param) {
        _common.updateBackFlg(KEY);
        param = param || "";
        location.href = url + param;
    }

    // 基础信息
    var integrationRoleDataToHideDiv = function () {
        $("#pmRole_id").val($("#roleId").val());
        $("#pmRole_name").val($("#role_basic_name").val());
        $("#pmRole_remark").val($("#roleBaseRemark").val());
    }
    // 资源信息
    var integrationResourcesDataToHideDiv = function () {
        var i = 0, g = 1;
        var insertHtml = "";
        $("#from_hide_resources").text("");
        $("div[name='resRow']").each(function (e) {
            var dep = $(this).find("input[name='depRes']").val();
            insertHtml += '<input name="resources[' + i + '].groupCode" type="hidden" value="' + Number(g) + '" />';
            insertHtml += '<input name="resources[' + i + '].type" type="hidden" value="' + 1 + '" />';
            insertHtml += '<input name="resources[' + i + '].resourceId" type="hidden" value="' + dep + '" />';
            //g++;
            i++;
            var pma = $(this).find("input[name='pmaRes']").val();
            if (pma != null && pma != "") {
                insertHtml += '<input name="resources[' + i + '].groupCode" type="hidden" value="' + Number(g) + '" />';
                insertHtml += '<input name="resources[' + i + '].type" type="hidden" value="' + 2 + '" />';
                insertHtml += '<input name="resources[' + i + '].resourceId" type="hidden" value="' + pma + '" />';
                //g++;
                i++;
            }
            var category = $(this).find("input[name='categoryRes']").val();
            if (category != null && category != "") {
                insertHtml += '<input name="resources[' + i + '].groupCode" type="hidden" value="' + Number(g) + '" />';
                insertHtml += '<input name="resources[' + i + '].type" type="hidden" value="' + 3 + '" />';
                insertHtml += '<input name="resources[' + i + '].resourceId" type="hidden" value="' + category + '" />';
                //g++;
                i++;
            }
            var subCategory = $(this).find("input[name='subCategoryRes']").val();
            if (subCategory != null && subCategory != "") {
                insertHtml += '<input name="resources[' + i + '].groupCode" type="hidden" value="' + Number(g) + '" />';
                insertHtml += '<input name="resources[' + i + '].type" type="hidden" value="' + 4 + '" />';
                insertHtml += '<input name="resources[' + i + '].resourceId" type="hidden" value="' + subCategory + '" />';
                i++;
            }
            g++;
        });
        $("#from_hide_resources").append(insertHtml);
    }

    var integrationPermissionsDataToHideStore = function() {
        var itemDetail = [], num = 0;
        $("#from_hide_stores").text("");
        $("div[name='resStoreRow']").each(function (e) {
            ++num;
            var item = {
                regionCd:$(this).find("input[name='regionRes']").val(),
                cityCd:$(this).find("input[name='cityRes']").val(),
                districtCd:$(this).find("input[name='districtRes']").val(),
                storeCd:$(this).find("input[name='storeRes']").val(),
                serialNo:num
            }
            itemDetail.push(item);
        });
        $("#from_hide_stores").val(JSON.stringify(itemDetail));
    }

    var integrationPermissionsDataToHideDiv = function () {
        var permIds = "";
        var menuIds = "";
        var from_hide_permissions = $("#from_hide_permissions");
        var from_hide_menus = $("#from_hide_menus");
        var parentArr = [];
        var childArr = [];
        $("input[name='permId']:checked").each(function (e) {
            var ck = $(this);
            permIds += ck.val() + ",";
            var parent = ck.attr("parentMenu");
            var pi = parentArr.indexOf(parent);
            if (pi < 0) {
                parentArr.push(parent);
                menuIds += parent + ",";
            }
            var child = ck.attr("childMenu");
            var ci = childArr.indexOf(child);
            if (ci < 0) {
                childArr.push(child);
                menuIds += child + ",";
            }

        });
        permIds = permIds.substring(0, permIds.length - 1);
        from_hide_permissions.val(permIds);
        menuIds = menuIds.substring(0, menuIds.length - 1);
        from_hide_menus.val(menuIds);
    }


    self.init = init;
    return self;
});
var _start = require('start');
var _roleedit = require('roleedit');
_start.load(function (_common) {
    _roleedit.init(_common);
});
