package com.ming.auto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

//@Component
//@ConfigurationProperties(prefix="constant")
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class.getName());// slf4j记录器
    int counter = 0;
    int retryLimit = 2;
    @Override
    public boolean retry(ITestResult iTestResult) {
        if(counter < retryLimit)
        {
            counter++;
            logger.info("重试第"+counter+"次");
            return true;
        }
        return false; // false 代表不重试
    }
}
