package com.ming.auto.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public interface  BaseService {
	/*
	* 写一个测试类
	 */
	public void getPathStr();

	/*
	 * 得到测试时运行的系统类型
	 */
	public String getSystemtype();

	/*
	* 获得driver接口
	 */
	public WebDriver getDriver(String browsername);

	/*
	 * 清除driver对象接口
	 */
	public void cleanDriver();

	/*
	 * 执行完后，任务管理器中会有浏览器进程和驱动的进程，直接杀掉
	 * 需要传systemtype:windows，linux
	 */
	public  void killBrowser(String systemtype);

	/*
	 * 传方法名和描述文字，得到id^kw
	 * 在把id^kw转换成element对象
	 */
	public By getBy(String method, String locatordiscribename);

	/*
	 * 等待，单位是秒
	 */
	public void setSleep(long second);

	/*
	 * 传方法名，步骤id，步骤的字段标题名，得到步骤字段的值
	 *
	 */
	public String getTestCaseFromExcel(String method, String stepid, String titlename);

	/*
	 * 截图
	 */
	public  void saveScreenshot(WebDriver driver, String methodname);
}
