package com.ht.eventbus.activity;


import com.ht.eventbus.annotion.ClassId;

/**
 * Created by Xiaofei on 16/4/25.
 */
//接口的方式  描述 一个类
@ClassId("com.ht.eventbus.UserManager")
public interface IUserManager {

    public Person getPerson();

    public void setPerson(Person person);
}
