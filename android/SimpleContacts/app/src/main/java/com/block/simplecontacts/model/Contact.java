package com.block.simplecontacts.model;

import java.io.Serializable;

public class Contact implements Serializable {

    public String name;
    public String phone;

    public Contact(){

    }

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
