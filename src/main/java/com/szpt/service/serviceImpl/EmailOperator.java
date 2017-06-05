package com.szpt.service.serviceImpl;

import com.szpt.beans.GetMail;
import com.szpt.beans.OperateMail;
import com.szpt.beans.SendMail;
import com.szpt.beans.model.MailInfo;
import com.szpt.service.IEmailOperator;
import com.szpt.utils.MailUtil;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Kane on 2017/5/21.
 */
@Service
public class EmailOperator implements IEmailOperator{

    @Autowired
    private EmailGetter emailGetter;
    private MailUtil mailUtil;

    @Override
    public List<MailInfo> searchMail(OperateMail operateMail) {
        List<MailInfo> searchMailInfo = null;
        GetMail getMail1 = new GetMail();
        getMail1.setUser(operateMail.getUser());
        getMail1.setPassword(operateMail.getPassword());
        getMail1.setPageSize(30);
        emailGetter.setGetMail(getMail1);
        GetMail getMail = emailGetter.getGetMail();
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(operateMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            emailGetter.connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            getMail.setFolder(folder);
            final String keyword = operateMail.getKeyword();
            SearchTerm searchCondition = new SearchTerm() {
                @Override
                public boolean match(Message message) {
                    try {
                        String subject = message.getSubject();
                        String from = message.getFrom().toString();
                        if(subject != null && from != null ){
                            if (subject.contains(keyword) || from.contains(keyword)) {
                                return true;
                            }
                        }
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            };
            Message[] msg = folder.search(searchCondition);
            emailGetter.setMailUtil(mailUtil);
            searchMailInfo = emailGetter.getMailInfo(msg);
            emailGetter.closeConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return searchMailInfo;
    }

    @Override
    public boolean deleteMail(OperateMail operateMail) {
        GetMail getMail1 = new GetMail();
        getMail1.setUser(operateMail.getUser());
        getMail1.setPassword(operateMail.getPassword());
        emailGetter.setGetMail(getMail1);
        GetMail getMail = emailGetter.getGetMail();
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(operateMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            emailGetter.connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            emailGetter.setMailUtil(mailUtil);
            Message theMsg = emailGetter.getMsgByID(msg,operateMail.getEmailID());
            Message[] copyTo = new Message[]{theMsg};
            if(store.getFolder("Deleted Messages") == null){
                return false;
            }else {
                Folder deletedFolder = store.getFolder("Deleted Messages");
                folder.copyMessages(copyTo, deletedFolder);
            }
            theMsg.setFlag(Flags.Flag.DELETED,true);
            emailGetter.closeConnect();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setIsSeen(OperateMail operateMail) {
        GetMail getMail1 = new GetMail();
        getMail1.setUser(operateMail.getUser());
        getMail1.setPassword(operateMail.getPassword());
        emailGetter.setGetMail(getMail1);
        GetMail getMail = emailGetter.getGetMail();
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(operateMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            emailGetter.connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            emailGetter.setMailUtil(mailUtil);
            Message theMsg = emailGetter.getMsgByID(msg,operateMail.getEmailID());
            theMsg.setFlag(Flags.Flag.SEEN,true);
            emailGetter.closeConnect();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setUnSeen(OperateMail operateMail) {
        GetMail getMail1 = new GetMail();
        getMail1.setUser(operateMail.getUser());
        getMail1.setPassword(operateMail.getPassword());
        emailGetter.setGetMail(getMail1);
        GetMail getMail = emailGetter.getGetMail();
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(operateMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            emailGetter.connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            emailGetter.setMailUtil(mailUtil);
            Message theMsg = emailGetter.getMsgByID(msg,operateMail.getEmailID());
            theMsg.setFlag(Flags.Flag.SEEN,false);
            emailGetter.closeConnect();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTrashMail(OperateMail operateMail) {
        GetMail getMail1 = new GetMail();
        getMail1.setUser(operateMail.getUser());
        getMail1.setPassword(operateMail.getPassword());
        emailGetter.setGetMail(getMail1);
        GetMail getMail = emailGetter.getGetMail();
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(operateMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            emailGetter.connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("Deleted Messages");
            folder.open(Folder.READ_WRITE);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            emailGetter.setMailUtil(mailUtil);
            Message theMsg = emailGetter.getMsgByID(msg,operateMail.getEmailID());
            theMsg.setFlag(Flags.Flag.DELETED,true);
            emailGetter.closeConnect();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveDrafts(SendMail sendMail){
        mailUtil =  new MailUtil();
        String from = sendMail.getUser();
        Address[] to = mailUtil.convertAddr(sendMail.getTo());
        Address[] copy_to = mailUtil.convertAddr(sendMail.getCopy_to());
        String subject = sendMail.getSubject();
        String content = sendMail.getContent();
        try {
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


        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
