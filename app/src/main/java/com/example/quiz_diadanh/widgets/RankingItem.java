package com.example.quiz_diadanh.widgets;

public class RankingItem {
    private int iconResId; // ID of the icon
    private String name; // Name of the player
    private int score; // Score

    public RankingItem(int iconResId, String name, int score) {
        this.iconResId = iconResId;
        this.name = name;
        this.score = score;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}

