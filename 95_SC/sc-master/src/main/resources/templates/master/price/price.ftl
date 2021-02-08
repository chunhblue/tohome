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
    <script src="${m.staticpath}/[@spring.message 'js.priceByDay'/]"></script>
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
	[@common.nav "HOME&Master&Price Query"][/@common.nav]
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
									</div>

									<div class="form-group">
									    <label for="effective_date" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Query Date</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="effective_date" class="form-control input-sm select-date" type="text" value="" disabled>
									    </div>
									    <label for="item_code" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code</label>
									    <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
									    	<input id="item_code" class="form-control" type="text" value="">
									    </div>
										<label for="item_name" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Name</label>
										<div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
											<input id="item_name" class="form-control" type="text" value="">
										</div>
									    <label for="item_barcode" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
									    <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
									    	<input id="item_barcode" class="form-control" type="text" value="">
									    </div>
									    <label for="vendor_id" class="col-xs-12 col-sm-2 col-md-1 col-lg-1 control-label hidden">Vendor Code</label>
                                        <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2 hidden">
                                            <input id="vendor_id" class="form-control" type="text" value="">
                                        </div>
								    </div><!-- form-group -->

								    <div class="form-group">
[#--                                        <label for="top_depCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Top Department</label>--]
[#--                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
[#--                                            <select id="top_depCd" class="form-control input-sm">--]
[#--                                                <option value="">-- All/Please Select --</option>--]
[#--                                            </select>--]
[#--                                        </div>--]
[#--                                        <label for="depCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>--]
[#--                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
[#--                                            <select id="depCd" class="form-control input-sm">--]
[#--                                                <option value="">-- All/Please Select --</option>--]
[#--                                            </select>--]
[#--                                        </div>--]
[#--                                        <label for="category" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Category</label>--]
[#--                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
[#--                                            <select id="category" class="form-control input-sm">--]
[#--                                                <option value="">-- All/Please Select --</option>--]
[#--                                            </select>--]
[#--                                        </div>--]
[#--                                        <label for="sub_category" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sub-Category</label>--]
[#--                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">--]
[#--                                            <select id="sub_category" class="form-control input-sm">--]
[#--                                                <option value="">-- All/Please Select --</option>--]
[#--                                            </select>--]
[#--                                        </div>--]
										<label for="dep" class="col-sm-1 control-label" style="white-space: nowrap">Top Department</label>
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
										<label for="pma" class="col-sm-1 control-label">Department</label>
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
										<label for="category" class="col-sm-1 control-label">Category</label>[#--中分类--]
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="category">[#-- placeholder="全中分类"--]
												<a id="categoryRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="categoryRemove" href="javascript:void(0);" title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label for="subCategory" class="col-sm-1 control-label">Sub Category</label>[#--小分类--]
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
									</div>	<!-- form-group -->
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
				<table id="zgGridTable"></table>
			</div>
		</div>
	 </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="searchJson" value=""/>
	[@permission code="SC-ZD-PBD-002"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</body>
</html>
