package com.xloya.wenda.service;


import com.xloya.wenda.dao.MessageDAO;
import com.xloya.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId():0;
    }

    public List<Message> getConversationDetail(String conversation_id,int offset,int limit){
        return messageDAO.getConversationDetail(conversation_id,offset,limit);
    }

    public List<Message> getConversationList(int user_id,int offset,int limit){
        return messageDAO.getConversationList(user_id,offset,limit);
    }

    public int getConversationUnreadCount( String conversation_id,int user_id){
        return messageDAO.getConversationUnreadCount(conversation_id,user_id);
    }
}
