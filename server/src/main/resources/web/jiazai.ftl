<html>
<head>
<meta charset="utf-8">
<title>文件加载中</title>
</head>
<body>
<div id="app">
<script src="js/jquery-3.0.0.min.js" type="text/javascript"></script>
<link type="text/css" href="css/style.css" rel="stylesheet" />
	<div class="text-wrapper">
		<div class="text part1">
			<div>
				<span class="letter"><div class="character">L</div> <span></span></span>
				<span class="letter"><div class="character">o</div> <span></span></span>
				<span class="letter"><div class="character">a</div> <span></span></span>
				<span class="letter"><div class="character">d</div> <span></span></span>
				<span class="letter"><div class="character">i</div> <span></span></span>
				<span class="letter"><div class="character">n</div> <span></span></span>
				<span class="letter"><div class="character">g</div> <span></span></span>
			</div>
		</div>
	<div class="how-to"><h1><span>正在转换中，请您耐心等待...</span><h1></div>
		
	</div>
</div>
<#if pdfUrl?contains("http://") || pdfUrl?contains("https://")>
    <#assign finalUrl="${pdfUrl}">
<#else>
    <#assign finalUrl="${baseUrl}${pdfUrl}">
</#if>
<div id="divContent" class="panel-body">
</div>
<script>
  window.onload = function(){
        $.ajax({
		   url: '${baseUrl}onlinePrevieww?url=${pdfUrl}',
            success: function (data) {
				 $('#app').html("");
				 // $('#divContent').html(data);
				document.write(data);
				document.close();
            },
            error: function (data) {
			 alert("转换失败了");
			//document.write(data);
               // console.log(data);
            }
        })
    }
</script>
</body>
</html>