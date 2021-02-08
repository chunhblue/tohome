[#ftl]
[#import "common/spring.ftl" as spring/]
[#import "common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>[@spring.message 'common.title'/]</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0" user-scalable=no />
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
	<style>
		.error-img-div{
			margin-top:10%;
		}
	</style>
</head>

<body class="error">
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="logo"></div>
			</div>
		</div>
	</div>
	<div class="container-fluid" >
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="text-align: center">
				<img class="img-responsive center-block error-img-div" src="${m.staticpath}/error.png">
				<sapn style="color:#B3BBC6;font:normal bold 28px '微软雅黑';">
					Sorry, the page is not accessible！Please refresh the page or contact your system administrator
				</sapn>
			</div>
		</div>
	</div>
	[@common.footer][/@common.footer]
</body>
</html>
