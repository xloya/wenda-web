package com.xloya.wenda.controller;

import com.xloya.wenda.model.EntityType;
import com.xloya.wenda.model.HostHolder;
import com.xloya.wenda.service.LikeService;
import com.xloya.wenda.utils.WenDaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = "/like",method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser()==null){
            return WenDaUtils.getJSONString(999);
        }

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        return WenDaUtils.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = "/dislike",method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser()==null){
            return WenDaUtils.getJSONString(999);
        }

        long dislikeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        return WenDaUtils.getJSONString(0,String.valueOf(dislikeCount));
    }
}
