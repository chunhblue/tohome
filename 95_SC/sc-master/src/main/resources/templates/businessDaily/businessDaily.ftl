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
    <script src="${m.staticpath}/[@spring.message 'js.businessDaily'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
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
		.businessDaily_box{
			border-color: #ddd;
			margin-bottom: 20px;
			background-color: #fff;
			-webkit-box-shadow: 0 1px 1px rgba(0,0,0,.05);
			padding: 15px;
			border: 1px solid #ddd;
			border-radius: 4px;
			font-size: 12px;
		}
		.table th{
			font-size: 13px;
			text-align: center;
			background-color: 	#87CEFF;
		}
		.table th,.table tr{
			height: 28px;
			line-height: 28px;
		}
		@media (min-width: 1200px) {
			.col-lg-1-5 {
				width: 25%;
				float: left;
			}
			.col-lg-offset-1-5 {
				 margin-left: 12.5%;
			 }
			.col-lg-5-1 {
				width: 20%;
				float: left;
			}
			.col-lg-offset-5-1 {
				margin-left: 40%;
			}
			.col-lg-1-6 {
				width: 22%;
				float: left;
			}
			.col-lg-offset-1-6 {
				margin-left: 4%;
			}
		}
		@media (min-width: 670px) {
			.col-lg-1-5 {
				width: 25%;
				float: left;
			}
			.col-lg-offset-1-5 {
				margin-left: 12.5%;
			}
			.col-lg-5-1 {
				width: 20%;
				float: left;
			}
			.col-lg-offset-5-1 {
				margin-left: 40%;
			}
			.col-lg-1-6 {
				width: 22%;
				float: left;
			}
			.col-lg-offset-1-6 {
				margin-left: 4%;
			}
		}
		.td-align tr td:first-child{
			text-align: left;
			width: 60%;
		}
		.td-align tr td:last-child{
			text-align: right;
			width: 40%;
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
	[@common.nav "HOME&Sale&Operation Daily Report"][/@common.nav]
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

										<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="aStore">
												<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>

										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Business Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="date" nextele="sell_end_date" placeholder="Business Date" class="form-control input-sm select-date" type="text" value="">
										</div>
										[#-- delete by lyz 20200623 --]
										[#--<label for="dep" class="col-sm-1 control-label not-null">Top Department</label>
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="dep">
												<a id="depRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="depRemove" href="javascript:void(0);" title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label for="pma" class="col-sm-1 control-label not-null">Department</label>
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm"autocomplete="off"
													   id="pma">
												<a id="pmaRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="pmaRemove" href="javascript:void(0);"
												   title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label for="category" class="col-sm-1 control-label not-null">Category</label>--][#--中分类--][#--
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="category">--][#-- placeholder="全中分类"--][#--
												<a id="categoryRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="categoryRemove" href="javascript:void(0);" title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>--]
									</div>
									<div class="form-group">
										<div class="collapse"> [#--折叠--]
											<label for="subCategory" class="col-sm-1 control-label not-null">Sub Category</label>[#--小分类--]
											<div class="col-sm-2">
												<div class="aotu-pos">
													<input type="text"
														   class="form-control my-automatic input-sm" autocomplete="off"
														   id="subCategory">[#-- placeholder="全小分类"--]
													<a id="subCategoryRefresh" href="javascript:void(0);" title="Refresh"
													   class="auto-but glyphicon glyphicon-refresh refresh" ></a>
													<a id="subCategoryRemove" href="javascript:void(0);" title="Clear"
													   class="auto-but glyphicon glyphicon-remove circle"></a>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <div class="wire"></div>
                                <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                                <button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
                                <button id='printHtml' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print icon-right'></span>Print</button>
								<button id='export' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span>Export</button>
                            </div>
						</div>
					</div>
				</div><!--box end-->
			</div>
		</div>
		<!-- 打印-->
		<!--startprint-->
		<div id="print">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 [#--col-lg-offset-1--] businessDaily_box">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
						<span style="font-size: 25px;font-weight: bold">Store Operation Daily Report</span>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<span style="float: left">Business Date：<span id="bsDate">${bsDate?string('dd/MM/yyyy')}</span></span>
						<input type="hidden" id="businessDate" value="${bsDate?string('dd/MM/yyyy')}" >
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<span style="float: left" id="storeName">Store：</span>
						<span style="float: right" id="printDate">Print Date：${printTime?string('dd/MM/yyyy hh:mm:ss')}</span>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table" style="margin-bottom: 10px">
						<tr>
							<th>Sales Amount</th>[#--营业毛额--]
							<th>Discount Amount</th>[#--折扣毛额--]
							<th>Income</th>[#--实际收入--]
							<th>Spill Amount</th>[#--溢收--]
							<th>Over Amount</th>[#--舍去金额--]
							<th>Refund Amount</th>[#--舍去金额--]
							<th>Services</th>[#--服务费--]
							<th>Charge</th>[#--充值--]
							<th>Charge Refund</th>[#--充值退款--]
							<th>Count Customer</th>[#--充值退款--]
[#--							<th>Last Month Sales Amount</th>--][#--充值退款--]
							<th>PSD</th>[#--充值退款--]
						</tr>
						<tr style="text-align: right">
							<td id="grossSaleAmount">0</td>
							<td id="discountAmount">0</td>
							<td id="saleAmount">0</td>
							<td id="spillAmount">0</td>
							<td id="overAmount">0</td>
							<td id="refundAmount">0</td>
							<td id="serviceAmount">0</td>
							<td id="chargeAmount">0</td>
							<td id="chargeRefundAmount">0</td>
							<td id="countCustomer">0</td>
[#--							<td id="lastMonthSalesAmount">0</td>--]
							<td id="Average4WeekSalesAmount">0</td>
						</tr>
					</table>
					</div>
				</div>
				<div class="wire"></div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-1-5 col-lg-1-5">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" style="margin-bottom: 10px">
							<tr>
								[#--支付方式--]
								<th colspan="2">Payment Type</th>
							</tr>
							<tr>
								[#--现金--]
								<td>Cash</td>
								<td id="payAmt0">0</td>
							</tr>
							<tr>
								[#--苏宁卡--]
								<td>Card Payment</td>
								<td id="payAmt1">0</td>
							</tr>
							<tr>
								[#--银联--]
								<td>E-Voucher</td>
								<td id="payAmt2">0</td>
							</tr>
							<tr>
								[#--商品券--]
								<td>Momo</td>
								<td id="payAmt3">0</td>
							</tr>
							<tr>
								[#--打折券--]
								<td>Zalo</td>
								<td id="payAmt4">0</td>
							</tr>
							<tr>
								[#--预付卡--]
								<td>VNPAY</td>
								<td id="payAmt5">0</td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td>Total</td>
								<td id="payAmt">0</td>
							</tr>
						</table>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-1-5 col-md-offset-1-5 col-lg-1-5 col-lg-offset-1-5">
						<table  id="exponse" class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Expense</th> [#--门店费用--]
							</tr>
							<tr class="exponse">
								<td></td>
								<td></td>
							</tr>
							<tr class="exponse">
								<td></td>
								<td></td>
							</tr>
							<tr class="exponse">
								<td></td>
								<td></td>
							</tr>
							<tr class="exponse">
								<td></td>
								<td></td>
							</tr>
							<tr class="exponse">
								<td></td>
								<td></td>
							</tr>
							<tr class="exponse">
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE" id="exponse_total">
								<td>Total</td>
								<td></td>
							</tr>
						</table>
					</div>
					<div><input id="receivablesAmount" hidden></div>
					<div class="col-xs-12 col-sm-12 col-md-1-5 col-md-offset-1-5 col-lg-1-5 col-lg-offset-1-5">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Bank Deposit</th>[#--银行缴费--]
							</tr>
							<tr>
								<td>Working capital retained yesterday</td>[#--昨日留存现金--]
								<td id="retentionAmount">0</td>
							</tr>
							[#--<tr>
								<td>Subtotal due today</td>--][#--今日应收小计--][#--
								<td id="receivablesAmount">0</td>
							</tr>--]
							<tr>
								<td>Subtotal of accumulated cash</td>[#--今日累计现金小计--]
								<td id="cashAmount">0</td>

							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td>Total</td>
								<td id="bankDepositTotal">0</td>
							</tr>
						</table>
					</div>
				</div>
				<div class="wire"></div>
				[#-- delte by lyz 202006 23 --]
				[#--<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-1-5 col-lg-1-5">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Membership Points</th>--][#--会员积分--][#--
							</tr>
							<tr>
								<td></td>--][#--补贴--][#--
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td>Total</td>
								<td></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-1-5 col-md-offset-1-5 col-lg-1-5 col-lg-offset-1-5">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Membership Charge</th>--][#--会员充值方式--][#--
							</tr>
							<tr>
								<td></td>--][#--现金--][#--
								<td></td>
							</tr>
							<tr>
								<td></td>--][#--银联卡--][#--
								<td></td>
							</tr>
							<tr>
								<td></td>--][#--WeChat Pay--][#--
								<td></td>
							</tr>
							<tr>
								<td></td>--][#--支付宝--][#--
								<td></td>
							</tr>
							<tr>
								<td></td>--][#--饿了么--][#--
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td>Total</td>
								<td></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="wire"></div>--]
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-1-6 col-lg-1-6">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" id="categoryTable_1" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Category Sales</th>[#--分类销售--]
							</tr>
							<tr id="saleGroup_1">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_2">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_3">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_4">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_5">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_6">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_7">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_8">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_9">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_10">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_11">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_12">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_13">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_14">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_15">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_16">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_17">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_18">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_19">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_20">
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td class="categoryTable_total">Total</td>
								<td id="saleGroupTotal_1"></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-1-6 col-md-offset-1-6 col-lg-1-6 col-lg-offset-1-6">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" id="categoryTable_2" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Category Sales(Continued)</th>[#--分类销售(续)--]
							</tr>
							<tr id="saleGroup_21">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_22">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_23">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_24">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_25">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_26">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_27">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_28">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_29">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_30">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_31">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_32">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_33">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_34">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_35">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_36">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_37">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_38">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_39">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_40">
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td class="categoryTable_total">Total</td>
								<td  id="saleGroupTotal_2"></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-1-6 col-md-offset-1-6 col-lg-1-6 col-lg-offset-1-6">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" id="categoryTable_3" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Category Sales(Continued)</th>[#--分类销售(续)--]
							</tr>
							<tr id="saleGroup_41">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_42">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_43">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_44">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_45">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_46">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_47">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_48">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_49">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_50">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_51">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_52">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_53">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_54">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_55">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_56">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_57">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_58">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_59">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_60">
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td class="categoryTable_total">Total</td>
								<td id="saleGroupTotal_3"></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-12 col-sm-12 col-md-1-6 col-md-offset-1-6 col-lg-1-6 col-lg-offset-1-6">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" id="categoryTable_4" style="margin-bottom: 10px">
							<tr>
								<th colspan="2">Category Sales(Continued)</th>[#--分类销售(续)--]
							</tr>
							<tr id="saleGroup_61">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_62">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_63">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_64">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_65">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_66">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_67">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_68">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_69">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_70">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_71">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_72">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_73">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_74">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_75">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_76">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_77">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_78">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_79">
								<td></td>
								<td></td>
							</tr>
							<tr id="saleGroup_80">
								<td></td>
								<td></td>
							</tr>
							<tr style="background-color: #00B2EE">
								<td class="categoryTable_total">Total</td>
								<td id="saleGroupTotal_4"></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-4 col-sm-offset-4 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4">
						<table class="table table-hover table-striped table-condensed table-bordered zgrid-table td-align" style="margin-bottom: 10px">
							<tr style="background-color: #87CEFF;font-weight: bold">
								<td style="width: 65%">Total Category Sales</td>[#--分类销售总合计--]
								<td id="saleAmountTotal" style="width: 35%"></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<span style="float: right">
							Sign In ：<input type="text" name="name" style="outline:none;text-align:center;font-size:13px;border-color: #878787;border-style: solid;border-top-width: 0px;border-right-width: 0px;border-bottom-width: 1px;border-left-width: 0px" autocomplete="off">[#--店长签名--]
						</span>
					</div>
				</div>
			</div>
		</div>
		<!--endprint-->
		 <!--row 分割-->
		<!--页脚-->
		[@common.footer][/@common.footer]
	 </div>
</body>

<input type="hidden" id="checkFlg" value='${checkFlg!}'/>
[@permission code="SC-BD-002"]<input type="hidden" class="permission-verify" id="printBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-BD-003"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</html>
