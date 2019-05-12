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
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;
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
//    DisplayMetrics dm;
//    public int dwidth;
//    public int dheight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewEx viewEx = new ViewEx(this);
        setContentView(viewEx);
//        dm = getApplicationContext().getResources().getDisplayMetrics();
//        dwidth = dm.widthPixels;
//        dheight = dm.heightPixels;
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
    private Paint condition=new Paint();
    private Paint rnqns=new Paint();
    private Paint scorePaint=new Paint();
    private int fishX=10;
    private int fishY;
    Bitmap icon=BitmapFactory.decodeResource(getResources(),R.drawable.sw);
    Bitmap twodu=BitmapFactory.decodeResource(getResources(),R.drawable.twodu);
    Bitmap thrdu=BitmapFactory.decodeResource(getResources(),R.drawable.thrdu);
    Bitmap rktma=BitmapFactory.decodeResource(getResources(),R.drawable.rktma);
    Bitmap memory;

    private Bitmap life[] = new Bitmap[2];
    private int score, lifeCounter,scorecheck;
    private boolean touch=false;
//    private RhythmGame rhythmGame;
    private boolean once=true;
    int checkcheck =5;
    int touchx,touchy;


    public ViewEx(Context context)
    {
        super(context);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        condition.setColor(Color.RED);
        condition.setAntiAlias(false);

        rnqns.setColor(Color.YELLOW);
        rnqns.setAntiAlias(false);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);
        score=0;
        scorecheck=0;

        lifeCounter =3;




        Random ran=new Random();
        int rnum=ran.nextInt(3);
        

        if(rnum==0) {
            greenX = 115;
            memory=twodu;
            checkcheck=0;
        }
        else if(rnum==1) {
            greenX = 475;
            memory=thrdu;
            checkcheck=1;
        }
        else if(rnum==2) {
            greenX = 835;
            memory=rktma;
            checkcheck=2;
        }


    }
    protected void onDraw(Canvas canvas) {
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        super.onDraw(canvas);
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();

//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        greenY=greenY +greenSpeed;
        if(greenY >canvasHeight+30)
        {
            if(scorecheck!=score-10) {
                lifeCounter--;

            }
            else  scorecheck += 10;

            greenY=0;
//
//            int random=(int)Math.floor(Math.random() *(canvasWidth));
//            int random3_1=(int)((Math.random() *(canvasWidth))+1)/3;
//            int random3_2=(int)(Math.floor(Math.random() *(canvasWidth)))*2/3;

            Random ran=new Random();
            int rnum=ran.nextInt(3);

            if(rnum==0) {
                greenX = (int) canvasWidth / 6 - icon.getWidth() / 2;
                memory=twodu;
                checkcheck=0;
            }
            else if(rnum==1) {
                greenX = (int) canvasWidth / 2 - icon.getWidth() / 2;
                memory=thrdu;
                checkcheck=1;
            }
            else if(rnum==2) {
                greenX = (int) canvasWidth * 5 / 6 - icon.getWidth() / 2;
                memory=rktma;
                checkcheck=2;
            }
            Log.e("check",(String.valueOf((int)canvasWidth/6-icon.getWidth()/2))+"."+(String.valueOf((int)canvasWidth/2-icon.getWidth()/2))+"."+(String.valueOf((int)canvasWidth*5/6-icon.getWidth()/2)));

//            condition.setAlpha(255);
            once=true;
        }
        canvas.drawBitmap(memory,greenX,greenY,greenPaint);
        canvas.drawLine(50,canvasHeight-300,canvasWidth-50,canvasHeight-300,greenPaint);
        canvas.drawLine(50,canvasHeight-600,canvasWidth-50,canvasHeight-600,greenPaint);

        canvas.drawLine((int)canvasWidth/3,0,(int)canvasWidth/3,canvasHeight,rnqns);
        canvas.drawLine((int)canvasWidth*2/3,0,(int)canvasWidth*2/3,canvasHeight,rnqns);



        if (touch)
        {
//            Log.e("check1",String.valueOf(condition.getAlpha()));
            if(once==true) {
                condition.setColor(Color.RED);
                canvas.drawCircle(50, canvasHeight - 650, 55, condition);
//
//                if (hitrhythmChecker(greenX, greenY)) {
//                    condition.setColor(Color.BLACK);
//                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
//                    canvas.drawText("Good",50,canvasHeight - 650,condition);
//                    condition.setTextSize(200);
//                    score = score + 10;
////                    condition.setAlpha(50);
////                    Log.e("check2",String.valueOf(condition.getAlpha()));
//                    once=false;
//                }
                if (checkcheck==0&&hitrhythmChecker(greenX,greenY)&&hitrhythmChecker_2du(touchx,touchy)){
                    condition.setColor(Color.BLACK);
                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
                    canvas.drawText("Good",50,canvasHeight - 650,condition);
                    condition.setTextSize(200);
                    score = score + 10;
//                    condition.setAlpha(50);
//                    Log.e("check2",String.valueOf(condition.getAlpha()));
                    once=false;
                }
                if (checkcheck==1&&hitrhythmChecker(greenX,greenY)&&hitrhythmChecker_3du(touchx,touchy)){
                    condition.setColor(Color.BLACK);
                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
                    canvas.drawText("Good",50,canvasHeight - 650,condition);
                    condition.setTextSize(200);
                    score = score + 10;
//                    condition.setAlpha(50);
//                    Log.e("check2",String.valueOf(condition.getAlpha()));
                    once=false;
                }
                if (checkcheck==2&&hitrhythmChecker(greenX,greenY)&&hitrhythmChecker_rktma(touchx,touchy)){
                    condition.setColor(Color.BLACK);
                    canvas.drawCircle(50, canvasHeight - 650, 57, condition);
                    canvas.drawText("Good",50,canvasHeight - 650,condition);
                    condition.setTextSize(200);
                    score = score + 10;
//                    condition.setAlpha(50);
//                    Log.e("check2",String.valueOf(condition.getAlpha()));
                    once=false;
                }

            }
            touch=false;
        }
        else {
            condition.setColor(Color.RED);
            canvas.drawCircle(50,canvasHeight-650,50,condition);
            touch =false;
        }

        for(int i=0; i<3; i++)
        {
            int x= (int) (580+life[0].getWidth() *1.5 *i);
            int y= 30;

            if(i<lifeCounter)
            {
                canvas.drawBitmap(life[0], x,y,null);

            }
            else {
                canvas.drawBitmap(life[1], x,y,null);
            }
        }
        canvas.drawText("Score : "+score, 20, 60, scorePaint);
    }


    public boolean hitrhythmChecker(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300){
            return true;
        }
        return false;
    }
    public boolean hitrhythmChecker_2du(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300 && x<(int)canvasWidth/3){
            return true;
        }
        return false;
    }
    public boolean hitrhythmChecker_3du(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300 && (int)canvasWidth/3<=x&& x<(int)canvasWidth*2/3){
            return true;
        }
        return false;
    }
    public boolean hitrhythmChecker_rktma(int x,int y)
    {
        if(canvasHeight-600<y && y<canvasHeight-300 && x>(int)canvasWidth*2/3){
            return true;
        }
        return false;
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            touchx=(int)event.getX();
            touchy=(int)event.getY();
            touch =true;

        }
        return true;
    }


}


