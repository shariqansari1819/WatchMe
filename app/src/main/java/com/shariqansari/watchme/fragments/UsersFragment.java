package com.shariqansari.watchme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.adapter.UserAdapter;
import com.shariqansari.watchme.extras.L;
import com.shariqansari.watchme.pojo.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment implements UserAdapter.OnUserClick {

    private ArrayList<User> userArrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private static UsersFragment usersFragment;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        userAdapter = new UserAdapter(getActivity(), userArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userAdapter);
        loadData();

        userAdapter.setOnUserClick(this);

        return view;
    }

    public void loadData() {
        if (userArrayList.size() > 0)
            userArrayList.clear();
        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        User user = documentChange.getDocument().toObject(User.class);
                        userArrayList.add(user);
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public static UsersFragment newInstance() {
        if (usersFragment == null)
            usersFragment = new UsersFragment();
        return usersFragment;
    }

    @Override
    public void onUserClick(View view, int position, String id) {
        L.T(getActivity(), id);
    }
}
