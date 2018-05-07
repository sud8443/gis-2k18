package com.udacity.googleindiascholarships.members.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.googleindiascholarships.R;

/**
 * Created by Sudhanshu on 07-05-2018.
 */

public class EditProfileProjectsAdapter extends RecyclerView.Adapter<EditProfileProjectsAdapter.EditProfileViewHolder> {

    public static final int VIEW_TYPE_PROJECT = 0;
    public static final int VIEW_TYPE_ADD_PROJECT = 1;
    public static int mListSize = 10;

    @Override
    public EditProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        int layoutId = R.layout.list_item_profile_projects;
        switch (viewType){
            case VIEW_TYPE_PROJECT:
                layoutId = R.layout.list_item_profile_projects;
                break;
            case VIEW_TYPE_ADD_PROJECT:
                layoutId = R.layout.list_item_add_profile_projects;
                break;
        }
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId ,parent ,false);
        return new EditProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EditProfileViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListSize;
    }

    public class EditProfileViewHolder extends RecyclerView.ViewHolder {

        public EditProfileViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mListSize - 1){
            return VIEW_TYPE_ADD_PROJECT;
        }else {
            return VIEW_TYPE_PROJECT;
        }
    }
}
