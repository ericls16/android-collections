package com.ls.rxjava.vo;

import java.util.List;

/**
 * Created by liu song on 2017/2/10.
 */

public class Student {
    private String name;
    private List<String> courses;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, List<String> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
