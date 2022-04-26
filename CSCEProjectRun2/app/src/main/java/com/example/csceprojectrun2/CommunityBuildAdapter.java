package com.example.csceprojectrun2;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommunityBuildAdapter extends RecyclerView.Adapter<ViewHolder> {
    CommunityBuildList communityBuildList;
    List<Model> modelList;

    //Community build adapter constructor
    public CommunityBuildAdapter(CommunityBuildList communityBuildList, List<Model> modelList) {
        this.communityBuildList = communityBuildList;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(communityBuildList);
                //get data
                String id = modelList.get(position).getId();
                String title = modelList.get(position).getTitle();
                String description = modelList.get(position).getDescription();
                String accountName = modelList.get(position).getAccountName();
                String accountID = modelList.get(position).getAccountID();
                String currentUser = communityBuildList.userId;

                System.out.print(currentUser);
                if (accountID.equals(currentUser)) {
                    //options to display in dialog
                    String[] options = {"Show", "Update", "Delete"};
                    builder.setItems(options, (dialogInterface, i1) -> {
                        //SHOW is clicked
                        if (i1 == 0) {
                            //intent to start activity
                            Intent intent = new Intent(communityBuildList, CommunityBuildInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            intent.putExtra("pAccountName", accountName);
                            intent.putExtra("pAccountID", accountID);
                            //start activity
                            communityBuildList.startActivity(intent);
                        }
                        if (i1 == 1) {
                            //UPDATE is clicked
                            //intent to start activity
                            Intent intent = new Intent(communityBuildList, CommunityBuilds.class);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            //start activity
                            communityBuildList.startActivity(intent);
                        }
                        if (i1 == 2) {
                            //DELETE is clicked
                            communityBuildList.deleteData(position);
                        }
                    }).create().show();
                } else {
                    //options to display in dialog
                    String[] options = {"Show"};
                    builder.setItems(options, (dialogInterface, i) -> {
                        //SHOW is clicked
                        if (i == 0) {
                            //SHOW is clicked
                            Intent intent = new Intent(communityBuildList, CommunityBuildInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            intent.putExtra("pAccountName", accountName);
                            intent.putExtra("pAccountID", accountID);
                            //start activity
                            communityBuildList.startActivity(intent);
                        }
                    }).create().show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //this will be called when user long click item
                //Creating AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(communityBuildList);
                //get data
                String id = modelList.get(position).getId();
                String title = modelList.get(position).getTitle();
                String description = modelList.get(position).getDescription();
                String accountName = modelList.get(position).getAccountName();
                String accountID = modelList.get(position).getAccountID();
                String currentUser = communityBuildList.userId;

                System.out.print(currentUser);
                if (accountID.equals(currentUser)) {
                    //options to display in dialog
                    String[] options = {"Show", "Update", "Delete"};
                    builder.setItems(options, (dialogInterface, i1) -> {
                        if (i1 == 0) {
                            //SHOW is clicked
                            //intent to start activity
                            Intent intent = new Intent(communityBuildList, CommunityBuildInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            intent.putExtra("pAccountName", accountName);
                            intent.putExtra("pPosition", position);
                            intent.putExtra("pAccountID", accountID);
                            //start activity
                            communityBuildList.startActivity(intent);
                        }
                        if (i1 == 1) {
                            //UPDATE is clicked
                            //intent to start activity
                            Intent intent = new Intent(communityBuildList, CommunityBuilds.class);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            //start activity
                            communityBuildList.startActivity(intent);
                        }
                        if (i1 == 2) {
                            //DELETE is clicked
                            communityBuildList.deleteData(position);
                        }
                    }).create().show();
                } else {
                    //options to display in dialog
                    String[] options = {"Show"};
                    builder.setItems(options, (dialogInterface, i) -> {
                        if (i == 0) {
                            //SHOW is clicked
                            //intent to start activity
                            Intent intent = new Intent(communityBuildList, CommunityBuildInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", description);
                            intent.putExtra("pAccountName", accountName);
                            intent.putExtra("pPosition", position);
                            intent.putExtra("pAccountID", accountID);
                            //start activity
                            communityBuildList.startActivity(intent);
                        }
                    }).create().show();
                }
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
        return modelList.size();
    }
}
