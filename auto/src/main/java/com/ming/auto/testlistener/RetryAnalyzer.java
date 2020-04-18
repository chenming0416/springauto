package com.ming.auto.testlistener;

import com.ming.auto.common.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class.getName());// slf4j记录器
    int counter = 0;
    int retryLimit = 2;
    @Override
    public boolean retry(ITestResult iTestResult) {
        if(counter < retryLimit)
        {
            counter++;
            logger.error("测试用例【"+ BaseServiceImpl.methodnamestr+"】重试第"+counter+"次");
            return true;
        }
        return false; // false 代表不重试
    }
}
