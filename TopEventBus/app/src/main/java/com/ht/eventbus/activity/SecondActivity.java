package com.ht.eventbus.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ht.eventbus.R;
import com.ht.eventbus.core.Hermes;
import com.ht.eventbus.service.HermesService;

public class SecondActivity extends AppCompatActivity {
    IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Hermes.getDefault().connect(this, HermesService.class);
    }

    public void userManager(View view) {
//        userManager = Hermes.getDefault().getInstance(IUserManager.class);
        userManager = Hermes.getDefault().getObject(IUserManager.class);
        userManager.setPerson(new Person("XiaoXiao", "123456"));
        Person person = userManager.getPerson();
        Toast.makeText(this, "Persion=" + person, Toast.LENGTH_SHORT).show();
    }
}
