package cn.keking.utils;

import cn.keking.service.FileHandlerService;
import cn.keking.web.controller.IndexController;
import io.mola.galimatias.GalimatiasParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : kl
 * create : 2020-12-27 1:30 上午
 **/
public class WebUtils {
    private final static String ENCODE = "GBK";
    /**
     * 获取标准的URL
     * @param urlStr url
     * @return 标准的URL
     */
    public static URL normalizedURL(String urlStr) throws GalimatiasParseException, MalformedURLException {
        /**
        try {
            String newFileName = java.net.URLDecoder.decode(urlStr, "utf-8");
            URL urlStrr = new URL(newFileName);
            return io.mola.galimatias.URL.parse(urlStr).toJavaURL();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
         */
        return io.mola.galimatias.URL.parse(urlStr).toJavaURL();
    }

    /**
     * 获取url中的参数
     *
     * @param url  url
     * @param name 参数名
     * @return 参数值
     */
    public static String getUrlParameterReg(String url, String name) {
        Map<String, String> mapRequest = new HashMap<>();
        String strUrlParam = truncateUrlPage(url);
        if (strUrlParam == null) {
            return "";
        }
        //每个键值为一组
        String[] arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else if (!arrSplitEqual[0].equals("")) {
                //只有参数没有值，不加入
                mapRequest.put(arrSplitEqual[0], "");
            }
        }
        return mapRequest.get(name);
    }


    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        strURL = strURL.trim();
        if (strURL == null ){
            return strAllParam;
        }
        String[] arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 从url中剥离出文件名
     *
     * @param url 格式如：http://www.com.cn/20171113164107_月度绩效表模板(新).xls?UCloudPublicKey=ucloudtangshd@weifenf.com14355492830001993909323&Expires=&Signature=I D1NOFtAJSPT16E6imv6JWuq0k=
     * @return 文件名
     */
    public static String getFileNameFromURL(String url) {
        // 因为url的参数中可能会存在/的情况，所以直接url.lastIndexOf("/")会有问题
        // 所以先从？处将url截断，然后运用url.lastIndexOf("/")获取文件名
       // String noQueryUrl = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        if (url.toLowerCase().startsWith("file:")) {
            try {
                URL urlObj = new URL(url);
                url = urlObj.getPath().substring(1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        String noQueryUrl = url;
        try {
            noQueryUrl= FileHandlerService.zhuanyii(noQueryUrl);
            noQueryUrl = URLDecoder.decode(noQueryUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return noQueryUrl.substring(noQueryUrl.lastIndexOf("/") + 1);
    }


    /**
     * 从url中获取文件后缀
     *
     * @param url url
     * @return 文件后缀
     */
    public static String suffixFromUrl(String url) {
       // String nonPramStr = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        String nonPramStr = url;
        try {
            nonPramStr= FileHandlerService.zhuanyii(nonPramStr);
            nonPramStr = URLDecoder.decode(nonPramStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String fileName = nonPramStr.substring(nonPramStr.lastIndexOf("/") + 1);
        return KkFileUtils.suffixFromFileName(fileName);
    }

    /**
     * 对url中的文件名进行UTF-8编码
     *
     * @param url url
     * @return 文件名编码后的url
     */
    public static String encodeUrlFileName(String url) {
       // String noQueryUrl = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        int fileNameStartIndex = url.lastIndexOf('/') + 1;
        int fileNameEndIndex = url.lastIndexOf('.');
        if (fileNameStartIndex >= fileNameEndIndex) {
            return url;
        }
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(url.substring(fileNameStartIndex, fileNameEndIndex), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        encodedFileName = encodedFileName.replace("\\+", "%20");
        return url.substring(0, fileNameStartIndex) + encodedFileName + url.substring(fileNameEndIndex);
    }

    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 将 Base64 字符串解码，默认使用 UTF-8
     * @param source 原始 Base64 字符串
     * @return decoded string
     */
    public static String decodeBase64String(String source) {
        return decodeBase64String(source, StandardCharsets.UTF_8);
    }
    /**
     * 将 Base64 字符串使用指定字符集解码
     * @param source 原始 Base64 字符串
     * @param charsets 字符集
     * @return decoded string
     */
    public static String decodeBase64String(String source, Charset charsets) {
        /*
         * url 传入的参数里加号会被替换成空格，导致解析出错，这里需要把空格替换回加号
         * 有些 Base64 实现可能每 76 个字符插入换行符，也一并去掉
         * https://github.com/kekingcn/kkFileView/pull/340
         */
        return new String(Base64Utils.decodeFromString(
                source.replaceAll(" ", "+").replaceAll("\n", "")
        ), charsets);
    }
    /**
     * 从 ServletRequest 获取预览的源 url , 已 base64 解码
     *
     * @param request 请求 request
     * @return url
     */
    public static String getSourceUrl(ServletRequest request) {
        String url = request.getParameter("url");
        String currentUrl = request.getParameter("currentUrl");
        String urlPath = request.getParameter("urlPath");
        if (StringUtils.isNotBlank(url)) {
            return url;
        }
        if (StringUtils.isNotBlank(currentUrl)) {
            return currentUrl;
        }
        if (StringUtils.isNotBlank(urlPath)) {
            return urlPath;
        }
        return null;
    }

    /**
     * 获取 url 的 host
     * @param urlStr url
     * @return host
     */
    public static String getHost(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return url.getHost().toLowerCase();
        } catch (MalformedURLException ignored) {
        }
        return null;
    }

}
