package com.example.zhangqingling.helloworld.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhangqingling.helloworld.R;
import com.example.zhangqingling.helloworld.entity.DownloadEntity;

import java.io.File;
import java.util.ArrayList;

public class NetworkActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText = null;
    Button mDownloadBtn = null;
    DownloadManager mDm = null;
    long downloadId = 0;
    BroadcastReceiver mDownloadBroadcast = null;
    ArrayList<DownloadEntity> mDownloadList = null;
    Button mInstallApkBtn = null;
    Button mWebViewBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        initView();
        initData();
    }

    private void initView() {
        editText = (EditText)findViewById(R.id.edit_text_id);
        mDownloadBtn = (Button)findViewById(R.id.download_btn_id);
        mDownloadBtn.setOnClickListener(this);
        mInstallApkBtn = (Button)findViewById(R.id.install_apk_btn_id);
        mInstallApkBtn.setOnClickListener(this);
        mWebViewBtn = (Button)findViewById(R.id.webview_btn_id);
        mWebViewBtn.setOnClickListener(this);
    }

    private void initData() {
        mDm = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadList = new ArrayList<>();

//        registerDownloadBroadcast();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_btn_id:
                doDownloadManagerDemo();
                break;
            case R.id.install_apk_btn_id:
                Intent apkIntent = new Intent(Intent.ACTION_VIEW);
                apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                apkIntent.setDataAndType(Uri.fromFile(new File("/storage/emulated/0/Download/personaldoc_test_V_5.2.0_549_false.apk")),
                        "application/vnd.android.package-archive");
                startActivity(apkIntent);
                break;

            case R.id.webview_btn_id:
                startActivity(new Intent(this, WebViewActivity.class));
                break;

            default:
                break;
        }
    }

    private void doDownloadManagerDemo() {
        String url = editText.getText().toString();
        if(url != null && !url.isEmpty()) {
            //下载文件
            if(url.endsWith(".apk")) {
                String fileName = url.substring(url.lastIndexOf("/")+1);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                downloadId = mDm.enqueue(request);
                DownloadEntity entity = new DownloadEntity();
                entity.downloadId  = downloadId;
                entity.fileName = fileName;
                entity.path =  getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName;
                mDownloadList.add(entity);
            }
        }
    }

    private void registerDownloadBroadcast() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        if(mDownloadBroadcast == null) {
            mDownloadBroadcast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if( id != -1) {
                        for(DownloadEntity entity : mDownloadList) {
                            if(entity.downloadId == id) {
                                //安装apk
                                Intent apkIntent = new Intent(Intent.ACTION_VIEW);
                                apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                apkIntent.setDataAndType(Uri.parse("file://" + entity.path), "application/vnd.android.package-archive");
                                startActivity(apkIntent);
                            }
                        }
                    }
                    //从list 中删除已经下载完成的apk
                    //相同文件下载，怎么办？
                }
            };
        }

        registerReceiver(mDownloadBroadcast, filter);
    }

    private void unRegisterDownloadBroadcast() {
        if(mDownloadBroadcast != null) {
            unregisterReceiver(mDownloadBroadcast);
        }
    }

    @Override
    protected void onDestroy() {
//        unRegisterDownloadBroadcast();
        super.onDestroy();
    }
}
