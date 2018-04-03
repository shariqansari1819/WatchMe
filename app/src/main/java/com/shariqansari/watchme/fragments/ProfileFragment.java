package com.shariqansari.watchme.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.pojo.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //    Android fields...
    private CircleImageView circularImageView;
    private TextView textViewName, textViewUserName;
    private AlertDialog alertDialog;

    //    Firebase fields...
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String userId;

//    Instance variables...

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //   Android fields initialization...
        circularImageView = view.findViewById(R.id.imageViewProfile);
        textViewName = view.findViewById(R.id.textViewNameProfile);
        textViewUserName = view.findViewById(R.id.textViewUserNameProfile);
        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);
        alertDialog.setCancelable(false);

        //   Firebase fields initialization...
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
            loadUserData();
        }

        return view;
    }

    private void loadUserData() {
        alertDialog.show();
        firebaseFirestore.collection("Users").document(userId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                User user = documentSnapshot.toObject(User.class);
                Picasso.get().load(user.getUserProfileImage()).placeholder(R.drawable.man).error(R.drawable.man).into(circularImageView);
                textViewName.setText(user.getUserName());
                textViewUserName.setText(user.getUserProfileName());
                alertDialog.dismiss();
            }
        });
    }

}