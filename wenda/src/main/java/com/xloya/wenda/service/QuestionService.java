package com.xloya.wenda.service;


import com.xloya.wenda.dao.QuestionDAO;
import com.xloya.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDAO questionDAO;


    public List<Question> getLatestQuestions(int user_id, int offset, int limit) {
        return questionDAO.selectLatestQuestion(user_id,offset,limit);
    }
}
