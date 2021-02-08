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
    <script src="${m.staticpath}/[@spring.message 'js.suspendSaleEntry'/]"></script>
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
		.disable {
			pointer-events: none;
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
        .zgGrid-main-header{
            overflow-y: scroll;
        }
		.zgGrid-main-body {
			border-left: 1px solid #428bca;
			border-right: 1px solid #428bca;
			overflow: hidden;
			overflow-y: scroll;
			clear: both;
			position: relative;
			overflow-x: auto;
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
	<!--导航  紧急变价申请登录-->
	[@common.nav "HOME&Sale&Urgent Sale Pause Entry"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">
						Basic information
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
									<div class="form-group ">
										<label for="acc_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null" >Business Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="acc_date" disabled class="form-control input-sm select-date" type="text" value="${accDate?substring(6,8)}/${accDate?substring(4,6)}/${accDate?substring(0,4)}">
										</div>

										<label for="changeId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" >Document No.</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="changeId" readonly class="form-control input-sm">
										</div>
									</div>

									<div class="form-group ">
										<label for="auto_item"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null" >Item</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos" id="auto_item">
												<input type="text" class="form-control my-automatic input-sm" id="articleId" placeholder="Please enter 3 digit!" autocomplete="off">
												<a id="a_item_refresh"  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="a_item_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>

										<label for="unitName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" >UOM</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" class="form-control my-automatic input-sm" id="unitName" placeholder="" readonly="readonly">
										</div>
									</div>

									<div class="form-group ">
										<label for="spec"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label" >Spec</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" class="form-control my-automatic input-sm" id="spec" placeholder="" readonly="readonly">
										</div>

										<label for="oldPrice"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label" >Current Selling Price</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" class="form-control my-automatic input-sm" id="oldPrice" placeholder="" readonly="readonly">
										</div>
									</div>
								</div>
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
				<button id="nextBtn" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-chevron-right icon-right"></span>Next</button>[#--录入下一张变价单--]
				<button id="submitBtn" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			</div>
		</div>
	 </div>
	<div id="priceChange_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Suspension of sales maintenance</h4><!--暂停销售维护-->
			</div>
			<div class="modal-body">
				<div class="row" id="stores">
					<div class="form-horizontal">
						<div class="form-group">
							<label for="aRegion" class="col-sm-4 control-label not-null">Region</label>[#-- 大区 --]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" class="form-control my-automatic input-sm" id="aRegion">
									<a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
									<a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="aCity" class="col-sm-4 control-label not-null">City</label>[#-- 城市 --]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" class="form-control my-automatic input-sm" id="aCity">
									<a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
									<a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="aDistrict" class="col-sm-4 control-label not-null">District</label>[#-- 区域 --]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" class="form-control my-automatic input-sm" id="aDistrict">
									<a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
									<a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="aStore" class="col-sm-4 control-label">Store</label>[#-- 店铺 --]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" class="form-control my-automatic input-sm" id="aStore">
									<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
									<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="barcode"  class="col-sm-4 control-label" >Barcode</label>
							<div class="col-sm-5">
								<input type="text" id="barcode" readonly class="form-control my-automatic input-sm">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlg" value="" />
				<button id="cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			</div>
		</div>
	</div>
</div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="businessDate" value="${accDate!}"/>
	<input type="hidden" id="tempTableType" value="-1"/>
	<input type="hidden" id="searchJson" value=""/>
</body>
</html>
