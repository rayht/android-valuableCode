package com.ht.eventbus.activity;

/**
 * Created by Administrator on 2018/5/23.
 */

public class Person {
    String name;
    String password;

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
