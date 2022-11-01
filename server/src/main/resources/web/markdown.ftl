<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <link rel="stylesheet" href="md/css/css.css"/>
    <title>${file.name}文本预览</title>
<script src="js/watermark.js" type="text/javascript"></script>
<script src="js/base64.min.js" type="text/javascript"></script>
</head>
<body>
<script>
    /**
     * 初始化水印
     */
    function initWaterMark() {
        let watermarkTxt = '${watermarkTxt}';
        if (watermarkTxt !== '') {
            watermark.init({
                watermark_txt: '${watermarkTxt}',
                watermark_x: 0,
                watermark_y: 0,
                watermark_rows: 0,
                watermark_cols: 0,
                watermark_x_space: ${watermarkXSpace},
                watermark_y_space: ${watermarkYSpace},
                watermark_font: '${watermarkFont}',
                watermark_fontsize: '${watermarkFontsize}',
                watermark_color: '${watermarkColor}',
                watermark_alpha: ${watermarkAlpha},
                watermark_width: ${watermarkWidth},
                watermark_height: ${watermarkHeight},
                watermark_angle: ${watermarkAngle},
            });
        }
    }

</script>

<script>
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
function loadText() {
	 neirong = htmlttt(Base64.decode("${textData}"));
	 return neirong;
	}
</script>
	<div id=app></div>
    <script type="text/javascript" src="md/js/manifest.f8f7e750f6d0737b68e0.js"></script>
    <script type="text/javascript" src="md/js/vendor.89a436f29164fce8caf9.js"></script>
    <script type="text/javascript" src="md/js/app.5865965e0e77e43dcfa6.js"></script>
</body>
<script>

if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
     
</script>

</html>
