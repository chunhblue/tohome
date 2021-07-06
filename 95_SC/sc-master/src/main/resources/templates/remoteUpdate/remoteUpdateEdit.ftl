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
    <script src="${m.staticpath}/[@spring.message 'js.remoteUpdateEdit'/]"></script>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	[#--[if lt IE 9]>--]
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.html5shiv'/]"></script>-->
	<!--<script type="text/javascript" src="${m.staticpath}/[@spring.message 'js.respond'/]"></script>-->
	[#--<![endif]--]
	<script>
	</script>
	<STYLE>
		.box div.radio-inline label {
			padding-left: 20px;
			position: relative;
		}
	</STYLE>
    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>

<!--导航-->
[@common.nav "HOME&Basic&POS Remote Update Details"][/@common.nav]
<div class="container-fluid" id="main_box">
<!--row 分割-->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">
                        Remote Update Information
                    </div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="updateType" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Update Type</label>[#-- 分组 --]
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="updateType" class="form-control input-sm"  disabled="true">
                                            <option value="">-- All/Please Select --</option>
                                        </select>
                                    </div>
                                    <label for="startDate" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label not-null">Start Date</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="startDate" disabled="true" class="form-control input-sm" >
                                    </div>
                                    <label for="groupName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Group Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="groupName" disabled="true" class="form-control input-sm" maxlength="50" >
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<!--row 分割-->
	<div class="row">
        <div class="box col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box-title ">
                <div class="tt">
                    Remote Update Store
                </div>
            </div>
            <div id="StoreTree" style="max-height: 800px;overflow: auto;"></div>
        </div>
	</div>
    <div class="row">
        <div class="box col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <button id='attachments' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>
        </div>
    </div>
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
		    <button id='saveBtn' type='button' class='btn btn-default'><span class='glyphicon glyphicon-ok icon-right'></span>Save</button>
			<button id="returnsViewBut" type="button" class="btn btn-default returnsViewBut"><span class="glyphicon glyphicon-share icon-right"></span>Back</button>
		</div>
	</div>
</div>
<!--附件一览-->
[@common.attachments][/@common.attachments]
<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="viewSts" value="${viewSts!}"/>
<input type="hidden" id="id" value="${data.id!}"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="oldFtpFilePath" value=""/>
</body>
</html>
