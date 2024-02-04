package com.example.PocSaldoTransferencia.transferenciaStatus.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    private Gson gson;

    public JsonUtil(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    public <T> String toJson(T object){
        return gson.toJson(object);
    }

    public <T> T toObject(String json, Class<T> clazz){
        T object = (T) gson.fromJson(json, clazz);
        return object;
    }
}
