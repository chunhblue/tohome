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
          user-scalable=no/>
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet"
          type="text/css"/>
[#--<link href="${m.staticpath}/[@spring.message 'css.actorassignment'/]" rel="stylesheet"--]
[#--type="text/css"/>--]
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.actorassignment'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
[#--[if lt IE 9]>--]
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
[#--<![endif]--]
    <script>
    </script>
    <STYLE>

    </STYLE>
</head>

<body>
<!--页头-->
	[@common.header][/@common.header]
<!--导航-->
[#--	[@common.nav "HOME&Privilege management&特殊角色授权"][/@common.nav]--]
	[@common.nav "HOME&Privilege&Special User Privilege Management"][/@common.nav]
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">Query Condition</div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="iyPost"
                                           class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Privilege Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="roleName" placeholder=""/>
                                            <a id="" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="iyOccup"
                                           class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">User ID</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text"
                                                   class="form-control my-automatic input-sm"
                                                   id="userId" placeholder=""/>
                                            <a id="" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="iyDpt"
                                           class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Authorization Type</label>
                                    <div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">

                                        <div class="radio-inline" id="">
                                            <label>
                                                <input type="radio" name="assType"
                                                       checked="checked"
                                                       value="0"/>
                                                Normal
                                            </label>
                                        </div>
                                        [#if isDS == 1]
                                        <div class="radio-inline" id="">
                                            <label>[#--代审--]
                                                <input type="radio" name="assType" value="1"/>
                                                Pending trial
                                            </label>
                                        </div>
                                        [/#if]
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <button id="search" type="button"
                                        class="btn btn-primary btn-sm"><span
                                        class="glyphicon glyphicon-search icon-right"></span>Inquire
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--row 分割-->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
            <table id="zgGridTtable"></table>
        </div>
    </div>
    <!--row 分割-->
    <div id="edit_dialog" class="modal fade bs-example-modal-md" data-backdrop="static"
         tabindex="-1"
         role="dialog" aria-labelledby="">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Edit</h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label for="e_iyPost" class="col-sm-3 control-label">Authorization Type</label>[#--授权类型--]
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <div class="radio-inline" id="">
                                        <label>
                                            <input type="radio" name="d_assType" id="d_assType0"
                                                   checked="checked"
                                                   value="0"/>
                                            Normal
                                        </label>
                                    </div>
                                    [#if isDS == 1]
                                    <div class="radio-inline" id="">
                                        <label>
                                            <input type="radio" name="d_assType" id="d_assType1"
                                                   value="1"/>
                                            Pending trial
                                        </label>
                                    </div>
                                    [/#if]
                                </div>
                            </div>
                        </div>
                        [#if isSysDS == 1]
                        <div class="form-group" id="p_user" style="display: none;">
                            <label for="e_iyPost"
                                   class="col-sm-3 control-label not-null">Assignor</label>[#--授权用户--]
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text"
                                           class="form-control my-automatic input-sm"
                                           id="p_userId" name="p_userId" placeholder=""
                                           required/>
                                    <a id="" href="javascript:void(0);" title="Refresh"
                                       class="auto-but glyphicon glyphicon-refresh refresh a-up-hide"></a>
                                    <a id="" href="javascript:void(0);" title="Clear"
                                       class="auto-but glyphicon glyphicon-remove circle a-up-hide"></a>
                                </div>
                            </div>
                        </div>
                        [/#if]
                        <div class="form-group">
                            <label for="e_iyPost"
                                   class="col-sm-3 control-label not-null">Asignee</label>[#--被授权用户--]
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text"
                                           class="form-control my-automatic input-sm"
                                           id="d_userId" name="d_userId" placeholder=""
                                           required/>
                                    <a id="" href="javascript:void(0);" title="Refresh"
                                       class="auto-but glyphicon glyphicon-refresh refresh a-up-hide"></a>
                                    <a id="" href="javascript:void(0);" title="Clear"
                                       class="auto-but glyphicon glyphicon-remove circle a-up-hide"></a>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="e_iyOccup"
                                   class="col-sm-3 control-label not-null">Privilege</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text"
                                           class="form-control my-automatic input-sm not-null"
                                           id="d_roleId" name="d_roleId" placeholder=""
                                           required/>
                                    <a id="" href="javascript:void(0);" title="Refresh"
                                       class="auto-but glyphicon glyphicon-refresh refresh a-up-hide"></a>
                                    <a id="" href="javascript:void(0);" title="Clear"
                                       class="auto-but glyphicon glyphicon-remove circle a-up-hide"></a>
                                </div>
                            </div>
                        </div>

                        <div class="wire" id="blueline" style="display: none;"></div>
                        <div class="form-group" style="display: none;" id="startDateDiv">
                            <label for="e_iyDpt"
                                   class="col-sm-3 control-label not-null">Start Date</label>[#--代审开始日期--]
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text"
                                           class="form-control my-automatic input-sm"
                                           id="startDate" name="startDate"
                                           required/>

                                </div>
                            </div>
                        </div>
                        <div class="form-group " style="display: none;" id="endDateDiv">
                            <label for="e_role"
                                   class="col-sm-3 control-label not-null">End Date</label>[#--代审结束日期--]
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text"
                                           class="form-control my-automatic input-sm"
                                           id="endDate" name="endDate"
                                           required/>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="e_id" value=""/>
                    <button id="affirm" type="button" class="btn btn-primary btn-sm"><span
                            class="glyphicon glyphicon-ok icon-right"></span>Submit
                    </button>
                    <button id="cancel" type="button" class="btn btn-default btn-sm"><span
                            class="glyphicon glyphicon-remove icon-right"></span>Cancel
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!--页脚-->
	[@common.footer][/@common.footer]
    <input type="hidden" id="toKen" value="${toKen}"/>
	[@permission code="P-UAAS-002"]<input type="hidden" id="editPer"
                                          value="${displayVar!}"/>[/@permission]
	[@permission code="P-UAAS-003"]<input type="hidden" id="cancelPer"
                                          value="${displayVar!}"/>[/@permission]
</body>
</html>
