package com.ht.xutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ht.xutils.core.InjectUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);
    }

}
