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
    <script src="${m.staticpath}/[@spring.message 'js.bm'/]"></script>
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
</head>

<body>
	
	<div class="error-pcode" id="error_pcode">
		<div class="error-pcode-text"> ${useMsg!} </div>
		<div class="shade"></div>
	</div>
	<!--页头-->
	[@common.header][/@common.header]
	<!--导航-->
	[@common.nav "HOME&其他M管理&BM管理"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				 <div class="box">
				 	<div class="box-title ">
						<div class="tt">
							Query Condition
						</div>
					</div>
					<div class="box-body box-body-padding-10" style="">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-horizontal">
									<div id="show_user_info" class=""><!-- 只有采购和店铺显示此模块 -->
										<div class="form-group">
										    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
										    	<div class="my-user-info">
											    	<span class="my-label">员工编码</span>
											    	<span class="my-text">${bmUserId!}</span>
											    	<div class="my-div" id="show_div">
											    		<span class="my-label">事业部</span>
											    		<span class="my-text">${div!} ${divName!}</span>
											    		<span class="my-label">部</span>
											    		<span class="my-text">${dep!} ${depName!}</span>
												    </div>
												     <div class="my-div" id="show_store">
												     	<span class="my-label">店铺</span>
											    		<span class="my-text">${store!} ${storeName!}</span>
													 </div>
										    	</div>
										    </div>
									    </div><!-- form-group -->
								  	  <div class="wire"></div>
									</div>
									<div class="form-group" id="show_status">
									    <label for="iyPost"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">BM商品状态</label>
									    <div class="col-xs-11 col-sm-10 col-md-10 col-lg-11 col-xs-offset-1  col-sm-offset-0  col-md-offset-0 col-lg-offset-0 ">
									    	<div class="radio-inline " id="show_status_all">
											  <label class="">
											    <input type="radio" name="bm_status" id="bm_status_1"  value="1">
											    全部
											  </label>
											</div>
											<div class="radio-inline">
											  <label>
											    <input type="radio" name="bm_status" id="bm_status_2" nextele="bm_status_3" value="2">
											     新签约
											  </label>
											</div>
											<div class="radio-inline">
											  <label>
											    <input type="radio" name="bm_status" id="bm_status_3" nextele="bm_status_4"  value="3"  />
											     已生效
											  </label>
											</div>
											<div class="radio-inline" id="show_status_del">
											  <label>
											    <input type="radio" name="bm_status" id="bm_status_4" nextele="bm_status_5" value="4">
											     手工删除
											  </label>
											</div>
									    	<div class="radio-inline">
											  <label>
											    <input type="radio" name="bm_status" id="bm_status_5" value="5">
											    修改
											  </label>
											  <select id="bm_status_5_select" style="width:150px;" nextele="bm_status_6" class="form-control radio-select-inline">
													  <option value="">-- 请选择 --</option>
													  <option value="0">修改BM价格/折扣</option>
													  <option value="1">期间延长</option>
													  <option value="2">修改价格和期间</option>
											  </select>
											</div>
											<div class="radio-inline" id="show_status_reject">
											  <label>
											    <input type="radio" name="bm_status" id="bm_status_6" value="6" nextele="bm_division">
											     已驳回
											  </label>
											</div>
		    							</div>
									</div><!-- form-group -->
									<div class="form-group" id="show_select_div">
									    <label for="bm_division"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">事业部</label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<select id="bm_division" class="form-control input-sm" nextele="bm_department">
											  	<option value="" selected>-- All/Please Select --</option>
											  	[#if division?exists]
												  	[#list division as item ]
								                        <option value="${item.k}">${item.k} ${item.v}</option>
													[/#list]
												[/#if]
											</select>
										</div>
									</div><!-- form-group -->
									<div class="form-group"id="show_select_dpt">
									    <label for="bm_department"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">DPT</label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<select id="bm_department" class="form-control input-sm" nextele="bm_be_part">
											  	<option value="">-- All/Please Select --</option>
											</select>
										</div>
									</div><!-- form-group -->
									<div class="form-group" id="bm_be_part_div">
									    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">BM所属</label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<select  id="bm_be_part" class="form-control input-sm" nextele="bm_type">
											  <option value="0" select>自部门</option>
											  <option value="1">跨部门</option>
											</select>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="bm_type_box">
									    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1  control-label">BM类型</label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<select id="bm_type" class="form-control input-sm" nextele="show_bm_code_input">
											  	<option value="">-- All/Please Select --</option>
											  	<option value="01">01 捆绑</option>
												<option value="02">02 混合</option>
												<option value="03">03 固定组合</option>
												<option value="04">04 阶梯折扣</option>
												<option value="05">05 AB组</option>
											</select>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group" id="">
									    <label for="show_bm_code_radio"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">
										     BM编码
									     </label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-1">
									    	<input id="show_bm_code_input" type="text"  maxlength="3" class="form-control input-sm" nextele="input_item">
									    </div>
								    </div>
								    <div class="form-group" id="bm_item_code_box">
									    <label for="bm_item"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">
									     	Item Barcode
									     </label>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<div class="aotu-pos good-select">
											    <input value="" status="0" type="text" maxlength="13" nextele="sell_start_date" class="form-control my-automatic input-sm detail-clear-input" id="input_item" placeholder="请输入完整的Item Barcode">
										        <a id="search_item_but" href="javascript:void(0);" title="点击查找商品" class="auto-but glyphicon glyphicon-zoom-in  item-select-but "></a>
									    	</div>
									    </div>
									    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									    	<span id="item_name"  class="text-muted p-line-height bm-code-text"></span>
									    </div>
								    </div><!-- form-group -->
									<div class="form-group">
									    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">销售日</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="sell_start_date" readonly="true" nextele="sell_end_date" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">
									    </div>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
									    	<input  id="sell_end_date" readonly="true" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">
									    </div>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-1">
									    	<button id="clear_sell_date" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear</button>
									    </div>
								    </div><!-- form-group -->
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
								[@permission code="P-BM-011"]
									[#if displayVar==1]
										<button id='add' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-plus'></span> 新 增</button>
										<button id='up' type='button' class='btn  btn-warning   btn-sm ' ><span class='glyphicon glyphicon-pencil'></span> 修 改</button>
									[/#if]
								[/@permission]
								[@permission code="P-BM-031"]
									[#if displayVar==1]
										<button id='add' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-plus'></span> 新 增</button>
										<button id='up' type='button' class='btn  btn-warning   btn-sm ' ><span class='glyphicon glyphicon-pencil'></span> 修 改</button>
									[/#if]
								[/@permission]
								[@permission code="P-BM-034"]
									[#if displayVar==1]
										<button id='listDel' type='button' class='btn  btn-danger   btn-sm ' ><span class='glyphicon glyphicon-th-list'></span> 过期BM批量删除</button>
									[/#if]
								[/@permission]
							</div>
						</div>
					</div>
				 </div><!--box end-->
			 </div>
		 </div>
		 <!--row 分割-->
		<div class="row alert-div-box" id="alert_div">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="alert alert-info alert-div" role="alert">
					<h5>您今天有<strong id="checkCount"> ${checkCount!} </strong>条BM商品需要审核，其中紧急的有<strong id="urgencyCount"> ${urgencyCount!} </strong>条</h5>
					<h5>* 注意：两天后开始生效的BM商品，今天必须审核完成！</h5>
					<h5>* 修改BM当日必须审核完。</h5>
				</div>
			</div>
		</div>
		 <!--row 分割-->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="zgGridTtable"></table>
			</div>
		</div>
		 <!--row 分割-->
		<div class="row del-alert" id="del_alert">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="alert alert-info" role="alert">
				  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
				 <strong> 删除时，需先选中某条记录后，再点击删除按钮进行删除</strong>
				</div>
			</div>
		</div>
		<!--row 分割-->
	 </div>
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
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="store" value="${store!}"/>
	<input type="hidden" id="checkResources" value="${checkResources!}"/>
	<input type="hidden" id="entiretyBmStatus" value=""/>
	<input type="hidden" id="tempTableType" value="-1"/>
	<input type="hidden" id="searchJson" value=""/>
	<div style="display:none;" id="permission_verify_div">
		[@permission code="P-BM-001"]<input class="permission-verify" id="p_code_excel" value="${displayVar!}" />EXCEL导出权限[/@permission]

		[@permission code="P-BM-010"]<input flg="1" class="permission-verify" id="p_code_buyer" value="${displayVar!}" />-采购[/@permission]
		[@permission code="P-BM-014"]<input class="permission-verify" id="p_code_buyer_del" value="${displayVar!}" />-采购删除[/@permission]
		
		[@permission code="P-BM-020"]<input flg="2" class="permission-verify" id="p_code_div_ephor" value="${displayVar!}" />-事业部长[/@permission]
		[@permission code="P-BM-021"]<input class="permission-verify" id="p_code_div_check" value="${displayVar!}" />-事业部长-审核驳回[/@permission]
		
		[@permission code="P-BM-030"]<input flg="3" class="permission-verify" id="p_code_sys" value="${displayVar!}" />-系统部[/@permission]
		[@permission code="P-BM-032"]<input class="permission-verify" id="p_code_sys_check" value="${displayVar!}" />-系统部-审核驳回[/@permission]
		[@permission code="P-BM-033"]<input class="permission-verify" id="p_code_sys_del" value="${displayVar!}" />-系统部-删除[/@permission]
		
		[@permission code="P-BM-040"]<input flg="4" class="permission-verify" id="p_code_store" value="${displayVar!}" />-店铺[/@permission]
	</div>
</body>
</html>
