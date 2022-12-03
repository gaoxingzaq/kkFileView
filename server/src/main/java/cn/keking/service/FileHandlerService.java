package cn.keking.service;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.FileType;
import cn.keking.service.cache.CacheService;
import cn.keking.utils.*;
import com.aspose.cad.CodePages;
import com.aspose.cad.Color;
import com.aspose.cad.Image;
import com.aspose.cad.LoadOptions;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PdfOptions;
import com.itextpdf.text.pdf.PdfReader;
import jodd.util.StringUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yudian-it
 */
@Component
public class FileHandlerService {

    private final Logger logger = LoggerFactory.getLogger(FileHandlerService.class);

    private final String fileDir = ConfigConstants.getFileDir();
    private final CacheService cacheService;
    private static final String FILE_DIR = ConfigConstants.getFileDir();
    public FileHandlerService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    /**
     * @return 已转换过的文件集合(缓存)
     */
    public Map<String, String> listConvertedFiles() {
        return cacheService.getPDFCache();
    }
    /**
     * @return 已转换过的文件，根据文件名获取
     */
    public String getConvertedFile(String key) {
        return cacheService.getPDFCache(key);
    }
    /**
     * @param key pdf本地路径
     * @return 已将pdf转换成图片的图片本地相对路径
     */
    public Integer getConvertedPdfImage(String key) {
        return cacheService.getPdfImageCache(key);
    }
    /**
     * 从路径中获取文件负
     *
     * @param path 类似这种：C:\Users\yudian-it\Downloads
     * @return 文件名
     */
    public String getFileNameFromPath(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    /**
     * 获取相对路径
     *
     * @param absolutePath 绝对路径
     * @return 相对路径
     */
    public String getRelativePath(String absolutePath) {
        return absolutePath.substring(fileDir.length());
    }
    /**
     * 记录是否转换
     *
     */

    public static Map<String, Integer> AT_CONVERT_MAP =  new HashMap<>();

    /**
     * 添加转换后PDF缓存
     *
     * @param fileName pdf文件名
     * @param value    缓存相对路径
     */
    public void addConvertedFile(String fileName, String value) {
        cacheService.putPDFCache(fileName, value);
    }

    /**
     * 添加转换后图片组缓存
     *
     * @param pdfFilePath pdf文件绝对路径
     * @param num         图片张数
     */
    public void addConvertedPdfImage(String pdfFilePath, int num) {
        cacheService.putPdfImageCache(pdfFilePath, num);
    }

    /**
     * 获取redis中压缩包内图片文件
     *
     * @param fileKey fileKey
     * @return 图片文件访问url列表
     */
    public List<String> getImgCache(String fileKey) {
        return cacheService.getImgCache(fileKey);
    }

    /**
     * 设置redis中压缩包内图片文件
     *
     * @param fileKey fileKey
     * @param imgs    图片文件访问url列表
     */
    public void putImgCache(String fileKey, List<String> imgs) {
        cacheService.putImgCache(fileKey, imgs);
    }

    /**
     * 对转换后的文件进行操作(改变编码方式)
     *
     * @param outFilePath 文件绝对路径
     */
    public void doActionConvertedFile(String outFilePath) {
        StringBuilder sb = new StringBuilder();
        String charset = EncodingDetects.getJavaEncode(outFilePath);
        try (InputStream inputStream = new FileInputStream(outFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while (null != (line = reader.readLine())) {
                if (line.contains("charset=gb2312")) {
                    line = line.replace("charset=gb2312", "charset=utf-8");
                }
                sb.append(line);
            }
            // 添加sheet控制头
            sb.append("<script src=\"js/jquery-3.0.0.min.js\" type=\"text/javascript\"></script>");
            sb.append("<script src=\"js/excel.header.js\" type=\"text/javascript\"></script>");
            sb.append("<link rel=\"stylesheet\" href=\"bootstrap/css/xlsx.css\">");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 重新写入文件
        try (FileOutputStream fos = new FileOutputStream(outFilePath);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取PDF 页数
     *
     */
    public static int pdfpage(String pdfName) {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(new FileInputStream(pdfName)); //获取PDF页数 如果小于等于1就不进行分割
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pages = pdfReader.getNumberOfPages();
        return pages;
    }

    /**
     * 对分割的PDF文件下载到本地
     */
    private File getNetUrlHttp(String newUrl, String fileName){
        //保存到本地的路径
        String filePath=FILE_DIR+"ls"+fileName;
        File file = null;
        URL urlfile;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            //判断文件的父级目录是否存在，不存在则创建
            file = new File(filePath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try{
                //创建文件
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
            //下载
            urlfile = new URL(newUrl.replace(" ", "%20"));
            inputStream = urlfile.openStream();
            outputStream = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead=inputStream.read(buffer,0,8192))!=-1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     *  判断文件大小
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }


    /**
     *  输出文件名转义
     */
    public static String zhuanyii(String fuhao) {
        fuhao = fuhao.replace("%","%25");
        //  fuhao = fuhao.replace("%","%25");
        //    fuhao = fuhao.replace("+","%2B");
        //   fuhao = fuhao.replace(" ","%20");
        //   fuhao = fuhao.replace("?","%3F");
        fuhao = fuhao.replace("[","%5B");
        fuhao = fuhao.replace("]","%5D");
        fuhao = fuhao.replace("#","%23");
        fuhao = fuhao.replace("&","%26");
        //    fuhao = fuhao.replace("=","%3D");
        return fuhao;
    }


    /**
     *  查询是否本地URL
     */
    public static boolean kuayu(String host, String wjl) {  //查询域名是否相同
        if (wjl.contains(host)) {
            return true;
        }else {
            return false;
        }
    }
    /**
     *  获取URL地址
     */
    public static String hqurl(String url) {
        java.net.URL urls = null;
        try {
            urls = new java.net.URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int port = urls.getPort();
        if (port < 0) {
            // 获取对应协议的默认端口号
            port = urls.getDefaultPort();
        }
        String host = urls.getHost()+ ":" + port;// 获取http信息
        return host;
    }
    /**
     *  获取文件头
     */
    public static String geshi(String outFilePath,int ff) {
        String  geshi = null;
        try {
            if (ff ==1){  //OFD PDF 查询方法
                geshi = WjtTypeUtils.pdftype(new FileInputStream(outFilePath));
            }else {
                geshi = WjtTypeUtils.getPicType(new FileInputStream(outFilePath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return geshi;
    }
    /**
     *  pdf文件转换成jpg图片集
     * pdfFilePath pdf文件路径
     * pdfName pdf文件名称
     * baseUrl 基础访问地址
     * 图片访问集合
     */


    public List<String> pdf2jpg(String pdfFilePath, String pdfName, String baseUrl, FileAttribute fileAttribute) {
        String gengxin=fileAttribute.getgengxin();
        List<String> imageUrls = new ArrayList<>();
        int pdfjpg= Integer.parseInt(ConfigConstants.getpdfjpg());
        Integer imageCount;
        String imageFileSuffix = ".jpg";
        String pdfFolder = pdfName.substring(0, pdfName.length() - 4);
        if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) {  //去缓存更新
            imageCount = Integer.valueOf("0");
        }else {
            if(ConfigConstants.getpdffy().equalsIgnoreCase("false") ||ConfigConstants.getpdfpagee().equalsIgnoreCase("0") ){    //是否开启分片功能
                imageCount = this.getConvertedPdfImage(pdfFilePath);
            }else {
                imageCount = this.getConvertedPdfImage(FILE_DIR+"ls"+pdfName);
            }
        }
        String urlPrefix;
        if(ConfigConstants.getpdffy().equalsIgnoreCase("false")){    //是否开启分片功能
            urlPrefix = baseUrl + pdfFolder;   //不改变路径
        }else {
            if(ConfigConstants.getpdfpagee().equalsIgnoreCase("0") || pdfpage(pdfFilePath)<=1){
                urlPrefix = baseUrl + pdfFolder;   //不改变路径
            }else {
                if (imageCount != null && imageCount > 0) {
                    urlPrefix = baseUrl +"ls"+ pdfFolder;  //改变路径 添加LS路径
                    pdfFilePath = FILE_DIR+"ls"+pdfName;
                }else {
                    urlPrefix = baseUrl +"ls"+ pdfFolder;  //改变路径 添加LS路径
                    pdfFilePath =baseUrl +"download?urlPath="+"file:///" + pdfFilePath+"?";    //链接到PDF分割功能
                    pdfFilePath = String.valueOf(getNetUrlHttp(pdfFilePath,pdfName));
                }
            }
        }
        urlPrefix = urlPrefix.replaceFirst(baseUrl,"");
        String urlPrefixx= zhuanyii(urlPrefix);  //图片名称转义
        if (imageCount != null && imageCount > 0) {
            for (int i = 0; i < imageCount; i++) {
                imageUrls.add(urlPrefixx + "/" + i + imageFileSuffix);
            }
            return imageUrls;
        }
        File pdfFile = new File(pdfFilePath);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(pdfFile);
            int pageCount = doc.getNumberOfPages();
            PDFRenderer pdfRenderer = new PDFRenderer(doc);
            String folder = FILE_DIR + urlPrefix;
            File path = new File(folder);
            if (!path.exists() && !path.mkdirs()) {
                logger.error("创建转换文件【{}】目录失败，请检查目录权限！", folder);
            }
            String imageFilePath;
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                imageFilePath = folder + File.separator + pageIndex + imageFileSuffix;
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, pdfjpg, ImageType.RGB);
                if(ConfigConstants.getyashuo().equalsIgnoreCase("true")) {
                    Thumbnails.of(image).scale(1.0).outputQuality(0.5).toFile(new File(imageFilePath));   //压缩图片
                }else {
                    ImageIOUtil.writeImage(image, imageFilePath, pdfjpg);
                }
                imageUrls.add(urlPrefixx + "/" + pageIndex + imageFileSuffix);
            }
            doc.close();
            this.addConvertedPdfImage(pdfFilePath, pageCount);
        } catch (IOException e) {
            logger.error("Convert pdf to jpg exception, pdfFilePath：{}", pdfFilePath, e);
        }finally{
            if(doc != null){   //关闭
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageUrls;
    }

    /**
     * cad文件转pdf
     * @param inputFilePath cad文件路径
     * @param outputFilePath pdf输出文件路径
     * @return 转换是否成功
     */
    public boolean cadToPdf(String inputFilePath, String outputFilePath)  {
        File outputFile = new File(outputFilePath);
        FileHandlerService.AT_CONVERT_MAP.put(outputFile.getName(), 1);
        LoadOptions opts = new LoadOptions();
        opts.setSpecifiedEncoding(CodePages.SimpChinese);
        com.aspose.cad.Image cadImage = Image.load(inputFilePath, opts);
        CadRasterizationOptions cadRasterizationOptions = new CadRasterizationOptions();
        cadRasterizationOptions.setBackgroundColor(Color.getWhite());
        cadRasterizationOptions.setPageWidth(1400);
        cadRasterizationOptions.setPageHeight(650);
        cadRasterizationOptions.setAutomaticLayoutsScaling(true);
        cadRasterizationOptions.setNoScaling (false);
        cadRasterizationOptions.setDrawType(1);
        PdfOptions pdfOptions = new PdfOptions();
        pdfOptions.setVectorRasterizationOptions(cadRasterizationOptions);
        OutputStream stream;
        try {
            stream = new FileOutputStream(outputFile);
            cadImage.save(stream, pdfOptions);
            stream.close();
            cadImage.close();
            FileHandlerService.AT_CONVERT_MAP.remove(outputFile.getName(), 1);
            return true;
        } catch (IOException e) {
            logger.error("PDFFileNotFoundException，inputFilePath：{}", inputFilePath, e);
        }finally{
            if(cadImage != null){   //关闭
                cadImage.close();
            }
        }
        return false;
    }

    /**
     * 获取文件属性
     *
     * @param url url
     * @return 文件属性
     */
    public FileAttribute getFileAttribute(String url, HttpServletRequest req) {
        FileAttribute attribute = new FileAttribute();
        String suffix;
        FileType type;
        String fileName;
        String fullFileName = WebUtils.getUrlParameterReg(url, "fullfilename");
        if (StringUtils.hasText(fullFileName)) {
            fileName = fullFileName;
            type = FileType.typeFromFileName(fullFileName);
            suffix = KkFileUtils.suffixFromFileName(fullFileName);
        } else {
            fileName = WebUtils.getFileNameFromURL(url);
            type = FileType.typeFromUrl(url);
            suffix = WebUtils.suffixFromUrl(url);
        }
        if(UrlEncoderUtilss.hasUrlEncoded(fileName) && UrlEncoderUtilss.hasUrlEncoded(suffix)){  //判断文件名是否转义
            try {
                fileName = URLDecoder.decode(fileName, "UTF-8");
                suffix = URLDecoder.decode(suffix, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        attribute.setType(type);
        attribute.setName(fileName);
        attribute.setSuffix(suffix);
        url = WebUtils.encodeUrlFileName(url);
        attribute.setUrl(url);
        if (req != null) {
            String officePreviewType = req.getParameter("officePreviewType");
            String fileKey = WebUtils.getUrlParameterReg(url,"fileKey");
            String gengxin = req.getParameter("gengxin");
            if (StringUtils.hasText(officePreviewType)) {
                attribute.setOfficePreviewType(officePreviewType);
            }
            if (StringUtils.hasText(fileKey)) {
                attribute.setFileKey(fileKey);
            }
            if (StringUtils.hasText(gengxin)) {
                attribute.setgengxin(gengxin);
            }
            String tifPreviewType = req.getParameter("tifPreviewType");
            if (StringUtils.hasText(tifPreviewType)) {
                attribute.setTifPreviewType(tifPreviewType);
            }
            String filePassword = req.getParameter("filePassword");
            if (StringUtils.hasText(filePassword)) {
                attribute.setFilePassword(filePassword);
            }
        }
        return attribute;
    }

    /**
     * @return 已转换过的视频文件集合(缓存)
     */
    public Map<String, String> listConvertedMedias() {
        return cacheService.getMediaConvertCache();
    }

    /**
     * 添加转换后的视频文件缓存
     * fileName
     * value
     */
    public void addConvertedMedias(String fileName, String value) {
        cacheService.putMediaConvertCache(fileName, value);
    }

    /**
     * @return 已转换视频文件缓存，根据文件名获取
     */
    public String getConvertedMedias(String key) {
        return cacheService.getMediaConvertCache(key);
    }
}
