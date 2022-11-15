package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.*;
import cn.keking.utils.DownloadUtils;
import cn.keking.utils.KkFileUtils;
import cn.keking.utils.OfficeUtils;
import cn.keking.web.filter.BaseUrlFilter;
import jodd.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kl on 2018/1/17.
 * Content :处理office文件
 */
@Service
public class OfficeFilePreviewImpl implements FilePreview {

    public static final String OFFICE_PREVIEW_TYPE_IMAGE = "image";
    public static final String OFFICE_PREVIEW_TYPE_ALL_IMAGES = "allImages";
    private static final String FILE_DIR = ConfigConstants.getFileDir();
    private final FileHandlerService fileHandlerService;
    private final OfficeToPdfService officeToPdfService;
    private final OtherFilePreviewImpl otherFilePreview;

    public OfficeFilePreviewImpl(FileHandlerService fileHandlerService, OfficeToPdfService officeToPdfService, OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.officeToPdfService = officeToPdfService;
        this.otherFilePreview = otherFilePreview;
    }
    @Value("${office.officexh:1}")
    private String officexh;
    @Value("${pdfsize:10}")
    private int pdfsize;
    @Value("${pdfpagee:0}")
    private String pdfpagee;
    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        // 预览Type，参数传了就取参数的，没传取系统默认
        String gengxin=fileAttribute.getgengxin();
        String officePreviewType = fileAttribute.getOfficePreviewType();
        String baseUrl = BaseUrlFilter.getBaseUrl();
        String suffix = fileAttribute.getSuffix();
        String fileName = fileAttribute.getName();
        String filePassword = fileAttribute.getFilePassword();
        String regEx = "[`#%:\\[\\];.\"\\\\]"; //针对特殊符号
        String fileNamee = Pattern.compile(regEx).matcher(fileName).replaceAll("").trim();
        String imagesss = FILE_DIR + fileName;
        if(officePreviewType.equalsIgnoreCase("poi") ||officexh.equals("2")){  //判断需要用什么转换器
            officexh= String.valueOf(2);
        }else if (officePreviewType.equalsIgnoreCase("office")){
            officexh= String.valueOf(1);
        }else {
            officexh=String.valueOf(1);
        }
        boolean isHtml;
        boolean doc = suffix.equalsIgnoreCase("doc") || suffix.equalsIgnoreCase("docx") || suffix.equalsIgnoreCase("wps") || suffix.equalsIgnoreCase("dot") || suffix.equalsIgnoreCase("docm") || suffix.equalsIgnoreCase("vsd")  || suffix.equalsIgnoreCase("wmf") || suffix.equalsIgnoreCase("emf")|| suffix.equalsIgnoreCase("dps") || suffix.equalsIgnoreCase("vsdx") || suffix.equalsIgnoreCase("ott");
        boolean xls = suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx") || suffix.equalsIgnoreCase("xlsm")  || suffix.equalsIgnoreCase("et") || suffix.equalsIgnoreCase("ods")|| suffix.equalsIgnoreCase("ots")|| suffix.equalsIgnoreCase("tsv");
        boolean ppt = suffix.equalsIgnoreCase("ppt") || suffix.equalsIgnoreCase("pptx") || suffix.equalsIgnoreCase("dps") || suffix.equalsIgnoreCase("odp") || suffix.equalsIgnoreCase("otp") || suffix.equalsIgnoreCase("sxi") ;
        if (officexh.equals("1")) {
             isHtml = suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx") || suffix.equalsIgnoreCase("xlsm") || suffix.equalsIgnoreCase("csv") || suffix.equalsIgnoreCase("et")|| suffix.equalsIgnoreCase("ods")|| suffix.equalsIgnoreCase("ots")|| suffix.equalsIgnoreCase("tsv");
        }else {
             isHtml = suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")  || suffix.equalsIgnoreCase("doc") || suffix.equalsIgnoreCase("docx") || suffix.equalsIgnoreCase("wps") || suffix.equalsIgnoreCase("ppt") || suffix.equalsIgnoreCase("pptx");
        }
        String pdfName = fileNamee + "." + (isHtml ? "html" : "pdf");
        String ptxName = fileNamee + "." + "file";
        String outFilePath = FILE_DIR + pdfName;
        // 判断之前是否已转换过，如果转换过，直接返回，否则执行转换
        boolean pdfgx ;
        if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) { //去缓存更新
            pdfgx= true;
        }else {
            pdfgx= false;
        }
        boolean xlsx =  suffix.equalsIgnoreCase("xlsx");

        if(officePreviewType.equalsIgnoreCase("xlsx") || ConfigConstants.getxlsxzh().equalsIgnoreCase("true")){  //是否开启xlsx直接输出功能

            if(officePreviewType.equalsIgnoreCase("html")){

            }else if(officePreviewType.equalsIgnoreCase("xlsx")) {
                if(xlsx){
                    model.addAttribute("pdfUrl", url);
                    return XLSX_FILE_PREVIEW_PAGE;
                }
            }else {
                if(xlsx){
                    model.addAttribute("pdfUrl", url);
                    return XLSX_FILE_PREVIEW_PAGE;
                }
            }
        }
        if (pdfgx || !fileHandlerService.listConvertedFiles().containsKey(pdfName) || !ConfigConstants.isCacheEnabled()) {
            String filePath;
            ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, null);
            if (response.isFailure()) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
            }
            filePath = response.getContent();
         if (officexh.equals("2")) { //POI转换
                KkFileUtils.deleteFileByPath(outFilePath);
                try {
                    if(doc){
                        if(POIWordToHtml.wordToHtml(filePath, imagesss, fileName, outFilePath)){
                        }else {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他，尝试其他文件");
                        }
                    }else if(xls){
                        if(POIExcelToHtml.excelToHtml(outFilePath,  filePath,"")){
                        }else {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他，尝试其他文件");
                        }
                    }else if(ppt){
                        int pptx = Integer.parseInt(ConfigConstants.getPPTXTP());
                        if(POIPptToHtml.pptToHtml(filePath, ptxName,outFilePath, pptx)){
                        }else {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他，尝试其他文件");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {   //开源openoffice 或  LibreOffice转换
             if (StringUtils.hasText(outFilePath)) {
                 String geshi =FileHandlerService.geshi(filePath,0);// 获取文件头信息
                     if (geshi.equals(".2003office") || geshi.equals(".2010offcie")  || geshi.equals(".QT") || geshi.equals(".rtf") || geshi.equals(".xmln") || suffix.equalsIgnoreCase("wmf") || suffix.equalsIgnoreCase("emf") ){  //判断是什么格式的文件
                         File file = new File(outFilePath);
                         if(pdfgx){
                             FileHandlerService.AT_CONVERT_MAP.remove(file.getName(), 1);
                         }
                         if (ConfigConstants.isCacheEnabled()) {
                             if (FileHandlerService.AT_CONVERT_MAP.get(file.getName()) != null) {   //判断文件是否在转换中
                                 //  System.out.println(" 文件 [%s] 正在转换中" +file.getName());
                                 return otherFilePreview.notSupportedFile(model, fileAttribute, "文件["+fileNamee+"]正在转换中,请稍后刷新访问");
                             }else {
                                 if (OfficeUtils.isPwdProtected(filePath) && !StringUtils.hasLength(filePassword)) {
                                     FileHandlerService.AT_CONVERT_MAP.remove(file.getName(), 1);
                                     model.addAttribute("pdfUrl", url);
                                     return  Jimi_FILE_PAGE;   //是加密文件 密码为空 输出密码框
                                 }else {
                                     try {
                                         officeToPdfService.openOfficeToPDF(filePath, outFilePath, fileAttribute); //转换
                                     } catch (Exception e) {
                                         if (OfficeUtils.isCompatible(filePath, filePassword) == false) {
                                             FileHandlerService.AT_CONVERT_MAP.remove(file.getName(), 1);
                                             model.addAttribute("pdfUrl", url);
                                             return  Jimi_FILE_PAGE;   //是加密文件 密码为空 输出密码框
                                         }
                                         return otherFilePreview.notSupportedFile(model, fileAttribute, "抱歉，该文件版本不兼容，文件版本错误1。");
                                     }
                                 }
                             }
                         }else {
                             if (OfficeUtils.isPwdProtected(filePath) && !StringUtils.hasLength(filePassword)) {
                                 FileHandlerService.AT_CONVERT_MAP.remove(file.getName(), 1);
                                 model.addAttribute("pdfUrl", url);
                                 return  Jimi_FILE_PAGE;   //是加密文件 密码为空 输出密码框
                             }else {
                                 try {
                                     officeToPdfService.openOfficeToPDF(filePath, outFilePath, fileAttribute); //转换
                                 } catch (Exception e) {
                                     if (OfficeUtils.isCompatible(filePath, filePassword) == false) {
                                         FileHandlerService.AT_CONVERT_MAP.remove(file.getName(), 1);
                                         model.addAttribute("pdfUrl", url);
                                         return  Jimi_FILE_PAGE;   //是加密文件 密码为空 输出密码框
                                     }
                                     return otherFilePreview.notSupportedFile(model, fileAttribute, "抱歉，该文件版本不兼容，文件版本错误2。");
                                 }
                             }
                         }
                     }else if(geshi.equals(".xml")) {  //如果是XML格式的WORD就用下面方法
                         String fileData = null;
                         try {
                             fileData = HtmlUtils.htmlEscape(SimTextFilePreviewImpl.textData(filePath));
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         model.addAttribute("textData", Base64.encodeBase64String(fileData.getBytes()));
                         return XML_FILE_PREVIEW_PAGE;
                     }else if(suffix.equalsIgnoreCase("csv")) {  //如果是XML格式的WORD就用下面方法
                         String fileData = null;
                         try {
                             fileData = HtmlUtils.htmlEscape(SimTextFilePreviewImpl.textData(filePath));
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         model.addAttribute("textData", Base64.encodeBase64String(fileData.getBytes()));
                         return TXT_FILE_PREVIEW_PAGE;
                     }else {
                         if(ConfigConstants.getofficedel().equalsIgnoreCase("false")){  //是否保留OFFICE源文件
                             KkFileUtils.deleteFileByPath(filePath);
                         }
                         return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                     }

                 if (isHtml) {
                     // 对转换后的文件进行操作(改变编码方式)
                     fileHandlerService.doActionConvertedFile(outFilePath);
                 }
             }
         }
            if( ConfigConstants.getofficedel().equalsIgnoreCase("false")){  //是否保留OFFICE源文件
                KkFileUtils.deleteFileByPath(filePath);
            }
            File file = new File(FILE_DIR + pdfName);   //判断文件是否存在
            if(!file.exists() || file.length() == 0) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他，尝试其他文件");
            }
            if (ConfigConstants.isCacheEnabled()) {
                // 加入缓存
                if (null != filePassword && !"".equals(filePassword))  {

                }else {
                    fileHandlerService.addConvertedFile(pdfName, fileHandlerService.getRelativePath(outFilePath));
                }

            }
        }
        if (!isHtml && baseUrl != null && (OFFICE_PREVIEW_TYPE_IMAGE.equals(officePreviewType) || OFFICE_PREVIEW_TYPE_ALL_IMAGES.equals(officePreviewType))) {
            return getPreviewType(model, fileAttribute, officePreviewType, baseUrl, pdfName, outFilePath, fileHandlerService, OFFICE_PREVIEW_TYPE_IMAGE, otherFilePreview);
        }

        if(isHtml || ConfigConstants.getpdffy().equalsIgnoreCase("false")){  //是否开启PDF分割功能

        }else {

          if(FileHandlerService.pdfpage(pdfName) <=1){  //判断PDF文件页码 当小于等于1就不进行分割
              pdfName= FileHandlerService.zhuanyii(pdfName); //文件名转义
          }else {
              double pdfdx = FileHandlerService.getDirSize(new File(FILE_DIR + pdfName)); //判断PDF文件大小 大于设定值就分页
              BigDecimal data = new BigDecimal(pdfdx);  //判断文件大小
              pdfName= FileHandlerService.zhuanyii(pdfName);  //文件名转义
              if (data.compareTo(data) < pdfsize) {

              //    try {   //PDF压缩
              //      new Pdfyashuo().manipulatePdf(FILE_DIR + "综合宣传册.pdf",
                     //       FILE_DIR +"ls22"+ "业绩册.pdf");
             //    } catch (IOException e) {
               //      e.printStackTrace();
             //    } catch (DocumentException e) {
               //      e.printStackTrace();
              //    }
                  model.addAttribute("fenye",FileHandlerService.pdfpage(pdfName));
                  model.addAttribute("pdfpagee",pdfpagee);
                  pdfName ="download?urlPath="+pdfName;
                  model.addAttribute("pdfUrl", pdfName);
                  return  FYPDF_FILE_PREVIEW_PAGE;
              }else {
                  pdfName ="download?urlPath="+pdfName+"?page="+pdfpagee;   //分割PDF文件
              }
          }
        }
        model.addAttribute("pdfUrl", pdfName);
       return isHtml ? EXEL_FILE_PREVIEW_PAGE : PDF_FILE_PREVIEW_PAGE;
    }

    static String getPreviewType(Model model, FileAttribute fileAttribute, String officePreviewType, String baseUrl, String pdfName, String outFilePath, FileHandlerService fileHandlerService, String officePreviewTypeImage, OtherFilePreviewImpl otherFilePreview) {
        String suffix = fileAttribute.getSuffix();
        boolean isPPT = suffix.equalsIgnoreCase("ppt") || suffix.equalsIgnoreCase("pptx");
        List<String> imageUrls = fileHandlerService.pdf2jpg(outFilePath, pdfName, baseUrl, fileAttribute);
        if (imageUrls == null || imageUrls.size() < 1) {
            return otherFilePreview.notSupportedFile(model, fileAttribute, "office转图片异常，请联系管理员");
        }
        model.addAttribute("imgurls", imageUrls);
        model.addAttribute("currentUrl", imageUrls.get(0));
        if (officePreviewTypeImage.equals(officePreviewType)) {
            // PPT 图片模式使用专用预览页面
            return (isPPT ? PPT_FILE_PREVIEW_PAGE : OFFICE_PICTURE_FILE_PREVIEW_PAGE);
        } else {
            return PICTURE_FILE_PREVIEW_PAGE;
        }
    }
}
