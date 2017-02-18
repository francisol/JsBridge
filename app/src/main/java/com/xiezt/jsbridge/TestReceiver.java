package com.xiezt.jsbridge;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.xiezt.javajsbridge.Receiver;

/**
 * Created by francis on 17-2-18.
 */
public class TestReceiver implements Receiver {
    private Context mContext;
    public TestReceiver(Context context) {
        mContext=context;
    }

    @Override
    public String getTag() {
        return "test";
    }
    @JavascriptInterface
    public void showMsg(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }


}
