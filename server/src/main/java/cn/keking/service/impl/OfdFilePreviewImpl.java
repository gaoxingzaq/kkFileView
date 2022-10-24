package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileHandlerService;
import cn.keking.service.FilePreview;
import cn.keking.utils.DownloadUtils;
import cn.keking.utils.KkFileUtils;
import cn.keking.web.filter.BaseUrlFilter;
import jodd.util.StringUtil;
import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.SVGMaker;
import org.ofdrw.reader.OFDReader;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Content :处理OFD文件
 */
@Service
public class OfdFilePreviewImpl implements FilePreview {
    private final FileHandlerService fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;
    private static final String FILE_DIR = ConfigConstants.getFileDir();
    public OfdFilePreviewImpl(FileHandlerService fileHandlerService, OtherFilePreviewImpl otherFilePreview) {
        this.fileHandlerService = fileHandlerService;
        this.otherFilePreview = otherFilePreview;
    }
    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        String fileName = fileAttribute.getName();
        String baseUrl = BaseUrlFilter.getBaseUrl();
        String pdfName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + "ofd";
        String outFilePath = FILE_DIR + pdfName;
        String gengxin=fileAttribute.getgengxin();
        String regEx = "[`#%:;\\[\\].\"\\\\]";
        String fileNamee = Pattern.compile(regEx).matcher(fileName).replaceAll("").trim();
        String ofdName =  fileNamee + "." + "pdf";
        String ofdFilePath = FILE_DIR + ofdName;
        String svgName =  fileNamee  + "svg";
        String svgFilePath = FILE_DIR + svgName;
        boolean pdfgx ;
        if(StringUtil.isNotBlank(gengxin) && "ok".equalsIgnoreCase(gengxin)) { //去缓存更新
            pdfgx= true;
        }else {
            pdfgx= false;
        }
        if(ConfigConstants.getOfdPreviewType().equalsIgnoreCase("pdf")){
            if (pdfgx ||!fileHandlerService.listConvertedFiles().containsKey(ofdName) || !ConfigConstants.isCacheEnabled()) {
                String filePath;
                ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, null);
                if (response.isFailure()) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
                }
                filePath = response.getContent();
                File filee = new File(filePath);   //判断文件是否存在
                if(!filee.exists() || filee.length() == 0) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                }
                String geshi =FileHandlerService.geshi(filePath,1);// 获取文件头信息
                if (geshi.equals(".ofd")){
                    if (StringUtils.hasText(filePath)) {
                        boolean convertResult = convertPdf(filePath,ofdFilePath);
                        if( ConfigConstants.getofficedel().equalsIgnoreCase("false")){  //是否保留OFFICE源文件
                            KkFileUtils.deleteFileByPath(filePath);
                        }
                        if (!convertResult) {
                            return otherFilePreview.notSupportedFile(model, fileAttribute, "OFD文件转换异常，请联系管理员");
                        }
                        if (ConfigConstants.isCacheEnabled()) {
                            // 加入缓存
                            fileHandlerService.addConvertedFile(ofdName, fileHandlerService.getRelativePath(ofdFilePath));
                        }
                    }
                }else if (geshi.equals(".pdf")){
                    model.addAttribute("pdfUrl",url);
                    return PDF_FILE_PREVIEW_PAGE;
                }else {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                }
            }
            model.addAttribute("pdfUrl", ofdName);
            return PDF_FILE_PREVIEW_PAGE;
        } else if(ConfigConstants.getOfdPreviewType().equalsIgnoreCase("svg")){
            if (pdfgx ||!fileHandlerService.listConvertedFiles().containsKey(svgName) || !ConfigConstants.isCacheEnabled()) {
                String filePath;
                ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, pdfName);
                if (response.isFailure()) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
                }
                filePath = response.getContent();
                File filee = new File(filePath);   //判断文件是否存在
                if(!filee.exists() || filee.length() == 0) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                }
                String geshi =FileHandlerService.geshi(filePath,1);// 获取文件头信息
                if (geshi.equals(".ofd")){
                    if (StringUtils.hasText(filePath)) {
                        try {
                            boolean convertResult = toSVG(filePath,svgFilePath);
                            if (!convertResult) {
                                return otherFilePreview.notSupportedFile(model, fileAttribute, "OFD文件转换异常，请联系管理员");
                            }
                            if (ConfigConstants.isCacheEnabled()) {
                                // 加入缓存
                                fileHandlerService.addConvertedFile(svgName, fileHandlerService.getRelativePath(svgFilePath));
                            }
                            if( ConfigConstants.getofficedel().equalsIgnoreCase("false")){  //是否保留OFFICE源文件
                                KkFileUtils.deleteFileByPath(filePath);
                            }
                            List<String> fileNames = new ArrayList<>();
                            File fileee = new File(svgFilePath);
                            findFileList(fileee,fileNames);
                            model.addAttribute("fileTree", fileNames);
                            return Ofdsvg_FILE_PAGE;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else if (geshi.equals(".pdf")){
                    model.addAttribute("pdfUrl",FileHandlerService.zhuanyii(url));
                    return PDF_FILE_PREVIEW_PAGE;
                }else {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                }
            }
            List<String> fileNames = new ArrayList<>();
            File filee = new File(svgFilePath);
            findFileList(filee,fileNames);
            model.addAttribute("fileTree", fileNames);
            return Ofdsvg_FILE_PAGE;
        }else {
            String  host = FileHandlerService.hqurl(url);
            boolean bendi = FileHandlerService.kuayu(host, baseUrl); //判断是否是本地URL 是本地的启用分页功能 不是就直接在跨域输出
            if(!bendi){
                model.addAttribute("pdfUrl",url);
                return OFD_FILE_PREVIEW_PAGE;
            }else {
                outFilePath = FILE_DIR +url.replace(baseUrl, "");  //本地URL 不下载去掉ULR 组合成本地路径
                File filee = new File(outFilePath);   //判断文件是否存在
                if(!filee.exists() || filee.length() == 0) {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件不存在");
                }
                String geshi =FileHandlerService.geshi(outFilePath,1);// 获取文件头信息
                if (geshi.equals(".ofd")){
                    model.addAttribute("pdfUrl",FileHandlerService.zhuanyii(url));
                    return OFD_FILE_PREVIEW_PAGE;
                }else if (geshi.equals(".pdf")){
                    model.addAttribute("pdfUrl",FileHandlerService.zhuanyii(url));
                    return PDF_FILE_PREVIEW_PAGE;
                }else {
                    return otherFilePreview.notSupportedFile(model, fileAttribute, "文件错误或者其他类型,"+ geshi );
                }
            }
        }
    }
    private static boolean toSVG(String filename, String dirPath) throws IOException {
        //为不规范的字体名创建映射
        Files.createDirectories(Paths.get(dirPath));
        Path src = Paths.get(filename);
        try(OFDReader reader = new OFDReader(src)){
            SVGMaker svgMaker = new SVGMaker(reader, 5d);
            svgMaker.config.setDrawBoundary(false);
            svgMaker.config.setClip(false);
            for (int i = 0; i < svgMaker.pageSize(); i++) {
                String str=String.format("%03d",i);
                String svg = svgMaker.makePage(Integer.parseInt(str));
                Path dist = Paths.get(dirPath, str + ".svg");
                Files.write(dist, svg.getBytes());
            }
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
            newStr = newStr.replace(FILE_DIR, "");
            //  newStr = newStr.replace("\\", "/");
            fileNames.add(newStr + "/" + file.getName());// 添加文件全路径名
        }
    }

    public static boolean convertPdf(String filename, String dirPath) {
        try {
            ConvertHelper.toPdf(Paths.get(filename), Paths.get(dirPath));
            return true;
        } catch (GeneralConvertException e) {
          //  e.printStackTrace();
            return false;
        }

    }

}

