package com.ht.eventbus.activity;

public class UserManager implements IUserManager{
    Person person;
    private static UserManager sInstance = null;

    private UserManager() {

    }
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
