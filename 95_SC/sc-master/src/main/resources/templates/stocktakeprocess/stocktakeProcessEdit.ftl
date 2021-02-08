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
	<script src="${m.staticpath}/[@spring.message 'js.stocktakeProcessEdit'/]"></script>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	[#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
	[#--<![endif]--]
	<script>
		// function screenzb(event)
		// {
		// 	xzb=event.screenX
		// 	yzb=event.screenY
		// 	console.log("X=" + xzb + " Y=" + yzb)
		// }
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
		.myDiv{
			float:left
		}
	</STYLE>
	<!--页头-->
	[@common.header][/@common.header]
</head>

<body[#-- onmousedown="screenzb(event)"--]>

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--导航-->
[@common.nav "HOME&Stocktaking&Stocktaking Variance Report"][/@common.nav]
<div class="container-fluid" id="main_box">

	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Basic information
					</div>
				</div>
				<div class="box-body box-body-padding-10" style="">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="storeNo" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="pd_date" nextele="sell_end_date" placeholder="Stocktak Date" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos good-select">
											<input type="text" id="store" class="form-control input-sm">
										</div>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Type</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="skType" class="form-control input-sm" nextele="pd_status">
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Time</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input id="pd_start_time" nextele="sell_end_date" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">
									</div>
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input  id="pd_end_time" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Method</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="skMethod" class="form-control input-sm" disabled nextele="dj_status">
										</select>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Status</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<select id="skStatus" class="form-control input-sm" disabled nextele="pd_status">
										</select>
									</div>
								</div>

								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="createUser" readonly="readonly" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Modified By</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="updateUser" readonly="readonly" class="form-control input-sm">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-5">
										<input type="text" id="remarks" class="form-control input-sm">
									</div>

								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="wire"></div>
							<div class="form-horizontal">
								<div class="form-group">
									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code/Name</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<input type="text" id="searchstr" class="form-control input-sm" placeholder="">
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Variance Qty</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div  class="myDiv" style="width:40%;">
											<input type="text" id="startQty" class="form-control input-sm">
										</div>
										<label class="myDiv" style="width:20%;text-align:center;line-height:30px">~</label>
										<div class="myDiv" style="width:40%;">
											<input type="text" id="endQty" class="form-control input-sm" >
										</div>
									</div>

									<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Variance Amount</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div  class="myDiv" style="width:40%;">
											<input type="text" id="startAmt" class="form-control input-sm">
										</div>
										<label class="myDiv" style="width:20%;text-align:center;line-height:30px">~</label>
										<div class="myDiv" style="width:40%;">
											<input type="text" id="endAmt" class="form-control input-sm" >
										</div>

									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="wire"></div>
							<button id="search_btn" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
							<button id="reset_btn" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-refresh icon-right"></span>Reset</button>
							<button id="export_btn" type="button" class="btn btn-primary btn-sm" disabled><span class="glyphicon glyphicon-export icon-right"></span>Export</button>
						</div>

					</div>
				</div>
			</div><!--box end-->
		</div>
	</div>

	<!--row 分割-->
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
			<ul id="card" class="nav nav-tabs">
				<li class="active"><a href="#card1" data-toggle="tab">Item(Stocktaking Done)</a></li>
				<li><a href="#card2" data-toggle="tab">Item(Out of Stock in Store)</a></li>
				<li><a href="#card3" data-toggle="tab">Item(Out of Stock in Financially)</a></li>
			</ul>
		</div>

		<div class="tab-content">
			<div class="tab-pane active" id="card1">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<table id="zgGridTtable1"></table>
				</div>
			</div>
			<div class="tab-pane" id="card2">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<table id="zgGridTtable2"></table>
				</div>
			</div>
			<div class="tab-pane" id="card3">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<table id="zgGridTtable3"></table>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
			<button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div><!--row 分割-->
</div>
<!--审核-->
[@common.audit][/@common.audit]
<!--页脚-->
[#--货号定位高亮参照 ：https://blog.csdn.net/mhshencaobo/article/details/84588847 --]
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="piCdParam" value="${piCdParam!}"/>
<input type="hidden" id="piDateParam" value="${piDateParam!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
</body>
</html>
