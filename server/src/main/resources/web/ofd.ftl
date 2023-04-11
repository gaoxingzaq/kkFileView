<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}文件预览</title>
    <#include "*/commonHeader.ftl">
</head>
<#if pdfUrl?contains("http://") || pdfUrl?contains("https://")|| pdfUrl?contains("ftp://")>
    <#assign finalUrl="${pdfUrl}">
<#else>
    <#assign finalUrl="${baseUrl}${pdfUrl}">
</#if>
<body>
<iframe src="" width="100%" frameborder="0"></iframe>
</body>
<script type="text/javascript">
    var url = '${finalUrl}';
    var baseUrl = '${baseUrl}'.endsWith('/') ? '${baseUrl}' : '${baseUrl}' + '/';
    if (!url.startsWith(baseUrl)) {
        url = baseUrl + 'getCorsFile?urlPath=' + encodeURIComponent(url);
    } 
    if(IsPhone()){
   	document.getElementsByTagName('iframe')[0].src = "${baseUrl}ofd/index.html?file=" + encodeURIComponent(url)+"&pdfXianzhi=${pdfXianzhi}&scale=width";
    }else{
    document.getElementsByTagName('iframe')[0].src = "${baseUrl}ofd/index.html?file=" + encodeURIComponent(url)+"&pdfXianzhi=${pdfXianzhi}";
    
    }

    document.getElementsByTagName('iframe')[0].height = document.documentElement.clientHeight - 10;
    /**
     * 页面变化调整高度
     */
    window.onresize = function () {
        var fm = document.getElementsByTagName("iframe")[0];
        fm.height = window.document.documentElement.clientHeight - 10;
    }

 function IsPhone() {
        var info = navigator.userAgent;
        //通过正则表达式的test方法判断是否包含“Mobile”字符串
        var isPhone = /mobile/i.test(info);
        //如果包含“Mobile”（是手机设备）则返回true
        return isPhone;
    }
   		 /*初始化水印*/
 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
</script>
\
</html>