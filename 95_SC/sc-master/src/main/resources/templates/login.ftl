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
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.login'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
	</script>
	<STYLE>
		.my-footer{
			background-color:transparent;
			border:none;
			
		}
		.navbar-default .navbar-text{
			color:#fff;
		}
		.codeimg{
			margin:0 1px;
			width:100px;
			height:30px;
		}
		.codeimg-box{
			padding:0;
		}
		.msg-test{
			color:red;
		}
	</STYLE>
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
	<div class="container-fluid" id="login_box">
		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-5 col-lg-4  col-sm-offset-3 col-md-offset-4 col-lg-offset-4">
				<div class="login-box">
					<div class="login-box-bg"></div>
					<form action="${syspath}/dologin" method="post" id="loginForm" name="loginForm" target="_top">
					  <div class="form-group">
					  	<label for="userid">User ID</label>
					    <input type="text" class="form-control " title="Please enter user ID" value="${userid!}" name="userid" id="userid" placeholder="Please enter user ID"patterntext="User ID cannot be empty"  errormsg="User ID cannot be empty" required
							   oninvalid="setCustomValidity('This is the required field');" oninput="setCustomValidity('');"
						>
					  </div>
					  <div class="form-group">
					  	<label for="password">Password</label>
					    <input type="password" class="form-control " title="Please enter password" value="" name="password" id="password" placeholder="Please enter password" required
							   oninvalid="setCustomValidity('This is the required field');" oninput="setCustomValidity('');">
					  </div>
					  [#--<div class="form-group">
						  <label >验证码:&nbsp;</label>
						  <input type="text" id="valCode" class="wu-text" style="width: 160px" placeholder="请输入验证码">

						  <img id="vimg" src="[@spring.url '/a/valCode' /]" width="90" height="30" onclick="resh()">
					  </div>--]
[#--					  <div class="form-group">--]
[#--						<label for="store">店铺</label>--]
[#--						  <select id="store" name="store" befValue="" class="form-control input-sm" required>--]
[#--							  [#if storeList?exists]--]
[#--								  [#list storeList as store ]--]
[#--									  <option value="${store.storeCd}">${store.storeCd} ${store.storeName}</option>--]
[#--								  [/#list]--]
[#--							  [/#if]--]
[#--						  </select>--]
[#--					  </div>--]
					  [#if Session.S_SHOWCAPTCHA??]
						<div class="form-group">
							<label for="captcha">[@spring.message 'login.captcha'/]</label>
							<div class="input-group">
						      <input type="text" class="form-control capmsgtcha" placeholder="Please enter the verification code..."  id="captcha" name="captcha" maxlength="4" value="">
						      <div class="input-group-addon codeimg-box">
							  <div>
						      	<span>
									<a class="codelink" id="codelink" href="javascript:void(0);" title="Can't see clearly? Click the picture to change it!" align="absmiddle">
										<img id="codeimg" src="[@spring.url '/a/captcha' /]" style="border-style: none" class="codeimg" align="absmiddle"/>
									</a>
								</span>
							  </div>
						      </div>
						    </div>
						</div>
						[/#if]
						<div class="form-group msg-test" id="msg">
							<div class="alert alert-danger" id="msgTest">
							</div>
						  </div>
					  <button type="submit" id="submit" class="btn btn-primary login-but btn-lg">Login</button>
					  	<input type="hidden" id="errMsg" value="${errMsg!''}">
						<input type="hidden" id="code" value="${code!''}" />
					</form>
				</div>
			</div>
		</div>
	</div>
	[@common.footer][/@common.footer]
</body>
</html>
