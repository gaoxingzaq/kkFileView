package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileHandlerService;
import cn.keking.service.FilePreview;
import cn.keking.utils.DownloadUtils;
import cn.keking.utils.EncodingDetects;
import jodd.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.util.HtmlUtils;

import java.io.*;

/**
 * Created by kl on 2018/1/17.
 * Content :处理文本文件
 */
@Service
public class SimTextFilePreviewImpl implements FilePreview {

    private final FileHandlerService fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;

    public SimTextFilePreviewImpl(FileHandlerService fileHandlerService,OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.otherFilePreview = otherFilePreview;
    }
    private static final String FILE_DIR = ConfigConstants.getFileDir();
    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        String fileName = fileAttribute.getName();
        String gengxin=fileAttribute.getgengxin();
        String outFilePath = FILE_DIR + fileName;
        String suffix = fileAttribute.getSuffix();
        boolean pdfgx ;
        if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) { //去缓存更新
            pdfgx= true;
        }else {
            pdfgx= false;
        }
        //  System.out.println(suffix);
        if (pdfgx ||!fileHandlerService.listConvertedFiles().containsKey(fileName) || !ConfigConstants.isCacheEnabled()) {
            ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, fileName);
            if (response.isFailure()) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
            }
            outFilePath = response.getContent();
            if (ConfigConstants.isCacheEnabled()) {
                fileHandlerService.addConvertedFile(fileName, outFilePath);  //加入缓存
            }
            try {
                File filee = new File(outFilePath);   //判断文件是否存在
                if(!filee.exists() || filee.length() == 0) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                }
                String  fileData = HtmlUtils.htmlEscape(textData(outFilePath));
                model.addAttribute("textData", Base64.encodeBase64String(fileData.getBytes()));
            } catch (IOException e) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, e.getLocalizedMessage());
            }
            return TXT_FILE_PREVIEW_PAGE;
        }
        File filee = new File(outFilePath);   //判断文件是否存在
        if(!filee.exists() || filee.length() == 0) {
            return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
        }
        String  fileData = null;
        try {
            fileData = HtmlUtils.htmlEscape(textData(outFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("textData", Base64.encodeBase64String(fileData.getBytes()));
        return TXT_FILE_PREVIEW_PAGE;
    }
    static String textData(String baseUrll) throws IOException {
        File file = new File(baseUrll);
        if(!file.exists() || file.length() == 0) {
            return "";
        }else {
            String charset = EncodingDetects.getJavaEncode(baseUrll);
            if (charset.equalsIgnoreCase("ASCII")){
                charset ="UTF-8";
            }
            System.out.println(charset);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(baseUrll), charset));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\r\n");
            }
            br.close();
            return result.toString();
        }
    }


}
