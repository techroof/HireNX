package com.app.hirenx.SearchDialog;

import android.app.SearchableInfo;

import ir.mirrajabi.searchdialog.core.Searchable;


public class SearchDialogs implements Searchable {

    private String mTitle;

    public SearchDialogs(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;

    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
