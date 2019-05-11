package com.example.soo.grabbing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.BitmapFactory.decodeResource;

public class RhythmGame extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private int greenX,greenY, greenSpeed=20;
    private Paint greenPaint =new Paint();
    private int fishX=10;
    private int fishY;
    private Handler handler=new Handler();
    private final  static long Interval = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewEx viewEx = new ViewEx(this);
        setContentView(viewEx);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        viewEx.invalidate();
                    }
                });
            }
        },0,Interval);
//        setContentView(R.layout.activity_rhythm_game);


        checkDangerousPermissions();

//        Button musicPlayBtn = findViewById(R.id.music_start);
//        musicPlayBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                playAudioFromRawResource();
////                playAudioFromExternalStorage();
//            }
//        });


        mMediaPlayer= MediaPlayer.create(this,R.raw.blue_moon);
        mMediaPlayer.setVolume(0.8f,0.8f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

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
class ViewEx extends View
{
    private int canvasWidth, canvasHeight;
    private int greenX,greenY, greenSpeed=15;
    private Paint greenPaint =new Paint();
    private int fishX=10;
    private int fishY;
    Bitmap icon=BitmapFactory.decodeResource(getResources(),R.drawable.sw);
    private Bitmap life[] = new Bitmap[2];
    public ViewEx(Context context)
    {
        super(context);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);


    }
    protected void onDraw(Canvas canvas) {
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        super.onDraw(canvas);
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();
        greenX = 200;
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        greenY=greenY +greenSpeed;
        if(greenY >canvasHeight+30)
        {

            greenY=0;
//            greenY=(int)Math.floor(Math.random() *(maxFishY -minFishY))+minFishY;

        }
        canvas.drawBitmap(icon,greenX,greenY,greenPaint);


    }




}


