package com.example.quiz_diadanh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quiz_diadanh.model.database.Quiz;
import com.example.quiz_diadanh.model.database.Result;
import com.example.quiz_diadanh.model.database.Room;
import com.example.quiz_diadanh.model.database.RoomUser;
import com.example.quiz_diadanh.model.database.Topic;
import com.example.quiz_diadanh.model.database.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    TextView txtCreateRoom, txtJoin, test, test2;
    NavigationDrawerController drawerController;

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        demoInsertDataToFirebase();

        txtCreateRoom = findViewById(R.id.txtCreateRoom);
        txtJoin = findViewById(R.id.txtJoin);
        test = findViewById(R.id.test);
        test2 = findViewById(R.id.test2);

//        test.setVisibility(View.GONE);
//        test2.setVisibility(View.GONE);


        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQuizActivity.class);
                startActivity(intent);
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditQuizActivity.class);
                startActivity(intent);
            }
        });
        txtCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    private void demoInsertDataToFirebase() {
        demoLoadToRealTimeDB();
        demoLoadImgToStorage();
        deleteFileStorage("images/cau1.jpg");
        moveFileStorage("images/topic_1_quiz_2_20231124090030.jpg", "images/topic_1/topic_1_quiz_2_20231124090030.jpg");
        loadQuizzesFromJsonFile();
    }

    private void loadQuizzesFromJsonFile() {
        try {
            // Load the JSON file
            InputStream inputStream = getAssets().open("quizzes.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON string into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);

            // Get Firebase Database reference
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference quizzesRef = database.getReference("quizzes");

            // Iterate over the JSONArray and upload each quiz
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Create a Quiz object from the JSON object
                Quiz quiz = new Quiz(
                        jsonObject.getInt("topic_id"),
                        jsonObject.getString("image"),
                        jsonObject.getString("question"),
                        jsonObject.getString("option_a"),
                        jsonObject.getString("option_b"),
                        jsonObject.getString("option_c"),
                        jsonObject.getString("option_d"),
                        jsonObject.getString("correct_option")
                );

                // Push the quiz to Firebase
                quizzesRef.child(String.valueOf(i+1)).setValue(quiz); // Assuming the quiz ID is just the index + 1
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }
    public void moveFileStorage(String originalRefName, String newRefName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Đường dẫn đến ảnh gốc
        StorageReference originalRef = storageRef.child(originalRefName);

// Đường dẫn đến vị trí mới của ảnh trong thư mục 'topic_1'
        StorageReference newRef = storageRef.child(newRefName);

// Bắt đầu sao chép ảnh
        originalRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Tạo bản sao ảnh
                newRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Ảnh đã được sao chép, bây giờ xóa ảnh gốc
                        originalRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Ảnh gốc đã được xóa thành công
                                Log.d("Storage", "Original image deleted successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Lỗi khi xóa ảnh gốc
                                Log.d("Storage", "Failed to delete original image");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Lỗi khi tạo bản sao ảnh
                        Log.d("Storage", "Failed to copy image to the new location");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Lỗi khi tải ảnh gốc
                Log.d("Storage", "Failed to download original image");
            }
        });

    }

    public void deleteFileStorage(String name) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child(name);

        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("Storage", "File deleted successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("Storage", "Failed to delete file");
            }
        });

    }

    private void demoLoadImgToStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference cau1Ref = storageRef.child("images/topic_1_quiz_1_20231124090030.jpg");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.topic_1_quiz_1_20231124090030);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = cau1Ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("that bai");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("thanh cong");
            }
        });
    }

    private void demoLoadToRealTimeDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quizzesRef = database.getReference("quizzes");

        // Tạo dữ liệu cho quiz 1
        DatabaseReference quiz1Ref = quizzesRef.child("1");
        quiz1Ref.setValue(new Quiz(1, // topic_id
                "gs://quiz-dia-danh.appspot.com/images/topic_1_quiz_1_20231124090030.jpg", // image
                "Câu hỏi số 1?", // question
                "Lựa chọn A1", // option_a
                "Lựa chọn B1", // option_b
                "Lựa chọn C1", // option_c
                "Lựa chọn D1", // option_d
                "A" // correct_option
        ));

        // Tạo dữ liệu cho quiz 2
        DatabaseReference quiz2Ref = quizzesRef.child("2");
        quiz2Ref.setValue(new Quiz(
                1, // topic_id
                "gs://quiz-dia-danh.appspot.com/images/topic_1_quiz_2_20231124090030.jpg", // image
                "Câu hỏi số 2?", // question
                "Lựa chọn A2", // option_a
                "Lựa chọn B2", // option_b
                "Lựa chọn C2", // option_c
                "Lựa chọn D2", // option_d
                "B" // correct_option
        ));

        // Demo load data for 'users'
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child("1").setValue(new User(1, "Nguyễn Văn A", "0123456789", "email1@example.com", "password1"));
        usersRef.child("2").setValue(new User(2, "Trần Thị B", "0987654321", "email2@example.com", "password2"));

        // Demo load data for 'topics'
        DatabaseReference topicsRef = database.getReference("topics");
        topicsRef.child("1").setValue(new Topic(1, "Việt Nam", "Chủ đề về địa danh Việt Nam"));
        topicsRef.child("2").setValue(new Topic(2, "Biển VN", "Chủ đề về địa danh Biển VN"));

        // Demo load data for 'rooms'
        DatabaseReference roomsRef = database.getReference("rooms");
        roomsRef.child("1").setValue(new Room(1, 1, 1, "Phòng 1", "pass1", 30));
        roomsRef.child("2").setValue(new Room(2, 2, 2, "Phòng 2", "pass2", 45));

        // Demo load data for 'room_users'
        DatabaseReference roomUsersRef = database.getReference("room_users");
        roomUsersRef.child("1").setValue(new RoomUser(1, 1, 1));
        roomUsersRef.child("2").setValue(new RoomUser(2, 2, 2));

        // Demo load data for 'results'
        DatabaseReference resultsRef = database.getReference("results");
        resultsRef.child("1").setValue(new Result(1, 1, 1, "A", "2023-04-01T12:00:00", 10));
        resultsRef.child("2").setValue(new Result(2, 2, 2, "B", "2023-04-02T13:00:00", 20));

    }

    public static void setTextViewDrawableSize(TextView textView, int drawableResId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(textView.getContext(), drawableResId);
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
        }
        textView.setCompoundDrawables(null, drawable, null, null);
    }

}
