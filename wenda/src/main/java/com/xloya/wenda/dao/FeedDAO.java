package com.xloya.wenda.dao;


import com.xloya.wenda.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FeedDAO {
    String table_name="feed";
    String insert_fields="created_date,user_id,data,type";
    String select_fields="id,"+insert_fields;

    @Insert("insert into "+table_name+"("+insert_fields+") values(#{created_date},#{user_id},#{data},#{type})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addFeed(Feed feed);


    @Select("select "+select_fields+" from "+table_name+" where id = #{id}")
    Feed getFeedById(int id);

    @Select("<script>"+
            "select "+select_fields+" from "+table_name+
            "  where id &lt; #{maxId}"+
            "<if test='userIds.size()!=0'>"+
            " and user_id in  "+
            " <foreach item=\"item\" index=\"index\" collection=\"userIds\" "+
            " open=\"(\" separator=\",\" close=\")\">"+
            " #{item}"+
            " </foreach>"+
            "</if>"+
            "order by id desc LIMIT #{count}"+
            "</script>")
    List<Feed> selectUserFeeds(@Param("maxId")int maxId,
                               @Param("userIds")List<Integer> userIds,
                               @Param("count")int count);

}
