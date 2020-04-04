package common;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Base {

	public static final String locatorfile = "src/test/resources/locator.xml";
	public static final String casedatafile = "src/test/resources/data.xml";
	public static final String caseDataExcel = "src/test/resources/casedata.xlsx";
	public static final String chromedriverfile = "src/test/resources/chromedriver.exe";
	public static final String savescreenshot = "src/test/out/";
	// slf4j日志记录器
	private static final Logger log = LoggerFactory.getLogger(Base.class);
	public  WebDriver driver = getSingleDriver();

	/*
	 * 执行完后，任务管理器中会有浏览器进程和驱动的进程，直接杀掉
	 * 需要传systemtype:windows，linux
	 */
	public  void killBrowser(String systemtype){
		 Runtime runtime = Runtime.getRuntime();
		  try {
		  	if (systemtype.contains("win")) {
				runtime.exec("tskill chrome");
				runtime.exec("tskill chromedriver");
				System.out.println("this is windwos");
			}else {
				runtime.exec("tskill chrome");
				runtime.exec("tskill chromedriver");
				System.out.println("this is linux");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 *  取得浏览器对象
	 */
	private  WebDriver getSingleDriver(){
		System.setProperty("webdriver.chrome.driver",chromedriverfile);
		//创建无Chrome无头参数
		ChromeOptions chromeOptions=new ChromeOptions();
		chromeOptions.addArguments("-headless");
		// 创建Drive实例，无头浏览器
		WebDriver noheaddriver = new ChromeDriver(chromeOptions);
		// 有头浏览器
		//	WebDriver headdriver = new ChromeDriver();
		// 注册监听器
		WebDriver driver = new EventFiringWebDriver(noheaddriver).register(new EventListenerLog());
		// 隐式等待8秒钟，超时定位元素失败，是全局变量
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		return driver;
	}

	/*
	 * 从xml中得到定位符，是id^kw形式
	 */
	public String getLocator(String method, String locatordiscribename) {
		String locator = null;
		// 创建SAXReader
		SAXReader reader = new SAXReader();
		// 读入xml文件
		File readFile = new File(locatorfile);
		Document document = null;
		try {
			document = reader.read(readFile);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 得到根节点
		Element node = document.getRootElement();
		// 得到方法节点
		Element methodnode = getChildElement(node, method);
		// 得到定位符节点
		Element childnode = getChildElement(methodnode, locatordiscribename);
		// 得到定位符，字符串形式：id^kw
		locator = childnode.getText();
		return locator;
	}

	/*
	 * 传一个节点元素和此节点的子节点节点名，得到子节点元素
	 */
	public Element getChildElement(Element node, String childname) {
		Element childelement = null;
		Iterator<Element> it = node.elementIterator();
		while (it.hasNext()) {
			Element temp = it.next();
			if (temp.getName().equals(childname)) {
				childelement = temp;
			}
		}
		return childelement;
	}

	/*
	 * 通过xpath得到By对象（xpath是id^kw形式）
	 */
	public By splitlocator(String locator) {
		By by = null;
		// ^ 切分
		String arr[] = locator.split("\\^");
		// 定位
		if (arr[0].equals("id")) {
			by = By.id(arr[1]);
		} else if (arr[0].equals("name")) {
			by = By.name(arr[1]);
		} else if (arr[0].equals("linktext")) {
			by = By.linkText(arr[1]);
		} else if (arr[0].equals("xpath")) {
			by = By.xpath(arr[1]);
		} else if (arr[0].equals("classname")) {
			by = By.className(arr[1]);
		} else if (arr[0].equals("tagname")) {
			by = By.tagName(arr[1]);
		} else if (arr[0].equals("partiallinktext")) {
			by = By.partialLinkText(arr[1]);
		} else if (arr[0].equals("cssselector")) {
			by = By.cssSelector(arr[1]);
		}
		return by;
	}

	/*
	 * 传入一个xpath（id^kw），返回一个元素对象
	 */
	public WebElement getElement(String locator) {
		By by = splitlocator(locator);
		WebElement wb = driver.findElement(by);
		return wb;
	}

	/*
	 * 等待，单位是秒
	 */
	public void setSleep(long second){
		try {
			Thread.sleep(1000*second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 传一个excel文件，得到此文件的workbook
	 */
	public Workbook getWorkbook() {
		Workbook workbook = null;
		File xlsFile = new File(caseDataExcel);
		try {
			workbook = WorkbookFactory.create(xlsFile);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
	}

	/*
	 * 读工作簿的第一个sheet，即目录页，得到具体用例的sheet名称字符串
	 */
	public String getSheetname(Workbook workbook, String method) {
		String casesheetstr = null;
		// 取第一个sheet，确认是用例列表，在用例列表列表中定位测试用例的sheet名称
		Sheet caselistsheet = workbook.getSheetAt(0);
		String caselistsheetname = caselistsheet.getSheetName();
		if (("caselist").equals(caselistsheetname)) {
			int rows = caselistsheet.getLastRowNum() + 1;
			for (int row = 1; row < rows; row++) {
				Row r = caselistsheet.getRow(row);
				String methodname = r.getCell(1).getStringCellValue();
				String caseidsheet = r.getCell(0).getStringCellValue();
				if (methodname.equals(method)) {
					casesheetstr = caseidsheet;
					break;
				}
			}
		}
		return casesheetstr;
	}

	/*
	 * 用通过先获取用例sheet的名称字符串，再得到用例的sheet对象
	 */
	public Sheet getCaseSheet(Workbook workbook,String method) {
		String casesheetstr = null;
		Sheet casesheet = null;
		int sheetCount = workbook.getNumberOfSheets();
		Sheet caselistsheet = workbook.getSheetAt(0);
		String caselistsheetname = caselistsheet.getSheetName();
		if (("caselist").equals(caselistsheetname)) {
			int rows = caselistsheet.getLastRowNum() + 1;
			for (int row = 1; row < rows; row++) {
				Row r = caselistsheet.getRow(row);
				String methodname = r.getCell(1).getStringCellValue();
				String caseidsheet = r.getCell(0).getStringCellValue();
				if (methodname.equals(method)) {
					casesheetstr = caseidsheet;
					break;
				}
			}
		}

		for (int i = 0; i < sheetCount; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getSheetName().equals(casesheetstr)) {
				casesheet = sheet;
			}
		}
		return casesheet;
	}

	/*
	 * 把sheet中的数据放入到一个list中，list的元素是map，是一个步骤对应一个map。
	 */
	public List getCaseListFromSheet(Sheet casesheet) {
		List<Map> testcaseobj = new ArrayList<Map>();

		for(int number=3; number<=casesheet.getLastRowNum(); number+=2){
			Map<String,String> casestepmap = new HashMap<String,String>();
			Row titlerow = casesheet.getRow(number);
			Row datarow = casesheet.getRow(number + 1);
			int cols = titlerow.getPhysicalNumberOfCells();
			for (int col = 0; col < cols; col++) {
				log.info("111111111111111111111111");
				log.info("这是第{}条记录", col);
				log.info("222222222222222222222222222");
				//读取数据前设置单元格类型
				titlerow.getCell(col).setCellType(CellType.STRING);
				String keytemp = titlerow.getCell(col).getStringCellValue();
				//读取数据前设置单元格类型
				datarow.getCell(col).setCellType(CellType.STRING);
				String valuetemp = datarow.getCell(col).getStringCellValue();
				casestepmap.put(keytemp, valuetemp);
			}

			testcaseobj.add(casestepmap);
		}

		return testcaseobj;
	}

	/*
	 * 把单个用例的数据从excel放入到list中，list里元素是map
	 */
	public List getTestCaseFromExcel(String method) {
		Workbook workbook = getWorkbook();
		Sheet casesheet = this.getCaseSheet(workbook, method);
		List caselist = this.getCaseListFromSheet(casesheet);
		return caselist;

	}

	/*
	 * 截图
	 */
	public static void saveScreenshot(WebDriver driver,String methodname){
		 File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//利用FileUtils工具类的copyFile()方法保存getScreenshotAs()返回的文件对象。
		 try {
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			 // 日期时间转字符串
			 LocalDateTime time = LocalDateTime.now();
			 String timestr = time.format(formatter);
			 String pngpathstr = savescreenshot+methodname+timestr+".png";
			 FileUtils.copyFile(srcFile, new File(pngpathstr));
			 log.debug(pngpathstr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
