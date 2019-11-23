package com.example.baoxiaojianapp.baoxiaojianapp.classpakage;

import java.io.Serializable;

public class AppraisalPointItem implements Serializable {

    public String getPointimageUrl() {
        return pointimageUrl;
    }

    public void setPointimageUrl(String pointimageUrl) {
        this.pointimageUrl = pointimageUrl;
    }

    public String getPointtext() {
        return pointtext;
    }

    public void setPointtext(String pointtext) {
        this.pointtext = pointtext;
    }

    private String pointimageUrl;
    private String pointtext;

    public String getPointcontent() {
        return pointcontent;
    }

    public String getStickFigureURL() {
        return stickFigureURL;
    }

    public void setStickFigureURL(String stickFigureURL) {
        this.stickFigureURL = stickFigureURL;
    }

    public String getBigStickFigureURL() {
        return bigStickFigureURL;
    }

    public void setBigStickFigureURL(String bigStickFigureURL) {
        this.bigStickFigureURL = bigStickFigureURL;
    }

    public void setPointcontent(String pointcontent) {
        this.pointcontent = pointcontent;
    }

    private String pointcontent;



    public void setType(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }

    private int type;
    private String stickFigureURL;
    private String bigStickFigureURL;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

}
