package com.ming.auto.testlistener;

import com.ming.auto.AutoApplication;
import com.ming.auto.common.BaseService;
import com.ming.auto.common.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import javax.annotation.PostConstruct;

/**
 * @Author chenming
 * @create 2020/4/19 9:41
 */

@Component
public class TestngListener extends AbstractTestNGSpringContextTests implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestngListener.class.getName());// slf4j记录器
    @Autowired
    private BaseService baseService;
    private static TestngListener testngListener;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        testngListener = this;
        testngListener.baseService = this.baseService;
        // 初使化时将已静态化的testService实例化
    }

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
        String systemtyp = testngListener.baseService.getSystemtype();
        testngListener.baseService.killBrowser(systemtyp);

    }

    public void onTestFailure(ITestResult result) {
        if (BaseServiceImpl.webDriver != null) {
            logger.error(result.getName());
            testngListener.baseService.saveScreenshot(BaseServiceImpl.webDriver, result.getName());
        }else {
            logger.error("webdrvier对象为空");
        }
    }

    public void onTestSkipped(ITestResult result) {
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
