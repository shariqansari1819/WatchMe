package com.shariqansari.watchme.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.adapter.PostAdapter;
import com.shariqansari.watchme.extras.L;
import com.shariqansari.watchme.pojo.Posts;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //    Android fields...
    private PostAdapter postAdapter;
    private ArrayList<Posts> postsArrayList = new ArrayList<>();
    private AAH_CustomRecyclerView customRecyclerView;
    private static HomeFragment homeFragment;

    //    Firebase fields...
    private FirebaseFirestore firebaseFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        if (homeFragment == null)
            homeFragment = new HomeFragment();
        return homeFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //   Android fields initialization...
        customRecyclerView = view.findViewById(R.id.rv_home);
        customRecyclerView.setActivity(getActivity());
        customRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        customRecyclerView.setCheckForMp4(false);

        //   Firebase fields initialization...
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loadPosts();
            postAdapter = new PostAdapter(postsArrayList);
            customRecyclerView.setAdapter(postAdapter);
            customRecyclerView.smoothScrollBy(0, 1);
            customRecyclerView.smoothScrollBy(0, -1);
            customRecyclerView.setVisiblePercent(60);
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        customRecyclerView.stopVideos();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.T(getActivity(), "Home fragment destroyed.");
    }

    public void loadPosts() {
        if (postsArrayList.size() > 0)
            postsArrayList.clear();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            Posts posts = documentChange.getDocument().toObject(Posts.class);
                            postsArrayList.add(posts);
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }
}