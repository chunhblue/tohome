[#ftl]
[#import "/common/spring.ftl" as spring/]
[#import "/common/common.ftl" as common/]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>[@spring.message 'common.title'/]</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">
	 <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.msg'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
<style>
	.box{
		margin-top:45%;
		background-color:#fff;
		position: relative;
	   	z-index: 9999;
	   	padding:30px;
	   	min-height:150px;
	   	border: 1px solid #428bca;
	   	border-radius: 5px;
	   	-webkit-box-shadow: 0 0 10px rgba(6,6,6,.5);
	    -moz-box-shadow: 0 0 10px rgba(6,6,6,.5);
	    box-shadow: 0 0 10px rgba(6,6,6,.5);
	}
	.msg-font{
		font-size:16px;
	}
	.second{
		text-decoration:underline;
		margin-right:5px;
		font-size:14px;
		color:red;
		font-weight: 900;
	}
	.second-box{
		margin-bottom:10px;
	}
</style>
</head>
<body class="login">
<div class="login-bg"></div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="logo"></div>
			</div>
		</div>
	</div>
	<div class="container-fluid" >
		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-5 col-lg-4  col-sm-offset-3 col-md-offset-4 col-lg-offset-4">
				<div class="box">
					<div class="row">
						<div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">
							<div class="attention"></div>
						</div>
						<div class="col-xs-8 col-sm-9 col-md-9 col-lg-9">
							<div class="row">
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<p class="text-danger msg-font">${text!}</p>
								</div>
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" id="box_2">
									<div class="second-box">[#--5秒，后自动跳转至登录页面--]
										<span id="second" class="second">5</span>seconds，Then it will automatically jump to the login page <span id="point">...</span>

									</div>
									<button id="toUrlButton" class="btn btn-primary but-width-d btn-sm">Click jump now</botton>[#--点击马上跳转--]
									<input id="url" type="hidden" value="${targetUrl!}" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	[@common.footer][/@common.footer]
</body>
</html>