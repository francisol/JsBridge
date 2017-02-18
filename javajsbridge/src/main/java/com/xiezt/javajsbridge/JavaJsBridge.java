package com.xiezt.javajsbridge;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by xiez on 10/13/2016.
 */

public class JavaJsBridge {

    private Receiver mReceiver;
    private IConverter mConverter;
    private WebView mWebView;
    private final Map<Method, BridgeMethod> serviceMethodCache = new ConcurrentHashMap<>();
    private JavaJsBridge(){}
    private IAdapter mAdapter;
    private boolean mCallable=false;

    private final LinkedList<BaseResult> mBridgeMethods=new LinkedList<>();


    public IAdapter getAdapter(){
        return mAdapter;
    }


    public <T> T create(final Class<T> cls){
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                BridgeMethod serviceMethod = loadServiceMethod(method);
                return serviceMethod.method(args);
            }
        });
    }

    BridgeMethod loadServiceMethod(Method method) {
        BridgeMethod result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new BridgeMethod.Builder(this,method).build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }



    public IConverter getConverter() {
        return mConverter;
    }

    public void load(final BaseResult baseResult) {
        if (mCallable) {
            loadScript(baseResult);
        }else {
            mBridgeMethods.add(baseResult);
        }
    }

    private void loadScript(BaseResult baseResult){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            loadScriptWithCallBack(baseResult);
        }else {
            loadScriptWithoutCallBack(baseResult);
        }
    }

    private void loadScriptWithoutCallBack(final BaseResult baseResult){
        mWebView.loadUrl(baseResult.script());
        System.gc();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadScriptWithCallBack(final BaseResult baseResult){
        mWebView.evaluateJavascript(baseResult.script(), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                baseResult.callback(value);
                System.gc();
            }
        });
    }


    public final static class Builder{
        private JavaJsBridge mJavaJsBridge;

        public Builder() {
            mJavaJsBridge=new JavaJsBridge();
        }

        public  Builder receive(Receiver receiver){
            mJavaJsBridge.mReceiver=receiver;
            return this;
        }
        public Builder converter(IConverter converter){
            mJavaJsBridge.mConverter=converter;
            return this;
        }
        public Builder inject(WebView webView){
            mJavaJsBridge.mWebView=webView;
            return this;
        }

        public Builder adapter(IAdapter adapter){
            mJavaJsBridge.mAdapter=adapter;
            return this;
        }

        public JavaJsBridge build(){

            return mJavaJsBridge.finish();
        }
    }

    private JavaJsBridge finish() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(mReceiver, mReceiver.getTag());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                while (mBridgeMethods.size()>0) {
                    loadScript(mBridgeMethods.removeFirst());
                }
                mCallable=true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        return this;
    }
}
