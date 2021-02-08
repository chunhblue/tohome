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
    <script src="${m.staticpath}/[@spring.message 'js.bmHisView'/]"></script>
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
			width:50px;
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
		.but-hide{
			display:none;
		}
				/* 小屏幕（平板，大于等于 768px） */
		@media (max-width: 768px) { 
				.my-text-right{
					text-align: right;
				}
		
		}
	</STYLE>
</head>

<body>
	<!--页头-->
	[@common.header/]
	<!--导航-->
	[@common.nav "HOME&其他M管理&BM管理&${pagename!}" /]
	<div class="container-fluid" id="main_box">
		<div class="row" id="base_box">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="box">
				 	<div class="box-title ">
						<div class="tt">
						BM基本信息
						</div>
					</div><!--box-title -end -->
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
									<div class="form-group" id="">
								    	<label for=""  class="col-xs-8 col-sm-2 col-md-2 col-lg-1  control-label">BM类型</label>
								    	<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2 my-text-right">
								    		<p class="text-muted p-line-height bm-code-text" id="bmTypeText">${data.baseBm.bmType!}</p>
								   		 </div>
							    	</div><!-- form-group -->
									<div class="form-group" id="">
								    	<label for=""  class="col-xs-8 col-sm-2 col-md-2 col-lg-1  control-label">BM编码</label>
								    	<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2 my-text-right">
								    		<p class="text-muted p-line-height bm-code-text" id="">${data.baseBm.bmCode!}</p>
								   		 </div>
							    	</div><!-- form-group -->
									<div class="form-group" id="">
								    	<label for=""  class="col-xs-8 col-sm-2 col-md-2 col-lg-1  control-label">销售开始日</label>
								    	<div class="col-xs-4 col-sm-4 col-md-3 col-lg-1 my-text-right">
								    		<p class="text-muted p-line-height bm-code-text" id="startDate">${data.baseBm.bmEffFrom?c}</p>
								   		 </div>
								    	<label for=""  class="col-xs-8 col-sm-2 col-md-2 col-lg-1  control-label">销售结束日</label>
								    	<div class="col-xs-4 col-sm-4 col-md-3 col-lg-1 my-text-right">
								    		<p class="text-muted p-line-height bm-code-text" id="endDate">${data.baseBm.bmEffTo?c}</p>
								   		 </div>
							    	</div><!-- form-group -->
							    	<div class="form-group" id="bm_promotion_box">
									    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">
									    	促销店铺
									   	</label>
									    <div  class="col-xs-12 col-sm-10 col-md-10 col-lg-11 ">
									    	<div id="promotion_show_store" class="clear-div selected-store-box">
									    		[#if data.stroes?exists]
											    	[#list data.stroes as st ]
									    				<span class="text-muted p-line-height">${st!}</span>
													[/#list]
												[/#if]
									    	<div>
									    </div>
								    </div><!-- form-group -->
								</div>
							</div>
						</div>
					</div><!--box-body -end -->
				</div>
			</div>
		</div><!--row -end -->
		<div class="row detail-box" style="margin-top:10px;" id="detail_box">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
				<div class="box">
				 	<div class="box-title ">
						<div class="tt">
						BM商品明细
						</div>
					</div><!--box-title -end -->
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							[#if data.baseBm.bmType=="01" || data.baseBm.bmType=="02" || data.baseBm.bmType=="03"]
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="form-horizontal">
										<div class="form-group" id="">
										    <label for="item_name"  class="col-xs-8 col-sm-2 col-md-2 col-lg-1 control-label">BM数量</label>
										    <div class="col-xs-4 col-sm-10 col-md-10 col-lg-1 my-text-right">
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.bmCount!}</span>
										    </div>
										    <label for="item_name"  class="col-xs-8 col-sm-2 col-md-2 col-lg-1 control-label">BM折扣</label>
										    <div class="col-xs-4 col-sm-10 col-md-10 col-lg-1 my-text-right">
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.bmDiscount!}</span>
										    </div>
										    <label for="item_name"  class="col-xs-8 col-sm-2 col-md-2 col-lg-1 control-label">BM商品价格</label>
										    <div class="col-xs-4 col-sm-10 col-md-10 col-lg-1 my-text-right">
										    	<span id=""  class="text-muted p-line-height bm-code-text">${(data.bmPrice?string('0.0'))!}</span>
										    </div>
									    </div><!-- form-group -->
									</div>
								</div><!-- col-XX-12 -->
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="table-responsive">
										<table class="table  table-condensed table-bordered">
									      <thead>
									        <tr class="table-tt static-color">
									          <th class="no">No</th>
									          <th class="store">店铺号</th>
									          <th class="bm-code">BM编码</th>
									          <th class="bm-price">BM销售价格</th>
									          <th class="item-code">Item Barcode</th>
									          <th class="item-name">商品名称</th>
									          <th class="dpt">DPT</th>
									          <th class="cost-tax">进货单价</th>
									          <th class="price-tax">销售单价</th>
									          <th class="dis-price">折扣销售单价</th>
									          <th class="profit-rate">毛利率%</th>
									          <th class="status">确认状态</th>
									        </tr>
									      </thead>
									      <tbody id="tableTbody"  class="detail-clear-tbody">
									      </tbody>
									    </table>
									 </div>
								</div><!-- col-XX-12 -->
							[#elseif data.baseBm.bmType=="04"]
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="form-horizontal">
										<div class="form-group" id="">
										    <label for="item_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
										    <div class="col-xs-12 col-sm-10 col-md-10 col-lg-11">
										    	<div id="" class="clear-div selected-store-box">
										    		[#if data.items?exists]
														[#list data.items as item ]
														<span class="text-muted p-line-height">${item!}</span>
											    		[/#list]
													[/#if]
										    	</div>
										    </div>
									    </div><!-- form-group -->
										<div class="form-group" id="">
										    <label for="item_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">购买数量</label>
										    <div class="col-xs-12 col-sm-10 col-md-10 col-lg-1">
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.buyCount!}</span>
										    </div>
										    <label for="item_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">折扣</label>
										    <div class="col-xs-12 col-sm-10 col-md-10 col-lg-1">
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.bmDiscount!}</span>
										    </div>
									    </div><!-- form-group -->
									</div>
								</div><!-- col-XX-12 -->
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="table-responsive">
									<!-- 04 -->
										<table class="table  table-condensed table-bordered">
									      <thead>
									         <tr class="table-tt static-color">
									          <th class="no">No</th>
									          <th class="store">店铺号</th>
									          <th class="bm-price">BM销售价格</th>
									          <th class="item-code">Item Barcode</th>
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
									      <tbody id="tableTbody"  class="detail-clear-tbody">
									      </tbody>
									    </table>
									 </div>
								</div><!-- col-XX-12 -->
							[#elseif data.baseBm.bmType=="05"]
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="form-horizontal">
										<div class="form-group" id="">
										    <label for="item_name"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
											 <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
											    <span class="text-muted p-line-height">列表内商品，组合购买A组任意商品</span>
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.numA!}</span>
										    	<span class="text-muted p-line-height">个及B组任意商品</span>
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.numB!}</span>
										    	<span class="text-muted p-line-height">个,折扣</span>
										    	<span id=""  class="text-muted p-line-height bm-code-text">${data.bmDiscount!}</span>
										    	<span class="text-muted p-line-height">%</span>
									    	</div>
									    </div><!-- form-group -->
									</div>
								</div><!-- col-XX-12 -->
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<div class="table-responsive">
									<!-- 05 -->
										<table class="table table-condensed table-bordered " >
											  <caption>AB组商品</caption>
										      <thead>
										       <tr class="table-tt static-color">
										          <th class="no">No</th>
										          <th class="store">店铺号</th>
										          <th class="store">AB组</th>
										          <th class="item-code">Item Barcode</th>
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
									      <tbody id="tableTbody"  class="detail-clear-tbody">
									      </tbody>
									    </table>
									 </div>
								</div><!-- col-XX-12 -->
							[/#if]
						</div>
					</div><!--box-body -end -->
				</div>
			</div>
		</div><!--row -end -->
	</div><!--container-fluid -end -->
	
	<div class="container-fluid" id="">
		<div class="row" id="">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center box-body-padding-10">
				
				[@permission code="P-BM-012"]
					[#if displayVar==1]
						<button id="affirm" type="button" class="btn  btn-warning but-hide" ><span class="glyphicon glyphicon-check"></span> 确认</button>
						<button id="reject_aff" type="button" class="btn  btn-danger but-hide"><span class="glyphicon glyphicon-share"></span> 驳 回</button>
						<input type="hidden" id="pass" value="07"/>
						<input type="hidden" id="notPass" value="08"/>
					[/#if]
				[/@permission]
				[@permission code="P-BM-021"]
					[#if displayVar==1]
						<button id="check" type="button" class="btn  btn-primary but-hide"><span class="glyphicon glyphicon-edit"></span> 审 核</button>
						<button id="reject" type="button" class="btn  btn-danger but-hide"><span class="glyphicon glyphicon-share"></span> 驳 回</button>
						<input type="hidden" id="pass" value="17"/>
						<input type="hidden" id="notPass" value="18"/>
					[/#if]
				[/@permission]
				[@permission code="P-BM-032"]
					[#if displayVar==1]
						<button id="check" type="button" class="btn  btn-primary but-hide"><span class="glyphicon glyphicon-edit"></span> 审 核</button>
						<button id="reject" type="button" class="btn  btn-danger but-hide"><span class="glyphicon glyphicon-share"></span> 驳 回</button>
						<input type="hidden" id="pass" value="27"/>
						<input type="hidden" id="notPass" value="28"/>
					[/#if]
				[/@permission]
				<button id="returnsViewBut" type="button" class="btn btn-default"><span class="glyphicon glyphicon-share icon-right"></span>返回</button>
			</div><!--row -end -->
		</div>
	</div><!--container-fluid -end -->
		<!--reject_dialog 分割-->
	<div id="reject_dialog" class="modal fade " data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
		  <div class="modal-dialog " role="document">
		    <div class="modal-content">
		     <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="">请输入驳回理由</h4>
		      </div>
		      <div class="modal-body">
			     <textarea id="rejectreason" class="form-control" rows="3"></textarea>
		      </div>
		      <div class="modal-footer">
		    	<button id="reject_dialog_cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
			    <button id="reject_dialog_affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>确认</button>
		      </div>
		    </div>
		  </div>
		</div>
	<!--reject_dialog 分割-->
	<div id="table_hidden_input" style="display:none" >
	[#if data.bmItemInput?exists]
		[#list data.bmItemInput as bmItem ]
		<input type="text" 
			id="in_${bmItem.id!''}" 
			store="${bmItem.store!''}" 
			bmcode="${bmItem.bmcode!''}" 
			itemsystem="${bmItem.itemsystem!''}" 
			itemcode="${bmItem.itemcode!''}" 
			itemname="${bmItem.itemname!''}" 
			dpt="${bmItem.dpt!''}" 
			costtax="${bmItem.costtax?string('0.00')!}" 
			pricetax="${bmItem.pricetax?string('0.00')!}" 
			status="${bmItem.status!''}" 
			bmprice="${(bmItem.bmprice?string('0.00'))!''}" 
			disprice="${bmItem.disprice?string('0.00')!}" 
			profitrate="${bmItem.profitrate?string('0.00')!}" 
			buycount="${bmItem.buycount?string('0.#')!}" 
			discount="${bmItem.discount?string('0.#')!}" 
			ab="${bmItem.ab?upper_case!''}" 
			value="${bmItem.value!}" />
		[/#list]
	[/#if]
	</div>
	<input type="hidden" id="toKen" value="${toKen}"/>
	<input type="hidden" id="bmCode" value="${data.baseBm.bmCode!}" />
	<input type="hidden" id="bmType" value="${data.baseBm.bmType!}" />
	<input type="hidden" id="promotion_store" value="${data.stroeStr!}" />
	<!--页脚-->
	[@common.footer/]
</body>
</html>
