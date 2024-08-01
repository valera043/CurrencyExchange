package com.currencyexchangeweb.util;
import com.google.gson.Gson;
public class JsonConverter {
    public static String convertToJson(Object object) {
        Gson gson= new Gson();
        return gson.toJson(object);
    }
}
