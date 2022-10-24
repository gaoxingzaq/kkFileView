package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileHandlerService;
import cn.keking.service.FilePreview;
import cn.keking.utils.DownloadUtils;
import cn.keking.web.filter.BaseUrlFilter;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kl on 2018/1/17.
 * Content :处理pdf文件
 */
@Service
public class PdfFilePreviewImpl implements FilePreview {

    private final FileHandlerService fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;
    private static final String FILE_DIR = ConfigConstants.getFileDir();

    public PdfFilePreviewImpl(FileHandlerService fileHandlerService, OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.otherFilePreview = otherFilePreview;
    }
    @Value("${pdfpagee:0}")
    private String pdfpagee;
    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        String gengxin=fileAttribute.getgengxin();
        String fileName = fileAttribute.getName();
        String regEx = "[`#%:;\\[\\]\"\\\\]";
        String fileNamee = Pattern.compile(regEx).matcher(fileName).replaceAll("").trim();
        String officePreviewType = fileAttribute.getOfficePreviewType();
        String baseUrl = BaseUrlFilter.getBaseUrl();
        String pdfName = fileNamee.substring(0, fileNamee.lastIndexOf(".") + 1) + "pdf";
        String outFilePath = FILE_DIR + pdfName;
        String  host = FileHandlerService.hqurl(url);
        boolean bendi = FileHandlerService.kuayu(host, baseUrl); //判断是否是本地URL 是本地的启用分页功能 不是就直接在跨域输出
        if (OfficeFilePreviewImpl.OFFICE_PREVIEW_TYPE_IMAGE.equals(officePreviewType) || OfficeFilePreviewImpl.OFFICE_PREVIEW_TYPE_ALL_IMAGES.equals(officePreviewType)) {
            boolean pdfgx ;
            if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) { //去缓存更新
                pdfgx= true;
            }else {
                pdfgx= false;
            }
            //当文件不存在时，就去下载
            if (pdfgx ||!fileHandlerService.listConvertedFiles().containsKey(pdfName) || !ConfigConstants.isCacheEnabled()) {
                if(!bendi){
                    ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, fileName);
                if (response.isFailure()) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
                }
                    outFilePath = response.getContent();
                }else {
                    outFilePath = FILE_DIR +url.replace(baseUrl, "");  //本地URL 不下载去掉ULR 组合成本地路径

                }
                File file = new File(outFilePath);   //判断文件是否存在
                if(!file.exists() || file.length() == 0) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                }
                String geshi =FileHandlerService.geshi(outFilePath,1);// 获取文件头信息
                if (geshi.equals(".pdf")){

                }else if (geshi.equals(".ofd")){
                    model.addAttribute("pdfUrl",FileHandlerService.zhuanyii(url));
                    return OFD_FILE_PREVIEW_PAGE;
                }else {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                }
                if (ConfigConstants.isCacheEnabled()) {
                    // 加入缓存
                    fileHandlerService.addConvertedFile(pdfName, fileHandlerService.getRelativePath(outFilePath));
                }
            }
            if(!bendi){

            }else {
                outFilePath = FILE_DIR +url.replace(baseUrl, "");  //本地URL 不下载去掉ULR 组合成本地路径
            }

            List<String> imageUrls = fileHandlerService.pdf2jpg(outFilePath, pdfName, baseUrl,fileAttribute);
            if (imageUrls == null || imageUrls.size() < 1) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, "pdf转图片异常，请联系管理员");
            }
            model.addAttribute("imgurls", imageUrls);
            model.addAttribute("currentUrl", imageUrls.get(0));
            if (OfficeFilePreviewImpl.OFFICE_PREVIEW_TYPE_IMAGE.equals(officePreviewType)) {
                return OFFICE_PICTURE_FILE_PREVIEW_PAGE;
            } else {
                return PICTURE_FILE_PREVIEW_PAGE;
            }
        } else {

            // 不是http开头，浏览器不能直接访问，需下载到本地
            if (url != null && !url.toLowerCase().startsWith("http")) {
                if (!fileHandlerService.listConvertedFiles().containsKey(pdfName) || !ConfigConstants.isCacheEnabled()) {
                    ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, pdfName);
                    if (response.isFailure()) {
                        return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
                    }
                    outFilePath = response.getContent();
                    String geshi =FileHandlerService.geshi(outFilePath,1);// 获取文件头信息
                    if (geshi.equals(".pdf")){

                    }else if (geshi.equals(".ofd")){
                        model.addAttribute("pdfUrl",FileHandlerService.zhuanyii(url));
                        return OFD_FILE_PREVIEW_PAGE;
                    }else {
                        return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                    }

                    model.addAttribute("pdfUrl", fileHandlerService.getRelativePath(response.getContent()));
                    if (ConfigConstants.isCacheEnabled()) {
                        // 加入缓存
                        fileHandlerService.addConvertedFile(pdfName, fileHandlerService.getRelativePath(outFilePath));
                    }
                } else {
                    pdfName= FileHandlerService.zhuanyii(pdfName);
                    if( ConfigConstants.getpdffy().equalsIgnoreCase("false")){

                    }else {
                        pdfName= FileHandlerService.zhuanyii(pdfName); //文件名转义
                        pdfName ="download?urlPath="+pdfName+"&"+FileHandlerService.pdfpage(pdfName);
                        model.addAttribute("pdfUrl",pdfName);
                        return FYPDF_FILE_PREVIEW_PAGE;
                    }

                    model.addAttribute("pdfUrl", pdfName);
                }
            } else {
                if(fileHandlerService.listConvertedFiles().containsKey(pdfName)){
                    String  fileTree =fileHandlerService.getConvertedFile(pdfName);
                    model.addAttribute("pdfUrl",FileHandlerService.zhuanyii(fileTree));
                    return PDF_FILE_PREVIEW_PAGE;
                }
                if( ConfigConstants.getpdffy().equalsIgnoreCase("false")){  //查询是否开启分页模式
                    if(!bendi){  //不是本地直接输出
                        model.addAttribute("pdfUrl",url);
                        return PDF_FILE_PREVIEW_PAGE;
                    }else { //是本地文件
					outFilePath = FILE_DIR +url.replace(baseUrl, "");
                        File file = new File(outFilePath);   //判断文件是否存在
                        if(!file.exists() || file.length() == 0) {
                            outFilePath = FILE_DIR +"demo/" + pdfName;
                        }
                        File filee = new File(outFilePath);   //判断文件是否存在
                        if(!filee.exists() || filee.length() == 0) {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                        }
                        String geshi =FileHandlerService.geshi(outFilePath,1);// 获取文件头信息
                        if (geshi.equals(".pdf")){
                            model.addAttribute("pdfUrl",  FileHandlerService.zhuanyii(url));
                            return PDF_FILE_PREVIEW_PAGE;
                        }else if (geshi.equals(".ofd")){
                            model.addAttribute("pdfUrl",  FileHandlerService.zhuanyii(url));
                            return OFD_FILE_PREVIEW_PAGE;
                        }else {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                        }
                    }

                }else {  //开启分页模式
                    if(!bendi){
                        model.addAttribute("pdfUrl",url);
                        return PDF_FILE_PREVIEW_PAGE;
                    }else {
                        outFilePath = FILE_DIR +url.replace(baseUrl, "");
                        File filee = new File(outFilePath);   //判断文件是否存在
                        if(!filee.exists() || filee.length() == 0) {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                        }
                        String geshi =FileHandlerService.geshi(outFilePath,1);// 获取文件头信息
                        if (geshi.equals(".pdf")){
                            pdfName = url.replace(baseUrl, "");
                            model.addAttribute("fenye",FileHandlerService.pdfpage(pdfName));
                            model.addAttribute("pdfpagee",pdfpagee);
                            pdfName ="download?urlPath="+pdfName;
                            model.addAttribute("pdfUrl",pdfName);
                            return FYPDF_FILE_PREVIEW_PAGE;
                        }else if (geshi.equals(".ofd")){
                            model.addAttribute("pdfUrl", FileHandlerService.zhuanyii(url));
                            return OFD_FILE_PREVIEW_PAGE;
                        }else {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                        }
                    }
                }
            }

            return PDF_FILE_PREVIEW_PAGE;
        }
    }
}
