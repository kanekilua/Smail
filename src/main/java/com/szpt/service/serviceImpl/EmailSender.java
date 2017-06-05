package com.szpt.service.serviceImpl;

import com.szpt.beans.SendMail;
import com.szpt.service.IEmailSender;
import com.szpt.utils.MailUtil;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 * Created by Kane on 2017/5/13.
 */
@Service
public class EmailSender implements IEmailSender {

    private SendMail sendMail;

    private MailUtil mailUtil;

    @Override
    public boolean send(SendMail sendMail1) {
        sendMail = sendMail1;
        mailUtil = new MailUtil();
        String SMTPHost = "smtp." + mailUtil.getMailType(sendMail1.getUser());
        sendMail.setSMTPHost(SMTPHost);
        String from = sendMail.getUser();
        Address[] to = mailUtil.convertAddr(sendMail.getTo());
        Address[] copy_to = mailUtil.convertAddr(sendMail.getCopy_to());
        String subject = sendMail.getSubject();
//        ArrayList<String> filename = sendMail.getFilename();
        String content = sendMail.getContent();

        try {// 连接smtp服务器
            connect();
            // 创建一个MimeMessage 对象
            MimeMessage message = new MimeMessage(sendMail.getMailSession());

            // 指定发件人邮箱
            message.setFrom(new InternetAddress(from));
            // 指定收件人邮箱
            message.addRecipients(Message.RecipientType.TO, to);
            if (!"".equals(copy_to))
                // 指定抄送人邮箱
                message.addRecipients(Message.RecipientType.CC, copy_to);
            // 指定邮件主题
            message.setSubject(subject);
            // 指定邮件发送日期
            message.setSentDate(new Date());
            // 指定邮件优先级 1：紧急 3：普通 5：缓慢
            message.setHeader("X-Priority", "1");
            message.saveChanges();
            // 判断附件是否为空
//            if (!filename.isEmpty()) {
//                // 新建一个MimeMultipart对象用来存放多个BodyPart对象
//                Multipart container = new MimeMultipart();
//                // 新建一个存放信件内容的BodyPart对象
//                BodyPart textBodyPart = new MimeBodyPart();
//                // 给BodyPart对象设置内容和格式/编码方式
//                textBodyPart.setContent(content, "text/html;charset=gbk");
//                // 将含有信件内容的BodyPart加入到MimeMultipart对象中
//                container.addBodyPart(textBodyPart);
//                Iterator<String> fileIterator = filename.iterator();
//                while (fileIterator.hasNext()) {// 迭代所有附件
//                    String attachmentString = fileIterator.next();
//                    // 新建一个存放信件附件的BodyPart对象
//                    BodyPart fileBodyPart = new MimeBodyPart();
//                    // 将本地文件作为附件
//                    FileDataSource fds = new FileDataSource(attachmentString);
//                    fileBodyPart.setDataHandler(new DataHandler(fds));
//                    // 处理邮件中附件文件名的中文问题
//                    String attachName = fds.getName();
//                    attachName = MimeUtility.encodeText(attachName);
//                    // 设定附件文件名
//                    fileBodyPart.setFileName(attachName);
//                    // 将附件的BodyPart对象加入到container中
//                    container.addBodyPart(fileBodyPart);
//                }
//                // 将container作为消息对象的内容
//                message.setContent(container);
//            } else {// 没有附件的情况
                message.setContent(content,"text/html,charset=utf-8");
//            }
            // 发送邮件
            Transport transport = sendMail.getTransport();
            transport.sendMessage(message,message.getAllRecipients());
            if (transport != null)
                transport.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("发送失败，失败原因："+ex.getMessage());
            return false;
        }
    }

    public  void connect() throws Exception {
        // 创建一个属性对象
        Properties props = new Properties();
        // 指定SMTP服务器
        props.put("mail.smtp.host", sendMail.getSMTPHost());
        // 指定是否需要SMTP验证
        props.put("mail.smtp.auth", "true");
        // 创建一个授权验证对象
//        SmtpAuth auth = new SmtpAuth();
//        auth.setAccount(sendMail.getUser(),sendMail.getPassword());
        // 创建一个Session对象
//        mailSession = Session.getDefaultInstance(props, auth);
        Session mailSession = Session.getDefaultInstance(props);
        // 设置是否调试
        mailSession.setDebug(true);
        sendMail.setMailSession(mailSession);
//        if (transport != null)
//            transport.close();// 关闭连接
        // 创建一个Transport对象
        Transport transport = mailSession.getTransport("smtps");
        // 连接SMTP服务器
        transport.connect(sendMail.getSMTPHost(),sendMail.getUser(),sendMail.getPassword());
        sendMail.setTransport(transport);
    }

    public void closeConnect(){
        Transport transport = sendMail.getTransport();
        if(transport != null){
            try {
                transport.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public SendMail getSendMail() {
        return sendMail;
    }

    public void setSendMail(SendMail sendMail) {
        this.sendMail = sendMail;
    }
}
