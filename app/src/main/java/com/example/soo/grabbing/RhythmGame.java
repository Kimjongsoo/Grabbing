package com.example.soo.grabbing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RhythmGame extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhythm_game);


        checkDangerousPermissions();

        Button musicPlayBtn = findViewById(R.id.music_start);
        musicPlayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                playAudioFromRawResource();
//                playAudioFromExternalStorage();
            }
        });

//        Button videoPlayBtn = findViewById(R.id.videoPlayBtn);
//        videoPlayBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playVideo();
//            }
//        });
//
//        Button imageCaptureBtn = findViewById(R.id.imageCaptureBtn);
//        imageCaptureBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dispatchTakePictureIntent();
//            }
//        });
//        Button vidioRec=findViewById(R.id.videoRecBtn);
//        vidioRec.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                dispatchTakeVideoIntent();
//            }
//
//        });



    }

    final int REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA = 1;

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA);

        }
    }//외부메모리 확인되지 않을시 멈추는거


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted
            switch (requestCode) {
                case REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA:
                    playAudioFromRawResource();
                    break;
            }
        } else { // permission was denied
            Toast.makeText(getApplicationContext(),"접근 권한이 필요합니다",Toast.LENGTH_SHORT).show();
        }
    }//외부메모리에서 allow누르면 바로 노래 재생 권한설정


    private void playAudioFromRawResource(){

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + "blue_moon");
        try {
            playAudio(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }//오류 에러 캐치 구문

    }
    //raw 에 있는 gitan 을 실행하는 uri


//    private void playAudioFromExternalStorage(){
//        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Music/" +  "blue_moon.mp3");
//        try {
//            playAudio(uri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onStop() {
        super.onStop();
        killMediaPlayer();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    //비디오 출력
    private void playAudio(Uri uri) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(getApplicationContext(), uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }
}
