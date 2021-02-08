[#ftl]
[#import "../../common/spring.ftl" as spring/]
[#import "../../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport"
          content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0"
          user-scalable=no/>
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet"
          type="text/css"/>
[#--<link href="${m.staticpath}/[@spring.message 'css.rolelist'/]" rel="stylesheet"--]
[#--type="text/css"/>--]
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.rolelist'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
[#--[if lt IE 9]>--]
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
    <!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
[#--<![endif]--]
    <style>

    </style>
</head>

<body>
<!--页头-->
	[@common.header][/@common.header]
<!--导航-->
	[@common.nav "HOME&Privilege&Privilege Management"][/@common.nav]
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <div class="box">
                <div class="box-title">
                    <div class="tt">Query Condition</div>[#--检索项--]
                </div>
                <div class="box-body box-body-padding-10">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group ">
                                    <label for="roleName"
                                           class="col-sm-2 control-label">Privilege Name</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control input-sm"
                                               id="name">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <button id="search" type="button" class="btn btn-primary btn-sm">
                                <span class="glyphicon glyphicon-search icon-right"></span>Inquire
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <div class="box">
                <div class="box-content ">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <table id="zgGrid"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<input id="toKen" name="toKen" value="${toKen!}" type="hidden">
[@permission code="P-ROLE-002"]<input type="hidden" id="butEditPermission"
                                      value="${displayVar!}"/>[/@permission]
[@permission code="P-ROLE-001"]<input type="hidden" id="butViewPermission"
                                      value="${displayVar!}"/>[/@permission]
[@permission code="P-ROLE-003"]<input type="hidden" id="butDelPermission"
                                      value="${displayVar!}"/>[/@permission]
<!--页脚-->
	[@common.footer][/@common.footer]

</body>
</html>
