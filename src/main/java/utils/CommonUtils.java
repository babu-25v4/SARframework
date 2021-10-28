package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import org.testng.ITestNGMethod;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class CommonUtils {

//	protected static String projectLocation;
	private static Properties bundle;

	public static void loadProperties() {		
		try {
			String basePath = getProjectBasePath();
			System.out.println("basepath: "+basePath);
//			projectLocation = basePath;
			File file = new File(basePath+"\\SeleniumAppiumDemo\\config\\test.properties");
			FileInputStream fis = new FileInputStream(file);
			bundle = new Properties();
			bundle.load(fis);
			Report.info("test.properties loaded successfully...");
		}catch(Exception e) {
			Report.fail("test.properties file is not found. Hence could not load properties."+e);
		}				
	}	
	
	
	public static String getProperty(String key) {
		return bundle.getProperty(key);
	}
	
	public static String getProjectBasePath() {
		String basePath = null;
		File directory = new File("..");
		try {
			basePath = directory.getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return basePath;
	}

	
	public static Object[][] getExcelInputData(ITestNGMethod method, String bookName, String sheetName, String testName) {

		int tdepth = 0;
		String tWorkBook = null;
		String tSheetName = null;
		String testPath = bookName.substring(0, bookName.lastIndexOf(File.separator))+File.separator;
				
		Map<String, String> testparams = method.findMethodParameters(method.getXmlTest());
		boolean keyFound = testparams.containsKey("testdepth");
		boolean tBookFound = testparams.containsKey("Workbook");
		boolean tSheetFound = testparams.containsKey("Sheet");
		if(tBookFound && tSheetFound){
			tWorkBook = testPath + testparams.get("Workbook");
			tSheetName = testparams.get("Sheet");
		}else{
			tWorkBook = bookName;
			tSheetName = sheetName;
		}
		
		if (keyFound) {
			tdepth = Integer.parseInt(testparams.get("testdepth"));
			Report.info("TestDepth for the Method {" + method.getMethodName() + "} - " + tdepth);
		}
		
		System.out.println("BookName: " + tWorkBook);
		System.out.println("SheetName: " + tSheetName);
		int testDepth = Integer.parseInt(getProperty("testDepth"));

		// dTestDepth parameter from -D parameters
		String dTestDepth = System.getProperty("testdepth");
		if (dTestDepth != null && !dTestDepth.isEmpty()) {
			testDepth = Integer.parseInt(dTestDepth.trim());
		}
		Report.info("testdepth is: "+testDepth);
		return getExcelInputDataArrayBasedOnDepth(tWorkBook, tSheetName, testName, testDepth);
	}

	public static Object[][] getExcelInputDataArrayBasedOnDepth(String bookName, String sheetName, String testName, int testDepth) {

		Object[][] tabArray;
		int startRow, startCol, endRow = 0, endCol = 0, ci, cj;
		int k = 0;
		Workbook wb = null;
		WorkbookSettings wbs = new WorkbookSettings();
		wbs.setSuppressWarnings(true);

		try {
			Report.log("testDepth:" + testDepth);
			wb = Workbook.getWorkbook(new File(bookName), wbs);
			Sheet sheet = wb.getSheet(sheetName.trim());
			Cell tableStart = sheet.findCell(testName.trim() + " Start");

			startRow = tableStart.getRow();
			startCol = tableStart.getColumn();

			Cell tableEnd = sheet.findCell(testName.trim() + " End");
			if (tableEnd != null) {
				endRow = tableEnd.getRow();
				endCol = tableEnd.getColumn();
			} else {
				Report.error(testName.trim() + " End is not found in " + sheetName + " in " + bookName);
			}

			int sizeRow = endRow - startRow - 1;
			int sizeCol = endCol - startCol - 1;

			if (sizeRow > testDepth)
				sizeRow = testDepth;

			tabArray = new Object[sizeRow][sizeCol];
			ci = 0;

			for (int i = startRow + 1; i < endRow; i++, ci++) {
				if (k == testDepth)
					break;
				cj = 0;
				for (int j = startCol + 1; j < endCol; j++, cj++) {
					tabArray[ci][cj] = sheet.getCell(j, i).getContents().trim();
				}
				k++;
			}
		} catch (Exception e) {
			tabArray = new Object[5][5];
			e.printStackTrace();
		} finally {
			wb.close();
		}
		return tabArray;
	}

	
}
