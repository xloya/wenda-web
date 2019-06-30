package com.xloya.wenda.mapper;

import com.xloya.wenda.model.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {
    String table_name="user";
    String insert_fields="name,password,salt,head_url";
    String select_fields="id,"+insert_fields;

    @Insert("insert into "+table_name+"("+insert_fields+") values(#{name},#{password},#{salt},#{head_url})")
    int addUser(User user);

    @Select("select "+select_fields+" from "+table_name+" where id = #{id}")
    User selectById(int id);

    @Update("update "+table_name+" set password=#{password} where id=#{id}")
    void updatePassword(User user);

    @Delete("delete from "+table_name+" where id=#{id}")
    void deleteById(int id);
}
