[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport"
          content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0"
          user-scalable=no>
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet"
          type="text/css"/>
[#--<link href="${m.staticpath}/[@spring.message 'css.roleedit'/]" rel="stylesheet"--]
[#--type="text/css"/>--]
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.roleedit'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
[#--[if lt IE 9]>--]
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
[#--<![endif]--]
    <style>
        .form-group-resource {
            margin-bottom: 0px;
        }

        .res-info {
            margin-top: 7px;
        }

        .form-horizontal .perm-parent-menu {
            text-align: left;
            margin-left: 20px;
        }

        .perm-div {
            padding-left: 10px;
        }

        .perm-div label {
            font-weight: 500;
            padding-left: -15px;
            color: #1b809e;
        }

        .perm-div label.sel,
        .perm-div label.sel:hover,
        .perm-div label:hover {
            color: #ff8008;
        }

        .perm-line-box {
            border-bottom: 1px dashed #cccccc;
        }

        .perm-background {
        }

        .perm-background .perm-line-box:hover {
            /*background-color: #cccccc;*/
            border-radius: 3px;
            box-shadow: 0 0px 20px rgba(0, 0, 0, 0.1);
            background-color: #f9f9f9;
        }

        #permissionDiv .parent_all,
        #permissionDiv .child_all {
            position: absolute;
            margin: 2px 0 0 -18px;
            line-height: normal;
        }

    </style>
</head>


<body>
<!--页头-->
	[@common.header][/@common.header]
<!--导航-->
	[@common.nav "HOME&Privilege&Privilege Management&Edit"][/@common.nav]
<input id="roleId" value="${roleId!}" type="hidden"/>

<div class="container-fluid" id="ui_box">
    <!-- 角色权限核心html -->
    <!-- 角色基本 -->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">Privilege</div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="iyPost"
                                           class="col-sm-1 control-label not-null" style="white-space: nowrap">Privilege Name</label>
                                    <div class="col-sm-6">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control input-sm not-null"
                                                   id="role_basic_name" name="roleBasicName"
                                                   value="${(role.name)!}"
                                                   required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="iyPost" class="col-sm-1 control-label" style="white-space: nowrap">Privilege Description</label>
                                    <div class="col-sm-6">
                                        <textarea class=" form-control " cols="20"
                                                  id="roleBaseRemark"
                                                  rows="2">${(remark.remark)!}</textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

[#--<div class="row"><!-- 角色资源 -->--]
    [#--<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--<div class="box">--]
[#--<div class="box-title ">--]
[#--<div class="tt">资源分配</div>--]
[#--</div>--]
[#--<div class="box-body box-body-padding-10" style="">--]
[#--<div class="row">--]
[#--<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--1--]
[#--</div>--]
[#--</div>--]
[#--<div class="wire"></div>--]
[#--<div class="row">--]
[#--<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--2--]
[#--</div>--]
[#--</div>--]

[#--</div>--]
[#--</div>--]
[#--</div>--]
[#--</div>--]

    <div class="row" hidden>
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">Resource Allocation</div>
                </div>
                <div class="box-body box-body-padding-10" id="resPost">
                    [#assign index=0]
                    [#if dtos?? && dtos?size > 0]
                        [#list dtos as dto]
                            <div class="row" name="resRow">
                                <div class="col-xs-12 col-sm-12 col-md-11 col-lg-11">
                                    <div class="form-horizontal">
                                        <div class="form-group form-group-resource">
                                            <label for="iyPost"
                                                   class="col-sm-1 control-label" style="white-space: nowrap">Top Department</label>
                                            <div class="col-sm-2">
                                                <div class="res-info">${(dto.depName)!}</div>
                                                <input type="hidden"
                                                       class="form-control my-automatic input-sm"
                                                       name="depRes" value="${dto.depCd!}">
                                            </div>
                                            <label for="iyPost"
                                                   class="col-sm-1 control-label">Department</label>[#--大分类--]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.pmaName!}</div>
                                                <input type="hidden"
                                                       class="form-control my-automatic input-sm"
                                                       name="pmaRes" value="${dto.pmaCd!}">
                                            </div>
                                            <label for="iyPost"
                                                   class="col-sm-1 control-label">Category</label>[#--中分类--]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.categoryName!}</div>
                                                <input type="hidden"
                                                       class="form-control my-automatic input-sm"
                                                       name="categoryRes" value="${dto.categoryCd!}">
                                            </div>
                                            <label for="iyPost"
                                                   class="col-sm-1 control-label">Sub Category</label>[#--小分类--]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.subCategoryName!}</div>
                                                <input type="hidden"
                                                       class="form-control my-automatic input-sm"
                                                       name='subCategoryRes' value="${dto.subCategoryCd!}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
                                    <div class="col-sm-1">
                                        <a class="btn btn-default btn-sm"
                                           href="javascript:void(0);"
                                           role="button" id="delResource${index}">
                                            <div class="glyphicon glyphicon-minus"></div>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            [#assign index=index + 1]
                        [/#list]
                    [/#if]
                    <input type="hidden" id="flgVal" value="${index}"/>
                    <div class="wire" id="splitLine" style="margin-top: 4px;"></div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-11 col-lg-11">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="dep" class="col-sm-1 control-label" style="white-space: nowrap">Top Department</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="dep">
                                            <a id="depRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="depRemove" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="pma" class="col-sm-1 control-label">Department</label>
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="pma">
                                            <a id="pmaRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="pmaRemove" href="javascript:void(0);"
                                               title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="category" class="col-sm-1 control-label">Category</label>[#--中分类--]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="category">[#-- placeholder="全中分类"--]
                                            <a id="categoryRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="categoryRemove" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="subCategory" class="col-sm-1 control-label">Sub Category</label>[#--小分类--]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="subCategory">[#-- placeholder="全小分类"--]
                                            <a id="subCategoryRefresh" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="subCategoryRemove" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
                            <div class="col-sm-1">
                                <a class="btn btn-default btn-sm" href="javascript:void(0);"
                                   role="button" id="addResource">
                                    <div class="glyphicon glyphicon-plus"></div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row" hidden>
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
            <div class="tt">Organization Distribution</div>[#--组织分配--]
                </div>
                <div class="box-body box-body-padding-10" id="resStore">
                    [#assign storeIndex=0]
                    [#if storeDtos?? && storeDtos?size > 0]
                        [#list storeDtos as dto]
                            <div class="row" name="resStoreRow">
                                <div class="col-xs-12 col-sm-12 col-md-11 col-lg-11">
                                    <div class="form-horizontal">
                                        <div class="form-group form-group-resource">
                                            <label for="iyPost" class="col-sm-1 control-label">Region</label>[#-- 大区 --]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.regionName!}</div>
                                                <input type="hidden" class="form-control input-sm" name="regionRes" value="${dto.regionCd!}">
                                            </div>
                                            <label for="iyPost" class="col-sm-1 control-label">City</label>[#-- 城市 --]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.cityName!}</div>
                                                <input type="hidden" class="form-control input-sm" name="cityRes" value="${dto.cityCd!}">
                                            </div>
                                            <label for="iyPost" class="col-sm-1 control-label">District</label>[#-- 区域 --]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.districtName!}</div>
                                                <input type="hidden" class="form-control input-sm" name="districtRes" value="${dto.districtCd!}">
                                            </div>
                                            <label for="iyPost" class="col-sm-1 control-label">Store</label>[#-- 店铺 --]
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.storeName!}</div>
                                                <input type="hidden" class="form-control input-sm" name='storeRes' value="${dto.storeCd!}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
                                    <div class="col-sm-1">
                                    <a class="btn btn-default btn-sm" href="javascript:void(0);" role="button" id="delStoreResource${storeIndex}">
                                        <div class="glyphicon glyphicon-minus"></div>
                                    </a>
                                    </div>
                                </div>
                            </div>
                            [#assign storeIndex=storeIndex + 1]
                        [/#list]
                    [/#if]
                    <div class="wire" id="storeSplitLine" style="margin-top: 4px;"></div>
                    <input type="hidden" id="storeFlgVal" value="${storeIndex}"/>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-11 col-lg-11">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="a_region" class="col-sm-1 control-label">Region</label>[#-- 大区 --]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="a_region">
                                            <a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="a_city" class="col-sm-1 control-label">City</label>[#-- 城市 --]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="a_city">
                                            <a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="a_district" class="col-sm-1 control-label">District</label>[#-- 区域 --]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="a_district">
                                            <a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                    <label for="a_store" class="col-sm-1 control-label">Store</label>[#-- 店铺 --]
                                    <div class="col-sm-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="a_store">
                                            <a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
                            <div class="col-sm-1">
                                <a class="btn btn-default btn-sm" href="javascript:void(0);" role="button" id="addStoreResource">
                                    <div class="glyphicon glyphicon-plus"></div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row"><!-- 角色权限 -->
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">Permission Assignment</div>[#--权限分配--]
                </div>
                <div class="box-body box-body-padding-10" id="permissionDiv">
                [#--<div class="row">--]
                [#--<div class="col-xs-12 col-sm-12 col-md-2 col-lg-2">--]
                [#--<div class="form-horizontal">--]
                [#--<div class="form-group form-group-resource">--]
                [#--<label for="iyPost"--]
                [#--class="col-sm-12 control-label perm-parent-menu">基础信息管理</label>--]
                [#--<input type="hidden" name="menuId" value=""/>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                [#--<div class="col-xs-12 col-sm-12 col-md-10 col-lg-10 perm-background">--]
                [#--<div class="form-horizontal perm-line-box">--]
                [#--<div class="form-group form-group-resource ">--]
                [#--<div class="col-xs-12 col-sm-12 col-md-2 col-lg-2">--]
                [#--<div class="form-horizontal">--]
                [#--<div class="form-group form-group-resource">--]
                [#--<label for="iyPost"--]
                [#--class="col-sm-12 control-label perm-parent-menu">用户管理</label>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                [#--<div class="col-xs-12 col-sm-12 col-md-10 col-lg-10">--]
                [#--<div class="form-horizontal perm-div">--]
                [#--<div class="form-group form-group-resource">--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--<label class="col-xs-6 col-sm-4 col-md-4 col-lg-3 checkbox sel"--]
                [#--groupCode=""><input--]
                [#--groupCode="" menuid=""--]
                [#--type="checkbox" value=""--]
                [#--code=""/>用户管理 - 列表查看权限</label>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                [#--</div>--]
                </div>
            </div>
        </div>
    </div>

    <!-- 角色权限核心html-end -->
    <div class="row"><!-- 按钮 -->
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="ui-role-box text-center">
                <button type="button" class="btn btn-primary" id="submitData"><span
                        class="glyphicon glyphicon-check icon-right"></span>Submit
                </button>
                <button type="button" class="btn btn-default" id="returnPage"><span
                        class="glyphicon glyphicon-share icon-right"></span>Back
                </button>
            </div>
        </div>
    </div>
</div>

<form id="submitFrom" style="display:none;">
    <!-- 角色 -->
    <div id="from_hide_role">
        <input name="id" type="hidden" id="pmRole_id" value=""/><!--角色 信息 :角色id-->
        <input name="name" type="hidden" id="pmRole_name" value=""/><!--角色 信息：角色名称-->
        <input name="remark" type="hidden" id="pmRole_remark" value=""/>
        <!--角色 信息：角色概述-->
    </div>
    <!-- 资源 -->
    <div id="from_hide_resources"></div>
    <!-- 权限 -->
    <input id="from_hide_permissions" type="hidden" name="permissionsStr" value=""/>
    <!-- 权限 -->
    <input id="from_hide_stores" type="hidden" name="storesStr" value=""/>
    <!-- 菜单 -->
    <input id="from_hide_menus" type="hidden" name="menusStr" value=""/>
    <input id="toKen" name="toKen" value="${toKen}" type="hidden">
</form>
<!--页脚-->
	[@common.footer][/@common.footer]

</body>
</html>
