package MavenFramework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;
	private static Row row;
	private static Cell cell;
	static FileInputStream fis;

	public static String getCellData(int colNum) { // Should I make this generic???
		cell = row.getCell(colNum);
		if (cell == null || cell.getCellType() == CellType.BLANK)
			return null;
		// cell.setCellType(CellType.STRING);
		DataFormatter df = new DataFormatter();
		return df.formatCellValue(cell);// cell.getStringCellValue();
	}

	public static String getCellData(String columnName) { // Should I make this generic???
		return getCellData(getColNum(columnName));
	}

	public static int numberOfRows() {
		return sheet.getPhysicalNumberOfRows();
	}

	public static void setExcelSheet(String fileName, String sheetName) throws IOException {
		fis = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(fis);

		int sheetCount = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
				sheet = workbook.getSheetAt(i);
				break;
			}
		}
	}

	public static void setRow(int rowNum) {
		row = sheet.getRow(rowNum);
	}

	public static int getColNum(String columnName) {
		Row firstRow = sheet.getRow(0);
		Iterator<Cell> cells = firstRow.cellIterator();
		int colNum = 0;
		while (cells.hasNext()) {
			Cell currentColumnName = cells.next();
			if (currentColumnName.getStringCellValue().equalsIgnoreCase(columnName))
				break;
			colNum++;
		}
		return colNum;
	}
}
