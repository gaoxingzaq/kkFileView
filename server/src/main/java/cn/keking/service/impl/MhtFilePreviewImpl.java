package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileHandlerService;
import cn.keking.service.FilePreview;
import cn.keking.utils.DownloadUtils;
import cn.keking.utils.KkFileUtils;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author chenjh
 * @since 2019/11/21 14:28
 */
@Service
public class MhtFilePreviewImpl implements FilePreview {


    private static final String FILE_DIR = ConfigConstants.getFileDir();

    private final FileHandlerService fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;

    public MhtFilePreviewImpl(FileHandlerService fileHandlerService, OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.otherFilePreview = otherFilePreview;
    }
    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        // 预览Type，参数传了就取参数的，没传取系统默认
        String gengxin=fileAttribute.getgengxin();
        String fileName = fileAttribute.getName();
        String regEx = "[`#%:;\\[\\].\"\\\\]";
        String fileNamee = Pattern.compile(regEx).matcher(fileName).replaceAll("").trim();
        String imagesss = FILE_DIR + fileName;
        String pdfName =  fileNamee + "." + "html";
        String outFilePath = FILE_DIR + pdfName;
        boolean pdfgx ;
        if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) { //去缓存更新
            pdfgx= true;
        }else {
            pdfgx= false;
        }
        // 判断之前是否已转换过，如果转换过，直接返回，否则执行转换
        if (pdfgx ||!fileHandlerService.listConvertedFiles().containsKey(pdfName) || !ConfigConstants.isCacheEnabled()) {
            String filePath;
            ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, null);
            if (response.isFailure()) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
            }
            filePath = response.getContent();
            if (StringUtils.hasText(outFilePath)) {
                boolean convertResult = Mht2HtmlUtil.mht2html(filePath, outFilePath, imagesss);
                if( ConfigConstants.getofficedel().equalsIgnoreCase("false")){  //是否保留OFFICE源文件
                    KkFileUtils.deleteFileByPath(filePath);
                }
                if (!convertResult) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "Mht文件转换异常，请联系管理员");
                }
                if (ConfigConstants.isCacheEnabled()) {
                    // 加入缓存
                    fileHandlerService.addConvertedFile(pdfName, fileHandlerService.getRelativePath(outFilePath));
                }
            }
        }

        model.addAttribute("pdfUrl", pdfName);
        return EXEL_FILE_PREVIEW_PAGE;
    }


}
