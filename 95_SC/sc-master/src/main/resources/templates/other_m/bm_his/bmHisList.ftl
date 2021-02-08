[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
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
    <script src="${m.staticpath}/[@spring.message 'js.bmhis'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
	</script>
	<STYLE>
		
	</STYLE>
</head>

<body>
	<!--页头-->
	[@common.header/]
	<!--导航-->
	[@common.nav "HOME&其他M管理&BM历史查询"/]
	
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">Query Condition</div>
					</div>
					<div class="box-body box-body-padding-10">
						<div class="form-horizontal">
							<div class="form-group" id="">
							    <label for="s_div"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">事业部</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<select  id="s_div" class="form-control input-sm">
							    		<option value="" selected>-- All/Please Select --</option>
									  	[#if init.div?exists]
										  	[#list init.div as d ]
						                        <option value="${d.k!}">${d.k!} ${d.v!}</option>
											[/#list]
										[/#if]
									</select>
							    </div>
							    <label for="s_dpt"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DPT</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<select  id="s_dpt" class="form-control input-sm">
							    		<option value="" selected>-- All/Please Select --</option>
									  	[#if init.dpt?exists]
										  	[#list init.dpt as d ]
						                        <option value="${d.k!}">${d.k!} ${d.v!}</option>
											[/#list]
										[/#if]
									</select>
							    </div>
						    </div><!-- form-group -->
							<div class="form-group" id="">
							    <label for="s_store"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">店铺</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<select  id="s_store" class="form-control input-sm">
							    		<option value="" selected>-- All/Please Select --</option>
									  	[#if init.stores?exists]
										  	[#list init.stores as store ]
						                        <option value="${store.k!}">${store.k!} ${store.v!}</option>
											[/#list]
										[/#if]
									</select>
							    </div>
							    <label for="s_new_flg"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">审核区分</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<select  id="s_new_flg" class="form-control input-sm">
							    		<option value="" selected>-- All/Please Select --</option>
							    		<option value="0" >Add</option>
							    		<option value="2" >手工删除</option>
							    		<option value="1" >修改</option>
									</select>
							    </div>
							    <label for="s_op_flg"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">操作类型</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<select  id="s_op_flg" class="form-control input-sm">
							    		<option value="" selected>-- All/Please Select --</option>
							    		<!-- 选择01 后台也需要检索07的数据-->
							    		<option value="01" >采购员提交</option>
							    		<option value="08" >采购员驳回</option>
							    		<option value="17" >事业部长通过</option>
							    		<option value="18" >事业部长驳回</option>
							    		<option value="27" >系统部通过</option>
							    		<option value="28" >系统部驳回</option>
							    		<option value="06" >商品部删除</option>
							    		<option value="26" >系统部删除</option>
							    		<option value="21" >系统部提交</option>
							    		<option value="29" >过期BM删除</option>
									</select>
							    </div>
						    </div><!-- form-group -->
							<div class="form-group" id="">
							    <label for="s_bm_type"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">BM类型</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<select  id="s_bm_type" class="form-control input-sm">
							    		<option value="">-- All/Please Select --</option>
									  	<option value="01">01 捆绑</option>
										<option value="02">02 混合</option>
										<option value="03">03 固定组合</option>
										<option value="04">04 阶梯折扣</option>
										<option value="05">05 AB组</option>
									</select>
							    </div>
							    <label for="s_bm_code"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">BM编码</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	 <input  id="s_bm_code" class="form-control input-sm" type="text" value="" />
							    </div>
							    <label for="s_buyer"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">采购员编码</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<input  id="s_buyer" class="form-control input-sm" type="text" value="" />
							    </div>
						    </div><!-- form-group -->
							<div class="form-group" id="">
							    <label for="s_item"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<input  id="s_item" class="form-control input-sm" type="text" value="" />
							    </div>
						    </div><!-- form-group -->
						    <div class="form-group" id="">
							    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">操作开始日</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<input  id="s_start_date" readonly="true" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">
							    </div>
							    <label for="s_end_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">操作结束日</label>
							    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
							    	<input  id="s_end_date" readonly="true" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">
							    </div>
						    </div><!-- form-group -->
						</div><!-- form-horizontal -->
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
							</div>
						</div>
					</div><!-- box-body  -->
				</div>
			</div>
		</div>
		<div class="row " id="alert_div">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="alert alert-info" style="margin-bottom:0;" role="alert">
					<h5>查询结果中的 “ DPT ” 列不是您DPT时，表示该BM商品为跨部门BM商品。</h5>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="zgGridTtable"></table>
			</div>
		</div>
	</div>
	<input type="hidden" id="searchJson" value=""/>
	[@permission code="P-BM-HIS-020"]<input type="hidden" class="permission-verify" id="p_code_excel" value="${displayVar!}" />[/@permission]
	<!--页脚-->
	[@common.footer/]
</body>
</html>
