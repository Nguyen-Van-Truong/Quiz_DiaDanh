package com.example.quiz_diadanh;

public class UserModel {
    private int srcImage;
    private String userName;

    public UserModel(int srcImage, String userName) {
        this.srcImage = srcImage;
        this.userName = userName;
    }

    public int getSrcImage() {
        return srcImage;
    }

    public String getUsername() {
        return userName;
    }
}
