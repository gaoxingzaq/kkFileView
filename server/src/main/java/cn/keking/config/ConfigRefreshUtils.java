package cn.keking.config;

import org.artofsolving.jodconverter.office.OfficeUtils;

import java.util.Properties;

/**
 * 配置文件刷新工具类
 *
 * @author chenjh,zhongjiahua
 * @date 2022-11-29
 */
public class ConfigRefreshUtils {

    public static void parseProperties(Properties properties) {
        String text;
        String media;
        boolean cacheEnabled;
        String[] textArray;
        String[] mediaArray;
        String officePreviewType;
        String officePreviewSwitchDisabled;
        String ftpUsername;
        String ftpPassword;
        String ftpControlEncoding;
        String baseUrl;
        String trustHost;
        String pdfDownloadDisable;
        String pdfXianzhi;
        String BeiAn;
        String pdfpagee;
        String mediagg;
        String prohibit;
        String[] prohibitArray;
        boolean fileUploadDisable;
        String tifPreviewType;
        String ofdPreviewType;
        String xlsxzh;
        String officedel;
        String PPTXTP;
        String pdfjpg;
        String yashuo;
        String localpreview;
        String previewindex;
        String pdffy;
        OfficeUtils.restorePropertiesFromEnvFormat(properties);
        cacheEnabled = Boolean.parseBoolean(properties.getProperty("cache.enabled", ConfigConstants.DEFAULT_CACHE_ENABLED));
        text = properties.getProperty("simText", ConfigConstants.DEFAULT_TXT_TYPE);
        media = properties.getProperty("media", ConfigConstants.DEFAULT_MEDIA_TYPE);
        officePreviewType = properties.getProperty("office.preview.type", ConfigConstants.DEFAULT_OFFICE_PREVIEW_TYPE);
        officePreviewSwitchDisabled = properties.getProperty("office.preview.switch.disabled", ConfigConstants.DEFAULT_OFFICE_PREVIEW_SWITCH_DISABLED);
        ftpUsername = properties.getProperty("ftp.username", ConfigConstants.DEFAULT_FTP_USERNAME);
        ftpPassword = properties.getProperty("ftp.password", ConfigConstants.DEFAULT_FTP_PASSWORD);
        ftpControlEncoding = properties.getProperty("ftp.control.encoding", ConfigConstants.DEFAULT_FTP_CONTROL_ENCODING);
        textArray = text.split(",");
        mediaArray = media.split(",");
        baseUrl = properties.getProperty("base.url", ConfigConstants.DEFAULT_BASE_URL);
        trustHost = properties.getProperty("trust.host", ConfigConstants.DEFAULT_TRUST_HOST);
        pdfDownloadDisable = properties.getProperty("pdf.download.disable", ConfigConstants.DEFAULT_PDF_DOWNLOAD_DISABLE);
        pdfXianzhi = properties.getProperty("pdf.xianzhi", ConfigConstants.DEFAULT_pdfXianzhi_DISABLE);
        BeiAn = properties.getProperty("BeiAn", ConfigConstants.DEFAULT_BeiAn_DISABLE);
        pdfpagee = properties.getProperty("pdfpagee", ConfigConstants.DEFAULT_pdfpagee_DISABLE);
        mediagg = properties.getProperty("gg.media", ConfigConstants.DEFAULT_mediagg_DISABLE);
        prohibit = properties.getProperty("prohibit", ConfigConstants.DEFAULT_prohibit_DISABLE);
        fileUploadDisable = Boolean.parseBoolean(properties.getProperty("file.upload.disable", ConfigConstants.DEFAULT_FILE_UPLOAD_DISABLE));
        tifPreviewType = properties.getProperty("tif.preview.type", ConfigConstants.DEFAULT_TIF_PREVIEW_TYPE);
        ofdPreviewType = properties.getProperty("OFD.preview.type", ConfigConstants.DEFAULT_OFD_PREVIEW_TYPE);
        xlsxzh = properties.getProperty("xlsxzh", ConfigConstants.DEFAULT_xlsxzh_PREVIEW_TYPE);
        officedel = properties.getProperty("officedel", ConfigConstants.DEFAULT_officedel_PREVIEW_TYPE);
        PPTXTP = properties.getProperty("PPTXTP", ConfigConstants.DEFAULT_PPTXTP_PREVIEW_TYPE);
        pdfjpg = properties.getProperty("pdfjpg", ConfigConstants.DEFAULT_pdfjpg_PREVIEW_TYPE);
        yashuo = properties.getProperty("yashuo", ConfigConstants.DEFAULT_yashuo_PREVIEW_TYPE);
        localpreview = properties.getProperty("local.preview.dir", ConfigConstants.DEFAULT_localpreview_PREVIEW_TYPE);
        previewindex = properties.getProperty("local.preview.index", ConfigConstants.DEFAULT_previewindex_PREVIEW_TYPE);
        pdffy = properties.getProperty("pdffy", ConfigConstants.DEFAULT_pdffy_PREVIEW_TYPE);
        prohibitArray = prohibit.split(",");

        ConfigConstants.setCacheEnabledValueValue(cacheEnabled);
        ConfigConstants.setSimTextValue(textArray);
        ConfigConstants.setMediaValue(mediaArray);
        ConfigConstants.setOfficePreviewTypeValue(officePreviewType);
        ConfigConstants.setFtpUsernameValue(ftpUsername);
        ConfigConstants.setFtpPasswordValue(ftpPassword);
        ConfigConstants.setFtpControlEncodingValue(ftpControlEncoding);
        ConfigConstants.setBaseUrlValue(baseUrl);
        ConfigConstants.setTrustHostValue(trustHost);
        ConfigConstants.setOfficePreviewSwitchDisabledValue(officePreviewSwitchDisabled);
        ConfigConstants.setPdfDownloadDisableValue(pdfDownloadDisable);
        ConfigConstants.setpdfXianzhiValue(pdfXianzhi);
        ConfigConstants.setBeiAnValue(BeiAn);
        ConfigConstants.setpdfpageeValue(pdfpagee);
        ConfigConstants.setmediaggValue(mediagg);
        ConfigConstants.setprohibitValue(prohibitArray);
        ConfigConstants.setFileUploadDisableValue(fileUploadDisable);
        ConfigConstants.setTifPreviewTypeValue(tifPreviewType);
        ConfigConstants.setOfdPreviewTypeValue(ofdPreviewType);
        ConfigConstants.setxlsxzhValue(xlsxzh);
        ConfigConstants.setofficedelValue(officedel);
        ConfigConstants.setPPTXTPValue(PPTXTP);
        ConfigConstants.setpdfjpgValue(pdfjpg);
        ConfigConstants.setyashuoValue(yashuo);
        ConfigConstants.setlocalpreviewValue(localpreview);
        ConfigConstants.setpreviewindexValue(previewindex);
        ConfigConstants.setpdffyValue(pdffy);
        setWatermarkConfig(properties);
    }

    private static void setWatermarkConfig(Properties properties) {
        String watermarkTxt = properties.getProperty("watermark.txt", WatermarkConfigConstants.DEFAULT_WATERMARK_TXT);
        String watermarkXSpace = properties.getProperty("watermark.x.space", WatermarkConfigConstants.DEFAULT_WATERMARK_X_SPACE);
        String watermarkYSpace = properties.getProperty("watermark.y.space", WatermarkConfigConstants.DEFAULT_WATERMARK_Y_SPACE);
        String watermarkFont = properties.getProperty("watermark.font", WatermarkConfigConstants.DEFAULT_WATERMARK_FONT);
        String watermarkFontsize = properties.getProperty("watermark.fontsize", WatermarkConfigConstants.DEFAULT_WATERMARK_FONTSIZE);
        String watermarkColor = properties.getProperty("watermark.color", WatermarkConfigConstants.DEFAULT_WATERMARK_COLOR);
        String watermarkAlpha = properties.getProperty("watermark.alpha", WatermarkConfigConstants.DEFAULT_WATERMARK_ALPHA);
        String watermarkWidth = properties.getProperty("watermark.width", WatermarkConfigConstants.DEFAULT_WATERMARK_WIDTH);
        String watermarkHeight = properties.getProperty("watermark.height", WatermarkConfigConstants.DEFAULT_WATERMARK_HEIGHT);
        String watermarkAngle = properties.getProperty("watermark.angle", WatermarkConfigConstants.DEFAULT_WATERMARK_ANGLE);
        String watermarkqy = properties.getProperty("watermark.qy", WatermarkConfigConstants.DEFAULT_WATERMARK_qy);
        WatermarkConfigConstants.setwatermarkqyValue(watermarkqy);
        WatermarkConfigConstants.setWatermarkTxtValue(watermarkTxt);
        WatermarkConfigConstants.setWatermarkXSpaceValue(watermarkXSpace);
        WatermarkConfigConstants.setWatermarkYSpaceValue(watermarkYSpace);
        WatermarkConfigConstants.setWatermarkFontValue(watermarkFont);
        WatermarkConfigConstants.setWatermarkFontsizeValue(watermarkFontsize);
        WatermarkConfigConstants.setWatermarkColorValue(watermarkColor);
        WatermarkConfigConstants.setWatermarkAlphaValue(watermarkAlpha);
        WatermarkConfigConstants.setWatermarkWidthValue(watermarkWidth);
        WatermarkConfigConstants.setWatermarkHeightValue(watermarkHeight);
        WatermarkConfigConstants.setWatermarkAngleValue(watermarkAngle);
    }

}
