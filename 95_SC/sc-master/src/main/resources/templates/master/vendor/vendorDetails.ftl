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
    <script src="${m.staticpath}/[@spring.message 'js.vendorDetails'/]"></script>
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
        .fieldsetModule{
            margin:5px;
            padding: 10px;
            border:1px solid #CCCCCC;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -khtml-border-radius: 5px;
        }
        .legendTitle{
            margin:0;
            font-size:12px;
            border-bottom:none;
            text-align:center;
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
	[@common.nav "HOME&Master&Vendor Master Entry"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">
						    Vendor Master Entry
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
								    <fieldset class="fieldsetModule" style="border-style:none;">
                                        <div class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
                                            <div class="form-group">
                                                <label for="masterVendor" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Master Vendor</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                    <input id="masterVendor" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                                <label for="masterPayment" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Master Payment<br>(Sales Rebate)</label>
                                                <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                    <select id="masterPayment" disabled="disabled" class="form-control input-sm">
                                                        <option value=""></option>
                                                        <option value="1">Yes</option>
                                                        <option value="0">No</option>
                                                    </select>
                                                </div>
                                            </div><!-- form-group -->

                                            <div class="form-group">
                                                <label for="regionCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Region</label>
                                                <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                    <input id="regionCd" readonly="true" class="form-control input-sm" type="text" value="">
                                                </div>

                                                <label for="vendorType" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Vendor Type</label>
                                                <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                    <select id="vendorType" disabled="disabled" class="form-control input-sm">
                                                        <option value=""></option>
                                                        <option value="D">EOM</option>
                                                        <option value="C">General Merchandising</option>
                                                        <option value="S">Service</option>
                                                        <option value="H">Local Vendor in North</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label for="vendorId" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Vendor Code<br>(Payable)</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                    <input id="vendorId" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                                <label for="vendorCodeReceivable" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Vendor Code Receivable</label>
                                                <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                    <input id="vendorCodeReceivable" readonly="true" class="form-control input-sm" type="text" value="">
                                                </div>
                                            </div><!-- form-group -->

                                            <div class="form-group">
                                                <label for="effectiveStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" style="white-space: nowrap">Effective Start Date</label>
                                                <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                    <input id="effectiveStartDate" readonly="true" class="form-control input-sm" type="text" value="">
                                                </div>
                                                <label for="effectiveEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Effective End Date</label>
                                                <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                    <input id="effectiveEndDate" readonly="true" class="form-control input-sm" type="text" value="">
                                                </div>
                                            </div><!-- form-group -->
                                        </div>

                                        [#--trading term 列表--]
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4" style="">
                                            <table id="tradingGridTtable"></table>
                                        </div>
                                    </fieldset><!-- fieldset-1 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:70px;">General</legend>
                                        <div class="form-group">
                                            <label for="vendorName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="vendorName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="vendorNameShort" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Alias Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="vendorNameShort" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            [#--<label for="depCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Top Department</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="depCd" readonly="true" class="form-control" type="text" value="">
                                            </div>--]
                                            [#--delete by lyz 20210207--]
                                            [#--<label for="orderFlg" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Can Order</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="orderFlg" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>--]
                                            <label for="businessType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Business Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <select id="businessType" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="0">Distribution Center</option>
                                                    <option value="1">Logistics Vendor</option>
                                                    <option value="2">Self-delivering Vendor</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->

                                        [#--<div class="form-group">
                                            <label for="tradeStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Trade Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="tradeStartDate" readonly="true" class="form-control input-sm" type="text" value="">
                                            </div>
                                            <label for="tradeEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Trade End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="tradeEndDate" readonly="true" class="form-control input-sm" type="text" value="">
                                            </div>
                                        </div>--]<!-- form-group -->
                                    </fieldset><!-- fieldset-2 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Vendor Information</legend>
                                        <div class="form-group">
                                            <label for="vendorZipCode" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Postal Code</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="vendorZipCode" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="vendorEmail" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Email<br>(Use';'separate)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="vendorEmail" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="vendorAddress1" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Registered Address</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="vendorAddress1" readonly="true" class="form-control" type="text" value="">
                                            </div>
[#--                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" style="text-align:left;">Street/Town/Village</label>--]
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="vendorAddress2" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Address2</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="vendorAddress2" readonly="true" class="form-control" type="text" value="">
                                            </div>
[#--                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" style="text-align:left;">Community/Building</label>--]
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="vendorTelNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Tel</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="vendorTelNo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="vendorFaxNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Fax</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="vendorFaxNo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="adminName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Contact Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="adminName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-3 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:400px;">Min. Order Quantity/Amount & Product Group & Department</legend>
                                        <div class="form-group">
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                                <table id="minOrderAmtGridTable"></table>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                                <table id="productGroupGridTable"></table>
                                            </div>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                                <table id="departmentGridTable"></table>
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-5 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Ordering Information</legend>
                                        <div class="form-group">
                                            <label for="orderSendMethod" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="orderSendMethod" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Email</option>
                                                </select>
                                            </div>
                                            <label for="shipmentFlg" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Order Sequence</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="shipmentFlg" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="cutoffTime" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Cutoff Time</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="cutoffTime" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        [#--<div class="form-group">
                                            <label for="orderAddress1" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Order Address1</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="orderAddress1" readonly="true" class="form-control" type="text" value="">
                                            </div>
--][#--                                            <label for="orderEmail" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Email(Use';'separate)</label>--][#--
--][#--                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">--][#--
--][#--                                                <input id="orderEmail" readonly="true" class="form-control" type="text" value="">--][#--
--][#--                                            </div>--][#--
                                            <label for="emailSetting" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Order Email Setting</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <button id='emailSetting' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-option-horizontal'></span></button>
                                            </div>
                                        </div>--]<!-- form-group -->

                                        [#--<div class="form-group">
                                            <label for="orderAddress2" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Order Address2</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="orderAddress2" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="orderAdminName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Order Contact Name 1</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="orderAdminName" readonly="true" class="form-control" type="text" value="">
                                            </div>
--][#--                                            <label class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" style="text-align:left;">Community/Building</label>--][#--
                                        </div>--]<!-- form-group -->

                                        [#--<div class="form-group">
                                            <label for="orderTelNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Tel</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="orderTelNo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="orderFaxNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Fax</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="orderFaxNo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="orderAdminName2" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Order Contact Name 2</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="orderAdminName2" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div>--]<!-- form-group -->

                                        <div class="form-group">
                                            <label for="supplyPurchaseRate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Logistic Fee Rate(%)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="supplyPurchaseRate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="badDiscountRate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Rejection Rate(%)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="badDiscountRate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            [#--<label for="orderAdminName3" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Order Contact Name 3</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="orderAdminName3" readonly="true" class="form-control" type="text" value="">
                                            </div>--]
                                            <label for="emailSetting" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Order Email Setting</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <button id='emailSetting' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-option-horizontal'></span></button>
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-4 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:180px;">Bank Information By Region</legend>
                                        <div class="form-group" style="margin:2px;">
                                            <table id="bankInfoGridTable"></table>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-6 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:100px;">Financial Data</legend>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-8">
                                            <div class="form-group">
                                                <label for="payTypeCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Payment Type</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <select id="payTypeCd" disabled="disabled" class="form-control input-sm">
                                                        <option value=""></option>
                                                        <option value="1">Cash</option>
                                                        <option value="2">Bank</option>
                                                        <option value="3">Buckle Account Payable</option>
                                                    </select>
                                                </div>
                                                <label for="payPeriodDay" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Payment Term(day)</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <input id="payPeriodDay" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                            </div><!-- form-group -->

                                            <div class="form-group">
                                                <label for="payPeriodCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Payment Period(type)</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <select id="payPeriodCd" disabled="disabled" class="form-control input-sm">
                                                        <option value=""></option>
                                                        <option value="1">Monthly Settlement</option>
                                                        <option value="2">Half Monthly Settlement</option>
                                                        <option value="3">Weekly Settlement</option>
                                                        <option value="4">Daily Settlement</option>
                                                    </select>
                                                </div>
                                                <label for="payCurrencyType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Currency</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <select id="payCurrencyType" disabled="disabled" class="form-control input-sm">
                                                        <option value=""></option>
                                                        [#--<option value="1">Dollar</option>
                                                        <option value="2">RMB</option>
                                                        <option value="3">Dong</option>--]
                                                        <option value="1">VND</option>
                                                        <option value="2">##</option>
                                                    </select>
                                                </div>
                                                [#--<label for="bankCountry" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Bank Country</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                    <input id="bankCountry" readonly="true" class="form-control" type="text" value="">
                                                </div>--]
                                            </div><!-- form-group -->

                                            <div class="form-group">
                                                <label for="payTaxNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Tax ID</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <input id="payTaxNo" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                                <label for="businessRegNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Business certification<br>registration No</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <input id="businessRegNo" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                            </div>

                                            <div class="form-group">

                                                <label for="creditLimit" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Credit Limit</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <input id="creditLimit" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                                <label for="keepCreditLimit" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Commitment Limit</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-5">
                                                    <input id="keepCreditLimit" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                            </div><!-- form-group -->

                                            [#--<div class="form-group">
                                                <label for="accountPayable" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Account Payable</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                    <input id="accountPayable" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                                <label for="accountReceivable" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Account Receivable</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                    <input id="accountReceivable" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                            </div>--]<!-- form-group -->

                                            <div class="form-group">
                                                <label for="remarks" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remarks</label>
                                                <div class="col-xs-4 col-sm-4 col-md-3 col-lg-11">
                                                    <input id="remarks" readonly="true" class="form-control" type="text" value="">
                                                </div>
                                            </div><!-- form-group -->
                                        </div>

                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                            <table id="accountGridTable"></table>
                                        </div>
                                    </fieldset><!-- fieldset-7 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:150px;">Enclosed Documents</legend>
                                        <div class="form-group" style="margin:2px;">
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                <label for="contractSigned" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="contractSigned" disabled="disabled" type="checkbox">
                                                    Contract Signed
                                                </label>
                                                <label for="liquidationAgreement" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="liquidationAgreement" disabled="disabled" type="checkbox">
                                                    Liquidation Agreement
                                                </label>
                                                <label for="license" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="license" disabled="disabled" type="checkbox">
                                                    Business License Notarized
                                                </label>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group" style="margin:2px;">
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                <label for="accountsReceivable" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="accountsReceivable" disabled="disabled" type="checkbox">
                                                    Accounts Receivable Confirmation
                                                </label>
                                                <label for="bankInformation" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="bankInformation" disabled="disabled" type="checkbox">
                                                    Bank Information Registered with Tax
                                                </label>
                                                <label for="addendumSigned" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="addendumSigned" disabled="disabled" type="checkbox">
                                                    Addendum Signed
                                                </label>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group" style="margin:2px;">
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                <label for="trading" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="trading" disabled="disabled" type="checkbox">
                                                    Trading Term Proposal
                                                </label>
                                                <label for="authorizationLetter" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="authorizationLetter" disabled="disabled" type="checkbox">
                                                    Authorization Letter
                                                </label>
                                                <label for="listingProposal" class="col-xs-6 col-sm-4 col-md-4 col-lg-4 checkbox">
                                                    <input id="listingProposal" disabled="disabled" type="checkbox">
                                                    Evaluation & Listing Proposal
                                                </label>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group" style="margin:2px;">
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-1">
                                                <label for="othersCheck" class="col-xs-12 col-sm-12 col-md-12 col-lg-12 checkbox">
                                                    <input id="othersCheck" disabled="disabled" type="checkbox">
                                                    Others
                                                </label>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-8">
                                                <input id="others" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-8 -->

                                    <div style="margin:5px;">
                                        <table id="zgGridTtable"></table>
                                    </div>
								</div>
							</div>
						</div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
                                <button id="back" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
                            </div>
                        </div><!--row 分割-->
					</div>
				 </div><!--box end-->
			 </div>
		 </div>
		 <!--row 分割-->
		<div class="row">
		</div>
	 </div>

[#--Order email setting--]
    <div id="email_setting_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="" style="margin: 0 auto">
        <div class="modal-dialog modal-lg" role="document" style="width: 1500px;">
            <div class="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Order Email Setting</h4>
                </div>
                <div class="modal-body">
                    <table id="emailGridTtable"></table>
                </div>
                <div class="modal-footer">
                    <button id="back" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right" aria-hidden="true"></span>Back</button>
                </div>
            </div>
        </div>
    </div>

	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="_vendorId" value="${dto.vendorId!}"/>
	<input type="hidden" id="_effectiveStartDate" value="${dto.effectiveStartDate!}"/>
	<input type="hidden" id="searchJson" value=""/>
</body>
</html>
