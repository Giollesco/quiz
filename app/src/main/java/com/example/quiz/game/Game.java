package com.example.quiz.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.models.GameButtonState;
import com.example.quiz.models.GameQuestion;
import com.example.quiz.models.GameState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity {
    private DatabaseReference db;
    private GameQuestion[] questions;
    private TextView questionText;
    private TextView currentQuestionIndexText;
    private TextView totalNumOfQuestionsText;
    private Button[] allButtons;
    private Button selectedButton;
    private GameState gameState = GameState.IDLE;
    private int currentQuestionIndex = 0;
    private List<GameQuestion> shuffledQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.db = FirebaseDatabase.getInstance().getReference().child("questions");
        ConfirmationSheet confirmationSheetFragment = new ConfirmationSheet();
        ConstraintLayout backButton = findViewById(R.id.game_back_button);

        // Fetching questions and setting up
        fetchQuestions();
        showNextQuestion();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        questionText = findViewById(R.id.game_question);
        currentQuestionIndexText = findViewById(R.id.current_question_index);
        totalNumOfQuestionsText = findViewById(R.id.total_num_of_questions);
        Button button_a = findViewById(R.id.game_option_a);
        Button button_b = findViewById(R.id.game_option_b);
        Button button_c = findViewById(R.id.game_option_c);
        Button button_d = findViewById(R.id.game_option_d);

        allButtons = new Button[]{button_a, button_b, button_c, button_d};

        for (Button button : allButtons) {
            button.setOnClickListener(onSelect(button));
        }
    }

    public View.OnClickListener onSelect(final Button clickedButton) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameState != GameState.PENDING_NEXT_QUESTION){
                    for (Button button : allButtons) {
                        if (button == clickedButton) {
                            selectedButton = button;
                            gameState = GameState.PENDING_CONFIRMATION;
                            changeButtonState(selectedButton, GameButtonState.IDLE);
                            showConfirmationSheetFragment();
                        } else {
                            // Other buttons, set to INACTIVE state
                            changeButtonState(button, GameButtonState.INACTIVE);
                        }
                    }
                }
            }
        };
    }

    public void onConfirmationSheetButtonClick() {
        if (gameState == GameState.PENDING_CONFIRMATION) {
            changeButtonState(selectedButton, GameButtonState.CORRECT);
            updateTextInFragment("Sljedeće pitanje");
            gameState = GameState.PENDING_NEXT_QUESTION;
        } else if (gameState == GameState.PENDING_NEXT_QUESTION) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showNextQuestion();
                    changeButtonState(selectedButton, GameButtonState.INACTIVE);
                    updateTextInFragment("Jeste li sigurni?");
                    closeConfirmationSheetFragment();
                    gameState = GameState.IDLE;
                }
            });
        } else {
            gameState = GameState.PENDING_CONFIRMATION;
        }
    }

    public void updateTextInFragment(String newText) {
        TextView text = findViewById(R.id.fragment_text);
        if (text != null) {
            text.setText(newText);
        }
    }

    private void showConfirmationSheetFragment() {
        if (getSupportFragmentManager().findFragmentByTag(ConfirmationSheet.class.getSimpleName()) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, 0, 0, 0);
            transaction.add(R.id.fragment_container_view, ConfirmationSheet.class, null, ConfirmationSheet.class.getSimpleName());
            transaction.commit();
        }
    }

    private void closeConfirmationSheetFragment() {
        ConfirmationSheet confirmationSheetFragment = (ConfirmationSheet) getSupportFragmentManager().findFragmentByTag(ConfirmationSheet.class.getSimpleName());
        if (confirmationSheetFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(0, R.anim.slide_down);
            transaction.remove(confirmationSheetFragment).commit();
        }
    }

    public void changeButtonState(Button button, GameButtonState state) {
        switch (state) {
            case INACTIVE:
                button.setBackgroundResource(R.drawable.game_button_inactive_style);
                button.setTextColor(getResources().getColor(R.color.light_gray));
                break;
            case IDLE:
                button.setBackgroundResource(R.drawable.game_button_idle_style);
                button.setTextColor(getResources().getColor(R.color.black));
                break;
            case CORRECT:
                button.setBackgroundResource(R.drawable.game_button_correct_style);
                button.setTextColor(getResources().getColor(R.color.black));
                break;
            case INCORRECT:
                button.setBackgroundResource(R.drawable.game_button_incorrect_style);
                button.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }
    private void shuffleQuestions() {
        if (questions != null && questions.length > 0) {
            shuffledQuestions = Arrays.asList(questions);
            Collections.shuffle(shuffledQuestions);
        }
    }

    private void showNextQuestion() {
        if (shuffledQuestions != null && !shuffledQuestions.isEmpty()) {
            if (currentQuestionIndex < shuffledQuestions.size()) {
                GameQuestion nextQuestion = shuffledQuestions.get(currentQuestionIndex);
                currentQuestionIndex++;
                questionText.setText(nextQuestion.getQuestion());
                currentQuestionIndexText.setText(String.valueOf(currentQuestionIndex));
            } else {
                // Restart the loop from the beginning
                currentQuestionIndex = 0;
                shuffleQuestions();
                // Recursive call to display the first question after shuffling
                showNextQuestion();
            }
        } else {
            Log.e("Game", "Questions not shuffled or empty");
        }
    }


    public void fetchQuestions() {
        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        GenericTypeIndicator<List<GameQuestion>> typeIndicator = new GenericTypeIndicator<List<GameQuestion>>() {};
                        List<GameQuestion> questionsList = dataSnapshot.getValue(typeIndicator);

                        if (questionsList != null) {
                            questions = questionsList.toArray(new GameQuestion[0]);
                            // Shuffle the questions
                            shuffleQuestions();
                            // Initial setup
                            if (questions.length > 0) {
                                totalNumOfQuestionsText.setText(String.valueOf(questions.length));
                                currentQuestionIndex = 0;
                                showNextQuestion();
                            }
                        } else {
                            Log.e("firebase", "Failed to parse questions list");
                        }
                    } else {
                        Log.e("firebase", "No data in the 'questions' node");
                    }
                } else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }


}
