package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Topic;

import java.util.ArrayList;

public class ChooseTopicActivity extends AppCompatActivity {
    GridLayout categoryContainer; // Thêm biến để tham chiếu đến GridLayout
    FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topic);

        categoryContainer = findViewById(R.id.category_container); // Ánh xạ GridLayout

        // Gọi phương thức để lấy tất cả các chủ đề từ Firebase
        firebaseService = new FirebaseService();
        firebaseService.getAllActiveTopics(new FirebaseService.OnActiveTopicsReceivedListener() {
            @Override
            public void onActiveTopicsReceived(ArrayList<Topic> activeTopics) {
                // Hiển thị danh sách các chủ đề trên giao diện
                displayTopics(activeTopics);
            }

            @Override
            public void onError(Exception exception) {
                // Xử lý khi có lỗi xảy ra trong quá trình lấy dữ liệu
            }
        });
    }

    // Phương thức để hiển thị danh sách chủ đề trên giao diện
    private void displayTopics(ArrayList<Topic> topics) {
        int totalColumns = 2; // Số lượng cột bạn muốn hiển thị
        int row = 0;
        int col = 0;
        int columnSpacing = 70; // Khoảng cách giữa các cột

        for (int i = 0; i < topics.size(); i++) {
            Topic topic = topics.get(i);

            // Tạo một TextView mới để hiển thị chủ đề
            TextView textView = new TextView(this);
            textView.setText(topic.getName());
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.category_button_background);
            textView.setPadding(0, 10, 10, 10);

            // Đặt ID cho TextView dựa trên ID của chủ đề
            textView.setId(topic.getId());

            GridLayout.Spec rowSpec = GridLayout.spec(row); // Dòng
            GridLayout.Spec colSpec = GridLayout.spec(col, GridLayout.FILL, 1f); // Cột và layout_columnWeight
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);

            // Áp dụng các thuộc tính LayoutParams cho TextView
            params.setMargins(0, 0, columnSpacing, 0); // Đặt margin cho khoảng cách giữa các cột
            textView.setLayoutParams(params);

            // Đính kèm sự kiện onClick để xử lý khi người dùng chọn một chủ đề
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChooseTopicActivity.this, WaitingRoomActivity.class);
                    // Gửi ID của chủ đề được chọn đến hoạt động WaitingRoomActivity
                    intent.putExtra("topic_id", topic.getId());
                    startActivity(intent);
                }
            });

            // Thêm TextView vào GridLayout
            categoryContainer.addView(textView);

            col++;
            if (col == totalColumns) {
                col = 0;
                row++;
            }
        }
    }

}
