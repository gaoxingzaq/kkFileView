package cn.keking.service;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
public class POIExcelToHtml {

    public static boolean excelToHtml(String filePath, String path, String seapdaWebUrl) throws Exception {

        StringBuilder sb =null;
        InputStream inputStream = new FileInputStream(path);
        Workbook wb = null;
        try {
          wb = WorkbookFactory.create(inputStream);// 此WorkbookFactory在POI-3.10版本中使用需要添加dom4j
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
        String head = htmlHead(wb, seapdaWebUrl);
        writeFile(head, filePath);  //写入头
        try {
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                //生成内容
                for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                    Sheet sheet = wb.getSheetAt(numSheet);// 获取Sheet的内容
                    if (sheet == null) {
                        continue;
                    }
                    if(sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append("<div id='").append(sheet.getSheetName().replace(" ", "")).append("1' style='position:relative;top:30px;'>\n");
                    int lastRowNum = sheet.getLastRowNum();
                    Map<String, String>[] map = getRowSpanColSpanMap(sheet);
                    sb.append("<table style='border-collapse:collapse;'>\n");
                    writeFile(sb.toString(), filePath); //追加写入前部分内容
                    sb = null;
                    StringBuilder ssb = null;
                    for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
                        if(ssb == null) {
                            ssb = new StringBuilder();
                        }
                        ssb.append("<tr>\n");
                        String htmlExcel = getExcelInfo(xWb,map, sheet, rowNum, true);
                        ssb.append(htmlExcel);
                        ssb.append("</tr>\n");
                        if(rowNum % 5000 == 0) {
                            //每5000行写一次
                            writeFile(ssb.toString(), filePath); //追加写入部分内容
                            ssb = null; //防止内存溢出，清空
                        }
                    }
                    if (ssb == null) {
                        ssb = new StringBuilder();
                    }
                    ssb.append("</table>\n");
                    ssb.append("  </div>\n");
                    //每不足5000行一次写
                    writeFile(ssb.toString(), filePath); //追加写入部分内容
                    ssb = null;
                }
            } else if (wb instanceof HSSFWorkbook) {
                HSSFWorkbook hWb = (HSSFWorkbook) wb;
                //生成内容
                for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                    Sheet sheet = wb.getSheetAt(numSheet);// 获取Sheet的内容
                    if (sheet == null) {
                        continue;
                    }
                    if(sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append("<div id='").append(sheet.getSheetName().replace(" ", "")).append("1' style='position:relative;top:30px;'>\n");
                    int lastRowNum = sheet.getLastRowNum();
                    Map<String, String>[] map = getRowSpanColSpanMap(sheet);
                    sb.append("<table style='border-collapse:collapse;'>\n");
                    writeFile(sb.toString(), filePath); //追加写入前部分内容
                    sb = null;
                    StringBuilder ssb = null;
                    for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
                        if(ssb == null){
                            ssb = new StringBuilder();
                        }
                        ssb.append("<tr>\n");
                        String htmlExcel = getExcelInfo(hWb, map, sheet, rowNum, true);
                        ssb.append(htmlExcel);
                        ssb.append("</tr>\n");
                        if (rowNum % 5000 == 0) {
                            //每5000行写一次
                            writeFile(ssb.toString(), filePath); //追加写入部分内容
                            ssb = null; //防止内存溢出，清空
                        }
                    }
                    if (ssb == null) {
                        ssb = new StringBuilder();
                    }
                    ssb.append("</table>\n");
                    ssb.append("  </div>\n");
                    //每不足5000行一次写
                    writeFile(ssb.toString(), filePath); //追加写入部分内容
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        } finally
        {
            try {
                writeFile(htmlBottom(), filePath); //追加写入最后内容
                inputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static void writeFile(String content, String path) {
        FileOutputStream fos;
        BufferedWriter bw;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file,true);
            bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            bw.write(content);
            bw.flush();
            bw.close();
            fos.close();
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }
    }
    private static String htmlHead(Workbook wb, String seapdaWebUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
        sb.append("    <link rel=\"stylesheet\" href=\"/exce/easyui.css\" type=\"text/css\"/>\n");
        sb.append("    <script type=text/javascript src=\"/exce/jquery.min.js\"></script>\n");
        sb.append("    <script type=text/javascript src=\"/exce/jquery.easyui.min.js\"></script>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<div data-options=\"region:'center',border:false\" style=\"position:fixed; z-index:9999; \">\n");
        sb.append("<div id='viewTabs'>\n");
        //生成目录
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            Sheet sheet = wb.getSheetAt(numSheet);// 获取Sheet的内容
            if (sheet == null) {
                continue;
            }
            sb.append("<div title='").append(sheet.getSheetName().replace(" ", "")).append("' style='padding:0px'></div>\n");
        }
        sb.append("  </div>\n");
        sb.append("  </div>\n");
        return sb.toString();
    }

    private static String htmlBottom() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("$('#viewTabs').show();\n");
        sb.append("$('#viewTabs').tabs({\n");
        sb.append(" border:false,\n");
        sb.append("fit:true,\n");
        sb.append("    tabPosition:'top',\n");
        sb.append("onSelect:function(title,index){\n");
        sb.append("    var len = $('#viewTabs').tabs('tabs').length;\n");
        sb.append("    for(var i = 0 ; i < len ; i ++){\n");
        sb.append("        var titleT = $('#viewTabs').tabs('getTab',i).panel('options').title;\n");
        sb.append("        if(titleT == title){\n");
        sb.append("           $('#'+title+'1').show();\n");
        sb.append("       }else{\n");
        sb.append("           $('#'+titleT+'1').hide();\n");
        sb.append("       }\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("}\n");
        sb.append("});\n");
        sb.append("</script>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }


    private static String getExcelInfo(Workbook wb , Map<String, String>[] map, Sheet sheet, int rowNum, boolean isWithStyle) {
        StringBuffer sb = new StringBuffer();
        Row row; // 兼容
        Cell cell; // 兼容
        row = sheet.getRow(rowNum);
        if (row == null) {
            //sb.append("<tr><td >  </td></tr>");
            return sb.toString();
        }
        int lastColNum = row.getLastCellNum();
        for (int colNum = 0; colNum < lastColNum; colNum++) {
            cell = row.getCell(colNum);
            if (cell == null) { // 特殊情况 空白的单元格会返回null
                //sb.append("<td> </td>");
                continue;
            }
            String stringValue = getCellValue(cell);
            if (map[0].containsKey(rowNum + "," + colNum)) {
                String pointString = map[0].get(rowNum + "," + colNum);
                map[0].remove(rowNum + "," + colNum);
                int bottomeRow = Integer.parseInt(pointString.split(",")[0]);
                int bottomeCol = Integer.parseInt(pointString.split(",")[1]);
                int rowSpan = bottomeRow - rowNum + 1;
                int colSpan = bottomeCol - colNum + 1;
                sb.append("<td rowspan= '").append(rowSpan).append("' colspan= '").append(colSpan).append("' \n");
            } else if (map[1].containsKey(rowNum + "," + colNum)) {
                map[1].remove(rowNum + "," + colNum);
                continue;
            } else {
                sb.append("<td ");
            }
            // 判断是否需要样式
            if (isWithStyle) {
                dealExcelStyle(wb, sheet, cell, sb, stringValue);// 处理单元格样式
            }
            sb.append(">");
            if (stringValue == null || "".equals(stringValue.trim())) {
                sb.append("&nbsp;&nbsp;&nbsp;");
            } else {
                // 将ascii码为160的空格转换为html下的空格（ ）
                sb.append(stringValue.replace(String.valueOf((char) 160), "&nbsp;&nbsp;"));
            }
            sb.append("</td>\n");
        }
        return sb.toString();
    }
    private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
        Map<String, String> map0 = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        int mergedNum = sheet.getNumMergedRegions();
        CellRangeAddress range;
        for (int i = 0; i < mergedNum; i++) {
            range = sheet.getMergedRegion(i);
            int topRow = range.getFirstRow();
            int topCol = range.getFirstColumn();
            int bottomRow = range.getLastRow();
            int bottomCol = range.getLastColumn();
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            // System.out.println(topRow + "," + topCol + "," + bottomRow + ","
            // + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = {map0, map1};
        return map;
    }


    private static String getCellValue(Cell cell) {
        String result;
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil
                            .getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // System.out.println(temp);
                    // 单元格设置成常规
                    if ("General".equals(temp)) {
//                    	if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case BLANK:
            default:
                result = "";
                break;
        }
        return result;
    }

    /**
     * 处理表格样式
     */
    private static void dealExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuffer sb, String stringValue) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle != null) {
            short alignment = cellStyle.getAlignment();

            sb.append("align='").append(convertAlignToHtml(alignment)).append("' ");// 单元格内容的水平对齐方式
            short verticalAlignment = cellStyle.getVerticalAlignment();
            sb.append("valign='").append(convertVerticalAlignToHtml(verticalAlignment)).append("' ");// 单元格中内容的垂直排列方式
            if (wb instanceof XSSFWorkbook) {
                XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
                sb.append("style='");
                sb.append("font-weight:bold;"); // 字体加粗
                sb.append("font-size: ").append(xf.getFontHeight() / 2).append("%;"); // 字体大小
                int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());

                sb.append("width:").append(columnWidth).append("%");

                XSSFColor xc = xf.getXSSFColor();
                if (xc != null && !"".equals(xc)) {
                    sb.append("color:#").append(xc.getARGBHex().substring(2)).append(";"); // 字体颜色
                }
                XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
                if (bgColor != null && !"".equals(bgColor)) {
                    sb.append("background-color:#").append(bgColor.getARGBHex().substring(2)).append(";"); // 背景颜色
                }
                BorderStyle border = cellStyle.getBorderBottomEnum();
                sb.append(getBorderStyle(0,border.getCode(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
                sb.append(getBorderStyle(1,border.getCode(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
                sb.append(getBorderStyle(2,border.getCode(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
                sb.append(getBorderStyle(3,border.getCode(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));
            } else if (wb instanceof HSSFWorkbook) {
                HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
                short fontColor = hf.getColor();
                sb.append("style='");
                HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
                HSSFColor hc = palette.getColor(fontColor);
                sb.append("font-weight:bold;"); // 字体加粗
                sb.append("font-size: ").append(hf.getFontHeight() / 2).append("%;"); // 字体大小
                String fontColorStr = convertToStardColor(hc);
                if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
                    sb.append("color:").append(fontColorStr).append(";"); // 字体颜色
                }
                int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
                sb.append("width:").append(columnWidth).append("px;");
                //sb.append("width:" + stringValue.trim().length() + "%");
                short bgColor = cellStyle.getFillForegroundColor();
                hc = palette.getColor(bgColor);
                String bgColorStr = convertToStardColor(hc);
                if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
                    sb.append("background-color:").append(bgColorStr).append(";"); // 背景颜色
                }
                sb.append( getBorderStyle(palette,0,cellStyle.getBorderTop(),cellStyle.getTopBorderColor()));
                sb.append( getBorderStyle(palette,1,cellStyle.getBorderRight(),cellStyle.getRightBorderColor()));
                sb.append( getBorderStyle(palette,3,cellStyle.getBorderLeft(),cellStyle.getLeftBorderColor()));
                sb.append( getBorderStyle(palette,2,cellStyle.getBorderBottom(),cellStyle.getBottomBorderColor()));
            }
            sb.append("' ");
        }
    }

    /**
     * 单元格内容的水平对齐方式
     *
     * @param alignment
     * @return
     */
    private static String convertAlignToHtml(short alignment) {
        String align = "left";
        if(alignment == HorizontalAlignment.LEFT.getCode()){ align = "left";}
        else if(alignment == HorizontalAlignment.CENTER.getCode()){ align = "center";}
        else if(alignment == HorizontalAlignment.RIGHT.getCode()){ align = "right";}
        return align;
    }

    /**
     * 单元格中内容的垂直排列方式
     *
     * @param verticalAlignment
     * @return
     */
    private static String convertVerticalAlignToHtml(short verticalAlignment) {
        String valign = "middle";
        if(verticalAlignment == VerticalAlignment.BOTTOM.getCode()){valign = "bottom";}
        else if(verticalAlignment == VerticalAlignment.CENTER.getCode()){valign = "center";}
        else if(verticalAlignment == VerticalAlignment.TOP.getCode()){valign = "top";}
        return valign;
    }

    private static String convertToStardColor(HSSFColor hc) {
        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
            }
        }
        return sb.toString();
    }

    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }

    static String[] bordesr = {"border-top:", "border-right:", "border-bottom:", "border-left:"};
    static String[] borderStyles = {"solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ",
            "solid ", "solid", "solid", "solid", "solid", "solid"};

    private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {
        if (s == 0)
            return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        String borderColorStr = convertToStardColor(palette.getColor(t));
        borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
        return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
    }

    private static String getBorderStyle(int b, short s, XSSFColor xc) {
        if (s == 0)
            return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        if (xc != null && !"".equals(xc)) {
            String borderColorStr = xc.getARGBHex();// t.getARGBHex();
            borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000"
                    : borderColorStr.substring(2);
            return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
        }
        return "";
    }

}

