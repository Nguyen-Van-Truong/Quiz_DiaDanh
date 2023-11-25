package com.example.quiz_diadanh.model.database;

public class User {
    public int id;
    public String full_name;
    public String phone_number;
    public String email;
    public String password;

    public User() {
    }

    public User(int id, String full_name, String phone_number, String email, String password) {
        this.id = id;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.email = email;
        this.password = password;
    }
}