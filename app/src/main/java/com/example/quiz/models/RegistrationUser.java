package com.example.quiz.models;

import com.google.firebase.database.IgnoreExtraProperties;

public class RegistrationUser {

    public String name;
    public String email;
    public String password;
    public Boolean isAdmin;
    public RegistrationUser(String name, String email, String password, Boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
