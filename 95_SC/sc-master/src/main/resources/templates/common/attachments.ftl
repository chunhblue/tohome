[#ftl]
[#--附件一览--]
<div id="attachments_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Attachments</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <table id="attachmentsTable"></table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="cancelByAttachments" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
            </div>
        </div>
    </div>
</div>
[#--附件上传--]
<div id="fileUpload_dialog" class="modal fade bs-example-modal-md" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="">
    <div class="modal-dialog modal-md" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Upload</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="file_name"  class="col-sm-4 control-label not-null">File Name</label>
                        <div class="col-sm-5">
                            <input type="text" id="file_name" class="form-control input-sm">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="fileData"  class="col-sm-4 control-label not-null">Import File</label>
                        <div class="col-sm-5">
                            <input type="file" id="fileData" name="fileData" class="form-control input-sm">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input type="hidden" id="operateFlgByFile" value="" />
                <button id="cancelByFile" type="button" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-remove icon-right"></span>Cancel</button>
                <button id="affirmByFile" type="button" class="btn btn-primary btn-sm"><span class="glyphicon  glyphicon-ok icon-right"></span>Submit</button>
            </div>
        </div>
    </div>
</div>
