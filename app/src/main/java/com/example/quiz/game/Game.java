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
import com.example.quiz.models.GameState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Game extends AppCompatActivity {
    private DatabaseReference db;
    private ConfirmationSheet confirmationSheetFragment;
    private Button[] allButtons;
    public Button selectedButton;
    public GameState gameState = GameState.IDLE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.db = FirebaseDatabase.getInstance().getReference();
        confirmationSheetFragment = new ConfirmationSheet();
        ConstraintLayout backButton = findViewById(R.id.game_back_button);

        // Fetching questions
        fetchQuestions();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        if(gameState == GameState.PENDING_CONFIRMATION){
            changeButtonState(selectedButton, GameButtonState.CORRECT);
            updateTextInFragment("Sljedeće pitanje");
            gameState = GameState.PENDING_NEXT_QUESTION;
        }
        else if (gameState == GameState.PENDING_NEXT_QUESTION){
            changeButtonState(selectedButton, GameButtonState.INACTIVE);
            updateTextInFragment("Sljedeće pitanje");
            closeConfirmationSheetFragment();
            gameState = GameState.IDLE;
        }
        else{
            gameState = GameState.PENDING_CONFIRMATION;
        }
    }

    // Method to update the text in the fragment
    public void updateTextInFragment(String newText) {
        TextView text = findViewById(R.id.fragment_text);
        if(text != null){
            text.setText(newText);
        }
    }

    private void showConfirmationSheetFragment() {
        // Check if the fragment is already visible
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
                // Set background color and font color for INACTIVE state
                button.setBackgroundResource(R.drawable.game_button_inactive_style);
                button.setTextColor(getResources().getColor(R.color.light_gray));
                break;
            case IDLE:
                // Set background color and font color for IDLE state
                button.setBackgroundResource(R.drawable.game_button_idle_style);
                button.setTextColor(getResources().getColor(R.color.black));
                break;
            case CORRECT:
                // Set background color and font color for CORRECT state
                button.setBackgroundResource(R.drawable.game_button_correct_style);
                button.setTextColor(getResources().getColor(R.color.black));
                break;
            case INCORRECT:
                // Set background color and font color for INCORRECT state
                button.setBackgroundResource(R.drawable.game_button_incorrect_style);
                button.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    public void fetchQuestions(){
        db.child("questions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}