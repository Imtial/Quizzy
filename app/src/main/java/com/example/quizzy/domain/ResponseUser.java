package com.example.quizzy.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseUser implements Serializable {
    @SerializedName("token")
    String token;
    @SerializedName("user")
    UserInfo userInfo;

    public ResponseUser() {
    }


    public ResponseUser(UserInfo userInfo, String token) {
        this.userInfo = userInfo;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "token='" + token + '\'' +
                ", user=" + userInfo +
                '}';
    }
}

