package com.szpt.service.serviceImpl;

import com.szpt.beans.GetMail;
import com.szpt.beans.model.MailInfo;
import com.szpt.service.IEmailGetter;
import com.szpt.utils.MailUtil;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Kane on 2017/5/13.
 */
@Service
public class EmailGetter implements IEmailGetter {

    private GetMail getMail;
    private MailUtil mailUtil;

    @Override
    public List<MailInfo> getInboxMsg(GetMail getMail1) {
        List<MailInfo> mailInfoList = null;
        getMail = getMail1;
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(getMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            mailInfoList = getMailInfo(msg);
            closeConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mailInfoList;
    }

    @Override
    public List<MailInfo> getSentMsg(GetMail getMail1) {
        List<MailInfo> sentMsgList = null;
        getMail = getMail1;
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(getMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("Sent Messages");
            folder.open(Folder.READ_ONLY);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            sentMsgList = getMailInfo(msg);
            closeConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sentMsgList;
    }

    @Override
    public List<MailInfo> getDraftsMsg(GetMail getMail1) {
        List<MailInfo> draftsMsgList = null;
        getMail = getMail1;
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(getMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("Drafts");
            folder.open(Folder.READ_ONLY);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            draftsMsgList = getMailInfo(msg);
            closeConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return draftsMsgList;
    }

    @Override
    public List<MailInfo> getDeleteMsg(GetMail getMail1) {
        List<MailInfo> deleteMsgList = null;
        getMail = getMail1;
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(getMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            connect();
            Store store = getMail.getStore();
            Folder folder = store.getDefaultFolder().getFolder("Deleted Messages");
            folder.open(Folder.READ_ONLY);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            deleteMsgList = getMailInfo(msg);
            closeConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return deleteMsgList;
    }

    @Override
    public MailInfo getMsgDetail(GetMail getMail1) {
        MailInfo mailInfo = null;
        getMail = getMail1;
        mailUtil = new MailUtil();
        String IMAPHost = "imap." + mailUtil.getMailType(getMail.getUser());
        getMail.setIMAPHost(IMAPHost);
        try {
            connect();
            Store store = getMail1.getStore();
            Folder folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            getMail.setFolder(folder);
            Message[] msg = folder.getMessages();
            Message theMsg = getMsgByID(msg,getMail.getEmailID());
            mailInfo = mailUtil.parseMessage(theMsg);
            closeConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mailInfo;
    }

    // 连接邮件服务器
    public void connect() throws Exception {
        int port = 143;
        Properties prop = new Properties();
        prop.put("mail.imap.host", getMail.getIMAPHost());
        prop.put("mail.imap.port", port);
        // 取得一个Session对象
        Session session = Session.getDefaultInstance(prop);
        getMail.setSession(session);
        // 取得一个Store对象
        Store store = session.getStore("imap");
        store.connect(getMail.getIMAPHost(), getMail.getUser(),getMail.getPassword());
        getMail.setStore(store);
    }

    // 取得邮件列表的信息
    public List<MailInfo> getMailInfo(Message[] msg) throws Exception {
        List<MailInfo> result = new ArrayList();
        MailInfo mailInfo ;
        int length = msg.length;
        int pageSize = getMail.getPageSize();
        // 取出每个邮件的信息
        for (int i = msg.length -1 ; i >= (length > pageSize ? length - pageSize:0); i--) {
            mailInfo = mailUtil.parseMessage(msg[i]);
            result.add(mailInfo);
        }
        return result;
    }

    // 关闭连接
    public void closeConnect() {
        Folder folder = getMail.getFolder();
        Store store = getMail.getStore();
        try {
            if (folder != null)
                folder.close(true);// 关闭连接时是否删除邮件，true删除邮件
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (store != null)
                    store.close();// 关闭收件箱连接
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public Message getMsgByID(Message[] msg,String ID) throws Exception{
        Message resultMsg = null;
        int i;
        for(i = 0;i<msg.length;++i){
            String msgID = mailUtil.getMsgID((MimeMessage) msg[i]);
            if(ID.equals(msgID)){
                return msg[i];
            }
        }
        return resultMsg;
    }

    public GetMail getGetMail() {
        return getMail;
    }

    public void setGetMail(GetMail getMail) {
        this.getMail = getMail;
    }

    public MailUtil getMailUtil() {
        return mailUtil;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }
}
