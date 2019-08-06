package com.xloya.wenda.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.xloya.wenda.async.EventHandler;
import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.model.*;
import com.xloya.wenda.service.*;
import com.xloya.wenda.utils.JedisAdapter;
import com.xloya.wenda.utils.RedisKeyUtil;
import com.xloya.wenda.utils.WenDaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    MessageService messageService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel eventModel){
        Map<String,String> map = new HashMap<>();
        User actor = userService.getUser(eventModel.getActor_id());
        if(actor==null)
            return null;
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHead_url());
        map.put("userName",actor.getName());

        if(eventModel.getEvent_type()==EventType.COMMENT||
                (eventModel.getEvent_type()==EventType.FOLLOW&&eventModel.getEntity_type()==EntityType.ENTITY_QUESTION)){
            Question question = questionService.selectById(eventModel.getEntity_id());
            if(question==null)
                return null;
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }

        return null;
    }
    @Override
    public void doHandle(EventModel eventModel) {
        Feed feed = new Feed();
        feed.setCreated_date(new Date());
        feed.setUser_id(eventModel.getActor_id());
        feed.setType(eventModel.getEvent_type().getValue());
        feed.setData(buildFeedData(eventModel));
        if(feed.getData()==null)
            return ;
        feedService.addFeed(feed);
        // 给所有粉丝推送
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,eventModel.getActor_id(),Integer.MAX_VALUE);
        followers.add(0);
        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            // 注意此处要修改sql语句的配置
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));

        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}
