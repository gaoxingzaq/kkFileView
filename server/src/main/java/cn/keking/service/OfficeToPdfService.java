package cn.keking.service;
import cn.keking.model.FileAttribute;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author yudian-it
 */
@Component
public class OfficeToPdfService {

    private final static Logger logger = LoggerFactory.getLogger(OfficeToPdfService.class);
    private final OfficePluginManager officePluginManager;

    public OfficeToPdfService(OfficePluginManager officePluginManager) {
        this.officePluginManager = officePluginManager;
    }

    public void openOfficeToPDF(String inputFilePath, String outputFilePath, FileAttribute fileAttribute) {
        office2pdf(inputFilePath, outputFilePath, fileAttribute);
    }


    public static void converterFile(File inputFile, String outputFilePath_end, OfficeDocumentConverter converter, FileAttribute fileAttribute) {
        File outputFile = new File(outputFilePath_end);
        // 假如目标路径不存在,则新建该路径
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            logger.error("创建目录【{}】失败，请检查目录权限！",outputFilePath_end);
        }
        converter.convert(inputFile, outputFile, fileAttribute.toFileProperties());
    }


    public void office2pdf(String inputFilePath, String outputFilePath, FileAttribute fileAttribute) {
        OfficeDocumentConverter converter = officePluginManager.getDocumentConverter();
        File inputFile = new File(inputFilePath);
        File inputFilef = new File(outputFilePath);
        FileHandlerService.AT_CONVERT_MAP.put(inputFilef.getName(), 1);  //添加转换记录
        if (null != inputFilePath) {
            // 判断目标文件路径是否为空
            if (null == outputFilePath) {
                // 转换后的文件路径
                String outputFilePath_end = getOutputFilePath(inputFilePath);
                FileHandlerService.AT_CONVERT_MAP.remove(inputFilef.getName(), 1);  //转换成功删除转换记录
                if (inputFile.exists()) {
                    // 找不到源文件, 则返回
                    converterFile(inputFile, outputFilePath_end, converter, fileAttribute);
                }
            } else {
                if (inputFile.exists()) {
                    // 找不到源文件, 则返回
                    converterFile(inputFile, outputFilePath, converter, fileAttribute);
                    FileHandlerService.AT_CONVERT_MAP.remove(inputFilef.getName(), 1);  //转换成功删除转换记录
                }
            }
        }
    }

    public static String getOutputFilePath(String inputFilePath) {
        return inputFilePath.replaceAll("."+ getPostfix(inputFilePath), ".pdf");
    }

    public static String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }

}
