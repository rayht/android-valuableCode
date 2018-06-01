package com.ht.eventbus.activity;


/**
 * Created by Xiaofei on 16/4/25.
 */

public class UserManager implements IUserManager{
    Person person;
    private static UserManager sInstance = null;

    private UserManager() {

    }
//约定这个进程A  单例对象的     规则    getInstance()
    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
