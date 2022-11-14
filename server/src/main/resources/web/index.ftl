<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0"/>
    <title>kkFileView高雄修改版演示首页</title>
    <link rel="stylesheet" href="css/viewer.min.css"/>
    <link rel="stylesheet" href="css/loading.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="bootstrap-table/bootstrap-table.min.css"/>
    <script type="text/javascript" src="js/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="js/jquery.form.min.js"></script>
    <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="bootstrap-table/bootstrap-table.min.js"></script>
 
    <script type="text/javascript" src="js/base64.min.js"></script>
</head>
		<style>
 td{
white-space:pre-line;word-wrap: break-word;    word-break: break-all;
}
</style>
<body>
<div class="panel-group container" id="accordion">
    <h1>文件预览项目接入和测试界面</h1>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                    接入说明(需要引入BASE64)
                </a>
            </h4>
        </div>
        <div class="panel-body">
            <div>
                如果你的项目需要接入文件预览项目，达到对docx、excel、ppt、jpg等文件的预览效果，那么通过在你的项目中加入下面的代码就可以
                成功实现：
                <pre style="background-color: #2f332a;color: #cccccc">
&lt;script type="text/javascript" src="${baseUrl}js/base64.min.js">&lt;/script> 
var url = 'http://127.0.0.1:8080/file/test.txt'; //要预览文件的访问地址
window.open('${baseUrl}onlinePreview?url='+encodeURIComponent(Base64.encode(url)));
更新功能&gengxin=ok 固定格式 为了开启缓存 而且需要更新的文件设立的
其他功能  &filePassword=(密码)&highlightAll=(PDF高亮)&watermarkTxt=(水印)&&page=(页码)
下载流图片的方法 &officePreviewType=imagexz 其他转换内核 &officePreviewType=poi 
                </pre>
            </div>
            <div>
                新增多图片同时预览功能，接口如下：
                <pre style="background-color: #2f332a;color: #cccccc">
var fileUrl =url1+"|"+"url2";//多图使用“|”字符隔开
window.open('${baseUrl}picturesPreview?urls='+encodeURIComponent(Base64.encode(fileUrl)));
                </pre>
            </div>
			<div>
                下载流方法，接口如下：
                <pre style="background-color: #2f332a;color: #cccccc">
var originUrl = 'http://127.0.0.1:8080/filedownload?fileId=1'; //要预览文件的访问地址
var previewUrl = originUrl + '&fullfilename=/test.txt'   //这里必须加反斜杠
window.open('${baseUrl}onlinePreview?url='+encodeURIComponent(Base64.encode(previewUrl)));
                </pre>
            </div>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion">
                    输入下载地址预览文件
                </a>
            </h4>
        </div>
        <div class="panel-body">
            <label>文件下载地址：<input type="text" id="_url" style="min-width:45em"/></label>
            <form action="${baseUrl}onlinePreview" target="_blank" id="preview_by_url" style="display: inline-block">
                <input type="hidden" name="url"/>
				<label><input type="checkbox"  name="genxin" value="ok" />更新</label> 
                <input type="text" id="filePassword" name="filePassword" placeholder="密码" style="width:40px;"> 
                 <input type="text" id="page" name="page" placeholder="页码" style="width:40px;"> 
                 <input type="text" id="highlightAll" name="highlightAll" placeholder="高亮显示" style="width:60px;"> 
				<input type="text" id="watermarkTxt" name="watermarkTxt" placeholder="插入水印" style="width:60px;"> 
                <input type="submit" value="预览">
            </form>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion"
                   href="#collapseTwo">
                    预览测试
                </a>
            </h4>
        </div>
        <div class="panel-body">
            <#if fileUploadDisable == false>
                <div style="padding: 10px">
                    <form enctype="multipart/form-data" id="fileUpload">
                        <input type="file" name="file"/>
                        <input type="button" id="btnSubmit" value=" 上 传 "/>
                    </form>
                </div>
            </#if>
            <div>
                <table id="table" data-pagination="true"></table>
            </div>
        </div>
    </div>
    <div class="panel panel-default">
      <div class="panel-heading">
            <h4 class="panel-title">
               <a target="_blank"  href="https://gitee.com/gaoxingzaq/file-online-preview-master#历史更新记录" >历史更新记录</a>
            </h4>
        </div>
    </div>
     <div style="display: grid; place-items: center;">
    <div>
       <a target="_blank"  href="https://beian.miit.gov.cn/" >${BeiAn}</a>
    </div>
</div>
</div>
<script>
    function deleteFile(fileName) {
        $.ajax({
            url: '${baseUrl}deleteFile?fileName=' + encodeURIComponent(fileName),
            success: function (data) {
			console.log(data);
                // 删除完成，刷新table
                if (1 === data.code) {
				console.log(data);
                    alert(data.msg);
                } else {
                    $('#table').bootstrapTable('refresh', {});
                }
            },
            error: function (data) {
                console.log(data);
            }
        })
    }

    $(function () {
        $('#table').bootstrapTable({
            url: 'listFiles',
            columns: [{
                field: 'fileName',
                title: '文件名'
            }, {
                field: 'action',
                title: '操作'
            },]
        }).on('pre-body.bs.table', function (e, data) {
            // 每个data添加一列用来操作
            $(data).each(function (index, item) {
                item.action = "<a class='btn btn-default' target='_blank' href='${baseUrl}onlinePreview?url=" + encodeURIComponent(Base64.encode('${baseUrl}' + item.fileName)) + "'>预览</a>" + "<a class='btn btn-default' href='javascript:void(0);' onclick='deleteFile(\"" + encodeURIComponent(item.fileName)+ "\")'>删除</a>";
            });
            return data;
        }).on('post-body.bs.table', function (e, data) {
            return data;
        });

        $('#preview_by_url').submit(function() {
            var _url = $("#_url").val();
			
if(_url==""||_url==null||_url==undefined){
 alert("地址为空弹出默认图片");
    _url="http://n.sinaimg.cn/news/crawl/167/w1080h687/20210311/4044-kmeeius6951805.jpg";
}
 if (!checkUrl(_url)) {
                alert('请输入正确的url');
                return false;
            }
            var urlField = $(this).find('[name=url]');
            var b64Encoded = Base64.encode(_url);
            urlField.val(b64Encoded);
        });


        function showLoadingDiv() {
            var height = window.document.documentElement.clientHeight - 1;
            $(".loading_container").css("height", height).show();
        }
        $("#btnSubmit").click(function () {
            showLoadingDiv();
            $("#fileUpload").ajaxSubmit({
                success: function (data) {
                    // 上传完成，刷新table
                    if (1 === data.code) {
                        alert(data.msg);
                    } else {
                        $('#table').bootstrapTable('refresh', {});
                    }
                    $(".loading_container").hide();
                },
                error: function () {
                    alert('上传失败，请联系管理员');
                    $(".loading_container").hide();
                },
                url: 'fileUpload', /*设置post提交到的页面*/
                type: "post", /*设置表单以post方法提交*/
                dataType: "json" /*设置返回值类型为文本*/
            });
        }); 
    });
	
    
       function checkUrl(url){
        //url= 协议://(ftp的登录信息)[IP|域名](:端口号)(/或?请求参数)
        var strRegex = '^((https|http|ftp)://)'//(https或http或ftp)
            + '(([\\w_!~*\'()\\.&=+$%-]+: )?[\\w_!~*\'()\\.&=+$%-]+@)?' //ftp的user@  可有可无
            + '(([0-9]{1,3}\\.){3}[0-9]{1,3}' // IP形式的URL- 3位数字.3位数字.3位数字.3位数字
            + '|' // 允许IP和DOMAIN（域名）
            + '(localhost)|'	//匹配localhost
            + '([\\w_!~*\'()-]+\\.)*' // 域名- 至少一个[英文或数字_!~*\'()-]加上.
            + '\\w+\\.' // 一级域名 -英文或数字  加上.
            + '[a-zA-Z]{1,6})' // 顶级域名- 1-6位英文
            + '(:[0-9]{1,5})?' // 端口- :80 ,1-5位数字
            + '((/?)|' // url无参数结尾 - 斜杆或这没有
            + '(/[\\w_!~*\'()\\.;?:@&=+$,%#-]+)+/?)$';//请求参数结尾- 英文或数字和[]内的各种字符
        var re = new RegExp(strRegex,'i');//i不区分大小写
        //将url做uri转码后再匹配，解除请求参数中的中文和空字符影响
        if (re.test(encodeURI(url))) {
            return (true);
        } else {
            return (false);
        }
    }
	
     function show (event) {
        //取消冒泡
        let oevent = event || window.event
        if (document.all) {
            oevent.cancelBubble = true
        } else {
            oevent.stopPropagation()
        }
        if (document.getElementById('div').style.display === 'none' || document.getElementById('div').style.display === '') {
            document.getElementById('div').style.display = 'block'
        } else {
            document.getElementById('div').style.display = 'none'
        }
    }
    document.onclick = function () {
        document.getElementById('div').style.display = 'none'
    }
    document.getElementById('div').onclick = function (event) {
        let oevent = event || window.event
        oevent.stopPropagation()
    }
</script>
</body>
</html>