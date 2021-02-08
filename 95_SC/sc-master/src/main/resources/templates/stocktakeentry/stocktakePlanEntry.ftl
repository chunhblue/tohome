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
	<script src="${m.staticpath}/[@spring.message 'js.stocktakePlanEntry'/]"></script>
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
		.zgGrid-main-body {
			border-left: 1px solid #428bca;
			border-right: 1px solid #428bca;
			overflow: hidden;
			overflow-y: scroll;
			clear: both;
			position: relative;
			overflow-x: auto;
		}
		#zgGridTtable_main_body{
			overflow-y: scroll;
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
[@common.nav "HOME&Stocktaking&Stocktaking Result Entry and Query"][/@common.nav]
<div class="container-fluid" id="main_box">
	<!--row 分割-->
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title">
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
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
[#--										<input id="pd_start_date" readonly="true" nextele="sell_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">--]
										<input id="pd_start_date"  nextele="sell_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
									</div>
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null"></label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
[#--										<input  id="pd_end_date" readonly="true" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">--]
										<input  id="pd_end_date" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
									</div>
[#--									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-1">--]
[#--										<button id="clear_pd_date" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear Date</button>--]
[#--									</div>--]
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="pd_cd" class="form-control input-sm">
									</div>
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Status</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="pd_status" class="form-control input-sm" nextele="dj_status">
											<option value="">-- All/Please Select --</option>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="wire"></div>
							<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
							<button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
[#--							<button id='add' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-plus'></span> Add</button>--]
							[#--										<button id='up' type='button' class='btn  btn-warning   btn-sm ' ><span class='glyphicon glyphicon-pencil'></span> 修 改</button>--]
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
<!--审核记录-->
[@common.approvalRecords][/@common.approvalRecords]

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="enterFlag" value="${enterFlag!}"/>
<input type="hidden" id="piCdParam" value="${piCdParam!}"/>
<input type="hidden" id="piDateParam" value="${piDateParam!}"/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="businessDate" value="${businessDate!}">
[@permission code="SC-PD-ENTRY-001"]<input type="hidden" class="permission-verify" id="viewButPm" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-ENTRY-002"]<input type="hidden" class="permission-verify" id="editButPm" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-ENTRY-003"]<input type="hidden" class="permission-verify" id="exportButPm" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-ENTRY-004"]<input type="hidden" class="permission-verify" id="importButPm" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-ENTRY-005"]<input type="hidden" class="permission-verify" id="printButPm" value="${displayVar!}" />[/@permission]
[@permission code="SC-PD-ENTRY-007"]<input type="hidden" class="permission-verify" id="approvalRecordButPm" value="${displayVar!}" />[/@permission]
</body>
</html>
