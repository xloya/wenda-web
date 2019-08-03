package com.xloya.wenda.async.handler;

import com.xloya.wenda.async.EventHandler;
import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel eventModel) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"),"登录ip异常","mails/login_exception.html",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
