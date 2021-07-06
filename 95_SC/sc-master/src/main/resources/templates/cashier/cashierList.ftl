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
    <script src="${m.staticpath}/[@spring.message 'js.cashier'/]"></script>
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
	[@common.nav "HOME&Sale&Cashier Management"][/@common.nav]
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
									    <label for="cashierId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Cashier ID</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="cashierId" class="form-control input-sm">
										</div>
										<label for="cashierName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Cashier Name</label>
										<div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input type="text" id="cashierName" class="form-control input-sm">
										</div>
									</div><!-- form-group -->
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
    [#-- 收银员资料维护 --]
    <div id="cashier_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Cashier Profile Maintenance</h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal" id="cashierform">
                        <div class="form-group">
                            <label for="info_store" class="col-sm-3 control-label not-null">Store</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class=" form-control my-automatic input-sm" id="info_store" autocomplete="off" name="info_store" required>
                                    <a id="info_store_refresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                    <a id="info_store_clear" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="i_cashierId"  class="col-sm-3 control-label not-null">Cashier No.</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="i_cashierId" placeholder="" name="cashierId">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="i_cashierName" class="col-sm-3 control-label not-null">Cashier Name
                            </label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="i_cashierName" placeholder="" name="cashierName">
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="ini_password">
                            <label for="inital_password" class="col-sm-3 control-label not-null">Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="inital_password" autocomplete="off" name="initalPassword">
                                    <span>(Please enter 1-8 digit.)</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="i_cashierEmail" class="col-sm-3 control-label">Email Address
                            </label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="i_cashierEmail" placeholder="" name="cashierEmail">
                                </div>
                            </div>
                        </div>
[#--                        12 18--]
[#--                        <div class="form-group">--]
[#--                            <label for="duty" class="col-sm-3 control-label not-null">Position</label>--]
[#--                            <div class="col-sm-5">--]
[#--                                <div class="aotu-pos">--]
[#--                                    <input type="text" class="form-control my-automatic input-sm" id="duty" placeholder="" name="duty">--]
[#--                                </div>--]
[#--                            </div>--]
[#--                        </div>--]
                        <div class="form-group">
                            <label for="cashierLevel"  class="col-sm-3 control-label not-null">Cashier Privilege</label>
                            <div class="col-sm-5">
                                <select id="cashierLevel" class="form-control input-sm" name="cashierLevel">
                                    <option value="">-- All/Please Select ----</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="effectiveSts" class="col-sm-3 control-label">Status</label>
                            <div class="col-sm-5">
                                <select id="effectiveSts" class="form-control input-sm" disabled="disabled">
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="operateFlg" value="" />
                    <button id="cancelByAdd" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
                    <button id="affirmByAdd" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
                </div>
            </div>
        </div>
    </div>
    [#-- 修改密码弹窗 --]
    <div id="cashierPass_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Cashier Password Setting</h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal" id="passwordform">
                        <div class="form-group">
                            <label for="pwd_cashierId"  class="col-sm-4 control-label">Staff ID</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" readonly="readonly" id="pwd_cashierId" placeholder="" name="pwd_cashierId">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pwd_cashierName" class="col-sm-4 control-label">Staff Name</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" readonly="readonly" id="pwd_cashierName" placeholder="" name="pwd_cashierName">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pwd_duty" class="col-sm-4 control-label">Position</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" readonly="readonly" id="pwd_duty" placeholder="" name="pwd_duty">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pwd_cashierLevel"  class="col-sm-4 control-label">Cashier Privilege</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="pwd_cashierLevel" readonly="readonly" >
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pwd_effectiveSts" class="col-sm-4 control-label">Status</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="pwd_effectiveSts" readonly="readonly">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="oldPassword" class="col-sm-4 control-label not-null">Original Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="oldPassword" autocomplete="off" name="oldPassword">
                                    [#--<span>(Please enter 1-6 digit.)</span>--]
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="newPassword" class="col-sm-4 control-label not-null">New Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="newPassword" autocomplete="off" name="newPassword">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="repeatPassword" class="col-sm-4 control-label not-null">Repeat New Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="repeatPassword" autocomplete="off" name="newPassword2">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="cancelByUpdatePwd" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
                    <button id="affirmByUpdatePwd" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
                </div>
            </div>
        </div>
    </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="entiretyBmStatus" value=""/>
	<input type="hidden" id="tempTableType" value="-1"/>
	<input type="hidden" id="pwdStoreCd" value="-1"/>
	<input type="hidden" id="searchJson" value=""/>
	[@permission code="SC-CASHIER-001"]<input type="hidden" class="permission-verify" id="addBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-CASHIER-002"]<input type="hidden" class="permission-verify" id="editBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-CASHIER-003"]<input type="hidden" class="permission-verify" id="eliminateBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-CASHIER-004"]<input type="hidden" class="permission-verify" id="reactivateBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-CASHIER-005"]<input type="hidden" class="permission-verify" id="initPasswordBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-CASHIER-005"]<input type="hidden" class="permission-verify" id="resetPasswordBut" value="${displayVar!}" />[/@permission]
	[@permission code="SC-CASHIER-009"]<input type="hidden" class="permission-verify" id="exportBut" value="${displayVar!}" />[/@permission]
</body>
</html>
