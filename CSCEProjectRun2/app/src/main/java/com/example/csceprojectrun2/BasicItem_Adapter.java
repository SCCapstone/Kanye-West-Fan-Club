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

class BasicItem_Adapter extends RecyclerView.Adapter<BasicItem_Adapter.MyViewHolder> {
    private final BasicItem_RecyclerView basicItem_RecyclerView;

    //Class Variables
    Context context;
    ArrayList<BasicItemModel> basicItemsModels;


    //Constructor
    public BasicItem_Adapter(Context context, ArrayList<BasicItemModel> basicItemsModels,
                             BasicItem_RecyclerView basicItem_RecyclerView) {
        this.context = context;
        this.basicItemsModels = basicItemsModels;
        this.basicItem_RecyclerView = basicItem_RecyclerView;
    }

    @NonNull
    @Override
    public BasicItem_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout ( Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new BasicItem_Adapter.MyViewHolder(view, basicItem_RecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicItem_Adapter.MyViewHolder holder, int position) {
        //assign values to the views we created in the item_card layout
        holder.textViewName.setText((basicItemsModels.get(position).getBasicItemName()));
        holder.imageView.setImageResource(basicItemsModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        // gives the recycler view the number of items you want displayed
        return basicItemsModels.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // grab the views from our item_card layout file

        ImageView imageView;
        TextView textViewName;

        public MyViewHolder(@NonNull View itemView, BasicItem_RecyclerView basicItem_RecyclerView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (basicItem_RecyclerView != null) {
                        int pos = getAdapterPosition();
                        //check if position is valid
                        if(pos!=RecyclerView.NO_POSITION){
                            basicItem_RecyclerView.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
