package com.ming.auto.testcase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.testng.annotations.Test;

/**
 * @Author chenming
 * @create 2020/4/21 16:37
 */
public class SendMails {

    public  String SMTPSERVER = "smtp.163.com";
    public  String SMTPPORT = "465";
    public  String ACCOUT = "chen-ming-163@163.com";
    public  String PWD = "DH"; // 优先取客户端授权密码，没设置取普通密码
    public  String addresser = "chenming";// 发件人昵称
    public  String targetaddr = "chen-ming-163@163.com,265882449@qq.com"; // 收件人
    public String mailtitle = "我是标题";
    public String mailcontent = "我是内容";

    @Test
    public void f() throws Exception {// 创建邮件配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", SMTPPORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl


        // 根据邮件配置创建会话，注意session别导错包
        Session session = Session.getDefaultInstance(props);
        // 开启debug模式，可以看到更多详细的输入日志
        session.setDebug(true);
        //创建邮件
        MimeMessage message = createEmail(session,mailtitle,mailcontent,targetaddr);
        //获取传输通道
        Transport transport = session.getTransport();
        transport.connect(SMTPSERVER,ACCOUT, PWD);
        //连接，并发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }


    public MimeMessage createEmail(Session session,String mailtitle,String mailcontent,String targetaddr) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(ACCOUT,
                addresser, "utf-8");
        // 设置发送邮件方
        msg.setFrom(fromAddress);

        // 设置邮件接收方,发送单个邮件
        // InternetAddress receiveAddress = new InternetAddress(
        //         "265882449@qq.com", "test", "utf-8");
        // msg.setRecipient(RecipientType.TO, receiveAddress);
        // 设置邮件接收方，发送多个邮件
        msg.addRecipients(RecipientType.TO, Address(targetaddr));
        // 设置邮件标题
        msg.setSubject(mailtitle, "utf-8");
        msg.setText(mailcontent);
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        return msg;
    }

    public   InternetAddress[]  Address(String targetaddr){

        //多个接收账号
        String str=targetaddr;
        InternetAddress[] address=null;
        try {
            List<InternetAddress> list = new ArrayList<InternetAddress>();//不能使用string类型的类型，这样只能发送一个收件人
            String []median=str.split(",");//对输入的多个邮件进行逗号分割
            for(int i=0;i<median.length;i++){
                list.add(new InternetAddress(median[i]));
            }
            address =(InternetAddress[])list.toArray(new InternetAddress[list.size()]);

        } catch (AddressException e) {
            e.printStackTrace();
        }
        return address;
    }
}
