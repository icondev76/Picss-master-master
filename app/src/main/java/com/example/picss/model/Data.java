package com.example.picss.model;

import com.example.picss.model.children.Children;

import java.util.ArrayList;

public class Data {
    public ArrayList<Children> children;

    public String after;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public ArrayList<Children> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Children> children) {
        this.children = children;
    }
}
