package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.CompressFileReader;
import cn.keking.service.FileHandlerService;
import cn.keking.service.FilePreview;
import cn.keking.utils.DownloadUtils;
import cn.keking.utils.KkFileUtils;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kl on 2018/1/17.
 * Content :处理压缩包文件
 */
@Service
public class CompressFilePreviewImpl implements FilePreview {

    private final FileHandlerService fileHandlerService;
    private final CompressFileReader compressFileReader;
    private final OtherFilePreviewImpl otherFilePreview;
    private static final String FILE_DIR = ConfigConstants.getFileDir();

    public CompressFilePreviewImpl(FileHandlerService fileHandlerService, CompressFileReader compressFileReader, OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.compressFileReader = compressFileReader;
        this.otherFilePreview = otherFilePreview;
    }
    @Value("${file.ysb:ashuobao789}")
    private String ysb;

    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        String fileName=fileAttribute.getName();
        String regEx = "[`#%:\\[\\];.\"\\\\]";
        String fileNamee = Pattern.compile(regEx).matcher(fileName).replaceAll("").trim();
        String filePassword = fileAttribute.getFilePassword();
        String fileTree;
        String gengxin=fileAttribute.getgengxin();
        boolean pdfgx ;
        if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) { //去缓存更新
            pdfgx= true;
        }else {
            pdfgx= false;
        }
        // 判断文件名是否存在(redis缓存读取)
        if (pdfgx||!fileHandlerService.listConvertedFiles().containsKey(fileName) || !ConfigConstants.isCacheEnabled()) {
            ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, fileName);
            if (response.isFailure()) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
            }
            String filePath = response.getContent();
            File file = new File(filePath);   //判断文件是否存在
            if(!file.exists() || file.length() == 0) {
                return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
            }
            KkFileUtils.deleteDirectory(FILE_DIR+ysb+"/"+fileNamee);
            fileTree = compressFileReader.un7z(filePath, FILE_DIR+ysb+"/"+fileNamee,filePassword);
            if ("null".equals(fileTree)){
                return otherFilePreview.notSupportedFile(model, fileAttribute, "压缩文件类型不受支持");
            }
            KkFileUtils.deleteDirectory(FILE_DIR+ysb+"/"+fileNamee + "/__MACOSX");
            KkFileUtils.deleteFileByPath(FILE_DIR+ysb+"/"+fileNamee + "/.DS_Store");
            if ( ConfigConstants.isCacheEnabled()) {
                fileHandlerService.addConvertedFile(fileName, fileTree);  //加入缓存
            }
            List<String> fileNames = new ArrayList<>();
            File filee = new File(FILE_DIR+ysb+"/"+fileNamee);
            findFileList(filee,fileNames);
            model.addAttribute("fileTree", fileNames);
            return COMPRESS_FILE_PREVIEW_PAGE;

        }else {
            fileTree = fileHandlerService.getConvertedFile(fileName);
            if (fileTree != null && !"null".equals(fileTree)) {
                List<String> fileNames = new ArrayList<>();
                File file = new File(FILE_DIR+ysb+"/"+fileNamee);
                findFileList(file,fileNames);
                model.addAttribute("fileTree", fileNames);
                return COMPRESS_FILE_PREVIEW_PAGE;
            } else {
                return otherFilePreview.notSupportedFile(model, fileAttribute, "压缩文件类型不受支持,或者没加入KK识别");
            }
        }
    }
    public  void findFileList(File dir, List<String> fileNames) {
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            return;
        }
        String newStr = String.valueOf(dir);
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                newStr = newStr.replace(FILE_DIR, "");
                newStr = newStr.replace("\\", "/");
                fileNames.add(newStr + "/" + file.getName());// 添加文件全路径名
            } else {// 如果是目录
                findFileList(file, fileNames);// 回调自身继续查询
            }
        }
    }
}