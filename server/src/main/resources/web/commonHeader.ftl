<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
<script src="js/jquery-3.0.0.min.js" type="text/javascript"></script>
<script src="js/jquery.form.min.js" type="text/javascript"></script>
<script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/watermark.js" type="text/javascript"></script>
<script src="js/base64.min.js" type="text/javascript"></script>
<#if "true" == pdfXianzhi>
  <script type="text/javascript">
//屏蔽右键菜单
document.oncontextmenu = function (event) {
  if (window.event) {
    event = window.event;
  }
  try {
    var the = event.srcElement;
    if (!((the.tagName == "INPUT" && the.type.toLowerCase() == "text") || the.tagName == "TEXTAREA")) {
      return false;
    }
    return true;
  } catch (e) {
    return false;
  }
}
//屏蔽粘贴
document.onpaste = function (event) {
  if (window.event) {
    event = window.event;
  }
  try {
    var the = event.srcElement;
    if (!((the.tagName == "INPUT" && the.type.toLowerCase() == "text") || the.tagName == "TEXTAREA")) {
      return false;
    }
    return true;
  } catch (e) {
    return false;
  }
}
//屏蔽复制
document.oncut = function (event) {
  if (window.event) {
    event = window.event;
  }
  try {
    var the = event.srcElement;
    if (!((the.tagName == "INPUT" && the.type.toLowerCase() == "text") || the.tagName == "TEXTAREA")) {
      return false;
    }
    return true;
  } catch (e) {
    return false;
  }
}
//禁止f12

function fuckyou() {
  window.close(); //关闭当前窗口(防抽)
  window.location = "about:blank"; //将当前窗口跳转置空白页
}
//禁止Ctrl+U
var arr = [123, 17, 18];
document.oncontextmenu = new Function("event.returnValue=false;"), //禁用右键

  window.onkeydown = function (e) {
    var keyCode = e.keyCode || e.which || e.charCode;
    var ctrlKey = e.ctrlKey || e.metaKey;
    console.log(keyCode + "--" + keyCode);
    if (ctrlKey && keyCode == 85) {
      e.preventDefault();
    }
    if (arr.indexOf(keyCode) > -1) {
      e.preventDefault();
    }
  }

function ck() {
  console.profile();
  console.profileEnd();
  //我们判断一下profiles里面有没有东西，如果有，肯定有人按F12了，没错！！
  if (console.clear) {
    console.clear()
  };
  if (typeof console.profiles == "object") {
    return console.profiles.length > 0;
  }
}

function hehe() {
  if ((window.console && (console.firebug || console.table && /firebug/i.test(console.table()))) || (
      typeof opera ==
      'object' && typeof opera.postError == 'function' && console.profile.length > 0)) {
    fuckyou();
  }
  if (typeof console.profiles == "object" && console.profiles.length > 0) {
    fuckyou();
  }
}
hehe();
window.onresize = function () {
  if ((window.outerHeight - window.innerHeight) > 200)
    //判断当前窗口内页高度和窗口高度，如果差值大于200，那么呵呵
    fuckyou();
}

document.onkeydown = function (event) {
  if ((event.keyCode == 112) || //屏蔽 F1
    (event.keyCode == 113) || //屏蔽 F2
    (event.keyCode == 114) || //屏蔽 F3
    (event.keyCode == 115) || //屏蔽 F4
    // (event.keyCode == 116) || //屏蔽 F5
    (event.keyCode == 117) || //屏蔽 F6
    (event.keyCode == 118) || //屏蔽 F7
    (event.keyCode == 119) || //屏蔽 F8
    (event.keyCode == 120) || //屏蔽 F9
    (event.keyCode == 121) || //屏蔽 F10
    (event.keyCode == 122) || //屏蔽 F11
	(event.keyCode == 80) ||
    (event.keyCode == 123)) //屏蔽 F12
  {
    return false;
  }
}

document.ondragstart = function() {
        return false;
};
window.onhelp = function () {
  return false;
}
</script>

</#if>

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

