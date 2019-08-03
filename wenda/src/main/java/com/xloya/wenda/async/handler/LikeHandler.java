package com.xloya.wenda.async.handler;

import com.xloya.wenda.async.EventHandler;
import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.model.Message;
import com.xloya.wenda.model.User;
import com.xloya.wenda.service.MessageService;
import com.xloya.wenda.service.UserService;
import com.xloya.wenda.utils.WenDaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFrom_id(WenDaUtils.SYSTEM_USERID);
        message.setTo_id(eventModel.getEntity_ownerid());
        message.setCreated_date(new Date());
        User user = userService.getUser(eventModel.getActor_id());
        message.setContent("用户"+user.getName()+"赞了你的评论，http://localhost:8080/question/"+eventModel.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
