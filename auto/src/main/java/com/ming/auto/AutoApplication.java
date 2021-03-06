package com.ming.auto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author chenming
 * @create 2020/4/19 9:41
 */

@SpringBootApplication
public class AutoApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(AutoApplication.class);
        SpringApplication.run(AutoApplication.class, args);

    }

}
