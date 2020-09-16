package com.example.quizzy.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "table_user")
public class UserInfo implements Serializable {

    String name;
    String email;
    String password;
    @NonNull
    @PrimaryKey
    String _id = "kiChuMicHu";
    @ColumnInfo(name = "created_at")
    Date createdAt = new Date(System.currentTimeMillis());
    @ColumnInfo(name = "updated_at")
    Date updatedAt = new Date(System.currentTimeMillis());;
    String token;

    @Ignore
    public UserInfo(String name, String email, String token, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    @Ignore
    public UserInfo(String name, String email, String token) {
        this.name = name;
        this.email = email;
        this.token = token;
        this.password = "";
    }

    public UserInfo() {
    }


    @NotNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NotNull String _id) {
        this._id = _id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

