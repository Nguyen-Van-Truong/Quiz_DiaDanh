package com.example.quiz_diadanh.widgets;

public class RankingItem {
    private int iconResId; // ID của biểu tượng
    private String name; // Tên người chơi
    private int score; // Điểm số

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
