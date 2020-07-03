package com.app.audiobook.component;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JSONManager {


    public static String exportToJSON(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T importFromJSON(String jsonStr, Class<T> tClass) {
        Gson gson = new Gson();

        return gson.fromJson(jsonStr, tClass);
    }

    public static <T> T importFromJSON(String jsonStr, Type type) {
        Gson gson = new Gson();

        return gson.fromJson(jsonStr, type);
    }

}
