package com.example.baoxiaojianapp.baoxiaojianapp.classpakage;

public class LoginRequest {
    int loginType;
    String phoneNum;
    String sms;
    String username;
    String password;

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setThirdPartyToken(String thirdPartyToken) {
        this.thirdPartyToken = thirdPartyToken;
    }

    String thirdPartyToken;
}
