package com.shariqansari.watchme.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.L;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    //    Android fields...
    private Toolbar toolbarSetting;
    private CircularImageView circularImageView;
    private EditText editTextName;
    private Button buttonUpdate;
    private AlertDialog alertDialog;

    //    Firebase fields...
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String userId;

    //    Instance fields...
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //   Android fields initialization...
        toolbarSetting = findViewById(R.id.appBarSetting);
        circularImageView = findViewById(R.id.imageViewProfileSetting);
        editTextName = findViewById(R.id.editTextNameSetting);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        alertDialog = new SpotsDialog(this, R.style.Custom);

        //   Setting toolbar...
        setSupportActionBar(toolbarSetting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile Setting");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //   Firebase fields initialization...
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        loadData();

//        Event listeners...
        buttonUpdate.setOnClickListener(this);
        circularImageView.setOnClickListener(this);

    }

    public void loadData() {
        alertDialog.show();
        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("userName");
                        String image = task.getResult().getString("userProfileImage");
                        editTextName.setText(name);
                        if (image != null) {
                            Picasso.get()
                                    .load(image)
                                    .placeholder(R.drawable.man)
                                    .error(R.drawable.man)
                                    .into(circularImageView);
                        }
                    }
                } else {
                    L.T(SetupActivity.this, task.getException().getMessage());
                }
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonUpdate:
                String name = editTextName.getText().toString();
                uploadData(name);
                break;
            case R.id.imageViewProfileSetting:
                imagePicker();
                break;
        }
    }

    private void uploadData(final String name) {
        if (!TextUtils.isEmpty(name) && profileImageUri != null) {
            alertDialog.show();
            storageReference.child("profile_images").child(userId + ".jpg").putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        String downloadUrl = task.getResult().getDownloadUrl().toString();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("userProfileImage", downloadUrl);
                        userMap.put("userName", name);
                        firebaseFirestore.collection("Users").document(userId)
                                .update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    L.T(SetupActivity.this, "User data updated.");
                                    startActivity(new Intent(SetupActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    L.T(SetupActivity.this, task.getException().getMessage());
                                }
                                alertDialog.dismiss();
                            }
                        });
                    } else {
                        L.T(SetupActivity.this, task.getException().getMessage());
                        alertDialog.dismiss();
                    }
                }
            });
        }
    }

    private void imagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            pickImage();
        }
    }

    public void pickImage() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                circularImageView.setImageURI(profileImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                L.T(this, error.getMessage());
            }
        }
    }
}
