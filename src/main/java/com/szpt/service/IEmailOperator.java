package com.szpt.service;

import com.szpt.beans.OperateMail;
import com.szpt.beans.SendMail;
import com.szpt.beans.model.MailInfo;

import java.util.List;

/**
 * Created by Kane on 2017/5/21.
 */
public interface IEmailOperator {
    List<MailInfo> searchMail(OperateMail operateMail);
    boolean deleteMail(OperateMail operateMail);
    boolean setIsSeen (OperateMail operateMail);
    boolean setUnSeen(OperateMail operateMail);
    boolean deleteTrashMail(OperateMail operateMail);
    boolean saveDrafts(SendMail sendMail);
}
