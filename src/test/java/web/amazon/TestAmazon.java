package web.amazon;

import org.testng.ITestNGMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import core.DriverBase;
import listeners.TestListener;
import web.pages.AmazonPage;
import utils.CommonUtils;
import utils.Constants;

@Listeners(TestListener.class)
public class TestAmazon extends DriverBase{

	AmazonPage amazon;
	String workbook = "amazonTestData.xls";
	String sheet = "productSearch";
	private static final String TESTPATH = Constants.TEST_DATA_AMAZON_PATH;
	
	@BeforeClass
	public void init(){
		amazon = new AmazonPage(driver);
	}
	
	/* ************************************Data Provider**********************************************/
	
	@DataProvider(name = "testSearchProduct")
	public Object[][] testCreateExportListJob(ITestNGMethod method) {
		Object[][] params = CommonUtils.getExcelInputData(method,  TESTPATH + workbook, sheet, "testSearchItem");
		return (params);
	}
	
	/* ************************************ Test Methods **********************************************/
	@Test(enabled = true, dataProvider = "testSearchProduct")
	public void searchItem(String itemName){
		amazon.searchItem(itemName);
	}
	
	
	@AfterClass()
	public void tearDown() {

	}
}
