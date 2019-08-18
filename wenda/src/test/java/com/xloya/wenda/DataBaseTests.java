package com.xloya.wenda;
/*
import com.xloya.wenda.dao.QuestionDAO;
import com.xloya.wenda.dao.UserDAO;
import com.xloya.wenda.model.Question;
import com.xloya.wenda.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WendaApplication.class)
public class DataBaseTests {


	@Autowired
	UserDAO userDAO;

	@Autowired
	QuestionDAO questionDAO;

	@Test
	public void UserTest() {
		User user = new User();
		user.setId(1);
		user.setPassword("333");
		userDAO.updatePassword(user);
	}

	@Test
	public void QuestionTest(){
		Question question = new Question();
		question.setTitle("测试");
		question.setContent("哈哈啊哈");
		question.setUser_id(1);
		question.setCreated_date(new Date());
		question.setComment_count(100);
		questionDAO.addQuestion(question);
		List<Question> list = questionDAO.selectLatestQuestion(1,1,1);
		System.out.println(list);
	}



}
*/