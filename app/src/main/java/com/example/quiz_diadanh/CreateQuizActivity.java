package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateQuizActivity extends AppCompatActivity {

    TextView answerOneTextView, answerTwoTextView, answerThreeTextView, answerFourTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        answerOneTextView = findViewById(R.id.answerOneTextView);
        answerTwoTextView = findViewById(R.id.answerTwoTextView);
        answerThreeTextView = findViewById(R.id.answerThreeTextView);
        answerFourTextView = findViewById(R.id.answerFourTextView);

        answerOneTextView.setOnClickListener(this::onAnswerClick);
        answerTwoTextView.setOnClickListener(this::onAnswerClick);
        answerThreeTextView.setOnClickListener(this::onAnswerClick);
        answerFourTextView.setOnClickListener(this::onAnswerClick);
    }
    private void onAnswerClick(View view) {
        // Kiểm tra xem TextView hiện tại có đang được chọn không
        boolean isSelected = view.isSelected();

        // Reset trạng thái của tất cả các TextView khác về mặc định
        answerOneTextView.setSelected(false);
        answerTwoTextView.setSelected(false);
        answerThreeTextView.setSelected(false);
        answerFourTextView.setSelected(false);

        // Set drawable mặc định cho tất cả các TextView
        setTextViewDrawableRight(answerOneTextView, R.drawable.ic_checkmark);
        setTextViewDrawableRight(answerTwoTextView, R.drawable.ic_checkmark);
        setTextViewDrawableRight(answerThreeTextView, R.drawable.ic_checkmark);
        setTextViewDrawableRight(answerFourTextView, R.drawable.ic_checkmark);

        // Chỉ đổi trạng thái và drawable của TextView được chọn
        if (!isSelected) {
            view.setSelected(true);
            setTextViewDrawableRight((TextView) view, R.drawable.ic_checkmark_selector);
        }
    }

    private void setTextViewDrawableRight(TextView textView, int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }
}