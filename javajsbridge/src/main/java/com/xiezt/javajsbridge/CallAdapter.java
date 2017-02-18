package com.xiezt.javajsbridge;

/**
 * Created by xiez on 10/14/2016.
 */

public class CallAdapter implements IAdapter<CallResult> {

    private JavaJsBridge mJavaJsBridge;
    private String mMethod;
    private Object[] mArgs;
    private Class mResultType;
    @Override
    public CallResult factory(JavaJsBridge javaJsBridge) {
        mJavaJsBridge=javaJsBridge;
        CallResult result= new CallResult(javaJsBridge);
        result.parse(mMethod,mArgs,mResultType);
        return result;

    }

    @Override
    public IAdapter parse(String method, Object[] args,Class resultType) {
        mMethod=method;
        mArgs=args;
        mResultType=resultType;
        return this;
    }


    @Override
    public Class<CallResult> getTypeClass() {
       return CallResult.class;
    }
}
