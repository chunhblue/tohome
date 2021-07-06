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
    <script src="${m.staticpath}/[@spring.message 'js.bankPayIn'/]"></script>
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
<!--页头-->
[@common.header][/@common.header]
</head>

<body>

[#--   <div class="error-pcode" id="error_pcode">--]
[#--      <div class="error-pcode-text"> ${useMsg!} </div>--]
[#--      <div class="shade"></div>--]
[#--   </div>--]
	<!--导航-->
	[@common.nav "HOME&Sale&Bank Deposit"][/@common.nav]
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
                                    <div class="form-group">
                                        <label for="aRegion" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Region</label>[#-- 大区 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aRegion">
                                                <a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label for="aCity" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">City</label>[#-- 城市 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aCity">
                                                <a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
[#--                                    </div>--]
[#--									<div class="form-group ">--]
                                        <label for="aDistrict" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">District</label>[#-- 区域 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aDistrict">
                                                <a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
										<label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>[#-- 店铺 --]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="aStore">
												<a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
									</div>
									<div class="form-group ">
[#--                                    </div>--]
[#--									<div class="form-group">--]
										<label for="bs_start_date"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null" style="white-space:nowrap">Business Date</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
[#--											<input id="bs_start_date" readonly="true" nextele="sale_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">--]
											<input id="bs_start_date"  nextele="sale_end_date" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
										</div>
										<label for="bs_end_date" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null"></label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<input id="bs_end_date"  placeholder="End Date" class="form-control input-sm select-date" type="text" value="">

[#--											<input  id="bs_end_date" readonly="true" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">--]
										</div>
										<label for="s_payPerson"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Manager</label>[#--缴款人--]
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<select id="s_payPerson" class="form-control input-sm">
												<option value=""></option>
											</select>
										</div>
[#--										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-1">--]
[#--										<button id="clear_bs_date" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear</button>--]
[#--										</div>--]
								</div><!-- form-group -->
								    <div class="form-group">

									</div><!-- form-group -->
								    <div class="form-group">
										<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Area Manager</label>
										<div class="col-sm-2">
											<div class="aotu-pos">
												<input type="text"
													   class="form-control my-automatic input-sm" autocomplete="off"
													   id="am">
												<a id="amRefresh" href="javascript:void(0);" title="Refresh"
												   class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="amRemove" href="javascript:void(0);"
												   title="Clear"
												   class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Operation Manager</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="om">
												<a id="omRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="omRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
										<label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Operation Controller</label>
										<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
											<div class="aotu-pos">
												<input type="text" class="form-control my-automatic input-sm" id="oc">
												<a id="ocefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
												<a id="ocRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
											</div>
										</div>
									</div>


								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="wire"></div>
								<button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
								<button id='reset' type='button' class='btn  btn-primary btn-sm ' ><span class='glyphicon glyphicon-refresh'></span>Reset</button>
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
	<div id="bankPayIn_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
		<div class="modal-dialog modal-md" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Bank Deposit Detail
					</h4>
				</div>
				<div class="modal-body">
					<div class="form-horizontal">
						<div class="form-group">
							<label for="i_a_store"  class="col-sm-4 control-label not-null">Store</label>
							<div class="col-sm-5">
								<div class="aotu-pos" id="add_store">
									<input type="text" class="form-control my-automatic input-sm" id="i_a_store" placeholder="" autocomplete="off">
									<a id=""  href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
									<a id="i_a_store_clear"  href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
								</div>
								<div id="update_store">
									<input type="text" readonly="readonly" class="form-control my-automatic input-sm" id="update_store_name">
								</div>
							</div>

						</div>
						<div class="form-group">
							<label for="bs_date"  class="col-sm-4 control-label not-null">Business Date</label>
							<div class="col-sm-5">
								<input id="bs_date"  placeholder="Business Date" class="form-control input-sm select-date" type="text" value="">
							</div>
						</div>
						<div class="form-group">
							<label for="depositDate"  class="col-sm-4 control-label not-null">Deposit Date</label>
							<div class="col-sm-5">
								<input id="depositDate"  placeholder="Deposit Date" class="form-control input-sm select-date" type="text" value="">
							</div>
						</div>
						<div class="form-group">
							<label for="payPerson"  class="col-sm-4 control-label not-null">Store Manager</label>[#--缴款人--]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<select id="payPerson" disabled="disabled" class="form-control input-sm"  >
									</select>
								</div>
                                <div id="update_payPerSon">
                                    <input type="text" readonly="readonly" class="form-control my-automatic input-sm" id="update_payPerSon_name">
								
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="payAmt"  class="col-sm-4 control-label not-null">Bank Deposit Amount</label>[#--银行缴款金额--]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<input type="text" class="form-control my-automatic input-sm" id="payAmt" placeholder="">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="description"  class="col-sm-4 control-label">Remarks</label>[#--备注--]
							<div class="col-sm-5">
								<div class="aotu-pos">
									<textarea id="description" class="form-control" rows="5"></textarea>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="deltaNo" value="" />
					<button id="cancel" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
					<button id="attachments" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon glyphicon-file"></span>Attachments</button>[#--附件--]
					<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
				</div>
			</div>
		</div>
	</div>
	<!--附件一览-->
	[@common.attachments][/@common.attachments]
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="toKen" value="${toKen!}"/>
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="entiretyBmStatus" value=""/>
	<input type="hidden" id="tempTableType" value="-1"/>
	<input type="hidden" id="searchJson" value=""/>
     <input type="hidden" id="businessDate" value="${businessDate!}"/>
     <input type="hidden" id="msg" value=""/>
	[@permission code="SC-PI-001"]<input type="hidden" class="permission-verify" id="addBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-PI-002"]<input type="hidden" class="permission-verify" id="editBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-PI-003"]<input type="hidden" class="permission-verify" id="delBut" value="${displayVar!}" />[/@permission]
</body>
</html>
