package com.example.quiz.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quiz.R;
import com.example.quiz.models.GameButtonState;

public class Game extends AppCompatActivity {
    private ConfirmationSheet confirmationSheetFragment;
    private Button[] allButtons;
    public Button selectedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        confirmationSheetFragment = new ConfirmationSheet();

        ConstraintLayout gameButton = findViewById(R.id.game_back_button);

        gameButton.setOnClickListener(new View.OnClickListener() {
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
                for (Button button : allButtons) {
                    if (button == clickedButton) {
                        changeButtonState(button, GameButtonState.IDLE);
                        selectedButton = button;
                        showConfirmationSheetFragment();
                    } else {
                        // Other buttons, set to INACTIVE state
                        changeButtonState(button, GameButtonState.INACTIVE);
                    }
                }
            }
        };
    }

    public void onConfirmationSheetButtonClick() {
        changeButtonState(selectedButton, GameButtonState.CORRECT);
        updateTextInFragment("Sljedeće pitanje");
    }

    // Method to update the text in the fragment
    public void updateTextInFragment(String newText) {
        if (confirmationSheetFragment != null) {
            confirmationSheetFragment.updateText(newText);
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