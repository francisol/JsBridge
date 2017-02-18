package com.xiezt.javajsbridge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by xiez on 10/13/2016.
 */

public class ConverterFactory {

    public static class JsonConverter implements IConverter<Object, Object> {
        private Gson mGson= new GsonBuilder().create();
        @Override
        public String request(Object msg) {
            if (msg instanceof Number){
                return msg.toString();
            }else if (msg instanceof CharSequence){
                return "'"+msg.toString()+"'";
            }else if (msg instanceof Boolean){
                return msg.toString();
            }
            return mGson.toJson(msg);
        }

        @Override
        public Object response(String msg,Class cls) {
            return mGson.fromJson(msg,cls);
        }
    }
}
