[#ftl]
<div class="row" id="audit_process">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="box">
            <div class="box-title ">
                <div class="tt">
                    Audit Process
                </div>
            </div>
            <div class="box-body box-body-padding-10" style="">
                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <div class="form-horizontal">
                            <div class="form-group" id="auditFlowChart">
                                <img alt="" src="${m.staticpath}/line.jpg" />
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <div class="wire"></div>
                        <button id="test" type="button" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search icon-right"></span>Test</button>
                    </div>
                </div>
            </div>
        </div><!--box end-->
    </div>
</div>
<script>
    [#--require("${m.staticpath}/line.jpg");--]
    for (let i = 1; i < 11; i++) {
        [#--require('${m.staticpath}/Button/button_' + i + '_blue.png');--]
        [#--require('${m.staticpath}/Button/button_' + i + '_silver.png');--]
    }
</script>
