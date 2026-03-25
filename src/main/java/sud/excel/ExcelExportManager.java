package sud.excel;


import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static sud.excel.ExcelExportManager.CellFormat.*;

public class ExcelExportManager {
    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    public static final BigDecimal TEN_THOUSANDS = BigDecimal.valueOf(10_000);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_LONG_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_SHORT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final XSSFWorkbook wb = new XSSFWorkbook();
    private final EnumMap<CellFormat, XSSFCellStyle> styles = new EnumMap<>(CellFormat.class);
    private final Map<String, File> sheets = new HashMap<>();

    private final XSSFDataFormat fmt = wb.createDataFormat();

    private ExcelExportManager() {
    }

    public static ExcelExportManager create() {
        return new ExcelExportManager().addDefaultStyle();
    }

    private static String toString(final Object o, ColumnDefinition columnDefinition) {
        if (o == null) {
            return "";
        }
        if (o instanceof Number) {
            return o.toString().replace(",", ".");
        }
        String result = null;
        if (columnDefinition != null && columnDefinition.getExportColumnSetting() != null && columnDefinition.getExportColumnSetting().getFormat() != null) {
            result = formatAttribute(o, columnDefinition.getExportColumnSetting().getFormat());
        }
        if (result == null) {
            result = o.toString().replace("&", " ");
        }
        return result;
    }

    private static void substitute(File zipFile, Map<String, File> sheets, OutputStream out) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile);
             ZipOutputStream zos = new ZipOutputStream(out)) {

            Enumeration<? extends ZipEntry> en = zip.entries();
            while (en.hasMoreElements()) {
                ZipEntry ze = en.nextElement();
                if (!sheets.containsKey(ze.getName())) {
                    zos.putNextEntry(new ZipEntry(ze.getName()));
                    InputStream is = zip.getInputStream(ze);
                    copyStream(is, zos);
                    is.close();
                }
            }

            for (Map.Entry<String, File> entry : sheets.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                InputStream is = new FileInputStream(entry.getValue());
                copyStream(is, zos);
                is.close();
            }
        }
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] chunk = new byte[1024];
        int count;
        while ((count = in.read(chunk)) >= 0) {
            out.write(chunk, 0, count);
        }
    }

    private void addStyle(CellFormat name, String format) {
        addStyle(name, format, null, null, null, null);
    }

    private void addStyle(CellFormat name, String format, HorizontalAlignment ha, Boolean bold, final IndexedColors ic, final FillPatternType fpt) {
        XSSFCellStyle style = wb.createCellStyle();

        if (format != null) {
            style.setDataFormat(fmt.getFormat(format));
        }

        if (bold != null) {
            XSSFFont font = wb.createFont();
            font.setBold(bold);
            style.setFont(font);
        }

        if (ic != null) {
            style.setFillForegroundColor(ic.getIndex());
        }

        if (fpt != null) {
            style.setFillPattern(fpt);
        }

        if (ha != null) {
            style.setAlignment(ha);
        }
        styles.put(name, style);
    }

    private ExcelExportManager addDefaultStyle() {
        addStyle(CellFormat.HEADER, null, HorizontalAlignment.CENTER, true, IndexedColors.GREY_25_PERCENT, FillPatternType.SOLID_FOREGROUND);
        addStyle(PERCENT, "0.0%");
        addStyle(CellFormat.CURRENCY, "#,##0.00");
        addStyle(RATE, "#,##0.0000");
        addStyle(AMOUNT, "#,##0.00");
        addStyle(DATE, "mmm dd yyyy");
        return this;
    }

    public void addSheet(String name, final Iterator<?> iterator, List<ColumnDefinition> def, int maxRows) throws IOException {
        XSSFSheet sheet = wb.createSheet(name);
        String ref = sheet.getPackagePart().getPartName().getName().substring(1);
        File tmp = File.createTempFile(UUID.randomUUID().toString(), ".xml");
        sheets.put(ref, tmp);
        exportSheet(iterator, def, tmp, maxRows);
    }

    private void exportSheet(final Iterator<?> iterator, List<ColumnDefinition> def, File file, int maxRows) throws IOException {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            SpreadsheetWriter sw = new SpreadsheetWriter(w);
            sw.beginSheet();
            int rownum = 0;

            sw.insertRow(rownum++);
            buildHeader(def, sw);

            while (iterator.hasNext()) {
                if (maxRows > 0 && rownum >= maxRows) {
                    break;
                }
                Object rowObj = iterator.next();
                sw.insertRow(rownum++);
                for (int colDefIndex = 0; colDefIndex < def.size(); colDefIndex++) {
                    buildRow(def, sw, rowObj, colDefIndex);
                }
                sw.endRow();
                if (rownum % 10000 == 0) {
                    System.out.println(rownum + " rows exported");
                }
            }
            sw.endSheet();
        }
    }

    private void buildRow(List<ColumnDefinition> def, SpreadsheetWriter sw, Object rowObj, int colDefIndex) throws IOException {
        ColumnDefinition cd = def.get(colDefIndex);
        int style = -1;
        if (cd.getExportColumnSetting() != null && cd.getExportColumnSetting().getFormat() != null) {
            switch (cd.getExportColumnSetting().getFormat()) {
                case "amount" -> style = styles.get(AMOUNT).getIndex();
                case "rate" -> style = styles.get(RATE).getIndex();
            }
        }
        Object o = cd.get(rowObj);
        sw.createCell(colDefIndex, toString(o, cd), style, o instanceof Number ? SpreadsheetWriter.NUMBER_TYPE : SpreadsheetWriter.INLINE_STRING_TYPE);
    }

    private void buildHeader(List<ColumnDefinition> def, SpreadsheetWriter sw) throws IOException {
        int styleIndex = styles.get(HEADER).getIndex();
        for (int c = 0; c < def.size(); c++) {
            ColumnDefinition d = def.get(c);
            sw.createCell(c, d.getLabelOrName(), styleIndex);
        }
        sw.endRow();
    }

    public void build(final File file) throws IOException {
        File tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        try (FileOutputStream os = new FileOutputStream(tmpFile)) {
            wb.write(os);
        }

        try (FileOutputStream out = new FileOutputStream(file)) {
            substitute(tmpFile, sheets, out);
        }
        try {
            Files.delete(Path.of(tmpFile.getPath()));
            sheets.values().forEach(f -> {
                if (f.exists() && f.isFile()) {
                    try {
                        Files.delete(Path.of(f.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

    enum CellFormat {
        HEADER,
        PERCENT,
        CURRENCY,
        RATE,
        AMOUNT,
        DATE

    }

    public static String formatAttribute(Object value, String format) {
        if (value == null) return "";
        switch (format) {
            case "date" -> {
                return formatDate(value);
            }
            case "datetime" -> {
                return formatDateTime(value);
            }
            case "rate" -> {
                return formatNumberFixRate(value, "%.4f", TEN_THOUSANDS);

            }
            case "amount" -> {
                return formatNumberFixRate(value, "%.2f", ONE_HUNDRED);

            }
            case "boolean" -> {
                if (value instanceof Boolean bool)
                    return bool ? "Да" : "Нет";// NOSONAR
            }
            default -> {
                return value.toString();
            }
        }
        return value.toString();
    }

    private static String formatNumberFixRate(Object value, String format, BigDecimal multiplier) {
        if (value instanceof BigDecimal bdValue) {
            return String.format(format, bdValue);
        }
        return String.format(format, new BigDecimal(value.toString()).multiply(multiplier).divide(multiplier, MathContext.DECIMAL64));
    }

    private static String formatDateTime(Object value) {
        if (value instanceof LocalDate localDate)
            return localDate.format(DATE_TIME_SHORT_FORMAT);
        if (value instanceof LocalDateTime localDateTime)
            return localDateTime.format(DATE_TIME_LONG_FORMAT);
        return null;
    }

    private static String formatDate(Object value) {
        if (value instanceof LocalDate localDate)
            return localDate.format(DATE_FORMAT);
        if (value instanceof LocalDateTime localDateTime)
            return localDateTime.format(DATE_FORMAT);
        return null;
    }

}
