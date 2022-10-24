package cn.keking.service;

import fr.opensagres.poi.xwpf.converter.core.BasicURIResolver;
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * POIExcelToHtml 文件转换：
 *
 * @author generator
 * @version 1.0-SNAPSHORT 2021-08-09 18:06
 * 类描述
 * 修订历史：
 * 日期：2021-08-09
 * 作者：shadow
 * 参考：https://blog.csdn.net/u013585096/article/details/85104888
 * 描述：Execl转HTML
 * @Copyright 湖南视拓信息技术股份有限公司. All rights reserved.
 * @see null
 */
public class POIWordToHtml {
    private static final String ENCODING = "GB2312";// UTF-8
    public static boolean wordToHtml(String sourcePath, String picturesPath, String imagessslj, String targetPath)  {
        String htmlData = "预览失败";
        String ext = FileUtils.GetFileExt(sourcePath);
        File picturesDir = new File(picturesPath);

        try {
            InputStream inputStream = new FileInputStream(sourcePath);
            if ("docx".equalsIgnoreCase(ext)) {
                XWPFDocument docxDocument;
                try {
                    docxDocument = new XWPFDocument(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!picturesDir.isDirectory()) {
                    picturesDir.mkdirs();
                }
                XHTMLOptions options = XHTMLOptions.create();
                options.setIgnoreStylesIfUnused(false);
                options.setExtractor(new FileImageExtractor(picturesDir));
                options.URIResolver(new BasicURIResolver(imagessslj));
                ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
                XHTMLConverter.getInstance().convert(docxDocument, htmlStream, options);
                htmlData = htmlStream.toString();
                docxDocument.close();
                htmlStream.close();
                FileUtils.writeFile(htmlData, targetPath);
                return true;
            } else  {
                HWPFDocument wordDocument;
                try {
                    wordDocument = new HWPFDocument(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!picturesDir.isDirectory()) {
                    picturesDir.mkdirs();
                }
                WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                wordToHtmlConverter.setPicturesManager((content1, pictureType, suggestedName, widthInches, heightInches) -> {
                    File file = new File(picturesPath + "/" + suggestedName);
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(file);
                        fos.write(content1);
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return imagessslj + "/" + suggestedName;
                });

            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            htmlData =  outStream.toString();
                int index = htmlData.lastIndexOf(" TOC \\");
                if (index != -1){
                    System.out.println("index============"+index);
                    String str = htmlData.substring(0,index);
                    int i = str.lastIndexOf("<span");
                    String substring = str.substring(i, index);
                    int i1 = htmlData.indexOf("</span", index);
                    String substring1 = htmlData.substring(i, i1+7);
                    htmlData = htmlData.replace(substring1,"");
                }
                wordDocument.close();
                outStream.close();
            FileUtils.writeFile(htmlData, targetPath);
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
