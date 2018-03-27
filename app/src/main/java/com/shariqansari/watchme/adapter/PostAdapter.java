package com.shariqansari.watchme.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.application.MyApplication;
import com.shariqansari.watchme.pojo.Posts;

import java.util.ArrayList;

public class PostAdapter extends AAH_VideosAdapter {

    private ArrayList<Posts> postsList = new ArrayList<>();

    public PostAdapter(ArrayList<Posts> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostVideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AAH_CustomViewHolder holder, int position) {
//        ((PostVideoHolder) holder).textViewName.setText(postsList.get(position).getVideo_name());
        holder.setVideoUrl(postsList.get(position).getPostUrl());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class PostVideoHolder extends AAH_CustomViewHolder {

        TextView textViewName;

        public PostVideoHolder(View x) {
            super(x);
        }
    }
}
