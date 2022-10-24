<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}文件预览</title>
    <#include "*/commonHeader.ftl">
</head>
<body>
<noscript><iframe src='*.htm'></iframe></noscript>
<#if pdfUrl?contains("http://") || pdfUrl?contains("https://")|| pdfUrl?contains("ftp://")>
    <#assign finalUrl="${pdfUrl}">
<#else>
    <#assign finalUrl="${baseUrl}${pdfUrl}">
</#if>
<iframe src="" width="100%" frameborder="0"></iframe>
	<#if "${file.suffix?lower_case}" == "dwg" || "${file.suffix?lower_case}" == "dxf" >
		<#else>
<#if "false" == switchDisabled>
    <img src="images/jpg.svg" width="63" height="63"
         style="position: fixed; cursor: pointer; top: 40%; right: 48px; z-index: 999;" alt="使用图片预览" title="使用图片预览"
         onclick="goForImage()"/>
</#if>
	</#if>
</body>
<script type="text/javascript">
String.prototype.startsWithh = function(str) {
    var reg = new RegExp("^" + str);
    return reg.test(this);
}
String.prototype.endsWithh = function(str) {
    var reg = new RegExp(str + "$");
    return reg.test(this);
}
    var url = '${finalUrl}';   //获取地址
    var baseUrl = '${baseUrl}'.endsWithh('/') ? '${baseUrl}' : '${baseUrl}' + '/';    //判断地址是否为本地地址   (这里去实现判断是否本地地址)
    if (!url.startsWithh(baseUrl)) {  //不是本地地址转到跨域方法
        url = baseUrl + 'getCorsFile?urlPath=' + encodeURIComponent(url);   //如果不适用KK本地上传  这里可以直接写死跨域方法
    }
	
		if (!!window.ActiveXObject || "ActiveXObject" in window)
	
{
 document.getElementsByTagName('iframe')[0].src = "${baseUrl}IEpdfjs/web/viewer.html?file=" + encodeURIComponent(url) + "&disabledownload=${pdfDownloadDisable}&pdfXianzhi=${pdfXianzhi}";
}else{
 document.getElementsByTagName('iframe')[0].src = "${baseUrl}pdfjs/web/viewer.html?file=" + encodeURIComponent(url) + "&disabledownload=${pdfDownloadDisable}&pdfXianzhi=${pdfXianzhi}&watermarkTxt=${watermarkTxt}&pdfhighlightAll=${highlightAll}#page=${page}";

}
   
    document.getElementsByTagName('iframe')[0].height = document.documentElement.clientHeight - 10;
    /**
     * 页面变化调整高度
     */
    window.onresize = function () {
        var fm = document.getElementsByTagName("iframe")[0];
        fm.height = window.document.documentElement.clientHeight - 10;
    }

    function goForImage() {
        var url = window.location.href;
        if (url.indexOf("officePreviewType=pdf") != -1) {
            url = url.replace("officePreviewType=pdf", "officePreviewType=image");
        } else {
            url = url + "&officePreviewType=image";
        }
        window.location.href = url;
    }

    /*初始化水印*/
 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
</script>

</html>