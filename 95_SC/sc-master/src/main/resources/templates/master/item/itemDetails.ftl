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
    <script src="${m.staticpath}/[@spring.message 'js.itemDetails'/]"></script>
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
        label.radio{
            margin-left:20px;
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
	[@common.nav "HOME&Master&Item Master"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">
						    Item Master Entry
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
								    <fieldset class="fieldsetModule" style="border-style:none;">
                                        <div class="form-group">
                                            <label for="itemCode" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="itemCode" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="effectiveStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Effective Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="effectiveStartDate" readonly="true" class="form-control input-sm select-date" type="text" value="">
                                            </div>
                                            <label for="effectiveEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Effective End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="effectiveEndDate" readonly="true" class="form-control input-sm select-date" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="approvalRegion" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Approval Region</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="approvalRegion" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="qosExpireDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Certificate Expiry Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="qosExpireDate" readonly="true" class="form-control input-sm select-date" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-1 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:140px;">Essential Information</legend>
                                        <div class="form-group">
                                            <label for="itemNameVietnamese" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Name(non-accented Vietnamese)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="itemNameVietnamese" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="itemNameEnAb" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">English Short Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="itemNameEnAb" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="vietnameseShortName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vietnamese Short Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="vietnameseShortName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="itemShortNameVietnamese" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Name(accented Vietnamese)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="itemShortNameVietnamese" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="itemNameEn" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item English Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="itemNameEn" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="topDepCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Top Department</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="topDepCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="depCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="depCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="category" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Category</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="category" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="subCategory" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sub-Category</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="subCategory" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="specification" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Item Specification</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="specification" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="materialType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Material Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="materialType" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="01">General Merchandise</option>
                                                    <option value="02">Finished Product</option>
                                                    <option value="03">Premium</option>
                                                    <option value="04">Raw Material</option>
                                                    <option value="05">Packaging Material</option>
                                                    <option value="06">Services</option>
                                                </select>
                                            </div>
                                            <label for="brand" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Brand(Full)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="brand" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="salesUom" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sales UOM</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="salesUom" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="manufacturer" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Manufacturer</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="manufacturer" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="origin" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Origin</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="origin" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="servicePartner" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Service Partner</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="servicePartner" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="10">MOMO</option>
                                                    <option value="20">VIETTEL</option>
                                                </select>
                                            </div>
                                            <label for="phoneCard" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Phone Card Value</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="phoneCard" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="itemStatus" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Product Status</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="itemStatus" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="10">Core item</option>
                                                    <option value="20">New Item</option>
                                                    <option value="30">Terminated item</option>
                                                    <option value="40">Normal</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="remark" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Remark</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-11">
                                                <input id="remark" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-2 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Control Information</legend>
                                        <div class="form-group">
                                            <label for="isPrivate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Private Label</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="isPrivate" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                            <label for="specialItem" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Special Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="specialItem" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="0">High-price Item</option>
                                                    <option value="1">Special Order Item</option>
                                                    <option value="2">Booking Order Item</option>
                                                </select>
                                            </div>
                                            <label for="isFoodService" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Food Service Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="isFoodService" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="isNew" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">New Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="isNew" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                            <label for="isStockCount" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Food Service Stock Count</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="isStockCount" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                            <label for="labelType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Label Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="labelType" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="0">Big Price Tag</option>
                                                    <option value="1">Medium Price Tag</option>
                                                    <option value="2">Small Price Tag</option>
                                                    <option value="N">NA</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="seasonalItem" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Seasonal Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="seasonalItem" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="0">No</option>
                                                    <option value="1">Yes</option>
                                                </select>
                                            </div>
                                            <label for="converter" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Converter</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <input id="converter" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="purchaseItem" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Purchase Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="purchaseItem" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="salesItem" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sales Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="salesItem" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                            <label for="inventoryItem" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Inventory Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="inventoryItem" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                            <label for="costItem" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Cost Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="costItem" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="isDefaultFree" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Free Gift Item</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                <select id="isDefaultFree" disabled="disabled" class="form-control input-sm">
                                                    <option value=""></option>
                                                    <option value="1">Yes</option>
                                                    <option value="0">No</option>
                                                </select>
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-3 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Additional Attributes</legend>

                                        <ul class="nav nav-tabs">
                                            <li class="active"><a href="#card1" data-toggle="tab">POG & General Data</a></li>
                                            <li><a href="#card2" data-toggle="tab">Barcode Data</a></li>
                                            <li><a href="#card4" data-toggle="tab">Ordering control</a></li>
                                            <li><a href="#card5" data-toggle="tab">Sales control</a></li>
                                            <li><a href="#card9" data-toggle="tab">Carton's Specification for DC</a></li>
                                            <li><a href="#card3" data-toggle="tab">Product's Specification for DC</a></li>
                                        </ul>

                                        <div class="tab-content" style="border-width:0 1px 1px 1px;border-style:solid;border-color:#CCCCCC;padding:10px;">
                                            <div class="tab-pane active" id="card1">
                                                <div class="form-group">
                                                    <label for="income" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Income</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <select id="income" disabled="disabled" class="form-control input-sm">
                                                            <option value=""></option>
                                                            <option value="1">High</option>
                                                            <option value="2">Medium</option>
                                                            <option value="3">Low</option>
                                                            <option value="N">NA</option>
                                                        </select>
                                                    </div>
                                                    <label for="customer" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Customer</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <select id="customer" disabled="disabled" class="form-control input-sm">
                                                            <option value=""></option>
                                                            <option value="1">All</option>
                                                            <option value="2">Foreign</option>
                                                            <option value="3">Local</option>
                                                            <option value="4">Non-food</option>
                                                            <option value="10">Individual</option>
                                                            <option value="20">Family</option>
                                                            <option value="30">Kids</option>
                                                            <option value="40">Teenagers</option>
                                                            <option value="N">NA</option>
                                                        </select>
                                                    </div>
                                                    <label for="minDisplayQty" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Display on Shelf</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="minDisplayQty" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->

                                                <div class="form-group">
                                                    <label for="purchasingUnit" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Purchasing UoM</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="purchasingUnit" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="oplMethod" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">AI Order</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <select id="oplMethod" disabled="disabled" class="form-control input-sm">
                                                            <option value=""></option>
                                                            <option value="1">Yes</option>
                                                            <option value="0">No</option>
                                                        </select>
                                                    </div>
                                                    <label for="orderType" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Order Type</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <select id="orderType" disabled="disabled" class="form-control input-sm">
                                                            <option value=""></option>
                                                            <option value="10">DC Order</option>
                                                            <option value="20">Direct Order</option>
                                                        </select>
                                                    </div>
                                                </div><!-- form-group -->

                                                <div class="form-group">
                                                    <label for="warrantyDays" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Shelf-life(day)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="warrantyDays" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="preserveType" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Storage Condition</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <select id="preserveType" disabled="disabled" class="form-control input-sm">
                                                            <option value=""></option>
                                                            <option value="0">Dry</option>
                                                            <option value="1">Cool</option>
                                                            <option value="2">Air Conditioner</option>
                                                        </select>
                                                    </div>
                                                </div><!-- form-group -->

                                            </div><!-- tab-1 -->

                                            <div class="tab-pane" id="card2">
                                                <div class="form-group">
                                                    <label for="barcodeType" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Barcode Type</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <select id="barcodeType" disabled="disabled" class="form-control input-sm">
                                                            <option value=""></option>
                                                            <option value="1">Standard Barcode</option>
                                                            <option value="2">In-House Barcode</option>
                                                        </select>
                                                    </div>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                                                        <table id="barcodeGridTable"></table>
                                                    </div>
                                                </div><!-- form-group -->
                                            </div><!-- tab-2 -->

                                            <div class="tab-pane" id="card3">
                                                <div class="form-group">
                                                    <label for="packageIsWeight" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Net Weight(g)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="packageIsWeight" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="packageOsWeight" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Gross Weight(g)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="packageOsWeight" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->

                                                <div class="form-group">
                                                    <label for="widthCm" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Width(cm)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="widthCm" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="heightCm" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Height(cm)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="heightCm" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->

                                                <div class="form-group">
                                                    <label for="packageSizeLength" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Package Size Length(cm)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="packageSizeLength" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="unitsPer" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Units Per Block/Bundle</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="unitsPer" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->
                                            </div><!-- tab-3 -->

                                            <div class="tab-pane" id="card4">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="orderingGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-4 -->

                                            <div class="tab-pane" id="card5">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="salesGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-5 -->

                                            <div class="tab-pane" id="card9">
                                                <div class="form-group">
                                                    <label for="netWeight" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Net Weight(g)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="netWeight" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="grossWeight" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Gross Weight(g)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="grossWeight" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="bundlePer" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Bundle Per Carton</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="bundlePer" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->

                                                <div class="form-group">
                                                    <label for="unitsPerCarton" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Units Per Carton</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="unitsPerCarton" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="cartonsPerLayer" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Cartons Per Layer</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="cartonsPerLayer" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="cartonsPerPallet" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Layer Per Pallet</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="cartonsPerPallet" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->

                                                <div class="form-group">
                                                    <label for="lengthForDC" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Length(cm)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="lengthForDC" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="widthForDC" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Width(cm)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="widthForDC" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                    <label for="heightForDC" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Height(cm)</label>
                                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                        <input id="heightForDC" readonly="true" class="form-control" type="text" value="">
                                                    </div>
                                                </div><!-- form-group -->
                                            </div><!-- tab-9 -->
                                        </div>

                                    </fieldset><!-- fieldset-4 -->
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
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="_articleId" value="${dto.articleId!}"/>
	<input type="hidden" id="_effectiveStartDate" value="${dto.effectiveStartDate!}"/>
	<input type="hidden" id="searchJson" value=""/>
</body>
</html>
