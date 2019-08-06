package com.xloya.wenda.controller;


import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventProducer;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.model.Comment;
import com.xloya.wenda.model.EntityType;
import com.xloya.wenda.model.HostHolder;
import com.xloya.wenda.service.CommentService;
import com.xloya.wenda.service.QuestionService;
import com.xloya.wenda.utils.WenDaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;


    @Autowired
    EventProducer eventProducer;


    @RequestMapping(path = "/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId")int questionId,
                             @RequestParam("content")String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUser_id(hostHolder.getUser().getId());
            } else {
                comment.setUser_id(WenDaUtils.ANONYMOUS_USERID);
            }
            comment.setCreated_date(new Date());
            comment.setEntity_type(EntityType.ENTITY_QUESTION);
            comment.setEntity_id(questionId);
            commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntity_id(),EntityType.ENTITY_QUESTION);
            questionService.updateCommentCount(comment.getEntity_id(),count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActor_id(comment.getUser_id())
                    .setEntity_id(questionId));
        }catch(Exception e){
            LOGGER.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }

}
