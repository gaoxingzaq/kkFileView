<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}代码预览</title>
    <#include  "*/commonHeader.ftl">
 <link rel="stylesheet" href="css/vs2015.min.css">
<script src="js/highlight.min.js"></script>
<script>hljs.highlightAll();</script>

    <style>
        div.code {
           white-space: pre-wrap;
        }
    </style>
</head>
<body>

<input hidden id="textData" value="${textData}"/>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                    ${file.name}
                </a>
            </h4>
        </div>
        <div class="panel-body">
            <div id="code" class='code'></div>
        </div>
    </div>
</div>


<script>
    /**
     *加载普通文本
     */
    function loadText() {
        var base64data = $("#textData").val()
        var textData = Base64.decode(base64data);

        var textPreData = "<pre><code>" + textData + "</code></pre>";
        $("#code").append(textPreData);
        document.querySelectorAll('div.code').forEach(block => {
            // then highlight each
            hljs.highlightBlock(block);
        });
    }
	
loadText();
if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
     
</script>

</body>

</html>
