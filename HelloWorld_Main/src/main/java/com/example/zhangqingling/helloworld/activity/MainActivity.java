package com.example.zhangqingling.helloworld.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zhangqingling.helloworld.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String Tag = "Fragment-MainActivity";
    private ViewPager mViewPager = null;
    private Button btn = null;
    private WebView webView = null;
    private GridView gridView = null;
    private Button mRxjavaBtn = null;
    private Button mNetworkActivityBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid_view);
        mNetworkActivityBtn = (Button)findViewById(R.id.start_network_activity_btn_id);
        mNetworkActivityBtn.setOnClickListener(this);
//        mViewPager = (ViewPager)findViewById(R.id.viewpager_id);

        getTotalMemory();
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTotalMemory();
                webView.loadUrl("http://www.baidu.com/");
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return false;
                    }
                });
            }
        });

        webView = (WebView)findViewById(R.id.webview);
        mRxjavaBtn = (Button)findViewById(R.id.rxjava_btn);
        mRxjavaBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rxjava_btn:
                doRxjavaTest();
                break;
            case R.id.start_network_activity_btn_id:
                startActivity(new Intent(this, NetworkActivity.class));
                break;
            default:
                break;
        }
    }

    private void doRxjavaTest() {
        List<String> list = new ArrayList<>();
        list.add(1+"");
        list.add(3+"");
        list.add(4+"");
        list.add(10+"");
        list.add(7+"");
        list.add(8+"");
        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object s) throws Exception {

                return Integer.parseInt((String)s) % 2 == 0;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println((String)o);
            }
        });
    }

    public static class MainFragmentAdapter extends FragmentPagerAdapter {

        private Context mContext = null;
        public MainFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    public void getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2="";
        String str3="/proc/cpuinfo";
        Log.i("zql","getTotalMemory");
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i("zqlCPU", "---" + str2);
            }
        } catch (IOException e) {
            Log.e("zql", "cannot read /proc/meminfo");
        }
        try {
            FileReader fr = new FileReader(str3);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i("zqlmemory", "---" + str2);
            }
        } catch (IOException e) {
            Log.e("zql", "cannot read /proc/cpuinfo");
        }
    }


}
