package com.example.csceprojectrun2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class PopularBuild_Adapter extends RecyclerView.Adapter<PopularBuild_Adapter.MyViewHolder> {
    private final PopularBuild_RecyclerView popularBuild_RecyclerView;

    //Class Variables
    Context context;
    ArrayList<PopularBuildModel> buildItemsModels;


    //Constructor
    public PopularBuild_Adapter(Context context, ArrayList<PopularBuildModel> buildItemsModels,
                             PopularBuild_RecyclerView popularBuild_RecyclerView) {
        this.context = context;
        this.buildItemsModels = buildItemsModels;
        this.popularBuild_RecyclerView = popularBuild_RecyclerView;
    }

    @NonNull
    @Override
    public PopularBuild_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout ( Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new PopularBuild_Adapter.MyViewHolder(view, popularBuild_RecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularBuild_Adapter.MyViewHolder holder, int position) {
        //assign values to the views we created in the item_card layout
        holder.textViewName.setText((buildItemsModels.get(position).getBasicItemName()));
        holder.imageView.setImageResource(buildItemsModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        // gives the recycler view the number of items you want displayed
        return buildItemsModels.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // grab the views from our item_card layout file

        ImageView imageView;
        TextView textViewName;

        public MyViewHolder(@NonNull View itemView, PopularBuild_RecyclerView popularBuild_RecyclerView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (popularBuild_RecyclerView != null) {
                        int pos = getAdapterPosition();
                        //check if position is valid
                        if (pos != RecyclerView.NO_POSITION) {
                            popularBuild_RecyclerView.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
