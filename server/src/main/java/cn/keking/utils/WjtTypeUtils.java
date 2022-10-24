package cn.keking.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class WjtTypeUtils {
    /**
     常用文件的文件头如下：(以前八位为准)
     */
    public static final String TYPE_JPG = ".jpg";
    public static final String TYPE_PNG = ".png";
    public static final String TYPE_GIF = ".gif";
    public static final String TYPE_BMP = ".bmp";
    public static final String TYPE_WEBP = ".webp";
    public static final String TYPE_TIF = ".tif";
    public static final String TYPE_TIFF = ".tiff";
    public static final String TYPE_CAD = ".cad";
    public static final String TYPE_DXF = ".cad";
    public static final String TYPE_DWF = ".cad";
    public static final String TYPE_XML= ".xml";
    public static final String TYPE_PDF = ".pdf";
    public static final String TYPE_OFD = ".ofd";
    public static final String TYPE_zip = ".zip";
    public static final String TYPE_RAR = ".rar";
    public static final String TYPE_JAR = ".jar";
  //  public static final String TYPE_7ZIP = ".7-zip";
    public static final String TYPE_TAR = ".tar";
 //   public static final String TYPE_GZIP = ".gzip";
  //  public static final String TYPE_7Z = ".7z";
    public static final String TYPE_2003office = ".2003office";
    public static final String TYPE_2010offcie= ".2010offcie";
    public static final String TYPE_xmln = ".xmln";
 //   public static final String TYPE_DOC = ".doc";
  //  public static final String TYPE_XLS = ".xls";
   // public static final String TYPE_DOCX = ".docx";
   // public static final String TYPE_XLSX = ".xls";
  //  public static final String TYPE_WPS = ".wps";
 //   public static final String TYPE_DOCM = ".docm";
    public static final String TYPE_CSV = ".csv";
    public static final String TYPE_QT = ".QT";
 //   public static final String TYPE_VSD = ".vsd";
    public static final String RTF = ".rtf";

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getPicType(InputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[5];
        try {
            fis.read(b, 0, b.length);
            String type = Objects.requireNonNull(bytesToHexString(b)).toUpperCase();
          //  System.out.print(type);
            if (type.contains("FFD8FF")) {
                return TYPE_JPG;
            }else if(type.contains("89504E47")) {
                return TYPE_PNG;
            }else if(type.contains("47494638")) {
                return TYPE_GIF;
            }else if(type.contains("424D3600")) {
                return TYPE_BMP;
            }else if(type.contains("52494646")){
                return TYPE_WEBP;
            } else if(type.contains("49492A00")){
                return TYPE_TIF;
            }else if(type.contains("4D4D002A")){
                return TYPE_TIFF;
            }else if(type.contains("41433130")){
                return TYPE_CAD;
            }else if(type.contains("2020300D")){
                return TYPE_DXF;
            }else if(type.contains("28445746")){
                return TYPE_DWF;
            }else if(type.contains("3C3F786D")){
                return TYPE_XML;
            }else if(type.contains("25504446")){
                return TYPE_PDF;
            }else if(type.contains("3C68746D")){
                return TYPE_xmln;
            }else if(type.contains("D0CF11E0")){
                return TYPE_2003office;
            }else if(type.contains("504B0304")){
                return TYPE_2010offcie;
            }else if(type.contains("D0F2BAC5")){
                return TYPE_CSV;
            }else if(type.contains("7B5C7274")){
                return RTF;
            } else{
                return "未知文件";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public static String pdftype(InputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = Objects.requireNonNull(bytesToHexString(b)).toUpperCase();
          if(type.contains("25504446")){
                return TYPE_PDF;
            }else if(type.contains("504B0304")){
                return TYPE_OFD;
            } else{
                return "未知类型";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}
