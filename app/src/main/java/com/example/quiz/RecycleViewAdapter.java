package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A recycleViewAdapter implementálása
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mScores = new ArrayList<>();

    /**
     * Konstruktor
     *
     * @param mNames
     * @param mScores
     * @param mContext
     */
    public RecycleViewAdapter(ArrayList<String> mNames, ArrayList<String> mScores, Context mContext) {
        this.mNames = mNames;
        this.mScores = mScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mNames.get(position));
        holder.score.setText(mScores.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView score;
        final RelativeLayout recycleParent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.leader_name);
            score = itemView.findViewById(R.id.score);
            recycleParent = itemView.findViewById(R.id.recycle_parent);
        }
    }
}
