package com.example.baoxiaojianapp.baoxiaojianapp.Utils;



import java.util.regex.Pattern;

public class RegexUtils {

    public static boolean checkPhoneNumber(String phoneNum){
            String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
            Pattern p = Pattern.compile(regex);
            if (p.matches(regex, phoneNum)){
                return true;
            }else {
                return false;
            }
    }
    public static boolean checkVertifyCode(String vertifycode){
        String regex="([0-9]{4})";
        Pattern p = Pattern.compile(regex);
        if (p.matches(regex, vertifycode)){
            return true;
        }else {
            return false;
        }
    }
}
