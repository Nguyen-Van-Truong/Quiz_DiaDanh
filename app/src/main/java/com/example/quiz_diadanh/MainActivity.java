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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    TextView txtCreateRoom, txtJoin, test, test2;
    NavigationDrawerController drawerController;

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        demoLoadToRealTimeDB();
//        demoLoadImgToStorage();

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
                Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotActivity.class);
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
