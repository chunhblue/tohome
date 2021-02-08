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
	<script src="${m.staticpath}/[@spring.message 'js.stocktakeQueryEdit'/]"></script>
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
[@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Stocktake&Returning to Vendor Request Note Detail"][/@common.nav]
<div class="container-fluid" id="main_box">
	<button id="search" type="button" class="hidden"></button>
	<!--row 分割-->
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<table id="zgGridTtable"></table>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<button id="saveBut" type="button" class="btn btn-default saveBut"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>

<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Item Information</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">Barcode</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="barcode" readonly="true" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">Item Code</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="article_id" readonly="true" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">Item Name</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="article_name" readonly="true" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">UOM</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="uom" readonly="true" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">Spec</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="spec" readonly="true" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
					[#--<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">Position Code</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="area_cd" readonly="true" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>--]
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">First Stocktake Qty</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="first_qty" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="e_iyDpt" class="col-sm-2 col-lg-4 control-label">Second Stocktake Qty</label>
						<div class="col-sm-6">
							<div class="aotu-pos">
								<input type="text" id="second_qty" class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<input type="hidden" id="e_id" value="" />
				<button id="close" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
				<button id="submit" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>

	<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="piCd" value="${piCd!}"/>
<input type="hidden" id="piDate" value="${piDate!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>

<input type="hidden" id="flag" value="${flag!}">
<input type="hidden" id="userId" value="${userId!}">
[#--<input type="hidden" id="list" value="${searchList}">--]
<input type="hidden" id="unitId">
</body>
</html>
