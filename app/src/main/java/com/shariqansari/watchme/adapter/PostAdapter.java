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

    private ArrayList<Posts> postsArrayList = new ArrayList<>();

    public PostAdapter(ArrayList<Posts> postsArrayList) {
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(AAH_CustomViewHolder holder, int position) {
        String videoUrl = postsArrayList.get(position).getPostUrl();
//        ((PostHolder) holder).textViewName.setText(post.get(position).getVideo_name());
        holder.setVideoUrl(MyApplication.getProxy().getProxyUrl(videoUrl + ""));
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class PostHolder extends AAH_CustomViewHolder {

        public PostHolder(View x) {
            super(x);
        }
    }
}
