[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="zh-CN">
<head>

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.subbmitterQuery'/]"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    [#--[if lt IE  9]>--]

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
            padding-right: 20px;
        }
        .good-select .item-select-but {
            width: 20px;
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
            margin-left:6px;
        }
        span.my-text{
            margin-right:12px;
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
        /*修改模态框出现的位置和大小*/
        .modal.fade.in{
            center:150px;
        }
    </STYLE>

</head>

<!--导航-->
<div class="container-fluid" id="main_box">
    <!--row 分割-->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">
                        Subbmitter Query     [#-- 页面开始 --]
                    </div>
                </div>
                <div class="box-body box-body-padding-10" style="">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-horizontal">
                                <div class="form-group">

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">User ID</label>
                                    <div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
                                        <div class="aotu-pos good-select">
                                            <input type="text" id="user_id" class="form-control input-sm">
                                            <a id="UserId" href="javascript:void(0);" class="auto-but glyphicon glyphicon-zoom-in  item-select-but "></a>
                                        </div>
                                    </div>

                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">User Name</label>
                                    <div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
                                        <div class="aotu-pos good-select">
                                            <div class="aotu-pos good-select">
                                                <input type="text" id="user_name" class="form-control input-sm">
                                                <a id="UserName" href="javascript:void(0);" class="auto-but glyphicon glyphicon-zoom-in  item-select-but "></a>
                                            </div>
                                        </div>
                                    </div>


                                <div class="">
                                    <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">User Role</label>
                                    <div class="aotu-pos good-select">
                                        <input type="text" id="user_role" class="form-control input-sm">
                                        <a id="UserRole" href="javascript:void(0);" class="auto-but glyphicon glyphicon-zoom-in  item-select-but "></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <div class="wire"></div>
                        <button id="search" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-center"></span>Inquire</button>
                        <button id='reset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-center'></span>Clear</button>
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
</div>

<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="searchJson" value=""/>
<input type="hidden" id="editPer" value="" />
</body>
</html>
