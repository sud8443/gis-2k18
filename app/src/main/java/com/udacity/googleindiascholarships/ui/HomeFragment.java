package com.udacity.googleindiascholarships.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.googleindiascholarships.R;
import com.udacity.googleindiascholarships.ui.adapters.AnnouncementsRecyclerViewAdapter;
import com.udacity.googleindiascholarships.ui.adapters.BlogsRecyclerViewAdapter;

/**
 * Created by Sudhanshu on 12-05-2018.
 */

public class HomeFragment extends Fragment {

    AnnouncementsRecyclerViewAdapter announcementsRecyclerViewAdapter;
    BlogsRecyclerViewAdapter blogsRecyclerViewAdapter;
    RecyclerView blogsRecyclerView, announcementsRecyclerView;

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Fetching the Recycler views from the layout file
        blogsRecyclerView = (RecyclerView) v.findViewById(R.id.rv_blogs);
        announcementsRecyclerView = (RecyclerView) v.findViewById(R.id.rv_announcements);

        blogsRecyclerViewAdapter = new BlogsRecyclerViewAdapter(getContext());
        announcementsRecyclerViewAdapter = new AnnouncementsRecyclerViewAdapter(getContext());

        LinearLayoutManager blogsHorizontalLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager announcementsHorizontalLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        blogsRecyclerView.setLayoutManager(blogsHorizontalLinearLayoutManager);
        blogsRecyclerView.setAdapter(blogsRecyclerViewAdapter);

        announcementsRecyclerView.setLayoutManager(announcementsHorizontalLinearLayoutManager);
        announcementsRecyclerView.setAdapter(announcementsRecyclerViewAdapter);
        return v;
    }
}
