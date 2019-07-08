package com.xloya.wenda.controller;



import com.xloya.wenda.model.*;
import com.xloya.wenda.service.CommentService;
import com.xloya.wenda.service.QuestionService;
import com.xloya.wenda.service.UserService;
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
    HostHolder hostHolder;

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
            if(questionService.addQuestion(question) > 0)
                return WenDaUtils.getJSONString(0);
        }catch (Exception e){
            LOGGER.error("增加问题失败"+e.getMessage());
        }
        return WenDaUtils.getJSONString(1,"失败");
    }

    @RequestMapping(path = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid")int qid){

        Question question = questionService.selectById(qid);
        model.addAttribute("question",question);

        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for(Comment comment : commentList){
            ViewObject viewObject = new ViewObject();
            viewObject.setObjects("comment",comment);
            viewObject.setObjects("user",userService.getUser(comment.getUser_id()));
            comments.add(viewObject);
        }
        model.addAttribute("comments",comments);

        return "detail";
    }
}
