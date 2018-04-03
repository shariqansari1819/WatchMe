package com.shariqansari.watchme.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.application.MyApplication;
import com.shariqansari.watchme.pojo.Posts;
import com.shariqansari.watchme.pojo.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    public void onBindViewHolder(final AAH_CustomViewHolder holder, final int position) {
        String id = postsList.get(position).getUserId();
        FirebaseFirestore.getInstance().collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    String profileImage = user.getUserProfileImage();
                    String name = user.getUserName();
                    ((PostVideoHolder) holder).textViewName.setText(name);
                    Picasso.get()
                            .load(profileImage)
                            .into(((PostVideoHolder) holder).circularImageView);
                } else {

                }
            }
        });
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
        CircleImageView circularImageView;

        public PostVideoHolder(View x) {
            super(x);
            textViewName = x.findViewById(R.id.textViewUserNamePost);
            circularImageView = x.findViewById(R.id.imageViewProfilePost);
        }
    }
}
