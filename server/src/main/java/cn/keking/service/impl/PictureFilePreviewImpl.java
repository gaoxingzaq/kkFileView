package cn.keking.service.impl;

import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FilePreview;
import cn.keking.utils.DownloadUtils;
import cn.keking.service.FileHandlerService;
import cn.keking.web.filter.BaseUrlFilter;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kl on 2018/1/17.
 * Content :图片文件处理
 */
@Service
public class PictureFilePreviewImpl implements FilePreview {

    private final FileHandlerService fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;

    public PictureFilePreviewImpl(FileHandlerService fileHandlerService, OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.otherFilePreview = otherFilePreview;
    }

    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        String baseUrl = BaseUrlFilter.getBaseUrl();
        String officePreviewType = fileAttribute.getOfficePreviewType();
        List<String> imgUrls = new ArrayList<>();
        String s = url;
        String[] as = s.split("\\|");
        for (int i = 0; i < as.length; i++) {
            url = as[i];
            imgUrls.add(as[i]);
            //  System.out.println(as[i]);
        }
        String fileKey = fileAttribute.getFileKey();
        List<String> zipImgUrls = fileHandlerService.getImgCache(fileKey);
        if (!CollectionUtils.isEmpty(zipImgUrls)) {
            imgUrls.addAll(zipImgUrls);
        }
        // 不是http开头，用跨域方法
        if (url != null && url.toLowerCase().startsWith("ftp")) {
            model.addAttribute("imgUrls", imgUrls);
            model.addAttribute("currentUrl", url);
            return XZLPICTURE_FILE_PREVIEW_PAGE;
        } else {
           
            if (officePreviewType.equalsIgnoreCase("imagexz")){
                model.addAttribute("imgUrls", imgUrls);
                model.addAttribute("currentUrl", url);
                return XZLPICTURE_FILE_PREVIEW_PAGE;
            }else {
                model.addAttribute("imgUrls", imgUrls);
                model.addAttribute("currentUrl", url);
            }
        }
        return PICTURE_FILE_PREVIEW_PAGE;
    }
}
