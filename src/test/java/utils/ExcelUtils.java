package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    private String filePath;
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath){
        this.filePath = filePath;
        loadWorkBook();
    }

    private void loadWorkBook() {
        try{
            FileInputStream fis = new FileInputStream(filePath);
            this.workbook = new XSSFWorkbook(fis);
            fis.close();
        } catch(IOException e){
            throw new RuntimeException("Failed");

        }
    }

    public void setSheet(String sheetName){
        sheet = workbook.getSheet(sheetName);
        if(sheet == null){
            throw new RuntimeException("sheet" + sheetName);
        }
    }

    public int getRowCount(){
        return sheet.getLastRowNum() + 1;
    }

    public int getColumnCount(){
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            return 0;
        }
        return firstRow.getLastCellNum();
    }
    public String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return "";
        }

        Cell cell = row.getCell(colNum);
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    public Object[][] getDataAsObjectArray(String sheetName, boolean includeHeader) {
        setSheet(sheetName);
        int startRow = includeHeader ? 1 : 0;
        int totalRows = getRowCount() - startRow;
        int totalCols = getColumnCount();

        if (totalRows <= 0) {
            return new Object[0][0];
        }

        Object[][] data = new Object[totalRows][totalCols];

        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                data[i][j] = getCellData(i + startRow, j);
            }
        }

        return data;
    }
    public static Object[][] getExcelData(String filePath, String sheetName, boolean includeHeader) {
        ExcelUtils excelUtils = new ExcelUtils(filePath);
        Object[][] data = excelUtils.getDataAsObjectArray(sheetName, includeHeader);
        excelUtils.closeWorkbook();
        return data;
    }
    public void closeWorkbook() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing workbook: " + e.getMessage());
        }
    }
}
