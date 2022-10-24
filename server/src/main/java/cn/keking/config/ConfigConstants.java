package cn.keking.config;

import org.artofsolving.jodconverter.util.ConfigUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;

/**
 * @author: chenjh
 * @since: 2019/4/10 17:22
 */
@Component
public class ConfigConstants {

    static {
        //pdfbox兼容低版本jdk
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    private static Boolean cacheEnabled;
    private static String[] simTexts = {};
    private static String[] medias = {};
    private static String[] convertMedias = {};
    private static String mediaConvertDisable;
    private static String officePreviewType;
    private static String officePreviewSwitchDisabled;
    private static String ftpUsername;
    private static String ftpPassword;
    private static String ftpControlEncoding;
    private static String baseUrl;
    private static String fileDir = ConfigUtils.getHomePath() + File.separator + "file" + File.separator;
    private static CopyOnWriteArraySet<String> trustHostSet;
    private static String pdfDownloadDisable;
    private static String pdfXianzhi;
    private static String BeiAn;
    private static String mediagg;
    private static String pdfyeman;
    private static Boolean fileUploadDisable;
    private static String tifPreviewType;
    private static String ofdPreviewType;
    private static String xlsxzh;
    private static String officedel;
    private static String PPTXTP;
    private static String pdfjpg;
    private static String yashuo;
    private static String localpreview;
    private static String previewindex;
    private static String pdffy;

    public static final String DEFAULT_CACHE_ENABLED = "true";
    public static final String DEFAULT_TXT_TYPE = "txt,xml,json,properties,md,gitignore,log,m,bas,prg";
    public static final String DEFAULT_MEDIA_TYPE = "mp3,wav,mp4,flv";
    public static final String DEFAULT_OFFICE_PREVIEW_TYPE = "image";
    public static final String DEFAULT_OFFICE_PREVIEW_SWITCH_DISABLED = "false";
    public static final String DEFAULT_FTP_USERNAME = null;
    public static final String DEFAULT_FTP_PASSWORD = null;
    public static final String DEFAULT_FTP_CONTROL_ENCODING = "UTF-8";
    public static final String DEFAULT_BASE_URL = "default";
    public static final String DEFAULT_FILE_DIR_VALUE = "default";
    public static final String DEFAULT_TRUST_HOST = "default";
    public static final String DEFAULT_PDF_DOWNLOAD_DISABLE = "true";
    public static final String DEFAULT_pdfXianzhi_DISABLE = "true";
    public static final String DEFAULT_BeiAn_DISABLE = "无";
    public static final String DEFAULT_mediagg_DISABLE = "高雄修改";
    public static final String DEFAULT_FILE_UPLOAD_DISABLE = "false";
    public static final String DEFAULT_TIF_PREVIEW_TYPE = "tif";
    public static final String DEFAULT_OFD_PREVIEW_TYPE = "ofd";
    public static final String DEFAULT_xlsxzh_PREVIEW_TYPE = "false";
    public static final String DEFAULT_officedel_PREVIEW_TYPE = "false";
    public static final String DEFAULT_PPTXTP_PREVIEW_TYPE = "2";
    public static final String DEFAULT_pdfjpg_PREVIEW_TYPE = "100";
    public static final String DEFAULT_yashuo_PREVIEW_TYPE = "false";
    public static final String DEFAULT_localpreview_PREVIEW_TYPE = "true";
    public static final String DEFAULT_previewindex_PREVIEW_TYPE = "true";
    public static final String DEFAULT_pdffy_PREVIEW_TYPE = "false";


    public static Boolean isCacheEnabled() {
        return cacheEnabled;
    }

    @Value("${cache.enabled:true}")
    public void setCacheEnabled(String cacheEnabled) {
        setCacheEnabledValueValue(Boolean.parseBoolean(cacheEnabled));
    }

    public static void setCacheEnabledValueValue(Boolean cacheEnabled) {
        ConfigConstants.cacheEnabled = cacheEnabled;
    }

    public static String[] getSimText() {
        return simTexts;
    }

    @Value("${simText:txt,html,htm,asp,jsp,xml,json,properties,md,gitignore,log,java,py,c,cpp,sql,sh,bat,m,bas,prg,cmd}")
    public void setSimText(String simText) {
        String[] simTextArr = simText.split(",");
        setSimTextValue(simTextArr);
    }

    public static void setSimTextValue(String[] simText) {
        ConfigConstants.simTexts = simText;
    }

    public static String[] getMedia() {
        return medias;
    }

    @Value("${media:mp3,wav,mp4,flv}")
    public void setMedia(String media) {
        String[] mediaArr = media.split(",");
        setMediaValue(mediaArr);
    }

    public static void setMediaValue(String[] Media) {
        ConfigConstants.medias = Media;
    }

    public static String[] getConvertMedias() {
        return convertMedias;
    }

    @Value("${convertMedias:avi,mov,wmv,mkv,3gp,rm}")
    public void setConvertMedias(String convertMedia) {
        String[] mediaArr = convertMedia.split(",");
        setConvertMediaValue(mediaArr);
    }

    public static void setConvertMediaValue(String[] ConvertMedia) {
        ConfigConstants.convertMedias = ConvertMedia;
    }

    public static String getMediaConvertDisable() {
        return mediaConvertDisable;
    }


    @Value("${media.convert.disable:true}")
    public void setMediaConvertDisable(String mediaConvertDisable) {
        setMediaConvertDisableValue(mediaConvertDisable);
    }
    public static void setMediaConvertDisableValue(String mediaConvertDisable) {
        ConfigConstants.mediaConvertDisable = mediaConvertDisable;
    }

    public static String getOfficePreviewType() {
        return officePreviewType;
    }

    @Value("${office.preview.type:image}")
    public void setOfficePreviewType(String officePreviewType) {
        setOfficePreviewTypeValue(officePreviewType);
    }

    public static void setOfficePreviewTypeValue(String officePreviewType) {
        ConfigConstants.officePreviewType = officePreviewType;
    }

    public static String getFtpUsername() {
        return ftpUsername;
    }

    @Value("${ftp.username:}")
    public void setFtpUsername(String ftpUsername) {
        setFtpUsernameValue(ftpUsername);
    }

    public static void setFtpUsernameValue(String ftpUsername) {
        ConfigConstants.ftpUsername = ftpUsername;
    }

    public static String getFtpPassword() {
        return ftpPassword;
    }

    @Value("${ftp.password:}")
    public void setFtpPassword(String ftpPassword) {
        setFtpPasswordValue(ftpPassword);
    }

    public static void setFtpPasswordValue(String ftpPassword) {
        ConfigConstants.ftpPassword = ftpPassword;
    }

    public static String getFtpControlEncoding() {
        return ftpControlEncoding;
    }

    @Value("${ftp.control.encoding:UTF-8}")
    public void setFtpControlEncoding(String ftpControlEncoding) {
        setFtpControlEncodingValue(ftpControlEncoding);
    }

    public static void setFtpControlEncodingValue(String ftpControlEncoding) {
        ConfigConstants.ftpControlEncoding = ftpControlEncoding;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    @Value("${base.url:default}")
    public void setBaseUrl(String baseUrl) {
        setBaseUrlValue(baseUrl);
    }

    public static void setBaseUrlValue(String baseUrl) {
        ConfigConstants.baseUrl = baseUrl;
    }

    public static String getFileDir() {
        return fileDir;
    }

    @Value("${file.dir:default}")
    public void setFileDir(String fileDir) {
        setFileDirValue(fileDir);
    }

    public static void setFileDirValue(String fileDir) {
        if (!DEFAULT_FILE_DIR_VALUE.equalsIgnoreCase(fileDir)) {
            if (!fileDir.endsWith(File.separator)) {
                fileDir = fileDir + File.separator;
            }
            ConfigConstants.fileDir = fileDir;
        }
    }

    @Value("${trust.host:default}")
    public void setTrustHost(String trustHost) {
        setTrustHostValue(trustHost);
    }

    public static void setTrustHostValue(String trustHost) {
        CopyOnWriteArraySet<String> trustHostSet;
        if (DEFAULT_TRUST_HOST.equalsIgnoreCase(trustHost)) {
            trustHostSet = new CopyOnWriteArraySet<>();
        } else {
            String[] trustHostArray = trustHost.toLowerCase().split(",");
            trustHostSet = new CopyOnWriteArraySet<>(Arrays.asList(trustHostArray));
            setTrustHostSet(trustHostSet);
        }
        setTrustHostSet(trustHostSet);
    }

    public static Set<String> getTrustHostSet() {
        return trustHostSet;
    }

    private static void setTrustHostSet(CopyOnWriteArraySet<String> trustHostSet) {
        ConfigConstants.trustHostSet = trustHostSet;
    }

    public static String getPdfDownloadDisable() {
        return pdfDownloadDisable;
    }


    @Value("${pdf.download.disable:true}")
    public void setPdfDownloadDisable(String pdfDownloadDisable) {
        setPdfDownloadDisableValue(pdfDownloadDisable);
    }
    public static void setPdfDownloadDisableValue(String pdfDownloadDisable) {
        ConfigConstants.pdfDownloadDisable = pdfDownloadDisable;
    }

    public static String getBeiAn() {
        return BeiAn;
    }
    @Value("${BeiAn:无}")
    public void setBeiAn(String BeiAn) {
        setBeiAnValue(BeiAn);
    }
    public static void setBeiAnValue(String BeiAn) {
        ConfigConstants.BeiAn = BeiAn;
    }

    public static String getpdfXianzhi() {
        return pdfXianzhi;
    }
    @Value("${pdf.xianzhi:true}")
    public void setpdfXianzhi(String pdfXianzhi) {
        setpdfXianzhiValue(pdfXianzhi);
    }
    public static void setpdfXianzhiValue(String pdfXianzhi) {
        ConfigConstants.pdfXianzhi = pdfXianzhi;
    }

    public static String getmediagg() {
        return mediagg;
    }
    @Value("${gg.media:高雄修改版本}")
    public void setmediagg(String mediagg) {
        setmediaggValue(mediagg);
    }
    public static void setmediaggValue(String mediagg) {
        ConfigConstants.mediagg = mediagg;
    }

    public static String getOfficePreviewSwitchDisabled() {
        return officePreviewSwitchDisabled;
    }
    @Value("${office.preview.switch.disabled:true}")
    public void setOfficePreviewSwitchDisabled(String officePreviewSwitchDisabled) {
        ConfigConstants.officePreviewSwitchDisabled = officePreviewSwitchDisabled;
    }
    public static void setOfficePreviewSwitchDisabledValue(String officePreviewSwitchDisabled) {
        ConfigConstants.officePreviewSwitchDisabled = officePreviewSwitchDisabled;
    }
    
    public static Boolean getFileUploadDisable() {
        return fileUploadDisable;
    }

    @Value("${file.upload.disable:false}")
    public static void setFileUploadDisable(Boolean fileUploadDisable) {
        setFileUploadDisableValue(fileUploadDisable);
    }

    public static void setFileUploadDisableValue(Boolean fileUploadDisable) {
        ConfigConstants.fileUploadDisable = fileUploadDisable;
    }




    public static String getTifPreviewType() {
        return tifPreviewType;
    }

    @Value("${tif.preview.type:tif}")
    public void setTifPreviewType(String tifPreviewType) {
        setTifPreviewTypeValue(tifPreviewType);
    }

    public static void setTifPreviewTypeValue(String tifPreviewType) {
        ConfigConstants.tifPreviewType = tifPreviewType;
    }

    public static String getOfdPreviewType() {
        return ofdPreviewType;
    }

    @Value("${OFD.preview.type:ofd}")
    public void setOfdPreviewType(String ofdPreviewType) {
        setOfdPreviewTypeValue(ofdPreviewType);
    }

    public static void setOfdPreviewTypeValue(String ofdPreviewType) {
        ConfigConstants.ofdPreviewType = ofdPreviewType;
    }

    public static String getxlsxzh() {
        return xlsxzh;
    }

    @Value("${xlsxzh:false}")
    public void setxlsxzh(String xlsxzh) {
        setxlsxzhValue(xlsxzh);
    }

    public static void setxlsxzhValue(String xlsxzh) {
        ConfigConstants.xlsxzh = xlsxzh;
    }

    public static String getofficedel() {
        return officedel;
    }

    @Value("${officedel:false}")
    public void setofficedel(String officedel) {
        setofficedelValue(officedel);
    }

    public static void setofficedelValue(String officedel) {
        ConfigConstants.officedel = officedel;
    }


    public static String getpdffy() {
        return pdffy;
    }

    @Value("${pdffy:false}")
    public void setpdffy(String pdffy) {
        setofficedelValue(pdffy);
    }

    public static void setpdffyValue(String pdffy) {
        ConfigConstants.pdffy = pdffy;
    }


    public static String getyashuo() {
        return yashuo;
    }

    @Value("${yashuo:false}")
    public void setyashuo(String yashuo) {
        setyashuoValue(yashuo);
    }

    public static void setyashuoValue(String yashuo) {
        ConfigConstants.yashuo = yashuo;
    }


    public static String getpdfjpg() {
        return pdfjpg;
    }

    @Value("${pdfjpg:false}")
    public void setpdfjpg(String pdfjpg) {
        setpdfjpgValue(pdfjpg);
    }

    public static void setpdfjpgValue(String pdfjpg) {
        ConfigConstants.pdfjpg = pdfjpg;
    }


    public static String getPPTXTP() {
        return PPTXTP;
    }

    @Value("${PPTXTP:2}")
    public void setPPTXTP(String PPTXTP) {
        setPPTXTPValue(PPTXTP);
    }
    public static void setPPTXTPValue(String PPTXTP) {
        ConfigConstants.PPTXTP = PPTXTP;
    }

    public static String getlocalpreview() {
        return localpreview;
    }

    @Value("${local.preview.dir:true}")
    public void setlocalpreview(String localpreview) {
        setlocalpreviewValue(localpreview);
    }
    public static void setlocalpreviewValue(String localpreview) {
        ConfigConstants.localpreview = localpreview;
    }

    public static String getpreviewindex() {
        return previewindex;
    }

    @Value("${local.preview.index:true}")
    public void setpreviewindex(String previewindex) {
        setpreviewindexValue(previewindex);
    }

    public static void setpreviewindexValue(String previewindex) {
        ConfigConstants.previewindex = previewindex;
    }

}
