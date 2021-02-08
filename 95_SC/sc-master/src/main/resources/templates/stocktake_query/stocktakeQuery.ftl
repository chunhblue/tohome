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
	<script src="${m.staticpath}/[@spring.message 'js.stocktakeQuery'/]"></script>
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
[@common.nav "HOME&Stocktaking&Stocktaking Plan Entry"][/@common.nav]
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
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktak Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="sd_date" placeholder="Start Date" class="form-control input-sm select-date" type="text">
										</div>

										[#--部门, 大分类--]
										[#--<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Top Department</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="dep" class="form-control input-sm">
											</select>
										</div>
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="pma" class="form-control input-sm">
											</select>
										</div>--]
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="wire"></div>
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
								<button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
								<button id="first_st_complete" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>First Stock Take Complete</button>
								<button id="st_differ_confirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Stock Take Difference Confirm</button>
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
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="box">
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
									<div class="form-group">
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="checkbox" id="query_no_stock"> Query No Stock
										</div>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="checkbox" id="by_position"> Inventory Table To Distinguish Inventory Position
										</div>
										[#--<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Position Code</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="area_cd" class="form-control input-sm">
										</div>--]
										<button id="view" type="button" class="btn btn-info btn-sm"><span class="glyphicon glyphicon-list-alt icon-right"></span>View Stock Take</button>
										<button id="fEntry" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-plus icon-right"></span>First Stock Qty Entry</button>
										<button id="lEntry" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-plus icon-right"></span>Last Stock Qty Entry</button>
									</div><br/><br/>
									<div class="form-group">
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-12">
											Invoice:<br/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Click, the selected categories of goods under the real-time inventory account<br/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. Check 'inventory table to distinguish inventory position', and then display/input the inventory quantity according to each position; If not checked, only the total inventory quantity of the product can be checked. At this time, the inventory quantity cannot be recorded。<br/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. Check the 'only query unlisted goods', only show the number of the first disk, redo not enter the goods。
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div><!--box end-->
			</div>
		</div>
	</div>

[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
[@permission code="SC-PD-QUERY-001"]<input type="hidden" class="permission-verify" id="firstStCompleteBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-QUERY-002"]<input type="hidden" class="permission-verify" id="stDifferConfirmBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-QUERY-003"]<input type="hidden" class="permission-verify" id="viewBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-QUERY-004"]<input type="hidden" class="permission-verify" id="firstEntryBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-QUERY-005"]<input type="hidden" class="permission-verify" id="lastEntryBut" value="${displayVar!}" />[/@permission]
</body>
</html>
