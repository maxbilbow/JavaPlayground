<#macro mainLayout>
<!DOCTYPE html>
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js"><!--<![endif]-->
<head>
    <meta charset="utf-8">

    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/common-ui?src=/lib/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/common-ui?src=/lib/bootstrap-grid.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/common-ui?src=/lib/bootstrap-reboot.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/custom.css"/>

    <script lang="text/javascript" src="${rc.contextPath}/common-ui?src=js/lib/jquery.js"></script>
    <script lang="text/javascript" src="${rc.contextPath}/common-ui?src=js/lib/popper.js"></script>
    <script lang="text/javascript" src="${rc.contextPath}/common-ui?src=js/lib/popper-utils.js"></script>
    <script lang="text/javascript" src="${rc.contextPath}/common-ui?src=js/lib/bootstrap.js"></script>
<#--End additional scripts-->


</head>
<body>
<div class="widget">
    <div class="widget-extra themed-background-dark">
        <div class="row">
            <h5 class="widget-content-light col-md-12 header-text">
                <span class="page-heading-text"></span>
            </h5>
        </div>
    </div>
    <div id="requests-content" class="widget-extra-full">
        <#nested/>
    </div>
</div>
</body>
</html>
</#macro>