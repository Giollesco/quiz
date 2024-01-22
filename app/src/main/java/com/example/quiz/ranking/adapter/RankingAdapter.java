package com.example.quiz.ranking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Collections;
import java.util.List;
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private List<User> userList;
    private Context ctx;
    public ImageView pointsIcon;

    public RankingAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.ctx = parent.getContext();
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.ranking_list_item, parent, false);
        pointsIcon = view.findViewById(R.id.ranking_item_points_icon);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        User model = userList.get(position);
        // Check the position to apply different styles
        if (position == 0) {
            applyFirstItemStyle(holder);
        } else if (position == 1) {
            applySecondItemStyle(holder);
        }
        else if (position == 2){
            applyThirdItemStyle(holder);
        }
        holder.position.setText(String.valueOf(position + 1));
        holder.username.setText(model.name);
        holder.points.setText(String.valueOf(model.points));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView position, username, points;

        RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.position = itemView.findViewById(R.id.ranking_item_position);
            this.username = itemView.findViewById(R.id.ranking_item_username);
            this.points = itemView.findViewById(R.id.ranking_item_points);
        }
    }

    private void applyFirstItemStyle(RankingViewHolder holder) {
        holder.itemView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
        holder.itemView.setMinimumHeight(120);
        holder.username.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        holder.points.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        holder.position.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        pointsIcon.setImageResource(R.drawable.star_black);
    }

    private void applySecondItemStyle(RankingViewHolder holder) {
        holder.itemView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.purple));
        holder.username.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        holder.points.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        holder.position.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        pointsIcon.setImageResource(R.drawable.star_black);
    }

    private void applyThirdItemStyle(RankingViewHolder holder) {
        holder.itemView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.blue));
        holder.username.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        holder.points.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        holder.position.setTextColor(ContextCompat.getColor(ctx, R.color.black));
        pointsIcon.setImageResource(R.drawable.star_black);
    }
}
