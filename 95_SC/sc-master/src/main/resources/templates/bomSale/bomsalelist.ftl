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
    <script src="${m.staticpath}/[@spring.message 'js.bomSale'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
    [#--<![endif]--]
    <script>
	</script>
	<STYLE>
		.good-select .form-control {
            padding-right: 35px;
		}
		.table-box {
		    margin-top: 10px;
            font-family: verdana,arial,sans-serif;
            text-align: center;
        }
        .table-box .grid_table_title {
            font-size:20px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        table.gridTable {
            color:#333333;
            margin: auto;
            border: none;
            border-collapse: collapse;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -khtml-border-radius: 5px;
        }
        table.gridTable th {
            padding: 8px;
            font-size:13px;
            border-top: 1px solid #666666;
            border-bottom: 1px solid #666666;
            background-color: #fff;
        }
        table.gridTable td {
            border: none;
            padding: 8px;
            font-size:12px;
            background-color: #ffffff;
        }
        table.gridTable tr:last-child td {
            border-top: 1px dashed #666666;
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
[#--	[@common.nav "HOME&Sale&BOM Sales Report"][/@common.nav]--]
	[@common.nav "HOME&Report&BOM Sales Report"][/@common.nav]
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
                                        <label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Store</label>[#-- 店铺 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aStore">
                                                <a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                        <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Sales Date</label>
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-2">
                                            <input id="saleStartDate"  placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
                                        </div>

                                        <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"></label>
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-2">
                                            <input id="saleEndDate"  placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                        </div>

                                        <label for="item" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item</label>
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="item">
                                                <a id="itemRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="itemRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
									</div><!-- form-group -->

								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="wire"></div>
								<button id='search' type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>
								<button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>
								<button id='print' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-print'></span>Print</button>
								<button id='export' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span>Export</button>
							</div>
						</div>
					</div>
                </div><!--box end-->
            </div>
        </div>
        <!--row 分割-->
        <div class="row">
            <div id="table_div" class="col-xs-12 col-sm-12 col-md-6 col-lg-12 table-box"> [#--最外层div--]
                <div id="grid_table_title" class="grid_table_title">BOM Sales Report</div>
                <table id="grid_table" class="gridTable" border=0 cellSpacing=0 cellPadding=0 freezeColumnNum="0" freezeRowNum="1">

                </table>
            </div>
        </div>
    </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="use" value="${use!}"/>
	<input type="hidden" id="identity" value="${identity!}"/>
	<input type="hidden" id="searchJson" value=""/>
    [@permission code="SC-RF-BOM-002"]<input type="hidden" class="permission-verify" id="printBut" value="${displayVar!}" />[/@permission]
</body>
</html>
