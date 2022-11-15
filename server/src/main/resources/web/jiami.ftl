<html>
<head>
<meta charset="utf-8">
<title>未输入密码</title>
<script type="text/javascript" src="js/base64.min.js"></script>
</head>
<body>

<script languange="JavaScript">
        var password=""
         password=prompt('请输入密码','');
        const locationHref = window.location.href;
        function custom_close()
        {
            window.opener=null;
            window.open('','_self');
            window.close();        
        }    
       if  (password !='' || password != null)
            {    
          //  alert("即将打开")
		    var url ='${pdfUrl}'; //要预览文件的访问地址
         window.location.href='${baseUrl}onlinePreview?url='+encodeURIComponent(Base64.encode(url))+'&filePassword='+password;
            }else {
             window.location.href=new URL(locationHref);
            }
</script>
</body>
</html>