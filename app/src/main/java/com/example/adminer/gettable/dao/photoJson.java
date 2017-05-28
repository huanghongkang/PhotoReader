package com.example.adminer.gettable.dao;

import java.util.List;

/**
 * Created by adminer on 2017/5/15.
 */

public class photoJson {

    private String[] strArray;

    public List<Photo> getApplist() {
        return applist;
    }

    public void setApplist(List<Photo> applist) {
        this.applist = applist;
    }

    public String[] getStrArray() {
        return strArray;
    }

    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    private List<Photo> applist;
}
