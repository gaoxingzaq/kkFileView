<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
	  <title>${file.name}压缩包文件预览</title>
    <link href="css/zTreeStyle.css" rel="stylesheet" type="text/css">
    <#include "*/commonHeader.ftl">
    <style type="text/css">
        body {
            background-color: #404040;
        }
		li{
	list-style-type: none;
    border-radius:5px 5px 0px 0px;
    border:1px solid #ccc;
    border-bottom:none;
    text-align: left;
    line-height: 30px;
    background-color:#EEEEEE;
  
}
        h1, h2, h3, h4, h5, h6 {color: #2f332a;font-weight: bold;font-family: Helvetica, Arial, sans-serif;padding-bottom: 5px;}
		h2 {color: red;font-weight: normal;font-size: 22px;letter-spacing: 1px;line-height: 24px;}
        h6 {font-weight: normal;font-size: 18px;letter-spacing: 1px;line-height: 24px}
        div.zTreeDemoBackground {width:800px;text-align:center;margin: 0 auto;background-color: #ffffff;}
		a:link{color: 000;}  /*超链接默认样式*/
        a:visited{color: blue;}  /*超链接被访问后的样式*/
        a:hover{color: red;}   /*鼠标经过超链接的样式*/
        a:active{color: yellow;}  /*超链接被激活时的样式*/
    </style>

</head>
<body>

<div class="zTreeDemoBackground left">
    <ul id="treeDemo" class="ztree"></ul>
	  <h2>${file.name}列表</font></h2> 
	  <#list fileTree as img>
	<#if img?contains("http://") || img?contains("https://")>
    <#assign finalUrl="${img}">
<#else>
    <#assign finalUrl="${baseUrl}${img}">
</#if>
<div class="zTreeDemoBackground left">
 <h6><li><a href="javascript:void(0);" onclick="openFile('${finalUrl}')">${img?substring(img?lastIndexOf("/")+1)}</a></li>
     </div>
    </#list>
</div>
<script>
 function openFile(fileName) {
 window.open('${baseUrl}onlinePreview?url='+encodeURIComponent(Base64.encode(fileName))+"&watermarkTxt=${watermarkTxt}");
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