# springauto
springboot+testng+selenium+slf4j（logback）

这是调试阶段，还没有完成，会持续更新

已实现：

1、selenium自动化，使用无头浏览器

2、excel组织用例，把需要输入的文本数据组织起来

3、xml组织用例，用来保存每个元素的定位xpath，id等

4、WebDriverEventListener监听定位元素的异常，例如找不到的情况，日志输出

5、用IRetryAnalyzer来监听用例执行情况，要重试指定次数后才最终置为失败状态。

6、ITestListener监听用例执行的结果，失败后截图保存

7、slf4j（logback）写日志

8、testng的testng.xml组织用例，来决定执行哪些测试用例

9、使用第三方的ExtentReports框架生成报告

10、调试发邮件成功。

11、日志，截图，报告，邮件都处理完毕，但还未整合到一起。




我的邮箱：chenming0416@foxmail.com
