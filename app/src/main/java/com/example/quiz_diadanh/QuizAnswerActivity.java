package com.example.quiz_diadanh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz_diadanh.model.FirebaseService;
import com.example.quiz_diadanh.model.Quiz;
import com.example.quiz_diadanh.model.Room;
import com.example.quiz_diadanh.model.RoomUser;
import com.example.quiz_diadanh.model.User;
import com.example.quiz_diadanh.model.UserPreferences;
import com.example.quiz_diadanh.widgets.NavigationDrawerController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizAnswerActivity extends AppCompatActivity {

    NavigationDrawerController drawerController; // Khai báo controller cho Navigation Drawer
    // Views
    TextView questionTextView;
    RadioGroup answerGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private TextView timerTextView;
    private ImageView imageView;
    // Data
    private FirebaseService firebaseService;
    private ArrayList<Quiz> quizList;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    Room room;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        answerGroup = findViewById(R.id.answerGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        imageView = findViewById(R.id.imageView);

        // Setup navigation drawer
        drawerController = new NavigationDrawerController(this);
        drawerController.setupDrawer();

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            room = (Room) receivedIntent.getSerializableExtra("room");
        }
        UserPreferences userPreferences = new UserPreferences(this);
        User user = userPreferences.getUser();
        userId = user.getId();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Load quiz questions for the selected topic when the activity is starting or becoming visible
        getAllQuizForTopic(room.getTopicId());
    }

    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Room").setMessage("Do you want to leave the quiz answer?").setPositiveButton("Yes", (dialog, which) -> handleExit()).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void handleExit() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (room != null) {
            if (room.getCreatorId() == userId) {
                finish();
            } else {
                exitRoomAsParticipant();
            }
        } else {
            // Handle the case where the room object is null
            Toast.makeText(QuizAnswerActivity.this, "Room not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void exitRoomAsParticipant() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("exitType", "participantExit");

        setResult(RESULT_OK, resultIntent);

        finish();
    }


    private void startCountdownTimer(long timeInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer TextView with the remaining time
                timerTextView.setText(String.format("%02d:%02d",
                        millisUntilFinished / 1000 / 60,
                        millisUntilFinished / 1000 % 60));
            }

            @Override
            public void onFinish() {
                goToNextQuestion();
            }
        }.start();
    }

    private void getAllQuizForTopic(int topicId) {
        // Initialize FirebaseService and the quiz list
        firebaseService = new FirebaseService();
        quizList = new ArrayList<>();

        // Let's say the topic ID you want to fetch is "1"
        firebaseService.getAllQuizzesForTopic(topicId, new FirebaseService.OnAllQuizzesReceivedListener() {
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

            startCountdownTimer(5000);

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

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToNextQuestion();
                        clearRadioButtons();
                        enableRadioButtons();
                    }
                }, 1000); // Delay of 1 second before moving to the next question
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
