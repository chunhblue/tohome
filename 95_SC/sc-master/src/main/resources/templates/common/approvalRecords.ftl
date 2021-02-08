[#ftl]
<div id="approvalRecords_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-lg" role="document" style="width: 80%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Approval Records</h4><!--审核记录-->
            </div>
            <div class="modal-body">
[#--                <div class="row">--]
[#--                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--                        <div class="box">--]
[#--                            <div class="box-title ">--]
[#--                                <div class="tt">--]
[#--                                    Query Condition--]
[#--                                </div>--]
[#--                            </div>--]
[#--                            <div class="box-body box-body-padding-10" style="">--]
[#--                                <div class="row">--]
[#--                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--                                        <div class="form-horizontal">--]
[#--                                            <div class="form-group">--]
[#--                                                <label for="userCode"  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">User Code</label>--]
[#--                                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--                                                    <input type="text" id="userCode" class="form-control input-sm">--]
[#--                                                </div>--]
[#--                                                <label for=""  class="col-xs-12 col-sm-2 col-md-2 col-lg-1 control-label">Order Date</label>--]
[#--                                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--                                                    <input id="subStartDate" placeholder="Start Date" class="form-control input-sm select-date" type="text" value="">--]
[#--                                                </div>--]
[#--                                                <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2">--]
[#--                                                    <input  id="subEndDate" placeholder="End Date" class="form-control input-sm select-date" type="text" value="">--]
[#--                                                </div>--]
[#--                                            </div>--]
[#--                                        </div>--]
[#--                                    </div>--]
[#--                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">--]
[#--                                        <div class="wire"></div>--]
[#--                                        <button id="approvalRecordsSearch" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Inquire</button>--]
[#--                                        <button id='approvalRecordsReset' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-refresh icon-right'></span>Reset</button>--]
[#--                                    </div>--]
[#--                                </div>--]
[#--                            </div>--]
[#--                        </div><!--box end-->--]
[#--                    </div>--]
[#--                </div>--]
                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <table id="approvalRecordsTable"></table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="cancel" type="button" class="btn btn-default btn-sm" data-dismiss="modal"> <span class="glyphicon glyphicon-remove icon-right"></span>Close</button>
            </div>
        </div>
    </div>
</div>
