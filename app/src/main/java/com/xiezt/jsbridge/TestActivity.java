package com.xiezt.jsbridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.xiezt.javajsbridge.CallAdapter;
import com.xiezt.javajsbridge.CallResult;
import com.xiezt.javajsbridge.ConverterFactory;
import com.xiezt.javajsbridge.JavaJsBridge;
import com.xiezt.javajsbridge.Receiver;
import com.xiezt.javajsbridge.action.Failure;
import com.xiezt.javajsbridge.action.Success;

public class TestActivity extends AppCompatActivity {

    private JavaJsBridge mJavaJsBridge;
    private TestFunction mFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        WebView webView=(WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/html/index.html");
        mJavaJsBridge= new JavaJsBridge.Builder().adapter(new CallAdapter()).receive(new TestReceiver(this)).converter(new ConverterFactory.JsonConverter()).inject(webView).build();
        mFunction=mJavaJsBridge.create(TestFunction.class);


    }

    public void addClick(View view){
         mFunction.add(1,1).ifSuccess(new Success<Integer>() {
             @Override
             public void success(Integer value) {
                 Toast.makeText(getBaseContext(),value.toString(),Toast.LENGTH_LONG).show();
             }
         }).ifError(new Failure() {
             @Override
             public void failure(Throwable value) {

             }
         }).call();
    }

    public void show(View v){
        mFunction.showMsg("Hello").ifSuccess(new Success<Void>() {
            @Override
            public void success(Void value) {

            }
        }).ifError(new Failure() {
            @Override
            public void failure(Throwable value) {

            }
        }).call();
    }
}
