<html>
<head>
<meta charset="utf-8">
<title>未输入密码</title>
<script type="text/javascript" src="js/base64.min.js"></script>
</head>
<body>
<script languange="JavaScript">
        var password=""
         password=prompt('你正在预览加密文件，请输入文件密码。');
        function custom_close()
        {
            window.opener=null;
            window.open('','_self');
            window.close();        
        }    
       if  (password !='' || password != null)
            {    
          //  alert("即将打开")
          const locationHref = window.location.href;
		    var url ='${pdfUrl}'; //要预览文件的访问地址
         window.location.href='${baseUrl}onlinePreview?url='+encodeURIComponent(Base64.encode(url))+'&jiami='+password;
            }else {
             window.location.href=new URL(locationHref);
            }
</script>
</body>
</html>