package com.example.adminer.gettable.dao;

import java.io.Serializable;

/**
 * Created by adminer on 2017/5/16.
 */

public class photoInfomation implements Serializable {
    private int id;
    private String filename;
    private String confirmname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getConfirmname() {
        return confirmname;
    }

    public void setConfirmname(String confirmname) {
        this.confirmname = confirmname;
    }
}
