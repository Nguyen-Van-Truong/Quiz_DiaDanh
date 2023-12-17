package com.example.quiz_diadanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Quiz;
import com.example.quiz_diadanh.model.Topic;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class CreateQuizActivity extends AppCompatActivity {

    TextView questionPrefixTextView;
    TextView btnAddQuiz, saveEnd;
    private EditText topicNameEditText, timerEditText, scoreEditText, questionEditText;
    private TextView[] answerTextViews = new TextView[4];
    private String selectedAnswer = "";
    private ArrayList<Quiz> quizzes = new ArrayList<>();
    private ArrayList<Uri> quizImageUris = new ArrayList<>();
    private int currentQuizIndex = 0;
    private FirebaseService firebaseService;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri currentImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_quiz);

        initUI();
        setAnswerClickListeners();
        saveEnd = findViewById(R.id.saveEnd);
        saveEnd.setOnClickListener(v -> saveEnd());

        firebaseService = new FirebaseService();

        btnAddQuiz = findViewById(R.id.btnAddQuiz);
        btnAddQuiz.setOnClickListener(v -> addOrUpdateQuiz());

        ImageView imgIconBack = findViewById(R.id.imgIconBack);
        ImageView imgIconNext = findViewById(R.id.imgIconNext);
        imgIconBack.setOnClickListener(v -> navigateQuizzes(-1));
        imgIconNext.setOnClickListener(v -> navigateQuizzes(1));

        updateNavigationButtons();
    }

    private void initUI() {
        topicNameEditText = findViewById(R.id.topicNameEditText);
        timerEditText = findViewById(R.id.timerEditText);
        scoreEditText = findViewById(R.id.scoreEditText);
        questionPrefixTextView = findViewById(R.id.questionPrefixTextView);
        questionEditText = findViewById(R.id.questionEditText);
        answerTextViews[0] = findViewById(R.id.answerOneTextView);
        answerTextViews[1] = findViewById(R.id.answerTwoTextView);
        answerTextViews[2] = findViewById(R.id.answerThreeTextView);
        answerTextViews[3] = findViewById(R.id.answerFourTextView);
    }

    private void setAnswerClickListeners() {
        for (int i = 0; i < answerTextViews.length; i++) {
            final int index = i;
            answerTextViews[i].setOnClickListener(view -> onAnswerClick(view, index));
        }
    }

    private void onAnswerClick(View view, int index) {
        resetAnswerSelection();
        view.setSelected(true);
        setTextViewDrawableRight((TextView) view, R.drawable.ic_checkmark_selector);
        selectedAnswer = answerTextViews[index].getText().toString(); // Get the actual answer text
    }

    private void resetAnswerSelection() {
        for (TextView textView : answerTextViews) {
            textView.setSelected(false);
            setTextViewDrawableRight(textView, R.drawable.ic_checkmark);
        }
    }

    private void addOrUpdateQuiz() {
        String question = questionEditText.getText().toString();
        String[] options = new String[4];
        for (int i = 0; i < options.length; i++) {
            options[i] = answerTextViews[i].getText().toString();
        }
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Please select the correct answer.", Toast.LENGTH_SHORT).show();
            return;
        }
        Quiz quiz = new Quiz(question, options[0], options[1], options[2], options[3], selectedAnswer, "", "published", 1);

        currentQuizIndex++;
        if (currentQuizIndex >= 0 && currentQuizIndex < quizzes.size()) {
            quizzes.set(currentQuizIndex, quiz);
            quizImageUris.set(currentQuizIndex, currentImageUri); // Update the existing quiz image URI
            currentQuizIndex = quizzes.size();
        } else {
            quizzes.add(quiz);
            quizImageUris.add(currentImageUri); // Add new quiz image URI
        }

        resetQuizForm();
        updateNavigationButtons();
    }

    private void resetQuizForm() {
        questionEditText.setText("");
        resetAnswerSelection();
        selectedAnswer = "";
//        currentQuizIndex = -1; // Reset to add new quiz mode
        questionPrefixTextView.setText("C" + (currentQuizIndex + 1) + ":");
        questionEditText.setText("");
        answerTextViews[0].setText("");
        answerTextViews[1].setText("");
        answerTextViews[2].setText("");
        answerTextViews[3].setText("");


        if (currentQuizIndex >= 0 && currentQuizIndex < quizzes.size() - 1) {
            btnAddQuiz.setText("Cập nhật và thêm");
        } else {
            btnAddQuiz.setText("Thêm");
        }

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.img_notfound);

        Log.d("currentQuizIndex", "currentQuizIndex: " + currentQuizIndex);
        Log.d("currentQuizIndex333333333333", "quizImageUris: " + quizImageUris);
        showQuizzes();
    }

    private void showQuizzes() {
        for (int i = 0; i < quizzes.size(); i++) {
            Log.d("currentQuizIndex", "quizzes " + i + ": " + quizzes.get(i));
        }
    }

    private void navigateQuizzes(int direction) {
        if (quizzes.isEmpty()) return;

        int newIndex = currentQuizIndex + direction;
        if (newIndex >= 0 && newIndex < quizzes.size()) {
            currentQuizIndex = newIndex;
            loadQuizDetails(quizzes.get(currentQuizIndex));
        }

        questionPrefixTextView.setText("C" + (currentQuizIndex + 1) + ":");
        if (currentQuizIndex >= 0 && currentQuizIndex < quizzes.size() - 1) {
            btnAddQuiz.setText("Cập nhật và thêm");
        } else {
            btnAddQuiz.setText("Thêm");
        }

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageURI(quizImageUris.get(currentQuizIndex));

        Log.d("currentQuizIndex", "currentQuizIndex: " + currentQuizIndex);
        showQuizzes();
        updateNavigationButtons();
    }

    private void loadQuizDetails(Quiz quiz) {
        questionEditText.setText(quiz.getQuestion());
        answerTextViews[0].setText(quiz.getOptionA());
        answerTextViews[1].setText(quiz.getOptionB());
        answerTextViews[2].setText(quiz.getOptionC());
        answerTextViews[3].setText(quiz.getOptionD());

        resetAnswerSelection();
        // Find the index of the correct answer
        for (int i = 0; i < answerTextViews.length; i++) {
            if (answerTextViews[i].getText().toString().equals(quiz.getCorrectOption())) {
                answerTextViews[i].setSelected(true);
                setTextViewDrawableRight(answerTextViews[i], R.drawable.ic_checkmark_selector);
                break;
            }
        }
    }

    private void updateNavigationButtons() {
        findViewById(R.id.imgIconBack).setVisibility(currentQuizIndex > 0 ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.imgIconNext).setVisibility((currentQuizIndex < quizzes.size() - 1 || currentQuizIndex == -1) ? View.VISIBLE : View.INVISIBLE);
    }


    private void saveEnd() {
        String nameTopic = topicNameEditText.getText().toString();
        if (!nameTopic.isEmpty()) {
            // Get the maximum ID from the topics node
            firebaseService.getMaxIdFromNode("topics", maxTopicId -> {
                int nextTopicId = maxTopicId + 1;
                Topic newTopic = new Topic(nextTopicId, nameTopic, nameTopic, "active");
                firebaseService.addTopic(newTopic);

                // Now get the maximum ID from the quizzes node
                firebaseService.getMaxIdFromNode("quizzes", maxQuizId -> {
                    int quizId = maxQuizId + 1; // Start from the next available quiz ID
                    for (int i = 0; i < quizzes.size(); i++) {
                        Quiz quiz = quizzes.get(i);
                        quiz.setTopicId(nextTopicId); // Set the new topic ID for each quiz

                        if (quizImageUris.get(i) != null) {
                            String imagePath = uploadImageToFirebase(quizImageUris.get(i), nextTopicId, quizId);
                            String imageFirebasePath = "gs://quiz-dia-danh.appspot.com/" + imagePath;
                            quiz.setImageUrl(imageFirebasePath); // Set the image path for the quiz
                        }

                        firebaseService.addQuiz(quiz, quizId++);
                    }

                    Toast.makeText(CreateQuizActivity.this, "Topic and quizzes saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        } else {
            Toast.makeText(this, "Topic name is required", Toast.LENGTH_SHORT).show();
        }
    }

    private String uploadImageToFirebase(Uri imageUri, int topicId, int quizId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Naming the image file based on topic and quiz ID
        String fileName = "topic_" + topicId + "_quiz_" + quizId + "_" + System.currentTimeMillis() + ".jpg";
        String imagePath = "images/topic_" + topicId + "/" + fileName;
        StorageReference imageRef = storageRef.child(imagePath);

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(CreateQuizActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(CreateQuizActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
        });

        return imagePath; // Return the storage path of the image
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Toast.makeText(CreateQuizActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            // Task completed successfully
            Toast.makeText(CreateQuizActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            // Get download URL and do something with it (e.g., save it with your quiz data)
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Use imageUrl for your quiz
            });
        });
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            currentImageUri = data.getData();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(currentImageUri);
        }
    }

    private void setTextViewDrawableRight(TextView textView, int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }
}
