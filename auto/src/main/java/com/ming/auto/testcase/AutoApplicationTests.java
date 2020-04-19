package com.ming.auto.testcase;

import com.ming.auto.AutoApplication;
import com.ming.auto.common.BaseService;
import com.ming.auto.common.BaseServiceImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @Author chenming
 * @create 2020/4/19 9:41
 */

// 必须继承AbstractTestNGSpringContextTests才能使用spring注入
@SpringBootTest(classes = AutoApplication.class )
class AutoApplicationTests extends AbstractTestNGSpringContextTests {
    private static final Logger logger = LoggerFactory.getLogger(AutoApplicationTests.class.getName());// slf4j记录器

    @Autowired
    private BaseService baseService ;

    @Test(retryAnalyzer = com.ming.auto.testlistener.RetryAnalyzer.class)
    public void testConstant(){
        String methodstr = Thread.currentThread().getStackTrace()[1].getMethodName();
        BaseServiceImpl.methodnamestr = methodstr;
        WebDriver driver = baseService.getDriver("chrome");
        baseService.getPathStr();
        Assert.assertFalse(true);
    }
    // @Test注释后面的括号，是指定重试监听器类
    @Test(retryAnalyzer = com.ming.auto.testlistener.RetryAnalyzer.class)
    public void test2(){
        // 这个方法名初始化后，是用在重试监听器里，打印log时，明确哪个用例（方法）出问题
        String methodstr = Thread.currentThread().getStackTrace()[1].getMethodName();
        BaseServiceImpl.methodnamestr = methodstr;
        // 这个driver初始化后，用例中执行使用，并且在结果监听器里截图时使用
        WebDriver driver = baseService.getDriver("chrome");
//        Assert.assertFalse(true);
    }

    @Test(retryAnalyzer = com.ming.auto.testlistener.RetryAnalyzer.class)
    public void contextLoads() {
        String methodstr = Thread.currentThread().getStackTrace()[1].getMethodName();
        BaseServiceImpl.methodnamestr = methodstr;
        WebDriver driver = baseService.getDriver("chrome");

        driver.get("https://www.baidu.com");
        By seachtextby = baseService.getBy(methodstr, "百度搜索文本框");
        String searchtxet = baseService.getTestCaseFromExcel(methodstr, "1", "component_name");
        driver.findElement(seachtextby).sendKeys(searchtxet);
        By seachbuttonby = baseService.getBy(methodstr, "百度搜索按钮");
        driver.findElement(seachbuttonby).click();
        baseService.setSleep(2);
        By seachresultby = baseService.getBy(methodstr, "搜索查询结果");
        String resulttext = driver.findElement(seachresultby).getText();
        Assert.assertFalse(true);
    }

}
