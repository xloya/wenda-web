package com.xloya.wenda.dao;


import com.xloya.wenda.model.Comment;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface CommentDAO {
    String table_name="comment";
    String insert_fields="content,user_id,entity_id,entity_type,created_date,status";
    String select_fields="id,"+insert_fields;

    @Insert("insert into "+table_name+"("+insert_fields+") values(#{content},#{user_id},#{entity_id},#{entity_type},#{created_date},#{status})")
    int addComment(Comment comment);


    @Select("select "+select_fields+" from "+table_name+
            " where entity_id = #{entity_id} and entity_type = #{entity_type} order by created_date desc")
    List<Comment> selectCommentByEntity(@Param("entity_id") int entity_id,
                                        @Param("entity_type") int entity_type);

    @Select("select count(id) from "+table_name+
            " where entity_id = #{entity_id} and entity_type = #{entity_type}")
    int getCommentCount(@Param("entity_id") int entity_id,
                        @Param("entity_type") int entity_type);

    @Update("update "+table_name+" set status=#{status} where id=#{id}")
    int updateStatusById(@Param("id") int id,@Param("status") int status);
}
