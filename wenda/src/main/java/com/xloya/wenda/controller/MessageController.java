package com.xloya.wenda.controller;


import com.xloya.wenda.model.HostHolder;
import com.xloya.wenda.model.Message;
import com.xloya.wenda.model.User;
import com.xloya.wenda.model.ViewObject;
import com.xloya.wenda.service.MessageService;
import com.xloya.wenda.service.UserService;
import com.xloya.wenda.utils.WenDaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @RequestMapping(path = "/msg/list",method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder.getUser()==null)
            return "redirect:/reglogin";

        int localUserId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
        List<ViewObject> conversations = new ArrayList<>();
        for(Message message:conversationList){
            ViewObject viewObject = new ViewObject();
            viewObject.setObjects("conversation",message);
            int targetid = message.getFrom_id() == localUserId ? message.getTo_id() : message.getFrom_id();
            viewObject.setObjects("user",userService.getUser(targetid));
            viewObject.setObjects("unread",messageService.getConversationUnreadCount(message.getConversation_id(),localUserId));
            conversations.add(viewObject);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }

    @RequestMapping(path = "/msg/detail",method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversation_id")String conversation_id){
        try{
            List<Message> messageList = messageService.getConversationDetail(conversation_id,0,10);
            List<ViewObject> messages = new ArrayList<>();
            for(Message message :messageList){
                ViewObject viewObject = new ViewObject();
                viewObject.setObjects("message",message);
                viewObject.setObjects("user",userService.getUser(message.getFrom_id()));
                messages.add(viewObject);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            LOGGER.error("获取详情失败"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = "/msg/addMessage",method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName")String toName,
                             @RequestParam("content")String content){

        try{
            if(hostHolder.getUser()==null){
                return WenDaUtils.getJSONString(999,"未登录");
            }

            User user = userService.selectByName(toName);

            if(user==null){
                return WenDaUtils.getJSONString(1,"用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setCreated_date(new Date());
            message.setFrom_id(hostHolder.getUser().getId());
            message.setTo_id(user.getId());
            messageService.addMessage(message);

            return WenDaUtils.getJSONString(0);
        }catch(Exception e){
            LOGGER.error("添加消息失败"+e.getMessage());
            return WenDaUtils.getJSONString(1,"发信失败");
        }

    }
}
