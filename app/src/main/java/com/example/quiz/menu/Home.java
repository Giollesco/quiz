package com.example.quiz.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.game.Game;
import com.example.quiz.models.User;
import com.example.quiz.ranking.Ranking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser loggedUser;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.auth = FirebaseAuth.getInstance();
        this.loggedUser = this.auth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance().getReference();

        TextView username = findViewById(R.id.home_page_username);
        TextView points = findViewById(R.id.home_page_points);
        ConstraintLayout gameButton = findViewById(R.id.home_page_play_quiz_button);
        ConstraintLayout rankingButton = findViewById(R.id.home_page_ranking_button);

        // User data fetch
        if(loggedUser != null){
            this.db.child("users").child(this.loggedUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        try {
                            User currentUser = task.getResult().getValue(User.class);
                            if (currentUser != null) {
                                username.setText(currentUser.name.toString());
                                points.setText(currentUser.points.toString());
                            }
                        }
                        catch (NullPointerException e){
                            Log.e("NoData", e.getMessage());
                        }
                    }
                    else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });
        }

        // Play quiz navigation
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Game.class);
                startActivity(intent);
            }
        });

        // Ranking navigation
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Ranking.class);
                startActivity(intent);
            }
        });
    }
}