package com.example.game.ListView;

import java.io.Serializable;


public class listUser implements Serializable {
    private String userName;
    private String fullName;
    private String point;

    public listUser(String userName, String fullName, String point) {
        this.userName = userName;
        this.fullName = fullName;
        this.point = point;
    }

    public void setUserName(String userName) { this.userName = userName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPoint(String point) { this.point = point; }

    public String getUserName() { return userName; }
    public String getFullName() { return fullName; }
    public String getPoint() { return point; }
}