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
    <script src="${m.staticpath}/[@spring.message 'js.cashierDetail'/]"></script>
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
	</STYLE>
<!--页头-->
[@common.header][/@common.header]
</head>

<body>

[#--   <div class="error-pcode" id="error_pcode">--]
[#--      <div class="error-pcode-text"> ${useMsg!} </div>--]
[#--      <div class="shade"></div>--]
[#--   </div>--]
	<!--导航-->
	[@common.nav "HOME&Sale&Sale Information Query"][/@common.nav]
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
										<label for="sale_start_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Sales Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="sale_start_date"  nextele="sale_end_date" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">
										</div>
										<label for="sale_start_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input  id="sale_end_date"  placeholder="End Time" class="form-control input-sm select-date" type="text" value="">
										</div>
[#--										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-1">--]
[#--											<button id="clear_sale_date" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear</button>--]
[#--										</div>--]
										<label for="cashierId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Cashier</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
[#--											<input type="text" id="cashierId" class="form-control input-sm">--]
											<select id="cashierId" class="form-control input-sm" nextele="">
												<option value="">-- All/Please Select --</option>
											</select>
										</div>
										<label for="shift"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Shift</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="shift" class="form-control input-sm" nextele="">
												<option value="">-- All/Please Select --</option>
												<option value="00">Shift1</option>
												<option value="01">Shift2</option>
												<option value="02">Shift3</option>
											</select>
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="payType"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Payment Type</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="payType" class="form-control input-sm" nextele="">
												<option value="">-- All/Please Select --</option>
												[#list payList as pay ]
													<option value="${pay.payCd}">${pay.payNamePrint}</option>
												[/#list]
											</select>
										</div>
										<div class="col-xs-12 col-sm-4 col-md-2 col-lg-1">
											<select id="calType" class="form-control input-sm" nextele="">
												<option value="">-- All/Please Select --</option>
												<option value=">=">>=</option>
												<option value="=">=</option>
												<option value="<="><=</option>
											</select>
										</div>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text"
												   id="payAmt" class="form-control input-sm">
										</div>
										<label for="memberId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Membership ID</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="memberId" class="form-control input-sm">
										</div>
										<label for="posId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1  control-label" style="white-space: nowrap">POS No.</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="posId" class="form-control input-sm" nextele="">
												<option value="">-- All/Please Select --</option>
											</select>
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="aStore">
												<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>

										<label for="barcode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="barcode" class="form-control input-sm">
										</div>
										<label for="articleShortName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Name</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="articleShortName" class="form-control input-sm">
										</div>
										<label for="Total Amount"   id="totalAmountA" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Total Amount</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="totalAmount" class="form-control input-sm" disabled="disabled">
										</div>
									</div><!-- form-group -->
									<div class="form-group">  [#-- 电子小票编号 --]
										<label for="saleSerialNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Receipt No.</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="saleSerialNo" class="form-control input-sm">
										</div>

										<label for="tranSerialNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">SAP Receipt No</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="tranSerialNo" class="form-control input-sm">
										</div>

										<label for="billSaleNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">SAP Bill No.</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="billSaleNo" class="form-control input-sm">
										</div>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="wire"></div>
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
								<button id='reset' type='button' class='btn  btn-primary btn-sm ' ><span class='glyphicon glyphicon-refresh'></span>Reset</button>
								<button id='export' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span>Export</button>
							</div>
						</div>
					</div>
				 </div><!--box end-->
			 </div>
		 </div>
		 <!--row 分割-->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-9">
				<table id="zgGridTtable"></table>
			</div>
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-3">
				<table id="payDetailTable"></table>
			</div>
		</div>
		<!--row 分割-->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="itemDetailTable"></table>
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
	<input type="hidden" id="toSaleQty" value=""/>
	<input type="hidden" id="toSaleAmount" value=""/>
	<input type="hidden" id="toDiscountAmount" value=""/>
</body>
[@permission code="SC-CS-DETAIL-002"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</html>
