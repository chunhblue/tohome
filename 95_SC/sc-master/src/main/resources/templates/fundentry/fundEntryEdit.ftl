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
    <script src="${m.staticpath}/[@spring.message 'js.fundEntryEdit'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
    </script>
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
            padding-right: 35px;
        }
        .good-select .item-select-but {
            width: 30px;
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
            margin-left:10px;
        }
        span.my-text{
            margin-right:20px;
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
    </STYLE>
    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--导航-->
[@common.nav "HOME&Operation&Expenditure Entry"][/@common.nav]
<div class="container-fluid" id="main_box">

    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">
                        Basic Information
                    </div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
                            <div class="form-horizontal " id="ExpenditureForm">
                                <div class="form-group ">
                                    <label for="storeName" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Store</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" id="storeName" name="StoreName" class="form-control my-automatic input-sm" autocomplete="off">
                                            <a id="refreshIcon"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="clearIcon"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>

                                    <label for="businessDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Business Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        [#--										<input id="businessDate" readonly="true" placeholder="Business Date" class="form-control input-sm select-date" type="text" value="">--]
                                        <input id="businessDate" name="BusinessDate" placeholder="Business Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                </div>

                                <div class="form-group ">
                                    <label for="expenditureNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Expenditure No.</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="expenditureNo" name="ExpenditureNo" class="form-control input-sm" maxlength="20">
                                    </div>

                                    <label for="department" class="col-xs-12 col-sm-2 col-md-2 col-lg2 control-label not-null">Department</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="department" class="form-control input-sm" name="Department">
                                            <option value="">-- All/Please Select --</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="paymentType" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Payment Type</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="paymentType" class="form-control input-sm" name="PaymentType">
                                            <option value="">-- All/Please Select --</option>
                                        </select>
                                    </div>

                                    <label for="expenditureSubject" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Expenditure Subject</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="expenditureSubject" class="form-control input-sm" name="ExpenditureSubject">
                                            <option value="">-- All/Please Select --</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="expenseType" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Expense Type</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="expenseType" class="form-control input-sm" name="expenseType">
                                            <option value="">-- All/Please Select --</option>
                                            <option value="04">Additional</option>
                                            <option value="05">Offset Claim</option>
                                        </select>
                                    </div>

                                    <label for="expenditureAmt" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Expenditure Amount</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="expenditureAmt" class="form-control input-sm" name="ExpenditureAmt">
                                    </div>
                                </div>

                                <div class="form-group">
[#--                                    <label for="expenditureStatus" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Expenditure Status</label>--]
[#--                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--                                        <select id="expenditureStatus" class="form-control input-sm" name="ExpenditureStatus">--]
[#--                                            <option value="">-- All/Please Select --</option>--]
[#--                                        </select>--]
[#--                                    </div>--]

                                    <label for="operator" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Operator</label>[#-- 店铺 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="operator" disabled="disabled">
                                            <a id="operatorRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="operatorRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="description" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Expenditure Description</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-6">
                                        <input type="text" id="description" class="form-control input-sm">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="remarks" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Expenditure Remarks</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-6">
                                        <input type="text" id="remarks" class="form-control input-sm">
                                    </div>
                                    [#--附件一览--]
                                    <button id="attachments" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon glyphicon-file"></span>Attachments</button>[#--附件--]
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--box end-->
        </div>
    </div>

    <!--row 分割-->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
            <button id="submitBtn" type="button" class="btn btn-primary returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
            <button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div><!--row 分割-->
</div>
<!--审核-->
[@common.audit][/@common.audit]
<!--附件一览-->
[@common.attachments][/@common.attachments]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="addFlag" value="0"/>
<input type="hidden" id="viewSts" value="${viewSts!}"/>
<input type="hidden" id="_storeCd" value="${data.storeCd!}">
<input type="hidden" id="_accDate" value="${data.accDate!}"/>
<input type="hidden" id="_voucherNo" value="${data.voucherNo!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="_url" value=""/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="hidStore" value=""/>
</body>
</html>
