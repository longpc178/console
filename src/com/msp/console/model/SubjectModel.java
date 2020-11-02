package com.msp.console.model;

public class SubjectModel {

    private String name;
    private float mark;

    public SubjectModel(String name, float mark) {
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public float getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return "SubjectModel{" +
                "name='" + name + '\'' +
                ", mark=" + mark +
                '}';
    }
}
