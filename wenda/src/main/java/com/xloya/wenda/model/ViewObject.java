package com.xloya.wenda.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    private Map<String,Object> objects = new HashMap<>();

    public void setObjects(String key,Object object){
        objects.put(key,object);
    }

    public Object getObjects(String key){
        return objects.get(key);
    }
}
