package testcase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class testCaseTest2 {

    @Test
    public void test2(){
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        System.out.println("testCase222 ");
    }
    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }
}