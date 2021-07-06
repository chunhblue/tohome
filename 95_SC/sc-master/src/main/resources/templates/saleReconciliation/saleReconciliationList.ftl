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
	<script src="${m.staticpath}/[@spring.message 'js.saleReconciliation'/]"></script>
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
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none;
        }
        input[type="number"]{
            -moz-appearance: textfield;
        }
		#update{
			display: none;
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
[@common.nav "HOME&Sale&Cashier Balancing Query"][/@common.nav]
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
									<label for="aRegion" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Region</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aRegion">
											<a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									</div>
									<label for="aCity" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">City</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aCity">
											<a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="aDistrict" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">District</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aDistrict">
											<a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									</div>
									<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Store</label>[#-- 店铺 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aStore">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div>
								<div class="form-group" id="searchForm">
[#--									<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space: nowrap">Area Manager</label>--]
[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--										<div class="aotu-pos">--]
[#--											<input type="text" class="form-control my-automatic input-sm not-null" id="am"  required>--]
[#--											<a id="amRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>--]
[#--											<a id="amRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>--]
[#--										</div>--]
[#--									</div>--]
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null" >Business Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label"></label>
										<input id="bs_start_date"  nextele="sale_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
									</div>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label"></label>
										<input  id="bs_end_date"  placeholder="End Date" class="form-control input-sm select-date not-null" name="bs_end_date" type="text" value="" required>
									</div>

[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--										<input  id="bs_end_date" readonly="true" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">--]
[#--										<input  id="bs_end_date"  placeholder="End Date" class="form-control input-sm select-date not-null" name="bs_end_date" type="text" value="" required>--]
[#--									</div>--]
[#--									<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>--]
[#--									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null" >Business Date</label>--]
[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--										--][#--										<input id="bs_start_date" readonly="true" nextele="sale_end_date" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">--]
[#--										<input id="bs_start_date"  nextele="sale_end_date" placeholder="Start Date" name="bs_start_date" class="form-control input-sm select-date not-null" type="text" value="" required>--]
[#--									</div>--]
[#--									--][#--									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label"></label>--]
[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--										--][#--										<input  id="bs_end_date" readonly="true" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">--]
[#--										<input  id="bs_end_date"  placeholder="End Date" class="form-control input-sm select-date not-null" name="bs_end_date" type="text" value="" required>--]
[#--									</div>--]
									<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label " style="white-space: nowrap"></label>
									<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label " style="white-space: nowrap">Area Manager</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm not-null" id="am"  required>
											<a id="amRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="amRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div><!-- form-group -->

								<div class="form-group">
									<label for="payAmtDiff"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Variance Amount</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="payAmtFlg" class="form-control input-sm" nextele="">
											<option value="">-- All/Please Select --</option>
											<option value="1"><</option>
											<option value="2">≤</option>
											<option value="3">></option>
											<option value="4">≥</option>
										</select>
									</div>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="number" id="payAmtDiff" class="form-control input-sm">
									</div>

								</div><!-- form-group -->
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="wire"></div>
							<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
							<button id='reset' type='button' class='btn  btn-primary btn-sm ' ><span class='glyphicon glyphicon-refresh'></span>Reset</button>
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
<div id="saleReconciliation_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">View/Edit</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="payAmtDiff1"  class="col-sm-4 control-label not-null">Difference Amount</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="payAmtDiff1" placeholder="">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="e_id" value="" />
				<button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			</div>
		</div>
	</div>
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="position" value="${position!}">
[@permission code="SC-SL-CF-002"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</body>
</html>
