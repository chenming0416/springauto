package testcase;

import common.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class testCaseTest2 {

    private static final Logger logger = LoggerFactory.getLogger(testCaseTest2.class.getName());// slf4j记录器
   // 定义运行的操作系统，用在杀浏览器方法中，是调dos还是调shell
    String systemtype = "windows";
    @Test
    public void test2(){
        String methodstr = Thread.currentThread().getStackTrace()[1].getMethodName();
        Base base = new Base();
        WebDriver driver = base.driver;
        driver.get("https://www.baidu.com");
        driver.findElement(By.id("kw")).sendKeys("hello");
        driver.findElement(By.id("su")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("this is slf4j message");
        String aa = driver.findElement(By.xpath("//*[@id='containe']/div[2]/div/div[2]/span")).getText();
        System.out.println(aa);
        System.out.println("testCase222 ");
        base.killBrowser(systemtype);
    }
    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }
}