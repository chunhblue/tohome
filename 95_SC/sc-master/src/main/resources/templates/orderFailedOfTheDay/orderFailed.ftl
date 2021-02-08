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
    <script src="${m.staticpath}/[@spring.message 'js.dayOrderFailure'/]"></script>
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

<!--页头-->
[@common.header][/@common.header]
<!--导航-->
[@common.nav "HOME&Ordering&Failed Order List of the day"][/@common.nav]
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
                                    <label for="orderDate"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Order Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input id="orderDate"  nextele="" placeholder="Order Date" class="form-control input-sm select-date" type="text" value="" disabled="disabled">
                                    </div>
                                    <label for="articleId"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Vendor Code/Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="vendorInfo" class="form-control input-sm">
                                    </div>
                                    <label for="articleName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Code/Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="ItemInfo" class="form-control input-sm">
                                    </div>
                                </div><!-- form-group -->
                                <div class="form-group">
                                    <label for="barcode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Item Barcode</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="barcode" class="form-control input-sm">
                                    </div>
                                    <label for="orderBy"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order By</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="createUserId" class="form-control input-sm">
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
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="userName" value="${userName!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="businessDate" value="${date!}"/>
[@permission code="SC-DAY-OD-FAILED-003"]<input type="hidden" class="permission-verify" id="orderButView" value="${displayVar!}" />[/@permission]
[@permission code="SC-DAY-OD-FAILED-002"]<input type="hidden" class="permission-verify" id="orderButExport" value="${displayVar!}" />[/@permission]
</body>
</html>
