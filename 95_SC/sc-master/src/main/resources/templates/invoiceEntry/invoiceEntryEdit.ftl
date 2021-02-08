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
	<script src="${m.staticpath}/[@spring.message 'js.invoiceEntryEdit'/]"></script>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	[#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
	[#--<![endif]--]
	<script>
	</script>
	<STYLE>
		/*.box div.radio-inline {*/
		/*	margin-left: 0;*/
		/*	padding-left: 0;*/
		/*}*/
		/*.box div.radio-inline label {*/
		/*	padding-left: 20px;*/
		/*	position: relative;*/
		/*}*/
		/*.good-select .form-control {*/
		/*	padding-right: 35px;*/
		/*}*/
		/*.good-select .item-select-but {*/
		/*	width: 30px;*/
		/*	right: 1px;*/
		/*}*/
		/*.my-user-info{*/
		/*	padding:10px;*/
		/*	overflow:hidden;*/
		/*}*/
		/*span.my-label,*/
		/*span.my-text{*/
		/*	float:left;*/
		/*	color: #333333;*/
		/*	font-size:12px;*/
		/*	margin-left:10px;*/
		/*}*/
		/*span.my-text{*/
		/*	margin-right:20px;*/
		/*}*/
		/*span.my-label{*/
		/*	font-weight: 900;*/
		/*}*/
		/*.my-div{*/
		/*	overflow:hidden;*/
		/*	float:left;*/
		/*}*/
		/*.del-alert{*/
		/*	display:none;*/
		/*}*/
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
[@common.nav "HOME&Sale&Invoice Entry Detils"][/@common.nav]
<div class="container-fluid" id="main_box">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Basic Information
					</div>
				</div>
				<div class="box-body box-body-padding-10">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group ">
									<label class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Customer’s name</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" class="form-control my-automatic input-sm" id="customerName" placeholder="">
									</div>

									<label for="expenditureNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Company’s name</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="companyName" class="form-control input-sm">
									</div>
								</div>

								<div class="form-group">
									<label for="department" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">TAX CODE</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="tax" class="form-control input-sm">
									</div>

									<label for="expenditureSubject" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Address</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" class="form-control input-sm" id="address" placeholder="">
									</div>
									<span>(which register with the Tax government)</span>
								</div>

								<div class="form-group">
									<label for="expenditureAmt" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Telephone Number 1</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="phone" class="form-control input-sm">
									</div>

									<label for="paymentType" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">E-mail 1</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="email" class="form-control input-sm">
									</div>
								</div>
								<div class="form-group">
									<label for="expenditureAmt" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Telephone Number 2</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="phone2" class="form-control input-sm">
									</div>

									<label for="paymentType" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">E-mail 2</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="email2" class="form-control input-sm">
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div><!--box end-->
		</div>
	</div>
	<div class="row"></div>
	<div class="row"></div>
[#--	<div class="wire"></div>--]
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title" id="searchForm">
					<div class="tt">
						Query Condition
					</div>
				</div>
				<div class="box-body box-body-padding-10" id="box">
					<div class="row" id="searchFormJson" >
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group ">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space: nowrap">Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="startDate" nextele="sell_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
									</div>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input  id="endDate" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space: nowrap">Store No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos good-select">
											<input type="text" id="storeNo" class="form-control input-sm" style="overflow:scroll;text-overflow:ellipsis;">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">The Receipt Number</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos good-select">
											<input type="text" id="searchReceiptNo" class="form-control input-sm">
										</div>
									</div>
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
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
				<table id="zgGridTtable"></table>
			</div>
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
				<table id="receiptGridTable"></table>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			[#--<span class="glyphicon glyphicon-ok icon-right"></span>--]
			<button id="saveBut" type="button" class="btn btn-default">Submit</button>
			<button id="issueBut" type="button" class="btn btn-default">Issue Invoice</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->

	<div class="row"></div>
	<div class="row"></div>
	<div class="wire"></div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="form-horizontal">
				<div class="form-group ">
					<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Submit</label>
					<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
						<div class="aotu-pos good-select">
							<input type="text" id="submitter" readonly="readonly" class="form-control input-sm">
						</div>
					</div>

					<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Date</label>
					<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
						<div class="aotu-pos good-select">
							<input type="text" id="submitDate" readonly="readonly" class="form-control input-sm">
						</div>
					</div>

					<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Issue</label>
					<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
						<div class="aotu-pos good-select">
							<input type="text" id="issuer" readonly="readonly" class="form-control input-sm">
						</div>
					</div>

					<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Date</label>
					<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
						<div class="aotu-pos good-select">
							<input type="text" id="issueDate" readonly="readonly" class="form-control input-sm">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="enterFlag" value="${enterFlag!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="enterFlag" value="${enterFlag!}"/>
<input type="hidden" id="accIdParam" value="${accId!}"/>
<input type="hidden" id="storeNoParam" value="${storeNo!}"/>
</body>
</html>
