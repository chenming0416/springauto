package com.ming.auto;

import com.ming.auto.common.BaseService;
import com.ming.auto.common.BaseServiceImpl;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;


public class TestngListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestngListener.class.getName());// slf4j记录器
//    @Autowired
//    private BaseService baseService;
    // autowired取不到，只好new一个
    BaseService baseService = new BaseServiceImpl();

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
        baseService.killBrowser("windows");
    }

    public void onTestFailure(ITestResult result) {
        if (BaseServiceImpl.webDriver != null)
        {
            baseService.saveScreenshot(BaseServiceImpl.webDriver,result.getName());
        }
    }

    public  void onTestSkipped(ITestResult result) {
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onTestFailedWithTimeout(ITestResult result) {
        this.onTestFailure(result);
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }

}
