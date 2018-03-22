package com.shariqansari.watchme.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.emrekose.recordbutton.OnRecordListener;
import com.emrekose.recordbutton.RecordButton;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.L;

import java.io.File;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity implements OnRecordListener {

    //    Android fields...
    private CameraView cameraView;
    private RecordButton recordButton;

    //    Instance variables...
    private int recordCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //   Full screen...
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //   Android fields initialization...
        cameraView = findViewById(R.id.cameraView);
        recordButton = findViewById(R.id.recordBtn);


        //   Setting camera...
        cameraView.setVideoMaxDuration(15000);

        //   Event listeners...
        recordButton.setRecordListener(this);
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                super.onVideoTaken(video);
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
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
        if (recordCounter >= 1) {
            Log.e("TAG", "onRecord");
            File internalDir = getDir("myvideos", Context.MODE_PRIVATE); //Creating an internal dir;
            File fileWithinMyDir = new File(internalDir, UUID.randomUUID().toString() + ".mp4");
            cameraView.startCapturingVideo(fileWithinMyDir);
        }
    }

    @Override
    public void onRecordCancel() {
        Log.e("TAG", "onRecordCancel");
    }

    @Override
    public void onRecordFinish() {
        Log.e("TAG", "onRecordFinish");
    }
}
