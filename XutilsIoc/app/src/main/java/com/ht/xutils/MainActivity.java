package com.ht.xutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ht.xutils.core.ContentView;
import com.ht.xutils.core.OnClick;
import com.ht.xutils.core.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.textview)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView.setText("测试后的内容");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @OnClick(R.id.button)
    public void buttonClick(View view) {
        Toast.makeText(this, "Button onClick", Toast.LENGTH_LONG).show();
    }
}
