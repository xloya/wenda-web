package com.xloya.wenda.interceptor;


import com.xloya.wenda.dao.LoginTicketDAO;
import com.xloya.wenda.dao.UserDAO;
import com.xloya.wenda.model.HostHolder;
import com.xloya.wenda.model.LoginTicket;
import com.xloya.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * 在controller处理前拦截所有请求进行身份认证
 */
@Component
public class PassportIntercepter implements HandlerInterceptor {

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    //请求预处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        if(request.getCookies()!=null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }


        if(ticket!=null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);

            // 判断ticket是否为空、是否超时、是否状态不正确
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
                return true;
            }

            //存入HostHolder
            User user = userDAO.selectById(loginTicket.getUser_id());
            hostHolder.setUsers(user);
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null){
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
