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
    <script src="${m.staticpath}/[@spring.message 'js.informLogList'/]"></script>
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
	[@common.nav "HOME&Message&Notifications Log"][/@common.nav]
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
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control input-sm" id="inform_cd" placeholder="" >
											</div>
										</div>
										<label for="inform_title"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space:nowrap">Notification Title</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control input-sm" id="inform_title" placeholder="" >
											</div>
										</div>
										<label for="log_type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Log Type</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<select id="log_type" class="form-control input-sm">
													<option value="">-- All/Please Select --</option>
												</select>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Date</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="create_start_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
										</div>
										<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null"></label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input  id="create_end_date" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
										</div>
[#--										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-1">--]
[#--											<button id="clear_create_date" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear</button>--]
[#--										</div>--]
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
	<input type="hidden" id="toKen" value="${toKen!}"/>
</body>
</html>
