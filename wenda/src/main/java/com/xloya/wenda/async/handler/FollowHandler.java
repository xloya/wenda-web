package com.xloya.wenda.async.handler;

import com.xloya.wenda.async.EventHandler;
import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.model.EntityType;
import com.xloya.wenda.model.Message;
import com.xloya.wenda.model.User;
import com.xloya.wenda.service.MessageService;
import com.xloya.wenda.service.QuestionService;
import com.xloya.wenda.service.UserService;
import com.xloya.wenda.utils.WenDaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    QuestionService questionService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFrom_id(WenDaUtils.SYSTEM_USERID);
        message.setTo_id(eventModel.getEntity_ownerid());
        message.setCreated_date(new Date());
        User user = userService.getUser(eventModel.getActor_id());
        if(eventModel.getEntity_type()== EntityType.ENTITY_QUESTION)
            message.setContent("用户"+user.getName()+"关注了你的问题，http://localhost:8080/question/"+eventModel.getEntity_id());
        else if(eventModel.getEntity_type()== EntityType.ENTITY_USER)
            message.setContent("用户"+user.getName()+"关注了你，http://localhost:8080/user/"+eventModel.getActor_id());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
