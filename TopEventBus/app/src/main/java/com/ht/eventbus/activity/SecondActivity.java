package com.ht.eventbus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.ht.eventbus.R;
import com.ht.eventbus.core.Hermes;
import com.ht.eventbus.service.HermesService;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class SecondActivity extends Activity {
    IUserManager userManager;
//    IDownManager downManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Hermes.getDefault().connect(this, HermesService.class);
    }

    public void userManager(View view) {
//         userManager  进程A的副本
        userManager = Hermes.getDefault().getInstance(IUserManager.class);
        Toast.makeText(this,"-----> ", Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"-----> "+userManager.getPerson(), Toast.LENGTH_SHORT).show();
//        downManager=Hermes.getDefault().getInstance(IDownManager.class);
    }
    public void getUser(View view) {
//        userManager.setPerson(new com.dongnao.eventbus.Person("lance","789"));
//        Toast.makeText(this,"-----> "+downManager.getFileRecord(), Toast.LENGTH_SHORT).show();
    }
}
