<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}文件分页预览</title>
    <#include "*/commonHeader.ftl">
</head>
<body>
<#if pdfUrl?contains("http://") || pdfUrl?contains("https://")>
    <#assign finalUrl="${pdfUrl}">
<#else>
    <#assign finalUrl="${baseUrl}${pdfUrl}">
</#if>
<#if "${pdfpagee}" == "0" >
<script type="text/javascript">
	var page= '${fenye}';
	test = location.href ;
    var index = test.lastIndexOf("\?");
   var index1 = test.lastIndexOf("\=");
   now = test.substring(index1 + 1,test.length);
  if(test.search("page=") != -1){
  test = test.substring(0,index);
  };
  function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}
   if (!isNumber(now) || now.length == 0|| now=="pdf"  ){
   now = 1;
   }
   
  var shangye = Number(now)-1;
	if(shangye <=0 ||shangye.length == 0){
	shangye = 1;
	}	
	 var xiaye = Number(now)+1;
	if(xiaye > page){
	xiaye = page;
	}
if(xiaye.length == 0){
	xiaye = 2;
	}
$('body').append('<a href="'+test+'&?page=1"><button class="prev" >首页</button></a>');
$('body').append('<a href="'+test+'&?page='+shangye+'"><button class="prev" >上一页</button></a>');
$('body').append('<a href="'+test+'&?page='+xiaye+'"><button class="next" >下一页</button></a>');
$('body').append('<a href="'+test+'&?page='+page+'"><button class="next" >最后一页</button></a>');
$('body').append('当前:' + now + '页   ');
$('body').append('共计:' + page + '页');
</script>
<#else>
<script type="text/javascript">
$('body').append('共计:' + ${fenye} + '页');
</script>
</#if>
<iframe  src="" width="100%" frameborder="0"></iframe>
</body>
<script type="text/javascript">
  function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}
	 test = location.href ;
   var index1 = test.lastIndexOf("\=");
   now = test.substring(index1 + 1,test.length);
   if (!isNumber(now) || now.length == 0|| now=="pdf"  ){
   now = 1;
   }
    var url = '${finalUrl}'+"?page="+now;
     document.getElementsByTagName('iframe')[0].src = "${baseUrl}pdfjs/web/viewer.html?file=" + encodeURIComponent(url) + "&disabledownload=${pdfDownloadDisable}&pdfXianzhi=${pdfXianzhi}&watermarkTxt=${watermarkTxt}&pdfhighlightAll=${highlightAll}#page=${page}";
    document.getElementsByTagName('iframe')[0].height = document.documentElement.clientHeight - 10;
    /**
     * 页面变化调整高度
     */
    window.onresize = function () {
        var fm = document.getElementsByTagName("iframe")[0];
        fm.height = window.document.documentElement.clientHeight - 10;
    }
  		 /*初始化水印*/
 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
</script>

</html>