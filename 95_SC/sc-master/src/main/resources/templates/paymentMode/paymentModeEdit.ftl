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
    <script src="${m.staticpath}/[@spring.message 'js.paymentModeEdit'/]"></script>
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
	[@common.nav "HOME&Basic&POS Payment Methods Management"][/@common.nav]
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
										<label for="payCd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Payment No.</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="payCd" class="form-control input-sm" maxlength="2">
										</div>
										<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 col-lg-offset-1" >
											<label for="changeSts" class="checkbox">
												<input id="changeSts" type="checkbox">
												Change Available
											</label>
										</div>
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Call External Interface</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<div class="radio-inline " id="">
												<label class="">
													<input type="radio" name="interfaceSts" id="interfaceSts_1"  value="1">
													Yes
												</label>
											</div>
											<div class="radio-inline " id="">
												<label class="">
													<input type="radio" name="interfaceSts" id="interfaceSts_2"  value="0">
													No
												</label>
											</div>
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="payName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Payment Name</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="payName" class="form-control input-sm" maxlength="20">
										</div>
										<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 col-lg-offset-1" >
											<label for="swipingCardSts" class="checkbox">
												<input id="swipingCardSts" type="checkbox">
												Card Payment Available
											</label>
										</div>
										<label id="interfaceAddress_lable" for="interfaceAddress" class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">External Interface Link</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="interfaceAddress" class="form-control input-sm" maxlength="255">
										</div>
									</div><!-- form-group -->
									<div  class="form-group">
										<label for="payNamePrint"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label not-null">Printed Name</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="payNamePrint" class="form-control input-sm" maxlength="20">
										</div>
										<div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 col-lg-offset-1" >
											<label for="exChargeSts" class="checkbox">
												<input id="exChargeSts" type="checkbox">
												Overcharge Available
											</label>
										</div>
									<div>
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
			<div  class="col-xs-12 col-sm-12 col-md-12 col-lg-6" hidden>
				<table id="zgGridTtable1"></table>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
				<button id="submitBtn" type="button" class="btn btn-primary returnsViewBut"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
				<button id="approvalBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-paperclip icon-right"></span>Approval</button>
				<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
			</div>
		</div>
	 </div>
	<div id="paymentModeEdit_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">POS Payment Methods Management</h4><!--POS支付方式维护-->
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="a_store"  class="col-sm-3 control-label not-null">Store</label>
						<div class="col-sm-5">
							<div class="aotu-pos" id="auto_store">
								<input type="hidden" id="oldStoreCd">
								<input type="text" class="form-control my-automatic input-sm" id="a_store" placeholder="" autocomplete="off">
								<a id="" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
								<a id="a_store_clear" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="posId" class="col-sm-3 control-label not-null">POS No.</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="hidden" id="oldPosId">
								<input hidden id="recordStatus">
								<select id="posId" class="form-control input-sm">
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="posDisplay" class="col-sm-3 control-label not-null" >Visible on POS</label>
						<div class="col-sm-5">
							<div class="aotu-pos">
								<select id="posDisplay" class="form-control input-sm">
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlgByDialog" value="" />
				<button id="cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
			</div>
		</div>
	</div>
</div>
		</div>
	</div>
	<!--审核-->
	[@common.audit][/@common.audit]
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="operateFlg" value="${operateFlg!}"/>
	<input type="hidden" id="h_payCd" value="${payCd!}"/>
	<input type="hidden" id="typeId" value="${typeId!}">
	<input type="hidden" id="reviewId" value="${reviewId!}">
	<input type="hidden" id="auditFlag" value="${auditFlag!}">
	<input type="hidden" id="delJsonParam" value=""/>
	<input type="hidden" id="userName" value="${userName!}"/>
	<input type="hidden" id="userId" value="${userId!}"/>
	<input type="hidden" id="delJsonParam" value=""/>
	<input type="hidden"  id="roleId" value=""/>

</body>
</html>
