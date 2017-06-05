package com.szpt.controller;

import com.szpt.beans.GetMail;
import com.szpt.beans.OperateMail;
import com.szpt.beans.SendMail;
import com.szpt.beans.User;
import com.szpt.beans.model.JsonResult;
import com.szpt.beans.model.MailInfo;
import com.szpt.service.IEmailGetter;
import com.szpt.service.IEmailOperator;
import com.szpt.service.IEmailSender;
import com.szpt.service.serviceImpl.EmailGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kane on 2017/5/20.
 */
@RestController
@RequestMapping(path = "/email")
@CrossOrigin(origins = "*")
public class EmailController {


    @Autowired
    private IEmailSender iEmailSender;

    @Autowired
    private IEmailGetter iEmailGetter;

    @Autowired
    private IEmailOperator iEmailOperator;

    @PostMapping
    public JsonResult sendEmail(@RequestBody @Valid SendMail sendMail1){
        JsonResult result = new JsonResult();
        boolean isSend = iEmailSender.send(sendMail1);
        result.setSuccess(isSend);
        if (isSend) {
            result.setMessage("发送成功！");
        }else{
            result.setMessage("发送失败！");
        }
        return result;
    }

    @PostMapping(path = "/list")
    public JsonResult listEmail(@RequestBody @Valid GetMail getMail){
        JsonResult result = new JsonResult();
        List<MailInfo> msgList = iEmailGetter.getInboxMsg(getMail);
        Map resultMap = new HashMap();
        for(int i =0;i<msgList.size();++i){
            resultMap.put(""+i,msgList.get(i));
        }
        if(msgList != null){
            result.setSuccess(true);
            result.setMessage("获取所有邮箱的信息成功");
            result.setResult(resultMap);
        }else{
            result.setSuccess(false);
            result.setMessage("获取所有邮箱的信息失败");
        }
        return result;
    }

    @PostMapping(path = "/emailDetail")
    public JsonResult getMailDetail(@RequestBody @Valid GetMail getMail){
        JsonResult result = new JsonResult();
        MailInfo mailInfo = iEmailGetter.getMsgDetail(getMail);
        if(mailInfo != null){
            result.setSuccess(true);
            result.setMessage("获取邮箱内容成功！");
            result.setResult(mailInfo);
        }else{
            result.setSuccess(false);
            result.setMessage("获取邮箱内容失败，请稍后再试！");
        }
        return result;
    }

    @PostMapping(path = "/searchEmail")
    public JsonResult searchEmail(@RequestBody @Valid OperateMail searchMail){
        JsonResult result = new JsonResult();
        List<MailInfo> mailList = iEmailOperator.searchMail(searchMail);
        Map resultMap = new HashMap();
        for(int i =0;i<mailList.size();++i){
            resultMap.put(""+i,mailList.get(i));
        }
        if(mailList != null){
            result.setSuccess(true);
            result.setMessage("获取邮箱内容成功！");
            result.setResult(resultMap);
        }else{
            result.setSuccess(false);
            result.setMessage("获取邮箱内容失败，请稍后再试！");
        }
        return result;
    }

    @DeleteMapping
    public JsonResult deleteEmail(@RequestBody @Valid OperateMail operateMail){
        JsonResult result = new JsonResult();
        boolean flag = iEmailOperator.deleteMail(operateMail);
        if(flag == true){
            result.setSuccess(true);
            result.setMessage("删除邮件成功！");
        }else{
            result.setSuccess(false);
            result.setMessage("删除邮件失败！");
        }
        return result;
    }

    @PostMapping(path = "/seeEmail")
    public JsonResult seeEmail(@RequestBody @Valid OperateMail operateMail){
        JsonResult result = new JsonResult();
        boolean flag = iEmailOperator.setIsSeen(operateMail);
        if(flag == true){
            result.setSuccess(true);
            result.setMessage("设置邮件已读！");
        }else{
            result.setSuccess(false);
            result.setMessage("设置邮件已读失败！");
        }
        return result;
    }

    @PostMapping(path = "/unSeeEmail")
    public JsonResult unSeeEmail(@RequestBody @Valid OperateMail operateMail){
        JsonResult result = new JsonResult();
        boolean flag = iEmailOperator.setUnSeen(operateMail);
        if(flag == true){
            result.setSuccess(true);
            result.setMessage("设置邮件为未读成功！");
        }else{
            result.setSuccess(false);
            result.setMessage("设置邮件为未读失败！");
        }
        return result;
    }

    @PostMapping(path = "/listSent")
    public JsonResult getSentEmail(@RequestBody @Valid GetMail getMail){
        JsonResult result = new JsonResult();
        List<MailInfo> msgList = iEmailGetter.getSentMsg(getMail);
        Map resultMap = new HashMap();
        for(int i =0;i<msgList.size();++i){
            resultMap.put(""+i,msgList.get(i));
        }
        if(msgList != null){
            result.setSuccess(true);
            result.setMessage("获取所有已发送的邮件成功");
            result.setResult(resultMap);
        }else{
            result.setSuccess(false);
            result.setMessage("获取所有已发送的邮件失败");
        }
        return result;
    }

    @PostMapping(path = "/listDrafts")
    public JsonResult getDraftsEmail(@RequestBody @Valid GetMail getMail){
        JsonResult result = new JsonResult();
        List<MailInfo> msgList = iEmailGetter.getDraftsMsg(getMail);
        Map resultMap = new HashMap();
        for(int i =0;i<msgList.size();++i){
            resultMap.put(""+i,msgList.get(i));
        }
        if(msgList != null){
            result.setSuccess(true);
            result.setMessage("获取所有草稿箱的邮件成功");
            result.setResult(resultMap);
        }else{
            result.setSuccess(false);
            result.setMessage("获取所有草稿箱的邮件失败");
        }
        return result;
    }

    @PostMapping(path = "/listDeleted")
    public JsonResult getDeleteEmail(@RequestBody @Valid GetMail getMail){
        JsonResult result = new JsonResult();
        List<MailInfo> msgList = iEmailGetter.getDeleteMsg(getMail);
        Map resultMap = new HashMap();
        for(int i =0;i<msgList.size();++i){
            resultMap.put(""+i,msgList.get(i));
        }
        if(msgList != null){
            result.setSuccess(true);
            result.setMessage("获取所有已删除的邮件成功");
            result.setResult(resultMap);
        }else{
            result.setSuccess(false);
            result.setMessage("获取所有已删除的邮件失败");
        }
        return result;
    }

    @DeleteMapping(path = "/trashMail")
    public JsonResult deleteTrashMail(@RequestBody @Valid OperateMail operateMail){
        JsonResult result = new JsonResult();
        boolean flag = iEmailOperator.deleteTrashMail(operateMail);
        if(flag == true){
            result.setSuccess(true);
            result.setMessage("删除垃圾箱邮件成功");
        }else{
            result.setSuccess(false);
            result.setMessage("删除垃圾箱邮件失败");
        }
        return result;
    }
}
