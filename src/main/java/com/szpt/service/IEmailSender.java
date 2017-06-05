package com.szpt.service;

import com.szpt.beans.SendMail;

/**
 * Created by Kane on 2017/5/13.
 */
public interface IEmailSender {
    boolean send(SendMail sendMail);
}
