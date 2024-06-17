package com.block.postingapp.model;

import org.xmlpull.v1.XmlPullParser;

public class User {

    public String email;
    public String password;

    public User(){

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
