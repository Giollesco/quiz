package com.example.quiz.ranking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.models.User;
import com.example.quiz.ranking.adapter.RankingAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking extends AppCompatActivity {

    private DatabaseReference db;
    ConstraintLayout backButton;
    RecyclerView eventRecyclerView;
    RankingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        this.backButton = findViewById(R.id.ranking_back_button);
        this.db = FirebaseDatabase.getInstance().getReference().child("users");

        this.eventRecyclerView = findViewById(R.id.rankingListView);
        this.eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display data in descending order
        fetchData();

        // Back handler
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void fetchData() {
        db.orderByChild("points").limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }

                // Reverse the list to get descending order
                Collections.reverse(userList);

                // Set up the adapter with the reversed list
                adapter = new RankingAdapter(userList);
                eventRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Ranking", "Error getting data", databaseError.toException());
            }
        });
    }
}

