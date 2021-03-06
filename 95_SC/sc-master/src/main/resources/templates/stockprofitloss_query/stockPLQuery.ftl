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
	<script src="${m.staticpath}/[@spring.message 'js.stocktakePLQ'/]"></script>
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
[@common.nav "HOME&Stocktaking&Stocktaking Query"][/@common.nav]
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
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="sd_date" readonly="true" placeholder="Start Date" class="form-control input-sm select-date" type="text">
										</div>

										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Query Items</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<div class="radio-inline"><label><input type="radio" name="order_type" id="order_type_1"value="1" checked="checked">All</label></div>
											<div class="radio-inline"><label><input type="radio" name="order_type" id="order_type_2" value="2"/>Stock</label></div>
											<div class="radio-inline"><label><input type="radio" name="order_type" id="order_type_2" value="2"  />NoStock</label></div>
										</div>

										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Difference Qty more than</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="allocation_code" class="form-control input-sm">
										</div>

										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Difference Amt more than</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="allocation_code" class="form-control input-sm">
										</div>
									</div>
									<div class="form-group">
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Top Department</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="dep" class="form-control input-sm">
											</select>
										</div>
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="pma" class="form-control input-sm">
											</select>
										</div>
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Category Name</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="cat" class="form-control input-sm">
											</select>
										</div>
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sub-category Name</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="scat" class="form-control input-sm">
											</select>
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
				<table id="zgGridTtable"></table>
			</div>
		</div>
	</div>

[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>



</body>
</html>
