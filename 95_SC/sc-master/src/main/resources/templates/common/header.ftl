[#ftl]
[#import "/spring.ftl" as spring/]
[#import "common.ftl" as common/]
    <head>
        <link rel= "Shortcut Icon" href= "${m.staticpath}/favicon.ico">
    </head>
<div class="loading-mask-div"></div>
<div class="init-loading-box">
	<div class="loading-box-assist">
		<div class="progress">
	      <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
	      <span class="sr-only">Loading ... ...</span>
	      </div>
	    </div>
	</div>
</div>
<!---->
<header class="navbar navbar-default navbar-fixed-top bs-docs-nav my-header" id="top">
  <div class="container-fluid">
    <div class="navbar-header">
    	[#if Session.IY_MASTER_USER??]
      <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#bs-navbar" aria-controls="bs-navbar" aria-expanded="false">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      [/#if]
      <a href="#" class="navbar-brand ">
      	<div class="logo"></div>
[#--          [@spring.message 'common.title'/]--]
      </a>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse menu">
       [@common.menu/]
       [@common.user/]
    </nav>
  </div>
</header>
<!---->
[#if Session.IY_MASTER_USER??]

<!-- 退出dialog -->
<div id="myOut" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title"><span class="glyphicon glyphicon-exclamation-sign" style="color:#ff8008;"></span> Confirm to exit the system？</h4>
            </div>
            <div class="modal-body  text-center">
                <a target="_top" href="${syspath}/logout" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-log-out icon-right"></span>Exit</a>
                <a href="" class="btn btn-default btn-sm" data-dismiss="modal"><span class="glyphicon glyphicon-remove-sign icon-right"></span>Cancel</a>
            </div>
        </div>
    </div>
</div>
<!-- 确认dialog -->
<div id="myConfirm" class="modal fade bs-example-modal-md"  aria-hidden="true" data-backdrop="static" data-keyboard="false" style="z-index:999999;" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title" id="myConfirm_text"></h4>
            </div>
            <div class="modal-body  text-center">
            	<input id="myConfirm_function_name" type="hidden" value="">
            	<input id="myConfirm_status" type="hidden" value="false">
                <a status="true" href="javascript:void(0);" class="btn btn-primary"><span class="glyphicon glyphicon-ok icon-right"></span>Confirm</a>
                <a status="false" href="javascript:void(0);" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove-sign icon-right"></span>Cancel</a>
            </div>
        </div>
    </div>
</div>
[/#if]