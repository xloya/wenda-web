package com.xloya.wenda.dao;



import com.xloya.wenda.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String table_name="message";
    String insert_fields="from_id,to_id,content,created_date,has_read,conversation_id";
    String select_fields="id,"+insert_fields;

    @Insert("insert into "+table_name+"("+insert_fields+") values(#{from_id},#{to_id},#{content},#{created_date},#{has_read},#{conversation_id})")
    int addMessage(Message message);


    @Select("select "+select_fields+" from "+table_name+
            " where conversation_id = #{conversation_id} order by created_date desc limit #{offset},#{limit} ")
    List<Message> getConversationDetail(@Param("conversation_id") String conversation_id,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select("select "+insert_fields+" , count(id) as id from (select * from "+table_name+
            "  where from_id = #{user_id} or to_id = #{user_id} order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit} ")
    List<Message> getConversationList(@Param("user_id") int user_id,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select("select count(id) from "+table_name+" where has_read = 0 and to_id = #{user_id} and conversation_id = #{conversation_id}")
    int getConversationUnreadCount(@Param("conversation_id") String conversation_id,
                                   @Param("user_id") int user_id);
}
