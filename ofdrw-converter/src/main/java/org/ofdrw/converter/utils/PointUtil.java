package org.ofdrw.converter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ofdrw.converter.point.PathPoint;
import org.ofdrw.converter.point.TextCodePoint;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.graph.pathObj.AbbreviatedData;
import org.ofdrw.core.graph.pathObj.OptVal;
import org.ofdrw.core.text.CT_CGTransform;
import org.ofdrw.core.text.TextCode;
import org.ofdrw.reader.DeltaTool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.ofdrw.converter.utils.CommonUtil.converterDpi;


/**
 * @author dltech21
 * @since 2020/8/11
 */
public class PointUtil {
    private final static Logger logger = LoggerFactory.getLogger(PointUtil.class);

    /**
     * 解析压缩路径为点坐标
     *
     * @param abbreviatedData 压缩路径
     * @return 绘制点坐标序列
     */
    public static List<PathPoint> convertPathAbbreviatedDatatoPoint(String abbreviatedData) {
        // 解析成各个操作符和操作数
        final LinkedList<OptVal> optValArr = AbbreviatedData.parse(abbreviatedData);
        List<PathPoint> pointList = new ArrayList<>();
        for (OptVal optVal : optValArr) {
            double[] array = optVal.expectValues();
            switch (optVal.opt) {
                case "M":
                    pointList.add(new PathPoint("M",
                            (float) array[0], (float) array[1],
                            0, 0,
                            0, 0));
                    break;
                case "L":
                    pointList.add(new PathPoint("L",
                            (float) array[0], (float) array[1],
                            0, 0,
                            0, 0));
                    break;
                case "C":
                    pointList.add(new PathPoint("C", 0, 0, 0, 0, 0, 0));
                    break;
                case "S":
                    pointList.add(new PathPoint("S",
                            (float) array[0], (float) array[1],
                            0, 0,
                            0, 0));
                    break;
                case "B":
                    pointList.add(new PathPoint("B",
                            (float) array[0], (float) array[1],
                            (float) array[2], (float) array[3],
                            (float) array[4], (float) array[5]));
                    break;
                case "Q":
                    pointList.add(new PathPoint("Q",
                            (float) array[0], (float) array[1],
                            (float) array[2], (float) array[3],
                            0, 0));
                    break;
                case "A":
                    pointList.add(new PathPoint("A",
                            (float) array[0], (float) array[1],
                            (float) array[2], (float) array[3],
                            (float) array[4], (float) array[5],
                            (float) array[6]));
                    break;
            }
        }
        return pointList;
    }

    public static List<PathPoint> calPathPoint(List<PathPoint> abbreviatedPoint) {
        List<PathPoint> pointList = new ArrayList<>();
        for (PathPoint point : abbreviatedPoint) {
            if (point.type.equals("M") || point.type.equals("L") || point.type.equals("C") || point.type.equals("S")) {
                double x = 0, y = 0;
                x = point.x1;
                y = point.y1;
                point.x1 = (float) converterDpi(x);
                point.y1 = (float) converterDpi(y);
                pointList.add(point);
            } else if (point.type.equals("B")) {
                double x1 = point.x1, y1 = point.y1;
                double x2 = point.x2, y2 = point.y2;
                double x3 = point.x3, y3 = point.y3;
                PathPoint realPoint = new PathPoint("B", (float) converterDpi(x1), (float) converterDpi(y1),
                        (float) converterDpi(x2), (float) converterDpi(y2),
                        (float) converterDpi(x3), (float) converterDpi(y3));
                pointList.add(realPoint);
            }
        }
        return pointList;
    }

    public static List<TextCodePoint> calTextPoint(List<TextCode> textCodes) {
        double x = 0, y = 0;
        List<TextCodePoint> textCodePointList = new ArrayList<>();
        for (TextCode textCode : textCodes) {
            x = textCode.getX();
            y = textCode.getY();

            Double[] deltaXList = null;
            Double[] deltaYList = null;
            if (textCode.getDeltaX() != null && textCode.getDeltaX().getArray().size() > 0) {
                deltaXList = textCode.getDeltaX().toDouble();
            }
            if (textCode.getDeltaY() != null && textCode.getDeltaY().getArray().size() > 0) {
                deltaYList = textCode.getDeltaY().toDouble();
            }
            for (int i = 0; i < textCode.getText().length(); i++) {
                if (i > 0 && Objects.nonNull(deltaXList)) {
                    x += deltaXList[i - 1];
                }
                if (i > 0 && Objects.nonNull(deltaYList)) {
                    y += deltaYList[i - 1];
                }
                String text = textCode.getText().substring(i, i + 1);
                TextCodePoint textCodePoint = new TextCodePoint(converterDpi(x), converterDpi(y), text);
                textCodePointList.add(textCodePoint);
            }
        }
        return textCodePointList;
    }


    public static double[] adjustPos(double width, double height, double x, double y, ST_Box boundary) {
        double realX = boundary.getTopLeftX() + x;
//        if (realX > (boundary.getTopLeftX() + boundary.getWidth())) {
//            realX = boundary.getTopLeftX() + boundary.getWidth();
//        }
        double realY = boundary.getTopLeftY() + y;
//        if (realY > height) {
//            realY = height;
//        }
        return new double[]{realX, realY};
    }

    public static double[] ctmCalPoint(double x, double y, Double[] ctm) {
        double ctmX = x * ctm[0] + y * ctm[2] + 1 * ctm[4];
        double ctmY = x * ctm[1] + y * ctm[3] + 1 * ctm[5];
        return new double[]{ctmX, ctmY};
    }

    public static List<PathPoint> calPdfPathPoint(double width, double height, ST_Box boundary, List<PathPoint> abbreviatedPoint, boolean hasCtm, ST_Array ctm, ST_Box compositeObjectBoundary, ST_Array compositeObjectCTM, boolean fixOriginToPdf) {
        List<PathPoint> pointList = new ArrayList<>();
        for (PathPoint point : abbreviatedPoint) {
            if (point.type.equals("M") || point.type.equals("L") || point.type.equals("C") || point.type.equals("S")) {
                double x = 0, y = 0;
                x = point.x1;
                y = point.y1;

                if (hasCtm) {
                    double[] newPoint = ctmCalPoint(x, y, ctm.toDouble());
                    x = newPoint[0];
                    y = newPoint[1];
                }
                double[] realPos = adjustPos(width, height, x, y, boundary);
                point.x1 = (float) converterDpi(realPos[0]);
                point.y1 = (float) converterDpi(fixOriginToPdf ? (height - realPos[1]) : realPos[1]);
                if (compositeObjectBoundary != null) {
                    if (compositeObjectCTM != null) {
                        realPos = ctmCalPoint(realPos[0], realPos[1], compositeObjectCTM.toDouble());
                    }
                    realPos = adjustPos(width, height, realPos[0], realPos[1], compositeObjectBoundary);
                    point.x1 = (float) converterDpi(realPos[0]);
                    point.y1 = (float) converterDpi(fixOriginToPdf ? (height - realPos[1]) : realPos[1]);
                }
                pointList.add(point);
            } else if (point.type.equals("B")) {
                double x1 = point.x1, y1 = point.y1;
                double x2 = point.x2, y2 = point.y2;
                double x3 = point.x3, y3 = point.y3;
                if (hasCtm) {
                    double[] newPoint = ctmCalPoint(x1, y1, ctm.toDouble());
                    x1 = newPoint[0];
                    y1 = newPoint[1];
                    newPoint = ctmCalPoint(x2, y2, ctm.toDouble());
                    x2 = newPoint[0];
                    y2 = newPoint[1];
                    newPoint = ctmCalPoint(x3, y3, ctm.toDouble());
                    x3 = newPoint[0];
                    y3 = newPoint[1];
                }
                double[] realPos = adjustPos(width, height, x1, y1, boundary);
                x1 = realPos[0];
                y1 = realPos[1];
                realPos = adjustPos(width, height, x2, y2, boundary);
                x2 = realPos[0];
                y2 = realPos[1];
                realPos = adjustPos(width, height, x3, y3, boundary);
                x3 = realPos[0];
                y3 = realPos[1];
                PathPoint realPoint = new PathPoint("B", (float) converterDpi(x1), (float) converterDpi(fixOriginToPdf ? (height - y1) : y1),
                        (float) converterDpi(x2), (float) converterDpi(fixOriginToPdf ? (height - y2) : y2),
                        (float) converterDpi(x3), (float) converterDpi(fixOriginToPdf ? (height - y3) : y3));
                pointList.add(realPoint);
            } else if (point.type.equals("Q")) {
                double x1 = point.x1, y1 = point.y1;
                double x2 = point.x2, y2 = point.y2;
                if (hasCtm) {
                    double[] newPoint = ctmCalPoint(x1, y1, ctm.toDouble());
                    x1 = newPoint[0];
                    y1 = newPoint[1];
                    newPoint = ctmCalPoint(x2, y2, ctm.toDouble());
                    x2 = newPoint[0];
                    y2 = newPoint[1];
                }
                double[] realPos = adjustPos(width, height, x1, y1, boundary);
                x1 = realPos[0];
                y1 = realPos[1];
                realPos = adjustPos(width, height, x2, y2, boundary);
                x2 = realPos[0];
                y2 = realPos[1];
                PathPoint realPoint = new PathPoint("Q", (float) converterDpi(x1), (float) converterDpi(fixOriginToPdf ? (height - y1) : y1),
                        (float) converterDpi(x2), (float) converterDpi(fixOriginToPdf ? (height - y2) : y2),
                        0, 0);
                pointList.add(realPoint);
            } else if (point.type.equals("A")) {
                double rx = point.rx, ry = point.ry;
                float rotation = point.rotation, arc = point.arc, sweep = point.sweep;
                double x = point.x, y = point.y;
                if (hasCtm) {
                    double[] newPoint = ctmCalPoint(x, y, ctm.toDouble());
                    x = newPoint[0];
                    y = newPoint[1];
                }
                double[] realPos = adjustPos(width, height, x, y, boundary);
                x = realPos[0];
                y = realPos[1];
                PathPoint realPoint = new PathPoint("A", (float) converterDpi(rx), (float) converterDpi(ry),
                        rotation, arc, sweep,
                        (float) converterDpi(x), (float) converterDpi(fixOriginToPdf ? (height - y) : y));
                pointList.add(realPoint);
            }
        }
        return pointList;
    }

    public static List<TextCodePoint> calPdfTextCoordinate(double width, double height, ST_Box boundary, float fontSize, List<TextCode> textCodes, boolean hasCtm, ST_Array ctm, boolean fixOriginToPdf) {
        double x = 0, y = 0;
        List<TextCodePoint> textCodePointList = new ArrayList<>();
        for (TextCode textCode : textCodes) {
            x = textCode.getX() == null ? 0 : textCode.getX();
            y = textCode.getY() == null ? 0 : textCode.getY();
            if (hasCtm) {
                double[] newPoint = ctmCalPoint(x, y, ctm.toDouble());
                x = newPoint[0];
                y = newPoint[1];
            }
            List<Float> deltaXList = null;
            List<Float> deltaYList = null;
            String textStr = textCode.getText();
            if (textCode.getDeltaX() != null && textCode.getDeltaX().getArray().size() > 0) {
                deltaXList = DeltaTool.getDelta(textCode.getDeltaX(), textStr.length());
            }
            if (textCode.getDeltaY() != null && textCode.getDeltaY().getArray().size() > 0) {
                deltaYList = DeltaTool.getDelta(textCode.getDeltaY(), textStr.length());
            }

            textStr = textStr.replaceAll("&lt;", "<");
            textStr = textStr.replaceAll("&gt;", ">");
            textStr = textStr.replaceAll("&amp;", "&");
            textStr = textStr.replaceAll("\n", "");
            textStr = textStr.replaceAll("&nbsp;", " ");
            textStr = textStr.replaceAll("&quot;", "\"");
            textStr = textStr.replaceAll("&copy;", "");
            textStr = textStr.replaceAll("&apos;", "'");
            for (int i = 0; i < textStr.length(); i++) {
                float decent = 0;

//                if (textCode.getY() > 0) {
//                    decent = fontSize - textCode.getY().floatValue();
//                }
                if (i > 0 && Objects.nonNull(deltaXList)) {
                    if (hasCtm) {
                        Double[] ctms = ctm.toDouble();
                        double a = ctms[0].doubleValue();
                        double b = ctms[1].doubleValue();
                        double c = ctms[2].doubleValue();
                        double d = ctms[3].doubleValue();
                        double e = ctms[4].doubleValue();
                        double f = ctms[5].doubleValue();
                        double angel = Math.atan2(-b, d);
                        if (angel == 0) {
                            double[] newPoint = ctmCalPoint(deltaXList.get(i - 1), 0, ctm.toDouble());
                            x += newPoint[0];
                        } else {
                            x += deltaXList.get(i - 1);
                        }
                    } else {
                        x += deltaXList.get(i - 1);
                    }
                }
                if (i > 0 && Objects.nonNull(deltaYList)) {
                    if (hasCtm) {
                        Double[] ctms = ctm.toDouble();
                        double a = ctms[0].doubleValue();
                        double b = ctms[1].doubleValue();
                        double c = ctms[2].doubleValue();
                        double d = ctms[3].doubleValue();
                        double e = ctms[4].doubleValue();
                        double f = ctms[5].doubleValue();
                        double angel = Math.atan2(-b, d);
                        if (angel == 0) {
                            double[] newPoint = ctmCalPoint(0, deltaYList.get(i - 1), ctm.toDouble());
                            y += newPoint[1];
                        } else {
                            y += deltaYList.get(i - 1);
                        }
                    } else {
                        y += deltaYList.get(i - 1);
                    }
                }
                double[] realPos = adjustPos(width, height, x, y, boundary);
                String text = textStr.substring(i, i + 1);
                TextCodePoint textCodePoint = new TextCodePoint(converterDpi(realPos[0]), converterDpi(fixOriginToPdf ? (height - realPos[1]) : realPos[1]), text);
                textCodePointList.add(textCodePoint);
            }
        }
        return textCodePointList;
    }

    public static List<TextCodePoint> calPdfTextCoordinate(double width, double height, ST_Box boundary, float fontSize, List<TextCode> textCodes, List<CT_CGTransform> cgTransforms, ST_Box compositeObjectBoundary, ST_Array compositeObjectCTM, boolean hasCtm, ST_Array ctm, boolean fixOriginToPdf) {
        double x = 0, y = 0;
        List<TextCodePoint> textCodePointList = new ArrayList<>();
        for (TextCode textCode : textCodes) {
            x = textCode.getX() == null ? 0 : textCode.getX();
            y = textCode.getY() == null ? 0 : textCode.getY();

            if (hasCtm) {
                double[] newPoint = ctmCalPoint(x, y, ctm.toDouble());
                x = newPoint[0];
                y = newPoint[1];
            }
            List<String> deltaXList = null;
            List<String> deltaYList = null;
            String textStr = textCode.getText();
            if (textCode.getDeltaX() != null && textCode.getDeltaX().getArray().size() > 0) {
                deltaXList = textCode.getDeltaX().getArray();
            }
            if (textCode.getDeltaY() != null && textCode.getDeltaY().getArray().size() > 0) {
                deltaYList = textCode.getDeltaY().getArray();
            }

            textStr = textStr.replaceAll("&lt;", "<");
            textStr = textStr.replaceAll("&gt;", ">");
            textStr = textStr.replaceAll("&amp;", "&");
            textStr = textStr.replaceAll("\n", "");
            textStr = textStr.replaceAll("&nbsp;", " ");
            textStr = textStr.replaceAll("&quot;", "\"");
            textStr = textStr.replaceAll("&copy;", "");
            textStr = textStr.replaceAll("&apos;", "'");

            int skipCount = 0;
            for (int i = 0; i < textStr.length(); i++) {
                String text = textStr.substring(i, i + 1);
                boolean skipPosition = false;
                if (i > 0 && Objects.nonNull(deltaXList)) {
                    for (CT_CGTransform cgTransform : cgTransforms) {
                        int pos = cgTransform.getCodePosition();
                        int codeCount = cgTransform.getCodeCount();
                        if (i > pos && i < (pos + codeCount)) {
                            if (cgTransform.getGlyphs().size() < codeCount) {
                                skipPosition = true;

                                skipCount++;
                            }
                            break;
                        }
                    }
                    int index = i - 1;
//                    boolean keepSameDX = false;
                    if (index >= deltaXList.size()) {
                        index = deltaXList.size() - 1;
//                        keepSameDX = true;
                    }
                    double dx = Double.parseDouble(deltaXList.get(index));
                    if (dx != 0) {
                        if (hasCtm) {
                            Double[] ctms = ctm.toDouble();
                            double a = ctms[0].doubleValue();
                            double b = ctms[1].doubleValue();
                            double c = ctms[2].doubleValue();
                            double d = ctms[3].doubleValue();
                            double e = ctms[4].doubleValue();
                            double f = ctms[5].doubleValue();
                            double angel = Math.atan2(-b, d);
                            if (angel == 0) {
                                double[] newPoint = ctmCalPoint(dx, 0, ctm.toDouble());
                                dx = newPoint[0];
                            } else {
                                if (a == 0 && d == 0) {
                                    dx = dx * fontSize;
                                }
                            }
                        } else {
                            if (skipPosition) {
                                deltaXList.add(i - 1, "0");
                            }
                        }
                    }
//                    x += keepSameDX ? dx : !skipPosition ? dx : 0;
                    x += !skipPosition ? dx : 0;
                }
                if (i > 0 && Objects.nonNull(deltaYList)) {
                    int index = i - 1;
//                    boolean keepSameDY = false;
                    if ((i - 1) >= deltaYList.size()) {
                        index = deltaYList.size() - 1;
//                        keepSameDY = true;
                    }
                    double dy = Double.parseDouble(deltaYList.get(index));
                    if (dy != 0) {
                        if (hasCtm) {
                            Double[] ctms = ctm.toDouble();
                            double a = ctms[0].doubleValue();
                            double b = ctms[1].doubleValue();
                            double c = ctms[2].doubleValue();
                            double d = ctms[3].doubleValue();
                            double e = ctms[4].doubleValue();
                            double f = ctms[5].doubleValue();
                            double angel = Math.atan2(-b, d);
                            if (angel == 0) {
                                double[] newPoint = ctmCalPoint(0, dy, ctm.toDouble());
                                dy = newPoint[1];
                            } else {
                                if (a == 0 && d == 0) {
                                    dy = dy * fontSize;
                                }
                            }
                        }
                    }
//                    y += keepSameDY ? 0 : dy;
                    y += dy;
                }
                double[] realPos = adjustPos(width, height, x, y, boundary);
                if (compositeObjectCTM != null) {
                    realPos = ctmCalPoint(realPos[0], realPos[1], compositeObjectCTM.toDouble());
                }
                if (skipPosition) {
                    text = "";
                }
                TextCodePoint textCodePoint = new TextCodePoint(converterDpi(realPos[0]), converterDpi(fixOriginToPdf ? (height - realPos[1]) : realPos[1]), text);
                textCodePointList.add(textCodePoint);
            }
        }//todo 先按textcode有值
        if (textCodePointList.size() > 0) {
            for (CT_CGTransform cgTransform : cgTransforms) {
                int pos = cgTransform.getCodePosition();
                int glyphCount = cgTransform.getGlyphCount();
                int codeCount = cgTransform.getCodeCount();
                for (int i = pos; i < glyphCount + pos; i++) {
                    if (textCodePointList.size() <= i) {
                        String glyphs = textCodePointList.get(textCodePointList.size() - 1).getGlyph() + " " + cgTransform.getGlyphs().getArray().get(i - pos);
                        textCodePointList.get(textCodePointList.size() - 1).setGlyph(glyphs);
                    } else {
                        textCodePointList.get(i).setGlyph(cgTransform.getGlyphs().getArray().get(i - pos));
                    }
                }
            }
        }
        return textCodePointList;
    }
}
