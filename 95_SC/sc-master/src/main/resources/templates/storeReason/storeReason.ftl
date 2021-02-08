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
    <script src="${m.staticpath}/[@spring.message 'js.storeReason'/]"></script>
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
	[@common.nav "HOME&Basic&Stores Reason"][/@common.nav]
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
										<label for="reason_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Reasons No.</label>[#--原因编号--]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control input-sm" id="reason_cd" placeholder="" >
											</div>
										</div>
										<label for="reason_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Reasons Name</label>[#--原因名称--]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control input-sm" id="reason_name" placeholder="" >
											</div>
										</div>
										<label for="reason_type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Reasons Category</label>[#--原因类别--]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select  id="reason_type" class="form-control input-sm">
												<option value="" selected>-- All/Please Select --</option>
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
	<div id="storeReason_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
		<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Store Reason Maintenance</h4><!--店铺理由维护-->
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="i_reason_cd"  class="col-sm-3 control-label not-null">Reasons No.</label>[#--原因编号--]
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_reason_cd" placeholder="" maxlength="10">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="i_reason_name"  class="col-sm-3 control-label not-null" style="white-space:nowrap">Reasons Name</label>[#--原因名称--]
						<div class="col-sm-5">
							<div class="aotu-pos">
								<input type="text" class="form-control my-automatic input-sm" id="i_reason_name" placeholder="" maxlength="50">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="i_reason_type" class="col-sm-3 control-label not-null" style="white-space:nowrap">Reasons Category</label>[#--原因类别--]
						<div class="col-sm-5">
							<select  id="i_reason_type" class="form-control input-sm">
							</select>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlg" value="" />
				<button id="cancel" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Save</button>
			</div>
		</div>
	</div>
	</div>
	<!--页脚-->
	[@common.footer][/@common.footer]

	[@permission code="SC-SR-001"]<input type="hidden" class="permission-verify" id="storeReasonButAdd" value="${displayVar!}" />[/@permission]
	[@permission code="SC-SR-002"]<input type="hidden" class="permission-verify" id="storeReasonButEdit" value="${displayVar!}" />[/@permission]
	[@permission code="SC-SR-003"]<input type="hidden" class="permission-verify" id="storeReasonButDel" value="${displayVar!}" />[/@permission]
</body>
</html>
