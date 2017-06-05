package com.szpt.service;


import com.szpt.beans.GetMail;
import com.szpt.beans.model.MailInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by kane on 5/12/17.
 */
public interface IEmailGetter {
    List<MailInfo> getInboxMsg(GetMail getMail);
    MailInfo getMsgDetail(GetMail getMail);
    List<MailInfo> getSentMsg(GetMail getMail);
    List<MailInfo> getDraftsMsg(GetMail getMail);
    List<MailInfo> getDeleteMsg(GetMail getMail);
}
