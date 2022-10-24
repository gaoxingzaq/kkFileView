package cn.keking.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ConvertPicUtil {
    /**
     * 将Jpg图片转换为Pdf文件
     * @param strJpgFile 输入的jpg的路径和文件名
     * @param strPdfFile 输出的pdf的路径和文件名
     * @return
     */
    private static final int FIT_WIDTH = 500;
    private static final int FIT_HEIGHT = 900;
    public static boolean convertJpg2Pdf(String strJpgFile, String strPdfFile) {
        Document document= null;
        RandomAccessFileOrArray rafa = null;
        try {
            document = new Document();
            PdfWriter.getInstance(document,  new FileOutputStream(strPdfFile));
            document.open();
            rafa = new RandomAccessFileOrArray(new FileChannelRandomAccessSource(new RandomAccessFile(strJpgFile, "r").getChannel()));
            int pages = TiffImage.getNumberOfPages(rafa);
            Image image;
            for (int i = 1; i <= pages; i++) {
                try {
                    image = TiffImage.getTiffImage(rafa, i);
                    image.scaleToFit(FIT_WIDTH, FIT_HEIGHT);
                    document.add(image);
                } catch (Exception e) {
                    document.close();
                    rafa.close();
                    e.printStackTrace();
                    return false;
                }
            }
            document.close();
            rafa.close();
            return true;
        }
        catch (Exception e)
        {
            KkFileUtils.deleteFileByPath(strPdfFile);
            System.out.println("错误:"+ e.getMessage());
        }
        finally {
            document.close();
            try {
                rafa.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

