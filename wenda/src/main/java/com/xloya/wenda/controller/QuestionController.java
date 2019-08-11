package com.xloya.wenda.controller;



import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventProducer;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.model.*;
import com.xloya.wenda.service.*;
import com.xloya.wenda.utils.WenDaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);


    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;


    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title, @RequestParam("content")String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreated_date(new Date());
            if(hostHolder.getUser()==null)
                return WenDaUtils.getJSONString(999);
            else
                question.setUser_id(hostHolder.getUser().getId());
            if(questionService.addQuestion(question) > 0) {
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActor_id(question.getUser_id()).setEntity_id(question.getId())
                        .setExt("title", question.getTitle()).setExt("content", question.getContent()));
                return WenDaUtils.getJSONString(0);
            }
        }catch (Exception e){
            LOGGER.error("增加问题失败"+e.getMessage());
        }
        return WenDaUtils.getJSONString(1,"失败");
    }

    @RequestMapping(path = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid")int qid){
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);

        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.setObjects("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.setObjects("liked", 0);
            } else {
                vo.setObjects("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            vo.setObjects("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.setObjects("user", userService.getUser(comment.getUser_id()));
            comments.add(vo);
        }

        model.addAttribute("comments", comments);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.setObjects("name", u.getName());
            vo.setObjects("head_url", u.getHead_url());
            vo.setObjects("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(EntityType.ENTITY_QUESTION, qid, hostHolder.getUser().getId()));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }
}
