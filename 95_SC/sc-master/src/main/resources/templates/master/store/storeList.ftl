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
    <script src="${m.staticpath}/[@spring.message 'js.storeList'/]"></script>
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
	[@common.nav "HOME&Master&Store Master Query"][/@common.nav]
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
									    <label for="storeCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="storeCd" class="form-control" type="text" value="">
									    </div>
                                        <label for="storeName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <input id="storeName" class="form-control" type="text" value="">
                                        </div>
                                        <label for="corpCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Legal Person</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="corpCd" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
                                        <label for="maCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Price Group</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="maCd" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
									</div>
								    <div class="form-group">
                                        <label for="storeTypeCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Cluster</label>
                                        <div class="col-sm-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" autocomplete="off"
                                                       id="storeTypeCd">
                                                <a id="typeCdRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="typeCdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>

                                        <label for="storeGroupCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Group</label>
                                        <div class="col-sm-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" autocomplete="off"
                                                       id="storeGroupCd">
                                                <a id="storeGroupCdRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="storeGroupCdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label for="licenseType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Contract Type</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="licenseType" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
                                        [#--<label for="storeType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Type</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="storeType" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>--]
                                        <label for="surroundings" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Surroundings</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="surroundings" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="province" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Province</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text"
                                                       class="form-control my-automatic input-sm"
                                                       id="province">[#-- placeholder="省"--]
                                                <a id="provinceRefresh" href="javascript:void(0);" title="Refresh"
                                                   class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="provinceRemove" href="javascript:void(0);" title="Clear"
                                                   class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label for="district" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">District</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text"
                                                       class="form-control my-automatic input-sm"
                                                       id="district">[#-- placeholder="地区"--]
                                                <a id="districtRefresh" href="javascript:void(0);" title="Refresh"
                                                   class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="districtRemove" href="javascript:void(0);" title="Clear"
                                                   class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label for="ward" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Ward</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text"
                                                       class="form-control my-automatic input-sm"
                                                       id="ward">[#-- placeholder=""--]
                                                <a id="wardRefresh" href="javascript:void(0);" title="Refresh"
                                                   class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="wardRemove" href="javascript:void(0);" title="Clear"
                                                   class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label for="currentLocation" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Current Location</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="currentLocation" class="form-control input-sm">
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
	<input type="hidden" id="searchJson" value=""/>
    [@permission code="SC-ZD-SMQ-001"]<input type="hidden" class="permission-verify" id="viewBut" value="${displayVar!}" />[/@permission]
    [@permission code="SC-ZD-SMQ-003"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</body>
</html>
