package com.app.hirenx.Models;

public class Skills {

    private String skill;
    private boolean isChecked=false;


    public Skills(String skill, boolean isChecked) {
        this.skill = skill;
        this.isChecked = isChecked;
    }

    public String getSkill() {
        return skill;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Skills() {


    }


}
