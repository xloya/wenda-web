package com.xloya.wenda.controller;

import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventProducer;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.model.*;
import com.xloya.wenda.service.CommentService;
import com.xloya.wenda.service.FollowService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FollowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowController.class);
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;


    @RequestMapping(path = "/followUser",method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null)
            return WenDaUtils.getJSONString(999);

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActor_id(hostHolder.getUser().getId())
                .setEntity_id(userId)
                .setEntity_type(EntityType.ENTITY_USER)
                .setEntity_ownerid(userId));

        return WenDaUtils.getJSONString(ret ? 0 : 1,String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER,hostHolder.getUser().getId())));
    }

    @RequestMapping(path = "/unfollowUser",method = {RequestMethod.POST})
    @ResponseBody
    public String unFollowUser(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null)
            return WenDaUtils.getJSONString(999);

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActor_id(hostHolder.getUser().getId())
                .setEntity_id(userId)
                .setEntity_type(EntityType.ENTITY_USER)
                .setEntity_ownerid(userId));

        return WenDaUtils.getJSONString(ret ? 0 : 1,String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER,hostHolder.getUser().getId())));
    }

    @RequestMapping(path = "/followQuestion",method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId")int questionId){
        if(hostHolder.getUser()==null)
            return WenDaUtils.getJSONString(999);

        Question question = questionService.selectById(questionId);

        if(question==null)
            return WenDaUtils.getJSONString(1,"问题不存在");

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActor_id(hostHolder.getUser().getId())
                .setEntity_id(questionId)
                .setEntity_type(EntityType.ENTITY_QUESTION)
                .setEntity_ownerid(question.getUser_id()));

        Map<String,Object> info = new HashMap<>();
        info.put("headUrl",hostHolder.getUser().getHead_url());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));
        return WenDaUtils.getJSONString(ret ? 0 : 1,info);
    }

    @RequestMapping(path = "/unfollowQuestion",method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId")int questionId){
        if(hostHolder.getUser()==null)
            return WenDaUtils.getJSONString(999);

        Question question = questionService.selectById(questionId);

        if(question==null)
            return WenDaUtils.getJSONString(1,"问题不存在");

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActor_id(hostHolder.getUser().getId())
                .setEntity_id(questionId)
                .setEntity_type(EntityType.ENTITY_QUESTION)
                .setEntity_ownerid(question.getUser_id()));

        Map<String,Object> info = new HashMap<>();
        info.put("headUrl",hostHolder.getUser().getHead_url());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));

        return WenDaUtils.getJSONString(ret ? 0 : 1,info);
    }

    @RequestMapping(path = "/user/{uid}/followees",method = {RequestMethod.GET})
    public String followees(Model model,@PathVariable("uid")int userId){
        List<Integer> followeeIds = followService.getFollowees(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followees",getUsersInfo(hostHolder.getUser().getId(),followeeIds));
        }else{
            model.addAttribute("followees",getUsersInfo(0,followeeIds));
        }

        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));

        return "followees";
    }

    @RequestMapping(path = "/user/{uid}/followers",method = {RequestMethod.GET})
    public String followers(Model model,@PathVariable("uid")int userId){
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(),followerIds));
        }else{
            model.addAttribute("followers",getUsersInfo(0,followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        model.addAttribute("curUser", userService.getUser(userId));

        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localUserId,List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.setObjects("user", user);
            vo.setObjects("commentCount", commentService.getUserCommentCount(uid));
            vo.setObjects("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.setObjects("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, uid));
            if (localUserId != 0) {
                vo.setObjects("followed", followService.isFollower(EntityType.ENTITY_USER, uid, localUserId));
            } else {
                vo.setObjects("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
