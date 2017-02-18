package com.xiezt.javajsbridge;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.webkit.WebView;

import com.xiezt.javajsbridge.action.Failure;
import com.xiezt.javajsbridge.action.Success;

/**
 * Created by xiez on 10/14/2016.
 */

public class CallResult<T extends Object> implements BaseResult {
    private long mOutTimes=3000;
    private JavaJsBridge mJavaJsBridge;
    private String mMethod;
    private Object[] mArgs;
    private String mContent;
    private Success<T> mSuccess;
    private Failure mFailure;
    private Class mResultType;

    protected CallResult(JavaJsBridge javaJsBridge){
        mJavaJsBridge=javaJsBridge;
    }

    public long getmOutTimes() {
        return mOutTimes;
    }

    public void setmOutTimes(long mOutTimes) {
        this.mOutTimes = mOutTimes;
    }

    public void call(){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("javascript:");
        stringBuffer.append(mMethod);
        stringBuffer.append("( ");
        for(Object o:mArgs){
            stringBuffer.append(mJavaJsBridge.getConverter().request(o));
            stringBuffer.append(',');
        }
        stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
        stringBuffer.append(')');
        mContent=stringBuffer.toString();
            mJavaJsBridge.load(this);
    }



    public CallResult<T> ifSuccess(Success<T> success){
        mSuccess=success;
        return this;
    }

    public CallResult<T> ifError(Failure failure){
        mFailure=failure;
        return this;
    }

    public void parse(String method, Object[] args,Class resultType) {
        mMethod=method;
        mArgs=args;
        mResultType=resultType;
    }

    @Override
    public String script() {
        return mContent;
    }

    @Override
    public void callback(String value) {
        if (value == null){
            mSuccess.success(null);
        }
        try{
            if (mResultType== Integer.class){
                mSuccess.success((T) mResultType.cast(Integer.parseInt(value)));
            }else if (mResultType==Long.class) {
                mSuccess.success((T) mResultType.cast(Long.parseLong(value)));
            }else if (mResultType==Float.class) {
                mSuccess.success((T) mResultType.cast(Float.parseFloat(value)));
            }else if (mResultType==Double.class) {
                mSuccess.success((T) mResultType.cast(Double.parseDouble(value)));
            }else if (mResultType==String.class){
                mSuccess.success((T) value);
            }else if (mResultType==Boolean.class){
                mSuccess.success((T) Boolean.valueOf(value));
            }else {
                mSuccess.success((T) mJavaJsBridge.getConverter().response(value,mResultType));
            }
        }catch (Exception e){
            if (mFailure!=null){
                mFailure.failure(e);
            }}

    }
}
