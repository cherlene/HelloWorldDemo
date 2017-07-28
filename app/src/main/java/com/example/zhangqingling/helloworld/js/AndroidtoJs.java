package com.example.zhangqingling.helloworld.js;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by zhangqingling on 2017/7/28
 * For company 平安健康互联网股份有限公司上海分公司
 * E-Mail: zhangqingling@jk.cn
 * Copyright: Copyright (c) 2017<p/>
 * Title: AndroidtoJs<p/>
 * Description:
 */
public class AndroidtoJs {
    //被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        Log.e("zql", "JS 调用了android的hello方法");
    }
}
