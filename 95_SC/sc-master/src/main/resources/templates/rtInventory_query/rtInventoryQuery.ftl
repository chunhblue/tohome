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
	<script src="${m.staticpath}/[@spring.message 'js.rtInventoryQuery'/]"></script>
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

<!--导航-->
[@common.nav "HOME&Inventory&Real-time Inventory Query"][/@common.nav]
<div class="container-fluid" id="main_box">
	<!--row 分割-->
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="box">
				<div class="box-title ">
					<div class="tt">
						Query Condition     [#-- 页面开始 --]
					</div>
				</div>
				<div class="box-body box-body-padding-10" >
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-horizontal">
								<div class="form-group">
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
								</div>

								<div class="form-group">
									<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="aStore">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
									<label for="stock_date"  class="col-xs-12 col-sm-4 col-md-1 col-lg-1 control-label not-null">Stock Date</label>
                                    <div class="col-xs-6 col-sm-2 col-md-2 col-lg-2">
                                        <input id="stock_date"   placeholder="Stock Date" class="form-control input-sm" type="text"  value="" readonly="readonly" style="text-align: center">
                                    </div>
									<label for="item_barcode"  class="col-xs-12 col-sm-4 col-md-1 col-lg-1 control-label" >Item Barcode</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2" >
                                        <input type="text" id="item_barcode" class="form-control input-sm" >
									</div>
                                    <label for="item_code"  class="col-xs-12 col-sm-4 col-md-1 col-lg-1 control-label" >Item Code</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="item_code" class="form-control input-sm" >
                                    </div>
								</div>
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

									<label for="vendorId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Vendor Code/Name</label>
									<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="vendorId">
											<a id="" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="vendorIdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
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

					<!-- 模态框（Modal） --> [#--tabindex="-1" : 设置模态框为独立的页面，通过Tab键切换焦点时，只能在模态框内--]
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="rtInventory Query" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
									<h4 class="modal-title" id="submitter_query">rtInventory Model</h4>
								</div>

								<div class="modal-body">
									<div class="form-horizontal">
										<div class="form-group">
											<label for="code" class="col-sm-2 col-lg-4 control-label">Item Code</label>
											<div class="col-sm-6">
												<input type="text" id="code"  class="form-control my-automatic input-sm">
											</div>
										</div>

										<div class="form-group">
											<label for="barcode" class="col-sm-2 col-lg-4 control-label">Item Barcode</label>
											<div class="col-sm-6">
												<input type="text" id="barcode"  class="form-control my-automatic input-sm">
											</div>
										</div>

										<div class="form-group">
											<label for="name" class="col-sm-2 col-lg-4 control-label">Item Name</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="name"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="specification" class="col-sm-2 col-lg-4 control-label">Specification</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="specification"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="uom" class="col-sm-2 col-lg-4 control-label">UOM</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="uom"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="inventoryQty" class="col-sm-2 col-lg-4 control-label">Inventory Quantity Pre Day</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="inventoryQty"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>


										<div class="form-group">
											<label for="salesQty" class="col-sm-2 col-lg-4 control-label">Sales Quantity the day</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="salesQty"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="receivingQty" class="col-sm-2 col-lg-4 control-label">Receiving Quantity the day</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="receivingQty"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="adjustmentQty" class="col-sm-2 col-lg-4 control-label">Inventory Adjustment Quantity</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="adjustmentQty"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="transitQty" class="col-sm-2 col-lg-4 control-label">Stock on Hand Quantity</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="handQty"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>

										<div class="form-group">
											<label for="handQty" class="col-sm-2 col-lg-4 control-label">Real Time Stock Quantity</label>
											<div class="col-sm-6">
												<div class="aotu-pos">
													<input type="text" id="transitQty"  class="form-control my-automatic input-sm">
												</div>
											</div>
										</div>


									</div>
								</div>

								<div class="modal-footer">
									<div class="row">
										<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
											<input type="hidden" id="e_id" value="" />
											<button id="cancel" type="button" class="btn btn-primary btn-sm" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
											<button id="tip" type='button'  class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Submit</button>
										</div>
									</div>
								</div>
							</div><!--box end-->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--row 分割-->
	<div class="row">
		<div class="col-x-12 col-sm-12 col-md-12 col-lg-12">
			<table id="zgGridTable"></table>
		</div>
	</div>
</div>

[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="businessDate" value="${date!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>

</body>
</html>
