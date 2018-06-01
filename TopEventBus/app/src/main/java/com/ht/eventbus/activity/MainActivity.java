package com.ht.eventbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ht.eventbus.EventBus;
import com.ht.eventbus.R;
import com.ht.eventbus.Subscribe;
import com.ht.eventbus.core.Hermes;

public class MainActivity extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        Hermes.getDefault().init(this);
        Hermes.getDefault().register(UserManager.class);
        UserManager.getInstance().setPerson(new Person("Leiht", "881129"));
//        Hermes.getDefault().register(DownManager.class);
//        DownManager.getInstance().setFileRecord(new FileRecord("/sdcard/0",12344));
    }

    public void change(View view)
    {
        startActivity(new Intent(this,SecondActivity.class));
    }
//主线程---子线程接受
//    public void receive(Friend friend ) {
//        Log.i("david", "thread: " + Thread.currentThread().getName());
//    }

    public void postMsg(View view) {
        EventBus.getDefault().post("Hello Event");
    }

    @Subscribe
    public void receiveMsg(String msg) {
        Toast.makeText(this,"Receive post msg " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    public void getPerson(View view) {
//        Toast.makeText(this,"----->  "+UserManager.getInstance().getPerson(), Toast.LENGTH_SHORT).show();
//    }
}
