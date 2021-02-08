[#ftl]
[#if Session.IY_MASTER_USER??]
<div id="approval_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-md" role="document" >
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Approval Details</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="auditUserName"  class="col-sm-3 control-label">Approver</label>
                        <div class="col-sm-5">
                            <input type="text" id="auditUserName" readonly="readonly" class="form-control input-sm" value="${Session.IY_MASTER_USER.user.userName!}">
                            <input type="hidden" id="auditUserId" value="${Session.IY_MASTER_USER.user.userId!}">
                        </div>
                    </div>
[#--                    <div class="form-group">--]
[#--                        <label for="auditTime"  class="col-sm-3 control-label">Time</label>--]
[#--                        <div class="col-sm-5">--]
[#--                            <input type="text" id="auditTime" readonly="readonly" class="form-control input-sm" value="${.now?string('dd/MM/yyyy hh:mm:ss')}">--]
[#--                        </div>--]
[#--                    </div>--]
                    <div class="form-group">
                        <label for="auditStatusGroup"  class="col-sm-3 control-label">Approval Result</label>
                        <div class="col-sm-5" id="auditStatusGroup">
                            <div class="radio-inline" id="">
                                <label>
                                    <input type="radio" name="auditStatus"
                                           checked="checked"
                                           value="1"/>
                                    Approve
                                </label>
                            </div>
                            <div class="radio-inline" id="">
                                <label>
                                    <input type="radio" name="auditStatus"
                                           value="0"/>
                                    Reject
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="auditContent" class="col-sm-3 control-label">Comments</label>
                        <div class="col-sm-8">
                            <textarea id="auditContent" class="form-control" rows="5"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input  hidden id="auditId">
                <button id="audit_cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
                <button id="audit_affirm" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-ok icon-right"></span>Confirm</button>
            </div>
        </div>
    </div>
</div>
[/#if]