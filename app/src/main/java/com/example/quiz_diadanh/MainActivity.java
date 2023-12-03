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
import com.example.quiz_diadanh.model.Result;
import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.Topic;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.widgets.NavigationDrawerController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtCreateRoom, txtJoin, test, test2;
    NavigationDrawerController drawerController;
    private FirebaseService firebaseService;

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        demoFireBaseSerivice();

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

    public static void setTextViewDrawableSize(TextView textView, int drawableResId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(textView.getContext(), drawableResId);
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
        }
        textView.setCompoundDrawables(null, drawable, null, null);
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

    private void demoFireBaseSerivice() {
        // Initialize FirebaseService
        firebaseService = new FirebaseService();


//        firebaseService.getAllQuizzesForTopic(1, new FirebaseService.OnAllQuizzesReceivedListener() {
//            @Override
//            public void onAllQuizzesReceived(ArrayList<Quiz> quizzes) {
//                System.out.println("size: " + quizzes.size());
//                System.out.println(quizzes);
//            }
//
//            @Override
//            public void onError(Exception exception) {
//                // Handle the error, show message to the user or log to the console
//            }
//        });
//        // Example: Add a new quiz with id = 21
//        Quiz newQuiz = new Quiz(
//                222,
//                "What is the capital of France?",
//                "Paris",
//                "London",
//                "Berlin",
//                "Madrid",
//                "A",
//                "image_url_here",
//                "published",
//                1 // Assuming the topic ID is 1
//        );
//
//        // Add quiz to Firebase
//        firebaseService.addQuiz(newQuiz);

//        firebaseService.deleteQuiz("21");
//        Quiz newQuiz2 = new Quiz(
//                21,
//                "What is the capital of VietNam?",
//                "Ha Long",
//                "Vung Tau",
//                "Do Son",
//                "Can gio",
//                "A",
//                "image_url_here",
//                "published",
//                "1" // Assuming the topic ID is 1
//        );
//        firebaseService.updateQuiz("21", newQuiz2);

//        // Example: Add a new user
//        User newUser = new User(3, "user@example.com", "User Name", "password", "0123456789", "active");
//        firebaseService.addUser(newUser);

//        // Example: Update a user
//        User updatedUser = new User(3, "user@example.com", "Updated Name", "newpassword", "0987654321", "inactive");
//        firebaseService.updateUser("3", updatedUser);
//
//        // Example: Delete a user
//        firebaseService.deleteUser("3");
//
//        // Example: Add a new room
//        Room newRoom = new Room(4, 1, "Room Name", "roompass", "open", 60, 1);
//        firebaseService.addRoom(newRoom);
//
//        // Example: Update a room
//        Room updatedRoom = new Room(4, 1, "Updated Room Name", "newroompass", "closed", 30, 2);
//        firebaseService.updateRoom("4", updatedRoom);
//
//        // Example: Delete a room
//        firebaseService.deleteRoom("4");
//
        // Example: Add a new topic
        Topic newTopic = new Topic(5, "New Topic", "Description of new topic", "active");
        firebaseService.addTopic(newTopic);
//
//        // Example: Update a topic
//        Topic updatedTopic = new Topic(5, "Updated Topic", "Updated description", "inactive");
//        firebaseService.updateTopic("5", updatedTopic);
//
//        // Example: Delete a topic
//        firebaseService.deleteTopic("5");
//
        // Example: Add a new result
//        Result newResult = new Result(6, 1, 100, "A", "accepted", "2023-04-03T12:00:00", 1);
//        firebaseService.addResult(newResult);
//
        // Example: Update a result
//        Result updatedResult = new Result(6, 1, 80, "B", "review", "2023-04-03T13:00:00", 2);
//        firebaseService.updateResult("6", updatedResult);
//
//        // Example: Delete a result
//        firebaseService.deleteResult("6");
//
//        // Example: Add a new room user
//        RoomUser newRoomUser = new RoomUser(7, 1, "valid", 1);
//        firebaseService.addRoomUser(newRoomUser);
//
//        // Example: Update a room user
//        RoomUser updatedRoomUser = new RoomUser(7, 2, "invalid", 2);
//        firebaseService.updateRoomUser("7", updatedRoomUser);
//
//        // Example: Delete a room user
//        firebaseService.deleteRoomUser("7");
    }

}
