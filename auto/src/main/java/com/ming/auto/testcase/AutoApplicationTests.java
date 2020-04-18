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
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest(classes = AutoApplication.class )
class AutoApplicationTests extends AbstractTestNGSpringContextTests {
    private static final Logger logger = LoggerFactory.getLogger(AutoApplicationTests.class.getName());// slf4j记录器

    @Autowired // 无法得到对象，只好new了一个
    private BaseService baseService ;
//    BaseService baseService = new BaseServiceImpl();

    @Test
    public void testConstant(){
        baseService.getPathStr();
    }

    @Test(retryAnalyzer = com.ming.auto.testlistener.RetryAnalyzer.class)
    public void test2(){
        String methodstr = Thread.currentThread().getStackTrace()[1].getMethodName();
        BaseServiceImpl.methodnamestr = methodstr;
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
