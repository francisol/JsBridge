package com.xiezt.javajsbridge;

/**
 * Created by xiez on 10/13/2016.
 */

public interface IConverter<T,C> {
    String request(T msg);
    C response(String msg,Class cls);
}
