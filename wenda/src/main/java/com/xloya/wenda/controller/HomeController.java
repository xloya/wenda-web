package com.xloya.wenda.controller;

import com.xloya.wenda.model.Question;
import com.xloya.wenda.model.ViewObject;
import com.xloya.wenda.service.QuestionService;
import com.xloya.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    /**
     * 获取特定用户的问题
     *
     * @param model   装填用户前10个问题
     * @param user_id 用户id
     * @return 用户问题页面
     */
    @RequestMapping(path = {"/user/{user_id}"}, method = {RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("user_id") int user_id) {
        try {
            model.addAttribute("vos", getQuestions(user_id, 0, 10));
        } catch (Exception e) {
            LOGGER.error("获取用户问题异常" + e.getMessage());
        }
        return "index";
    }

    /**
     * 首页
     *
     * @param model 装填前10个问题
     * @return 主页
     */
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model) {
        try {
            model.addAttribute("vos", getQuestions(0, 0, 10));
        } catch (Exception e) {
            LOGGER.error("获取问题异常" + e.getMessage());
        }
        return "index";
    }

    /**
     * 获取问题方法
     *
     * @param user_id 用户id
     * @param offset  起始游标
     * @param limit   偏移游标
     * @return 返回用户前10个问题
     */
    private List<ViewObject> getQuestions(int user_id, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(user_id, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject viewObject = new ViewObject();
            viewObject.setObjects("question", question);
            viewObject.setObjects("user", userService.getUser(question.getUser_id()));
            vos.add(viewObject);
        }
        return vos;
    }
}
