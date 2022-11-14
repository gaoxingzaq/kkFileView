package cn.keking.web.controller;

import cn.keking.config.ConfigConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.keking.model.ReturnResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author yudian-it
 * @date 2017/12/1
 */
@RestController
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final String fileDir = ConfigConstants.getFileDir();
    private final String demoDir = "demo";
    private final String demoPath = demoDir + File.separator;

    @PostMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws JsonProcessingException {
        if (ConfigConstants.getFileUploadDisable()) {
            return new ObjectMapper().writeValueAsString(ReturnResponse.failure("文件传接口已禁用"));
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        //判断是否为IE浏览器的文件名，IE浏览器下文件名会带有盘符信息

        // escaping dangerous characters to prevent XSS
        fileName = HtmlUtils.htmlEscape(fileName, StandardCharsets.UTF_8.name());
        String regEx = "[`\\[\\]~@#%^&*()|{}:;\\\\<>/?！…（）—【】‘；：”“’。，、？']";
        fileName = Pattern.compile(regEx).matcher(fileName).replaceAll("").trim();

        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (Math.max(winSep, unixSep));
        if (pos != -1)  {
            fileName = fileName.substring(pos + 1);
        }
        String fileType= "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fileType= fileName.substring(i+1);
            fileType= fileType.toLowerCase();
        }
        if ( fileType == null || fileType.length() == 0 ||fileType.equalsIgnoreCase("exe") ||fileType.equalsIgnoreCase("dll") ){
            return new ObjectMapper().writeValueAsString(ReturnResponse.failure(fileType+"不允许上传的类型"));
        }
        // 判断是否存在同名文件
        if (existsFile(fileName)) {
            return new ObjectMapper().writeValueAsString(ReturnResponse.failure("存在同名文件，请先删除原有文件再次上传"));
        }
        File outFile = new File(fileDir + demoPath);
        if (!outFile.exists() && !outFile.mkdirs()) {
            logger.error("创建文件夹【{}】失败，请检查目录权限！",fileDir + demoPath);
        }
        logger.info("上传文件：{}", fileDir + demoPath + fileName);
        try(InputStream in = file.getInputStream()) {
            if(in.available() <= 0){
                return new ObjectMapper().writeValueAsString(ReturnResponse.failure("文件错误小于等于0KB"));
            }else {
                OutputStream out = new FileOutputStream(fileDir + demoPath + fileName);
                StreamUtils.copy(in, out);
                in.close();
                out.close();
                return new ObjectMapper().writeValueAsString(ReturnResponse.success(null));
            }

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return new ObjectMapper().writeValueAsString(ReturnResponse.failure());
        }
    }

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * 路径遍历 漏洞修复
     * @param str
     * @return
     */
    public static String filenameFilter(String str) {
        return str==null?null:FilePattern.matcher(str).replaceAll("");
    }

    @GetMapping("/deleteFile")
    public String deleteFile(String fileName, HttpServletRequest request) throws JsonProcessingException {
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
        if (fileName == null || fileName.length()<= 0){
            logger.info("url异常：{}，IP：{}", fileName,ip);
            return new ObjectMapper().writeValueAsString("地址非法，删除失败！");
        }
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        fileName= filenameFilter(fileName);
        File file = new File(fileDir + demoPath + fileName);
        logger.info("删除文件：{}，IP：{}", file.getAbsolutePath(),ip);
        if (file.exists() && !file.delete()) {
           logger.error("删除文件【{}】失败，请检查目录权限！，IP：{}",file.getPath(),ip);
        }
        return new ObjectMapper().writeValueAsString(ReturnResponse.success());
    }

    /**
     * 时间排序方法
     */
    public static List<File> getFileSort(String path) {
        List<File> list = getFiles(path, new ArrayList<>());
        if (list != null && list.size() > 0) {
            Collections.sort(list, (file, newFile) -> {
                if (file.lastModified() < newFile.lastModified()) {
                    return 1;
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    return -1;
                }
            });
        }
        return list;
    }
    public static List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }
    @GetMapping("/listFiles")
    public String getFiles() throws JsonProcessingException {
        List<Map<String, String>> list = new ArrayList<>();
        List<File> listl = getFileSort(fileDir + demoPath);
        for (File file : listl) {
            Map<String, String> fileName = new HashMap<>();
            fileName.put("fileName", demoDir + "/" + file.getName());
            list.add(fileName);
        }
        return new ObjectMapper().writeValueAsString(list);
    }

    private boolean existsFile(String fileName) {
        File file = new File(fileDir + demoPath + fileName);
        return file.exists();
    }
}
