package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class testCaseTest2 {

    @Test
    public void test2(){
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        //创建无Chrome无头参数
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.addArguments("-headless");

        //创建Drive实例
        WebDriver driver = new ChromeDriver(chromeOptions);
        //WebDriver driver = new ChromeDriver();
        driver.get("https://www.baidu.com");
        driver.findElement(By.id("kw")).sendKeys("hello");
        driver.findElement(By.id("su")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String aa = driver.findElement(By.xpath("//*[@id='container']/div[2]/div/div[2]/span")).getText();
        System.out.println(aa);
        System.out.println("testCase222 ");
    }
    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }
}