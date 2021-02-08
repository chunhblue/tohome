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
	[#--<script src="${m.staticpath}/[@spring.message 'js.vendorReceipt'/]"></script>--]
	<script src="${m.staticpath}/[@spring.message 'js.invoiceEntryList'/]"></script>
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

	<!--页头-->
[@common.header][/@common.header]
	<!--导航-->
[@common.nav "HOME&Sale&Invoice Query"][/@common.nav]
<div class="container-fluid" id="main_box">
	<!--row 分割-->
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
									<label for="aRegion" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Region</label>[#-- 大区 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aRegion">
											<a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for="aCity" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">City</label>[#-- 城市 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aCity">
											<a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for="aDistrict" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">District</label>[#-- 区域 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aDistrict">
											<a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>[#-- 店铺 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aStore">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="searchStartDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="searchStartDate" nextele="sell_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for="searchEndDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null"></label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input  id="searchEndDate" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
									</div>
[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-1">--]
[#--										<button id="clearDate" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear Date</button>--]
[#--									</div>--]
								</div>
								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Customer's  Name</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="searchCustomer" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Telephone Number</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="searchTelephone" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Status</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="searchStatus" class="form-control input-sm" nextele="dj_status">
											<option value="">-- All/Please Select --</option>
										</select>
									</div>
								</div><!-- form-group -->
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

	<!--页脚-->
</div>

[#--	<div id="dialogBox" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="" style="width: 1000px;margin: 0 auto">--]
[#--		<div class="modal-dialog modal-lg" role="document" style="width: 100%">--]
[#--			<div class="modal-content">--]
[#--				<div class="modal-header">--]
[#--					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--]
[#--					<h4 class="modal-title" id="myModalLabel">Detail</h4>--]
[#--				</div>--]

[#--				<div class="modal-body">--]
[#--					<div class="form-horizontal">--]
[#--						--][#--Customer’s  name--]
[#--						<div class="form-group">--]
[#--							<label for="i_cashierId"  class="col-sm-3 control-label">Customer’s  name</label>--]
[#--							<div class="col-sm-5">--]
[#--								<div class="aotu-pos">--]
[#--									<input type="text" class="form-control my-automatic input-sm" id="customer" placeholder="">--]
[#--								</div>--]
[#--							</div>--]
[#--						</div>--]

[#--						--][#--Company’s Name--]
[#--						<div class="form-group">--]
[#--							<label for="i_cashierId"  class="col-sm-3 control-label">Company’s name</label>--]
[#--							<div class="col-sm-5">--]
[#--								<div class="aotu-pos">--]
[#--									<input type="text" class="form-control my-automatic input-sm" id="company" placeholder="">--]
[#--								</div>--]
[#--							</div>--]
[#--						</div>--]

[#--						--][#--TAX CODE--]
[#--						<div class="form-group">--]
[#--							<label for="i_cashierId"  class="col-sm-3 control-label">TAX CODE</label>--]
[#--							<div class="col-sm-5">--]
[#--								<div class="aotu-pos">--]
[#--									<input type="text" class="form-control my-automatic input-sm" id="taxCode" placeholder="">--]
[#--								</div>--]
[#--							</div>--]
[#--						</div>--]

[#--						--][#--Address--]
[#--						<div class="form-group">--]
[#--							<label for="i_cashierId"  class="col-sm-3 control-label">Address</label>--]
[#--							<div class="col-sm-5">--]
[#--								<div class="aotu-pos">--]
[#--									<input type="text" class="form-control my-automatic input-sm" id="address" placeholder="">--]
[#--									<span>(which register with the Tax government</span>--]
[#--								</div>--]
[#--							</div>--]
[#--						</div>--]

[#--						--][#--Telephone Number--]
[#--						<div class="form-group">--]
[#--							<label for="i_cashierId"  class="col-sm-3 control-label">Telephone Number</label>--]
[#--							<div class="col-sm-5">--]
[#--								<div class="aotu-pos">--]
[#--									<input type="text" class="form-control my-automatic input-sm" id="telephone" placeholder="">--]
[#--								</div>--]
[#--							</div>--]
[#--						</div>--]

[#--						--][#--Telephone Number--]
[#--						<div class="form-group">--]
[#--							<label class="col-sm-3 control-label">E-mail</label>--]
[#--							<div class="col-sm-5">--]
[#--								<div class="aotu-pos">--]
[#--									<input type="text" class="form-control my-automatic input-sm" id="email" placeholder="">--]
[#--								</div>--]
[#--							</div>--]
[#--						</div>--]

[#--						--][#--消费头档列表--]
[#--						<div class="form-group" style="width: 800px; margin: 0 auto">--]
[#--							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--								<table id="receiptGridTable"></table>--]
[#--							</div>--]
[#--						</div>--]
[#--					</div>--]
[#--				</div>--]
[#--				<div class="modal-footer">--]
[#--					<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>--]
[#--					<button id="invoiceDetialBtn" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Issue Invoice</button>--]
[#--					<button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>--]
[#--				</div>--]
[#--			</div>--]
[#--		</div>--]
[#--	</div>--]

[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="editPer" value="" />
	[@permission code="SC-INVOICE-001"]<input type="hidden" class="permission-verify" id="addButPm" value="${displayVar!}" />[/@permission]
	[@permission code="SC-INVOICE-002"]<input type="hidden" class="permission-verify" id="viewButPm" value="${displayVar!}" />[/@permission]
	[@permission code="SC-INVOICE-003"]<input type="hidden" class="permission-verify" id="issueButPm" value="${displayVar!}" />[/@permission]
</body>
</html>
