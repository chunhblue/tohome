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
    <script src="${m.staticpath}/[@spring.message 'js.roleview'/]"></script>
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
            margin-left: 11px;
        }

        .perm-div {
        }

        .perm-div label {
            font-weight: 500;
            padding-left: -15px;
            color: #1b809e;
        }

        .perm-div label.sel {
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

    </style>
</head>


<body>
<!--页头-->
	[@common.header][/@common.header]
<!--导航-->
	[@common.nav "HOME&Privilege&Privilege Management&View"][/@common.nav]
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
                                    <label for="iyPost" class="col-sm-1 control-label" style="white-space: nowrap">Privilege Name</label>
                                    <div class="col-sm-6">
                                        <div class="res-info">
                                        ${(role.name)!}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="iyPost" class="col-sm-1 control-label" style="white-space: nowrap">Privilege Description</label>
                                    <div class="col-sm-11">
                                        <div class="res-info">
                                        ${(remark.remark)!}
                                        </div>
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
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group form-group-resource">
                                    <label for="iyPost"
                                           class="col-sm-1 control-label" style="white-space: nowrap">Top Department</label>
                                    <div class="col-sm-2">
                                        <div class="res-info">${(dto.depName)!}</div>

                                    </div>
                                    <label for="iyPost"
                                           class="col-sm-1 control-label">Department</label>
                                    <div class="col-sm-2">
                                        <div class="res-info">${dto.pmaName!}</div>

                                    </div>
                                    <label for="iyPost"
                                           class="col-sm-1 control-label">Category</label>
                                    <div class="col-sm-2">
                                        <div class="res-info">${dto.categoryName!}</div>

                                    </div>
                                    <label for="iyPost"
                                           class="col-sm-1 control-label">Sub Category</label>
                                    <div class="col-sm-2">
                                        <div class="res-info">${dto.subCategoryName!}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                            [#assign index=index + 1]
                        [/#list]
                    [/#if]
                    <input type="hidden" id="flgVal" value="${index}"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row" hidden>
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title">
                    <div class="tt">Organization Distribution</div>[#--组织分配--]
                </div>
                <div class="box-body box-body-padding-10" id="resPost">
                    [#assign storeIndex=0]
                    [#if storeDtos?? && storeDtos?size > 0]
                        [#list storeDtos as dto]
                            <div class="row" name="resStoreRow">
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <div class="form-horizontal">
                                        <div class="form-group form-group-resource">
                                            <label for="iyPost" class="col-sm-1 control-label">Region</label>
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.regionName!}</div>
                                            </div>
                                            <label for="iyPost" class="col-sm-1 control-label">City</label>
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.cityName!}</div>
                                            </div>
                                            <label for="iyPost" class="col-sm-1 control-label">District</label>
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.districtName!}</div>
                                            </div>
                                            <label for="iyPost" class="col-sm-1 control-label">Store</label>
                                            <div class="col-sm-2">
                                                <div class="res-info">${dto.storeName!}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            [#assign storeIndex=storeIndex + 1]
                        [/#list]
                    [/#if]
                    <input type="hidden" id="storeFlgVal" value="${storeIndex}"/>
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
                </div>
            </div>
        </div>
    </div>

    <!-- 角色权限核心html-end -->
    <div class="row"><!-- 按钮 -->
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="ui-role-box text-center">
                <button type="button" class="btn btn-default" id="returnPage"><span
                        class="glyphicon glyphicon-share icon-right"></span>Back
                </button>
            </div>
        </div>
    </div>
</div>

<!--页脚-->
	[@common.footer][/@common.footer]

</body>
</html>
