package cn.keking.service;

import cn.keking.model.FileAttribute;
import org.springframework.ui.Model;

/**
 * Created by kl on 2018/1/17.
 * Content :
 */
public interface FilePreview {

    String FLV_FILE_PREVIEW_PAGE = "flv";
    String PDF_FILE_PREVIEW_PAGE = "pdf";
    String PPT_FILE_PREVIEW_PAGE = "ppt";
    String COMPRESS_FILE_PREVIEW_PAGE = "compress";
    String MEDIA_FILE_PREVIEW_PAGE = "media";
    String PICTURE_FILE_PREVIEW_PAGE = "picture";
    String PPICTURE_FILE_PREVIEW_PAGE = "ppicture";
    String XZLPICTURE_FILE_PREVIEW_PAGE = "xzlpicture";
    String TIFF_FILE_PREVIEW_PAGE = "tiff";
    String OFD_FILE_PREVIEW_PAGE = "ofd";
	String SVG_FILE_PREVIEW_PAGE = "svg";
    String PSD_FILE_PREVIEW_PAGE = "psd";
	String EML_FILE_PREVIEW_PAGE = "eml";
    String OFFICE_PICTURE_FILE_PREVIEW_PAGE = "officePicture";
    String TXT_FILE_PREVIEW_PAGE = "txt";
    String CODE_FILE_PREVIEW_PAGE = "code";
    String EXEL_FILE_PREVIEW_PAGE = "html";
    String XML_FILE_PREVIEW_PAGE = "xml";
    String XLSX_FILE_PREVIEW_PAGE = "xlsx";
	String FYPDF_FILE_PREVIEW_PAGE = "fypdf";
    String MARKDOWN_FILE_PREVIEW_PAGE = "markdown";
    String NOT_SUPPORTED_FILE_PAGE = "fileNotSupported";
    String Online3D_FILE_PAGE = "online3D";
    String Mht_FILE_PAGE = "mht";
    String Jimi_FILE_PAGE = "jiami";
    String Jiaz_FILE_PAGE = "jiazai";
    String Ofdsvg_FILE_PAGE = "ofdsvg";

    String filePreviewHandle(String url, Model model, FileAttribute fileAttribute);
}
