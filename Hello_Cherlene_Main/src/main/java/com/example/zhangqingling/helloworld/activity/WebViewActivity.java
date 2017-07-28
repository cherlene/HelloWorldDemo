package com.example.zhangqingling.helloworld.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhangqingling.helloworld.R;
import com.example.zhangqingling.helloworld.js.AndroidtoJs;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleView;
    private RelativeLayout mWebViewLayout;
    private WebView mWebView;
    private Button mLoadJSBtn;
    private Button mJsCallAndroidBtn;

    private WebSettings mWebSettings;
    private WebViewClient mWebViewClient;
    private WebChromeClient mWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        mTitleView = (TextView)findViewById(R.id.title_id);
        mLoadJSBtn = (Button)findViewById(R.id.load_js_btn_id);
        mLoadJSBtn.setOnClickListener(this);
        mJsCallAndroidBtn = (Button)findViewById(R.id.js_call_android_id);
        mJsCallAndroidBtn.setOnClickListener(this);
        mWebViewLayout = (RelativeLayout)findViewById(R.id.webview_layout_id);
        mWebView = new WebView(getApplicationContext());
        mWebViewLayout.addView(mWebView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initWebConfig();

        mWebView.loadUrl("file:///android_asset/javascript.html");
    }

    private void initWebConfig() {
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("zql", "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("zql", "onPageFinished");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        };
        mWebView.setWebViewClient(mWebViewClient);

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        });
        mWebChromeClient = new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e("zql", "title:" + title);
                mTitleView.setText(title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("Alter")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false);
                builder.create().show();
                return true;
            }
        };
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    private void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if(mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_js_btn_id:
                androidCallJSFunc();
                break;
            case R.id.js_call_android_id:
                jsCallAndroid();
                break;
        }
    }

    private void androidCallJSFunc() {
        if(Build.VERSION.SDK_INT < 18) {
            mWebView.loadUrl("javascript:callJS()");
        } else {
            mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.e("zql", value);
                }
            });
        }
    }

    private void jsCallAndroid() {
        //通过addJavascriptInterface将java对象映射到js对象
        //参数1：Javascript对象名;参数2：Java对象名
        mWebView.addJavascriptInterface(new AndroidtoJs(), "test");
        mWebView.loadUrl("file:///android_asset/jscallandroid.html");
    }
}
