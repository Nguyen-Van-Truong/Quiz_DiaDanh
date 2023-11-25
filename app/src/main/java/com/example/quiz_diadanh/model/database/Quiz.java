package com.example.quiz_diadanh.model.database;

public class Quiz {
    private int topic_id;
    private String image;
    private String question;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String correct_option;

    public Quiz(int topic_id, String image, String question, String option_a, String option_b, String option_c, String option_d, String correct_option) {
        this.topic_id = topic_id;
        this.image = image;
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.correct_option = correct_option;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "topic_id=" + topic_id +
                ", image='" + image + '\'' +
                ", question='" + question + '\'' +
                ", option_a='" + option_a + '\'' +
                ", option_b='" + option_b + '\'' +
                ", option_c='" + option_c + '\'' +
                ", option_d='" + option_d + '\'' +
                ", correct_option='" + correct_option + '\'' +
                '}';
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getCorrect_option() {
        return correct_option;
    }

    public void setCorrect_option(String correct_option) {
        this.correct_option = correct_option;
    }
}
