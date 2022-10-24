<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}文本预览</title>
    <script src="js/marked.min.js" type="text/javascript"></script>
    <link id="mdstyle" rel="stylesheet" type="text/css" href="css/markdown.css">
    <#include "*/commonHeader.ftl">
</head>
<body>
<input hidden id="textData" value="${textData}"/>
<div class="container">
    <div class="panel panel-default">
        <div id="markdown_btn" class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                    ${file.name}
                </a>
            </h4>
        </div>
        <div id="text_btn" class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                    ${file.name}
                </a>
            </h4>
        </div>
        <div class="panel-body">
            <div id="markdown"></div>
        </div>
    </div>
</div>
<script>
 
    /**
     * 加载markdown
     */
	   function htmlttt (str){ 
             var s = "";
             if(str.length == 0) return "";
             s = str.replace(/&amp;/g,"&");
             s = s.replace(/&lt;/g,"<");
             s = s.replace(/&gt;/g,">");
             s = s.replace(/&nbsp;/g," ");
             s = s.replace(/&#39;/g,"\'");
             s = s.replace(/&quot;/g,"\"");
			 s = s.replace(/<script.*?>.*?<\/script>/ig, ''); 
             s = s.replace(/<script/gi, "&lt;script ");
			  s = s.replace(/<iframe/gi, "&lt;iframe ");
             return s;  
       } 
    function loadMarkdown() {
        var textData = Base64.decode($("#textData").val())
		  textData = htmlttt(textData);
        window.textPreData = "<pre style='background-color: #FFFFFF;border:none'>" + textData + "</pre>";
        window.textMarkdownData = marked.parse(textData);
        $("#markdown").html(window.textMarkdownData);
    }
    $("#markdown_btn").hide();
       loadMarkdown();
	   
        $("#markdown_btn").click(function () {
            $("#markdown").html(window.textMarkdownData);
            $("#text_btn").show()
            $("#markdown_btn").hide()
        });

        $("#text_btn").click(function () {
            $("#markdown_btn").show()
            $("#text_btn").hide();
            $("#markdown").html(window.textPreData);
        });
  
	

 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}

</script>
</body>

</html>
