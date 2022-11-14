package cn.keking.web.controller;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.service.FileHandlerService;
import cn.keking.service.FilePreview;
import cn.keking.service.FilePreviewFactory;
import cn.keking.service.cache.CacheService;
import cn.keking.service.impl.OtherFilePreviewImpl;
import cn.keking.utils.WebUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import fr.opensagres.xdocreport.core.io.IOUtils;
import io.mola.galimatias.GalimatiasParseException;
import jodd.io.NetUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import static cn.keking.service.FilePreview.Jiaz_FILE_PAGE;
import static cn.keking.service.FilePreview.PPICTURE_FILE_PREVIEW_PAGE;

/**
 * @author yudian-it
 */
@Controller
public class OnlinePreviewController {

    public static final String BASE64_DECODE_ERROR_MSG = "Base64解码失败，请检查你的 %s 是否采用 Base64 + urlEncode 双重编码了！";
    private final Logger logger = LoggerFactory.getLogger(OnlinePreviewController.class);
    private static final String FILE_DIR = ConfigConstants.getFileDir();
    private final FilePreviewFactory previewFactory;
    private final CacheService cacheService;
    private final FileHandlerService fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;

    public OnlinePreviewController(FilePreviewFactory filePreviewFactory, FileHandlerService fileHandlerService, CacheService cacheService, OtherFilePreviewImpl otherFilePreview) {
        this.previewFactory = filePreviewFactory;
        this.fileHandlerService = fileHandlerService;
        this.cacheService = cacheService;
        this.otherFilePreview = otherFilePreview;
    }
    @Value("${pdfpagee:0}")
    private String pdfpagee;
    @GetMapping( "/onlinePrevieww")
    public String onlinePrevieww(String url,String highlightAll, String page, Model model, HttpServletRequest req) {
        String ip = jilvip(req);  //获取IP地址
        if (url == null || url.length()<= 0){
            logger.info("URL异常：{}", ip);
            return otherFilePreview.notSupportedFile(model, "该文件不允许预览：" + url);
        }
        String fileUrl;
        try {
            if(IndexController.isBase64(url)){
                fileUrl = WebUtils.decodeBase64String(url);
            }else {
                fileUrl = url;
            }
        } catch (Exception ex) {
            String errorMsg = String.format(BASE64_DECODE_ERROR_MSG, "url");
            return otherFilePreview.notSupportedFile(model, errorMsg);
        }
        FileAttribute fileAttribute = fileHandlerService.getFileAttribute(fileUrl, req);
        if (highlightAll == null || highlightAll == "" ){
            highlightAll = "null";
        }
        if (page == null || page.equals("")){
            page = "1";
        }
        highlightAll= HtmlUtils.htmlEscape(highlightAll);
        page= HtmlUtils.htmlEscape(page);
        model.addAttribute("highlightAll", highlightAll);
        model.addAttribute("page", page);
        model.addAttribute("file", fileAttribute);
        FilePreview filePreview = previewFactory.get(fileAttribute);
        if(!ConfigConstants.getlocalpreview().equalsIgnoreCase("false")) {
            if (fileUrl.toLowerCase().startsWith("file:") || fileUrl.toLowerCase().startsWith("file%3")) {
                logger.info("URL异常：{}", fileUrl);
                return otherFilePreview.notSupportedFile(model, "该文件不允许预览：" + fileUrl);
            }
        }
        boolean wjl = FileHandlerService.kuayu("&fullfilename=", fileUrl);  //判断是否启用文件流
        if(wjl){
            fileUrl =  fileUrl.substring(0,fileUrl.lastIndexOf("&"));  //删除添加的文件流内容
        }
        logger.info("预览文件url：{}，IP：{}，previewType：{}", fileUrl,ip, fileAttribute.getType());
        return filePreview.filePreviewHandle(fileUrl, model, fileAttribute);
    }
    @GetMapping( "/onlinePreview")
    public String onlinePreview(HttpServletRequest request, Model model) throws IOException{
        String query = request.getQueryString();
        if(query == null||query.length()<= 0){
            return otherFilePreview.notSupportedFile(model, "url异常或者不正确：" + query);
        }
        String urlPath = query.replaceFirst("url=","");
        model.addAttribute("pdfUrl",urlPath);
        return Jiaz_FILE_PAGE;
    }

    @GetMapping( "/picturesPreview")
    public String picturesPreview(String urls, Model model, HttpServletRequest req) throws UnsupportedEncodingException {
        if (urls == null || urls.length()<= 0){
            logger.info("URL异常：{}", urls);
            return otherFilePreview.notSupportedFile(model, "NULL地址不允许预览：");
        }
        String ip = jilvip(req);  //获取IP地址
        String fileUrls;
        try {
            if(IndexController.isBase64(urls)){
                fileUrls = WebUtils.decodeBase64String(urls);
            }else {
                fileUrls = urls;
            }
            fileUrls= HtmlUtils.htmlEscape(fileUrls);
        } catch (Exception ex) {
            String errorMsg = String.format(BASE64_DECODE_ERROR_MSG, "urls");
            return otherFilePreview.notSupportedFile(model, errorMsg);
        }
        if(!ConfigConstants.getlocalpreview().equalsIgnoreCase("false")) {
            if ( fileUrls.toLowerCase().startsWith("file:") || fileUrls.toLowerCase().startsWith("file%3")) {
                logger.info("URL异常：{}", fileUrls);
                return otherFilePreview.notSupportedFile(model, "该文件不允许预览：" + fileUrls);
            }
        }
        boolean wjl = FileHandlerService.kuayu("&fullfilename=", fileUrls.toLowerCase());  // 转换成小写 判断是否启用文件流
        if(wjl){
            fileUrls =  fileUrls.substring(0,fileUrls.lastIndexOf("&"));  //删除添加的文件流内容
        }
        logger.info("预览文件url：{}，ip：{}", fileUrls,ip);

        // 抽取文件并返回文件列表
        String[] images = fileUrls.split("\\|");
        List<String> imgUrls = Arrays.asList(images);
        model.addAttribute("imgUrls", imgUrls);
        String currentUrl = req.getParameter("currentUrl");
        if (StringUtils.hasText(currentUrl)) {
            String decodedCurrentUrl = new String(Base64.decodeBase64(currentUrl));
            model.addAttribute("currentUrl", decodedCurrentUrl);
        } else {
            model.addAttribute("currentUrl", imgUrls.get(0));
        }
        return PPICTURE_FILE_PREVIEW_PAGE;
    }
       static String jilvip(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
    /**
     * 根据url获取文件内容
     * 当pdfjs读取存在跨域问题的文件时将通过此接口读取
     *
     * @param response response
     */
    @GetMapping("/getCorsFile")
    public String getCorsFile(String urlPath, Model model, HttpServletResponse response) {
        if (urlPath == null || urlPath.length()<= 0){
            logger.info("URL异常：{}", urlPath);
            return otherFilePreview.notSupportedFile(model, "NULL地址不允许预览：");
        }
        HttpURLConnection urlcon;
        boolean xieyi ;
        if(urlPath.toLowerCase().startsWith("ftp:")) {
            xieyi= false;
        }else {
            xieyi= true;
        }
        if(!ConfigConstants.getlocalpreview().equalsIgnoreCase("false")) {
            if(urlPath.toLowerCase().startsWith("file:") || urlPath.toLowerCase().startsWith("file%3")) {
                return otherFilePreview.notSupportedFile(model, "不支持本地协议：" + urlPath);
            }
        }
            urlPath = urlPath.replace("%20", " ");
            urlPath = urlPath.replace("?pdfXianzhi="+ConfigConstants.getpdfXianzhi(),"");
            try {
                urlPath = URLDecoder.decode(urlPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } if(xieyi){
                try {
                    URL url = WebUtils.normalizedURL(urlPath);
                    urlcon=(HttpURLConnection)url.openConnection();
                    urlcon.setConnectTimeout(30000);
                    urlcon.setReadTimeout(30000);
                    urlcon.setInstanceFollowRedirects(false);
                    if(urlcon.getResponseCode() ==302 ||urlcon.getResponseCode() ==301){
                        url =new URL(urlcon.getHeaderField("Location"));
                        urlcon=(HttpURLConnection)url.openConnection();
                        urlcon.setConnectTimeout(30000);
                        urlcon.setReadTimeout(30000);
                        urlcon.setInstanceFollowRedirects(false);
                        //  System.out.println(urlcon.getResponseCode());
                    }
                    if(urlcon.getResponseCode() ==404 ||urlcon.getResponseCode() ==403 ||urlcon.getResponseCode() ==500 ){
                        logger.error("读取跨域文件异常，url：{}", urlPath);
                        return otherFilePreview.notSupportedFile(model, "地址错误：" + urlPath);
                    }else {
                        if(urlPath.contains( ".svg")) {
                            response.setContentType("image/svg+xml");
                        }
                        byte[] bytes = NetUtil.downloadBytes(url.toString());
                        IOUtils.write(bytes, response.getOutputStream());
                    }
                } catch (IOException | GalimatiasParseException e) {
                    logger.error("读取跨域文件异常，url：{}", urlPath);
                    return otherFilePreview.notSupportedFile(model, "文件有问题：" + urlPath);
                }
            }else {
                try {
                    URL url = WebUtils.normalizedURL(urlPath);
                    if(urlPath.contains( ".svg")) {
                        response.setContentType("image/svg+xml");
                    }
                    byte[] bytes = NetUtil.downloadBytes(url.toString());
                    IOUtils.write(bytes, response.getOutputStream());
                } catch (IOException | GalimatiasParseException e) {
                    logger.error("读取跨域文件异常，url：{}", urlPath);
                    return otherFilePreview.notSupportedFile(model, "文件有问题：" + urlPath);
                }
            }
        return null;
    }
    /**
     * PDF分片功能
     */
    @GetMapping("/download")
    public String download(String urlPath, Model model, HttpServletResponse response,HttpServletRequest req) throws IOException, DocumentException {
        String ip = jilvip(req);
        if (urlPath == null ||urlPath.length()<= 0 ) {
            logger.info("读取文件异常：{}，ip：{}", urlPath,ip);
            return otherFilePreview.notSupportedFile(model, "文件异常：" + urlPath);
        }
        if(urlPath.toLowerCase().startsWith("file:") || urlPath.toLowerCase().startsWith("file%3")){
            logger.info("文件地址异常：{}", urlPath);
        }else if(!urlPath.toLowerCase().startsWith("http")){
            urlPath ="file:///"+  FILE_DIR + urlPath;
        }
        String page = null;
        if(pdfpagee.equalsIgnoreCase("0")){
            try {
                page = urlPath.substring(urlPath.lastIndexOf("=") + 1);
            } catch (Exception e) {
                logger.error("地址不合法，url：{}", urlPath);
            }
        }
        try {
            urlPath = urlPath.substring(0,urlPath.lastIndexOf("?"));
            urlPath = URLDecoder.decode(urlPath, "UTF-8");
        } catch (Exception e) {
            logger.error("没有分页参数，url：{}", urlPath);
        }
        if (page == null || "".equals(page) ||"pdf".equals(page) ||"NaN".equals(page) ){
            page="1";
        }
        logger.info("读取PDF分页文件url：{}", URLDecoder.decode(urlPath, "UTF-8"));
        try {
            String pdfname = urlPath.substring(urlPath.lastIndexOf("."));
            if(pdfname.equalsIgnoreCase(".pdf")){ //判断是否PDF文件
                // System.out.println(urlPath);
                PdfReader reader = new PdfReader(urlPath);
                //总页数
                int numberOfPages = reader.getNumberOfPages();
                // 截取开始页
                //截取pdf部分页，格式"2-5" 第2页到第5页 页码超出范围（10页，你选择"15-20"）只会读最后一页
                int start;
                if(pdfpagee.equalsIgnoreCase("0")){
                    start = Integer.parseInt(page.substring(0, 1));
                    reader.selectPages(page);
                }else {
                    start = Integer.parseInt(pdfpagee.substring(0, 1));
                    reader.selectPages(pdfpagee);
                }
                //源码没怎么看懂，但是需要内存中存放文件流，所以用了HttpServletResponse
                PdfStamper stamp = new PdfStamper(reader, response.getOutputStream());
                // 开始页 如果大于pdf总页数，不返回文件流，stamp.close()结果返回1
                if(start <= numberOfPages){
                    stamp.close();
                }
                reader.close();
            }else {
                logger.info("文件异常：{}", urlPath);
                return otherFilePreview.notSupportedFile(model, "非PDF文件不能使用该功能：" + urlPath);
            }
        } catch (IOException e) {
            logger.info("文件异常：{}", urlPath);
            return otherFilePreview.notSupportedFile(model, "非PDF文件不能使用该功能：" + urlPath);
        }
        return null;
    }
    /**
     * 通过api接口入队
     *
     * @param url 请编码后在入队
     */
    @GetMapping("/addTask")
    @ResponseBody
    public String addQueueTask(String url) {
        logger.info("添加转码队列url：{}", url);
        if (url == null ||url.length()<= 0 ) {
            return "地址为空!失败";
        }
        if(!ConfigConstants.getlocalpreview().equalsIgnoreCase("false")) {
            if ( url.toLowerCase().startsWith("file:") || url.toLowerCase().startsWith("file%3")) {
                return "地址不合法!失败";
            }
        }
        cacheService.addQueueTask(url);
        return "success";
    }
}
