<#if RequestParameters['name']??> 
{
	"code": 1,
	"name": "${file.name}预览",
	"totalSize": 0,
	"data": [
<#assign index = 0>
<#list imgurls as img>
<#if index != 0>,</#if>{
		"title": null,
		"url": "${baseUrl}${img}",
		"thumbUrl": "${baseUrl}${img}",
		"ratio": 0.5625
	}<#assign index = index + 1>
</#list>],
	"desc": "Success"
}
<#else>
<!DOCTYPE html>
<html lang="en">
 <head>
 <title>${file.name}预览</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link href="pptx/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="pptx/idocv/idocv_common.min.css" rel="stylesheet">
<link href="pptx/jquery.contextMenu.css" rel="stylesheet">
<script src="js/watermark.js" type="text/javascript"></script>

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
<style>
    * {
        margin: 0;
        padding: 0;
    }

    html, body {
        height: 100%;
        width: 100%;
    }

</style>
 
    <style type="text/css">
      .thumbnail{
        /*
        max-width: 200px;
        */
        cursor: pointer;
      }
    </style>
    
    <!--[if lt IE 9]>
      <script src="js/html5shiv.min.js"></script>
    <![endif]-->

  </head>

  <body onload="resetImgSize();" class="ppt-body">

<div class="loading-mask" style="display: block;">
    <div class="loading-zone">
        <div class="text"><img src="pptx/img/loader_indicator_lite.gif">加载中...</div>
    </div>
</div>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <!-- FILE NAME HERE -->
          <!-- SIGN UP & SIGN IN -->
          <div class="nav-collapse collapse">
            <p class="navbar-text pull-right">
              <a href="#" title="全屏" class="fullscreen-link"><i class="icon-fullscreen icon-white"></i></a>
            </p>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid" style="max-height: 100%;">
      <div class="row-fluid">
        <div class="span2 hidden-phone" style="position: fixed; top: 60px; left: 20px; bottom: 20px; padding-right: 10px; border-right: 3px solid #c8c8c8; max-height: 100%; overflow: auto; text-align: center;">
          <!--Sidebar content-->
          <!-- 
          <div class="thumbnail">
            <img src="">
          </div>
          1/20<br />
          -->
        </div>
        <div class="span9 offset2">
          <div class="slide-img-container">
            <div class="ppt-turn-left-mask"></div>
            <div class="ppt-turn-right-mask"></div>
            <!-- 
            <img src="" class="img-polaroid" style="max-height: 100%;">
             -->
          </div>
          <!-- ONLY AVAILABLE ON MOBILE -->
          <div class="span12 visible-phone text-center" style="position: fixed; bottom: 10px; left: 0px; z-index: 1000;">
            <select class="select-page-selector span1" style="width: 80px; margin-top: 10px;">
              <!-- PAGE NUMBERS HERE -->
            </select>
          </div>
        </div>
      </div>
    </div>


    <div class="progress progress-striped active bottom-paging-progress">
      <div class="bar" style="width: 0%;"></div>
    </div>
    <!-- JavaSript
    ================================================== -->
    
<script src="js/jquery-3.0.0.min.js"></script>
<script src="pptx/idocv/idocv_common.min.js"></script>

<script>
    var params = getAllUrlParams(window.location.href); // 如果用urlObj.param()方法获取则被非正常解码
    var name = 'pptx';
    if (!!name) {
        params.name = name;
    }
</script>
<!-- 客户自定义JS -->
<script src="pptx/jquery.mobile-events.min.js"></script>
<script src="pptx/ppt.js"></script>
<script type="text/javascript">
 		 /*初始化水印*/
 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
</script>
</body>
</html>
</#if>
