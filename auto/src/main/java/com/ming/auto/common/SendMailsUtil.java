package com.ming.auto.common;

import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @Author chenming
 * @create 2020/4/21 16:37
 */
@Component
public class SendMailsUtil {

    public MimeMessage createEmail(Session session,String mailtitle,String mailcontent,String targetaddr,String account,String addresser) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(account,addresser, "utf-8");
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
