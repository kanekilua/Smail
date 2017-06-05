package com.szpt.service.serviceImpl;

import com.szpt.beans.SendMail;
import com.szpt.beans.User;
import com.szpt.service.IAccountService;
import com.szpt.utils.MailUtil;
import org.springframework.stereotype.Service;

/**
 * Created by Kane on 2017/5/13.
 */
@Service
public class AccountService implements IAccountService {

    private SendMail sendMail;
    private MailUtil mailUtil;
    private EmailSender emailSender;

    @Override
    public boolean passwordAuthenticator(User user) {
        mailUtil = new MailUtil();
        emailSender = new EmailSender();
        boolean isPass = false;
        String mailType = mailUtil.getMailType(user.getUser());
        String smtpHostName = "smtp."+mailType;
        sendMail = new SendMail();
        sendMail.setSMTPHost(smtpHostName);
        sendMail.setUser(user.getUser());
        sendMail.setPassword(user.getPassword());
        emailSender.setSendMail(sendMail);
        if(mailUtil.checkUser(smtpHostName,user.getUser(),user.getPassword())){
            try{
                emailSender.connect();
                isPass = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        emailSender.closeConnect();
        return isPass;
    }



}
