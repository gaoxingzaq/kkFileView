chcp 65001
@echo off
title 文件预览服务
taskkill /f /im soffice.bin
taskkill /f /im soffice.exe
set "KKFILEVIEW_BIN_FOLDER=%cd%"
cd "%KKFILEVIEW_BIN_FOLDER%"
echo Using KKFILEVIEW_BIN_FOLDER %KKFILEVIEW_BIN_FOLDER%
echo Starting kkFileView...
echo Please check log file in ../log/kkFileView.log for more information
echo You can get help in our official homesite: https://kkFileView.keking.cn
echo If this project is helpful to you, please star it on https://gitee.com/kekingcn/file-online-preview/stargazers
echo Kaohsiung Revised Edition, please star it on https://gitee.com/gaoxingzaq/file-online-preview-master
java -Dfile.encoding=UTF-8 -Dspring.config.location=..\config\application.properties -jar kkFileView-4.7.9.2.jar -> ..\log\kkFileView.log