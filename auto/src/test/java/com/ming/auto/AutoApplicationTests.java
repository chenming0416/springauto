package com.ming.auto;

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
import org.testng.IAnnotationTransformer;
import org.testng.annotations.Test;


@SpringBootTest(classes={AutoApplication.class})
class AutoApplicationTests implements IAnnotationTransformer {

BaseService baseService = new BaseServiceImpl();
    private static final Logger logger = LoggerFactory.getLogger(AutoApplicationTests.class.getName());// slf4j记录器
    // 定义运行的操作系统，用在杀浏览器方法中，是调dos还是调shell
    String systemtype = "windows";// linux

    @Test(retryAnalyzer = com.ming.auto.RetryAnalyzer.class)
    public void test2(){
        String methodstr = Thread.currentThread().getStackTrace()[1].getMethodName();
        BaseServiceImpl.methodnamestr = methodstr;
        WebDriver driver = baseService.getDriver("chrome");
//        Assert.assertFalse(true);
    }

    @Test(retryAnalyzer = com.ming.auto.RetryAnalyzer.class)
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
        logger.debug("this is slf4j message");
        By seachresultby = baseService.getBy(methodstr, "搜索查询结果");
        String aa = driver.findElement(seachresultby).getText();
        System.out.println(aa);
        Assert.assertFalse(true);
        System.out.println("testCase222 ");
    }

}
