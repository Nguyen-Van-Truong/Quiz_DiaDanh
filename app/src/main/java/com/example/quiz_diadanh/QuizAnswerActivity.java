package com.example.quiz_diadanh;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Quiz;
import com.example.quiz_diadanh.widgets.NavigationDrawerController;

import java.util.ArrayList;

public class QuizAnswerActivity extends AppCompatActivity {

    NavigationDrawerController drawerController; // Khai báo controller cho Navigation Drawer
    // Views
    TextView questionTextView;
    RadioGroup answerGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private ImageView imageView;

    // Data
    private FirebaseService firebaseService;
    private ArrayList<Quiz> quizList;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        answerGroup = findViewById(R.id.answerGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        imageView = findViewById(R.id.imageView);

        // Setup navigation drawer
        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Load quiz questions for the selected topic when the activity is starting or becoming visible
        getAllQuizForTopic();
    }

    private void getAllQuizForTopic() {
        // Initialize FirebaseService and the quiz list
        firebaseService = new FirebaseService();
        quizList = new ArrayList<>();

        // Let's say the topic ID you want to fetch is "1"
        firebaseService.getAllQuizzesForTopic(1, new FirebaseService.OnAllQuizzesReceivedListener() {
            @Override
            public void onAllQuizzesReceived(ArrayList<Quiz> quizzes) {
                quizList.addAll(quizzes);
                displayCurrentQuestion();
            }

            @Override
            public void onError(Exception exception) {
                // Handle the error, show message to the user or log to the console
            }
        });
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex < quizList.size()) {
            Quiz currentQuiz = quizList.get(currentQuestionIndex);
            questionTextView.setText(currentQuiz.getQuestion());
            radioButton1.setText(currentQuiz.getOptionA());
            radioButton2.setText(currentQuiz.getOptionB());
            radioButton3.setText(currentQuiz.getOptionC());
            radioButton4.setText(currentQuiz.getOptionD());

            firebaseService = new FirebaseService();
            firebaseService.loadQuizImage(currentQuiz.getImageUrl(), imageView, this);

            setRadioButtonAction(radioButton1, currentQuiz);
            setRadioButtonAction(radioButton2, currentQuiz);
            setRadioButtonAction(radioButton3, currentQuiz);
            setRadioButtonAction(radioButton4, currentQuiz);
        } else {
            // Handle the end of the quiz
        }
    }

    private void clearRadioButtons() {
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        radioButton4.setChecked(false);
    }

    private void disableRadioButtons() {
        radioButton1.setEnabled(false);
        radioButton2.setEnabled(false);
        radioButton3.setEnabled(false);
        radioButton4.setEnabled(false);
    }

    private void enableRadioButtons() {
        radioButton1.setEnabled(true);
        radioButton2.setEnabled(true);
        radioButton3.setEnabled(true);
        radioButton4.setEnabled(true);
    }

    private void setRadioButtonAction(RadioButton radioButton, Quiz currentQuiz) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the answer is correct
                String selectedOption = radioButton.getText().toString();
                boolean isCorrect = selectedOption.equals(currentQuiz.getCorrectOption());
                Toast.makeText(QuizAnswerActivity.this, isCorrect ? "Correct" : "Incorrect", Toast.LENGTH_SHORT).show();

                radioButton.setChecked(true);
                disableRadioButtons();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToNextQuestion();
                        clearRadioButtons();
                        enableRadioButtons();
                    }
                }, 2000); // Delay of 1 second before moving to the next question
            }
        });
    }

    private void goToNextQuestion() {
        if (currentQuestionIndex < quizList.size() - 1) {
            currentQuestionIndex++;
            answerGroup.setEnabled(true);
            displayCurrentQuestion();
        } else {
            // Handle the end of the quiz
            Toast.makeText(QuizAnswerActivity.this, "End of Quiz", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity when there are no more questions
        }
    }


    // ... Rest of your activity code, including event handlers and navigation logic ...
}
