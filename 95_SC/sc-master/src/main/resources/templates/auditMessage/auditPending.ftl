[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.auditPending'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE  9]>--]


    [@common.header][/@common.header]
</head>
<!--导航-->
[@common.nav "HOME&Message&Pending Approvals"][/@common.nav]
<div class="container-fluid" id="main_box">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">
                        Query Condition
                    </div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="typeId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Approval Type</label>[#--审核类型--]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <select id="typeId" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
                                    </div>

                                    [#--隐藏 start--]
                                    <label for="userCode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" hidden>Submitter Code</label>[#--发起人code--]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control input-sm" id="userCode" placeholder="" >
                                        </div>
                                    </div>
                                    [#--隐藏 end --]

                                    <label for="userName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Created By</label>[#--发起人姓名--]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control input-sm" id="userName" placeholder="" >
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Date Created</label>[#--发起日期--]
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="startDate" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap"></label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input  id="endDate" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
[#--                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-1">--]
[#--                                        <button id="clear_date" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear</button>--]
[#--                                    </div>--]
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                            <button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
                        </div>
                    </div>
                </div>
            </div><!--box end-->
        </div>
    </div>
    <!--row 分割-->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <table id="zgGridTtable"></table>
        </div>
    </div>
</div>
<!--审核-->
[@common.audit][/@common.audit]
<!--审核记录-->
[@common.approvalRecords][/@common.approvalRecords]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="toKen" value="${toKen!}"/>
[@permission code="SC-INF-AUDIT-001"]<input type="hidden" class="permission-verify" id="auditBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-INF-AUDIT-002"]<input type="hidden" class="permission-verify" id="viewBut" value="${displayVar!}" />[/@permission]
</body>
</html>
