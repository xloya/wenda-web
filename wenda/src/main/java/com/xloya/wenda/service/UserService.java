package com.xloya.wenda.service;


import com.xloya.wenda.dao.LoginTicketDAO;
import com.xloya.wenda.dao.UserDAO;
import com.xloya.wenda.model.LoginTicket;
import com.xloya.wenda.model.User;
import com.xloya.wenda.utils.WenDaUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;


import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }
    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public Map<String, Object> register(String username, String password) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(WenDaUtils.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(userDAO.selectByName(user.getName()).getId());
        map.put("ticket",ticket);

        return map;
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public Map<String, Object> login(String username, String password) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }

        if(!WenDaUtils.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        map.put("userId", user.getId());

        return map;
    }

    /**
     * 分发loginticket
     * @param user_id 用户id
     * @return
     */
    public String addLoginTicket(int user_id){

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUser_id(user_id);
        Date now = new Date();
        now.setTime(3600*24*100 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);

        return loginTicket.getTicket();
    }


    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}

