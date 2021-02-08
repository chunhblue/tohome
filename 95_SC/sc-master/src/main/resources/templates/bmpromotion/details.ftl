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
    <script src="${m.staticpath}/[@spring.message 'js.promotionDetails'/]"></script>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]

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
        .fieldsetModule{
            margin:5px;
            padding: 10px;
            border:1px solid #CCCCCC;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -khtml-border-radius: 5px;
        }
        .legendTitle{
            margin:0;
            font-size:12px;
            border-bottom:none;
            text-align:center;
        }
        label.radio{
            margin-left:20px;
        }
	</STYLE>
<!--页头-->
[@common.header][/@common.header]
</head>

<body>

[#--   <div class="error-pcode" id="error_pcode">--]
[#--      <div class="error-pcode-text"> ${useMsg!} </div>--]
[#--      <div class="shade"></div>--]
[#--   </div>--]
	<!--导航-->
	[@common.nav "HOME&Master&MM Promotion Details"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">
						    MM Promotion Details
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
								    <fieldset class="fieldsetModule" style="border-style:none;">
                                        <div class="form-group">
                                            <label for="department" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Top Department</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="department" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="regionCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Approval Region</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="regionCd" readonly="true" class="form-control" type="text" value="">
                                            </div>

                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="promotionCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Code</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="promotionCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="promotionName" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Theme</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="promotionName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="promotionStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="promotionStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="promotionEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="promotionEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="promotionStartTime" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Start Time</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="promotionStartTime" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="promotionEndTime" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion End Time</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="promotionEndTime" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Schedule</label>
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-10">
                                                <label for="monday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="monday" disabled="disabled" type="checkbox">
                                                    Monday
                                                </label>
                                                <label for="tuesday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="tuesday" disabled="disabled" type="checkbox">
                                                    Tuesday
                                                </label>
                                                <label for="wednesday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="wednesday" disabled="disabled" type="checkbox">
                                                    Wednesday
                                                </label>
                                                <label for="thursday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="thursday" disabled="disabled" type="checkbox">
                                                    Thursday
                                                </label>
                                                <label for="friday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="friday" disabled="disabled" type="checkbox">
                                                    Friday
                                                </label>
                                                <label for="saturday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="saturday" disabled="disabled" type="checkbox">
                                                    Saturday
                                                </label>
                                                <label for="sunday" class="col-xs-6 col-sm-4 col-md-4 col-lg-1 checkbox">
                                                    <input id="sunday" disabled="disabled" type="checkbox">
                                                    Sunday
                                                </label>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="promotionPromptFlgC" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Reminder</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-1">
                                                <select id="promotionPromptFlgC" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                            <label for="promotionPromptName" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">MM Promotion Reminder Description</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="promotionPromptName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-1 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:140px;">MM Promotion Setting</legend>

                                        <div class="form-group">
                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Pattern</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionPattern" disabled="disabled" type="radio" value="01"/>
                                                A</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionPattern" disabled="disabled" type="radio" value="02"/>
                                                A+B</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionPattern" disabled="disabled" type="radio" value="03"/>
                                                A+B+C</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionPattern" disabled="disabled" type="radio" value="04"/>
                                                Spend and Save</label>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionType" disabled="disabled" type="radio" value="1"/>
                                                Amount</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionType" disabled="disabled" type="radio" value="2"/>
                                                Discount Amount</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionType" disabled="disabled" type="radio" value="3"/>
                                                Discount Rate</label>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">MM Promotion Discount Distribution Ratio Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionRatioType" disabled="disabled" type="radio" value="1"/>
                                                Manually Set Ratio</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionRatioType" disabled="disabled" type="radio" value="2"/>
                                                Automatically Set Ratio</label>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <label class="radio">
                                                    <input name="promotionRatioType" disabled="disabled" type="radio" value="3"/>
                                                No Distribution</label>
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-2 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:200px;">MM Promotion Condition Setting</legend>
                                        <div class="form-group" style="margin:2px;">
                                            <table id="conditionGridTable"></table>
                                        </div>
                                    </fieldset><!-- fieldset-3 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:270px;">MM Promotion Items and Category Setting</legend>

                                        <ul class="nav nav-tabs">
                                            <li class="active"><a href="#card1" data-toggle="tab">MM Promotion Item Setting</a></li>
                                            <li><a href="#card2" data-toggle="tab">MM Promotion Category Setting</a></li>
                                            <li><a href="#card3" data-toggle="tab">MM Promotion Brand Setting</a></li>
                                        </ul>

                                        <div class="tab-content" style="border-width:0 1px 1px 1px;border-style:solid;border-color:#CCCCCC;padding:10px;height:200px;">
                                            <div class="tab-pane active" id="card1">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="itemGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-1 -->

                                            <div class="tab-pane" id="card2">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="categoryGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-2 -->

                                            <div class="tab-pane" id="card3">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="brandGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-3 -->
                                        </div>
                                    </fieldset><!-- fieldset-4 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:180px;">Store Promotion Area Setting</legend>

                                        <ul class="nav nav-tabs">
                                            <li class="active"><a href="#card4" data-toggle="tab">Promotion Area</a></li>
                                            <li><a href="#card5" data-toggle="tab">Add or exclude exceptions to stores</a></li>
                                        </ul>

                                        <div class="tab-content" style="border-width:0 1px 1px 1px;border-style:solid;border-color:#CCCCCC;padding:10px;height:200px;">
                                            <div class="tab-pane active" id="card4">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="areaGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-1 -->

                                            <div class="tab-pane" id="card5">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="exceptionGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-2 -->
                                        </div>
                                    </fieldset><!-- fieldset-5 -->
								</div>
							</div>
						</div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
                                <button id="back" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
                            </div>
                        </div><!--row 分割-->
					</div>
				 </div><!--box end-->
			 </div>
		 </div>
		 <!--row 分割-->
        <div id="details_dialog" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-horizontal" >
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <div id="categoryExDiv" class="form-group" style="margin:5px;">
                                        <table id="categoryExDetailsTable"></table>
                                    </div><!-- form-group -->
                                    <div id="brandExDiv" class="form-group" style="margin:5px;">
                                        <table id="brandExDetailsTable"></table>
                                    </div><!-- form-group -->
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" id="e_id" value="" />
                        <button id="dialog_cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
                    </div>
                </div>
            </div>
        </div>
	 </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="_promotionCd" value="${promotionCd!}"/>
	<input type="hidden" id="searchJson" value=""/>
</body>
</html>
