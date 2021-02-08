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
    <script src="${m.staticpath}/[@spring.message 'js.fsInventoryPlanEdit'/]"></script>
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

[#--	<div class="error-pcode" id="error_pcode">--]
[#--		<div class="error-pcode-text"> ${useMsg!} </div>--]
[#--		<div class="shade"></div>--]
[#--	</div>--]
<!--页头-->
[@common.header][/@common.header]
<!--导航-->
[@common.nav "HOME&Stocktake&Stocktaking Plan Setting"][/@common.nav]
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
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking No.</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="store" class="form-control input-sm">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input id="pd_date" readonly="true" nextele="sell_end_date" placeholder="Stocktaking Date" class="form-control input-sm select-date" type="text" value="">
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Store</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos good-select">
                                            <input type="text" id="store" class="form-control input-sm">
                                            <a id="search_item_but" href="javascript:void(0);" title="Stocktaking Store" class="auto-but glyphicon glyphicon-zoom-in  item-select-but "></a>
                                        </div>
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Type</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="pd_status">
                                            <option value="01">Draw</option>
                                            <option value="02">overall</option>
                                            <option value="03">Clapper</option>
                                        </select>
                                    </div>

                                </div>
                                <div class="form-group">

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Method</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="dj_status">
                                            <option value="01">By Item</option>
                                            <option value="02">By Order</option>
                                        </select>
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Status</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="dj_status" class="form-control input-sm" nextele="pd_status">
                                            <option value="01">Yes</option>
                                            <option value="02">Wait</option>
                                        </select>
                                    </div>
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Stocktaking Time</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input id="pd_start_time" readonly="true" nextele="sell_end_date" placeholder="Start Time" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input  id="pd_end_time" readonly="true" placeholder="End Time" class="form-control input-sm select-date" type="text" value="">
                                    </div>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-1">
                                        <button id="clear_pd_time" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-trash icon-right"></span>Clear Time</button>
                                    </div>

                                </div>

                                <div class="form-group">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Modifier</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="store" class="form-control input-sm">
                                    </div>
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Summary
                                    </label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-8">
                                        <input type="text" id="store" class="form-control input-sm">
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
                            [#--										<button id='add' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon-plus'></span> 新 增</button>--]
                            [#--										<button id='up' type='button' class='btn  btn-warning   btn-sm ' ><span class='glyphicon glyphicon-pencil'></span> 修 改</button>--]
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
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
            <button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
        </div>
    </div><!--row 分割-->
</div>
<!--
<div id="update_dialog" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
	<div class="modal-dialog modal-lg" role="document" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">盘点开始</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-horizontal" >
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="form-group">
								<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">分类编号</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									<input type="text" id="typeCode" class="form-control input-sm">
								</div>
								<label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">分类名称</label>
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
									<input type="text" id="typeName" class="form-control input-sm">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="e_id" value="" />
				<button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>关闭</button>
				<button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>确认提交</button>
			</div>
		</div>
	</div>
</div>-->
<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Start Stocktaking</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="e_iyDpt" class="col-sm-2 control-label">Type Code</label>
                        <div class="col-sm-8">
                            <div class="aotu-pos">
                                <input type="text" id="typeCode" class="form-control my-automatic input-sm" id="e_iyDpt" placeholder="Please Entry">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="e_iyDpt" class="col-sm-2 control-label">Type Name</label>
                        <div class="col-sm-8">
                            <div class="aotu-pos">
                                <input type="text" id="typeName" class="form-control my-automatic input-sm" id="e_iyDpt" placeholder="Please Entry">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <input type="hidden" id="e_id" value="" />
                <button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
                <button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
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
<input type="hidden" id="searchJson" value=""/>
</body>
</html>
