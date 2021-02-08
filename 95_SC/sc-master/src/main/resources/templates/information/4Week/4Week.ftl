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
    <script src="${m.staticpath}/[@spring.message 'js.fourWeekOperation'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]

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
		label.radio{
            margin-left:20px;
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
	[@common.nav "HOME&Report&4-week Operation Query"][/@common.nav]
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
									    <label for="businessDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Date</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
											<input id="businessDate" readonly="true" class="form-control input-sm select-date" type="text" value="">
									    </div>
									    <label for="currentWeek" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Current Week</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-1">
                                            <input id="currentWeek" readonly="true" class="form-control input-sm" type="text" value=""/>
                                        </div>
									</div><!-- form-group -->

									<div class="form-group">
									    <label class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Level</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <label class="radio">
                                                <input name="level" type="radio" value="0" checked/>
                                            Store Level</label>
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <label class="radio">
                                                <input name="level" type="radio" value="1"/>
                                            Top Department Level</label>
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <label class="radio">
                                                <input name="level" type="radio" value="2"/>
                                            Department Level</label>
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <label class="radio">
                                                <input name="level" type="radio" value="3"/>
                                            Sub-category Level</label>
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <label class="radio">
                                                <input name="level" type="radio" value="4"/>
                                            Item Level</label>
                                        </div>
									</div><!-- form-group -->

								    <div class="form-group">
                                        <label for="topDepCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Top Department</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="topDepCd" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value="01">Merchandising</option>
                                                <option value="02">Food Service</option>
                                            </select>
                                        </div>
                                        <label for="depCd" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="depCd" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value="012">Beer</option>
                                                <option value="0210">Food Service</option>
                                            </select>
                                        </div>
                                        <label for="category" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Category</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="category" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value="01223">Beer</option>
                                                <option value="021035">Food Prepared Off-site</option>
                                            </select>
                                        </div>
                                        <label for="subCategory" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Sub-Category</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <select id="subCategory" class="form-control input-sm">
                                                <option value="">-- All/Please Select --</option>
                                                <option value="01223325">Intl Brand-Imported</option>
                                                <option value="021035404">Desserts</option>
                                            </select>
                                        </div>
                                    </div><!-- form-group -->

									<div class="form-group">
									    <label for="barcode" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
									    <div class="col-xs-4 col-sm-4 col-md-2 col-lg-2">
									    	<input id="barcode" class="form-control" type="text" value="">
									    </div>
									    <label for="itemName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Name</label>
									    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                            <input id="itemName" readonly="true" class="form-control input-sm" type="text" value="">
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
			<div id="div_table1" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="zgGridTable1"></table>
			</div>
			<div id="div_table2" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="zgGridTable2"></table>
			</div>
			<div id="div_table3" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<table id="zgGridTable3"></table>
			</div>
		</div>
	 </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
[#--	<input type="hidden" id="toKen" value="${toKen!}"/>--]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
[#--	<input type="hidden" id="store" value="${store!}"/>--]
[#--	<input type="hidden" id="checkResources" value="${checkResources!}"/>--]
	<input type="hidden" id="entiretyBmStatus" value=""/>
	<input type="hidden" id="tempTableType" value="-1"/>
	<input type="hidden" id="searchJson" value=""/>
</body>
</html>
