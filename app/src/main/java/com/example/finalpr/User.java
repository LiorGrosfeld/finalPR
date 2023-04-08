package com.example.finalpr;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String password;
    private double age;
    private String email;
    private int accessLevel;

    public User(String userName, String password, double age, String email) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.email = email;
        this.accessLevel = 0;
    }
    public User(String userName, String password, double age, String email, int accessLevel) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.email = email;
        this.accessLevel = accessLevel;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public double getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAccessLevel() {
        return accessLevel;
    }
    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
