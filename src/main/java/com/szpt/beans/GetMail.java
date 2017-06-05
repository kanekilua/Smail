package com.szpt.beans;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Kane on 2017/5/14.
 */
public class GetMail {
    private String IMAPHost;
    private String user;
    private String password;
    private int pageSize;
    private String emailID;
    private Session session;
    private Folder folder;
    private Store store;
    private Message[] msg;

    public String getIMAPHost() {
        return IMAPHost;
    }

    public void setIMAPHost(String IMAPHost) {
        this.IMAPHost = IMAPHost;
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Message[] getMsg() {
        return msg;
    }

    public void setMsg(Message[] msg) {
        this.msg = msg;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
}
