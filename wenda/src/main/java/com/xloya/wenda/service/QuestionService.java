package com.xloya.wenda.service;


import com.xloya.wenda.dao.QuestionDAO;
import com.xloya.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;


    public Question selectById(int id){
        return questionDAO.selectById(id);
    }

    public int addQuestion(Question question){
        // 敏感词过滤
        // html过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        // 字典树敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return  questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }


    public List<Question> getLatestQuestions(int user_id, int offset, int limit) {
        return questionDAO.selectLatestQuestion(user_id,offset,limit);
    }
    public void updateCommentCount(int id, int comment_count){
        questionDAO.updateCommentCount(id,comment_count);
    }
}
