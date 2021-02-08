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
    <script src="${m.staticpath}/[@spring.message 'js.bmEdit'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
	</script>
	<STYLE>
		.input-d-w{
			width:50px;
			text-align: center;
		}
		.permission-verify-div{
			display:none;
		}
		.selected-store-box{
			overflow:hidden;
		}
		.selected-store-box .text-muted{
			    display: block;
			    float: left;
			    text-align: left;
		}
		.selected-store-box .text-muted + .text-muted:before{
			    padding: 0 5px;
			    color: #3b78ad;
			    content: "，";
			    font-weight: 900;
		}
		caption {
		    color: #337ab7;
		    font-size: 16px;
		    font-weight: 900;
		}
		.bm-code-text{
		}
		.good-select .form-control{
			padding-right:35px;
		}
		.good-select .item-select-but{
			width:30px;
			right:1px;
		}
		tr.table-tt th{
			text-align:center;
		}
		td.text-c{
			text-align:center;
		}
		td.text-l{
			text-align:left;
		}
		td.text-r{
			text-align:right;
		}
		th.no{
			width:30px;
		}
		th.store{
			width:50px;
		}
		th.bm-code{
			width:100px;
		}
		th.bm-price{
			width:100px;
		}
		th.item-code{
			width:100px;
		}
		th.item-name{
			width:250px;
		}
		th.dpt{
			width:50px;
		}
		th.cost-tax{
			width:70px;
		}
		th.price-tax{
			width:70px;
		}
		th.dis-price{
			width:100px;
		}
		th.profit-rate{
			width:80px;
		}
		th.status{
			width:80px;
		}
		td.text-blue{
			color:#3b78ad;
		}
		td.text-red{
			color:red;
		}
		.alert-box{
			margin-bottom:10px;
		}
		.vice-but-box{
			margin-top:10px;
			margin-bottom:10px;
		}
		/* 小屏幕（平板，大于等于 768px） */
		@media (max-width: 768px) { 
			.label-index{
				margin-bottom:5px;
			}
			.xs-top{
				margin-top:5px;
			}
		}
	</STYLE>
</head>

<body>

	<div class="error-pcode" id="error_pcode">
		<div class="error-pcode-text"> ${useMsg!} </div>
		<div class="shade"></div>
	</div>
	<!--页头-->
	[@common.header /]
	<!--导航-->
	[@common.nav "HOME&其他M管理&BM管理&${pagename!}" /]
	<div class="container-fluid alert-box" id="alert_div_box">
		<div class="row ">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="alert alert-info alert-div" role="alert" id="alert_1">
					<h5>※ 新签约在实施日前2天提交。</h5>
					<h5>※ 跨部门在实施日前3天提交。</h5>
					<h5>※ 同一商品不能同时参加两个或多个BM销售。</h5>
					<h5>※ 一组BM号码最多只能登记32个单品。</h5>
					<h5>※ BM与店铺的满百省不能同时进行，如有冲突以店铺活动优先，BM销售将不会生效。</h5>
					<h5>※ BM商品不能做时间段变价。</h5>
					<h5>※ 过期BM数据会由系统部定期清理。</h5>
					<h5>※ 手工删除BM应在实施日前两天提交。</h5>
					<h5>※ BM商品销售单价以画面显示为准。</h5>
				</div>
				<div class="alert alert-info alert-div" role="alert" id="alert_2">
					<h5>※ 修改BM价格、期间应在实施日期前1天提交。</h5>
					<h5>※ BM商品销售单价以画面显示为准。</h5>
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid" id="main_box">
		<div class="row" id="base_box">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="box">
				 	<div class="box-title ">
						<div class="tt">
						BM基本信息
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
									<div class="form-group" id="bm_type_box">
								    	<label for="bm_type_select"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2  control-label not-null">BM类型</label>
								    	<div class="col-xs-8 col-sm-4 col-md-3 col-lg-2">
									    		<select id="bm_type" befValue="" class="form-control input-sm">
												  	<option value="">-- 请选择 --</option>
												  	<option value="01">01 捆绑</option>
													<option value="02">02 混合</option>
													<option value="03">03 固定组合</option>
													<option value="04" >04 阶梯折扣</option>
													<option value="05" >05 AB组</option>
												</select>
								   		 </div>
							    	</div><!-- form-group -->
									<div class="form-group" id="bm_code_box">
								    	<label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-2  control-label not-null">BM编码</label>
								    	<div class="col-xs-8 col-sm-4 col-md-3 col-lg-2">
								    		<p class="text-muted p-line-height bm-code-text" id="bm_code_p"></p>
								    		<input type="hidden" id="bm_code" value="" />
								   		 </div>
							    	</div><!-- form-group -->
							    	<div class="form-group" id="bm_date_box">
									    <label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">销售开始日</label>
									    <div class="col-xs-8 col-sm-3 col-md-2 col-lg-2 label-index">
												<input id="start_date" readonly="true" placeholder="点击选择Start Time" class="form-control input-sm select-date clear-input" type="text" value=""/>
									    </div>
									    
									    <label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">销售结束日</label>
									    <div class="col-xs-8 col-sm-3 col-md-2 col-lg-2 label-index">
									    		<input  id="end_date" readonly="true" placeholder="点击选择结束日期" class="form-control input-sm select-date clear-input" type="text" value=""/>
									    </div>
								    </div><!-- form-group -->
							    	<div class="form-group" id="bm_promotion_box">
									    <label for=""  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">
									    	促销店铺
									   	</label>
									    <div class="col-xs-8 col-sm-10 col-md-10 col-lg-2">
									    	<button id="select_store_but" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-zoom-in icon-right"><span>	
									    		选择店铺
									    	</button>
									    	<input type="hidden" id="promotion_store" class="clear-input" value="" />
									    </div>
									    <div  class="col-xs-12 col-sm-10 col-md-10 col-lg-10 col-lg-offset-2 col-md-offset-2 col-sm-offset-2">
									    	<div id="promotion_show_store" class="clear-div selected-store-box">
									    	<div>
									    </div>
								    </div><!-- form-group -->
							    </div><!-- form-horizontal -->
							</div>
						</div>
					</div>
				</div><!--box 分割-->
			</div>
		</div><!--row 分割-->
		<div class="row detail-box" id="detail_box">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center vice-but-box">
				<button id="submitBut2" type="button" class="btn btn-primary submitBut"><span class="glyphicon glyphicon-ok icon-right"></span>提交</button>
				<button id="returnsViewBut2" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>返回</button>
			</div>
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="box">
				 	<div class="box-title ">
						<div class="tt">
						BM商品明细
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal" id="edit_detail_box">
									<div class="form-group" id="detail_goods_code_box">
									    <label for="input_item"  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label">Item Barcode</label>
									    <div class="col-xs-8 col-sm-4 col-md-3 col-lg-2">
									    	<div class="aotu-pos good-select">
											    <input value="" type="text" maxlength="13" class="form-control my-automatic input-sm detail-clear-input" id="input_item" placeholder="请输入完整的Item Barcode">
										        <a id="search_item_but"  href="javascript:void(0);" title="点击查找商品" class="auto-but glyphicon glyphicon-zoom-in  item-select-but "></a>
									    	</div>
									    </div>
									    <div class="col-xs-4 col-sm-2 col-md-1 col-lg-2" id="add_item_box">
									   		<button id="add_item_but" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-plus icon-right"><span>	
									    		单品追加
									    	</button>
									    </div>
									    <div class="col-xs-4 col-sm-2 col-md-1 col-lg-2" id="affirm_item_box">
									   		<button id="affirm_item_but" type="button" class="btn btn-primary btn-sm " >
									    		<span class="glyphicon glyphicon-ok icon-right"><span>	
									    		确认商品
									    	</button>
									    </div>
									    <div class="col-xs-12 col-sm-2 col-md-1 col-lg-8 xs-top" id="add_ab_group_box">
									   		<button id="add_a_but_but" grouptype="A" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-plus icon-right"><span>	
									    		添加为 A组 商品
									    	</button>
									   		<button id="add_b_but_but" grouptype="B" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-plus icon-right"><span>	
									    		添加为 B组 商品
									    	</button>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="item_name_box">
									    <label for="item_name"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label">商品名称</label>
									    <div class="col-xs-8 col-sm-10 col-md-10 col-lg-10">
									    	<span id="item_name"  class="text-muted p-line-height bm-code-text"></span>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="bm_count_box">
									    <label for="bm_count"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">BM数量</label>
									    <div class="col-xs-8 col-sm-1 col-md-1 col-lg-2">
									    	<input type="text" maxlength="2" id="bm_count" class="form-control input-sm detail-clear-input">
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="discounts_box">
									    <label for="select_discounts"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">优惠方法</label>
									    <div class="col-xs-8 col-sm-3 col-md-2 col-lg-2">
									    	<select id="select_discounts" class="form-control input-sm detail-clear-select">
											  	<option value="">-- 请选择 --</option>
											  	<option value="1">折扣</option>
												<option value="2">价格</option>
											</select>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="bm_dis_box">
									    <label for="bm_dis"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">BM折扣</label>
									    <div class="col-xs-3 col-sm-1 col-md-1 col-lg-2">
									    	<input type="text" id="bm_dis" maxlength="5" class="form-control input-sm detail-clear-input">
									    </div>
									    <div class="col-xs-5 col-sm-1 col-md-1 col-lg-2">
									    	<button id="bm_dis_but" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-refresh icon-right"><span>	
									    		计算折扣价格
									    	</button>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="bm_price_box">
									    <label for="bm_price"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label not-null">BM商品价格</label>
									    <div class="col-xs-3 col-sm-1 col-md-1 col-lg-2">
									    	<input type="text" id="bm_price" class="form-control input-sm detail-clear-input">
									    </div>
									     <div class="col-xs-5 col-sm-3 col-md-2 col-lg-2">
									    	<button id="bm_price_but" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-refresh icon-right"><span>	
									    		更新BM价格
									    	</button>
									    </div>
								    </div><!-- form-group -->
								    <div class="form-group" id="buy_count_discount_box">
									    <label for="buy_count"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label">购买数量</label>
									    <div class="col-xs-8 col-sm-1 col-md-1 col-lg-2 label-index">
									    	<input id="buy_count" type="text" class="form-control input-sm detail-clear-input">
									    </div>
									    <label for="discount_rate"  class="col-xs-4 col-sm-2 col-md-2 col-lg-2 control-label">折扣</label>
									    <div class="col-xs-8 col-sm-1 col-md-2 col-lg-2 label-index">
									    	<input id="discount_rate" maxlength="2" type="text" class="form-control input-sm detail-clear-input">
									    </div>
									    <div class="col-xs-12 col-sm-3 col-md-2 col-lg-2">
									    	<button id="discount_rate_but" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-plus icon-right"><span>	
									    		添加折扣
									    	</button>
									    </div>
								    </div><!-- form-group -->
								    <div class="form-group" id="atwill_item_a_b_box">
									    <label for="atwill_item_a_count"  class="col-xs-12 col-sm-12 col-md-2 col-lg-2 control-label"></label>
									    <div class="col-xs-12 col-sm-8 col-md-8 col-lg-5">
									    	<span class="text-muted p-line-height">列表内商品，组合购买A组任意商品</span>
									    	<input id="atwill_item_a_count" class="form-control simulate-form-control input-sm input-d-w detail-clear-input" />
									    	<span class="text-muted p-line-height">个及B组任意商品</span>
									    	<input id="atwill_item_b_count" class="form-control simulate-form-control input-sm input-d-w detail-clear-input" />
									    	<span class="text-muted p-line-height">个,折扣</span>
									    	<input id="detail_a_b_discount" maxlength="2" class="form-control simulate-form-control input-sm input-d-w detail-clear-input" />
									    	<span class="text-muted p-line-height">%</span>
									    </div>
									     <div class="col-xs-12 col-sm-4 col-md-2 col-lg-2 xs-top">
									    	<button id="atwill_item_a_b_but" type="button" class="btn btn-primary btn-sm">
									    		<span class="glyphicon glyphicon-refresh icon-right"><span>	
									    		计算折扣价格
									    	</button>
									    </div>
								    </div><!-- form-group -->
								</div><!--form-horizontal 分割-->
								<div class="form-horizontal" style="display:none;" id="view_detail_box">
									<div style="display:none;" id="view_detail_box_123">
										<div class="form-group" >
										    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label ">BM数量</label>
										    <div class="col-xs-12 col-sm-1 col-md-1 col-lg-2">
										    	<input type="text" id="view_bm_count" class="form-control input-sm">
										    </div>
										    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label ">BM折扣</label>
										    <div class="col-xs-12 col-sm-1 col-md-1 col-lg-2">
										    	<input type="text" id="view_bm_discount" class="form-control input-sm">
										    </div>
										    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-2 control-label ">BM商品价格</label>
										    <div class="col-xs-12 col-sm-1 col-md-1 col-lg-2">
										    	<input type="text" id="view_bm_price" class="form-control input-sm">
										    </div>
									    </div><!-- form-group -->
									</div>
									<div style="display:none;" id="view_detail_box_4">
										<div class="form-group" >
										</div>
									</div>
									<div style="display:none;" id="view_detail_box_5">
										<div class="form-group" >
										</div>
									</div>
								</div><!--form-horizontal 分割-->
							</div>
						</div><!--box-body > row 分割-->
						<div class="wire"></div>
						<div class="row" id="detail_table_123_box">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
								<div class="table-responsive">
									<table class="table  table-condensed table-bordered" id="detail_table_123">
								      <thead>
								        <tr class="table-tt static-color">
								          <th class="no">No</th>
								          <th class="store">店铺号</th>
								          <th class="bm-code">BM编码</th>
								          <th class="bm-price">BM销售价格</th>
								          <th class="item-code">Item Barcode</th>
								          <th class="no">&nbsp;</th>
								          <th class="item-name">商品名称</th>
								          <th class="dpt">DPT</th>
								          <th class="cost-tax">进货单价</th>
								          <th class="price-tax">销售单价</th>
								          <th class="dis-price">折扣销售单价</th>
								          <th class="profit-rate">毛利率%</th>
								          <th class="status">确认状态</th>
								        </tr>
								      </thead>
								      <tbody id="detail_table_123_tbody" class="detail-clear-tbody">
								      </tbody>
								    </table>
								 </div>
							</div>
						</div><!--box-body > row 2 分割-->
						<div class="row" id="detail_table_4_box">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="table-responsive">
									<table class="table table-condensed table-bordered" id="detail_table_4">
								      <thead>
									      <tr class="table-tt static-color">
									          <th class="no">No</th>
									          <th class="store">店铺号</th>
									          <th class="bm-price">BM销售价格</th>
									          <th class="item-code">Item Barcode</th>
									          <th class="no">&nbsp;</th>
									          <th class="item-name">商品名称</th>
									          <th class="dpt">DPT</th>
									          <th class="cost-tax">进货单价</th>
									          <th class="price-tax">销售单价</th>
									          <th class="price-tax">购买数量</th>
									          <th class="price-tax">折扣%</th>
									          <th class="dis-price">折扣销售单价</th>
									          <th class="profit-rate">毛利率%</th>
									          <th class="status">确认状态</th>
									        </tr>
								      </thead>
								      <tbody class="detail-clear-tbody" id="detail_table_4_tbody">
								        
								      </tbody>
								    </table>
								</div>
							</div>
						</div><!--box-body > row 3 分割-->
						<div class="row" id="detail_table_5_box">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="table-responsive">
									<table class="table table-condensed table-bordered " id="detail_table_5">
									  <caption>AB组商品</caption>
								      <thead>
								       <tr class="table-tt static-color">
								          <th class="no">No</th>
								          <th class="store">店铺号</th>
								          <th class="store">AB组</th>
								          <th class="item-code">Item Barcode</th>
								          <th class="no">&nbsp;</th>
								          <th class="item-name">商品名称</th>
								          <th class="dpt">DPT</th>
								          <th class="cost-tax">进货单价</th>
								          <th class="price-tax">销售单价</th>
								          <th class="price-tax">购买数量</th>
								          <th class="price-tax">折扣%</th>
								          <th class="dis-price">折扣销售单价</th>
								          <th class="profit-rate">毛利率%</th>
								          <th class="status">确认状态</th>
								        </tr>
								      </thead>
								      <tbody class="detail-clear-tbody" id="detail_table_5_tbody">
								       
								      </tbody>
								    </table>
								</div>
							</div>
						</div><!--box-body > row 4 分割-->
					</div>
				</div><!--box 分割-->
			</div>
		</div><!--row 分割-->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
				<button id="submitBut" type="button" class="btn btn-primary submitBut"><span class="glyphicon glyphicon-ok icon-right"></span>提交</button>
				<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>返回</button>
			</div>
		</div><!--row 分割-->
	</div>
	[@common.selectStore "selectStoreDiv",IyStorelist! /]
	<!--页脚-->
	[@common.footer/]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="user_id" value="${userId!}"/>
	<input type="hidden" id="user_stroe" value="${userStore!}"/>
	<input type="hidden" id="user_dpt" value="${userDpt!}"/>
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="newNo" value=""/>
	<input type="hidden" id="my_dpt_flg" value=""/>
	<!--操作人 身份 1：采购样式，2:大事业部(商品部长)部长 3：系统部，4：店铺样式-->
	<input class="hidden" id="identity" value="${identity!}" />
	<!--画面类型 0:Add ；1修改  2审核 3查看-->
	<input type="hidden" id="page_type" value="${pageType!}"/>
	<!--画面类型 0:Add ；1修改  2审核 3查看-->
	<input type="hidden" id="dptFlg" value=""/>
	<!--当前日期-->
	<input type="hidden" id="now_date" value="20191021"/>
	<!--04类型使用的单品数量-->
	<input type="hidden" id="addDisCount_04" value="0" />
	<!--单品条码集合逗号分割-->
	<input type="hidden" id="items_input_list" />
	<!--05 单品条码A组集合逗号分割-->
	<input type="hidden" id="items_input_a_list" />
	<!--05 单品条码B组集合逗号分割-->
	<input type="hidden" id="items_input_b_list" />
	<div  id="table_hidden_input"></div>
</body>
</html>
