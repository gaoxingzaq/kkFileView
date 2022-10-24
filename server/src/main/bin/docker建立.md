# docker版用法

## 一、准备工作

1. 上传编译好的 kkFileView-4.7.2.tar.gz 文件到 docker host，并解压

```
mkdir /opt/kkfileview
cd /opt/kkfileview
上传 kkFileView-4.7.2.tar.gz 文件到 /opt/kkfileview 目录
tar -zxvf kkFileView-4.7.2.tar.gz 
```

2. 建立软连接，方便版本升级

```
ln -s kkFileView-4.7.2 kkFileView-latest
```

3. 新建启动脚本 vim /opt/kkfileview/kkFileView-latest/run.sh

```
java -Dfile.encoding=UTF-8 -Dspring.config.location=/opt/kkFileView/config/application.properties -jar /opt/kkFileView/bin/$(ls /opt/kkFileView/bin | grep kkFileView-)
```

4. 启动脚本可执行权限

```
chmod +0755 /opt/kkfileview/kkFileView-latest/run.sh
```

## 二、dockerfile 生成 image

1. 复制 dockerfile 到 docker host 主机，假定目录为：/opt/kkfileview

```
cd /opt/kkfileview
```

2. 从 dockerfile 构建 image，之后是漫长的等待

```
docker build -t kkfv:001 .
```

3. 从 image 运行 container

```
docker run --name kkfv \
-p 8012:8012 \
-v /opt/kkfileview/kkFileView-latest:/opt/kkFileView \
-itd --privileged=true \
--restart=always \
kkfv:001 /bin/bash /opt/kkFileView/run.sh
```

## 三、修改应用配置和 nginx 反代

1. 修改配置

    打开 /opt/kkFileView-latest/conf 目录，下面有一个 application.properties 配置文件，有部分配置是可以在程序运行中变更的，其他的变更需要重新启动程序，例如，修改默认的路径 `/preview` 为 `/document` ：

```
第 3 行：
server.servlet.context-path= /document

第 73 行：
base.url = ${KK_BASE_URL:default}


新的：
第 3 行：
server.servlet.context-path= ${KK_CONTEXT_PATH:/document}

第 73 行：
base.url = ${KK_BASE_URL:https://xx.xxxxxx.com/document}

第85行，视频广告关闭：
gg.media = 

第 117 行，取消水印：
watermark.txt = ${WATERMARK_TXT:}
```


2. nginx 反代配置

```
server {
listen                     80;
listen                     443 ssl;
server_name                xx.xxxxxx.com;
ssl_certificate            /opt/ssl/xx.xxxxxx.com.crt;
ssl_certificate_key        /opt/ssl/xx.xxxxxx.com.key;
ssl_ciphers                ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA256:DHE-RSA-AES128-SHA256:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA:ECDHE-RSA-DES-CBC3-SHA:EDH-RSA-DES-CBC3-SHA:AES256-GCM-SHA384:AES128-GCM-SHA256:AES256-SHA256:AES128-SHA256:AES256-SHA:AES128-SHA:DES-CBC3-SHA:HIGH:!aNULL:!eNULL:!EXPORT:!CAMELLIA:!DES:!MD5:!PSK:!RC4;
ssl_prefer_server_ciphers  on;
ssl_protocols              TLSv1 TLSv1.1 TLSv1.2;
ssl_session_cache          shared:SSL:5m;
ssl_session_timeout        5m;

if ($scheme = http) {
return  301 https://$host$request_uri;
}

# 这个是 kkfileview 的反代
location /document {
proxy_pass        http://127.0.0.1:8012;
proxy_set_header  X-Real-IP $remote_addr;
}
}
```

## 四、应用版本升级

当群主发布的新的版本，比如：kkFileView-xxx.tar.gz，xxx是新版本号，升级操作如下：

1. 上传编译好的 kkFileView-xxx.tar.gz 文件到 docker host 的 /opt/kkfileview 目录，并解压：

```
mkdir /opt/kkfileview
cd /opt/kkfileview
上传 kkFileView-xxx.tar.gz 文件到 /opt/kkfileview 目录
tar -zxvf kkFileView-xxx.tar.gz 
```

2. 新版本解压后的目录是kkFileView-xxx，将原来的配置文件和启动脚本复制到新版本目录中

```
cp /opt/kkfileview/kkFileView-latest/conf/application.properties /opt/kkfileview/kkFileView-xxx/conf/application.properties
cp /opt/kkfileview/kkFileView-latest/run.sh /opt/kkfileview/kkFileView-xxx/run.sh
```

3. 重新建立软连接到 kkFileView-latest

```
删除原来的软连接
rm -rf /opt/kkfileview/kkFileView-latest
重新建立新版本的软连接
ln -s kkFileView-xxx kkFileView-latest
```

4. 重启 container 即可

```
docker container restart kkfv
```