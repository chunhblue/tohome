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
    <script src="${m.staticpath}/[@spring.message 'js.storeItemRelationship'/]"></script>
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

[#--   <div class="error-pcode" id="error_pcode">--]
[#--      <div class="error-pcode-text"> ${useMsg!} </div>--]
[#--      <div class="shade"></div>--]
[#--   </div>--]
	<!--导航-->
	[@common.nav "HOME&Ordering&POG Entry and Query"][/@common.nav][#--订货店铺与货品关系--]
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
										<label for="" class="col-sm-1 control-label" style="white-space: nowrap">Top Department</label>
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
													   class="form-control my-automatic input-sm" autocomplete="off"
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
												   class="auto-but glyphicon glyphicon-remove circle" ></a>
											</div>
										</div>
										<label for="subCategory" class="col-sm-1 control-label">Sub Category</label>[#--小分类--]
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="subCategory">[#-- placeholder="全小分类"--]
												<a id="subCategoryRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="subCategoryRemove" href="javascript:void(0);" title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label for="itemCode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="itemCode" class="form-control input-sm">
										</div>
										<label for="shelf"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Shelf</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="shelf" class="form-control input-sm">
										</div>
										<label for="subShelf"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sub-Shelf</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="subShelf" class="form-control input-sm">
										</div>
										<label for="descG"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Product Type</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="descG" class="form-control input-sm">
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="loc"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Location</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="loc" class="form-control input-sm">
										</div>
										<label for="vFacing"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">V. Facing</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="vFacing" class="form-control input-sm">
										</div>
										<label for="hFacing"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">H. Facing</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="hFacing" class="form-control input-sm">
										</div>
										<label for="dFacing"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">D. Facing</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="dFacing" class="form-control input-sm">
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="aStore" autocomplete="off">
												<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>

										<label for="storeTypeCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Cluster</label>
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" autocomplete="off"
													   id="storeTypeCd">
												<a id="typeCdRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="typeCdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>

										<label for="maCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Group</label>
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" autocomplete="off"
													   id="maCd">
												<a id="maCdRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="maCdRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>

										<label for="totalFacing"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Total Facing</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="totalFacing" class="form-control input-sm">
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="createDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="createDate" placeholder="Created Date" class="form-control input-sm select-date" type="text" value="">
										</div>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="wire"></div>
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
								<button id='reset' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
								<button id='import' type='button' class='btn  btn-primary  btn-sm ' ><span class='glyphicon glyphicon-zoom-in icon-right'></span>Upload</button>
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
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
				<button id="approvalBut" type="button" class="btn btn-default"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
			</div>
		</div><!--row 分割-->
	 </div>
	<div id="storeItemRelationship_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
		<div class="modal-dialog modal-md" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Upload</h4>
				</div>
				<div class="modal-body">
					<div class="form-horizontal">
						<div class="form-group">
							<label for="a_store"  class="col-sm-4 control-label not-null">Store</label>
							<div class="col-sm-5">
								<div class="aotu-pos" id="auto_store">
									<input type="text" class="form-control my-automatic input-sm" id="a_store" placeholder="" autocomplete="off">
									<a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
									<a id="a_store_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="r_storeTypeCd" class="col-sm-4 control-label">Store Cluster</label>
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" id="r_storeTypeCd" disabled class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="r_maCd" class="col-sm-4 control-label">Store Group</label>
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" id="r_maCd" disabled class="form-control input-sm">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="file"  class="col-sm-4 control-label not-null">File</label>
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="file" id="fileData" name="fileData" class="form-control input-sm">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="e_id" value="" />
					<button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
					<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
				</div>
			</div>
		</div>
	</div>
	<!--审核-->
	[@common.audit][/@common.audit]
	<!--审核记录-->
	[@common.approvalRecords][/@common.approvalRecords]
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="storeCd" value="${storeCd!}"/>
	<input type="hidden" id="storeName" value="${storeName!}"/>
	<input type="hidden" id="typeId" value="${typeId!}">
	<input type="hidden" id="reviewId" value="${reviewId!}">
	<input type="hidden" id="searchJson" value=""/>

	[@permission code="SC-S-I-R-002"]<input type="hidden" class="permission-verify" id="storeShelfButUpload" value="${displayVar!}" />[/@permission]
	[@permission code="SC-S-I-R-003"]<input type="hidden" class="permission-verify" id="storeShelfButExport" value="${displayVar!}" />[/@permission]
	[@permission code="SC-S-I-R-004"]<input type="hidden" class="permission-verify" id="approvalButBm" value="${displayVar!}" />[/@permission]
</body>
</html>
