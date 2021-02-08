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
    <script src="${m.staticpath}/[@spring.message 'js.pogShelfListView'/]"></script>
    <script>
    </script>
    <STYLE>
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
[@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Ordering&POG Shelf Maintenance Management"][/@common.nav]
<div class="container-fluid" id="main_box">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title">
                    <div class="tt">
                        Basic Information
                    </div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="pogCd" hidden class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Department</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2" hidden>
                                        <input type="text" id="pogCd" readonly="true" class="form-control input-sm" value="${dto.pogCd!}">
                                    </div>

                                    <label for="pogName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Document Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="pogName" readonly="true" class="form-control input-sm" value="${dto.pogName!}">
                                    </div>

                                    <label for="storeCd"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store No.</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="storeCd" readonly="true" class="form-control input-sm" value="${dto.storeCd!}">
                                    </div>

                                    <label for="storeName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store Name</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="storeName" readonly="true" class="form-control input-sm" value="${dto.storeName!''}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="createTime"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">Department Date</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="createTime" readonly="true" class="form-control input-sm" value="${dto.createTime!''}">
                                    </div>

                                    <label for="createUserName"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Created By</label>
                                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="createUserName" readonly="true" class="form-control input-sm" value="${dto.createUserName!''}">
                                    </div>
                                </div>
                            </div>
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
[#--模态框--]
<div id="update_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">POG Shelf Detail</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group" hidden>
                        <label for="input_pogCd"  class="col-sm-2 col-lg-4 control-label">Department</label>
                        <div class="col-xs-12 col-sm-6">
                            <input type="text" class="form-control input-sm" id="input_pogCd" readonly="readonly" autocomplete="off" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="input_pogName"  class="col-sm-2 col-lg-4 control-label">Document Name</label>
                        <div class="col-xs-12 col-sm-6">
                            <input type="text" id="input_pogName" readonly="readonly" class="form-control input-sm">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="input_shelf"  class="col-sm-2 col-lg-4 control-label">Shelf</label>
                        <div class="col-xs-12 col-sm-6">
                            <input type="text" id="input_shelf" readonly="readonly" class="form-control input-sm">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="input_Sub_Shelf"  class="col-sm-2 col-lg-4 control-label">Sub-Shelf</label>
                        <div class="col-xs-12 col-sm-6">
                            <input type="text" id="input_Sub_Shelf" readonly="readonly" class="form-control input-sm">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="cancel" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
                <button id="affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Submit</button>
            </div>
        </div>
    </div>
</div>


<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="searchJson" value=""/>
</body>
</html>
