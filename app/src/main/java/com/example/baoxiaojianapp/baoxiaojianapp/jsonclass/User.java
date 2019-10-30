package com.example.baoxiaojianapp.baoxiaojianapp.jsonclass;

import android.graphics.ImageDecoder;

public class User {
    private int id;
    private int is_enterprise;
    private String username;
    private String avatar_url;
    private int phone_num;
    private int point;
    private int hasPunch;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIs_enterprise(int is_enterprise) {
        this.is_enterprise = is_enterprise;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setPhone_num(int phone_num) {
        this.phone_num = phone_num;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setHasPunch(int hasPunch) {
        this.hasPunch = hasPunch;
    }

    public void setThird_party_type(int third_party_type) {
        this.third_party_type = third_party_type;
    }

    public void setTuring_token(long turing_token) {
        this.turing_token = turing_token;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAvatar_Info(ImageDecoder.ImageInfo avatar_Info) {
        this.avatar_Info = avatar_Info;
    }

    public int getIs_enterprise() {
        return is_enterprise;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public int getPhone_num() {
        return phone_num;
    }

    public int getPoint() {
        return point;
    }

    public int getHasPunch() {
        return hasPunch;
    }

    public int getThird_party_type() {
        return third_party_type;
    }

    public long getTuring_token() {
        return turing_token;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getSex() {
        return sex;
    }

    public String getLocation() {
        return location;
    }

    public ImageDecoder.ImageInfo getAvatar_Info() {
        return avatar_Info;
    }

    private int third_party_type;
    private long turing_token;
    private String nick_name;
    private String sex;
    private String location;
    private ImageDecoder.ImageInfo avatar_Info;
}
