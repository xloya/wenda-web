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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;



    @RequestMapping(path = "/search",method = {RequestMethod.GET})
    public String search(Model model,@RequestParam("q")String keyword,
                         @RequestParam(value = "offset",defaultValue = "0")int offset,
                         @RequestParam(value = "count",defaultValue = "10")int count){
        try {
            List<Question> questionList = searchService.searchQuestion(keyword,offset,count,"<em>","</em>");
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                Question q = questionService.selectById(question.getId());
                ViewObject viewObject = new ViewObject();
                if(question.getContent()!=null){
                    q.setContent(question.getContent());
                }

                if(question.getTitle()!=null){
                    q.setTitle(question.getTitle());
                }
                viewObject.setObjects("question", q);
                viewObject.setObjects("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                viewObject.setObjects("user", userService.getUser(q.getUser_id()));
                vos.add(viewObject);
            }
            model.addAttribute("vos",vos);
        }catch(Exception e){
            LOGGER.error("搜索评论失败"+e.getMessage());
        }
        return "result";
    }

}
