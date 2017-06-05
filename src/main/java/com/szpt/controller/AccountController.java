package com.szpt.controller;

import com.szpt.beans.User;
import com.szpt.beans.model.JsonResult;
import com.szpt.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Kane on 2017/5/20.
 */
@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private IAccountService iAccountService;

    @PostMapping
    public JsonResult passwordAuthenticator(@RequestBody @Valid User user){
        JsonResult result = new JsonResult();
        boolean ifLogin = iAccountService.passwordAuthenticator(user);
        result.setSuccess(ifLogin);
        if(ifLogin){
            result.setMessage("登录成功！");
        }else {
            result.setMessage("用户名或密码错误！");
        }
        return result;
    }
}
