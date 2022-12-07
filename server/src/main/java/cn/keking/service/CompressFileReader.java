package cn.keking.service;

import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yudian-it create 2017/11/27
 */
@Component
public class CompressFileReader {
    public static final String[] encodes = new String[] { "UTF-8", "GBK", "GB2312", "ISO-8859-1", "ISO-8859-2" };
    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
    public static String getEncode(String str) {
        byte[] data = str.getBytes();
        byte[] b = null;
        a:for (int i = 0; i < encodes.length; i++) {
            try {
                b = str.getBytes(encodes[i]);
                if (b.length!=data.length)
                    continue;
                for (int j = 0; j < b.length; j++) {
                    if (b[j] != data[j]) {
                        continue a;
                    }
                }
                return encodes[i];
            } catch (UnsupportedEncodingException e) {
                continue;
            }
        }
        return null;
    }
    public String getUtf8String(String str) {
        if (str != null && str.length() > 0) {
            String needEncodeCode = "ISO-8859-1";
            String neeEncodeCode = "ISO-8859-2";
            String gbkEncodeCode = "GBK";
            //  System.out.println(getEncode(str));
            try {
                if (Charset.forName(needEncodeCode).newEncoder().canEncode(str)) {//这个方法是关键，可以判断乱码字符串是否为指定的编码
                    //      System.out.println(1);
                    str = new String(str.getBytes(needEncodeCode), "UTF-8");
                }
                if (Charset.forName(neeEncodeCode).newEncoder().canEncode(str)) {//这个方法是关键，可以判断乱码字符串是否为指定的编码
                    //          System.out.println(2);
                    str = new String(str.getBytes(neeEncodeCode), "UTF-8");
                }
                if (Charset.forName(gbkEncodeCode).newEncoder().canEncode(str)) {//这个方法是关键，可以判断乱码字符串是否为指定的编码
                    //          System.out.println(3);
                    str = new String(getUTF8BytesFromGBKString(str), "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }
    /**
     * 判断是否是中日韩文字
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    public static boolean judge(char c){
        if((c >='0' && c<='9')||(c >='a' && c<='z' ||  c >='A' && c<='Z')){
            return true;
        }
        return false;
    }
    public static boolean isMessyCode(String strName) {
        //去除字符串中的空格 制表符 换行 回车
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        //去除字符串中的标点符号
        String temp = after.replaceAll("\\p{P}", "");
        //处理之后转换成字符数组
        char[] ch = temp.trim().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            //判断是否是数字或者英文字符
            if (!judge(c)) {
                //判断是否是中日韩文
                if (!isChinese(c)) {
                    //如果不是数字或者英文字符也不是中日韩文则表示是乱码返回true
                    return true;
                }
            }
        }
        //表示不是乱码 返回false
        return false;
    }
    public String un7z(String file7zPath, final String outPutPath, String passWord){
        RandomAccessFile randomAccessFile = null;
        IInArchive archive = null;
        String panduan = outPutPath;
        try {
            randomAccessFile = new RandomAccessFile(file7zPath, "r");
            archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), passWord);
            ISimpleInArchive   simpleInArchive = archive.getSimpleInterface();
            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                final int[] hash = new int[] { 0 };
                if (!item.isFolder()) {
                    ExtractOperationResult result;
                    final long[] sizeArray = new long[1];
                    result = item.extractSlow(data -> {
                        try {
                            String str = getUtf8String(item.getPath());
                            if (isMessyCode(str)){
                                str = new String(item.getPath().getBytes("ISO-8859-1"), "gbk");
                            }
                            str = str.replace("\\", File.separator); //Linux 下路径错误
                            String  str1 = str.substring(0, str.lastIndexOf(File.separator)+ 1);
                            File file = new File(outPutPath + File.separator + str1);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            OutputStream out = new FileOutputStream( outPutPath + File.separator + str, true);
                            IOUtils.write(data, out);
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hash[0] ^= Arrays.hashCode(data);
                        sizeArray[0] += data.length;
                        return data.length;
                    }, passWord);
                    if (result == ExtractOperationResult.OK) {
                        // System.out.println("解压成功...." + String.format("%9X | %10s | %s", hash[0], sizeArray[0], getUtf8String(item.getPath())));
                    } else {
                        String Str1 = String.valueOf(result);
                        if (Str1.equalsIgnoreCase("WRONG_PASSWORD")) {
                            panduan ="Password";
                        }else {
                            System.out.println("解压失败：密码错误或者其他错误...." + result);
                            panduan ="null";
                        }

                    }
                }
            }
        } catch (FileNotFoundException e) {
            panduan ="null";
            e.printStackTrace();
        } catch (SevenZipException e) {
            if (e.getMessage().contains("Password") ) {
                panduan ="Password";
            }else {
                e.printStackTrace();
            }
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e);
                }
            }
            if (archive != null) {
                try {
                    archive.close();
                } catch (SevenZipException e) {
                    System.err.println("Error closing archive: " + e);
                }
            }
        }
        return panduan;
    }
}