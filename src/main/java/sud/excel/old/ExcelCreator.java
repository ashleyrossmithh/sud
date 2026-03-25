package sud.excel.old;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class ExcelCreator {

    public static void saveControlSheet(Workbook workbook, List<String> rows,
                                        String controlSheetName) {
        Sheet sheet = workbook.createSheet(controlSheetName);
        int sheetIndex = workbook.getSheetIndex(sheet);
//        workbook.setSheetHidden(sheetIndex, true);
        for (int i = 0; i < rows.size(); i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(rows.get(i));
        }
    }

    public static void finish(File fileXlsx, XSSFWorkbook xssfWorkbook) throws IOException {
        FileOutputStream out = new FileOutputStream(fileXlsx);
        xssfWorkbook.write(out);
        out.close();
    }


    public static void main(String[] args) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Font font12 = ExcelHelper.crateFontHeigh(workbook, 12);
            saveControlSheet(workbook, Arrays.asList("dddd", "fffff"), "вввывыв");
            //File f = ExcelHelper.createFile("список", true);
            finish(new File("C:\\test", "my.xlsx"), workbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
