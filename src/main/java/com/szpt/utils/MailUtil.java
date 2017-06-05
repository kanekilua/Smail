package com.szpt.utils;

import com.sun.mail.imap.IMAPMessage;
import com.szpt.beans.model.MailInfo;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kane on 2017/5/13.
 */
public class MailUtil {
    public MailInfo parseMessage(Message msg) throws MessagingException,IOException {
        MimeMessage mmsg = (MimeMessage) msg;
        IMAPMessage imapMessage = (IMAPMessage) msg;
        MailInfo mailInfo = new MailInfo();
        mailInfo.setID(imapMessage.getMessageID());
        mailInfo.setFrom(getFrom(mmsg));
        mailInfo.setTo(getReceiveAddress(mmsg,null));
        mailInfo.setSeen(isSeen(mmsg));
        mailInfo.setSendDate(getSentDate(mmsg,null));
        mailInfo.setSubject(getSubject(mmsg));
        StringBuffer contentString = new StringBuffer();
        getMailTextContent(mmsg,contentString);
        mailInfo.setContent(contentString.toString());
        return mailInfo;
    }

    public String getMailType(String mailAddress){
        int atIndex = mailAddress.indexOf('@');
        String mailType = mailAddress.substring(atIndex+1);
        return mailType;
    }

    // 验证用户输入数据的有效性
    public boolean checkUser(String SMTPHost,String user,String password) {
        boolean check = false;
        boolean checkSMTP = SMTPHost.toLowerCase().startsWith("smtp");// 验证smtp服务器
        boolean checkPassword = !"".equals(password) && password.length() >= 1;
        boolean checkUser = checkEmailAdress(user);//验证邮箱的有效性
        if (checkSMTP && checkPassword && checkUser) {
            check = true;// 验证通过
        }
        return check;
    }





    /**
     * 获得邮件的Message-ID
     * @param msg Message-ID
     * @return 解码后的邮件ID
     */
    public String getMsgID(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        String msgID = "";
        Enumeration enumMail = msg.getAllHeaders();
        Header h = null;
        while (enumMail.hasMoreElements()) {
            h = (Header) enumMail.nextElement();
            if (h.getName().equals("Message-ID")
                    || h.getName().equals("Message-Id")) {
                msgID = h.getValue();
                return msgID;
            }
        }
        return msgID;
    }

    /**
     * 获得邮件主题
     * @param msg 邮件内容
     * @return 解码后的邮件主题
     */
    public String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        if(msg.getSubject() != null) {
            return MimeUtility.decodeText(msg.getSubject());
        }else {
            return "";
        }
    }

    /**
     * 获得邮件发件人
     * @param msg 邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";

        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     * @param msg 邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress)address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length()-1); //删除最后一个逗号

        return receiveAddress.toString();
    }

    /**
     * 获得邮件发送时间
     * @param msg 邮件内容
     * @return yyyy年mm月dd日 星期X HH:mm
     * @throws MessagingException
     */
    public String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = "yyyy年MM月dd日 E HH:mm ";

        return new SimpleDateFormat(pattern).format(receivedDate);
    }

    /**
     * 判断邮件是否已读
     * @param msg 邮件内容
     * @return 如果邮件已读返回true,否则返回false
     * @throws MessagingException
     */
    public boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 获得邮件文本内容
     * @param part 邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part)part.getContent(),content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart,content);
            }
        } else {
            content.append(part.getContent().toString());
        }
    }

    /**
     * 文本解码
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    private boolean checkEmailAdress(String input){
        Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public Address[] convertAddr(String stringAddr){
        Address[] to = null;
        int i = 0;
        if(stringAddr != null && stringAddr != "") {
            StringTokenizer tokenizer = new StringTokenizer(stringAddr, ";");
            to = new Address[tokenizer.countTokens()];// 动态的决定数组的长度
            while (tokenizer.hasMoreTokens()) {
                String d = tokenizer.nextToken();
                try {
                    d = MimeUtility.encodeText(d);
                    to[i] = new InternetAddress(d);// 将字符串转换为整型
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        return to;
    }
}
