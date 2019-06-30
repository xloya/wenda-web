package com.xloya.wenda.mapper;


import com.xloya.wenda.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    String table_name="question";
    String insert_fields="title,content,user_id,create_time,comment_count";
    String select_fields="id,"+insert_fields;

    @Insert("insert into "+table_name+"("+insert_fields+") values(#{title},#{content},#{user_id},#{create_time},#{comment_count})")
    int addQuestion(Question question);

    @Select("<script>" +
            "select "+select_fields+" from "+table_name+
            "<if test='user_id!=0'>" +
            " where user_id = #{user_id} " +
            "</if>" +
            "order by id desc limit #{offset},#{limit}" +
            "</script>")
    List<Question> selectLatestQuestion(@Param("user_id") int user_id,
                                        @Param("offset") int offset,
                                        @Param("limit")  int limit);

    @Update("update "+table_name+" set content=#{content} where id=#{id}")
    void updateContent(Question question);

    @Delete("delete from "+table_name+" where id=#{id}")
    void deleteById(int id);
}
