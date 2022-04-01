package com.example.csceprojectrun2;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    CommunityBuildList communityBuildList;
    List<Model> modelList;


    public CustomAdapter(CommunityBuildList communityBuildList, List<Model> modelList) {
        this.communityBuildList = communityBuildList;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.community_build_card, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will be called when user long click item
                //Creating AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(communityBuildList);
                //options to display in dialog
                String[] options = {"Update", "Delete"};
                builder.setItems(options, (dialogInterface, i1) -> {
                    if (i1 == 0) {
                        //UPDATE is clicked
                        //get data
                        String id = modelList.get(position).getId();
                        String title = modelList.get(position).getTitle();
                        String description = modelList.get(position).getDescription();

                        //intent to start activity
                        Intent intent = new Intent(communityBuildList, CommunityBuilds.class);
                        //put data in intent
                        intent.putExtra("pId", id);
                        intent.putExtra("pTitle", title);
                        intent.putExtra("pDescription", description);
                        //start activity
                        communityBuildList.startActivity(intent);
                    }
                    if (i1 == 1) {
                        //DELETE is clicked
                        communityBuildList.deleteData(position);
                    }
                }).create().show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //this will be called when user long click item
                //Creating AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(communityBuildList);
                //options to display in dialog
                String[] options = {"Update", "Delete"};
                builder.setItems(options, (dialogInterface, i2) -> {
                    if (i2 == 0) {
                        //update is clicked
                        //get data
                        String id = modelList.get(position).getId();
                        String title = modelList.get(position).getTitle();
                        String description = modelList.get(position).getDescription();

                        //intent to start activity
                        Intent intent = new Intent(communityBuildList, CommunityBuilds.class);
                        //put data in intent
                        intent.putExtra("pId", id);
                        intent.putExtra("pTitle", title);
                        intent.putExtra("pDescription", description);
                        //start activity
                        communityBuildList.startActivity(intent);
                    }
                    if (i2 == 1) {
                        //delete is clicked
                        communityBuildList.deleteData(position);
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
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
