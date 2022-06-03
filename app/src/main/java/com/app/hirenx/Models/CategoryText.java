package com.app.hirenx.Models;

import java.util.ArrayList;

public class CategoryText {

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CategoryText(String categoryName, String id) {
        this.categoryName = categoryName;
        this.id = id;
    }

    private String categoryName,id;



    //public ArrayList<Skills> skillsModelClass;

   /* public CategoryText(String catetoryText, ArrayList<Skills> skillsModelClass) {
        this.catetoryText = catetoryText;
        this.skillsModelClass = skillsModelClass;
    }*/


    public CategoryText() {
    }


}
