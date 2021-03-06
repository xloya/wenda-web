package com.xloya.wenda.controller;


import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventProducer;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    /**
     * 注册
     * @param model
     * @return 主页
     */
    @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                                    @RequestParam("password") String password,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      @RequestParam(value = "next",required = false) String next,
                      HttpServletResponse response){

        try {
            Map<String, Object> map = userService.register(username, password);

            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(!StringUtils.isEmpty(next))
                    return "redirect:"+next;
                return "redirect:/";
            }else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            LOGGER.error("注册异常"+e.getMessage());
            return "login";
        }
    }

    /**
     * 登录
     * @param model
     * @return 主页
     */
    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value = "rememberme",defaultValue = "false")boolean rememberme,
                        HttpServletResponse response){

        try {
            Map<String, Object> map = userService.login(username, password);

            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                /* 登录异常处理器
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("email","xxxx")
                        .setExt("username",username)
                        .setActor_id((int)map.get("userId")));
                */
                if(!StringUtils.isEmpty(next))
                    return "redirect:"+next;
                return "redirect:/";
            }else{
                model.addAttribute("msg", map.get("msg"));

                return "login";
            }
        }
        catch (Exception e){
            LOGGER.error("登录异常"+e.getMessage());

            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"},method = {RequestMethod.GET})
    public String reglogin(Model model, @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";

    }

    /**
     * 退出
     * @param
     * @return 主页
     */
    @RequestMapping(path = {"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket")String ticket) {

        userService.logout(ticket);

        return "redirect:/";
    }

}
