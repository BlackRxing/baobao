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

    public void setPointcontent(String pointcontent) {
        this.pointcontent = pointcontent;
    }

    private String pointcontent;
}
