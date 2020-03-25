package testcase;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class testCaseTest {
    @Test
    public void beforeclass(){
        System.out.println("2222222222222");
    }
    @BeforeMethod
    public void setUp() {
        System.out.println("333333333333");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("4444444444");
    }
}