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
	<script src="${m.staticpath}/[@spring.message 'js.cashierAmount'/]"></script>
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
		.res-info {
			margin-top: 7px;
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
<!--导航  收银员收款金额登录-->
[@common.nav "HOME&Sale&Cash Balancing Entry"][/@common.nav]
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
								<div class="form-group ">
									<label for="a_store" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Store</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="a_store" placeholder="" autocomplete="off">
											<a id="a_store_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="a_store_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Business Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="bs_date"  placeholder="Business Date" class="form-control input-sm" type="text" value="">
									</div>
									<label for="a_cashier" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" hidden>Cashier</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="a_cashier" placeholder="" autocomplete="off">
											<a id="a_cashier_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="a_cashier_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>

									<label for="" hidden class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Cash split</label>[#--现金是否拆分--]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
										<div class="radio-inline">
											<label>
												<input type="radio" name="cash_split_flag" checked="checked" id="cash_split_flag_1" value="1">
												Yes
											</label>
										</div>
										<div class="radio-inline">
											<label>
												<input type="radio" name="cash_split_flag" id="cash_split_flag_2" value="0"/>
												No
											</label>
										</div>
									</div>
								</div>
								<div class="form-group" hidden>

									<label for="shift" hidden class="col-Query Resultxs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Shift</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
										<select id="shift" class="form-control input-sm" nextele="">
											<option value="">-- All/Please Select --</option>
											<option value="00">Shift1</option>
											<option value="01">Shift2</option>
											<option value="02">Shift3</option>
										</select>
									</div>
									<label for="posId" hidden class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">POS No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" hidden>
										<select id="posId" class="form-control input-sm" nextele="">
											<option value="">-- All/Please Select --</option>
										</select>
									</div>

								</div><!-- form-group -->
								<div class="form-group">
									<label for="remark"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Remarks</label>
									<div class="col-xs-12 col-sm-4 col-md-6 col-lg-6">
										<input type="text" id="remark" class="form-control input-sm">
									</div>
								</div><!-- form-group -->
								<div class="form-group">
                                <div id="resExpend">
									<div  id="expendlitLine"></div>
									<div id="expenditureNoDiv">

								    <div id="expenditureNoA" style="margin-left:5px">
									<label for="expenditureNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" style="margin-left:-2px">Expenditure No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="expenditureNo" placeholder="" autocomplete="off"  style="margin-left:-3px">

											<a id="expenditureNo_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="expenditureNo_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for="description" class="col-xs-12 col-sm-4 col-md-3 col-lg-2 control-label" style="white-space: nowrap;margin-left:5px;">Expenditure Description</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" style="margin-left:-5px">
										<input type="text" id="description"  readonly="true" class="form-control input-sm">
									</div>
									<div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
										<div class="col-sm-1" style="margin-left:10px">
											<a class="btn btn-default btn-sm" href="javascript:void(0);" role="button" id="addexpenditureNo">
												<div class="glyphicon glyphicon-plus"></div>
											</a>
										</div>
									</div>

										</div>
									</div>
								</div>
								    <div class="form-group">
									<input type="number" id="expendFlgVal" hidden/>
								    </div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="wire"></div>
							<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok"></span>Confirm</button>
							<button id='reset' type='button' class='btn  btn-primary btn-sm ' ><span class='glyphicon glyphicon-refresh'></span>Reset</button>
						</div>
					</div>
				</div>
			</div><!--box end-->
		</div>
	</div>
	<!--row 分割-->
</div>
	<div class="row">
			<div id="choose1">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-7">
					<table id="zgGridTtable"></table>
				</div>
			</div>
			<div  id="choose2">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-5" id="zgGridTtable1Div">
					<table id="zgGridTtable1"></table>
				</div>
			</div>
	</div>

	<div class="row">
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-10 text-center">
		<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-refresh icon-right"></span>Back</button>
		<button id="submitBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
	</div>
</div><!--row 分割-->

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="businessDate" value="${date!}"/>
<input type="hidden" id="viewSts" value="${viewSts!}"/>
<input type="hidden" id="itemSts" value="0"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="payId" value="${data.payId!}">
<input type="hidden" id="storeCd1" value="${data.storeCd!}"/>
<input type="hidden" id="storeName1" value="${data.storeName!}"/>
<input type="hidden" id="payDate" value="${data.payDate!}"/>
<input type="hidden" id="userId" value="${data.userId!}"/>
<input type="hidden" id="userName" value="${data.userName!}"/>
<input type="hidden" id="shift1" value="${data.shift!}"/>
<input type="hidden" id="posId1" value="${data.posId!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="expenditurehide" hidden>
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="totalShift1" value="">
<input type="hidden" id="totalShift2" value="">
<input type="hidden" id="totalShift3" value="">
<div class="aotu-pos" style="diaplay: none;">
<input id="from_hide_expend" type="hidden" name="expendStr" value=""/>
	<select id="differenceReason" class="form-control input-sm">
		<option value="">-- All/Please Select --</option>
	</select>
</div>
</body>
</html>
