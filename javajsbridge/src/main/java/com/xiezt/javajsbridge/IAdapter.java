package com.xiezt.javajsbridge;

/**
 * Created by xiez on 10/14/2016.
 */

public interface IAdapter<T> {
     T factory(JavaJsBridge javaJsBridge);
    IAdapter parse(String method,Object[] args,Class resultType);
    Class<T> getTypeClass();
}
