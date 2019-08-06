package com.xloya.wenda.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Feed {
    private int id;
    private int type;
    private int user_id;
    private Date created_date;
    // json
    private String data;
    private JSONObject dataJSON = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", type=" + type +
                ", user_id=" + user_id +
                ", created_date=" + created_date +
                ", data='" + data + '\'' +
                '}';
    }
}
