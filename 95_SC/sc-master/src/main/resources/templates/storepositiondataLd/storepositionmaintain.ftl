[#ftl]
[#import "../common/spring.ftl" as spring/]
[#import "../common/common.ftl" as common/]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>[@spring.message 'common.title'/]</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0"
          user-scalable=no/>
    <meta name="renderer" content="webkit">

    <link href="${m.staticpath}/[@spring.message 'css.common-all'/]" rel="stylesheet" type="text/css"/>
    <script src="${m.staticpath}/[@spring.message 'js.common-all'/]"></script>
    <script src="${m.staticpath}/[@spring.message 'js.storePositionMaintain'/]"></script>
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

        .my-user-info {
            padding: 10px;
            overflow: hidden;
        }

        span.my-label,
        span.my-text {
            float: left;
            color: #333333;
            font-size: 12px;
            margin-left: 10px;
        }

        span.my-text {
            margin-right: 20px;
        }

        span.my-label {
            font-weight: 900;
        }

        .my-div {
            overflow: hidden;
            float: left;
        }

        .del-alert {
            display: none;
        }
    </STYLE>
    <!--页头-->
    [@common.header][/@common.header]
</head>

<body>
<!--导航-->
[@common.nav "HOME&Hierarchy&User Management"][/@common.nav]

<div class="container-fluid" id="main_box">

    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="box">
                <div class="box-title ">
                    <div class="tt">
                        Query Condition
                    </div>
                </div>
                <div class="box-body box-body-padding-10">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 box-main">
                            <div class="form-horizontal">
                             <div class="form-group">
                                    <label for="empNumId" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">User ID</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="empNumId" class="form-control input-sm">
                                    </div>
                                    <label for="empName" class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label"> User Name</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <input type="text" id="empName" class="form-control input-sm">
                                    </div>
                                    <label for="a_store"
                                           class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Store</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <div class="aotu-pos">
                                            <input type="text" class="form-control my-automatic input-sm" id="a_store"
                                                   placeholder="" autocomplete="off" >
                                            <a id="a_store" href="javascript:void(0);" title="Refresh"
                                               class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                            <a id="" href="javascript:void(0);" title="Clear"
                                               class="auto-but glyphicon glyphicon-remove circle"></a>
                                        </div>
                                   </div>
                                 <label for="jobTypeCd"
                                        class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Position</label>
                                 <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                     <select id="jobTypeCd" class="form-control input-sm">
                                         <option value="">-- All/Please Select --</option>
                                     </select>
                                 </div>


                              </div>
                             <div class="form-group">
                                    <label for="effectiveStatus"
                                           class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label" style="white-space: nowrap">User Status</label>
                                    <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
                                        <select id="effectiveStatus" class="form-control input-sm">
                                            <option value="">-- All/Please Select --</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="wire"></div>
                            <button id="search" type="button" class="btn btn-primary btn-sm"><span
                                        class="glyphicon glyphicon-search icon-right"></span>Inquire
                            </button>
                            <button id='reset' type='button' class='btn  btn-primary   btn-sm '><span
                                        class='glyphicon glyphicon-refresh'></span>Reset
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <table id="zgGridTtable"></table>
            </div>
        </div><!--row 分割-->
    <div id="user_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog"
         aria-labelledby="" style="overflow: auto">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                    [#--                        员工资料--]
                    <h4 class="modal-title" id="myModalLabel">User Entry</h4>
                </div>
                <div class="modal-body">
                    <from class="form-horizontal" id="userform">
                        <fieldset>
                        <div class="form-group">
                            <label for="emp_num_id" class="col-sm-3 control-label not-null">User ID</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control  input-sm" id="emp_num_id" name="empNumId"
                                           placeholder="" autocomplete="off">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_name" class="col-sm-3 control-label not-null">User Name</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="emp_name" name="empName"
                                           placeholder="">
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="password">
                            <label for="emp_password" class="col-sm-3 control-label">Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="emp_password" name="password" autocomplete="off">
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="storeGroup">
                            <label for="storeCd" class="col-sm-3  my-automatic control-label not-null">Store</label>
                            <div class="col-xs-12 col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control input-sm" id="storeCd" placeholder="Please Entry 3 Letter" autocomplete="off" name="a_store">
                                    <a id="storeCdRefresh" href="javascript:void(0);" title="Refresh"
                                       class="auto-but glyphicon glyphicon-refresh refresh"></a>
                                    <a id="storeCdClear" href="javascript:void(0);" title="Clear"
                                       class="auto-but glyphicon glyphicon-remove circle"></a>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="job_type_cd" class="col-sm-3 control-label not-null">Position</label>
                            <div class="col-sm-5">
                                <select id="job_type_cd" class="form-control input-sm" name="position">
                                    <option value="">-- All/Please Select --</option>
                                </select>
                                <input id="job_type_cd1" class="form-control input-sm">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="effective_status" class="col-sm-3 control-label not-null">User Status</label>
                            <div class="col-sm-5">
                                <select id="effective_status" class="form-control input-sm" name="userStatus">
                                    <option value="">-- All/Please Select --</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_gender" class="col-sm-3 control-label not-null">Gender</label>
                            <div class="col-sm-5">
                                <select id="emp_gender" class="form-control input-sm" name="gender">
                                    <option value="">-- All/Please Select --</option>
                                </select>
                             </div>
                        </div>
                        <div class="form-group">
                                <label for="emp_country" class="col-sm-3 control-label">Country</label>
                                <div class="col-sm-5">
                                    <div class="aotu-pos">
                                        <input type="text" class="form-control input-sm" id="emp_country" name="country">
                                    </div>
                                </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_education" class="col-sm-3 control-label">Education</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="emp_education" name="education">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="telephone_no" class="col-sm-3 control-label">Telephone No.</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="telephone_no" name="telNo">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mobile_phone_no" class="col-sm-3 control-label">Mobile Phone No.</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="mobile_phone_no" name="mobileNo">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_address" class="col-sm-3 control-label">Address</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="emp_address" name="address">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_birthdate" class="col-sm-3 control-label">Birth Date</label>
                            <div class="col-sm-5">
                                <input id="emp_birthdate" nextele="emp_enter_time" placeholder="Birth Date"
                                       class="form-control input-sm select-date" type="text" value="" name="birthDate">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_date" class="col-sm-3 control-label">Start Date</label>
                            <div class="col-sm-5">
                                <input id="emp_date" nextele="emp_left_time" placeholder="Start Date"
                                       class="form-control input-sm select-date" type="text" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_leave_date" class="col-sm-3 control-label">Leave Date</label>
                            <div class="col-sm-5">
                                <input id="emp_leave_date" nextele="add_emp_post_code" placeholder="Leave Date"
                                       class="form-control input-sm select-date" type="text" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_postal_code" class="col-sm-3 control-label">Postal Code</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control input-sm"
                                           id="emp_postal_code" name="postalCode">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_email" class="col-sm-3 control-label">Email</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="emp_email" name="email">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_comment" class="col-sm-3 control-label">Comment</label>
                            <div class="col-sm-8">
                                <textarea id="emp_comment" class="form-control" rows="3" name="comment"></textarea>
                            </div>
                        </div>
                        </fieldset>
                </from>
                <div class="modal-footer">
                    <input type="hidden" id="operateFlg" value=""/>
                    <button id="cancel" type="button" class="btn btn-default btn-sm"><span
                                class="glyphicon glyphicon-remove icon-right"></span>Cancel
                    </button>
                    <button id='resetForm' type='button' class='btn  btn-primary   btn-sm '><span
                                class='glyphicon glyphicon-refresh icon-right'></span>Reset
                    </button>
                    <button id="affirm" type="button" class="btn btn-primary btn-sm"><span
                                class="glyphicon glyphicon-ok icon-right"></span>Save
                    </button>
                </div>
            </div>
        </div>
    </div>
    </div>
    <div id="updatePassword" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog"
         aria-labelledby="">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                    [#--                        修改密码--]
                    <h4 class="modal-title" id="myModalLabel">Modify Password</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="passwordForm">
                        <div class="form-group">
                            <label for="userId" class="col-sm-4 control-label not-null">User Id</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" readonly="readonly" class="form-control my-automatic input-sm" id="userId" name="userId">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userName" class="col-sm-4 control-label not-null">User Name</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" readonly="readonly" class="form-control my-automatic input-sm" id="userName" name="userName">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_old_password" class="col-sm-4 control-label not-null">Old Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="text" class="form-control my-automatic input-sm" id="emp_old_password" name="oldPassword">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_new_password" class="col-sm-4 control-label not-null">New Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="emp_new_password" name="newPassword" >
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emp_new_password2" class="col-sm-4 control-label not-null">Repeat New Password</label>
                            <div class="col-sm-5">
                                <div class="aotu-pos">
                                    <input type="password" class="form-control my-automatic input-sm" id="emp_new_password2" name="newPassword2">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="operateFlg" value=""/>
                    <button id="cancelByAdd" type="button" class="btn btn-default btn-sm"><span
                                class="glyphicon glyphicon-remove icon-right"></span>Cancel
                    </button>
                    <button id="saveNewPassWordReset" type='button' class='btn  btn-primary   btn-sm '><span
                                class='glyphicon glyphicon-refresh'></span>Reset
                    </button>
                    <button id="saveNewPassWord" type="button" class="btn btn-primary btn-sm"><span
                                class="glyphicon glyphicon-ok icon-right"></span>Save
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="stores_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Store Information</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <table id="storesTable"></table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="colseStoresTable" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
            </div>
        </div>
    </div>
</div>

<!--页脚-->
[@common.footer][/@common.footer]
<input type="hidden" id="toKen" value="${toKen!}"/>
<input type="hidden" id="use" value="${use!}"/>
<input type="hidden" id="identity" value="${identity!}"/>
<input type="hidden" id="entiretyBmStatus" value=""/>
<input type="hidden" id="tempTableType" value="-1"/>
<input type="hidden" id="addSearch" value=""/>
<input type="hidden" id="modifySearch" value=""/>
<input type="hidden" id="paramUpdatePassWord" value=""/>
<input type="hidden" id="deleteSearch" value=""/>
<input type="hidden" id="viewPer" value="${displayVar!}" />
<input type="hidden" id="editPer" value="${displayVar!}" />
<input type="hidden" id="cancelPer" value="${displayVar!}" />
[#--<input type="hidden" id="paramUpdatePassWord11" value=""/>--]
[@permission code="SC-EM-INFOR-001"]<input type="hidden" class="permission-verify" id="addBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-EM-INFOR-002"]<input type="hidden" class="permission-verify" id="resetBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-EM-INFOR-003"]<input type="hidden" class="permission-verify" id="editBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-EM-INFOR-004"]<input type="hidden" class="permission-verify" id="viewBut" value="${displayVar!}" />[/@permission]
[@permission code="SC-EM-INFOR-005"]<input type="hidden" class="permission-verify" id="delBut" value="${displayVar!}" />[/@permission]
</body>
</html>
