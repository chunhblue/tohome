[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">


    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.mySubmissions'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE  9]>--]

    <STYLE>
        .box div.radio-inline {
            margin-left: 0;
            padding-left: 0;
        }
        .box div.radio-inline label {
            padding-left: 20px;
            position: relative;
        }
        .good-select .form-control {
            padding-right: 20px;
        }
        .good-select .item-select-but {
            width: 20px;
            right: 1px;
        }
        .my-user-info{
            padding:10px;
            overflow:hidden;
        }
        span.my-label,
        span.my-text{
            float:left;
            color: #333333;
            font-size:12px;
            margin-left:6px;
        }
        span.my-text{
            margin-right:12px;
        }
        span.my-label{
            font-weight: 900;
        }
        .my-div{
            overflow:hidden;
            float:left;
        }
        .del-alert{
            display:none;
        }
        /*修改模态框出现的位置和大小*/
        .modal.fade.in{
            center:150px;
        }
    </STYLE>

    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Message&My Submissions"][/@common.nav]
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
                                    <label for="userCode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" hidden="">Submitter Code</label>[#--发起人code--]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden="">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control input-sm" id="userCode" placeholder="" >
                                        </div>
                                    </div>
                                    [#--隐藏 end--]

                                    <label for="userName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Created By</label>[#--发起人姓名--]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control input-sm" id="userName" placeholder="" >
                                        </div>
                                    </div>
                                    <label for="DocumentNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Document No</label>[#--发起人姓名--]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control input-sm" id="documentNo" placeholder="" >
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
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Approval Status</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <select id="approvalStatus" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
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
<!--撤销-->
<div id="approval_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Withdraw Details</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="auditUserName"  class="col-sm-3 control-label">Withdraw by</label>
                        <div class="col-sm-5">
                            <input type="text" id="auditUserName" readonly="readonly" class="form-control input-sm" value="${Session.IY_MASTER_USER.user.userName!}">
                            <input type="hidden" id="auditUserId" value="${Session.IY_MASTER_USER.user.userId!}">
                        </div>
                    </div>
                    [#--                    <div class="form-group">--]
                    [#--                        <label for="auditTime"  class="col-sm-3 control-label">Time</label>--]
                    [#--                        <div class="col-sm-5">--]
                    [#--                            <input type="text" id="auditTime" readonly="readonly" class="form-control input-sm" value="${.now?string('dd/MM/yyyy hh:mm:ss')}">--]
                    [#--                        </div>--]
                    [#--                    </div>--]
                    [#--隐藏 start--]
                    <div class="form-group" hidden="">
                        <label for="auditStatusGroup"  class="col-sm-3 control-label">Withdraw Result</label>
                        <div class="col-sm-5" id="auditStatusGroup">
                            <div class="radio-inline" id="">
                                <label>
                                    <input type="radio" name="auditStatus" checked="checked" value="2"/>
                                    Withdraw
                                </label>
                            </div>
                        </div>
                    </div>
                    [#--隐藏 end--]
                    <div class="form-group">
                        <label for="auditContent" class="col-sm-3 control-label">Withdraw Reason</label>
                        <div class="col-sm-8">
                            <textarea id="auditContent" class="form-control" rows="5"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input id="auditId" type="hidden">
                <button id="audit_cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
                <button id="audit_affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Confirm</button>
            </div>
        </div>
    </div>
</div>

<!--审核记录-->
[@common.approvalRecords][/@common.approvalRecords]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="toKen" value="${toKen!}"/>
[@permission code="SC-INF-DOCUMENT-001"]<input type="hidden" class="permission-verify" id="withdrawBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-INF-DOCUMENT-002"]<input type="hidden" class="permission-verify" id="viewBut" value="${displayVar!}" />[/@permission]
</body>
</html>