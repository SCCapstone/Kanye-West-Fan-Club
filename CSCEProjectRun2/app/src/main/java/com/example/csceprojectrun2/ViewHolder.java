package com.example.csceprojectrun2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleTv, mDescriptionTv;
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //item click
        itemView.setOnClickListener(view -> mClickListener.onItemClick(view, getAdapterPosition()));
        //item long click listener
        itemView.setOnLongClickListener(view -> {
            mClickListener.onItemLongClick(view, getAdapterPosition());
            return false;
        });

        //Initialize views
        mTitleTv = itemView.findViewById(R.id.rTitleTv);
        mDescriptionTv = itemView.findViewById(R.id.rDescriptionTV);
    }

    private ViewHolder.ClickListener mClickListener;

    //interface for click listener
    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
