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
    <script src="${m.staticpath}/[@spring.message 'js.electronicReceipt'/]"></script>
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
		.receipt{
			border-color: #ddd;
			margin-bottom: 20px;
			background-color: #fff;
			-webkit-box-shadow: 0 1px 1px rgba(0,0,0,.05);
			padding: 15px;
			border: 1px solid #ddd;
			border-radius: 4px;
			font-size: 12px;
			max-height: 510px;
			overflow-y: scroll;
		}
        pre {
            white-space: pre-wrap;
            white-space: -moz-pre-wrap;
            white-space: -pre-wrap;
            white-space: -o-pre-wrap;
            *word-wrap: break-word;
            *white-space : normal ;
        }
	</STYLE>
    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>
	<!--导航-->
	[@common.nav "HOME&Sale&Electronic Receipts Query"][/@common.nav]
	<div class="container-fluid" id="main_box">
		<div class="row" style="height: 100%">
			<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
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
                                        <label for="aRegion" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Region</label>[#-- 大区 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-4">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aRegion">
                                                <a id="regionRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="regionRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group ">
                                        <label for="aCity" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">City</label>[#-- 城市 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-4">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aCity">
                                                <a id="cityRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="cityRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group ">
                                        <label for="aDistrict" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">District</label>[#-- 区域 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-4">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="aDistrict">
                                                <a id="districtRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="districtRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group ">
                                        <label for="aStore" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Store</label>[#-- 店铺 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-4">
                                            <div class="aotu-pos">
                                                <input id="hidStore" hidden value="">
                                                <input type="text" class="form-control my-automatic input-sm" id="aStore">
                                                <a id="storeRefresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="storeRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="saleStartDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label not-null">Sales  Date</label>
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
[#--                                            <input id="saleStartDate" readonly="true" nextele="saleEndDate" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">--]
                                            <input id="saleStartDate"  nextele="saleEndDate" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">
                                        </div>
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
[#--                                            <input  id="saleEndDate" readonly="true" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">--]
                                            <input  id="saleEndDate" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">
                                        </div>
                                    </div><!-- form-group -->

                                    <div class="form-group">
[#--                                        <label for="posNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">POS</label>--]
                                        <label for="posNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">POS</label>
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="text" id="posNo" class="form-control input-sm">
                                        </div>
                                    </div><!-- form-group -->

                                    <div class="form-group">
                                        <label for="saleNo"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Receipt No</label>
                                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="text" id="saleNo" class="form-control input-sm">
                                        </div>
                                    </div><!-- form-group -->
                                    <div class="form-group">
[#--                                        输入商品名字--]
                                        <label for="receiptType" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Item Name</label>
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-4">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="itemInfo">
                                                <a id="itemInfofresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="itemInfoRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="receiptType" class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Receipt Type</label>[#-- 大区 --]
                                        <div class="col-xs-12 col-sm-4 col-md-3 col-lg-4">
                                            <div class="aotu-pos">
                                                <input type="text" class="form-control my-automatic input-sm" id="receiptType">
                                                <a id="receiptfresh" href="javascript:void(0);" title="Refresh" class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                                <a id="receiptRemove" href="javascript:void(0);" title="Clear" class="auto-but glyphicon glyphicon-remove circle"></a>
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
                <div class="box" style="margin-top: 20px">
                    <div class="box-title ">
                        <div class="tt">
                            Receipt Keywords Query
                        </div>
                    </div>
                    <div class="box-body box-body-padding-10" style="">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <div class="form-horizontal">
                                    <div class="form-group">
                                        <label for="keywords"  class="col-xs-12 col-sm-2 col-md-2 col-lg-3 control-label">Find What</label>
                                        <div class="col-xs-4 col-sm-4 col-md-3 col-lg-4">
                                            <input type="hidden" id="keywordTemp" readonly="true">
                                            <input type="text" id="keywords" class="form-control input-sm">
                                        </div>
                                    </div><!-- form-group -->
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <div class="wire"></div>
                                <button id="nextBtn" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Find Next</button>
                                <button id="toPdf" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-log-out icon-right"></span>To PDF</button>
                            </div>
                        </div>
                    </div>
                </div><!--box end-->
			 </div>
			<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" >
			    <div class="receipt" id="receipts"></div>
			</div>
		 </div>
	 </div>
	<!--页脚-->
	[@common.footer][/@common.footer]
	<input type="hidden" id="searchJson" value=""/>
	<input type="hidden" id="_startIndex" value="0"/>
	<input type="hidden" id="_oldContent" value=""/>
</body>
</html>
