package com.example.maebaldwin.petdaycare;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pengchong on 4/25/17.
 */

public class Account implements Serializable{
    private int user_id;
    private String user_name;
    private String phone_num;
    private String email;
    private String hometown;
    private ArrayList<String> pets;


    public int getId() {
        return user_id;
    }

    public void setId(int id) {
        this.user_id = id;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getPhone() {
        return phone_num;
    }

    public void setPhone(String phone) {
        this.phone_num = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    // TODO:
    public void addPet(String pet) {
        pets.add(pet);
    }

    public Account(){

    }

    public Account(String name, String phone, String email, String hometown) {
        super();
        this.user_name = name;
        this.phone_num = phone;
        this.email = email;
        this.hometown = hometown;
    }

    public String toString(){
        return "ACCOUNT: " + this.getName() + " ID: " + this.getId() + " EMAIL: " + this.getEmail() +
                " PHONE: " + this.getPhone() + " HOMETOWN: " + this.getHometown();
    }
}
