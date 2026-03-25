package sud.excel.old;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Deprecated
public class ExcelHelper {
    public static File createFile(String fileName, boolean isXlsx) {
        File file = null;
        String ext = isXlsx ? ".xlsx" : ".xls";
        try {
            String tmpDir = System.getProperty("java.io.tmpdir"); //File.createTempFile()
            file = new File(tmpDir, fileName + ext);
            if (file.exists()) {
                fileName += "_";
                int count = 2;
                do {
                    file = new File(tmpDir, fileName + count + ext);
                    count++;
                } while (file.exists());
            }
            file.createNewFile();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        return file;
    }

    public static CellStyle createStyleNoBorderNoWrap(Workbook workbook, Font font) {
        return createStyleWithFont(workbook, font, false, false);
    }

    private static CellStyle createStyleWithFont(Workbook workbook, Font font, boolean needWrap, boolean hasBorder) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        if (hasBorder) {
            setThinBorder(style);
        }
//        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        if (needWrap) {
            style.setWrapText(true);
        }
        return style;
    }

    public static CellStyle createStyleWithFont(Workbook workbook, Font font, boolean needWrap) {
        return createStyleWithFont(workbook, font, needWrap, true);
    }

    public static CellStyle createStyleWithFontNoBorder(Workbook workbook, Font font) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
//        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    public static CellStyle createStyleMainData(Workbook workbook, Font font) {
        CellStyle style = createStyleWithFont(workbook, font, false);
//        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
//        style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        return style;
    }

    public static CellStyle createStyleDisabled(Workbook workbook, Font font) {
        XSSFCellStyle disableStyle = (XSSFCellStyle) workbook.createCellStyle();
        disableStyle.setFont(font);
        XSSFColor grey = new XSSFColor();
        disableStyle.setFillForegroundColor(grey);
//        disableStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        setThinBorder(disableStyle);
        return disableStyle;
    }

    public static CellStyle createStyleDisabled(Workbook workbook, Font font, int value) {
        XSSFCellStyle disableStyle = (XSSFCellStyle) workbook.createCellStyle();
        disableStyle.setFont(font);
        XSSFColor grey = new XSSFColor();
        disableStyle.setFillForegroundColor(grey);
//        disableStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        setThinBorder(disableStyle);
        return disableStyle;
    }

    public static Font crateFontHeigh(Sheet sheet, int point) {
        return crateFontHeigh(sheet.getWorkbook(), point);
    }

    public static Font crateFontHeigh(Workbook workbook, int point) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) point);
        font.setFontName("Times New Roman");
        return font;
    }

    public static void setThinBorder(CellStyle style) {
//        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
    }

    public static CellRangeAddress createRangeRowInColumn(int rowNum, int rowShift, int columnNum) {
        return new CellRangeAddress(rowNum, rowNum + rowShift, columnNum, columnNum);
    }

    public static void mergeRowInColumn(Sheet sheet, int rowNum, int rowShift, int columnNum, CellStyle style) {
        mergeRegion(sheet, createRangeRowInColumn(rowNum, rowShift, columnNum), style);
    }

    public static void mergeRegion(Sheet sheet, int rowNumStart, short rowShift,
                                   int columnNumStart, int columnShift,
                                   CellStyle style) {
        mergeRegion(sheet, new CellRangeAddress(rowNumStart, rowNumStart + rowShift - 1,
                columnNumStart, columnNumStart + columnShift - 1), style);
    }

    public static void mergeRegion(Sheet sheet, CellRangeAddress region, CellStyle style) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(style);
            }
        }
        sheet.addMergedRegion(region);
    }

    public static void mergeCellInRow(Sheet sheet, Row row, int columnNum, int columnShift, CellStyle style) {
        int rowNum = row.getRowNum();
        CellRangeAddress region = new CellRangeAddress(rowNum, rowNum, columnNum, columnNum + columnShift - 1);
        mergeCellInRow(sheet, region, style);
    }

    public static void mergeCellInRow(Sheet sheet, CellRangeAddress region, CellStyle style) {
        Row row = sheet.getRow(region.getFirstRow());
        for (int i = region.getFirstColumn(); i <= region.getLastColumn(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
        }
        sheet.addMergedRegion(region);
    }

    @Deprecated
    public static void filterByExecuted(Map<Integer, String> checkErrors, List<Integer> executedCheckNumbers) {
        Iterator<Map.Entry<Integer, String>> it = checkErrors.entrySet().iterator();
        while (it.hasNext()) {
            if (!executedCheckNumbers.contains(it.next().getKey())) {
                it.remove();
            }
        }
    }


    @Deprecated
    public static void setCellValue(Cell cell, Date date) {
        if (date == null)
            return;
        cell.setCellValue(date);
    }

    static public void setCellValue(Cell cell, Number value, boolean emptyForZero) {
        if (value == null || (emptyForZero && value.doubleValue() == 0))
            return;
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
    }

    public static void setCellValueEmptyForZero(Cell cell, Number value) {
        setCellValue(cell, value, true);
    }

    public static Cell createCell(Row row, short currColomnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(currColomnIndex);
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(style);
        return cell;
    }

    public static Cell createCell(Row row, short currColomnIndex, CellStyle style) {
        Cell cell = row.createCell(currColomnIndex);
        cell.setCellStyle(style);
        return cell;
    }

    public static CellStyle setSummFormat(Workbook workbook, CellStyle style, String pattern) {
        XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();
        style.setDataFormat(format.getFormat(pattern));
        return style;
    }

    public static CellStyle createDateFormat(Workbook workbook, CellStyle style) {
        XSSFCreationHelper createHelper = (XSSFCreationHelper) workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        return style;
    }

    private static List<String> toList(String fieldNames) {
        if (fieldNames == null || fieldNames.trim().equals(""))
            return new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (String each : fieldNames.split(",")) {
            result.add(each.trim());
        }
        return result;
    }
}
