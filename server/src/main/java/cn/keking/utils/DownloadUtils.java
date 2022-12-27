package cn.keking.utils;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileHandlerService;
import io.mola.galimatias.GalimatiasParseException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static cn.keking.utils.KkFileUtils.isFtpUrl;
import static cn.keking.utils.KkFileUtils.isHttpUrl;

/**
 * @author yudian-it
 */
@Component
public class DownloadUtils {

    private final static Logger logger = LoggerFactory.getLogger(DownloadUtils.class);
    private static final String fileDir = ConfigConstants.getFileDir();
    private static final String URL_PARAM_FTP_USERNAME = "ftp.username";
    private static final String URL_PARAM_FTP_PASSWORD = "ftp.password";
    private static final String URL_PARAM_FTP_CONTROL_ENCODING = "ftp.control.encoding";
    /**
     * @param fileAttribute fileAttribute
     * @param fileName      文件名
     * @return 本地文件绝对路径
     */
    public static ReturnResponse<String> downLoad(FileAttribute fileAttribute, String fileName) {
        try {
            SSLUtils.ignoreSsl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String urlStr = fileAttribute.getUrl().replaceAll("\\+", "%20");
       // urlStr= FileHandlerService.zhuanyii(urlStr);
        String  urlStrr = urlStr.toLowerCase();  //转换为小写对比
        boolean wjl = FileHandlerService.kuayu("&fullfilename=", urlStrr);  //判断是否启用文件流
        if(wjl){
            urlStr =  urlStr.substring(0,urlStr.lastIndexOf("&"));  //删除添加的文件流内容
        }
        ReturnResponse<String> response = new ReturnResponse<>(0, "下载成功!!!", "");
        ReturnResponse<String> xiazai = new ReturnResponse<>(0, "下载失败!!!", "");
        String realPath = DownloadUtils.getRelFilePath(fileName, fileAttribute);
        String  prohibit=  ConfigConstants.getprohibit();
        String[] simTextArr = prohibit.split(",");
        for (int ii = 0; ii < simTextArr.length; ii++) {
            if (realPath.toLowerCase().contains(simTextArr[ii])){
                System.out.println("该类型文件不允许下载");
                xiazai.setCode(1);
                xiazai.setContent(null);
                return xiazai;
            }
        }
        HttpURLConnection urlcon = null;
        try {
            URL url = WebUtils.normalizedURL(urlStr);
            if(!urlStr.toLowerCase().startsWith("file") && !urlStr.toLowerCase().startsWith("ftp")) {
                urlcon=(HttpURLConnection)url.openConnection();
                urlcon.setConnectTimeout(30000);
                urlcon.setReadTimeout(30000);
                urlcon.setInstanceFollowRedirects(false);
                //  System.out.println("返回码: " + urlcon.getResponseCode());
                try {
                    if(urlcon.getResponseCode() ==404 ||urlcon.getResponseCode() ==403 ||urlcon.getResponseCode() ==500 ){
                        System.out.println("地址错误");
                        xiazai.setCode(1);
                        xiazai.setContent(null);
                        return xiazai;
                    }

                    if(urlcon.getResponseCode() ==302 ||urlcon.getResponseCode() ==301){
                        url =new URL(urlcon.getHeaderField("Location"));
                        urlcon=(HttpURLConnection)url.openConnection();
                        urlcon.setConnectTimeout(30000);
                        urlcon.setReadTimeout(30000);
                        urlcon.setInstanceFollowRedirects(false);
                        if(urlcon.getResponseCode() ==404 ||urlcon.getResponseCode() ==403 ||urlcon.getResponseCode() ==500 ){
                            System.out.println("地址错误");
                            xiazai.setCode(1);
                            xiazai.setContent(null);
                            urlcon.disconnect();
                            return xiazai;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("地址错误");
                    xiazai.setCode(1);
                    xiazai.setContent(null);
                    urlcon.disconnect();
                    return xiazai;
                }
            }
            if (!fileAttribute.getSkipDownLoad()) {
                if (isHttpUrl(url)) {
                    File realFile = new File(realPath);
                     try {
                        FileUtils.copyURLToFile(url, realFile);
                    } catch (IOException e) {
                        System.out.println(e);
                        xiazai.setCode(1);
                        xiazai.setContent(null);
                        return xiazai;
                    }
                    int retryTimes =0;
                    while (retryTimes <3){
                        FileUtils.copyURLToFile(url, realFile);
                       if (realFile.length() != 0){
                          break;
                        }
                        realFile.delete();
                        retryTimes++;
                    }
                } else if (isFtpUrl(url)) {
                    String ftpUsername = WebUtils.getUrlParameterReg(fileAttribute.getUrl(), URL_PARAM_FTP_USERNAME);
                    String ftpPassword = WebUtils.getUrlParameterReg(fileAttribute.getUrl(), URL_PARAM_FTP_PASSWORD);
                    String ftpControlEncoding = WebUtils.getUrlParameterReg(fileAttribute.getUrl(), URL_PARAM_FTP_CONTROL_ENCODING);
                    FtpUtils.download(fileAttribute.getUrl(), realPath, ftpUsername, ftpPassword, ftpControlEncoding);
                } else {
                    response.setCode(1);
                    response.setMsg("url不能识别url" + urlStr);
                }
            }
            response.setContent(realPath);
            response.setMsg(fileName);
            return response;
        } catch (IOException | GalimatiasParseException e) {
            if(urlcon!= null){
                urlcon.disconnect();
            }
            logger.error("文件下载失败，url：{}", urlStr, e);
            response.setCode(1);
            response.setContent(null);
            if (e instanceof FileNotFoundException) {
                response.setMsg("文件不存在!!!");
            } else {
                response.setMsg(e.getMessage());
            }
            return response;
        }
    }


    /**
     * 获取真实文件绝对路径
     *
     * @param fileName 文件名
     * @return 文件路径
     */
    private static String getRelFilePath(String fileName, FileAttribute fileAttribute) {
        String type = fileAttribute.getSuffix();
        if (null == fileName) {
            UUID uuid = UUID.randomUUID();
            fileName = uuid + "." + type;
        } else { // 文件后缀不一致时，以type为准(针对simText【将类txt文件转为txt】)
            fileName = fileName.replace(fileName.substring(fileName.lastIndexOf(".") + 1), type);
        }
        // 判断是否非法地址
        if (KkFileUtils.isIllegalFileName(fileName)) {
            return null;
        }
        String realPath = fileDir + fileName;
        File dirFile = new File(fileDir);
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            logger.error("创建目录【{}】失败,可能是权限不够，请检查", fileDir);
        }
        return realPath;
    }

}
