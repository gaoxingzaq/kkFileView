<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>${file.name}图片预览</title>
    <script src="js/lazyload.js"></script>
    <#include "*/commonHeader.ftl">
    <style>
        body {
            background-color: #404040;
        }
        .container {
            width: 100%;
            height: 100%;
        }
        .img-area {
         text-align: center;
        }
img {
max-width: 98%;
 margin:0 auto;
 border-radius:3px;
 box-shadow:rgba(0,0,0,0.15) 0 0 8px;
 background:#FBFBFB;
 border:1px solid #ddd;
 margin:1px auto;
 margin-left: 15px;
 padding:5px;
}

</style>
</head>
<body>
<div class="container">

   <#list fileTree?sort as img>
	<#if img?contains("http://") || img?contains("https://")>
    <#assign finalUrl="${img}">
<#else>
    <#assign finalUrl="${baseUrl}${img}">
</#if>
        <div class="img-area">
            <img class="my-photo" alt="loading"  data-src="${finalUrl}" src="images/loading.gif" >
		
        </div>
    </#list>

</div>
<script>
	 checkImgs();
    window.onscroll = throttle(checkImgs);
    function changePreviewType(previewType) {
        var url = window.location.href;
        if (url.indexOf("officePreviewType=image") !== -1) {
            url = url.replace("officePreviewType=image", "officePreviewType="+previewType);
        } else {
            url = url + "&officePreviewType="+previewType;
        }
        if ('allImages' === previewType) {
            window.open(url)
        } else {
            window.location.href = url;
        }
    }
	 		 /*初始化水印*/
 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
</script>
</body>
</html>