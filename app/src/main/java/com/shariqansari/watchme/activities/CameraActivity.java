package com.shariqansari.watchme.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.emrekose.recordbutton.OnRecordListener;
import com.emrekose.recordbutton.RecordButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.L;
import com.shariqansari.watchme.pojo.Posts;

import java.io.File;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class CameraActivity extends AppCompatActivity implements OnRecordListener {

    //    Android fields...
    private CameraView cameraView;
    private RecordButton recordButton;
    private android.app.AlertDialog alertDialog;

    //    Firebase fields...
    private StorageReference storageReference;
    private StorageMetadata storageMetadata;
    private UploadTask uploadTask;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    //    Instance variables...
    private int recordCounter = 0;
    private Uri videoFile;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //   Full screen...
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //   Android fields initialization...
        cameraView = findViewById(R.id.cameraView);
        recordButton = findViewById(R.id.recordBtn);
        alertDialog = new SpotsDialog(this, R.style.Custom);

        //   Firebase fields initialization...
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //   Setting camera...
        cameraView.setVideoMaxDuration(15000);

        //   Event listeners...
        recordButton.setRecordListener(this);
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(final File video) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(CameraActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CameraActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("Upload Video");
                arrayAdapter.add("Watch Video");
                arrayAdapter.add("Record Again");

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                videoFile = Uri.fromFile(new File(video.getAbsolutePath()));
                                uploadVideo(videoFile);
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                        }
                    }
                });
                builderSingle.show();
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                L.T(CameraActivity.this, exception.getMessage());
            }
        });

    }

    public void uploadVideo(Uri videoFile) {
        alertDialog.show();
        alertDialog.setCancelable(false);

        storageMetadata = new StorageMetadata.Builder()
                .setContentType("video/mp4")
                .build();
        uploadTask = storageReference.child("post_videos/" + videoFile.getLastPathSegment()).putFile(videoFile, storageMetadata);
        uploadTask.addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.e("TAG", "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(CameraActivity.this, new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("TAG", "Upload paused.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                alertDialog.dismiss();
            }
        }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                saveDataInFirebase(taskSnapshot);
            }
        });
    }

    private void saveDataInFirebase(UploadTask.TaskSnapshot taskSnapshot) {
        Uri downloadVideoUri = taskSnapshot.getDownloadUrl();
        String videoId = UUID.randomUUID().toString();
        final String lastName = downloadVideoUri.getLastPathSegment();
        String videoName = lastName.substring(0, lastName.indexOf("."));
        String videoUrl = downloadVideoUri.toString();
        String userId = firebaseAuth.getCurrentUser().getUid();
        Posts posts = new Posts(videoId, videoName, videoUrl, userId);
        firebaseFirestore.collection("Posts").document(videoId).set(posts).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    File internalDir = getDir("myvideos", Context.MODE_PRIVATE); //Creating an internal dir;
                    File file = new File(internalDir, fileName);
                    if (file.delete())
                        L.T(CameraActivity.this, "Deleted");
                    else
                        L.T(CameraActivity.this, "Not deleted");
                    startActivity(new Intent(CameraActivity.this, HomeActivity.class));
                    finish();
                    L.T(CameraActivity.this, "Video uploaded successfully.");
                } else {
                    if (task.getException() != null) {
                        L.T(CameraActivity.this, task.getException().getMessage());
                    }
                }
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }

    @Override
    public void onRecord() {
        recordCounter++;
        if (recordCounter == 1) {
            Log.e("TAG", "onRecord");
            File internalDir = getDir("myvideos", Context.MODE_PRIVATE); //Creating an internal dir;
            fileName = UUID.randomUUID().toString() + ".mp4";
            File fileWithinMyDir = new File(internalDir, fileName);
            cameraView.startCapturingVideo(fileWithinMyDir);
        }
    }

    @Override
    public void onRecordCancel() {
        if (cameraView.isCapturingVideo())
            cameraView.stopCapturingVideo();
        Log.e("TAG", "onRecordCancel");
    }

    @Override
    public void onRecordFinish() {
        if (cameraView.isCapturingVideo())
            cameraView.stopCapturingVideo();
        Log.e("TAG", "onRecordFinish");
    }
}
