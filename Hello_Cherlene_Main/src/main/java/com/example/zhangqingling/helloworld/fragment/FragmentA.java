package com.example.zhangqingling.helloworld.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by zhangqingling on 2016/4/29.
 */
public class FragmentA extends Fragment {
    private static final String Tag = "FragmentAAAAAA";

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
