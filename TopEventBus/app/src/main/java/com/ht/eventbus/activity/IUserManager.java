package com.ht.eventbus.activity;


import com.ht.eventbus.annotion.ClassId;

@ClassId("com.ht.eventbus.activity.UserManager")
public interface IUserManager {

    IUserManager getUserManager();

    public Person getPerson();

    public void setPerson(Person person);
}
