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
	<script src="${m.staticpath}/[@spring.message 'js.order'/]"></script>
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
	</STYLE>
	<!--页头-->
	[@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Ordering&Order New Store Entry"][/@common.nav]
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
											<input type="text" class="form-control my-automatic input-sm" id="aStore" autocomplete="off">
											<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>

									<label for="vendorId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Supplier</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" id="vendorId">
											<a id="" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="vendorIdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>
								</div>
								<div class="form-group" hidden>
									<label for="order_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Order Date</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										[#--										<input id="order_date" disabled="disabled" nextele="" placeholder="Order Date" class="form-control input-sm select-date" type="text" value="">--]
										<input id="order_date"  nextele="" placeholder="Order Date" class="form-control input-sm select-date" type="text" value="" disabled="disabled">
									</div>
								</div>
								<div class="form-group" id="show_status">
									[#--										分类类型--]
									<label for="type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Type</label>
									<div class="col-xs-11 col-sm-10 col-md-10 col-lg-11 col-xs-offset-1  col-sm-offset-0  col-md-offset-0 col-lg-offset-0 ">

										<div class="radio-inline">
											<label>
												<input type="radio" name="order_type" id="order_type_1" nextele="order_type_2" value="1">
												[#--													商品分类--]
												Category
											</label>
										</div>
										<div class="radio-inline">
											<label>
												<input type="radio" name="order_type" id="order_type_2" value="2"  />
												[#--													商品货架--]
												Item Shelf
											</label>
										</div>
									</div>
								</div><!-- form-group -->
								<div class="form-group" id="item_type">
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
									<!-- form-group -->
								</div>
								<div class="form-group" id="shelf_type">
									<label for="excelName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Excel Name</label>
									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text"
												   class="form-control my-automatic input-sm" autocomplete="off" id="excelName">
											<a id="excelNameRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="excelNameRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>

									<label for="param_shelf"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Shelf</label>

									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" autocomplete="off" id="param_shelf">
											<a id="param_shelfRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="param_shelfRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
									</div>


									<label for="param_subShelf"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sub-Shelf</label>

									<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
										<div class="aotu-pos">
											<input type="text" class="form-control my-automatic input-sm" autocomplete="off" id="param_subShelf">
											<a id="param_subShelfRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
											<a id="param_subShelfRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
										</div>
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
<!--审核记录-->
[@common.approvalRecords][/@common.approvalRecords]
<!--页脚-->
[@common.footer][/@common.footer]
[#--	<input type="hidden" id="toKen" value="${toKen!}"/>--]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="businessDate" value="${date!}"/>
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="typeId" value="${typeId!}">
<input type="hidden" id="reviewId" value="${reviewId!}">
<input type="hidden" id="hms" value="${hms!}">


[@permission code="SC-ORDER-001"]<input type="hidden" class="permission-verify" id="orderButView" value="${displayVar!}" />[/@permission]
[@permission code="SC-ORDER-002"]<input type="hidden" class="permission-verify" id="orderButEdit" value="${displayVar!}" />[/@permission]
</body>
</html>
