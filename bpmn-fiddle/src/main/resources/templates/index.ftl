<#import "layout/layout.ftl" as Layout/>


<@Layout.mainLayout>

<div class="block">
    <div class="block-title">
        <h2>Renumber BPMN</h2>
    </div>
    <form action="${rc.contextPath}/upload" method="POST" class="form-horizontal form-bordered"
          enctype="multipart/form-data">


        <div class="form-group">
            <label for="prefix" class="col-sm-3 control-label">Prefix</label>
            <div class="col-sm-6">
                <input name="prefix" class="form-control input-sm" value="_"/>
            </div>
        </div>
        <div class="form-group">
            <label for="suffix" class="col-sm-3 control-label">Suffix</label>
            <div class="col-sm-6">
                <input name="suffix" class="form-control input-sm" value=""/>
            </div>
        </div>
        <div class="form-group">
            <label for="padding" class="col-sm-3 control-label">Number of Digits</label>
            <div class="col-sm-6">
                <input name="padding" class="form-control input-sm" type="number" value="3"/>
            </div>
        </div>


        <div class="form-group">
            <label for="start" class="col-sm-3 control-label">First Number</label>
            <div class="col-sm-6">
                <input name="start" class="form-control input-sm" value="1" type="number" min="1" required/>
            </div>
        </div>
        <div class="form-group">
            <label for="shift" class="col-sm-3 control-label">Increase By</label>
            <div class="col-sm-6">
                <input name="shift" class="form-control input-sm" value="0" type="number" min="0" required/>
            </div>
        </div>
        <div class="form-group">
            <label for="file" class="col-sm-3 control-label">File</label>
            <div class="col-sm-6">
                <input name="file" class=" " type="file" accept=".bpmn" required/>
            </div>
        </div>


        <div class="form-group">
            <label for="" class="col-sm-3 control-label"></label>
            <div class="col-sm-6">
                <input type="submit" class="btn btn-info" value="Go!"/>
            </div>
        </div>

    </form>
</div>


<script src="${rc.contextPath}/js/upload.js"></script>
</@Layout.mainLayout>

