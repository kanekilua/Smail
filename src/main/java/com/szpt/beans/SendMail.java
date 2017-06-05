package com.szpt.beans;

import javax.mail.*;
import java.util.ArrayList;

/**
 * Created by Kane on 2017/5/13.
 */
public class SendMail {
    private String SMTPHost = ""; // SMTP服务器
    private String user = ""; // 登录SMTP服务器的帐号
    private String password = ""; // 登录SMTP服务器的密码

    private String from = ""; // 发件人邮箱
    private String to = null; // 收件人邮箱
    private String subject = ""; // 邮件标题
    private String content = ""; // 邮件内容
    private String copy_to = null;// 抄送邮件到
    private Session mailSession = null;
    private Transport transport = null;
    private ArrayList<String> filename = new ArrayList<String>(); // 附件文件名
    public SendMail(){
    }

    public String getSMTPHost() {
        return SMTPHost;
    }

    public void setSMTPHost(String SMTPHost) {
        this.SMTPHost = SMTPHost;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
//        try {
//            // 解决标题的中文问题
//            subject = MimeUtility.encodeText(subject);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCopy_to() {
        return copy_to;
    }

    public void setCopy_to(String copy_to) {
        this.copy_to = copy_to;
    }

    public Session getMailSession() {
        return mailSession;
    }

    public void setMailSession(Session mailSession) {
        this.mailSession = mailSession;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public ArrayList<String> getFilename() {
        return filename;
    }

    public void setFilename(ArrayList<String> filename) {
//        Iterator<String> iterator = filename.iterator();
//        ArrayList<String> attachArrayList = new ArrayList<String>();
//        while (iterator.hasNext()) {
//            String attachment = iterator.next();
//            try {
//                // 解决文件名的中文问题
//                attachment = MimeUtility.decodeText(attachment);
//                // 将文件路径中的'\'替换成'/'
//                attachment = attachment.replaceAll("\\\\", "/");
//                attachArrayList.add(attachment);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
        this.filename = filename;
    }








}
