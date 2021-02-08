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
    <script src="${m.staticpath}/[@spring.message 'js.storeDetails'/]"></script>
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
	[@common.nav "HOME&Master&Store Master"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">
                            Store Master Entry
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<form class="form-horizontal" id="storeForm">
								    <fieldset class="fieldsetModule" style="border-style:none;">
                                        <div class="form-group">
                                            <label for="storeCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="storeCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="referenceStoreCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">AI Reference Store</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                                <input id="referenceStoreCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="effectiveStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Effective Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="effectiveStartDate" readonly="true" class="form-control input-sm select-date" type="text" value="">
                                            </div>
                                            <label for="effectiveEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Effective End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
                                                <input id="effectiveEndDate" readonly="true" class="form-control input-sm select-date" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-1 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:140px;">Basic Info</legend>
                                        <div class="form-group">
                                            <label for="storeName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="storeName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storeNameEn" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Name(English)</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="storeNameEn" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="attention" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store's License Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-3 col-lg-7">
                                                <input id="attention" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storeNameShort" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Alias Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeNameShort" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="corpCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Legal Person</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="corpCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="zoCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">City</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="zoCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="doCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">District</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="doCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="maCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Price Group</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="maCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="om" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Operation Manager</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="om" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="ofc" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Area Manager</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="ofc" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="oc" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Operation Controller</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="oc" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="sm" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Manager</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="sm" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storeTypeCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Cluster</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeTypeCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="oldStoreCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Origin Store No.</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="oldStoreCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storeGroupName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Group</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeGroupName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-2 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Contact Info</legend>
                                        <div class="form-group">
                                            <label for="storeOwnerName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store In-Charge/Franchisee Name</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeOwnerName" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="province" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Province</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="province" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="district" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">District</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="district" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="ward" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Ward</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="ward" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storeAddress1" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Address</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-7">
                                                <input id="storeAddress1" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storeEmail" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Email</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeEmail" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storeManagerEmail" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Manager Email</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeManagerEmail" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storePhoneNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Tel</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storePhoneNo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storePhoneNo2" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Manager Tel</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storePhoneNo2" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storeZipCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Post Code</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeZipCd" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storeFaxNo2" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Receiving Fax</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeFaxNo2" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-3 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Additional Store Information</legend>
                                        <div class="form-group">
                                            <label for="licenseType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Contract Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="licenseType" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storeSizeGroup" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Size-Group</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="storeSizeGroup" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="shelvesType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Shelf Type</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="shelvesType" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="storeFrontageLength" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Frontage Length</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="storeFrontageLength" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="tradingArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Trading Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="tradingArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                            <label for="seatingArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Seating Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="seatingArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storageArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Storage Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="storageArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                            <label for="totalStoreArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Total Store Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="totalStoreArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="storeArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="storeArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                            <label for="sharingArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Sharing Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="sharingArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="subLeaseArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Sub-Lease Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="subLeaseArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                            <label for="idleArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Idle Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="idleArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="totalLeasedArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Total Leased Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="totalLeasedArea" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">㎡</div>
                                                </div>
                                            </div>
                                            <label for="outSeatArea" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Outdoor Seating Area</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="seatArea" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="numOfPos" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Quantity of POS</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="numOfPos" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="unusedLevels" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">No. of Unused Levels</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="unusedLevels" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="chillerDoors" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Quantity of PD/Beer Chiller Doors</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="chillerDoors" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="racksOne" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Quantity of 1.8m Racks</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="racksOne" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="racksTwo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Quantity of 1.4m Racks</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="racksTwo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="qtyOpenCase" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Quantity Open-Case</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="qtyOpenCase" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="lenOpenCase" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" >Length of Open-Case</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="lenOpenCase" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">m</div>
                                                </div>
                                            </div>
                                            <label for="lenCheckCounter" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Length of Checkout Counter</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <div class="aotu-pos">
                                                    <input id="lenCheckCounter" readonly="true" class="form-control" type="text" value="">
                                                    <div style="position: absolute; right: -18px;top: 15px;font-size: 15px">m</div>
                                                </div>
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="bizHoursFrom" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Business Start Time</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="bizHoursFrom" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="bizHoursTo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Business End Time</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="bizHoursTo" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="openDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Open Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="openDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="closeDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Store Close Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="closeDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="renovationStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Renovation Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="renovationStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="renovationEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Renovation End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="renovationEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="systemStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">System Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="systemStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="systemEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">System End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="systemEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="testOrderStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Test Order Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="testOrderStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="testOrderEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Test Order End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="testOrderEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="orderStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Order Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="orderStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="orderEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Order End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="orderEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="saleStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sales Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="saleStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="saleEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sales End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="saleEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset><!-- fieldset-4 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Property Information</legend>
                                        <div class="form-group">
                                            <label for="leaseStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Lease Start Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="leaseStartDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="leaseEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Lease End Date</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="leaseEndDate" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="nameOfLandlord" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Name Of Landlord</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-7">
                                                <input id="nameOfLandlord" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="landlordAddress" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Landlord Address</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-7">
                                                <input id="landlordAddress" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="phoneNumber1" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Phone Number1</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="phoneNumber1" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="phoneNumber2" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Phone Number2</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="phoneNumber2" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="propertyEmail" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Property Email</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-7">
                                                <input id="propertyEmail" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="rent" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Rent</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="rent" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                            <label for="depositAmount" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Deposit Amount</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-3">
                                                <input id="depositAmount" readonly="true" class="form-control" type="text" value="">
                                            </div>
                                        </div><!-- form-group -->

                                        <div class="form-group">
                                            <label for="comments" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Comments</label>
                                            <div class="col-xs-4 col-sm-4 col-md-2 col-lg-7">
                                                <textarea class=" form-control " cols="20" id="comments" rows="4" readonly="true"></textarea>
                                            </div>
                                        </div><!-- form-group -->

                                    </fieldset><!-- fieldset-5 -->

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Attached Property</legend>
                                        <div class="form-group">
                                            <label for="type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Current Location:</label>
                                            <div id="current_location" class="col-xs-11 col-sm-10 col-md-10 col-lg-11">
                                            </div>
                                        </div><!-- form-group -->
                                        <div class="form-group">
                                            <label for="type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Surroundings:</label>
                                            <div id="surroundings" class="col-xs-11 col-sm-10 col-md-10 col-lg-11">
                                            </div>
                                        </div><!-- form-group -->
                                    </fieldset>

                                    <fieldset class="fieldsetModule" style="">
                                        <legend class="legendTitle" style="width:130px;">Other Property</legend>

                                        <ul class="nav nav-tabs">
                                            <li class="active"><a href="#card1" data-toggle="tab">License</a></li>
                                            <li><a href="#card2" data-toggle="tab">Accounting</a></li>
                                            <li><a href="#card3" data-toggle="tab">Competitor</a></li>
                                        </ul>

                                        <div class="tab-content" style="border-width:0 1px 1px 1px;border-style:solid;border-color:#CCCCCC;padding:10px;height:200px;">
                                            <div class="tab-pane active" id="card1">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="licenseGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-1 -->

                                            <div class="tab-pane" id="card2">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="accountingGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-2 -->

                                            <div class="tab-pane" id="card3">
                                                <div class="form-group" style="margin:5px;">
                                                    <table id="competitorGridTable"></table>
                                                </div><!-- form-group -->
                                            </div><!-- tab-3 -->
                                        </div>
                                    </fieldset>
								</form>
							</div>
						</div>
					</div>
				 </div><!--box end-->
			 </div>
		 </div>
		 <!--row 分割-->
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
                <button id="back" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
            </div>
        </div><!--row 分割-->
	 </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="_storeCd" value="${dto.storeCd!}"/>
	<input type="hidden" id="_effectiveStartDate" value="${dto.effectiveStartDate!}"/>
	<input type="hidden" id="searchJson" value=""/>
</body>
</html>
