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
	<script src="${m.staticpath}/[@spring.message 'js.maItemSaleEdit'/]"></script>
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
[@common.nav "HOME&Sale&Material Item Sales Detail"][/@common.nav]
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
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for="voucher_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="voucher_cd" class="form-control input-sm">
									</div>

									<label for="sale_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Selling Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="sale_date" readonly="true" nextele="sell_end_date" placeholder="Sale Date" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for="store_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="store_name" class="form-control input-sm">
									</div>

									<label for="barcode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="barcode" class="form-control input-sm">
									</div>
								</div>
								<div class="form-group">
									<label for="formulaArticleId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Formula Item Code</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="formulaArticleId" class="form-control input-sm">
									</div>
									<label for="formulaArticleName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Formula Item Name</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="formulaArticleName" class="form-control input-sm">
									</div>
									<label for="tranSerialNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Tran Serial No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="tranSerialNo" class="form-control input-sm">
									</div>
									<label for="posId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">POS ID</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="posId" class="form-control input-sm">
									</div>
								</div>
								<div class="form-group">
									<label for="saleQty"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sales Qty</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="saleQty" class="form-control input-sm" style="text-align: right">
									</div>
									<label for="saleAmt"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sales Amount</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" readonly="true" id="saleAmt" class="form-control input-sm" style="text-align: right">
									</div>
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
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<table id="zgGridTtable"></table>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="voucherCd" value="${voucherCd!}"/>
</body>
</html>
