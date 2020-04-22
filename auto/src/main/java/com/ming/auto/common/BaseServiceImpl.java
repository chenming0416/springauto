package com.ming.auto.common;

import com.ming.auto.testlistener.EventListenerLog;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenming
 * @create 2020/4/19 9:41
 */

@Component
public class BaseServiceImpl implements BaseService {

	@Autowired
	private SendMailsUtil sendMailsUtil;
	@Value("${mail.smtpserver}")
	private String smtpserver;
	@Value("${mail.smtpport}")
	private String smtpport;
	@Value("${mail.account}")
	private String account;
	@Value("${mail.pwd}")
	private String pwd;
	@Value("${mail.addresser}")
	private String addresser;
	@Value("${mail.targetaddr}")
	private String targetaddr;
	@Value("${constantstr.locatorfilepath}")
	private String locatorfilepath;
	@Value("${constantstr.caseDataExcelpath}")
	private String caseDataExcelpath;
	@Value("${constantstr.chromedriverfilepath}")
	private String chromedriverfilepath;
	@Value("${constantstr.savescreenshotpath}")
	private String savescreenshotpath;
	@Value("${constantstr.systemtype}")
	private String systemtype;

	public String getSystemtype(){
		return systemtype;
	}

	public void getPathStr(){
		logger.error("locatorfilepath:"+locatorfilepath);
		logger.error("caseDataExcelpath:"+caseDataExcelpath);
		logger.error("chromedriverfilepath:"+chromedriverfilepath);
		logger.error("savescreenshotpath:"+savescreenshotpath);
		logger.error("systemtype:"+systemtype);
	}

	// slf4j日志记录器
	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
	public static WebDriver webDriver = null;
	// 定义个静态的方法名字符串，用来各个文件之间传递
	public static String methodnamestr = null;


	/*
	 * 获得driver接口
	 */
	public WebDriver getDriver(String browsername){
		if (null == webDriver) {
			webDriver = getSingleDriver(browsername);
		}
		return webDriver;
	}

	/*
	 * 清除driver对象接口
	 */
	public void cleanDriver(){
		webDriver = null;
	}

	/*
	 * 执行完后，任务管理器中会有浏览器进程和驱动的进程，直接杀掉
	 * 需要传systemtype:windows，linux
	 */
	public  void killBrowser(String systemtype){
//		 cleanDriver();
		 Runtime runtime = Runtime.getRuntime();
		  try {
		  	if (systemtype.contains("win")) {
		  		synchronized (this) {
					runtime.exec("tskill chrome");
					runtime.exec("tskill chromedriver");
				}
			}else {
		  		synchronized (this) {
					runtime.exec("tskill chrome");
					runtime.exec("tskill chromedriver");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 *  取得浏览器对象
	 */
	private  WebDriver getSingleDriver(String browsername){
		WebDriver drivertemp = null;
		if (browsername.contains("ch")) {
			System.setProperty("webdriver.chrome.driver", chromedriverfilepath);
			//创建无Chrome无头参数
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("-headless");
			// 创建Drive实例，无头浏览器
		//	drivertemp = new ChromeDriver(chromeOptions);
			// 有头浏览器
			drivertemp = new ChromeDriver();
		}
		// 注册监听器
		WebDriver driver = new EventFiringWebDriver(drivertemp).register(new EventListenerLog());
		// 隐式等待8秒钟，超时定位元素失败，是全局变量
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		return driver;
	}

	/*
	 * 传方法名和描述文字，得到id^kw
	 * 在把id^kw转换成element对象
	 */
	public By getBy(String method, String locatordiscribename){
		String locator = getLocator(method,locatordiscribename);
		By by = splitlocator(locator);
		return by;

	}

	/*
	 * 从xml中得到定位符，是id^kw形式
	 */
	public String  getLocator(String method, String locatordiscribename) {
		String locator = null;
		// 创建SAXReader
		SAXReader reader = new SAXReader();
		// 读入xml文件
		File readFile = new File(locatorfilepath);
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
		File xlsFile = new File(caseDataExcelpath);
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
	 * 用通过先获取用例sheet的名称字符串，再得到用例的sheet对象
	 */
	public Sheet getCaseSheet(Workbook workbook, String method) {
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
				// log.info("这是第{}条记录", col);
				//读取标题前设置单元格类型
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
	 * 把单个用例的数据从excel放入到list中，list里元素是map,一个map就是一个步骤
	 */
	public String getTestCaseFromExcel(String method,String stepid,String titlename) {
		String stepdatastr = null;
		Workbook workbook = getWorkbook();
		Sheet casesheet = this.getCaseSheet(workbook, method);
		List caselist = this.getCaseListFromSheet(casesheet);
		stepdatastr = this.getTestCaseStepData(caselist,stepid,titlename);
		return stepdatastr;
	}

	/*
	 *list是一个用例，list元素是map，一个map是一个步骤
	 * stepid   String 第一步
	 * titlename   String 数据的标题
	 * list Map<String,String> 整个用例的list数据
	 */
	public String getTestCaseStepData(List<Map<String,String>> list,String stepid,String titlename){
		String stepdatastr = null;
		for (Map<String,String> map:list ) {  // 取出每一步的map
			if(stepid.equals(map.get("step"))){  // 定位到第几步
				stepdatastr = map.get(titlename);
			}
		}
		return stepdatastr;
	}

	/*
	 * 截图
	 */
	public  void saveScreenshot(WebDriver driver, String methodname){

 		 File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//利用FileUtils工具类的copyFile()方法保存getScreenshotAs()返回的文件对象。
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			 // 日期时间转字符串
			 LocalDateTime time = LocalDateTime.now();
			 String timestr = time.format(formatter);
			 String pngpathstr = savescreenshotpath+methodname+timestr+".png";
		try {
			FileUtils.copyFile(srcFile, new File(pngpathstr));
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.error("测试用例【"+ BaseServiceImpl.methodnamestr+"】失败时的截图是："+savescreenshotpath+methodname+timestr+".png");
			 killBrowser(systemtype);

	}

	/*
	 * 发邮件
	 * 参数需要确认
	 */
	public  void sendMail(String mailtitle,String mailcontent)  {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", smtpserver); // 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.smtp.port", smtpport);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
		props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl

		try {
			// 根据邮件配置创建会话，注意session别导错包
			Session session = Session.getDefaultInstance(props);
			// 开启debug模式，可以看到更多详细的输入日志
			session.setDebug(true);
			//创建邮件
			MimeMessage message = null;
			message = sendMailsUtil.createEmail(session,mailtitle,mailcontent,targetaddr,account,addresser);

			//获取传输通道
			Transport transport = null;
			transport = session.getTransport();

			transport.connect(smtpserver,account, pwd);
			//连接，并发送邮件
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			logger.error("发邮件报异常了");
			e.printStackTrace();
		}


	}


}
