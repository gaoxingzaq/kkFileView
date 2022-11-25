<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${file.name}万能播放器</title>
		<link type="text/css" rel="stylesheet" href="ckplayer/css/ckplayer.css" />
		<script type="text/javascript" src="ckplayer/flv.js/flv.min.js"></script>
	<script type="text/javascript" src="ckplayer/js/ckplayer.min.js" charset="UTF-8"></script>
		<#include "*/commonHeader.ftl">
		<style>
			.adpause{
				width: 90%;
				height: 90%;
				max-width: 580px;
				max-height: 360px;
				color: #FFF;
				position: absolute;
				background: #07141E;
				top:0;
				bottom: 0;
				left: 0;
				right: 0;
				margin: auto;
				font-size: 30px;
				line-height: 38px;
				box-sizing:border-box;
				-moz-box-sizing:border-box; /* Firefox */
				-webkit-box-sizing:border-box; /* Safari */
				padding: 50px;
				display: none;
			}
		</style>
	</head>
	<body>
		<div class="video"
		style="width: 100%;
		height: 500px;
		max-width: 800px;  
		margin: auto;
        top: 0;
       left: 0;
       bottom: 0;
       right: 0;
     background-color: green;">播放容器</div>

		<script>

			var videoObject = {
				container: '.video', //视频容器
				//autoplay:true,//自动播放
				plug:'flv.js',//设置使用flv插件
			//	poster:'ckplayer/poster.png',//封面图片
				menu:[
			{
				title:'高雄修改版播放器',
				link:'https://qt.cxcp.net',
				underline:true
			},
			{
				title:'播放/暂停',
				click:'player.playOrPause',
			},
			{
				title:'关于视频',
				click:'aboutShow'
			}
		],
		ad:{//定义广告
			<#if "${mediagg}" != ''>
					pause:{//配置暂停广告
						close:true,//暂停广告是否显示关闭按钮
						list:[//暂停广告列表
							{
								content: '.adpause',//显示一个层
								type: 'node',//类别定义成node
								time: 5//显示时长
							}
						]
					}
			</#if>
				},
		video:'${mediaUrl}'//视频地址
			};
			var player=new ckplayer(videoObject)//调用播放器并赋值给变量player
			  		 /*初始化水印*/
 if (!!window.ActiveXObject || "ActiveXObject" in window)
{
}else{
 initWaterMark();
}
		</script>

		<div class="adpause">${mediagg}</div>
	</body>
</html>
