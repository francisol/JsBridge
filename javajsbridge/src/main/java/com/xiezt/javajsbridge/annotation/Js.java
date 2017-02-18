package com.xiezt.javajsbridge.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by xiez on 10/13/2016.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Js {
    String method();
}
