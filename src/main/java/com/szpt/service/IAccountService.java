package com.szpt.service;

import com.szpt.beans.User;

/**
 * Created by kane on 5/10/17.
 */
public interface IAccountService {
    boolean passwordAuthenticator(User user);
}
