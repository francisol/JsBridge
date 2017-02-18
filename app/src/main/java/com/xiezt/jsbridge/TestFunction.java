package com.xiezt.jsbridge;

import com.xiezt.javajsbridge.CallResult;
import com.xiezt.javajsbridge.annotation.Js;

/**
 * Created by francis on 17-2-17.
 */

public interface TestFunction {
    @Js(method = "show")
    CallResult<Void> showMsg(String msg);
    @Js(method = "add")
    CallResult<Integer> add(int a,int b);
}
