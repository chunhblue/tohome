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
    <script src="${m.staticpath}/[@spring.message 'js.informEdit'/]"></script>
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

		.res-info {
			margin-top: 7px;
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
	[@common.nav "HOME&Message&Notifications Management"][/@common.nav]
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
									<div class="form-group">
										<label for="inform_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Notification No.</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="inform_cd" class="form-control input-sm" readonly="readonly">
										</div>
										<label for="inform_title"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space:nowrap">Notification Title</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
											<input type="text" id="inform_title" class="form-control input-sm" maxlength="50">
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Display Date</label>[#--显示日期--]
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="inform_start_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
										</div>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input  id="inform_end_date" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="inform_content"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Notification Content</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-10">
											<div class="aotu-pos">
												<textarea id="inform_content" class="form-control" rows="5"></textarea>
											</div>
										</div>
									</div><!-- form-group -->
								</div>
							</div>
						</div>
					</div>
				 </div><!--box end-->
			 </div>
		 </div>
		 <!--row 分割-->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-5">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#card1" data-toggle="tab">Store Range</a></li>
					<li><a href="#card2" data-toggle="tab">Privilege Range</a></li>
					<li><a href="#card3" data-toggle="tab">Attachments</a></li>
					<li id="replyTab"><a href="#card4" data-toggle="tab">Reply List</a></li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="tab-content">
				<div class="tab-pane active" id="card1">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-10" id="resStore">
						[#assign storeIndex=0]
						[#if storeDtos?? && storeDtos?size > 0]
							[#list storeDtos as dto]
								<div class="row" name="resStoreRow">
									<div class="col-xs-12 col-sm-12 col-md-11 col-lg-11">
										<div class="form-horizontal">
											<div class="form-group form-group-resource">
												<label for="iyPost" class="col-sm-1 control-label">Region</label>[#-- 大区 --]
												<div class="col-sm-2">
													<div class="res-info">${dto.regionName!}</div>
													<input type="hidden" class="form-control input-sm" name="regionRes" value="${dto.regionCd!}">
												</div>
												<label for="iyPost" class="col-sm-1 control-label">City</label>[#-- 城市 --]
												<div class="col-sm-2">
													<div class="res-info">${dto.cityName!}</div>
													<input type="hidden" class="form-control input-sm" name="cityRes" value="${dto.cityCd!}">
												</div>
												<label for="iyPost" class="col-sm-1 control-label">District</label>[#-- 区域 --]
												<div class="col-sm-2">
													<div class="res-info">${dto.districtName!}</div>
													<input type="hidden" class="form-control input-sm" name="districtRes" value="${dto.districtCd!}">
												</div>
												<label for="iyPost" class="col-sm-1 control-label">Store</label>[#-- 店铺 --]
												<div class="col-sm-2">
													<div class="res-info">${dto.storeName!}</div>
													<input type="hidden" class="form-control input-sm" name='storeRes' value="${dto.storeCd!}">
												</div>
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
										<div class="col-sm-1">
											<a class="btn btn-default btn-sm" href="javascript:void(0);" role="button" id="delStoreResource${storeIndex}">
												<div class="glyphicon glyphicon-minus"></div>
											</a>
										</div>
									</div>
								</div>
								[#assign storeIndex=storeIndex + 1]
							[/#list]
						[/#if]
						<div class="wire" id="storeSplitLine" style="margin-top: 4px;"></div>
						<input type="hidden" id="storeFlgVal" value="${storeIndex}"/>
						<div class="row" id="stores">
							<div class="col-xs-12 col-sm-12 col-md-11 col-lg-11">
								<div class="form-horizontal">
									<div class="form-group">
										<label for="iyPost" class="col-sm-1 control-label">Region</label>[#-- 大区 --]
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="a_region">
												<a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label for="iyPost" class="col-sm-1 control-label">City</label>[#-- 城市 --]
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="a_city">
												<a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label for="iyPost" class="col-sm-1 control-label">District</label>[#-- 区域 --]
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="a_district">
												<a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label for="iyPost" class="col-sm-1 control-label">Store</label>[#-- 店铺 --]
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="a_store">
												<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-1 col-lg-1">
								<div class="col-sm-1">
									<a class="btn btn-default btn-sm" href="javascript:void(0);" role="button" id="addStoreResource">
										<div class="glyphicon glyphicon-plus"></div>
									</a>
								</div>
							</div>
						</div>
					</div>
[#--					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-4">--]
[#--						<table id="zgGridTtable"></table>--]
[#--					</div>--]
				</div>
				<div class="tab-pane" id="card2">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-4">
						<table id="zgGridTtable1"></table>
					</div>
				</div>
				<div class="tab-pane" id="card3">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-4">
						<table id="zgGridTtable2"></table>
					</div>
				</div>
				<div class="tab-pane" id="card4">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-10">
						<table id="zgGridTtable3"></table>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
				<button id="submitBtn" type="button" class="btn btn-primary returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
				<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
			</div>
		</div>
	 </div>
	<div id="informEditStore_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Notification information maintenance</h4><!--通知信息维护-->
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="i_store"  class="col-sm-3 control-label not-null">Store</label>
						<div class="col-sm-5">
							<div class="aotu-pos" id="auto_store">
								<input type="hidden" id="oldStoreCd">
								<input type="text" class="form-control my-automatic input-sm" id="i_store" placeholder="" autocomplete="off">
								<a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="i_store_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlgByStore" value="" />
				<button id="cancelByStore" type="button" class="btn btn-default btn-sm" > <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirmByStore" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>

	<div id="informEditRole_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Notification information maintenance</h4><!--通知信息维护-->
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="a_store"  class="col-sm-3 control-label not-null">Privilege</label>
						<div class="col-sm-5">
							<div class="aotu-pos" id="auto_store">
								<input type="hidden" id="oldRoleId">
								<input type="text" class="form-control my-automatic input-sm" id="a_role" placeholder="" autocomplete="off">
								<a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="a_role_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlgByRole" value="" />
				<button id="cancelByRole" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirmByRole" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			</div>
		</div>
	</div>
</div>

<!--附件上传-->
[@common.upload][/@common.upload]

<div id="informEditStore1_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Notification information maintenance</h4><!--通知信息维护-->
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="iyPost" class="col-sm-1 control-label">Region</label>[#-- 大区 --]
						<div class="col-sm-2">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="a_region">
								<a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
						<label for="iyPost" class="col-sm-1 control-label">City</label>[#-- 城市 --]
						<div class="col-sm-2">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="a_city">
								<a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
						<label for="iyPost" class="col-sm-1 control-label">District</label>[#-- 区域 --]
						<div class="col-sm-2">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="a_district">
								<a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
						<label for="iyPost" class="col-sm-1 control-label">Store</label>[#-- 店铺 --]
						<div class="col-sm-2">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="a_store">
								<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlgByStore" value="" />
				<button id="cancelByStore" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirmByStore" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			</div>
		</div>
	</div>
</div>
<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="operateFlg" value="${operateFlg!}"/>
	<input type="hidden" id="h_inform_cd" value="${informCd!}"/>
	<!-- 店铺范围 -->
	<input id="from_hide_stores" type="hidden" name="storesStr" value=""/>
</body>
</html>
