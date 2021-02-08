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
    <script src="${m.staticpath}/[@spring.message 'js.index'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
	<style>
		.def-box{
			height:1000px;
		}
		.tmp-div{
			clear:both;
			margin-top:5px;
			margin-bottom:5px;
			overflow:hidden;
		}
		.tmp-div div{
			float:left;
		}
		.tmp-div .d1{
			width:120px;
			text-align: right;
			padding-right:10px;
		}
		.tmp-div .d2{
			padding-left:1px;
			
		}
		.tmp-div .d2 select{
			width:350px;
		}
		.box-title.none {
		    height: 0;
		    overflow: hidden;
		    padding: 0;
		    border: none;
		    border-bottom: 1px solid #3b78ad;
		    margin: 0;
		}
	</style>
</head>


<body>
	<!--页头-->
	[@common.header][/@common.header]
	<!--导航-->
	[@common.nav "HOME"][/@common.nav]
	<div class="container-fluid">
		<div class="row" style="display:none;">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="box">
					<div class="box-title ">
						<div class="tt">标题1</div>
						<div class="other">
							<ul class="list-inline other-ul">
								<li><a href="#">操作</a></li>
							</ul>
						</div>
					</div>
					<div class="box-body" style="overflow-y:auto;height:200px;">
						<ul>
							[#list stores as s ]
	                       		 <li>${s}</li>
							[/#list]
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- row start-->
		<div class="row" style="">
			<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
				<div class="box">
					<div class="box-title none">
						<div class="tt">标题1</div>
					</div>
					<div class="box-body" style="">
						<div class="tmp-div">
							<div class="d1">DPT：</div>
							<div  class="d2">
								<select class="">
								  <option>1</option>
								  <option>2</option>
								  <option>3</option>
								  <option>4</option>
								  <option>5</option>
								</select>
							</div>
						</div>
						<div class="tmp-div">
							<div  class="d1">修改标志：</div>
							<div  class="d2">
								<select class="">
								  <option>1</option>
								  <option>2</option>
								  <option>3</option>
								  <option>4</option>
								  <option>5</option>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- row end-->
		<!-- row start-->
		<div class="row" style="">
			<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
				<div class="box">
					<div class="box-title none">
						<div class="tt">标题1</div>
					</div>
					<div class="box-body" style="">
						<div class="row" style="">
							<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
								<div class="tmp-div">
									<div  class="d1">商品编码：</div>
									<div  class="d2">
										<input />
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
								<div class="tmp-div">
									<div class="">商品名称：</div>
									<div  class="">
										<input style="width:200px;" />
									</div>
								</div>
							</div>
						</div>
						
						<div class="row" style="">
							<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
								<div class="tmp-div">
									<div  class="d1">店铺：</div>
									<div  class="d2">
										<button type="button" class="btn btn-primary btn-xs">选择店铺</button>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row" style="">
							<div class="col-xs-12 col-sm-12 col-md-3 col-lg-6">
								<div class="tmp-div">
									<div  class="d1">物流属性：</div>
									<div  class="d2">
										<select class="" style="width:100px;">
										  <option>1</option>
										  <option>2</option>
										  <option>3</option>
										  <option>4</option>
										  <option>5</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-3">
								<div class="tmp-div">
									<div class="">仓库编码：</div>
									<div  class="">
										<input />
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-4 col-lg-3">
								<div class="tmp-div">
									<div class="">物流费率：</div>
									<div  class="">
										<input />
									</div>
								</div>
							</div>
						</div>
						<div class="row" style="">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-right">
								 	<div style="margin-right:20px;margin-bottom:10px">
									 <button type="button" class="btn btn-primary btn-xs">追加</button>
	  							     <button type="button" class="btn btn-primary btn-xs">修改</button>
								 	</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- row end-->
		<div class="row" style="">
			<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
				<table class="table table-bordered table-hover table-condensed">
			      <thead>
			        <tr>
			          <th>操作</th>
			          <th>优先度</th>
			          <th>商品名称</th>
			          <th>商品编码</th>
			          <th>店好</th>
			          <th>原物流属性</th>
			          <th>xxx</th>
			          <th>xxx</th>
			        </tr>
			      </thead>
			      <tbody>
			        <tr>
			          <th scope="row">X</th>
			          <td>Mark</td>
			          <td>Otto</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			        </tr>
			        <tr>
			          <th scope="row">X</th>
			          <td>Mark</td>
			          <td>Otto</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			        </tr>
			        <tr>
			          <th scope="row">X</th>
			          <td>Mark</td>
			          <td>Otto</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			        </tr>
			        <tr>
			          <th scope="row">X</th>
			          <td>Mark</td>
			          <td>Otto</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			        </tr>
			        <tr>
			          <th scope="row">X</th>
			          <td>Mark</td>
			          <td>Otto</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			          <td>@mdo</td>
			        </tr>
			        
			      </tbody>
			    </table>
			</div>
		</div>
	</div>
	<!--页脚-->
	[@common.footer][/@common.footer]

</body>
</html>
