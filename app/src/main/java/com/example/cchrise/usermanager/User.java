package com.example.cchrise.usermanager;

/**
 * Created by C.Chrise on 08.04.2015.
 */
public class User {

    private int id;
    private String name;


    public User(int id, String name){
        this.name = name;
        this.id = id;

    }

    public int getId(){ return this.id; }

    public String getName(){ return this.name; }

}
