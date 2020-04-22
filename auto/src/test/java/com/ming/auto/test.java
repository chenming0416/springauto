package com.ming.auto;

import com.ming.auto.common.BaseService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AutoApplication.class )
public class test  extends AbstractTestNGSpringContextTests {

    @Autowired
    private BaseService baseService;

    @Test
    public void testConstant(){
//        baseService.getPathStr();
        try {
            baseService.sendMail("我是陈明","写点邮件内容");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
