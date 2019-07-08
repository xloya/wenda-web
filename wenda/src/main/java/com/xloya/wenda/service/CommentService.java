package com.xloya.wenda.service;


import com.xloya.wenda.dao.CommentDAO;
import com.xloya.wenda.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entity_id,int entity_type){
        return commentDAO.selectCommentByEntity(entity_id,entity_type);
    }

    public int addComment(Comment comment){
        // 敏感词过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entity_id,int entity_type){
        return commentDAO.getCommentCount(entity_id,entity_type);
    }

    public boolean deleteComment(int id){
       return commentDAO.updateStatusById(id,1) > 0;
    }
}
