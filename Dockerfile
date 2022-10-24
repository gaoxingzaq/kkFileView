FROM ubuntu:20.04


RUN cd /tmp && \
    echo "nameserver 223.5.5.5" >> /etc/resolv.conf && \
    echo "nameserver 8.8.8.8" >> /etc/resolv.conf && \
    echo "nameserver 114.114.114.114" >> /etc/resolv.conf && \
    sed -i s@/archive.ubuntu.com/@/mirrors.aliyun.com/@g /etc/apt/sources.list && \
    sed -i s@/security.ubuntu.com/@/mirrors.aliyun.com/@g /etc/apt/sources.list && \
    apt-get clean && apt-get update && apt-get install --assume-yes apt-utils && \
    apt-get install -y locales language-pack-zh-hans wget && \
    localedef -i zh_CN -c -f UTF-8 -A /usr/share/locale/locale.alias zh_CN.UTF-8 && locale-gen zh_CN.UTF-8 && \
    apt-get install -y tzdata && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    apt-get install -y libxrender1 libxt6 libxext-dev libfreetype6-dev unzip && \
    # apt-get install -y libfreetype6=2.10.1-2 && \
    apt-get install -y ttf-mscorefonts-installer fontconfig ttf-wqy-microhei ttf-wqy-zenhei xfonts-wqy && \
    # mkdir kkfileview release
    mkdir -p /opt/kkFileView && \
    # wget fonts
    mkdir -p /usr/share/fonts/chinese && \
    wget http://kkfileview.keking.cn/fonts.zip && \
    unzip /tmp/fonts.zip && mv /tmp/zhFonts/* /usr/share/fonts/chinese/ && \
	# wget jre , jre is java runtime
    wget https://kkfileview.keking.cn/server-jre-8u251-linux-x64.tar.gz -cO server-jre-8u251-linux-x64.tar.gz && \
    tar -zxf /tmp/server-jre-8u251-linux-x64.tar.gz && mv /tmp/jdk1.8.0_251 /usr/local/ && \
    #	安装 libreoffice_7.1.4
    apt-get install -y libxinerama1 libcairo2 libcups2 libx11-xcb1 && \
    wget https://kkfileview.keking.cn/LibreOffice_7.1.4_Linux_x86-64_deb.tar.gz -cO libreoffice_deb.tar.gz &&\
    tar -zxf /tmp/libreoffice_deb.tar.gz && cd /tmp/LibreOffice_7.1.4.2_Linux_x86-64_deb/DEBS &&\
    dpkg -i *.deb && \
    rm -rf /tmp/* && rm -rf /var/lib/apt/lists/* && \
    apt-get autoremove && \
    cd /usr/share/fonts/chinese && \
    mkfontscale && \
    mkfontdir && \
    fc-cache -fv
ENV JAVA_HOME /usr/local/jdk1.8.0_251
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin
ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8
ENV KKFILEVIEW_BIN_FOLDER /opt/kkFileView/bin