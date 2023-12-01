package com.example.quiz_diadanh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Quiz;
import com.example.quiz_diadanh.widgets.NavigationDrawerController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    TextView txtCreateRoom, txtJoin, test, test2;
    NavigationDrawerController drawerController;
    private FirebaseService firebaseService;

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        demoLoadToRealTimeDB();
//        demoLoadImgToStorage();
        demoFireBaseSerivice();


        txtCreateRoom = findViewById(R.id.txtCreateRoom);
        txtJoin = findViewById(R.id.txtJoin);
        test = findViewById(R.id.test);
        test2 = findViewById(R.id.test2);

//        test.setVisibility(View.GONE);
        test2.setVisibility(View.GONE);


        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditQuizActivity.class);
                startActivity(intent);
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizAnswerActivity.class);
                startActivity(intent);
            }
        });
        txtCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseTopicActivity.class);
                startActivity(intent);
            }
        });

        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CodeRoomActivity.class);
                startActivity(intent);
            }
        });


    }

    private void demoFireBaseSerivice() {
        // Initialize FirebaseService
        firebaseService = new FirebaseService();

        // Example: Add a new quiz with id = 21
        Quiz newQuiz = new Quiz(
                21,
                "What is the capital of France?",
                "Paris",
                "London",
                "Berlin",
                "Madrid",
                "A",
                "image_url_here",
                "published",
                "1" // Assuming the topic ID is 1
        );

        // Add quiz to Firebase
        firebaseService.addQuiz(newQuiz);

//        // Example: Update a user
//        User user = new User();
//        // Set properties of user
//        user.setEmail("user@example.com");
//        user.setFullName("John Doe");
//        user.setPassword("password123");
//        user.setPhoneNumber("1234567890");
//        user.setStatus("active");
//        // Update user in Firebase (assuming userID is "user1")
//        firebaseService.updateUser("user1", user);
//
//        // Example: Delete a quiz (assuming quizID is "quiz1")
//        firebaseService.deleteQuiz("quiz1");
//
//        // Example: Delete a user (assuming userID is "user2")
//        firebaseService.deleteUser("user2");
    }

    private void demoLoadImgToStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference cau1Ref = storageRef.child("images/cau1.jpg");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cau1);
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
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("haha");
    }

    public static void setTextViewDrawableSize(TextView textView, int drawableResId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(textView.getContext(), drawableResId);
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
        }
        textView.setCompoundDrawables(null, drawable, null, null);
    }

}
