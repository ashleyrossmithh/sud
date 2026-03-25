package sud.excel;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Objects;

public class SpreadsheetWriter {

    public static final String INLINE_STRING_TYPE = "inlineStr";
    public static final String NUMBER_TYPE = "n";

    private final Writer _out;
    private int _rownum;

    public SpreadsheetWriter(Writer out) {
        _out = out;
    }

    public void beginSheet() throws IOException {
        _out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        _out.write("<sheetData>\n");
    }

    public void endSheet() throws IOException {
        _out.write("</sheetData>");
        _out.write("</worksheet>");
    }

    /**
     * Insert a new row
     *
     * @param rownum 0-based row number
     */
    public void insertRow(int rownum) throws IOException {
        _out.write("<row r=\"" + (rownum + 1) + "\">\n");
        this._rownum = rownum;
    }

    /**
     * Insert row end marker
     */
    public void endRow() throws IOException {
        _out.write("</row>\n");
    }

    public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
        createCell(columnIndex, value, styleIndex, INLINE_STRING_TYPE);
    }

    public void createCell(int columnIndex, String value, int styleIndex, String type) throws IOException {
        String ref = new CellReference(_rownum, columnIndex).formatAsString();
        _out.write("<c r=\"" + ref + "\"");
        if (type != null) _out.write(" t=\"" + type + "\"");
        if (styleIndex != -1) _out.write(" s=\"" + styleIndex + "\"");
        _out.write(">");
        if (type != null && type.equals(NUMBER_TYPE)) {
            _out.write("<v>" + value + "</v>");
        } else {
            _out.write("<is><t>" + EscapeUtil.escape(value) + "</t></is>");
        }
        _out.write("</c>");
    }

    public void createCell(int columnIndex, String value) throws IOException {
        createCell(columnIndex, value, -1);
    }

    public void createCell(int columnIndex, Number value, int styleIndex) throws IOException {
        String v = Objects.toString(value);
        if (v != null) {
            v = v.replaceAll(",", ".");
        }
        createCell(columnIndex, v, styleIndex, NUMBER_TYPE);
    }

    public void createCell(int columnIndex, Number value) throws IOException {
        createCell(columnIndex, value, -1);
    }

    public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
        createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
    }
}
