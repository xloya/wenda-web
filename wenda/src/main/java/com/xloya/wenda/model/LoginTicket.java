package com.xloya.wenda.model;

import java.util.Date;

public class LoginTicket {
    private int Id;
    private int user_id;
    private Date expired;
    private int status;
    private String ticket;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "Id=" + Id +
                ", user_id=" + user_id +
                ", expired=" + expired +
                ", status=" + status +
                ", ticket='" + ticket + '\'' +
                '}';
    }
}
