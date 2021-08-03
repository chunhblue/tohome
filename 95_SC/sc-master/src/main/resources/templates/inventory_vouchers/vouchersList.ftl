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
    <script src="${m.staticpath}/[@spring.message 'js.vouchersList'/]"></script>
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
	[@common.nav "HOME&Inventory&Inventory Documents Query"][/@common.nav]
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
									    <label for="voucherNo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document No.</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="voucherNo" class="form-control" type="text" value="">
									    </div>
                                        <label for="voucherType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Document Type</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="voucherType" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
[#--                                                <option value="501">Store Transfer In</option>--]
                                                <option value="502">Store Transfer</option>
[#--                                                <option value="504">Transfer In Correction</option>--]
[#--                                                <option value="505">Transfer Out Correction</option>--]
                                                <option value="604">Inventory Adjustment</option>
                                                <option value="603">Inventory Write-off</option>
                                                <option value="601">In-store Item Transfer</option>
[#--                                                <option value="602">In-store Item Transfer</option>--]
                                            </select>
                                        </div>
									    <label for="voucherStartDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space: nowrap">Document Date</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
[#--                                            <input id="voucherStartDate" nextele="voucherEndDate" placeholder="Start Date" readonly="true" class="form-control input-sm select-date" type="text" value="">--]
                                            <input id="voucherStartDate" nextele="voucherEndDate" placeholder="Start Date"  class="form-control input-sm select-date" type="text" value="">
                                        </div>
                                        <label for="voucherEndDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null"></label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
[#--                                            <input id="voucherEndDate" readonly="true" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">--]
                                            <input id="voucherEndDate"  placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                        </div>
                                    </div><!-- form-group -->
									<div class="form-group">
									    <label for="voucherStatus" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Document Status</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="voucherStatus" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
                                        <label for="isUpload" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Upload</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="isUpload" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
                                        <label for="itemInfo" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Item Code/Name</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <input type="text" id="itemInfo" class="form-control input-sm">
                                        </div>
                                        <label for="reason" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Reason</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text"
                                                       class="form-control my-automatic input-sm" autocomplete="off"
                                                       id="reason">
                                                <a id="reasonRefresh" href="javascript:void(0);" title="Refresh"
                                                   class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="reasonRemove" href="javascript:void(0);" title="Clear"
                                                   class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                    </div><!-- form-group -->
							        <div class="form-group">
                                        <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Area Manager</label>
                                        <div class="col-sm-2">
                                            <div class="aotu-pos">
                                                <input type="text"
                                                       class="form-control my-automatic input-sm" autocomplete="off"
                                                       id="am">
                                                <a id="amRefresh" href="javascript:void(0);" title="Refresh"
                                                   class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="amRemove" href="javascript:void(0);"
                                                   title="Clear"
                                                   class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Operation Manager</label>
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="om">
                                                <a id="omRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="omRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Operation Controller</label>
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="oc">
                                                <a id="ocefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="ocRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
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
    <!--审核记录-->
    [@common.approvalRecords][/@common.approvalRecords]
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
    [@permission code="SC-ST-KCCPYL-001"]<input type="hidden" class="permission-verify" id="viewButPm" value="${displayVar!}" />[/@permission]
    [@permission code="SC-ST-KCCPYL-002"]<input type="hidden" class="permission-verify" id="editButPm" value="${displayVar!}" />[/@permission]
    [@permission code="SC-ST-KCCPYL-003"]<input type="hidden" class="permission-verify" id="exportButPm" value="${displayVar!}" />[/@permission]
</body>
</html>
