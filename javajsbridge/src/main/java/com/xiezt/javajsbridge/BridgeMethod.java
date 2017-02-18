package com.xiezt.javajsbridge;

import com.xiezt.javajsbridge.annotation.Js;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Created by xiez on 10/14/2016.
 */

public class BridgeMethod {
    private Builder mBuilder;
    private String mContent;

    public  BridgeMethod(Builder builder) {
        mBuilder=builder;
    }

    public BaseResult method(Object[] args) {
        IAdapter adapter= mBuilder.mJavaJsBridge.getAdapter();
       return (BaseResult) adapter.parse(mBuilder.mJsMethod,args, (Class) mBuilder.mResultType.getActualTypeArguments()[0]).factory(mBuilder.mJavaJsBridge);
    }

    static final class Builder {


        private final Method mMethod;
        private final Annotation[] mMethodAnnotations;
        private String mJsMethod;
        private JavaJsBridge mJavaJsBridge;
        private ParameterizedType mResultType;

        public Builder(JavaJsBridge javaJsBridge, Method method) {
            mJavaJsBridge=javaJsBridge;
            this.mMethod = method;
            this.mMethodAnnotations = method.getAnnotations();

        }

        public BridgeMethod build() {
            for (Annotation annotation : mMethodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            mResultType = (ParameterizedType) mMethod.getGenericReturnType();
            if(mJavaJsBridge.getAdapter().getTypeClass()!=mResultType.getRawType()){

            }
            return new BridgeMethod(this);
        }


        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof Js) {
                mJsMethod = ((Js) annotation).method();
            }
        }
    }
}
