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
    <script src="${m.staticpath}/[@spring.message 'js.informReplyEdit'/]"></script>
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
	[@common.nav "HOME&Message&Annoucement Check And Reply"][/@common.nav]
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
										<label for="inform_cd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Notification No.</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="inform_cd" class="form-control input-sm" readonly="readonly">
										</div>
										<label for="inform_title"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Notification title</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
											<input type="text" id="inform_title" class="form-control input-sm" maxlength="50" readonly="readonly">
										</div>
										<label for="store_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="store_name" class="form-control input-sm" readonly="readonly">
										</div>
									</div><!-- form-group -->
									<div class="form-group">
										<label for="inform_content"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Notification content</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-6">
											<div class="aotu-pos">
												<textarea id="inform_content" class="form-control" rows="8" readonly="readonly"></textarea>
											</div>
										</div>
										<div class="col-xs-12 col-sm-12 col-md-12 col-lg-4">
											<table id="zgGridTtable1"></table>
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
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-10">
				<table id="zgGridTtable2"></table>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
				<button id="submitBtn" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-send icon-right"></span>Reply</button>
				<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
			</div>
		</div>
	 </div>
	<div id="informReply_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Reply</h4><!--回复-->
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label for="inform_content"  class="col-sm-3 control-label not-null">Reply Content</label>
						<div class="col-sm-9">
							<div class="aotu-pos">
								<textarea id="inform_reply_content" class="form-control" rows="5"></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="operateFlgByDialog" value="" />
				<button id="cancelByReply" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
				<button id="affirmByReply" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Reply</button>
			</div>
		</div>
	</div>
</div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="operateFlg" value="${operateFlg!}"/>
	<input type="hidden" id="h_store_cd" value="${storeCd!}"/>
	<input type="hidden" id="h_inform_cd" value="${informCd!}"/>
</body>
</html>
