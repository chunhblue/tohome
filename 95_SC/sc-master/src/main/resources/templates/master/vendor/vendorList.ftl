[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
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
    <script src="${m.staticpath}/[@spring.message 'js.vendorList'/]"></script>
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
	[@common.nav "HOME&Master&Vendor Master Query"][/@common.nav]
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
									    <label for="vendorId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Code</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="vendorId" class="form-control" type="text" value="">
									    </div>
                                        <label for="vendorName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Name</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                            <input id="vendorName" class="form-control" type="text" value="">
                                        </div>
									</div><!-- form-group -->

								    <div class="form-group">
                                        <label for="businessType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Business Type</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="businessTypeList" multiple="multiple" style="width: 100%;">
                                                <option value="0">Distribution Center</option>
                                                <option value="1">Logistics Vendor</option>
                                                <option value="2">Self-delivering Vendor</option>
                                            </select>
                                        </div>
                                        <label for="deliveryType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Delivery Type</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="deliveryType" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value="001">Room Temperature Storage</option>
                                                <option value="002">Refrigerated Storage</option>
                                                <option value="003">Frozen Storage</option>
                                            </select>
                                        </div>
                                        <label for="orderType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Type</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="orderType" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value="1">Email</option>
                                            </select>
                                        </div>
                                    </div><!-- form-group -->

								    <div class="form-group">
                                        <label for="contactName" class="col-xs-12 col-sm-2 col-md-1 col-lg-1 control-label">Order Contact Name</label>
                                        <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                            <input id="contactName" class="form-control" type="text" value="">
                                        </div>
                                        <label for="behalfVendorId" class="col-xs-12 col-sm-2 col-md-1 col-lg-1 control-label">Master Vendor</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <input id="behalfVendorId" class="form-control input-sm" type="text" value="">
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
	 </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
[#--	<input type="hidden" id="toKen" value="${toKen!}"/>--]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
[#--	<input type="hidden" id="store" value="${store!}"/>--]
[#--	<input type="hidden" id="checkResources" value="${checkResources!}"/>--]
	<input type="hidden" id="entiretyBmStatus" value=""/>
	<input type="hidden" id="tempTableType" value="-1"/>
	<input type="hidden" id="searchJson" value=""/>
	[@permission code="SC-ZD-VMQ-001"]<input type="hidden" class="permission-verify" id="viewBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-ZD-VMQ-003"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</body>
</html>
