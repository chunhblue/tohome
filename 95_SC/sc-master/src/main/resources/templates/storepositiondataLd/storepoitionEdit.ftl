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
    <script src="${m.staticpath}/[@spring.message 'js.storepositionEdit'/]"></script>
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
</head>

<body>

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--页头-->
[@common.header][/@common.header]
<!--导航-->
[@common.nav "HOME&Operation&Role Management"][/@common.nav]
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
                            <div class="form-horizontal ">
                                <div class="form-group ">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-4 control-label">Business Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input id="convert_date" readonly="true" nextele="sell_end_date" placeholder="Business Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Business Code</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="dj_cd" class="form-control input-sm">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-4 control-label">Top Department</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="dj_status">
                                            <option value="01">Purchasing Department</option>
                                        </select>
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Expenditure Type</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="dj_status">
                                            <option value="01">Card</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-4 control-label">Expenditure Amount</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="dj_cd" class="form-control input-sm">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Payment Type</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="dj_status">
                                            <option value="01">AliPay</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-4 control-label">Expenditure Status</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="dj_status">
                                            <option value="01">Pending</option>
                                        </select>
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Operator</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="dj_cd" class="form-control input-sm" value="张三 13476544464">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-4 control-label">Expenditure Description</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-5">
                                        <input type="text" id="remarks" class="form-control input-sm">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-4 control-label">Expenditure Remarks</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-5">
                                        <input type="text" id="approver" class="form-control input-sm">
                                    </div>
                                </div>
                            </div>
                        </div>
                        [#--<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
                        [#--<div class="wire"></div>--]
                        [#--<button id="search_btn" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>--]

                        [#--										<button id='add' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-plus'></span> 新 增</button>--]
                        [#--										<button id='up' type='button' class='btn  btn-warning   btn-sm ' ><span class='glyphicon glyphicon-pencil'></span> 修 改</button>--]
                        [#--</div>--]
                    </div>
                </div>
            </div><!--box end-->
        </div>
    </div>

    <!--row 分割-->
    [#--<div class="row">--]
    [#--<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
    [#--<table id="zgGridTtable"></table>--]
    [#--</div>--]
    [#--</div>--]
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
            <button id="submit" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div><!--row 分割-->
</div>

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
</body>
</html>
