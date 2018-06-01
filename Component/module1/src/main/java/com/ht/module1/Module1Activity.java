package com.ht.module1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class Module1Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
    }
    //    String msg;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_module2);
//        DNRouter.getInstance().inject(this);
//        Log.i("module2", "我是模块2:" + msg);
//
//        //当处于组件模式的时候
//        if (BuildConfig.isModule){
//            TestService testService = (TestService) DNRouter.getInstance().build("/main/service1")
//                    .navigation();
//            testService.test();
//
//            TestService testService1 = (TestService) DNRouter.getInstance().build("/main/service2")
//                    .navigation();
//            testService1.test();
//
//            TestService testService2 = (TestService) DNRouter.getInstance().build("/module1/service")
//                    .navigation();
//            testService2.test();
//
//            TestService testService3 = (TestService) DNRouter.getInstance().build("/module2/service")
//                    .navigation();
//            testService3.test();
//        }
//
//    }
//
//    public void mainJump(View view) {
//        if (BuildConfig.isModule){
//            DNRouter.getInstance().build("/main/test").withString("a",
//                    "从Module2").navigation(this);
//        }else{
//            Toast.makeText(this,"当前处于组件模式,无法使用此功能",0).show();
//        }
//    }
//
//    public void module1Jump(View view) {
//        DNRouter.getInstance().build("/module1/test").withString("msg",
//                "从Module2").navigation(this);
//    }
}
