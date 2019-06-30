package com.xloya.wenda.dao;


import com.xloya.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDAO {
    String table_name="login_ticket";
    String insert_fields="user_id,ticket,expired,status";
    String select_fields="id,"+insert_fields;

    @Insert("insert into "+table_name+"("+insert_fields+") values(#{user_id},#{ticket},#{expired},#{status})")
    int addTicket(LoginTicket loginTicket);

    @Select("select "+select_fields+" from "+table_name+" where ticket = #{ticket}")
    LoginTicket selectByTicket(String ticket);

    @Update("update "+table_name+" set status = #{status} where ticket = #{ticket}")
    void updateStatus(@Param("ticket")String ticket,@Param("status")int status);
}
