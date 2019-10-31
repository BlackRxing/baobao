package com.example.baoxiaojianapp.baoxiaojianapp.jsonclass;

import android.graphics.ImageDecoder;

public class User {
    private static User user;
    private int id;
    private boolean is_enterprise;
    private String username;
    private String avatar_url;
    private String phone_num;
    private int point;
    private int hasPunch;
    private int third_party_type;
    private String turing_token;
    private String nick_name;
    private String sex;
    private String location;
    private String weibo_token;
    public static User getInstance(){
        if(user==null){
            synchronized (User.class){
                if (user==null){
                    user=new User();
                }
            }
        }
        return user;
    }

    public String getWeibo_token() {
        return weibo_token;
    }

    public void setWeibo_token(String weibo_token) {
        this.weibo_token = weibo_token;
    }

    public String getWeixin_token() {
        return weixin_token;
    }

    public void setWeixin_token(String weixin_token) {
        this.weixin_token = weixin_token;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    private String weixin_token;
    private String open_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIs_enterprise(boolean is_enterprise) {
        this.is_enterprise = is_enterprise;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setPhone_num(String phone_num) {
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

    public void setTuring_token(String turing_token) {
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


    public boolean getIs_enterprise() {
        return is_enterprise;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getPhone_num() {
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

    public String getTuring_token() {
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




}
