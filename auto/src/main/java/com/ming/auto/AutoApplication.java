package com.ming.auto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(AutoApplication.class);
        SpringApplication.run(AutoApplication.class, args);

    }

}
