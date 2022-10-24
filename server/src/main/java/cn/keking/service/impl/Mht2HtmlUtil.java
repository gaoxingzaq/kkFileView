package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Enumeration;

public class Mht2HtmlUtil {

    /**
     * 将 mht文件转换成 html文件
     * @param srcMht      // mht 文件的位置
     * @param destHtml    // 转换后输出的HTML的位置
     * @return
     */
    private static final String FILE_DIR = ConfigConstants.getFileDir();
    public static boolean mht2html(String srcMht, String destHtml, String serverPath) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(srcMht);
            Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
            MimeMessage msg = new MimeMessage(mailSession, fis);
            Object content = msg.getContent();
            if (content instanceof Multipart) {

                MimeMultipart mp = (MimeMultipart) content;
                // 第一部分是text/html（可能有例外）
                MimeBodyPart mbp = (MimeBodyPart) mp.getBodyPart(0);
                // 获取mht文件内容代码的编码
                String strEncodng = getEncoding(mbp);
                // 获取mht文件的内容
                String strText = getHtmlText(mbp, strEncodng);
                if (strText == null)
                    return false;
                // 创建以mht文件名称的文件夹，主要用来保存资源文件。  这里不需要所以注释掉了
                File parent = null;
                if (mp.getCount() > 1) {
                    String sourcePath = destHtml.substring(0, destHtml.lastIndexOf("."));
                    parent = new File(new File(sourcePath).getAbsolutePath()+ "mht");
                    parent.mkdirs();
                    if (!parent.exists()) { // 创建文件夹失败的话则退出
                        return false;
                    }
                }

                //FOR中代码 主要是保存资源文件及替换路径   第二部分开始为资源文件
                for (int i = 1; i < mp.getCount(); ++i) {
                    MimeBodyPart bp = (MimeBodyPart) mp.getBodyPart(i);
                    // 获取资源文件的路径
                    // 例（获取： http://xxx.com/abc.jpg）
                    String strUrl = getResourcesUrl(bp);
                    if (strUrl == null || strUrl.length() == 0)
                        continue;
                    // 获取资源文件的绝对路径
                    String FilePath = parent.getAbsolutePath() + File.separator + getName(strUrl, i);
                    File resources = new File(FilePath);

                    // 保存资源文件
                    if (saveResourcesFile(resources, bp.getInputStream())) {
                        // 将远程地址替换为本地地址 如图片、JS、CSS样式等等
                        String replacePath = strUrl;
                        if(strUrl.startsWith("file://")){
                            replacePath = getRelativePath(strUrl);
                        }
                        String relativePath = getRelativePath(FilePath);
                        strText = strText.replace(replacePath, relativePath);
                    }
                }

                // 最后保存HTML文件
               strText = strText.replace(FILE_DIR,"");
                saveHtml(strText, destHtml, strEncodng);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    /**
     * 取相对路径
     * @param filePath
     * @return
     */
    public static String getRelativePath(String filePath) {
        char separator = '/';

        if (filePath.lastIndexOf(separator) < 0) {
            separator = '\\';
        }
        if(filePath.lastIndexOf(separator) < 0){
            return "";
        }
        String partStr = filePath.substring(0, filePath.lastIndexOf(separator));
        partStr = partStr.substring(0, partStr.lastIndexOf(separator));
        return filePath.substring(partStr.length() + 1);

    }

    /**
     * 获取mht文件内容中资源文件的名称
     *
     * @param strName
     * @param ID
     * @return
     */
    public static String getName(String strName, int ID) {
        char separator1 = '/';
        char separator2 = '\\';
        // 将换行替换
        strName = strName.replaceAll("\r\n", "");

        // 获取文件名称
        if (strName.lastIndexOf(separator1) >= 0) {
            return strName.substring(strName.lastIndexOf(separator1) + 1);
        }
        if (strName.lastIndexOf(separator2) >= 0) {
            return strName.substring(strName.lastIndexOf(separator2) + 1);
        }
        return "";
    }

    /**
     * 将提取出来的html内容写入保存的路径中。
     *
     * @param htmlTxt
     * @param htmlPath
     * @param encode
     */
    public static boolean saveHtml(String htmlTxt, String htmlPath, String encode) {
        //若不存在父目录，则创建
        File htmlFile = new File(htmlPath);
        if(!htmlFile.getParentFile().exists()){
            htmlFile.getParentFile().mkdirs();
        }
        try {
            Writer out = null;
            out = new OutputStreamWriter(new FileOutputStream(htmlPath, false), encode);
            out.write(htmlTxt);
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 保存网页中的JS、图片、CSS样式等资源文件
     *
     * @param srcFile 源文件
     * @param inputStream 输入流
     * @return
     */
    private static boolean saveResourcesFile(File srcFile, InputStream inputStream) {
        if (srcFile == null || inputStream == null) {
            return false;
        }

        BufferedInputStream in = null;
        FileOutputStream fio = null;
        BufferedOutputStream osw = null;
        try {
            in = new BufferedInputStream(inputStream);
            fio = new FileOutputStream(srcFile);
            osw = new BufferedOutputStream(new DataOutputStream(fio));
            int index = 0;
            byte[] a = new byte[1024];
            while ((index = in.read(a)) != -1) {
                osw.write(a, 0, index);
            }
            osw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (osw != null)
                    osw.close();
                if (fio != null)
                    fio.close();
                if (in != null)
                    in.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 获取mht文件里资源文件的URL路径
     *
     * @param bp
     * @return
     */
    private static String getResourcesUrl(MimeBodyPart bp) {
        if (bp == null) {
            return null;
        }
        try {
            Enumeration list = bp.getAllHeaders();
            while (list.hasMoreElements()) {
                javax.mail.Header head = (javax.mail.Header) list.nextElement();
                if (head.getName().compareTo("Content-Location") == 0) {
                    return head.getValue();
                }
            }
            return null;
        } catch (MessagingException e) {
            return null;
        }
    }

    /**
     * 获取mht文件中的内容代码
     *
     * @param bp

     * @return
     */
    private static String getHtmlText(MimeBodyPart bp, String strEncoding) {
        InputStream textStream = null;
        BufferedInputStream buff = null;
        BufferedReader br = null;
        Reader r = null;
        try {
            textStream = bp.getInputStream();
            buff = new BufferedInputStream(textStream);
            r = new InputStreamReader(buff, strEncoding);
            br = new BufferedReader(r);
            StringBuffer strHtml = new StringBuffer("");
            String strLine = null;
            while ((strLine = br.readLine()) != null) {
                //System.out.println(strLine);
                strHtml.append(strLine + "\r\n");
            }
            br.close();
            r.close();
            textStream.close();
            return strHtml.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (buff != null)
                    buff.close();
                if (textStream != null)
                    textStream.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 获取mht网页文件中内容代码的编码
     *
     * @param bp
     * @return
     */
    private static String getEncoding(MimeBodyPart bp) {
        if (bp == null) {
            return null;
        }
        try {
            Enumeration list = bp.getAllHeaders();
            while (list.hasMoreElements()) {
                javax.mail.Header head = (javax.mail.Header) list.nextElement();
                if (head.getName().equalsIgnoreCase("Content-Type")) {
                    String strType = head.getValue();
                    int pos = strType.indexOf("charset=");
                    if (pos >= 0 && strType.indexOf("text/html") >= 0) {
                        String strEncoding = strType.substring(pos + 8, strType.length());
                        if (strEncoding.startsWith("\"") || strEncoding.startsWith("\'")) {
                            strEncoding = strEncoding.substring(1, strEncoding.length());
                        }
                        if (strEncoding.endsWith("\"") || strEncoding.endsWith("\'")) {
                            strEncoding = strEncoding.substring(0, strEncoding.length() - 1);
                        }
                        if (strEncoding.toLowerCase().compareTo("gb2312") == 0) {
                            //strEncoding = "gbk"; 貌似不行，得用ISO-8859-1
                            strEncoding = "ISO-8859-1";
                        }
                        return strEncoding;
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}