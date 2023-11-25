package com.example.quiz_diadanh.model.database;

public class Topic {
    public int id;
    public String name;
    public String description;

    public Topic() {
    }

    public Topic(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}