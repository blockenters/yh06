package com.block.employer.model;

import java.io.Serializable;

public class Employer implements Serializable {

    public int id;
    public String name;
    public int salary;
    public int age;

    public Employer(){

    }
    public Employer(int id, String name, int salary, int age) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
    }
}



