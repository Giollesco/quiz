package com.example.quiz.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quiz.R;
import com.example.quiz.models.GameButtonState;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ConstraintLayout gameButton = findViewById(R.id.game_back_button);

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button1 = findViewById(R.id.game_option_a);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the selected answer is correct
                boolean isCorrect = true; // Implement this method
                changeButtonState(button1, GameButtonState.IDLE);
            }
        });
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
}