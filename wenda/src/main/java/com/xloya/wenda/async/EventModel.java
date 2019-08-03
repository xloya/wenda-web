package com.xloya.wenda.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType event_type;
    private int actor_id;
    private int entity_type;
    private int entity_id;
    private int entity_ownerid;

    private Map<String,String> exts = new HashMap<>();

    public EventModel(EventType eventType){
        this.event_type = eventType;
    }

    public EventModel(){}

    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getEvent_type() {
        return event_type;
    }

    public EventModel setEvent_type(EventType event_type) {
        this.event_type = event_type;
        return this;
    }

    public int getActor_id() {
        return actor_id;
    }

    public EventModel setActor_id(int actor_id) {
        this.actor_id = actor_id;
        return this;
    }

    public int getEntity_type() {
        return entity_type;
    }

    public EventModel setEntity_type(int entity_type) {
        this.entity_type = entity_type;
        return this;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public EventModel setEntity_id(int entity_id) {
        this.entity_id = entity_id;
        return this;
    }

    public int getEntity_ownerid() {
        return entity_ownerid;
    }

    public EventModel setEntity_ownerid(int entity_ownerid) {
        this.entity_ownerid = entity_ownerid;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "event_type=" + event_type +
                ", actor_id=" + actor_id +
                ", entity_type=" + entity_type +
                ", entity_id=" + entity_id +
                ", entity_ownerid=" + entity_ownerid +
                ", exts=" + exts +
                '}';
    }
}
