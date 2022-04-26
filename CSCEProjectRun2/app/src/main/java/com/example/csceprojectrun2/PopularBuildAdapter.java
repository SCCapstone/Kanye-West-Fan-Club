package com.example.csceprojectrun2;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PopularBuildAdapter extends RecyclerView.Adapter<ViewHolder> {
    PopularBuildList popularBuildList;
    List<Model> modelList;

    //Popular build adapter constructor
    public PopularBuildAdapter(PopularBuildList popularBuildList, List<Model> modelList) {
        this.popularBuildList = popularBuildList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_build_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will be called when user click item
                //Creating AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(popularBuildList);
                //get data
                String id = modelList.get(position).getId();
                String title = modelList.get(position).getTitle();
                String description = modelList.get(position).getDescription();
                String accountName = modelList.get(position).getAccountName();
                String accountID = modelList.get(position).getAccountID();

                //options to display in dialog
                String[] options = {"Show", "Remove"};
                builder.setItems(options, (dialogInterface, i1) -> {
                    if (i1 == 0) {
                        //SHOW is clicked
                        //intent to start activity
                        Intent intent = new Intent(popularBuildList, PopularBuildInfo.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //put data in intent
                        intent.putExtra("pId", id);
                        intent.putExtra("pTitle", title);
                        intent.putExtra("pDescription", description);
                        intent.putExtra("pAccountName", accountName);
                        intent.putExtra("pAccountID", accountID);
                        //start activity
                        popularBuildList.startActivity(intent);
                    }
                    if (i1 == 1) {
                        //REMOVE is clicked
                        popularBuildList.removeData(position);
                    }
                }).create().show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //this will be called when user long click item
                //Creating AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(popularBuildList);
                //get data
                String id = modelList.get(position).getId();
                String title = modelList.get(position).getTitle();
                String description = modelList.get(position).getDescription();
                String accountName = modelList.get(position).getAccountName();
                String accountID = modelList.get(position).getAccountID();

                //options to display in dialog
                String[] options = {"Show", "Remove"};
                builder.setItems(options, (dialogInterface, i1) -> {
                    if (i1 == 0) {
                        //SHOW is clicked
                        //intent to start activity
                        Intent intent = new Intent(popularBuildList, PopularBuildInfo.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //put data in intent
                        intent.putExtra("pId", id);
                        intent.putExtra("pTitle", title);
                        intent.putExtra("pDescription", description);
                        intent.putExtra("pAccountName", accountName);
                        intent.putExtra("pPosition", position);
                        intent.putExtra("pAccountID", accountID);
                        //start activity
                        popularBuildList.startActivity(intent);
                    }
                    if (i1 == 1) {
                        //REMOVE is clicked
                        popularBuildList.removeData(position);
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind views / set data
        holder.mTitleTv.setText(modelList.get(position).getTitle());
        holder.mDescriptionTv.setText(modelList.get(position).getDescription());
        holder.mAccountNameTv.setText(modelList.get(position).getAccountName());
        holder.mAccountIDTv.setText(modelList.get(position).getAccountID());
    }

    @Override
    public int getItemCount() {
        //Get model list count
        return modelList.size();
    }
}
