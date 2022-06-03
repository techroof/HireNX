package com.app.hirenx.Models;

public class States {

    public States(int id, String name, String iso2) {
        this.id = id;
        this.name = name;
        this.iso2 = iso2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIso2() {
        return iso2;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    private int id;
    private String name;
    private String iso2;
}
